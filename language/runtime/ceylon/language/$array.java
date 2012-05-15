package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Method;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.Sequenced;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;

@Ceylon
@Method
public final class $array {

    private $array() {}
    
    @TypeParameters(@TypeParameter(value="Element"))
    @TypeInfo("ceylon.language.Array<Element>")
    public static <Element> Array<Element> array(
    @Name("elements")
    @Sequenced
    @TypeInfo("ceylon.language.Empty|ceylon.language.Sequence<Element>")
    final ceylon.language.Iterable<? extends Element> elements) {
		if (elements.getEmpty()) {
            return $arrayOfNone.<Element>arrayOfNone();
		} else {
            return $arrayOfSome.arrayOfSome(elements);
		}
    }
    
    @Ignore
    public static <Element> Array<Element> array() {
        return $arrayOfNone.<Element>arrayOfNone();
    }
            
}
