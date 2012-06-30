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
@TypeParameters({@TypeParameter(value = "Element", variance = Variance.OUT),
	             @TypeParameter(value = "Null", variance = Variance.OUT, 
	                            satisfies="ceylon.language.Nothing")})
@SatisfiedTypes("ceylon.language.Container")
public interface ContainerWithFirstElement<Element,Null> extends Container {
	@Annotations(@Annotation("formal"))
	@TypeInfo("Null|Element")
    public Element getFirst();
}
