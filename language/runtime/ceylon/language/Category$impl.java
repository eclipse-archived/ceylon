package ceylon.language;

public class Category$impl {
    public static boolean containsEvery(Category $this, ceylon.language.Iterable<? extends Object> elements) {
        for (ceylon.language.Iterator<? extends ceylon.language.Object> $element$iter$1 = elements.getIterator(); $element$iter$1 != null; $element$iter$1 = $element$iter$1.getTail()) {
            final Object element = $element$iter$1.getHead();
            if (!$this.contains(element)) {
                return false;
            }
        }
        return true;
    }

    public static boolean containsAny(Category $this, ceylon.language.Iterable<? extends Object> elements) {
        for (ceylon.language.Iterator<? extends ceylon.language.Object> $element$iter$1 = elements.getIterator(); $element$iter$1 != null; $element$iter$1 = $element$iter$1.getTail()) {
            final Object element = $element$iter$1.getHead();
            if ($this.contains(element)) {
                return true;
            }
        }
        return false;
    }
}
