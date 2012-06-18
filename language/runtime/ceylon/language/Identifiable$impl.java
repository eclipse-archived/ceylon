package ceylon.language;

import static com.redhat.ceylon.compiler.java.Util.isIdentifiable;

import com.redhat.ceylon.compiler.java.metadata.Ignore;

@Ignore
public class Identifiable$impl {
    private final java.lang.Object $this;

    public Identifiable$impl(java.lang.Object $this) {
        this.$this = $this;
    }

    public boolean equals(java.lang.Object that) {
        return _equals($this, that);
    }
    static boolean _equals(java.lang.Object $this, java.lang.Object that) {
        if (isIdentifiable(that)) {
            return $this == that;
        }
        else {
            return false;
        }
    }
    
    @Override
    public int hashCode() {
        return _hashCode($this);
    }
    static int _hashCode(java.lang.Object $this) {
        return System.identityHashCode($this);
    }
    
}