serializable shared class ImplicitCallableFunctionalParamCaptured(String f()) {
    shared actual String string => f();
}
serializable shared class ImplicitCallableFunctionalParamShared(shared void f()) {
    
}
serializable shared class ImplicitCallableFunctionalInitializerParameter(f) {
    shared void f();
}