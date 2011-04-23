shared X? minOrNull<X>(X... xs) 
        given X satisfies Comparable<X> {
    if (exists X first = xs.first) {
        variable X min := first;
        for (X y in xs) { 
            if (y<min) { 
                min:=y;
            }
        }
        return Something<X>(min);
    }
    else {
        return null;
    }
}