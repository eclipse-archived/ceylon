package com.redhat.ceylon.compiler.java.codegen;

import com.redhat.ceylon.compiler.typechecker.tree.*;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.AbstractedType;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.AdaptedTypes;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.AddAssignOp;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Alias;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.AndAssignOp;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.AndOp;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Annotation;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.AnnotationList;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.AnyAttribute;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.AnyClass;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.AnyInterface;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.AnyMethod;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ArgumentList;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ArithmeticAssignmentOp;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ArithmeticOp;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.AssignOp;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.AssignmentOp;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Atom;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.AttributeArgument;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.AttributeDeclaration;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.AttributeGetterDefinition;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.AttributeSetterDefinition;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.BaseMemberExpression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.BaseMemberOrTypeExpression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.BaseType;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.BaseTypeExpression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.BinaryOperatorExpression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.BitwiseAssignmentOp;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.BitwiseOp;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Block;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Body;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.BooleanCondition;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Break;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.CaseClause;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.CaseItem;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.CaseTypes;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.CatchClause;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.CatchVariable;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.CharLiteral;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ClassBody;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ClassDeclaration;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ClassDefinition;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ClassOrInterface;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.CompareOp;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ComparisonOp;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.CompilationUnit;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.CompilerAnnotation;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ComplementAssignOp;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ComplementOp;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Comprehension;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ComprehensionClause;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Condition;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Continue;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ControlClause;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ControlStatement;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Declaration;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.DecrementOp;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.DefaultArgument;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.DefaultOp;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.DifferenceOp;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Directive;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.DivideAssignOp;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Element;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ElementOrRange;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ElementRange;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Ellipsis;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ElseClause;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.EntryOp;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.EqualOp;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.EqualityOp;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ExecutableStatement;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Exists;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ExistsCondition;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ExistsOrNonemptyCondition;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Expression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ExpressionComprehensionClause;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ExpressionList;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ExpressionStatement;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ExtendedType;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ExtendedTypeExpression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Extends;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.FinallyClause;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.FlipOp;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.FloatLiteral;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ForClause;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ForComprehensionClause;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ForIterator;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ForStatement;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.FunctionArgument;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.FunctionModifier;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.FunctionalParameterDeclaration;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.IdenticalOp;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Identifier;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.IfClause;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.IfComprehensionClause;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.IfStatement;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Implicit;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Import;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ImportList;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ImportMember;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ImportMemberOrType;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ImportMemberOrTypeList;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ImportPath;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ImportType;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ImportWildcard;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.InOp;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.IncrementOp;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.IndexExpression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.IndexOp;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.IndexOperator;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.InferredTypeArguments;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.InitializerExpression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.InterfaceBody;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.InterfaceDeclaration;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.InterfaceDefinition;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.IntersectAssignOp;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.IntersectionOp;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.IntersectionType;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.InvocationExpression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.IsCase;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.IsCondition;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.IsOp;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.KeyValueIterator;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Lambda;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.LargeAsOp;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.LargerOp;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Literal;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.LocalModifier;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.LogicalAssignmentOp;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.LogicalOp;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.MatchCase;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.MemberOp;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.MemberOperator;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.MemberOrTypeExpression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.MethodArgument;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.MethodDeclaration;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.MethodDefinition;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.MultiplyAssignOp;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.NamedArgument;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.NamedArgumentList;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.NaturalLiteral;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.NegativeOp;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Nonempty;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.NonemptyCondition;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.NotEqualOp;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.NotOp;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ObjectArgument;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ObjectDefinition;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.OperatorExpression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.OrAssignOp;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.OrOp;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Outer;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Parameter;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ParameterList;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.PositionalArgument;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.PositionalArgumentList;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.PositiveOp;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.PostfixDecrementOp;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.PostfixExpression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.PostfixIncrementOp;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.PostfixOperatorExpression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.PowerOp;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.PrefixOperatorExpression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Primary;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ProductOp;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.QualifiedMemberExpression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.QualifiedMemberOrTypeExpression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.QualifiedType;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.QualifiedTypeExpression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.QuotedLiteral;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.QuotientOp;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.RangeOp;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.RemainderAssignOp;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.RemainderOp;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Resource;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Return;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.SafeIndexOp;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.SafeMemberOp;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.SatisfiedTypes;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Satisfies;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.SatisfiesCase;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.SatisfiesCondition;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.SelfExpression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.SequenceEnumeration;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.SequencedArgument;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.SequencedType;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.SequencedTypeParameterDeclaration;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.SimpleType;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.SmallAsOp;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.SmallerOp;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.SpecifiedArgument;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.SpecifierExpression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.SpecifierOrInitializerExpression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.SpecifierStatement;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.SpreadOp;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Statement;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.StatementOrArgument;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.StaticMemberOrTypeExpression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.StaticType;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.StringLiteral;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.StringTemplate;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.SubtractAssignOp;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.SumOp;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Super;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.SuperType;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.SwitchCaseList;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.SwitchClause;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.SwitchStatement;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.SyntheticVariable;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Term;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ThenOp;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.This;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Throw;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.TryCatchStatement;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.TryClause;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Type;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.TypeArgumentList;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.TypeArguments;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.TypeConstraint;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.TypeConstraintList;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.TypeDeclaration;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.TypeOperatorExpression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.TypeParameterDeclaration;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.TypeParameterList;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.TypeSpecifier;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.TypeVariance;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.TypedArgument;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.TypedDeclaration;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.UnaryOperatorExpression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.UnionAssignOp;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.UnionOp;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.UnionType;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ValueIterator;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ValueModifier;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ValueParameterDeclaration;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Variable;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.VoidModifier;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.WhileClause;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.WhileStatement;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.XorAssignOp;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.XorOp;

