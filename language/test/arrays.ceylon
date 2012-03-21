//Array tests
void testArrays() {
    assert(!nonempty array(), "arrayOfNone");
    value a1 = array(1);
    assert(nonempty a1, "nonempty array");
    assert(a1.size==1, "array.size");
    assert(a1[0] exists, "array[0]");
    assert(!a1.empty, "array.empty");
    assert(a1.hash==32, "array.hash is " a1.hash " instead of 32");
    a1.setItem(0,10);
    if (exists i=a1[0]) {
        assert(i==10, "array.setItem");
    } else { fail("array.setItem"); }
    a1.setItem(0,null);
    if (exists i=a1[0]) {
        fail("array.setItem (null)");
    }
    value a2=array(1,2,3);
    value a3=arrayOfSome({1,2,3});
    assert(a2==a3, "array.equals");
    assert(a2.size==a3.size, "array.size");
    assert(nonempty a2, "nonempty array 2");
    a2.setItem(0,10);
    a3.setItem(0,null);
    if (exists i=a2[0]) {
        assert(i==10, "array.setItem 2");
    } else { fail("array.setItem 2"); }
    if (exists i=a3[0]) {
        fail("array.setItem (null) 2");
    }
}
