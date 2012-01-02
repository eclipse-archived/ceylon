package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.SatisfiedTypes;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;

@Ceylon
@TypeParameters(@TypeParameter(value="Other",
        satisfies="ceylon.language.Integral<Other>"))
@SatisfiedTypes({"ceylon.language.Numeric<Other>",
	             "ceylon.language.Ordinal<Other>"})
public interface Integral<Other extends Integral<Other>> 
        extends Numeric<Other>, Ordinal<Other> {
    public Other remainder(@Name("other") Other number);
    public boolean getZero();
    public boolean getUnit();
}
