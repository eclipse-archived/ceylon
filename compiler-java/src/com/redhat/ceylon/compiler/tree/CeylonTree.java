package com.redhat.ceylon.compiler.tree;

import java.io.StringWriter;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import org.antlr.runtime.Token;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;

import com.redhat.ceylon.compiler.parser.CeylonParser;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;

public abstract class CeylonTree {
  
    interface Annotation {
    }
    
    public abstract static class Declaration extends CeylonTree {
        public List<CeylonTree> typeParameters;
        public List<CeylonTree> typeConstraintList;
        
        public void addTypeConstraint(CeylonTree t) {
            if (typeConstraintList == null)
                typeConstraintList = List.<CeylonTree>nil();
            typeConstraintList = typeConstraintList.append(t);
        }

        public void setTypeParameterList(List<CeylonTree> typeParameters) {
            this.typeParameters = typeParameters;            
        }
    }
    
    interface IType {
    }

    /**
     * Create a Ceylon compilation unit from an ANTLR tree.
     */
    public static CompilationUnit build(Tree src) {
        Token token = ((CommonTree) src).getToken();
        if (token != null) {
            // ANTLR doesn't create a null top-level node when it
            // would only have one child.  We want one always, to
            // map to the compilation unit, so we create one where
            // necessary.
            Tree tmp = new CommonTree((Token) null);
            tmp.addChild(src);
            src = tmp;
        }
        return (CompilationUnit) consume(src);
    }

    /**
     * Create a Ceylon tree from an ANTLR tree.
     */
    private static CeylonTree consume(Tree src) {
        Token token = ((CommonTree) src).getToken();

        Class<? extends CeylonTree> klass;
        if (token == null) {
            klass = CompilationUnit.class;
        }
        else {
            int type = token.getType();
            klass = classes.get(type);
            assert klass != null : type + ": " + CeylonParser.tokenNames[type];
        }

        CeylonTree dst;
        try {
            dst = klass.newInstance();
        }
        catch (InstantiationException e) {
            throw new RuntimeException(e);
        }
        catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        dst.token = token;

        ListBuffer<CeylonTree> children = new ListBuffer<CeylonTree>();
        for (int i = 0; i < src.getChildCount(); i++) {
            CeylonTree child = consume(src.getChild(i));
            child.parent = dst;
            children.append(child);
        }
        dst.children = children.toList();

        return dst;
    }

    /**
     * Mapping of ANTLR tokens to CeylonTree subclasses.
     */
    private static Map<Integer, Class<? extends CeylonTree>> classes;

