import imports.pack { a=attribute, ... }

class TestWildcard() {
    method();
    Class c = Class("gavin");
    Interface i = c;
    String name = c.name;
    Dupe d = Dupe();
    d.hello();
    String hello = a;
    @error d.goodbye();
    String hi = singleton.hello;
}