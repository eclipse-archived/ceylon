//Array tests
void testArrays() {
    assert(!nonempty arrayOfNone(), "arrayOfNone");
    value a1 = arrayOfSome({1});
    //assert(nonempty a1, "nonempty array");
    assert(a1.size==1, "array.size");
    assert(a1[0] exists, "array[0]");
    assert(!a1.empty, "array.empty");
    assert(a1.hash==1, "array.hash");
    a1.setItem(0,10);
    if (exists i=a1[0]) {
        assert(i==10, "array.setItem");
    } else { fail("array.setItem"); }
    a1.setItem(0,null);
    if (exists i=a1[0]) {
        fail("array.setItem (null)");
    }
}
