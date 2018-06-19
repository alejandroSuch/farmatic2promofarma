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
  Boolean revision;

  public boolean validAndReviewed() {
    return StringUtils.hasText(uniqueCode) && revision;
  }
}
