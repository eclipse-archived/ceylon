class Generics() {
    
    class Holder<X>(X x) {
        shared X held = x;
        shared X add(X x, X y) {
            @error return x + y;
        }
        shared Y noop<Y>(Y y) {
            return y;
        }
    }
    
    Holder<W> create<W>(W w) {
        return Holder<W>(w);
    }
    
    @type:"Generics.Holder<String>" Holder<String> hs;
    
    @type:"Generics.Holder<String>" Holder<String>("hello");
    @type:"String" value shh = Holder<String>("hello").held;
    
    @type:"Generics.Holder<String>" Holder("hello");
    
    Holder<String> h1 = Holder<String>("hello");
    String hello = h1.held;
    String hw = h1.add("hello", "world");
    @error Integer en = h1.add(1,2);
    
    Holder<Integer> h2 = Holder<Integer>{ x = 0; };
    Integer zero = h2.held;
    Integer n = h2.add(1,2);
    @error String ehw = h2.add("hello", "world");
    
    @type:"Float" h2.noop<Float>(1.0);
    String s1 = h2.noop<String>("Hi!");
    String s2 = h2.noop("Hi!");
    
    @type:"Generics.Holder<Integer>" create<Integer>(3);
    Integer nn = create<Integer>(5).held;
    
    X op<X>(X arg) {
        return arg;
    }
    String hi = op<String>("Hi");
    @error String hi2 = op<X>("Hi");
    
    class C<X>() {
        shared X cop(X arg) {
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
    
    class Variances<out X, in Y, Z>(X x, Y y, Z z) 
            given X satisfies Object
            given Y satisfies Object {
        X goodAtt = x;
        @error Y badAtt = y;
        X[] goodAtt2 { return [x]; }
        @error Y[] badAtt2 { return [y]; }
        X goodMethod() { return goodAtt; }
        @error Y badMethod() { return badAtt; }
        X[] goodMethod2() { return [goodAtt]; }
        @error Y[] badMethod2() { return [badAtt]; }
        Producer<X> goodMethod3() { return Producer<X>(); }
        @error Consumer<X> badMethod3() { return Consumer<X>(); }
        Consumer<Y> goodMethod4() { return Consumer<Y>(); }
        @error Producer<Y> badMethod4() { return Producer<Y>(); }
        void goodAnythingMethod(Y y) {}
        void badAnythingMethod(@error X x) {}
        void goodAnythingMethod2(Y[] y) {}
        void badAnythingMethod2(@error X[] x) {}
        void goodAnythingMethod3(Consumer<X> c) {}
        void badAnythingMethod3(@error Consumer<Y> p) {}
        void goodAnythingMethod4(Producer<Y> c) {}
        void badAnythingMethod4(@error Producer<X> p) {}
        class GoodClass(Y y) {}
        class BadClass(@error X x) {}
        class GoodClass2(Y[] y) {}
        class BadClass2(@error X[] x) {}
        class GoodClassInheritance() 
                extends Object() 
                satisfies Sequence<X> {
            //fake implementations
            shared actual Integer lastIndex = 0;
            shared actual X[] rest = {};
            shared actual GoodClassInheritance clone = GoodClassInheritance();
            shared actual X? item(Integer key) { return null; }
            shared actual X[] segment(Integer from, Integer length) { return this; }
            shared actual X first { throw; }
            shared actual X last { throw; }
            shared actual Sequence<X> reversed { return this; }
            shared actual X[] span(Integer from, Integer to) { return this; }
            shared actual X[] spanTo(Integer to) { return this; }
            shared actual X[] spanFrom(Integer from) { return this; }
        }
        @error class BadClassInheritance() satisfies Sequence<Y> {}
        class GoodClassInheritance2() extends Producer<X>() {}
        @error class BadClassInheritance2() extends Producer<Y>() {}
        class GoodClassInheritance3() extends Consumer<Y>() {}
        @error class BadClassInheritance3() extends Consumer<X>() {}
        class BadParameterizedClassGiven1<T>(T t) @error given T satisfies X {}
        class GoodParameterizedClassGiven2<T>(T t) given T satisfies Y {}
        class GoodParameterizedClassGiven3<in T>(T t) given T satisfies Y {}
        class GoodParameterizedClassGiven4<out T>(T t) given T satisfies Y {}
        class OKParameterizedClassGiven1<out T>(T t) given T satisfies Y {
        	shared T get() { return t; }
        }
        class OKParameterizedClassGiven2<in T>(T t) given T satisfies Y {
        	shared void put(T t) {}
        }
        class GoodParameterizedClass<out T>(T t) {}
        class GoodParameterizedClass2<out T>(Producer<T> t) {}
        class GoodParameterizedClass3<out T>(Consumer<T> t) {}
        class GoodParameterizedClass4<out T>(void get(T t)) {}
        class GoodParameterizedClass5<out T>(void get(T[] t)) given T satisfies Object {}
        class GoodParameterizedClass6<out T>(void get(Producer<T> t)) {}
        class GoodParameterizedClass7<in T>(T t) {}
        class GoodParameterizedClass8<in T>(void get(Consumer<T> t)) {}
        class GoodParameterizedClass9<in T>(Producer<T> t) {}
        class GoodParameterizedClass10<in T>(Consumer<T> t) {}
        class BadParameterizedClass<out T>(void get(Consumer<T> t)) {}
        class BadParameterizedClass2<in T>(void get(T t)) {}
        class BadParameterizedClass3<in T>(void get(T[] t)) given T satisfies Object {}
        class BadParameterizedClass4<in T>(void get(Producer<T> t)) {}
        void goodHigherOrderMethod(void get(X x)) {}
        void badHigherOrderMethod(void get(@error Y x)) {}
        void goodHigherOrderMethod2(void get(X[] x)) {}
        void badHigherOrderMethod2(void get(@error Y[] x)) {}
        void goodHigherOrderMethod3(void get(Producer<X> x)) {}
        void badHigherOrderMethod3(void get(@error Consumer<X> x)) {}
        
        Z methodWithNonvariant(Z z) { return z; }
        Z attributeWithNonvariant;
        variable Z variableAtt = z;
        @error variable X badVariableAtt = x;
        @error variable Y badVariableAtt2 = y;
        Z goodGetter { return z; }
        assign goodGetter { }
        @error X badGetter { return x; }
        assign badGetter { }
        @error Y badGetter2 { return y; }
        assign badGetter2 { }
        
        class NestedClass() {
            X x;
            @error Y y;
            void goodMethod(Y y) {}
            void badMethod(@error X x) {}
            Z method(Z z) { return z; }
        }
        
        void goodGenericMethod1<D>(D d) given D satisfies Y {}
        void goodGenericMethod3<D>(D d) given D satisfies Consumer<X> {}
        void goodGenericMethod4<D>(D d) given D satisfies Producer<Y> {}
        void badGenericMethod1<D>(D d) @error given D satisfies X {}
        void badGenericMethod5<D>(D d) @error given D satisfies Producer<X> {}
        void badGenericMethod6<D>(D d) @error given D satisfies Consumer<Y> {}
    }
    
    class Bar() {
        shared String hello = "Hello";
    }
    class Foo<X>(X x) given X satisfies Bar {
        @type:"String" value xh = x.hello;
    }
    
    class Outer<X>(X x) given X satisfies Object {
        //shared X x = x;
        shared class Inner<Y>(Y y) given Y satisfies Object {
            //shared Y y = y;
            shared Entry<X,Y> getIt() {
                return x->y;
            }
        }
        shared Inner<String> createInner(String string) {
            return Inner<String>(string);
        }
    }
        
    @type:"Generics.Outer<Integer>.Inner<String>" Outer<Integer>(1).Inner<String>("hello");
    @type:"Generics.Outer<Integer>.Inner<String>" Outer<Integer>(1).createInner("hello");
    @type:"Entry<Integer,String>" Outer<Integer>(1).Inner<String>("hello").getIt();
    @type:"Entry<Integer,String>" Outer<Integer>(1).createInner("hello").getIt();

    Outer<Integer>.Inner<String> aa = Outer<Integer>(1).Inner<String>("hello");
    @error Inner<String> bb = Outer<Integer>(1).Inner<String>("hello");
    @error Outer<Integer> dd = Outer<Integer>(1).Inner<String>("hello");
    @error Outer<Integer>.Inner<String> cc = Outer<Float>(1.3).Inner<String>("hello");
    
    @type:"Generics.Outer<Integer>.Inner<String>" value aaa = aa;
    @type:"Entry<Integer,String>" aa.getIt();
    
    class Num() satisfies Comparable<Num> {
        //fake implementation
        shared actual Comparison compare(Num other) {
            throw;
        }
    }
    
    abstract class SortedList<T>(T* elements) 
        extends Object()
        satisfies Sequence<T> 
        given T satisfies Comparable<T> {
        shared actual formal String string;
    }
    
    interface WithCovariant<out X> {}
    class Good1<T>() satisfies WithCovariant<T> {}
    class Good2<out T>() satisfies WithCovariant<Producer<T>> {}
    class Good3<in T>() satisfies WithCovariant<Consumer<T>> {}
    @error class Bad1<in T>() satisfies WithCovariant<T> {}
    @error class Bad2<out T>() satisfies WithCovariant<Consumer<T>> {}
    
    interface WithContravariant<in X> {}
    class Good4<T>() satisfies WithContravariant<T> {}
    class Good5<in T>() satisfies WithContravariant<Producer<T>> {}
    @error class Bad6<out T>() satisfies WithContravariant<Consumer<T>> {}
    @error class Bad3<out T>() satisfies WithContravariant<T> {}
    @error class Bad4<in T>() satisfies WithContravariant<Consumer<T>> {}
    
    interface SequenceSequence<out T, out X> 
            satisfies Sequence<T>
            given T satisfies Sequence<X> & Object
            given X satisfies Object {}
    
    interface BadSequenceSequence<out T> 
            satisfies Sequence<T>
            /*@error*/ given T/*<out X>*/ satisfies Sequence<X> & Object
            @error given X satisfies Object {}
    
    class Upper<X>(X x)
            given X satisfies Object {
        shared Entry<X,Y> method<Y>(X x, Y y) 
                given Y satisfies Object { 
            return x->y; 
        }
        shared class Inner<Y>(X x, Y y) {}
    }
    
    class Lower<W>(W w) extends Upper<W>(w)
            given W satisfies Object {}
     
    @type:"Entry<String,Integer>" Lower<String>("hello").method<Integer>("world",1);
    @type:"Generics.Upper<String>.Inner<Float>" Lower<String>("hello").Inner<Float>("world", 2.3);
    
    interface Some<out X> {}
    class Foo1() satisfies Some<Object> {}
    class Bar1() extends Foo1() satisfies Some<String> {}
    Some<String> bar1 = Bar1();
    class Foo2() satisfies Some<String> {}
    class Bar2() extends Foo2() satisfies Some<Object> {}
    Some<String> bar2 = Bar2();
    interface Foo3 satisfies Some<Object> {}
    interface Bar3 satisfies Some<String> {}
    class Baz3() satisfies Foo3 & Bar3 {}
    Some<String> baz3 = Baz3();
    class Foo4() satisfies Some<Object> {}
    class Baz4() extends Foo4() satisfies Some<String> {}
    Some<String> baz4 = Baz4();
    class Foo5() satisfies Some<String> {}
    class Baz5() extends Foo5() satisfies Some<Object> {}
    Some<String> baz5 = Baz5();
    
    interface Self<out T> of T {
        shared default T get {
            return this of T;
        }
    }
    
    class Super() 
            satisfies Self<Super> {
        /*shared actual default Super get {
            return this;
        }*/
    }
    
    class Sub() 
            extends Super() 
            satisfies Self<Sub> {
        /*shared actual Sub get {
            return this;
        }*/
    }
    
    class SubSub()
            extends Sub() 
            satisfies Self<SubSub> {
        shared actual SubSub get {
            return this;
        }
    }
    
    @error class Wrong() 
            extends Super() 
            satisfies Self<String> {
        /*@error shared actual String get {
            return "hello";
        }*/
    }
    
    Super self1 = Super().get;
    Sub self2 = Sub().get;
    SubSub self3 = SubSub().get;

    void method<X>() {}

    @error Producer<Holder>();
    @error Producer<Holder> hp = Producer<Holder>();
    @error Producer<Holder>? ohp = null;
    @error Generics.Producer<Holder>();
    @error Generics.Producer<Holder> ghp = Generics.Producer<Holder>();
    @error Generics.Producer<Holder>? gohp = null;
    
    @error object wc satisfies WithCovariant {}
    @error object wch satisfies WithCovariant<Holder> {}
    @error object gwc satisfies Generics.WithCovariant {}
    @error object gwch satisfies Generics.WithCovariant<Holder> {}
    
    @error method<Holder>();
    @error this.method<Holder>();
    
    @error Producer<Holder<String,String>>();
    @error Producer<Holder<String,String>> hpss = Producer<Holder<String,String>>();
    @error Producer<Holder<String,String>>? ohpss = null;
    @error Generics.Producer<Holder<String,String>>();
    @error Generics.Producer<Holder<String,String>> ghpss = Generics.Producer<Holder<String,String>>();
    @error Generics.Producer<Holder<String,String>>? gohpss = null;
    
    @error object wchss satisfies WithCovariant<Holder<String,String>> {}
    
    @error method<Holder<String,String>>();
    @error this.method<Holder<String,String>>();
    
    @error Producer<String<String,String>>();
    @error Producer<String<String,String>> spss = Producer<String<String,String>>();
    @error Producer<String<String,String>>? ospss = null;
    @error Generics.Producer<String<String,String>>();
    @error Generics.Producer<String<String,String>> gspss = Generics.Producer<String<String,String>>();
    @error Generics.Producer<String<String,String>>? gospss = null;
    
    @error object wcsss satisfies WithCovariant<String<String,String>> {}
    
    @error method<String<String,String>>();
    @error this.method<String<String,String>>();
    
    @error Producer<Holder<Holder>>();
    @error method<Holder<Holder>>();
    
    @error Holder<String>.X wrong;
    
    T choose<T>(Boolean b, T first, T second) {
    	if (b) {
    		return first;
    	}
    	else {
    		return second;
    	}
    }
    
    P getFirst<P>(Sequence<P> list) {
        return list.first;
    } 
    Number getFirstNumber(Sequence<Number> nums) {
        return getFirst(nums);
    } 
    Object getFirstNonEmpty(Sequence<String> strs, Sequence<Object> obs) {
        return getFirst(choose(true, strs, obs));
    	/*if (nonempty strs) {
            return getFirst(strs);
        }
        else {
            return getFirst(obs);
        }*/
    }
    Object getFirstNonEmpty2(Sequence<String> strs, Sequence<Integer> ints) {
        return getFirst(choose(false, strs, ints));
        /*if (nonempty strs) {
            return getFirst(strs);
        }
        else {
            return getFirst(ints);
        }*/
    }
    
    interface Addable<in T> {
    	shared formal void add(T t);
    }
    class Var() { 
    	Boolean bool = true; 
    	void addTo(Addable<Var> trues, Addable<Var> falses) {
    		choose(bool, trues, falses).add(this);
    		//if (mValue) { trues.add(this); } else { falses.add(this); }
    	}
    }
    
    interface Inter1<in T> {
    	shared void put(T t) { throw; }
    }
    interface Inter2<out T> {
    	shared T get() { throw; }
    }

    interface Super1 satisfies Inter1<Nothing> & Inter2<String> {}
    interface Super2 satisfies Inter1<Integer> & Inter2<Object> {}
    @error class Impl() satisfies Super1 & Super2 {}
    value impl = Impl();
    String implget1 = impl.get();
    @error Integer implget2 = impl.get();
    impl.put(1);
    @error impl.put("hello");
    Inter1<Integer> inter1 = impl;
    Inter2<String> inter2 = impl;
    Inter1<Nothing> inter1b = impl;
    Inter2<Object> inter2o = impl;

    interface Super3 satisfies Inter1<Object> & Inter2<String> {}
    interface Super4 satisfies Inter1<Integer> & Inter2<Nothing> {}
    @error class Conc() satisfies Super3 & Super4 {}
    value conc = Conc();
    String concget1 = conc.get();
    Integer concget2 = conc.get();
    conc.put(1);
    conc.put("hello");
    Inter1<Integer> inter3 = conc;
    Inter2<String> inter4 = conc;
    Inter1<Nothing> inter3b = conc;
    Inter2<Object> inter4o = conc;
    
    abstract class Co1<out T>(T t) {
        shared class Foo() {
            shared T get() { return t; } 
        } 
        shared void bar(@error Foo foo) {
            T t = foo.get();
        }
    }
    
    abstract class Co2<out T>(T t) {
        shared class Foo() {
            shared T get() { return t; } 
        } 
        shared void bar(@error Co2<T>.Foo foo) {
            T t = foo.get();
        }
    }
    
    interface Enumerated<out U, in V, W> 
            of E0<U,V,W> | E1<U,V,W> | E2<U,V,W> | E3<U,V,W> | E4<V,W> | E5<U,W> | E6<U,V,W> {}
    @error interface E0<out U, in V, out W> satisfies Enumerated<U,V,W> {}
    @error interface E1<out U, in V, in W> satisfies Enumerated<U,V,W> {}
    @error interface E2<U, in V, W> satisfies Enumerated<U,V,W> {}
    @error interface E3<out U, V, W> satisfies Enumerated <U,V,W>{}
    interface E4<in V, W> satisfies Enumerated<Nothing,V,W> {}
    interface E5<out U, W> satisfies Enumerated<U,Anything,W> {}
    interface E6<out U, in V, W> satisfies Enumerated<U,V,W> {}

    abstract class Algebraic<out U, in V, W>() 
            of A0<U,V,W> | A1<U,V,W> | A2<U,V,W> | A3<U,V,W> | A4<V,W> | A5<U,W> | A6<U,V,W> {}
    @error class A0<out U, in V, out W>() extends Algebraic<U,V,W>() {}
    @error class A1<out U, in V, in W>() extends Algebraic<U,V,W>() {}
    @error class A2<U, in V, W>() extends Algebraic<U,V,W>() {}
    @error class A3<out U, V, W>() extends Algebraic<U,V,W>() {}
    class A4<in V, W>() extends Algebraic<Nothing,V,W>() {}
    class A5<out U, W>() extends Algebraic<U,Anything,W>() {}
    class A6<out U, in V, W>() extends Algebraic<U,V,W>() {}
    
    T genericMethod1<T>(T t) given T satisfies Numeric<T> {
        return t;
    }
    T genericMethod2<T>(T? t) given T satisfies Object {
        if (exists t) {
            return t;
        }
        else {
            throw;
        }
    }
    @error genericMethod1("hello");
    @type:"Integer" genericMethod1(1);
    @type:"String" genericMethod2("hello");
    @type:"String" genericMethod2(true then "hello");
    
    @type:"Iterable<String,Null>" coalesce(null, "hello");
    @type:"Sequential<String>" join({}, {"hello", "world"}, {"goodbye"});
    
    class ParamOuter<T>() {
        class Inner<Y>(){
            class Innerest() {}
            @error ParamOuter<String>.Inner<Y>.Innerest();
            @error ParamOuter<T>.Inner<Integer>.Innerest();
            @error ParamOuter<T>.Inner<Y>.Innerest();
        }
        @error ParamOuter<String>.Inner<Integer>();
        @error ParamOuter<T>.Inner<Integer>();
    }
        
    void withParamOfMethod<T>(T t) { Anything x = t; } 
    class WithParamOfClass<T>(T t) { Anything x = t; } 

    void unsatisfiable1<T>() @error given T satisfies Null&String {}
    void unsatisfiable2<T>() @error given T satisfies Null&Container {}
    
}