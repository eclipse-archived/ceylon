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

@Ceylon(major = 1)
@Method
public final class coalesce {

    private coalesce() {}

    @TypeParameters(@TypeParameter(value="Element", satisfies="ceylon.language.Object"))
    @TypeInfo("ceylon.language.Empty|ceylon.language.Sequence<Element>")
    public static <Element> Iterable<? extends Element> coalesce(
    @Name("values") @Sequenced
    @TypeInfo("ceylon.language.Iterable<ceylon.language.Nothing|Element>")
    final ceylon.language.Iterable<? extends Element> values) {
		List<Element> list = new ArrayList<Element>();
		java.lang.Object $tmp;
		for (Iterator<? extends Element> iter=values.getIterator(); !(($tmp = iter.next()) instanceof Finished);) {
			Element elem = (Element)$tmp;
			if (elem!=null) list.add(elem);
		}
        if (list.isEmpty()) {
            return (Iterable<? extends Element>) $empty.getEmpty();
        }
        else {
            return new ArraySequence<Element>(list);
        }
    }
    @Ignore
    public static <Element> Iterable<? extends Element> coalesce() {
        return (Iterable<? extends Element>) $empty.getEmpty();
    }
}
