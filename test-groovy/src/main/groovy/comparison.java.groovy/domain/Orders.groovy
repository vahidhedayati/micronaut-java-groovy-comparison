package comparison.java.groovy.domain

import comparison.java.groovy.view.Product


class Orders {

    Product product
    Integer quantity

    String description
    BigDecimal price
    Currency currency = Currency.getInstance(Locale.UK)



}
