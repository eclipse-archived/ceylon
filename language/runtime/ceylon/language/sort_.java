package ceylon.language;

import java.util.Comparator;

import com.redhat.ceylon.compiler.java.Util;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Method;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.Sequenced;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;

@Ceylon(major = 2)
@Method
public final class sort_ {
    
    private sort_() {}
    
    @TypeParameters(@TypeParameter(value="Element", satisfies="ceylon.language.Comparable<Element>"))
    @TypeInfo("ceylon.language.Empty|ceylon.language.Sequence<Element>")
    public static <Element extends Comparable<? super Element>> Iterable<? extends Element> sort(
        @Name("elements")
        @Sequenced
        @TypeInfo("ceylon.language.Iterable<Element>")
        final Iterable<? extends Element> elements) {
        
        if( elements.getEmpty() ) {
            return (Iterable)empty_.getEmpty();   
        }
        
        java.util.List<Element> list = Util.collectIterable(elements);

        java.util.Collections.sort(list, new Comparator<Element>() {
            public int compare(Element x, Element y) {
                Comparison result = x.compare(y);
                if (result.largerThan()) return 1;
                if (result.smallerThan()) return -1;
                return 0;
            }
        });

        return new ArraySequence<Element>(list);
    }
    
    @Ignore
    public static <Element> Iterable<? extends Element> sort() {
        return (Iterable)empty_.getEmpty();
    }
    
}