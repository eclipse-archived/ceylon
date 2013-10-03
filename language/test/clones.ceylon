class Prototype<out Element>(Element e)
           satisfies Cloneable<Prototype<Element>> {
   shared actual Prototype<Element> clone { return this; }
}

class Proto2<out Element>(element)
        satisfies Cloneable<Proto2<Element>> {
    shared Element element;
    shared actual Proto2<Element> clone { return Proto2(element); }
}

@test
shared void clones() {
    value prot = Prototype("hello");
    check(prot===prot.clone, "clone");
    value p2 = Proto2(5);
    check(!(p2===p2.clone), "clone");
    check(p2.element==p2.clone.element, "clone");
}
