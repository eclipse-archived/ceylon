import ceylon.language.meta.model { MemberClassConstructor }

class ClassContainer() {
    
    shared object memberObject {
        shared class Ctor {
            shared new ctor() {}
            shared new other() {}
        }
    }
    
    shared void test(ClassContainer other){
        MemberClassConstructor<\ImemberObject,\ImemberObject.Ctor,[]> ctorCtor 
                = `\ImemberObject.Ctor.\Ictor`;
        print(ctorCtor(other.memberObject)());
        MemberClassConstructor<\ImemberObject,\ImemberObject.Ctor,[]> ctorOther
                = `\ImemberObject.Ctor.\Iother`;
        print(ctorOther(other.memberObject)());
        @error:"constructor is not a type" 
        MemberClassConstructor<\ImemberObject,\ImemberObject.Ctor.\Ictor,[]> ctorCtor2;
        @error:"constructor is not a type" 
        MemberClassConstructor<\ImemberObject,\ImemberObject.Ctor.\Iother,[]> ctorOther2;
    }
    
}