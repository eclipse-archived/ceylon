import check { ... }

alias Strinteger = String|Integer;

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
    check(AliasingSubclass().aliasingSubclass(), "Aliased member class");
    class InnerSubalias() = AliasingSubclass;
    check(InnerSubalias().aliasingSubclass(), "Aliased top-level class");
    interface AliasedIface2 = AliasingClass.AliasingIface;
    Boolean use(AliasedIface2 aif) { return aif.aliasingIface(); }
    check(use(AliasingSub2().iface), "Aliased member interface");
    Strinteger xxxxx = 5;
    check(is Integer xxxxx, "Type alias");
}
