"Return a Sequential instance containing the same elements as 
 the given Iterable, iterating it only once."
Element[] sequential<Element>({Element*} elements) {
    if (is Element[] elements) {
        return elements;
    }
    value iterator = elements.iterator();
    variable value elem = iterator.next();
    if (is Finished x=elem) {
        return empty;
    }
    assert(is Element x=elem);
    variable Array<Element> array = arrayOfSize<Element>(5, x);
    variable Integer index = 0;
    while (!is Finished y=elem) {
        if (index >= array.size) {
            value newarray = arrayOfSize<Element>(array.size + array.size.rightLogicalShift(1), x);
            array.copyTo(newarray);
            array = newarray;
        }
        array.set(index, y);
        index++;
        elem = iterator.next();
    }
    return array.take(index).sequence;
}