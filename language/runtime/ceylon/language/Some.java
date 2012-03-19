package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.metadata.Variance;

@Ceylon
@TypeParameters(@TypeParameter(value = "Element", variance = Variance.OUT))
public interface Some<Element> extends FixedSized<Element> {
    
    @TypeInfo("Element")
    @Override 
    public Element getFirst();
    
    @Override
    public boolean getEmpty();
    
    @TypeInfo("ceylon.language.FixedSized<Element>")
    public FixedSized<? extends Element> getRest();

    @Ignore
    public static final class Some$impl {
        public static <Element> Element getFirst(Some<Element> $this){
            java.lang.Object first = $this.getIterator().next();
            if (first instanceof Finished) {
                throw new Exception(null, null);
            }
            else {
                return (Element) first;
            }
        }
        public static <Element> boolean getEmpty(Some<Element> $this){
            return false;
        }
    }

}