    static {
        classes = new HashMap<Integer, Class<? extends CeylonTree>>();

        classes.put(CeylonParser.ABSTRACT, Abstract.class);
        classes.put(CeylonParser.ABSTRACTS_LIST, AbstractsList.class);
        classes.put(CeylonParser.ABSTRACT_MEMBER_DECL,
                    AbstractMemberDeclaration.class);
        classes.put(CeylonParser.ALIAS_DECL, AliasDeclaration.class);
        classes.put(CeylonParser.AND, Operator.class);
        classes.put(CeylonParser.ANDEQ, Operator.class);
        classes.put(CeylonParser.ANNOTATION_LIST, AnnotationList.class);
        classes.put(CeylonParser.ANNOTATION_NAME, AnnotationName.class);
        classes.put(CeylonParser.ANON_METH, AnonymousMethod.class);
        classes.put(CeylonParser.ARG_LIST, ArgumentList.class);
        classes.put(CeylonParser.ARG_NAME, ArgumentName.class);
        classes.put(CeylonParser.ASSIGN, Assign.class);
        classes.put(CeylonParser.ATTRIBUTE_SETTER, AttributeSetter.class);
        classes.put(CeylonParser.BITWISEAND, Operator.class);
        classes.put(CeylonParser.BITWISEANDEQ, Operator.class);
        classes.put(CeylonParser.BITWISENOT, Operator.class);
        classes.put(CeylonParser.BITWISEOR, Operator.class);
        classes.put(CeylonParser.BITWISEOREQ, Operator.class);
        classes.put(CeylonParser.BITWISEXOR, Operator.class);
        classes.put(CeylonParser.BITWISEXOREQ, Operator.class);
        classes.put(CeylonParser.BLOCK, Block.class);
        classes.put(CeylonParser.BREAK, Break.class);
        classes.put(CeylonParser.BREAK_STMT, BreakStatement.class);
        classes.put(CeylonParser.CALL_EXPR, CallExpression.class);
        classes.put(CeylonParser.CASE, Case.class);
        classes.put(CeylonParser.CASE_DEFAULT, CaseDefault.class);
        classes.put(CeylonParser.CASE_ITEM, CaseItem.class);
        classes.put(CeylonParser.CATCH, Catch.class);
        classes.put(CeylonParser.CATCH_BLOCK, CatchBlock.class);
        classes.put(CeylonParser.CATCH_STMT, CatchStatement.class);
        classes.put(CeylonParser.CHARLITERAL, CharLiteral.class);
        classes.put(CeylonParser.CHAR_CST, CharConstant.class);
        classes.put(CeylonParser.CLASS, Klass.class);
        classes.put(CeylonParser.CLASS_BODY, ClassBody.class);
        classes.put(CeylonParser.CLASS_DECL, ClassDeclaration.class);
        classes.put(CeylonParser.COLON, Operator.class);
        classes.put(CeylonParser.COLONEQ, Operator.class);
        classes.put(CeylonParser.COMMA, Operator.class);
        classes.put(CeylonParser.COMPARE, Operator.class);
        classes.put(CeylonParser.CONDITION, Condition.class);
        classes.put(CeylonParser.CONTINUE, Continue.class);
        classes.put(CeylonParser.DECREMENT, Operator.class);
        classes.put(CeylonParser.DEFAULT, Default.class);
        classes.put(CeylonParser.DIVIDED, Operator.class);
        classes.put(CeylonParser.DIVIDEDEQ, Operator.class);
        classes.put(CeylonParser.DO, Do.class);
        classes.put(CeylonParser.DOT, OperatorDot.class);
        classes.put(CeylonParser.DOTEQ, Operator.class);
        classes.put(CeylonParser.DO_BLOCK, DoBlock.class);
        classes.put(CeylonParser.DO_ITERATOR, DoIterator.class);
        classes.put(CeylonParser.ELLIPSIS, Operator.class);
        classes.put(CeylonParser.ELSE, Else.class);
        classes.put(CeylonParser.ENTRY, Operator.class);
        classes.put(CeylonParser.ENUM_LIST, EnumList.class);
        classes.put(CeylonParser.EQ, Operator.class);
        classes.put(CeylonParser.EQEQ, Operator.class);
        classes.put(CeylonParser.EXISTS, Exists.class);
        classes.put(CeylonParser.EXISTS_EXPR, ExistsExpression.class);
        classes.put(CeylonParser.EXPR, Expression.class);
        classes.put(CeylonParser.EXPR_LIST, ExpressionList.class);
        classes.put(CeylonParser.EXTENDS, Extends.class);
        classes.put(CeylonParser.EXTENSION, Extension.class);
        classes.put(CeylonParser.FAIL_BLOCK, FailBlock.class);
        classes.put(CeylonParser.FINALLY, Finally.class);
        classes.put(CeylonParser.FINALLY_BLOCK, FinallyBlock.class);
        classes.put(CeylonParser.FLOATLITERAL, FloatLiteral.class);
        classes.put(CeylonParser.FLOAT_CST, FloatConstant.class);
        classes.put(CeylonParser.FOR, For.class);
        classes.put(CeylonParser.FORMAL_PARAMETER, FormalParameter.class);
        classes.put(CeylonParser.FORMAL_PARAMETER_LIST, FormalParameterList.class);
        classes.put(CeylonParser.FOR_CONTAINMENT, ForContainment.class);
        classes.put(CeylonParser.FOR_ITERATOR, ForIterator.class);
        classes.put(CeylonParser.FOR_STMT, ForStatement.class);
        classes.put(CeylonParser.GET, Get.class);
        classes.put(CeylonParser.GET_EXPR, GetExpression.class);
        classes.put(CeylonParser.GT, Operator.class);
        classes.put(CeylonParser.GTEQ, Operator.class);
        classes.put(CeylonParser.HASH, Operator.class);
        classes.put(CeylonParser.IDENTICAL, Operator.class);
        classes.put(CeylonParser.IF, If.class);
        classes.put(CeylonParser.IF_FALSE, IfFalse.class);
        classes.put(CeylonParser.IF_STMT, IfStatement.class);
        classes.put(CeylonParser.IF_TRUE, IfTrue.class);
        classes.put(CeylonParser.IMPLIES, Operator.class);
        classes.put(CeylonParser.IMPORT, Import.class);
        classes.put(CeylonParser.IMPORT_DECL, ImportDeclaration.class);
        classes.put(CeylonParser.IMPORT_LIST, ImportList.class);
        classes.put(CeylonParser.IMPORT_PATH, ImportPath.class);
        classes.put(CeylonParser.IMPORT_WILDCARD, ImportWildcard.class);
        classes.put(CeylonParser.IN, Operator.class);
        classes.put(CeylonParser.INCREMENT, Operator.class);
        classes.put(CeylonParser.INIT_EXPR, InitializerExpression.class);
        classes.put(CeylonParser.INST_DECL, InstanceDeclaration.class);
        classes.put(CeylonParser.INTERFACE, Interface.class);
        classes.put(CeylonParser.INTERFACE_DECL, InterfaceDeclaration.class);
        classes.put(CeylonParser.INT_CST, IntConstant.class);
        classes.put(CeylonParser.IN, Operator.class);
        classes.put(CeylonParser.IS, Operator.class);
        classes.put(CeylonParser.IS_EXPR, IsExpression.class);
        classes.put(CeylonParser.LANG_ANNOTATION, LanguageAnnotation.class);
        classes.put(CeylonParser.LBRACE, Operator.class);
        classes.put(CeylonParser.LBRACKET, Operator.class);
        classes.put(CeylonParser.LIDENTIFIER, Identifier.class);
        classes.put(CeylonParser.LINE_COMMENT, SingleLineComment.class);
        classes.put(CeylonParser.LOCAL, Local.class);
        classes.put(CeylonParser.LOOP_BLOCK, LoopBlock.class);
        classes.put(CeylonParser.LOWER_BOUND, LowerBound.class);
        classes.put(CeylonParser.LPAREN, Operator.class);
        classes.put(CeylonParser.LT, Operator.class);
        classes.put(CeylonParser.LTEQ, Operator.class);
        classes.put(CeylonParser.MEMBER_DECL, MemberDeclaration.class);
        classes.put(CeylonParser.MEMBER_NAME, MemberName.class);
        classes.put(CeylonParser.MEMBER_TYPE, MemberType.class);
        classes.put(CeylonParser.MINUS, Operator.class);
        classes.put(CeylonParser.MINUSEQ, Operator.class);
        classes.put(CeylonParser.MODULE, Module.class);
        classes.put(CeylonParser.MULTI_COMMENT, MultiLineComment.class);
        classes.put(CeylonParser.MUTABLE, Mutable.class);
        classes.put(CeylonParser.NAMED_ARG, NamedArgument.class);
        classes.put(CeylonParser.NATURALLITERAL, NaturalLiteral.class);
        classes.put(CeylonParser.NIL, Nil.class);
        classes.put(CeylonParser.NONE, None.class);
        classes.put(CeylonParser.NONEMPTY, Nonempty.class);
        classes.put(CeylonParser.NONEMPTY_EXPR, NonemptyExpression.class);
        classes.put(CeylonParser.NOT, Operator.class);
        classes.put(CeylonParser.NOTEQ, Operator.class);
        classes.put(CeylonParser.NULL, Null.class);
        classes.put(CeylonParser.OPTIONAL, Optional.class);
        classes.put(CeylonParser.OR, Operator.class);
        classes.put(CeylonParser.OREQ, Operator.class);
        classes.put(CeylonParser.OUT, Out.class);
        classes.put(CeylonParser.OVERRIDE, Override.class);
        classes.put(CeylonParser.PACKAGE, Package.class);
        classes.put(CeylonParser.PLUS, Operator.class);
        classes.put(CeylonParser.PLUSEQ, Operator.class);
        classes.put(CeylonParser.POSTFIX_EXPR, PostfixExpression.class);
        classes.put(CeylonParser.POWER, Operator.class);
        classes.put(CeylonParser.PREFIX_EXPR, PrefixExpression.class);
        classes.put(CeylonParser.PRIVATE, Private.class);
        classes.put(CeylonParser.PUBLIC, Public.class);
        classes.put(CeylonParser.QMARK, Operator.class);
        classes.put(CeylonParser.QMARKEQ, Operator.class);
        classes.put(CeylonParser.QUESDOT, Operator.class);
        classes.put(CeylonParser.QUOTEDLITERAL, QuotedLiteral.class);
        classes.put(CeylonParser.QUOTE_CST, QuoteConstant.class);
        classes.put(CeylonParser.RANGE, Operator.class);
        classes.put(CeylonParser.RBRACE, Operator.class);
        classes.put(CeylonParser.RBRACKET, Operator.class);
        classes.put(CeylonParser.REFLECTED_LITERAL, ReflectedLiteral.class);
        classes.put(CeylonParser.REMAINDER, Operator.class);
        classes.put(CeylonParser.REMAINDEREQ, Operator.class);
        classes.put(CeylonParser.RENDER, Operator.class);
        classes.put(CeylonParser.RETRY, Retry.class);
        classes.put(CeylonParser.RETRY_STMT, RetryStatement.class);
        classes.put(CeylonParser.RETURN, Return.class);
        classes.put(CeylonParser.RET_STMT, ReturnStatement.class);
        classes.put(CeylonParser.RPAREN, Operator.class);
        classes.put(CeylonParser.SATISFIES, Satisfies.class);
        classes.put(CeylonParser.SATISFIES_LIST, SatisfiesList.class);
        classes.put(CeylonParser.SELECTOR_LIST, SelectorList.class);
        classes.put(CeylonParser.SEMI, Operator.class);
        classes.put(CeylonParser.SET, Set.class);
        classes.put(CeylonParser.SET_EXPR, SetExpression.class);
        classes.put(CeylonParser.SIMPLESTRINGLITERAL, SimpleStringLiteral.class);
        classes.put(CeylonParser.STARDOT, Operator.class);
        classes.put(CeylonParser.STRING_CONCAT, StringConcatenation.class);
        classes.put(CeylonParser.STRING_CST, StringConstant.class);
        classes.put(CeylonParser.SUBSCRIPT_EXPR, SubscriptExpression.class);
        classes.put(CeylonParser.SUBTYPE, Subtype.class);
        classes.put(CeylonParser.SUPER, Super.class);
        classes.put(CeylonParser.SUPERCLASS, Superclass.class);
        classes.put(CeylonParser.SWITCH, Switch.class);
        classes.put(CeylonParser.SWITCH_CASE_LIST, SwitchCaseList.class);
        classes.put(CeylonParser.SWITCH_EXPR, SwitchExpression.class);
        classes.put(CeylonParser.SWITCH_STMT, SwitchStatement.class);
        classes.put(CeylonParser.THIS, This.class);
        classes.put(CeylonParser.THROW, Throw.class);
        classes.put(CeylonParser.THROW_STMT, ThrowStatement.class);
        classes.put(CeylonParser.TIMES, Operator.class);
        classes.put(CeylonParser.TIMESEQ, Operator.class);
        classes.put(CeylonParser.TRY, Try.class);
        classes.put(CeylonParser.TRY_BLOCK, TryBlock.class);
        classes.put(CeylonParser.TRY_CATCH_STMT, TryCatchStatement.class);
        classes.put(CeylonParser.TRY_RESOURCE, TryResource.class);
        classes.put(CeylonParser.TRY_STMT, TryStatement.class);
        classes.put(CeylonParser.TYPE, Type.class);
        classes.put(CeylonParser.TYPE_ARG_LIST, TypeArgumentList.class);
        classes.put(CeylonParser.TYPE_CONSTRAINT, TypeConstraint.class);
        classes.put(CeylonParser.TYPE_CONSTRAINT_LIST, TypeConstraintList.class);
        classes.put(CeylonParser.TYPE_DECL, TypeDeclaration.class);
        classes.put(CeylonParser.TYPE_NAME, TypeName.class);
        classes.put(CeylonParser.TYPE_PARAMETER, TypeParameter.class);
        classes.put(CeylonParser.TYPE_PARAMETER_LIST, TypeParameterList.class);
        classes.put(CeylonParser.TYPE_VARIANCE, TypeVariance.class);
        classes.put(CeylonParser.UIDENTIFIER, Identifier.class);
        classes.put(CeylonParser.UPPER_BOUND, UpperBound.class);
        classes.put(CeylonParser.USER_ANNOTATION, UserAnnotation.class);
        classes.put(CeylonParser.VARARGS, Varargs.class);
        classes.put(CeylonParser.VOID, Void.class);
        classes.put(CeylonParser.VOLATILE, Volatile.class);
        classes.put(CeylonParser.WHILE, While.class);
        classes.put(CeylonParser.WHILE_BLOCK, WhileBlock.class);
        classes.put(CeylonParser.WHILE_STMT, WhileStatement.class);
        classes.put(CeylonParser.WS, Whitespace.class);
    }

