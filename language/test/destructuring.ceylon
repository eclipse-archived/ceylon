void tupleVar([Integer, Float, String] tuple) {
    value [i1, f1, s1] = tuple;
    assert(i1 == 0);
    assert(f1 == 1.0);
    assert(s1 == "foo");
    value [i2, Float f2, s2] = tuple;
    assert(i2 == 0);
    assert(f2 == 1.0);
    assert(s2 == "foo");
    value [Integer i3, Float f3, String s3] = tuple;
    assert(i3 == 0);
    assert(f3 == 1.0);
    assert(s3 == "foo");
}

void tupleLiteral() {
    value [i1, f1, s1] = [0, 1.0, "foo"];
    assert(i1 == 0);
    assert(f1 == 1.0);
    assert(s1 == "foo");
    value [i2, Float f2, s2] = [0, 1.0, "foo"];
    assert(i2 == 0);
    assert(f2 == 1.0);
    assert(s2 == "foo");
    value [Integer i3, Float f3, String s3] = [0, 1.0, "foo"];
    assert(i3 == 0);
    assert(f3 == 1.0);
    assert(s3 == "foo");
}

void tupleGeneric() {
    class Foo<T>() {}
    class FooSub<T>() extends Foo<T>() {}
    value [f1] = [FooSub<Integer>()];
    value [FooSub<Integer> f2] = [FooSub<Integer>()];
    value [Foo<Integer> f3] = [FooSub<Integer>()];
}

void entryVar(Integer->String entry) {
    value i1->s1 = entry;
    assert(i1 == 0);
    assert(s1 == "foo");
    value Integer i2->s2 = entry;
    assert(i2 == 0);
    assert(s2 == "foo");
    value Integer i3->String s3 = entry;
    assert(i3 == 0);
    assert(s3 == "foo");
}

void entryLiteral() {
    value i1->s1 = 0->"foo";
    assert(i1 == 0);
    assert(s1 == "foo");
    value Integer i2->s2 = 0->"foo";
    assert(i2 == 0);
    assert(s2 == "foo");
    value Integer i3->String s3 = 0->"foo";
    assert(i3 == 0);
    assert(s3 == "foo");
}

void entryGeneric() {
    class Foo<T>() {}
    class FooSub<T>() extends Foo<T>() {}
    value i1->f1 = 0->FooSub<Integer>();
    value Integer i2->FooSub<Integer> f2 = 0->FooSub<Integer>();
    value Integer i3->Foo<Integer> f3 = 0->FooSub<Integer>();
}

@test
shared void testDestructuring() {
    tupleVar([0, 1.0, "foo"]);
    tupleLiteral();
    tupleGeneric();
    entryVar(0->"foo");
    entryLiteral();
    entryGeneric();
}
