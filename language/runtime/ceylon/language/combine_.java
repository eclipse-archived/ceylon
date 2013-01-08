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
public class combine_ {

    private combine_(){}

    @TypeInfo("ceylon.language::Iterable<Result>")
    @TypeParameters({
        @TypeParameter("Result"), @TypeParameter("Element"), @TypeParameter("OtherElement")
    })
    public static <Result,Element,OtherElement> Iterable<Result> combine(
            @Name("combination") @TypeInfo("ceylon.language::Callable<Result,ceylon.language::Tuple<Element|OtherElement,Element,ceylon.language::Tuple<OtherElement,OtherElement,ceylon.language::Empty>>>")
            final Callable<? extends Result> combination,
            @Name("elements") @TypeInfo("ceylon.language::Iterable<Element>")
            final Iterable<? extends Element> elements,
            @Name("otherElements") @TypeInfo("ceylon.language::Iterable<OtherElement>")
            final Iterable<? extends OtherElement> otherElements) {
        return new AbstractIterable<Result>() {
            @Override @Ignore
            public Iterator<? extends Result> getIterator() {
                final Iterator<? extends Element> ei = elements.getIterator();
                final Iterator<? extends OtherElement> oi = otherElements.getIterator();
                return new Iterator<Result>(){
                    @Override @Ignore
                    public java.lang.Object next() {
                        java.lang.Object e = ei.next();
                        java.lang.Object o = oi.next();
                        if (e instanceof Finished || o instanceof Finished) {
                            return finished_.getFinished$();
                        }
                        return combination.$call(e, o);
                    }
                };
            }
        };
    }
}
