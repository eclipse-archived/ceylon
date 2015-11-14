class ExpressionRecoveryClassSetterBody(void append(Integer i)) {
    shared String attribute {
        append(3);
        return "";
    }
    assign attribute {
        append(5);
        value x = asdfClassSetterBody();
        append(6);
    }
}
void expressionRecoveryClassSetterBody_main(void append(Integer i)) {
    append(1);
    value i = ExpressionRecoveryClassSetterBody(append);
    append(2);
    value x = i.attribute;
    append(4);
    i.attribute = x;
    append(7);
}