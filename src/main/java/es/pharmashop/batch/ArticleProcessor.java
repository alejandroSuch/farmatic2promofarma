package es.pharmashop.batch;

import es.pharmashop.domain.Article;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.batch.item.ItemProcessor;

@Data
@Builder
@AllArgsConstructor
public class ArticleProcessor implements ItemProcessor<Article, Article> {
    private Float factor;

    @Override
    public Article process(Article article) throws Exception {
        article.fixStock();
        article.applyFactorToStock(factor);
        return article;
    }
}
