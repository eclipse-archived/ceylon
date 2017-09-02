class SuperA() {
    shared default class B() {}
}
class SubA() extends SuperA() {
    shared actual class B() extends super.B() {}
}

class UsesVirtualType<AType>() 
        given AType satisfies SuperA {
    $error AType.B bThing = nothing;
    
    alias AAlias => AType;
    $error AAlias.B bThingAliased = nothing;
}

class UsesVirtualTypeInGiven<AType, BType>()
        given AType satisfies SuperA
        $error given BType satisfies AAlias.B {
    alias AAlias => AType;
}
