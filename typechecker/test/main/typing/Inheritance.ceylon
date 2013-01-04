class Inheritance() {
    
    interface I<T> {
        shared void doIt(T t) {}
    }
    
    class X<T>(T t) 
            satisfies I<T> 
            given T satisfies Comparable<T> {
        T it = t;
        shared T getIt() { return it; }
        shared Boolean isIt(T t) { return t==it; }
    }
    
    class Y<T>(T t) 
            extends X<T>(t) 
            given T satisfies Comparable<T> {}
    
    class Z() 
            extends X<String>("Hello") {
        void method() {
            String sss = getIt();
            Boolean b = isIt("hi");
        }
    }
    
    class W<U,V>(U u, V v) 
            extends X<V>(v) 
            given V satisfies Comparable<V> {
        void method() {
            V vv = getIt();
        }
    }
    
    class ZZ(Integer n) 
            extends X<Integer>(n) {
        @error String sss = getIt();
        @error Boolean b = isIt("hi");
    }
    
    class GoodEnough(Integer n) 
            extends X<String>(n.string) {}
    
    @error class Bad1() extends X<String>() {}
    
    @error class Bad2(Integer n) extends X<String>(n) {}
    
    @error class Bad3() extends I<String>() {}
    
    @error class Bad4() satisfies X<String> {}
    
    X<String> ys = Y<String>("foo");
    ys.doIt("to a string");
    @error ys.doIt(1);
    @type:"String" ys.getIt();
    I<String> iys = ys;
    Object oys = iys;
    
    X<Integer> yn = Y<Integer>(1);
    yn.doIt(6);
    @type:"Integer" yn.getIt();
    I<Integer> iyn = yn;
    
    X<String> z = Z();
    z.doIt("to a string");
    @type:"String" z.getIt();
    I<String> iz = z;
    
    X<Float> w = W<String,Float>("amount", 1.3);
    w.doIt(4.5);
    @type:"Float" w.getIt();
    I<Float> iw = w;
    
    @type:"String" X<String>("goodbye").getIt();
    
    @type:"Inheritance.Y<String>" Y<String>("hello");
    
    @type:"String" Y<String>("adios").getIt();
    
    @type:"Inheritance.Z" Z();
    
    @type:"String" Z().getIt();
    
    @type:"Inheritance.W<Float,Integer>" W<Float,Integer>(1.2, 1);
    
    @type:"Integer" W<Float,Integer>(1.2, 1).getIt();
    
    /*object none satisfies Bottom[] {
        shared actual Integer? lastIndex = null;
        shared actual Nothing value(Integer n) {
            return null;
        }
    }*/
    
    class Outer() {
        void print(String s) {}
        shared interface Inner {
            shared formal void hello();
            shared formal void hi();
        }
        shared interface Inner2 satisfies Inner {
            shared actual void hello() {
                print("hello");
            }
        }
        shared class Inner3() satisfies Outer.Inner2 {
            shared actual void hi() { 
                hello(); 
            }
        }
        shared class Inner4() satisfies Inheritance.Outer.Inner2 {
            shared actual void hi() { 
                hello(); 
            }
        }
        class Inner5() extends Inheritance.Outer.Inner4() {}
        shared Inner2 inner1 = Inner3();
        Outer.Inner2 inner2 = Inner3();
        Outer.Inner2 inner3 = Outer.Inner3();
        Inner2 inner4 = Outer.Inner3();
        Inheritance.Outer.Inner2 inner5 = Inheritance.Outer.Inner3();
        Inner2 inner6 = Inner4();
        Inheritance.Outer.Inner2 inner7 = Inheritance.Outer.Inner4();
    }
    
    @error class Outer2() satisfies Outer.Inner2 {
        shared actual void hi() { 
            hello(); 
        }
    }
    
    Outer.Inner2 oi2 = Outer2();
    Outer.Inner2 oi3 = Outer().Inner3();
    Outer.Inner2 oi4 = Outer().inner1;
    @error Outer.Inner2 oi5 = Outer.Inner3();
    @error Inheritance.Outer.Inner2 oi6 = Inheritance.Outer.Inner3();
    @error Object foo = Outer.Foo.Bar();
    
    void method<T>(T x) 
            given T satisfies Outer.Inner2 {
        x.hello();
    }
    
    method(Outer().Inner3());
    method(Outer().inner1);
    method(oi3);
    
    abstract class A() {
        shared formal void hello();
        shared formal class C() {}
    }
    
    class B() extends A() {
        @error super.hello();
        @error super.C();
        shared actual void hello() {
            @error super.hello();
        }
        shared actual class C() 
            extends super.C() {}
    }
    
    class D() {
        @error Void sup1 = super;
        Void sup2;
        @error sup2 = super;
        variable Void sup3 = null;
        @error sup3 = super;
        void accept(Void v) {}
        @error accept(super);
        Void supe() {
            @error return super;
        }
    }
    
    class XXXX(String s) {}
    class YYYY(s) extends XXXX(s) {
        shared String s;
    }
    
}

interface MyIdentif satisfies Identifiable {}
class MyClass() extends Object() satisfies MyIdentif {}

@error class MyBottom() extends Bottom() {}
