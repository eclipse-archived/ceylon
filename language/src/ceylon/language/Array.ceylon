"A fixed-sized array of mutable elements. An _empty_ array 
 is an array of [[size]] `0`. An array may be created with
 a list of initial elements, or, via the constructor 
 [[ofSize]], with a size and single initial value for all 
 elements.
 
     value array = Array { \"hello\", \"world\" };
     value ints = Array.ofSize(1k, 0);
 
 Arrays are mutable. Any element of an array may be set to a 
 new value.
 
     value array = Array { \"hello\", \"world\" };
     array.set(0, \"goodbye\");
 
 This class is provided primarily to support interoperation 
 with Java, and for some performance-critical low-level 
 programming tasks."
shared final serializable native class Array<Element>
        satisfies List<Element> &
                  Ranged<Integer,Element,Array<Element>> {
    
    "Create an array with the given [[elements]]."
    shared native new ({Element*} elements) {}
    
    "Create an array of the specified [[size]], populating 
     every index with the given [[element]]. The specified 
     `size` must be no larger than [[runtime.maxArraySize]].
     If `size<=0`, the new array will have no elements."
    throws (`class AssertionError`, 
        "if `size>runtime.maxArraySize`")
    see (`value runtime.maxArraySize`)
    shared native new ofSize(
            "The size of the resulting array. If the size is 
             non-positive, an empty array will be created."
            Integer size, 
            "The element value with which to populate the array.
             All elements of the resulting array will have the 
             same value."
            Element element) {
        assert (size<runtime.maxArraySize);
    }
    
    //"Get the element at the specified index, or `null` if
    // the index falls outside the bounds of this array."
    //shared actual native Element? get(Integer index);
    
    "Get the element at the specified index, where the array
     is indexed from the _end_ of the array, or `null` if
     the index falls outside the bounds of this array."
    shared actual native Element? getFromLast(Integer index);
    
    "Get the element at the specified index, or `null` if
     the index falls outside the bounds of this array."
    shared actual native Element? getFromFirst(Integer index);
    
    //shared actual native Boolean->Element? lookup(Integer index);
    
    shared actual native Integer? lastIndex;
    
    shared actual native Element? first;
    shared actual native Element? last;
    
    shared actual native Boolean empty;
    shared actual native Integer size;
    shared actual native Boolean defines(Integer index);
    shared actual native Iterator<Element> iterator();
    shared actual native Boolean contains(Object element);
    shared actual native Element[] sequence();
    shared actual native {Element&Object*} coalesced;
    
    "A new array with the same elements as this array."
    shared actual native Array<Element> clone();
    
    "Replace the existing element at the specified index 
     with the given element."
    throws (`class AssertionError`,
        "if the given index is out of bounds, that is, if 
         `index<0` or if `index>lastIndex`")
    shared native 
    void set(
        "The index of the element to replace."
        Integer index,
        "The new element."
        Element element);
    
    "Efficiently copy the elements in the segment
     `sourcePosition:length` of this array to the segment 
     `destinationPosition:length` of the given 
     [[array|destination]]."
    shared native 
    void copyTo(
        "The array into which to copy the elements."
        Array<Element> destination,
        "The index of the first element in this array to 
         copy."
        Integer sourcePosition = 0,
        "The index in the given array into which to copy the 
         first element."
        Integer destinationPosition = 0,
        "The number of elements to copy."
        Integer length 
                = smallest(size - sourcePosition,
                    destination.size - destinationPosition));
        
    shared actual native 
    Array<Element> span(Integer from, Integer to);
    shared actual native 
    Array<Element> spanFrom(Integer from);
    shared actual native 
    Array<Element> spanTo(Integer to);
    shared actual native 
    Array<Element> measure(Integer from, Integer length);
    
    shared actual native {Element*} skip(Integer skipping);
    shared actual native {Element*} take(Integer taking);
    shared actual native {Element*} by(Integer step);
    
    shared actual native 
    Integer count(Boolean selecting(Element element));
    shared actual native 
    void each(void step(Element element));
    shared actual native
    Boolean any(Boolean selecting(Element element));
    shared actual native
    Boolean every(Boolean selecting(Element element));
    shared actual native
    Element? find(Boolean selecting(Element&Object element));
    shared actual native
    Element? findLast(Boolean selecting(Element&Object element));
    shared actual native
    <Integer->Element>? locate(Boolean selecting(Element&Object element));
    shared actual native
    <Integer->Element>? locateLast(Boolean selecting(Element&Object element));
    shared actual native
    Result|Element|Null reduce<Result>
            (Result accumulating(Result|Element partial, Element element));
    
    shared actual native
    Integer? firstOccurrence(Anything element);
    shared actual native
    Integer? lastOccurrence(Anything element);
    shared actual native
    Boolean occurs(Anything element);
    
    "Reverses the order of the current elements in this 
     array. This operation works by side-effect, modifying 
     the array."
    shared native 
    void reverseInPlace();
    
    "Sorts the elements in this array according to the 
     order induced by the given 
     [[comparison function|comparing]]. This operation 
     works by side-effect, modifying the array."
    shared native 
    void sortInPlace(
        "A comparison function that compares pairs of
         elements of this array."
        Comparison comparing(Element x, Element y));
    
    "Sorts the elements in this array according to the 
     order induced by the given 
     [[comparison function|comparing]], returning a new
     sequence. This operation has no side-effect, and does
     not modify the array."
    shared actual native 
    Element[] sort(
        "A comparison function that compares pairs of
         elements of this array."
        Comparison comparing(Element x, Element y));
    
    shared actual Boolean equals(Object that) 
            => (super of List<Element>).equals(that);
    shared actual Integer hash 
            => (super of List<Element>).hash;
}
