package ceylon.language;

import com.redhat.ceylon.compiler.metadata.java.Ceylon;
import com.redhat.ceylon.compiler.metadata.java.TypeParameter;
import com.redhat.ceylon.compiler.metadata.java.TypeParameters;
import com.redhat.ceylon.compiler.metadata.java.Variance;

@Ceylon
@TypeParameters({
    @TypeParameter(value = "Other", variance = Variance.OUT)
})
public interface Ordinal<Other extends Ordinal<Other>>
        extends Equality {
    public Other getSuccessor();
    public Other getPredecessor();
}
