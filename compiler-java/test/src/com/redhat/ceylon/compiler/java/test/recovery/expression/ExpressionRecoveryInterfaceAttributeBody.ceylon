interface ExpressionRecoveryInterfaceAttributeBody {
    shared formal SequenceBuilder<Integer> sb;
    shared String attribute {
        sb.append(3);
        asdfInterfaceAttributeBody();
        sb.append(4);
        return "";
    }
}
class ExpressionRecoveryInterfaceAttributeBody_class (shared actual SequenceBuilder<Integer> sb) satisfies ExpressionRecoveryInterfaceAttributeBody {
    
}
void expressionRecoveryInterfaceAttributeBody_main(SequenceBuilder<Integer> sb) {
    sb.append(1);
    value i = ExpressionRecoveryInterfaceAttributeBody_class(sb);
    sb.append(2);
    value x = i.attribute;
    sb.append(5);
}