package es.pharmashop.rest;

import es.pharmashop.persistence.sqlite.entity.Product;
import es.pharmashop.persistence.sqlite.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("products")
public class ProductController {
  private ProductRepository productRepository;

  @GetMapping
  public Page<Product> findAll(Pageable pageRequest) {
    return productRepository.findAll(pageRequest);
  }
}