    /**
     * This node's parent and children.
     */
    public CeylonTree parent;

    public List<CeylonTree> children;
  
    public List<Annotation> annotations;
  
    public String name;
  
    void setName(String name) {
        this.name = name;
    }
  
    void add (ClassDeclaration decl)
    {
        append(decl);
    }
  
    void add (InterfaceDeclaration decl)
    {
        append(decl);
    }
  
    void add (BaseMethodDeclaration decl)
    {
        append(decl);
    }
  
    void add (BaseMemberDeclaration decl)
    {
        append(decl);
    }
  
    void add (AliasDeclaration decl)
    {
        append(decl);
    }
  
    public void setAnnotations(List<Annotation> annotations) {
        // FIXME: This should not be a method of CeylonTree
        this.annotations = annotations;
    }
    
    public void setCondition(Condition t) {
        throw new RuntimeException();
    }
    
    public void setIfTrue(CeylonTree block) {
        throw new RuntimeException();
    }
    
    public void setIfFalse(CeylonTree block) {
        throw new RuntimeException();
    }
    
    void pushType(IType t) {
        throw new RuntimeException();
    }
    
    void append(CeylonTree expr) {
        throw new RuntimeException();
    }
    
    public void setSuperclass(IType type) {
        throw new RuntimeException();
    }
    
    public void add(Declaration t) {
        // FIXME: Do this properly
        if (ClassDeclaration.class.isAssignableFrom(t.getClass()))
            add ((ClassDeclaration)t);
        else if (InterfaceDeclaration.class.isAssignableFrom(t.getClass()))
            add ((InterfaceDeclaration)t);
        else if (BaseMethodDeclaration.class.isAssignableFrom(t.getClass()))
            add ((BaseMethodDeclaration)t);
        else if (BaseMemberDeclaration.class.isAssignableFrom(t.getClass()))
            add ((BaseMemberDeclaration)t);
        else if (AliasDeclaration.class.isAssignableFrom(t.getClass()))
            add ((AliasDeclaration)t);
        else
            throw new RuntimeException();
    }
  
    public void add(Annotation ann) {
        if (annotations == null)
            annotations = List.<Annotation>nil();
        annotations = annotations.append(ann);
    }

    public void addTypeConstraint(CeylonTree t) {
        throw new RuntimeException();
    }
    
    public void setTypeArgumentList(CeylonTree t) {
        throw new RuntimeException();
    }
 
    public void setParameterList(List<FormalParameter> theList) {
        throw new RuntimeException();
    }

    /**
     * The ANTLR token from which this node was constructed.
     */
    public Token token;

