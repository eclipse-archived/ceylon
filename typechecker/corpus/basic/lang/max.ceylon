shared X max<X>(X x, X... xs) 
        given X satisfies Comparable<X> {
    variable X max := x;
    for (X y in xs) { 
        if (y>max) { 
            max:=y
        }
    }
    return max
}