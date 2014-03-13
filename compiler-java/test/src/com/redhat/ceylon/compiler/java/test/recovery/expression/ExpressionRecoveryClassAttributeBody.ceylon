class ExpressionRecoveryClassAttributeBody(SequenceBuilder<Integer> sb) {
    shared String attribute {
        sb.append(3);
        asdfClassAttributeBody();
        sb.append(4);
        return "";
    }
}
void expressionRecoveryClassAttributeBody_main(SequenceBuilder<Integer> sb) {
    sb.append(1);
    value i = ExpressionRecoveryClassAttributeBody(sb);
    sb.append(2);
    value x = i.attribute;
    sb.append(5);
}