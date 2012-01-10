/*
 * Copyright Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the authors tag. All rights reserved.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License version 2.
 * 
 * This particular file is subject to the "Classpath" exception as provided in the 
 * LICENSE file that accompanied this code.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */

package com.redhat.ceylon.compiler.java.codegen;

import static com.sun.tools.javac.code.Flags.FINAL;

import java.util.HashMap;
import java.util.Map;

import org.antlr.runtime.Token;

import com.redhat.ceylon.compiler.java.loader.CeylonModelLoader;
import com.redhat.ceylon.compiler.java.loader.TypeFactory;
import com.redhat.ceylon.compiler.java.tools.CeylonLog;
import com.redhat.ceylon.compiler.java.util.Util;
import com.redhat.ceylon.compiler.loader.ModelLoader.DeclarationType;
import com.redhat.ceylon.compiler.typechecker.model.BottomType;
import com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.IntersectionType;
import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.Module;
import com.redhat.ceylon.compiler.typechecker.model.ModuleImport;
import com.redhat.ceylon.compiler.typechecker.model.Package;
import com.redhat.ceylon.compiler.typechecker.model.Parameter;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.TypeParameter;
import com.redhat.ceylon.compiler.typechecker.model.TypedDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.UnionType;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Expression;
import com.sun.tools.javac.code.BoundKind;
import com.sun.tools.javac.code.Symtab;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.code.TypeTags;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.Factory;
import com.sun.tools.javac.tree.JCTree.JCAnnotation;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCFieldAccess;
import com.sun.tools.javac.tree.JCTree.JCLiteral;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.tree.JCTree.LetExpr;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Log;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.util.Position;
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
    protected Log log;

    public AbstractTransformer(Context context) {
        this.context = context;
        make = TreeMaker.instance(context);
        names = Name.Table.instance(context);
        syms = Symtab.instance(context);
        loader = CeylonModelLoader.instance(context);
        typeFact = TypeFactory.instance(context);
        log = CeylonLog.instance(context);
    }

    @Override
    public TreeMaker make() {
        return make;
    }

    private static JavaPositionsRetriever javaPositionsRetriever = null;
    public static void trackNodePositions(JavaPositionsRetriever positionsRetriever) {
        javaPositionsRetriever = positionsRetriever;
    }
    
    @Override
    public Factory at(Node node) {
        if (node == null) {
            make.at(Position.NOPOS);
            
        }
        else {
            Token token = node.getToken();
            if (token != null) {
                int tokenStartPosition = getMap().getStartPosition(token.getLine()) + token.getCharPositionInLine();
                make().at(tokenStartPosition);
                if (javaPositionsRetriever != null) {
                    javaPositionsRetriever.addCeylonNode(tokenStartPosition, node);
                }
            }
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
    
    protected JCExpression makeUnquotedIdent(String ident) {
        return make().Ident(names().fromString(ident));
    }

    protected JCExpression makeQuotedIdent(String ident) {
        return make().Ident(names().fromString(Util.quoteIfJavaKeyword(ident)));
    }

    protected JCExpression makeQualIdentFromString(String nameAsString) {
        return makeQualIdent(null, nameAsString.split("\\."));
    }

    protected JCExpression makeQualIdent(Iterable<String> components) {
        JCExpression type = null;
        for (String component : components) {
            if (type == null)
                type = makeUnquotedIdent(component);
            else
                type = makeSelect(type, component);
        }
        return type;
    }

    protected JCExpression makeQualIdent(JCExpression expr, String... names) {
        if (names != null) {
            for (String component : names) {
                if (component != null) {
                    if (expr == null) {
                        expr = makeUnquotedIdent(component);
                    } else {
                        expr = makeSelect(expr, component);
                    }
                }
            }
        }
        return expr;
    }

    protected JCExpression makeFQIdent(String... components) {
        return makeQualIdent(makeUnquotedIdent(""), components);
    }

    protected JCExpression makeIdent(Type type) {
        return make().QualIdent(type.tsym);
    }

    protected JCFieldAccess makeSelect(JCExpression s1, String s2) {
        return make().Select(s1, names().fromString(s2));
    }

    protected JCFieldAccess makeSelect(String s1, String s2) {
        return makeSelect(makeUnquotedIdent(s1), s2);
    }

    protected JCFieldAccess makeSelect(String s1, String s2, String... rest) {
        return makeSelect(makeSelect(s1, s2), rest);
    }

    protected JCFieldAccess makeSelect(JCFieldAccess s1, String[] rest) {
        JCFieldAccess acc = s1;
        for (String s : rest) {
            acc = makeSelect(acc, s);
        }
        return acc;
    }

    protected JCLiteral makeNull() {
        return make().Literal(TypeTags.BOT, null);
    }
    
    protected JCExpression makeInteger(int i) {
        return make().Literal(Integer.valueOf(i));
    }
    
    protected JCExpression makeLong(long i) {
        return make().Literal(Long.valueOf(i));
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
    
    // Creates a "foo foo = new foo();"
    protected JCTree.JCVariableDecl makeLocalIdentityInstance(String varName, boolean isShared) {
        JCExpression name = makeUnquotedIdent(varName);
        
        JCExpression initValue = makeNewClass(varName);
        List<JCAnnotation> annots = List.<JCAnnotation>nil();

        int modifiers = isShared ? 0 : FINAL;
        JCTree.JCVariableDecl var = make().VarDef(
                make().Modifiers(modifiers, annots), 
                names().fromString(varName), 
                name, 
                initValue);
        
        return var;
    }
    
    // Creates a "new foo();"
    protected JCTree.JCNewClass makeNewClass(String className) {
        return makeNewClass(className, List.<JCTree.JCExpression>nil());
    }
    
    // Creates a "new foo(arg1, arg2, ...);"
    protected JCTree.JCNewClass makeNewClass(String className, List<JCTree.JCExpression> args) {
        JCExpression name = makeUnquotedIdent(className);
        return makeNewClass(name, args);
    }
    
    // Creates a "new foo(arg1, arg2, ...);"
    protected JCTree.JCNewClass makeNewClass(JCExpression clazz, List<JCTree.JCExpression> args) {
        return make().NewClass(null, null, clazz, args, null);
    }

    protected JCVariableDecl makeVar(String varName, JCExpression typeExpr, JCExpression valueExpr) {
        return make().VarDef(make().Modifiers(0), names().fromString(varName), typeExpr, valueExpr);
    }
    
    // Creates a "( let var1=expr1,var2=expr2,...,varN=exprN in varN; )"
    // or a "( let var1=expr1,var2=expr2,...,varN=exprN,exprO in exprO; )"
    protected JCExpression makeLetExpr(JCExpression... args) {
        return makeLetExpr(tempName(), null, args);
    }

    // Creates a "( let var1=expr1,var2=expr2,...,varN=exprN in statements; varN; )"
    // or a "( let var1=expr1,var2=expr2,...,varN=exprN,exprO in statements; exprO; )"
    protected JCExpression makeLetExpr(String varBaseName, List<JCStatement> statements, JCExpression... args) {
        String varName = null;
        List<JCVariableDecl> decls = List.nil();
        int i;
        for (i = 0; (i + 1) < args.length; i += 2) {
            JCExpression typeExpr = args[i];
            JCExpression valueExpr = args[i+1];
            varName = varBaseName + ((args.length > 3) ? "$" + i : "");
            JCVariableDecl varDecl = makeVar(varName, typeExpr, valueExpr);
            decls = decls.append(varDecl);
        }
        
        JCExpression result;
        if (i == args.length) {
            result = makeUnquotedIdent(varName);
        } else {
            result = args[i];
        }
        if (statements != null) {
            return make().LetExpr(decls, statements, result);
        } else {
            return make().LetExpr(decls, result);
        }
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

    boolean isBooleanTrue(Declaration decl) {
        return decl == loader().getDeclaration("ceylon.language.$true", DeclarationType.VALUE);
    }
    
    boolean isBooleanFalse(Declaration decl) {
        return decl == loader().getDeclaration("ceylon.language.$false", DeclarationType.VALUE);
    }
    
    // A type is optional when it is a union of Nothing|Type...
    protected boolean isOptional(ProducedType type) {
        return typeFact().isOptionalType(type);
    }
    
    protected boolean isNothing(ProducedType type) {
        return typeFact.getNothingDeclaration().getType().isExactly(type);
    }
    
    protected boolean isVoid(ProducedType type) {
        return typeFact.getVoidDeclaration().getType().isExactly(type);
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
        } else if (tdecl instanceof IntersectionType && tdecl.getSatisfiedTypes().size() == 1) {
            // Special case when the Intersection contains only a single SatisfiedType
            // FIXME This is not correct! We might lose information about type arguments!
            type = tdecl.getSatisfiedTypes().get(0);
        }
        
        return type;
    }
    
    protected ProducedType actualType(Tree.TypedDeclaration decl) {
        return decl.getType().getTypeModel();
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
                || typeFact().isUnion(type)|| typeFact().isIntersection(type));
    }
    
    protected boolean willEraseToException(ProducedType type) {
        type = simplifyType(type);
        return (sameType(syms().ceylonExceptionType, type));
    }

    protected boolean isCeylonString(ProducedType type) {
        return (sameType(syms().ceylonStringType, type));
    }
    
    protected boolean isCeylonBoolean(ProducedType type) {
        return (sameType(syms().ceylonBooleanType, type));
    }
    
    protected boolean isCeylonInteger(ProducedType type) {
        return (sameType(syms().ceylonIntegerType, type));
    }
    
    protected boolean isCeylonFloat(ProducedType type) {
        return (sameType(syms().ceylonFloatType, type));
    }
    
    protected boolean isCeylonCharacter(ProducedType type) {
        return (sameType(syms().ceylonCharacterType, type));
    }

    protected boolean isCeylonArray(ProducedType type) {
        return (sameType(syms().ceylonJavaObjectArraySequenceType, type.getDeclaration().getType()));
    }
    
    protected boolean isCeylonBasicType(ProducedType type) {
        return (isCeylonString(type) || isCeylonBoolean(type) || isCeylonInteger(type) || isCeylonFloat(type) || isCeylonCharacter(type));
    }

    /*
     * Java Type creation
     */
    
    static final int SATISFIES = 1 << 0;
    static final int EXTENDS = 1 << 1;
    static final int TYPE_ARGUMENT = 1 << 2;
    static final int NO_PRIMITIVES = 1 << 2; // Yes, same as TYPE_ARGUMENT
    static final int WANT_RAW_TYPE = 1 << 3;
    static final int CATCH = 1 << 4;
    static final int SMALL_TYPE = 1 << 5;
    static final int CLASS_NEW = 1 << 6;

    protected JCExpression makeJavaType(TypedDeclaration typeDecl) {
        boolean isGenericsType = isGenericsImplementation(typeDecl);
        return makeJavaType(typeDecl.getType(), isGenericsType ? AbstractTransformer.TYPE_ARGUMENT : 0);
    }

    protected JCExpression makeJavaType(ProducedType producedType) {
        return makeJavaType(producedType, 0);
    }

    protected JCExpression makeJavaType(ProducedType type, int flags) {
        if(type == null)
            return make().Erroneous();
        
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
        } else if (willEraseToException(type)) {
            if ((flags & CLASS_NEW) != 0
                    || (flags & EXTENDS) != 0) {
                return make().Type(syms().ceylonExceptionType);
            } else if ((flags & CATCH) != 0) {
                return make().Type(syms().exceptionType);
            } else {
                return make().Type(syms().throwableType);
            }
        } else if ((flags & (SATISFIES | EXTENDS | TYPE_ARGUMENT | CLASS_NEW)) == 0 && !isOptional(type)) {
            if (isCeylonString(type)) {
                return make().Type(syms().stringType);
            } else if (isCeylonBoolean(type)) {
                return make().TypeIdent(TypeTags.BOOLEAN);
            } else if (isCeylonInteger(type)) {
                if ((flags & SMALL_TYPE) != 0) {
                    return make().TypeIdent(TypeTags.INT);
                } else {
                    return make().TypeIdent(TypeTags.LONG);
                }
            } else if (isCeylonFloat(type)) {
                if ((flags & SMALL_TYPE) != 0) {
                    return make().TypeIdent(TypeTags.FLOAT);
                } else {
                    return make().TypeIdent(TypeTags.DOUBLE);
                }
            } else if (isCeylonCharacter(type)) {
                return make().TypeIdent(TypeTags.INT);
            } else if (isCeylonArray(type)) {
                ProducedType simpleType = simplifyType(type);
                TypeDeclaration tdecl = simpleType.getDeclaration();
                java.util.List<ProducedType> tal = simpleType.getTypeArgumentList();
                return make().TypeArray(makeJavaType(tal.get(0), 0));
            }
        }
        
        JCExpression jt;
        ProducedType simpleType = simplifyType(type);
        TypeDeclaration tdecl = simpleType.getDeclaration();
        java.util.List<ProducedType> tal = simpleType.getTypeArgumentList();
        
        if (((flags & WANT_RAW_TYPE) == 0) && tal != null && !tal.isEmpty()) {
            // GENERIC TYPES

            ListBuffer<JCExpression> typeArgs = new ListBuffer<JCExpression>();

            int idx = 0;
            for (ProducedType ta : tal) {
                if (isOptional(ta)) {
                    // For an optional type T?:
                    // - The Ceylon type Foo<T?> results in the Java type Foo<T>.
                    ta = typeFact().getDefiniteType(ta);
                }
                if (typeFact().isUnion(ta) || typeFact().isIntersection(ta)) {
                    // For any other union type U|V (U nor V is Optional):
                    // - The Ceylon type Foo<U|V> results in the raw Java type Foo.
                    // For any other intersection type U|V:
                    // - The Ceylon type Foo<U&V> results in the raw Java type Foo.
                    // A bit ugly, but we need to escape from the loop and create a raw type, no generics
                    ProducedType iterType = typeFact().getNonemptyIterableType(typeFact().getDefiniteType(ta));
                    // don't break if the union type is erased to something better than Object
                    if(iterType == null){
                        typeArgs = null;
                        break;
                    }else
                        ta = iterType;
                }
                JCExpression jta;
                if (sameType(syms().ceylonVoidType, ta)) {
                    // For the root type Void:
                    if ((flags & (SATISFIES | EXTENDS)) != 0) {
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
                            jta = make().Wildcard(make().TypeBoundKind(BoundKind.UNBOUND), makeJavaType(ta, flags));
                        } else {
                            jta = make().Type(syms().objectType);
                        }
                    }
                } else if (ta.getDeclaration() instanceof BottomType) {
                    // For the bottom type Bottom:
                    if ((flags & (SATISFIES | EXTENDS)) != 0) {
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
                    if ((flags & (SATISFIES | EXTENDS)) != 0) {
                        // - The Ceylon type Foo<T> appearing in an extends or satisfies clause
                        //   results in the Java type Foo<T>
                        jta = makeJavaType(ta, TYPE_ARGUMENT);
                    } else {
                        // - The Ceylon type Foo<T> appearing anywhere else results in the Java type
                        // - Foo<T> if Foo is invariant in T,
                        // - Foo<? extends T> if Foo is covariant in T, or
                        // - Foo<? super T> if Foo is contravariant in T
                        TypeParameter tp = tdecl.getTypeParameters().get(idx);
                        if (((flags & CLASS_NEW) == 0) && tp.isContravariant()) {
                            jta = make().Wildcard(make().TypeBoundKind(BoundKind.SUPER), makeJavaType(ta, TYPE_ARGUMENT));
                        } else if (((flags & CLASS_NEW) == 0) && tp.isCovariant()) {
                            jta = make().Wildcard(make().TypeBoundKind(BoundKind.EXTENDS), makeJavaType(ta, TYPE_ARGUMENT));
                        } else {
                            jta = makeJavaType(ta, TYPE_ARGUMENT);
                        }
                    }
                }
                typeArgs.add(jta);
                idx++;
            }

            if (typeArgs != null && typeArgs.size() > 0) {
                jt = make().TypeApply(makeQualIdentFromString(getDeclarationName(tdecl)), typeArgs.toList());
            } else {
                jt = makeQualIdentFromString(getDeclarationName(tdecl));
            }
        } else {
            // For an ordinary class or interface type T:
            // - The Ceylon type T results in the Java type T
            if(tdecl instanceof TypeParameter)
                jt = makeUnquotedIdent(tdecl.getName());
            else
                jt = makeQualIdentFromString(getDeclarationName(tdecl));
        }
        
        return jt;
    }
    
    private String getDeclarationName(Declaration decl) {
        if (decl.getContainer() instanceof Method) {
            return decl.getName();
        } else {
            return decl.getQualifiedNameString();
        }
    }
    
    protected ProducedType getThisType(Tree.Declaration decl) {
        if (decl instanceof Tree.TypeDeclaration) {
            return getThisType(((Tree.TypeDeclaration)decl).getDeclarationModel());
        } else {
            return getThisType(((Tree.TypedDeclaration)decl).getDeclarationModel());
        }
    }
    
    protected ProducedType getThisType(Declaration decl) {
        ProducedType thisType;
        if (decl instanceof ClassOrInterface) {
            thisType = ((ClassOrInterface)decl).getType();
        } else if (decl.isToplevel()) {
            thisType = ((TypedDeclaration)decl).getType();
        } else {
            thisType = getThisType((Declaration)decl.getContainer());
        }
        return thisType;
    }
    
    /*
     * Annotation generation
     */
    
    List<JCAnnotation> makeAtOverride() {
        return List.<JCAnnotation> of(make().Annotation(makeIdent(syms().overrideType), List.<JCExpression> nil()));
    }

    // FIXME
    public static boolean disableModelAnnotations = false;
    
    public boolean checkCompilerAnnotations(Tree.Declaration decl){
        boolean old = disableModelAnnotations;
        if(Util.hasCompilerAnnotation(decl, "nomodel"))
            disableModelAnnotations  = true;
        return old;
    }

    public void resetCompilerAnnotations(boolean value){
        disableModelAnnotations = value;
    }

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

    protected List<JCAnnotation> makeAtModule(Module module) {
        String name = module.getNameAsString();
        String version = module.getVersion();
        java.util.List<ModuleImport> dependencies = module.getImports();
        ListBuffer<JCExpression> imports = new ListBuffer<JCTree.JCExpression>();
        for(ModuleImport dependency : dependencies){
            Module dependencyModule = dependency.getModule();
            JCExpression dependencyName = make().Assign(makeUnquotedIdent("name"), make().Literal(dependencyModule.getNameAsString()));
            JCExpression dependencyVersion = null;
            if(dependencyModule.getVersion() != null)
                dependencyVersion = make().Assign(makeUnquotedIdent("version"), make().Literal(dependencyModule.getVersion()));
            List<JCExpression> spec;
            if(dependencyVersion != null)
                spec = List.<JCExpression>of(dependencyName, dependencyVersion);
            else
                spec = List.<JCExpression>of(dependencyName);
            JCAnnotation atImport = make().Annotation(makeIdent(syms().ceylonAtImportType), spec);
            // TODO : add the export & optional annotations also ?
            imports.add(atImport);
        }
        JCExpression nameAttribute = make().Assign(makeUnquotedIdent("name"), make().Literal(name));
        JCExpression versionAttribute = make().Assign(makeUnquotedIdent("version"), make().Literal(version));
        JCExpression importAttribute = make().Assign(makeUnquotedIdent("dependencies"), make().NewArray(null, null, imports.toList()));
        return makeModelAnnotation(syms().ceylonAtModuleType, 
                List.<JCExpression>of(nameAttribute, versionAttribute, importAttribute));
    }

    protected List<JCAnnotation> makeAtPackage(Package pkg) {
        String name = pkg.getNameAsString();
        boolean shared = pkg.isShared();
        JCExpression nameAttribute = make().Assign(makeUnquotedIdent("name"), make().Literal(name));
        JCExpression sharedAttribute = make().Assign(makeUnquotedIdent("shared"), makeBoolean(shared));
        return makeModelAnnotation(syms().ceylonAtPackageType, 
                List.<JCExpression>of(nameAttribute, sharedAttribute));
    }

    protected List<JCAnnotation> makeAtName(String name) {
        return makeModelAnnotation(syms().ceylonAtNameType, List.<JCExpression>of(make().Literal(name)));
    }

    protected List<JCAnnotation> makeAtType(String name) {
        return makeModelAnnotation(syms().ceylonAtTypeInfoType, List.<JCExpression>of(make().Literal(name)));
    }

    public JCAnnotation makeAtTypeParameter(String name, java.util.List<ProducedType> satisfiedTypes, boolean covariant, boolean contravariant) {
        JCExpression nameAttribute = make().Assign(makeUnquotedIdent("value"), make().Literal(name));
        // variance
        String variance = "NONE";
        if(covariant)
            variance = "OUT";
        else if(contravariant)
            variance = "IN";
        JCExpression varianceAttribute = make().Assign(makeUnquotedIdent("variance"), 
                make().Select(makeIdent(syms().ceylonVarianceType), names().fromString(variance)));
        // upper bounds
        ListBuffer<JCExpression> upperBounds = new ListBuffer<JCTree.JCExpression>();
        for(ProducedType satisfiedType : satisfiedTypes){
            String type = serialiseTypeSignature(satisfiedType);
            upperBounds.append(make().Literal(type));
        }
        JCExpression satisfiesAttribute = make().Assign(makeUnquotedIdent("satisfies"), 
                make().NewArray(null, null, upperBounds.toList()));
        // all done
        return make().Annotation(makeIdent(syms().ceylonAtTypeParameter), 
                List.<JCExpression>of(nameAttribute, varianceAttribute, satisfiesAttribute));
    }

    public List<JCAnnotation> makeAtTypeParameters(List<JCExpression> typeParameters) {
        JCExpression value = make().NewArray(null, null, typeParameters);
        return makeModelAnnotation(syms().ceylonAtTypeParameters, List.of(value));
    }

    protected List<JCAnnotation> makeAtSequenced() {
        return makeModelAnnotation(syms().ceylonAtSequencedType);
    }

    protected List<JCAnnotation> makeAtDefaulted() {
        return makeModelAnnotation(syms().ceylonAtDefaultedType);
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

    protected List<JCAnnotation> makeAtClass(ProducedType extendedType) {
        List<JCExpression> attributes = List.nil();
        if(!extendedType.isExactly(typeFact.getIdentifiableObjectDeclaration().getType())){
            JCExpression extendsAttribute = make().Assign(makeUnquotedIdent("extendsType"), 
                    make().Literal(serialiseTypeSignature(extendedType)));
            attributes = attributes.prepend(extendsAttribute);
        }
        return makeModelAnnotation(syms().ceylonAtClassType, attributes);
    }

    protected List<JCAnnotation> makeAtSatisfiedTypes(java.util.List<ProducedType> satisfiedTypes) {
        if(satisfiedTypes.isEmpty())
            return List.nil();
        ListBuffer<JCExpression> upperBounds = new ListBuffer<JCTree.JCExpression>();
        for(ProducedType satisfiedType : satisfiedTypes){
            String type = serialiseTypeSignature(satisfiedType);
            upperBounds.append(make().Literal(type));
        }
        JCExpression satisfiesAttribute = make().Assign(makeUnquotedIdent("value"), 
                make().NewArray(null, null, upperBounds.toList()));
        
        return makeModelAnnotation(syms().ceylonAtSatisfiedTypes, List.of(satisfiesAttribute));
    }

    protected List<JCAnnotation> makeAtIgnore() {
        return makeModelAnnotation(syms().ceylonAtIgnore);
    }

    protected boolean needsAnnotations(Declaration decl) {
        Declaration reqdecl = decl;
        if (reqdecl instanceof Parameter) {
            Parameter p = (Parameter)reqdecl;
            reqdecl = p.getDeclaration();
        }
        return reqdecl.isToplevel() || (reqdecl.isClassOrInterfaceMember() && reqdecl.isShared());
    }

    protected List<JCTree.JCAnnotation> makeJavaTypeAnnotations(TypedDeclaration decl) {
        if(decl.getType() == null)
            return List.nil();
        return makeJavaTypeAnnotations(decl.getType(), needsAnnotations(decl));
    }

    protected List<JCTree.JCAnnotation> makeJavaTypeAnnotations(ProducedType type, boolean required) {
        if (!required)
            return List.nil();
        // Add the original type to the annotations
        return makeAtType(serialiseTypeSignature(type));
    }
    
    protected String serialiseTypeSignature(ProducedType type){
        if(isTypeParameter(type))
            return type.getProducedTypeName();
        return type.getProducedTypeQualifiedName();

    }
    
    /*
     * Boxing
     */
    public enum BoxingStrategy {
        UNBOXED, BOXED, INDIFFERENT;
    }

    protected JCExpression boxUnboxIfNecessary(JCExpression javaExpr, Tree.Term expr,
            ProducedType exprType,
            BoxingStrategy boxingStrategy) {
        boolean exprBoxed = !Util.isUnBoxed(expr);
        return boxUnboxIfNecessary(javaExpr, exprBoxed, exprType, boxingStrategy);
    }
    
    protected JCExpression boxUnboxIfNecessary(JCExpression javaExpr, boolean exprBoxed,
            ProducedType exprType,
            BoxingStrategy boxingStrategy) {
        if(boxingStrategy == BoxingStrategy.INDIFFERENT)
            return javaExpr;
        boolean targetBoxed = boxingStrategy == BoxingStrategy.BOXED;
        // only box if the two differ
        if(targetBoxed == exprBoxed)
            return javaExpr;
        if (targetBoxed) {
            // box
            javaExpr = boxType(javaExpr, exprType);
        } else {
            // unbox
            javaExpr = unboxType(javaExpr, exprType);
        }
        return javaExpr;
    }

    protected boolean isTypeParameter(ProducedType type) {
        return type.getDeclaration() instanceof TypeParameter;
    }
    
    protected JCExpression unboxType(JCExpression expr, ProducedType targetType) {
        if (isCeylonInteger(targetType)) {
            expr = unboxInteger(expr);
        } else if (isCeylonFloat(targetType)) {
            expr = unboxFloat(expr);
        } else if (isCeylonString(targetType)) {
            expr = unboxString(expr);
        } else if (isCeylonCharacter(targetType)) {
            expr = unboxCharacter(expr);
        } else if (isCeylonBoolean(targetType)) {
            expr = unboxBoolean(expr);
        } else if (isCeylonArray(targetType)) {
            expr = unboxArray(expr);
        }
        return expr;
    }

    protected JCExpression boxType(JCExpression expr, ProducedType exprType) {
        if (isCeylonInteger(exprType)) {
            expr = boxInteger(expr);
        } else if (isCeylonFloat(exprType)) {
            expr = boxFloat(expr);
        } else if (isCeylonString(exprType)) {
            expr = boxString(expr);
        } else if (isCeylonCharacter(exprType)) {
            expr = boxCharacter(expr);
        } else if (isCeylonBoolean(exprType)) {
            expr = boxBoolean(expr);
        } else if (isCeylonArray(exprType)) {
            expr = boxArray(expr);
        }
        return expr;
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
    
    private JCTree.JCMethodInvocation boxBoolean(JCExpression value) {
        return makeBoxType(value, syms().ceylonBooleanType);
    }
    
    private JCTree.JCMethodInvocation boxArray(JCExpression value) {
        return makeBoxType(value, syms().ceylonJavaObjectArraySequenceType);
    }
    
    private JCTree.JCMethodInvocation makeBoxType(JCExpression value, Type type) {
        return make().Apply(null, makeSelect(makeIdent(type), "instance"), List.<JCExpression>of(value));
    }
    
    private JCTree.JCMethodInvocation unboxInteger(JCExpression value) {
        return makeUnboxType(value, "longValue");
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
    
    private JCTree.JCMethodInvocation unboxBoolean(JCExpression value) {
        return makeUnboxType(value, "booleanValue");
    }
    
    private JCTree.JCMethodInvocation unboxArray(JCExpression value) {
        return makeUnboxType(value, "toArray");
    }
    
    private JCTree.JCMethodInvocation makeUnboxType(JCExpression value, String unboxMethodName) {
        return make().Apply(null, makeSelect(value, unboxMethodName), List.<JCExpression>nil());
    }
    
    protected ProducedType determineExpressionType(Tree.Expression expr) {
        return determineExpressionType(expr.getTerm());
    }
    
    protected ProducedType determineExpressionType(Tree.Term term) {
        ProducedType exprType = term.getTypeModel();
        if (term instanceof Tree.InvocationExpression) {
            Declaration decl = ((Tree.InvocationExpression)term).getPrimary().getDeclaration().getRefinedDeclaration();
            if (decl instanceof Method) {
                exprType = ((Method)decl).getType();
            }
        }
        return exprType;
    }

    protected boolean isGenericsImplementation(TypedDeclaration decl) {
        return ((TypedDeclaration)Util.getTopmostRefinedDeclaration(decl)).getTypeDeclaration() instanceof TypeParameter;
    }


    /*
     * Sequences
     */
    
    /**
     * Returns a JCExpression along the lines of 
     * {@code new ArraySequence<seqElemType>(list...)}
     * @param list The elements in the sequence
     * @param seqElemType The sequence type parameter
     * @return a JCExpression
     * @see #makeSequenceRaw(java.util.List)
     */
    protected JCExpression makeSequence(java.util.List<Expression> list, ProducedType seqElemType) {
        ListBuffer<JCExpression> elems = new ListBuffer<JCExpression>();
        for (Expression expr : list) {
            // no need for erasure casts here
            elems.append(expressionGen().transformExpression(expr));
        }
        ProducedType seqType = typeFact().getDefaultSequenceType(seqElemType);
        JCExpression typeExpr = makeJavaType(seqType, CeylonTransformer.TYPE_ARGUMENT);
        return makeNewClass(typeExpr, elems.toList());
    }
    
    /**
     * Returns a JCExpression along the lines of 
     * {@code new ArraySequence(list...)}
     * @param list The elements in the sequence
     * @return a JCExpression
     * @see #makeSequence(java.util.List, ProducedType)
     */
    protected JCExpression makeSequenceRaw(java.util.List<Expression> list) {
        ListBuffer<JCExpression> elems = new ListBuffer<JCExpression>();
        for (Expression expr : list) {
            // no need for erasure casts here
            elems.append(expressionGen().transformExpression(expr));
        }
        ProducedType seqType = typeFact().getDefaultSequenceType(typeFact().getObjectDeclaration().getType());
        JCExpression typeExpr = makeJavaType(seqType, CeylonTransformer.WANT_RAW_TYPE);
        return makeNewClass(typeExpr, elems.toList());
    }
    
    protected JCExpression makeEmpty() {
        return make().Apply(
                List.<JCTree.JCExpression>nil(),
                makeSelect("ceylon", "language", Util.quoteIfJavaKeyword("$empty"), Util.getGetterName("$empty")),
                List.<JCTree.JCExpression>nil());
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

    // Creates comparisons of expressions against types
    // The expression to pass must be free of side-effects!
    protected JCExpression makeTypeTest(JCExpression testExpr, ProducedType type) {
        return makeTypeTest(testExpr, testExpr, type);
    }

    // Creates comparisons of expressions against types
    // In case the expression is free of side effects just pass the same one twice
    // otherwise pass the expression with side-effect as the first one and an alternative
    // one free of side effects as the second
    protected JCExpression makeTypeTest(JCExpression firstTestExpr, JCExpression restTestExpr, ProducedType type) {
        JCExpression result = null;
        if (typeFact().isUnion(type)) {
            UnionType union = (UnionType)type.getDeclaration();
            for (ProducedType pt : union.getCaseTypes()) {
                JCExpression partExpr = makeTypeTest(firstTestExpr, restTestExpr, pt);
                if (result == null) {
                    result = partExpr;
                } else {
                    result = make().Binary(JCTree.OR, result, partExpr);
                }
                firstTestExpr = restTestExpr;
            }
        } else if (typeFact().isIntersection(type)) {
            IntersectionType union = (IntersectionType)type.getDeclaration();
            for (ProducedType pt : union.getSatisfiedTypes()) {
                JCExpression partExpr = makeTypeTest(firstTestExpr, restTestExpr, pt);
                if (result == null) {
                    result = partExpr;
                } else {
                    result = make().Binary(JCTree.AND, result, partExpr);
                }
                firstTestExpr = restTestExpr;
            }
        } else if (type.isExactly(typeFact().getNothingDeclaration().getType())){
            // is Nothing => is null
            return make().Binary(JCTree.EQ, firstTestExpr, makeNull());
        } else if (type.isExactly(typeFact().getObjectDeclaration().getType())){
            // is Object => is not null
            return make().Binary(JCTree.NE, firstTestExpr, makeNull());
        } else if (type.isExactly(typeFact().getVoidDeclaration().getType())){
            // everything is Void, it's the root of the hierarchy
            return makeIgnoredEvalAndReturn(firstTestExpr, makeBoolean(true));
        } else if (type.isExactly(typeFact().getEqualityDeclaration().getType())){
            // it's erased
            return makeUtilInvocation("isEquality", List.of(firstTestExpr));
        } else if (type.isExactly(typeFact().getIdentifiableObjectDeclaration().getType())){
            // it's erased
            return makeUtilInvocation("isIdentifiableObject", List.of(firstTestExpr));
        } else if (type.getDeclaration() instanceof BottomType){
            // nothing is Bottom
            return makeIgnoredEvalAndReturn(firstTestExpr, makeBoolean(false));
        } else {
            JCExpression rawTypeExpr = makeJavaType(type, NO_PRIMITIVES | WANT_RAW_TYPE);
            result = make().TypeTest(firstTestExpr, rawTypeExpr);
        }
        return result;
    }

    /**
     * Invokes a static method of the Util helper class
     * @param methodName name of the method
     * @param params parameters
     * @return the invocation AST
     */
    protected JCExpression makeUtilInvocation(String methodName, List<JCExpression> params) {
        return make().Apply(null, make().Select(make().QualIdent(syms().ceylonUtilType.tsym), 
                                                names().fromString(methodName)), 
                            params);
    }

    protected LetExpr makeIgnoredEvalAndReturn(JCExpression toEval, JCExpression toReturn){
        // define a variable of type j.l.Object to hold the result of the evaluation
        JCVariableDecl def = makeVar(tempName(), make().Type(syms().objectType), toEval);
        // then ignore this result and return something else
        return make().LetExpr(def, toReturn);

    }

    public Context getContext() {
        return context;
    }
}
