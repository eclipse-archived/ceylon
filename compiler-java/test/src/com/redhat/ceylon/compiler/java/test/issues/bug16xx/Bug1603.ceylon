class Bug1603<Element>(shared Element first, Integer length) 
        given Element satisfies Ordinal<Element> & 
        Comparable<Element> & Summable<Element> {

    shared Boolean m1(Object element) {
        if (is Integer element, 
            is Integer first) {
            return element <= first;
        }
        return false;
    }
    
    shared Boolean m2(Object element) {
        if (is Integer element, 
            is Integer first) {
            return first+length < element <= first;
        }
        return false;
    }
}