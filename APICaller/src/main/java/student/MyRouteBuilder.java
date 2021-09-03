package student;

import org.apache.camel.Predicate;
import org.apache.camel.builder.RouteBuilder;

/**
 * A Camel Java DSL Router
 */
public class MyRouteBuilder extends RouteBuilder {

    /**
     * Let's configure the Camel routing rules using Java code...
     */
    public void configure() {
        Predicate personLondon = xpath("/person/city = 'London'");
        Predicate personKarlsruhe = xpath("/person/city = 'Karlsruhe'");
        // here is a sample which processes the input files
        // (leaving them in place - see the 'noop' flag)
        // then performs content based routing on the message using XPath
        from("file:src/data?noop=true")
            .choice()
                .when(personLondon)
                    .log("UK message")
                    .transform().simple("foo")
                    .to("file:target/messages/uk?fileName=uk.txt")
                .when(personKarlsruhe)
                    .log("DE message")
                    .to("file:target/messages/de")
                .otherwise()
                    .log("Other message")
                    .to("file:target/messages/others");
    }

}
