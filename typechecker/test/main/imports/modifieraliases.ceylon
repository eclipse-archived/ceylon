import ceylon.language { public=shared, var=variable, @error abstract=sum }

public class Foo() {
    public var Integer count=0;
}

void testFoo() {
    value foo = Foo();
    foo.count++;
    print(foo.count);
}