package comparison.java.groovy.client;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.annotation.Client;

@Client(id = "streamer" , path="/stream")
public interface StreamClient {

    @Get("/test")
    HttpResponse<?> test();

}
