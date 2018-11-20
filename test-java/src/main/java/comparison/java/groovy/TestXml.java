package comparison.java.groovy;

import com.fasterxml.aalto.AsyncByteArrayFeeder;
import com.fasterxml.aalto.AsyncInputFeeder;
import com.fasterxml.aalto.AsyncXMLStreamReader;
import com.fasterxml.aalto.stax.InputFactoryImpl;

import static javax.xml.stream.XMLStreamConstants.END_DOCUMENT;

public class TestXml {

    void runMe() {
        try {
            //byte[] XML = "<html>Very <b>simple</b> input document!</html>";
            byte[] input_part1 = "<root>val".getBytes("UTF-8");
            AsyncXMLStreamReader<AsyncByteArrayFeeder> parser = new InputFactoryImpl().createAsyncFor(input_part1);
            final AsyncInputFeeder feeder = parser.getInputFeeder();
            int inputPtr = 0; // as we feed byte at a time
            int type = 0;

            do {
                // May need to feed multiple "segments"
                while ((type = parser.next()) == AsyncXMLStreamReader.EVENT_INCOMPLETE) {
                   // feeder.feedInput(buf, inputPtr++, 1);
                   // if (inputPtr >= input_part1.length) { // to indicate end-of-content (important for error handling)
                        feeder.endOfInput();
                   // }
                }
                // and once we have full event, we just dump out event type (for now)
                System.out.println("Got event of type: "+type+" "+parser.getText()+" "+parser.getLocalName());
                // could also just copy event as is, using Stax, or do any other normal non-blocking handling:
                // xmlStreamWriter.copyEventFromReader(asyncReader, false);
            } while (type != END_DOCUMENT);
            parser.close();
        } catch (Exception e) {

        }

    }
}
