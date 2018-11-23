package comparison.java.groovy.client;

import comparison.java.groovy.view.Product;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.retry.annotation.CircuitBreaker;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;

import java.util.List;
@Client(id = "product" , path="/products")
//@CircuitBreaker(delay = "1s", attempts = "5", multiplier = "3", reset = "100s")
public interface ProductClient {


    @Get("/")
    Maybe<List<Product>> list();


    @Get("/find/{name}")
    Maybe<Product> find(String name);

    @Get("/finds/{name}")
    Flowable<Product> finds(String name);

}