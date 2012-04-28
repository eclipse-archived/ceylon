package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.metadata.Variance;

@Ceylon
@TypeParameters({
    @TypeParameter(value = "Index", variance = Variance.IN,
    		satisfies="ceylon.language.Comparable<Index>"),
    @TypeParameter(value = "Span", variance = Variance.OUT)
})
public interface Ranged<Index extends Comparable<? super Index>, Span> {
    
	public Span span(@Name("from") Index from, 
			@Name("to") @TypeInfo("ceylon.language.Nothing|Index") Index to);
    
	public Span segment(@Name("from") Index from, @Name("length") long length);
	
}
