//Array tests
void testArrays() {
    check(array().size==0, "array size 0");
    value a1 = array(1);
    check(a1.size==1, "array.size");
    check(a1[0] exists, "array[0]");
    check(!a1.empty, "array.empty");
    check(a1.hash==32, "array.hash is " a1.hash " instead of 32");
    a1.setItem(0,10);
    if (exists i=a1[0]) {
        check(i==10, "array.setItem");
    } else { fail("array.setItem"); }
    value a2=array(1,2,3);
    value a3=array([1,2,3]*);
    check(a2==a3, "array.equals");
    check(a2.size==a3.size, "array.size");
    a2.setItem(0,10);
    if (exists i=a2[0]) {
        check(i==10, "array.setItem 2");
    } else { fail("array.setItem 2"); }
    value a4 = arrayOfSize(5, ".");
    check(a4.size == 5, "makeArray 1");
    if (exists i=a4[4]) {
        check(i==".", "makeArray 2");
    } else { fail("makeArray 2"); }
    value a5 = arrayOfSize(3, 0);
    check(a5.size == 3, "makeArray 3");
    if (exists i=a5[2]) {
        check(i==0, "makeArray 4");
    } else { fail("makeArray 4"); }
    value a6 = array<Integer?>(1);
    a6.setItem(0,null);
    if (exists i=a6[0]) {
        fail("array.setItem (null)");
    }
    value a7=array<Integer?>(1,2,3);
    a7.setItem(0,null);
    if (exists i=a7[0]) {
        fail("array.setItem (null) 2");
    }
    check(array(1,2,3).reversed==array(3,2,1), "Array.reversed");
}
