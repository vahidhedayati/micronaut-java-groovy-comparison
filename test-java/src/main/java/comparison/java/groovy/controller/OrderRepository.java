package comparison.java.groovy.controller;
import comparison.java.groovy.client.ItemClient;
import comparison.java.groovy.view.Item;
import io.lettuce.core.KeyValue;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.reactive.RedisReactiveCommands;
import io.reactivex.Flowable;
import io.micronaut.core.convert.value.ConvertibleValues;

import io.reactivex.functions.Function;
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
public class OrderRepository {

    private static final Logger LOG = LoggerFactory.getLogger(OrderRepository.class);

    private final StatefulRedisConnection<String, String> redisConnection;
    private final ItemClient itemClient;


    public OrderRepository(ItemClient itemClient,StatefulRedisConnection<String, String> redisConnection) {
        this.itemClient=itemClient;
        this.redisConnection = redisConnection;
    }

    /**
     * @return Returns all current offers
     */
    public Mono<List<Orders>> all() {
        RedisReactiveCommands<String, String> commands = redisConnection.reactive();

        return commands.keys("*").flatMap(keyToOffer(commands)).collectList();
    }
    /**
     * @return Obtain a random offer or return {@link Mono#empty()} if there is none
     */
    public Mono<Orders> random() {
        RedisReactiveCommands<String, String> commands = redisConnection.reactive();
        return commands.randomkey().flatMap(keyToOffer(commands));
    }

    private Function<String, Mono<? extends Orders>> keyToOffer(RedisReactiveCommands<String, String> commands) {
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
                        return Mono.from(findItemFlowable).map(pet -> new Orders(pet, description, price));
                    });
        };
    }
}
