package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Method;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.Sequenced;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;

@Ceylon(major = 1)
@Method
public final class entries {
    
    private entries() {}
    
    @TypeParameters(@TypeParameter("Element"))
    @TypeInfo("ceylon.language.Iterable<ceylon.language.Entry<ceylon.language.Integer,Element&ceylon.language.Object>>")
    public static <Element> Iterable<? extends Entry<? extends Integer,? extends Element>> entries(@Name("elements")
    @Sequenced @TypeInfo("ceylon.language.Iterable<Element>")
    final ceylon.language.Iterable<? extends Element> elements) {
        return elements.getIndexed();
    }
    @Ignore
    public static <Element> Iterable<? extends Entry<? extends Integer,? extends Element>> entries() {
        return (Iterable) $empty.getEmpty();
    }
}
