package xml.streamer.view;


import java.math.BigDecimal;
import java.util.Currency;
import java.util.Locale;

public class Orders {


   // private Product product;

    private String name;
    private String description;
    private BigDecimal price;
    private Currency currency = Currency.getInstance(Locale.UK);

    public Orders(//Product product,
                  String name,
                  String description, BigDecimal price) {
       // this.product = product;
        this.name=name;
        this.description = description;
        this.price = price;
    }

    protected Orders() {
    }

   // public Product getProduct() {
   //     return product;
   // }

   // public void setProduct(Product product) {
    //    this.product = product;
   // }

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


    public String toString() {
        return name+" - "+description+" - "+price+" - "+currency;
    }

}
