package com.redhat.ceylon.compiler.codegen;

import java.util.HashMap;
import java.util.Map;

import org.antlr.runtime.Token;
import org.antlr.runtime.tree.CommonTree;

import com.redhat.ceylon.compiler.loader.CeylonModelLoader;
import com.redhat.ceylon.compiler.loader.ModelLoader.DeclarationType;
import com.redhat.ceylon.compiler.loader.TypeFactory;
import com.redhat.ceylon.compiler.typechecker.model.BottomType;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.TypeParameter;
import com.redhat.ceylon.compiler.typechecker.model.UnionType;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Expression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.LocalModifier;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.TypedDeclaration;
import com.sun.tools.javac.code.BoundKind;
import com.sun.tools.javac.code.Symtab;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.code.TypeTags;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.Factory;
import com.sun.tools.javac.tree.JCTree.JCAnnotation;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCFieldAccess;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.util.Position.LineMap;

/**
 * Base class for all delegating transformers
 */
public abstract class AbstractTransformer implements Transformation {
    private Context context;
    private TreeMaker make;
    private Name.Table names;
    private Symtab syms;
    private CeylonModelLoader loader;
    private TypeFactory typeFact;

    public AbstractTransformer(Context context) {
        this.context = context;
        make = TreeMaker.instance(context);
        names = Name.Table.instance(context);
        syms = Symtab.instance(context);
        loader = CeylonModelLoader.instance(context);
        typeFact = TypeFactory.instance(context);
    }

    @Override
    public TreeMaker make() {
        return make;
    }

    @Override
    public Factory at(Node node) {
        CommonTree antlrTreeNode = node.getAntlrTreeNode();
        Token token = antlrTreeNode.getToken();
        if (token != null) {
            make().at(getMap().getStartPosition(token.getLine()) + token.getCharPositionInLine());
        }
        return make();
    }

    @Override
    public Symtab syms() {
        return syms;
    }

    @Override
    public Name.Table names() {
        return names;
    }

    @Override
    public CeylonModelLoader loader() {
        return loader;
    }
    
    @Override
    public TypeFactory typeFact() {
        return typeFact;
    }

    public void setMap(LineMap map) {
        gen().setMap(map);
    }

    protected LineMap getMap() {
        return gen().getMap();
    }

    @Override
    public CeylonTransformer gen() {
        return CeylonTransformer.getInstance(context);
    }
    
    @Override
    public ExpressionTransformer expressionGen() {
        return ExpressionTransformer.getInstance(context);
    }
    
    @Override
    public StatementTransformer statementGen() {
        return StatementTransformer.getInstance(context);
    }
    
    @Override
    public ClassTransformer classGen() {
        return ClassTransformer.getInstance(context);
    }
    
    @Override
    public GlobalTransformer globalGen() {
        return GlobalTransformer.getInstance(context);
    }
    
    protected JCExpression makeIdent(String nameAsString) {
        return makeIdent(nameAsString.split("\\."));
    }

    protected JCExpression makeIdent(Iterable<String> components) {
        JCExpression type = null;
        for (String component : components) {
            if (type == null)
                type = make().Ident(names().fromString(component));
            else
                type = makeSelect(type, component);
        }
        return type;
    }

    protected JCExpression makeIdent(String... components) {
        JCExpression type = null;
        for (String component : components) {
            if (type == null)
                type = make().Ident(names().fromString(component));
            else
                type = makeSelect(type, component);
        }
        return type;
    }

    protected JCExpression makeIdent(Type type) {
        return make().QualIdent(type.tsym);
    }

    protected JCExpression makeInteger(long i) {
        // FIXME Using Integer only to make hashCode() work!!
        // We should introduce "small"!!
        return make().Literal(Integer.valueOf((int)i));
    }
    
    protected JCExpression makeBoolean(boolean b) {
        JCExpression expr;
        if (b) {
            expr = make().Literal(TypeTags.BOOLEAN, Integer.valueOf(1));
        } else {
            expr = make().Literal(TypeTags.BOOLEAN, Integer.valueOf(0));
        }
        return expr;
    }

