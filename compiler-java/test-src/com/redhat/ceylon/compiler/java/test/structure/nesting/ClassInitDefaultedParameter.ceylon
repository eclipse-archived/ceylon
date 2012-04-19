
@nomodel
class C1(Integer i = 1) { }

@nomodel
class C3(Integer i = 1, Integer n = 2*i) { }

@nomodel
class C4(Integer i, Integer n = 2*i) { }

@nomodel
class C5<U>(Integer i, U? u = null) { }

@nomodel
void positional() {
    C1();
    C1(1);
    C3();
    C3(1);
    C3(1, 2);
    C4(1);
    C4(1, 2);
    C5<String>(1);
    C5<String>(1, "");
}

@nomodel
void named() {
    C1{};
    C1{i=1;};
    C3{};
    C3{i=10;};
    C3{i=10; n=20;};
    C3{n=20; i=10;};
    C3{n=10;};
    C4{n=20; i=10;};
    C4{i=10; n=20;};
    C5<String>{i=10;};
    C5<String>{i=10; u="";};
    C5<String>{u=""; i=10; };
}
