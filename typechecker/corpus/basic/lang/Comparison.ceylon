public class Comparison() {
    
    doc "The receiving object is larger than 
         the given object."
    case larger, 
    
    doc "The receiving object is smaller than 
         the given object."
    case smaller, 
    
    doc "The receiving object is exactly equal 
         to the given object."
    case equal;
        
    public Boolean larger return this==larger;
    public Boolean smaller return this==smaller;
    public Boolean equal return this==equal;
    public Boolean unequal return this!=equal;
    public Boolean largeAs return this!=smaller;
    public Boolean smallAs return this!=larger;
    
}