class AvoidBackwardBranchWithSuper<T>(Integer i, T t, Integer[] iseq) {
}
class AvoidBackwardBranchWithSuper_sub(Character[] cs) extends AvoidBackwardBranchWithSuper<Integer>(1, 2, cs*.integer) {
}
class AvoidBackwardBranchWithSuper_sub2<S>(Character[] cs, S s) extends AvoidBackwardBranchWithSuper<S>(1, s, cs*.integer) {
}
void avoidBackwardBranchWithSuper_run() {
    AvoidBackwardBranchWithSuper_sub([]);
    AvoidBackwardBranchWithSuper_sub2<Integer>([], 2);
}
