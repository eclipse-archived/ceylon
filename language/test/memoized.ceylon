class Memoized() {
    variable value i = 0;
    shared late Integer memoized = ++i;
}
variable Integer memoized_i = 0;
shared late Integer memoized_1 = ++memoized_i;
shared late Integer memoized_2 = ++memoized_i;

@test
shared void memoized() {
    variable value m = Memoized();
    check(1 == m.memoized);
    check(1 == m.memoized);
    try {
        m.memoized = -100;
        throw;
    } catch (InitializationError e) {}
    
    m = Memoized();
    m.memoized = -100;
    check(-100 == m.memoized);
    check(-100 == m.memoized);
    try {
        m.memoized = -200;
        throw;
    } catch (InitializationError e) {}
    
    check(1 == memoized_1);
    check(1 == memoized_1);
    try {
        memoized_1 = -100;
        throw;
    } catch (InitializationError e) {}
    
    memoized_2 = -100;
    check(-100 == memoized_2);
    check(-100 == memoized_2);
    try {
        memoized_2 = -200;
        throw;
    } catch (InitializationError e) {}
}