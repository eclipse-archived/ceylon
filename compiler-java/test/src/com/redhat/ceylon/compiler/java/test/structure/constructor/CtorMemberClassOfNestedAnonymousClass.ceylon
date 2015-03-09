@noanno
class CtorMemberClassOfNestedAnonymousClass(){
    shared object memberObject {
        shared class Ctor {
            shared new Ctor() {}
            shared new Other() {}
        }
    }
}
@noanno
CtorMemberClassOfNestedAnonymousClass.\ImemberObject.Ctor ctorMemberClassOfNestedAnonymousClass 
        = CtorMemberClassOfNestedAnonymousClass().memberObject.Ctor.Other();