    /**
     * Base class for visitors.
     */
    public static class Visitor {
        public void visit(Abstract that)                  { visitDefault(that); }
        public void visit(AbstractMemberDeclaration that) { visitDefault(that); }
        public void visit(AbstractMethodDeclaration that) { visitDefault(that); }
        public void visit(AbstractsList that)             { visitDefault(that); }
        public void visit(AliasDeclaration that)          { visitDefault(that); }
        public void visit(AnnotationList that)            { visitDefault(that); }
        public void visit(AnnotationName that)            { visitDefault(that); }
        public void visit(AnonymousMethod that)           { visitDefault(that); }
        public void visit(ArgumentList that)              { visitDefault(that); }
        public void visit(ArgumentName that)              { visitDefault(that); }
        public void visit(Assign that)                    { visitDefault(that); }
        public void visit(AttributeSetter that)           { visitDefault(that); }
        public void visit(Break that)                     { visitDefault(that); }
        public void visit(BreakStatement that)            { visitDefault(that); }
        public void visit(CallExpression that)            { visitDefault(that); }
        public void visit(Case that)                      { visitDefault(that); }
        public void visit(CaseDefault that)               { visitDefault(that); }
        public void visit(CaseItem that)                  { visitDefault(that); }
        public void visit(Catch that)                     { visitDefault(that); }
        public void visit(CatchBlock that)                { visitDefault(that); }
        public void visit(CatchStatement that)            { visitDefault(that); }
        public void visit(CharConstant that)              { visitDefault(that); }
        public void visit(CharLiteral that)               { visitDefault(that); }
        public void visit(Klass that)                     { visitDefault(that); }
        public void visit(ClassBody that)                 { visitDefault(that); }
        public void visit(ClassDeclaration that)          { visitDefault(that); }
        public void visit(CompilationUnit that)           { visitDefault(that); }
        public void visit(Condition that)                 { visitDefault(that); }
        public void visit(Continue that)                  { visitDefault(that); }
        public void visit(Default that)                   { visitDefault(that); }
        public void visit(Do that)                        { visitDefault(that); }
        public void visit(DoBlock that)                   { visitDefault(that); }
        public void visit(DoIterator that)                { visitDefault(that); }
        public void visit(Else that)                      { visitDefault(that); }
        public void visit(EnumList that)                  { visitDefault(that); }
        public void visit(Exists that)                    { visitDefault(that); }
        public void visit(ExistsExpression that)          { visitDefault(that); }
        public void visit(Expression that)                { visitDefault(that); }
        public void visit(ExpressionList that)            { visitDefault(that); }
        public void visit(Extends that)                   { visitDefault(that); }
        public void visit(Extension that)                 { visitDefault(that); }
        public void visit(FailBlock that)                 { visitDefault(that); }
        public void visit(Finally that)                   { visitDefault(that); }
        public void visit(FinallyBlock that)              { visitDefault(that); }
        public void visit(FloatConstant that)             { visitDefault(that); }
        public void visit(FloatLiteral that)              { visitDefault(that); }
        public void visit(For that)                       { visitDefault(that); }
        public void visit(ForContainment that)            { visitDefault(that); }
        public void visit(ForIterator that)               { visitDefault(that); }
        public void visit(ForStatement that)              { visitDefault(that); }
        public void visit(FormalParameter that)           { visitDefault(that); }
        public void visit(FormalParameterList that)       { visitDefault(that); }
        public void visit(Get that)                       { visitDefault(that); }
        public void visit(GetExpression that)             { visitDefault(that); }
        public void visit(Identifier that)                { visitDefault(that); }
        public void visit(If that)                        { visitDefault(that); }
        public void visit(IfFalse that)                   { visitDefault(that); }
        public void visit(IfStatement that)               { visitDefault(that); }
        public void visit(IfTrue that)                    { visitDefault(that); }
        public void visit(Import that)                    { visitDefault(that); }
        public void visit(ImportDeclaration that)         { visitDefault(that); }
        public void visit(ImportList that)                { visitDefault(that); }
        public void visit(ImportPath that)                { visitDefault(that); }
        public void visit(ImportWildcard that)            { visitDefault(that); }
        public void visit(InitializerExpression that)     { visitDefault(that); }
        public void visit(InstanceDeclaration that)       { visitDefault(that); }
        public void visit(IntConstant that)               { visitDefault(that); }
        public void visit(Interface that)                 { visitDefault(that); }
        public void visit(InterfaceDeclaration that)      { visitDefault(that); }
        public void visit(IsExpression that)              { visitDefault(that); }
        public void visit(LanguageAnnotation that)        { visitDefault(that); }
        public void visit(Local that)                     { visitDefault(that); }
        public void visit(LoopBlock that)                 { visitDefault(that); }
        public void visit(LowerBound that)                { visitDefault(that); }
        public void visit(MemberDeclaration that)         { visitDefault(that); }
        public void visit(MemberName that)                { visitDefault(that); }
        public void visit(MemberType that)                { visitDefault(that); }
        public void visit(MethodDeclaration that)         { visitDefault(that); }
        public void visit(Module that)                    { visitDefault(that); }
        public void visit(MultiLineComment that)          { visitDefault(that); }
        public void visit(Mutable that)                   { visitDefault(that); }
        public void visit(NamedArgument that)             { visitDefault(that); }
        public void visit(NaturalLiteral that)            { visitDefault(that); }
        public void visit(Nil that)                       { visitDefault(that); }
        public void visit(None that)                      { visitDefault(that); }
        public void visit(Nonempty that)                  { visitDefault(that); }
        public void visit(NonemptyExpression that)        { visitDefault(that); }
        public void visit(Null that)                      { visitDefault(that); }
        public void visit(Operator that)                  { visitDefault(that); }
        public void visit(OperatorDot that)               { visitDefault(that); }
        public void visit(Optional that)                  { visitDefault(that); }
        public void visit(Out that)                       { visitDefault(that); }
        public void visit(Override that)                  { visitDefault(that); }
        public void visit(Package that)                   { visitDefault(that); }
        public void visit(PostfixExpression that)         { visitDefault(that); }
        public void visit(PrefixExpression that)          { visitDefault(that); }
        public void visit(Private that)                   { visitDefault(that); }
        public void visit(Public that)                    { visitDefault(that); }
        public void visit(QuoteConstant that)             { visitDefault(that); }
        public void visit(QuotedLiteral that)             { visitDefault(that); }
        public void visit(ReflectedLiteral that)          { visitDefault(that); }
        public void visit(Retry that)                     { visitDefault(that); }
        public void visit(RetryStatement that)            { visitDefault(that); }
        public void visit(Return that)                    { visitDefault(that); }
        public void visit(ReturnStatement that)           { visitDefault(that); }
        public void visit(Satisfies that)                 { visitDefault(that); }
        public void visit(SatisfiesList that)             { visitDefault(that); }
        public void visit(SelectorList that)              { visitDefault(that); }
        public void visit(Set that)                       { visitDefault(that); }
        public void visit(SetExpression that)             { visitDefault(that); }
        public void visit(SimpleStringLiteral that)       { visitDefault(that); }
        public void visit(SingleLineComment that)         { visitDefault(that); }
        public void visit(Block that)             { visitDefault(that); }
        public void visit(StringConcatenation that)       { visitDefault(that); }
        public void visit(StringConstant that)            { visitDefault(that); }
        public void visit(SubscriptExpression that)       { visitDefault(that); }
        public void visit(Subtype that)                   { visitDefault(that); }
        public void visit(Super that)                     { visitDefault(that); }
        public void visit(Superclass that)                { visitDefault(that); }
        public void visit(Switch that)                    { visitDefault(that); }
        public void visit(SwitchCaseList that)            { visitDefault(that); }
        public void visit(SwitchExpression that)          { visitDefault(that); }
        public void visit(SwitchStatement that)           { visitDefault(that); }
        public void visit(This that)                      { visitDefault(that); }
        public void visit(Throw that)                     { visitDefault(that); }
        public void visit(ThrowStatement that)            { visitDefault(that); }
        public void visit(Try that)                       { visitDefault(that); }
        public void visit(TryBlock that)                  { visitDefault(that); }
        public void visit(TryCatchStatement that)         { visitDefault(that); }
        public void visit(TryResource that)               { visitDefault(that); }
        public void visit(TryStatement that)              { visitDefault(that); }
        public void visit(Type that)                      { visitDefault(that); }
        public void visit(TypeArgumentList that)          { visitDefault(that); }
        public void visit(TypeConstraint that)            { visitDefault(that); }
        public void visit(TypeConstraintList that)        { visitDefault(that); }
        public void visit(TypeDeclaration that)           { visitDefault(that); }
        public void visit(TypeName that)                  { visitDefault(that); }
        public void visit(TypeParameter that)             { visitDefault(that); }
        public void visit(TypeParameterList that)         { visitDefault(that); }
        public void visit(TypeVariance that)              { visitDefault(that); }
        public void visit(UpperBound that)                { visitDefault(that); }
        public void visit(UserAnnotation that)            { visitDefault(that); }
        public void visit(Varargs that)                   { visitDefault(that); }
        public void visit(Void that)                      { visitDefault(that); }
        public void visit(Volatile that)                  { visitDefault(that); }
        public void visit(While that)                     { visitDefault(that); }
        public void visit(WhileBlock that)                { visitDefault(that); }
        public void visit(WhileStatement that)            { visitDefault(that); }
        public void visit(Whitespace that)                { visitDefault(that); }

