package comparison.java.groovy.controller

import comparison.java.groovy.domain.Orders
import comparison.java.groovy.view.Product
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.reactivex.Flowable
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

import javax.inject.Inject
import java.time.Duration

@Controller("/orders")
class OrdersController {


    @Inject
    OrdersRepository ordersRepository

    @Inject
    Duration offerDelay

    /**
     * A non-blocking infinite JSON stream of offers that change every 10 seconds
     * @return A {@link reactor.core.publisher.Flux} stream of JSON objects
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
        List<String> sequences = new ArrayList<>();

        for (int a=0; a < 1000; a++) {
            sequences.add("a"+a);
            //sequences.add(new SequenceTest("Name: "+a.toString(),new Date()));
        }
        Flowable.fromIterable(sequences)
                .forEach({k-> this.save(k, new BigDecimal(12.22),Duration.ofMinutes(222222),"Some description "+k).subscribe()});

        returnordersRepository.all();
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
