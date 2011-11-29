package ceylon.language;

import static com.redhat.ceylon.compiler.metadata.java.Variance.OUT;

import com.redhat.ceylon.compiler.metadata.java.TypeParameter;
import com.redhat.ceylon.compiler.metadata.java.TypeParameters;

@TypeParameters({@TypeParameter(value="Inverse", variance=OUT)})
public interface Invertable<Inverse> {
    public Inverse getPositiveValue();
    public Inverse getNegativeValue();
}
