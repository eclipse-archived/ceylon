package ceylon.language;

import com.redhat.ceylon.compiler.metadata.java.TypeParameter;
import com.redhat.ceylon.compiler.metadata.java.TypeParameters;
import com.redhat.ceylon.compiler.metadata.java.Variance;

@TypeParameters({
    @TypeParameter(value = "Types", variance = Variance.IN)
})
public interface Castable<Types> {
    <CastValue extends Types> CastValue as();
}
