package es.pharmashop.persistence.sqlite.entity;

import es.pharmashop.persistence.sqlite.converter.BooleanToIntegerConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product")
public class Product {
  @Id
  @Column(name = "id")
  Long id;

  @Column(name = "unique_code")
  String uniqueCode;

  @Column(name = "cn")
  String cn;

  @Column(name = "ean")
  String ean;

  @Column(name = "revision")
  @Convert(converter = BooleanToIntegerConverter.class)
  Boolean revision;
}
