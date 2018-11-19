package comparison.java.groovy.client

import comparison.java.groovy.view.Product
import io.micronaut.http.annotation.Get
import io.micronaut.http.client.annotation.Client
import io.reactivex.Maybe
import io.reactivex.Single

@Client(id = "product" , path="/product")
class ProductFall implements ProductClient {


    public  Maybe<List<Product>> list() {

    }

    public Maybe<Product> find(String name) {

        Single.just(new Product("Blank","description",0,0.00)).toMaybe();
    }
}
