package comparison.java.groovy.controller

import com.fasterxml.aalto.AsyncByteArrayFeeder
import com.fasterxml.aalto.AsyncInputFeeder
import com.fasterxml.aalto.AsyncXMLStreamReader
import com.fasterxml.aalto.stax.InputFactoryImpl
import comparison.java.groovy.domain.Orders
import comparison.java.groovy.view.IncommingOrders
import io.micronaut.http.HttpResponse
import io.netty.buffer.CompositeByteBuf
import reactor.core.publisher.Mono

import javax.inject.Singleton
import javax.xml.stream.events.XMLEvent
import java.time.Duration

@Singleton
class TestXml {



    public  Mono<List<Orders>> parseXml(OrdersRepository ordersRepository, HttpResponse response ) {
        List<Orders> input =new ArrayList<>();
        List<IncommingOrders> orders = new ArrayList<>();
        try {
            CompositeByteBuf content = (CompositeByteBuf) response.body();
            println "-- content = $content"

            //content.retain();
            byte[] bytes = new byte[content.readableBytes()];
            int readerIndex = content.readerIndex();
            content.getBytes(readerIndex, bytes);
            String read = new String(bytes);
            //System.out.println("RES"+ read);

            /**
             *
             * At the moment not sure why content.getBytes(readerIndex, bytes) causes below stack trace:
             * same identical code in java did appear to give issues but then started to work - not sure why
             *
             * -- content = CompositeByteBuf(freed, components=17)
             * io.netty.util.IllegalReferenceCountException: refCnt: 0
             *         at io.netty.buffer.AbstractByteBuf.ensureAccessible(AbstractByteBuf.java:1446)
             *         at io.netty.buffer.AbstractByteBuf.checkIndex(AbstractByteBuf.java:1376)
             *         at io.netty.buffer.AbstractByteBuf.checkDstIndex(AbstractByteBuf.java:1401)
             *         at io.netty.buffer.CompositeByteBuf.getBytes(CompositeByteBuf.java:854)
             *         at io.netty.buffer.CompositeByteBuf.getBytes(CompositeByteBuf.java:44)
             *         at io.netty.buffer.AbstractByteBuf.getBytes(AbstractByteBuf.java:487)
             *         at io.netty.buffer.CompositeByteBuf.getBytes(CompositeByteBuf.java:1744)
             *         at io.netty.buffer.CompositeByteBuf$getBytes.call(Unknown Source)
             *         at org.codehaus.groovy.runtime.callsite.CallSiteArray.defaultCall(CallSiteArray.java:47)
             *         at org.codehaus.groovy.runtime.callsite.AbstractCallSite.call(AbstractCallSite.java:116)
             *         at org.codehaus.groovy.runtime.callsite.AbstractCallSite.call(AbstractCallSite.java:136)
             *         at comparison.java.groovy.controller.TestXml.parseXml(TestXml.groovy:30)
             *
             *
             */

            byte[] input_part1 = read.getBytes("UTF-8");

            AsyncXMLStreamReader<AsyncByteArrayFeeder> asyncXMLStreamReader = new InputFactoryImpl().createAsyncFor(input_part1);
            final AsyncInputFeeder asyncInputFeeder = asyncXMLStreamReader.getInputFeeder();
            int inputPtr = 0; // as we feed byte at a time
            int type = 0;

            int bufferFeedLength = 1; // feed 1 byte at a time to the asynchronous parser
            boolean recordItem=false;
           // content.release();
            IncommingOrders order = new IncommingOrders();
            String lastItem="";
            while (type != XMLEvent.END_DOCUMENT) {
                //keep looping till event is complete
                while ((type = asyncXMLStreamReader.next()) == AsyncXMLStreamReader.EVENT_INCOMPLETE) {
                    byte[] buffer = [input_part1[inputPtr]] as byte[]
                    inputPtr++;
                    ((AsyncByteArrayFeeder) asyncInputFeeder).feedInput(buffer, 0, bufferFeedLength);
                    //check for end of input
                    if (inputPtr >= input_part1.length) {
                        asyncInputFeeder.endOfInput();
                    }
                }
                //handle parser event and extract parsed data
                switch (type) {
                    case XMLEvent.START_DOCUMENT:
                        //System.out.println("start document");
                        // input.add("start document");

                        break;
                    case XMLEvent.START_ELEMENT:
                        //System.out.println("start element: " + asyncXMLStreamReader.getName());
                        //input.add("start element: " + asyncXMLStreamReader.getName());

                        if (asyncXMLStreamReader.getName().toString()=="order") {
                            recordItem=true;
                            lastItem="";
                            order = new IncommingOrders();
                        } else {
                            if (recordItem) {
                                lastItem=asyncXMLStreamReader.getName().toString();
                            }
                        }

                        break;
                    case XMLEvent.CHARACTERS:
                        //System.out.println("characters: " + asyncXMLStreamReader.getText());
                        //input.add("characters: " + asyncXMLStreamReader.getText());
                        switch (lastItem) {
                            case "price":
                                order.setPrice(new BigDecimal(asyncXMLStreamReader.getText().toString()));
                            break
                            case "description":
                                order.setDescription(asyncXMLStreamReader.getText());
                                break
                            case "name":
                                order.setName(asyncXMLStreamReader.getText());
                                break
                        }
                        break;
                    case XMLEvent.END_ELEMENT:
                        //System.out.println("end element: " + asyncXMLStreamReader.getName());
                        //input.add("end element: " + asyncXMLStreamReader.getName());
                        if (asyncXMLStreamReader.getName().toString()=="order") {
                            recordItem=false;
                            orders.add(order);
                            lastItem="";
                        }
                        break;
                    case XMLEvent.END_DOCUMENT:
                        //System.out.println("end document");
                        // input.add("end document");

                        break;
                    default:
                        break;
                }

            }
            asyncXMLStreamReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("We have "+orders.size());
        for (int a=0; a < orders.size(); a++) {
            IncommingOrders orders1 = orders.get(a);
            //System.out.println("About to save order"+orders1.getName());
            input.add(ordersRepository.save(
                    orders1.getName(), orders1.getPrice(), Duration.ofMinutes(222222), orders1.getDescription()
            ).block());
        }
        return Mono.just(input);
    }
}
