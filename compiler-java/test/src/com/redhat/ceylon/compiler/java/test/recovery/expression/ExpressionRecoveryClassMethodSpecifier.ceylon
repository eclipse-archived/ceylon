class ExpressionRecoveryClassMethodSpecifier() {
    shared void method(void append(Integer i)) => asdfClassMethodSpecifier();
     
}
void expressionRecoveryClassMethodSpecifier_main(void append(Integer i)) {
    append(1);
    value i = ExpressionRecoveryClassMethodSpecifier();
    append(2);
    i.method(append);
    append(5);
}