package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Method;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;

@Ceylon
@Method
public class emptyOrSingleton {

    private emptyOrSingleton(){}

    @TypeParameters(@TypeParameter(value="Element", satisfies="ceylon.language.Object"))
    @TypeInfo("ceylon.language.Empty|ceylon.language.Sequence<Element>")
    public static <Element> Iterable<Element> emptyOrSingleton(
            @Name("element")
            @TypeInfo("Nothing|Element")
            Element element) {
        return element==null ? 
                $empty.getEmpty() : 
                new Singleton<Element>(element);
    }
}
