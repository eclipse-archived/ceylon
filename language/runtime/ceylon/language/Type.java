package ceylon.language;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class Type
{
    final Class klass;

    private static Map<Class, Sequence> annotations
        = new HashMap<Class, Sequence>();

    public <T> Sequence<T> annotations(java.lang.Class<T> annoType) {
        // System.out.println("lookup: " + annoType);
        Sequence seq = annotations.get(annoType);
        // System.out.println("found: " + seq);
        return (Sequence<T>) seq;
    }

    Type(Class klass) {
        this.klass = klass;
    }

    public <T> void addAnnotations(java.lang.Class<T> unused,
                                   Sequence<T> annos)
    {
        java.lang.Class annoType = annos.value(Integer.instance(0)).getClass();
        Sequence<T> seq = (Sequence<T>)annotations.get(annoType);
        if (seq == null) {
            annotations.put(annoType, annos);
        } else {
/*            annotations.put(annoType,
                            ((ArrayList<T>)seq).addAll((ArrayList<T>)annos));
                            */
        	throw new RuntimeException("Not implemented");
        }
    }

    public String asString() {
        return String.instance(klass.toString());
    }
}
