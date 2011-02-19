class Generics() {
    
    class Holder<X>(X x) {
        shared X held = x;
        X add(X x, X y) {
            return x + y;
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
            if (arg=="ERROR") {
                X nothing { return arg; }
                return nothing;
            }
            else {
                return arg;
            }
        }
    }
    String hiback = C<String>().cop("Hi back!");
    
}