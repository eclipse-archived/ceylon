void parameterInference() {
	
    $type:"Iterable<Character,Null>" value mapped = "hello world".map((c) => c.uppercased);
    $type:"Iterable<Character,Null>" value mapped1 = "hello world".map(=> element.uppercased);
    $type:"Iterable<Character,Null>" value filtered = "hello world".filter((c) { return !c.whitespace; });
    $type:"Integer" value folded = (1..10).fold(0, (r, x) => r+x);
    $type:"Integer" value folded2 = (1..10).fold(0, (r, x) { Integer r; Integer x; return r+x; });
    $type:"Integer" value folded3 = (1..10).fold(0, (r, x) { return r+x; });
    $type:"Integer" value reduced = (1..10).reduce<Integer>((r, x) => r+x);
    $type:"Integer" value reduced1 = (1..10).reduce<Integer>(=> partial+element);
    $type:"Float|Integer" value reduced2 = (1..10).reduce<Float>((r, x) {
        switch (r) 
        case (is Integer) { return (r+x).float; } 
        case (is Float) { return r+x; } 
    });
    function float(Float|Integer num) {
        switch (num) 
        case (is Integer) { return num.float; } 
        case (is Float) { return num; } 
    }
    $type:"Float|Integer" value reduced3 
            = (1..10).reduce<Float>((r, x) => float(r)+x);
    $type:"Float|Integer" value reduced4 
            = (1..10).reduce<Float> { (r, x) => float(r)+x; };
    
    T? fun<T>({T*} ts)(Boolean match(T t)) => ts.find(match);
    Character?(Boolean(Character)) fun1 = fun("goodbye*world");
    
    Character? found
            = fun("hello-world")
    ((ch)=>!ch.letter);
    Character? found1
            = fun1((ch)=>!ch.letter);
    
    void accept(String(Float) fun) => fun(1.0);
    accept((f) => f.string);
    accept(=> it.string);
    accept { (f) => f.string; };
    accept { (f) { return f.string; }; };
    
    void variadic(Anything(String)* args) {}
    variadic((string)=>print(string), (string)=>print(string));
    void iterable({Anything(String)*} args) {}
    iterable { (string)=>print(string),
        (string)=>print(string) };

    String hello(String(String)? fun) 
            => if (exists fun) then fun("hello") else "";
    print(hello((str) => str + "world")); 
    print(hello(null));
    
    $type:"Iterable<Character,Null>" value mapped_ = "hello world".map((value c) => c.uppercased);
    $type:"Iterable<Character,Null>" value filtered_ = "hello world".filter((value c) { return !c.whitespace; });
    $type:"Integer" value folded_ = (1..10).fold(0, (value r, value x) => r+x);
    accept((value f)=>f.string);
    variadic((value string)=>print(string), (value string)=>print(string));
    
    value funWithNoParamType = (p) => 0;
    value funWithNoParamType_ = (value p) => 0;
    
    $error value funWithNoParams = => "";
    
    T silly<T>(T t, T(T) f) => f(t);
    $type:"String" silly("", => it);
}

void testNotVariable() {
    void fun(void asdfas(Integer i)) {}
    fun(void (y) {
        $error y = 1; 
        $error print(y=2);
        $error y++;
    });
}

void moreParamInference() {
	function fold<E,R>({E*} es, R i)(R f(R j, E e)) => es.fold(i, f);
	
	$type:"Float" value folded0 = fold(1..10, 0.0)((value r, value x) => r+x);
	$type:"Float" value folded1 = fold(1..10, 0.0)((r, x) => r+x);
	$type:"Float" value folded2 = fold(1..10, 0.0)((r, x) { Float r; Integer x; return r+x; });
	$type:"Float" value folded3 = fold(1..10, 0.0)((r, x) { return r+x; });

}

void fun(String|Anything(String) foo) {}

alias Alias => String|Anything(String);

void fun2(Alias foo) {}

void funrun() {
    fun((str) => 1);
    fun(=> 1);
    fun2((str) => 1); 
    fun2(=> 1);
}

void usageInference() {
    $type:"String(Integer)" 
    value fun1 = (n) => Integer.format(n, 16);
    $type:"String(Integer)" 
    value fun2 = (value n) => Integer.format(n, 16);
    $type:"String(Integer)" 
    value fun3 = (n) => Integer.format { integer = n; radix = 16; };
    $type:"String(Integer)" 
    value fun4 = (n) { value ref = Integer.format; return ref(n, 16); };
    $type:"String(Nothing)"
    value fun5 = (n) => Integer.format(n) + Float.format(n);
    $type:"String(Integer=)" 
    $error:"argument must be assignable to parameter 'float'"
    value fun6 = (value n=0) => Integer.format(n) + Float.format(n);
}