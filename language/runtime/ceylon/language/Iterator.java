package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.metadata.Variance;

@Ceylon
@TypeParameters(@TypeParameter(value = "Element", variance = Variance.OUT))
public interface Iterator<Element> {
    
    public Element getHead();
    
    @TypeInfo("ceylon.language.Nothing|ceylon.language.Iterator<Element>")
    public Iterator<? extends Element> getTail();
    
}
