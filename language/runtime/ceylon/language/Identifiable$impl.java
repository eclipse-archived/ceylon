package ceylon.language;

import static com.redhat.ceylon.compiler.java.Util.isIdentifiable;

import com.redhat.ceylon.compiler.java.metadata.Ignore;

@Ignore
public class Identifiable$impl {
    private final Identifiable $this;

    public Identifiable$impl(Identifiable $this) {
        this.$this = $this;
    }

    public boolean equals(java.lang.Object that) {
        return _equals($this, that);
    }
    static boolean _equals(Identifiable $this, java.lang.Object that) {
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
    static int _hashCode(Identifiable $this) {
        return System.identityHashCode($this);
    }
    
}