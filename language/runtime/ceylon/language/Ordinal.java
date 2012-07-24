package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Annotation;
import com.redhat.ceylon.compiler.java.metadata.Annotations;
import com.redhat.ceylon.compiler.java.metadata.CaseTypes;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.metadata.Variance;

@Ceylon(major = 2)
@TypeParameters(@TypeParameter(value = "Other", variance = Variance.OUT,
    		       satisfies="ceylon.language.Ordinal<Other>"))
@CaseTypes(of = "Other")
public interface Ordinal<Other extends Ordinal<? extends Other>> {

    @Annotations(@Annotation("formal"))
    public Other getSuccessor();

    @Annotations(@Annotation("formal"))
    public Other getPredecessor();
}
