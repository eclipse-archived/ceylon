class SomeImpl<out Element>() satisfies Some<Element> {
    shared actual List<Element> rest = bottom;
    shared actual SomeImpl<Element> clone {
        return bottom;
    }
    shared actual Iterator<Element> iterator = bottom;
    shared actual Integer size = 0;
    shared actual Element last = bottom;
}