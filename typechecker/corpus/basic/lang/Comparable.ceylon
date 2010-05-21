public interface Comparable<in T> {
    
    doc "The binary compare operator |<=>|. Compares this 
         object with the given object."
    public Comparison compare(T other);
    
}