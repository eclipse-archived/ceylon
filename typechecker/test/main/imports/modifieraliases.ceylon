import ceylon.language { public=shared, var=variable, 
    @error abstract=sum, shared=product }

public class Foo() {
    public var Integer count=0;
}

void testFoo() {
    value foo = Foo();
    foo.count++;
    foo.count = shared { 3, 3, 4 };
    print(foo.count);
}