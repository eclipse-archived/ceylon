shared Element largest<Element>(Sequence<Element> sequence) 
        given Element satisfies Comparable<Element> {
    variable Element max := sequence.first;
    for (Element element in sequence) { 
        if (element>max) { 
            max:=element;
        }
    }
    return max;
}