shared List<X> generate(Natural length, X first, X next(X previous)) {
    OpenList<X> list = ArrayList<X>();
    list.append(first);
    variable X x := first;
    for (Natural i in 0..length-1) {
        x := element(x);
        list.append(x);
    }
    return list;
}