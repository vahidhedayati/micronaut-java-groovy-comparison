package comparison.java.groovy.view

import grails.gorm.annotation.Entity


@Entity
class Product {

    String name
    String description

    Date date

    BigDecimal price
    Boolean wheels=false


    Integer width=0
    Integer height=0

}