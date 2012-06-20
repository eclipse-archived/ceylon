class NoneImpl<out Element>() satisfies None<Element> {

    shared actual NoneImpl<Element> clone {
        return bottom;
    }
}