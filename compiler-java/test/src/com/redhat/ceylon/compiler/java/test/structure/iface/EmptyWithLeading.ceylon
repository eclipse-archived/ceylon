void emptyWithLeading() {
    print([].withLeading("A"));
    print({"A"}.sequence());
    assert([].withLeading("A")=={"A"}.sequence());
}