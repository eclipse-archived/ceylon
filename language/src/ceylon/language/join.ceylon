doc "Given a list of sequences, return a new sequence 
     formed by incrementally appending each sequence in the
     list to the empty sequence. If the list of sequences is
     empty, return the empty sequence."
see (SequenceBuilder)
shared Element[] join<Element>(Element[]... sequences) {
    value builder = SequenceBuilder<Element>();
    for (sequence in sequences) {
        builder.appendAll(sequence...);
    }
    return builder.sequence;
}