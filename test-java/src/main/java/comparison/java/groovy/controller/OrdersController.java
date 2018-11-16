package comparison.java.groovy.controller;

import comparison.java.groovy.domain.Orders;
import io.micronaut.context.annotation.Value;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.reactivex.Flowable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Controller("/orders")
public class OrdersController {

    private final OrdersRepository ordersRepository;
    private final Duration offerDelay;

    public OrdersController(OrdersRepository ordersRepository, @Value("${offers.delay:3s}") Duration offerDelay) {
        this.ordersRepository = ordersRepository;
        this.offerDelay = offerDelay;
    }

    /**
     * A non-blocking infinite JSON stream of offers that change every 10 seconds
     * @return A {@link Flux} stream of JSON objects
     */
    @Get(uri = "/", produces = MediaType.APPLICATION_JSON_STREAM)
    public Flux<Orders> current() {
        return ordersRepository
                .random()
                .repeat(100)
                .delayElements(offerDelay);
    }

    /**
     * A non-blocking infinite JSON stream of offers that change every 10 seconds
     * @return A {@link Flux} stream of JSON objects
     */
    @Get(uri = "/all")
    public Mono<List<Orders>> all() {
        return ordersRepository.all();
    }


    @Get(uri = "/test")
    public Mono<List<Orders>> test() {
        List<BigDecimal> sequences = new ArrayList<>();
        BigDecimal price=new BigDecimal("0.11");
        for (int a=0; a < 1000; a++) {
            sequences.add(new BigDecimal(price.intValue()+a));
            //sequences.add(new SequenceTest("Name: "+a.toString(),new Date()));
        }
        Flowable.fromIterable(sequences)
                .forEach(k-> this.save("Executive Chair", k,Duration.ofMinutes(222222),"Some description "+k).subscribe());

        return ordersRepository.all();
    }
    /**
     * Consumes JSON and saves a new offer
     *
     * @param name the name of Item
     * @param price The price of the offer
     * @param duration The duration of the offer
     * @param description The description of the offer
     * @return The offer or 404 if no pet exists for the offer
     */
    @Post("/")
    public Mono<Orders> save(
            String name,
            BigDecimal price,
            Duration duration,
            String description) {
        return ordersRepository.save(
                name, price, duration, description
        );
    }
}
