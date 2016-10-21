import ceylon.language.meta.model{
    IncompatibleTypeException,
    TypeApplicationException
} 

@test
shared void staticTest() {
    assert(`value StaticMembers.attribute`.name == "attribute");
    assert(`value StaticMembers.attribute`.static);
    try {
        `value StaticMembers.attribute`.get();
        assert(false);
    } catch (TypeApplicationException e) {
        assert(e.message == "Cannot apply a static declaration: use staticApply"); 
    }
    assert(is String staticallyGot = `value StaticMembers.attribute`.staticGet(`StaticMembers<Anything>`),
        staticallyGot == "");
    // Should we also support this?
    assert(is String memberlyGot = `value StaticMembers.attribute`.memberGet(StaticMembers<Anything>()),
        memberlyGot == "");
    
    
    assert(`function StaticMembers.method`.name == "method");
    assert(`function StaticMembers.method`.static);
    try {
        `function StaticMembers.method`.invoke();
        assert(false);
    } catch (TypeApplicationException e) {
        // TODO
    } 
    assert(is String staticResult = `function StaticMembers.method`.staticInvoke(`StaticMembers<String>`, [`Integer`], *["", 1]),
        staticResult == "");
    assert(is String memberResult = `function StaticMembers.method`.memberInvoke(StaticMembers<String>(), [`Integer`], *["", 1]),
        memberResult == "");
    
    
    assert(`class StaticMembers.MemberClass`.name == "MemberClass");
    assert(`class StaticMembers.MemberClass`.static);
    //assert(`value StaticMembers.attribute`.invoke());
    // TODO interfaces
    // TODO aliases
    // TODO static object
}