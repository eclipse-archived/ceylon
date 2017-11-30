import check { check }
//Test the new syntax for class and method parameters
Integer newSyntaxTest(f, Integer a, Integer b) {
  Integer f(Integer a, Integer b);
  return f(a, b);
}

class TestNewSyntax(proc) {
  variable String privString = "0";
  shared String pubString => privString;
  assign pubString => privString = pubString;
  shared String proc(String s);
}

Integer newSyntaxTest2(a, Integer b) {
  Integer a;
  return a+b;
}

void testNewSyntax() {
  check(newSyntaxTest((Integer i1, Integer i2) => i1+i2, 2, 3) == 5, "new syntax for functions in methods");
  check(TestNewSyntax((String s) => s.uppercased).proc("hola") == "HOLA", "new syntax for functions in classes");
  check(newSyntaxTest(newSyntaxTest2, 6, 4) == 10, "new syntax for attributes in methods");
  value fats = TestNewSyntax((String s) => s);
  check(fats.pubString == "0", "fat arrow getter");
  fats.pubString = "HEY!";
  check(fats.pubString == "HEY!", "fat arrow setter still returns '``fats.pubString``'");
}
