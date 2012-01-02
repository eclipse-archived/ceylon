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
@SatisfiedTypes({
    "ceylon.language.Correspondence<ceylon.language.Integer,Element>",
    "ceylon.language.Ordered<Element>",
    "ceylon.language.Sized",
    "ceylon.language.Cloneable<ceylon.language.Sequence<Element>>",
    "ceylon.language.Ranged<ceylon.language.Integer,ceylon.language.Empty|ceylon.language.Sequence<Element>>"
})
public interface Sequence<Element> 
    extends Correspondence<Integer, Element>, Ordered<Element>, 
        Sized, Cloneable<Sequence<Element>>, 
        Ranged<Integer,Iterable<? extends Element>> {
    
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
    
    class SequenceIterator<Element>
            implements Iterator<Element> {
        private long from;
        private Sequence<Element> $this;
        SequenceIterator(Sequence<Element> $this, long from){
            this.from = from;
            this.$this = $this;
        }
        @TypeInfo("ceylon.language.Nothing|Element")
        public final Element getHead() { 
            return $this.item(Integer.instance(from));
        }
        @TypeInfo("ceylon.language.Nothing|ceylon.language.Iterator<Element>")
        public final Iterator<Element> getTail() {
            return from<$this.getLastIndex() ? 
            		new SequenceIterator<Element>($this, from+1) : null;
        }
        public final java.lang.String toString() {
            return "SequenceIterator";
        }
    }
    
    @Override
    @TypeInfo("ceylon.language.Empty|ceylon.language.Sequence<Element>")
    public Iterable<? extends Element> span(@Name("from") Integer from, 
    		@TypeInfo("ceylon.language.Nothing|ceylon.language.Integer")
    		@Name("to") Integer to);
    
    @Override
    @TypeInfo("ceylon.language.Empty|ceylon.language.Sequence<Element>")
    public Iterable<? extends Element> segment(@Name("from") Integer from, 
    		@Name("length") Integer length);
    
    public java.lang.String toString();

}
