package ceylon.language;

public interface Iterator<Element> {
    
    public Element getHead();
    public Iterator<Element> getTail();
}
