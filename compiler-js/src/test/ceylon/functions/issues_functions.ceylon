import check { check }

class Test5858() {
    Integer echo(Integer s = 1);
    echo = (Integer s = 2) => s;
    check(echo()==1,"#5858 class");
}
void test5858() {
    Integer echo(Integer s = 1);
    echo = (Integer s = 2) => s;
    check(echo()==1,"#5858 fun");
}

void issues() {
    test517();
    test5858();
    Test5858();
}
