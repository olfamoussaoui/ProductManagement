package labs.pm.data;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;
import java.util.ResourceBundle;

public class ProductManager {
    private Product product;
    private Review review;

    private final Locale locale;
    private final ResourceBundle resources;
    private final DateTimeFormatter dateFormat;
    private final NumberFormat moneyFormat;

    public ProductManager(final Locale locale) {
        this.locale = locale;
        resources = ResourceBundle.getBundle("labs.pm.data.resources", locale);
        dateFormat = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).localizedBy(locale);
        moneyFormat = NumberFormat.getCurrencyInstance(locale);
    }

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

    public void printProductReport() {
        System.out.println("product = " + product);
        System.out.println("this = " + this);
        StringBuilder txt = new StringBuilder();
        txt.append(MessageFormat.format(resources.getString("product"),
                product.getName(),
                moneyFormat.format(product.getPrice()),
                product.getRating().getStars(),
                dateFormat.format(product.getBestBefore()))
        );
        txt.append('\n');
        if (review != null) {
            txt.append(MessageFormat.format(resources.getString("review"),
                    review.getRating().getStars(),
                    review.getComments())
            );
        } else {
            txt.append(resources.getString("no.reviews"));
        }
        txt.append('\n');
        System.out.println(txt);
    }
}
