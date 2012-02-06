package com.redhat.ceylon.compiler.java.codegen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import com.redhat.ceylon.compiler.typechecker.model.Scope;
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

public class PluggableVisitor extends Visitor {
    private List<VisitorPlugin> plugins = new ArrayList<VisitorPlugin>();
    private PluggableVisitorContext context = new PluggableVisitorContext();
    private Stack<PluggableVisitorScope> scopes = new Stack<PluggableVisitorScope>();
    
    public class PluggableVisitorContext {
        Stack<PluggableVisitorScope> scopes() {
            return scopes;
        }
        
        PluggableVisitorScope getScope() {
            return scopes.peek();
        }
    }

    public class PluggableVisitorScope {
        public Scope scope;
        public Map<String, Node> userData;
        
        public PluggableVisitorScope(Scope scope) {
            this.scope = scope;
            userData = new HashMap<String, Node>();
        }
        
    }

    public void add(VisitorPlugin plugin) {
        plugin.setContext(context);
        plugins.add(plugin);
    }
    
    @Override
    public void visit(CompilationUnit that) {
        scopes.push(new PluggableVisitorScope(that.getScope()));
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
        scopes.pop();
    }

    @Override
    public void visit(ImportList that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(Import that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(ImportPath that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(ImportMemberOrTypeList that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(ImportMemberOrType that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(ImportMember that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(ImportType that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(Alias that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(Implicit that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(ImportWildcard that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(Declaration that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(TypeDeclaration that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(ClassOrInterface that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(SatisfiedTypes that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(AbstractedType that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(AdaptedTypes that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(CaseTypes that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(ExtendedType that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(TypeConstraintList that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(TypeConstraint that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(TypeSpecifier that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(AnyClass that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(ClassDefinition that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(ClassDeclaration that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(AnyInterface that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(InterfaceDefinition that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(InterfaceDeclaration that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(TypedDeclaration that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(AnyAttribute that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(AttributeDeclaration that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(AttributeGetterDefinition that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(AttributeSetterDefinition that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(AnyMethod that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(MethodDefinition that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(MethodDeclaration that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(VoidModifier that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(ObjectDefinition that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(ParameterList that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(Parameter that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(DefaultArgument that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(ValueParameterDeclaration that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(FunctionalParameterDeclaration that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(TypeParameterList that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(TypeParameterDeclaration that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(TypeVariance that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(SequencedTypeParameterDeclaration that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(Body that) {
        scopes.push(new PluggableVisitorScope(that.getScope()));
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
        scopes.pop();
    }

    @Override
    public void visit(Block that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(ClassBody that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(InterfaceBody that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(Type that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(StaticType that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(SimpleType that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(BaseType that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(UnionType that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(IntersectionType that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(QualifiedType that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(SuperType that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(LocalModifier that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(ValueModifier that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(FunctionModifier that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(SyntheticVariable that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(TypeArguments that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(TypeArgumentList that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(InferredTypeArguments that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(SequencedType that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(Directive that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(Return that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(Throw that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(Continue that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(Break that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(StatementOrArgument that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(Statement that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(CompilerAnnotation that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(ExecutableStatement that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(SpecifierStatement that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(ExpressionStatement that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(ControlStatement that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(ControlClause that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(IfStatement that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(IfClause that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(ElseClause that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(SwitchStatement that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(SwitchClause that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(SwitchCaseList that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(CaseClause that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(CaseItem that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(MatchCase that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(IsCase that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(SatisfiesCase that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(TryCatchStatement that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(TryClause that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(CatchClause that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(FinallyClause that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(Resource that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(CatchVariable that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(ForStatement that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(ForClause that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(ForIterator that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(ValueIterator that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(KeyValueIterator that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(WhileStatement that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(WhileClause that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(Condition that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(BooleanCondition that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(ExistsOrNonemptyCondition that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(ExistsCondition that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(NonemptyCondition that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(IsCondition that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(SatisfiesCondition that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(Variable that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(Term that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(OperatorExpression that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(BinaryOperatorExpression that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(ArithmeticOp that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(SumOp that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(DifferenceOp that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(ProductOp that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(QuotientOp that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(PowerOp that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(RemainderOp that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(AssignmentOp that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(AssignOp that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(ArithmeticAssignmentOp that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(AddAssignOp that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(SubtractAssignOp that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(MultiplyAssignOp that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(DivideAssignOp that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(RemainderAssignOp that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(BitwiseAssignmentOp that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(IntersectAssignOp that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(UnionAssignOp that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(XorAssignOp that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(ComplementAssignOp that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(LogicalAssignmentOp that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(AndAssignOp that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(OrAssignOp that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(LogicalOp that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(AndOp that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(OrOp that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(BitwiseOp that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(IntersectionOp that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(UnionOp that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(XorOp that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(ComplementOp that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(EqualityOp that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(EqualOp that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(NotEqualOp that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(ComparisonOp that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(LargerOp that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(SmallerOp that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(LargeAsOp that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(SmallAsOp that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(DefaultOp that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(ThenOp that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(IdenticalOp that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(EntryOp that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(RangeOp that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(CompareOp that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(InOp that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(UnaryOperatorExpression that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(NotOp that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(Exists that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(Nonempty that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(FlipOp that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(NegativeOp that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(PositiveOp that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(TypeOperatorExpression that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(IsOp that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(Satisfies that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(Extends that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(PrefixOperatorExpression that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(IncrementOp that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(DecrementOp that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(PostfixOperatorExpression that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(PostfixIncrementOp that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(PostfixDecrementOp that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(ExpressionList that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(Expression that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(Primary that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(PostfixExpression that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(InvocationExpression that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(MemberOrTypeExpression that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(ExtendedTypeExpression that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(StaticMemberOrTypeExpression that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(BaseMemberOrTypeExpression that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(BaseMemberExpression that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(BaseTypeExpression that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(QualifiedMemberOrTypeExpression that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(QualifiedMemberExpression that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(QualifiedTypeExpression that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(MemberOperator that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(MemberOp that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(SafeMemberOp that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(SpreadOp that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(IndexExpression that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(IndexOperator that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(IndexOp that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(SafeIndexOp that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(ElementOrRange that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(Element that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(ElementRange that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(Outer that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(ArgumentList that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(NamedArgumentList that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(SequencedArgument that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(PositionalArgumentList that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(Ellipsis that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(PositionalArgument that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(FunctionArgument that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(NamedArgument that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(SpecifiedArgument that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(TypedArgument that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(MethodArgument that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(AttributeArgument that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(ObjectArgument that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(SpecifierOrInitializerExpression that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(SpecifierExpression that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(InitializerExpression that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(Atom that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(Literal that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(NaturalLiteral that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(FloatLiteral that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(CharLiteral that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(StringLiteral that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(QuotedLiteral that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(SelfExpression that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(This that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(Super that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(SequenceEnumeration that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(StringTemplate that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(Lambda that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(Annotation that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(AnnotationList that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(Identifier that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(Comprehension that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(ComprehensionClause that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(ExpressionComprehensionClause that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(ForComprehensionClause that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }

    @Override
    public void visit(IfComprehensionClause that) {
        super.visit(that);
        for (Visitor plugin : plugins) {
            plugin.visit(that);
        }
    }


}
