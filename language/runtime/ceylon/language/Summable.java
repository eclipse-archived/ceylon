package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Annotation;
import com.redhat.ceylon.compiler.java.metadata.Annotations;
import com.redhat.ceylon.compiler.java.metadata.CaseTypes;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;

@Ceylon(major = 3)
@TypeParameters(@TypeParameter(value = "Other",
        satisfies="ceylon.language.Summable<Other>"))
@CaseTypes(of = "Other")
public interface Summable<Other extends Summable<Other>> {

    @Annotations(@Annotation("formal"))
    public Other plus(@Name("other") Other number);
}
