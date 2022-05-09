package labs.pm.data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Objects;

import static labs.pm.data.Rating.*;

public abstract class Product implements Rateable<Product> {
    public static final BigDecimal DISCOUNT_RATE = BigDecimal.valueOf(0.1);
    private final int id;
    private final String name;
    private final BigDecimal price;

    private final Rating rating;

    public Product() {
        this(0, "no name", BigDecimal.ZERO);
    }

    Product(int id, String name, BigDecimal price, Rating rating) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.rating = rating;
    }

    public Product(int id, String name, BigDecimal price) {
        this(id, name, price, NOT_RATED);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public BigDecimal getDiscount() {
        return this.price.multiply(DISCOUNT_RATE).setScale(2, RoundingMode.HALF_UP);
    }

    public LocalDate getBestBefore() {
        return LocalDate.now();
    }

    @Override
    public Rating getRating() {
        return rating;
    }

    public abstract Product applyRating(Rating rating);

    @Override
    public String toString() {
        return "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", discount=" + getDiscount() +
                ", rating=" + rating.getStars() +
                ", bestBefore=" + getBestBefore();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof Product) {
            final Product product = (Product) o;
            return id == product.id &&
                    Objects.equals(name, product.name) &&
                    Objects.equals(price, product.price) &&
                    rating == product.rating;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        return 23 * hash + this.id;
    }
}