        public void visitDefault(CeylonTree tree) {
            throw new RuntimeException(tree.getClassName());
        }
    
        // Synthetic tree nodes generated during analysis
        public void visit(MethodType that)          { visitDefault(that); }
    }

    /**
     * Return the name of this subclass of CeylonTree,
     * useful for debugging.
     */
    public String getClassName() {
        String fullName = getClass().getName();
        String prefix = CeylonTree.class.getName() + "$";
        int index = prefix.length();
        assert fullName.substring(0, index).equals(prefix);
        return fullName.substring(index);
    }

    /**
     * Visit this tree with a given visitor.
     */
    public abstract void accept(Visitor v);

    /**
     * Convert a tree to a pretty-printed string
     */
    public String toString() {
        StringWriter s = new StringWriter();
        this.accept(new TreePrinter(s));
        return s.toString();
    }


    // Node subclasses

    /**
     * The word "abstract"
     */
    public static class Abstract extends CeylonTree {
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * An abstract member declaration
     */
    public static class AbstractMemberDeclaration extends BaseMemberDeclaration {
        public void accept(Visitor v) { v.visit(this); }
   }

    public static class AbstractMethodDeclaration extends BaseMethodDeclaration {
        public void accept(Visitor v) { v.visit(this); }

   }

    /**
     * A list of abstracted types
     */
    public static class AbstractsList extends CeylonTree {
        List<IType> types = List.<IType>nil();
        void pushType(IType type) {
            types.append(type);
        }
  
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * An alias declaration
     */
    public static class AliasDeclaration extends Declaration {
     
        public List<IType> satisfiesList = List.<IType>nil();
        public List<CeylonTree> typeParameters;
        
        public void setVisibility(CeylonTree v) {
            // TODO
        }

        public void accept(Visitor v) { v.visit(this); }

        public void setName(MemberName name) {
            throw new RuntimeException();
        
        }

        public void setParameterList(List<FormalParameter> theList) {
            throw new RuntimeException();
        }

        public void pushType(IType type) {
            satisfiesList = satisfiesList.append(type);
        }
    }

    /**
     * A list of annotations
     */
    public static class AnnotationList extends CeylonTree {
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * An annotation name
     */
    public static class AnnotationName extends CeylonTree {
        public String name;
        public void setName(String name) {
            this.name = name;
        }
      
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * An anonymous method
     */
    public static class AnonymousMethod extends CeylonTree {
        List<FormalParameter> params;
        CeylonTree body;
        
        public void setParameterList(List<FormalParameter> theList) {
            assert(params == null);
            params = theList;
        }
        
        public void append(CeylonTree body) {
           assert(this.body == null);
           this.body = body;
        }
        
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * A list of arguments
     */
    public static class ArgumentList extends CeylonTree {
        List<CeylonTree> theList = List.<CeylonTree>nil();
        
        public void append(CeylonTree expr) {
            theList = theList.append(expr);
        }
        
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * An argument name
     */
    public static class ArgumentName extends CeylonTree {
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * The word "assign"
     */
    public static class Assign extends CeylonTree {
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * An attribute setter
     */
    public static class AttributeSetter extends CeylonTree {
        Declaration decl;
        MemberName name;
        
        public void append(CeylonTree t) {
            if (t instanceof Declaration) {
                assert(decl==null);
                decl = (Declaration)t;
            } else if (t instanceof MemberName) {
                assert(name == null);
                name = (MemberName)t;
            } else {
                throw new RuntimeException();
            }
            
        }
        
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * A list of statements
     */
    public static class Block extends CeylonTree {
        List<CeylonTree> stmts = List.<CeylonTree>nil();
        
        public void append(CeylonTree t) {
            stmts = stmts.append(t);
        }
        
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * The word "break"
     */
    public static class Break extends CeylonTree {
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * A break statement
     */
    public static class BreakStatement extends CeylonTree {
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * A call expression
     */
    public static class CallExpression extends CeylonTree {
        CeylonTree method;
        
        public List<CeylonTree> args = List.<CeylonTree>nil();
        
        public void append(CeylonTree arg) {
            args = args.append(arg);
        }
       
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * The word "case"
     */
    public static class Case extends CeylonTree {
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * A case default
     */
    public static class CaseDefault extends CeylonTree {
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * A case item
     */
    public static class CaseItem extends CeylonTree {
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * The word "catch"
     */
    public static class Catch extends CeylonTree {
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * A catch block
     */
    public static class CatchBlock extends CeylonTree {
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * A catch statement
     */
    public static class CatchStatement extends CeylonTree {
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * A character constant
     */
    public static class CharConstant extends CeylonTree {
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * A character literal
     */
    public static class CharLiteral extends CeylonTree {
        public String value;

        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * The word "class"
     */
    public static class Klass extends CeylonTree {
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * A class body
     */
    public static class ClassBody extends CeylonTree {
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * A class declaration
     */
    public static class ClassDeclaration extends Declaration {
        public List<FormalParameter> params;
        public List<CeylonTree> stmts;
        public List<CeylonTree> typeParameters;
        public IType superclass;
      
	public void append(CeylonTree stmt) {
            if (stmts == null)
                stmts = List.<CeylonTree>nil();
            stmts = stmts.append(stmt);
        }
          
        public void setVisibility(CeylonTree v) {           
        }
        public String name;
        public void setName(String name) {
            this.name = name;
        }
    
        public void accept(Visitor v) { v.visit(this); }

        public void setName(MemberName name) {
            throw new RuntimeException();       
        }

        public void setParameterList(List<FormalParameter> p) {
            params = p;             
        }

        public void pushType(IType type) {
            throw new RuntimeException();
        }
        
        public void setSuperclass(IType type) {
            assert(superclass == null);
            superclass = type;
        }
    }

    /**
     * A compilation unit
     */
    public static class CompilationUnit extends CeylonTree {
        public List<ClassDeclaration> classDecls;

        public List<InterfaceDeclaration> interfaceDecls;

        public List<BaseMethodDeclaration> methods;
      
        public List <BaseMemberDeclaration> members;
      
        public List <AliasDeclaration> aliases;
      

        List<CeylonTree.Annotation> pendingAnnotations;
        public void add(Annotation ann) {
            if (pendingAnnotations == null)
                pendingAnnotations = List.<Annotation>nil();
            pendingAnnotations.append(ann);
        }

        void add (ClassDeclaration decl)
        {
            if (classDecls == null)
                classDecls = List.<ClassDeclaration>nil();
            classDecls = classDecls.append(decl);
        }
      
        void add (InterfaceDeclaration decl)
        {
            if (interfaceDecls == null)
                interfaceDecls = List.<InterfaceDeclaration>nil();
            interfaceDecls = interfaceDecls.append(decl);
        }
      
        void add (BaseMethodDeclaration decl)
        {
            if (methods == null)
                methods = List.<BaseMethodDeclaration>nil();
            methods = methods.append(decl);
        }
      
        void add (BaseMemberDeclaration decl)
        {
            if (members == null)
                members = List.<BaseMemberDeclaration>nil();
            members = members.append(decl);
        }
      
