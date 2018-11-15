package comparison.java.groovy.client

import comparison.java.groovy.view.Item
import io.micronaut.http.annotation.Get
import io.micronaut.http.client.annotation.Client
import io.reactivex.Maybe
import io.reactivex.Single

@Client(id = "groovy-item" , path="items")
interface ItemClient {


    @Get("/{name}")
    Maybe<Item> find(String name)

    @Get("/")
    Single<List<Item>> list()
}
