package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Alias;
import com.redhat.ceylon.compiler.java.metadata.Annotation;
import com.redhat.ceylon.compiler.java.metadata.Annotations;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.metadata.Variance;

@Ceylon(major = 3)
@Alias("test::ContainerWithFirstElement<Element,ceylon.language::Bottom>")
@Annotations({@Annotation("shared")})
@TypeParameters({@TypeParameter(value = "Element", variance = Variance.OUT, satisfies = {}, caseTypes = {})})
public interface NonemptyContainer<Element> {
}