        void add (AliasDeclaration decl)
        {
            if (aliases == null)
                aliases = List.<AliasDeclaration>nil();
            aliases = aliases.append(decl);
        }
      
        List<CeylonTree.ImportDeclaration> importDeclarations;
      
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * A condition
     */
    public static class Condition extends CeylonTree {
        CeylonTree operand;
        void append(CeylonTree expr) {
            assert(operand == null);
            operand = expr;
        }
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * The word "continue"
     */
    public static class Continue extends CeylonTree {
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * The word "default"
     */
    public static class Default extends CeylonTree {
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * The word "do"
     */
    public static class Do extends CeylonTree {
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * A do block
     */
    public static class DoBlock extends CeylonTree {
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * A do iterator
     */
    public static class DoIterator extends CeylonTree {
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * The word "else"
     */
    public static class Else extends CeylonTree {
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * A list of enums
     */
    public static class EnumList extends CeylonTree {
        List<CeylonTree> members = List.<CeylonTree>nil();;
                
        public void append(CeylonTree expr) {
            members = members.append(expr);
        }
        
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * The word "exists"
     */
    public static class Exists extends CeylonTree {
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * An exists expression
     */
    public static class ExistsExpression extends CeylonTree {
        CeylonTree thing;
        void append(CeylonTree tree) {
            assert (thing == null);
            thing = tree;
        }
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * An expression
     */
    public static class Expression extends CeylonTree {
        CeylonTree thing;
        OperatorDot dot;
        CeylonTree typeArgumentList;
        
        public void setTypeArgumentList(CeylonTree t) {
            // FIXME: An expression has a type argument list in the case of something like
            // 
            // String stringify<Y>(Y y) where Y satisfies Number;
            // String zero = stringify<Natural>(0);
            //
            // The TypeArgumentList should be attached to the CallExpression
            assert(typeArgumentList == null);
            typeArgumentList = t;
        }
        
        public void accept(Visitor v) { v.visit(this); }
        
        public void addSubscript(SubscriptExpression expr) {
            assert(thing != null);
            assert(expr.operand == null);
            expr.operand = thing;
            thing = expr;
        }
        
        public void addMemberName(MemberName name) {
            assert(name.operand == null);
            if (dot != null) {
                assert(thing != null);
                dot.operand = thing;
                dot.memberName = name;
                thing = dot;
                dot = null;
            } else {
                assert(thing == null);
                thing = name;
            }
        }
        
        public void pushDot(OperatorDot dot) {
            assert(this.dot == null);
            this.dot = dot;
        }
        
        void append(CeylonTree expr) {
            if (expr instanceof MemberName) {
                // FIXME: This is a kludge
                addMemberName((MemberName)expr);
            } else if (expr instanceof CallExpression) {
                CallExpression c = (CallExpression)expr;
                if (thing != null) {
                    assert(c.method == null);
                    c.method = thing;
                }
                thing = c;
            } else if (expr instanceof PostfixExpression) {
                if (thing != null)
                    expr.append(thing);
                thing = expr;   
            } else {
                assert(thing == null);
                thing = expr;
            }
        }
    }

    /**
     * A list of expressions
     */
    public static class ExpressionList extends CeylonTree {
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * The word "extends"
     */
    public static class Extends extends CeylonTree {
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * The word "extension"
     */
    public static class Extension extends CeylonTree {
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * A fail block
     */
    public static class FailBlock extends CeylonTree {
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * The word "finally"
     */
    public static class Finally extends CeylonTree {
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * A finally block
     */
    public static class FinallyBlock extends CeylonTree {
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * A float constant
     */
    public static class FloatConstant extends CeylonTree {
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * A float literal
     */
    public static class FloatLiteral extends CeylonTree {
        double value;
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * The word "for"
     */
    public static class For extends CeylonTree {
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * A for containment
     */
    public static class ForContainment extends CeylonTree {
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * A for iterator
     */
    public static class ForIterator extends CeylonTree {
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * A for statement
     */
    public static class ForStatement extends CeylonTree {
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * A formal parameter
     */
    public static class FormalParameter extends Declaration {
        IType type;
      
        CeylonTree initializer;
        
        void append(CeylonTree expr) {
            if (this.initializer != null)
                throw new RuntimeException();
            this.initializer = expr;
        }
        
        public void accept(Visitor v) { v.visit(this); }
    
        public void pushType(IType type) {
            this.type = type;
        }

        public void setParameterList(List<FormalParameter> params) {
            // If a FormalParameter has a formal parameter list, then it
            // must be of method type.
            //
            // TODO: I think this is right, but we need to check
            MethodType methodType = new CeylonTree.MethodType();
            methodType.formalParameters = params;
            methodType.returnType = type;
            type = methodType;
        }

        public void setTypeParameterList(List<CeylonTree> typeParameters) {
            throw new RuntimeException();

            
        }
    }

    /**
     * A list of formal parameters
     */
    public static class FormalParameterList extends CeylonTree {
        public List<FormalParameter> theList = List.<FormalParameter>nil();
      
        public void addFormalParameter(FormalParameter p) {
            theList = theList.append(p);
        }
      
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * The word "get"
     */
    public static class Get extends CeylonTree {
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * A get expression
     */
    public static class GetExpression extends CeylonTree {
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * An identifier
     */
    public static class Identifier extends CeylonTree {
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * The word "if"
     */
    public static class If extends CeylonTree {
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * The taken-if-false block of an if clause
     */
    public static class IfFalse extends CeylonTree {
        CeylonTree block;
        
        public void append(CeylonTree block) {
            assert(this.block == null);
            this.block = block;
        }
        
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * An if statement
     */
    public static class IfStatement extends CeylonTree {
        Condition condition;
        CeylonTree ifTrue; // FIXME: Always an instance of Block
        CeylonTree ifFalse; // FIXME: Always an instance of Block
               
        public void setIfTrue(CeylonTree t) {
            assert(ifTrue == null);
            ifTrue = t;
        }
            
        public void setIfFalse(CeylonTree t) {
            assert(ifFalse == null);
            ifFalse = t;
        }
            
        public void setCondition(Condition t) {
            assert(condition == null);
            condition = t;
        }
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * The taken-if-true block of an if clause
     */
    public static class IfTrue extends CeylonTree {
        CeylonTree block;
        
        public void append(CeylonTree block) {
            assert(this.block == null);
            this.block = block;
        }
        
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * The word "import"
     */
    public static class Import extends CeylonTree {
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * An import declaration
     */
    public static class ImportDeclaration extends CeylonTree {
        List<CeylonTree> importPath = List.<CeylonTree>nil();
        
        public void append(CeylonTree t) {
            importPath.append(t);
        }
        
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * A list of imports
     */
    public static class ImportList extends CeylonTree {
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * An import path
     */
    public static class ImportPath extends CeylonTree {
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * An import wildcard
     */
    public static class ImportWildcard extends CeylonTree {
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * An initializer expression
     */
    public static class InitializerExpression extends CeylonTree {
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * An instance declaration
     */
    public static class InstanceDeclaration extends Declaration {
        MemberName name;
        boolean open = false;
        
        public void setVisibility(CeylonTree v) {           
        }

        public void accept(Visitor v) { v.visit(this); }

        public void setName(MemberName name) {
            assert(this.name == null);
            this.name = name;       
        }

        public void setParameterList(List<FormalParameter> p) {
            throw new RuntimeException();       
        }

        public void pushType(IType type) {
            throw new RuntimeException();
        
        }

