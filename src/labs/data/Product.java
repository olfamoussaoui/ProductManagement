package labs.data;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static labs.data.Rating.*;

public class Product {
    public static final BigDecimal DISCOUNT_RATE = BigDecimal.valueOf(0.1);
    private int id;
    private String name;
    private BigDecimal price;

    private Rating rating;

    public Product() {
        this(0, "no name", BigDecimal.ZERO);
    }

    public Product(int id, String name, BigDecimal price, Rating rating) {
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

    public void setPrice(final BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getDiscount() {
        return this.price.multiply(DISCOUNT_RATE).setScale(2, RoundingMode.HALF_UP);
    }

    public Rating getRating() {
        return rating;
    }
}
