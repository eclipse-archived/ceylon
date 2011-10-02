shared class Klass(Integer p1, Integer? p2, Integer[] p3) {
  Integer m1(Integer p1){return +1;}
  Integer? m2(Integer? p2){return +1;}
  Integer[] m3(Integer[] p3){return {+1};}
}
