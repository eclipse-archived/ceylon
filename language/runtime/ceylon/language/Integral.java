package ceylon.language;

import com.redhat.ceylon.compiler.metadata.java.Name;

public interface Integral<Other extends Integral<Other>> 
 extends Numeric<Other>, Ordinal<Other> {
    public Other remainder(@Name("other") Other number);
}
