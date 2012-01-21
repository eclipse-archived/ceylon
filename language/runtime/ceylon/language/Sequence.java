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
@SatisfiedTypes({
    "ceylon.language.List<Element>",
    "ceylon.language.Ranged<ceylon.language.Integer,ceylon.language.Empty|ceylon.language.Sequence<Element>>",
    "ceylon.language.Cloneable<ceylon.language.Sequence<Element>>"
})
public interface Sequence<Element> 
        extends List<Element> {
    
    @TypeInfo("ceylon.language.Integer")
    public long getLastIndex();
    
    @TypeInfo("Element")
    public Element getFirst();
    
    @TypeInfo("ceylon.language.Empty|ceylon.language.Sequence<Element>")
    public Iterable<? extends Element> getRest();
    
    public boolean getEmpty();
    
    @TypeInfo("ceylon.language.Integer")
    public long getSize();
    
    public Element getLast();
    
    public boolean defines(@Name("index") Integer index);
    
    //this depends on efficient implementation of rest
    /*
    shared actual default object iterator 
            extends Object()
            satisfies Iterator<Element> {
        shared actual Element head { 
            return first;
        }
        shared actual Iterator<Element> tail {
            return rest.iterator;
        }
    }
    */
    
    @TypeInfo("ceylon.language.Iterator<Element>")
    public Iterator<? extends Element> getIterator();
    
    @Override
    @TypeInfo("ceylon.language.Empty|ceylon.language.Sequence<Element>")
    public List<? extends Element> span(@Name("from") Integer from, 
    		@TypeInfo("ceylon.language.Nothing|ceylon.language.Integer")
    		@Name("to") Integer to);
    
    @Override
    @TypeInfo("ceylon.language.Empty|ceylon.language.Sequence<Element>")
    public List<? extends Element> segment(@Name("from") Integer from, 
    		@Name("length") Integer length);
    
    public java.lang.String toString();

    @Ignore
    public static final class Sequence$impl {
        public static <Element> boolean getEmpty(Sequence<Element> $this){
            return false;
        }

        public static <Element> long getSize(Sequence<Element> $this){
            return $this.getLastIndex()+1;
        }

        public static <Element> Element getLast(Sequence<Element> $this){
            Element x = $this.item(Integer.instance($this.getLastIndex()));
            if (x != null) {
                return x;
            }
            else {
                return $this.getFirst(); //actually never occurs
            } 
        }

        public static <Element> boolean defines(Sequence<Element> $this, Integer index){
            return index.longValue() <= $this.getLastIndex();
        }

        public static <Element> Iterator<? extends Element> getIterator(final Sequence<Element> $this){
            class SequenceIterator<Element> implements Iterator<Element> {
                private long from;
                SequenceIterator(){
                    this.from = from;
                }
                @TypeInfo("Element|ceylon.language.Finished")
                public final java.lang.Object next() { 
                    if (from <= $this.getLastIndex()) {
                        return $this.item(Integer.instance(from++));
                    } else {
                        return exhausted.getExhausted();
                    }
                }
                public final java.lang.String toString() {
                    return "SequenceIterator";
                }
            }
            
            return new SequenceIterator<Element>();
        }
        
        public static <Element> java.lang.String toString(Sequence<Element> $this) {
            java.lang.StringBuilder result = new java.lang.StringBuilder("{ ");
            java.lang.Object elem;
            for (Iterator<? extends Element> iter=$this.getIterator(); !((elem = iter.next()) instanceof Finished);) {
                result.append(elem).append(", ");
            }
            result.setLength(result.length()-2);
            result.append(" }");
            return result.toString();
        }
        
    }    
}
