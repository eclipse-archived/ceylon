void c() {
  Sequence<String?> seq = { null, "a", null, "b", null };
  print({ for (x in seq) if (exists x) if (exists c=x.initial(1)[0]) c.uppercased });
}
