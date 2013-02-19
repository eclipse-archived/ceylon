package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Method;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

@Ceylon(major = 4)
@Method
public final class array_ {

    private array_() {}
    
    @TypeParameters(@TypeParameter(value="Element"))
    @TypeInfo("ceylon.language::Array<Element>")
    public static <Element> Array<Element> array(@Ignore TypeDescriptor $reifiedElement, 
    @Name("elements")
    @TypeInfo("ceylon.language::Iterable<Element,ceylon.language::Null>")
    final ceylon.language.Iterable<? extends Element,? extends java.lang.Object> elements) {
        return array($reifiedElement, null, elements);
    }
    
    @Ignore
    public static <Element> Array<Element> array(TypeDescriptor $reifiedElement, 
            Class typeClass,
            final ceylon.language.Iterable<? extends Element,? extends java.lang.Object> elements) {
        return Array.<Element>instance($reifiedElement, typeClass, elements);
    }
    
    @Ignore
    public static <Element> Array<Element> array(TypeDescriptor $reifiedElement) {
        return Array.<Element>instance($reifiedElement, (Class)null, 0, null);
    }
    
    @Ignore
    public static <Element> Array<Element> array(TypeDescriptor $reifiedElement, Class typeClass) {
        return Array.<Element>instance($reifiedElement, typeClass, 0, null);
    }
            
}
