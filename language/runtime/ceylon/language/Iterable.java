package ceylon.language;

import com.redhat.ceylon.compiler.metadata.java.Ceylon;
import com.redhat.ceylon.compiler.metadata.java.TypeInfo;
import com.redhat.ceylon.compiler.metadata.java.TypeParameter;
import com.redhat.ceylon.compiler.metadata.java.TypeParameters;
import com.redhat.ceylon.compiler.metadata.java.Variance;

@Ceylon
@TypeParameters({
    @TypeParameter(value = "Element", variance = Variance.OUT)
 })
 public interface Iterable<Element> extends Container {

	public boolean getEmpty();
    
    @TypeInfo("ceylon.language.Nothing|ceylon.language.Iterator<Element>")
    public Iterator<? extends Element> getIterator();
    
}
