public interface Case<in X> {

    doc "Determine if the given value matches
         this case, returning |true| iff the
         value matches."
    public Boolean test(X value);
    
}