interface SuperWithinSyntheticClass {
    shared default Integer m() => 0; 
}
interface SuperWithinSyntheticClass2 satisfies SuperWithinSyntheticClass {
    void f() {
        value ref = super.m;
        value it = { super.m() };
        value com = { for (x in [super.m()]) x };
    }
}