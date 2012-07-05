package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Annotation;
import com.redhat.ceylon.compiler.java.metadata.Annotations;
import com.redhat.ceylon.compiler.java.metadata.CaseTypes;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.SatisfiedTypes;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;

@Ceylon(major = 2)
@TypeParameters(@TypeParameter(value="Other",
        satisfies="ceylon.language.Integral<Other>"))
@SatisfiedTypes({"ceylon.language.Numeric<Other>",
	             "ceylon.language.Ordinal<Other>"})
@CaseTypes(of = "Other")
public interface Integral<Other extends Integral<Other>> 
        extends Numeric<Other>, Ordinal<Other> {

    @Annotations(@Annotation("formal"))
    public Other remainder(@Name("other") Other number);

    @Annotations(@Annotation("formal"))
    public boolean getZero();

    @Annotations(@Annotation("formal"))
    public boolean getUnit();
}
