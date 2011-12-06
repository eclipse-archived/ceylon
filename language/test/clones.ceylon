class Prototype<out Element>(Element e)
           satisfies Cloneable<Prototype<Element>> {
   shared actual Prototype<Element> clone { return this; }
}

void clones() {
    value prot = Prototype("hello");
    assert(prot===prot.clone, "clone");
}