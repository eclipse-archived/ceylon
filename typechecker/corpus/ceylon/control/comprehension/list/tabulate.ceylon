shared List<X> tabulate(Natural length, X containing(Natural index)) {
    OpenList<X> list = ArrayList<X>();
    for (Natural i in 0..length-1) {
        list.append(containing(i));
    }
    return list;
}