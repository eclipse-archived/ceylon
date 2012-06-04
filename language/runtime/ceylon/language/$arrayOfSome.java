package ceylon.language;

import java.util.ArrayList;
import java.util.List;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Method;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;

@Ceylon
@Method
public final class $arrayOfSome {

    private $arrayOfSome() {}

    @TypeParameters(@TypeParameter(value="Element"))
    @TypeInfo("ceylon.language.Array<Element>&Some<Element>")
    public static <Element> Array<Element> arrayOfSome(
    @Name("elements")
    @TypeInfo("ceylon.language.Sequence<Element>")
    final Iterable<? extends Element> elements) {
		List<Element> list = new ArrayList<Element>();
		java.lang.Object $tmp;
		for (Iterator<? extends Element> iter=elements.getIterator(); 
		        !(($tmp = iter.next()) instanceof Finished);) {
			Element elem = (Element)$tmp;
			if (elem!=null) list.add(elem);
		}
        return new ArrayOfSome<Element>(list);
    }
}
