"Efficiently generate a [[nonempty sequence|Sequence]] of 
 the given [[size]], by starting with the given [[first]]
 element and recursively applying the given 
 [[generator function|next]].
 
     generateSequence(100, 1, (Integer last) => last*2);
 
 Hint: to generate a sequence using a function of the 
 element index, use `collect()` on a [[Range]]:
 
     (0..100).collect((Integer index) => index*index)"
throws (`class AssertionError`, "if `size<=0`")
see (`function sequence`,
    `function Sequence.collect`)
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
    
    value array = arrayOfSize(size, first);
    variable value element = first;
    for (index in 1:size-1) {
        element = next(element);
        array.set(index, element);
    }
    return ArraySequence(array);
}
