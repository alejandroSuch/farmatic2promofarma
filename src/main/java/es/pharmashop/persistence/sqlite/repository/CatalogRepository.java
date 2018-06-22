package es.pharmashop.persistence.sqlite.repository;

import es.pharmashop.persistence.sqlite.entity.Catalog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CatalogRepository extends JpaRepository<Catalog, String> {
  public Catalog findByEan(String ean);
}
