shared annotation class AnnoTest1(text,count=1) {
  shared String text;
  shared Integer count;
}
shared annotation AnnoTest1 annotest1(String text="") => AnnoTest1(text);
shared annotation AnnoTest1 annotest2(Integer count) => AnnoTest1("With Count", count);

shared annotest1 class Example1() {
  shared actual String string => "Example1";
}
annotest1 "with something different"
shared class Example2() {
  annotest1{text="named call";}
  shared actual String string => "Example2";
}

annotest2{count=5;}
shared void test() {
  print(Example1());
  print(Example2());
}
