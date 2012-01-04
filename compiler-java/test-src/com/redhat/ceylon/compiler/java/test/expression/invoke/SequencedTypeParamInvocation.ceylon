@nomodel
class SequencedTypeParamInvocation<T>() {
    shared void algo(T... strings) {}
}
@nomodel
void foo() {
	SequencedTypeParamInvocation<String>().algo("x", "y");
}