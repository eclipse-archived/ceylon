class C(Float f) satisfies Comparable<C> {
    shared actual Comparison compare(C other) {
        return f<=>other.f;
    }
    shared actual String string = "C(`` f.string ``)";
}

@test
shared void comparables() {
    C[] cs = [ C(0.0), C(1.0), C(-2.0) ];
    Comparable<C>[] comparables = cs;
    for (c in comparables) {
        check(c.compare(C(0.5))!=equal, "custom comparables 1");
    }
    C[] cs2 = [ C(1.0), C(2.0), C(0.0) ];
    for (z in zipEntries(cs, cs2)) {
        check(z.key<z.item, "custom comparables 2: `` z.key `` < `` z.item ``?");
    }
}
