package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Annotation;
import com.redhat.ceylon.compiler.java.metadata.Annotations;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Class;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.SatisfiedTypes;
import com.redhat.ceylon.compiler.java.metadata.Sequenced;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;

@Ceylon(major = 1)
@Class(extendsType="ceylon.language.Object")
@SatisfiedTypes({"ceylon.language.Sequence<Element>", 
	             "ceylon.language.Category"})
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
    public final Integer getLastIndex(){
        return Integer.instance(size - 1);
    }

    @Override
    @TypeInfo("ceylon.language.Empty|ceylon.language.Sequence<Element>")
    public FixedSized<? extends Element> getRest() {
    	if (first.equals(last)) {
    	    return (FixedSized)$empty.getEmpty();
    	}
    	else {
            return new Range<Element>(next(getFirst()), getLast());
    	}
    }

    @TypeInfo("ceylon.language.Nothing|Element")
    @Override
    public Element item(@Name("key") Integer key) {
        long index = 0;
        Element x = first;
        while (index<key.longValue()) {
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
                if (!(current instanceof Finished)){
                    if(current.equals(getLast())) {
                        current = exhausted.getExhausted();
                    } else {
                        current = Range.this.next((Element) current);
                    }
                }
                return result;
            }
            
            @Override
            public java.lang.String toString() {
                return "RangeIterator";
            }
        }
        
        return new RangeIterator();
    }

    @Override
    public final boolean contains(@Name("element") java.lang.Object element) {
        // FIXME
    	try {
    		return element != null /*&& value instanceof Element*/ ?
	             includes((Element) element) : false;
    	}
    	catch (ClassCastException cce) { //ugly hack
    		return false;
    	}
    }

    @Override
    public final long count(@Name("element") java.lang.Object element) {
        return contains(element) ? 1 : 0;
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
    @Ignore
    public final boolean equals(@Name("that") java.lang.Object that){
        if (that instanceof Range) {
            Range<Element> $that = (Range<Element>) that;
            return $that.getFirst().equals(getFirst()) && $that.getLast().equals(getLast());
        }
        else {
            return List$impl._equals(this, that);
        }
    }
    
    @Override
    @Ignore
    public int hashCode(){
        return List$impl._hashCode(this);
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
    @Ignore
    public Category getKeys() {
        return Correspondence$impl._getKeys(this);
    }

    @Override
    @Ignore
    public boolean definesEvery(@Sequenced @Name("keys") 
    @TypeInfo("ceylon.language.Iterable<ceylon.language.Integer>")
    Iterable<? extends Integer> keys) {
        return Correspondence$impl._definesEvery(this, keys);
    }
    @Override
    @Ignore
    public boolean definesEvery() {
        return Correspondence$impl._definesEvery(this, (Iterable)$empty.getEmpty());
    }
    @Override
    @Ignore
    public Iterable<? extends Integer> definesEvery$keys() {
        return (Iterable)$empty.getEmpty();
    }

    @Override
    @Ignore
    public boolean definesAny(@Sequenced @Name("keys") 
    @TypeInfo("ceylon.language.Iterable<ceylon.language.Integer>")
    Iterable<? extends Integer> keys) {
        return Correspondence$impl._definesAny(this, keys);
    }
    @Override
    @Ignore
    public boolean definesAny() {
        return Correspondence$impl._definesAny(this, (Iterable)$empty.getEmpty());
    }
    @Override
    @Ignore
    public Iterable<? extends Integer> definesAny$keys() {
        return (Iterable)$empty.getEmpty();
    }

    @Override
    @Ignore
    public ceylon.language.List<? extends Element> items(@Sequenced @Name("keys") 
    @TypeInfo("ceylon.language.Iterable<ceylon.language.Integer>")
    Iterable<? extends Integer> keys) {
        return Correspondence$impl._items(this, keys);
    }
    
    @Override
    @Ignore
    public ceylon.language.List<? extends Element> items() {
        return Correspondence$impl._items(this, (Iterable)$empty.getEmpty());
    }
    
    @Override
    @Ignore
    public Iterable<? extends Integer> items$keys() {
        return (Iterable)$empty.getEmpty();
    }

    //TODO: @TypeInfo
    @Override
    @Ignore
    public boolean containsEvery(@Sequenced @Name("elements") 
    @TypeInfo("ceylon.language.Iterable<ceylon.language.Object>")
    Iterable<?> elements) {
        return Category$impl._containsEvery(this, elements);
    }
    @Override
    @Ignore
    public boolean containsEvery() {
        return Category$impl._containsEvery(this, $empty.getEmpty());
    }
    @Override
    @Ignore
    public Iterable<?> containsEvery$elements() {
        return $empty.getEmpty();
    }

    //TODO: @TypeInfo
    @Override
    @Ignore
    public boolean containsAny(@Sequenced @Name("elements") 
    @TypeInfo("ceylon.language.Iterable<ceylon.language.Object>")
    Iterable<?> elements) {
        return Category$impl._containsAny(this, elements);
    }
    @Override
    @Ignore
    public boolean containsAny() {
        return Category$impl._containsAny(this, $empty.getEmpty());
    }
    @Override
    @Ignore
    public Iterable<?> containsAny$elements() {
        return $empty.getEmpty();
    }

    @Override
    @Ignore
    public boolean getEmpty() {
        return Some$impl._getEmpty(this);
    }

    @Override
    @Ignore
    public boolean defines(@Name("key") Integer key) {
        return List$impl._defines(this, key);
    }

    @Annotations({@Annotation("actual")})
    @Override
    public Sequence<? extends Element> getReversed() {
    	return new Range<Element>(last, first);
    }
    
    @Override
    @TypeInfo("ceylon.language.Empty|ceylon.language.Range<Element>")
    public ceylon.language.List<? extends Element> segment(
    		@Name("from") final Integer from, 
    		@Name("length") final long length) {
        //only positive length for now
        if (length<=0 || !defines(from)) {
        	return (ceylon.language.List)$empty.getEmpty();
        }
        Element x = this.first;
        for (int i=0; i < from.longValue(); i++) { x = this.next(x); }
        Element y = x;
        for (int i=1; i < length; i++) { y = this.next(y); }
        if (!includes(y)) { y = this.last; }
        return new Range<Element>(x, y);
    }
    
    @Override
    @TypeInfo("ceylon.language.Empty|ceylon.language.Range<Element>")
    public ceylon.language.List<? extends Element> span(
    		@Name("from") final Integer from,
    		@TypeInfo("ceylon.language.Nothing|ceylon.language.Integer")
    		@Name("to") final Integer to) {
    	Integer fromIndex = largest.largest(Integer.instance(0), from);
        Integer toIndex = to==null ? getLastIndex() : to;
    	if (!defines(fromIndex)) {
            //If it's an inverse range, adjust the "from" (upper bound)
            if (fromIndex.compare(toIndex) == larger.getLarger() && defines(toIndex)) {
                //Decrease the upper bound
                while (!defines(fromIndex)) {
                    fromIndex = fromIndex.getPredecessor();
                }
            } else {
                return (ceylon.language.List)$empty.getEmpty();
            }
        } else while (!defines(toIndex)) {
            //decrease the upper bound
            toIndex = toIndex.getPredecessor();
        }
        return new Range<Element>(this.item(fromIndex), this.item(toIndex));
    }

    @Override
    @Ignore
    public Iterable<? extends Element> by(long step) {
    	return Iterable$impl._by(this, step);
    }

    @Override 
    @Ignore 
    public Iterable<? extends Element> getSequence() { 
        return Iterable$impl._getSequence(this);
    }
    @Override 
    @Ignore 
    public Element find(Callable<? extends Boolean> f) { 
        return Iterable$impl._find(this, f);
    }
    @Override 
    @Ignore
    public Iterable<? extends Element> sorted(Callable<? extends Comparison> f) { 
        return Iterable$impl._sorted(this, f);
    }
    @Override 
    @Ignore 
    public <Result> Iterable<Result> map(Callable<? extends Result> f) { 
        return new MapIterable<Element, Result>(this, f);
    }
    @Override 
    @Ignore 
    public Iterable<? extends Element> filter(Callable<? extends Boolean> f) { 
        return new FilterIterable<Element>(this, f);
    }
    @Override 
    @Ignore 
    public <Result> Result fold(Result ini, Callable<? extends Result> f) { 
        return Iterable$impl._fold(this, ini, f); 
    }
    @Override @Ignore
    public boolean any(Callable<? extends Boolean> f) {
        return Iterable$impl._any(this, f);
    }
    @Override @Ignore
    public boolean every(Callable<? extends Boolean> f) {
        return Iterable$impl._every(this, f);
    }
    @Override
    @TypeInfo("ceylon.language.Range<Element>|ceylon.language.Empty")
    public Iterable<? extends Element> skipping(@Name("take") long skip) {
        long x=0;
        Element e=first;
        while (x++<skip) {
            e=next(e);
        }
        return this.includes(e) ? new Range(e, last) : (Iterable)$empty.getEmpty();
    }
    @Override
    @TypeInfo("ceylon.language.Range<Element>|ceylon.language.Empty")
    public Iterable<? extends Element> taking(@Name("take") long take) {
        if (take == 0) {
            return (Iterable)$empty.getEmpty();
        }
        long x=0;
        Element e=first;
        while (++x<take) {
            e=next(e);
        }
        return this.includes(e) ? new Range(first, e) : this;
    }

}
