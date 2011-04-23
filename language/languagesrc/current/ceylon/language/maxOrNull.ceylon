shared X? maxOrNull<X>(X... xs) 
        given X satisfies Comparable<X> {
    if (exists X first = xs.first) {
        variable X max := first;
        for (X y in xs.rest) { 
            if (y>max) { 
                max:=y;
            }
        }
        return max;
    }
    else {
        return null;
    }
}