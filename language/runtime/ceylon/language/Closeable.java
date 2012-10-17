package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Annotation;
import com.redhat.ceylon.compiler.java.metadata.Annotations;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;

@Ceylon(major = 3)
public interface Closeable {

    @Annotations(@Annotation("formal"))
    @TypeInfo("ceylon.language::Void")
    java.lang.Object open();

    @Annotations(@Annotation("formal"))
    @TypeInfo("ceylon.language::Void")
    java.lang.Object close(@Name("exception") 
    @TypeInfo("ceylon.language::Nothing|ceylon.language::Exception") 
    java.lang.Throwable exception);
}
