class SetImpl<out Element>() extends Object() satisfies Set<Element> 
        given Element satisfies Object {

    shared actual SetImpl<Element> clone = bottom;
    shared actual Iterator<Element> iterator = bottom;
    shared actual Integer size = bottom;

    shared actual Set<Element|Other> union<Other>(Set<Other> set) 
            given Other satisfies Object {
        return bottom;
    }

    shared actual Set<Element&Other> intersection<Other>(Set<Other> set)
            given Other satisfies Object {
        return bottom;
    }

    shared actual Set<Element|Other> exclusiveUnion<Other>(Set<Other> set)
            given Other satisfies Object {
        return bottom;
    }

    shared actual Set<Element> complement<Other>(Set<Other> set)
            given Other satisfies Object {
        return bottom;
    }
}