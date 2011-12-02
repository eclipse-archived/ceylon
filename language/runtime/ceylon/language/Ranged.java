package ceylon.language;

import com.redhat.ceylon.compiler.metadata.java.Ceylon;
import com.redhat.ceylon.compiler.metadata.java.Name;
import com.redhat.ceylon.compiler.metadata.java.TypeInfo;
import com.redhat.ceylon.compiler.metadata.java.TypeParameter;
import com.redhat.ceylon.compiler.metadata.java.TypeParameters;
import com.redhat.ceylon.compiler.metadata.java.Variance;

@Ceylon
@TypeParameters({
    @TypeParameter(value = "Span", variance = Variance.OUT)
})
public interface Ranged<Span> {
    
	public Span span(@Name("from") @TypeInfo("ceylon.language.Natural") long from, 
			@Name("to") @TypeInfo("ceylon.language.Natural") long to);
    
	public Span segment(@Name("from") @TypeInfo("ceylon.language.Natural") long from, 
			@Name("length") @TypeInfo("ceylon.language.Natural") long length);
	
}
