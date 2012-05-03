class TypeArgInference() {
    
    class X<T>(T t, T s) {
        shared class Y<S>(S ss) {}
        shared R? opt<R>(R r) { return null; }
    }
    
    @type["TypeArgInference.X<String>"] X("hello", "world");
    @type["TypeArgInference.X<Float|Integer>"] X(1.0, 1000);
    @type["TypeArgInference.X<String>.Y<Float>"] X("hello", "world").Y(3.0);
    @type["Nothing|String"] X(1,2).opt("hello");
    
    @type["TypeArgInference.X<String>"] X{ t="hello"; s="world"; };
    @type["TypeArgInference.X<Float|Integer>"] X { t=1.0; s=1000; };
    @type["TypeArgInference.X<String>.Y<Float>"] X{ t="hello"; s="world"; }.Y { ss=3.0; };
    @type["Nothing|String"] X { t=1; s=2; }.opt{ r="hello"; };
    
    U first<U>(U u, U v) { return u; }

    @type["String"] first("hello", "world");
    @type["Integer|TypeArgInference.X<String>"] first(+1,X("hello", "world"));
    
    @type["String"] first{ u="hello"; v="world"; };
    @type["Integer|TypeArgInference.X<String>"] first{ u=+1; v=X{ t="hello"; s="world"; }; };
    
    @type["TypeArgInference.X<IdentifiableObject>"] X { object t {} object s {} }; 
    @type["TypeArgInference.X<Float>"] X { Float t { return 1.0; } Float s { return -1.0; } }; 
    @type["TypeArgInference.X<Boolean>"] X(true, false);
    
    @error X x = X(1,2);
    
    //@error String firstString(String x, String y) = first;
    //String secondString(String x, String y) = first<String>;
        
    class Const<T,S>(T t) given T satisfies Numeric<T> {}
    @type["TypeArgInference.Const<Integer,Bottom>"] Const(1);
    @error Const("hello");
    
    @error first("hello");
    @error first("ullo", "ullo", "ullo");
    @error first { u="hi"; };
    
    T? firstElem<T>(T[]? sequence) {
        if (exists sequence) {
            return sequence.first;
        }
        else {
            return null;
        }
    }
    
    @type["Nothing|Empty|Sequence<String>"] String[]? strings = {"hello", "goodbye"};
    @type["Nothing|String"] value s = firstElem(strings);
    @type["Nothing|String"] value ss = firstElem { sequence=strings; };

    T corner<T>(Sequence<Sequence<T>> matrix) {
        return matrix.first.first;
    }
    
    @type["Sequence<Sequence<Integer>>"] value ints = {{-1}};
    @type["Integer"] value i = corner(ints);
    @type["Integer"] value ii = corner { matrix = ints; };
    
    T method<T>(String|Sequence<T> s) { throw; }
    @type["String"] method({"hello"});
    @type["Bottom"] method("hello");
    
    T? firstElt<T>(T... args) {
        return args.sequence.first;
    }
    @type["Nothing|String"] firstElt("hello", "world");
    @type["Nothing|Sequence<String>"] firstElt({"hello", "world"});
    @type["Nothing|String"] firstElt({"hello", "world"}...);
    @type["Nothing|String"] firstElt { "hello", "world" };
    @type["Nothing|Sequence<String>"] firstElt {{"hello", "world"}};
    @type["Nothing|String"] firstElt { args = {"hello", "world"}; };
    
    T? createNull<T>() {
        return null;
    }
    @type["Nothing"] value opt = createNull();
    
    void print(String s) {}
    print("Hello");
    @error print<String>("Hello");

    V f1<U,V>(U u) 
            given U satisfies V {
    	return u;
    }
    
    V f2<U,V>(U u) 
            given U satisfies Sequence<V> {
    	return u.first;
    }
    
    @type["String"] f1("hello");
    @type["Integer"] f2({1,2,3});
    
    interface Z {}
    interface W {}
    object xzn extends X<Integer>(0, 1) satisfies Z&W {}
    Z&X<Integer> zxn = xzn;
    
    function g<T>(X<T>&Z x) { return x; }
    
    @type["TypeArgInference.X<Integer>&TypeArgInference.Z"] g(xzn);
    @type["TypeArgInference.X<Integer>&TypeArgInference.Z"] g(zxn);
    
    interface One<out T> {}
    interface Two<out T> {}
    
    interface A {}
    interface B {}
    
    object test0 satisfies One<A> & Two<B> {}
    object test1 satisfies One<Integer> & Two<Float> {}
    object test2 satisfies One<Number> & Two<Integer&String> {}
    
    T? acceptOneTwo<T>(One<T>&Two<T> arg) {
    	return null;
    }
    T? acceptOneOrTwo<T>(One<T>|Two<T> arg) {
    	return null;
    }
    
    @type["Nothing|TypeArgInference.A|TypeArgInference.B"] acceptOneTwo(test0);
    @type["Nothing|Integer|Float"] acceptOneTwo(test1);
    @type["Nothing|Number"] acceptOneTwo(test2);
    
    @type["Nothing|TypeArgInference.A&TypeArgInference.B"] @error acceptOneOrTwo(test0);
    @type["Nothing"] @error acceptOneOrTwo(test1);
    @type["Nothing"] acceptOneOrTwo(test2);

}