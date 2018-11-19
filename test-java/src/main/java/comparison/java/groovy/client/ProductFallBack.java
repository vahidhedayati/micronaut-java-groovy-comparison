package comparison.java.groovy.client;

import comparison.java.groovy.view.Product;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.retry.annotation.Fallback;
import io.reactivex.Maybe;
import io.reactivex.Single;

import java.util.List;
@Client(id = "product" , path="/product")
@Fallback
public class ProductFallBack  implements ProductClient {

    @Get("/")
    public  Maybe<List<Product>> list() {

        return null;
    }


    @Get("/find/{name}")
    public Maybe<Product> find(String name) {
        System.out.println("Fall back is called");
        return Single.just(new Product("Blank","description",0,0.00)).toMaybe();
    }
}
