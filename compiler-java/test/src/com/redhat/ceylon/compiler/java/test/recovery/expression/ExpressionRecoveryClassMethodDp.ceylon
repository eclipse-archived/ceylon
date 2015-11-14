class ExpressionRecoveryClassMethodDp() {
    shared void method(void append(Integer i), String s = asdfClassMethodDp()) {
        append(5);
    }
}
void expressionRecoveryClassMethodDp_throw(void append(Integer i)) {
    append(1);
    value i = ExpressionRecoveryClassMethodDp();
    append(2);
    i.method(append);
    append(10);
}
void expressionRecoveryClassMethodDp_nothrow(void append(Integer i)) {
    append(1);
    value i = ExpressionRecoveryClassMethodDp();
    append(2);
    i.method(append, "");
    append(10);
}
void expressionRecoveryClassMethodDp_throw2(void append(Integer i)) {
    append(1);
    value i = ExpressionRecoveryClassMethodDp();
    append(2);
    value ref = i.method;
    append(3);
    ref(append);
    append(10);
}
void expressionRecoveryClassMethodDp_nothrow2(void append(Integer i)) {
    append(1);
    value i = ExpressionRecoveryClassMethodDp();
    append(2);
    value ref = i.method;
    append(3);
    ref(append, "");
    append(10);
}