class Generics() {
    
    class Holder<X>(X x) {
        shared X held = x;
        X add(X x, X y) {
            @error return x + y;
        }
        Y noop<Y>(Y y) {
            return y;
        }
    }
    
    Holder<W> create<W>(W w) {
        return Holder<W>(w);
    }
    
    @type["Generics.Holder<String>"] Holder<String> hs;
    
    @type["Generics.Holder<String>"] Holder<String>("hello");
    @type["String"] Holder<String>("hello").held;
    
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
        X cop(X arg) {
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
    
    class Variances<out X, in Y, Z>(X x, Y y) 
            given X satisfies Equality<X>
            given Y satisfies Equality<Y> {
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
        class GoodClassInheritance() satisfies X[] {}
        @error class BadClassInheritance() satisfies Y[] {}
        class GoodClassInheritance2() satisfies Producer<X> {}
        @error class BadClassInheritance2() satisfies Producer<Y> {}
        class GoodClassInheritance3() satisfies Consumer<Y> {}
        @error class BadClassInheritance3() satisfies Consumer<X> {}
        class GoodParameterizedClass<out T>(T t) {}
        class GoodParameterizedClass2<out T>(Producer<T> t) {}
        class GoodParameterizedClass3<out T>(Consumer<T> t) {}
        class GoodParameterizedClass4<out T>(void get(T t)) {}
        class GoodParameterizedClass5<out T>(void get(T[] t)) given T satisfies Equality<T> {}
        class GoodParameterizedClass6<out T>(void get(Producer<T> t)) {}
        class GoodParameterizedClass7<in T>(T t) {}
        class GoodParameterizedClass8<in T>(void get(Consumer<T> t)) {}
        class GoodParameterizedClass9<in T>(Producer<T> t) {}
        class GoodParameterizedClass10<in T>(Consumer<T> t) {}
        class BadParameterizedClass<out T>(void get(Consumer<T> t)) {}
        class BadParameterizedClass2<in T>(void get(T t)) {}
        class BadParameterizedClass3<in T>(void get(T[] t)) given T satisfies Equality<T> {}
        class BadParameterizedClass4<in T>(void get(Producer<T> t)) {}
        void goodHigherOrderMethod(void get(X x)) {}
        void badHigherOrderMethod(void get(@error Y x)) {}
        void goodHigherOrderMethod2(void get(X[] x)) {}
        void badHigherOrderMethod2(void get(@error Y[] x)) {}
        void goodHigherOrderMethod3(void get(Producer<X> x)) {}
        void badHigherOrderMethod3(void get(@error Consumer<X> x)) {}
        
        Z methodWithNonvariant(Z z) { return z; }
        Z attributeWithNonvariant;
        
        class NestedClass() {
            X x;
            @error Y y;
            void goodMethod(Y y) {}
            void badMethod(@error X x) {}
            Z method(Z z) { return z; }
        }
    }
    
    class Bar() {
        String hello = "Hello";
    }
    class Foo<X>(X x) given X satisfies Bar {
        @type["String"] x.hello;
    }
    
    class Outer<X>(X x) given X satisfies Equality<X> {
        //shared X x = x;
        class Inner<Y>(Y y) given Y satisfies Equality<Y> {
            //shared Y y = y;
            Entry<X,Y> getIt() {
                return x->y;
            }
        }
        Inner<String> createInner(String string) {
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
    
    @type["Generics.Outer<Natural>.Inner<String>"] aa;
    @type["Entry<Natural,String>"] aa.getIt();
    
    class Num() satisfies Comparable<Num> {}
    
    abstract class SortedList<T>(T... elements) 
        satisfies T[] 
        given T satisfies Comparable<T> {}
    
    interface WithCovariant<out X> {}
    class Good1<T>() satisfies WithCovariant<T> {}
    @error class Bad1<in T>() satisfies WithCovariant<T> {}
    
    interface WithContravariant<in X> {}
    class Good2<T>() satisfies WithContravariant<T> {}
    @error class Bad2<out T>() satisfies WithContravariant<T> {}
}