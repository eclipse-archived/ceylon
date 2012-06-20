class IterableImpl<out Element>() satisfies Iterable<Element> {

    shared actual Iterator<Element> iterator = bottom;
}