package comparison.java.groovy.client;

import comparison.java.groovy.view.Product;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.annotation.Client;
import io.reactivex.Maybe;

import java.util.List;

@Client(id = "product" , path="/product")
public interface ProductClient {


    @Get("/")
    Maybe<List<Product>> list();


    @Get("/find/{name}")
    Maybe<Product> find(String name);

}

