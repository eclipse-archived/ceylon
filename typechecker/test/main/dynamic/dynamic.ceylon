void test() {
    dynamic {
        value x = value { y="hello"; };
        function f(value z) => x.y + " " + z;
        value s = f(name);
        print(s.uppercased);
        for (c in s) {
            print(c.integer);
        }
        value xs = value { 1, 2, 3 };
        for (i in xs) {
            print(i^2);
        }
        String sx = x;
    }
}

T foo<T>(T t) given T satisfies Object => t;

void run() {
    dynamic {
        value x = value {};
        @error value f1 = foo(x);
        value f2 = foo<Integer>(x);
        value f3 = foo(x of Integer);
        
        for (xx in x) {}
        
        switch (x)
        case (is String) {}
        case (is Integer) {}
        
        if (is String x) {}
        
        @error value sing = Singleton(x);
        @error value iter = {x};
        @error value seq = [x];
    }
}