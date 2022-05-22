package labs.pm.app;

import labs.pm.data.Product;
import labs.pm.data.ProductManager;
import labs.pm.data.Rating;

import java.math.BigDecimal;
import java.util.Locale;

import static labs.pm.data.Rating.*;

public class Shop {
    public static void main(String[] args) {

        ProductManager pm = new ProductManager(Locale.US);

        Product p1 = pm.createProduct(
                101,
                "Tea",
                BigDecimal.valueOf(1.99),
                Rating.THREE_STAR
        );

        pm.printProductReport();
        p1 = pm.reviewProduct(p1, FOUR_STAR, "Nice hot cup of tea");
        p1 = pm.reviewProduct(p1, TWO_STAR, "Rather weak tea");
        p1 = pm.reviewProduct(p1, FOUR_STAR, "Fine tea");
        p1 = pm.reviewProduct(p1, FOUR_STAR, "Good tea");
        p1 = pm.reviewProduct(p1, FIVE_STAR, "Perfect tea");
        p1 = pm.reviewProduct(p1, THREE_STAR, "Just add some lemon");
        pm.printProductReport();

//        Product p2 = pm.createProduct(
//                102,
//                "Coffee",
//                BigDecimal.valueOf(1.99),
//                Rating.FOUR_STAR
//        );
//        Product p3 = pm.createProduct(
//                103,
//                "Cake",
//                BigDecimal.valueOf(1.99),
//                Rating.FIVE_STAR,
//                LocalDate.now().plusDays(2)
//        );
//
//        Product p4 = pm.createProduct(
//                105,
//                "Cookie",
//                BigDecimal.valueOf(1.99),
//                Rating.TWO_STAR,
//                LocalDate.now()
//        );
//
//        Product p5 = p2.applyRating(Rating.THREE_STAR);
//
//        Product p6 = pm.createProduct(
//                104,
//                "Chocolate",
//                BigDecimal.valueOf(2.99),
//                Rating.FOUR_STAR
//        );
//
//        Product p7 = pm.createProduct(
//                104,
//                "Chocolate",
//                BigDecimal.valueOf(2.99),
//                Rating.FOUR_STAR,
//                LocalDate.now().plusDays(2)
//        );
//
//        Product p8 = p4.applyRating(Rating.FIVE_STAR);
//        Product p9 = p1.applyRating(Rating.TWO_STAR);
//
//        System.out.println(p6.equals(p7));
//        p1.getBestBefore();
//        p3.getBestBefore();
//
//        System.out.println("p1 = " + p1);
//        System.out.println("p2 = " + p2);
//        System.out.println("p3 = " + p3);
//        System.out.println("p4 = " + p4);
//        System.out.println("p5 = " + p5);
//        System.out.println("p6 = " + p6);
//        System.out.println("p7 = " + p7);
//        System.out.println("p8 = " + p8);
//        System.out.println("p9 = " + p9);
    }
}