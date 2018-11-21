package comparison.java.groovy.controller;

import com.fasterxml.aalto.AsyncByteArrayFeeder;
import com.fasterxml.aalto.AsyncInputFeeder;
import com.fasterxml.aalto.AsyncXMLStreamReader;
import com.fasterxml.aalto.stax.InputFactoryImpl;
import comparison.java.groovy.domain.Orders;
import comparison.java.groovy.view.IncommingOrders;
import io.micronaut.http.HttpResponse;
import io.netty.buffer.CompositeByteBuf;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.inject.Singleton;
import javax.xml.stream.events.XMLEvent;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class TestXml {


    public  Mono<List<Orders>> parseXml(  OrdersRepository ordersRepository,HttpResponse response ) {
       List<Orders> input =new ArrayList<>();
        List<IncommingOrders> orders = new ArrayList<>();
        try {
            CompositeByteBuf content = (CompositeByteBuf) response.body();
            byte[] bytes = new byte[content.readableBytes()];
            int readerIndex = content.readerIndex();
            content.getBytes(readerIndex, bytes);
            String read = new String(bytes);
            //System.out.println("RES"+ read);

                byte[] input_part1 = read.getBytes("UTF-8");

                AsyncXMLStreamReader<AsyncByteArrayFeeder> asyncXMLStreamReader = new InputFactoryImpl().createAsyncFor(input_part1);
                final AsyncInputFeeder asyncInputFeeder = asyncXMLStreamReader.getInputFeeder();
                int inputPtr = 0; // as we feed byte at a time
                int type = 0;

                int bufferFeedLength = 1; // feed 1 byte at a time to the asynchronous parser
                boolean recordItem=false;

                IncommingOrders order = new IncommingOrders();
                String lastItem="";
                do {
                    //keep looping till event is complete
                    while ((type = asyncXMLStreamReader.next()) == AsyncXMLStreamReader.EVENT_INCOMPLETE) {
                        byte[] buffer = new byte[]{input_part1[inputPtr]};
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
                                    break;
                                case "description":
                                    order.setDescription(asyncXMLStreamReader.getText());
                                    break;
                                case "name":
                                    order.setName(asyncXMLStreamReader.getText());
                                    break;
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

                } while (type != XMLEvent.END_DOCUMENT);

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


    public Flux runMe() {
        System.out.println("Run me is called");
        ArrayList<String> input =  new ArrayList<String>();
        try {
            //byte[] XML = "<html>Very <b>simple</b> input document!</html>";
            byte[] input_part1 =  ("<?xml version=\"1.0\" encoding=\"utf-8\"?><breakfast_menu>" +
                    "<food>" +
                        "<name>Belgian Waffles</name>" +
                        "<price>$5.95</price>" +
                        "<description>Two of our famous Belgian Waffles with plenty of real maple syrup</description>" +
                        "<calories>650</calories>" +
                    "</food>" +
                    "<food>" +
                    "<name>Strawberry Belgian Waffles</name>" +
                    "<price>$7.95</price>" +
                    "<description>Light Belgian waffles covered with strawberries and whipped cream - yes please can I have some more</description>" +
                    "<calories>950</calories>" +
                    "</food>" +
                    "</breakfast_menu>").getBytes("UTF-8");
            AsyncXMLStreamReader<AsyncByteArrayFeeder> asyncXMLStreamReader = new InputFactoryImpl().createAsyncFor(input_part1);
            final AsyncInputFeeder asyncInputFeeder = asyncXMLStreamReader.getInputFeeder();
            int inputPtr = 0; // as we feed byte at a time
            int type = 0;

            int bufferFeedLength = 1; // feed 1 byte at a time to the asynchronous parser

            do{
                //keep looping till event is complete
                while((type = asyncXMLStreamReader.next()) == AsyncXMLStreamReader.EVENT_INCOMPLETE){
                    byte[] buffer = new byte[]{input_part1[inputPtr]};
                    inputPtr++;
                    ((AsyncByteArrayFeeder) asyncInputFeeder).feedInput(buffer, 0, bufferFeedLength);
                    //check for end of input
                    if(inputPtr >= input_part1.length) {
                        asyncInputFeeder.endOfInput();
                    }
                }
                //handle parser event and extract parsed data
                switch (type) {
                    case XMLEvent.START_DOCUMENT:
                        System.out.println("start document");
                        input.add("start document");
                        break;
                    case XMLEvent.START_ELEMENT:
                        System.out.println("start element: " + asyncXMLStreamReader.getName());
                        input.add("start element: " + asyncXMLStreamReader.getName());
                        break;
                    case XMLEvent.CHARACTERS:
                        System.out.println("characters: " + asyncXMLStreamReader.getText());
                        input.add("characters: " + asyncXMLStreamReader.getText());
                        break;
                    case XMLEvent.END_ELEMENT:
                        System.out.println("end element: " + asyncXMLStreamReader.getName());
                        input.add("end element: " + asyncXMLStreamReader.getName());
                        break;
                    case XMLEvent.END_DOCUMENT:
                        System.out.println("end document");
                        input.add("end document");
                        break;
                    default:
                        break;
                }

            }while(type != XMLEvent.END_DOCUMENT);

            asyncXMLStreamReader.close();
        } catch (Exception e) {

        }
        return Flux.just(input);
    }
}
