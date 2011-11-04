@nomodel 
void f() {
    f();
}

@nomodel
shared void bug148() {
    f();
}
