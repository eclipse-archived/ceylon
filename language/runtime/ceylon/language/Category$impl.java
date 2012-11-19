package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ignore;

@Ignore
public final class Category$impl {
    private final Category $this;

    public Category$impl(Category $this) {
        this.$this = $this;
    }
    public static boolean _containsEvery(Category $this, ceylon.language.List<?> elements) {
        java.lang.Object element;
        for (ceylon.language.Iterator<?> iter = elements.getIterator(); 
                !((element = iter.next()) instanceof Finished);) {
            if (!$this.contains(element)) {
                return false;
            }
        }
        return true;
    }
    public boolean containsEvery(ceylon.language.List<?> elements) {
        return _containsEvery($this, elements);
    }
    public boolean containsEvery() {
        return _containsEvery($this, empty_.getEmpty$());
    }
    public ceylon.language.List containsEvery$elements() {
        return empty_.getEmpty$();
    }

    public static boolean _containsAny(Category $this, ceylon.language.List<?> elements) {
        java.lang.Object element;
        for (ceylon.language.Iterator<?> iter = elements.getIterator(); 
                !((element = iter.next()) instanceof Finished);) {
            if ($this.contains(element)) {
                return true;
            }
        }
        return false;
    }
    public boolean containsAny(ceylon.language.List<?> elements) {
        return _containsAny($this, elements);
    }
    public boolean containsAny() {
        return _containsAny($this, empty_.getEmpty$());
    }
    public ceylon.language.List containsAny$elements() {
        return empty_.getEmpty$();
    }
}