@nomodel
void sequencedTypeParamInvocation2<T>(T... ts) {}
@nomodel
void sequencedTypeParamInvocation2_test() {
    sequencedTypeParamInvocation2{ts = 1; ts = 2;};
}