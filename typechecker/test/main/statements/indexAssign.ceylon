
void test(CorrespondenceMutator<Integer, String> cm, Correspondence<Integer, String> c) {
    cm[3] = "baz";
    @error cm[3] = null;
    @error cm[3] = 1;
    @error cm[1..3] = "baz";
    @error c[3] = "baz";
}
