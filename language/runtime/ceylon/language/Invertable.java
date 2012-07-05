package ceylon.language;

import static com.redhat.ceylon.compiler.java.metadata.Variance.OUT;

import com.redhat.ceylon.compiler.java.metadata.Annotation;
import com.redhat.ceylon.compiler.java.metadata.Annotations;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;

@Ceylon(major = 2)
@TypeParameters(@TypeParameter(value="Inverse", variance=OUT))
public interface Invertable<Inverse> {

    @Annotations(@Annotation("formal"))
    public Inverse getPositiveValue();
    
    @Annotations(@Annotation("formal"))
    public Inverse getNegativeValue();
}
