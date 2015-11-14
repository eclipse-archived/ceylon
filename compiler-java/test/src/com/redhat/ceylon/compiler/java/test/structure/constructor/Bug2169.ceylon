shared void bug2169(){
    class Foo{ 
        checker.note("a");
        shared new (){
            checker.note("b");
        } 
        checker.note("c");
        shared new baz() extends Foo(){
            checker.note("d");
        } 
        checker.note("e");
    }
    Foo.baz();
    checker.check("[a, b, c, d, e]");
}