"Efficiently populate a [[nonempty sequence|Sequence]] of 
 the given [[size]], using a [[function|element]] of the 
 element index.
 
     populateSequence(100, 1, (Integer last) => last*2);"
throws (`class AssertionError`, "if `size<=0`")
see (`function sequence`,
     `function generateSequence`)
by ("Gavin")
shared [Element+] populateSequence<Element>(
        "The [[size|Sequence.size]] of the resulting sequence."
        Integer size,
        "A function to populate an element of the sequence, 
         given its [[index]]."
        Element element(Integer index)) {
    
    "size of the nonempty sequence must be greater than
     zero"
    assert (size>0);
    
    return ArraySequence(populateArray(size, element));
}
