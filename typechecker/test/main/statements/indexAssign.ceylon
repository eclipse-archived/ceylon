
void test(IndexedCorrespondenceMutator<String> cmi,
    KeyedCorrespondenceMutator<Integer, String> cmk, 
    CorrespondenceMutator<String> cm,
    Correspondence<Integer, String> c) {
    cmi[3] = "baz";
    $error cmi[3] = null;
    $error cmi[3] = 1;
    $error cmi[1..3] = "baz";

    cmk[3] = "baz";
    $error cmk[3] = null;
    $error cmk[3] = 1;
    $error cmk[1..3] = "baz";

    $error cm[3] = "baz";
    
    $error c[3] = "baz";
}
