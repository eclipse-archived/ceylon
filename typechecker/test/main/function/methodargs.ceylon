void testShortcutMethodSpec(){
    Float f(Float g(Float x));
    f(Float g(Float x)) => g(1.0);
    f {
        g(Float x) => x*2.0;
    };
    f {
        g = (Float x) => x*2.0;
    };
    f {
        function g(Float x) => x*2.0;
    };
    //f {
    //    @error value g(Float x) => x*2.0;
    //};
    f {
        Float g(Float x) => x*2.0;
    };
    f {
        @error g(Integer x) => x*2.0;
    };
    f {
        @error g = (Integer x) => x*2.0;
    };
    f {
        @error function g(Integer x) => x*2.0;
    };
    f {
        @error g(Float x) => x.integer*2;
    };
    f {
        @error g = (Float x) => x.integer*2;
    };
    f {
        @error function g(Float x) => x.integer*2;
    };
    
    Float h(Float x);
    h(Float x) => x;
    h {
        x => 5.0;
    };
    h {
        value x => 5.0;
    };
    h {
        @error function x => 5.0;
    };
    h {
        Float x => 5.0;
    };
    h {
        @error x => 5;
    };
}

void bla(String a, Integer* p){
    bla("a", for(i in {1, 2}) i);
    bla("a");
    bla("a", 1, 2);
    bla("a", *[1, 2]);
}
void bli(Integer* p){
    bli(for(i in {1, 2}) i);
    bli();
    bli(1, 2);
    bli(*[1, 2]);
}

void testIndirectSpread() {
    value f = function(Integer a, Integer b=2, Integer* c) => 1;
    f(*[1]);
    f(1, *[]);
    f(1, *[2]);
    f(*[1, 2]);
    f(1, 2, *[]);
    f(1, 2, *[3]);
    f(1, 2, 3, *[]);
    f(*[1, 2, 3]);
}

void testIndirectWithUnknownParamTypes() {
    Callable<Anything, Nothing> f4 = function (String a, Integer b) => 2;
    @error f4();
    @error f4(1);
    @error f4(1, 3);

    Callable<Anything, Nothing[]> f5 = function (Integer* a) => 2;
    f5();
    @error f5(1);
    @error f5(1, 3);
}

shared void parameterizedByArgs<Args>(Args args, Callable<Anything,Args> fun) 
    given Args satisfies Anything[] {

    fun(*args);
    @error fun();
    @error fun(args);
    @error fun(1, 2, 3);

    Anything(String,Integer) fn = (String s, Integer i) => s[i];
    fn(*["a", 2]);
    parameterizedByArgs(["a", 2], fn);
}

shared void overload<T,V>(T t) 
        given T satisfies Anything[] 
        given V satisfies Anything[] {
    function g([Float]|[String,Integer]|[]|[String,String,String,String]|Character[] args) => args[0];
    Callable<Character|Float|Integer|String?,[Float]|[String,Integer]|[]|[String,String,String,String]|Character[]> f = flatten(g);
    Character|Integer|Float|String? x = f(5.0);
    @error Character|Integer|Float|String? y = f("hello");
    Character|Integer|Float|String? z = f();
    Character|Integer|Float|String? w = f("hello",1);
    Character|Integer|Float|String? v = f('a','b','c','d');
    @error Character|Integer|Float|String? u = f(5.0, 'x');
    
    function gg(T|V args) => nothing;
    Callable<Nothing,T|V> ff = flatten(gg);
    ff(*t);
}

void testSpreadTypeArg<Args>(Args args) 
        given Args satisfies Object[] {
    @type:"Tuple<Object,String,Args>" 
    value tup = ["hello", *args];
}

void testSpreadNoneptyTypeArg<Args>(Args args) 
        given Args satisfies [Object+] {
    @type:"Tuple<Object,String,Args>" 
    value tup = ["hello", *args];
}

void testSpreadEmptyIterTypeArg<Args>(Args args) 
        given Args satisfies {Object*} {
    @type:"Tuple<Object,String,Sequential<Object>>" 
    value tup = ["hello", *args];
}

void testSpreadNonemptyIterTypeArg<Args>(Args args) 
        given Args satisfies {Object+} {
    @type:"Tuple<Object,String,Sequence<Object>>" 
    value tup = ["hello", *args];
}

void testSpreadUnkIterTypeArg<Args,Abs>(Args args) 
        given Abs satisfies Null
        given Args satisfies Iterable<Object,Abs> {
    //@type:"Tuple<Object,String,Sequential<Object>&Iterable<Object,Abs>>" 
    @type:"Tuple<Object,String,Sequential<Object>>" 
    value tup = ["hello", *args];
}

void testSpreadNoniterable({String*}? args) {
    void fun(String* args) {}
    @error value v1 = { @error *args };
    @error value v2 = [ @error *args ];
    printAll { @error *args };
    @error fun(*args);
}

void moreSpreadTests() {
    function f(Integer a, Integer b, Integer+ c) => 1;
    function f0(Integer a, Integer b=2, Integer* c) => 1;
    value f1 = (Integer a, Integer b=2, Integer* c) => 1;
    value f2 = (Integer a, Integer b, Integer+ c) => 1;
    f(1, 2, 3, *empty);
    f0(1, 2, 3, *empty);
    f1(1, 2, 3, *empty);
    f2(1, 2, 3, *empty);
    @error f(1, 2, *empty);
    @error f(1, *empty);
    @error f(*empty);
    f0(1, 2, *empty);
    f0(1, *empty);
    @error f0(*empty);
    f1(1, 2, *empty);
    f1(1, *empty);
    @error f1(*empty);
    @error f2(1, 2, *empty);
    @error f2(1, *empty);
    @error f2(*empty);
    
    f2(*[1, 2, 3]);
    f2(*[1, 2, 3, 4]);
    @error f2(*[1, 2]);
    @error f2(*[1]);
    @error f2(*[]);
    f1(*[1, 2, 3]);
    f1(*[1, 2, 3, 4]);
    f1(*[1, 2]);
    f1(*[1]);
    @error f1(*[]);
    
    String g(String x="", String y="") => x+y;
    String(String=, String=) g1 = g;
    g("s1", "s2", *[]);
    g("s1", *[]);
    g(*[]);
    g1("s1", "s2", *[]);
    g1("s1", *[]);
    g1(*[]);
    
    @error g("s1", "s2", *["s3"]);
    @error g("s1", *["s2", "s3"]);
    g("s1", *["s2"]);
    g(*["s1"]);
    @error g1("s1", "s2", *["s3"]);
    @error g1("s1", *["s2", "s3"]);
    g1("s1", *["s2"]);
    g1(*["s1"]);

}

void testFooBar() {
    void foo({String+} items) {}
    @error foo(); // error: missing argument to required parameter items of foo
    @error foo{}; // NO ERROR
    foo{""};
    foo{items={""};};
    void bar({String+} items1, {String+} items2) {}
    @error bar(); // error: missing argument to required parameter items1 (and items2) of foo
    @error bar({""}); // error: missing argument to required parameter items2 of foo
    @error bar{}; // error: missing argument to required parameter items2 of foo
    @error bar{items1 = {""};}; // NO ERROR
    bar{items1 = {""}; ""};
    bar{items1 = {""}; items2 = {""};};
}

alias Attributes => {String*};

class Div(Attributes attributes = {}, {Object*} children = {}) {}

void run() {
    Div { "foo", "bar" };
    Div { @error Div{} };
    Div { @error "foo", "bar", Div{} };
}
