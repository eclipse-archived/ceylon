import check { check }

//Tests for spread invocations
Integer spread1(Integer* a) {
  variable value r  = 0;
  for (i in a) {
    r+=i;
  }
  return r;
}

Integer spread2({Integer*} a) {
  variable value r  = 0;
  for (i in a) {
    r+=i;
  }
  return r;
}
Integer spread3(Integer a, Integer *b) {
  variable value r = a;
  for (i in b) {
    r+=i;
  }
  return r;
}
Integer spread4(Integer a, Integer b, Integer c) {
  return a+b+c;
}
Integer spread5(Integer a, Integer b, Integer c, Integer d=20) {
  return a+b+c-d;
}

String testIssue296(String a, String b="B") {
  return a+" "+b;
}

T issue296<T>(T(String, String=) callable, [String, String=] tup)
        => callable(*tup);

void testSpread() {
  value ints = [8,9,10];
  check(spread1(1,2,3)==6, "spread [1]");
  check(spread1(*ints)==27, "spread [2]");
  check(spread1(3,*ints)==30, "spread [3]");
  check(spread1(for (i in ints) i*10)==270, "spread [4]");
  check(spread1(2,3,for (i in ints) i*10)==275, "spread [5]");
  check(spread2{1,2,3}==6, "spread [6]");
  check(spread2{*ints}==27, "spread [7]");
  check(spread2{3,*ints}==30, "spread [8]");
  check(spread2{for (i in ints) i*10}==270, "spread [9]");
  check(spread2{2,3,for (i in ints) i*10}==275, "spread [10]");
  check(spread3(1,*ints)==28, "spread [11]");
  check(spread4(*ints)==27, "spread [12]");
  check(spread5(*ints)==7, "spread [13]");
  check(testIssue296("one") == "one B", "issue 296 [1]");
  check(issue296(testIssue296,["two", "three"])=="two three", "issue 296 [2]");
  check(issue296(testIssue296,["four"])=="four B", "issue 296 [3]");
}
