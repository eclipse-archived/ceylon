shared class Klass(Integer p1, Integer? p2, Integer[] p3) {
    shared Integer m1(Integer p1){return +1;}
    shared Integer? m2(Integer? p2){return +1;}
    shared Integer[] m3(Integer[] p3){return {+1};}
}
