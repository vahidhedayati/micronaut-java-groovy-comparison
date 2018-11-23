package comparison.java.groovy.controller;

import comparison.java.groovy.domain.Product;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.validation.Validated;
import io.reactivex.Maybe;
import io.reactivex.Single;

import javax.validation.Valid;
import java.util.List;

@Validated
public interface ProductOperations<T extends Product> {

    @Get("/")
    Single<List<Product>> list();

    @Get("/status")
    HttpResponse status();

    @Get("/find/{name}")
    Maybe<Product> find(String name);


    @Post("/")
    Single<T> save(@Valid @Body T beer);
}
