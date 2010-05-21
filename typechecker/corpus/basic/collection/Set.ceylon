public interface Set<out X> 
        satisfies Collection<X>, Correspondence<Object, Boolean> {
    
    doc "Determine if the set is a superset of the given set.
         Return |true| if it is a superset."
    public Boolean superset(Set<Object> set);

    doc "Determine if the set is a subset of the given set.
         Return |true| if it is a subset."
    public Boolean subset(Set<Object> set);
    
    public override Set<T> with<T>(T... elements) where T <= X;
    public override OpenSet<T> copy<T>() where T <= X;

}