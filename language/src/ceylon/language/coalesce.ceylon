doc "Return a sequence of all elements of the given sequence
     which are not null."
shared Element[] coalesce<Element>(Element?[] sequence) 
        given Element satisfies Object {
    value builder = SequenceBuilder<Element>();
    for (element in sequence) {
        if (exists element) {
            builder.append(element);
        }
    }
    return builder.sequence;
}