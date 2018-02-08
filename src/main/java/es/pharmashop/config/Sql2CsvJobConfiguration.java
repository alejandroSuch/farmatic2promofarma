package es.pharmashop.config;

import es.pharmashop.batch.ArticleProcessor;
import es.pharmashop.batch.StringHeaderWriter;
import es.pharmashop.domain.Article;
import es.pharmashop.persistence.ArticleMapper;
import lombok.extern.java.Log;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.FieldExtractor;
import org.springframework.batch.item.file.transform.LineAggregator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;

@Log
@Configuration
public class Sql2CsvJobConfiguration {
    private static final String TMP_FILE_PREFIX = "farmatic2promofarma";
    private static final String TMP_FILE_SUFFIX = ".csv";

    private final static String FIND_ARTICLES_QUERY =
            "SELECT a.IdArticu, s.Sinonimo, a.Descripcion, a.Pvp, a.Puc, ti.Piva, a.StockActual, a.StockMinimo, a.StockMaximo, a.LoteOptimo, a.FechaUltimaEntrada, a.FechaUltimaSalida, a.FechaCaducidad\n" +
            "FROM Articu a\n" +
            "  LEFT JOIN ItemListaArticu i on a.IdArticu = i.XItem_IdArticu\n" +
            "  LEFT JOIN ListaArticu l on i.XItem_IdLista = l.IdLista\n" +
            "  LEFT JOIN Sinonimo s ON s.IdArticu = a.IdArticu \n" +
            "  LEFT JOIN TablaIva ti ON ti.IdTipoArt = a.XGrup_IdGrupoIva AND ti.IdTipoPro = '05'\n" +
            "WHERE l.Descripcion = 'PROMOFARMA'\n" +
            "  AND s.IdAplicacion = '00000' \n" +
            "  AND LEN(TRIM(s.Sinonimo)) = 13";

    @Bean
    ItemReader<Article> databaseItemReader(DataSource dataSource) {
        JdbcCursorItemReader<Article> itemReader = new JdbcCursorItemReader<>();

        itemReader.setDataSource(dataSource);
        itemReader.setSql(FIND_ARTICLES_QUERY);
        itemReader.setRowMapper(new ArticleMapper());

        return itemReader;
    }

    @Bean
    public ItemProcessor articleItemProcessor(
            @Value("${farmatic2csv.stock.factor:1}") Float factor,
            @Value("${farmatic2csv.price.margin}") Float margin

    ) {
        return ArticleProcessor.builder()
                .factor(factor)
                .margin(margin)
                .build();
    }

    @Bean
    public ItemWriter<Article> articleItemWriter(
            @Value("${farmatic2csv.out.file:#{null}}") String outputFileName,
            @Value("${farmatic2csv.use.price:#{false}}") Boolean usePrice
    ) {
        FlatFileItemWriter<Article> itemWriter = new FlatFileItemWriter<>();

        String exportFileHeader = usePrice ? "national_code;ean;title;price;taxes;stock" : "national_code;ean;title;stock";
        StringHeaderWriter headerWriter = new StringHeaderWriter(exportFileHeader);
        itemWriter.setHeaderCallback(headerWriter);

        String exportFilePath = outputFileName == null ? getOutputFileName() : outputFileName;
        log.info("Output file is: " + exportFilePath);

        itemWriter.setResource(new FileSystemResource(exportFilePath));

        LineAggregator<Article> lineAggregator = createArticleLineAggregator(usePrice);
        itemWriter.setLineAggregator(lineAggregator);

        return itemWriter;
    }

    private String getOutputFileName() {
        String outFile = null;

        try {
            File tempFile = File.createTempFile(TMP_FILE_PREFIX, TMP_FILE_SUFFIX);
            outFile = tempFile.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return outFile;
    }

    private LineAggregator<Article> createArticleLineAggregator(Boolean usePrice) {
        DelimitedLineAggregator<Article> lineAggregator = new DelimitedLineAggregator<>();
        lineAggregator.setDelimiter(";");

        FieldExtractor<Article> fieldExtractor = createArticleFieldExtractor(usePrice);
        lineAggregator.setFieldExtractor(fieldExtractor);

        return lineAggregator;
    }

    private FieldExtractor<Article> createArticleFieldExtractor(Boolean usePrice) {
        BeanWrapperFieldExtractor<Article> extractor = new BeanWrapperFieldExtractor<>();
        extractor.setNames(
                usePrice ?
                        new String[]{"id", "ean", "description", "price", "taxes", "stock"} :
                        new String[]{"id", "ean", "description", "stock"}
        );
        return extractor;
    }

    @Bean
    public Step importArticles(
            StepBuilderFactory steps,
            ItemReader databaseItemReader,
            ItemProcessor articleItemProcessor,
            ItemWriter articleItemWriter
    ) {
        return steps.get("importArticlesStep")
                .chunk(100)
                .reader(databaseItemReader)
                .processor(articleItemProcessor)
                .writer(articleItemWriter)
                .build();
    }

    @Bean
    public Job farmatic2csv(
            JobBuilderFactory jobBuilderFactory,
            Step importArticles
    ) {
        return jobBuilderFactory.get("farmatic2csv")
                .incrementer(new RunIdIncrementer())
                .start(importArticles)
                .build();
    }
}
