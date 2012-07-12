@nomodel
void defaultFunctionReference(void f(Object o)=print) {
    f(1);
}
@nomodel
void defaultFunctionReference_call() {
    defaultFunctionReference();
}