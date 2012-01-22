package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.metadata.Variance;

@Ceylon
@TypeParameters(@TypeParameter(value = "Element", variance = Variance.OUT))
public interface Collection<Element> 
        extends Iterable<Element>, Sized, Category,
                Equality, Cloneable<Collection<Element>> {

    @Ignore
    public static final class Collection$impl {
        public static <Element> boolean getEmpty(Collection<Element> $this){
            return $this.getSize() == 0;
        }

    }

}
