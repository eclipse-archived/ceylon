package ceylon.language;

import com.redhat.ceylon.compiler.java.TypeDescriptor;
import com.redhat.ceylon.compiler.java.language.AbstractIterable;
import com.redhat.ceylon.compiler.java.language.AbstractIterator;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Method;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;

@Ceylon(major = 4)
@Method
public final class arrayOfSize_ {

    private arrayOfSize_() {}

    @TypeParameters(@TypeParameter(value="Element"))
    @TypeInfo("ceylon.language::Array<Element>")
    public static <Element> Array<Element> arrayOfSize(@Ignore TypeDescriptor $reifiedElement, 
    @Name("size")
    @TypeInfo("ceylon.language::Integer")
    final long size,
    @Name("element")
    @TypeInfo("Element")
    final Element element) {
        return arrayOfSize($reifiedElement, null, size, element);
    }
    
    @Ignore
    public static <Element> Array<Element> arrayOfSize(TypeDescriptor $reifiedElement, 
            final Class typeClass,
            final long size,
            final Element element) {
        //TODO: This is horribly inefficient. We should
        //      create an empty array, and then use
        //      Arrays.fill() to populate it!
        return Array.<Element>instance($reifiedElement, typeClass, (int)size, element);
    }
}
