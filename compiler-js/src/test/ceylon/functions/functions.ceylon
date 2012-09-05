import check { ... }

shared void helloWorld() {
    print("hello world");
}

shared void hello(String name) {
    print("hello" + name);
}

shared void helloAll(String... names) {}

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
    shared actual Element? item(Integer index) { return seq[index]; }
    shared actual Element[] span(Integer from, Integer? to) { return seq.span(from, to); }
    shared actual Element[] segment(Integer from, Integer length) { return seq.segment(from, length); }
    shared actual MySequence<Element> clone { return this; }
    shared actual String string { return seq.string; }
    shared actual Integer hash { return seq.hash; }
    shared actual Boolean equals(Object other) { return seq.equals(other); }
    shared actual Sequence<Element> reversed { return seq.reversed; }
}

class RefHelper() {
    shared Boolean f(Integer i) { return true; }
}

void testMethodReference() {
    value obj1 = RefHelper();
    value obj2 = MySequence<String>({"hi"});
    Boolean tst(Boolean x(Integer i)) {
        return x(0);
    }
    check(tst(obj1.f), "Reference to method");
    check(tst(obj2.defines), "Reference to method from ceylon.language");
}

String defParamTest(Integer i1, Integer i2=i1+1, Integer i3=i1+i2) {
    return "" i1 "," i2 "," i3 "";
}
class DefParamTest1(Integer i1, Integer i2=i1+1, Integer i3=i1+i2) {
    shared String s = "" i1 "," i2 "," i3 "";
}
class DefParamTest2(Integer i1, Integer i2=i1+1, Integer i3=i1+i2) {
    shared String f() { return "" i1 "," i2 "," i3 ""; }
}
class DefParamTest3() {
    shared String f(Integer i1, Integer i2=i1+1, Integer i3=i1+i2) {
        return "" i1 "," i2 "," i3 "";
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
    variable Integer i:=0;
    shared Integer x { return ++i; }
  }
  value gt = GetterTest();
  check(gt.x==1, "getter defined as method 1");
  check(gt.x==2, "getter defined as method 2");
  check(gt.x==3, "getter defined as method 3");
}

String namedArgFunc(String x="x", String y=x+"y", String... z) {
    variable String result := x + "," + y;
    for (s in z) { result += "," + s; }
    return result;
}

class Issue105(i, Issue105... more) {
    shared Integer i;
}

void testNamedArguments() {
    check(namedArgFunc{}=="x,xy", "named arguments 1");
    check(namedArgFunc{x="a";}=="a,ay", "named arguments 2");
    check(namedArgFunc{y="b";}=="x,b", "named arguments 3");
    check(namedArgFunc{"c"}=="x,xy,c", "named arguments 4");
    check(namedArgFunc{x="a";y="b";"c"}=="a,b,c", "named arguments 5");
    check(namedArgFunc{y="b";x="a";"c","d"}=="a,b,c,d", "named arguments 6");
    check(namedArgFunc{x="a";"c"}=="a,ay,c", "named arguments 7");
    check(namedArgFunc{y="b";"c"}=="x,b,c", "named arguments 8");
    check(namedArgFunc{y="b";x="a";}=="a,b", "named arguments 9");
    check(namedArgFunc{z={};}=="x,xy", "named arguments 10");
    check(namedArgFunc{z={"c", "d"};}=="x,xy,c,d", "named arguments 11");
    check(namedArgFunc{y="b";z={"c"};x="a";}=="a,b,c", "named arguments 12");
    
    value issue105 = Issue105 { i=1; Issue105 { i=2; } };
    check(issue105.i==1, "issue #105");
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
    results();
}
