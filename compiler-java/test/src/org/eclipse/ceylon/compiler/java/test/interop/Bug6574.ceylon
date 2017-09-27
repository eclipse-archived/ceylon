import java.util {
    Arrays { asList }
}

shared void bug6574() {
    value al = asList<String>;
    assert(al("a") == Arrays.asList<String>("a"));
    assert(al("a", "b") == Arrays.asList<String>("a", "b"));
    assert(al("a", "b", "c") == Arrays.asList<String>("a", "b", "c"));
    assert(al("a", "b", "c", "d") == Arrays.asList<String>("a", "b", "c", "d"));
    assert(al("a", "b", "c", "d", "e") == Arrays.asList<String>("a", "b", "c", "d", "e"));
    String[] x = ["1", "2", "3"];
    assert(al(*x) == Arrays.asList<String>(*x));
    assert(al("a", *x) == Arrays.asList<String>("a", *x));
    assert(al("a", "b", *x) == Arrays.asList<String>("a", "b", *x));
    assert(al("a", "b", "c", *x) == Arrays.asList<String>("a", "b", "c", *x));
    assert(al("a", "b", "c", "d", *x) == Arrays.asList<String>("a", "b", "c", "d", *x));
    assert(al("a", "b", "c", "d", "e", *x) == Arrays.asList<String>("a", "b", "c", "d", "e", *x));
}