public abstract class VisitorPlugin extends Visitor {
    protected PluggableVisitor.PluggableVisitorContext context;
    
    protected void setContext(PluggableVisitor.PluggableVisitorContext context) {
        this.context = context;
    }
    
    @Override
    final public void visitAny(Node that) {
        // We're a plugin, so we don't do anything here!
    }

    @Override
    public void visit(CompilationUnit that) {
    }

    @Override
    public void visit(ImportList that) {
    }

    @Override
    public void visit(Import that) {
    }

    @Override
    public void visit(ImportPath that) {
    }

    @Override
    public void visit(ImportMemberOrTypeList that) {
    }

    @Override
    public void visit(ImportMemberOrType that) {
    }

    @Override
    public void visit(ImportMember that) {
    }

    @Override
    public void visit(ImportType that) {
    }

    @Override
    public void visit(Alias that) {
    }

    @Override
    public void visit(Implicit that) {
    }

    @Override
    public void visit(ImportWildcard that) {
    }

    @Override
    public void visit(Declaration that) {
    }

    @Override
    public void visit(TypeDeclaration that) {
    }

    @Override
    public void visit(ClassOrInterface that) {
    }

    @Override
    public void visit(SatisfiedTypes that) {
    }

    @Override
    public void visit(AbstractedType that) {
    }

    @Override
    public void visit(AdaptedTypes that) {
    }

    @Override
    public void visit(CaseTypes that) {
    }

    @Override
    public void visit(ExtendedType that) {
    }

    @Override
    public void visit(TypeConstraintList that) {
    }

    @Override
    public void visit(TypeConstraint that) {
    }

    @Override
    public void visit(TypeSpecifier that) {
    }

    @Override
    public void visit(AnyClass that) {
    }

    @Override
    public void visit(ClassDefinition that) {
    }

    @Override
    public void visit(ClassDeclaration that) {
    }

    @Override
    public void visit(AnyInterface that) {
    }

    @Override
    public void visit(InterfaceDefinition that) {
    }

    @Override
    public void visit(InterfaceDeclaration that) {
    }

    @Override
    public void visit(TypedDeclaration that) {
    }

    @Override
    public void visit(AnyAttribute that) {
    }

    @Override
    public void visit(AttributeDeclaration that) {
    }

    @Override
    public void visit(AttributeGetterDefinition that) {
    }

    @Override
    public void visit(AttributeSetterDefinition that) {
    }

    @Override
    public void visit(AnyMethod that) {
    }

    @Override
    public void visit(MethodDefinition that) {
    }

    @Override
    public void visit(MethodDeclaration that) {
    }

    @Override
    public void visit(VoidModifier that) {
    }

