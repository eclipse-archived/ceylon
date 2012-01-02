package ceylon.language;

import static com.redhat.ceylon.compiler.java.metadata.Variance.OUT;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;

@Ceylon
@TypeParameters(@TypeParameter(value="Inverse", variance=OUT))
public interface Invertable<Inverse> {
    public Inverse getPositiveValue();
    public Inverse getNegativeValue();
}
