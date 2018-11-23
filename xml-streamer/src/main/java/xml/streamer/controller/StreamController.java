package xml.streamer.controller;

import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.reactivex.Single;
import xml.streamer.view.Orders;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Taken from :
 * https://dzone.com/articles/micronaut-mastery-return-response-based-on-http-ac
 *
 *
 * htmlClient used in test-java to call streamController when it processes the order -
 * which in turn returns an xml response back to the caller - in which it processes the response that it already had
 * back from xml sent back in this site - its all terribly sketched out - but was more about processing xml that it would receive
 */

@Controller("/stream")
public class StreamController {

    @Get("/basicTest")
    public Single<HttpResponse<?>> basicTest() {
        System.out.println("TEST IS CALLED");
        return Single.just(HttpResponse.ok("<current_orders>"+encodeAsXml(new Orders("a1", "a1", new BigDecimal(12.22)))+"</current_orders>").contentType(MediaType.APPLICATION_XML_TYPE));
    }

    @Get("/test")
    public Single<HttpResponse<?>> test() {

        List<String> sequences = new ArrayList<>();
        for (int a=0; a < 1000; a++) {
            sequences.add(encodeAsXml(new Orders("a"+a, "a"+a, new BigDecimal(12.22))));
        }
        //System.out.println("About to test:: "+String.join(",",sequences));
        return Single.just(HttpResponse.ok("<orders>\n"+String.join(" ",sequences)+"</orders>\n")
              .contentType(MediaType.APPLICATION_XML_TYPE));

        //System.out.println("TEST IS CALLED");

        //return Single.just(HttpResponse.ok("<current_orders>"+encodeAsXml(new Orders("a1", "a1", new BigDecimal(12.22)))+"</current_orders>").contentType(MediaType.APPLICATION_XML_TYPE));

        //return HttpResponse.ok(  encodeAsXml(new Orders("a", "a description", new BigDecimal(12.22)))).contentType(MediaType.APPLICATION_XML_TYPE);
    }



    @Get("/process")
    public HttpResponse<?> process(final Orders orders) {
            final String xml = encodeAsXml(orders);
            // Return response and set content type
            // to "application/xml".
            return HttpResponse.ok(xml)
                    .contentType(MediaType.APPLICATION_XML_TYPE);
    }

    @Get("/sample")
    public HttpResponse<?> sample(final HttpHeaders headers) {
        // Simple object to be returned from this method either
        // as XML or JSON, based on the HTTP Accept header.
        final Orders orders = new Orders("test","test",new BigDecimal(22.22));
        // Check if HTTP Accept header is "application/xml".
        if (headerAcceptXml(headers)) {
            // Encode messages as XML.
            final String xml = encodeAsXml(orders);
            // Return response and set content type
            // to "application/xml".
            return HttpResponse.ok(xml)
                    .contentType(MediaType.APPLICATION_XML_TYPE);
        }
        // Default response as JSON.
        return HttpResponse.ok(orders);
    }

    /**
     * Check HTTP Accept header with value "application/xml"
     * is sent by the client.
     *
     * @param headers Http headers sent by the client.
     * @return True if the Accept header contains "application/xml".
     */
    private boolean headerAcceptXml(final HttpHeaders headers) {
        return headers.accept().contains(MediaType.APPLICATION_XML_TYPE);
    }

    /**
     * Very simple way to create XML String with message content.
     *
     * @param orders Message to be encoded as XML String.
     * @return XML String with message content.
     */
    private String encodeAsXml(final Orders orders) {
        return String.format("<order><name>%s</name><description>%s</description><price>%s</price></order>", orders.getName(), orders.getDescription(), orders.getPrice());
    }
}
