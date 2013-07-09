interface Q1<out T> {
    shared default class M()  {}
}

class P1<out T>() satisfies Q1<T> {
  void testQ1ualifiedTypes() {
    M m = R1<T>().M();
    @error M b = R1<Object>().M();
  }    
}

class R1<out T>() satisfies Q1<T> {
    shared actual class M() extends super.M() {}
  void testQ1ualifiedTypes() {
    @error M m = P1<T>().M();
    @error M b = P1<Object>().M();
  }    
}

void testQ1ualifiedTypes1() {
    P1<String>.M ps = R1<String>().M();
    P1<Object>.M po = R1<String>().M();
    @error P1<Integer>.M po = R1<String>().M();
    @error R1<String>.M rs = P1<String>().M();
    @error R1<Object>.M ro = P1<String>().M();
    @error R1<Integer>.M ro = P1<String>().M();
}

interface Q2<in T> {
    shared default class M()  {}
}

class P2<in T>() satisfies Q2<T> {
  void testQ1ualifiedTypes() {
    M m = R2<T>().M();
    @error M b = R2<Object>().M();
  }    
}

class R2<in T>() satisfies Q2<T> {
    shared actual class M() extends super.M() {}
  void testQ1ualifiedTypes() {
    @error M m = P2<T>().M();
    @error M b = P2<Object>().M();
  }    
}

void testQ1ualifiedTypes2() {
    P2<String>.M ps = R2<String>().M();
    P2<Nothing>.M po = R2<String>().M();
    @error P2<Integer>.M po = R2<String>().M();
    @error R2<String>.M rs = P2<String>().M();
    @error R2<Nothing>.M ro = P2<String>().M();
    @error R2<Integer>.M ro = P2<String>().M();
}