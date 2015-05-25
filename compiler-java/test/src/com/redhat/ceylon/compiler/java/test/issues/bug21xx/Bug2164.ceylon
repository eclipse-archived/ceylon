@noanno
shared class Bug2164 {
    shared new() {}
    shared new A() {}
    
    value local = {for (x in [1, "str"]) if (is Integer x) x};
}