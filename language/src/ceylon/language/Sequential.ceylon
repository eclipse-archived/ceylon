shared interface Sequential<out Element>
        of Empty|Sequence<Element>
        satisfies List<Element> & 
                  Ranged<Integer,Element[]> &
                  Cloneable<Element[]> {
    
    shared actual formal Element[] reversed;
    
    doc "This sequence."
    shared actual default Element[] sequence => this;
    
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