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
import com.redhat.ceylon.compiler.java.metadata.Variance;

@Ceylon(major = 3)
@TypeParameters(@TypeParameter(value = "Element",
        variance = Variance.OUT,
        satisfies="ceylon.language::Object"))
@Class(extendsType="ceylon.language::Object")
@SatisfiedTypes("ceylon.language::Sequence<Element>")
public class Singleton<Element>
        implements Sequence<Element> {
    private final ceylon.language.Category$impl $ceylon$language$Category$this;
    private final ceylon.language.Correspondence$impl $ceylon$language$Correspondence$this;
    private final ceylon.language.Iterable$impl<Element> $ceylon$language$Iterable$this;
    private final ceylon.language.List$impl<Element> $ceylon$language$List$this;
    private final ceylon.language.Sequence$impl<Element> $ceylon$language$Sequence$this;

	Element element;

	public Singleton(Element element) {
        this.$ceylon$language$Category$this = new ceylon.language.Category$impl(this);
        this.$ceylon$language$Correspondence$this = new ceylon.language.Correspondence$impl(this);
        this.$ceylon$language$Iterable$this = new ceylon.language.Iterable$impl<Element>(this);
        this.$ceylon$language$List$this = new ceylon.language.List$impl<Element>(this);
        this.$ceylon$language$Sequence$this = new ceylon.language.Sequence$impl<Element>(this);
		this.element = element;
	}

    private Correspondence$impl<Integer,Element> correspondence$impl = new Correspondence$impl<Integer,Element>(this);
    
    @Ignore
    @Override
    public Correspondence$impl<? super Integer,? extends Element> $ceylon$language$Correspondence$impl(){
        return correspondence$impl;
    }

    @Override
    @Ignore
    public Correspondence$impl<? super Integer, ? extends Element>.Items Items$new(Sequence<? extends Integer> keys) {
        return correspondence$impl.Items$new(keys);
    }

    @Override
	public Singleton<Element> getClone() {
		return this;
	}

	@Override
	@TypeInfo("ceylon.language::Null|Element")
	public Element item(@Name("key") Integer key) {
		return key.longValue()==0 ? element : null;
	}

	@Override
	@Ignore
	public Category getKeys() {
		return $ceylon$language$Correspondence$this.getKeys();
	}

	@Override
	@Ignore
	public boolean definesEvery(@Sequenced @Name("keys")
    @TypeInfo("ceylon.language::Sequential<ceylon.language::Integer>")
	Sequential<? extends Integer> keys) {
		return $ceylon$language$Correspondence$this.definesEvery(keys);
	}
	@Override @Ignore
    @SuppressWarnings({"rawtypes", "unchecked"})
    public boolean definesEvery() {
	    return $ceylon$language$Correspondence$this.definesEvery((Sequential)empty_.getEmpty$());
	}
	@Override @Ignore
    @SuppressWarnings({"rawtypes", "unchecked"})
    public Sequential<? extends Integer> definesEvery$keys() {
        return (Sequential)empty_.getEmpty$();
    }

	@Override
	@Ignore
	public boolean definesAny(@Sequenced @Name("keys")
    @TypeInfo("ceylon.language::Sequential<ceylon.language::Integer>")
	Sequential<? extends Integer> keys) {
		return $ceylon$language$Correspondence$this.definesAny(keys);
	}
	@Override @Ignore
    @SuppressWarnings({"rawtypes", "unchecked"})
    public boolean definesAny() {
	    return $ceylon$language$Correspondence$this.definesAny((Sequential)empty_.getEmpty$());
	}
	@Override @Ignore
    @SuppressWarnings({"rawtypes", "unchecked"})
    public Sequential<? extends Integer> definesAny$keys() {
        return (Sequential)empty_.getEmpty$();
    }

	@Override
	@Ignore
	public Sequential<? extends Element> items(@Sequenced @Name("keys")
    @TypeInfo("ceylon.language::Sequential<ceylon.language::Integer>")
	Sequential<? extends Integer> keys) {
		return $ceylon$language$Correspondence$this.items(keys);
	}
	@Override @Ignore
    @SuppressWarnings({"rawtypes", "unchecked"})
	public Sequential<? extends Element> items() {
	    return $ceylon$language$Correspondence$this.items((Sequential)empty_.getEmpty$());
	}
	@Override @Ignore
    @SuppressWarnings({"rawtypes", "unchecked"})
    public Sequential<? extends Integer> items$keys() {
        return (Sequential)empty_.getEmpty$();
    }

	@Override
    @TypeInfo("ceylon.language::Integer")
	public Integer getLastIndex() {
		return Integer.instance(0);
	}

	@Override
	public Element getFirst() {
		return element;
	}

	@Override
	@TypeInfo("ceylon.language::Empty")
    @SuppressWarnings({"rawtypes", "unchecked"})
	public Sequential<? extends Element> getRest() {
		return (Sequential)empty_.getEmpty$();
	}

	@Override
	public boolean getEmpty() {
		return false;
	}

	@Override
	@TypeInfo("ceylon.language::Integer")
	public long getSize() {
		return 1;
	}

	@Override
	public Element getLast() {
		return element;
	}

	@Override
	public boolean defines(@Name("key") Integer key) {
		return key.longValue()==0;
	}
	@Override
	public Iterator<Element> getIterator() {
		class SingletonIterator implements Iterator<Element> {
		    boolean done;
			@Override
			public java.lang.Object next() {
			    if (done) {
			        return finished_.getFinished$();
			    }
			    else {
			    	done = true;
			    	return element;
			    }
			}
			@Override
			public java.lang.String toString() {
				return "SingletonIterator";
			}
		};
		return new SingletonIterator();
	}

    @Override
    @TypeInfo("ceylon.language::Empty|ceylon.language::Singleton<Element>")
    @SuppressWarnings({"rawtypes", "unchecked"})
    public Sequential<? extends Element> segment(@Name("from") Integer from,
    		@Name("length") long length) {
    	return from.longValue()==0 && length>0 ? this : (Sequential)empty_.getEmpty$();
    }

    @Override
    @TypeInfo("ceylon.language::Empty|ceylon.language::Singleton<Element>")
    @SuppressWarnings({"rawtypes", "unchecked"})
    public Sequential<? extends Element> span(@Name("from") Integer from,
    		@TypeInfo("ceylon.language::Null|ceylon.language::Integer")
    		@Name("to") Integer to) {
    	long lowest;
    	long highest;
    	if (to == null || from.longValue()<=to.longValue()) {
    		lowest = from.longValue();
    		highest = to == null ? from.longValue()+1 : to.longValue();
    	}
    	else {
    		lowest = to.longValue();
    		highest = from.longValue();
    	}
    	return lowest>0 || highest<0 ? (Sequential)empty_.getEmpty$(): this;
    }

    @Override
    @TypeInfo("ceylon.language::Empty|ceylon.language::Singleton<Element>")
    @SuppressWarnings({"rawtypes", "unchecked"})
    public Sequential<? extends Element> spanFrom(@Name("from") Integer from) {
        return from.value > 0 ? (Sequential)empty_.getEmpty$() : this;
    }

    @Override
    @TypeInfo("ceylon.language::Empty|ceylon.language::Singleton<Element>")
    @SuppressWarnings({"rawtypes", "unchecked"})
    public Sequential<? extends Element> spanTo(@Name("to") Integer to) {
        return to.value < 0 ? (Sequential)empty_.getEmpty$() : this;
    }

    @Override
	public java.lang.String toString() {
		return "{ " + getFirst().toString() + " }";
	}

    @Override
    public boolean contains(java.lang.Object element) {
        return this.element!=null &&
                this.element.equals(element);
    }

    @Override
    @Ignore
    public boolean containsEvery(Sequential<?> elements) {
        return $ceylon$language$Category$this.containsEvery(elements);
    }

    @Override
    @Ignore
    public boolean containsEvery() {
        return $ceylon$language$Category$this.containsEvery(empty_.getEmpty$());
    }
    @Override
    @Ignore
    public Sequential<?> containsEvery$elements() {
        return empty_.getEmpty$();
    }

    @Override
    @Ignore
    public boolean containsAny(Sequential<?> elements) {
        return $ceylon$language$Category$this.containsAny(elements);
    }

    @Override
    @Ignore
    public boolean containsAny() {
        return $ceylon$language$Category$this.containsAny(empty_.getEmpty$());
    }

    @Override
    @Ignore
    public Sequential<?> containsAny$elements() {
        return empty_.getEmpty$();
    }

    @Annotations({@Annotation("actual")})
    @Override
    public Sequence<? extends Element> getReversed() {
    	return this;
    }
    
    @Override
    public boolean equals(java.lang.Object that) {
        if (that instanceof Sequential) {
            @SuppressWarnings("unchecked")
            Sequential<? extends java.lang.Object> other = (Sequential<? extends java.lang.Object>)that;
            if (other.getSize()==1) {
                java.lang.Object elem = other.item(Integer.instance(0));
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

    @Override
    @Ignore
    public
    Sequence<? extends Element> getSequence() {
        return $ceylon$language$Sequence$this.getSequence();
    }
    @Override @Ignore
    public Element find(Callable<? extends Boolean> f) {
        return f.$call(element).booleanValue() ? element : null;
    }
    @Override @Ignore
    public Element findLast(Callable<? extends Boolean> f) {
        return f.$call(element).booleanValue() ? element : null;
    }
    @Override
    @TypeInfo("ceylon.language::Singleton<Element>")
    public Singleton<? extends Element> sort(@Name("comparing")
            @TypeInfo("ceylon.language::Callable<ceylon.language::Comparison,ceylon.language::Tuple<Element,Element,ceylon.language::Tuple<Element,Element,ceylon.language::Empty>>>")
            Callable<? extends Comparison> comparing) {
        return this;
    }
    @Override
    @TypeInfo("ceylon.language::Sequence<Result>")
    public <Result> Sequence<? extends Result> map(@Name("selecting")
            @TypeInfo("ceylon.language::Callable<Result,ceylon.language::Tuple<Element,Element,ceylon.language::Empty>>")
            Callable<? extends Result> selecting) {
        return new Singleton<Result>(selecting.$call(element));
    }
    @Override
    @TypeInfo("ceylon.language::Singleton<Element>|ceylon.language::Empty")
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Iterable<? extends Element> filter(@Name("selecting")
            @TypeInfo("ceylon.language::Callable<ceylon.language::Boolean,ceylon.language::Tuple<Element,Element,ceylon.language::Empty>>")
            Callable<? extends Boolean> selecting) {
        return selecting.$call(element).booleanValue() ? this : (Iterable)empty_.getEmpty$();
    }
    @Override
    @TypeInfo("ceylon.language::Sequence<Result>")
    public <Result> Sequence<? extends Result> collect(@Name("selecting")
            @TypeInfo("ceylon.language::Callable<Result,ceylon.language::Tuple<Element,Element,ceylon.language::Empty>>")
            Callable<? extends Result> selecting) {
        return new Singleton<Result>(selecting.$call(element));
    }
    @Override
    @TypeInfo("ceylon.language::Singleton<Element>|ceylon.language::Empty")
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Sequential<? extends Element> select(@Name("selecting")
            @TypeInfo("ceylon.language::Callable<ceylon.language::Boolean,ceylon.language::Tuple<Element,Element,ceylon.language::Empty>>")
            Callable<? extends Boolean> selecting) {
        return selecting.$call(element).booleanValue() ? this : (Sequential)empty_.getEmpty$();
    }
    @Override
    @Ignore
    public <Result> Result fold(Result ini, Callable<? extends Result> f) {
        return f.$call(ini, element);
    }
    @Override @Ignore
    public boolean any(Callable<? extends Boolean> f) {
        return f.$call(element).booleanValue();
    }
    @Override @Ignore
    public boolean every(Callable<? extends Boolean> f) {
        return f.$call(element).booleanValue();
    }
    @Override
    @TypeInfo("ceylon.language::Singleton<Element>|ceylon.language::Empty")
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Iterable<? extends Element> skipping(long skip) {
        return skip>0 ? (Iterable)empty_.getEmpty$() : this;
    }
    @Override
    @TypeInfo("ceylon.language::Singleton<Element>|ceylon.language::Empty")
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Iterable<? extends Element> taking(long take) {
        return take<1 ? (Iterable)empty_.getEmpty$() : this;
    }
    @Override @Ignore
    public Iterable<? extends Element> by(long step) {
        return this;
    }
    @Override @Ignore
    public long count(Callable<? extends Boolean> f) {
        return f.$call(element).booleanValue() ? 1 : 0;
    }
    @Override
    @TypeInfo("ceylon.language::Singleton<Element>")
    public Singleton<? extends Element> getCoalesced() {
        return this;
    }
    @Override @Ignore
    public Singleton<? extends Entry<? extends Integer, ? extends Element>> getIndexed() {
        return new Singleton<Entry<? extends Integer, ? extends Element>>(new Entry<Integer, Element>(Integer.instance(0), element));
    }
    @SuppressWarnings("rawtypes")
    @Override @Ignore public <Other>Iterable chain(Iterable<? extends Other> other) {
        return $ceylon$language$Iterable$this.chain(other);
    }
    @Override @Ignore
    public <Key> Map<? extends Key, ? extends Sequence<? extends Element>> group(Callable<? extends Key> grouping) {
        return $ceylon$language$Iterable$this.group(grouping);
    }

    @Override @SuppressWarnings("rawtypes")
    @Annotations(@Annotation("actual"))
    @TypeParameters(@TypeParameter("Other"))
    @TypeInfo("ceylon.language::Sequence<Element|Other>")
    public <Other>Sequence withLeading(Other elem) {
        return $ceylon$language$List$this.withLeading(elem);
    }
    @Override @SuppressWarnings("rawtypes")
    @Annotations(@Annotation("actual"))
    @TypeParameters(@TypeParameter("Other"))
    @TypeInfo("ceylon.language::Sequence<Element|Other>")
    public <Other>Sequence withTrailing(Other elem) {
        return $ceylon$language$List$this.withTrailing(elem);
    }

}
