
@nomodel
interface I<T> {
    shared formal void m(Integer i = 1);

    shared formal Integer m2(Integer i = 1);

    shared formal Integer m3(Integer i = 1, Integer n = 2*i);

    shared formal Integer m4(Integer i, Integer n = 2*i);

    shared formal Integer m5<U>(Integer i, T? t = null, U? u = null);
}

@nomodel
void positional<T>(I<T> i, I<String> i2) {
    i.m();
    i2.m(2);
    i2.m2();
    i.m2(2);
    i.m3();
    i.m3(5);
    i.m3(4, 5);
    i.m4(5);
    i.m4(4, 5);
    i.m5<String>(5);
    i.m5<String>(5, null);
    i.m5<String>(5, null, "");
}

@nomodel
void named<T>(I<T> i, I<String> i2) {
    i.m{};
    i2.m{i=2;};
    i2.m2{};
    i.m2(2);
    i.m3{};
    i.m3{i=5;};
    i.m3{i=4; n=5;};
    i.m3{n=5; i=4;};
    i.m3{n=5;};
    i.m4(5);
    i.m4(4, 5);
    i.m5<String>(5);
    i.m5<String>(5, null);
    i.m5<String>(5, null, "");
}
 
@nomodel
class C<T>() satisfies I<T> {
    shared actual void m(Integer i) {}

    shared actual Integer m2(Integer i) { return i;}

    shared actual Integer m3(Integer i, Integer n) { return i;}

    shared actual Integer m4(Integer i, Integer n) { return i;}

    shared actual Integer m5<U>(Integer i, T? t, U? u) { return i;}
}