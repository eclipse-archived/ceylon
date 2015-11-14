class ExpressionRecoveryClassAttributeBody(void append(Integer i)) {
    shared String attribute {
        append(3);
        asdfClassAttributeBody();
        append(4);
        return "";
    }
}
void expressionRecoveryClassAttributeBody_main(void append(Integer i)) {
    append(1);
    value i = ExpressionRecoveryClassAttributeBody(append);
    append(2);
    value x = i.attribute;
    append(5);
}