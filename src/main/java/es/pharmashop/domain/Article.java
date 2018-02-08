package es.pharmashop.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Data
@Builder
@ToString
@AllArgsConstructor
public class Article {
    String id;
    String ean;
    String description;
    BigDecimal price;
    BigDecimal taxes;
    Integer stock;

    public void applyFactorToStock(Float factor) {
        double newStock = Math.ceil(this.stock * factor);
        this.stock = new Double(newStock).intValue();
    }

    public void applyMarginToPrice(Float margin) {
        if (margin == null) {
            throw new NullPointerException("margin cannot be null");
        }

        BigDecimal multiplicand = new BigDecimal(1f + margin).setScale(2, RoundingMode.UP);
        price = price.multiply(multiplicand).setScale(2, RoundingMode.UP);
    }

    public void fixStock() {
        if (this.stock < 0) {
            this.stock = 0;
        }
    }
}