    @Override
    public void visit(ObjectDefinition that) {
    }

    @Override
    public void visit(ParameterList that) {
    }

    @Override
    public void visit(Parameter that) {
    }

    @Override
    public void visit(DefaultArgument that) {
    }

    @Override
    public void visit(ValueParameterDeclaration that) {
    }

    @Override
    public void visit(FunctionalParameterDeclaration that) {
    }

    @Override
    public void visit(TypeParameterList that) {
    }

    @Override
    public void visit(TypeParameterDeclaration that) {
    }

    @Override
    public void visit(TypeVariance that) {
    }

    @Override
    public void visit(SequencedTypeParameterDeclaration that) {
    }

    @Override
    public void visit(Body that) {
    }

    @Override
    public void visit(Block that) {
    }

    @Override
    public void visit(ClassBody that) {
    }

    @Override
    public void visit(InterfaceBody that) {
    }

    @Override
    public void visit(Type that) {
    }

    @Override
    public void visit(StaticType that) {
    }

    @Override
    public void visit(SimpleType that) {
    }

    @Override
    public void visit(BaseType that) {
    }

    @Override
    public void visit(UnionType that) {
    }

    @Override
    public void visit(IntersectionType that) {
    }

    @Override
    public void visit(QualifiedType that) {
    }

    @Override
    public void visit(SuperType that) {
    }

    @Override
    public void visit(LocalModifier that) {
    }

    @Override
    public void visit(ValueModifier that) {
    }

    @Override
    public void visit(FunctionModifier that) {
    }

    @Override
    public void visit(SyntheticVariable that) {
    }

    @Override
    public void visit(TypeArguments that) {
    }

    @Override
    public void visit(TypeArgumentList that) {
    }

    @Override
    public void visit(InferredTypeArguments that) {
    }

    @Override
    public void visit(SequencedType that) {
    }

    @Override
    public void visit(Directive that) {
    }

    @Override
    public void visit(Return that) {
    }

    @Override
    public void visit(Throw that) {
    }

    @Override
    public void visit(Continue that) {
    }

    @Override
    public void visit(Break that) {
    }

    @Override
    public void visit(StatementOrArgument that) {
    }

    @Override
    public void visit(Statement that) {
    }

    @Override
    public void visit(CompilerAnnotation that) {
    }

    @Override
    public void visit(ExecutableStatement that) {
    }

    @Override
    public void visit(SpecifierStatement that) {
    }

    @Override
    public void visit(ExpressionStatement that) {
    }

    @Override
    public void visit(ControlStatement that) {
    }

    @Override
    public void visit(ControlClause that) {
    }

    @Override
    public void visit(IfStatement that) {
    }

    @Override
    public void visit(IfClause that) {
    }

    @Override
    public void visit(ElseClause that) {
    }

    @Override
    public void visit(SwitchStatement that) {
    }

    @Override
    public void visit(SwitchClause that) {
    }

    @Override
    public void visit(SwitchCaseList that) {
    }

    @Override
    public void visit(CaseClause that) {
    }

    @Override
    public void visit(CaseItem that) {
    }

    @Override
    public void visit(MatchCase that) {
    }

    @Override
    public void visit(IsCase that) {
    }

    @Override
    public void visit(SatisfiesCase that) {
    }

    @Override
    public void visit(TryCatchStatement that) {
    }

    @Override
    public void visit(TryClause that) {
    }

    @Override
    public void visit(CatchClause that) {
    }

    @Override
    public void visit(FinallyClause that) {
    }

    @Override
    public void visit(Resource that) {
    }

    @Override
    public void visit(CatchVariable that) {
    }

    @Override
    public void visit(ForStatement that) {
    }

    @Override
    public void visit(ForClause that) {
    }

    @Override
    public void visit(ForIterator that) {
    }

    @Override
    public void visit(ValueIterator that) {
    }

    @Override
    public void visit(KeyValueIterator that) {
    }

    @Override
    public void visit(WhileStatement that) {
    }

    @Override
    public void visit(WhileClause that) {
    }

    @Override
    public void visit(Condition that) {
    }

    @Override
    public void visit(BooleanCondition that) {
    }

    @Override
    public void visit(ExistsOrNonemptyCondition that) {
    }

