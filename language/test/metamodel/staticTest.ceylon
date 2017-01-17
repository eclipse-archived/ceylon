import ceylon.language.meta.model{
    IncompatibleTypeException,
    TypeApplicationException,
    Interface,
    Value,
    InvocationException
}
import ceylon.language.meta.declaration{
    AliasDeclaration,
    OpenInterfaceType
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
        assert (e.message == "Cannot apply a static declaration with no container type: use staticApply");
    } 
    assert(is String staticResult = `function StaticMembers.method`.staticInvoke(`StaticMembers<String>`, [`Integer`], *["", 1]),
        staticResult == "");
    assert(is String memberResult = `function StaticMembers.method`.memberInvoke(StaticMembers<String>(), [`Integer`], *["", 1]),
        memberResult == "");
    
    
    assert(`class StaticMembers.MemberClass`.name == "MemberClass");
    assert(`class StaticMembers.MemberClass`.static);
    try {
        `class StaticMembers.MemberClass`.instantiate();
        assert(false);
    } catch (TypeApplicationException e) {
        assert(e.message == "Cannot apply a static declaration with no container type: use staticApply");
    } 
    assert(is StaticMembers<String>.MemberClass<Integer> staticMemberResult = `class StaticMembers.MemberClass`.staticInstantiate(`StaticMembers<String>`, [`Integer`], *["", 1]),
        staticMemberResult.attribute == "");
    assert(is StaticMembers<String>.MemberClass<Integer> memberMemberResult = `class StaticMembers.MemberClass`.memberInstantiate(StaticMembers<String>(), [`Integer`], *["", 1]),
        memberMemberResult.attribute == "");
    
    
    // TODO interfaces
    
    
    // alias
    assert(is AliasDeclaration staticAliasDeclaration = `alias StaticMembers.Alias`);
    assert(is OpenInterfaceType a = `alias StaticMembers.Alias`.extendedType);
    //assert(is Interface<Set<String>> staticAlias = `StaticMembers<String>.Alias`);


    // static object
    assert(`value StaticAnon.anon`.static);
    try {
        `value StaticAnon.anon`.get();
        assert(false);
    } catch (TypeApplicationException e) {
        assert(e.message == "Cannot apply a static declaration: use staticApply"); 
    }
    assert(exists staticallyGotAnon = `value StaticAnon.anon`.staticGet(`StaticAnon`),
        staticallyGotAnon == StaticAnon.anon );
    assert(exists memberlyGotAnon = `value StaticAnon.anon`.memberGet(StaticAnon()),
        memberlyGotAnon == StaticAnon.anon);
    
    assert(`class StaticAnon.anon`.static);
    try {
        `class StaticAnon.anon`.instantiate();
        assert(false);
    } catch (TypeApplicationException e) {
        assert(e.message == "Cannot apply a static declaration with no container type: use staticApply");
    }
    try {
        `class StaticAnon.anon`.staticInstantiate(`StaticAnon`);
        assert(false);
    } catch (InvocationException e) {
        assert(e.message == "Object class cannot be instantiated");
    }
}