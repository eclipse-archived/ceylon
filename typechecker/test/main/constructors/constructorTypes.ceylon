import ceylon.language.meta.model { MemberClassConstructor }

class ClassContainer() {
    
    shared object memberObject {
        shared class Ctor {
            shared new Ctor() {}
            shared new Other() {}
        }
    }
    
    shared void test(ClassContainer other){
        MemberClassConstructor<\ImemberObject,\ImemberObject.Ctor,[]> ctorCtor 
                = `\ImemberObject.Ctor.Ctor`;
        print(ctorCtor(other.memberObject)());
        MemberClassConstructor<\ImemberObject,\ImemberObject.Ctor,[]> ctorOther
                = `\ImemberObject.Ctor.Other`;
        print(ctorOther(other.memberObject)());
        @error:"constructor is not a type" 
        MemberClassConstructor<\ImemberObject,\ImemberObject.Ctor.Ctor,[]> ctorCtor2;
        @error:"constructor is not a type" 
        MemberClassConstructor<\ImemberObject,\ImemberObject.Ctor.Other,[]> ctorOther2;
    }
    
}