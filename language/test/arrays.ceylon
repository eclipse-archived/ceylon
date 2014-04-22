//Array tests
@test
shared void testArrays() {
    check(Array{}.size==0, "array size 0");
    value a1 = Array{1};
    check(a1.size==1, "array.size");
    check(a1[0] exists, "array[0]");
    check(!a1.empty, "array.empty");
    check(a1.hash==32, "array.hash is ``a1.hash`` instead of 32");
    a1.set(0,10);
    if (exists i=a1[0]) {
        check(i==10, "array.set");
    } else { fail("array.set"); }
    value a2=Array{1,2,3};
    value a3=Array([1,2,3]);
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
    value a6 = Array<Integer?>{1};
    a6.set(0,null);
    if (exists i=a6[0]) {
        fail("array.set (null)");
    }
    value a7=Array<Integer?>{1,2,3};
    a7.set(0,null);
    if (exists i=a7[0]) {
        fail("array.set (null) 2");
    }
    check(Array{1,2,3}.reversed==Array{3,2,1}, "Array.reversed");
    
    for (ii in 0:3) {
        a3.set(ii, 5);
    }
    
    a2.copyTo(a3);
    check(a3==Array{ 10, 2, 3 }, "copyTo()");
    
    for (ii in 0:3) {
        a3.set(ii, 5);
    }
    
    a2.copyTo(a3, 1, 0, 1);
    check(a3==Array{ 2, 5, 5 }, "copyTo(1, 0, 1)");
    
    for (ii in 0:3) {
        a3.set(ii, 5);
    }
    
    a2.copyTo(a3, 1, 1, 1);
    check(a3==Array{ 5, 2, 5 }, "copyTo(1, 1, 1)");

    //copy to itself
    value a8=Array{1,2,3,null,null};
    a8.copyTo(a8,1,2,2);
    check(a8==Array{1,2,2,3,null}, "copyTo self 1 expected 1,2,2,3,null got ``a8``");
    value a9=Array{"a","b","c",null,null};
    a9.copyTo(a9,1,2,2);
    check(a9==Array{"a","b","b","c",null}, "copyTo self 2 expected a,b,b,c,null got ``a9``");
    a8.set(4,4);
    a8.copyTo(a8,2,1);
    check(a8==Array{1,2,3,4,4}, "copyTo self 3 expected 1,2,3,4,4 got ``a8``");
    a9.set(4,"d");
    a9.copyTo(a9,2,1);
    check(a9==Array{"a","b","c","d","d"}, "copyTo self 4 expected a,b,c,d,d got ``a9``");
    //native array for JVM
    value a10=Array{1,2,3,4,5};
    a10.copyTo(a10,1,2,3);
    check(a10==Array{1,2,2,3,4}, "copyTo self 5 expected 1,2,2,3,4 got ``a10``");
    value a11=Array{1,2,3,4,5};
    a11.copyTo(a11,2,1);
    check(a11==Array{1,3,4,5,5}, "copyTo self 6 expected 1,3,4,5,5 got ``a11``");
}
