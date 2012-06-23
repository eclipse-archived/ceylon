doc "Given a list of sequences, return a new sequence 
     formed by incrementally appending each sequence in the
     list to the empty sequence. If the list of sequences is
     empty, return the empty sequence."
see (SequenceBuilder)
shared Element[] join<Element>(Element[]... sequences) {
    return { for (sequence in sequences) for (element in sequence) element };
}