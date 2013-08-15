import check { check,fail,results }

import ceylon.language.model{
  annotations,
  SequencedAnnotation,OptionalAnnotation
}
import ceylon.language.model.declaration{
  ClassOrInterfaceDeclaration, ValueDeclaration,
  FunctionDeclaration
}

shared final annotation class AnnoTest1(text,count=1)
    satisfies OptionalAnnotation<AnnoTest1,ClassOrInterfaceDeclaration|ValueDeclaration|FunctionDeclaration>{
  shared String text;
  shared Integer count;
}
shared annotation AnnoTest1 annotest1(String text="") => AnnoTest1(text);
shared annotation AnnoTest1 annotest2(Integer count) => AnnoTest1("With Count", count);

shared final annotation class AnnoTest3(text)
    satisfies SequencedAnnotation<AnnoTest3,ClassOrInterfaceDeclaration|ValueDeclaration> {
  shared String text;
}
shared annotation AnnoTest3 annotest3(String text) => AnnoTest3(text);

shared annotest1 class Example1() {
  string => "Example1";
  shared annotest2(9) Integer printTime() {
    value m = process.milliseconds;
    print("printing at: ``m``");
    return m;
  }
}
annotest1("with something different")
annotest3("repeated twice")
annotest3("with different values")
shared class Example2() {
  annotest1{text="named call";}
  shared actual String string => "Example2";
}

annotest1("for an interface")
annotest3("for an interface")
interface Example3 {}

annotest3("one") annotest3("two") annotest3("three")
Singleton<String> testSingleton = Singleton("!");

shared final annotation class Issue235_1(shared String s, shared Issue235_2 b)
        satisfies OptionalAnnotation<Issue235_1,FunctionDeclaration> {
    string=>s+":"+b.string;
}

shared final annotation class Issue235_2(Integer i)
        satisfies OptionalAnnotation<Issue235_2,FunctionDeclaration> {
    string=>i.string;
} 

annotation Issue235_1 issue235_1() => Issue235_1("", Issue235_2(1));
annotation Issue235_1 issue235_2(Issue235_2 b=Issue235_2(2)) => Issue235_1("", b);

issue235_1 void testIssue235_1() {
}
issue235_2 void testIssue235_2() {
}

annotest2{count=5;}
shared void test() {
  value a1 = annotations(`AnnoTest1`, `Example1`);
  check(a1 exists, "Annotations 1 (opt on class)");
  value a2 = annotations(`AnnoTest3`, `Example2`);
  check(a2.size == 2, "Annotations 2 (seq on class)");
  value a3 = annotations(`AnnoTest1`, `test`);
  if (exists a3) {
    check(a3.count == 5, "Annotations 3 count");
    check(a3.text == "With Count", "Annotations 3 text");
  } else {
    fail("Annotations 3 (on toplevel method)");
  }
  check(annotations(`AnnoTest1`, `Example3`) exists, "Annotation on interface");
  
  value a4 = annotations(`AnnoTest3`, `testSingleton`);
  if (nonempty a4) {
    check(a4.size==3, "Annotations 4 size");
  } else {
    fail("Annotations 4 (top-level attribute)");
  }
  check(!annotations(`AnnoTest1`, `testSingleton`) exists, "testSingleton shouldn't have AnnoTest1");
  value a5 = annotations(`AnnoTest1`, `Example2.string`);
  if (exists a5) {
    check(a5.text=="named call", "Annotations 5 text");
  } else {
    fail("Annotations 5 (on attribute)");
  }
  print("FIXME: metamodel of member methods unavailable in lexical scope style");
  /*
  value a6 = annotations(`AnnoTest1`, `Example1.printTime`);
  if (exists a6) {
    check(a6.count == 9, "Annotations 6 count");
  } else {
    fail("Annotations 6 (on member method)");
  }*/
  value tiss235_1 = `testIssue235_1`.annotations<Issue235_1>();
  value tiss235_2 = `testIssue235_2`.annotations<Issue235_1>();
  if (nonempty tiss235_1) {
    check(tiss235_1.first.string == ":1", "Issue235_1 description - expected :1 got ``tiss235_1.first``");
  } else {
    fail("testIssue235_1 should have annotation Issue235_1");
  }
  if (nonempty tiss235_2) {
    check(tiss235_2.first.string == ":2", "Issue235_1 description - expected :2 got ``tiss235_2.first``");
  } else {
    fail("testIssue235_2 should have annotation Issue235_1");
  }
  results();
}
