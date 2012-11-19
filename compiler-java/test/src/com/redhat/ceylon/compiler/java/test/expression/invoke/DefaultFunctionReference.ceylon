@nomodel
void defaultFunctionReference(void f(Object o)=>print(o)) {
    f(1);
}
void defaultFunctionReference2(String s="hi", void f(Object... o)=>print(s + o.string)) {
    f(1);
}
@nomodel
void defaultFunctionReference_call() {
    defaultFunctionReference();
    defaultFunctionReference2();
}