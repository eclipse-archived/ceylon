interface SuperWithinSyntheticClass {
    shared default Integer m() => 0; 
}
interface SuperWithinSyntheticClass2 satisfies SuperWithinSyntheticClass {
    void f() {
        value ref = m;
        value it = { m() };
        value com = { for (x in [m()]) x };
        
        value ref2 = this.m;
        value it2 = { this.m() };
        value com2 = { for (x in [this.m()]) x };
        
        value ref3 = super.m;
        value it3 = { super.m() };
        value com3 = { for (x in [super.m()]) x };
    }
    shared formal Integer next();
    shared formal Integer i;
    void m(){
        value l = {for(i in {1}) next() + this.i};
    }
}
interface ComprehensionMemberQualInterfaceNonshared {
    Integer next() => 1;
    Integer i => 1;
    void m(){
        value l = {for(i in {1}) next() + this.i};
    }
}