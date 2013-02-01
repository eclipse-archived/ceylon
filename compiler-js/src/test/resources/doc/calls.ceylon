Boolean isEven(Integer x) {
  return x%2==0;
}
void greet(String? name) {
  if (exists name) {
    print("Hello, ``name``!");
  }
}
String magnify1(Integer|String x) {
  if (is Integer x) {
    variable Integer factor := 0;
    switch (x <=> 1000)
    case (smaller) { factor:=1000000; }
    case (larger)  { factor:=1000; }
    else { factor:=1; }
    return (x*factor).string;
  } else if (is String x) {
    return x.uppercased;
  }
  return className(x);
}
String magnify2(Integer|String x) {
  switch(x)
  case (is Integer) {
    return (x*1000).string;
  }
  case (is String) {
    return x.uppercased;
  }
}
void f() {
  Sequence<Integer> seq={1,2,3,4};
  Range<Integer> r = 1..10;
  Entry<Integer,String> e = 5->"E";
  for (i in seq) {
    print(i);
  }
}
