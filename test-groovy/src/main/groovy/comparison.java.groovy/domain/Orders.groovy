package comparison.java.groovy.domain

import comparison.java.groovy.view.Product


class Orders {

    Product product


    String description
    BigDecimal price
    Currency currency = Currency.getInstance(Locale.UK)



}
