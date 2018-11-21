package comparison.java.groovy.client

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Get
import io.micronaut.http.client.annotation.Client

@Client(id = "streamer" , path="/stream")
public interface StreamClient {

    //have tried https://github.com/micronaut-projects/micronaut-core/issues/519
    //HttpResponse<byte[]> this breaks other things
    @Get("/test")
    HttpResponse<?> test();
}
