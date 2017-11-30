/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
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
     array[0] = \"goodbye\";
 
 Arrays are lists and support all operations inherited from 
 [[List]], along with certain additional operations for 
 efficient mutation of the array: [[set]], [[swap]], [[move]], 
 [[sortInPlace]], [[reverseInPlace]], and [[copyTo]].
 
 This class is provided primarily to ease interoperation 
 with Java, and for some performance-critical low-level 
 programming tasks.
 
 On the JVM, for \"primitive\" element types [[Integer]], 
 [[Float]], [[Byte]], [[Character]], and [[Boolean]], 
 `Array` is easily the most efficient sort of `List` in 
 Ceylon. However, certain optimizations made by the compiler
 are impossible if the `Array` is assigned to a more generic 
 type such as [[Iterable]]. Peak efficiency is obtained for 
 algorithms coded to the static type `Array`.
 
 Furthermore, `Array` is itself a compromise between raw 
 performance, polymorphism, and portability. An instance of
 the Java primitive array type `java.lang.LongArray` 
 (written `long[]` in Java) exhibits superior micro-level 
 performance to an `Array<Integer>`, but:
 
 - is not a `List`, and
 - is not available except when compiling for the JVM.
 
 Fortunately, given a Java primitive array, it's easy to 
 obtain an `Array` backed by the primitive array:
 
     //unportable JVM-specific code 
     LongArray longArray = LongArray(size);
     Array<Integer> array = longArray.integerArray;"
tagged("Collections")
shared final serializable native 
class Array<Element>
        satisfies SearchableList<Element> 
                & Ranged<Integer,Element,Array<Element>> 
                & IndexedCorrespondenceMutator<Element> {
    
    "Create an array with the given [[elements]]."
    shared native new ({Element*} elements) {}
    
    "Create an array of the specified [[size]], populating 
     every index with the given [[element]]. The specified 
     `size` must be no larger than [[runtime.maxArraySize]].
     If `size<=0`, the new array will have no elements."
    throws (class AssertionError, 
        "if `size>runtime.maxArraySize`")
    see (value runtime.maxArraySize)
    since("1.2.0")
    shared native new ofSize(
            "The size of the resulting array. If the size is 
             non-positive, an empty array will be created."
            Integer size, 
            "The element value with which to populate the 
             array. All elements of the resulting array will 
             have the same value."
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
    
    "The immutable number of elements of this array."
    aliased ("length")
    shared actual native Integer size;
    
    shared actual native Boolean empty;
    shared actual native Boolean defines(Integer index);
    shared actual native Iterator<Element> iterator();
    shared actual native Boolean contains(Object element);
    shared actual native [Element+]|[] sequence();
    shared actual native {Element&Object*} coalesced;
    
    "A new array with the same elements as this array."
    shared actual native Array<Element> clone();
    
    "Replace the existing element at the specified [[index]] 
     with the given [[element]]."
    throws (class AssertionError,
        "if the given index is out of bounds, that is, if 
         `index<0` or if `index>lastIndex`")
    shared actual native 
    void set(
        "The index of the element to replace."
        Integer index,
        "The new element."
        Element element);
    
    "Efficiently copy the elements in the measure
     `sourcePosition:length` of this array to the measure 
     `destinationPosition:length` of the given 
     [[array|destination]], which may be this array.
     
     The given [[sourcePosition]] and [[destinationPosition]] 
     must be non-negative and, together with the given 
     [[length]], must identify meaningful ranges within the 
     two arrays, satisfying:
     
     - `size >= sourcePosition+length`, and 
     - `destination.size >= destinationPosition+length`.
     
     If the given `length` is not strictly positive, no
     elements are copied."
    throws (class AssertionError, 
        "if the arguments do not identify meaningful ranges 
         within the two arrays:
         
         - if the given [[sourcePosition]] or 
           [[destinationPosition]] is negative, 
         - if `size < sourcePosition+length`, or 
         - if `destination.size < destinationPosition+length`.")
    shared native 
    void copyTo(
        "The array into which to copy the elements, which 
         may be this array."
        Array<in Element> destination,
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
    {Element*} filter(Boolean selecting(Element element));
    shared actual native
    Element? find(Boolean selecting(Element&Object element));
    shared actual native
    Element? findLast(Boolean selecting(Element&Object element));
    shared actual native
    Integer? firstIndexWhere(Boolean selecting(Element&Object element));
    shared actual native
    Integer? lastIndexWhere(Boolean selecting(Element&Object element));
    shared actual native 
    {Integer*} indexesWhere(Boolean selecting(Element&Object element));
    shared actual native
    <Integer->Element&Object>? locate(Boolean selecting(Element&Object element));
    shared actual native
    <Integer->Element&Object>? locateLast(Boolean selecting(Element&Object element));
    shared actual native
    {<Integer->Element&Object>*} locations(Boolean selecting(Element&Object element));
    shared actual native
    Result|Element|Null reduce<Result>
            (Result accumulating(Result|Element partial, Element element));
    
    shared actual native 
    Boolean occursAt(Integer index, Element element);
    shared actual native
    Integer? firstOccurrence(Element element, Integer from, Integer length);
    shared actual native
    Integer? lastOccurrence(Element element, Integer from, Integer length);
    shared actual native
    Boolean occurs(Element element, Integer from, Integer length);
    
    shared actual native 
    {Integer*} occurrences(Element element, Integer from, Integer length);
    
    "Given two indices within this array, efficiently swap 
     the positions of the elements at these indices. If the 
     two given indices are identical, no change is made to 
     the array. The array always contains the same elements
     before and after this operation."
    throws (class AssertionError,
        "if either of the given indices is out of bounds") 
    since("1.2.0")
    shared native
    void swap(
            "The index of the first element."
            Integer i,
            "The index of the second element." 
            Integer j);
    
    "Efficiently move the element of this array at the given 
     [[source index|from]] to the given 
     [[destination index|to]], shifting every element 
     falling between the two given indices by one position 
     to accommodate the change of position. If the source 
     index is larger than the destination index, elements 
     are shifted toward the end of the array. If the source 
     index is smaller than the destination index, elements 
     are shifted toward the start of the array. If the given 
     indices are identical, no change is made to the array. 
     The array always contains the same elements before and 
     after this operation."
    throws (class AssertionError,
        "if either of the given indices is out of bounds") 
    since("1.2.0")
    shared native
    void move(
            "The source index of the element to move."
            Integer from, 
            "The destination index to which the element is
             moved."
            Integer to);
    
    "Reverses the order of the current elements in this 
     array. This operation works by side-effect, modifying 
     the array. The array always contains the same elements 
     before and after this operation."
    since("1.1.0")
    shared native 
    void reverseInPlace();
    
    "Sorts the elements in this array according to the 
     order induced by the given 
     [[comparison function|comparing]]. This operation works 
     by side-effect, modifying the array.  The array always 
     contains the same elements before and after this 
     operation."
    since("1.1.0")
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
    [Element+]|[] sort(
        "A comparison function that compares pairs of
         elements of this array."
        Comparison comparing(Element x, Element y));
    
    equals(Object that) 
            => (super of List<Element>).equals(that);
    hash => (super of List<Element>).hash;
    string => (super of Collection<Element>).string;
    
}
