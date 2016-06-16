interface Disambig {
    shared void m(Disambig? a=null){}
}
void use(Disambig d) {
    d.m(null);
}