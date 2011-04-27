class Generics() {
    
    class Holder<X>(X x) {
        shared X held = x;
        shared X add(X x, X y) {
            @error return x + y;
        }
        shared Y noop<Y>(Y y) {
            return y;
        }
    }
    
    Holder<W> create<W>(W w) {
        return Holder<W>(w);
    }
    
    @type["Generics.Holder<String>"] Holder<String> hs;
    
    @type["Generics.Holder<String>"] Holder<String>("hello");
    @type["String"] local shh = Holder<String>("hello").held;
    
    @error Holder("hello");
    
    Holder<String> h1 = Holder<String>("hello");
    String hello = h1.held;
    String hw = h1.add("hello", "world");
    @error Natural en = h1.add(1,2);
    
    Holder<Natural> h2 = Holder<Natural>{ x = 0; };
    Natural zero = h2.held;
    Natural n = h2.add(1,2);
    @error String ehw = h2.add("hello", "world");
    
    @type["Float"] h2.noop<Float>(1.0);
    String s1 = h2.noop<String>("Hi!");
    @error String s2 = h2.noop("Hi!");
    
    @type["Generics.Holder<Natural>"] create<Natural>(3);
    Natural nn = create<Natural>(5).held;
    
    X op<X>(X arg) {
        return arg;
    }
    String hi = op<String>("Hi");
    @error String hi2 = op<X>("Hi");
    
    class C<X>() {
        shared X cop(X arg) {
            if (false) {
                X nothing { return arg; }
                return nothing;
            }
            else {
                return arg;
            }
        }
    }
    String hiback = C<String>().cop("Hi back!");
    
    class Producer<out X>() {}
    class Consumer<in X>() {}
    
    Producer<Object> p = Producer<String>();
    Consumer<String> c = Consumer<Object>();
    Producer<Consumer<Producer<String>>> o = Producer<Consumer<Producer<Object>>>();
    
    class Variances<out X, in Y, Z>(X x, Y y, Z z) 
            given X satisfies Equality
            given Y satisfies Equality {
        X goodAtt = x;
        @error Y badAtt = y;
        X[] goodAtt2 { return {x}; }
        @error Y[] badAtt2 { return {y}; }
        X goodMethod() { return goodAtt; }
        @error Y badMethod() { return badAtt; }
        X[] goodMethod2() { return {goodAtt}; }
        @error Y[] badMethod2() { return {badAtt}; }
        Producer<X> goodMethod3() { return Producer<X>(); }
        @error Consumer<X> badMethod3() { return Consumer<X>(); }
        Consumer<Y> goodMethod4() { return Consumer<Y>(); }
        @error Producer<Y> badMethod4() { return Producer<Y>(); }
        void goodVoidMethod(Y y) {}
        void badVoidMethod(@error X x) {}
        void goodVoidMethod2(Y[] y) {}
        void badVoidMethod2(@error X[] x) {}
        void goodVoidMethod3(Consumer<X> c) {}
        void badVoidMethod3(@error Consumer<Y> p) {}
        void goodVoidMethod4(Producer<Y> c) {}
        void badVoidMethod4(@error Producer<X> p) {}
        class GoodClass(Y y) {}
        class BadClass(@error X x) {}
        class GoodClass2(Y[] y) {}
        class BadClass2(@error X[] x) {}
        class GoodClassInheritance() satisfies Sequence<X> {}
        @error class BadClassInheritance() satisfies Sequence<Y> {}
        class GoodClassInheritance2() extends Producer<X>() {}
        @error class BadClassInheritance2() extends Producer<Y>() {}
        class GoodClassInheritance3() extends Consumer<Y>() {}
        @error class BadClassInheritance3() extends Consumer<X>() {}
        class GoodParameterizedClass<out T>(T t) {}
        class GoodParameterizedClass2<out T>(Producer<T> t) {}
        class GoodParameterizedClass3<out T>(Consumer<T> t) {}
        class GoodParameterizedClass4<out T>(void get(T t)) {}
        class GoodParameterizedClass5<out T>(void get(T[] t)) given T satisfies Equality {}
        class GoodParameterizedClass6<out T>(void get(Producer<T> t)) {}
        class GoodParameterizedClass7<in T>(T t) {}
        class GoodParameterizedClass8<in T>(void get(Consumer<T> t)) {}
        class GoodParameterizedClass9<in T>(Producer<T> t) {}
        class GoodParameterizedClass10<in T>(Consumer<T> t) {}
        class BadParameterizedClass<out T>(void get(Consumer<T> t)) {}
        class BadParameterizedClass2<in T>(void get(T t)) {}
        class BadParameterizedClass3<in T>(void get(T[] t)) given T satisfies Equality {}
        class BadParameterizedClass4<in T>(void get(Producer<T> t)) {}
        void goodHigherOrderMethod(void get(X x)) {}
        void badHigherOrderMethod(void get(@error Y x)) {}
        void goodHigherOrderMethod2(void get(X[] x)) {}
        void badHigherOrderMethod2(void get(@error Y[] x)) {}
        void goodHigherOrderMethod3(void get(Producer<X> x)) {}
        void badHigherOrderMethod3(void get(@error Consumer<X> x)) {}
        
        Z methodWithNonvariant(Z z) { return z; }
        Z attributeWithNonvariant;
        variable Z variableAtt := z;
        @error variable X badVariableAtt := x;
        @error variable Y badVariableAtt2 := y;
        Z goodGetter { return z; }
        assign goodGetter { }
        @error X badGetter { return x; }
        assign badGetter { }
        @error Y badGetter2 { return y; }
        assign badGetter2 { }
        
        class NestedClass() {
            X x;
            @error Y y;
            void goodMethod(Y y) {}
            void badMethod(@error X x) {}
            Z method(Z z) { return z; }
        }
        
        void goodGenericMethod1<D>(D d) given D satisfies Y {}
        void goodGenericMethod3<D>(D d) given D satisfies Consumer<X> {}
        void goodGenericMethod4<D>(D d) given D satisfies Producer<Y> {}
        void badGenericMethod1<D>(D d) @error given D satisfies X {}
        void badGenericMethod5<D>(D d) @error given D satisfies Producer<X> {}
        void badGenericMethod6<D>(D d) @error given D satisfies Consumer<Y> {}
    }
    
    class Bar() {
        shared String hello = "Hello";
    }
    class Foo<X>(X x) given X satisfies Bar {
        @type["String"] local xh = x.hello;
    }
    
    class Outer<X>(X x) given X satisfies Equality {
        //shared X x = x;
        shared class Inner<Y>(Y y) given Y satisfies Equality {
            //shared Y y = y;
            shared Entry<X,Y> getIt() {
                return x->y;
            }
        }
        shared Inner<String> createInner(String string) {
            return Inner<String>(string);
        }
    }
        
    @type["Generics.Outer<Natural>.Inner<String>"] Outer<Natural>(1).Inner<String>("hello");
    @type["Generics.Outer<Natural>.Inner<String>"] Outer<Natural>(1).createInner("hello");
    @type["Entry<Natural,String>"] Outer<Natural>(1).Inner<String>("hello").getIt();
    @type["Entry<Natural,String>"] Outer<Natural>(1).createInner("hello").getIt();

    Outer<Natural>.Inner<String> aa = Outer<Natural>(1).Inner<String>("hello");
    @error Inner<String> bb = Outer<Natural>(1).Inner<String>("hello");
    @error Outer<Natural> dd = Outer<Natural>(1).Inner<String>("hello");
    @error Outer<Natural>.Inner<String> cc = Outer<Float>(1.3).Inner<String>("hello");
    
    @type["Generics.Outer<Natural>.Inner<String>"] local aaa = aa;
    @type["Entry<Natural,String>"] aa.getIt();
    
    class Num() satisfies Comparable<Num> {}
    
    abstract class SortedList<T>(T... elements) 
        satisfies Sequence<T> 
        given T satisfies Comparable<T> {}
    
    interface WithCovariant<out X> {}
    class Good1<T>() satisfies WithCovariant<T> {}
    class Good2<out T>() satisfies WithCovariant<Producer<T>> {}
    class Good3<in T>() satisfies WithCovariant<Consumer<T>> {}
    @error class Bad1<in T>() satisfies WithCovariant<T> {}
    @error class Bad2<out T>() satisfies WithCovariant<Consumer<T>> {}
    
    interface WithContravariant<in X> {}
    class Good4<T>() satisfies WithContravariant<T> {}
    class Good5<in T>() satisfies WithContravariant<Producer<T>> {}
    class Good6<out T>() satisfies WithContravariant<Consumer<T>> {}
    @error class Bad3<out T>() satisfies WithContravariant<T> {}
    @error class Bad4<in T>() satisfies WithContravariant<Consumer<T>> {}
    
    interface SequenceSequence<out T, out X> 
            satisfies Sequence<T>
            given T satisfies Sequence<X> & Equality
            given X satisfies Equality {}
    
    interface BadSequenceSequence<out T> 
            satisfies Sequence<T>
            @error given T/*<out X>*/ satisfies Sequence<X> & Equality
            @error given X satisfies Equality {}
    
    class Upper<X>(X x)
            given X satisfies Equality {
        shared Entry<X,Y> method<Y>(X x, Y y) 
                given Y satisfies Equality { 
            return x->y; 
        }
        shared class Inner<Y>(X x, Y y) {}
    }
    
    class Lower<W>(W w) extends Upper<W>(w)
            given W satisfies Equality {}
     
    @type["Entry<String,Natural>"] Lower<String>("hello").method<Natural>("world",1);
    @type["Generics.Upper<String>.Inner<Float>"] Lower<String>("hello").Inner<Float>("world", 2.3);
    
}