String int2string(Integer i) {
  return "INT ``i``";
}
void higher(String f(Integer i)) {
  f(0);
}
shared void run() {
  Callable<String,[Integer]> f1 = int2string;
  function f2(Integer i) => int2string(i);
  higher(f1);
  higher(f2);
}
