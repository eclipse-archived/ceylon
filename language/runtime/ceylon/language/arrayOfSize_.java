package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Method;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

@Ceylon(major = 7)
@Method
public final class arrayOfSize_ {
    
    private arrayOfSize_() {}
	
    private static long maxArraySize = runtime_.get_().getMaxArraySize().value;
    
    @TypeParameters(@TypeParameter(value="Element"))
    @TypeInfo("ceylon.language::Array<Element>")
    public static <Element> Array<Element> arrayOfSize(@Ignore final TypeDescriptor $reifiedElement, 
            @Name("size")
            @TypeInfo("ceylon.language::Integer")
            long size,
            @Name("element")
            @TypeInfo("Element")
            final Element element) {
		if (size>maxArraySize) {
    		throw new AssertionError("array size must be smaller than " + maxArraySize);
    	}
		if (size<0) {
    		throw new AssertionError("array size must not be negative");
    	}
        return new Array<Element>($reifiedElement, (int)size, element);
    }
    
}
