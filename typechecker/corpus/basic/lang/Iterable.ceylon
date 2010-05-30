public interface Iterable<out X> satisfies Container {
    
    doc "Produce an iterator."
    public Iterator<X> iterator();
    
}