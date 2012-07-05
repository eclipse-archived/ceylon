class FixedSizedImpl<out Element>() satisfies FixedSized<Element>&None<Element> {
    shared actual Integer size = 0;
    shared actual Iterator<Element> iterator = bottom;
    shared actual FixedSizedImpl<Element> clone {
        return bottom;
    }
    //shared actual Element first = bottom;
    shared actual Nothing last = bottom;
}