    protected JCExpression makeBooleanTest(JCExpression expr, boolean val) {
        return make().Binary(JCTree.EQ, expr, makeBoolean(val));
    }
    
    protected JCFieldAccess makeSelect(JCExpression s1, String s2) {
        return make().Select(s1, names().fromString(s2));
    }

    protected JCFieldAccess makeSelect(String s1, String s2) {
        return makeSelect(make().Ident(names().fromString(s1)), s2);
    }

    protected JCFieldAccess makeSelect(String s1, String s2, String... rest) {
        return makeSelect(makeSelect(s1, s2), rest);
    }

    protected JCFieldAccess makeSelect(JCFieldAccess s1, String[] rest) {
        JCFieldAccess acc = s1;

        for (String s : rest)
            acc = makeSelect(acc, s);

        return acc;
    }

    /*
     * Methods for making unique temporary and alias names
     */
    
    static class UniqueId {
        private long id = 0;
        private long nextId() {
            return id++;
        }
    }
    
    private long nextUniqueId() {
        UniqueId id = context.get(UniqueId.class);
        if (id == null) {
            id = new UniqueId();
            context.put(UniqueId.class, id);
        }
        return id.nextId();
    }
    
    protected String tempName() {
        String result = "$ceylontmp" + nextUniqueId();
        return result;
    }

    protected String tempName(String prefix) {
        String result = "$ceylontmp" + prefix + nextUniqueId();
        return result;
    }

    protected String aliasName(String name) {
        String result = "$" + name + "$" + nextUniqueId();
        return result;
    }
    
    /*
     * Type handling
     */
    
    protected boolean isShared(Tree.Declaration decl) {
        return decl.getDeclarationModel().isShared();
    }

    protected boolean isAbstract(Tree.ClassOrInterface decl) {
        return decl.getDeclarationModel().isAbstract();
    }

    protected boolean isDefault(Tree.Declaration decl) {
        return decl.getDeclarationModel().isDefault();
    }

    protected boolean isFormal(Tree.Declaration decl) {
        return decl.getDeclarationModel().isFormal();
    }

    protected boolean isActual(Tree.Declaration decl) {
        return decl.getDeclarationModel().isActual();
    }

    protected boolean isMutable(Tree.AttributeDeclaration decl) {
        return decl.getDeclarationModel().isVariable();
    }

    protected boolean isToplevel(Tree.Declaration decl) {
        return decl.getDeclarationModel().isToplevel();
    }
    
    protected boolean isInner(Tree.AnyMethod decl) {
        return decl.getDeclarationModel().getContainer() instanceof Method;
    }

    protected boolean isBooleanTrue(Declaration decl) {
        return decl == loader().getDeclaration("ceylon.language.$true", DeclarationType.VALUE);
    }
    
    protected boolean isBooleanFalse(Declaration decl) {
        return decl == loader().getDeclaration("ceylon.language.$false", DeclarationType.VALUE);
    }
    
    // A type is optional when it is a union of Nothing|Type...
    protected boolean isOptional(ProducedType type) {
        return typeFact().isOptional(type);
    }

    protected ProducedType simplifyType(ProducedType type) {
        if (isOptional(type)) {
            // For an optional type T?:
            //  - The Ceylon type T? results in the Java type T
            // Nasty cast because we just so happen to know that nothingType is a Class
            type = typeFact().getDefiniteType(type);
        }
        
        TypeDeclaration tdecl = type.getDeclaration();
        if (tdecl instanceof UnionType && tdecl.getCaseTypes().size() == 1) {
            // Special case when the Union contains only a single CaseType
            // FIXME This is not correct! We might lose information about type arguments!
            type = tdecl.getCaseTypes().get(0);
        }
        
        return type;
    }
    
    protected ProducedType actualType(TypedDeclaration decl) {
        ProducedType t = decl.getType().getTypeModel();
        if (decl.getType() instanceof LocalModifier) {
            LocalModifier m = (LocalModifier)(decl.getType());
            t = m.getTypeModel();
        }
        return t;
    }

    protected ProducedType toPType(com.sun.tools.javac.code.Type t) {
        return loader().getType(t.tsym.getQualifiedName().toString(), null);
    }
    
