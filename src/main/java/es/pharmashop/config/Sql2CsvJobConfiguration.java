package es.pharmashop.config;

import es.pharmashop.batch.ArticleProcessor;
import es.pharmashop.batch.StringHeaderWriter;
import es.pharmashop.domain.Article;
import es.pharmashop.persistence.ArticleMapper;
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

@Configuration
public class Sql2CsvJobConfiguration {
    private final static String FIND_ARTICLES_QUERY = "SELECT a.IdArticu, a.Descripcion, a.Pvp, a.StockActual, a.StockMinimo, a.StockMaximo, a.LoteOptimo, a.FechaUltimaEntrada, a.FechaUltimaSalida, a.FechaCaducidad\n" +
            "FROM Articu a\n" +
            "LEFT JOIN ItemListaArticu i on a.IdArticu = i.XItem_IdArticu\n" +
            "LEFT JOIN ListaArticu l on i.XItem_IdLista = l.IdLista\n" +
            "LEFT JOIN GrupoIva g ON a.XGrup_IdGrupoIva = g.IdGrupoIva\n" +
            "WHERE l.Descripcion = 'PROMOFARMA'";

    @Bean
    ItemReader<Article> databaseItemReader(DataSource dataSource) {
        JdbcCursorItemReader<Article> itemReader = new JdbcCursorItemReader<>();

        itemReader.setDataSource(dataSource);
        itemReader.setSql(FIND_ARTICLES_QUERY);
        itemReader.setRowMapper(new ArticleMapper());

        return itemReader;
    }

    @Bean
    public ItemProcessor articleItemProcessor(@Value("${farmatic.stock.factor:1}") Float factor) {
        return ArticleProcessor.builder()
                .factor(factor)
                .build();
    }

    @Bean
    public ItemWriter<Article> articleItemWriter() {
        FlatFileItemWriter<Article> itemWriter = new FlatFileItemWriter<>();

        String exportFileHeader = "ID;DESCRIPTION;PRICE;STOCK";
        StringHeaderWriter headerWriter = new StringHeaderWriter(exportFileHeader);
        itemWriter.setHeaderCallback(headerWriter);

        String exportFilePath = "/tmp/articles.csv";
        itemWriter.setResource(new FileSystemResource(exportFilePath));

        LineAggregator<Article> lineAggregator = createArticleLineAggregator();
        itemWriter.setLineAggregator(lineAggregator);

        return itemWriter;
    }

    private LineAggregator<Article> createArticleLineAggregator() {
        DelimitedLineAggregator<Article> lineAggregator = new DelimitedLineAggregator<>();
        lineAggregator.setDelimiter(";");

        FieldExtractor<Article> fieldExtractor = createArticleFieldExtractor();
        lineAggregator.setFieldExtractor(fieldExtractor);

        return lineAggregator;
    }

    private FieldExtractor<Article> createArticleFieldExtractor() {
        BeanWrapperFieldExtractor<Article> extractor = new BeanWrapperFieldExtractor<>();
        extractor.setNames(new String[]{"id", "description", "price", "stock"});
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
