@nomodel
class NamedArgumentSequencedTypeParamInvocation<T>() {
    shared void algo(T... strings) {}
}
@nomodel
void foo() {
    NamedArgumentSequencedTypeParamInvocation<String>().algo{"x", "y"};
}