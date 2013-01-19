doc "A possibly-empty, immutable sequence of values. The 
     type `Sequential<Element>` may be abbreviated 
     `[Element*]` or `Element[]`. 
     
     `Sequential` has two enumerated subtypes:
     
     - `Empty`, abbreviated `[]`, represents an empty 
        sequence, and
     - `Sequence<Element>`, abbreviated `[Element+]` 
        represents a non-empty sequence, and has the very
        important subclass `Tuple`."
see (Tuple)
shared interface Sequential<out Element>
        of Empty|Sequence<Element>
        satisfies List<Element> & 
                  Ranged<Integer,Element[]> &
                  Cloneable<Element[]> {
    
    doc "Reverse this sequence, returning a new sequence."
    shared actual formal Element[] reversed;
    
    doc "This sequence."
    shared actual default Element[] sequence => this;
    
    doc "The rest of the sequence, without the first 
         element."
    shared actual formal Element[] rest;
    
    shared actual default Element[] clone => this;
    
    doc "A string of form `\"[ x, y, z ]\"` where `x`, `y`, 
         and `z` are the `string` representations of the 
         elements of this collection, as produced by the
         iterator of the collection, or the string `\"{}\"` 
         if this collection is empty. If the collection 
         iterator produces the value `null`, the string
         representation contains the string `\"null\"`."
    shared actual default String string => 
            empty then "[]" else "[ " commaList(this) " ]";
    
}