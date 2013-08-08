@nomodel
nestedLeafDefaultedParameter
nestedLeafLiteralArgument
// defaulted parameter class instantiation
nestedBranchDPClassWithLiteralArgument
nestedBranchDPClassDefaultedArgument
// nested invocations at callsite
nestedBranchDPClassWithLiteralArgument(NestedLeaf())
nestedBranchDPClassWithLiteralArgument(NestedLeaf(3))
nestedBranchDPClassWithLiteralArgument(NestedLeaf{i1=4;})
nestedBranchDPClassWithLiteralArgument(nestedLeafDefaultedParameter())
nestedBranchDPClassWithLiteralArgument(nestedLeafDefaultedParameter(5))
nestedBranchDPClassWithLiteralArgument(nestedLeafDefaultedParameter{i2=6;})
nestedBranchDPClassWithLiteralArgument(nestedLeafLiteralArgument())
// defaulted parameter constructor invocation
nestedBranchDPCtorWithLiteralArgument
nestedBranchDPCtorNullary
nestedBranchDPCtorWithDefaultedArgument // This ONE!
// nested invocation of a class
nestedBranchClassNullary
nestedBranchClassLiteral
// nested invocation of a ctor
nestedBranchCtorNullary
nestedBranchCtorDefaultedArgument
nestedBranchCtorLiteral
// TODO nested invocation of a ctor
void ctorDefaultedParam_callsite(){
}

