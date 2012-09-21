void b() {
  Sequence<Integer|String> seq = { 1, "2", 3, "4", 5 };
  print({ for (x in seq) if (is Integer x) x*2 });
}
