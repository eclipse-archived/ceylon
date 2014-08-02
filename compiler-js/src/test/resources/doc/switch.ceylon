void switchPrint(Integer|String x) {
  switch (x)
  case (is String) { print(x.uppercased); } //x is String inside the block
  case (is Integer) {
    //x is Integer inside this block
    switch (x <=> 5)
    case (smaller) { print("``x`` is smaller than 5"); }
    case (larger) { print("``x`` is larger than 5"); }
    else { print("``x`` is 5"); }
  }
}

void runx() {
  switchPrint("hi");
  for (i in 4..6) {
    switchPrint(i);
  }
}
