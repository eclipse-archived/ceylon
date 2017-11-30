shared String expressionRecoverySetterBody {
    return "";
}
assign expressionRecoverySetterBody {
    asdfSetterBody();
}
void expressionRecoverySetterBody_main(void append(Integer i)) {
    append(1);
    value x = expressionRecoverySetterBody;
    append(2);
    expressionRecoverySetterBody = "";
    append(3);
}
