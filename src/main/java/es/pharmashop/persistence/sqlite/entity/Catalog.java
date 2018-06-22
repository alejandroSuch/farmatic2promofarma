package es.pharmashop.persistence.sqlite.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "catalog")
public class Catalog {
  @Id
  @Column(name = "IDPROMOFARMA")
  String idPromofarma;

  @Column(name = "EAN")
  String ean;

  @Column(name = "CN")
  String cn;
}
