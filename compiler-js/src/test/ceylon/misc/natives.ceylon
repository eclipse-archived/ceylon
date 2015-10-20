import check { check }

native Integer test655_1() {
  return 1;
}
native Integer test655_2() => 1;

void testNatives() {
  check(test655_1() == 1, "#655.1");
  check(test655_2() == 1, "#655.2");
}
