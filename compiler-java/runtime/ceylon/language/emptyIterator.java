package ceylon.language;

public class emptyIterator implements Iterator<java.lang.Object> {

    private static final emptyIterator instance = new emptyIterator();
    
    public static emptyIterator getEmptyIterator(){
        return instance;
    }
    
    @Override
    public Object getHead() {
        return null;
    }

    @Override
    public Iterator<java.lang.Object> getTail() {
        return this;
    }

}
