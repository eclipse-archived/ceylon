shared X largest<X>(Sequence<X> xs) 
        given X satisfies Comparable<X> {
    variable X max := xs.first;
    for (X y in xs) { 
        if (y>max) { 
            max:=y;
        }
    }
    return max;
}