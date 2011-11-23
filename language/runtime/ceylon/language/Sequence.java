 package ceylon.language;

import com.redhat.ceylon.compiler.metadata.java.Ceylon;
import com.redhat.ceylon.compiler.metadata.java.Name;
import com.redhat.ceylon.compiler.metadata.java.TypeInfo;
import com.redhat.ceylon.compiler.metadata.java.TypeParameter;
import com.redhat.ceylon.compiler.metadata.java.TypeParameters;
import com.redhat.ceylon.compiler.metadata.java.Variance;

@Ceylon
@TypeParameters({
    @TypeParameter(value = "Element", variance = Variance.OUT)
 })
 public interface Sequence<Element> 
    extends Correspondence<Natural, Element>, Ordered<Element>, 
        Sized, Cloneable<Sequence<Element>> {
    
    @TypeInfo("ceylon.language.Natural")
    public long getLastIndex();
    
    public Element getFirst();
    
    @TypeInfo("ceylon.language.Empty|ceylon.language.Sequence<Element|ceylon.language.Nothing>")
    public Iterable getRest();
    
    public boolean getEmpty();
    
    @TypeInfo("ceylon.language.Natural")
    public long getSize();
    
    public Element getLast();
    
    public boolean defines(@Name("index") Natural index);
    
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
    public Iterator<Element> getIterator();
    
    class SequenceIterator<Element> extends Object
    implements Iterator<Element> {
        private long from;
        private Sequence<Element> $this;
        SequenceIterator(Sequence<Element> $this, long from){
            this.from = from;
            this.$this = $this;
        }
        @TypeInfo("ceylon.language.Nothing|Element")
        public final Element getHead() { 
            return $this.item(Natural.instance(from));
        }
        @TypeInfo("ceylon.language.Nothing|ceylon.language.Iterator<Element>")
        public final Iterator<Element> getTail() {
            return new SequenceIterator<Element>($this, from+1);
        }
        public final java.lang.String toString() {
            return "SequenceIterator";
        }
    }
    
    @TypeInfo("ceylon.language.Empty|ceylon.language.Sequence<Element|ceylon.language.Nothing>")
    public Sequence<? extends Element> span(long from, long to);
    
    /*@Override
    public Ordered<Element> segment(long from, long length);*/

    public java.lang.String toString();

}
