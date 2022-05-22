package labs.pm.data;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;

public class ProductManager {
    public static final String LAN_TAG_US = Locale.US.toLanguageTag();
    public static final String LAN_TAG_UK = Locale.UK.toLanguageTag();
    public static final String LAN_TAG_ESUS = "es-US";
    public static final String LAN_TAG_FR = Locale.FRANCE.toLanguageTag();
    ;
    public static final String LAN_TAG_FRCA = "fr-CA";
    public static final String LAN_TAG_RU = "ru-RU";
    public static final String LAN_TAG_CN = Locale.CHINA.toLanguageTag();
    ;
    public static final String LAN_TAG_NL = "nl-NL";
    private Map<Product, List<Review>> products = new HashMap<>();
    private ResourceFormatter formatter;
    private static Map<String, ResourceFormatter> formatters = Map.of(
            LAN_TAG_UK, new ResourceFormatter(Locale.UK),
            LAN_TAG_US, new ResourceFormatter(Locale.US),
            LAN_TAG_ESUS, new ResourceFormatter(new Locale(LAN_TAG_ESUS)),
            LAN_TAG_FR, new ResourceFormatter(Locale.FRANCE),
            LAN_TAG_FRCA, new ResourceFormatter(new Locale("fr", "CA")),
            LAN_TAG_RU, new ResourceFormatter(new Locale("ru", "RU")),
            LAN_TAG_CN, new ResourceFormatter(Locale.CHINA),
            LAN_TAG_NL, new ResourceFormatter(new Locale("nl", "NL"))
    );

    public ProductManager(Locale locale) {
        this(locale.toLanguageTag());
    }

    public ProductManager(String languageTag) {
        changeLocale(languageTag);
    }

    public void changeLocale(String languageTag) {
        formatter
                = formatters.getOrDefault(languageTag, formatters.get(LAN_TAG_UK));
    }

    public static Set<String> getSupportedLocales() {
        return formatters.keySet();
    }


    public Product createProduct(int id, String name, BigDecimal price, Rating rating, LocalDate bestBefore) {
        Product product = new Food(id, name, price, rating, bestBefore);
        products.putIfAbsent(product, new ArrayList<>());
        return product;
    }

    public Product createProduct(int id, String name, BigDecimal price, Rating rating) {
        Product product = new Drink(id, name, price, rating);
        products.putIfAbsent(product, new ArrayList<>());
        return product;
    }

    public Product reviewProduct(Product product, Rating rating, String comments) {
        List<Review> reviews = products.get(product);
        products.remove(product, reviews);
        reviews.add(new Review(rating, comments));

        int sum = 0;
        for (Review review : reviews) {
            sum += review.getRating().ordinal();
        }
        product = product
                .applyRating(
                        Rateable.convert(Math.round((float) sum / reviews.size()))
                );
        products.put(product, reviews);
        return product;
    }

    public Product reviewProduct(int id, Rating rating, String comments) {
        Product product = findProduct(id);
        return reviewProduct(product, rating, comments);
    }

    public void printProductReport(Product product) {
        List<Review> reviews = products.get(product);
        StringBuilder txt = new StringBuilder();
        txt.append(formatter.formatProduct(product));
        txt.append('\n');
        Collections.sort(reviews);
        for (Review review : reviews) {
            txt.append(formatter.formatReview(review));
            txt.append('\n');
        }
        if (reviews.isEmpty()) {
            txt.append(formatter.getText("no.reviews"));
            txt.append('\n');
        }
        System.out.println(txt);
    }

    public void printProductReport(int id) {
        Product product = findProduct(id);
        printProductReport(product);
    }

    public Product findProduct(int id) {
        Product result = null;
        for (Product product : products.keySet()) {
            if (product.getId() == id) {
                result = product;
                break;
            }
        }
        return result;
    }

    private static class ResourceFormatter {
        private final Locale locale;
        private final ResourceBundle resources;
        private final DateTimeFormatter dateFormat;
        private final NumberFormat moneyFormat;

        private ResourceFormatter(Locale locale) {
            this.locale = locale;
            resources = ResourceBundle.getBundle("labs.pm.data.resources", locale);
            dateFormat = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).localizedBy(locale);
            moneyFormat = NumberFormat.getCurrencyInstance(locale);
        }

        private String formatProduct(Product product) {
            return MessageFormat.format(resources.getString("product"),
                    product.getName(),
                    moneyFormat.format(product.getPrice()),
                    product.getRating().getStars(),
                    dateFormat.format(product.getBestBefore()));
        }

        private String formatReview(Review review) {
            return MessageFormat.format(resources.getString("review"),
                    review.getRating().getStars(),
                    review.getComments());
        }

        private String getText(String key) {
            return resources.getString(key);
        }
    }
}
