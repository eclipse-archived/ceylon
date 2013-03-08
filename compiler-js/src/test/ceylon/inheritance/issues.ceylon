import check {check}

interface Issue150 {
    shared formal String f(Integer i=100);
}
class C150(String arg(Integer i)) satisfies Issue150 {
    f = arg;
}
void testIssues() {
    check(C150((Integer i) => "i=``i``").f()=="i=100", "issue 150");
}

