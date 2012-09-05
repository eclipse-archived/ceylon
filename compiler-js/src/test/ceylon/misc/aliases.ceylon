import assert { ... }

shared class AliasingClass() {
    shared interface AliasingIface {
        shared Boolean aliasingIface() { return true; }
    }
    shared class AliasingInner() {
        shared Boolean aliasingInner() { return true; }
    }
}

class AliasingSubclass() extends AliasingClass() {
    shared class InnerAlias() = AliasingInner;
    shared class SubAlias() extends InnerAlias() {}

    shared Boolean aliasingSubclass() {
        return SubAlias().aliasingInner();
    }
    shared interface AliasedIface = AliasingIface;
}

class AliasingSub2() extends AliasingSubclass() {
    shared AliasedIface iface {
        object aliased satisfies AliasedIface {
        }
        return aliased;
    }
}

void testAliasing() {
    print("testing type aliases");
    assert(AliasingSubclass().aliasingSubclass(), "Aliased member class");
    class InnerSubalias() = AliasingSubclass;
    assert(InnerSubalias().aliasingSubclass(), "Aliased top-level class");
    //TODO should this line even be valid?
    interface AliasedIface = AliasingClass.AliasingIface;
    Boolean use(AliasedIface aif) { return aif.aliasingIface(); }
    assert(use(AliasingSub2().iface), "Aliased member interface");
    
}
