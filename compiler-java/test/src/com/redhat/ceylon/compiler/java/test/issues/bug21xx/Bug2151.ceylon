@noanno
shared class Baz(){}

@noanno
class Foo(){
    shared class Bar() => Baz();
}

@noanno
class Foo2() extends Foo(){
    class Bar() =>Baz();
}