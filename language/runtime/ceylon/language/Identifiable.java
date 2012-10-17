package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Annotation;
import com.redhat.ceylon.compiler.java.metadata.Annotations;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;

@Ceylon(major = 3)
public interface Identifiable {

    @Annotations({@Annotation("default"), @Annotation("actual")})
    @Override
    public boolean equals(@Name("that") @TypeInfo("ceylon.language::Object") 
    java.lang.Object that);
    
    @Annotations({@Annotation("default"), @Annotation("actual")})
    @Override
    public int hashCode();
    
}