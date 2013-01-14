class AvoidBackwardBranchWithNew(Object[] arr){
    class Inner(Object[] arr) {
    }
    shared void test(){
        value arr = [1];
        value o = AvoidBackwardBranchWithNew(arr*.successor);
        o.Inner(arr*.successor);
    }
}
void avoidBackwardBranchWithNew_run() {
    // we don't actually have to run it, we just need to trigger bytecode 
    // verification
    AvoidBackwardBranchWithNew({}).test();
}
