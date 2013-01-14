doc "Applies a function to each element of two `Iterable`s
     and returns an `Iterable` with the results."
by "Gavin" "Enrique Zamudio" "Tako"
shared {Result*} combine<Result,Element,OtherElement>(
        Result combination(Element element, OtherElement otherElement), 
        {Element*} elements, 
        {OtherElement*} otherElements)  {
    
    class CombineIterable() satisfies {Result*} {
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
