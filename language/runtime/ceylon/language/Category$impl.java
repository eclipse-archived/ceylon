package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ignore;

@Ignore
public final class Category$impl {
    private final Category $this;

    public Category$impl(Category $this) {
        this.$this = $this;
    }
    static boolean _containsEvery(Category $this, ceylon.language.Iterable<?> elements) {
        java.lang.Object element;
        for (ceylon.language.Iterator<?> iter = elements.getIterator(); 
                !((element = iter.next()) instanceof Finished);) {
            if (!$this.contains(element)) {
                return false;
            }
        }
        return true;
    }
    public boolean containsEvery(ceylon.language.Iterable<?> elements) {
        return _containsEvery($this, elements);
    }

    static boolean _containsAny(Category $this, ceylon.language.Iterable<?> elements) {
        java.lang.Object element;
        for (ceylon.language.Iterator<?> iter = elements.getIterator(); 
                !((element = iter.next()) instanceof Finished);) {
            if ($this.contains(element)) {
                return true;
            }
        }
        return false;
    }
    public boolean containsAny(ceylon.language.Iterable<?> elements) {
        return _containsAny($this, elements);
    }
}