@noanno
class ThisQualifiedSpecification(variable Integer a) {
    Integer b;
    variable Integer c;
    late Integer d;
    shared Integer e;
    Boolean m();
    shared Boolean n();
    
    this.a = 1;
    this.b = 2;
    this.c = 3;
    this.d = 4;
    this.e = 4;
    
    this.m = () => true;
    this.n = () => true;
}
