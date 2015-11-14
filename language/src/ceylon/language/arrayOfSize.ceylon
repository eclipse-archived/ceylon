"Create an array of the specified [[size]], populating every 
 index with the given [[element]]. The specified `size` must 
 be no larger than [[runtime.maxArraySize]]. If `size<=0`, 
 the new array will have no elements."
throws (`class AssertionError`, 
        "if `size>runtime.maxArraySize`")
see (`value runtime.maxArraySize`)
deprecated ("Use [[Array.ofSize]]")
tagged("Collections")
shared Array<Element> arrayOfSize<Element>(
        "The size of the resulting array. If the size is 
         non-positive, an empty array will be created."
        Integer size,
        "The element value with which to populate the array.
         All elements of the resulting array will have the 
         same value." 
        Element element) 
        => Array.ofSize(size, element);
