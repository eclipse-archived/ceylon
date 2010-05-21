public interface Bag<out X> 
        satisfies Collection<X>, Correspondence<Object, Natural> {
    
    doc "A map from element to the number of occurrences of 
         the element. The returned map reflects changes to 
         the original bag."
    public Map<X,Natural> map;
    
    public override Bag<T> with<T>(T... elements) where T <= X;
    public override OpenBag<T> copy<T>() where T <= X;
    
}