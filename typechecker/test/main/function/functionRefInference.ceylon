void functionRefInference() {
    T[] accept<T>(T(Integer,Integer)* funs) => [for (fun in funs) fun(1, 0) ];
    @type:"Sequential<Integer>" accept(plus, times);
    @type:"Sequential<Integer>" accept((x,y)=>x+y, (x,y)=>x*y);
    
    U whatever<U>(U fun({String*} it)) => fun({""});
    @type:"Integer" whatever(Iterable.size);
    
    @type:"Iterable<Integer,Nothing>" {"asdfasdf", [1, 2, 3], 1..10}.map(Iterable.size);
    @type:"Iterable<Tuple<Integer,Integer,Empty>,Nothing>" value r1 
            = [1, 2, 3].map([].withTrailing);
    @type:"Integer" value product1 = [1, 2, 3].reduce(times);
    @type:"Float" value product2 = [1.0, 2.0, 3.0].reduce(times);
    @error value product3 = [1, 2, 3, 1.2].reduce(times);
    @error value product4 = ["1", "2"].reduce(times);
    @type:"String" value sum1 = ["1", "2"].reduce(plus);
    @type:"Integer" value sum2 = [1, 2, 3].fold(0)(plus);
    @error value sum2 = [1, 2, 3.0].fold(1)(times);
    @type:"Iterable<Singleton<Integer|Float>,Nothing>" value singletons1
            = [1, 2, 1.0, 2.0].map(Singleton);
    value singletons2 = [1, null, 2, null, 1.0, null, 2.0].map { 
        collecting = emptyOrSingleton; 
    };
}

void inf() {
    void foo1<Bar>(void func(Bar b)){} 
    @error foo1(void(b){});
    void foo2<Bar>(Anything(Bar) b){}
    @error foo2(void(b){});
    
    void fun<X>(X x) {}
    Y accept1<Y>(void f(Y y)) { throw; }
    Y accept2<Y>(Anything(Y) f) { throw; }
    @type:"Nothing" accept1(fun);
    @type:"Nothing" accept2(fun);
}

void g() {
    value pl = plus;
    [1,2,3].fold(0)(pl);
    f("");
    { "" }.each((a) => f(a));
    { "" }.each(f);
}

void f<Absent>(Absent|String a)
        given Absent satisfies Null => noop();
