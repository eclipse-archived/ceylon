package ceylon.language;

import java.util.Comparator;

import com.redhat.ceylon.compiler.java.TypeDescriptor;
import com.redhat.ceylon.compiler.java.Util;
import com.redhat.ceylon.compiler.java.language.ArraySequence;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Method;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;

@Ceylon(major = 4)
@Method
final class internalSort_ {
    
    private internalSort_() {}
    
    @TypeParameters(@TypeParameter(value="Element"))
    @TypeInfo("ceylon.language::Sequential<Element>")
    static <Element> Sequential<? extends Element> internalSort(@Ignore TypeDescriptor $reifiedElement,
        @Name("comparing")
        @TypeInfo("ceylon.language::Callable<ceylon.language::Comparison|ceylon.language::Null,ceylon.language::Tuple<Element,Element,ceylon.language::Tuple<Element,Element,ceylon.language::Empty>>>")
        final Callable<? extends Comparison> comparing,
        @Name("elements")
        @TypeInfo("ceylon.language::Iterable<Element,ceylon.language::Null>")
        final Iterable<? extends Element, ? extends java.lang.Object> elements) {
        
        if( elements.getEmpty() ) {
            return (Sequential)empty_.getEmpty$();   
        }
        
        java.util.List<Element> list = Util.collectIterable(elements);

        java.util.Collections.sort(list, new Comparator<Element>() {
            public int compare(Element x, Element y) {
                Comparison result = comparing.$call(x, y);
                if (result.largerThan()) return 1;
                if (result.smallerThan()) return -1;
                return 0;
            }
        });

        return new ArraySequence<Element>($reifiedElement, list);
    }
    
}