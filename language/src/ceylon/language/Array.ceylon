"A fixed-size array of elements. An array may have zero
 size (an empty array). Arrays are mutable. Any element
 of an array may be set to a new value.
 
 This class is provided primarily to support interoperation 
 with Java, and for some performance-critical low-level 
 programming tasks."
shared final native class Array<Element>({Element*} elements) 
        extends Object()
        satisfies List<Element> &
                  Cloneable<Array<Element>> &
                  Ranged<Integer, Array<Element>> {

    "Replace the existing element at the specified index 
     with the given element. Does nothing if the specified 
     index is negative or larger than the index of the 
     last element in the array."
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
     `destinationPosition:length` of the given array."
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
    
    shared actual native Array<Element> span(Integer from, Integer to);
    shared actual native Array<Element> spanFrom(Integer from);
    shared actual native Array<Element> spanTo(Integer to);
    shared actual native Array<Element> segment(Integer from, Integer length);
}

"Create an array of the specified size, populating every 
 index with the given element. If the specified size is
 smaller than `1`, return an empty array of the given
 element type."
shared native Array<Element> arrayOfSize<Element>(
        "The size of the resulting array. If the size
         is non-positive, an empty array will be 
         created."
        Integer size,
        "The element value with which to populate the
         array. All elements of the resulting array 
         will have the same value." 
        Element element);

