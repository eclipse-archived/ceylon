import imports.package { 
    m=method, 
    a=attribute, 
    C=Class { i=count, In=Inner }, 
    I=Interface, 
    D=Dupe 
}

class TestAliases() {
    m();
    C c = C("gavin");
    I i = c;
    String name = c.name;
    c.i++;
    C.In cin = c.In();
    D d = D();
    d.hello();
    String hello = a;
    Dupe dupe = Dupe();
    dupe.goodbye();
}