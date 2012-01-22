package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.SatisfiedTypes;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.metadata.Variance;

@Ceylon
@TypeParameters(@TypeParameter(value = "Element", variance = Variance.OUT))
@SatisfiedTypes("ceylon.language.Container")
public interface Iterable<Element> extends Container {

    public boolean getEmpty();
    
    @TypeInfo("ceylon.language.Iterator<Element>")
    public Iterator<? extends Element> getIterator();
    
    @Ignore
    public static final class Iterable$impl {
        public static <Element> boolean getEmpty(Iterable<Element> $this){
            return $this.getIterator().next() instanceof Finished;
        }
    }
}
