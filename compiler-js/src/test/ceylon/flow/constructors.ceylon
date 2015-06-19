import check { check }

class Issue525<T> satisfies Identifiable {
  value sb=StringBuilder().append("a");
  shared T t;
  shared new foo(T x) {
    if (x is String) {
      sb.append("b");
    }
    t=x;
  }
  sb.append("c");
  shared new bar(T x) {
    if (x is Integer) {
      sb.append("d");
    }
    t=x;
  }
  sb.append("e");
  string = sb.string;
}

shared void test525() {
  value f1=Issue525.foo("x");
  value f2=Issue525.bar(1);
  check(f1.t=="x", "#525.1");
  check(f2.t==1, "#525.2");
  check(f1.string=="abce", "#525.3");
  check(f2.string=="acde", "#525.4");
  check((f1 of Object) is Identifiable, "#525.5");
  check((f2 of Object) is Identifiable, "#525.6");
  check((f1 of Object) is Issue525<String>, "#525.7");
  check((f2 of Object) is Issue525<Integer>, "#525.8");
}
