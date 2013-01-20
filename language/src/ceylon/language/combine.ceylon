doc "Applies a function to each element of two `Iterable`s
     and returns an `Iterable` with the results."
by "Gavin" "Enrique Zamudio" "Tako"
shared Iterable<Result,Absent> combine<Result,Absent,Element,OtherElement>(
        Result combination(Element element, OtherElement otherElement), 
        Iterable<Element,Absent> elements, 
        Iterable<OtherElement,Absent> otherElements) 
        given Absent satisfies Null {
    
    class CombineIterable() satisfies Iterable<Result,Absent> {
        shared actual Iterator<Result> iterator {
            class CombineIterator() satisfies Iterator<Result> {
                value iter = elements.iterator;
                value otherIter = otherElements.iterator;
                shared actual Result|Finished next() {
                    value elem = iter.next();
                    value otherElem = otherIter.next();
                    if (!is Finished elem, !is Finished otherElem) {
                        return combination(elem, otherElem);
                    } else {
                        return finished;
                    }
                }
            }
            return CombineIterator();
        }
        
    }
    return CombineIterable();
}
