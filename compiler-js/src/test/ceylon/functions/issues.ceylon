import check { check }

void issues() {
    value i493 = void(){};
    value m493 =  [i493,i493];
    check(m493[0] == m493[1], "#493");
}
