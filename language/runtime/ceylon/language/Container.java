package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Annotation;
import com.redhat.ceylon.compiler.java.metadata.Annotations;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;

@Ceylon
public interface Container {
    @Annotations(@Annotation("formal"))
    public boolean getEmpty();
}
