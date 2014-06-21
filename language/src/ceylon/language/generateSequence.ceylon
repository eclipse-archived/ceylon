"Efficiently generate a [[nonempty sequence|Sequence]] of 
 the given [[size]], by starting with the given [[first]]
 element and recursively applying the given 
 [[generator function|next]].
 
     generateSequence(100, 1, (Integer index) => index*2);"
throws (`class AssertionError`, "if `size<=0`")
see (`function sequence`,
     `function populateSequence`)
by ("Gavin")
shared [Element+] generateSequence<Element>(
        "The [[size|Sequence.size]] of the resulting sequence."
        Integer size,
        "The [[first element|Sequence.first]] of the resulting
         sequence."
        Element first,
        "A function to generate an element of the sequence, 
         given the [[previous]] generated element."
        Element next(Element previous)) {
    
    "size of the nonempty sequence must be greater than
     zero"
    assert (size>0);
    
    return ArraySequence(generateArray(size, first, next));
}
