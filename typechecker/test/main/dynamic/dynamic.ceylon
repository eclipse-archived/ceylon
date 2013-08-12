void test() {
    dynamic {
        value x = value { y="hello"; };
        x.w = "goodbye";
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

void newit() {
    dynamic {
        value new = value {
            x="hello";
            y="world";
            bar(value name) => "oops ``name``";
        };
        print(new.x + " " + new.y);
        print(new.bar("dynamic"));
        value aa = value { a=1; b=2; 5,6,7,8 };
    }
}

variable String val = "";
void fun(String o) {}
String leak() {
  dynamic {
    value x = value { x=1; };
    val=x;
    fun(x);
    return x;
  }
}

void control() {
    dynamic {
        if (xyz) {}
        if (is String xyz) {
            @type:"String" value v = xyz;
        }
        if (!is String xyz) {
            @type:"unknown" value v = xyz;
        }
        if (exists xyz) {
            @type:"unknown" value v = xyz;
        }
        if (nonempty xyz) {
            @type:"unknown" value v = xyz;
        }
        for (i in xyz) {
            @type:"unknown" value v = i;
        }
        for (i->Object j in xyz) {
            @type:"unknown" value v = i;
            @type:"Object" value w = j;
        }
        for (String i in xyz) {
            @type:"String" value v = i;
        }
        switch (xyz)
        case (true) {}
        case (is String) {
             @type:"String" value v = xyz;
        }
    }
}