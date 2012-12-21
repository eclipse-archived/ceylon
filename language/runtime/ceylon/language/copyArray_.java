package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Method;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;

@Ceylon(major = 3)
@Method
public final class copyArray_ {

    private copyArray_() {}

    @TypeParameters(@TypeParameter(value="Element"))
    public static <Element> void copyArray(
    @TypeInfo("ceylon.language::Array<Element>")
    @Name("source")
    final Array<Element> source,
    @TypeInfo("ceylon.language::Array<Element>")
    @Name("target")
    final Array<Element> target,
    @Name("from")
    @TypeInfo("ceylon.language::Integer")
    final long from,
    @Name("to")
    @TypeInfo("ceylon.language::Integer")
    final long to,
    @Name("length")
    @TypeInfo("ceylon.language::Integer")
    final long length) {
        System.arraycopy(source.toArray(), (int) from, target.toArray(), (int) to, (int) length);
    }
    
    //TODO: stuff for defaulted parameters!

}
