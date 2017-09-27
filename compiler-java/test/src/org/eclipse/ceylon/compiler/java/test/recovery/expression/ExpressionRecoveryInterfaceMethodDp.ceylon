interface ExpressionRecoveryInterfaceMethodDp {
    shared void method(void append(Integer i), String s = asdfInterfaceMethodDp()) {
        append(5);
    }
}
class ExpressionRecoveryInterfaceMethodDp_class() satisfies ExpressionRecoveryInterfaceMethodDp {
}
void expressionRecoveryInterfaceMethodDp_throw(void append(Integer i)) {
    append(1);
    value i = ExpressionRecoveryInterfaceMethodDp_class();
    append(2);
    i.method(append);
    append(10);
}
void expressionRecoveryInterfaceMethodDp_nothrow(void append(Integer i)) {
    append(1);
    value i = ExpressionRecoveryInterfaceMethodDp_class();
    append(2);
    i.method(append, "");
    append(10);
}
void expressionRecoveryInterfaceMethodDp_throw2(void append(Integer i)) {
    append(1);
    value i = ExpressionRecoveryInterfaceMethodDp_class();
    append(2);
    value ref = i.method;
    append(3);
    ref(append);
    append(10);
}
void expressionRecoveryInterfaceMethodDp_nothrow2(void append(Integer i)) {
    append(1);
    value i = ExpressionRecoveryInterfaceMethodDp_class();
    append(2);
    value ref = i.method;
    append(3);
    ref(append, "");
    append(10);
}