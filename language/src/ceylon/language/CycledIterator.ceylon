class CycledIterator<Element>
                ({Element*} iterable, Integer times) 
        satisfies Iterator<Element> {
    
    variable Iterator<Element> iter = emptyIterator;
    variable Integer count=0;
    
    shared actual Element|Finished next() {
        if (!is Finished next = iter.next()) {
            return next;
        }
        else {
            if (count<times) {
                count++;
                iter = iterable.iterator();
            }
            else {
                iter = emptyIterator;
            }
            return iter.next();
        }
        
    }
    
    string => "``iterable``.repeat(``times``).iterator()";
    
}
