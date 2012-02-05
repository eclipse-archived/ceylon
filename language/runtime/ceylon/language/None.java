package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.metadata.Variance;

@Ceylon
@TypeParameters(@TypeParameter(value = "Element", variance = Variance.OUT))
public interface None<Element> extends FixedSized<Element> {
    
    @TypeInfo("ceylon.language.Nothing")
    @Override public Element getFirst();
    
    @Ignore
    public static final class None$impl {
        public static <Element> Element getFirst(None<Element> $this){
            return null;
        }
        public static <Element> long getSize(None<Element> $this){
            return 0;
        }
        public static <Element> boolean getEmpty(None<Element> $this){
            return true;
        }
        public static <Element> Iterator getIterator(None<Element> $this){
            return emptyIterator.getEmptyIterator();
        }
    }

}
