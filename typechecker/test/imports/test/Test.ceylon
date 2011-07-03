import imports.package { method, attribute, Class, Interface, Dupe }

class Test() {
    method();
    Class c = Class("gavin");
    Interface i = c;
    String name = c.name;
    Dupe d = Dupe();
    d.hello();
    String hello = attribute;
    @error d.goodbye();
}