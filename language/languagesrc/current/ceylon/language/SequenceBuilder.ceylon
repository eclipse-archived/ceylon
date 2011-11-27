shared class SequenceBuilder<Element>() {
    shared default Element[] sequence {
        throw;
    }
    shared void append(Element element) {
        throw;
    }
    default shared void appendAll(Element... elements) {
        throw;
    }
}

shared class SequenceAppender<Element>(Sequence<Element> elements) 
        extends SequenceBuilder<Element>() {
    shared actual Sequence<Element> sequence {
        throw;
    }
}