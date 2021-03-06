package comparison.java.groovy.init;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoCollection;
import comparison.java.groovy.dbConfig.ProductConfig;
import comparison.java.groovy.domain.Product;
import groovy.transform.CompileStatic;
import groovy.util.logging.Slf4j;
import io.micronaut.context.event.ApplicationEventListener;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.runtime.server.event.ServerStartupEvent;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Arrays;

import static com.mongodb.client.model.Filters.eq;

@Slf4j
@CompileStatic
@Singleton
public class BootStrap implements ApplicationEventListener<ServerStartupEvent> {

    final EmbeddedServer embeddedServer;
    private MongoClient mongoClient;
    private final ProductConfig config;

    final static Logger log = LoggerFactory.getLogger(BootStrap.class);

    @Inject
    public BootStrap(EmbeddedServer embeddedServer,MongoClient mongoClient,ProductConfig config) {
        this.embeddedServer = embeddedServer;
        this.mongoClient = mongoClient;
        this.config=config;
    }


    @Override
    public void onApplicationEvent(ServerStartupEvent event) {
        setupDefaults();
    }

    void setupDefaults() {

        for (int a=0; a < 1000; a++) {
            System.out.println("Setting up product: a "+a);

            Maybe<Product> currentBeer = Flowable.fromPublisher(
                    getProduct()
                            .find(eq("name", "a"+a))
                            .limit(1)
            ).firstElement();

            Product product = new Product("a"+a, "a"+a, 2000, 12.49+a);
            currentBeer.switchIfEmpty(
                    Single.fromPublisher(getProduct().insertOne(product))
                            .map(success -> product)

            ).subscribe(System.out::println);
        }
    }

    private MongoCollection<Product> getProduct() {
        return mongoClient
                .getDatabase(config.getDatabaseName())
                .getCollection(config.getCollectionName(), Product.class);
    }


}
