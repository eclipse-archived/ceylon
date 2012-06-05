package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.SatisfiedTypes;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.metadata.Variance;

@Ceylon
@TypeParameters(@TypeParameter(value = "Element", variance = Variance.OUT))
@SatisfiedTypes({"ceylon.language.Iterable<Element>",
        "ceylon.language.Sized",
        "ceylon.language.Category",
        "ceylon.language.Cloneable<Collection<Element>>"})
public interface Collection<Element> 
        extends Iterable<Element>, Sized, Category,
                Cloneable<Collection<? extends Element>> {
    
    @Override
    public boolean getEmpty();

    @Override
    public boolean contains(@Name("element") @TypeInfo("ceylon.language.Object")
            java.lang.Object element);
    
    public long count(@Name("element") @TypeInfo("ceylon.language.Object")
            java.lang.Object element);


}
