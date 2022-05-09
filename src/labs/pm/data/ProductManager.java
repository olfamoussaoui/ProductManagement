package labs.pm.data;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ProductManager {
    private Product product;
    private Review review;

    public Product createProduct(
            final int id,
            final String name,
            final BigDecimal price,
            final Rating rating,
            final LocalDate bestBefore) {
        product = new Food(
                id,
                name,
                price,
                rating,
                bestBefore
        );
        return product;
    }

    public Product createProduct(
            final int id,
            final String name,
            final BigDecimal price,
            final Rating rating) {
        product = new Drink(
                id,
                name,
                price,
                rating
        );
        return product;
    }

    public Product reviewProduct(
            final Product product,
            final Rating rating,
            final String comment
    ) {
        review = new Review(rating, comment);
        this.product = product.applyRating(rating);
        return this.product;
    }
}
