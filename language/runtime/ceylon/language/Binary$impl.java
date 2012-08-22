package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ignore;

@Ignore
public class Binary$impl<T extends Binary<T>> {
    
    private final Binary<T> $this;
    
    public Binary$impl(Binary<T> $this) {
        this.$this = $this;
    }

    public boolean set$bit(long index){
        return true;
    }
    
    public T set(long index){
        return $this.set(index, true);
    }
    
    public T clear(long index){
        return $this.set(index, false);
    }
}
