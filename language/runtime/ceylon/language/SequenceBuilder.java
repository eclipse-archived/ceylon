package ceylon.language;

import com.redhat.ceylon.compiler.java.language.ArraySequence;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Class;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.Sequenced;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.runtime.model.ReifiedType;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

@Ceylon(major = 4)
@Class
@TypeParameters(@TypeParameter(value = "Element"))
public class SequenceBuilder<Element> implements ReifiedType {

    private final static int MIN_CAPACITY = 5;
    private final static int MAX_CAPACITY = java.lang.Integer.MAX_VALUE;
    
    /** What will become the backing array of the ArraySequence we're building */
    private java.lang.Object[] array;
    
    /** The index of the first element in {@link array} */
    private int start = 0;
    
    /** The number of elements (from start) currently in {@link array} */
    private int length = 0;
    
    /** 
     * The number of elements in {@link array} which we're committed to
     * (because we've passed {@link array} to ArraySequence
     */
    private int committed = 0;
    
    /* Invariant: 0 <= start <= array.length */
    /* Invariant: 0 <= committed <= length */
    /* Invariant: 0 <= start + length <= array.length */
    @Ignore
    protected final TypeDescriptor $reifiedElement;
    
    public SequenceBuilder(@Ignore TypeDescriptor $reifiedElement) {
        this.$reifiedElement = $reifiedElement;
    }
    
    private void ensureCapacity$priv(long capacity) {
        
        if ((array == null && capacity > 0) 
                || (capacity > array.length-start)) {
            // Always have about 50% more capacity than requested
            long newcapacity = capacity+(capacity>>1);
            if (newcapacity < MIN_CAPACITY) {
                newcapacity = MIN_CAPACITY;
            } else if (newcapacity > MAX_CAPACITY) {
                newcapacity = capacity;
                if (newcapacity > MAX_CAPACITY) {
                    throw new RuntimeException("Can't allocate array bigger than " + MAX_CAPACITY);
                }
            }
            java.lang.Object[] newarray = new java.lang.Object[(int)newcapacity];
            if (array != null) {
                System.arraycopy(array, start, newarray, 0, length);
            }
            array = newarray;
            start = 0;
            committed = 0;
        }
    }
    
    @TypeInfo("ceylon.language::Sequential<Element>")
    public Sequential<? extends Element> getSequence() {
        if (array==null || length == 0) {
            return (Sequential)empty_.getEmpty$();
        }
        else {
            committed = Math.max(committed, length);
            return ArraySequence.backedBy$hidden($reifiedElement, (Element[])array, start, length);
        }
    }
    
    public final SequenceBuilder<Element> append(@Name("element") Element element) {
        ensureCapacity$priv(length+1);
    	array[start+length] = element;
    	length+=1;
    	return this;
    }
    
    public final SequenceBuilder<Element> appendAll(@Sequenced @Name("elements") 
    @TypeInfo("ceylon.language::Sequential<Element>") 
    Sequential<? extends Element> elements) {
        long added = elements.getSize();
        ensureCapacity$priv(length + added);
        if (elements instanceof ArraySequence) {
            ArraySequence as = (ArraySequence)elements;
            java.lang.Object[] a = as.backingArray$hidden();
            System.arraycopy(a, as.backingFirst$hidden(), array, start+length, (int)as.getSize());
        } else {
        	java.lang.Object elem;
        	int index = start+length;
        	for (Iterator<? extends Element> iter=elements.iterator(); !((elem = iter.next()) instanceof Finished);) {
        	    array[index] = elem;
        	    index++;
        	}
        }
        length += added;
    	return this;
    }
    
    @Ignore
    public final SequenceBuilder<Element> appendAll() {
        return this;
    }
    
    public final long getSize() {
        return length;
    }
     
    public final boolean getEmpty() {
        return length == 0;
    }
     
    @Override
    @Ignore
    public TypeDescriptor $getType() {
        return TypeDescriptor.klass(SequenceBuilder.class, $reifiedElement);
    }
}
