package comparison.java.groovy.client


import comparison.java.groovy.view.Product
import io.micronaut.http.annotation.Get
import io.micronaut.http.client.annotation.Client
import io.reactivex.Maybe

@Client(id = "groovy-item" , path="/product")
interface ProductClient {


    @Get("/")
    Maybe<List<Product>> list()


    @Get("/{name}")
    Maybe<Product> find(String name)



}
