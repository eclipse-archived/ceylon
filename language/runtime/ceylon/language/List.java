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
@SatisfiedTypes({"ceylon.language.Collection<Element>",
                 "ceylon.language.Correspondence<ceylon.language.Integer,Element>",
                 "ceylon.language.Ranged<ceylon.language.Integer,ceylon.language.List<Element>>",
                 "ceylon.language.Cloneable<ceylon.language.List<Element>>"})
public interface List<Element> 
        extends Collection<Element>, 
                Correspondence<Integer,Element>,
                Ranged<Integer,List<? extends Element>> {

    @TypeInfo("ceylon.language.Nothing|ceylon.language.Integer")
    public Integer getLastIndex();
    
    public boolean defines(@Name("index") Integer index);
    
    @Override
    @TypeInfo("ceylon.language.Empty|ceylon.language.Sequence<Element>")
    public List<? extends Element> span(@Name("from") Integer from, 
            @TypeInfo("ceylon.language.Nothing|ceylon.language.Integer")
            @Name("to") Integer to);
    
    @Override
    @TypeInfo("ceylon.language.Empty|ceylon.language.Sequence<Element>")
    public List<? extends Element> segment(@Name("from") Integer from, 
            @Name("length") Integer length);
    
}
