public interface Iterable<out X> extends Container {
    
    doc "Produce an iterator."
    public Iterator<X> iterator();
    
}