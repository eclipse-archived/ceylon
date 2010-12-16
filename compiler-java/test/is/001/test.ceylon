public void test(Process p) {
    class Foo() {
    }

    class Bar(Integer val) extends Foo() { 
        Integer value() { return val; }
    }

    Foo thing = Bar(99);

    if (is Bar thing) {
        p.writeLine(thing.value());
    }

    Foo thang = Foo();

    if (is Bar thang) {
        p.writeLine(thang.value());
    }

    p.writeLine("" thing is Bar " " thang is Bar "");
}
