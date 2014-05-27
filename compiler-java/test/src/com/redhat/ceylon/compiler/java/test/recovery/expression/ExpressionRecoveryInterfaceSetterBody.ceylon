interface ExpressionRecoveryInterfaceSetterBody {
    shared formal void append(Integer i);
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
class ExpressionRecoveryInterfaceSetterBody_class(shared actual void append(Integer i)) satisfies ExpressionRecoveryInterfaceSetterBody {
    
}
void expressionRecoveryInterfaceSetterBody_main(void append(Integer i)) {
    append(1);
    value i = ExpressionRecoveryInterfaceSetterBody_class(append);
    append(2);
    value x = i.attribute;
    append(4);
    i.attribute = x;
    append(7);
}