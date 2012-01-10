package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Class;
import com.redhat.ceylon.compiler.java.metadata.Ignore;

@Ignore
@Class
public class JavaObjectArraySequence<Element> extends ArraySequence<Element> {
    
    public JavaObjectArraySequence(Element... array) {
        super(array);
    }

    public static <Element> JavaObjectArraySequence<Element> instance(Element[] array) {
        return new JavaObjectArraySequence<Element>(array);
    }
    
    /*
    @TypeInfo("ceylon.language.Empty|ceylon.language.Sequence<Element>")
    public static <Element> java.lang.Object instance(Element[] array) {
        if (array.length > 0) {
            return new JavaObjectArraySequence<Element>(array);
        } else {
            return $empty.getEmpty();
        }
    }
    */
}