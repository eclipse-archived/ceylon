import org.apache.camel.impl { DefaultCamelContext }
import org.apache.camel.component.jetty { JettyHttpComponent }
import org.apache.camel.builder { RouteBuilder }
import java.lang { Thread { currentThread } }

"Run the module `simple`."
shared void run() {
  print("Start Camel");
  value context = DefaultCamelContext();
  context.addComponent("jetty", JettyHttpComponent());
  object routeBuilder extends RouteBuilder() {
    shared actual void configure() {
      from("jetty:http://localhost:8080").log("got request");
    }
  }
  context.addRoutes(routeBuilder);
  context.start();
  currentThread().sleep(10000);
  print("Stop Camel");
}
