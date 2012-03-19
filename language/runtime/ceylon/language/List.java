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
                Ranged<Integer, java.lang.Object> {

    @TypeInfo("ceylon.language.Nothing|ceylon.language.Integer")
    public Integer getLastIndex();
    
    @Override
    public long getSize();
    
    public boolean defines(@Name("key") Integer key);
    
    @TypeInfo("ceylon.language.Nothing|Element")
    @Override
    public Element item(@Name("key") Integer key);
    
    @TypeInfo("ceylon.language.Iterator<Element>")
    @Override
    public Iterator<? extends Element> getIterator();
    
    @Override
    @TypeInfo("ceylon.language.Empty|ceylon.language.Sequence<Element>")
    public Iterable<? extends Element> span(@Name("from") Integer from, 
            @TypeInfo("ceylon.language.Nothing|ceylon.language.Integer")
            @Name("to") Integer to);
    
    @Override
    @TypeInfo("ceylon.language.Empty|ceylon.language.Sequence<Element>")
    public Iterable<? extends Element> segment(@Name("from") Integer from, 
            @Name("length") Integer length);
    
    @Override
    public boolean equals(@Name("that") @TypeInfo("ceylon.language.Object")
    java.lang.Object that);
    
    @Override
    public int hashCode();
    
    @Override
    public java.lang.String toString();
    
    @Ignore
    public static final class List$impl {
        public static <Element> boolean getEmpty(List<Element> $this){
            return false;
        }

        public static <Element> long getSize(List<Element> $this){
            Integer lastIndex = $this.getLastIndex();
            return lastIndex==null ? 0 : lastIndex.longValue()+1;
        }

        public static <Element> boolean defines(List<Element> $this, Integer key){
            Integer lastIndex = $this.getLastIndex();
            return lastIndex==null ? false : key.longValue() <= lastIndex.longValue();
        }

        public static <Element> Iterator<? extends Element> getIterator(final List<Element> $this){
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
        
        public static <Element> boolean equals(final List<Element> $this, java.lang.Object that) {
        	if (that instanceof List) {
        		List other = (List) that;
        		if (other.getSize()==$this.getSize()) {
        			for (int i=0; i<$this.getSize(); i++) {
        				Element x = $this.item(Integer.instance(i));
        				java.lang.Object y = ((List) that).item(Integer.instance(i));
        				if (x==y || x!=null && y!=null && x.equals(y)) {
        					continue;
        				}
        				return false;
        			}
        			return true;
        		}
        	}
        	return false;
        }
        
        public static <Element> int hashCode(final List<Element> $this) {
            return (int) $this.getSize();
        }
        
        public static <Element> java.lang.String toString(List<Element> $this) {
            java.lang.StringBuilder result = new java.lang.StringBuilder("{ ");
            java.lang.Object elem;
            for (Iterator<? extends Element> iter=$this.getIterator(); !((elem = iter.next()) instanceof Finished);) {
                if (result.length() > 2) {
                    result.append(", ");
                }
                result.append(elem);
            }
            result.append(" }");
            return result.toString();
        }
    }    
}
