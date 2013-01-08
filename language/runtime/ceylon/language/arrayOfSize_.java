package ceylon.language;

import com.redhat.ceylon.compiler.java.language.AbstractIterable;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Method;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;

@Ceylon(major = 3)
@Method
public final class arrayOfSize_ {

    private arrayOfSize_() {}

    @TypeParameters(@TypeParameter(value="Element"))
    @TypeInfo("ceylon.language::Array<Element>")
    public static <Element> Array<Element> arrayOfSize(
    @Name("size")
    @TypeInfo("ceylon.language::Integer")
    final long size,
    @Name("element")
    @TypeInfo("Element")
    final Element element) {
        return arrayOfSize(null, size, element);
    }
    
    @Ignore
    public static <Element> Array<Element> arrayOfSize(
            final Class typeClass,
            final long size,
            final Element element) {
        //TODO: This is horribly inefficient. We should
        //      create an empty array, and then use
        //      Arrays.fill() to populate it!
        return Array.<Element>instance(typeClass, (int)size, element);
    }
    
    private static <Element> Iterable<Element> getIterable(final long size, 
    		final Element element) {
        return new AbstractIterable<Element>() {
            public Iterator<Element> getIterator() {
                return new Iterator<Element>() {
                    long idx = 0;
                    @TypeInfo(value="Element|ceylon.language::Finished", erased=true)
                    public java.lang.Object next() {
                        return idx++<size ? element : finished_.getFinished$();
                    }
                };
            }

            @Override
            public boolean getEmpty() {
                return size==0;
            }

        };
    }
}
