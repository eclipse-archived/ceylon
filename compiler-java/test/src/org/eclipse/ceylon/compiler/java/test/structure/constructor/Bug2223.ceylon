@noanno
shared class Bug2223() {
    class Foo{
        shared new foo{}
    }
    class Bar{
        shared new foo{}
    }
    Integer foo = 1;
}
@noanno
shared void bug2223(){
    class Foo{
        shared new foo{}
    }
    class Bar{
        shared new foo{}
    }
    Integer foo = 1;
}