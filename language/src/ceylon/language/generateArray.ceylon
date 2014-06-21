"Efficiently generate an [[Array]] of the given [[size]], 
 by starting with the given [[first]] element and 
 recursively applying the given [[generator function|next]].
 
     generateArray(100, 1, (Integer last) => last*2);"
throws (`class AssertionError`, 
        "if `size<=0` or `size>runtime.maxArraySize`")
see (`class Array`,
     `function populateArray`,
     `value runtime.maxArraySize`)
by ("Gavin")
shared native Array<Element> generateArray<Element>(
        "The [[size|Array.size]] of the resulting array."
        Integer size,
        "The [[first element|Array.first]] of the resulting
         array."
        Element first,
        "A function to generate an element of the array, 
         given the [[previous]] generated element."
        Element next(Element previous));