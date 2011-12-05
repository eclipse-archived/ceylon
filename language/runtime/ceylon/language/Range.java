package ceylon.language;

import com.redhat.ceylon.compiler.metadata.java.Class;
import com.redhat.ceylon.compiler.metadata.java.Name;
import com.redhat.ceylon.compiler.metadata.java.SatisfiedTypes;
import com.redhat.ceylon.compiler.metadata.java.Sequenced;
import com.redhat.ceylon.compiler.metadata.java.TypeInfo;

@Class(extendsType="ceylon.language.Object")
@SatisfiedTypes({"ceylon.language.Sequence<Element>", 
	             "ceylon.language.Category",
	             "ceylon.language.Equality"})
public class Range<Element extends Comparable<? super Element> & Ordinal<? extends Element>>
    extends Object
    implements Sequence<Element>, Category {
    
    private Element first;
    private Element last;
    private long index;
    private long size;
    
    public Range(@Name("first") Element first, 
    		     @Name("last") Element last) {
        this.first = first;
        this.last = last;
        this.index = 0;
        Element x = first;
        while(!pastEnd(x)){
            ++this.index;
            x = next(x);
        }
        this.size = index;
    }
    
    @Override
    public final Element getFirst(){
        return first;
    }
    
    @Override
    public final Element getLast(){
        return last;
    }
    
    @Override
    public final java.lang.String toString(){
        return first.toString() + ".." + last.toString();
    }
    
    public final boolean getDecreasing(){
        return last.smallerThan(first);
    }
    
    private final boolean pastEnd(Element x){
        if(getDecreasing())
            return x.smallerThan(last);
        else
            return x.largerThan(last);
    }
    
    private final Element next(Element x){
        if(getDecreasing())
            return x.getPredecessor();
        else
            return x.getSuccessor();
    }

    @Override
    @TypeInfo("ceylon.language.Natural")
    public final long getSize(){
        return size;
    }

    @Override
    @TypeInfo("ceylon.language.Natural")
    public final long getLastIndex(){
        return size - 1;
    }

    @Override
    public Sequence<? extends Element> getRest() {
        return new Range<Element>(next(getFirst()), getLast());
    }

    @TypeInfo("ceylon.language.Nothing|Element")
    @Override
    public Element item(@Name("n") Natural n) {
        long index = 0;
        Element x = first;
        while(index < n.longValue() && !pastEnd(x)){
            ++index;
            x=next(x);
        }
        if(pastEnd(x))
            return null;
        else
            return x;
    }

    @Override
    @TypeInfo("ceylon.language.Iterator<Element>")
	public Iterator<Element> getIterator() {
        class RangeIterator implements Iterator<Element> {
        	Element x;
            public RangeIterator(Element x) {
            	this.x = x;
            }
            public Element getHead() {
                return x;
            }
            @TypeInfo("ceylon.language.Nothing|ceylon.language.Iterator<Element>")
            public Iterator<Element> getTail() {
                 Element next = next(x);
                 if(pastEnd(next))
                     return null;
                 return new RangeIterator(next);
             }
        }
        return new RangeIterator(first);
    }

    @Override
    public final boolean contains(@Name("element") 
    @TypeInfo("ceylon.language.Equality") Object value) {
        // FIXME
    	try {
	        if(value != null /*&& value instanceof Element*/)
	            return includes((Element)value);
	        else
	            return false;
    	}
    	catch (ClassCastException cce) { //ugly hack
    		return false;
    	}
    }

    public final boolean includes(@Name("x") Element x){
        if(getDecreasing()){
            return x.asSmallAs(first) && x.asLargeAs(last);
        }else{
            return x.asLargeAs(first) && x.asSmallAs(last);
        }
    }
    
    @TypeInfo("ceylon.language.Empty|ceylon.language.Sequence<Element>")
    public final Iterable<Element> by(@Name("stepSize") 
    @TypeInfo("ceylon.language.Natural") long stepSize){
    	//TODO!!!!
        throw new UnsupportedOperationException();
    }
    
    @Override
    public final boolean equals(@Name("that") 
    @TypeInfo("ceylon.language.Equality") java.lang.Object that){
        if(that instanceof Range) {
            Range<Element> $that = (Range<Element>) that;
            return $that.getFirst().equals(getFirst()) && $that.getLast().equals(getLast());
        }
        else {
            return false;
        }
    }
    
    @Override
    public int hashCode(){
        return getFirst().hashCode()/2+getLast().hashCode()/2;// TODO: really should be xor
    }
    
    @Override
    public Range<Element> getClone() {
        return this;
    }
    
    /*@Override
    public Ordered<Element> segment(long from, long length) {
        throw new RuntimeException("Not implemented"); //todo!
    }*/

    @Override
    public Category getKeys() {
        return Correspondence$impl.getKeys(this);
    }

    @Override
    public boolean definesEvery(@Sequenced @Name("keys") 
    @TypeInfo("ceylon.language.Empty|ceylon.language.Sequence<ceylon.language.Natural>")
    Iterable<? extends Natural> keys) {
        return Correspondence$impl.definesEvery(this, keys);
    }

    @Override
    public boolean definesAny(@Sequenced @Name("keys") 
    @TypeInfo("ceylon.language.Empty|ceylon.language.Sequence<ceylon.language.Natural>")
    Iterable<? extends Natural> keys) {
        return Correspondence$impl.definesAny(this, keys);
    }

    @Override
    @TypeInfo("ceylon.language.Empty|ceylon.language.Sequence<Element|ceylon.language.Nothing>")
    public Iterable<? extends Element> items(@Sequenced @Name("keys") 
    @TypeInfo("ceylon.language.Empty|ceylon.language.Sequence<ceylon.language.Natural")
    Iterable<? extends Natural> keys) {
        return Correspondence$impl.items(this, keys);
    }

    //TODO: @TypeInfo
    @Override
    public boolean containsEvery(@Sequenced @Name("elements") 
    @TypeInfo("ceylon.language.Empty|ceylon.language.Sequence<ceylon.language.Equality>")
    Iterable<? extends Object> elements) {
        return Category$impl.containsEvery(this, elements);
    }

    //TODO: @TypeInfo
    @Override
    public boolean containsAny(@Sequenced @Name("elements") 
    @TypeInfo("ceylon.language.Empty|ceylon.language.Sequence<ceylon.language.Equality>")
    Iterable<? extends Object> elements) {
        return Category$impl.containsAny(this, elements);
    }

    @Override
    public boolean getEmpty() {
        return Sequence$impl.getEmpty(this);
    }

    @Override
    public boolean defines(@Name("index") Natural index) {
        return Sequence$impl.defines(this, index);
    }

    @Override
    @TypeInfo("ceylon.language.Empty|ceylon.language.Sequence<Element>")
    public Iterable<? extends Element> segment(
    		@Name("from") Natural from, @Name("length") Natural length) {
    	if (from.longValue()>getLastIndex()||length.longValue()==0) 
    		return $empty.getEmpty();
    	if (from.longValue()+length.longValue()>getSize()) length = 
    			Natural.instance(getSize()-from.longValue());
    	Element begin = first;
    	for (int i=0; i<from.longValue(); i++) {
    		begin = begin.getSuccessor();
    	}
    	Element end = first;
    	for (int i=0; i<length.longValue(); i++) {
    		end = end.getSuccessor();
    	}
    	return new Range<Element>(begin, end);
    }
    
    @Override
    @TypeInfo("ceylon.language.Empty|ceylon.language.Sequence<Element>")
    public Iterable<? extends Element> span(
    		@Name("from") Natural from, @Name("to") Natural to) {
    	if (from.longValue()>getLastIndex()||to.longValue()<from.longValue()) 
    		return $empty.getEmpty();
    	return this;
    }

}
