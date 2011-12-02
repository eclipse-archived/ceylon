shared Element[] join<Element>(Element[]... sequences) {
    value builder = SequenceBuilder<Element>();
    for (sequence in sequences) {
        builder.appendAll(sequence...);
    }
    return builder.sequence;

}