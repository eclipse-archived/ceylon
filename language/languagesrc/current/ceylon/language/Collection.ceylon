shared interface Collection<out Element>
        satisfies Category & Sized & Iterable<Element>     
        given Element satisfies Equality {

    doc "Returns the number of occurrences of the given
         value in this collection."
    shared default Natural count(Equality element) {
        variable Natural count:=0;
        for (elem in this) {
            if (elem.equals(element)) {
                count++;
            }
        }
        return count;
    }
    
    shared formal Set<Element> elements;
    
}