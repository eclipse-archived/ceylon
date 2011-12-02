package ceylon.language;

import java.util.ArrayList;
import java.util.List;

import com.redhat.ceylon.compiler.metadata.java.Ceylon;
import com.redhat.ceylon.compiler.metadata.java.Method;
import com.redhat.ceylon.compiler.metadata.java.Name;
import com.redhat.ceylon.compiler.metadata.java.Sequenced;
import com.redhat.ceylon.compiler.metadata.java.TypeInfo;
import com.redhat.ceylon.compiler.metadata.java.TypeParameter;
import com.redhat.ceylon.compiler.metadata.java.TypeParameters;

@Ceylon
@Method
public final class entries {
    
    private entries() {}
    
    @TypeParameters({
    @TypeParameter(value="Element", satisfies="ceylon.language.Equality")
    })
    @TypeInfo("ceylon.language.Empty|ceylon.language.Sequence<ceylon.language.Entry<ceylon.language.Natural,Element>>")
    public static <Element>ceylon.language.Iterable<? extends Entry<? extends Natural,? extends Element>> entries(@Name("sequence")
    @Sequenced @TypeInfo("ceylon.language.Empty|ceylon.language.Sequence<Element>")
    final ceylon.language.Iterable<? extends Element> sequence) {
        if (sequence.getEmpty()) {
            return $empty.getEmpty();
        }
        else {
        	long i=0;
            List<Entry<? extends Natural,? extends Element>> list = new ArrayList<Entry<? extends Natural,? extends Element>>();
            for (Iterator<? extends Element> iter=sequence.getIterator(); iter!=null; iter=iter.getTail()) {
                list.add(new Entry<Natural,Element>(Natural.instance(i++), iter.getHead()));
            }
            return new ArraySequence<Entry<? extends Natural,? extends Element>>(list.toArray(), 0);
        }
    }
}
