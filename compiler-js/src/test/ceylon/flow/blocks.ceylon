import check { ... }

void testBlocks() {
  value x=1;
  variable Integer ran=-1;
  //Case 1: "if"block enclosed, with translated break
  if (x == 1) {
    for (i in 2..10) {
      if (i % 2 == 0) {
        value ii=i;
        function f() { return ii; }
        break;
      }
      ran=i; //this should never run
    }
  }
  check(ran==-1,"Block test 1");

  //Case 2: "if"block enclosed, with regular break
  ran=-1;
  if (x == 1) {
    value xx = 2; //this will be enclosed in a function
    for (i in 1..10) {
      function f() { return xx; } //because of this capture
      if (i % 2 == 0) {
        break; //this should translate to a regular break
      }
      ran= i;
    }
    check(ran==1, "Block test 2");
    ran=100;
  }
  check(ran==100, "Block test 3");
  //Case 3: "if"block enclosed, with translated continue
  ran=-1;
  if (x == 1) {
    for (i in 2..10) {
      if (i % 2 == 0) {
        value ii=i;
        function f() { return ii; }
        continue;
      }
      ran=i;
    }
  }
  check(ran==9, "Block test 4");

  //Case 4: "if"block enclosed, with regular continue
  ran=-1;
  if (x == 1) {
    value xx=2;
    for (i in 2..10) {
      function f() { return xx; }
      if (i%2 == 0) {
        continue;
      }
      ran=i;
    }
    check(ran==9, "Block test 5");
    ran=100;
  }
  check(ran==100, "Block test 6");
}
