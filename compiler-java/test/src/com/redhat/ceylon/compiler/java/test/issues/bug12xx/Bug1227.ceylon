@noanno
Element? bug1227<Element>(List<Element> x) {
    Iterable<Integer> l = nothing;
    Boolean someBoolean = nothing;
    Integer from;
    for (index in l) {
        if (someBoolean) {
            from = index;
            break;
        }
    }
    else {
        return null;
    }
    return x[from];
}