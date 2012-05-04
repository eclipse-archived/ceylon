package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Method;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;

@Ceylon
@Method
public final class $arrayOfNone {

    private $arrayOfNone() {}

    @TypeParameters(@TypeParameter(value="Element"))
    @TypeInfo("ceylon.language.Array<Element>&None<Element>")
    public static <Element> Array<? extends Element> arrayOfNone() {
        return new ceylon.language.ArrayOfNone<Element>();
    }
}