    protected boolean sameType(Type t1, ProducedType t2) {
        return toPType(t1).isExactly(t2);
    }
    
    // Determines if a type will be erased to java.lang.Object once converted to Java
    protected boolean willEraseToObject(ProducedType type) {
        type = simplifyType(type);
        return (sameType(syms().ceylonVoidType, type) || sameType(syms().ceylonObjectType, type)
                || sameType(syms().ceylonNothingType, type) || sameType(syms().ceylonEqualityType, type)
                || sameType(syms().ceylonIdentifiableObjectType, type)
                || type.getDeclaration() instanceof BottomType
                || typeFact().isUnion(type));
    }
    
    private boolean isCeylonString(ProducedType type) {
        return (sameType(syms().ceylonStringType, type));
    }
    
    private boolean isCeylonBoolean(ProducedType type) {
        return (sameType(syms().ceylonBooleanType, type));
    }
    
    private boolean isCeylonNatural(ProducedType type) {
        return (sameType(syms().ceylonNaturalType, type));
    }
    
    private boolean isCeylonInteger(ProducedType type) {
        return (sameType(syms().ceylonIntegerType, type));
    }
    
    private boolean isCeylonFloat(ProducedType type) {
        return (sameType(syms().ceylonFloatType, type));
    }
    
    private boolean isCeylonCharacter(ProducedType type) {
        return (sameType(syms().ceylonCharacterType, type));
    }

    private boolean isCeylonBasicType(ProducedType type) {
        return (isCeylonString(type) || isCeylonBoolean(type) || isCeylonNatural(type) || isCeylonInteger(type) || isCeylonFloat(type) || isCeylonCharacter(type));
    }

    /*
     * Java Type creation
     */
    
    static final int SATISFIES = 1 << 0;
    static final int EXTENDS = 1 << 1;
    static final int TYPE_PARAM = 1 << 2;
    static final int CLASS_NEW = 1 << 1; // Yes, same as EXTENDS
    static final int WANT_RAW_TYPE = 1 << 3;

    protected JCExpression makeJavaType(ProducedType producedType) {
        return makeJavaType(producedType, 0);
    }

