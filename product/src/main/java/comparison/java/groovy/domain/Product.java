package comparison.java.groovy.domain;

public class Product {
    private  String name;
    private  String description;
    private long count;
    private double price;


    public Product(String name, String description, long count, double price) {
        this.name = name;
        this.description = description;
        this.count = count;
        this.price = price;
    }
    public Product() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
