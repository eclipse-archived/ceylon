shared X smallest<X>(Sequence<X> xs) 
        given X satisfies Comparable<X> {
    variable X min := xs.first;
    for (X y in xs) { 
        if (y<min) { 
            min:=y;
        }
    }
    return min;
}