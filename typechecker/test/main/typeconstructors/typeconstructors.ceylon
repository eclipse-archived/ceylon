class X<T<U>>() {
    shared T<String> f(T<Integer> t) => nothing;
}

T<String> getF<T<U>>(X<T> x, T<Integer> t) {
    return x.f(t);
}

void test() {
    @type:"Sequence<String>" value ts = getF<Sequence>(X<Sequence>(), ([1] of Sequence<Integer>));
    @type:"List<String>" value ls = X<List>().f([1]);
    @type:"Sequence<String>" value ss = X<Sequence>().f([1]);
    @error value es = X<Singleton>().f([1]);
    @type:"Singleton<String>" value sss = X<Singleton>().f(Singleton(1));
    X<Singleton> xs1 = X<Singleton>();
    @error X<Singleton> xs2 = X<List>();
}