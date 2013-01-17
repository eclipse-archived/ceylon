package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Method;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.Sequenced;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;

@Ceylon(major = 4)
@Method
public final class array_ {

    private array_() {}
    
    @TypeParameters(@TypeParameter(value="Element"))
    @TypeInfo("ceylon.language::Array<Element>")
    public static <Element> Array<Element> array(
    @Name("elements")
    @Sequenced
    @TypeInfo("ceylon.language::Sequential<Element>")
    final ceylon.language.Sequential<? extends Element> elements) {
        return array(null, elements);
    }
    
    @Ignore
    public static <Element> Array<Element> array(
            Class typeClass,
            final ceylon.language.Sequential<? extends Element> elements) {
        return Array.<Element>instance(typeClass, elements);
    }
    
    @Ignore
    public static <Element> Array<Element> array() {
        return Array.<Element>instance((Class)null, 0, null);
    }
    
    @Ignore
    public static <Element> Array<Element> array(Class typeClass) {
        return Array.<Element>instance(typeClass, 0, null);
    }
            
}
