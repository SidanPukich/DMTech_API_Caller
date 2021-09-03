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
        Predicate personLondonxpath = xpath("/person/city = 'London'");
        Predicate personLondonjsonpath = jsonpath("$..person[?(@.city=='London')]");
//        Predicate personLondon = PredicateBuilder.or(personLondonxpath, personLondonjsonpath);

        Predicate personKarlsruhexpath = xpath("/person/city = 'Karlsruhe'");
        Predicate personKarlsruhejsonpath = jsonpath("$..person[?(@.city=='Karlsruhe')]");
//        Predicate personKarlsruhe = PredicateBuilder.or(personKarlsruhexpath, personKarlsruhejsonpath);


        // here is a sample which processes the input files
        // (leaving them in place - see the 'noop' flag)
        // then performs content based routing on the message using XPath
        from("file:src/data?noop=true")
                .choice()
                    .when(personKarlsruhejsonpath)
                        .log("UK message")
                        .to("file:target/messages/uk")
                    .when(personLondonxpath)
                        .log("UK message")
                        .transform().simple("foo")
                        .to("file:target/messages/uk?fileName=uk.txt")
                    .when(personKarlsruhejsonpath)
                        .log("DE message")
                        .to("file:target/messages/de")
                    .when(personKarlsruhexpath)
                        .log("DE message")
                        .to("file:target/messages/de")
                    .otherwise()
                        .log("Other message")
                        .to("file:target/messages/others");
    }

}
