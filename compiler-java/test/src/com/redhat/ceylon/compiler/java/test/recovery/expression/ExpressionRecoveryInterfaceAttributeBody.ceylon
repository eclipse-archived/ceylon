interface ExpressionRecoveryInterfaceAttributeBody {
    shared formal void append(Integer i);
    shared String attribute {
        append(3);
        asdfInterfaceAttributeBody();
        append(4);
        return "";
    }
}
class ExpressionRecoveryInterfaceAttributeBody_class (shared actual void append(Integer i)) satisfies ExpressionRecoveryInterfaceAttributeBody {
    
}
void expressionRecoveryInterfaceAttributeBody_main(void append(Integer i)) {
    append(1);
    value i = ExpressionRecoveryInterfaceAttributeBody_class(append);
    append(2);
    value x = i.attribute;
    append(5);
}