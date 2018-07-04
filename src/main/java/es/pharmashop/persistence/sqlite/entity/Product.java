package es.pharmashop.persistence.sqlite.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product")
public class Product {
  @Id
  @Column(name = "ean")
  String ean;

  @Column(name = "unique_code")
  String uniqueCode;

  @Column(name = "cn")
  String cn;

  @Column(name = "name")
  String name;

  @Column(name = "revision")
  Integer revision;

  public boolean validAndReviewed() {
    return StringUtils.hasText(uniqueCode) && revision == 1;
  }

  public Product clone() {
    return Product.builder()
      .ean(ean)
      .uniqueCode(uniqueCode)
      .cn(cn)
      .name(name)
      .revision(revision)
      .build();
  }
}
