void testAssignmentInExpression() {
    variable Boolean b;
    value x = true -> (b:=true);
    print(b);
}
void testOkAssignmentInExpression() {
    variable Boolean b;
    value x = (b:=true) || false;
    value y = true && (b:=true);
    print(b);
}
void testBrokenAssignmentInExpression() {
    variable Boolean b;
    value x = true || (b:=true);
    value y = true && (b:=true);
    @error print(b);
}
void testAnotherBrokenAssignmentInExpression() {
    variable Boolean b;
    value x = (String s) (b:=true);
    @error print(b);
}