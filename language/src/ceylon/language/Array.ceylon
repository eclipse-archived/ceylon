"A fixed-sized array of mutable elements. An _empty_ array 
 is an array of [[size]] `0`. Any element of an array may be
 set to a new value.
 
     value array = Array { \"hello\", \"world\" };
     array.set(0, \"goodbye\");
 
 This class is provided primarily to support interoperation 
 with Java, and for some performance-critical low-level 
 programming tasks."
shared final native class Array<Element>({Element*} elements) 
        extends Object()
        satisfies List<Element> &
                  Ranged<Integer, Array<Element>> {

    "Replace the existing element at the specified index 
     with the given element."
    throws (`class AssertionError`, 
            "if the given index is out of bounds, that is, 
             if `index<0` or if `index>lastIndex`")
    shared native void set(
            "The index of the element to replace."
            Integer index, 
            "The new element."
            Element element);
    
    "Reverse this array, returning a new array."
    shared actual native Array<Element> reversed;

    "The rest of the array, without the first element."
    shared actual native Array<Element> rest;

    "Efficiently copy the elements in the segment
     `sourcePosition:length` of this array to the segment 
     `destinationPosition:length` of the given 
     [[array|other]]."
    shared native void copyTo(
        "The array into which to copy the elements." 
        Array<Element> other,
        "The index of the first element in this array to copy."  
        Integer sourcePosition = 0, 
        "The index in the given array into which to 
         copy the first element."
        Integer destinationPosition = 0, 
        "The number of elements to copy."
        Integer length = size-sourcePosition);
    
    shared actual native Element? get(Integer index);
    
    shared actual native Integer lastIndex;
    
    shared actual native Array<Element> span(Integer from, Integer to);
    shared actual native Array<Element> spanFrom(Integer from);
    shared actual native Array<Element> spanTo(Integer to);
    shared actual native Array<Element> segment(Integer from, Integer length);
    
    "A new array with the same elements as this array."
    shared actual native Array<Element> clone();
}
