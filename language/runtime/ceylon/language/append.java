package ceylon.language;

import java.util.ArrayList;
import java.util.List;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Method;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;

@Ceylon(major = 1)
@Method
public final class append {
    
    private append() {}
    
    @TypeParameters(@TypeParameter(value="Element"))
    @TypeInfo("ceylon.language.Empty|ceylon.language.Sequence<Element>")
    public static <Element> Sequence<? extends Element> append(
    @Name("sequence")
    @TypeInfo("ceylon.language.Empty|ceylon.language.Sequence<Element>")
    final Iterable<? extends Element> sequence,
    @Name("element")
    Element element) {
        final int size;
        if (sequence instanceof Sequence) {
            size = (int) ((Sequence) sequence).getSize()+1;
        }
        else {
            size = 1;
        }
        List<Element> list = new ArrayList<Element>(size);
        java.lang.Object $tmp2;
        for (Iterator<? extends Element> iter2=sequence.getIterator(); 
                !(($tmp2 = iter2.next()) instanceof Finished);) {
            final Element elem2 = (Element) $tmp2;
            if (elem2 != null) list.add(elem2);
        }
        list.add(element);
        return new ArraySequence<Element>(list);
    }
}
