package labs.pm.data;

import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.FormatStyle;
import java.util.*;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static java.lang.System.lineSeparator;
import static java.lang.System.out;

public class ProductManager {
    public static final String LAN_TAG_US = Locale.US.toLanguageTag();
    public static final String LAN_TAG_UK = Locale.UK.toLanguageTag();
    public static final String LAN_TAG_ESUS = "es-US";
    public static final String LAN_TAG_FR = Locale.FRANCE.toLanguageTag();
    public static final String LAN_TAG_FRCA = "fr-CA";
    public static final String LAN_TAG_RU = "ru-RU";
    public static final String LAN_TAG_CN = Locale.CHINA.toLanguageTag();
    public static final String LAN_TAG_NL = "nl-NL";
    private Map<Product, List<Review>> products = new HashMap<>();

    private static final Logger logger = Logger.getLogger(ProductManager.class.getName());
    private ResourceFormatter formatter;

    private ResourceBundle config = ResourceBundle
            .getBundle("labs.pm.data.config");

    private MessageFormat reviewFormat = new MessageFormat(config.getString("review.data.format"));
    private MessageFormat productFormat = new MessageFormat(config.getString("product.data.format"));
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

    private Path reportsFolder = Path.of(config.getString("reports.folder"));

    private Path dataFolder = Path.of(config.getString("data.folder"));

    private Path tempFolder = Path.of(config.getString("temp.folder"));

    public ProductManager(Locale locale) {
        this(locale.toLanguageTag());
    }

    public ProductManager(String languageTag) {
        changeLocale(languageTag);
        loadAllData();
    }

    public void changeLocale(String languageTag) {
        formatter = formatters
                .getOrDefault(languageTag, formatters.get(LAN_TAG_UK));
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
        product = product.applyRating(
                Rateable.convert(
                        (int) Math.round(
                                reviews.stream()
                                        .mapToInt(r -> r.getRating().ordinal())
                                        .average()
                                        .orElse(0))));
        products.put(product, reviews);
        return product;
    }

    public Product reviewProduct(int id, Rating rating, String comments) {
        try {
            return reviewProduct(findProduct(id), rating, comments);
        } catch (Exception exception) {
            logger
                    .log(Level.INFO, exception.getMessage());
        }
        return null;
    }

    public void printProductReport(Product product) throws IOException {
        List<Review> reviews = products.get(product);
        Collections.sort(reviews);
        Path productFile = reportsFolder
                .resolve(MessageFormat.format(config.getString("report.file"), product.getId()));

        try (PrintWriter out =
                     new PrintWriter(
                             new OutputStreamWriter(
                                     Files.newOutputStream(productFile, StandardOpenOption.CREATE),
                                     "UTF-8")
                     )
        ) {
            out.append(formatter.formatProduct(product) + lineSeparator());

            if (reviews.isEmpty()) {
                out.append(formatter.getText("no.reviews") + lineSeparator());
            } else {
                out.append(reviews.stream()
                        .map(review -> formatter.formatReview(review) + lineSeparator())
                        .collect(Collectors.joining()));
            }
        }

    }

    public void printProducts(Predicate<Product> filter, Comparator<Product> sorter) {
        StringBuilder txt = new StringBuilder();
        products.keySet().stream()
                .sorted(sorter)
                .filter(filter)
                .forEach(product -> txt.append(formatter.formatProduct(product)).append('\n'));
        out.println(txt);
    }

    private Review parseReview(String text) {
        Review review = null;
        try {
            Object[] values = reviewFormat.parse(text);
            review = new Review(
                    Rateable.convert(Integer.parseInt((String) values[0])),
                    (String) values[1]
            );
        } catch (ParseException | NumberFormatException ex) {
            logger
                    .log(
                            Level.WARNING,
                            "Error parsing review " + text);
        }
        return review;
    }

