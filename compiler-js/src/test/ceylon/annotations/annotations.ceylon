import check { ... }

import ceylon.language.metamodel{
  annotations,
  SequencedAnnotation,OptionalAnnotation
}
import ceylon.language.metamodel.declaration{
  ClassOrInterfaceDeclaration, AttributeDeclaration,
  FunctionDeclaration
}

shared annotation class AnnoTest1(text,count=1)
    satisfies OptionalAnnotation<AnnoTest1,ClassOrInterfaceDeclaration|AttributeDeclaration|FunctionDeclaration>{
  shared String text;
  shared Integer count;
}
shared annotation AnnoTest1 annotest1(String text="") => AnnoTest1(text);
shared annotation AnnoTest1 annotest2(Integer count) => AnnoTest1("With Count", count);

shared annotation class AnnoTest3(text)
    satisfies SequencedAnnotation<AnnoTest3,ClassOrInterfaceDeclaration> {
  shared String text;
}
shared annotation AnnoTest3 annotest3(String text) => AnnoTest3(text);

shared annotest1 class Example1() {
  shared actual String string => "Example1";
}
annotest1("with something different")
annotest3("repeated twice")
annotest3("with different values")
shared class Example2() {
  annotest1{text="named call";}
  shared actual String string => "Example2";
}

annotest2{count=5;}
shared void test() {
  value a1 = annotations(`AnnoTest1`, `Example1`);
  check(a1 exists, "Annotations 1");
  value a2 = annotations(`AnnoTest3`, `Example2`);
  check(a2.size == 2, "Annotations 2");
  value a3 = annotations(`AnnoTest1`, `Example2.string`);
  if (exists a3) {
    check(a3.text=="named call", "Annotations 3 text");
  } else {
    fail("Annotations 3");
  }
  value a4 = annotations(`AnnoTest1`, `test`);
  if (exists a4) {
    check(a4.count == 5, "Annotations 4 count");
  } else {
    fail("Annotations 4");
  }
  results();
}
