@nomodel
void v() {
    Natural three = 1.plus { other=2; };
    //1.plus(2).equals(3);
    1.plus { other=2; }.equals{that=3;};
}