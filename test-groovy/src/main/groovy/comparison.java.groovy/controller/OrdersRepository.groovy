package comparison.java.groovy.controller

import comparison.java.groovy.client.ProductClient
import comparison.java.groovy.domain.Orders
import comparison.java.groovy.view.Product
import io.lettuce.core.KeyValue
import io.lettuce.core.api.StatefulRedisConnection
import io.lettuce.core.api.reactive.RedisReactiveCommands
import io.micronaut.core.convert.value.ConvertibleValues
import io.reactivex.Flowable
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

import javax.inject.Inject
import java.time.Duration
import java.time.ZonedDateTime
import java.util.function.Function

class OrdersRepository {

    private static final Logger LOG = LoggerFactory.getLogger(this.class)

    @Inject
    private final StatefulRedisConnection<String, String> redisConnection

    @Inject
    private final ProductClient productClient

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

    /**
     * By default the RXJAVA Flowable -> { } elements break groovy code -
     *
     * Groovy does not like ->
     *
     */
    public Mono<Orders> save(
            String name,
            BigDecimal price,
            Duration duration,
            String description) {
        System.out.println("Attempting to save "+name);
        return Mono.from(productClient.find(
                name
        ).toFlowable())
                .flatMap({ productInstance ->
            ZonedDateTime expiryDate = ZonedDateTime.now().plus(duration)
            Orders order = new Orders(productInstance, description, price)
            Map<String, String> data = dataOf(price, description, order.getCurrency())

            String key = petInstance.getName();
            RedisReactiveCommands<String, String> redisApi = redisConnection.reactive()
            return redisApi.hmset(key, data)
                    .flatMap({ success -> redisApi.expireat(key, expiryDate.toEpochSecond()) })
                    .map({ ok -> order })
        })
    }

    private Map<String, String> dataOf(BigDecimal price, String description, Currency currency) {
        Map data = [:]
        data.currency = currency.getCurrencyCode()
        data.price = price as String
        data.description = description
        return data
    }

    /**
     * Broken conversion java groovy / now working but not sure if it is what is supposed to be and do :)
     * @param commands
     * @return
     */
    private Function<String, Mono<? extends Orders>> keyToOrder(RedisReactiveCommands<String, String> commands) {
        Flux<KeyValue<String, String>> values = commands.hmget(key, "price", "description")
            Map<String, String> map = new HashMap<>(3)
            values.reduce({all, keyValue->
                all.put(keyValue.getKey(), keyValue.getValue())
                return all
        }).map({entries -> ConvertibleValues.of(entries)})
                    .flatMap({entries ->  bindEntry(entries)})
        return values.key
    }

    private Mono<Orders> bindEntry(entries) {
        String description = entries.get("description", String.class).orElseThrow({ new IllegalStateException("No description")})
        BigDecimal price = entries.get("price", BigDecimal.class).orElseThrow({new IllegalStateException("No price")})
        Flowable<Product> findItemFlowable = productClient.find(key).toFlowable()
        return Mono.from(findItemFlowable).map({product -> new Orders(product, description, price)})

    }

}
