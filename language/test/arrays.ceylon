//Array tests
void testArrays() {
    check(array{}.size==0, "array size 0");
    value a1 = array{1};
    check(a1.size==1, "array.size");
    check(a1[0] exists, "array[0]");
    check(!a1.empty, "array.empty");
    check(a1.hash==32, "array.hash is ``a1.hash`` instead of 32");
    a1.set(0,10);
    if (exists i=a1[0]) {
        check(i==10, "array.set");
    } else { fail("array.set"); }
    value a2=array{1,2,3};
    value a3=array([1,2,3]);
    check(a2==a3, "array.equals");
    check(a2.size==a3.size, "array.size");
    a2.set(0,10);
    if (exists i=a2[0]) {
        check(i==10, "array.set 2");
    } else { fail("array.set 2"); }
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
    value a6 = array<Integer?>{1};
    a6.set(0,null);
    if (exists i=a6[0]) {
        fail("array.set (null)");
    }
    value a7=array<Integer?>{1,2,3};
    a7.set(0,null);
    if (exists i=a7[0]) {
        fail("array.set (null) 2");
    }
    check(array{1,2,3}.reversed==array{3,2,1}, "Array.reversed");
    
    for (ii in 0:3) {
        a3.set(ii, 5);
    }
    print("a2: ``a2`` a3: ``a3``");
    a2.copyTo(a3);
    check(a3.string=="{ 10, 2, 3 }", "copyTo()");
    
    for (ii in 0:3) {
        a3.set(ii, 5);
    }
    print("a2: ``a2`` a3: ``a3``");
    a2.copyTo(a3, 1, 0, 1);
    check(a3.string=="{ 2, 5, 5 }", "copyTo(1, 0, 1)");
    
    for (ii in 0:3) {
        a3.set(ii, 5);
    }
    print("a2: ``a2`` a3: ``a3``");
    a2.copyTo(a3, 1, 1, 1);
    check(a3.string=="{ 5, 2, 5 }", "copyTo(1, 1, 1)");
    
    
}
