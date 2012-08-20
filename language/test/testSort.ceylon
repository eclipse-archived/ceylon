shared void testSort() {
    assert(!nonempty sort(), "sort()");
    assert(!nonempty sort({}...), "sort({}...)");
    assert(sort(3, 2, 1) == {1, 2, 3}, "Sort integers");
    assert(sort(3.3, 2.2, 1.1) == {1.1, 2.2, 3.3}, "Sort floats");
    assert(sort(`c`, `b`, `a`) == {`a`, `b`, `c`}, "Sort characters");
    assert(sort("c", "b", "a") == {"a", "b", "c"}, "Sort strings");
    assert(sort(StubComparable(3), StubComparable(2), StubComparable(1)) == {StubComparable(1), StubComparable(2), StubComparable(3)}, "Sort custom comparable");
}

class StubComparable(Integer n) satisfies Comparable<StubComparable> {
    shared actual Comparison compare(StubComparable other) {
        return n<=>other.n;
    }
    shared actual Boolean equals(Object other) {
        if (is StubComparable other) {
            return n==other.n;
        }
        else {
            return false;
        }
    }
}