    protected JCExpression makeJavaType(ProducedType type, int flags) {
        int satisfiesOrExtendsOrTypeParam = flags & (SATISFIES | EXTENDS | TYPE_PARAM);
        int satisfiesOrExtends = flags & (SATISFIES | EXTENDS);
        
        // ERASURE
        if (willEraseToObject(type)) {
            // For an erased type:
            // - Any of the Ceylon types Void, Object, Nothing, Equality,
            //   IdentifiableObject, and Bottom result in the Java type Object
            // For any other union type U|V (U nor V is Optional):
            // - The Ceylon type U|V results in the Java type Object
            ProducedType iterType = typeFact().getNonemptyIterableType(typeFact().getDefiniteType(type));
            if (iterType != null) {
                // We special case the erasure of X[] and X[]?
                type = iterType;
            } else {
                if ((flags & SATISFIES) != 0) {
                    return null;
                } else {
                    return make().Type(syms().objectType);
                }
            }
        } else if (satisfiesOrExtendsOrTypeParam == 0 && !isOptional(type)) {
            if (isCeylonString(type)) {
                return make().Type(syms().stringType);
            } else if (isCeylonBoolean(type)) {
                return make().TypeIdent(TypeTags.BOOLEAN);
            } else if (isCeylonNatural(type)) {
                return make().TypeIdent(TypeTags.LONG);
            } else if (isCeylonInteger(type)) {
                return make().TypeIdent(TypeTags.INT);
            } else if (isCeylonFloat(type)) {
                return make().TypeIdent(TypeTags.DOUBLE);
            } else if (isCeylonCharacter(type)) {
                return make().TypeIdent(TypeTags.CHAR);
            }
        }
        
        JCExpression jt;
        type = simplifyType(type);
        TypeDeclaration tdecl = type.getDeclaration();
        java.util.List<ProducedType> tal = type.getTypeArgumentList();

        if (((flags & WANT_RAW_TYPE) == 0) && tal != null && !tal.isEmpty()) {
            // GENERIC TYPES

            ListBuffer<JCExpression> typeArgs = new ListBuffer<JCExpression>();

            int idx = 0;
            for (ProducedType ta : tal) {
                if (isOptional(ta)) {
                    ProducedType defta = typeFact().getDefiniteType(ta);
                    if (isCeylonBasicType(defta)) {
                        ta = defta;
                    }
                }
                if (typeFact().isUnion(ta)) {
                    // For any other union type U|V (U nor V is Optional):
                    // - The Ceylon type Foo<U|V> results in the raw Java type Foo.
                    // A bit ugly, but we need to escape from the loop and create a raw type, no generics
                    typeArgs = null;
                    break;
                }
                JCExpression jta;
                if (sameType(syms().ceylonVoidType, ta)) {
                    // For the root type Void:
                    if (satisfiesOrExtends != 0) {
                        // - The Ceylon type Foo<Void> appearing in an extends or satisfies
                        //   clause results in the Java raw type Foo<Object>
                        jta = make().Type(syms().objectType);
                    } else {
                        // - The Ceylon type Foo<Void> appearing anywhere else results in the Java type
                        // - Foo<Object> if Foo<T> is invariant in T
                        // - Foo<?> if Foo<T> is covariant in T, or
                        // - Foo<Object> if Foo<T> is contravariant in T
                        TypeParameter tp = tdecl.getTypeParameters().get(idx);
                        if (tp.isContravariant()) {
                            jta = make().Type(syms().objectType);
                        } else if (tp.isCovariant()) {
                            jta = make().Wildcard(make().TypeBoundKind(BoundKind.UNBOUND), makeJavaType(ta));
                        } else {
                            jta = make().Type(syms().objectType);
                        }
                    }
                } else if (ta.getDeclaration() instanceof BottomType) {
                    // For the bottom type Bottom:
                    if (satisfiesOrExtends != 0) {
                        // - The Ceylon type Foo<Bottom> appearing in an extends or satisfies
                        //   clause results in the Java raw type Foo
                        // A bit ugly, but we need to escape from the loop and create a raw type, no generics
                        typeArgs = null;
                        break;
                    } else {
                        // - The Ceylon type Foo<Bottom> appearing anywhere else results in the Java type
                        // - raw Foo if Foo<T> is invariant in T,
                        // - raw Foo if Foo<T> is covariant in T, or
                        // - Foo<?> if Foo<T> is contravariant in T
                        TypeParameter tp = tdecl.getTypeParameters().get(idx);
                        if (tp.isContravariant()) {
                            jta = make().Wildcard(make().TypeBoundKind(BoundKind.UNBOUND), makeJavaType(ta));
                        } else {
                            // A bit ugly, but we need to escape from the loop and create a raw type, no generics
                            typeArgs = null;
                            break;
                        }
                    }
                } else {
                    // For an ordinary class or interface type T:
                    if (satisfiesOrExtends != 0) {
                        // - The Ceylon type Foo<T> appearing in an extends or satisfies clause
                        //   results in the Java type Foo<T>
                        jta = makeJavaType(ta, satisfiesOrExtends);
                    } else {
                        // - The Ceylon type Foo<T> appearing anywhere else results in the Java type
                        // - Foo<T> if Foo is invariant in T,
                        // - Foo<? extends T> if Foo is covariant in T, or
                        // - Foo<? super T> if Foo is contravariant in T
                        TypeParameter tp = tdecl.getTypeParameters().get(idx);
                        if (tp.isContravariant()) {
                            jta = make().Wildcard(make().TypeBoundKind(BoundKind.SUPER), makeJavaType(ta, TYPE_PARAM));
                        } else if (tp.isCovariant()) {
                            jta = make().Wildcard(make().TypeBoundKind(BoundKind.EXTENDS), makeJavaType(ta, TYPE_PARAM));
                        } else {
                            jta = makeJavaType(ta, TYPE_PARAM);
                        }
                    }
                }
                typeArgs.add(jta);
                idx++;
            }

            if (typeArgs != null && typeArgs.size() > 0) {
                jt = make().TypeApply(makeIdent(tdecl.getQualifiedNameString()), typeArgs.toList());
            } else {
                jt = makeIdent(tdecl.getQualifiedNameString());
            }
        } else {
            // For an ordinary class or interface type T:
            // - The Ceylon type T results in the Java type T
            if(tdecl instanceof TypeParameter)
                jt = makeIdent(tdecl.getName());
            else
                jt = makeIdent(tdecl.getQualifiedNameString());
        }
        
        return jt;
    }
    
