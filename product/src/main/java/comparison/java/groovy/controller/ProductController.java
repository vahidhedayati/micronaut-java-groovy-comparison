package comparison.java.groovy.controller;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoCollection;
import comparison.java.groovy.dbConfig.ProductConfig;
import comparison.java.groovy.domain.Product;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.validation.Validated;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;

import javax.inject.Inject;
import javax.validation.Valid;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;
@Controller("/product")
public class ProductController implements ProductOperations<Product> {

    final EmbeddedServer embeddedServer;
    private final ProductConfig configuration;

    private MongoClient mongoClient;


    @Inject
    public ProductController(EmbeddedServer embeddedServer,
                           ProductConfig configuration,
                            MongoClient mongoClient) {
        this.embeddedServer = embeddedServer;
        this.configuration = configuration;
        this.mongoClient = mongoClient;
    }


    @Get("/")
    @Override
    public  Single<List<Product>> list() {
        return  Flowable.fromPublisher(getCollection().find()).toList();
    }


    @Get("/status")
    public HttpResponse status() {
        return HttpResponse.ok();
    }


    @Get("/find/{name}")
    @Override
    public Maybe<Product> find(String name) {
        //System.out.println("Att "+name);
        Maybe<Product> o = Flowable.fromPublisher(
                getCollection()
                        .find(eq("name", name))
                        .limit(1)
        ).firstElement();

        //System.out.println("__________________________________Found "+o.subscribe());
        return o;
    }

    @Override
    public Single<Product> save(@Valid Product product) {
        return find(product.getName())
                .switchIfEmpty(
                        Single.fromPublisher(getCollection().insertOne(product))
                                .map(success -> product)
                );

    }

    private MongoCollection<Product> getCollection() {
        return mongoClient
                .getDatabase(configuration.getDatabaseName())
                .getCollection(configuration.getCollectionName(), Product.class);
    }

}
