class UseSiteTest<T>(shared T t){}

class UseSiteG1(){}
class UseSiteG2() extends UseSiteG1(){}
class UseSiteG3() extends UseSiteG2(){}

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
}
