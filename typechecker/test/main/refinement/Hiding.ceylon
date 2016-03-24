interface BasicHiding {
    String name => "Gavin";
    class Outer() {}
    class Foo() {
        class Inner() {}
        String email = "Gavin";
        @error print(Foo().name);
        print(Foo().email);
        @error print(Foo().Outer());
        print(Foo().Inner());
        @error print(Bar().name);
        @error print(Bar().Outer());
        print(Bar().email);
        print(Bar().Inner());
        @error print(Bar().count);
    }
    class Bar() extends Foo() {
        Integer count = 1;
    }
}

interface Hiding {

shared class Foo() {
    shared String get() => "foo";
    @type:"String" Bar().get();
    shared class Inner() {}
    @type:"Hiding.Foo.Inner" Bar().Inner();
}

class Bar() extends Foo() {
    
    Integer get() => 2;
    
    class Inner() {}
    
    @type:"Integer" get();
    @type:"Integer" this.get();
    @type:"Integer" Bar().get();
    @type:"String" Foo().get();
    @type:"String" (Bar() of Foo).get();
    
    @type:"Hiding.Bar.Inner" Inner();
    @type:"Hiding.Bar.Inner" this.Inner();
    @type:"Hiding.Bar.Inner" Bar().Inner();
    @type:"Hiding.Foo.Inner" Foo().Inner();
    @type:"Hiding.Foo.Inner" (Bar() of Foo).Inner();
    
}

}

interface NoHiding {

class Super() {
    shared default Anything fu = null;
    
    shared default void bar(Sub sub) {
        String x = sub.fu;
    }
}

class Sub() extends Super() {
    shared actual String fu = "";
    
    shared actual void bar(Sub sub) {
        String x = sub.fu;
    }
}

void bar(Sub sub) {
    String x = sub.fu;
}

}