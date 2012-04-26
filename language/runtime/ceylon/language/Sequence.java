 package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.SatisfiedTypes;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.metadata.Variance;

@Ceylon
@TypeParameters(@TypeParameter(value = "Element", variance = Variance.OUT))
@SatisfiedTypes({
    "ceylon.language.List<Element>",
    "ceylon.language.Some<Element>",
    "ceylon.language.Cloneable<ceylon.language.Sequence<Element>>",
    "ceylon.language.Ranged<ceylon.language.Integer,ceylon.language.Empty|ceylon.language.Sequence<Element>>"
})
public interface Sequence<Element> 
        extends List<Element>, Some<Element> {
    
    @Override
    @TypeInfo("ceylon.language.Integer")
    public Integer getLastIndex();
    
    @Override
    public Element getFirst();
    
    public Element getLast();

    @Override
    @TypeInfo("ceylon.language.Empty|ceylon.language.Sequence<Element>")
    public FixedSized<? extends Element> getRest();
    
}
