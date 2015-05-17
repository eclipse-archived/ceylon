class Foo<T>() {
    shared class Bar<K>() {}
}

interface Set<T> {}

void testAdvancedStuff() {
    Functor<Float, Foo<Integer>.Bar> func1 = nothing;
    Foo<Integer>.Bar<String> fmap1 = func1.fmap(Float.string);
    
    Functor<Float, List|Set> func2 = nothing;
    List<Integer>|Set<Integer> fmap2 = func2.fmap(Float.integer);
    
    Functor<Integer,List>|Functor<Integer,Set> xfun = nothing;
    List<String>|Set<String> xfmap = xfun.fmap(Object.string);
    Functor<Integer, out List|Set> xf = xfun;

    Functor<Integer,List>&Functor<Integer,Set> yfun = nothing;
    Nothing n = yfun;
    @error List<String>&Set<String> yfmap1 = yfun.fmap(Object.string);
    Functor<Integer, out List&Set> yf = yfun;
    List<String>&Set<String> yfmap2 = yf.fmap(Object.string);
    
    Functor<Float,List> | Functor<Float,Sequence> f = nothing;
    @type:"List<String>" value result1 = f.fmap(Object.string);
    
    Functor<Float,List> | Functor<Integer,Sequence> g = nothing;
    @type:"List<String>" value result2 = g.fmap(Object.string);
    Functor<Float|Integer, out List|Sequence> gg = g;
    
    Functor<Float,out List> & Functor<Integer,out Sequence> h = nothing;
    @type:"Sequence<String>" value result3 = h.fmap(Object.string);
    Functor<Float|Integer, out List&Sequence> hh = h;
    
    Functor<Integer, Nothing> funny = 
            object satisfies Functor<Integer, Nothing> {
        shared actual Nothing fmap<Result>(Result(Integer) fun)
                => nothing;
    };
    Functor<Integer, out Iterable> funn = funny;
    
    Functor<String,Sequence> seqfun = nothing;
    @error Functor<String,List> listfun = seqfun;
    Functor<String,out List> wildlistfun = seqfun;

}

void moreConstraints() {
    Category cat = "hello world";
    
    void accept<E,C>(C<in E> c, E e) 
            given E satisfies Object
            given C<E_> satisfies Category<E_>
            given E_ satisfies Object {
        print(c.contains(e));
    }
    
    accept<String,Category>(cat, "hello");
    accept<String,Category>(cat, "goodbye");
    accept<Integer,List>(1..10, 5);
    accept<Integer,List>(1..10, -1);
}