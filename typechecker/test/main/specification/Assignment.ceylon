void testAssignmentInExpression() {
    variable Boolean b;
    value x = true -> (b=true);
    print(b);
}
void testAssignmentInLazyExpression() {
    variable Boolean b;
    value x => true -> (b=true);
    @error print(b);
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
    value x = (String s) => (b=true);
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
    print { val=(b=true); };
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
    class J(Boolean b) {}
    variable Boolean b;
    if (is I i = J(b=true)) {}
    @error print(b); //not really necessary in this special case!
}
void testAssignmentInIf3() {
	interface I {}
	Object hello="hello";
    variable Object o="hello";
    if (is String s = (o=hello)) {}
    print(o);
}
void testAssignmentInIf4() {
    variable Boolean b;
    if (1==1, true||(b=true)) {}
    @error print(b);
}
void testAssignmentInIf5() {
    interface I {}
    class J(Boolean b) {}
    variable Boolean b;
    if (1==1, is I i = J(b=true)) {}
    @error print(b);
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
	class J(Boolean b) {}
    variable Boolean b;
    while (is I i = J(b=true)) {}
    @error print(b); //not really necessary in this special case!
}
void testAssignmentInWhile3() {
	interface I {}
	Object hello="hello";
    variable Object o="hello";
    if (is String s = (o=hello)) {}
    print(o);
}

void testForwardLazySpecifier() {
    String name1;
    String name2;
    variable String name3;
    variable String name4;
    @error //TODO: should not display error here!
    name1 = "";
    name2 => "";
    name3 = "";
    @error name4 => "";
    @error name1 => "";
    @error name2 => "";
    @error name3 => "";
    @error name4 => "";
}

void withLocalCircular() {
    @error Integer circular = circular;
    @error Integer recursize => recursize;
    Integer split;
    @error split = split;
}
@error Integer circular = circular;
@error Integer recursize => recursize;

void testAssignmentInIfExpression(Boolean flag) {
    variable String name;
    value result = if (flag) then (name="A") else (name="B");
    @error print(name); //unnecessary error!
}
void testBrokenAssignmentInIfExpression1(Boolean flag) {
    variable String name;
    value result = if (flag) then (name="A") else "B";
    @error print(name);
}
void testBrokenAssignmentInIfExpression2(Boolean flag) {
    variable String name;
    value result = if (flag) then "A" else (name="B");
    @error print(name);
}

void testAssignmentInSwitchExpression(Boolean flag) {
    variable String name;
    value result = switch (flag) case (true) (name="A") else (name="B");
    @error print(name); //unnecessary error!
}
void testBrokenAssignmentInSwitchExpression1(Boolean flag) {
    variable String name;
    value result = switch (flag) case (true) (name="A") else "B";
    @error print(name);
}
void testBrokenAssignmentInSwitchExpression2(Boolean flag) {
    variable String name;
    value result = switch (flag) case (true) "A" else (name="B");
    @error print(name);
}