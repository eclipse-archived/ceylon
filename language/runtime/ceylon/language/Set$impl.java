package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ignore;

@Ignore
public final class Set$impl<Element> {
    
    private final Set<Element> $this;

    public Set$impl(Set<Element> $this) {
        this.$this = $this;
    }
    
    public long count(java.lang.Object element) {
        return _count($this, element);
    }
    static <Element> long _count(final Set<Element> $this, java.lang.Object element) {
        return $this.contains(element) ? 1 : 0;
    }
    
    public boolean superset(Set<? extends java.lang.Object> set) {
        return _superset($this, set);
    }
    static <Element> boolean _superset(Set<Element> $this, Set<? extends java.lang.Object> set) {
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
    static <Element> boolean _subset(Set<Element> $this, Set<? extends java.lang.Object> set) {
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
    static <Element> boolean _equals(final Set<Element> $this, java.lang.Object that) {
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
    static <Element> int _hashCode(final Set<Element> $this) {
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