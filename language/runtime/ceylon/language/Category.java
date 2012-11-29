package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Annotation;
import com.redhat.ceylon.compiler.java.metadata.Annotations;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.Sequenced;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;

@Ceylon(major = 3)
public interface Category {
    
    @Annotations(@Annotation("formal"))
    public boolean contains(@Name("element") java.lang.Object element);
    
    @Annotations(@Annotation("default"))
    public boolean containsEvery(@Sequenced @Name("elements") 
    @TypeInfo("ceylon.language::Sequential<ceylon.language::Object>")
    Sequential<?> elements);
    @Ignore
    public boolean containsEvery();
    @Ignore
    public Sequential<?> containsEvery$elements();

    @Annotations(@Annotation("default"))
    public boolean containsAny(@Sequenced @Name("elements") 
    @TypeInfo("ceylon.language::Sequential<ceylon.language::Object>")
    Sequential<?> elements);
    @Ignore
    public boolean containsAny();
    @Ignore
    public Sequential<?> containsAny$elements();

}
