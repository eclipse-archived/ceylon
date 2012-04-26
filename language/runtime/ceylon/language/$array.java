package ceylon.language;

import java.util.ArrayList;
import java.util.List;

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

    @Ignore
    public static <Element> Array<? extends Element> array() {
        return arrayOfNone.<Element>arrayOfNone();
    }
    
    @TypeParameters(@TypeParameter(value="Element"))
    @TypeInfo("ceylon.language.Array<Element>")
    public static <Element> Array<? extends Element> array(
    @Name("elements")
    @Sequenced
    @TypeInfo("ceylon.language.Empty|ceylon.language.Sequence<Element>")
    final ceylon.language.Iterable<? extends Element> elements) {
		List<Element> list = new ArrayList<Element>();
		java.lang.Object $tmp;
		for (Iterator<? extends Element> iter=elements.getIterator(); !(($tmp = iter.next()) instanceof Finished);) {
			Element elem = (Element)$tmp;
			if (elem!=null) list.add(elem);
		}
		if (list.size() > 0) {
		    return new ceylon.language.NonemptyArray<Element>(list);
		} else {
		    return arrayOfNone.<Element>arrayOfNone();
		}
    }
}
