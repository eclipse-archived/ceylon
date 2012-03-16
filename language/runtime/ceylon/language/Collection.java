package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.metadata.Variance;

@Ceylon
@TypeParameters(@TypeParameter(value = "Element", variance = Variance.OUT))
public interface Collection<Element> 
        extends Iterable<Element>, Sized, Category,
                Cloneable<Collection<? extends Element>> {
    
    @Override
    public boolean getEmpty();

    @Override
    public boolean contains(@Name("element") java.lang.Object element);
    
    public long count(@Name("element") java.lang.Object element);
    
    @Ignore
    public static final class Collection$impl {
        public static <Element> boolean getEmpty(Collection<Element> $this){
            return $this.getSize() == 0;
        }
        public static <Element> boolean contains(Collection<Element> $this, java.lang.Object element){
            java.lang.Object elem;
            for (ceylon.language.Iterator<?> $element$iter$1 = $this.getIterator(); !((elem = $element$iter$1.next()) instanceof Finished);) {
                if (elem!=null && element.equals(elem)) {
                    return true;
                }
            }
            return false;
        }
        public static <Element> long count(Collection<Element> $this, java.lang.Object element){
            long count=0;
            java.lang.Object elem;
            for (ceylon.language.Iterator<?> $element$iter$1 = $this.getIterator(); !((elem = $element$iter$1.next()) instanceof Finished);) {
                if (elem!=null && element.equals(elem)) {
                    count++;
                }
            }
            return count;
        }
    }

}
