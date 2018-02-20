package es.pharmashop.persistence;

import es.pharmashop.domain.Article;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ArticleMapper implements RowMapper<Article> {

    public static final String CN_COLUMN = "IdArticu";
    public static final String EAN_COLUMN = "Sinonimo";
    public static final String DESCRIPTION_COLUMN = "Descripcion";
    public static final String PUC_COLUMN = "Puc";
    public static final String TAXES_COLUMN = "Piva";
    public static final String STOCK_COLUMN = "StockActual";

    @Override
    public Article mapRow(ResultSet resultSet, int i) throws SQLException {
        Article article = Article.builder()
                .id(resultSet.getString(CN_COLUMN))
                .ean(resultSet.getString(EAN_COLUMN))
                .description(resultSet.getString(DESCRIPTION_COLUMN))
                .price(resultSet.getBigDecimal(PUC_COLUMN))
                .taxes(resultSet.getInt(TAXES_COLUMN))
                .stock(resultSet.getInt(STOCK_COLUMN))
                .build();

        return article;
    }
}