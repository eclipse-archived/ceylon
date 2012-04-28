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
                Ranged<Integer, List<? extends Element>> {

    @TypeInfo("ceylon.language.Nothing|ceylon.language.Integer")
    public Integer getLastIndex();
    
    @Override
    public long getSize();
    
    public boolean defines(@Name("index") Integer key);
    
    @TypeInfo("ceylon.language.Nothing|Element")
    @Override
    public Element item(@Name("index") Integer key);
    
    @TypeInfo("ceylon.language.Iterator<Element>")
    @Override
    public Iterator<? extends Element> getIterator();
    
    @Override
    public List<? extends Element> span(@Name("from") Integer from, 
            @TypeInfo("ceylon.language.Nothing|ceylon.language.Integer")
            @Name("to") Integer to);
    
    @Override
    public List<? extends Element> segment(@Name("from") Integer from, 
            @Name("length") Integer length);
    
    @Override
    public boolean equals(@Name("that") @TypeInfo("ceylon.language.Object")
    java.lang.Object that);
    
    @Override
    public int hashCode();
    
    @Override
    public java.lang.String toString();
    
}
