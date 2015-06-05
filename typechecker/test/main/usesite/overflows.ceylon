abstract class FooBar<Self>() 
        of Self 
        given Self satisfies FooBar<Self> {}

class BarBaz() extends FooBar<BarBaz>() {}

void foo(FooBar<in Nothing> foo) {
    switch (foo) 
    case (is FooBar<in Nothing>) {
        print(foo);
    }
}

shared void run() => foo(BarBaz());




abstract class Base<out Self>() of Self
        given Self satisfies Base<Self> {
    shared variable Integer a = 0;
}

class Sub1() extends Base<Sub1>() {
    shared variable Integer b = 0;
}

class Sub2() extends Base<Sub2>() {
    shared variable Integer b = 0;
}

Base<in Nothing> deserialize(String s) {
    switch (s)
    case ("sub1") {
        Sub1 r = Sub1();
        return r;
    }
    case ("sub2") {
        Sub2 r = Sub2();
        return r;
    }
    else {
        throw Exception("invalid input data");
    }
}

void process(Base<in Nothing> input) {
    switch (input)
    case (is Sub1) {
        // process Sub1
    }
    case (is Sub2) {
        // process Sub2
    }
    else {
        throw Exception("unsupported input class");
    }
}

shared void runit() {
    String sx = "sub2";
    Base<in Nothing> inputX = deserialize(sx);
    
    // some generic processing on Base
    print(inputX.a.string);
    
    process(inputX);
}