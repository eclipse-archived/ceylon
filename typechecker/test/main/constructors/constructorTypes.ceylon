import ceylon.language.meta.model { MemberClassCallableConstructor }

class ClassContainer() {
    
    shared object memberObject {
        shared class Ctor {
            shared new ctor() {}
            shared new other() {}
        }
    }
    
    shared void test(ClassContainer other){
        MemberClassCallableConstructor<\ImemberObject,\ImemberObject.Ctor,[]> ctorCtor 
                = `\ImemberObject.Ctor.ctor`;
        print(ctorCtor(other.memberObject)());
        MemberClassCallableConstructor<\ImemberObject,\ImemberObject.Ctor,[]> ctorOther
                = `\ImemberObject.Ctor.other`;
        print(ctorOther(other.memberObject)());
        $error:"constructor is not a type" 
        MemberClassCallableConstructor<\ImemberObject,\ImemberObject.Ctor.\Ictor,[]> ctorCtor2;
        $error:"constructor is not a type" 
        MemberClassCallableConstructor<\ImemberObject,\ImemberObject.Ctor.\Iother,[]> ctorOther2;
        
        $type:"ClassContainer.memberObject.Ctor" value good = memberObject.Ctor.ctor();
        $error:"constructor is not a type" value bad = memberObject.Ctor.\Ictor();
    }
    
}

class ClassWithUpperNameConstructors {
    shared new \iValue {}
    shared new \iFunction() {}
}

void testClassWithUpperNameConstructors() {
    ClassWithUpperNameConstructors bar1;
    ClassWithUpperNameConstructors baz1;
    bar1 = ClassWithUpperNameConstructors.\iValue;
    baz1 = ClassWithUpperNameConstructors.\iFunction();
    
    $error:"constructor is not a type" 
    ClassWithUpperNameConstructors.Value bar2;
    $error:"constructor is not a type" 
    ClassWithUpperNameConstructors.Function baz2;
    $error:"constructor is not a type" 
    bar2 = ClassWithUpperNameConstructors.Value;
    $error:"constructor is not a type" 
    baz2 = ClassWithUpperNameConstructors.Function();
}
