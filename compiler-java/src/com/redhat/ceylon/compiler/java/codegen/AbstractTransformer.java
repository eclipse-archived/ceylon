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

import static com.redhat.ceylon.compiler.java.codegen.CodegenUtil.NameFlag.QUALIFIED;
import static com.sun.tools.javac.code.Flags.FINAL;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;

import org.antlr.runtime.Token;

import com.redhat.ceylon.compiler.java.codegen.CodegenUtil.NameFlag;
import com.redhat.ceylon.compiler.java.loader.CeylonModelLoader;
import com.redhat.ceylon.compiler.java.loader.TypeFactory;
import com.redhat.ceylon.compiler.java.tools.CeylonLog;
import com.redhat.ceylon.compiler.java.util.Util;
import com.redhat.ceylon.compiler.loader.AbstractModelLoader;
import com.redhat.ceylon.compiler.loader.ModelLoader.DeclarationType;
import com.redhat.ceylon.compiler.typechecker.model.Annotation;
import com.redhat.ceylon.compiler.typechecker.model.BottomType;
import com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Functional;
import com.redhat.ceylon.compiler.typechecker.model.FunctionalParameter;
import com.redhat.ceylon.compiler.typechecker.model.Generic;
import com.redhat.ceylon.compiler.typechecker.model.Interface;
import com.redhat.ceylon.compiler.typechecker.model.IntersectionType;
import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.Module;
import com.redhat.ceylon.compiler.typechecker.model.ModuleImport;
import com.redhat.ceylon.compiler.typechecker.model.Package;
import com.redhat.ceylon.compiler.typechecker.model.Parameter;
import com.redhat.ceylon.compiler.typechecker.model.ProducedReference;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.ProducedTypedReference;
import com.redhat.ceylon.compiler.typechecker.model.Scope;
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
import com.sun.tools.javac.tree.JCTree.JCIdent;
import com.sun.tools.javac.tree.JCTree.JCLiteral;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import com.sun.tools.javac.tree.JCTree.JCTypeParameter;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.tree.JCTree.LetExpr;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Log;
import com.sun.tools.javac.util.Names;
import com.sun.tools.javac.util.Position;
import com.sun.tools.javac.util.Position.LineMap;

/**
 * Base class for all delegating transformers
 */
public abstract class AbstractTransformer implements Transformation, LocalId {
    private Context context;
    private TreeMaker make;
    private Names names;
    private Symtab syms;
    private AbstractModelLoader loader;
    private TypeFactory typeFact;
    protected Log log;

    public AbstractTransformer(Context context) {
        this.context = context;
        make = TreeMaker.instance(context);
        names = Names.instance(context);
        syms = Symtab.instance(context);
        loader = CeylonModelLoader.instance(context);
        typeFact = TypeFactory.instance(context);
        log = CeylonLog.instance(context);
    }

