import check { check }

variable Boolean nnn=false;
Integer nn {
  nnn=!nnn;
  return nnn then 1 else 2;
}

void testOptimisations() {
  variable value c=0;
  value n = 1;
  for (i in 1..n) {
    c++;
  }
  check(c==1, "range loop optimisation 1");
  c=0;
  for (i in n..1) {
    c++;
  }
  check(c==1, "range loop optimisation 2");
  c=0;
  for (i in 2..n) {
    c++;
  }
  check(c==2, "range loop optim 3");
  c=0;
  for (i in n..2) {
    c++;
  }
  check(c==2, "range loop optim 4");
  c=0;
  for (i in n..n) {
    c++;
  }
  check(c==1, "range loop optim 5");
  c=0;
  for (i in nn..nn) {
    c++;
  }
  check(c==2, "range loop optim 6");

  //spans
  c=0;
  for (i in n:0) {
    c++;
  }
  check(c==0, "span loop optim 1");
  for (i in n:1) {
    c++;
  }
  check(c==1, "span loop optim 2");
  c=0;
  for (i in 1:n) {
    c++;
  }
  check(c==1, "span loop optim 3");
  c=0;
  for (i in nn:nn) {
    c++;
  }
  check(c==2, "span loop optim 4");
}
