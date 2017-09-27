class ExpressionRecoveryClassMethodBody() {
    shared void method(void append(Integer i)) {
        append(3);
        asdfClassMethodBody();
        append(4);
    }
}
void expressionRecoveryClassMethodBody_main(void append(Integer i)) {
    append(1);
    value i = ExpressionRecoveryClassMethodBody();
    append(2);
    i.method(append);
    append(5);
}