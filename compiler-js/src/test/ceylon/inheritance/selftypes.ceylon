import check { check }

interface A141 satisfies Comparable<C141|A141> {}
interface C141 satisfies Comparable<C141|A141> {}

interface Comp141<in T> of T
        given T satisfies Comp141<T> {
    shared formal Comparison compare(T other);
}

interface Scal141<in T> of T
        satisfies Comp141<T> 
        given T satisfies Scal141<T> {}

class Bar141() satisfies Comp141<Bar141> {
    Integer birth = process.milliseconds;
    shared actual Comparison compare(Bar141 other) {
        return birth <=> other.birth;
    }
}

void testSelfTypes() {
    Comp141<Bar141> testOf = Bar141();
    Bar141 bar141 = testOf of Bar141;
    Anything v141 = testOf;
    Object? o141 = v141 of Object|Null;
    check(o141 is Bar141, "self types");
}
