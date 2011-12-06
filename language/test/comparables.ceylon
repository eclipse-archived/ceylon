class C(Float f) satisfies Comparable<C> {
    shared actual Comparison compare(C other) {
        return f<=>other.f;
    }
}

void comparables() {
    C[] cs = { C(0.0), C(1.0), C(-2.0) };
    Comparable<C>[] comparables = cs;
    for (c in comparables) {
        assert(c.compare(C(0.5))!=equal, "custom comparables");
    }
    C[] cs2 = { C(1.0), C(2.0), C(0.0) };
    for (z in zip(cs, cs2)) {
        assert(z.key<z.item, "custom comparables");
    }
}