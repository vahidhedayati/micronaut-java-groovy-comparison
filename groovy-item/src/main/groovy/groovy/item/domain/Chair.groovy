package groovy.item.domain

import grails.gorm.annotation.Entity


@Entity
class Chair extends Generic {

    Boolean wheels=false

  //  static mapping = {
      //  discriminator value: 'chair'
   // }
}