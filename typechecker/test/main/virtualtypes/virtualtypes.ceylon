interface Foo {
    shared formal alias Type;
    shared formal Type val;
    shared [Type] getIt() => [val];
    shared void takeIt(Type it) {}
}

class Bar() satisfies Foo {
    shared actual alias Type => String;
    shared actual String val = "hello";
    shared void useIt() {
        print("got: " + this.getIt().first);
        print("got: " + getIt().first);
        takeIt(val);
    }
}

void method() {
    Bar.Type s1 = Bar().val;
    [Bar.Type] s2 = Bar().getIt();
    String s3 = Bar().val;
    [String] s4 = Bar().getIt();
    Bar().takeIt("bye");
    Foo foo = Bar();
    @error [Foo.Type] xxx = foo.getIt();
}
