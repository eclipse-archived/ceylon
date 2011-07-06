shared abstract class Comparable2<in Other>() 
        given this is Other satisfies Comparable2<Other> {
    
    shared formal Integer compare(Other that);
    
    shared Integer reverseCompare(Other that) { 
        return that.compare(this);
    }
    
}

class Foo(Integer i)
        extends Comparable2<Foo>() {
    value i = i;
    shared actual Integer compare(Foo that) {
        return this.i - that.i;
    }
}

@error class Bar()
       extends Comparable2<Foo>() {
    shared actual Integer compare(Foo that) {
        @error return that.i;
    }
}

void test() {
    Comparable2<Foo> foo = Foo(+1);
    foo.compare(foo);
    Foo foo2 = foo;
}