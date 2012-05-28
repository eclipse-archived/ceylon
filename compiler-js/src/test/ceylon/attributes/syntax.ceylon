import assert { assert }
//Test the new syntax for class and method parameters
Integer newSyntaxTest(f, Integer a, Integer b) {
  Integer f(Integer a, Integer b);
  return f(a, b);
}

class TestNewSyntax(proc) {
  shared String proc(String s);
}

Integer newSyntaxTest2(a, Integer b) {
  Integer a;
  return a+b;
}

void testNewSyntax() {
  assert(newSyntaxTest((Integer i1, Integer i2) i1+i2, 2, 3) == 5, "new syntax for functions in methods");
  assert(TestNewSyntax((String s) s.reversed).proc("hola") == "aloh", "new syntax for functions in classes");
  assert(newSyntaxTest(newSyntaxTest2, 6, 4) == 10, "new syntax for attributes in methods");
}
