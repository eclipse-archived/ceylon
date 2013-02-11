package ceylon.language;

import com.redhat.ceylon.compiler.java.ReifiedType;
import com.redhat.ceylon.compiler.java.TypeDescriptor;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Class;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.SatisfiedTypes;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.metadata.Variance;

@Ceylon(major = 4)
@Class
@TypeParameters({@TypeParameter(value="Key", variance=Variance.IN), 
	             @TypeParameter(value="Item", variance=Variance.OUT)})
@SatisfiedTypes("ceylon.language::Category")
class Keys<Key,Item> implements Category, ReifiedType {

	private Correspondence<? super Key, ? extends Item> correspondence;
    @Ignore
	protected Category$impl $ceylon$language$Category$this; 
	
	Keys(@Ignore TypeDescriptor $reifiedKey, @Ignore TypeDescriptor $reifiedItem,
	        @Name("correspondence")
		@TypeInfo("ceylon.language::Correspondence<Key,Item>")
	    Correspondence<? super Key, ? extends Item> correspondence) {
		this.correspondence = correspondence;
		this.$ceylon$language$Category$this = new Category$impl(this);
	}
	
    @Ignore
    @Override
    public Category$impl $ceylon$language$Category$impl(){
        return $ceylon$language$Category$this;
    }

	@Override
	public boolean contains(@Name("key") java.lang.Object key) {
		return correspondence.defines((Key)key);
	}

	@Override
	@Ignore
	public boolean containsAny(Iterable<?, ?> keys) {
		return $ceylon$language$Category$this.containsAny(keys);
	}

	@Override
	@Ignore
	public boolean containsEvery(Iterable<?, ?> keys) {
		return $ceylon$language$Category$this.containsEvery(keys);
	}

    @Override
    public boolean $is(TypeDescriptor type) {
        // FIXME: implement me
        throw new RuntimeException("Not implemented");
    }
}
