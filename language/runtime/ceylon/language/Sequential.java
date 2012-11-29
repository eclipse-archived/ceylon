package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Annotation;
import com.redhat.ceylon.compiler.java.metadata.Annotations;
import com.redhat.ceylon.compiler.java.metadata.CaseTypes;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.SatisfiedTypes;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.metadata.Variance;

@Ceylon(major = 3)
@TypeParameters(@TypeParameter(value = "Element", variance = Variance.OUT))
@SatisfiedTypes({
    "ceylon.language::List<Element>",
    "ceylon.language::FixedSized<Element>",
    "ceylon.language::Ranged<ceylon.language::Integer,ceylon.language::Sequential<Element>>",
    "ceylon.language::Cloneable<ceylon.language::Sequential<Element>>"
})
@CaseTypes({"ceylon.language::Empty", "ceylon.language::Sequence<Element>"})
public interface Sequential<Element> 
        extends List<Element>, FixedSized<Element> {
	
    @Annotations({@Annotation("actual"), @Annotation("formal")})
    @Override
    @TypeInfo("ceylon.language::Sequential<Element>")
    public Sequential<? extends Element> getReversed();
    
}
