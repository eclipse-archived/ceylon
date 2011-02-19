class Generics() {
    
    class Holder<X>(X x) {
        shared X held = x;
        X add(X x, X y) {
            return x + y;
        }
    }
    
    @type["Holder<String>"] Holder<String> hs;
    
    @type["Holder<String>"] Holder<String>("hello");
    
    Holder<String> h1 = Holder<String>("hello");
    String hello = h1.held;
    String hw = h1.add("hello", "world");
    @error Natural en = h1.add(1,2);
    
    Holder<Natural> h2 = Holder<Natural>{ x = 0; };
    Natural zero = h2.held;
    Natural n = h2.add(1,2);
    @error String ehw = h2.add("hello", "world");
    
}