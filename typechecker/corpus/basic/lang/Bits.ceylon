public interface Bits<T> {
        
    doc "Bitwise or operator x|y"
    public T or(T bits);
    
    doc "Bitwise and operator x&y"
    public T and(T bits);
    
    doc "Bitwise xor operator x^y"
    public T xor(T bits);
    
    doc "Bitwise complement operator ~x"
    public T complement;
    
    doc "A list of the bits."
    public List<Boolean> bits;
    
}