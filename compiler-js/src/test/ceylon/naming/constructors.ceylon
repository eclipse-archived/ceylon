import check { check }

shared class SevenOneTwoTwo {
  shared Integer x;

  shared new() {
    x = 0;
  }

  new withInt(Integer i) {
    x = i;
  }

  shared new other(Integer i) {
    x = i;
  }

  shared SevenOneTwoTwo add(Integer i) {
    return SevenOneTwoTwo.withInt(x + i);
  }
  shared SevenOneTwoTwo sub(Integer i) {
    return SevenOneTwoTwo.other(x - i);
  }

  string = "Value:``x``";
}

void test7122() {
  check(SevenOneTwoTwo().add(5).x == 5, "#7122.1");
  check(SevenOneTwoTwo().sub(5).x == -5, "#7122.2");
}
