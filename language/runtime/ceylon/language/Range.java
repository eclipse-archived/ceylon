package ceylon.language;

import java.util.ArrayList;
import java.util.List;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Class;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.SatisfiedTypes;
import com.redhat.ceylon.compiler.java.metadata.Sequenced;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;

@Ceylon
@Class(extendsType="ceylon.language.Object")
@SatisfiedTypes({"ceylon.language.Sequence<Element>", 
	             "ceylon.language.Category",
	             "ceylon.language.Equality"})
@TypeParameters(@TypeParameter(value="Element", 
    satisfies={"ceylon.language.Comparable<Element>",
		       "ceylon.language.Ordinal<Element>"}))
public class Range<Element extends Comparable<? super Element> & Ordinal<? extends Element>>
    implements Sequence<Element>, Category {
    
    private final Element first;
    private final Element last;
    private final long size;
    
    public Range(@Name("first") Element first, 
    		     @Name("last") Element last) {
        this.first = first;
        this.last = last;
        long index = 0;
        Element x = first;
        while (!x.equals(last)) {
            ++index;
            x = next(x);
        }
        this.size = index+1;
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
        return last.compare(first).smallerThan();
    }
    
    private final Element next(Element x){
        return getDecreasing() ? 
        		x.getPredecessor() : x.getSuccessor();
    }

    @Override
    @TypeInfo("ceylon.language.Integer")
    public final long getSize(){
        return size;
    }

    @Override
    @TypeInfo("ceylon.language.Integer")
    public final long getLastIndex(){
        return size - 1;
    }

    @Override
    public Iterable<? extends Element> getRest() {
    	if (first.equals(last)) {
    	    return $empty.getEmpty();
    	}
    	else {
            return new Range<Element>(next(getFirst()), getLast());
    	}
    }

    @TypeInfo("ceylon.language.Nothing|Element")
    @Override
    public Element item(@Name("n") Integer n) {
        long index = 0;
        Element x = first;
        while (index<n.longValue()) {
        	if (x.equals(last)){
        		return null;
        	}
        	else {
                ++index;
                x=next(x);
        	}
        }
        return x;
    }

    @Override
    @TypeInfo("ceylon.language.Iterator<Element>")
	public Iterator<Element> getIterator() {
        class RangeIterator implements Iterator<Element> {
        	java.lang.Object current;

        	public RangeIterator() {
            	this.current = first;
            }
            
            @TypeInfo("Element|ceylon.language.Finished")
        	public java.lang.Object next() {
                java.lang.Object result = current;
                if (!(current instanceof Finished) && !current.equals(getLast())) {
                    current = Range.this.next((Element) current);
                } else {
                    current = exhausted.getExhausted();
                }
                return result;
            }
        }
        
        return new RangeIterator();
    }

    @Override
    public final boolean contains(@Name("element") 
    @TypeInfo("ceylon.language.Equality") java.lang.Object value) {
        // FIXME
    	try {
    		return value != null /*&& value instanceof Element*/ ?
	             includes((Element) value) : false;
    	}
    	catch (ClassCastException cce) { //ugly hack
    		return false;
    	}
    }

    public final boolean includes(@Name("x") Element x){
        if (getDecreasing()){
            return x.compare(first).asSmallAs() && 
                    x.compare(last).asLargeAs();
        }
        else {
            return x.compare(first).asLargeAs() && 
                    x.compare(last).asSmallAs();
        }
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
    @TypeInfo("ceylon.language.Empty|ceylon.language.Sequence<ceylon.language.Integer>")
    Iterable<? extends Integer> keys) {
        return Correspondence$impl.definesEvery(this, keys);
    }

    @Override
    public boolean definesAny(@Sequenced @Name("keys") 
    @TypeInfo("ceylon.language.Empty|ceylon.language.Sequence<ceylon.language.Integer>")
    Iterable<? extends Integer> keys) {
        return Correspondence$impl.definesAny(this, keys);
    }

    @Override
    @TypeInfo("ceylon.language.Empty|ceylon.language.Sequence<Element|ceylon.language.Nothing>")
    public Iterable<? extends Element> items(@Sequenced @Name("keys") 
    @TypeInfo("ceylon.language.Empty|ceylon.language.Sequence<ceylon.language.Integer")
    Iterable<? extends Integer> keys) {
        return Correspondence$impl.items(this, keys);
    }

    //TODO: @TypeInfo
    @Override
    public boolean containsEvery(@Sequenced @Name("elements") 
    @TypeInfo("ceylon.language.Empty|ceylon.language.Sequence<ceylon.language.Equality>")
    Iterable<?> elements) {
        return Category$impl.containsEvery(this, elements);
    }

    //TODO: @TypeInfo
    @Override
    public boolean containsAny(@Sequenced @Name("elements") 
    @TypeInfo("ceylon.language.Empty|ceylon.language.Sequence<ceylon.language.Equality>")
    Iterable<?> elements) {
        return Category$impl.containsAny(this, elements);
    }

    @Override
    public boolean getEmpty() {
        return Sequence$impl.getEmpty(this);
    }

    @Override
    public boolean defines(@Name("index") Integer index) {
        return Sequence$impl.defines(this, index);
    }

    @Override
    @TypeInfo("ceylon.language.Empty|ceylon.language.Sequence<Element>")
    public Iterable<? extends Element> segment(
    		@Name("from") final Integer from, 
    		@Name("length") final Integer length) {
    	long fromIndex = from.longValue();
		long resultLength = length.longValue();
		if (fromIndex>getLastIndex()||resultLength==0) 
    		return $empty.getEmpty();
    	long len = getSize();
		if (fromIndex+resultLength>len) 
			    resultLength = len-fromIndex;
    	Element begin = first;
    	for (int i=0; i<fromIndex; i++) {
    		begin = begin.getSuccessor();
    	}
    	Element end = begin;
    	for (int i=0; i<resultLength-1; i++) {
    		end = end.getSuccessor();
    	}
    	return new Range<Element>(begin, end);
    }
    
    @Override
    @TypeInfo("ceylon.language.Empty|ceylon.language.Sequence<Element>")
    public Iterable<? extends Element> span(
    		@Name("from") final Integer from,
    		@TypeInfo("ceylon.language.Nothing|ceylon.language.Integer")
    		@Name("to") final Integer to) {
		long lastIndex = getLastIndex();
    	long fromIndex = from.longValue();
		long toIndex = to==null ? lastIndex : to.longValue();
		if (fromIndex>lastIndex||toIndex<fromIndex) 
    		return $empty.getEmpty();
    	Element begin = first;
    	for (int i=0; i<fromIndex; i++) {
    		begin = begin.getSuccessor();
    	}
    	Element end = first;
    	for (int i=0; i<toIndex; i++) {
    		end = end.getSuccessor();
    	}
    	return new Range<Element>(begin, end);
    }
    
    public Sequence<? extends Element> by(
    		@TypeInfo("ceylon.language.Integer")
    		@Name("stepSize") long stepSize) {
    	if (stepSize==0) {
    		throw new Exception(String.instance("step size must be nonzero"), null);
    	}
    	if (first.equals(last) || stepSize==1) {
    		return this;
    	}
    	boolean decreasing = getDecreasing();
    	List<Element> list = new ArrayList<Element>();
    	for (Element elem = first; decreasing ? 
    			elem.compare(last).asLargeAs() : elem.compare(last).asSmallAs();) {
    		list.add(elem);
    		for (int i=0; i<stepSize; i++) {
    			elem = next(elem);
    		}
    	}
    	return new ArraySequence<Element>(list);
    }

}
