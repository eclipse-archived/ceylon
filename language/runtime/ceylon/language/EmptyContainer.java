package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Alias;
import com.redhat.ceylon.compiler.java.metadata.Annotation;
import com.redhat.ceylon.compiler.java.metadata.Annotations;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;

@Ceylon(major = 3)
@Alias("test::ContainerWithFirstElement<ceylon.language::Bottom,ceylon.language::Nothing>")
@Annotations({@Annotation("shared")})
public interface EmptyContainer {
}
