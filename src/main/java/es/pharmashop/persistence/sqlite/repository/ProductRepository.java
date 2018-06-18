package es.pharmashop.persistence.sqlite.repository;

import es.pharmashop.persistence.sqlite.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, String> {
  Product findByCnAndEan(String ean, String cn);
}
