Object[] avoidBackwardBranchWithVarargs(Object* v) {
    return v;
}
void avoidBackwardBranchWithVarargs_1() {
    value s = "Hello World".characters;
    value x = " ".join(s*.lowercase.string);
}
void avoidBackwardBranchWithVarargs_2(Character[] s={}) {
    avoidBackwardBranchWithVarargs(s*.integer);
}
void avoidBackwardBranchWithVarargs_3(List<Character> s={}) {
    avoidBackwardBranchWithVarargs(s*.integer);
}
void avoidBackwardBranchWithVarargs_4(List<Character> s={}) {
    avoidBackwardBranchWithVarargs(s*.integer*.string);
}
void avoidBackwardBranchWithVarargs_5(List<Character> s={}) {
    avoidBackwardBranchWithVarargs(avoidBackwardBranchWithVarargs(s*.integer)*.string);
}
void avoidBackwardBranchWithVarargs_run() {
    avoidBackwardBranchWithVarargs_1();
    avoidBackwardBranchWithVarargs_2();
    avoidBackwardBranchWithVarargs_3();
    avoidBackwardBranchWithVarargs_4();
    avoidBackwardBranchWithVarargs_5();
}