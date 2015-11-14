"An [[Iterator]] that returns the elements of two
 [[Iterable]]s, as if they were chained together."
see (`function Iterable.chain`)
by ("Enrique Zamudio")
tagged("Streams")
class ChainedIterator<out Element,out Other>
		        ({Element*} first, {Other*} second) 
        satisfies Iterator<Element|Other> {
    
    variable Iterator<Element|Other> iter = first.iterator();
    variable value more = true;
    
    shared actual Element|Other|Finished next() {
        value element = iter.next();
        if (more && element is Finished) {
            iter = second.iterator();
            more = false;
            return iter.next();
        }
        else {
            return element;
        }
    }
    
    string => "``first``.chain(``second``).iterator()";
}
