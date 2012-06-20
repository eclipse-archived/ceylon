package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Method;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;

@Ceylon(major = 1)
@Method
public final class $arrayOfNone {

    private $arrayOfNone() {}

    @TypeParameters(@TypeParameter(value="Element"))
    @TypeInfo("ceylon.language.Array<Element>&None<Element>")
    public static <Element> Array<Element> arrayOfNone() {
        return new ArrayOfNone<Element>(null);
    }
    
    @Ignore
    public static <Element> Array<Element> arrayOfNone(final Class<Element> typeClass) {
        return new ArrayOfNone<Element>(typeClass);
    }
}
