package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Alias;
import com.redhat.ceylon.compiler.java.metadata.Annotation;
import com.redhat.ceylon.compiler.java.metadata.Annotations;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.metadata.Variance;

@Ceylon(major = 3)
@Alias("ceylon.language::ContainerWithFirstElement<Element,ceylon.language::Nothing>")
@Annotations({@Annotation(value = "doc", arguments = {"\"Abstract supertype of objects which may or may not\ncontain one of more other values, called *elements*.\""}), @Annotation(value = "see", arguments = {"Category"}), @Annotation(value = "by", arguments = {"\"Gavin\""}), @Annotation("shared")})
@TypeParameters({@TypeParameter(value = "Element", variance = Variance.OUT, satisfies = {}, caseTypes = {})})
public interface Container<Element> {
}
