"An iterable object that produces the unique elements of a 
 given iterable object. This iterable object never produces 
 two elements that are equal, as determined by
 [[Object.equals]]."
deprecated
class UniqueElements<Element>(elements) 
        satisfies {Element*} 
        given Element satisfies Object {
    
    "An iterable object that may contain duplicate elements."
    {Element*} elements;
    
    shared actual Iterator<Element> iterator() {
        object iterator satisfies Iterator<Element> {
            variable Integer index = 0;
            value iter = elements.iterator();
            shared actual Element|Finished next() {
                variable value next = iter.next();
                while (!is Finished element=next,
                       element in elements.taking(index)) {
                    next = iter.next();
                    index++;
                }
                index++;
                return next;
            }
        }
        return iterator;
    }
    
}
