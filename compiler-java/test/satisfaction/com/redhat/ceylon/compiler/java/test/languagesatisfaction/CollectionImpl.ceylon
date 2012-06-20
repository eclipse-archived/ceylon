class CollectionImpl<out Element>() satisfies Collection<Element> {
    shared actual CollectionImpl<Element> clone {
        return bottom;
    }
    shared actual Integer size = 0;
    shared actual Iterator<Element> iterator = bottom;
}