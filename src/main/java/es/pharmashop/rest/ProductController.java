package es.pharmashop.rest;

import es.pharmashop.persistence.sqlite.entity.Product;
import es.pharmashop.persistence.sqlite.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@AllArgsConstructor
@RequestMapping("products")
public class ProductController {
  private ProductRepository productRepository;

  @GetMapping
  public Iterable<Product> findAll() {
    return productRepository.findAll();
  }

  @PostMapping
  public void update(@RequestBody Product product) {
    Product one = this.productRepository.findByCnAndEan(product.getCn(), product.getEan());

    product.setName(one.getName());
    product.setEan(one.getEan());
    product.setCn(one.getCn());

    this.productRepository.save(product);
  }
}
