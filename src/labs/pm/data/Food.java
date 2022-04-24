package labs.pm.data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public class Food extends Product {
    private LocalDate bestBefore;

    Food(int id, String name, BigDecimal price, Rating rating, LocalDate bestBefore) {
        super(id, name, price, rating);
        this.bestBefore = bestBefore;
    }

    public LocalDate getBestBefore() {
        return bestBefore;
    }

    @Override
    public String toString() {
        return super.toString() +
                ", bestBefore=" + bestBefore;
    }

    @Override
    public BigDecimal getDiscount() {
        var currentTime = LocalTime.now();
        return (currentTime.isAfter(LocalTime.of(16, 30))) &&
                (currentTime.isBefore(LocalTime.of(17, 30)))
                ? super.getDiscount() : BigDecimal.ZERO;
    }

    @Override
    public Product applyRating(Rating rating) {
        return new Food(
                this.getId(),
                this.getName(),
                this.getPrice(),
                rating,
                this.getBestBefore()
        );
    }
}
