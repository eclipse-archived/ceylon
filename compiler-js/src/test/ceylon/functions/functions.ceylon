import assert { ... }

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
    shared actual Empty|Sequence<Element|Other> withLeading<Other>(Other... others) {
        value sb = SequenceBuilder<Element|Other>();
        sb.appendAll(others...);
        sb.appendAll(this...);
        return sb.sequence;
    }
    shared actual Empty|Sequence<Element|Other> withTrailing<Other>(Other... others) {
        value sb = SequenceBuilder<Element|Other>();
        sb.appendAll(this...);
        sb.appendAll(others...);
        return sb.sequence;
    }
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
    assert(tst(obj1.f), "Reference to method");
    assert(tst(obj2.defines), "Reference to method from ceylon.language");
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
    assert(defParamTest(1)=="1,2,3", "defaulted parameters 1");
    assert(defParamTest(1, 3)=="1,3,4", "defaulted parameters 2");
    assert(defParamTest(1, 3, 0)=="1,3,0", "defaulted parameters 3");
    assert(defParamTest{i1=1;}=="1,2,3", "defaulted parameters named 1");
    assert(defParamTest{i1=1;i2=3;}=="1,3,4", "defaulted parameters named 2");
    assert(defParamTest{i1=1;i3=0;}=="1,2,0", "defaulted parameters named 3");

    assert(DefParamTest1(1).s=="1,2,3", "defaulted parameters class 1");
    assert(DefParamTest1(1, 3).s=="1,3,4", "defaulted parameters class 2");    
    assert(DefParamTest1(1, 3, 0).s=="1,3,0", "defaulted parameters class 3");    
    assert(DefParamTest1{i1=1;}.s=="1,2,3", "defaulted parameters class named 1");
    assert(DefParamTest1{i1=1;i2=3;}.s=="1,3,4", "defaulted parameters class named 2");    
    assert(DefParamTest1{i1=1;i3=0;}.s=="1,2,0", "defaulted parameters class named 3");    

    assert(DefParamTest2(1).f()=="1,2,3", "defaulted parameters class2 1");
    assert(DefParamTest2(1, 3).f()=="1,3,4", "defaulted parameters class2 2");    
    assert(DefParamTest2(1, 3, 0).f()=="1,3,0", "defaulted parameters class2 3");    
    assert(DefParamTest2{i1=1;}.f()=="1,2,3", "defaulted parameters class2 named 1");
    assert(DefParamTest2{i1=1;i2=3;}.f()=="1,3,4", "defaulted parameters class2 named 2");    
    assert(DefParamTest2{i1=1;i3=0;}.f()=="1,2,0", "defaulted parameters class2 named 3");   
    
    value tst = DefParamTest3();
    assert(tst.f(1)=="1,2,3", "defaulted method parameters 1");
    assert(tst.f(1, 3)=="1,3,4", "defaulted method parameters 2");
    assert(tst.f(1, 3, 0)=="1,3,0", "defaulted method parameters 3");
    assert(tst.f{i1=1;}=="1,2,3", "defaulted method parameters named 1");
    assert(tst.f{i1=1;i2=3;}=="1,3,4", "defaulted method parameters named 2");
    assert(tst.f{i1=1;i3=0;}=="1,2,0", "defaulted method parameters named 3");
}

shared void testGetterMethodDefinitions() {
  class GetterTest() {
    variable Integer i:=0;
    shared Integer x { return ++i; }
  }
  value gt = GetterTest();
  assert(gt.x==1, "getter defined as method 1");
  assert(gt.x==2, "getter defined as method 2");
  assert(gt.x==3, "getter defined as method 3");
}

String namedArgFunc(String x="x", String y=x+"y", String... z) {
    variable String result := x + "," + y;
    for (s in z) { result += "," + s; }
    return result;
}

void testNamedArguments() {
    assert(namedArgFunc{}=="x,xy", "named arguments 1");
    assert(namedArgFunc{x="a";}=="a,ay", "named arguments 2");
    assert(namedArgFunc{y="b";}=="x,b", "named arguments 3");
    assert(namedArgFunc{"c"}=="x,xy,c", "named arguments 4");
    assert(namedArgFunc{x="a";y="b";"c"}=="a,b,c", "named arguments 5");
    assert(namedArgFunc{y="b";x="a";"c","d"}=="a,b,c,d", "named arguments 6");
    assert(namedArgFunc{x="a";"c"}=="a,ay,c", "named arguments 7");
    assert(namedArgFunc{y="b";"c"}=="x,b,c", "named arguments 8");
    assert(namedArgFunc{y="b";x="a";}=="a,b", "named arguments 9");
    assert(namedArgFunc{z={};}=="x,xy", "named arguments 10");
    assert(namedArgFunc{z={"c", "d"};}=="x,xy,c,d", "named arguments 11");
    assert(namedArgFunc{y="b";z={"c"};x="a";}=="a,b,c", "named arguments 12");
}

shared void test() {
    helloWorld();
    hello("test");
    helloAll("Gavin", "Enrique", "Ivo");
    assert(toString(5)=="5", "toString(obj)");
    assert(add(1.5, 2.5)==4.0,"add(Float,Float)");
    //repeat(5, void p(Integer x) { print(x); });
    testMethodReference();
    testDefaultedParams();
    testGetterMethodDefinitions();
    testMultipleParamLists();
    testAnonymous();
    testNamedArguments();
    results();
}
