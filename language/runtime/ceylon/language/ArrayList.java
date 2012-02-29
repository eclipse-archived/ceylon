package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Class;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.SatisfiedTypes;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;

@Ignore
@Class(extendsType="ceylon.language.Object")
@SatisfiedTypes("ceylon.language.List<Element>")
abstract class ArrayList<Element> implements List<Element> {

    protected final Element[] array;
    protected final long first;

    public ArrayList(Element... array) {
        this(array,0);
    }
    
    @Ignore
    ArrayList(Element[] array, long first) {
        this.array = array;
        this.first = first;
    }

    @Ignore
    ArrayList(java.util.List<Element> list) {
    	this.array = (Element[]) list.toArray();
        this.first = 0;
    }

    @Ignore
    ArrayList() {
        this.array = (Element[]) new Object[0];
        this.first = 0;
    }

    @Override
    @TypeInfo("ceylon.language.Integer")
    public Integer getLastIndex() {
        return Integer.instance(array.length - first - 1);
    }

    @Override
    public boolean getEmpty() {
        return array.length == 0;
    }

    @Override
    @TypeInfo("ceylon.language.Integer")
    public long getSize() {
        return array.length - first;
    }

    @Override
    public boolean defines(Integer index) {
        long ind = index.longValue();
		return ind>=0 && ind+first<array.length;
    }

    @Override
    public Iterator<Element> getIterator() {
        return new ArrayListIterator();
    }

    public class ArrayListIterator implements Iterator<Element> {
        private long idx = first;
        
        @Override
        public java.lang.Object next() {
            if (idx <= getLastIndex().longValue()+first) {
                return array[(int) idx++];
            } 
            else {
                return exhausted.getExhausted();
            }
        }

        @Override
        public java.lang.String toString() {
            return "ArrayArrayListIterator";
        }

    }

    @Override
    public Element item(Integer key) {
        long index = key.longValue()+first;
        return index<0 || index >= array.length ? 
                null : array[(int) index];
    }

    @Override
    public Category getKeys() {
        return Correspondence$impl.getKeys(this);
    }

    @Override
    public boolean definesEvery(Iterable<? extends Integer> keys) {
        return Correspondence$impl.definesEvery(this, keys);
    }

    @Override
    public boolean definesAny(Iterable<? extends Integer> keys) {
        return Correspondence$impl.definesAny(this, keys);
    }

    @Override
    public ceylon.language.List<? extends Element> items(Iterable<? extends Integer> keys) {
        return Correspondence$impl.items(this, keys);
    }

    @Override
    public ArrayList<Element> getClone() {
        return this;
    }
    
    @Override
    public java.lang.String toString() {
        return "ArrayList TODO";
    }

    public Element[] toArray() {
        return array;
    }
    
    @Override
    public boolean equals(java.lang.Object that) {
        return List$impl.equals(this, that);
    }

    @Override
    public int hashCode() {
        return List$impl.hashCode(this);
    }
    
    @Override
    public boolean contains(java.lang.Object element) {
        for (Element x: array) {
            if (x!=null && element.equals(x)) return true;
        }
        return false;
    }

    @Override
    public long count(java.lang.Object element) {
        int count=0;
        for (Element x: array) {
            if (x!=null && element.equals(x)) count++;
        }
        return count;
    }

    @Override
    public boolean containsEvery(Iterable<?> elements) {
        return Category$impl.containsEvery(this, elements);
    }

    @Override
    public boolean containsAny(Iterable<?> elements) {
        return Category$impl.containsAny(this, elements);
    }
    
}