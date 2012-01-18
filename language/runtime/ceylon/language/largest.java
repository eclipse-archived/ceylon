package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Method;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;

@Ceylon
@Method
public class largest {

    private largest(){}

    @TypeParameters(@TypeParameter(value="Element", satisfies="ceylon.language.Comparable<Element>"))
    @TypeInfo("Element")
    public static <Element extends Comparable<? super Element>> Element largest(
            @Name("x")
            @TypeInfo("Element")
            Element x,
            @Name("y")
            @TypeInfo("Element")
            Element y) {
        return x.compare(y).largerThan() ? x : y;
    }
}
