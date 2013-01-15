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
}
