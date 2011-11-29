package ceylon.language;

import com.redhat.ceylon.compiler.metadata.java.Name;
import com.redhat.ceylon.compiler.metadata.java.TypeParameter;
import com.redhat.ceylon.compiler.metadata.java.TypeParameters;

@TypeParameters({@TypeParameter(value="Other",
        satisfies="ceylon.language.Integral<Other>")})
public interface Integral<Other extends Integral<Other>> 
        extends Numeric<Other>, Ordinal<Other> {
    public Other remainder(@Name("other") Other number);
    public boolean getZero();
    public boolean getUnit();
}
