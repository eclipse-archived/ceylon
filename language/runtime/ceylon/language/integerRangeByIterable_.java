package ceylon.language;

import com.redhat.ceylon.compiler.java.language.AbstractIterable;
import com.redhat.ceylon.compiler.java.language.AbstractIterator;
import com.redhat.ceylon.compiler.java.metadata.Annotation;
import com.redhat.ceylon.compiler.java.metadata.Annotations;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Method;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

@Ceylon(major = 4)
@Method
final class integerRangeByIterable_ {
    
    private integerRangeByIterable_() {}
    
    /**
     * Optimized implementation of {@code Range<Integer>.iterator}. This is 
     * necessary because we need reified generics in order to write 
     * the optimized version in Ceylon. 
     * @param range
     * @param step
     * @return
     */
    @Annotations(@Annotation("native"))
    @TypeParameters(@TypeParameter(value="Element", 
            satisfies={"ceylon.language::Ordinal<Element>", 
                "ceylon.language::Comparable<Element>"}))
    @TypeInfo(value="ceylon.language::Iterable<Element, Null>")
    static <Element extends ceylon.language.Ordinal<? extends Element> & ceylon.language.Comparable<? super Element>> 
            ceylon.language.Iterable<Element, Null> integerRangeByIterable(@Ignore final TypeDescriptor $reifiedElement,
                    @Name("range")
                    @TypeInfo(value="ceylon.language::Range<Element>")
                    ceylon.language.Range<Element> range,
                    @Name("step")
                    final long step) {
        final ceylon.language.Range<ceylon.language.Integer> r = (ceylon.language.Range)range; 
        //Optimize for Integer ranges
        return new AbstractIterable<Element,  Null>($reifiedElement, Null.$TypeDescriptor) {
            @Override
            @Annotations(@Annotation("formal"))
            @TypeInfo("ceylon.language::Iterator<Element>")
            public Iterator<? extends Element> getIterator() {

                return new AbstractIterator<Element>($reifiedElement) {
                    long current = r.getFirst().longValue();
                    final long lim = r.getLast().longValue();
                    boolean inverse = lim < current;

                    @TypeInfo(value="Element|ceylon.language::Finished", erased=true)
                    public java.lang.Object next() {
                        long result = current;
                        if (inverse) {
                            // avoid current < lim etc: possibility of overflow
                            if (current-lim < 0) return finished_.getFinished$();
                            current-=step;
                        } else {
                            if (current-lim > 0) return finished_.getFinished$();
                            current+=step;
                        }
                        return Integer.instance(result);
                    }

                    @Override
                    public java.lang.String toString() {
                        return "RangeIterator";
                    }
                };
            }
        };
    }
}

