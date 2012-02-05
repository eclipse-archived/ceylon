package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Class;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.SatisfiedTypes;
import com.redhat.ceylon.compiler.java.metadata.Sequenced;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.metadata.Variance;

@Ceylon
@TypeParameters(@TypeParameter(value = "Element", 
        variance = Variance.OUT, 
        satisfies="ceylon.language.Equality"))
@Class(extendsType="ceylon.language.Object")
@SatisfiedTypes("ceylon.language.Sequence<Element>")
public class Singleton<Element> 
        implements Sequence<Element> {
	
	Element element;
	
	public Singleton(Element element) {
		this.element = element;
	}
	
	@Override
	public Singleton<Element> getClone() {
		return this;
	}
	
	@Override
	@TypeInfo("ceylon.language.Nothing|Element")
	public Element item(@Name("index") Integer key) {
		return key.longValue()==0 ? element : null;
	}
	
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
	public List<? extends Element> items(@Sequenced @Name("keys") 
    @TypeInfo("ceylon.language.Empty|ceylon.language.Sequence<ceylon.language.Integer>")
	Iterable<? extends Integer> keys) {
		return Correspondence$impl.items(this, keys);
	}
	
	@Override
    @TypeInfo("ceylon.language.Integer")
	public Integer getLastIndex() {
		return Integer.instance(0);
	}
	
	@Override
	public Element getFirst() {
		return element;
	}
	
	@Override
	public Empty getRest() {
		return $empty.getEmpty();
	}
	
	@Override
	public boolean getEmpty() {
		return false;
	}
	
	@Override
	@TypeInfo("ceylon.language.Integer")
	public long getSize() {
		return 1;
	}
	
	@Override
	public Element getLast() {
		return element;
	}
	
	@Override
	public boolean defines(@Name("index") Integer index) {
		return index.longValue()==0;
	}
	@Override
	public Iterator<Element> getIterator() {
		class SingletonIterator implements Iterator<Element> {
		    java.lang.Object current;
		    SingletonIterator() {
		        current = element;
		    }
			@Override
			public java.lang.Object next() {
			    java.lang.Object result = current;
			    current = exhausted.getExhausted();
				return result;
			}
			@Override
			public java.lang.String toString() {
				return "SingletonIterator";
			}
		};
		return new SingletonIterator();
	}

    @Override
    @TypeInfo("ceylon.language.Empty|ceylon.language.Sequence<Element>")
    public List<? extends Element> segment(@Name("from") Integer from, 
    		@Name("length") Integer length) {
    	if (from.longValue()>0||length.longValue()==0) 
    		return $empty.getEmpty();
    	return this;
    }
    
    @Override
    @TypeInfo("ceylon.language.Empty|ceylon.language.Sequence<Element>")
    public List<? extends Element> span(@Name("from") Integer from, 
    		@TypeInfo("ceylon.language.Nothing|ceylon.language.Integer")
    		@Name("to") Integer to) {
    	//if (to==null) to = Integer.instance(0);
    	if (from.longValue()>0) 
    		return $empty.getEmpty();
    	return this;
    }

	@Override
	public java.lang.String toString() {
		return Sequence$impl.toString(this);
	}

    @Override
    public boolean contains(java.lang.Object element) {
        return this.element!=null && 
                this.element.equals(element);
    }

    @Override
    public long count(java.lang.Object element) {
        return contains(element) ? 1 : 0;
    }

    @Override
    public boolean containsEvery(Iterable<?> elements) {
        return Category$impl.containsEvery(this, elements);
    }

    @Override
    public boolean containsAny(Iterable<?> elements) {
        return Category$impl.containsAny(this, elements);
    }
	
    @Override
    public boolean equals(java.lang.Object that) {
        if (that instanceof List) {
            List other = (List) that;
            if (other.getSize()==1) {
                java.lang.Object elem = other.item(0);
                return element==elem || 
                        elem!=null && element!=null &&
                        elem.equals(element);
            }
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return 1;
    }
    
}
