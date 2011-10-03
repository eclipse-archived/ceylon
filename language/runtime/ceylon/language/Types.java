package ceylon.language;

import java.util.Map;
import java.util.HashMap;

class Types
{
    private static Map<Class, Type> classes = new HashMap<Class, Type>();

    static synchronized Type create(Class klass) {
        Type t = classes.get(klass);
        if (t != null)
            return t;
        t = new Type(klass);
        classes.put(klass, t);
        return t;
    }

}
