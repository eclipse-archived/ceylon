package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.SatisfiedTypes;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.metadata.Variance;

@Ceylon
@TypeParameters(@TypeParameter(value = "Element", variance = Variance.OUT))
@SatisfiedTypes("ceylon.language.Iterable<Element>")
public interface Ordered<Element> extends Iterable<Element> {

	@Override
    public boolean getEmpty();
    
    @TypeInfo("Element|ceylon.language.Nothing")
    public Element getFirst();
    
    //public Ordered<Element> segment(long skipping, long finishingAfter);
}