        public void setTypeParameterList(List<CeylonTree> typeParameters) {
            throw new RuntimeException();          
        }
        
        static boolean isEllipsis(CeylonTree expr) {
            if (! (expr instanceof Operator)) {
                return false;
            }
            Operator op = (Operator) expr;
            return op.operatorKind == CeylonParser.ELLIPSIS;
        }
        
        void append(CeylonTree expr) {
            if (expr instanceof MemberName) {
                setName((MemberName)expr);
            } else if (isEllipsis(expr)) {
                open = true;
            } else {
                throw new RuntimeException();
            }
        }

    }

    /**
     * An int constant
     */
    public static class IntConstant extends CeylonTree {
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * The word "interface"
     */
    public static class Interface extends CeylonTree {
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * An interface declaration
     */
    public static class InterfaceDeclaration extends Declaration {
        public List<CeylonTree> stmts;
        public List<CeylonTree> typeParameters;
        public List<FormalParameter> params;
        
        public void append(CeylonTree stmt) {
            if (stmts == null)
                stmts = List.<CeylonTree>nil();
            stmts = stmts.append(stmt);
        }
   
        public void setVisibility(CeylonTree v) {           
        }
        public void accept(Visitor v) { v.visit(this); }
        public void setParameterList(List<FormalParameter> p) {
            assert(params == null);
            params = p;                 
        }
   
        public void pushType(IType type) {
            throw new RuntimeException();
    
        }
    }

    /**
     * An is expression
     */
    public static class IsExpression extends CeylonTree {
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * A language annotation
     */
    public static class LanguageAnnotation extends CeylonTree implements Annotation {
        public CeylonTree kind;
      
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * The word "local"
     */
    public static class Local extends CeylonTree {
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * A loop block
     */
    public static class LoopBlock extends CeylonTree {
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * A lower bound
     */
    public static class LowerBound extends CeylonTree {
        CeylonTree initializer;
        
        void append(CeylonTree expr) {
            if (this.initializer != null)
                throw new RuntimeException();
            this.initializer = expr;
        }

        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * A member declaration
     */
    public abstract static class BaseMemberDeclaration extends Declaration {
        public IType type;
        public List<FormalParameter> params;
        public List<CeylonTree> stmts;
        List<CeylonTree> typeParameters;
        public AttributeSetter attributeSetter;
        
        public void append(CeylonTree stmt) {
            if (stmts == null)
                stmts = List.<CeylonTree>nil();
            stmts = stmts.append(stmt);
        }
        
        public void setName(MemberName name) {
            this.name = name.name;
        }
      
        public void setParameterList(List<FormalParameter> p) {
            params = p;
        }

        public void pushType(IType type) {
            this.type = type;
        
        }
    }

    public static class MemberDeclaration extends BaseMemberDeclaration {
        public void accept(Visitor v) { v.visit(this); }

        public void setTypeParameterList(List<CeylonTree> typeParameters) {
            assert(this.typeParameters == null);
            this.typeParameters = typeParameters;
        }
    }

     /**
     * A member name
     */
    public static class MemberName extends CeylonTree {
        CeylonTree operand;
        CeylonTree memberName;
        
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * A member type
     */
    public static class MemberType extends CeylonTree implements IType {
        IType type;
      
        public MemberType() {
            
        }
        
        public void accept(Visitor v) { v.visit(this); }

        public void pushType(IType type) {
            this.type = type;
        }
    }

    public static class MethodType extends CeylonTree implements IType {
        public List<FormalParameter> formalParameters;
        public IType returnType;
      
        public void accept(Visitor v) { v.visit(this); }

        public void pushType(IType type) {
            this.returnType = type;
        }
    }
  
    public static abstract class BaseMethodDeclaration extends Declaration {
        public IType type;
        public List<FormalParameter> params;
        
        public List<CeylonTree> stmts;
      
        public void pushType(IType type) {
            this.type = type;
        }

        public void setParameterList(List<FormalParameter> theList) {
            params = theList;
        }
    }

    public static class MethodDeclaration extends BaseMethodDeclaration {
        List<CeylonTree> typeParameters;
        
        public void accept(Visitor v) { v.visit(this); }

        public void setTypeParameterList(List<CeylonTree> typeParameters) {
            assert(this.typeParameters == null);
            this.typeParameters = typeParameters;
        }
    }

    /**
     * The word "module"
     */
    public static class Module extends CeylonTree {
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * A multi line comment
     */
    public static class MultiLineComment extends CeylonTree {
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * The word "mutable"
     */
    public static class Mutable extends CeylonTree {
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * A named argument
     */
    public static class NamedArgument extends CeylonTree {
        public CeylonTree value;
        public IType type;
        public List<FormalParameter> formalParams;
        
        public void append(CeylonTree expr) {
            value = expr;
        }
        
        public void pushType(IType type) {
            assert(this.type == null);
            this.type = type;
        }
        
        public void setParameterList(List<FormalParameter> theList) {
            assert(formalParams == null);
            formalParams = theList;
        }

        
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * A natural literal
     */
    public static class NaturalLiteral extends CeylonTree {
        public BigInteger value; 

        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * A nil
     */
    public static class Nil extends CeylonTree {
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * The word "none"
     */
    public static class None extends CeylonTree {
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * The word "nonempty"
     */
    public static class Nonempty extends CeylonTree {
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * A nonempty expression
     */
    public static class NonemptyExpression extends CeylonTree {
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * The word "null"
     */
    public static class Null extends CeylonTree {
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * An operator.
     */
    public static class Operator extends CeylonTree {
        public int operatorKind;
        public List<CeylonTree> operands = List.<CeylonTree>nil();
         
        public void append(CeylonTree operand) {
            operands = operands.append(operand);
        }
        
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * An operator.
     */
    public static class OperatorDot extends CeylonTree {
        public CeylonTree operand;
        public CeylonTree memberName;
  
        public void append(CeylonTree operand) {
            assert(this.operand == null);
            this.operand = operand;
        }
        
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * The word "optional"
     */
    public static class Optional extends CeylonTree {
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * The word "out"
     */
    public static class Out extends CeylonTree {
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * The word "override"
     */
    public static class Override extends CeylonTree {
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * The word "package"
     */
    public static class Package extends CeylonTree {
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * A postfix expression
     */
    public static class PostfixExpression extends CeylonTree {
        Operator operator;
        void append(CeylonTree expr) {
            if (expr instanceof Operator) {
                assert(operator == null);
                operator = (Operator)expr;
            }
        }
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * A prefix expression
     */
    public static class PrefixExpression extends CeylonTree {
        Operator operator;
        void append(CeylonTree expr) {
            if (expr instanceof Operator) {
                assert(operator == null);
                operator = (Operator)expr;
            }
        }
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * The word "private"
     */
    public static class Private extends CeylonTree implements Annotation {
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * The word "public"
     */
    public static class Public extends CeylonTree implements Annotation {
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * A quote constant
     */
    public static class QuoteConstant extends CeylonTree {
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * A quoted literal
     */
    public static class QuotedLiteral extends CeylonTree {
        public String value;

        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * A reflected literal
     */
    public static class ReflectedLiteral extends CeylonTree {
        CeylonTree operand;
        
        void append(CeylonTree member) {
            this.operand = member;
        }
        
