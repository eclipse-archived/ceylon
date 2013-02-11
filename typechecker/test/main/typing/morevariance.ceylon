shared interface X<in T> {}
shared interface Y<out T> {}
@error shared X<T> foo<out T>(X<T> x, X<T> y) => nothing;
shared Y<T> bar<out T>(Y<T> x, Y<T> y) => nothing;
shared X<T> baz<in T>(X<T> x, X<T> y) => nothing;
@error shared Y<T> qux<in T>(Y<T> x, Y<T> y) => nothing;

void testMethodVariance(X<String> x, Y<String> y, X<Object> z, Y<Object> w) {
    @type:"X<String>" baz(x,x);
    @type:"Y<String>" bar(y,y);
    @type:"X<String>" baz(x,z);
    @type:"Y<Object>" bar(y,w);
}
