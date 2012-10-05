package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Method;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.Sequenced;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;

@Ceylon(major = 3)
@Method
public final class elements_ {
    
    private elements_() {
    }
    
    @TypeInfo("ceylon.language.Iterable<Element>")
    @TypeParameters(@TypeParameter(value="Element"))
    public static <Element> Iterable<? extends Element> elements(@Name("elements") 
    @Sequenced @TypeInfo("ceylon.language.Iterable<Element>")
    final Iterable<? extends Element> elements) {
        return elements;
    }
    @Ignore
    public static <Element> Iterable<? extends Element> elements() {
        return (Iterable<? extends Element>) empty_.getEmpty$();
    }
}
