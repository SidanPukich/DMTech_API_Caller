package student;

import org.apache.camel.Predicate;
import org.apache.camel.builder.PredicateBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;

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
        from("file:src/data?noop=true&include=.*.xml")
                .choice()
                    .when(personLondonxpath)
                        .log("UK message")
                        .transform().simple("foo")
                        .to("file:target/messages/uk?fileName=uk.txt")
                    .when(personKarlsruhexpath)
                        .log("DE message")
                        .to("file:target/messages/de")
                    .otherwise()
                        .log("Other message")
                        .to("file:target/messages/others");

        from("file:src/data?noop=true&include=.*.json")
                .choice()
                    .when(personLondonjsonpath)
                        .log("UK message")
                        .to("file:target/messages/uk")
                    .when(personKarlsruhejsonpath)
                        .log("DE message")
                        .to("file:target/messages/de")
                    .otherwise()
                        .log("Other message")
                        .to("file:target/messages/others");

//        restConfiguration().component("netty-http").host("localhost").port(80).bindingMode(RestBindingMode.auto);
//
//        from("timer:mytimer?repeatCount=1")
//                .to("direct:covid-stat");
////
//        from("direct:covid-stat")
//                .log("sending request")
//                .to("https://covid-api.mmediagroup.fr/v1/cases?country=Germany&httpMethod=GET")
//                .log("${body}");
    }

}
