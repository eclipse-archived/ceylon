package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Attribute;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;

@Ceylon(major = 4)
@Attribute
public final class maxIntegerValue_ {

    @TypeInfo("ceylon.language::Integer")
    public static Integer getMaxIntegerValue$() {
        return Integer.instance(Long.MAX_VALUE);
    }
}
