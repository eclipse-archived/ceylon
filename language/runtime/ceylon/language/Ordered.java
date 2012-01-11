package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.SatisfiedTypes;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.metadata.Variance;

@Ceylon
@TypeParameters(@TypeParameter(value = "Element", variance = Variance.OUT))
@SatisfiedTypes("ceylon.language.Iterable<Element>")
public interface Ordered<Element> extends Iterable<Element> {

	@Override
    public boolean getEmpty();
    
    @TypeInfo("Element|ceylon.language.Nothing")
    public Element getFirst();
    
    //public Ordered<Element> segment(long skipping, long finishingAfter);
    
    @Ignore
    public static final class Ordered$impl {
        public static <Element> boolean getEmpty(Ordered<Element> $this){
            return $this.getIterator().next() == $finished.getFinished();
        }

        public static <Element> Element getFirst(Ordered<Element> $this){
            return (Element) $this.getIterator().next();
        }

        /*public static <Element> Ordered<Element> segment(Ordered<Element> $this, long skipping, final long finishingAfter){
            if(finishingAfter == 0){
                return new ArraySequence<Element>();
            }else{
                Iterator<Element> it = $this.getIterator();
                long skipped = 0;
                while(++skipped <= skipping){
                    it = it.getTail();
                }
                final Iterator<Element> capturedIt = it;
                
                class SegmentIterator implements Iterator<Element>{

                    private Iterator<Element> iterator;
                    private long remaining;

                    SegmentIterator(Iterator<Element> iterator, long remaining){
                        this.iterator = iterator;
                        this.remaining = remaining;
                    }
                    
                    @Override
                    public Element getHead() {
                        if(remaining == 0){
                            return null;
                        }else{
                            return iterator.getHead();
                        }
                    }

                    @Override
                    public Iterator<Element> getTail() {
                        return new SegmentIterator(iterator.getTail(), remaining-1);
                    }
                    
                }
                
                Ordered<Element> segment = new Ordered<Element>(){

                    @Override
                    public Iterator<Element> getIterator() {
                        return new SegmentIterator(capturedIt, finishingAfter);
                    }

                    @Override
                    public boolean getEmpty() {
                        return Ordered$Impl.getEmpty(this);
                    }

                    @Override
                    public Element getFirst() {
                        return Ordered$Impl.getFirst(this);
                    }

                    @Override
                    public Ordered<Element> segment(long skipping,
                            long finishingAfter) {
                        return Ordered$Impl.segment(this, skipping, finishingAfter);
                    }

                    @Override
                    public java.lang.String toString(){
                        return "segment"; //todo
                    }
                };
                
                return segment;
            }
        }*/

    }
}
