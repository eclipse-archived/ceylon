shared abstract class Object() 
        extends Void() {
        
    doc "A developer-friendly string representing the instance."
    shared formal String string;
    
    doc "Determine if this object belongs to the given |Category|.
         The binary |in| operator."
    see (Category)
    shared Boolean element(Category category) {
        return category.contains(this);
    }
}