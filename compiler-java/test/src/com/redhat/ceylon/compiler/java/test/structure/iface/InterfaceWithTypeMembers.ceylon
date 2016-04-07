interface InterfaceWithTypeMembers {
    class C() {
        shared void m() {
            outer.m();
        }
    }
    shared interface I {
        shared default void m() {
            outer.m();
        }
    }
    class CSatisfiesI() satisfies I {
        shared actual void m() {
            super.m();
            outer.m();
        }
    }
    
    void m() {
        CSatisfiesI().m();
        C c = C();
        c.m();
    }
}/*
interface X satisfies InterfaceWithTypeMembers {
    
    class Inter() {
        interface Foo {}
    
        class C() satisfies InterfaceWithTypeMembers.I&Foo {
            
        }
    }
    
}*/