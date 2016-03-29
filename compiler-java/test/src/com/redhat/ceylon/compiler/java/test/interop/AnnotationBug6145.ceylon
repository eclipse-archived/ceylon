import javax.ws.rs { produces }
import javax.ws.rs.core { MediaType { plainText = \iTEXT_PLAIN } }

@nomodel
shared class AnnotationBug6145() {
    
    produces ({ plainText })
    shared String getString() {
        return "Hello!";
    }
}