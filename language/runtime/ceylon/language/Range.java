package ceylon.language;

import com.redhat.ceylon.compiler.metadata.java.Class;
import com.redhat.ceylon.compiler.metadata.java.Name;
import com.redhat.ceylon.compiler.metadata.java.SatisfiedTypes;
import com.redhat.ceylon.compiler.metadata.java.Sequenced;
import com.redhat.ceylon.compiler.metadata.java.TypeInfo;
import com.redhat.ceylon.compiler.metadata.java.TypeParameter;
import com.redhat.ceylon.compiler.metadata.java.TypeParameters;

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
        while (!atEnd(x)) {
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
        return last.compare(first).smaller();
    }
    
    private final boolean atEnd(Element x){
        return x.compare(last).equal();
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
    public Iterable<? extends Element> getRest() {
    	if (atEnd(first)) {
    	    return $empty.getEmpty();
    	}
    	else {
            return new Range<Element>(next(getFirst()), getLast());
    	}
    }

    @TypeInfo("ceylon.language.Nothing|Element")
    @Override
    public Element item(@Name("n") Natural n) {
        long index = 0;
        Element x = first;
        while (index<n.longValue()) {
        	if (atEnd(x)){
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
        	Element x;
            public RangeIterator(Element x) {
            	this.x = x;
            }
            public Element getHead() {
                return x;
            }
            @TypeInfo("ceylon.language.Nothing|ceylon.language.Iterator<Element>")
            public Iterator<Element> getTail() {
                 return atEnd(x) ? 
                		 null : new RangeIterator(next(x));
             }
        }
        return new RangeIterator(first);
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
            return x.compare(first).smallAs() && 
                    x.compare(last).largeAs();
        }
        else {
            return x.compare(first).largeAs() && 
                    x.compare(last).smallAs();
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
    public boolean defines(@Name("index") Natural index) {
        return Sequence$impl.defines(this, index);
    }

    @Override
    @TypeInfo("ceylon.language.Empty|ceylon.language.Sequence<Element>")
    public Iterable<? extends Element> segment(
    		@Name("from") final Natural from, 
    		@Name("length") final Natural length) {
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
    		@Name("from") final Natural from,
    		@TypeInfo("ceylon.language.Nothing|ceylon.language.Natural")
    		@Name("to") final Natural to) {
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

}
