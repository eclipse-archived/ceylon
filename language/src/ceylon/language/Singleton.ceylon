doc "A sequence with exactly one element."
shared class Singleton<Element>(Element element)
        extends Object()
        satisfies Sequence<Element>
        given Element satisfies Object {

    doc "Returns 0."
    shared actual Integer lastIndex {
        return 0;
    }
    doc "Returns 1."
    shared actual Integer size {
        return 1;
    }
    doc "Returns the element contained in this `Singleton`."
    shared actual Element first {
        return element;
    }
    doc "Returns the element contained in this `Singleton`."
    shared actual Element last {
        return element;
    }
    doc "Returns `Empty`."
    shared actual Empty rest {
        return {};
    }
    doc "Returns the contained element, if the specified index is 0."
    shared actual Element? item(Integer index) {
        if (index==0) {
            return element;
        }
        else {
            return null;
        }
    }
    doc "Returns a `Singleton` with the same element."
    shared actual Singleton<Element> clone {
        return this;
    }

    shared actual default Iterator<Element> iterator {
        class SingletonIterator()
                satisfies Iterator<Element> {
            variable Element|Finished current := first;
            shared actual Element|Finished next() {
                Element|Finished result = current;
                current := exhausted;
                return result;
            }
            shared actual String string {
                return "SingletonIterator";
            }
        }
        return SingletonIterator();
    }

    shared actual String string {
        return "{ " first.string " }";
    }

    doc "Returns a `Singleton` if the starting position is 0 and the length is greater than 0."
    shared actual Element[] segment(Integer from, Integer length) {
        return from>0 || length==0 then {} else this;
    }

    doc "Returns a `Singleton` if the starting position is 0."
    shared actual Element[] span(Integer from, Integer? to) {
        return from>0 then {} else this;
    }

    doc "A `Singleton` can be equal to another `List` if that `List` has only one element which is
         equal to this `Singleton`'s element."
    shared actual Boolean equals(Object that) {
        if (is List<Element> that) {
            if (that.size!=1) {
                return false;
            }
            else if (exists elem = that[0]) {
                return elem==element;
            }
            else {
                return false;
            }
        }
        else {
            return false;
        }
    }

    shared actual Integer hash {
        return 1;
    }

    doc "Returns `true` if the specified element is this `Singleton`'s element."
    shared actual Boolean contains(Object element) {
        return this.element==element;
    }

    doc "Returns 1 if the specified element is this `Singleton`'s element, or 0 otherwise."
    shared actual Integer count(Object element) {
        return contains(element) then 1 else 0;
    }
}
