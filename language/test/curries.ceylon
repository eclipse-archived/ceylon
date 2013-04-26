Integer potencia(Integer base, Integer expo) => base^expo;
Integer veces(Integer x, Integer y) => x*y;

void testCurries() {
    value p2 = curry(potencia)(2);
    check(p2(0)==1, "p2 [1]");
    check(p2(8)==256, "p2 [2]");
    value triple = compose(curry(veces)(3), p2);
    check(triple(8)==768, "p2 [3]");
    //TODO: flatten, ,unflatten, shuffle
    
    check(unflatten(function () => 1)([]) == 1, "unflatten 0");
    check(unflatten(function (Integer a = 2) => a)([]) == 2, "unflatten 0.1");
    check(unflatten(function (Integer* a) => a[0] else 0)([]) == 0, "unflatten 0.2");
    
    check(unflatten(function (Integer a) => a)([1]) == 1, "unflatten 1");
    check(unflatten(function (Integer a = 2) => a)([1]) == 1, "unflatten 1.1");
    check(unflatten(function (Integer* a) => a[0] else 0)([1]) == 1, "unflatten 1.2");
    check(unflatten(function (Integer a, Integer b=0) => a)([1]) == 1, "unflatten 1.3");
    check(unflatten(function (Integer a, Integer b=0, Integer* c) => a)([1]) == 1, "unflatten 1.4");
    check(unflatten(function (Integer a, Integer b=0, Integer c=0, Integer d=0, Integer e=0) => a)([1]) == 1, "unflatten 1.5");
    check(unflatten(function (Integer a, Integer b=0, Integer c=0, Integer d=0, Integer* e) => a)([1]) == 1, "unflatten 1.6");

    check(unflatten(function (Integer a, Integer b) => b)([1,2]) == 2, "unflatten 2");
    check(unflatten(function (Integer a = 2, Integer b = 3) => b)([1,2]) == 2, "unflatten 2.1");
    check(unflatten(function (Integer* a) => a[1] else 0)([1,2]) == 2, "unflatten 2.2");
    check(unflatten(function (Integer a, Integer* b) => b[0] else 0)([1,2]) == 2, "unflatten 2.3");
    check(unflatten(function (Integer a, Integer b, Integer* c) => b)([1,2]) == 2, "unflatten 2.4");
    check(unflatten(function (Integer a, Integer b, Integer c=0, Integer d=0, Integer e=0) => b)([1,2]) == 2, "unflatten 2.5");
    check(unflatten(function (Integer a, Integer b, Integer c=0, Integer d=0, Integer* e) => b)([1,2]) == 2, "unflatten 2.6");

    check(unflatten(function (Integer a, Integer b, Integer c) => c)([1,2,3]) == 3, "unflatten 3");
    check(unflatten(function (Integer a = 2, Integer b = 3, Integer c=4) => c)([1,2,3]) == 3, "unflatten 3.1");
    check(unflatten(function (Integer* a) => a[2] else 0)([1,2,3]) == 3, "unflatten 3.2");
    check(unflatten(function (Integer a, Integer* b) => b[1] else 0)([1,2,3]) == 3, "unflatten 3.3");
    check(unflatten(function (Integer a, Integer b, Integer* c) => c[0] else 0)([1,2,3]) == 3, "unflatten 3.4");
    check(unflatten(function (Integer a, Integer b, Integer c, Integer* d) => c)([1,2,3]) == 3, "unflatten 3.5");
    check(unflatten(function (Integer a, Integer b, Integer c, Integer d=0, Integer e=0) => c)([1,2,3]) == 3, "unflatten 3.6");
    check(unflatten(function (Integer a, Integer b, Integer c, Integer d=0, Integer* e) => c)([1,2,3]) == 3, "unflatten 3.7");

    check(unflatten(function (Integer a, Integer b, Integer c, Integer d) => d)([1,2,3,4]) == 4, "unflatten 4");
    check(unflatten(function (Integer a = 2, Integer b = 3, Integer c = 4, Integer d = 5) => d)([1,2,3,4]) == 4, "unflatten 4.1");
    check(unflatten(function (Integer* a) => a[3] else 0)([1,2,3,4]) == 4, "unflatten 4.2");
    check(unflatten(function (Integer a, Integer* b) => b[2] else 0)([1,2,3,4]) == 4, "unflatten 4.3");
    check(unflatten(function (Integer a, Integer b, Integer* c) => c[1] else 0)([1,2,3,4]) == 4, "unflatten 4.4");
    check(unflatten(function (Integer a, Integer b, Integer c, Integer* d) => d[0] else 0)([1,2,3,4]) == 4, "unflatten 4.5");
    check(unflatten(function (Integer a, Integer b, Integer c, Integer d, Integer e=0) => d)([1,2,3,4]) == 4, "unflatten 4.6");
    check(unflatten(function (Integer a, Integer b, Integer c, Integer d, Integer* e) => d)([1,2,3,4]) == 4, "unflatten 4.7");

    check(unflatten(function (Integer a, Integer b, Integer c, Integer d, Integer e) => e)([1,2,3,4,5]) == 5, "unflatten 5");
    check(unflatten(function (Integer a = 2, Integer b = 3, Integer c = 4, Integer d = 5, Integer e = 6) => e)([1,2,3,4,5]) == 5, "unflatten 5.1");
    check(unflatten(function (Integer* a) => a[4] else 0)([1,2,3,4,5]) == 5, "unflatten 5.2");
    check(unflatten(function (Integer a, Integer* b) => b[3] else 0)([1,2,3,4,5]) == 5, "unflatten 5.3");
    check(unflatten(function (Integer a, Integer b, Integer* c) => c[2] else 0)([1,2,3,4,5]) == 5, "unflatten 5.4");
    check(unflatten(function (Integer a, Integer b, Integer c, Integer* d) => d[1] else 0)([1,2,3,4,5]) == 5, "unflatten 5.5");
    check(unflatten(function (Integer a, Integer b, Integer c, Integer d, Integer* e) => e[0] else 0)([1,2,3,4,5]) == 5, "unflatten 5.6");
    check(unflatten(function (Integer a, Integer b, Integer c, Integer d, Integer e, Integer f=0) => e)([1,2,3,4,5]) == 5, "unflatten 5.7");
    check(unflatten(function (Integer a, Integer b, Integer c, Integer d, Integer e, Integer* f) => e)([1,2,3,4,5]) == 5, "unflatten 5.8");
}
