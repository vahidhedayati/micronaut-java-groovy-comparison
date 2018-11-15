package comparison.java.groovy.controller;
import comparison.java.groovy.client.ItemClient;
import comparison.java.groovy.view.Item;
import io.lettuce.core.KeyValue;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.reactive.RedisReactiveCommands;
import io.reactivex.Flowable;
import io.micronaut.core.convert.value.ConvertibleValues;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.function.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import comparison.java.groovy.domain.Orders;

import javax.inject.Singleton;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

@Singleton
public class OrdersRepository {

    private static final Logger LOG = LoggerFactory.getLogger(OrdersRepository.class);

    private final StatefulRedisConnection<String, String> redisConnection;
    private final ItemClient itemClient;


    public OrdersRepository(ItemClient itemClient, StatefulRedisConnection<String, String> redisConnection) {
        this.itemClient=itemClient;
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

    public Mono<Orders> save(
            String slug,
            BigDecimal price,
            Duration duration,
            String description) {

        return Mono.from(itemClient.find(
                slug
        ).toFlowable())
                .flatMap(itemInstance -> {
                    ZonedDateTime expiryDate = ZonedDateTime.now().plus(duration);
                    Orders  order = new Orders(
                            itemInstance,
                            description,
                            price
                    );
                    Map<String, String> data = dataOf(price, description, order.getCurrency());

                    String key = itemInstance.getName();
                    RedisReactiveCommands<String, String> redisApi = redisConnection.reactive();
                    return redisApi.hmset(key,data)
                            .flatMap(success-> redisApi.expireat(key, expiryDate.toEpochSecond() ))
                            .map(ok -> order) ;
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
                        Flowable<Item> findItemFlowable = itemClient.find(key).toFlowable();
                        return Mono.from(findItemFlowable).map(item -> new Orders(item, description, price));
                    });
        };
    }
}
