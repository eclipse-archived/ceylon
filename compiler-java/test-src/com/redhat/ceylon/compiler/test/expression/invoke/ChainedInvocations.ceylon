@nomodel
class ChainedInvocations(){
    Natural m() {
        return ChainedInvocations().foo();
    }
    Natural foo() {
        return 1;
    }
}
