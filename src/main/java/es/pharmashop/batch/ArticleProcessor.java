package es.pharmashop.batch;

import es.pharmashop.domain.Article;
import es.pharmashop.persistence.sqlite.entity.Catalog;
import es.pharmashop.persistence.sqlite.entity.Product;
import es.pharmashop.persistence.sqlite.repository.CatalogRepository;
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
  private CatalogRepository catalogRepository;

  @Override
  public Article process(Article article) throws Exception {
    Product product = processProduct(article);
    String uniqueCode = product == null ? null : product.getUniqueCode();

    article.fixStock();
    article.applyFactorToStock(factor);
    article.applyMarginToPrice(margin);
    article.setUniqueCode(uniqueCode);

    return article;
  }

  private Product processProduct(Article article) {
    Product persistedProduct = productRepository.findOne(article.getEan());

    if (persistedProduct == null) {
      Catalog byEan = catalogRepository.findByEan(article.getEan());

      if (byEan != null) {
        persistedProduct = productRepository.save(Product.builder()
          .cn(article.getId())
          .ean(article.getEan())
          .name(article.getDescription())
          .revision(false)
          .uniqueCode(byEan.getIdPromofarma())
          .build());
      }
    }

    return persistedProduct;
  }
}
