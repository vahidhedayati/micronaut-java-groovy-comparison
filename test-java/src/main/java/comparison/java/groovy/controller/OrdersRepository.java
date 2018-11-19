package comparison.java.groovy.controller;

import comparison.java.groovy.client.ProductClient;
import comparison.java.groovy.domain.Orders;
import comparison.java.groovy.view.Product;
import io.lettuce.core.KeyValue;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.reactive.RedisReactiveCommands;
import io.micronaut.core.convert.value.ConvertibleValues;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.inject.Singleton;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.function.Function;

@Singleton
public class OrdersRepository {

    private static final Logger LOG = LoggerFactory.getLogger(OrdersRepository.class);

    private final StatefulRedisConnection<String, String> redisConnection;
    private final ProductClient productClient;


    public OrdersRepository(ProductClient productClient, StatefulRedisConnection<String, String> redisConnection) {
        this.productClient=productClient;
        this.redisConnection = redisConnection;
    }

    /**
     * @return Returns all current offers
     */
    public Mono<List<Orders>> all() {
        RedisReactiveCommands<String, String> commands = redisConnection.reactive();

        return commands.keys("*").flatMap(keyToOrder(commands)).collectList();
    }
    /**
     * @return Obtain a random offer or return {@link Mono#empty()} if there is none
     */
    public Mono<Orders> random() {
        RedisReactiveCommands<String, String> commands = redisConnection.reactive();
        return commands.randomkey().flatMap(keyToOrder(commands));
    }

    public Mono save1(String name, BigDecimal price, Duration duration, String description) {

        Maybe<Product> product =  productClient.find(name);
        if (product!=null) {
            product.subscribe(beer -> System.out.println("Current product Object -->>>" + beer.getName()));
        } else {
            System.out.println("ERRRRR");
        }
        ZonedDateTime expiryDate = ZonedDateTime.now().plus(duration);
        return Mono.from(product.toFlowable()).flatMap(productInstance -> {
                Orders  order = new Orders(productInstance, description, price);
                Map<String, String> data = dataOf(price, description, order.getCurrency());

            String key = productInstance.getName();
            RedisReactiveCommands<String, String> redisApi = redisConnection.reactive();
            Mono<Orders> o =  redisApi.hmset(key,data)
                    .flatMap(success-> redisApi.expireat(key, expiryDate.toEpochSecond() ))
                    .map(ok -> order) ;
            System.out.println("-------------->"+o.subscribe());
            return o;
        });


    }

    public Mono<Orders> save(
            String name,
            BigDecimal price,
            Duration duration,
            String description) {

        System.out.println("Attempting to save ->>>"+name);



        //System.out.println("Attempting to save <<<<<<<<<<<<<<<<<<<<<<<<<->>> "+p);
        return Mono.from(productClient.find(name).toFlowable())
                .flatMap(productInstance -> {
                    System.out.println("--------------------------->"+productInstance.getName());
                    ZonedDateTime expiryDate = ZonedDateTime.now().plus(duration);
                    Orders  order = new Orders(
                            productInstance,
                            description,
                            price
                    );
                    Map<String, String> data = dataOf(price, description, order.getCurrency());

                    String key = productInstance.getName();
                    RedisReactiveCommands<String, String> redisApi = redisConnection.reactive();
                    Mono<Orders> o =  redisApi.hmset(key,data)
                            .flatMap(success-> redisApi.expireat(key, expiryDate.toEpochSecond() ))
                            .map(ok -> order) ;
                    System.out.println("-------------->"+o.subscribe());
                    return o;
                });
    }

    private Map<String, String> dataOf(BigDecimal price, String description, Currency currency) {
        Map<String, String> data = new LinkedHashMap<>(4);
        data.put("currency", currency.getCurrencyCode());
        data.put("price", price.toString());
        data.put("description" ,description);
        return data;
    }

    private Function<String, Mono<? extends Orders>> keyToOrder(RedisReactiveCommands<String, String> commands) {
        return key -> {
            Flux<KeyValue<String, String>> values = commands.hmget(key, "price", "description");
            Map<String, String> map = new HashMap<>(3);
            return values.reduce(map, (all, keyValue) -> {
                all.put(keyValue.getKey(), keyValue.getValue());
                return all;
            })
                    .map(ConvertibleValues::of)
                    .flatMap(entries -> {
                        String description = entries.get("description", String.class).orElseThrow(() -> new IllegalStateException("No description"));
                        BigDecimal price = entries.get("price", BigDecimal.class).orElseThrow(() -> new IllegalStateException("No price"));
                        Flowable<Product> findItemFlowable = productClient.find(key).toFlowable();

                        return Mono.from(findItemFlowable).map(product -> new Orders(product, description, price));
                    });
        };
    }
}
