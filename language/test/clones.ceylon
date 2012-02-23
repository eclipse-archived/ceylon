class Prototype<out Element>(Element e)
           satisfies Cloneable<Prototype<Element>> {
   shared actual Prototype<Element> clone { return this; }
}

class Proto2<out Element>(Element e)
        satisfies Cloneable<Proto2<Element>> {
    shared Element element = e;
    shared actual Proto2<Element> clone { return Proto2(e); }
}

void clones() {
    value prot = Prototype("hello");
    assert(prot===prot.clone, "clone");
    value p2 = Proto2(5);
    assert(!(p2===p2.clone), "clone");
    assert(p2.element==p2.clone.element, "clone");
}
