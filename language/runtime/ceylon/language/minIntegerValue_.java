package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Attribute;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;

@Ceylon(major = 4)
@Attribute
public final class minIntegerValue_ {

    @TypeInfo("ceylon.language::Integer")
    public static Integer getMinIntegerValue$() {
        return Integer.instance(Long.MIN_VALUE);
    }
}
