@noanno
shared void bug986() {
    value values = { 100, 110, 120 };
    class Fun(shared Integer() fun) {}
    value funcs = { for (v in values) Fun(()=>v) };
    assert (exists first=funcs.sequence[0]);
    assert (exists second=funcs.sequence[1]);
    assert (exists third=funcs.sequence[2]);
    if (first.fun() != 100) {
        print(first.fun());
        throw Exception();
    }
    if (second.fun() != 110) {
        print(second.fun());
        throw Exception();
    } 
    if (third.fun() != 120) {
        print(third.fun());
        throw Exception();
    }
}