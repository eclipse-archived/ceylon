package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Annotation;
import com.redhat.ceylon.compiler.java.metadata.Annotations;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Class;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;

@Ceylon(major = 3)
@Class(extendsType="ceylon.language.IdentifiableObject")
@TypeParameters({@TypeParameter("Element"), @TypeParameter("Other")})
public class ChainedIterator<Element,Other> implements Iterator {

    private Iterator iter;
    private final Iterable<? extends Other> two;
    private boolean more = true;

    public ChainedIterator(final Iterable<? extends Element> i1, final Iterable<? extends Other> i2) {
        iter = i1.getIterator();
        two = i2;
    }

    @Override
    @TypeInfo("Element|Other|ceylon.language.Finished")
    public java.lang.Object next() {
        java.lang.Object e = iter.next();
        if (e == exhausted_.getExhausted() && more) {
            more = false;
            iter = two.getIterator();
            e = iter.next();
        }
        return e;
    }
}
