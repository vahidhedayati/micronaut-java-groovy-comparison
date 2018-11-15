package comparison.java.groovy.domain;

import comparison.java.groovy.view.Item;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Locale;

public class Orders {


    private Item item;

    private String description;
    private BigDecimal price;
    private Currency currency = Currency.getInstance(Locale.UK);

    public Orders(Item item,  String description, BigDecimal price) {
        this.item = item;
        this.description = description;
        this.price = price;
    }

    protected Orders() {
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

}
