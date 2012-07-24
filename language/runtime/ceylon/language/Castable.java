package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Annotation;
import com.redhat.ceylon.compiler.java.metadata.Annotations;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.metadata.Variance;

@Ceylon(major = 2)
@TypeParameters(@TypeParameter(value = "Types", variance = Variance.IN))
public interface Castable<Types> {
    @Annotations(@Annotation("formal"))
    <CastValue extends Types> CastValue castTo();
}
