package es.pharmashop.batch;

import es.pharmashop.domain.Article;
import es.pharmashop.persistence.sqlite.entity.Product;
import es.pharmashop.persistence.sqlite.repository.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.batch.item.ItemProcessor;

@Data
@Builder
@AllArgsConstructor
public class ArticleProcessor implements ItemProcessor<Article, Article> {
    private Float factor;
    private Float margin;
    private ProductRepository productRepository;

    @Override
    public Article process(Article article) throws Exception {
        Product product = productRepository.findByCnAndEan(article.getId(), article.getEan());
        if(product == null) {
            product = productRepository.save(Product.builder()
              .cn(article.getId())
              .ean(article.getEan())
              .name(article.getDescription())
              .revision(false)
              .uniqueCode(null)
              .build());
        }

        article.fixStock();
        article.applyFactorToStock(factor);
        article.applyMarginToPrice(margin);
        article.setUniqueCode(product.getUniqueCode());

        return article;
    }
}
