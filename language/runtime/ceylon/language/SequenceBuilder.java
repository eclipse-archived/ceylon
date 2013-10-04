package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Class;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.runtime.model.ReifiedType;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

@Ceylon(major = 5)
@Class
@TypeParameters(@TypeParameter(value = "Element"))
public class SequenceBuilder<Element> implements ReifiedType {

    private final static int MIN_CAPACITY = 5;
    private final static int MAX_CAPACITY = java.lang.Integer.MAX_VALUE;
    
    /** What will become the backing array of the ArraySequence we're building */
    java.lang.Object[] array;
    
    /** The number of elements (from start) currently in {@link array} */
    int length = 0;
    
    /* Invariant: 0 <= start <= array.length */
    /* Invariant: 0 <= committed <= length */
    /* Invariant: 0 <= start + length <= array.length */
    @Ignore
    protected final TypeDescriptor $reifiedElement;
    
    public SequenceBuilder(@Ignore TypeDescriptor $reifiedElement) {
        this.$reifiedElement = $reifiedElement;
    }
    
    @Ignore
    public SequenceBuilder(@Ignore TypeDescriptor $reifiedElement, int initialCapacity) {
        this($reifiedElement);
        if (initialCapacity >= 0) {
            resize$priv(initialCapacity);
        }
    }
    
    /** Ensures the array has at least the given capacity (it may allocate more) */
    @Ignore
    private void ensureCapacity$priv(long capacity) {
        
        if ((array == null && capacity > 0) 
                || (capacity > array.length)) {
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
            resize$priv(newcapacity);
        }
    }
    /** Resizes the array to the given size */
    @Ignore
    private void resize$priv(long newcapacity) {
        java.lang.Object[] newarray = new java.lang.Object[(int)newcapacity];
        if (array != null) {
            System.arraycopy(array, 0, newarray, 0, length);
        }
        array = newarray;
    }
    /** Trims the array so it's just big enough */
    @Ignore
    SequenceBuilder trim$priv() {
        if (array.length != length) {
            resize$priv(length);
        }
        return this;
    }
    
    @TypeInfo("ceylon.language::Sequential<Element>")
    public Sequential<? extends Element> getSequence() {
        if (array==null || length == 0) {
            return (Sequential)empty_.get_();
        }
        else {
            return ArraySequence.backedBy$hidden($reifiedElement, (Element[])array, 0, length);
        }
    }
    
    public final SequenceBuilder<Element> append(@Name("element") Element element) {
        ensureCapacity$priv(length+1);
    	array[length] = element;
    	length+=1;
    	return this;
    }
    
    public final SequenceBuilder<Element> appendAll(@Name("elements") 
    @TypeInfo("ceylon.language::Iterable<Element,ceylon.language::Null>") 
    Iterable<? extends Element, ? extends java.lang.Object> elements) {
        if (elements instanceof ArraySequence) {
            ArraySequence as = (ArraySequence)elements;
            int size = (int)as.getSize();
            ensureCapacity$priv(length + size);
            java.lang.Object[] a = as.array;
            System.arraycopy(a, as.first, array, length, size);
            length += size;
        } else {
        	java.lang.Object elem;
        	int index = length;
        	for (Iterator<? extends Element> iter=elements.iterator(); !((elem = iter.next()) instanceof Finished);) {
        	    // In general, Iterable.getSize() could cause an iteration 
                // through all the elements, so we can't allocate before the loop 
        	    ensureCapacity$priv(length + 1);
        	    array[index] = elem;
        	    index++;
        	    length++;
        	}
        }
        
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