    Context getContext() {
        return context;
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
    public Names names() {
        return names;
    }

    @Override
    public AbstractModelLoader loader() {
        return loader;
    }
    
    @Override
    public TypeFactory typeFact() {
        return typeFact;
    }

    void setMap(LineMap map) {
        gen().setMap(map);
    }

    LineMap getMap() {
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
    
    /** 
     * Makes an <strong>unquoted</strong> simple identifier
     * @param ident The identifier
     * @return The ident
     */
    JCExpression makeUnquotedIdent(String ident) {
        return make().Ident(names().fromString(ident));
    }

    /** 
     * Makes an <strong>quoted</strong> simple identifier
     * @param ident The identifier
     * @return The ident
     */
    JCIdent makeQuotedIdent(String ident) {
        return make().Ident(names().fromString(Util.quoteIfJavaKeyword(ident)));
    }
    
    /** 
     * Makes a <strong>quoted</strong> qualified (compound) identifier from 
     * the given qualified name. Each part of the name will be 
     * quoted if it is a Java keyword.
     * @param qualifiedName The qualified name 
     */
    JCExpression makeQuotedQualIdentFromString(String qualifiedName) {
        return makeQualIdent(null, Util.quoteJavaKeywords(qualifiedName.split("\\.")));
    }

    /** 
     * Makes an <strong>unquoted</strong> qualified (compound) identifier 
     * from the given qualified name components
     * @param components The components of the name.
     * @see #makeQuotedQualIdentFromString(String)
     */
    JCExpression makeQualIdent(Iterable<String> components) {
        JCExpression type = null;
        for (String component : components) {
            if (type == null)
                type = makeUnquotedIdent(component);
            else
                type = makeSelect(type, component);
        }
        return type;
    }
    
    JCExpression makeQuotedQualIdent(Iterable<String> components) {
        JCExpression type = null;
        for (String component : components) {
            if (type == null)
                type = makeQuotedIdent(component);
            else
                type = makeSelect(type, Util.quoteIfJavaKeyword(component));
        }
        return type;
    }

    /** 
     * Makes an <strong>unquoted</strong> qualified (compound) identifier 
     * from the given qualified name components
     * @param expr A starting expression (may be null)
     * @param names The components of the name (may be null)
     * @see #makeQuotedQualIdentFromString(String)
     */
    JCExpression makeQualIdent(JCExpression expr, String... names) {
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
    
    JCExpression makeQuotedQualIdent(JCExpression expr, String... names) {
        if (names != null) {
            for (String component : names) {
                if (component != null) {
                    if (expr == null) {
                        expr = makeQuotedIdent(component);
                    } else {
                        expr = makeSelect(expr, Util.quoteIfJavaKeyword(component));
                    }
                }
            }
        }
        return expr;
    }

    JCExpression makeFQIdent(String... components) {
        return makeQualIdent(makeUnquotedIdent(""), components);
    }

    JCExpression makeQuotedFQIdent(String... components) {
        return makeQuotedQualIdent(makeUnquotedIdent(""), components);
    }

    JCExpression makeQuotedFQIdent(String qualifiedName) {
        return makeQuotedFQIdent(Util.quoteJavaKeywords(qualifiedName.split("\\.")));
    }

    JCExpression makeIdent(Type type) {
        return make().QualIdent(type.tsym);
    }

    /**
     * Makes a <strong>unquoted</strong> field access
     * @param s1 The base expression
     * @param s2 The field to access
     * @return The field access
     */
    JCFieldAccess makeSelect(JCExpression s1, String s2) {
        return make().Select(s1, names().fromString(s2));
    }

    /**
     * Makes a <strong>unquoted</strong> field access
     * @param s1 The base expression
     * @param s2 The field to access
     * @return The field access
     */
    JCFieldAccess makeSelect(String s1, String s2) {
        return makeSelect(makeUnquotedIdent(s1), s2);
    }

    /**
     * Makes a sequence of <strong>unquoted</strong> field accesses
     * @param s1 The base expression
     * @param s2 The first field to access
     * @param rest The remaining fields to access
     * @return The field access
     */
    JCFieldAccess makeSelect(String s1, String s2, String... rest) {
        return makeSelect(makeSelect(s1, s2), rest);
    }

    JCFieldAccess makeSelect(JCFieldAccess s1, String[] rest) {
        JCFieldAccess acc = s1;
        for (String s : rest) {
            acc = makeSelect(acc, s);
        }
        return acc;
    }

    JCLiteral makeNull() {
        return make().Literal(TypeTags.BOT, null);
    }
    
    JCExpression makeInteger(int i) {
        return make().Literal(Integer.valueOf(i));
    }
    
    JCExpression makeLong(long i) {
        return make().Literal(Long.valueOf(i));
    }
    
    JCExpression makeBoolean(boolean b) {
        JCExpression expr;
        if (b) {
            expr = make().Literal(TypeTags.BOOLEAN, Integer.valueOf(1));
        } else {
            expr = make().Literal(TypeTags.BOOLEAN, Integer.valueOf(0));
        }
        return expr;
    }
    
    // Creates a "foo foo = new foo();"
    JCTree.JCVariableDecl makeLocalIdentityInstance(String varName, String className, boolean isShared) {
        JCExpression name = makeQuotedIdent(className);
        
        JCExpression initValue = makeNewClass(className, false);
        List<JCAnnotation> annots = List.<JCAnnotation>nil();

        int modifiers = isShared ? 0 : FINAL;
        JCTree.JCVariableDecl var = make().VarDef(
                make().Modifiers(modifiers, annots), 
                names().fromString(varName), 
                name, 
                initValue);
        
        return var;
    }
    JCTree.JCVariableDecl makeLocalIdentityInstance(String varName, boolean isShared) {
        return makeLocalIdentityInstance(varName, varName, isShared);
    }
    
    // Creates a "new foo();"
    JCTree.JCNewClass makeNewClass(String className, boolean fullyQualified) {
        return makeNewClass(className, List.<JCTree.JCExpression>nil(), fullyQualified);
    }
    
    // Creates a "new foo(arg1, arg2, ...);"
    JCTree.JCNewClass makeNewClass(String className, List<JCTree.JCExpression> args, boolean fullyQualified) {
        JCExpression name = fullyQualified ? makeQuotedFQIdent(className) : makeQuotedQualIdentFromString(className);
        return makeNewClass(name, args);
    }
    
    // Creates a "new foo(arg1, arg2, ...);"
    JCTree.JCNewClass makeNewClass(JCExpression clazz, List<JCTree.JCExpression> args) {
        if (args == null) {
            args = List.<JCTree.JCExpression>nil();
        }
        return make().NewClass(null, null, clazz, args, null);
    }

    JCVariableDecl makeVar(String varName, JCExpression typeExpr, JCExpression valueExpr) {
        return make().VarDef(make().Modifiers(0), names().fromString(varName), typeExpr, valueExpr);
    }
    
    // Creates a "( let var1=expr1,var2=expr2,...,varN=exprN in varN; )"
    // or a "( let var1=expr1,var2=expr2,...,varN=exprN,exprO in exprO; )"
    JCExpression makeLetExpr(JCExpression... args) {
        return makeLetExpr(tempName(), null, args);
    }

    // Creates a "( let var1=expr1,var2=expr2,...,varN=exprN in statements; varN; )"
    // or a "( let var1=expr1,var2=expr2,...,varN=exprN in statements; exprO; )"
    JCExpression makeLetExpr(String varBaseName, List<JCStatement> statements, JCExpression... args) {
        String varName = null;
        ListBuffer<JCStatement> decls = ListBuffer.lb();
        int i;
        for (i = 0; (i + 1) < args.length; i += 2) {
            JCExpression typeExpr = args[i];
            JCExpression valueExpr = args[i+1];
            varName = varBaseName + ((args.length > 3) ? "$" + i : "");
            JCVariableDecl varDecl = makeVar(varName, typeExpr, valueExpr);
            decls.append(varDecl);
        }
        
        JCExpression result;
        if (i == args.length) {
            result = makeUnquotedIdent(varName);
        } else {
            result = args[i];
        }
        if (statements != null) {
            decls.appendList(statements);
        } 
        return make().LetExpr(decls.toList(), result);
        
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
    
    String tempName() {
        String result = "$ceylontmp" + nextUniqueId();
        return result;
    }

    String tempName(String prefix) {
        String result = "$ceylontmp" + prefix + nextUniqueId();
        return result;
    }

    String aliasName(String name) {
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
    
    /**
     * Determines whether the given type is optional.
     */
    boolean isOptional(ProducedType type) {
        // Note we don't use typeFact().isOptionalType(type) because
        // that implements a stricter test used in the type checker.
        return typeFact().getNullDeclaration().getType().isSubtypeOf(type);
    }
    
    boolean isNothing(ProducedType type) {
        return type.getSupertype(typeFact.getNothingDeclaration()) != null;
    }
    
    public boolean isVoid(ProducedType type) {
        return typeFact.getVoidDeclaration().getType().isExactly(type);
    }

    private boolean isObject(ProducedType type) {
        return typeFact.getObjectDeclaration().getType().isExactly(type);
    }

    ProducedType simplifyType(ProducedType type) {
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
        } else if (tdecl instanceof IntersectionType){
            java.util.List<ProducedType> satisfiedTypes = tdecl.getSatisfiedTypes();
            if(satisfiedTypes.size() == 1) {
                // Special case when the Intersection contains only a single SatisfiedType
                // FIXME This is not correct! We might lose information about type arguments!
                type = satisfiedTypes.get(0);
            }else if(satisfiedTypes.size() == 2){
                // special case for T? simplified as T&Object
                if(isTypeParameter(satisfiedTypes.get(0))
                        && isObject(satisfiedTypes.get(1)))
                    type = satisfiedTypes.get(0);
            }
        }
        
        return type;
    }
    
    TypedDeclaration nonWideningTypeDecl(TypedDeclaration decl) {
        TypedDeclaration refinedDeclaration = (TypedDeclaration) decl.getRefinedDeclaration();
        if(decl != refinedDeclaration){
            /*
             * We are widening if the type:
             * - is not object
             * - is erased to object
             * - refines a declaration that is not erased to object
             */
            boolean isWidening = !sameType(syms().ceylonObjectType, decl.getType())
                    && willEraseToObject(decl.getType())
                    && !willEraseToObject(refinedDeclaration.getType());
            if(isWidening)
                return refinedDeclaration;
        }
        return decl;
    }

    ProducedType nonWideningType(TypedDeclaration declaration, TypedDeclaration refinedDeclaration){
        if(declaration == refinedDeclaration)
            return declaration.getType();
        // we must get the return type of the refined decl with any type param instantiated
        // Note(Stef): this magic taken from the IDE code
        ArrayList<ProducedType> params = new ArrayList<ProducedType>();
        if (refinedDeclaration instanceof Generic) {
            for (TypeParameter tp: ((Generic)refinedDeclaration).getTypeParameters()) {
                params.add(tp.getType());
            }
        }
        ProducedType outerType = declaration.getContainer().getDeclaringType(refinedDeclaration);
        ProducedReference producedReference = refinedDeclaration.getProducedReference(outerType, params);
        ProducedType refinedType = refinedDeclaration.getType();
        if(producedReference != null)
            refinedType = refinedType.substitute(producedReference.getTypeArguments());
        return refinedType;
    }
    
    private ProducedType toPType(com.sun.tools.javac.code.Type t) {
        return loader().getType(t.tsym.getQualifiedName().toString(), null);
    }
    
    private boolean sameType(Type t1, ProducedType t2) {
        return toPType(t1).isExactly(t2);
    }
    
    // Determines if a type will be erased to java.lang.Object once converted to Java
    boolean willEraseToObject(ProducedType type) {
        type = simplifyType(type);
        return (sameType(syms().ceylonVoidType, type) || sameType(syms().ceylonObjectType, type)
                || sameType(syms().ceylonNothingType, type)
                || sameType(syms().ceylonIdentifiableObjectType, type)
                || sameType(syms().ceylonIdentifiableType, type)
                || type.getDeclaration() instanceof BottomType
                || typeFact().isUnion(type)|| typeFact().isIntersection(type));
    }
    
    boolean willEraseToException(ProducedType type) {
        type = simplifyType(type);
        return (sameType(syms().ceylonExceptionType, type));
    }

    boolean isCeylonString(ProducedType type) {
        return (sameType(syms().ceylonStringType, type));
    }
    
    boolean isCeylonBoolean(ProducedType type) {
        return type.isSubtypeOf(typeFact.getBooleanDeclaration().getType())
                && !(type.getDeclaration() instanceof BottomType);
    }
    
    boolean isCeylonInteger(ProducedType type) {
        return (sameType(syms().ceylonIntegerType, type));
    }
    
    boolean isCeylonFloat(ProducedType type) {
        return (sameType(syms().ceylonFloatType, type));
    }
    
    boolean isCeylonCharacter(ProducedType type) {
        return (sameType(syms().ceylonCharacterType, type));
    }

    boolean isCeylonArray(ProducedType type) {
        return (sameType(syms().ceylonArrayType, type.getDeclaration().getType()));
    }
    
    boolean isCeylonBasicType(ProducedType type) {
        return (isCeylonString(type) || isCeylonBoolean(type) || isCeylonInteger(type) || isCeylonFloat(type) || isCeylonCharacter(type));
    }
    
    boolean isCeylonCallable(ProducedType type) {
        return type.isCallable();
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
    static final int COMPANION = 1 << 7;

    /**
     * This function is used solely for method return types and parameters 
     */
    JCExpression makeJavaType(TypedDeclaration typeDecl, ProducedType type) {
        if (typeDecl instanceof FunctionalParameter) {
            FunctionalParameter p = (FunctionalParameter)typeDecl;
            ProducedType pt = type;
            for (int ii = 1; ii < p.getParameterLists().size(); ii++) {
                pt = typeFact().getCallableType(pt);
            }
            return makeJavaType(typeFact().getCallableType(pt), 0);    
        } else {
            boolean usePrimitives = CodegenUtil.isUnBoxed(typeDecl);
            return makeJavaType(type, usePrimitives ? 0 : AbstractTransformer.NO_PRIMITIVES);
        }
    }

    JCExpression makeJavaType(ProducedType producedType) {
        return makeJavaType(producedType, 0);
    }

    JCExpression makeJavaType(ProducedType type, final int flags) {
        if(type == null)
            return make().Erroneous();
        
        // ERASURE
        if (willEraseToObject(type)) {
            // For an erased type:
            // - Any of the Ceylon types Void, Object, Nothing,
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
        } else if ((flags & (SATISFIES | EXTENDS | TYPE_ARGUMENT | CLASS_NEW)) == 0 
                && (!isOptional(type) || isJavaString(type))) {
            if (isCeylonString(type) || isJavaString(type)) {
                return make().Type(syms().stringType);
            } else if (isCeylonBoolean(type)) {
                return make().TypeIdent(TypeTags.BOOLEAN);
            } else if (isCeylonInteger(type)) {
                if ("byte".equals(type.getUnderlyingType())) {
                    return make().TypeIdent(TypeTags.BYTE);
                } else if ("short".equals(type.getUnderlyingType())) {
                    return make().TypeIdent(TypeTags.SHORT);
                } else if ((flags & SMALL_TYPE) != 0 || "int".equals(type.getUnderlyingType())) {
                    return make().TypeIdent(TypeTags.INT);
                } else {
                    return make().TypeIdent(TypeTags.LONG);
                }
            } else if (isCeylonFloat(type)) {
                if ((flags & SMALL_TYPE) != 0 || "float".equals(type.getUnderlyingType())) {
                    return make().TypeIdent(TypeTags.FLOAT);
                } else {
                    return make().TypeIdent(TypeTags.DOUBLE);
                }
            } else if (isCeylonCharacter(type)) {
                if ("char".equals(type.getUnderlyingType())) {
                    return make().TypeIdent(TypeTags.CHAR);
                } else {
                    return make().TypeIdent(TypeTags.INT);
                }
            }
        } else if (isCeylonBoolean(type)
                && !isTypeParameter(type)) {
                //&& (flags & TYPE_ARGUMENT) == 0){
            // special case to get rid of $true and $false types
            type = typeFact.getBooleanDeclaration().getType();
        }
        
        JCExpression jt = makeErroneous();
        
        ProducedType simpleType = simplifyType(type);
        
        java.util.List<ProducedType> qualifyingTypes = new java.util.ArrayList<ProducedType>();
        ProducedType qType = simpleType;
        boolean hasTypeParameters = false;
        while (qType != null) {
            hasTypeParameters |= !qType.getTypeArguments().isEmpty();
            qualifyingTypes.add(qType);
            qType = qType.getQualifyingType();
        }
        int firstQualifyingTypeWithTypeParameters = qualifyingTypes.size() - 1;
        // find the first static one, from the right to the left
        for(ProducedType pt : qualifyingTypes){
            TypeDeclaration declaration = pt.getDeclaration();
            if(Decl.isStatic(declaration)){
                break;
            }
            firstQualifyingTypeWithTypeParameters--;
        }
        if(firstQualifyingTypeWithTypeParameters < 0)
            firstQualifyingTypeWithTypeParameters = 0;
        // put them in outer->inner order
        Collections.reverse(qualifyingTypes);
        
        if (((flags & WANT_RAW_TYPE) == 0) && hasTypeParameters) {
            // special case for interfaces because we pull them into toplevel types
            if(Decl.isCeylon(simpleType.getDeclaration())
                    && qualifyingTypes.size() > 1
                    && simpleType.getDeclaration() instanceof Interface){
                JCExpression baseType = makeErroneous();
                TypeDeclaration tdecl = simpleType.getDeclaration();
                // collect all the qualifying type args we'd normally have
                java.util.List<TypeParameter> qualifyingTypeParameters = new java.util.ArrayList<TypeParameter>();
                java.util.Map<TypeParameter, ProducedType> qualifyingTypeArguments = new java.util.HashMap<TypeParameter, ProducedType>();
                for (ProducedType qualifiedType : qualifyingTypes) {
                    Map<TypeParameter, ProducedType> tas = qualifiedType.getTypeArguments();
                    java.util.List<TypeParameter> tps = qualifiedType.getDeclaration().getTypeParameters();
                    if (tps != null) {
                        qualifyingTypeParameters.addAll(tps);
                        qualifyingTypeArguments.putAll(tas);
                    }
                }
                ListBuffer<JCExpression> typeArgs = makeTypeArgs(isCeylonCallable(simpleType), 
                        flags, 
                        qualifyingTypeArguments, qualifyingTypeParameters);
                if (isCeylonCallable(type) && 
                        (flags & CLASS_NEW) != 0) {
                    baseType = makeIdent(syms().ceylonAbstractCallableType);
                } else {
                    baseType = makeDeclarationName(tdecl);
                }

                if (typeArgs != null && typeArgs.size() > 0) {
                    jt = make().TypeApply(baseType, typeArgs.toList());
                } else {
                    jt = baseType;
                }
            }else{
                JCExpression baseType = makeErroneous();
                int index = 0;
                for (ProducedType qualifyingType : qualifyingTypes) {
                    TypeDeclaration tdecl = qualifyingType.getDeclaration();
                    ListBuffer<JCExpression> typeArgs = null;
                    if(index >= firstQualifyingTypeWithTypeParameters)
                        typeArgs = makeTypeArgs( 
                                qualifyingType, 
                                //tdecl, 
                                flags);
                    if (isCeylonCallable(type) && 
                            (flags & CLASS_NEW) != 0) {
                        baseType = makeIdent(syms().ceylonAbstractCallableType);
                    } else if (index == 0) {
                        String name;
                        // in Ceylon we'd move the nested decl to a companion class
                        // but in Java we just don't have type params to the qualifying type if the
                        // qualified type is static
                        if (tdecl instanceof Interface
                                && qualifyingTypes.size() > 1
                                && firstQualifyingTypeWithTypeParameters == 0) {
                            name = getCompanionClassName(tdecl);
                        } else {
                            if ((flags & COMPANION) != 0) {
                                name = declName(tdecl, QUALIFIED, NameFlag.COMPANION);
                            } else {
                                name = declName(tdecl, QUALIFIED);
                            }
                        }
                        baseType = makeQuotedQualIdentFromString(name);
                    } else {
                        baseType = makeSelect(jt, tdecl.getName());
                    }

                    if (typeArgs != null && typeArgs.size() > 0) {
                        jt = make().TypeApply(baseType, typeArgs.toList());
                    } else {
                        jt = baseType;
                    }

                    index++;
                }
            }
        } else {
            TypeDeclaration tdecl = simpleType.getDeclaration();            
            // For an ordinary class or interface type T:
            // - The Ceylon type T results in the Java type T
            if(tdecl instanceof TypeParameter)
                jt = makeQuotedIdent(tdecl.getName());
            // don't use underlying type if we want no primitives
            else if((flags & (SATISFIES | NO_PRIMITIVES)) != 0 || simpleType.getUnderlyingType() == null)
                if ((flags & COMPANION) != 0) {
                    jt = makeQuotedQualIdentFromString(declName(tdecl, QUALIFIED, NameFlag.COMPANION));
                } else {
                    jt = makeQuotedQualIdentFromString(declName(tdecl, QUALIFIED));
                }
            else
                jt = makeQuotedFQIdent(simpleType.getUnderlyingType());
        }
        
        return jt;
    }

    private ListBuffer<JCExpression> makeTypeArgs(
            ProducedType simpleType, 
            int flags) {
        Map<TypeParameter, ProducedType> tas = simpleType.getTypeArguments();
        java.util.List<TypeParameter> tps = simpleType.getDeclaration().getTypeParameters();
        

        return makeTypeArgs(isCeylonCallable(simpleType), flags, tas, tps);
    }

    private ListBuffer<JCExpression> makeTypeArgs(boolean isCeylonCallable,
            int flags, Map<TypeParameter, ProducedType> tas,
            java.util.List<TypeParameter> tps) {
        ListBuffer<JCExpression> typeArgs = new ListBuffer<JCExpression>();
        int idx = 0;
        for (TypeParameter tp : tps) {
            ProducedType ta = tas.get(tp);
            if (idx > 0 &&
                    isCeylonCallable) {
                // In the runtime Callable only has a single type param
                break;
            }
            if (isOptional(ta)) {
                // For an optional type T?:
                // - The Ceylon type Foo<T?> results in the Java type Foo<T>.
                ta = getNonNullType(ta);
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
            if (isCeylonBoolean(ta)
                    && !isTypeParameter(ta)) {
                ta = typeFact.getBooleanDeclaration().getType();
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
        return typeArgs;
    }

    private ProducedType getNonNullType(ProducedType pt) {
        // typeFact().getDefiniteType() intersects with Object, which isn't 
        // always right for working with the java type system.
        if (typeFact().getVoidDeclaration().equals(pt.getDeclaration())) {
            pt = typeFact().getObjectDeclaration().getType();
        }
        else {
            pt = pt.minus(typeFact().getNothingDeclaration());
        }
        return pt;
    }
    
    private boolean isJavaString(ProducedType type) {
        return "java.lang.String".equals(type.getUnderlyingType());
    }

    private java.util.Map<Scope, Integer> locals;
    private java.util.Set<Interface> interfaces;
    
    private void resetLocals() {
        ((AbstractTransformer)gen()).locals = new java.util.HashMap<Scope, Integer>();
    }
    
    private void local(Scope decl) {
        Map<Scope, Integer> locals = ((AbstractTransformer)gen()).locals;
        locals.put(decl, locals.size());
    }
    
    boolean hasInterface(Interface iface) {
        return ((AbstractTransformer)gen()).interfaces != null 
                && ((AbstractTransformer)gen()).interfaces.contains(iface);
    }
    
    void noteDecl(Declaration decl) {
        if (decl.isToplevel()) {
            resetLocals();
        } else if (Decl.isLocal(decl)){
            local(decl.getContainer());
        }
        if (decl instanceof Interface) {
            if (((AbstractTransformer)gen()).interfaces == null) {
                ((AbstractTransformer)gen()).interfaces = new HashSet<Interface>();
            }
            ((AbstractTransformer)gen()).interfaces.add((Interface)decl);
        }
    }
    
    @Override
    public String getLocalId(Scope decl) {
        Integer id = ((AbstractTransformer)gen()).locals.get(decl);
        if (id == null) {
            throw new RuntimeException(decl + " has no local id");
        }
        return String.valueOf(id);
    }
    
    private ClassDefinitionBuilder ccdb;
    
    ClassDefinitionBuilder current() {
        return ((AbstractTransformer)gen()).ccdb; 
    }
    
    ClassDefinitionBuilder replace(ClassDefinitionBuilder ccdb) {
        ClassDefinitionBuilder result = ((AbstractTransformer)gen()).ccdb;
        ((AbstractTransformer)gen()).ccdb = ccdb;
        return result;
    }
    
    String getFQDeclarationName(final Declaration decl) {
        return declName(decl, QUALIFIED);
    }
    
    String declName(final Declaration decl, CodegenUtil.NameFlag... flags) {
        return CodegenUtil.declName(this, decl, flags);
    }
    
    private JCExpression makeDeclarationName(Declaration decl) {
        return makeQuotedQualIdentFromString(getFQDeclarationName(decl));
    }
    
    String getCompanionClassName(Declaration decl){
        return declName(decl, QUALIFIED, NameFlag.COMPANION);
    }
    
    /**
     * Gets the first type parameter from the type model representing a 
     * {@code ceylon.language.Callable<Result, ParameterTypes...>}.
     * @param typeModel A {@code ceylon.language.Callable<Result, ParameterTypes...>}.
     * @return The result type of the {@code Callable}.
     */
    ProducedType getReturnTypeOfCallable(ProducedType typeModel) {
        if (!isCeylonCallable(typeModel)) {
            throw new RuntimeException("Not a Callable<>: " + typeModel);
        }
        return typeModel.getTypeArgumentList().get(0);
    }
    
    /**
     * <p>Gets the type of the given functional
     * (ignoring parameter types) according to 
     * the functionals parameter lists. The result is always a 
     * {@code Callable} of some kind (because Functionals always have 
     * at least one parameter list).</p> 
     * 
     * <p>For example:</p>
     * <table>
     * <tbody>
     * <tr><th>functional</th><th>functionalType(functional)</th></tr>
     * <tr><td><code>String m()</code></td><td><code>Callable&lt;String&gt;</code></td></tr>
     * <tr><td><code>String m()()</code></td><td><code>Callable&lt;Callable&lt;String&gt;&gt;</code></td></tr>
     * </tbody>
     * </table>
     */
    ProducedType functionalType(Functional model) {
        return typeFact().getCallableType(functionalReturnType(model));
    }
    
    /**
     * <p>Gets the return type of the given functional (ignoring parameter 
     * types) according to the functionals parameter lists. If the functional 
     * has multiple parameter lists the return type will be a 
     * {@code Callable}.</p>
     * 
     * <p>For example:</p>
     * <table>
     * <tbody>
     * <tr><th>functional</th><th>functionalReturnType(functional)</th></tr>
     * <tr><td><code>String m()</code></td><td><code>String</code></td></tr>
     * <tr><td><code>String m()()</code></td><td><code>Callable&lt;String&gt;</code></td></tr>
     * </tbody>
     * </table>
     */
    ProducedType functionalReturnType(Functional model) {
        ProducedType callableType = model.getType();
        for (int ii = 1; ii < model.getParameterLists().size(); ii++) {
            callableType = typeFact().getCallableType(callableType);
        }
        return callableType;
    }
    
    /** 
     * Return the upper bound of any type parameter, instead of the type 
     * parameter itself 
     */
    static final int TP_TO_BOUND = 1<<0;
    /** 
     * Return the type of the sequenced parameter (T[]) rather than its element type (T) 
     */
    static final int TP_SEQUENCED_TYPE = 1<<1;
    
    ProducedType getTypeForParameter(Parameter parameter, ProducedReference producedReference, int flags) {
        if (parameter instanceof FunctionalParameter) {
            return getTypeForFunctionalParameter((FunctionalParameter)parameter);
        }
        if (producedReference == null) {
            return parameter.getType();
        }
        final ProducedTypedReference producedTypedReference = producedReference.getTypedParameter(parameter);
        final ProducedType type = producedTypedReference.getType();
        final TypedDeclaration producedParameterDecl = producedTypedReference.getDeclaration();
        final ProducedType declType = producedParameterDecl.getType();
        final TypeDeclaration declTypeDecl = declType.getDeclaration();
        if(isJavaVariadic(parameter) && (flags & TP_SEQUENCED_TYPE) == 0){
            // type of param must be T[]
            ProducedType elementType = typeFact.getElementType(type);
            if(elementType == null){
                log.error("ceylon", "Invalid type for Java variadic parameter: "+type.getProducedTypeQualifiedName());
                return type;
            }
            return elementType;
        }
        if (type.getDeclaration() instanceof ClassOrInterface) {
            // Explicit type parameter
            return producedTypedReference.getType();
        } else if (declTypeDecl instanceof ClassOrInterface) {
            return declType;
        } else if ((declTypeDecl instanceof TypeParameter)
                && (flags & TP_TO_BOUND) != 0) {
            if (!declTypeDecl.getSatisfiedTypes().isEmpty()) {
                // use upper bound
                return declTypeDecl.getSatisfiedTypes().get(0);
            }
         }
        return type;
    }

    private boolean isJavaVariadic(Parameter parameter) {
        return parameter.isSequenced()
                && parameter.getContainer() instanceof Method
                && isJavaMethod((Method) parameter.getContainer());
    }

    boolean isJavaMethod(Method method) {
        ClassOrInterface container = Decl.getClassOrInterfaceContainer(method);
        return container != null && !Decl.isCeylon(container);
    }

    private ProducedType getTypeForFunctionalParameter(FunctionalParameter fp) {
        java.util.List<ProducedType> typeArgs = new ArrayList<ProducedType>(fp.getTypeParameters().size());
        typeArgs.add(fp.getType());
        for (Parameter parameter : fp.getParameterLists().get(0).getParameters()) {
            typeArgs.add(parameter.getType());
        }
        return typeFact().getCallableType(typeArgs);
    }
    
    /*
     * Annotation generation
     */
    
    List<JCAnnotation> makeAtOverride() {
        return List.<JCAnnotation> of(make().Annotation(makeIdent(syms().overrideType), List.<JCExpression> nil()));
    }

    boolean checkCompilerAnnotations(Tree.Declaration decl){
        boolean old = gen().disableModelAnnotations;
        if(CodegenUtil.hasCompilerAnnotation(decl, "nomodel"))
            gen().disableModelAnnotations  = true;
        return old;
    }

    void resetCompilerAnnotations(boolean value){
        gen().disableModelAnnotations = value;
    }

    private List<JCAnnotation> makeModelAnnotation(Type annotationType, List<JCExpression> annotationArgs) {
        if (gen().disableModelAnnotations)
            return List.nil();
        return List.of(make().Annotation(makeIdent(annotationType), annotationArgs));
    }

    private List<JCAnnotation> makeModelAnnotation(Type annotationType) {
        return makeModelAnnotation(annotationType, List.<JCExpression>nil());
    }

    List<JCAnnotation> makeAtCeylon() {
        return makeModelAnnotation(syms().ceylonAtCeylonType);
    }

    List<JCAnnotation> makeAtModule(Module module) {
        String name = module.getNameAsString();
        String version = module.getVersion();
        java.util.List<ModuleImport> dependencies = module.getImports();
        ListBuffer<JCExpression> imports = new ListBuffer<JCTree.JCExpression>();
        for(ModuleImport dependency : dependencies){
            Module dependencyModule = dependency.getModule();
            // do not include the implicit java module as a dependency
            if(dependencyModule.getNameAsString().equals("java")
                    // nor ceylon.language
                    || dependencyModule.getNameAsString().equals("ceylon.language"))
                continue;
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

    List<JCAnnotation> makeAtPackage(Package pkg) {
        String name = pkg.getNameAsString();
        boolean shared = pkg.isShared();
        JCExpression nameAttribute = make().Assign(makeUnquotedIdent("name"), make().Literal(name));
        JCExpression sharedAttribute = make().Assign(makeUnquotedIdent("shared"), makeBoolean(shared));
        return makeModelAnnotation(syms().ceylonAtPackageType, 
                List.<JCExpression>of(nameAttribute, sharedAttribute));
    }

    List<JCAnnotation> makeAtName(String name) {
        return makeModelAnnotation(syms().ceylonAtNameType, List.<JCExpression>of(make().Literal(name)));
    }

    List<JCAnnotation> makeAtType(String name) {
        return makeModelAnnotation(syms().ceylonAtTypeInfoType, List.<JCExpression>of(make().Literal(name)));
    }

    final JCAnnotation makeAtTypeParameter(String name, java.util.List<ProducedType> satisfiedTypes, boolean covariant, boolean contravariant) {
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

    List<JCAnnotation> makeAtTypeParameters(List<JCExpression> typeParameters) {
        JCExpression value = make().NewArray(null, null, typeParameters);
        return makeModelAnnotation(syms().ceylonAtTypeParameters, List.of(value));
    }

    List<JCAnnotation> makeAtSequenced() {
        return makeModelAnnotation(syms().ceylonAtSequencedType);
    }

    List<JCAnnotation> makeAtDefaulted() {
        return makeModelAnnotation(syms().ceylonAtDefaultedType);
    }

    List<JCAnnotation> makeAtAttribute() {
        return makeModelAnnotation(syms().ceylonAtAttributeType);
    }

    List<JCAnnotation> makeAtMethod() {
        return makeModelAnnotation(syms().ceylonAtMethodType);
    }

    List<JCAnnotation> makeAtObject() {
        return makeModelAnnotation(syms().ceylonAtObjectType);
    }

    List<JCAnnotation> makeAtClass(ProducedType extendedType) {
        List<JCExpression> attributes = List.nil();
        if(!extendedType.isExactly(typeFact.getIdentifiableObjectDeclaration().getType())){
            JCExpression extendsAttribute = make().Assign(makeUnquotedIdent("extendsType"), 
                    make().Literal(serialiseTypeSignature(extendedType)));
            attributes = attributes.prepend(extendsAttribute);
        }
        return makeModelAnnotation(syms().ceylonAtClassType, attributes);
    }

    List<JCAnnotation> makeAtSatisfiedTypes(java.util.List<ProducedType> satisfiedTypes) {
        return makeTypesListAnnotation(syms().ceylonAtSatisfiedTypes, satisfiedTypes);
    }

    List<JCAnnotation> makeAtCaseTypes(java.util.List<ProducedType> caseTypes) {
        return makeTypesListAnnotation(syms().ceylonAtCaseTypes, caseTypes);
    }

    private List<JCAnnotation> makeTypesListAnnotation(Type annotationType, java.util.List<ProducedType> types) {
        if(types.isEmpty())
            return List.nil();
        ListBuffer<JCExpression> upperBounds = new ListBuffer<JCTree.JCExpression>();
        for(ProducedType type : types){
            String typeSig = serialiseTypeSignature(type);
            upperBounds.append(make().Literal(typeSig));
        }
        JCExpression caseAttribute = make().Assign(makeUnquotedIdent("value"), 
                make().NewArray(null, null, upperBounds.toList()));
        
        return makeModelAnnotation(annotationType, List.of(caseAttribute));
    }

    List<JCAnnotation> makeAtCaseTypes(ProducedType ofType) {
        if(ofType == null)
            return List.nil();
        String typeSig = serialiseTypeSignature(ofType);
        JCExpression caseAttribute = make().Assign(makeUnquotedIdent("of"), 
                make().Literal(typeSig));
        
        return makeModelAnnotation(syms().ceylonAtCaseTypes, List.of(caseAttribute));
    }

    List<JCAnnotation> makeAtIgnore() {
        return makeModelAnnotation(syms().ceylonAtIgnore);
    }

    List<JCAnnotation> makeAtAnnotations(java.util.List<Annotation> annotations) {
        if(annotations == null || annotations.isEmpty())
            return List.nil();
        ListBuffer<JCExpression> array = new ListBuffer<JCTree.JCExpression>();
        for(Annotation annotation : annotations){
            array.append(makeAtAnnotation(annotation));
        }
        JCExpression annotationsAttribute = make().Assign(makeUnquotedIdent("value"), 
                make().NewArray(null, null, array.toList()));
        
        return makeModelAnnotation(syms().ceylonAtAnnotationsType, List.of(annotationsAttribute));
    }

    private JCExpression makeAtAnnotation(Annotation annotation) {
        JCExpression valueAttribute = make().Assign(makeUnquotedIdent("value"), 
                                                    make().Literal(annotation.getName()));
        List<JCExpression> attributes;
        if(!annotation.getPositionalArguments().isEmpty()){
            java.util.List<String> positionalArguments = annotation.getPositionalArguments();
            ListBuffer<JCExpression> array = new ListBuffer<JCTree.JCExpression>();
            for(String val : positionalArguments)
                array.add(make().Literal(val));
            JCExpression argumentsAttribute = make().Assign(makeUnquotedIdent("arguments"), 
                                                            make().NewArray(null, null, array.toList()));
            attributes = List.of(valueAttribute, argumentsAttribute);
        }else if(!annotation.getNamedArguments().isEmpty()){
            Map<String, String> namedArguments = annotation.getNamedArguments();
            ListBuffer<JCExpression> array = new ListBuffer<JCTree.JCExpression>();
            for(Entry<String, String> entry : namedArguments.entrySet()){
                JCExpression argNameAttribute = make().Assign(makeUnquotedIdent("name"), 
                        make().Literal(entry.getKey()));
                JCExpression argValueAttribute = make().Assign(makeUnquotedIdent("value"), 
                        make().Literal(entry.getValue()));

                JCAnnotation namedArg = make().Annotation(makeIdent(syms().ceylonAtNamedArgumentType), 
                                                          List.of(argNameAttribute, argValueAttribute));
                array.add(namedArg);
            }
            JCExpression argumentsAttribute = make().Assign(makeUnquotedIdent("namedArguments"), 
                    make().NewArray(null, null, array.toList()));
            attributes = List.of(valueAttribute, argumentsAttribute);
        }else
            attributes = List.of(valueAttribute);

        return make().Annotation(makeIdent(syms().ceylonAtAnnotationType), attributes);
    }

    boolean needsAnnotations(Declaration decl) {
        Declaration reqdecl = decl;
        if (reqdecl instanceof Parameter) {
            Parameter p = (Parameter)reqdecl;
            reqdecl = p.getDeclaration();
        }
        return reqdecl.isToplevel() || (reqdecl.isClassOrInterfaceMember() && reqdecl.isShared() && !Decl.isAncestorLocal(reqdecl));
    }

    List<JCTree.JCAnnotation> makeJavaTypeAnnotations(TypedDeclaration decl) {
        if(decl == null || decl.getType() == null)
            return List.nil();
        ProducedType type;
        if (decl instanceof FunctionalParameter) {
            type = getTypeForFunctionalParameter((FunctionalParameter)decl);
        } else {
            type = decl.getType();
        }
        return makeJavaTypeAnnotations(type, needsAnnotations(decl));
    }

    private List<JCTree.JCAnnotation> makeJavaTypeAnnotations(ProducedType type, boolean required) {
        if (!required)
            return List.nil();
        // Add the original type to the annotations
        return makeAtType(serialiseTypeSignature(type));
    }
    
    private String serialiseTypeSignature(ProducedType type){
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

    JCExpression boxUnboxIfNecessary(JCExpression javaExpr, Tree.Term expr,
            ProducedType exprType,
            BoxingStrategy boxingStrategy) {
        boolean exprBoxed = !CodegenUtil.isUnBoxed(expr);
        return boxUnboxIfNecessary(javaExpr, exprBoxed, exprType, boxingStrategy);
    }
    
    JCExpression boxUnboxIfNecessary(JCExpression javaExpr, boolean exprBoxed,
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
    
    boolean isTypeParameter(ProducedType type) {
        if (typeFact().isOptionalType(type)) {
            type = type.minus(typeFact().getNothingDeclaration());
        } 
        return type.getDeclaration() instanceof TypeParameter;
    }
    
    JCExpression unboxType(JCExpression expr, ProducedType targetType) {
        if (isCeylonInteger(targetType)) {
            expr = unboxInteger(expr);
        } else if (isCeylonFloat(targetType)) {
            expr = unboxFloat(expr);
        } else if (isCeylonString(targetType)) {
            expr = unboxString(expr);
        } else if (isCeylonCharacter(targetType)) {
            boolean isJavaCharacter = targetType.getUnderlyingType() != null;
            expr = unboxCharacter(expr, isJavaCharacter);
        } else if (isCeylonBoolean(targetType)) {
            expr = unboxBoolean(expr);
        } else if (isCeylonArray(targetType)) {
            expr = unboxArray(expr);
        } else if (isOptional(targetType)) {
            targetType = typeFact().getDefiniteType(targetType);
            if (isCeylonString(targetType)){
                expr = unboxOptionalString(expr);
            }
        }
        return expr;
    }

    JCExpression boxType(JCExpression expr, ProducedType exprType) {
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
        } else if (isVoid(exprType)) {
            expr = make().LetExpr(List.<JCStatement>of(make().Exec(expr)), makeNull());
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
        return makeBoxType(value, syms().ceylonArrayType);
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
    
    private JCExpression unboxString(JCExpression value) {
        if (isStringLiteral(value)) {
            // If it's already a String literal, why call .toString on it?
            return value;
        }
        return makeUnboxType(value, "toString");
    }

    private boolean isStringLiteral(JCExpression value) {
        return value instanceof JCLiteral
                && ((JCLiteral)value).value instanceof String;
    }
    
    private JCExpression unboxOptionalString(JCExpression value){
        if (isStringLiteral(value)) {
            // If it's already a String literal, why call .toString on it?
            return value;
        }
        String name = tempName();
        JCExpression type = makeJavaType(typeFact().getStringDeclaration().getType(), NO_PRIMITIVES);
        JCExpression expr = make().Conditional(make().Binary(JCTree.NE, makeUnquotedIdent(name), makeNull()), 
                unboxString(makeUnquotedIdent(name)),
                makeNull());
        return makeLetExpr(name, null, type, value, expr);
    }
    
    private JCTree.JCMethodInvocation unboxCharacter(JCExpression value, boolean isJava) {
        return makeUnboxType(value, isJava ? "charValue" : "intValue");
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

    /*
     * Sequences
     */
    
    /**
     * Returns a JCExpression along the lines of 
     * {@code new ArraySequence<seqElemType>(list...)}
     * @param elems The elements in the sequence
     * @param seqElemType The sequence type parameter
     * @param makeJavaTypeOpts The option flags to pass to makeJavaType().
     * @return a JCExpression
     * @see #makeSequenceRaw(java.util.List)
     */
    private JCExpression makeSequence(List<JCExpression> elems, ProducedType seqElemType, int makeJavaTypeOpts) {
        ProducedType seqType = typeFact().getDefaultSequenceType(seqElemType);
        JCExpression typeExpr = makeJavaType(seqType, makeJavaTypeOpts);
        return makeNewClass(typeExpr, elems);
    }
    
    /**
     * Returns a JCExpression along the lines of 
     * {@code new ArraySequence<seqElemType>(list...)}
     * @param list The elements in the sequence
     * @param seqElemType The sequence type parameter
     * @return a JCExpression
     * @see #makeSequenceRaw(java.util.List)
     */
    JCExpression makeSequence(java.util.List<Expression> list, ProducedType seqElemType) {
        ListBuffer<JCExpression> elems = new ListBuffer<JCExpression>();
        for (Expression expr : list) {
            // no need for erasure casts here
            elems.append(expressionGen().transformExpression(expr));
        }
        return makeSequence(elems.toList(), seqElemType,CeylonTransformer.TYPE_ARGUMENT);
    }
    
    /**
     * Returns a JCExpression along the lines of 
     * {@code new ArraySequence(list...)}
     * @param list The elements in the sequence
     * @return a JCExpression
     * @see #makeSequence(java.util.List, ProducedType)
     */
    JCExpression makeSequenceRaw(java.util.List<Expression> list) {
        ListBuffer<JCExpression> elems = new ListBuffer<JCExpression>();
        for (Expression expr : list) {
            // no need for erasure casts here
            elems.append(expressionGen().transformExpression(expr));
        }
        return makeSequenceRaw(elems.toList());
    }
    
    JCExpression makeSequenceRaw(List<JCExpression> elems) {
        return makeSequence(elems, typeFact().getObjectDeclaration().getType(), CeylonTransformer.WANT_RAW_TYPE);
    }
    
    JCExpression makeEmpty() {
        return make().Apply(
                List.<JCTree.JCExpression>nil(),
                makeFQIdent("ceylon", "language", "$empty", Util.getGetterName("$empty")),
                List.<JCTree.JCExpression>nil());
    }
    
    JCExpression makeFinished() {
        return make().Apply(
                List.<JCTree.JCExpression>nil(),
                makeFQIdent("ceylon", "language", "$finished", Util.getGetterName("$finished")),
                List.<JCTree.JCExpression>nil());
    }

    /**
     * Turns a sequence into a Java array
     * @param expr the sequence
     * @param sequenceType the sequence type
     * @param boxingStrategy the boxing strategy for expr
     */
    JCExpression sequenceToJavaArray(JCExpression expr, ProducedType sequenceType, BoxingStrategy boxingStrategy) {
        String methodName = null;
        // find the sequence element type
        ProducedType type = typeFact().getElementType(sequenceType);
        if(boxingStrategy == BoxingStrategy.UNBOXED){
            if(isCeylonInteger(type)){
                if("byte".equals(type.getUnderlyingType()))
                    methodName = "toByteArray";
                else if("short".equals(type.getUnderlyingType()))
                    methodName = "toShortArray";
                else if("int".equals(type.getUnderlyingType()))
                    methodName = "toIntArray";
                else
                    methodName = "toLongArray";
            }else if(isCeylonFloat(type)){
                if("float".equals(type.getUnderlyingType()))
                    methodName = "toFloatArray";
                else
                    methodName = "toDoubleArray";
            } else if (isCeylonCharacter(type)) {
                if ("char".equals(type.getUnderlyingType()))
                    methodName = "toCharArray";
                // else it must be boxed, right?
            } else if (isCeylonBoolean(type)) {
                if ("boolean".equals(type.getUnderlyingType()))
                    methodName = "toBooleanArray";
                // else it must be boxed, right?
            } else if (isJavaString(type)) {
                methodName = "toJavaStringArray";
            } else if (isCeylonString(type)) {
                return objectSequenceToJavaArray(type, sequenceType, expr);
            }
            if(methodName == null){
                log.error("ceylon", "Don't know how to convert sequences of type "+type+" to Java array (This is a compiler bug)");
                return expr;
            }
            // since T[] is erased to Iterable<T> we probably need a cast to FixedSized<T>
            JCExpression seqTypeExpr = makeJavaType(typeFact().getFixedSizedType(sequenceType));
            expr = make().TypeCast(seqTypeExpr, expr);
            return makeUtilInvocation(methodName, List.of(expr), null);
        }else{
            return objectSequenceToJavaArray(type, sequenceType, expr);
        }
    }

    private JCExpression objectSequenceToJavaArray(ProducedType type,
            ProducedType sequenceType, JCExpression expr) {
        JCExpression klass1 = makeJavaType(type, AbstractTransformer.CLASS_NEW | AbstractTransformer.NO_PRIMITIVES);
        JCExpression klass2 = makeJavaType(type, AbstractTransformer.CLASS_NEW | AbstractTransformer.NO_PRIMITIVES);
        String baseName = tempName();

        String seqName = baseName +"$0";
        JCExpression seqTypeExpr1 = makeJavaType(typeFact().getFixedSizedType(sequenceType));
        JCExpression seqTypeExpr2 = makeJavaType(typeFact().getFixedSizedType(sequenceType));

        JCExpression sizeExpr = make().Apply(List.<JCExpression>nil(), 
                make().Select(makeUnquotedIdent(seqName), names().fromString("getSize")),
                List.<JCExpression>nil());
        sizeExpr = make().TypeCast(syms().intType, sizeExpr);

        JCExpression newArrayExpr = make().NewArray(klass1, List.of(sizeExpr), null);
        JCExpression sequenceToArrayExpr = makeUtilInvocation("toArray", 
                List.of(makeUnquotedIdent(seqName), 
                        newArrayExpr),
                List.of(klass2));
        
        // since T[] is erased to Iterable<T> we probably need a cast to FixedSized<T>
        JCExpression castedExpr = make().TypeCast(seqTypeExpr2, expr);
        
        return makeLetExpr(seqName, List.<JCStatement>nil(), seqTypeExpr1, castedExpr, sequenceToArrayExpr);
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
    JCExpression makeTypeTest(JCExpression firstTimeExpr, String varName, ProducedType type) {
        JCExpression result = null;
        if (typeFact().isUnion(type)) {
            UnionType union = (UnionType)type.getDeclaration();
            for (ProducedType pt : union.getCaseTypes()) {
                JCExpression partExpr = makeTypeTest(firstTimeExpr, varName, pt);
                firstTimeExpr = null;
                if (result == null) {
                    result = partExpr;
                } else {
                    result = make().Binary(JCTree.OR, result, partExpr);
                }
            }
        } else if (typeFact().isIntersection(type)) {
            IntersectionType union = (IntersectionType)type.getDeclaration();
            for (ProducedType pt : union.getSatisfiedTypes()) {
                JCExpression partExpr = makeTypeTest(firstTimeExpr, varName, pt);
                firstTimeExpr = null;
                if (result == null) {
                    result = partExpr;
                } else {
                    result = make().Binary(JCTree.AND, result, partExpr);
                }
            }
        } else {
            JCExpression varExpr = firstTimeExpr != null ? firstTimeExpr : makeUnquotedIdent(varName);
            if (type.isExactly(typeFact().getNothingDeclaration().getType())){
                // is Nothing => is null
                return make().Binary(JCTree.EQ, varExpr, makeNull());
            } else if (type.isExactly(typeFact().getObjectDeclaration().getType())){
                // is Object => is not null
                return make().Binary(JCTree.NE, varExpr, makeNull());
            } else if (isVoid(type)){
                // everything is Void, it's the root of the hierarchy
                return makeIgnoredEvalAndReturn(varExpr, makeBoolean(true));
            } else if (type.isExactly(typeFact().getIdentifiableObjectDeclaration().getType())){
                // it's erased
                return makeUtilInvocation("isIdentifiable", List.of(varExpr), null);
            } else if (type.getDeclaration() instanceof BottomType){
                // nothing is Bottom
                return makeIgnoredEvalAndReturn(varExpr, makeBoolean(false));
            } else {
                JCExpression rawTypeExpr = makeJavaType(type, NO_PRIMITIVES | WANT_RAW_TYPE);
                result = make().TypeTest(varExpr, rawTypeExpr);
            }
        }
        return result;
    }

    JCExpression makeNonEmptyTest(JCExpression firstTimeExpr, String varName) {
        Interface fixedSize = typeFact().getFixedSizedDeclaration();
        JCExpression test = makeTypeTest(firstTimeExpr, varName, fixedSize.getType());
        JCExpression fixedSizeType = makeJavaType(fixedSize.getType(), NO_PRIMITIVES | WANT_RAW_TYPE);
        JCExpression nonEmpty = makeNonEmptyTest(make().TypeCast(fixedSizeType, makeUnquotedIdent(varName)));
        return make().Binary(JCTree.AND, test, nonEmpty);
    }

    JCExpression makeNonEmptyTest(JCExpression expr){
        JCExpression getEmptyCall = make().Select(expr, names().fromString("getEmpty"));
        JCExpression empty = make().Apply(List.<JCExpression>nil(), getEmptyCall, List.<JCExpression>nil());
        return make().Unary(JCTree.NOT, empty);
    }
    
    /**
     * Invokes a static method of the Util helper class
     * @param methodName name of the method
     * @param params parameters
     * @return the invocation AST
     */
    public JCExpression makeUtilInvocation(String methodName, List<JCExpression> params, List<JCExpression> typeParams) {
        return make().Apply(typeParams, 
                            make().Select(make().QualIdent(syms().ceylonUtilType.tsym), 
                                          names().fromString(methodName)), 
                            params);
    }

    private LetExpr makeIgnoredEvalAndReturn(JCExpression toEval, JCExpression toReturn){
        // define a variable of type j.l.Object to hold the result of the evaluation
        JCVariableDecl def = makeVar(tempName(), make().Type(syms().objectType), toEval);
        // then ignore this result and return something else
        return make().LetExpr(def, toReturn);

    }
    
    JCExpression makeErroneous() {
        return makeErroneous(null);
    }
    
    /**
     * Makes an 'erroneous' AST node with no message
     */
    JCExpression makeErroneous(Node node) {
        return makeErroneous(node, null, List.<JCTree>nil());
    }
    
    /**
     * Makes an 'erroneous' AST node with a message to be logged as an error
     */
    JCExpression makeErroneous(Node node, String message) {
        return makeErroneous(node, message, List.<JCTree>nil());
    }
    
    /**
     * Makes an 'erroneous' AST node with a message to be logged as an error
     */
    JCExpression makeErroneous(Node node, String message, List<? extends JCTree> errs) {
        if (node != null) {
            at(node);
        }
        if (message != null) {
            if (node != null) {
                log.error(getPosition(node), "ceylon", message);
            } else {
                log.error("ceylon", message);
            }
        }
        return make().Erroneous(errs);
    }

    private JCTypeParameter makeTypeParameter(String name, java.util.List<ProducedType> satisfiedTypes, boolean covariant, boolean contravariant) {
        ListBuffer<JCExpression> bounds = new ListBuffer<JCExpression>();
        for (ProducedType t : satisfiedTypes) {
            if (!willEraseToObject(t)) {
                bounds.append(makeJavaType(t, AbstractTransformer.NO_PRIMITIVES));
            }
        }
        return make().TypeParameter(names().fromString(name), bounds.toList());
    }

    JCTypeParameter makeTypeParameter(TypeParameter declarationModel) {
        return makeTypeParameter(declarationModel.getName(), 
                declarationModel.getSatisfiedTypes(),
                declarationModel.isCovariant(),
                declarationModel.isContravariant());
    }
    
    JCTypeParameter makeTypeParameter(Tree.TypeParameterDeclaration param) {
        at(param);
        return makeTypeParameter(param.getDeclarationModel());
    }

    JCAnnotation makeAtTypeParameter(TypeParameter declarationModel) {
        return makeAtTypeParameter(declarationModel.getName(), 
                declarationModel.getSatisfiedTypes(),
                declarationModel.isCovariant(),
                declarationModel.isContravariant());
    }
    
    JCAnnotation makeAtTypeParameter(Tree.TypeParameterDeclaration param) {
        at(param);
        return makeAtTypeParameter(param.getDeclarationModel());
    }
    
    final List<JCExpression> typeArguments(Tree.AnyMethod method) {
        return typeArguments(method.getDeclarationModel());
    }
    
    final List<JCExpression> typeArguments(Tree.AnyClass type) {
        return typeArguments(type);
    }
    
    final List<JCExpression> typeArguments(Functional method) {
        return typeArguments(method.getTypeParameters(), method.getType().getTypeArguments());
    }
    
    final List<JCExpression> typeArguments(Tree.ClassOrInterface type) {
        return typeArguments(type.getDeclarationModel().getTypeParameters(), type.getDeclarationModel().getType().getTypeArguments());
    }
    
    final List<JCExpression> typeArguments(java.util.List<TypeParameter> typeParameters, Map<TypeParameter, ProducedType> typeArguments) {
        ListBuffer<JCExpression> typeArgs = ListBuffer.<JCExpression>lb();
        for (TypeParameter tp : typeParameters) {
            ProducedType type = typeArguments.get(tp);
            if (type != null) {
                typeArgs.append(makeJavaType(type, TYPE_ARGUMENT));
            } else {
                typeArgs.append(makeJavaType(tp.getType(), TYPE_ARGUMENT));
            }
        }
        return typeArgs.toList();
    }
    
    final String getCompanionFieldName(Interface def) {
        return "$" + CodegenUtil.getCompanionClassName(def.getName());
    }
    
    final JCExpression makeDefaultedParamMethodIdent(Method method, Parameter param) {
        Interface iface = (Interface)method.getRefinedDeclaration().getContainer();
        return makeQuotedQualIdent(makeQuotedIdent(getCompanionFieldName(iface)), 
                CodegenUtil.getDefaultedParamMethodName(method, param));
    }
    
    private int getPosition(Node node) {
        int pos = getMap().getStartPosition(node.getToken().getLine())
                + node.getToken().getCharPositionInLine();
                log.useSource(gen().getFileObject());
        return pos;
    }

    /**
     * Returns a copy of the given annotations, unless {@code @Ignore} is present, 
     * in which case returns a singleton containing {@code @Ignore}. 
     */
    List<JCAnnotation> filterAnnotations(
            ListBuffer<JCAnnotation> annotations) {
        ListBuffer<JCAnnotation> lb = ListBuffer.<JCAnnotation>lb();
        for (JCAnnotation anno : annotations) {
            JCTree type = anno.getAnnotationType();
            if (type instanceof JCFieldAccess
                    && ((JCFieldAccess)type).sym != null
                    && ((JCFieldAccess)type).sym.type == syms().ceylonAtIgnore) {
                lb.clear();
                lb.add(anno);
                break;
            }
            lb.add(anno);
        }
        return lb.toList();
    }
}
