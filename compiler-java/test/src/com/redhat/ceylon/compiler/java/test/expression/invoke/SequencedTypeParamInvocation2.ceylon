@nomodel
void sequencedTypeParamInvocation2<T>(T... ts) {}
@nomodel
void sequencedTypeParamInvocation2_test() {
    sequencedTypeParamInvocation2{1, 2};
}