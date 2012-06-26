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
public final class entries {
    
    private entries() {}
    
    @TypeParameters(@TypeParameter(value="Element", satisfies="ceylon.language.Object"))
    @TypeInfo("ceylon.language.Empty|ceylon.language.Sequence<ceylon.language.Entry<ceylon.language.Integer,Element>>")
    public static <Element> Iterable<? extends Entry<? extends Integer,? extends Element>> entries(@Name("elements")
    @Sequenced @TypeInfo("ceylon.language.Iterable<Element>")
    final ceylon.language.Iterable<? extends Element> elements) {
        if (elements.getEmpty()) {
            return (Iterable)$empty.getEmpty();
        }
        else {
        	long i=0;
            List<Entry<? extends Integer,? extends Element>> list = new ArrayList<Entry<? extends Integer,? extends Element>>();
            java.lang.Object elem;
            for (Iterator<? extends Element> iter=elements.getIterator(); !((elem = iter.next()) instanceof Finished);) {
                list.add(new Entry<Integer,Element>(Integer.instance(i++), (Element) elem));
            }
            return new ArraySequence<Entry<? extends Integer,? extends Element>>(list);
        }
    }
    @Ignore
    public static <Element> Iterable<? extends Entry<? extends Integer,? extends Element>> entries() {
        return (Iterable) $empty.getEmpty();
    }
}
