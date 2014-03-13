class ExpressionRecoveryClassSetterBody(SequenceBuilder<Integer> sb) {
    shared String attribute {
        sb.append(3);
        return "";
    }
    assign attribute {
        sb.append(5);
        value x = asdfClassSetterBody();
        sb.append(6);
    }
}
void expressionRecoveryClassSetterBody_main(SequenceBuilder<Integer> sb) {
    sb.append(1);
    value i = ExpressionRecoveryClassSetterBody(sb);
    sb.append(2);
    value x = i.attribute;
    sb.append(4);
    i.attribute = x;
    sb.append(7);
}