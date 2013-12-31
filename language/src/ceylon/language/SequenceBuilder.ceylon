"Since sequences are immutable, this class is used for
 constructing a new sequence by incrementally appending 
 elements to the empty sequence."
see (`class SequenceAppender`, 
     `function concatenate`, 
     `class Singleton`)
shared native class SequenceBuilder<Element>() {
    
    "The resulting sequence. If no elements have been
     appended, the empty sequence."
    shared native default Element[] sequence;
    
    "Append an element to the sequence and return this 
     `SequenceBuilder`"
    shared native SequenceBuilder<Element> append(Element element);
    
    "Append multiple elements to the sequence and return 
     this `SequenceBuilder`"
    default shared SequenceBuilder<Element> appendAll({Element*} elements) {
        for (element in elements) {
            append(element);
        }
        return this;
    }
    
    "The size of the resulting sequence."
    shared Integer size => sequence.size;
    
    "Determine if the resulting sequence is empty."
    shared Boolean empty => size==0;
    
}

"This class is used for constructing a new nonempty 
 sequence by incrementally appending elements to an
 existing nonempty sequence. The existing sequence is
 not modified, since `Sequence`s are immutable."
see (`class SequenceBuilder`)
shared native class SequenceAppender<Element>([Element+] elements) 
        extends SequenceBuilder<Element>() {
    
    "The resulting nonempty sequence. If no elements 
     have been appended, the original nonempty 
     sequence."
    shared actual native [Element+] sequence;
    
}