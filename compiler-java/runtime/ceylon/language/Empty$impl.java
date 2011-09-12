package ceylon.language;

public class Empty$impl {
    public static Natural getSize(Empty $this){
        return Natural.instance(0);
    }
    public static boolean getEmpty(Empty $this){
        return true;
    }
    public static Iterator<java.lang.Object> getIterator(Empty $this){
        return emptyIterator.getEmptyIterator();
    }
    public static java.lang.Object item(Empty $this, Natural key){
        return null;
    }
    public static java.lang.Object getFirst(Empty $this){
        return null;
    }
}