    /*
     * Annotation generation
     */
    
    List<JCAnnotation> makeAtOverride() {
        return List.<JCAnnotation> of(make().Annotation(makeIdent(syms().overrideType), List.<JCExpression> nil()));
    }

    // FIXME
    public static boolean disableModelAnnotations = false;
    
    private List<JCAnnotation> makeModelAnnotation(Type annotationType, List<JCExpression> annotationArgs) {
        if (disableModelAnnotations)
            return List.nil();
        return List.of(make().Annotation(makeIdent(annotationType), annotationArgs));
    }

    private List<JCAnnotation> makeModelAnnotation(Type annotationType) {
        return makeModelAnnotation(annotationType, List.<JCExpression>nil());
    }

    protected List<JCAnnotation> makeAtCeylon() {
        return makeModelAnnotation(syms().ceylonAtCeylonType);
    }

    protected List<JCAnnotation> makeAtName(String name) {
        return makeModelAnnotation(syms().ceylonAtNameType, List.<JCExpression>of(make().Literal(name)));
    }

    protected List<JCAnnotation> makeAtType(String name) {
        return makeModelAnnotation(syms().ceylonAtTypeInfoType, List.<JCExpression>of(make().Literal(name)));
    }

    protected List<JCAnnotation> makeAtAttribute() {
        return makeModelAnnotation(syms().ceylonAtAttributeType);
    }

    protected List<JCAnnotation> makeAtMethod() {
        return makeModelAnnotation(syms().ceylonAtMethodType);
    }

    protected List<JCAnnotation> makeAtObject() {
        return makeModelAnnotation(syms().ceylonAtObjectType);
    }

    protected List<JCTree.JCAnnotation> makeJavaTypeAnnotations(Declaration decl, ProducedType type) {
        return makeJavaTypeAnnotations(type, decl.isToplevel() || (decl.isClassOrInterfaceMember() && decl.isShared()));
    }

    protected List<JCTree.JCAnnotation> makeJavaTypeAnnotations(ProducedType type, boolean required) {
        if (!required)
            return List.nil();
        // Add the original type to the annotations
        return makeAtType(type.getProducedTypeQualifiedName());
    }
    
    /*
     * Boxing
     */
    
    protected JCExpression boxUnboxIfNecessary(JCExpression expr, ProducedType exprType, ProducedType targetType) {
        if (isBoxed(targetType) && !isBoxed(exprType)) {
            if (simplifyType(targetType).isExactly(exprType)) {
                // box
                expr = boxType(expr, exprType);
            }
        } else if (!isBoxed(targetType) && isBoxed(exprType)) {
            if (targetType.isExactly(simplifyType(exprType))) {
                // unbox
                expr = unboxType(expr, targetType);
            }
        }
        return expr;
    }

    // isBoxed() is be the wrong name.
    // It's more like: IF we would assign a value to a
    // variable of this type it would need to be boxed
    private boolean isBoxed(ProducedType type) {
        return (type  != null) && (isOptional(type) || type.getDeclaration() instanceof TypeParameter);
    }
    
    protected JCExpression unboxType(JCExpression expr, ProducedType targetType) {
        if (isCeylonNatural(targetType)) {
            expr = unboxNatural(expr);
        } else if (isCeylonInteger(targetType)) {
            expr = unboxInteger(expr);
        } else if (isCeylonFloat(targetType)) {
            expr = unboxFloat(expr);
        } else if (isCeylonString(targetType)) {
            expr = unboxString(expr);
        } else if (isCeylonCharacter(targetType)) {
            expr = unboxCharacter(expr);
        }
        return expr;
    }

