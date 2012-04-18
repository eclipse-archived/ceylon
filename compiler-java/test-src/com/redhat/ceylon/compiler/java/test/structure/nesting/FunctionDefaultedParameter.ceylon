@nomodel
void f1(Integer i = 1) {}

@nomodel
Integer f2(Integer i = 1) { return i; }

@nomodel
Integer f3(Integer i = 1, Integer n = 2*i) { return i; }

@nomodel
Integer f4(Integer i, Integer n = 2*i) { return i; }

@nomodel
Integer f5<U>(Integer i, U? u = null) {
    return i;
}

@nomodel
void positional() {
    f1();
    f1(1);
    f2();
    f2(2);
    f3();
    f3(1);
    f3(1, 2);
    f4(1);
    f4(1, 4);
    f5<String>(1);
    f5<String>(1, "");
}

@nomodel
void named() {
    f1{};
    f1{i=1;};
    f2{};
    f2{i=2;};
    f3{};
    f3{i=1;};
    f3{i=1; n=2;};
    f3{n=2; i=1;};
    f3{n=2;};
    f4{i=1;};
    f4{i=1; n=4;};
    f4{n=4; i=1;};
    f5<String>{i=1;};
    f5<String>{i=1; u="";};
    f5<String>{u=""; i=1;};
}