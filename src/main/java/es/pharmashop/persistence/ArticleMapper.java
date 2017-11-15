package es.pharmashop.persistence;

import es.pharmashop.domain.Article;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ArticleMapper implements RowMapper<Article> {
    @Override
    public Article mapRow(ResultSet resultSet, int i) throws SQLException {
        Article article = Article.builder()
                .id(resultSet.getString("IdArticu"))
                .description(resultSet.getString("Descripcion"))
                .price(resultSet.getBigDecimal("Pvp"))
                .stock(resultSet.getInt("StockActual"))
                .build();

        return article;
    }
}