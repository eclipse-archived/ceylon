package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Annotation;
import com.redhat.ceylon.compiler.java.metadata.Annotations;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.SatisfiedTypes;

@Ceylon(major = 1)
@SatisfiedTypes("ceylon.language.Container")
public interface Sized extends Container {

    @Annotations(@Annotation("formal"))
    public long getSize();
    
    @Annotations({@Annotation("actual"),@Annotation("default")})
    public boolean getEmpty();
}
