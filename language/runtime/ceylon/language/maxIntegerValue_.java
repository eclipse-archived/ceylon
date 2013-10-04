package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Attribute;

@Ceylon(major = 5) @Attribute
public class maxIntegerValue_ {

    public static Integer get_() {
        return Integer.instance(Long.MAX_VALUE);
    }
}
