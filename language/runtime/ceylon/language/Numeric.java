package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Annotation;
import com.redhat.ceylon.compiler.java.metadata.Annotations;
import com.redhat.ceylon.compiler.java.metadata.CaseTypes;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.SatisfiedTypes;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;

@Ceylon(major = 3)
@TypeParameters(@TypeParameter(value = "Other",
    		satisfies="ceylon.language.Numeric<Other>"))
@SatisfiedTypes({"ceylon.language.Summable<Other>",
		         "ceylon.language.Invertable<Other>"})
@CaseTypes(of = "Other")
public interface Numeric<Other extends Numeric<Other>> 
    extends Summable<Other>, Invertable<Other> {
	
    @Annotations(@Annotation("formal"))
	public Other minus(@Name("other") Other number);
    
    @Annotations(@Annotation("formal"))
    public Other times(@Name("other") Other number);
    
    @Annotations(@Annotation("formal"))
    public Other divided(@Name("other") Other number);
    
}
