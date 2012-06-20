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
public final class makeArray {

    private makeArray() {}

    @TypeParameters(@TypeParameter(value="Element"))
    @TypeInfo("ceylon.language.Array<Element>")
    public static <Element> Array<Element> makeArray(
    @Name("size")
    @TypeInfo("ceylon.language.Integer")
    final Integer size,
    @Name("index")
    @TypeInfo("ceylon.language.Callable<Element,ceylon.language.Integer>")
    final Callable<Element> init) {
        return makeArray(null, init);
    }
    
    @Ignore
    public static <Element> Array<Element> makeArray(
            final Class typeClass,
            final Integer size,
            final Callable<Element> init) {
        if (size.value > 0) {
            return new ArrayOfSome<Element>(typeClass, getIterable(size, init));
        } else {
            return new ArrayOfNone<Element>(typeClass);
        }
    }
    
    private static <Element> Iterable getIterable(final Integer size, final Callable<Element> init) {
        return new Iterable<Element>() {
            public Iterator<Element> getIterator() {
                return new Iterator<Element>() {
                    long idx = 0;
                    
                    @TypeInfo("Element|ceylon.language.Finished")
                    public java.lang.Object next() {
                        if(idx >= size.value) {
                            return exhausted.getExhausted();
                        } else {
                            return init.$call(Integer.instance(idx++));
                        }
                    }
                };
            }

            @Override
            public boolean getEmpty() {
                return size.value == 0;
            }

            @Override public Iterable<? extends Element> getSequence() { return Iterable$impl._getSequence(this); }
            @Override public Element find(Callable<? extends Boolean> f) { return Iterable$impl._find(this, f); }
            @Override public <Result> Iterable<Result> map(Callable<? extends Result> f) { return new MapIterable<Element, Result>(this, f); }
            @Override public Iterable<? extends Element> filter(Callable<? extends Boolean> f) { return new FilterIterable<Element>(this, f); }
            @Override public <Result> Result fold(Result ini, Callable<? extends Result> f) { return Iterable$impl._fold(this, ini, f); }
        };
    }
}
