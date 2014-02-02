package ceylon.language;

import java.util.Arrays;
import java.util.Comparator;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Method;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

@Ceylon(major = 6)
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
        final Iterable<? extends Element, ?> elements) {
        
        if( elements.getEmpty() ) {
            return (Sequential<? extends Element>)empty_.get_();   
        }
        
        ArraySequence<? extends Element> result = new ArraySequence<Element>($reifiedElement, elements);
        Arrays.sort((Element[])result.array, result.first, result.first+result.length, comparator(comparing));
        return result;
    }

    /** Make a {@link java.util.Comparator} from a {@code Callable<Comparison>} */
    private static <Element> Comparator<Element> comparator(
            final Callable<? extends Comparison> comparing) {
        return new Comparator<Element>() {
            public int compare(Element x, Element y) {
                Comparison result = comparing.$call$(x, y);
                if (result==larger_.get_()) return 1;
                if (result==smaller_.get_()) return -1;
                return 0;
            }
        };
    }
    
    static <Element> void sort(Element[] array, final Callable<? extends Comparison> comparing) {
        java.util.Arrays.sort(array, comparator(comparing));
    }
    
}