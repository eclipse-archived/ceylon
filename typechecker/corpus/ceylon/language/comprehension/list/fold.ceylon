shared Y fold<X,Y>(Iterable<X> iterable, Y initial, Y using(Y y, X x)) {
    variable Y y:=initial;
    for (X x in iterable) {
        y:=using(y,x);
    }
    return y;
}