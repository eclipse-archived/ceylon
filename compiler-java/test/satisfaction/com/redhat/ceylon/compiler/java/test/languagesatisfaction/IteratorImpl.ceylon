class IteratorImpl<out Element>() satisfies Iterator<Element> {
    shared actual Element|Finished next() {
        return bottom;
    }
}