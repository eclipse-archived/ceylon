package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
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
    
    @Ignore
    public static final class List$impl {
        public static <Element> boolean getEmpty(Sequence<Element> $this){
            return false;
        }

        public static <Element> long getSize(Sequence<Element> $this){
            Integer lastIndex = $this.getLastIndex();
            return lastIndex==null ? 0 : lastIndex.longValue()+1;
        }

        public static <Element> boolean defines(Sequence<Element> $this, Integer index){
            Integer lastIndex = $this.getLastIndex();
            return lastIndex==null ? false : index.longValue() <= lastIndex.longValue();
        }

        public static <Element> Iterator<? extends Element> getIterator(final Sequence<Element> $this){
            class ListIterator implements Iterator<Element> {
                private long index=0;
                public final java.lang.Object next() { 
                    Integer lastIndex = $this.getLastIndex();
                    if (lastIndex!=null && index <= lastIndex.longValue()) {
                        return $this.item(Integer.instance(index++));
                    } 
                    else {
                        return exhausted.getExhausted();
                    }
                }
                public final java.lang.String toString() {
                    return "listIterator";
                }
            }
            return new ListIterator();
        }
        
    }    
}
