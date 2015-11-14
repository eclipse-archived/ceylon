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

void issue526() {
  //Issue #526
  value sb = StringBuilder();
  void inv(void f()) => f();
  variable Anything() task = void() {
    sb.append("task");
  };
  value funs=[inv,inv,inv];
  for (c in funs) {
    Anything() f=task;
    task=void() {
      sb.append("nested ");
      c(f);
    };
  }
  check(sb.string.empty, "#526.1");
  task();
  check(sb.string=="nested nested nested task", "#526.2");
}

void issue589() {
  value sb = StringBuilder();
  variable {Anything()*} closures = [];
  for (x in ["4", "3"]) {
    closures = closures.follow(() => sb.append(x));
  }

  value it = ["2", "1"].iterator();
  while (!is Finished x = it.next()) {
    closures = closures.follow(() => sb.append(x));
  }

  for (f in closures) {
    f();
  }
  check(sb.string=="1234", "#589 expected 1234 got ``sb``");
}