    protected JCExpression boxType(JCExpression expr, ProducedType exprType) {
        if (isCeylonNatural(exprType)) {
            expr = boxNatural(expr);
        } else if (isCeylonInteger(exprType)) {
            expr = boxInteger(expr);
        } else if (isCeylonFloat(exprType)) {
            expr = boxFloat(expr);
        } else if (isCeylonString(exprType)) {
            expr = boxString(expr);
        } else if (isCeylonCharacter(exprType)) {
            expr = boxCharacter(expr);
        }
        return expr;
    }
    
    private JCTree.JCMethodInvocation boxNatural(JCExpression value) {
        return makeBoxType(value, syms().ceylonNaturalType);
    }
    
    private JCTree.JCMethodInvocation boxInteger(JCExpression value) {
        return makeBoxType(value, syms().ceylonIntegerType);
    }
    
    private JCTree.JCMethodInvocation boxFloat(JCExpression value) {
        return makeBoxType(value, syms().ceylonFloatType);
    }
    
    private JCTree.JCMethodInvocation boxString(JCExpression value) {
        return makeBoxType(value, syms().ceylonStringType);
    }
    
    private JCTree.JCMethodInvocation boxCharacter(JCExpression value) {
        return makeBoxType(value, syms().ceylonCharacterType);
    }
    
    private JCTree.JCMethodInvocation makeBoxType(JCExpression value, Type type) {
        return make().Apply(null, makeSelect(makeIdent(type), "instance"), List.<JCExpression>of(value));
    }
    
    private JCTree.JCMethodInvocation unboxNatural(JCExpression value) {
        return makeUnboxType(value, "longValue");
    }
    
    private JCTree.JCMethodInvocation unboxInteger(JCExpression value) {
        return makeUnboxType(value, "intValue");
    }
    
    private JCTree.JCMethodInvocation unboxFloat(JCExpression value) {
        return makeUnboxType(value, "doubleValue");
    }
    
    private JCTree.JCMethodInvocation unboxString(JCExpression value) {
        return makeUnboxType(value, "toString");
    }
    
    private JCTree.JCMethodInvocation unboxCharacter(JCExpression value) {
        return makeUnboxType(value, "charValue");
    }
    
    private JCTree.JCMethodInvocation makeUnboxType(JCExpression value, String unboxMethodName) {
        return make().Apply(null, makeSelect(value, unboxMethodName), List.<JCExpression>nil());
    }
    
    /*
     * Sequences
     */
    
    protected JCExpression makeSequence(java.util.List<Expression> list, ProducedType seqElemType) {
        ListBuffer<JCExpression> elems = new ListBuffer<JCExpression>();
        for (Expression expr : list) {
            elems.append(boxType(expressionGen().transformExpression(expr), expr.getTypeModel()));
        }
        ProducedType seqType = typeFact().makeDefaultSequenceType(seqElemType);
        JCExpression typeExpr = makeJavaType(seqType, CeylonTransformer.CLASS_NEW);
        return make().NewClass(null, null, typeExpr, elems.toList(), null);
    }
    
    protected JCExpression makeEmpty() {
        return globalGen().getGlobalValue(makeIdent("ceylon", "language"), "$empty");
    }
    
    /*
     * Variable name substitution
     */
    
    @SuppressWarnings("serial")
    protected static class VarMapper extends HashMap<String, String> {
    }
    
    private Map<String, String> getVarMapper() {
        VarMapper map = context.get(VarMapper.class);
        if (map == null) {
            map = new VarMapper();
            context.put(VarMapper.class, map);
        }
        return map;
    }
    
    String addVariableSubst(String origVarName, String substVarName) {
        return getVarMapper().put(origVarName, substVarName);
    }

    void removeVariableSubst(String origVarName, String prevSubst) {
        if (prevSubst != null) {
            getVarMapper().put(origVarName, prevSubst);
        } else {
            getVarMapper().remove(origVarName);
        }
    }
    
    /*
     * Checks a global map of variable name substitutions and returns
     * either the original name if none was found or the substitute.
     */
    String substitute(String varName) {
        if (getVarMapper().containsKey(varName)) {
            return getVarMapper().get(varName);            
        } else {
            return varName;
        }
    }

}
