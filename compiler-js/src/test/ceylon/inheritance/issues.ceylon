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
    //351
    Object i1={1,null};
    Object i2={1,2};
    assert(exists e1={1,null}.indexed.first);
    Object e2=e1; //Integer?&Object
    check(e2 is Integer->Integer?&Object, "#351.1");
    check(i2 is {Integer?*}, "#351.2");
    check(!i1 is {Integer*}, "#351.3");
    check(MyList360().string=="{ 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95, 96, 97, 98, 99, 100 }", "Issue 360 #1");
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

class MyList360() extends Object() satisfies List<String> {
    getFromFirst(Integer index) => index.string;
    lastIndex => 100;
    segment(Integer from, Integer length) => nothing;
    span(Integer from, Integer to) => nothing;
    spanFrom(Integer from) => nothing;
    spanTo(Integer to) => nothing;
    clone() => this;
}
