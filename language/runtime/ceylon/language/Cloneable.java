package ceylon.language;

import com.redhat.ceylon.compiler.metadata.java.Ceylon;
import com.redhat.ceylon.compiler.metadata.java.TypeParameter;
import com.redhat.ceylon.compiler.metadata.java.TypeParameters;
import com.redhat.ceylon.compiler.metadata.java.Variance;

@Ceylon
@TypeParameters(@TypeParameter(value="Clone", variance=Variance.OUT,
        satisfies="ceylon.language.Cloneable<Clone>"))
public interface Cloneable<Clone extends Cloneable<? extends Clone>> {
    public Clone getClone();
}
