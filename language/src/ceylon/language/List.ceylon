doc "Represents a collection in which every element has a unique index."
shared interface List<out Element>
        satisfies Collection<Element> &
                  Correspondence<Integer, Element> &
                  Ranged<Integer,List<Element>> &
                  Cloneable<List<Element>> {

    doc "The index of the last element of the list, or
         null if the list is empty."
    see (size)
    shared formal Integer? lastIndex;

    doc "The number of elements in this sequence, always
         `sequence.lastIndex+1`."
    see (lastIndex)
    shared actual default Integer size {
        return (lastIndex?-1) + 1;
    }

    doc "Determines if the given index refers to an element
         of this sequence, that is, if
         `index<=sequence.lastIndex`."
    shared actual default Boolean defines(Integer index) {
        return index <= lastIndex?-1;
    }

    doc "Returns the element of this sequence with the given
         index, or `null` if the given index is past the end
         of the sequence, that is, if
         `index>sequence.lastIndex`. The first element of
         the sequence has index `0`."
    shared actual formal Element? item(Integer index);

    shared actual default Iterator<Element> iterator {
        object listIterator
                satisfies Iterator<Element> {
            variable Integer index := 0;
            shared actual Element|Finished next() {
                if (index < lastIndex?-1) {
                    if (is Element elem = item(index++)) {
                        return elem;
                    }
                    else {
                        throw;
                    }
                }
                else {
                    return exhausted;
                }
            }
            shared actual String string {
                return "listIterator";
            }
        }
        return listIterator;
    }

    /*doc "Reverse this sequence, returning a new nonempty
         sequence."
    shared formal Sequence<Element> reversed;*/

    doc "Select the elements between the given indexes.
         If the start index is the same as the end index,
         return a sequence with a single element. If the
         start index larger than the end index, return the
         elements in the reverse order from the order in
         which they appear in this sequence. If both the
         start index and the end index are larger than the
         last index in the sequence, return an `Empty`
         sequence. Otherwise, if the last index is larger
         than the last index in the sequence, return all
         elements from the start index to last element of
         the sequence."
    shared actual formal Element[] span(Integer from,
                                        Integer? to);

    doc "Returns a sequence containing the elements
         beginning from the given index, with the given
         length."
    shared actual formal Element[] segment(Integer from,
                                           Integer length);

    doc "Two Lists are considered equal if they have the same
         size and for every index both have equal elements."
    shared actual default Boolean equals(Object that) {
        if (is List<Void> that) {
            if (that.size==size) {
                for (i in 0..size-1) {
                    value x = this[i];
                    value y = that[i];
                    if (exists x) {
                        if (exists y) {
                            if (x!=y) {
                                return false;
                            }
                        }
                        else {
                            return false;
                        }
                    }
                    else {
                        return !exists y;
                    }
                }
                else {
                    return true;
                }
            }
        }
        return false;
    }

    shared actual default Integer hash {
        variable Integer hashCode := 1;
        for(Element elem in this){
            hashCode *= 31;
            if(is Object elem){
                hashCode += elem.hash;
            }
        }
        return hashCode;
    }

    shared default actual String string {
        return "{ " elementsString " }";
    }

    String elementsString {
        variable String result := "";
        variable Boolean first := true;
        for (Element elem in this) {
            if (!first) {
                result := result + ", ";
            }
            if (is Object elem) {
                result := result + elem.string;
            } else {
                result := result + "null";
            }
            first := false;
        }
        return result;
        // This would have been nice: return ", ".join(this...);
    }

}