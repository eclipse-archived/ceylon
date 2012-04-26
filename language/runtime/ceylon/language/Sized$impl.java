package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ignore;

@Ignore
public final class Sized$impl {
    private final Sized $this;
    public Sized$impl(Sized $this) {
        this.$this = $this;
    }
    public boolean getEmpty(){
        return _getEmpty($this);
    }
    static final boolean _getEmpty(Sized $this){
        return $this.getSize() == 0;
    }
}
