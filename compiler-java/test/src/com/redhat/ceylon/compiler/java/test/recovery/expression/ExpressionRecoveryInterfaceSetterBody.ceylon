interface ExpressionRecoveryInterfaceSetterBody {
    shared formal SequenceBuilder<Integer> sb;
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
class ExpressionRecoveryInterfaceSetterBody_class(shared actual SequenceBuilder<Integer> sb) satisfies ExpressionRecoveryInterfaceSetterBody {
    
}
void expressionRecoveryInterfaceSetterBody_main(SequenceBuilder<Integer> sb) {
    sb.append(1);
    value i = ExpressionRecoveryInterfaceSetterBody_class(sb);
    sb.append(2);
    value x = i.attribute;
    sb.append(4);
    i.attribute = x;
    sb.append(7);
}