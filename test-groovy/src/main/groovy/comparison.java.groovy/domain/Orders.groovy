package comparison.java.groovy.domain

import comparison.java.groovy.view.Item

class Orders {

    Item item
    Integer quantity

    String description
    BigDecimal price
    Currency currency = Currency.getInstance(Locale.UK)

}
