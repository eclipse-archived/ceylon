variable Integer staticSideEffect = 0;

@noanno
class StaticAccessWithMemberPrimary {
    shared static String attr {
        return "";
    }
    shared static void meth() {
    }
    shared static class Class() {
    }
    
    shared new () {
        staticSideEffect+=1;
    }
    shared StaticAccessWithMemberPrimary self {
        return this;
    }
    shared StaticAccessWithMemberPrimary selfWithSideEffect {
        staticSideEffect+=1;
        return this;
    }
}
@noanno
void staticAccessWithMemberPrimary() {
    value inst = StaticAccessWithMemberPrimary();
    staticSideEffect = 0;
    variable Object o = StaticAccessWithMemberPrimary().attr;
    assert(staticSideEffect == 1);
    o = inst.selfWithSideEffect.attr;
    assert(staticSideEffect == 2);
    o = inst.self.attr;
    assert(staticSideEffect == 2);
    
    staticSideEffect = 0;
    StaticAccessWithMemberPrimary().meth();
    assert(staticSideEffect == 1);
    inst.selfWithSideEffect.meth();
    assert(staticSideEffect == 2);
    StaticAccessWithMemberPrimary().meth{};
    assert(staticSideEffect == 3);
    inst.selfWithSideEffect.meth{};
    assert(staticSideEffect == 4);
    variable value ref = StaticAccessWithMemberPrimary().meth;
    assert(staticSideEffect == 5);
    ref();
    assert(staticSideEffect == 5);
    ref = inst.selfWithSideEffect.meth;
    assert(staticSideEffect == 6);
    ref();
    assert(staticSideEffect == 6);
    
    //StaticAccessWithMemberPrimary().Class();
    //StaticAccessWithMemberPrimary().Class{};
    //o = StaticAccessWithMemberPrimary().Class;
}