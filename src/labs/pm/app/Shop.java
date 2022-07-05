package labs.pm.app;

import labs.pm.data.ProductManager;

public class Shop {
    public static void main(String[] args) {
        ProductManager pm = new ProductManager(ProductManager.LAN_TAG_UK);

//        pm.printProductReport(101);
//        pm.printProductReport(103);
//        int pId = 164;
//        pm.createProduct(pId, "Kombucha", BigDecimal.valueOf(1.99), Rating.NOT_RATED);
//        pm.reviewProduct(pId, TWO_STAR, "Looks like tea but is it?");
//        pm.reviewProduct(pId, FOUR_STAR, "Fine tea");
//        pm.reviewProduct(pId, FOUR_STAR, "This is not tea");
//        pm.reviewProduct(pId, FIVE_STAR, "Perfect!");
//
////        pm.printProductReport(pId);
//
//        pm.printProductReport(101);
//        pm.printProductReport(105);
//
////        Comparator<Product> ratingSorter = (p1, p2) -> p2.getRating().ordinal() - p1.getRating().ordinal();
////        Comparator<Product> priceSorter = (p1, p2) -> p2.getPrice().compareTo(p1.getPrice());
////
////        Predicate<Product> priceFilter = (p) -> p.getPrice().floatValue() < 2;
////        System.out.println("Products with price less than 2: ");
////        pm.printProducts(priceFilter, ratingSorter);
////        //pm.printProducts(p -> p.getPrice().floatValue() < 2, (p1, p2) -> p2.getRating().ordinal() - p1.getRating().ordinal());
////        System.out.println("Discounts: ");
//        pm.getDiscounts().forEach((rating, discount) -> System.out.println(rating + "\t" + discount));
    }

}