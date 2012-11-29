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
public final class coalesce_ {

    private coalesce_() {}

    @TypeParameters(@TypeParameter("Element"))
    @TypeInfo("ceylon.language::Iterable<Element&ceylon.language::Object>")
    public static <Element> Iterable<? extends Element> coalesce(
    @Name("values") @Sequenced
    @TypeInfo("ceylon.language::Sequential<Element>")
    final ceylon.language.Sequential<? extends Element> values) {
        return values.getCoalesced();
    }
    @Ignore
    public static <Element> Sequential<? extends Element> coalesce() {
        return (Sequential<? extends Element>) empty_.getEmpty$();
    }
}
