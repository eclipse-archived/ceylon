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
public final class coalesce {
    
    private coalesce() {}
    
    @TypeParameters(@TypeParameter(value="Element", satisfies="ceylon.language.Object"))
    @TypeInfo("ceylon.language.Empty|ceylon.language.Sequence<Element>")
    public static <Element> Iterable<? extends Element> coalesce(
    @Name("sequence")
    @TypeInfo("ceylon.language.Empty|ceylon.language.Sequence<ceylon.language.Nothing|Element>")
    final ceylon.language.Iterable<? extends Element> sequence) {
		List<Element> list = new ArrayList<Element>();
		java.lang.Object $tmp;
		for (Iterator<? extends Element> iter=sequence.getIterator(); !(($tmp = iter.next()) instanceof Finished);) {
			Element elem = (Element)$tmp;
			if (elem!=null) list.add(elem);
		}
        return new ArraySequence<Element>(list);
    }
}
