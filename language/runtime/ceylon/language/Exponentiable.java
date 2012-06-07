package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Annotation;
import com.redhat.ceylon.compiler.java.metadata.Annotations;
import com.redhat.ceylon.compiler.java.metadata.CaseTypes;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.SatisfiedTypes;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;

@Ceylon
@TypeParameters({@TypeParameter(value = "This",
    		satisfies="ceylon.language.Exponentiable<This,Other>"),
    		    @TypeParameter(value = "Other",
            satisfies="ceylon.language.Numeric<Other>")})
@SatisfiedTypes("ceylon.language.Numeric<This>")
@CaseTypes(of = "This")
public interface Exponentiable<This extends Exponentiable<This,Other>,
                               Other extends Numeric<Other>> 
    extends Numeric<This> {
	
    @Annotations(@Annotation("formal"))
    public This power(@Name("other") Other number);
        
}
