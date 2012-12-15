package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Annotation;
import com.redhat.ceylon.compiler.java.metadata.Annotations;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.SatisfiedTypes;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.metadata.Variance;

@Ceylon(major = 3)
@TypeParameters(@TypeParameter(value = "Element", variance = Variance.OUT))
@SatisfiedTypes({"ceylon.language::Iterable<Element>",
        "ceylon.language::Category",
        "ceylon.language::Cloneable<ceylon.language::Collection<Element>>"})
public interface Collection<Element> 
        extends Iterable<Element>, Category,
                Cloneable<Collection<? extends Element>> {
    
    @Annotations({@Annotation("actual"), @Annotation("default")})
    @Override
    public boolean getEmpty();

    @Annotations({@Annotation("actual"), @Annotation("default")})
    @Override
    public boolean contains(@Name("element") @TypeInfo("ceylon.language::Object")
            java.lang.Object element);

    @Annotations({@Annotation("actual"), @Annotation("default")})
    @Override
    public java.lang.String toString();

}
