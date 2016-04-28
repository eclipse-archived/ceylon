@compileUsing:"companion"
interface SuperToCompanion1 {
    shared default void m() {}
}
interface SuperToCompanion2 satisfies SuperToCompanion1 {
    
}
interface SuperToCompanion3 satisfies SuperToCompanion2 {
    void f() {
        super.m();
    }
}