package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Method;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;

@Ceylon(major = 3)
@Method
public class emptyOrSingleton_ {

    private emptyOrSingleton_(){}

    @TypeParameters(@TypeParameter(value="Element", satisfies="ceylon.language.Object"))
    @TypeInfo("ceylon.language.Empty|ceylon.language.Sequence<Element>")
    public static <Element> List<Element> emptyOrSingleton(
            @Name("element")
            @TypeInfo("ceylon.language.Nothing|Element")
            Element element) {
        return element==null ? 
                (List)empty_.getEmpty$() : 
                new Singleton<Element>(element);
    }
}
