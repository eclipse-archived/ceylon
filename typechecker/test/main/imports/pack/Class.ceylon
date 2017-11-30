shared class Class(name) 
        satisfies Interface {
    shared String name;
    shared variable Integer count = 0;
    shared class Inner() {}
    shared String method(String s, Integer i) {
        return s + i.string;
    }
}