package groovy.item.controller

import groovy.item.domain.Product
import groovy.item.service.ProductService
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.reactivex.Maybe

import javax.inject.Inject

@Controller("/product")


class ProductController {

    @Inject
    final ProductService productService


    @Get("/")
    List<Product> list() {
        return productService.findAll()
    }


    @Get("/{name}")
    Maybe<Product> find(String name) {
        return productService.find(name)
    }


}