        void pushType(IType member) {
            this.operand = (CeylonTree) operand;
        }
        
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * The word "retry"
     */
    public static class Retry extends CeylonTree {
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * A retry statement
     */
    public static class RetryStatement extends CeylonTree {
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * The word "return"
     */
    public static class Return extends CeylonTree {
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * A return statement
     */
    public static class ReturnStatement extends CeylonTree {
        CeylonTree expr;
        
        void append(CeylonTree expr) {
            if (this.expr != null)
                throw new RuntimeException();
            this.expr = expr;
        }
        
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * The word "satisfies"
     */
    public static class Satisfies extends CeylonTree {
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * A list of satisfied types
     */
    public static class SatisfiesList extends CeylonTree {
        List<IType> types = List.<IType>nil();
        void pushType(IType type) {
            types.append(type);
        }
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * A list of selectors
     */
    public static class SelectorList extends CeylonTree {
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * The word "set"
     */
    public static class Set extends CeylonTree {
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * A set expression
     */
    public static class SetExpression extends CeylonTree {
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * A simple string literal
     */
    public static class SimpleStringLiteral extends CeylonTree {
        public String value;
        
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * A single line comment
     */
    public static class SingleLineComment extends CeylonTree {
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * A string concatenation
     */
    public static class StringConcatenation extends CeylonTree {
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * A string constant
     */
    public static class StringConstant extends CeylonTree {
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * A subscript expression
     */
    public static class SubscriptExpression extends CeylonTree {
        CeylonTree operand;
        CeylonTree lowerBound;
        UpperBound upperBound;
        
        public void append(CeylonTree expr) {
            if (lowerBound != null || upperBound != null)
                throw new RuntimeException();
            lowerBound = expr;
        }
        
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * The word "subtype"
     */
    public static class Subtype extends CeylonTree {
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * The word "super"
     */
    public static class Super extends CeylonTree {
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * A superclass
     */
    public static class Superclass extends CeylonTree {
        public IType theSuperclass;
        public void pushType(IType type) {
            assert(theSuperclass == null);
            theSuperclass = type;
        }
        
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * The word "switch"
     */
    public static class Switch extends CeylonTree {
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * A list of switch cases
     */
    public static class SwitchCaseList extends CeylonTree {
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * A switch expression
     */
    public static class SwitchExpression extends CeylonTree {
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * A switch statement
     */
    public static class SwitchStatement extends CeylonTree {
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * The word "this"
     */
    public static class This extends CeylonTree {
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * The word "throw"
     */
    public static class Throw extends CeylonTree {
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * A throw statement
     */
    public static class ThrowStatement extends CeylonTree {
        public CeylonTree expr;
                
        void append(CeylonTree expr) {
            if (this.expr != null)
                throw new RuntimeException();
            this.expr = expr;
        }
      
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * The word "try"
     */
    public static class Try extends CeylonTree {
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * A try block
     */
    public static class TryBlock extends CeylonTree {
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * A try catch statement
     */
    public static class TryCatchStatement extends CeylonTree {
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * A try resource
     */
    public static class TryResource extends CeylonTree {
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * A try statement
     */
    public static class TryStatement extends CeylonTree {
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * A type
     */
    public static class Type extends CeylonTree implements IType {
        IType type;
        TypeArgumentList argList;
        
        public void accept(Visitor v) { v.visit(this); }

        public void pushType(IType type) {
            this.type = type;       
        }
        
        public void setTypeArgumentList(CeylonTree t) {
            this.argList = (TypeArgumentList)t;
        }
    }

    /**
     * A list of type arguments
     */
    public static class TypeArgumentList extends CeylonTree {
        List<IType> types = List.<IType>nil();
        void pushType(IType type) {
            types.append(type);
        }
        
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * A type constraint
     */
    public static class TypeConstraint extends CeylonTree {
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * A list of type constraints
     */
    public static class TypeConstraintList extends CeylonTree {
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * A type declaration
     */
    public static class TypeDeclaration extends Declaration {
        Declaration decl;

        public void setVisibility(CeylonTree v) {           
            throw new RuntimeException();
        }
        public void accept(Visitor v) { v.visit(this); }
    
        public void setParameterList(List<FormalParameter> p) {
            throw new RuntimeException();       
        }
        public void pushType(IType type) {
            throw new RuntimeException();   
        }
        void add (ClassDeclaration decl)
        {
            if (this.decl != null)
                throw new RuntimeException();
            this.decl = decl;
        }   
        void add (InterfaceDeclaration decl)
        {
            if (this.decl != null)
                throw new RuntimeException();
            this.decl = decl;
        }
        void add (AliasDeclaration decl)
        {
            if (this.decl != null)
                throw new RuntimeException();
            this.decl = decl;
        }
        public void setTypeParameterList(List<CeylonTree> typeParameters) {
            throw new RuntimeException();            
        }
    }

    /**
     * A type name
     */
    public static class TypeName extends CeylonTree {
        public String name;
      
        public void setName(String name) {
            this.name = name;
        }
        
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * A type parameter
     */
    public static class TypeParameter extends CeylonTree {
        public List<CeylonTree> operands = List.<CeylonTree>nil();
        
        public void append(CeylonTree operand) {
            operands = operands.append(operand);
        }

        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * A list of type parameters
     */
    public static class TypeParameterList extends CeylonTree {
        List<CeylonTree> params = List.<CeylonTree>nil();
        
        void append(CeylonTree t) { 
            params = params.append(t);
        }
        
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * A type variance
     */
    public static class TypeVariance extends CeylonTree {
        public List<CeylonTree> operands = List.<CeylonTree>nil();
        
        public void append(CeylonTree operand) {
            operands = operands.append(operand);
        }
        
         public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * An upper bound
     */
    public static class UpperBound extends CeylonTree {
        CeylonTree initializer;
        
        void append(CeylonTree expr) {
            if (this.initializer != null)
                throw new RuntimeException();
            this.initializer = expr;
        }

        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * A user annotation
     */
    public static class UserAnnotation extends CeylonTree implements Annotation
    {
        public String name;
        public void setName(String name) {
            this.name = name;
        }
      
        public CeylonTree value;
        void append(CeylonTree expr) {
            value = expr;
        }
        
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * A varargs
     */
    public static class Varargs extends CeylonTree {
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * The word "void"
     */
    public static class Void extends CeylonTree implements IType {
        IType type;
          
        public void accept(Visitor v) { v.visit(this); }

        public void pushType(IType type) {
            throw new RuntimeException();
        }
    }

    /**
     * The word "volatile"
     */
    public static class Volatile extends CeylonTree {
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * The word "while"
     */
    public static class While extends CeylonTree {
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * A while block
     */
    public static class WhileBlock extends CeylonTree {
        CeylonTree block;
        
        public void append(CeylonTree block) {
            assert(this.block == null);
            this.block = block;
        }
        
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * A while statement
     */
    public static class WhileStatement extends CeylonTree {
        Condition condition;
        CeylonTree ifTrue; // FIXME: Always an instance of Block
        
        public void setIfTrue(CeylonTree t) {
            assert(ifTrue == null);
            ifTrue = t;
        }
            
        public void setCondition(Condition t) {
            assert(condition == null);
            condition = t;
        }
        public void accept(Visitor v) { v.visit(this); }
    }

    /**
     * Whitespace
     */
    public static class Whitespace extends CeylonTree {
        public void accept(Visitor v) { v.visit(this); }
    }
}
