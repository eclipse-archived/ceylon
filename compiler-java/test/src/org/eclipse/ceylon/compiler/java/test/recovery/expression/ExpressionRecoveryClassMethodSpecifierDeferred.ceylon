class ExpressionRecoveryClassMethodSpecifierDeferred() {
    shared void method(void append(Integer i));
    method = asdfClassMethodSpecifierDeferred();
     
}
void expressionRecoveryClassMethodSpecifierDeferred_main(void append(Integer i)) {
    append(1);
    value i = ExpressionRecoveryClassMethodSpecifierDeferred();
    append(2);
    i.method(append);
    append(5);
}