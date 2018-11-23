package comparison.java.groovy.client;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.FullNettyClientHttpResponse;
import io.micronaut.http.client.annotation.Client;
import io.netty.buffer.CompositeByteBuf;
import io.reactivex.Single;

@Client(id = "streamer" , path="/stream")
public interface StreamClient {

    @Get("/test")
    Single<HttpResponse<CompositeByteBuf>> test();

}
