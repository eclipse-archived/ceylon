import check { ... }

shared void helloWorld() {
    print("hello world");
}

shared void hello(String name) {
    print("hello" + name);
}

shared void helloAll(String* names) {}

shared String toString(Object obj) {
    return obj.string;
}

shared Float add(Float x, Float y) {
    return x+y;
}

shared void repeat(Integer times, void f(Integer iter)) {
    f(0);
}

class MySequence<out Element>(Sequence<Element> seq)
            satisfies Sequence<Element> {
    shared actual Integer lastIndex { return seq.lastIndex; }
    shared actual Element first { return seq.first; }
    shared actual Element[] rest { return seq.rest; }
    shared actual Element? elementAt(Integer index) { return seq[index]; }
    shared actual Element[] span(Integer from, Integer to) { return seq.span(from, to); }
    shared actual Element[] spanFrom(Integer from) { return seq.spanFrom(from); }
    shared actual Element[] spanTo(Integer to) { return seq.spanTo(to); }
    shared actual Element[] segment(Integer from, Integer length) { return seq.segment(from, length); }
    shared actual String string { return seq.string; }
    shared actual Integer hash { return seq.hash; }
    shared actual Boolean equals(Object other) { return seq.equals(other); }
    shared actual Sequence<Element> reversed { return seq.reversed; }
    shared actual Element last => seq.last;
    shared actual Iterator<Element> iterator() => seq.iterator();
    shared actual Integer size => seq.size;
    shared actual Boolean contains(Object other) => seq.contains(other);
}

class RefHelper() {
    shared Boolean f(Integer i) { return true; }
}

void testMethodReference() {
    value obj1 = RefHelper();
    value obj2 = MySequence<String>(["hi"]);
    Boolean tst(Boolean x(Integer i)) {
        return x(0);
    }
    check(tst(obj1.f), "Reference to method");
    check(tst(obj2.defines), "Reference to method from ceylon.language");
}

String defParamTest(Integer i1, Integer i2=i1+1, Integer i3=i1+i2) {
    return "``i1``,``i2``,``i3``";
}
class DefParamTest1(Integer i1, Integer i2=i1+1, Integer i3=i1+i2) {
    shared String s = "``i1``,``i2``,``i3``";
}
class DefParamTest2(Integer i1, Integer i2=i1+1, Integer i3=i1+i2) {
    shared String f() { return "``i1``,``i2``,``i3``"; }
}
class DefParamTest3() {
    shared String f(Integer i1, Integer i2=i1+1, Integer i3=i1+i2) {
        return "``i1``,``i2``,``i3``";
    }
}
void testDefaultedParams() {
    check(defParamTest(1)=="1,2,3", "defaulted parameters 1");
    check(defParamTest(1, 3)=="1,3,4", "defaulted parameters 2");
    check(defParamTest(1, 3, 0)=="1,3,0", "defaulted parameters 3");
    check(defParamTest{i1=1;}=="1,2,3", "defaulted parameters named 1");
    check(defParamTest{i1=1;i2=3;}=="1,3,4", "defaulted parameters named 2");
    check(defParamTest{i1=1;i3=0;}=="1,2,0", "defaulted parameters named 3");

    check(DefParamTest1(1).s=="1,2,3", "defaulted parameters class 1");
    check(DefParamTest1(1, 3).s=="1,3,4", "defaulted parameters class 2");    
    check(DefParamTest1(1, 3, 0).s=="1,3,0", "defaulted parameters class 3");    
    check(DefParamTest1{i1=1;}.s=="1,2,3", "defaulted parameters class named 1");
    check(DefParamTest1{i1=1;i2=3;}.s=="1,3,4", "defaulted parameters class named 2");    
    check(DefParamTest1{i1=1;i3=0;}.s=="1,2,0", "defaulted parameters class named 3");    

    check(DefParamTest2(1).f()=="1,2,3", "defaulted parameters class2 1");
    check(DefParamTest2(1, 3).f()=="1,3,4", "defaulted parameters class2 2");    
    check(DefParamTest2(1, 3, 0).f()=="1,3,0", "defaulted parameters class2 3");    
    check(DefParamTest2{i1=1;}.f()=="1,2,3", "defaulted parameters class2 named 1");
    check(DefParamTest2{i1=1;i2=3;}.f()=="1,3,4", "defaulted parameters class2 named 2");    
    check(DefParamTest2{i1=1;i3=0;}.f()=="1,2,0", "defaulted parameters class2 named 3");   
    
    value tst = DefParamTest3();
    check(tst.f(1)=="1,2,3", "defaulted method parameters 1");
    check(tst.f(1, 3)=="1,3,4", "defaulted method parameters 2");
    check(tst.f(1, 3, 0)=="1,3,0", "defaulted method parameters 3");
    check(tst.f{i1=1;}=="1,2,3", "defaulted method parameters named 1");
    check(tst.f{i1=1;i2=3;}=="1,3,4", "defaulted method parameters named 2");
    check(tst.f{i1=1;i3=0;}=="1,2,0", "defaulted method parameters named 3");
}

shared void testGetterMethodDefinitions() {
  class GetterTest() {
    variable Integer i=0;
    shared Integer x { return ++i; }
  }
  value gt = GetterTest();
  check(gt.x==1, "getter defined as method 1");
  check(gt.x==2, "getter defined as method 2");
  check(gt.x==3, "getter defined as method 3");
}

String namedArgFunc(String x="x", String y=x+"y", String* z) {
    variable String result = x + "," + y;
    for (s in z) { result += "," + s; }
    return result;
}

