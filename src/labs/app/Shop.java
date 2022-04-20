package labs.app;

import labs.data.Product;
import labs.data.Rating;

import java.math.BigDecimal;

public class Shop {
    public static void main(String[] args) {
        Product p1 = new Product(101, "Tea", BigDecimal.valueOf(1.99));
        Product p2 = new Product(102, "Coffee", BigDecimal.valueOf(1.99), Rating.FOUR_STAR);
        Product p3 = new Product(103, "Cake", BigDecimal.valueOf(1.99), Rating.FIVE_STAR);
Product p4 = new Product();
//        p1.setId(101);
//        p1.setName("Tea");
//        p1.setPrice(BigDecimal.valueOf(1.99));

        System.out.println(p1.getId() + " " + p1.getName() + " " + p1.getPrice() + " " + p1.getDiscount() + " " + p1.getRating().getStars());
        System.out.println(p2.getId() + " " + p2.getName() + " " + p2.getPrice() + " " + p2.getDiscount() + " " + p2.getRating().getStars());
        System.out.println(p3.getId() + " " + p3.getName() + " " + p3.getPrice() + " " + p3.getDiscount() + " " + p3.getRating().getStars());
        System.out.println(p4.getId() + " " + p4.getName() + " " + p4.getPrice() + " " + p4.getDiscount() + " " + p4.getRating().getStars());
    }
}