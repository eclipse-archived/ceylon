@noanno
void bug1089() {
    assert(0<=0<2);
    assert(0<1<2);
    assert(0<=1<2);
    assert(0<1<=2);
    assert(0<=1<=2);
}