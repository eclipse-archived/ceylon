shared Element smallest<Element>(Sequence<Element> sequence) 
        given Element satisfies Comparable<Element> {
    variable Element min := sequence.first;
    for (Element element in sequence) { 
        if (element<min) { 
            min:=element;
        }
    }
    return min;
}