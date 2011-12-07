package ceylon.language;

import com.redhat.ceylon.compiler.metadata.java.Ceylon;
import com.redhat.ceylon.compiler.metadata.java.SatisfiedTypes;
import com.redhat.ceylon.compiler.metadata.java.TypeParameter;
import com.redhat.ceylon.compiler.metadata.java.TypeParameters;
import com.redhat.ceylon.compiler.metadata.java.Variance;

@Ceylon
@TypeParameters(@TypeParameter(value = "Other", variance = Variance.OUT,
    		       satisfies="ceylon.language.Ordinal<Other>"))
@SatisfiedTypes({"ceylon.language.Equality"})
public interface Ordinal<Other extends Ordinal<? extends Other>> {
    public Other getSuccessor();
    public Other getPredecessor();
}
