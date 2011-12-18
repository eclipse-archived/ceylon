doc "Since sequences are immutable, this class is used for
     constructing a new sequence by incrementally appending 
     elements to the empty sequence. This class is mutable
     but threadsafe."
see (SequenceAppender)
shared class SequenceBuilder<Element>() satisfies Sized {
    doc "The resulting sequence. If no elements have been
         appended, the empty sequence."
    shared default Element[] sequence {
        throw;
    }
    doc "Append an element to the sequence."
    shared void append(Element element) {
        throw;
    }
    doc "Append multiple elements to the sequence."
    default shared void appendAll(Element... elements) {
        for (element in elements) {
            append(element);
        }
    }
    doc "The size of the resulting sequence."
    shared actual Integer size {
        return sequence.size;
    }
    doc "Determine if the resulting sequence is empty."
    shared actual Boolean empty {
        return size==0;
    }
}

doc "This class is used for constructing a new nonempty 
     sequence by incrementally appending elements to an
     existing nonempty sequence. The existing sequence is
     not modified, since sequences are immutable. This class 
     is mutable but threadsafe."
see (SequenceBuilder)
shared class SequenceAppender<Element>(Sequence<Element> elements) 
        extends SequenceBuilder<Element>() {
    doc "The resulting nonempty sequence. If no elements 
         have been appended, the original nonempty 
         sequence."
    shared actual Sequence<Element> sequence {
        throw;
    }
}