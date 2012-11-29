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
public final class entries_ {
    
    private entries_() {}
    
    @TypeParameters(@TypeParameter("Element"))
    @TypeInfo("ceylon.language::Iterable<ceylon.language::Entry<ceylon.language::Integer,Element&ceylon.language::Object>>")
    public static <Element> Iterable<? extends Entry<? extends Integer, ? extends Element>> entries(@Name("elements")
    @Sequenced @TypeInfo("ceylon.language::Sequential<Element>")
    final ceylon.language.Sequential<? extends Element> elements) {
        return elements.getIndexed();
    }
    @Ignore
    public static <Element> Sequential<? extends Entry<? extends Integer, ? extends Element>> entries() {
        return (Sequential) empty_.getEmpty$();
    }
}