    private void dumpData() {
        try {
            // You could also use Files.createDirectories, that also creates all parent-folders that does not exist yet.
            // Then the check on existence would not be necessary.
            if (Files.notExists(tempFolder)) {
                Files.createDirectory(tempFolder);
            }
            Path tempFile = tempFolder.resolve(MessageFormat.format(config.getString("temp.file"), LocalDate.now()));
            try (ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(tempFile, StandardOpenOption.CREATE))) {
                System.out.println("Dump products to " + tempFile.getFileName());
                out.writeObject(products);
                products = new HashMap<>();
            }
        } catch (IOException exception) {
            logger.log(Level.SEVERE, "Error dumping data " + exception.getMessage());
        }

    }

    @SuppressWarnings("unchecked")
    private void restoreData() {
        try {
            Path tempFile = Files.list(tempFolder)
                    .filter(path -> path.getFileName().toString().endsWith(".tmp")).findFirst().orElseThrow();
            try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(tempFile, StandardOpenOption.DELETE_ON_CLOSE))) {
                System.out.println("Read projects from " + tempFile.getFileName());
                products = (HashMap) in.readObject();
            }
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Error reading data " + ex.getMessage(), ex);

        }

    }

    private void loadAllData() {
        try {
            products = Files
                    .list(dataFolder)
                    .filter(file -> file.getFileName().toString().startsWith("product"))
                    .map(file -> loadProduct(file))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toMap(
                                    product -> product,
                                    product -> loadReviews(product)
                            )
                    );
        } catch (IOException exception) {
            logger.log(Level.WARNING, "Error loading data " + exception.getMessage());
        }
    }

    private Product loadProduct(Path file) {
        Product product = null;
        if (Files.exists(file)) {
            try {
                //  Alternative for StandardCharsets.UTF_8: Charset.forName("UTF-8"). However, again this relies on a hardcoded string.
                product = parseProduct(
                        Files.lines(dataFolder.resolve(file), StandardCharsets.UTF_8).findFirst().orElseThrow());
            } catch (IOException ex) {
                logger.log(Level.WARNING, "Error loading product " + ex.getMessage());
            }
        }
        return product;
    }

    private List<Review> loadReviews(Product product) {
        List<Review> reviews = null;
        Path file = dataFolder
                .resolve(
                        MessageFormat.format(
                                config.getString("reviews.data.file"),
                                product.getId()
                        )
                );
        if (Files.notExists(file)) {
            reviews = new ArrayList<>();
        } else {
            try {
                reviews = Files
                        .lines(file, Charset.forName("UTF-8"))
                        .map(text -> parseReview(text))
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());
            } catch (IOException exception) {
                logger.log(Level.WARNING, "Error loading reviews" + exception.getMessage());
            }
        }
        return reviews;
    }

    public Product parseProduct(String text) {
        Product product = null;
        try {
            Object[] values = productFormat.parse(text);
            String type = (String) values[0];
            int id = Integer.parseInt((String) values[1]);
            String name = (String) values[2];
            BigDecimal price = BigDecimal.valueOf(Double.parseDouble((String) values[3]));
            Rating rating = Rateable.convert(Integer.parseInt((String) values[4]));
            switch (type) {
                case "D":
                    product = new Drink(id, name, price, rating);
                    break;
                case "F":
                    LocalDate bestBefore = LocalDate.parse((String) values[5]);
                    product = new Food(id, name, price, rating, bestBefore);
            }
        } catch (ParseException | NumberFormatException | DateTimeParseException ex) {
            logger.log(Level.WARNING, "Error parsing product " + text + " " + ex);
        }
        return product;
    }

    public Map<String, String> getDiscounts() {
        return products.keySet()
                .stream()
                .collect(
                        Collectors.groupingBy(
                                product -> product.getRating().getStars(),
                                Collectors.collectingAndThen(
                                        Collectors.summingDouble(product -> product.getDiscount().doubleValue()),
                                        discount -> formatter.moneyFormat.format(discount))));
    }

    public void printProductReport(int id) {
        try {
            printProductReport(findProduct(id));
        } catch (ProductManagerException exception) {
            logger
                    .log(Level.INFO, exception.getMessage());
        } catch (IOException exception) {
            logger
                    .log(Level.SEVERE, "Error printing product report " + exception.getMessage(), exception);
        }
    }

    public Product findProduct(int id) throws ProductManagerException {
        return products.keySet()
                .stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElseThrow(
                        () ->
                                new ProductManagerException(
                                        "Product with id " + id + " not found"
                                )
                );
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
