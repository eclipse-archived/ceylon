import check {check}

interface Issue150 {
    shared formal String f(Integer i=100);
}
class C150(String arg(Integer i)) satisfies Issue150 {
    f = arg;
}
void testIssues() {
    check(C150((Integer i) => "i=``i``").f()=="i=100", "issue 150");
    check(Issue231_1("Hola").string == "Hola", "Issue 231 [1]");
    check(Issue231_2("Hola").string == "Hola", "Issue 231 [2]");
    Object i266 = Issue266();
    check(i266 is Issue266i<String|Integer>, "Issue 266 [1]");
    check(!i266 is Issue266i<String>, "Issue 266 [2]");
    Object i266_2 = Issue266_2();
    check(!i266_2 is Issue266i<String|Integer>, "Issue 266 [3]");
    check(i266_2 is Issue266i<String>, "Issue 266 [4]");
    Object i266_3 = Issue266_3();
    check(!i266_3 is Issue266i<Issue266i<String>&Issue266two>, "Issue 266 [5]");
    Object i266_4 = Issue266_4();
    check(!i266_4 is Issue266i<Issue266three>, "Issue 266 [6]");
}

class Issue231_1(shared actual String string) {}
class Issue231_2(string) {
    shared actual String string;
}

interface Issue266i<Parm> {}
class Issue266() satisfies Issue266i<String|Integer>{}
class Issue266_2() satisfies Issue266i<String>{}
interface Issue266two{}
interface Issue266three satisfies Issue266i<String>&Issue266two{}
class Issue266_3() satisfies Issue266i<Issue266three> {}
class Issue266_4() satisfies Issue266i<Issue266i<String>&Issue266two> {}
