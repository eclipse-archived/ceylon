@nomodel
class SequencedTypeParamInvocation<T>() {
    shared void algo(T... strings) {}
}
@nomodel
void sequencedTypeParamInvocationMethod() {
    SequencedTypeParamInvocation<String>().algo("x", "y");
}