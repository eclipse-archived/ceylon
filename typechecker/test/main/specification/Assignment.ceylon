void testAssignmentInExpression() {
    variable Boolean b;
    value x = true -> (b=true);
    print(b);
}
void testOkAssignmentInExpression() {
    variable Boolean b;
    value x = (b=true) || false;
    value y = true && (b=true);
    print(b);
}
void testBrokenAssignmentInExpression() {
    variable Boolean b;
    value x = true || (b=true);
    value y = true && (b=true);
    @error print(b);
}
void testAnotherBrokenAssignmentInExpression() {
    variable Boolean b;
    value x = (String s) (b=true);
    @error print(b);
}

void testAssignmentInDec() {
    variable Boolean b;
    Boolean c = (b=true);
    print(b);
}
void testAssignmentInArg() {
    variable Boolean b;
    print(b=true);
    print(b);
}
void testAssignmentInNamedArg() {
    variable Boolean b;
    print { line=(b=true); };
    print(b);
}

void testAssignmentInIf() {
    variable Boolean b;
    if (b=true) {}
    print(b);
}
void testAssignmentInIf1() {
    variable Boolean b;
    if (true||(b=true)) {}
    @error print(b);
}
void testAssignmentInIf2() {
	interface I {}
    variable Boolean b;
    if (is I i = (b=true)) {}
    print(b);
}

void testAssignmentInWhile() {
    variable Boolean b;
    while (b=true) {}
    print(b);
}
void testAssignmentInWhile1() {
    variable Boolean b;
    while (true||(b=true)) {}
    @error print(b);
}
void testAssignmentInWhile2() {
	interface I {}
    variable Boolean b;
    while (is I i = (b=true)) {}
    print(b);
}
