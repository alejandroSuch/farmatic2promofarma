package es.pharmashop.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import sun.rmi.server.InactiveGroupException;

import java.math.BigDecimal;

@Data
@Builder
@ToString
@AllArgsConstructor
public class Article {
    String id;
    String description;
    BigDecimal price;
    Integer stock;

    public void applyFactorToStock(Float factor) {
        double newStock = Math.ceil(this.stock * factor);
        this.stock = new Double(newStock).intValue();
    }

    public void fixStock() {
        if (this.stock < 0) {
            this.stock = 0;
        }
    }
}
