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
                  Cloneable<Array<Element>> &
                  Ranged<Integer, Array<Element>> {

    "Replace the existing element at the specified index 
     with the given element."
    throws (`class AssertionException`, 
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
    shared actual native Array<Element> clone;
}

"Create an array of the specified [[size]], populating every 
 index with the given [[element]]. The specified `size` must
 be in the range `0..runtime.maxArraySize`."
throws (`class AssertionException`, 
        "if `size<0` or `size>runtime.maxArraySize`")
shared native Array<Element> arrayOfSize<Element>(
        "The size of the resulting array. If the size is 
         non-positive, an empty array will be created."
        Integer size,
        "The element value with which to populate the array.
         All elements of the resulting array will have the 
         same value." 
        Element element);