class Issue105(i, Issue105* more) {
    shared Integer i;
}

void testNamedArguments() {
    check(namedArgFunc{}=="x,xy", "named arguments 1");
    check(namedArgFunc{x="a";}=="a,ay", "named arguments 2");
    check(namedArgFunc{y="b";}=="x,b", "named arguments 3");
    check(namedArgFunc{z=["c"];}=="x,xy,c", "named arguments 4");
    check(namedArgFunc{x="a";y="b";z=["c"];}=="a,b,c", "named arguments 5");
    check(namedArgFunc{y="b";x="a";z=["c","d"];}=="a,b,c,d", "named arguments 6");
    check(namedArgFunc{x="a";z=["c"];}=="a,ay,c", "named arguments 7");
    check(namedArgFunc{y="b";z=["c"];}=="x,b,c", "named arguments 8");
    check(namedArgFunc{y="b";x="a";}=="a,b", "named arguments 9");
    //doesn't apply anymore check(namedArgFunc{z={};}=="x,xy", "named arguments 10");
    check(namedArgFunc{z=["c","d"];}=="x,xy,c,d", "named arguments 11");
    check(namedArgFunc{y="b";z=["c"];x="a";}=="a,b,c", "named arguments 12");
    
    value issue105 = Issue105 { i=1; more=[Issue105 { i=2; }]; };
    check(issue105.i==1, "issue #105");
}

interface LazyExprBase {
    shared formal String s1;
    shared formal String s2(Integer i);
}
class LazyExprTest() satisfies LazyExprBase {
    shared variable Integer x = 1000;
    shared String f1(Integer i, String f() => "``i``.``(x+1)``") => "``i``:``f()``"; //TODO was ++x
    shared Integer f2(Integer i) => 2*(++x)+i;
    shared Integer i1 => ++x;
    shared Integer i2;
    i2 => ++x*2;
    s1 => "``(++x)``.1";
    s2(Integer i) => "``(++x)``.``i``";
    
    shared String f3(String f(Integer i)) => f(++x);
}

variable Integer lx = 1000;
String lazy_f1(Integer i, String f() => "``i``.``(lx+1)``") => f(); //TODO was ++lx
Integer lazy_f2(Integer i) => 2*(++lx)+i;
Integer lazy_i1 => ++lx;

class LazyExprTest2() satisfies LazyExprBase {
    shared variable Integer x= 1000;
    shared actual default String s1 => (++x).string;
    shared actual default String s2(Integer i) => "``++x``-``i``";
}
class LazyExprTest3() extends LazyExprTest2() {
    shared actual variable String s1 = "s1";
}
class LazyExprTest4() extends LazyExprTest2() {
    shared variable String assigned = "";
    shared actual String s1 { return "s1-``super.s1``"; }
    assign s1 { assigned = s1; }
}

void testLazyExpressions() {
    value tst = LazyExprTest();
    tst.x = 1;
    check(tst.f1(3)=="3:3.2", "=> defaulted param");
    check(tst.f2(3)==7, "=> method");
    check(tst.i1==3, "=> attribute");
    check(tst.i2==8, "=> attribute specifier");
    check(tst.s1=="5.1", "=> attribute refinement");
    check(tst.s2(5)=="6.5", "=> method refinement");
    
    lx = 1;
    check(lazy_f1(3)=="3.2", "=> defaulted param toplevel");
    check(lazy_f2(3)==7, "=> method toplevel");
    check(lazy_i1==3, "=> attribute toplevel");
    
    variable Integer x = 1000;
    String f1(Integer i, String f() => "``i``.``(x+1)``") => f(); //TODO was ++x
    Integer f2(Integer i) => 2*(++x)+i;
    Integer i1 => ++x;
    Integer i2;
    i2 => ++x*2;
    
    x = 1;
    check(f1(3)=="3.2", "=> defaulted param local");
    check(f2(3)==7, "=> method local");
    check(i1==3, "=> attribute local");
    check(i2==8, "=> attribute specifier local");

    value tst3 = LazyExprTest3();
    tst3.x = 1;
    check(tst3.s1=="s1", "=> override variable 1");
    tst3.s1 = "abc";
    check(tst3.s1=="abc", "=> override variable 2");
    value tst4 = LazyExprTest4();
    tst4.x = 1;
    check(tst4.s1=="s1-2", "=> override getter/setter 1");
    tst4.s1 = "abc";
    check(tst4.s1=="s1-4", "=> override getter/setter 2");
    check(tst4.assigned=="abc", "=> override getter/setter 3");
    
    tst.x = 1;
    x = 10;
    check(tst.f1{i=>++x;}=="11:11.2", "=> named arg");
    check(tst.f1{i=>++x; f()=>(++x).string;}=="12:13", "=> named arg function");
    check(tst.f3{f(Integer i)=>"``i``-``++x``";}=="2-14", "=> named arg function with param");
}

shared void test() {
    helloWorld();
    hello("test");
    helloAll("Gavin", "Enrique", "Ivo");
    check(toString(5)=="5", "toString(obj)");
    check(add(1.5, 2.5)==4.0,"add(Float,Float)");
    //repeat(5, void p(Integer x) { print(x); });
    testMethodReference();
    testDefaultedParams();
    testGetterMethodDefinitions();
    testMultipleParamLists();
    testAnonymous();
    testNamedArguments();
    testLazyExpressions();
    testStaticMethodReferences();
    results();
}
