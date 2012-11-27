package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ignore;

@Ignore
public final class Set$impl<Element> {
    private final ceylon.language.Iterable$impl<Element> $ceylon$language$Iterable$this;

    private final Set<Element> $this;

    public Set$impl(Set<Element> $this) {
        this.$ceylon$language$Iterable$this = new ceylon.language.Iterable$impl<Element>($this);
        this.$this = $this;
    }
    
    public long count(Callable<? extends Boolean> f) {
        return $ceylon$language$Iterable$this.count(f);
    }

    public boolean superset(Set<? extends java.lang.Object> set) {
        return _superset($this, set);
    }
    private static <Element> boolean _superset(Set<Element> $this, Set<? extends java.lang.Object> set) {
        java.lang.Object elem;
        for (ceylon.language.Iterator<? extends java.lang.Object> iter = set.getIterator(); 
                !((elem = iter.next()) instanceof Finished);) {
            if (!$this.contains(elem)) return false;
        }
        return true;
    }
    
    public boolean subset(Set<? extends java.lang.Object> set) {
        return _subset($this, set);
    }
    private static <Element> boolean _subset(Set<Element> $this, Set<? extends java.lang.Object> set) {
        java.lang.Object elem;
        for (ceylon.language.Iterator<? extends Element> iter = $this.getIterator(); 
                !((elem = iter.next()) instanceof Finished);) {
            if (!set.contains(elem)) return false;
        }
        return true;
    }
    
    public boolean equals(java.lang.Object that) {
        return _equals($this, that);
    }
    private static <Element> boolean _equals(final Set<Element> $this, java.lang.Object that) {
        if (that instanceof Set) {
            Set other = (Set) that;
            if (other.getSize()==$this.getSize()) {
                java.lang.Object elem;
                for (ceylon.language.Iterator<? extends Element> iter = $this.getIterator(); 
                        !((elem = iter.next()) instanceof Finished);) {
                    if (!other.contains(elem)) return false;
                }
                return true;
            }
        }
        return false;
    }
    
    public int hashCode() {
        return _hashCode($this);
    }
    private static <Element> int _hashCode(final Set<Element> $this) {
        int hashCode = 1;
        java.lang.Object elem;
        for (Iterator<? extends Element> iter=$this.getIterator(); !((elem = iter.next()) instanceof Finished);) {
            hashCode *= 31;
            if (elem != null) {
                hashCode += elem.hashCode();
            }
        }
        return hashCode;
    }
}