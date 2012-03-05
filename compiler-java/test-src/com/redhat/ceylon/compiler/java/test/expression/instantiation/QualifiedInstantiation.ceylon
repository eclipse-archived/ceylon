@nomodel
class QualifiedInstantiation() {
    class Inner(String s) {
    }
    Inner inner;
    void m(QualifiedInstantiation q) {
        QualifiedInstantiation.Inner("");
        this.Inner("");
        q.Inner("");
        QualifiedInstantiation.Inner{s="";};
        this.Inner{s="";};
        q.Inner{s="";};
    }
}