    @Override
    public void visit(ExistsCondition that) {
    }

    @Override
    public void visit(NonemptyCondition that) {
    }

    @Override
    public void visit(IsCondition that) {
    }

    @Override
    public void visit(SatisfiesCondition that) {
    }

    @Override
    public void visit(Variable that) {
    }

    @Override
    public void visit(Term that) {
    }

    @Override
    public void visit(OperatorExpression that) {
    }

    @Override
    public void visit(BinaryOperatorExpression that) {
    }

    @Override
    public void visit(ArithmeticOp that) {
    }

    @Override
    public void visit(SumOp that) {
    }

    @Override
    public void visit(DifferenceOp that) {
    }

    @Override
    public void visit(ProductOp that) {
    }

    @Override
    public void visit(QuotientOp that) {
    }

    @Override
    public void visit(PowerOp that) {
    }

    @Override
    public void visit(RemainderOp that) {
    }

    @Override
    public void visit(AssignmentOp that) {
    }

    @Override
    public void visit(AssignOp that) {
    }

    @Override
    public void visit(ArithmeticAssignmentOp that) {
    }

    @Override
    public void visit(AddAssignOp that) {
    }

    @Override
    public void visit(SubtractAssignOp that) {
    }

    @Override
    public void visit(MultiplyAssignOp that) {
    }

    @Override
    public void visit(DivideAssignOp that) {
    }

    @Override
    public void visit(RemainderAssignOp that) {
    }

    @Override
    public void visit(BitwiseAssignmentOp that) {
    }

    @Override
    public void visit(IntersectAssignOp that) {
    }

    @Override
    public void visit(UnionAssignOp that) {
    }

    @Override
    public void visit(XorAssignOp that) {
    }

    @Override
    public void visit(ComplementAssignOp that) {
    }

    @Override
    public void visit(LogicalAssignmentOp that) {
    }

    @Override
    public void visit(AndAssignOp that) {
    }

    @Override
    public void visit(OrAssignOp that) {
    }

    @Override
    public void visit(LogicalOp that) {
    }

    @Override
    public void visit(AndOp that) {
    }

    @Override
    public void visit(OrOp that) {
    }

    @Override
    public void visit(BitwiseOp that) {
    }

    @Override
    public void visit(IntersectionOp that) {
    }

    @Override
    public void visit(UnionOp that) {
    }

    @Override
    public void visit(XorOp that) {
    }

    @Override
    public void visit(ComplementOp that) {
    }

    @Override
    public void visit(EqualityOp that) {
    }

    @Override
    public void visit(EqualOp that) {
    }

    @Override
    public void visit(NotEqualOp that) {
    }

    @Override
    public void visit(ComparisonOp that) {
    }

    @Override
    public void visit(LargerOp that) {
    }

    @Override
    public void visit(SmallerOp that) {
    }

    @Override
    public void visit(LargeAsOp that) {
    }

    @Override
    public void visit(SmallAsOp that) {
    }

    @Override
    public void visit(DefaultOp that) {
    }

    @Override
    public void visit(ThenOp that) {
    }

    @Override
    public void visit(IdenticalOp that) {
    }

    @Override
    public void visit(EntryOp that) {
    }

    @Override
    public void visit(RangeOp that) {
    }

    @Override
    public void visit(CompareOp that) {
    }

    @Override
    public void visit(InOp that) {
    }

    @Override
    public void visit(UnaryOperatorExpression that) {
    }

    @Override
    public void visit(NotOp that) {
    }

    @Override
    public void visit(Exists that) {
    }

    @Override
    public void visit(Nonempty that) {
    }

    @Override
    public void visit(FlipOp that) {
    }

    @Override
    public void visit(NegativeOp that) {
    }

    @Override
    public void visit(PositiveOp that) {
    }

    @Override
    public void visit(TypeOperatorExpression that) {
    }

    @Override
    public void visit(IsOp that) {
    }

    @Override
    public void visit(Satisfies that) {
    }

    @Override
    public void visit(Extends that) {
    }

    @Override
    public void visit(PrefixOperatorExpression that) {
    }

    @Override
    public void visit(IncrementOp that) {
    }

    @Override
    public void visit(DecrementOp that) {
    }

