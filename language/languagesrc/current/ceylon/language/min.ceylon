shared X min<X>(X x, X... xs) 
        given X satisfies Comparable<X> {
    variable X min := x;
    for (X y in xs) { 
        if (y<min) { 
            min:=y;
        }
    }
    return min;
}