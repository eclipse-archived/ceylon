abstract class Array<Element>() 
        extends Object()
        satisfies List<Element> & 
                  FixedSized<Element> &
                  Cloneable<Array<Element>> &
                  Ranged<Integer, Array<Element>> {
    
    shared formal void setItem(Integer index, Element element);
    
    shared actual Boolean equals(Object that) {
        //TODO: copy/pasted from List
        if (is List<Object> that) {
            if (that.size==size) {
                for (i in 0..size-1) {
                    value x = this[i];
                    value y = that[i];
                    if (!exists x && !exists y) {
                        continue;
                    }
                    if (is Object x) {
                        if (is Object y) {
                            if (x==y) {
                                continue;
                            }
                        }
                    }
                    return false;
                }
                else {
                    return true;
                }
            }
        }
        return false;
    }
    
    shared actual Integer hash {
        //TODO: copy/pasted from List
        return size;
    }
    
}

Array<Element> array<Element>(Element... elements) { throw; }
Array<Element>&None<Element> arrayOfNone<Element>() { throw; }
Array<Element>&Some<Element> arrayOfSome<Element>(Sequence<Element> elements) { throw; }