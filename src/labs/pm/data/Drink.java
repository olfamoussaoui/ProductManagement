package labs.pm.data;

import java.math.BigDecimal;

public class Drink extends Product {

    Drink(int id, String name, BigDecimal price, Rating rating) {
        super(id, name, price, rating);
    }

    @Override
    public Product applyRating(Rating rating) {
        return new Drink(
                this.getId(),
                this.getName(),
                this.getPrice(),
                rating
        );
    }
}
