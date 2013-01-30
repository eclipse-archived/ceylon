import check { check }
//Test defaulted params look at #164
class WithDefaulted1<out T>(t, a="A", b="B") given T satisfies Object {
  shared T t;
  shared String a;
  shared String b;
}

class WithDefaulted2(name, Integer* ints) {
  shared String name;
  shared Integer count => ints.size;
}

class SubDef1(String name) extends WithDefaulted1<String>(name) {}
class SubDef2(String n) extends WithDefaulted2(n) {}
class SubDef3(String n) extends WithDefaulted2(n,6,6,6) {}

void testDefaulted() {
  WithDefaulted1<Object> wd1 = WithDefaulted1(1);
  check(wd1.a == "A", "WithDefaulted1.a [1]");
  check(WithDefaulted1(1,"Z").a == "Z", "WithDefaulted1.a [2]");
  check(WithDefaulted1(1,"Z").b == "B", "WithDefaulted1.b [3]");
  check(WithDefaulted1(1,"Z","Y").b == "Y", "WithDefaulted1.b [4]");
  check(wd1 is WithDefaulted1<Integer>, "WithDefaulted1 type arguments [5]");
  check(WithDefaulted2("a").count==0, "WithDefaulted2.count [6]");
  check(WithDefaulted2("b",0,0,0).count==3, "WithDefaulted2.count [7]");
  check(SubDef1("F").a=="A", "SubDef1.a [8]");
  check(SubDef1("U").b=="B", "SubDef2.a [9]");
  check(SubDef2("C").count==0, "SubDef2.count [10]");
  check(SubDef3("K").count==3, "SubDef3.count [11]");
}
