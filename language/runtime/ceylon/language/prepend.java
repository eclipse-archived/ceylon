package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Method;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;

@Ceylon(major = 2)
@Method
public final class prepend {
    
    private prepend() {}
    
    @TypeParameters(@TypeParameter(value="Element"))
    @TypeInfo("ceylon.language.Sequence<Element>")
    public static <Element> Sequence<? extends Element> prepend(
    @Name("sequence")
    @TypeInfo("ceylon.language.Empty|ceylon.language.Sequence<Element>")
    final Iterable<? extends Element> sequence,
    @Name("element")
    Element element) {
    	ArraySequence<Element> as = new ArraySequence<Element>(element);
        if (sequence.getEmpty()) {
        	return as;
        }
        else {
        	return ((Sequence<Element>) sequence).withLeading(as);
        }
    }
}
