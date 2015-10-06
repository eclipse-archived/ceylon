package com.redhat.ceylon.compiler.java.test.interop;

class Bug2331Node {}

class Bug2331Tree {
    public static class AssignmentOp extends Bug2331Node{}
    public static class CompilationUnit extends Bug2331Node{}
    public static class UnaryOperatorExpression extends Bug2331Node{}
}

class Bug2331Visitor {
    public void visit(Bug2331Tree.AssignmentOp arg){}
    public void visit(Bug2331Tree.CompilationUnit arg){}
    public void visit(Bug2331Tree.UnaryOperatorExpression arg){}
}