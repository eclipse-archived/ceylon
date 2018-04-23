import check { check,fail }

String test410(String() f) {
  return f().lowercased;
}

//Static method references
class MethodRefTest(name) {
    shared String name;
    shared actual String string => "MethodRefTest ``name``";
    shared String suffix(Integer x) => "``string`` #``x``";

    shared class Inner(String kid) {
        shared actual String string => "``outer.string`` sub-``kid``";
        shared String prefix(Integer x) => "#``x`` ``string``";
    }
    shared String issue410() {
        function f() => name;
        return test410(f);
    }
}

void testGenericMethodReferences() {
    value strings={"a","b"};
    value f = strings.collect<String>;
    value g = f((String s)=>s.uppercased);
    check(g == ["A","B"], "Method references with type arguments");
    {String*}? opts = strings;
    value h = opts?.collect<String>;
    if (exists k=h((String s)=>s.uppercased)) {
      check(k == ["A","B"], "Method ref safe op w/type arguments");
    } else {
      fail("???? method ref safe op w/targs");
    }
    value w = strings.equals;
    value z = opts?.equals;
    check(!w(1), "method ref w/no targs");
    check(!(z(0) else false), "safeop method ref w/no targs");
}

<T> => F<G<T>>(T) compose1<F,G>
        (F<X> f<X>(X x), G<Y> g<Y>(Y y))
        given F<X> given G<Y>
        => <U>(U u) => f(g(u));
<T> => F<G<T>>(T) compose2<F,G>
        (<X>=>F<X>(X) f, <Y>=>G<Y>(Y) g)
        given F<X> given G<Y>
        => <U>(U u) => f(g(u));
        
void testHigherOrderGenericMethodReferences() {
    Set<Param> getSet<Param>(Set<Param> params, Param next) 
            given Param satisfies Object
            //=> set{next, *params};
            => set(params.sequence().withTrailing(next));
    
    value ref = getSet;
    
    //alias A => <Param> given Param satisfies Object => Set<Param>(Set<Param>, Param);
    
    function fun() => ref;
    //A gun() => ref;
    
    check(ref(set{1,2,3},4)==set{1,2,3,4}, "generic function ref 1");
    check(fun()(set{1,2,3},4)==set{1,2,3,4}, "generic function ref 2");
    //value mySet2 = gun()(set{1,2,3},4);
    
    T() lazy<T>(T t) => () => t;
    [T] singleton<T>(T t) => [t];
    value lazySingleton1 = compose1(lazy, singleton);
    value ls1 = compose1<<T>=>T(),<T>=>[T]>(lazy, singleton);
    [Integer]() int1 = lazySingleton1(4);
    [String]() str1 = lazySingleton1("hello");
    check(int1()==[4], "generic function ref 3");
    check(str1()==["hello"], "generic function ref 4");
    value lazySingleton2 = compose2(lazy, singleton);
    value ls2 = compose2<<T>=>T(),<T>=>[T]>(lazy, singleton);
    [Integer]() int2 = lazySingleton2(4);
    [String]() str2 = lazySingleton2("hello");
    check(int2()==[4], "generic function ref 3");
    check(str2()==["hello"], "generic function ref 4");
}

void testStaticMethodReferences() {
    print("Testing static method references...");
    value mr  = MethodRefTest("TEST");
    value mref = MethodRefTest.suffix(mr);
    check(mref(1) == "MethodRefTest TEST #1", "Static method ref 1");
    check(MethodRefTest.string(mr) == "MethodRefTest TEST", "Static method ref 2");
    check(mref(1) == MethodRefTest.suffix(mr)(1), "Static method ref 3");
    value mri = mr.Inner("T2");
    value iref = MethodRefTest.Inner.prefix(mri);
    check(iref(1) == "#1 MethodRefTest TEST sub-T2", "Static method ref 4");
    check(MethodRefTest.Inner.string(mri) == "MethodRefTest TEST sub-T2", "Static method ref 5");
    check(iref(1) == MethodRefTest.Inner.prefix(mri)(1), "Static method ref 6");
    value ints = {1,2,3,4}.sequence();
    check((List<Integer>.get(ints)(1) else -1) == 2, "Static method ref 7");
    check(List<Integer>.map<String>(ints)((Integer x)=>x.string).sequence() == ["1","2","3","4"], "Static method ref 8");
    value smr1 = List<Integer>.get;
    value smr2 = List<Integer>.map<String>;
    check((smr1(ints)(1) else -1) == 2, "Static method ref 9");
    check(smr2(ints)((Integer x)=>x.string).sequence() == ["1","2","3","4"], "Static method ref 10");
    value pad = String.padLeading;
    value test11=pad("x")(3,'.');
    check(test11=="..x", "Static method ref 11 expected ..x got ``test11``");
    check(mr.issue410() == "test", "Issue 410");
    Object() newd = C5900().D.create;
    check(newd() is C5900.D,"#5900");
}

shared class C5900() {
    shared class D {
        shared new create() {}
    }
}

void test517() {
    [Integer, R] time<R>(R() f) => [0, f()];
    time(noop);
}
