doc "A fixed-size array of elements which may or may not be empty."
shared abstract class Array<Element>() 
        extends Object()
        satisfies List<Element> & 
                  FixedSized<Element> &
                  Cloneable<Array<Element>> &
                  Ranged<Integer, Array<Element>> {

    doc "Replaces the existing item at the specified index with
         the specified element. Does nothing if the index is
         negative or beyond the array size."
    shared formal void setItem(Integer index, Element? element);

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

doc "Creates and returns an array containing the specified elements.
     If called without any arguments, returns an empty array."
shared Array<Element> array<Element>(Element... elements) { throw; }

doc "Returns an empty array."
shared Array<Element>&None<Element> arrayOfNone<Element>() { throw; }

doc "Returns an array with the elements in the sequence.
     This array can never be empty."
shared Array<Element>&Some<Element> arrayOfSome<Element>(Sequence<Element> elements) { throw; }

doc "Creates and returns an array of the specified size, populating 
     it with the result of calling `element()` for each index."
shared Array<Element> makeArray<Element>(Integer size, Element element(Integer)) { throw; }

shared void copyArray<Element>(Array<Element> source, Array<Element> target, 
        Integer from, Integer to, Integer length) { throw; }
        //TODO: defaults!
        //Integer from=0, Integer to=0, Integer length=smallest(source.size,target.size)) { throw; }
        