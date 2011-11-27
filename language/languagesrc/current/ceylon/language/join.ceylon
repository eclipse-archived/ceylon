shared Element[] join<Element>(Element[]... sequences) {
    value builder = SequenceBuilder<Element>();
    for (sequence in sequences) {
        for (element in sequence) {
            builder.append(element);
        }
    }
    return builder.sequence;

}