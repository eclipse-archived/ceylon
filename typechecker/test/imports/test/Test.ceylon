import imports.package { 
    method, attribute,
    Class, Interface, Dupe, Alias
}

class Test() {
    method();
    Class c = Class("gavin");
    Interface i = c;
    String name = c.name;
    Dupe d = Dupe();
    d.hello();
    String hello = attribute;
    @error d.goodbye();
    Alias strings = { "hello", "world" };
    for (String s in strings) {}
}