class Foobar1 {
    shared Integer a;
    shared Integer c;    
    c = 3;
    
    shared new foo(Integer val) {
        a = val;
        return;        
    }
    
    shared new bar(Integer val) {
        print(c);
        a = val;
    }
    
}

class Foobar2 {
    shared Integer a;
    shared Integer c;    
    c = 3;
    
    
    $error return;
    
    $error
    shared new foo(Integer val) {
        a = val;
        return;        
    }
    
    $error
    shared new bar(Integer val) {
        print(c);
        a = val;
    }
    
}

class Foobar3 {
    shared Integer a;
    shared Integer c;    
    c = 3;
    
    shared new foo(Integer val) {
        $error return;
        $error a = val;
    }
    
    shared new bar(Integer val) {
        print(c);
        a = val;
    }
    
}

void run0() {
    value ref = Bar.do;
    String x = "";
    class Bar() {
        shared void do() {
            print(x);
        }
    }
}

void run1() {
    $error Bar();
    String x = "";
    class Bar() {
        print(x);
    }
}

void run2() {
    $error Bar.create();
    String x = "";
    class Bar {
        shared new create() {
            print(x);
        }
    }
}

void run3() {
    $error Bar.create();
    String x = "";
    class Bar {
        $error shared static void create() {
            print(x);
        }
        new () {}
    }
}

void run4() {
    $error object bar extends Bar() {}
    $error object baz satisfies Baz {}
    String x = "";
    class Bar() {
        print(x);
    }
    interface Baz {
        void fun() => print(x);
    }
}
