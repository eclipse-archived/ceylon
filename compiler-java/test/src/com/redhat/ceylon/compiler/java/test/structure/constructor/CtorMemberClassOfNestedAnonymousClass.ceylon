@noanno
class CtorMemberClassOfNestedAnonymousClass(){
    shared object memberObject {
        shared class Ctor {
            shared new () {}
            shared new other() {}
        }
    }
}
@noanno
CtorMemberClassOfNestedAnonymousClass.\ImemberObject.Ctor ctorMemberClassOfNestedAnonymousClass 
        = CtorMemberClassOfNestedAnonymousClass().memberObject.Ctor.other();