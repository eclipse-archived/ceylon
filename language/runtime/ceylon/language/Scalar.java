package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Annotation;
import com.redhat.ceylon.compiler.java.metadata.Annotations;
import com.redhat.ceylon.compiler.java.metadata.CaseTypes;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.SatisfiedTypes;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;

@Ceylon(major = 1)
@TypeParameters(@TypeParameter(value = "Other",
    		satisfies="ceylon.language.Scalar<Other>"))
@SatisfiedTypes({"ceylon.language.Number",
		         "ceylon.language.Numeric<Other>",
		         "ceylon.language.Comparable<Other>"})
@CaseTypes(of = "Other")
public interface Scalar<Other extends Scalar<Other>> 
    extends Number, Numeric<Other>, Comparable<Other> {

    @Annotations({@Annotation("actual"), @Annotation("formal")})
    public Other getMagnitude();

    @Annotations({@Annotation("actual"), @Annotation("formal")})
    public Other getFractionalPart();

    @Annotations({@Annotation("actual"), @Annotation("formal")})
    public Other getWholePart();
    
}
