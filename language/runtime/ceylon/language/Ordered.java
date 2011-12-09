package ceylon.language;

import com.redhat.ceylon.compiler.metadata.java.Ceylon;
import com.redhat.ceylon.compiler.metadata.java.SatisfiedTypes;
import com.redhat.ceylon.compiler.metadata.java.TypeInfo;
import com.redhat.ceylon.compiler.metadata.java.TypeParameter;
import com.redhat.ceylon.compiler.metadata.java.TypeParameters;
import com.redhat.ceylon.compiler.metadata.java.Variance;

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
