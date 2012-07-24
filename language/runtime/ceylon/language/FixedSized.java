package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.CaseTypes;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.SatisfiedTypes;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.metadata.Variance;

@Ceylon(major = 2)
@CaseTypes({"ceylon.language.Some<Element>", 
            "ceylon.language.None<Element>"})
@SatisfiedTypes("ceylon.language.Collection<Element>")
@TypeParameters(@TypeParameter(value = "Element", variance = Variance.OUT))
public interface FixedSized<Element> 
        extends Collection<Element> {}
