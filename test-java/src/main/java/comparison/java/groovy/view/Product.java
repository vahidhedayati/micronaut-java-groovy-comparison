package comparison.java.groovy.view;

import java.math.BigDecimal;
import java.util.Date;

public class Product {

    private String name;
    private String description;

    private Date date;

    private BigDecimal price;
    private Boolean wheels=false;


    private Integer width=0;
    private Integer height=0;

    public Product(String name, String description, Date date, BigDecimal price, Boolean wheels, Integer width, Integer height) {
        this.name = name;
        this.description = description;
        this.date = date;
        this.price = price;
        this.wheels = wheels;
        this.width = width;
        this.height = height;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Boolean getWheels() {
        return wheels;
    }

    public void setWheels(Boolean wheels) {
        this.wheels = wheels;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }
}
