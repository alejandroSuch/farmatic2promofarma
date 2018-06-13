package es.pharmashop.persistence.sqlite.converter;

import javax.persistence.AttributeConverter;

public class BooleanToIntegerConverter implements AttributeConverter<Boolean, Integer> {
  @Override
  public Integer convertToDatabaseColumn(Boolean aBoolean) {
    return aBoolean ? 1 : 0;
  }

  @Override
  public Boolean convertToEntityAttribute(Integer integer) {
    return integer == 1;
  }
}
