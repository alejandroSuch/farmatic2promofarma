package es.pharmashop.persistence.sqlite.repository;

import es.pharmashop.persistence.sqlite.entity.Product;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ProductRepository extends PagingAndSortingRepository<Product, Long> {
}
