shared String f1()(Integer x) {
  return x.string;
}

shared String f2(Integer a)(Float b)(Object c) {
  return a.string + b.string + c.string;
}

