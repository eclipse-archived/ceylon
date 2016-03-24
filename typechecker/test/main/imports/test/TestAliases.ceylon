import imports.pack { 
    m=method, 
    a=attribute,
    sing = singleton /*{ hi=hello }*/,
    C=Class { i=count, 
              In=Inner, 
              m=method,
              @error i2=count },
    C2=ClassWithCtor{ o1=other },
    I=Interface, 
    D=Dupe,
    o=op,
    @error o2=op,
    @error m=method
}

class TestAliases() {
    m();
    C c = C("gavin");
    I i = c;
    String name = c.name;
    c.i++;
    C.In cin = c.In();
    c.m("hello",0);
    D d = D();
    d.hello();
    String hello = a;
    Dupe dupe = Dupe();
    dupe.goodbye();
    o(1);
    String hi = sing.hello;
    C2 c2 = C2();
    C2 c2o1 = C2.o1();
}