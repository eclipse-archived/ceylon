import ceylon.language.model{SequencedAnnotation}
import ceylon.language.model.declaration{FunctionDeclaration}

final annotation class NestedLeaf(shared Integer i1 = 1) 
    satisfies SequencedAnnotation<NestedLeaf, FunctionDeclaration>{
}

annotation NestedLeaf nestedLeafDefaultedParameter(Integer i2=2) => NestedLeaf(i2);
annotation NestedLeaf nestedLeafLiteralArgument() => NestedLeaf(3);

final annotation class NestedBranch(shared String s, shared NestedLeaf b) 
    satisfies SequencedAnnotation<NestedBranch, FunctionDeclaration>{
}

// TODO What about versions of NestedBranch with literal and dp values for b? 

annotation NestedBranch nestedBranchDPClassWithLiteralArgument(
        NestedLeaf b = NestedLeaf(100)) 
    => NestedBranch("nestedBranchDPClassWithLiteralArgument", b);

annotation NestedBranch nestedBranchDPClassDefaultedArgument(
        NestedLeaf b = NestedLeaf()) 
    => NestedBranch("nestedBranchDPClassDefaultedArgument", b);

annotation NestedBranch nestedBranchDPCtorWithLiteralArgument(
        NestedLeaf b = nestedLeafDefaultedParameter(101)) 
    => NestedBranch("nestedBranchDPCtorWithLiteralArgument", b);

annotation NestedBranch nestedBranchDPCtorNullary(
        NestedLeaf b = nestedLeafLiteralArgument()) 
    => NestedBranch("nestedBranchDPCtorNullary", b);

annotation NestedBranch nestedBranchDPCtorWithDefaultedArgument(
        NestedLeaf b = nestedLeafDefaultedParameter()) 
    => NestedBranch("nestedBranchDPCtorWithDefaultedArgument", b);

annotation NestedBranch nestedBranchClassNullary() 
    => NestedBranch("nestedBranchClassNullary", NestedLeaf());
annotation NestedBranch nestedBranchClassLiteral() 
    => NestedBranch("nestedBranchClassLiteral", NestedLeaf(110));
// Illegal annotation NestedBranch nestedBranchCtorNullary(Integer a6) 
//    => NestedBranch("", NestedLeaf(a6));

annotation NestedBranch nestedBranchCtorNullary() 
    => NestedBranch("nestedBranchCtorNullary", nestedLeafLiteralArgument());

annotation NestedBranch nestedBranchCtorDefaultedArgument() 
    => NestedBranch("nestedBranchCtorDefaultedArgument", nestedLeafDefaultedParameter());

annotation NestedBranch nestedBranchCtorLiteral() 
    => NestedBranch("nestedBranchCtorLiteral", nestedLeafDefaultedParameter{i2=512;});

// Illegal annotation NestedBranch nestedBranchCtorParamArg(Integer x) 
//    => NestedBranch("", nestedLeafDefaultedParameter{i2=x;});

// Illegal annotation NestedBranch ctorDefaultedParamA8(Integer x) 
//    => NestedBranch("", nestedLeafDefaultedParameter(x));

