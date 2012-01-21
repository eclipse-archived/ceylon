abstract class Array<Element>() 
        satisfies List<Element> & 
                  FixedSized<Element> &
                  Cloneable<Array<Element>> &
                  Ranged<Integer, Array<Element>> {
    shared formal void setItem(Integer index, Element element);
}

Array<Element> array<Element>(Element... elements) { throw; }
Array<Element>&None<Element> arrayOfNone<Element>() { throw; }
Array<Element>&Some<Element> arrayOfSome<Element>(Sequence<Element> elements) { throw; }