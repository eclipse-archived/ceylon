package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Annotation;
import com.redhat.ceylon.compiler.java.metadata.Annotations;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.metadata.Variance;

@Ceylon(major = 3)
@TypeParameters({
    @TypeParameter(value = "Index", variance = Variance.IN,
    		satisfies="ceylon.language::Comparable<Index>"),
    @TypeParameter(value = "Span", variance = Variance.OUT)
})
public interface Ranged<Index extends Comparable<? super Index>, Span> {
    
    @Annotations({@Annotation("shared"), @Annotation("formal")})
	public Span span(@Name("from") Index from, 
			@Name("to") Index to);
    
    @Annotations({@Annotation("shared"), @Annotation("formal")})
	public Span spanFrom(@Name("to") Index from);
    
    @Annotations({@Annotation("shared"), @Annotation("formal")})
	public Span spanTo(@Name("to") Index to);
    
    @Annotations({@Annotation("shared"), @Annotation("formal")})
	public Span segment(@Name("from") Index from, @Name("length") long length);
	
}
