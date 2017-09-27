import org.apache.camel.builder { RouteBuilder }

shared void bugJvm1290() {
  object routeBuilder extends RouteBuilder() {
    shared actual void configure() {
      from("jetty:http://localhost:8080").log("got request");
    }
  }
  Object o = {routeBuilder};
  assert(is Iterable<RouteBuilder> o);
}
