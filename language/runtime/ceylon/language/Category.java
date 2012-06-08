package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Annotation;
import com.redhat.ceylon.compiler.java.metadata.Annotations;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.Sequenced;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;

@Ceylon
public interface Category {
    
    @Annotations(@Annotation("formal"))
    public boolean contains(@Name("element") java.lang.Object element);
    
    @Annotations(@Annotation("default"))
    public boolean containsEvery(@Sequenced @Name("elements") 
    @TypeInfo("ceylon.language.Iterable<ceylon.language.Object>")
    Iterable<?> elements);
    @Ignore
    public boolean containsEvery();
    @Ignore
    public Iterable<?> containsEvery$elements();

    @Annotations(@Annotation("default"))
    public boolean containsAny(@Sequenced @Name("elements") 
    @TypeInfo("ceylon.language.Iterable<ceylon.language.Object>")
    Iterable<?> elements);
    @Ignore
    public boolean containsAny();
    @Ignore
    public Iterable<?> containsAny$elements();

}
