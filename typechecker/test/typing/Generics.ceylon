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
    
    @type["Holder<String>"] Holder<String> hs;
    
    @type["Holder<String>"] Holder<String>("hello");
    @type["String"] Holder<String>("hello").x;
    
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
    
    @type["Holder<Natural>"] create<Natural>(3);
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
    
    class BadVariances<out X, in Y>(X x, Y y) {
        X goodAtt = x;
        @error Y badAtt = y;
        X[] goodAtt2 { return {x}; }
        @error Y[] badAtt2 { return {y}; }
        X goodMethod() { return goodAtt; }
        @error Y badMethod() { return badAtt; }
        X[] goodMethod2() { return {goodAtt}; }
        @error Y[] badMethod2() { return {badAtt}; }
        Producer<X> goodMethod3() { return Producer<X>(); }
        @error Consumer<X> badMethod3() { @error return Consumer<X>(); }
        Consumer<Y> goodMethod4() { return Consumer<Y>(); }
        @error Producer<Y> badMethod4() { @error return Producer<Y>(); }
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
    }
    
}