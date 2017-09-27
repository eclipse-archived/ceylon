class Bug2331InterveningVisitor() extends Bug2331Visitor() {
    shared actual default void visit(Bug2331Tree.AssignmentOp ao) {
        super.visit(ao);
    }
    shared actual default void visit(Bug2331Tree.CompilationUnit ao) {
        super.visit(ao);
    }
}

class Bug2331ChildVisitor() extends Bug2331InterveningVisitor() {
    shared actual void visit(Bug2331Tree.UnaryOperatorExpression uoe) {
        //      ^
        //      error: Missing return statement
        super.visit(uoe);
    }
}
