package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Annotation;
import com.redhat.ceylon.compiler.java.metadata.Annotations;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.SatisfiedTypes;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.metadata.Variance;

@Ceylon(major = 1)
@TypeParameters(@TypeParameter(value = "Element", variance = Variance.OUT))
@SatisfiedTypes({"ceylon.language.FixedSized<Element>", 
	             "ceylon.language.ContainerWithFirstElement<Element>"})
public interface Some<Element> extends FixedSized<Element> {
    
    @Annotations({@Annotation("actual"), @Annotation("default")})
    @TypeInfo("Element")
    @Override 
    public Element getFirst();
    
    @Annotations(@Annotation("actual"))
    @Override
    public boolean getEmpty();
    
    @Annotations({@Annotation("formal"), @Annotation("actual")})
    @TypeInfo("ceylon.language.FixedSized<Element>")
    public FixedSized<? extends Element> getRest();

}
