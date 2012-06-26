package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Method;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;

@Ceylon(major = 1)
@Method
public final class arrayOfSize {

    private arrayOfSize() {}

    @TypeParameters(@TypeParameter(value="Element"))
    @TypeInfo("ceylon.language.Array<Element>")
    public static <Element> Array<Element> arrayOfSize(
    @Name("size")
    @TypeInfo("ceylon.language.Integer")
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
        return size>0 ?
        		//TODO: This is horribly inefficient. We should
        		//      create an empty array, and then use
        		//      Arrays.fill() to populate it!
                ArrayOfSome.<Element>instance(typeClass, getIterable(size, element)) :
                ArrayOfNone.<Element>instance(typeClass);
    }
    
    private static <Element> Iterable<Element> getIterable(final long size, 
    		final Element element) {
        return new Iterable<Element>() {
            public Iterator<Element> getIterator() {
                return new Iterator<Element>() {
                    long idx = 0;
                    @TypeInfo("Element|ceylon.language.Finished")
                    public java.lang.Object next() {
                        return idx++<size ? element : exhausted.getExhausted();
                    }
                };
            }

            @Override
            public boolean getEmpty() {
                return size==0;
            }

            @Override
            @Ignore
            public Iterable<? extends Element> getSequence() { 
                return Iterable$impl._getSequence(this); 
            }
            @Override 
            @Ignore
            public Element find(Callable<? extends Boolean> f) { 
                return Iterable$impl._find(this, f); 
            }
            @Override 
            @Ignore
            public Iterable<? extends Element> sorted(Callable<? extends Comparison> f) { 
                return Iterable$impl._sorted(this, f); 
            }
            @Override 
            @Ignore
            public <Result> Iterable<Result> map(Callable<? extends Result> f) { 
                return new MapIterable<Element, Result>(this, f); 
            }
            @Override 
            @Ignore
            public Iterable<? extends Element> filter(Callable<? extends Boolean> f) { 
                return new FilterIterable<Element>(this, f); 
            }
            @Override 
            @Ignore
            public <Result> Result fold(Result ini, Callable<? extends Result> f) { 
                return Iterable$impl._fold(this, ini, f); 
            }
            @Override @Ignore
            public boolean any(Callable<? extends Boolean> f) {
                return Iterable$impl._any(this, f);
            }
            @Override @Ignore
            public boolean every(Callable<? extends Boolean> f) {
                return Iterable$impl._every(this, f);
            }
        };
    }
}
