
class ExpressionRecoveryValueRefinementSpecifier()
        extends Object()
        satisfies List<Anything> {
    getFromFirst(Integer index) => asdfValueRefinementSpecifier;
    lastIndex => 1;
}
void expressionRecoveryValueRefinementSpecifier_main(void append(Integer i)) {
    append(1);
    value y = ExpressionRecoveryValueRefinementSpecifier();
    append(2);
    value x= y.getFromFirst(0);
    append(3);
}