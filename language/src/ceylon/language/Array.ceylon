doc "A fixed-size array of elements. An array may have zero
     size (an empty array). Arrays are mutable. Any element
     of an array may be set to a new value.
     
     This class is provided primarily to support interoperation 
     with Java, and for some performance-critical low-level 
     programming tasks."
shared abstract class Array<Element>() 
        extends Object()
        satisfies List<Element> &
                  Cloneable<Array<Element>> &
                  Ranged<Integer, Array<Element>> {

    doc "Replace the existing element at the specified index 
         with the given element. Does nothing if the specified 
         index is negative or larger than the index of the 
         last element in the array."
    shared formal void setItem(
            doc "The index of the element to replace."
            Integer index, 
            doc "The new element."
            Element element);
    
    doc "Reverse this array, returning a new array."
    shared actual formal Array<Element> reversed;

}

doc "Create an array containing the given elements. If no
     elements are provided, create an empty array of the
     given element type."
shared Array<Element> array<Element>(Element... elements) { throw; }

doc "Create an array of the specified size, populating every 
     index with the given element. If the specified size is
     smaller than `1`, return an empty array of the given
     element type."
shared Array<Element> arrayOfSize<Element>(
        doc "The size of the resulting array. If the size
             is non-positive, an empty array will be 
             created."
        Integer size,
        doc "The element value with which to populate the
             array. All elements of the resulting array 
             will have the same value." 
        Element element) { throw; }

doc "Efficiently copy the elements of one array to another 
     array."
shared void copyArray<Element>(
        doc "The source array containing the elements to be 
             copied."
        Array<Element> source,
        doc "The target array into which to copy the 
             elements." 
        Array<Element> target,
        doc "The index of the first element to copy in the
             source array." 
        Integer from, 
        doc "The index in the target array into which to 
             copy the first element."
        Integer to, 
        doc "The number of elements to copy."
        Integer length) { throw; }
        //TODO: defaults!
        //Integer from=0, Integer to=0, Integer length=smallest(source.size,target.size)) { throw; }
        