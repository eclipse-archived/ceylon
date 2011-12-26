shared class Class(String name) 
        satisfies Interface {
    shared String name = name;
    shared variable Integer count := 0;
    shared class Inner() {}
    shared String method(String s, Integer i) {
        return s + i.string;
    }
}