    @Override
    public void visit(PostfixOperatorExpression that) {
    }

    @Override
    public void visit(PostfixIncrementOp that) {
    }

    @Override
    public void visit(PostfixDecrementOp that) {
    }

    @Override
    public void visit(ExpressionList that) {
    }

    @Override
    public void visit(Expression that) {
    }

    @Override
    public void visit(Primary that) {
    }

    @Override
    public void visit(PostfixExpression that) {
    }

    @Override
    public void visit(InvocationExpression that) {
    }

    @Override
    public void visit(MemberOrTypeExpression that) {
    }

    @Override
    public void visit(ExtendedTypeExpression that) {
    }

    @Override
    public void visit(StaticMemberOrTypeExpression that) {
    }

    @Override
    public void visit(BaseMemberOrTypeExpression that) {
    }

    @Override
    public void visit(BaseMemberExpression that) {
    }

    @Override
    public void visit(BaseTypeExpression that) {
    }

    @Override
    public void visit(QualifiedMemberOrTypeExpression that) {
    }

    @Override
    public void visit(QualifiedMemberExpression that) {
    }

    @Override
    public void visit(QualifiedTypeExpression that) {
    }

    @Override
    public void visit(MemberOperator that) {
    }

    @Override
    public void visit(MemberOp that) {
    }

    @Override
    public void visit(SafeMemberOp that) {
    }

    @Override
    public void visit(SpreadOp that) {
    }

    @Override
    public void visit(IndexExpression that) {
    }

    @Override
    public void visit(IndexOperator that) {
    }

    @Override
    public void visit(IndexOp that) {
    }

    @Override
    public void visit(SafeIndexOp that) {
    }

    @Override
    public void visit(ElementOrRange that) {
    }

    @Override
    public void visit(Element that) {
    }

    @Override
    public void visit(ElementRange that) {
    }

    @Override
    public void visit(Outer that) {
    }

    @Override
    public void visit(ArgumentList that) {
    }

    @Override
    public void visit(NamedArgumentList that) {
    }

    @Override
    public void visit(SequencedArgument that) {
    }

    @Override
    public void visit(PositionalArgumentList that) {
    }

    @Override
    public void visit(Ellipsis that) {
    }

    @Override
    public void visit(PositionalArgument that) {
    }

    @Override
    public void visit(FunctionArgument that) {
    }

    @Override
    public void visit(NamedArgument that) {
    }

    @Override
    public void visit(SpecifiedArgument that) {
    }

    @Override
    public void visit(TypedArgument that) {
    }

    @Override
    public void visit(MethodArgument that) {
    }

    @Override
    public void visit(AttributeArgument that) {
    }

    @Override
    public void visit(ObjectArgument that) {
    }

    @Override
    public void visit(SpecifierOrInitializerExpression that) {
    }

    @Override
    public void visit(SpecifierExpression that) {
    }

    @Override
    public void visit(InitializerExpression that) {
    }

    @Override
    public void visit(Atom that) {
    }

    @Override
    public void visit(Literal that) {
    }

    @Override
    public void visit(NaturalLiteral that) {
    }

    @Override
    public void visit(FloatLiteral that) {
    }

    @Override
    public void visit(CharLiteral that) {
    }

    @Override
    public void visit(StringLiteral that) {
    }

    @Override
    public void visit(QuotedLiteral that) {
    }

    @Override
    public void visit(SelfExpression that) {
    }

    @Override
    public void visit(This that) {
    }

    @Override
    public void visit(Super that) {
    }

    @Override
    public void visit(SequenceEnumeration that) {
    }

    @Override
    public void visit(StringTemplate that) {
    }

    @Override
    public void visit(Lambda that) {
    }

    @Override
    public void visit(Annotation that) {
    }

    @Override
    public void visit(AnnotationList that) {
    }

    @Override
    public void visit(Identifier that) {
    }

    @Override
    public void visit(Comprehension that) {
    }

    @Override
    public void visit(ComprehensionClause that) {
    }

    @Override
    public void visit(ExpressionComprehensionClause that) {
    }

    @Override
    public void visit(ForComprehensionClause that) {
    }

    @Override
    public void visit(IfComprehensionClause that) {
    }

    
}
