//Test with only top-level methods and attributes
void simple1() {}
shared Integer simple2() { return 0; }
void simple3(Integer p1, String p2) {}
shared void defaulted1(Integer p1, Integer p2=5) {}
void sequenced1(Integer p1, String... p2) {}
void sequencedDefaulted(String s="x", Integer... ints) {}
shared Integer mpl1(String a)(String b) {
  return a.size + b.size;
}
String mpl2(Integer a)(String b)(Float c) {
  return a.string + ":" + b + ":" + c.string;
}
doc "A nested function. Should the doc be in the metamodel as well?"
shared Integer nested(String s) {
  shared String f() {
    return s + "!";
  }
  return f().size;
}
Integer i1 = 5;
shared String s1 = "hey";
variable Float pi := 3.14;
shared variable Sequence<Integer> seq := { 5 };
