class UseSiteTest<T>(shared T t){}

class UseSiteG1(){}
class UseSiteG2() extends UseSiteG1(){}
class UseSiteG3() extends UseSiteG2(){}

class Y() {}
class X() extends Y() satisfies Invertible<X> {
    shared actual X negated => X();
    shared actual X plus(X other) => X();
}

@test
shared void testUseSiteVariance() {
    Object f = UseSiteTest(UseSiteG2());
    check(f is UseSiteTest<out Anything>, "UseSiteTest<out Anything>");
    check(f is UseSiteTest<out Object>, "UseSiteTest<out Object>");
    check(f is UseSiteTest<out UseSiteG1>, "UseSiteTest<out UseSiteG1>");
    check(f is UseSiteTest<UseSiteG2>, "UseSiteTest<UseSiteG2>");
    check(f is UseSiteTest<in UseSiteG3>, "UseSiteTest<in UseSiteG3>");
    check(f is UseSiteTest<in Nothing>, "UseSiteTest<in Nothing>");
    //These should all be false
    check(!f is UseSiteTest<Anything>, "UseSiteTest<Anything>");
    check(!f is UseSiteTest<Object>, "UseSiteTest<Object>");
    check(!f is UseSiteTest<UseSiteG1>, "UseSiteTest<UseSiteG1>");
    check(!f is UseSiteTest<UseSiteG3>, "UseSiteTest<UseSiteG3>");
    check(!f is UseSiteTest<Nothing>, "UseSiteTest<Nothing>");
    check(!f is UseSiteTest<in UseSiteG1>, "UseSiteTest<in UseSiteG1>");
    check(!f is UseSiteTest<out UseSiteG3>, "UseSiteTest<out UseSiteG3>");
    
    Object x = X();
    check(x is Invertible<out Anything>);
    check(x is Invertible<out Y>);
    check(x is Invertible<out X>);
    check(x is Invertible<in X>);
    check(x is Invertible<in Nothing>);
    check(x is Summable<out Anything>);
    check(x is Summable<out Y>);
    check(x is Summable<out X>);
    check(x is Summable<in X>);
    check(x is Summable<in Nothing>);
    check(!x is Invertible<out Integer>);
    check(!x is Invertible<in Integer>);
    check(!x is Summable<out Integer>);
    check(!x is Summable<in Integer>);
    
    Integer i = 0;
    check(i is Invertible<out Anything>);
    check(i is Invertible<out Object>);
    check(i is Invertible<out Integer>);
    check(i is Invertible<in Integer>);
    check(i is Invertible<in Nothing>);
    check(i is Summable<out Anything>);
    check(i is Summable<out Object>);
    check(i is Summable<out Integer>);
    check(i is Summable<in Integer>);
    check(i is Summable<in Nothing>);
    
    Object j = 0;
    check(j is Invertible<out Anything>);
    check(j is Invertible<out Object>);
    check(j is Invertible<out Integer>);
    check(j is Invertible<in Integer>);
    check(j is Invertible<in Nothing>);
    check(j is Summable<out Anything>);
    check(j is Summable<out Object>);
    check(j is Summable<out Integer>);
    check(j is Summable<in Integer>);
    check(j is Summable<in Nothing>);
    check(!j is Invertible<out X>);
    check(!j is Invertible<in X>);
    check(!j is Summable<out X>);
    check(!j is Summable<in X>);
    
}
