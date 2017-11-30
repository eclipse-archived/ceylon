@noanno
class CtorDelegationWithGenericSuperBar<T>(){}
@noanno
class CtorDelegationWithGenericSuperFoo<T,U> extends CtorDelegationWithGenericSuperBar<Boolean>{
    
    shared new init() extends CtorDelegationWithGenericSuperBar<Boolean>(){}
    
    shared new () extends init(){}
    
}