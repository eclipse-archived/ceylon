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

import static com.redhat.ceylon.compiler.typechecker.model.Util.producedType;
import static com.sun.tools.javac.code.Flags.FINAL;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.antlr.runtime.Token;

import com.redhat.ceylon.ceylondoc.Util;
import com.redhat.ceylon.common.Versions;
import com.redhat.ceylon.compiler.java.codegen.Naming.DeclNameFlag;
import com.redhat.ceylon.compiler.java.codegen.Naming.SyntheticName;
import com.redhat.ceylon.compiler.java.loader.CeylonModelLoader;
import com.redhat.ceylon.compiler.java.loader.TypeFactory;
import com.redhat.ceylon.compiler.java.tools.CeylonLog;
import com.redhat.ceylon.compiler.loader.AbstractModelLoader;
import com.redhat.ceylon.compiler.typechecker.model.Annotation;
import com.redhat.ceylon.compiler.typechecker.model.Class;
import com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Functional;
import com.redhat.ceylon.compiler.typechecker.model.Interface;
import com.redhat.ceylon.compiler.typechecker.model.IntersectionType;
import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.MethodOrValue;
import com.redhat.ceylon.compiler.typechecker.model.Module;
import com.redhat.ceylon.compiler.typechecker.model.ModuleImport;
import com.redhat.ceylon.compiler.typechecker.model.NothingType;
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
import com.redhat.ceylon.compiler.typechecker.model.UnknownType;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Comprehension;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Expression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.PositionalArgument;
import com.sun.tools.javac.code.BoundKind;
import com.sun.tools.javac.code.Symbol.TypeSymbol;
import com.sun.tools.javac.code.Symtab;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.code.TypeTags;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.Factory;
import com.sun.tools.javac.tree.JCTree.JCAnnotation;
import com.sun.tools.javac.tree.JCTree.JCBinary;
import com.sun.tools.javac.tree.JCTree.JCBlock;
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
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.util.Names;
import com.sun.tools.javac.util.Position;
import com.sun.tools.javac.util.Position.LineMap;

/**
 * Base class for all delegating transformers
 */
public abstract class AbstractTransformer implements Transformation {
    
    private Context context;
    private TreeMaker make;
    private Names names;
    private Symtab syms;
    private AbstractModelLoader loader;
    private TypeFactory typeFact;
    protected Log log;
    final Naming naming;

    public AbstractTransformer(Context context) {
        this.context = context;
        make = TreeMaker.instance(context);
        names = Names.instance(context);
        syms = Symtab.instance(context);
        loader = CeylonModelLoader.instance(context);
        typeFact = TypeFactory.instance(context);
        log = CeylonLog.instance(context);
        naming = Naming.instance(context);
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
    
    public int position(Node node) {
        if (node == null || node.getToken() == null) {
            return Position.NOPOS;
        } else {
            Token token = node.getToken();
            return getMap().getPosition(token.getLine(), token.getCharPositionInLine());
        }
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
    
    /**
     * An AutoCloseable for restoring a captured source position
     */
    class SavedPosition implements AutoCloseable {
        
        private final int pos;

        SavedPosition(int pos) {
            this.pos = pos;
        }

        /**
         * Restores the captured source position
         */
        @Override
        public void close() {
            make.at(pos);
        }
    }
    
    /**
     * Returns an AutoCloseable whose {@link SavedPosition#close()} will 
     * restore the current position, and sets the position to the given value
     */
    public SavedPosition savePosition(int at) {
        SavedPosition saved = new SavedPosition(make.pos);
        make.at(at);
        return saved;
    }
    
    public SavedPosition savePosition(Node node) {
        SavedPosition saved = new SavedPosition(make.pos);
        at(node);
        return saved;
    }
    
    /**
     * Returns an AutoCloseable whose {@link SavedPosition#close()} will 
     * restore the current position, and sets the position to Position.NOPOS
     * (i.e. useful for compiler book-keeping code).
     */
    public SavedPosition noPosition() {
        SavedPosition saved = new SavedPosition(make.pos);
        make.at(Position.NOPOS);
        return saved;
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
        return naming.makeUnquotedIdent(ident);
    }

    /** 
     * Makes an <strong>unquoted</strong> simple identifier
     * @param ident The identifier
     * @return The ident
     */
    JCExpression makeUnquotedIdent(Name ident) {
        return naming.makeUnquotedIdent(ident);
    }

    /** 
     * Makes an <strong>quoted</strong> simple identifier
     * @param ident The identifier
     * @return The ident
     */
    JCIdent makeQuotedIdent(String ident) {
        // TODO Only 3 callers
        return naming.makeQuotedIdent(ident);
    }
    
    /** 
     * Makes a <strong>quoted</strong> qualified (compound) identifier from 
     * the given qualified name. Each part of the name will be 
     * quoted if it is a Java keyword.
     * @param qualifiedName The qualified name 
     */
    JCExpression makeQuotedQualIdentFromString(String qualifiedName) {
        return naming.makeQuotedQualIdentFromString(qualifiedName);
    }

    /** 
     * Makes an <strong>unquoted</strong> qualified (compound) identifier 
     * from the given qualified name components
     * @param expr A starting expression (may be null)
     * @param names The components of the name (may be null)
     * @see #makeQuotedQualIdentFromString(String)
     */
    JCExpression makeQualIdent(JCExpression expr, String name) {
        return naming.makeQualIdent(expr, name);
    }
    
    JCExpression makeQuotedQualIdent(JCExpression expr, String... names) {
        // TODO Remove this method: Only 1 caller 
        return naming.makeQuotedQualIdent(expr, names);
    }

    JCExpression makeQuotedFQIdent(String qualifiedName) {
        // TODO Remove this method??: Only 2 callers
        return naming.makeQuotedFQIdent(qualifiedName);
    }

    JCExpression makeIdent(Type type) {
        return naming.makeIdent(type);
    }

    /**
     * Makes a <strong>unquoted</strong> field access
     * @param s1 The base expression
     * @param s2 The field to access
     * @return The field access
     */
    JCFieldAccess makeSelect(JCExpression s1, String s2) {
        return naming.makeSelect(s1, s2);
    }

    /**
     * Makes a <strong>unquoted</strong> field access
     * @param s1 The base expression
     * @param s2 The field to access
     * @return The field access
     */
    JCFieldAccess makeSelect(String s1, String s2) {
        return naming.makeSelect(s1, s2);
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
    
    /** Makes a boxed Ceylon String */
    JCExpression makeCeylonString(String s) {
        return boxString(make().Literal(s));
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
        JCExpression typeExpr = makeQuotedIdent(className);
        return makeLocalIdentityInstance(typeExpr, varName, className, isShared, null);
    }
    
    // Creates a "foo foo = new foo(parameter);"
    JCTree.JCVariableDecl makeLocalIdentityInstance(JCExpression typeExpr, String varName, String className, boolean isShared, JCTree.JCExpression parameter) {
        JCExpression initValue = makeNewClass(className, false, parameter);

        int modifiers = isShared ? 0 : FINAL;
        JCTree.JCVariableDecl var = make().VarDef(
                make().Modifiers(modifiers), 
                names().fromString(varName), 
                typeExpr, 
                initValue);
        
        return var;
    }
    
    // Creates a "new foo();"
    JCTree.JCNewClass makeNewClass(String className, boolean fullyQualified, JCTree.JCExpression parameter) {
        JCExpression name = fullyQualified ? naming.makeQuotedFQIdent(className) : makeQuotedQualIdentFromString(className);
        List<JCTree.JCExpression> params = parameter != null ? List.of(parameter) : List.<JCTree.JCExpression>nil();
        return makeNewClass(name, params);
    }
    
    /** Creates a "new foo();" */
    JCTree.JCNewClass makeSyntheticInstance(Declaration decl) {
        JCExpression clazz = naming.makeSyntheticClassname(decl);
        return makeNewClass(clazz, List.<JCTree.JCExpression>nil());
    }
    
    JCTree.JCNewClass makeNewClass(JCExpression clazz) {
        return makeNewClass(clazz, null);
    }
    
    // Creates a "new foo(arg1, arg2, ...);"
    JCTree.JCNewClass makeNewClass(JCExpression clazz, List<JCTree.JCExpression> args) {
        if (args == null) {
            args = List.<JCTree.JCExpression>nil();
        }
        return make().NewClass(null, null, clazz, args, null);
    }

    JCBlock makeGetterBlock(TypedDeclaration declarationModel,
            final Tree.Block block,
            final Tree.SpecifierOrInitializerExpression expression) {
        List<JCStatement> stats;
        if (block != null) {
            stats = statementGen().transformBlock(block);
        } else {
            BoxingStrategy boxing = CodegenUtil.getBoxingStrategy(declarationModel);
            ProducedType type = declarationModel.getType();
            JCExpression transExpr = expressionGen().transformExpression(expression.getExpression(), boxing, type);
            stats = List.<JCStatement>of(make().Return(transExpr));
        }
        JCBlock getterBlock = make().Block(0, stats);
        return getterBlock;
    }

    JCBlock makeGetterBlock(final JCExpression expression) {
        List<JCStatement> stats = List.<JCStatement>of(make().Return(expression));
        JCBlock getterBlock = make().Block(0, stats);
        return getterBlock;
    }

    JCBlock makeSetterBlock(TypedDeclaration declarationModel,
            final Tree.Block block,
            final Tree.SpecifierOrInitializerExpression expression) {
        List<JCStatement> stats;
        if (block != null) {
            stats = statementGen().transformBlock(block);
        } else {
            ProducedType type = declarationModel.getType();
            JCExpression transExpr = expressionGen().transformExpression(expression.getExpression(), BoxingStrategy.INDIFFERENT, type);
            stats = List.<JCStatement>of(make().Exec(transExpr));
        }
        JCBlock setterBlock = make().Block(0, stats);
        return setterBlock;
    }

    JCVariableDecl makeVar(long mods, String varName, JCExpression typeExpr, JCExpression valueExpr) {
        return make().VarDef(make().Modifiers(mods), names().fromString(varName), typeExpr, valueExpr);
    }
    JCVariableDecl makeVar(String varName, JCExpression typeExpr, JCExpression valueExpr) {
        return makeVar(0, varName, typeExpr, valueExpr);
    }
    JCVariableDecl makeVar(Naming.SyntheticName varName, JCExpression typeExpr, JCExpression valueExpr) {
        return makeVar(0L, varName, typeExpr, valueExpr);
    }
    JCVariableDecl makeVar(long mods, Naming.SyntheticName varName, JCExpression typeExpr, JCExpression valueExpr) {
        return make().VarDef(make().Modifiers(mods), varName.asName(), typeExpr, valueExpr);
    }
    
    /**
     * Creates a {@code VariableBox<T>}, {@code VariableBoxBoolean}, 
     * {@code VariableBoxLong} etc depending on the given declaration model.
     */
    private JCExpression makeVariableBoxType(TypedDeclaration declarationModel) {
        JCExpression boxClass;
        boolean unboxed = CodegenUtil.isUnBoxed(declarationModel);
        if (unboxed && isCeylonBoolean(declarationModel.getType())) {
            boxClass = make().Type(syms().ceylonVariableBoxBooleanType);
        } else if (unboxed && isCeylonInteger(declarationModel.getType())) {
            boxClass = make().Type(syms().ceylonVariableBoxLongType);
        } else if (unboxed && isCeylonFloat(declarationModel.getType())) {
            boxClass = make().Type(syms().ceylonVariableBoxDoubleType);
        } else if (unboxed && isCeylonCharacter(declarationModel.getType())) {
            boxClass = make().Type(syms().ceylonVariableBoxIntType);
        } else {
            boxClass = make().Ident(syms().ceylonVariableBoxType.tsym);
            int flags = unboxed ? 0 : JT_TYPE_ARGUMENT;
            boxClass = make().TypeApply(boxClass, 
                    List.<JCExpression>of(
                            makeJavaType(declarationModel.getType(), flags)));
        }
        return boxClass;
    }
    
    /**
     * Makes a final {@code VariableBox<T>} (or {@code VariableBoxBoolean}, 
     * {@code VariableBoxLong}, etc) variable decl, so that a variable can 
     * be captured.
     * @param init The initial value 
     * @param The (value/parameter) declaration which is being accessed through the box.
     */
    JCVariableDecl makeVariableBoxDecl(JCExpression init, TypedDeclaration declarationModel) {
        List<JCExpression> args = init != null ? List.<JCExpression>of(init) : List.<JCExpression>nil();
        JCExpression newBox = make().NewClass(
                null, List.<JCExpression>nil(), 
                makeVariableBoxType(declarationModel), args, null);
        String varName = naming.getVariableBoxName(declarationModel);
        JCTree.JCVariableDecl var = make().VarDef(
                make().Modifiers(FINAL), 
                names().fromString(varName),
                makeVariableBoxType(declarationModel),
                newBox);
        return var;
    }
    
    /** 
     * Creates a {@code ( let var1=expr1,var2=expr2,...,varN=exprN in varN; )}
     * or a {@code ( let var1=expr1,var2=expr2,...,varN=exprN,exprO in exprO; )}
     * @param args
     * @return
     */
    JCExpression makeLetExpr(JCExpression... args) {
        return makeLetExpr(naming.temp(), null, args);
    }

    /** Creates a 
     * {@code ( let var1=expr1,var2=expr2,...,varN=exprN in statements; varN; )}
     * or a {@code ( let var1=expr1,var2=expr2,...,varN=exprN in statements; exprO; )}
     * 
     */
    JCExpression makeLetExpr(Naming.SyntheticName varBaseName, List<JCStatement> statements, JCExpression... args) {
        return makeLetExpr(varBaseName.getName(), statements, args);
    }
    private JCExpression makeLetExpr(String varBaseName, List<JCStatement> statements, JCExpression... args) {
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
     * Type handling
     */

    boolean isBooleanTrue(Declaration decl) {
        return decl == typeFact.getBooleanTrueDeclaration();
    }
    
    boolean isBooleanFalse(Declaration decl) {
        return decl == typeFact.getBooleanFalseDeclaration();
    }
    
    /**
     * Determines whether the given type is optional.
     */
    boolean isOptional(ProducedType type) {
        // Note we don't use typeFact().isOptionalType(type) because
        // that implements a stricter test used in the type checker.
        return typeFact().getNullValueDeclaration().getType().isSubtypeOf(type);
    }
    
    boolean isNull(ProducedType type) {
        return type.getSupertype(typeFact.getNullDeclaration()) != null;
    }

    boolean isNullValue(ProducedType type) {
        return type.getSupertype(typeFact.getNullValueDeclaration().getTypeDeclaration()) != null;
    }

    public static boolean isAnything(ProducedType type) {
        return CodegenUtil.isVoid(type);
    }

    private boolean isObject(ProducedType type) {
        return typeFact.getObjectDeclaration().getType().isExactly(type);
    }
    
    public boolean isAlias(ProducedType type) {
        return type.getDeclaration().isAlias() || typeFact.getDefiniteType(type).getDeclaration().isAlias();
    }

    ProducedType simplifyType(ProducedType orgType) {
        if(orgType == null)
            return null;
        ProducedType type = orgType.resolveAliases();
        if (isOptional(type)) {
            // For an optional type T?:
            //  - The Ceylon type T? results in the Java type T
            type = typeFact().getDefiniteType(type);
            if (type.getUnderlyingType() != null) {
                // A definite type should not have its underlyingType set so we make a copy
                type = type.withoutUnderlyingType();
            }
        }
        
        TypeDeclaration tdecl = type.getDeclaration();
        if (tdecl instanceof UnionType && tdecl.getCaseTypes().size() == 1) {
            // Special case when the Union contains only a single CaseType
            // FIXME This is not correct! We might lose information about type arguments!
            type = tdecl.getCaseTypes().get(0);
        } else if (tdecl instanceof IntersectionType) {
            java.util.List<ProducedType> satisfiedTypes = tdecl.getSatisfiedTypes();
            if (satisfiedTypes.size() == 1) {
                // Special case when the Intersection contains only a single SatisfiedType
                // FIXME This is not correct! We might lose information about type arguments!
                type = satisfiedTypes.get(0);
            } else if (satisfiedTypes.size() == 2) {
                // special case for T? simplified as T&Object
                if (isTypeParameter(satisfiedTypes.get(0)) && isObject(satisfiedTypes.get(1))) {
                    type = satisfiedTypes.get(0);
                }
            }
        }
        
        return type;
    }

    ProducedTypedReference getTypedReference(TypedDeclaration decl){
        java.util.List<ProducedType> typeArgs = Collections.<ProducedType>emptyList();
        if (decl instanceof Method) {
            // For methods create type arguments for any type parameters it might have
            Method m = (Method)decl;
            if (!m.getTypeParameters().isEmpty()) {
                typeArgs = new ArrayList<ProducedType>(m.getTypeParameters().size());
                for (TypeParameter p: m.getTypeParameters()) {
                    ProducedType pt = p.getType();
                    typeArgs.add(pt);
                }
            }
        }
        
        if(decl.getContainer() instanceof TypeDeclaration){
            TypeDeclaration containerDecl = (TypeDeclaration) decl.getContainer();
            return containerDecl.getType().getTypedMember(decl, typeArgs);
        }
        return decl.getProducedTypedReference(null, typeArgs);
    }
    
    ProducedTypedReference nonWideningTypeDecl(ProducedTypedReference typedReference) {
        return nonWideningTypeDecl(typedReference, typedReference.getQualifyingType());
    }
    
    ProducedTypedReference nonWideningTypeDecl(ProducedTypedReference typedReference, ProducedType currentType) {
        ProducedTypedReference refinedTypedReference = getRefinedDeclaration(typedReference, currentType);
        if(refinedTypedReference != null){
            /*
             * We are widening if the type:
             * - is not object
             * - is erased to object
             * - refines a declaration that is not erased to object
             */
            ProducedType declType = typedReference.getType();
            ProducedType refinedDeclType = refinedTypedReference.getType();
            if(declType == null || refinedDeclType == null)
                return typedReference;
            boolean isWidening = isWidening(declType, refinedDeclType);
            
            if(!isWidening){
                // make sure we get the instantiated refined decl
                if(refinedDeclType.getDeclaration() instanceof TypeParameter
                        && !(declType.getDeclaration() instanceof TypeParameter))
                    refinedDeclType = nonWideningType(typedReference, refinedTypedReference);
                isWidening = isWideningTypeArguments(declType, refinedDeclType, true);
            }
            
            if(isWidening)
                return refinedTypedReference;
        }
        return typedReference;
    }

    /*
     * We have several special cases here to find the best non-widening refinement in case of multiple inheritace:
     * 
     * - The first special case is for some decls like None.first, which inherits from ContainerWithFirstElement
     * twice: once with Nothing (erased to j.l.Object) and once with Element (a type param). Now, in order to not widen the
     * return type it can't be Nothing (j.l.Object), it must be Element (a type param that is not instantiated), because in Java
     * a type param refines j.l.Object but not the other way around.
     * - The second special case is when implementing an interface first with a non-erased type, then with an erased type. In this
     * case we want the refined decl to be the one with the non-erased type.
     * - The third special case is when we implement a declaration via multiple super types, without having any refining
     * declarations in those supertypes, simply by instantiating a common super type with different type parameters
     */
    private ProducedTypedReference getRefinedDeclaration(ProducedTypedReference typedReference, ProducedType currentType) {
        TypedDeclaration decl = typedReference.getDeclaration();
        TypedDeclaration modelRefinedDecl = (TypedDeclaration)decl.getRefinedDeclaration();
        ProducedType referenceQualifyingType = typedReference.getQualifyingType();
        boolean forMixinMethod = 
                currentType != null
                && decl.getContainer() instanceof ClassOrInterface
                && referenceQualifyingType != null
                && referenceQualifyingType.getDeclaration() != currentType.getDeclaration();
        // quick exit
        if(decl == modelRefinedDecl && !forMixinMethod)
            return null;
        TypeDeclaration qualifyingDeclaration = currentType.getDeclaration();
        if(qualifyingDeclaration instanceof ClassOrInterface){
            // only try to find better if we're erasing to Object and we're not returning a type param
            if(willEraseToObject(typedReference.getType())
                        || isWideningTypeArguments(decl.getType(), modelRefinedDecl.getType(), true)
                    && !isTypeParameter(typedReference.getType())){
                ClassOrInterface declaringType = (ClassOrInterface) qualifyingDeclaration;
                Set<TypedDeclaration> refinedMembers = getRefinedMembers(declaringType, decl.getName(), 
                        com.redhat.ceylon.compiler.typechecker.model.Util.getSignature(decl), false);
                // now we must select a different refined declaration if we refine it more than once
                if(refinedMembers.size() > (forMixinMethod ? 0 : 1)){
                    // first case
                    for(TypedDeclaration refinedDecl : refinedMembers){
                        // get the type reference to see if any eventual type param is instantiated in our inheritance of this type/method
                        ProducedTypedReference refinedTypedReference = getRefinedTypedReference(typedReference, refinedDecl);
                        // if it is not instantiated, that's the one we're looking for
                        if(isTypeParameter(refinedTypedReference.getType()))
                            return refinedTypedReference;
                    }
                    // second case
                    for(TypedDeclaration refinedDecl : refinedMembers){
                        // get the type reference to see if any eventual type param is instantiated in our inheritance of this type/method
                        ProducedTypedReference refinedTypedReference = getRefinedTypedReference(typedReference, refinedDecl);
                        // if we're not erasing this one to Object let's select it
                        if(!willEraseToObject(refinedTypedReference.getType()) && !isWideningTypeArguments(refinedDecl.getType(), modelRefinedDecl.getType(), true))
                            return refinedTypedReference;
                    }
                    // third case
                    if(isTypeParameter(modelRefinedDecl.getType())){
                        // it can happen that we have inherited a method twice from a single refined declaration 
                        // via different supertype instantiations, without having ever refined them in supertypes
                        // so we try each super type to see if we already have a matching typed reference
                        // first super type
                        ProducedType extendedType = declaringType.getExtendedType();
                        if(extendedType != null){
                            ProducedTypedReference refinedTypedReference = getRefinedTypedReference(extendedType, modelRefinedDecl);
                            ProducedType refinedType = refinedTypedReference.getType();
                            if(!isTypeParameter(refinedType)
                                    && !willEraseToObject(refinedType))
                                return refinedTypedReference;
                        }
                        // then satisfied interfaces
                        for(ProducedType satisfiedType : declaringType.getSatisfiedTypes()){
                            ProducedTypedReference refinedTypedReference = getRefinedTypedReference(satisfiedType, modelRefinedDecl);
                            ProducedType refinedType = refinedTypedReference.getType();
                            if(!isTypeParameter(refinedType)
                                    && !willEraseToObject(refinedType))
                                return refinedTypedReference;
                        }
                    }
                }
            }
        }
        return getRefinedTypedReference(typedReference, modelRefinedDecl);
    }

    // Finds all member declarations (original and refinements) with the
    // given name and signature within the given type and it's super
    // classes and interfaces
    public Set<TypedDeclaration> getRefinedMembers(TypeDeclaration decl,
            String name, 
            java.util.List<ProducedType> signature, boolean ellipsis) {
        Set<TypedDeclaration> ret = new HashSet<TypedDeclaration>();
        collectRefinedMembers(decl, name, signature, ellipsis, 
                new HashSet<TypeDeclaration>(), ret);
        return ret;
    }

    private void collectRefinedMembers(TypeDeclaration decl, String name, 
            java.util.List<ProducedType> signature, boolean ellipsis,
            java.util.Set<TypeDeclaration> visited, Set<TypedDeclaration> ret) {
        if (visited.contains(decl)) {
            return;
        }
        else {
            visited.add(decl);
            TypeDeclaration et = decl.getExtendedTypeDeclaration();
            if (et!=null) {
                collectRefinedMembers(et, name, signature, ellipsis, visited, ret);
            }
            for (TypeDeclaration st: decl.getSatisfiedTypeDeclarations()) {
                collectRefinedMembers(st, name, signature, ellipsis, visited, ret);
            }
            Declaration found = decl.getDirectMember(name, signature, ellipsis);
            if(found != null)
                ret.add((TypedDeclaration) found);
        }
    }

    private ProducedTypedReference getRefinedTypedReference(ProducedTypedReference typedReference, 
            TypedDeclaration refinedDeclaration) {
        return getRefinedTypedReference(typedReference.getQualifyingType(), refinedDeclaration);
    }

    private ProducedTypedReference getRefinedTypedReference(ProducedType qualifyingType, 
                                                            TypedDeclaration refinedDeclaration) {
        TypeDeclaration refinedContainer = (TypeDeclaration)refinedDeclaration.getContainer();

        ProducedType refinedContainerType = qualifyingType.getSupertype(refinedContainer);
        return refinedDeclaration.getProducedTypedReference(refinedContainerType, Collections.<ProducedType>emptyList());
    }

    public boolean isWidening(ProducedType declType, ProducedType refinedDeclType) {
        return !sameTypeForCeylonTypes(syms().ceylonObjectType, declType)
                && willEraseToObject(declType)
                && !willEraseToObject(refinedDeclType);
    }

    private boolean isWideningTypeArguments(ProducedType declType, ProducedType refinedDeclType, boolean allowSubtypes) {
        if(declType == null || refinedDeclType == null)
            return false;
        // make sure we work on simplified types, to avoid stuff like optional or size-1 unions
        declType = simplifyType(declType);
        refinedDeclType = simplifyType(refinedDeclType);
        // special case for type parameters
        if(declType.getDeclaration() instanceof TypeParameter
                && refinedDeclType.getDeclaration() instanceof TypeParameter){
            // consider them equivalent if they have the same bounds
            TypeParameter tp = (TypeParameter) declType.getDeclaration();
            TypeParameter refinedTP = (TypeParameter) refinedDeclType.getDeclaration();
            
            if(haveSameBounds(tp, refinedTP))
                return false;
            // if they don't have the same bounds and we don't allow subtypes then we're widening
            if(!allowSubtypes)
                return false;
            // if we allow subtypes, we're widening if tp is not a subtype of refinedTP
            return !tp.getType().isSubtypeOf(refinedTP.getType());
        }
        if(allowSubtypes){
            // if we don't erase to object and we refine something that does, we can't possibly be widening
            if((willEraseToObject(refinedDeclType) || willEraseToSequential(refinedDeclType))
                    && !willEraseToObject(declType) && !willEraseToSequential(declType))
                return false;

            // if we have exactly the same type don't bother finding a common ancestor
            if(!declType.isExactly(refinedDeclType)){
                // check if we can form an informed decision
                if(refinedDeclType.getDeclaration() == null)
                    return true;
                // find the instantiation of the refined decl type in the decl type
                // special case for optional types: let's find the definite type since
                // in java they are equivalent
                ProducedType definiteType = typeFact().getDefiniteType(refinedDeclType);
                if(definiteType != null)
                    refinedDeclType = definiteType;
                declType = declType.getSupertype(refinedDeclType.getDeclaration());
                // could not find common type, we must be widening somehow
                if(declType == null)
                    return true;
            }
        }
        Map<TypeParameter, ProducedType> typeArguments = declType.getTypeArguments();
        Map<TypeParameter, ProducedType> refinedTypeArguments = refinedDeclType.getTypeArguments();
        for(Entry<TypeParameter, ProducedType> typeArgument : typeArguments.entrySet()){
            ProducedType refinedTypeArgument = refinedTypeArguments.get(typeArgument.getKey());
            if(refinedTypeArgument == null)
                return true; // something fishy here
            // check if the type arg is widening due to erasure
            if(isWidening(typeArgument.getValue(), refinedTypeArgument))
                return true;
            // check if the type arg is a subtype, or if its type args are widening
            if(isWideningTypeArguments(typeArgument.getValue(), refinedTypeArgument, false))
                return true;
        }
        // so far so good
        return false;
    }

    public boolean haveSameBounds(TypeParameter tp, TypeParameter refinedTP) {
        java.util.List<ProducedType> satTP = tp.getSatisfiedTypes();
        java.util.List<ProducedType> satRefinedTP = new LinkedList<ProducedType>();
        satRefinedTP.addAll(refinedTP.getSatisfiedTypes());
        // same number of bounds
        if(satTP.size() != satRefinedTP.size())
            return false;
        // make sure all the bounds are the same
        OUT:
        for(ProducedType satisfiedType : satTP){
            for(ProducedType refinedSatisfiedType : satRefinedTP){
                // if we found it, remove it from the second list to not match it again
                if(satisfiedType.isExactly(refinedSatisfiedType)){
                    satRefinedTP.remove(satRefinedTP);
                    continue OUT;
                }
            }
            // not found
            return false;
        }
        // all bounds are equal
        return true;
    }

    ProducedType nonWideningType(ProducedTypedReference declaration, ProducedTypedReference refinedDeclaration){
        final ProducedReference pr;
        if (declaration == refinedDeclaration) {
            pr = declaration;
        } else {
            ProducedType refinedType = refinedDeclaration.getType();
            // if the refined type is a method TypeParam, use the original decl that will be more correct,
            // since it may have changed name
            if(refinedType.getDeclaration() instanceof TypeParameter
                    && refinedType.getDeclaration().getContainer() instanceof Method){
                // find its index in the refined declaration
                TypeParameter refinedTypeParameter = (TypeParameter) refinedType.getDeclaration();
                Method refinedMethod = (Method) refinedTypeParameter.getContainer();
                int i=0;
                for(TypeParameter tp : refinedMethod.getTypeParameters()){
                    if(tp.getName().equals(refinedTypeParameter.getName()))
                        break;
                    i++;
                }
                if(i >= refinedMethod.getTypeParameters().size()){
                    throw new RuntimeException("Can't find type parameter "+refinedTypeParameter.getName()+" in its container "+refinedMethod.getName());
                }
                // the refining method type parameter should be at the same index
                if(declaration.getDeclaration() instanceof Method == false)
                    throw new RuntimeException("Refining declaration is not a method: "+declaration);
                Method refiningMethod = (Method) declaration.getDeclaration();
                if(i >= refiningMethod.getTypeParameters().size()){
                    throw new RuntimeException("Refining method does not have enough type parameters to refine "+refinedMethod.getName());
                }
                pr = refiningMethod.getTypeParameters().get(i).getType();
            } else {
                pr = refinedType;
            }
        }
        if (pr.getDeclaration() instanceof Functional
                && Decl.isMpl((Functional)pr.getDeclaration())) {
            // Methods with MPL have a Callable return type, not the type of 
            // the innermost Callable.
            return getReturnTypeOfCallable(pr.getFullType());
        }
        return pr.getType();
    }

    private ProducedType javacCeylonTypeToProducedType(com.sun.tools.javac.code.Type t) {
        return loader().getType(getLanguageModule(), t.tsym.packge().getQualifiedName().toString(), t.tsym.getQualifiedName().toString(), null);
    }

    private ProducedType javacJavaTypeToProducedType(com.sun.tools.javac.code.Type t) {
        return loader().getType(getJDKBaseModule(), t.tsym.packge().getQualifiedName().toString(), t.tsym.getQualifiedName().toString(), null);
    }

    private boolean sameTypeForCeylonTypes(Type ceylonType, ProducedType otherType) {
        return otherType != null && javacCeylonTypeToProducedType(ceylonType).isExactly(otherType);
    }
    
    /**
     * Determines if a type will be erased to java.lang.Object once converted to Java
     * @param type
     * @return
     */
    boolean willEraseToObject(ProducedType type) {
        if(type == null)
            return false;
        type = simplifyType(type);
        TypeDeclaration decl = type.getDeclaration();
        // All the following types either are Object or erase to Object
        if (decl == typeFact.getObjectDeclaration()
                || decl == typeFact.getIdentifiableDeclaration()
                || decl == typeFact.getBasicDeclaration()
                || decl == typeFact.getNullDeclaration()
                || decl == typeFact.getNullValueDeclaration().getTypeDeclaration()
                || decl == typeFact.getAnythingDeclaration()
                || decl instanceof NothingType) {
            return true;
        }
        // Any Unions and Intersections erase to Object as well
        // except for the ones that erase to Sequential
        return ((decl instanceof UnionType || decl instanceof IntersectionType)
                && !typeFact().isSequentialType(type));
    }
    
    boolean willEraseToPrimitive(ProducedType type) {
        return (isCeylonBoolean(type) || isCeylonInteger(type) || isCeylonFloat(type) || isCeylonCharacter(type));
    }
    
    boolean willEraseToException(ProducedType type) {
        type = simplifyType(type);
        return (sameTypeForCeylonTypes(syms().ceylonExceptionType, type));
    }
    
    boolean willEraseToSequential(ProducedType type) {
        type = simplifyType(type);
        TypeDeclaration decl = type.getDeclaration();
        return (decl instanceof UnionType || decl instanceof IntersectionType)
                && typeFact().isSequentialType(type);
    }

    // keep in sync with MethodDefinitionBuilder.paramType()
    public boolean willEraseToBestBounds(Parameter param) {
        ProducedType type = param.getType();
        if (typeFact().isUnion(type) 
                || typeFact().isIntersection(type)) {
            final TypeDeclaration refinedTypeDecl = ((TypedDeclaration)CodegenUtil.getTopmostRefinedDeclaration(param.getModel())).getType().getDeclaration();
            if (refinedTypeDecl instanceof TypeParameter
                    && !refinedTypeDecl.getSatisfiedTypes().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    boolean hasErasure(ProducedType type) {
        return hasErasureResolved(type.resolveAliases());
    }
    
    private boolean hasErasureResolved(ProducedType type) {
        if(type == null)
            return false;
        TypeDeclaration declaration = type.getDeclaration();
        if(declaration == null)
            return false;
        if(declaration instanceof UnionType){
            UnionType ut = (UnionType) declaration;
            java.util.List<ProducedType> caseTypes = ut.getCaseTypes();
            // special case for optional types
            if(caseTypes.size() == 2){
                if(isOptional(caseTypes.get(0)))
                    return hasErasureResolved(caseTypes.get(1));
                if(isOptional(caseTypes.get(1)))
                    return hasErasureResolved(caseTypes.get(0));
            }
            // must be erased
            return true;
        }
        if(declaration instanceof IntersectionType){
            IntersectionType ut = (IntersectionType) declaration;
            java.util.List<ProducedType> satisfiedTypes = ut.getSatisfiedTypes();
            // special case for non-optional types
            if(satisfiedTypes.size() == 2){
                if(isObject(satisfiedTypes.get(0)))
                    return hasErasureResolved(satisfiedTypes.get(1));
                if(isObject(satisfiedTypes.get(1)))
                    return hasErasureResolved(satisfiedTypes.get(0));
            }
            // must be erased
            return true;
        }
        if(declaration instanceof TypeParameter){
            // consider type parameters with non-erased bounds as erased to force a cast
            // see https://github.com/ceylon/ceylon-compiler/issues/1327
            for(ProducedType bound : declaration.getSatisfiedTypes()){
                if(!willEraseToObject(bound))
                    return true;
            }
            return false;
        }
        // Note: we don't consider types like Anything, Null, Basic, Identifiable as erased because
        // they can never be better than Object as far as Java is concerned
        // FIXME: what about Nothing then?
        
        // special case for Callable where we stop after the first type param
        boolean isCallable = isCeylonCallable(type);
        
        // now check its type parameters
        for(ProducedType pt : type.getTypeArgumentList()){
            if(hasErasureResolved(pt))
                return true;
            if(isCallable)
                break;
        }
        // no erasure here
        return false;
    }

    /**
     * This method should do the same sort of logic as AbstractTransformer.makeTypeArgs to determine
     * that the given type will be turned raw as a return type
     */
    boolean isTurnedToRaw(ProducedType type){
        return isTurnedToRawResolved(type.resolveAliases());
    }
    
    private boolean isTurnedToRawResolved(ProducedType type) {
        // if we don't have type arguments we can't be raw
        if(type.getTypeArguments().isEmpty())
            return false;

        // we only go raw if every type param is an erased union/intersection
        
        boolean everyTypeArgumentIsErasedUnionIntersection = true;
        
        // start with type but consider ever qualifying type
        ProducedType singleType = type;
        do{
            // special case for Callable where we stop after the first type param
            boolean isCallable = isCeylonCallable(singleType);
            TypeDeclaration declaration = singleType.getDeclaration();
            Map<TypeParameter, ProducedType> typeArguments = singleType.getTypeArguments();
            for(TypeParameter tp : declaration.getTypeParameters()){
                ProducedType ta = typeArguments.get(tp);
                // skip invalid input
                if(tp == null || ta == null)
                    return false;

                // see makeTypeArgs: Nothing in contravariant position causes a raw type
                if(tp.isContravariant() && ta.getDeclaration() instanceof NothingType)
                    return true;
                
                everyTypeArgumentIsErasedUnionIntersection &= isErasedUnionOrIntersection(ta);

                // Callable really has a single type arg in Java
                if(isCallable)
                    break;
                // don't recurse
            }
            // move on to next qualifying type
        }while((singleType = singleType.getQualifyingType()) != null);
        
        // we're only raw if every type param is an erased union/intersection
        return everyTypeArgumentIsErasedUnionIntersection;
    }

    private boolean isErasedUnionOrIntersection(ProducedType producedType) {
        TypeDeclaration typeDeclaration = producedType.getDeclaration();
        if(typeDeclaration instanceof UnionType){
            UnionType ut = (UnionType) typeDeclaration;
            java.util.List<ProducedType> caseTypes = ut.getCaseTypes();
            // special case for optional types
            if(caseTypes.size() == 2){
                if(isNull(caseTypes.get(0))){
                    return isErasedUnionOrIntersection(caseTypes.get(1));
                }else if(isNull(caseTypes.get(1))){
                    return isErasedUnionOrIntersection(caseTypes.get(0));
                }
            }
            // it is erased unless we turn it into Sequential something
            return !willEraseToSequential(producedType);
        }
        if(typeDeclaration instanceof IntersectionType){
            IntersectionType ut = (IntersectionType) typeDeclaration;
            java.util.List<ProducedType> satisfiedTypes = ut.getSatisfiedTypes();
            // special case for non-optional types
            if(satisfiedTypes.size() == 2){
                if(isObject(satisfiedTypes.get(0))){
                    return isErasedUnionOrIntersection(satisfiedTypes.get(1));
                }else if(isObject(satisfiedTypes.get(1))){
                    return isErasedUnionOrIntersection(satisfiedTypes.get(0));
                }
            }
            // it is erased unless we turn it into Sequential something
            return !willEraseToSequential(producedType);
        }
        // we found something which is not erased entirely
        return false;
    }

    boolean isCeylonString(ProducedType type) {
        return (sameTypeForCeylonTypes(syms().ceylonStringType, type));
    }
    
    boolean isCeylonBoolean(ProducedType type) {
        TypeDeclaration declaration = type.getDeclaration();
        return declaration != null
                && (sameTypeForCeylonTypes(syms().ceylonBooleanType, type)
                        || isBooleanTrue(declaration)
                        || declaration == typeFact.getBooleanTrueClassDeclaration()
                        || isBooleanFalse(declaration)
                        || declaration == typeFact.getBooleanFalseClassDeclaration());
    }
    
    boolean isCeylonInteger(ProducedType type) {
        return (sameTypeForCeylonTypes(syms().ceylonIntegerType, type));
    }
    
    boolean isCeylonFloat(ProducedType type) {
        return (sameTypeForCeylonTypes(syms().ceylonFloatType, type));
    }
    
    boolean isCeylonCharacter(ProducedType type) {
        return (sameTypeForCeylonTypes(syms().ceylonCharacterType, type));
    }

    boolean isCeylonArray(ProducedType type) {
        return type.getSupertype(typeFact.getArrayDeclaration()) != null;
    }
    
    boolean isCeylonObject(ProducedType type) {
        return sameTypeForCeylonTypes(syms().ceylonObjectType, type);
    }
    
    boolean isCeylonBasicType(ProducedType type) {
        return (isCeylonString(type) || isCeylonBoolean(type) || isCeylonInteger(type) || isCeylonFloat(type) || isCeylonCharacter(type));
    }
    
    boolean isCeylonCallable(ProducedType type) {
        // only say yes for exactly Callable, as this is mostly used for erasure of its second type parameter
        // but we want subtypes of Callable such as the metamodel to have those extra type parameters ATM
        return type.getDeclaration() == typeFact.getCallableDeclaration();
//        return type.getDeclaration().getUnit().isCallableType(type);
    }

    boolean isCeylonCallableSubtype(ProducedType type) {
        return typeFact().isCallableType(type);
    }

    boolean isExactlySequential(ProducedType type) {
        return typeFact().getDefiniteType(type).getDeclaration() == typeFact.getSequentialDeclaration();
    }
    
    boolean isCeylonMetamodelDeclaration(ProducedType type) {
        return type.isSubtypeOf(typeFact().getMetamodelDeclarationDeclaration().getType());
    }

    boolean isCeylonSequentialMetamodelDeclaration(ProducedType type) {
        return type.isSubtypeOf(typeFact().getSequentialType(typeFact().getMetamodelDeclarationDeclaration().getType()));
    }

    /*
     * Java Type creation
     */
    
    /** For use in {@code implements} clauses. */
    static final int JT_SATISFIES = 1 << 0;
    /** For use in {@code extends} clauses. */
    static final int JT_EXTENDS = 1 << 1;
    
    /** For use when a primitive type won't do. */
    static final int JT_NO_PRIMITIVES = 1 << 2;
    
    /** For generating a type without type arguments. */
    static final int JT_RAW = 1 << 3;
    /** For use in {@code catch} statements. */
    static final int JT_CATCH = 1 << 4;
    /** 
     * Generate a 'small' primitive type (if the type is primitive and has a 
     * small variant). 
     */
    static final int JT_SMALL = 1 << 5;
    /** For use in {@code new} expressions. */
    static final int JT_CLASS_NEW = 1 << 6;
    /** Generates the Java type of the companion class of the given type */
    static final int JT_COMPANION = 1 << 7;
    static final int JT_NON_QUALIFIED = 1 << 8;
    
    private static final int __JT_RAW_TP_BOUND = 1 << 9;
    /** 
     * If the type is a type parameter, return the Java type for its upper bound. 
     * Implies {@link #JT_RAW}   
     */
    static final int JT_RAW_TP_BOUND = JT_RAW | __JT_RAW_TP_BOUND;
    
    private static final int __JT_TYPE_ARGUMENT = 1 << 10;
    /** For use when generating a type argument. Implies {@code JT_NO_PRIMITIVES} */
    static final int JT_TYPE_ARGUMENT = JT_NO_PRIMITIVES | __JT_TYPE_ARGUMENT;

    /** For use when we want a value type class. */
    static final int JT_VALUE_TYPE = 1 << 11;
    
    /** Generates the Java type of the companion class of the given class */
    static final int JT_ANNOTATION = 1 << 12;
    /** Generates the Java type of the companion class of the given class */
    static final int JT_ANNOTATIONS = 1 << 13;

    /** Do not resolve aliases, useful if we want a class literal pointing to the alias class itself. */
    static final int JT_CLASS_LITERAL = 1 << 14;

    /**
     * This function is used solely for method return types and parameters 
     */
    JCExpression makeJavaType(TypedDeclaration typeDecl, ProducedType type, int flags) {
        if (typeDecl instanceof Method
                && ((Method)typeDecl).isParameter()) {
            Method p = (Method)typeDecl;
            ProducedType pt = type;
            for (int ii = 1; ii < p.getParameterLists().size(); ii++) {
                pt = typeFact().getCallableType(pt);
            }
            return makeJavaType(typeFact().getCallableType(pt), flags);
        } else {
            boolean usePrimitives = CodegenUtil.isUnBoxed(typeDecl);
            return makeJavaType(type, flags | (usePrimitives ? 0 : AbstractTransformer.JT_NO_PRIMITIVES));
        }
    }

    JCExpression makeJavaType(TypeSymbol tsym){
        return make().QualIdent(tsym);
    }
    
    JCExpression makeJavaType(ProducedType producedType) {
        return makeJavaType(producedType, 0);
    }

    JCExpression makeJavaType(final ProducedType ceylonType, final int flags) {
        ProducedType type = ceylonType;
        if(type == null
                || type.getDeclaration() instanceof UnknownType)
            return make().Erroneous();
        
        // resolve aliases
        if((flags & JT_CLASS_LITERAL) == 0)
            type = type.resolveAliases();
        
        if ((flags & __JT_RAW_TP_BOUND) != 0
                && type.getDeclaration() instanceof TypeParameter) {
            type = ((TypeParameter)type.getDeclaration()).getExtendedType();    
        }
        
        // ERASURE
        if ((flags & JT_CLASS_LITERAL) == 0
                // don't consider erasure for class literals since it would resolve aliases and we want class
                // literals to the alias class
                && willEraseToObject(type)) {
            // For an erased type:
            // - Any of the Ceylon types Anything, Object, Null,
            //   Basic, and Nothing result in the Java type Object
            // For any other union type U|V (U nor V is Optional):
            // - The Ceylon type U|V results in the Java type Object
            if ((flags & JT_SATISFIES) != 0) {
                return null;
            } else {
                return make().Type(syms().objectType);
            }
        } else if (willEraseToSequential(type)) {
            type = typeFact().getDefiniteType(type).getSupertype(typeFact().getSequentialDeclaration());
        } else if (willEraseToException(type)) {
            if ((flags & JT_CLASS_NEW) != 0
                    || (flags & JT_EXTENDS) != 0) {
                return makeIdent(syms().ceylonExceptionType);
            } else if ((flags & JT_CATCH) != 0) {
                return make().Type(syms().exceptionType);
            } else {
                return make().Type(syms().throwableType);
            }
        } else if ((flags & (JT_SATISFIES | JT_EXTENDS | JT_NO_PRIMITIVES | JT_CLASS_NEW)) == 0
                && ((isCeylonBasicType(type) && !isOptional(type)) || isJavaString(type))) {
            if (isCeylonString(type) || isJavaString(type)) {
                return make().Type(syms().stringType);
            } else if (isCeylonBoolean(type)) {
                return make().TypeIdent(TypeTags.BOOLEAN);
            } else if (isCeylonInteger(type)) {
                if ("byte".equals(type.getUnderlyingType())) {
                    return make().TypeIdent(TypeTags.BYTE);
                } else if ("short".equals(type.getUnderlyingType())) {
                    return make().TypeIdent(TypeTags.SHORT);
                } else if ((flags & JT_SMALL) != 0 || "int".equals(type.getUnderlyingType())) {
                    return make().TypeIdent(TypeTags.INT);
                } else {
                    return make().TypeIdent(TypeTags.LONG);
                }
            } else if (isCeylonFloat(type)) {
                if ((flags & JT_SMALL) != 0 || "float".equals(type.getUnderlyingType())) {
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
        } else if ((flags & JT_VALUE_TYPE) == 0 && isJavaArray(type)){
            return getJavaArrayElementType(type, flags);
        }
        
        JCExpression jt = null;
        
        ProducedType simpleType;
        if((flags & JT_CLASS_LITERAL) == 0)
            simpleType = simplifyType(type);
        else
            simpleType = type;
        
        // see if we need to cross methods when looking up container types
        // this is required to properly collect all the type parameters for local interfaces
        // which we pull up to the toplevel and capture all the container type parameters
        boolean needsQualifyingTypeArgumentsFromLocalContainers =
                Decl.isCeylon(simpleType.getDeclaration())
                && simpleType.getDeclaration() instanceof Interface
                // this is only valid for interfaces, not for their companion which stay where they are
                && (flags & JT_COMPANION) == 0;
        
        java.util.List<ProducedType> qualifyingTypes = null;
        ProducedType qType = simpleType;
        boolean hasTypeParameters = false;
        while (qType != null) {
            hasTypeParameters |= !qType.getTypeArguments().isEmpty();
            if(qualifyingTypes != null)
                qualifyingTypes.add(qType);
            TypeDeclaration typeDeclaration = qType.getDeclaration();
            // local interfaces that are pulled to the toplevel need to cross containing methods to find
            // all the containing type parameters that it captures
            if(Decl.isLocal(typeDeclaration)
                    && needsQualifyingTypeArgumentsFromLocalContainers
                    && typeDeclaration instanceof ClassOrInterface){
                ClassOrInterface container = Decl.getClassOrInterfaceContainer(typeDeclaration, false);
                qType = container == null ? null : container.getType();
            }else{
                qType = qType.getQualifyingType();
                if(qType != null && qType.getDeclaration() instanceof ClassOrInterface == false){
                    // sometimes the typechecker throws qualifying intersections at us and
                    // we can't make anything of them, since some members may be unrelated to
                    // the qualified declaration. This happens with "extends super.Foo()"
                    // for example. See https://github.com/ceylon/ceylon-compiler/issues/1478
                    qType = qType.getSupertype((TypeDeclaration) typeDeclaration.getContainer());
                }
            }
            // delayed allocation if we have a qualifying type
            if(qualifyingTypes == null && qType != null){
                qualifyingTypes = new java.util.ArrayList<ProducedType>();
                qualifyingTypes.add(simpleType);
            }
        }
        int firstQualifyingTypeWithTypeParameters = qualifyingTypes != null ? qualifyingTypes.size() - 1 : 0;
        // find the first static one, from the right to the left
        if(qualifyingTypes != null){
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
        }
        
        if (((flags & JT_RAW) == 0) && hasTypeParameters) {
            // special case for interfaces because we pull them into toplevel types
            if(Decl.isCeylon(simpleType.getDeclaration())
                    && qualifyingTypes != null
                    && qualifyingTypes.size() > 1
                    && simpleType.getDeclaration() instanceof Interface
                    // this is only valid for interfaces, not for their companion which stay where they are
                    && (flags & JT_COMPANION) == 0){
                JCExpression baseType;
                TypeDeclaration tdecl = simpleType.getDeclaration();
                // collect all the qualifying type args we'd normally have
                java.util.List<TypeParameter> qualifyingTypeParameters = new java.util.ArrayList<TypeParameter>();
                java.util.Map<TypeParameter, ProducedType> qualifyingTypeArguments = new java.util.HashMap<TypeParameter, ProducedType>();
                collectQualifyingTypeArguments(qualifyingTypeParameters, qualifyingTypeArguments, qualifyingTypes);
                
                ListBuffer<JCExpression> typeArgs = makeTypeArgs(isCeylonCallable(simpleType), 
                        flags, 
                        qualifyingTypeArguments, qualifyingTypeParameters);
                if (isCeylonCallable(type) && 
                        (flags & JT_CLASS_NEW) != 0) {
                    baseType = makeIdent(syms().ceylonAbstractCallableType);
                } else {
                    baseType = naming.makeDeclarationName(tdecl, DeclNameFlag.QUALIFIED);
                }

                if (typeArgs != null && typeArgs.size() > 0) {
                    jt = make().TypeApply(baseType, typeArgs.toList());
                } else {
                    jt = baseType;
                }
            }else if((flags & JT_NON_QUALIFIED) == 0){
                int index = 0;
                if(qualifyingTypes != null){
                    for (ProducedType qualifyingType : qualifyingTypes) {
                        jt = makeParameterisedType(qualifyingType, type, flags, jt, qualifyingTypes, firstQualifyingTypeWithTypeParameters, index);
                        index++;
                    }
                }else{
                    jt = makeParameterisedType(simpleType, type, flags, jt, qualifyingTypes, firstQualifyingTypeWithTypeParameters, index);
                }
            }else{
                jt = makeParameterisedType(type, type, flags, jt, qualifyingTypes, 0, 0);
            }
        } else {
            TypeDeclaration tdecl = simpleType.getDeclaration();            
            // For an ordinary class or interface type T:
            // - The Ceylon type T results in the Java type T
            if(tdecl instanceof TypeParameter)
                jt = makeQuotedIdent(tdecl.getName());
            // don't use underlying type if we want no primitives
            else if((flags & (JT_SATISFIES | JT_NO_PRIMITIVES)) != 0 || simpleType.getUnderlyingType() == null){
                jt = naming.makeDeclarationName(tdecl, jtFlagsToDeclNameOpts(flags));
            }else
                jt = makeQuotedFQIdent(simpleType.getUnderlyingType());
        }
        
        return (jt != null) ? jt : makeErroneous(null, "compiler bug: the java type corresponding to " + ceylonType + " could not be computed");
    }

    /**
     * Collects all the type parameters and arguments required for an interface that's been pulled up to the
     * toplevel, including its containing type and method type parameters.
     */
    private void collectQualifyingTypeArguments(java.util.List<TypeParameter> qualifyingTypeParameters, 
            Map<TypeParameter, ProducedType> qualifyingTypeArguments, 
            java.util.List<ProducedType> qualifyingTypes) {
        // make sure we only add type parameters with the same name once, as duplicates are erased from the target interface
        // since they cannot be accessed
        Set<String> names = new HashSet<String>();
        // walk the qualifying types backwards to make sure we only add a TP with the same name once and the outer one wins
        for (int i = qualifyingTypes.size()-1 ; i >= 0 ; i--) {
            ProducedType qualifiedType = qualifyingTypes.get(i);
            Map<TypeParameter, ProducedType> tas = qualifiedType.getTypeArguments();
            java.util.List<TypeParameter> tps = qualifiedType.getDeclaration().getTypeParameters();
            // add any type params for this type
            if (tps != null) {
                int index = 0;
                for(TypeParameter tp : tps){
                    // add it only once
                    if(names.add(tp.getName())){
                        // start putting all these type parameters at 0 and then in order
                        // so that outer type params end up before inner type params but
                        // order is preserved within each type
                        qualifyingTypeParameters.add(index++, tp);
                        qualifyingTypeArguments.put(tp, tas.get(tp));
                    }
                }
            }
            // add any container method TP
            TypeDeclaration declaration = qualifiedType.getDeclaration();
            if(Decl.isLocal(declaration)){
                Scope scope = declaration.getContainer();
                // collect every container method until the next type or package
                java.util.List<Method> methods = new LinkedList<Method>();
                while(scope != null
                        && scope instanceof ClassOrInterface == false
                        && scope instanceof Package == false){
                    if(scope instanceof Method){
                        methods.add((Method) scope);
                    }
                    scope = scope.getContainer();
                }
                // methods are sorted inner to outer, which is the order we're following here for types
                for(Method method : methods){
                    java.util.List<TypeParameter> methodTypeParameters = method.getTypeParameters();
                    if (methodTypeParameters != null) {
                        int index = 0;
                        for(TypeParameter tp : methodTypeParameters){
                            // add it only once
                            if(names.add(tp.getName())){
                                // start putting all these type parameters at 0 and then in order
                                // so that outer type params end up before inner type params but
                                // order is preserved within each type
                                qualifyingTypeParameters.add(index++, tp);
                                qualifyingTypeArguments.put(tp, tp.getType());
                            }
                        }
                    }
                }
            }
        }
    }

    public boolean isJavaArray(ProducedType type) {
        if(type == null)
            return false;
        type = simplifyType(type);
        if(type == null)
            return false;
        return isJavaArray(type.getDeclaration());
    }

    public static boolean isJavaArray(TypeDeclaration decl) {
        return Decl.isJavaArray(decl);
    }

    public boolean isJavaObjectArray(ProducedType type) {
        if(type == null)
            return false;
        type = simplifyType(type);
        if(type == null)
            return false;
        return isJavaObjectArray(type.getDeclaration());
    }

    public static boolean isJavaObjectArray(TypeDeclaration decl) {
        return Decl.isJavaObjectArray(decl);
    }

    public static boolean isJavaArrays(TypeDeclaration decl) {
        if(decl instanceof Class == false)
            return false;
        Class c = (Class) decl;
        String name = c.getQualifiedNameString();
        return name.equals("java.lang::arrays");
    }

    private JCExpression getJavaArrayElementType(ProducedType type, int flags) {
        if(type == null)
            return makeErroneous(null, "compiler bug: "+ type + " is not a java array");
        type = simplifyType(type);
        if(type == null || type.getDeclaration() instanceof Class == false)
            return makeErroneous(null, "compiler bug: " + type + " is not a java array");
        Class c = (Class) type.getDeclaration();
        String name = c.getQualifiedNameString();
        if(name.equals("java.lang::ObjectArray")){
            // fetch its type parameter
            if(type.getTypeArgumentList().size() != 1)
                return makeErroneous(null, "compiler bug: " + type + " is missing parameter type to java ObjectArray");
            ProducedType elementType = type.getTypeArgumentList().get(0);
            if(elementType == null)
                return makeErroneous(null, "compiler bug: " + type + " has null parameter type to java ObjectArray");
            return make().TypeArray(makeJavaType(elementType, flags | JT_TYPE_ARGUMENT));
        }else if(name.equals("java.lang::ByteArray")){
            return make().TypeArray(make().TypeIdent(TypeTags.BYTE));
        }else if(name.equals("java.lang::ShortArray")){
            return make().TypeArray(make().TypeIdent(TypeTags.SHORT));
        }else if(name.equals("java.lang::IntArray")){
            return make().TypeArray(make().TypeIdent(TypeTags.INT));
        }else if(name.equals("java.lang::LongArray")){
            return make().TypeArray(make().TypeIdent(TypeTags.LONG));
        }else if(name.equals("java.lang::FloatArray")){
            return make().TypeArray(make().TypeIdent(TypeTags.FLOAT));
        }else if(name.equals("java.lang::DoubleArray")){
            return make().TypeArray(make().TypeIdent(TypeTags.DOUBLE));
        }else if(name.equals("java.lang::BooleanArray")){
            return make().TypeArray(make().TypeIdent(TypeTags.BOOLEAN));
        }else if(name.equals("java.lang::CharArray")){
            return make().TypeArray(make().TypeIdent(TypeTags.CHAR));
        }else {
            return makeErroneous(null, "compiler bug: " + type + " is an unknown java array type");
        }
    }

    public JCExpression makeParameterisedType(ProducedType type, ProducedType generalType, final int flags, 
            JCExpression qualifyingExpression, java.util.List<ProducedType> qualifyingTypes, 
            int firstQualifyingTypeWithTypeParameters, int index) {
        JCExpression baseType;
        TypeDeclaration tdecl = type.getDeclaration();
        ListBuffer<JCExpression> typeArgs = null;
        if(index >= firstQualifyingTypeWithTypeParameters)
            typeArgs = makeTypeArgs( 
                    type, 
                    //tdecl, 
                    flags);
        if (isCeylonCallable(generalType) && 
                (flags & JT_CLASS_NEW) != 0) {
            baseType = makeIdent(syms().ceylonAbstractCallableType);
        } else if (index == 0) {
            // in Ceylon we'd move the nested decl to a companion class
            // but in Java we just don't have type params to the qualifying type if the
            // qualified type is static
            if (tdecl instanceof Interface
                    && qualifyingTypes != null
                    && qualifyingTypes.size() > 1
                    && firstQualifyingTypeWithTypeParameters == 0) {
                baseType = naming.makeCompanionClassName(tdecl);
            } else {
                baseType = naming.makeDeclarationName(tdecl, jtFlagsToDeclNameOpts(flags));
            }
            
        } else {
            baseType = naming.makeDeclName(qualifyingExpression, tdecl, 
                    jtFlagsToDeclNameOpts(flags 
                            | JT_NON_QUALIFIED 
                            | (type.getDeclaration() instanceof Interface ? JT_COMPANION : 0)));
        }

        if (typeArgs != null && typeArgs.size() > 0) {
            qualifyingExpression = make().TypeApply(baseType, typeArgs.toList());
        } else {
            qualifyingExpression = baseType;
        }
        return qualifyingExpression;
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
        boolean onlyErasedUnions = true;
        ListBuffer<JCExpression> typeArgs = new ListBuffer<JCExpression>();
        
        for (TypeParameter tp : tps) {
            ProducedType ta = tas.get(tp);
            
            boolean isDependedOn = hasDependentTypeParameters(tps, tp);
            
            // Null will claim to be optional, but if we get its non-null type we will land with Nothing, which is not what
            // we want, so we make sure it's not Null
            if (isOptional(ta) && !isNull(ta)) {
                // For an optional type T?:
                // - The Ceylon type Foo<T?> results in the Java type Foo<T>.
                ta = getNonNullType(ta);
            }
            if (typeFact().isUnion(ta) || typeFact().isIntersection(ta)) {
                // For any other union type U|V (U nor V is Optional):
                // - The Ceylon type Foo<U|V> results in the raw Java type Foo.
                // For any other intersection type U&V:
                // - The Ceylon type Foo<U&V> results in the raw Java type Foo.
                ProducedType listType = typeFact().getDefiniteType(ta).getSupertype(typeFact().getSequentialDeclaration());
                // don't break if the union type is erased to something better than Object
                if(listType == null){
                    // use raw types if:
                    // - we're calling a constructor
                    // - we're not in a type argument (when used as type arguments raw types have more constraint than at the toplevel)
                    //   or we're in an extends or satisfies and the type parameter is a self type
                    if((flags & JT_CLASS_NEW) != 0
                            || ((flags & (JT_EXTENDS | JT_SATISFIES)) != 0 && tp.getSelfTypedDeclaration() != null)){
                        // A bit ugly, but we need to escape from the loop and create a raw type, no generics
                        typeArgs = null;
                        break;
                    } else if((flags & (__JT_TYPE_ARGUMENT | JT_EXTENDS | JT_SATISFIES)) != 0) {
                        onlyErasedUnions = false;
                    }
                    // otherwise just go on
                }else{
                    ta = listType;
                    onlyErasedUnions = false;
                }
            } else {
                onlyErasedUnions = false;
            }
            if (isCeylonBoolean(ta)
                    && !isTypeParameter(ta)) {
                ta = typeFact.getBooleanDeclaration().getType();
            } 
            JCExpression jta;
            
            if(!tp.getSatisfiedTypes().isEmpty()){
                boolean needsCastForBounds = false;
                for(ProducedType bound : tp.getSatisfiedTypes()){
                    bound = bound.substitute(tas);
                    needsCastForBounds |= expressionGen().needsCast(ta, bound, false, false, false);
                }
                if(needsCastForBounds){
                    // replace with the first bound
                    ta = tp.getSatisfiedTypes().get(0).substitute(tas);
                    if(tp.getSatisfiedTypes().size() > 1
                            || isBoundsSelfDependant(tp)
                            || willEraseToObject(ta)
                            // we should reject it for all non-covariant types, unless we're in satisfies/extends
                            || ((flags & (JT_SATISFIES | JT_EXTENDS)) == 0 && !tp.isCovariant())){
                        // A bit ugly, but we need to escape from the loop and create a raw type, no generics
                        typeArgs = null;
                        break;
                    }
                }
            }
            
            if (sameTypeForCeylonTypes(syms().ceylonAnythingType, ta)) {
                // For the root type Void:
                if ((flags & (JT_SATISFIES | JT_EXTENDS)) != 0) {
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
                        jta = make().Wildcard(make().TypeBoundKind(BoundKind.UNBOUND), makeJavaType(ta, flags | JT_TYPE_ARGUMENT));
                    } else {
                        jta = make().Type(syms().objectType);
                    }
                }
            } else if (ta.getDeclaration() instanceof NothingType
                    // if we're in a type argument, extends or satisfies already, union and intersection types should 
                    // use the same erasure rules as bottom: prefer wildcards
                    || ((flags & (__JT_TYPE_ARGUMENT | JT_EXTENDS | JT_SATISFIES)) != 0
                        && (typeFact().isUnion(ta) || typeFact().isIntersection(ta)))) {
                // For the bottom type Bottom:
                if ((flags & (JT_CLASS_NEW)) != 0) {
                    // - The Ceylon type Foo<Bottom> or Foo<erased_type> appearing in an instantiation
                    //   clause results in the Java raw type Foo
                    // A bit ugly, but we need to escape from the loop and create a raw type, no generics
                    typeArgs = null;
                    break;
                } else {
                    // - The Ceylon type Foo<Bottom> appearing in an extends or satisfies location results in the Java type
                    //   Foo<Object> (see https://github.com/ceylon/ceylon-compiler/issues/633 for why)
                    if((flags & (JT_SATISFIES | JT_EXTENDS)) != 0){
                        if (ta.getDeclaration() instanceof NothingType) {
                            jta = make().Type(syms().objectType);
                        } else {
                            if (!tp.getSatisfiedTypes().isEmpty()) {
                                // union or intersection: Use the common upper bound of the types
                                jta = makeJavaType(tp.getSatisfiedTypes().get(0), JT_TYPE_ARGUMENT);
                            } else {
                                jta = make().Type(syms().objectType);
                            }
                        }
                    }else if (ta.getDeclaration() instanceof NothingType){
                        // - The Ceylon type Foo<Bottom> appearing anywhere else results in the Java type
                        // - Foo if Foo is contravariant in T (see https://github.com/ceylon/ceylon-compiler/issues/1042), or
                        // - Foo<? extends Object> if Foo is covariant in T and not depended on by other type params
                        // - Foo<Object> otherwise
                        // this is more correct than Foo<?> because a method returning Foo<?> could never override a method returning Foo<Object>
                        // see https://github.com/ceylon/ceylon-compiler/issues/1003
                        if (tp.isContravariant()) {
                            typeArgs = null;
                            break;
                        } else if (tp.isCovariant() && !isDependedOn) {
                            jta = make().Wildcard(make().TypeBoundKind(BoundKind.EXTENDS), make().Type(syms().objectType));
                        } else {
                            jta = make().Type(syms().objectType);
                        }
                    }else{
                        // - The Ceylon type Foo<T> appearing anywhere else results in the Java type
                        // - Foo<T> if Foo is invariant in T,
                        // - Foo<? extends T> if Foo is covariant in T, or
                        // - Foo<? super T> if Foo is contravariant in T
                        if (((flags & JT_CLASS_NEW) == 0) && tp.isContravariant()) {
                            jta = make().Wildcard(make().TypeBoundKind(BoundKind.SUPER), makeJavaType(ta, JT_TYPE_ARGUMENT));
                        } else if (((flags & JT_CLASS_NEW) == 0) && tp.isCovariant() && !isDependedOn) {
                            jta = make().Wildcard(make().TypeBoundKind(BoundKind.EXTENDS), makeJavaType(ta, JT_TYPE_ARGUMENT));
                        } else {
                            jta = makeJavaType(ta, JT_TYPE_ARGUMENT);
                        }
                    }
                }
            } else {
                // For an ordinary class or interface type T:
                if ((flags & (JT_SATISFIES | JT_EXTENDS)) != 0) {
                    // - The Ceylon type Foo<T> appearing in an extends or satisfies clause
                    //   results in the Java type Foo<T>
                    jta = makeJavaType(ta, JT_TYPE_ARGUMENT);
                } else {
                    // - The Ceylon type Foo<T> appearing anywhere else results in the Java type
                    // - Foo<T> if Foo is invariant in T,
                    // - Foo<? extends T> if Foo is covariant in T, or
                    // - Foo<? super T> if Foo is contravariant in T
                    if (((flags & JT_CLASS_NEW) == 0) && tp.isContravariant()) {
                        jta = make().Wildcard(make().TypeBoundKind(BoundKind.SUPER), makeJavaType(ta, JT_TYPE_ARGUMENT));
                    } else if (((flags & JT_CLASS_NEW) == 0) && tp.isCovariant() && !isDependedOn) {
                        jta = make().Wildcard(make().TypeBoundKind(BoundKind.EXTENDS), makeJavaType(ta, JT_TYPE_ARGUMENT));
                    } else {
                        jta = makeJavaType(ta, JT_TYPE_ARGUMENT);
                    }
                }
            }
            typeArgs.add(jta);
            
            if (isCeylonCallable) {
                // In the runtime Callable only has a single type param
                break;
            }
        }
        if (onlyErasedUnions) {
            typeArgs = null;
        }
        return typeArgs;
    }

    protected ProducedType getNonNullType(ProducedType pt) {
        // typeFact().getDefiniteType() intersects with Object, which isn't 
        // always right for working with the java type system.
        if (typeFact().getAnythingDeclaration().equals(pt.getDeclaration())) {
            pt = typeFact().getObjectDeclaration().getType();
        }
        else {
            pt = pt.eliminateNull();
        }
        return pt;
    }
    
    private boolean isJavaString(ProducedType type) {
        return "java.lang.String".equals(type.getUnderlyingType());
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

    private DeclNameFlag[] jtFlagsToDeclNameOpts(int flags) {
        java.util.List<DeclNameFlag> args = new LinkedList<DeclNameFlag>();
        if ((flags & JT_COMPANION) != 0) {
            args.add(DeclNameFlag.COMPANION);
        }
        if ((flags & JT_ANNOTATION) != 0) {
            args.add(DeclNameFlag.ANNOTATION);
        }
        if ((flags & JT_ANNOTATIONS) != 0) {
            args.add(DeclNameFlag.ANNOTATIONS);
        }
        if ((flags & JT_NON_QUALIFIED) == 0) {
            args.add(DeclNameFlag.QUALIFIED);
        }
        DeclNameFlag[] opts = args.toArray(new DeclNameFlag[args.size()]);
        return opts;
    }
    
    /**
     * Gets the first type parameter from the type model representing a 
     * {@code ceylon.language.Callable<Result, ParameterTypes...>}.
     * @param typeModel A {@code ceylon.language.Callable<Result, ParameterTypes...>}.
     * @return The result type of the {@code Callable}.
     */
    ProducedType getReturnTypeOfCallable(ProducedType typeModel) {
        Assert.that(isCeylonCallableSubtype(typeModel), "Expected Callable<...>, but was " + typeModel);
        ProducedType ct = typeModel.getSupertype(typeFact().getCallableDeclaration());
        return ct.getTypeArgumentList().get(0);
    }
    
    ProducedType getParameterTypeOfCallable(ProducedType callableType, int parameter) {
        Assert.that(isCeylonCallableSubtype(callableType));
        ProducedType tuple = typeFact().getCallableTuple(callableType);
        if(tuple != null){
            java.util.List<ProducedType> elementTypes = typeFact().getTupleElementTypes(tuple);
            if(elementTypes.size() > parameter){
                return elementTypes.get(parameter);
            }
        }
        return typeFact().getUnknownType();
    }
    
    int getNumParametersOfCallable(ProducedType callableType) {
        ProducedType tuple = typeFact().getCallableTuple(callableType);
        int simpleNumParametersOfCallable = getSimpleNumParametersOfCallable(tuple);
        if(simpleNumParametersOfCallable != -1)
            return simpleNumParametersOfCallable;
        int count = 0;
        while (tuple != null) {
            ProducedType tst = typeFact().nonemptyArgs(tuple).getSupertype(typeFact().getTupleDeclaration());
            if (tst!=null) {
                java.util.List<ProducedType> tal = tst.getTypeArgumentList();
                if (tal.size()>=3) {
                    tuple = tal.get(2);
                    count++;
                    continue;
                }
            }
            else if (typeFact().isEmptyType(tuple)) {
                // do nothing
            }
            else if (typeFact().isSequentialType(tuple)) {
                count++; // we count variadic params as one
            }
            break;
        }
        return count;
    }
    
    private int getSimpleNumParametersOfCallable(ProducedType args) {
        // can be a defaulted tuple of Empty|Tuple
        TypeDeclaration declaration = args.getDeclaration();
        if(declaration instanceof UnionType){
            java.util.List<ProducedType> caseTypes = declaration.getCaseTypes();
            if(caseTypes == null || caseTypes.size() != 2)
                return -1;
            ProducedType caseA = caseTypes.get(0);
            TypeDeclaration caseADecl = caseA.getDeclaration();
            ProducedType caseB = caseTypes.get(1);
            TypeDeclaration caseBDecl = caseB.getDeclaration();
            if(caseADecl instanceof ClassOrInterface == false
                    || caseBDecl instanceof ClassOrInterface == false)
                return -1;
            if(caseADecl.getQualifiedNameString().equals("ceylon.language::Empty")
                    && caseBDecl.getQualifiedNameString().equals("ceylon.language::Tuple"))
                return getSimpleNumParametersOfCallable(caseB);
            if(caseBDecl.getQualifiedNameString().equals("ceylon.language::Empty")
                    && caseADecl.getQualifiedNameString().equals("ceylon.language::Tuple"))
                return getSimpleNumParametersOfCallable(caseA);
            return -1;
        }
        // can be Tuple, Empty, Sequence or Sequential
        if(declaration instanceof ClassOrInterface == false)
            return -1;
        String name = declaration.getQualifiedNameString();
        if(name.equals("ceylon.language::Tuple")){
            ProducedType rest = args.getTypeArgumentList().get(2);
            int ret = getSimpleNumParametersOfCallable(rest);
            if(ret == -1)
                return -1;
            return ret + 1;
        }
        if(name.equals("ceylon.language::Empty")){
            return 0;
        }
        if(name.equals("ceylon.language::Sequential")
           || name.equals("ceylon.language::Sequence")){
            return 1;
        }
        return -1;
    }

    boolean isVariadicCallable(ProducedType callableType) {
        ProducedType tuple = typeFact().getCallableTuple(callableType);
        return typeFact().isTupleLengthUnbounded(tuple);
    }

    public int getMinimumParameterCountForCallable(ProducedType callableType) {
        ProducedType tuple = typeFact().getCallableTuple(callableType);
        return typeFact().getTupleMinimumLength(tuple);
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
        boolean functional = parameter.getModel() instanceof Method;
        if (producedReference == null) {
            return parameter.getType();
        }
        final ProducedTypedReference producedTypedReference = producedReference.getTypedParameter(parameter);
        final ProducedType type = functional ? producedTypedReference.getFullType() : producedTypedReference.getType();
        final TypedDeclaration producedParameterDecl = producedTypedReference.getDeclaration();
        final ProducedType declType = producedParameterDecl.getType();
        // be more resilient to upstream errors
        if(declType == null)
            return typeFact.getUnknownType();
        final TypeDeclaration declTypeDecl = declType.getDeclaration();
        if(isJavaVariadic(parameter) && (flags & TP_SEQUENCED_TYPE) == 0){
            // type of param must be Iterable<T>
            ProducedType elementType = typeFact.getIteratedType(type);
            if(elementType == null){
                log.error("ceylon", "Invalid type for Java variadic parameter: "+type.getProducedTypeQualifiedName());
                return type;
            }
            return elementType;
        }
        // We must first check for that before we look at the type declaration, because it may have been simplified by
        // the typechecker to something we think is a class, such as Empty when in reality the expected type will be Sequential
        if (willEraseToSequential(declType)) {
            // Erasure: If the declType would be Sequential<Element>
            // treat the parameter type as Sequential<Element>, because that's all
            // Java will see.
            ProducedType erasedType = typeFact().getDefiniteType(declType).getSupertype(typeFact().getSequentialDeclaration());
            return erasedType.substitute(producedReference.getTypeArguments());
        } else if (declTypeDecl instanceof ClassOrInterface) {
            return type;
        } else if ((declTypeDecl instanceof TypeParameter)
                && (flags & TP_TO_BOUND) != 0) {
            ProducedType upperBound = null;
            boolean needsCastToBound = false;
            if(!declTypeDecl.getSatisfiedTypes().isEmpty()){
                // use upper bound
                upperBound = declTypeDecl.getSatisfiedTypes().get(0);
                // make sure we apply the type arguments
                upperBound = substituteTypeArgumentsForTypeParameterBound(producedReference, upperBound);
                ProducedType self = upperBound.getDeclaration().getSelfType();
                if (self != null) {
                    // make sure we apply the type arguments
                    upperBound = self.substitute(upperBound.getTypeArguments());
                }
                needsCastToBound = expressionGen().needsCast(type, upperBound, false, false, false);
            }
            if ((willEraseToObject(type) || needsCastToBound)
                    && upperBound != null) {
                if(!willEraseToObject(upperBound))
                    return upperBound;
            }
        }
        return type;
    }

    protected ProducedType substituteTypeArgumentsForTypeParameterBound(
            ProducedReference target, ProducedType bound) {
        Declaration declaration = target.getDeclaration();
        if(declaration.getContainer() instanceof ClassOrInterface){
            ProducedType targetType = target.getQualifyingType();
            // static methods have a container but do not capture type parameters
            if(targetType != null
                    && !declaration.isStaticallyImportable()){
                ClassOrInterface methodContainer = (ClassOrInterface) declaration.getContainer();
                Map<TypeParameter, ProducedType> typeArguments = targetType.getSupertype(methodContainer).getTypeArguments();
                // we need type arguments that may come from the method container
                bound = bound.substitute(typeArguments);
            }
        }
        // and those that may come from the method call itself
        return bound.substitute(target.getTypeArguments());
    }


    private boolean isJavaVariadic(Parameter parameter) {
        return parameter.isSequenced()
                && parameter.getDeclaration() instanceof Method
                && isJavaMethod((Method) parameter.getDeclaration());
    }

    boolean isJavaMethod(Method method) {
        ClassOrInterface container = Decl.getClassOrInterfaceContainer(method);
        return container != null && !Decl.isCeylon(container);
    }
    
    boolean isJavaCtor(Class cls) {
        return !Decl.isCeylon(cls);
    }

    ProducedType getTypeForFunctionalParameter(Method fp) {
        return fp.getProducedTypedReference(null, java.util.Collections.<ProducedType>emptyList()).getFullType();
    }
    
    /*
     * Annotation generation
     */
    
    List<JCAnnotation> makeAtOverride() {
        return List.<JCAnnotation> of(make().Annotation(makeIdent(syms().overrideType), List.<JCExpression> nil()));
    }

    int checkCompilerAnnotations(Tree.Declaration decl, ListBuffer<JCTree> result){
        int old = gen().disableAnnotations;
        if(CodegenUtil.hasCompilerAnnotation(decl, "noanno")) {
            gen().disableAnnotations = CeylonTransformer.DISABLE_MODEL_ANNOS | CeylonTransformer.DISABLE_USER_ANNOS;
        }
        if(CodegenUtil.hasCompilerAnnotation(decl, "nomodel"))
            gen().disableAnnotations = CeylonTransformer.DISABLE_MODEL_ANNOS;
        if(CodegenUtil.hasCompilerAnnotation(decl, "erroneous")) {
            String message = CodegenUtil.getCompilerAnnotationArgument(decl, "erroneous");
            result.append(gen().makeErroneous(decl, message));
        }
        return old;
    }

    void resetCompilerAnnotations(int value){
        gen().disableAnnotations = value;
    }

    private List<JCAnnotation> makeModelAnnotation(Type annotationType, List<JCExpression> annotationArgs) {
        if ((gen().disableAnnotations & CeylonTransformer.DISABLE_MODEL_ANNOS) != 0)
            return List.nil();
        return List.of(make().Annotation(makeIdent(annotationType), annotationArgs));
    }
    
    private List<JCAnnotation> makeAnnoAnnotation(Type annotationType, List<JCExpression> annotationArgs) {
        return List.of(make().Annotation(makeIdent(annotationType), annotationArgs));
    }

    private List<JCAnnotation> makeModelAnnotation(Type annotationType) {
        return makeModelAnnotation(annotationType, List.<JCExpression>nil());
    }

    List<JCAnnotation> makeAtCeylon() {
        JCExpression majorAttribute = make().Assign(naming.makeUnquotedIdent("major"), make().Literal(Versions.JVM_BINARY_MAJOR_VERSION));
        List<JCExpression> annotationArgs;
        if(Versions.JVM_BINARY_MINOR_VERSION != 0){
            JCExpression minorAttribute = make().Assign(naming.makeUnquotedIdent("minor"), make().Literal(Versions.JVM_BINARY_MINOR_VERSION));
            annotationArgs = List.<JCExpression>of(majorAttribute, minorAttribute);
        }else{
            // keep the minor implicit value of 0 to reduce bytecode size
            annotationArgs = List.<JCExpression>of(majorAttribute);
        }
        return makeModelAnnotation(syms().ceylonAtCeylonType, annotationArgs);
    }

    /** Returns a ListBuffer with assignment expressions for the doc, license and by arguments, as well as name,
     * to be used in an annotation which requires them (such as Module and Package) */
    ListBuffer<JCExpression> getLicenseAuthorsDocAnnotationArguments(String name, java.util.List<Annotation> anns) {
        ListBuffer<JCExpression> authors = new ListBuffer<JCTree.JCExpression>();
        ListBuffer<JCExpression> res = new ListBuffer<JCExpression>();
        res.add(make().Assign(naming.makeUnquotedIdent("name"), make().Literal(name)));
        for (Annotation a : anns) {
            if (a.getPositionalArguments() != null && !a.getPositionalArguments().isEmpty()) {
                if (a.getName().equals("doc")) {
                    res.add(make().Assign(naming.makeUnquotedIdent("doc"),
                            make().Literal(a.getPositionalArguments().get(0))));
                } else if (a.getName().equals("license")) {
                    res.add(make().Assign(naming.makeUnquotedIdent("license"),
                            make().Literal(a.getPositionalArguments().get(0))));
                } else if (a.getName().equals("by")) {
                    for (String author : a.getPositionalArguments()) {
                        authors.add(make().Literal(author));
                    }
                }
            }
        }
        if (!authors.isEmpty()) {
            res.add(make().Assign(naming.makeUnquotedIdent("by"), make().NewArray(null, null, authors.toList())));
        }
        return res;
    }

    List<JCAnnotation> makeAtModule(Module module) {
        ListBuffer<JCExpression> imports = new ListBuffer<JCTree.JCExpression>();
        for(ModuleImport dependency : module.getImports()){
            Module dependencyModule = dependency.getModule();
            // do not include the implicit language module as a dependency
            if(dependencyModule.getNameAsString().equals("ceylon.language"))
                continue;
            JCExpression dependencyName = make().Assign(naming.makeUnquotedIdent("name"),
                    make().Literal(dependencyModule.getNameAsString()));
            JCExpression dependencyVersion = null;
            if(dependencyModule.getVersion() != null)
                dependencyVersion = make().Assign(naming.makeUnquotedIdent("version"),
                        make().Literal(dependencyModule.getVersion()));
            
            List<JCExpression> spec;
            if(dependencyVersion != null)
                spec = List.<JCExpression>of(dependencyName, dependencyVersion);
            else
                spec = List.<JCExpression>of(dependencyName);
            
            if (Util.getAnnotation(dependency, "shared") != null) {
                JCExpression exported = make().Assign(naming.makeUnquotedIdent("export"), make().Literal(true));
                spec = spec.append(exported);
            }
            
            if (Util.getAnnotation(dependency, "optional") != null) {
                JCExpression exported = make().Assign(naming.makeUnquotedIdent("optional"), make().Literal(true));
                spec = spec.append(exported);
            }
            
            JCAnnotation atImport = make().Annotation(makeIdent(syms().ceylonAtImportType), spec);
            imports.add(atImport);
        }

        ListBuffer<JCExpression> annotationArgs = getLicenseAuthorsDocAnnotationArguments(
                module.getNameAsString(), module.getAnnotations());
        annotationArgs.add(make().Assign(naming.makeUnquotedIdent("version"), make().Literal(module.getVersion())));
        annotationArgs.add(make().Assign(naming.makeUnquotedIdent("dependencies"),
                make().NewArray(null, null, imports.toList())));
        return makeModelAnnotation(syms().ceylonAtModuleType, annotationArgs.toList());
    }

    List<JCAnnotation> makeAtPackage(Package pkg) {
        ListBuffer<JCExpression> annotationArgs = getLicenseAuthorsDocAnnotationArguments(
                pkg.getNameAsString(), pkg.getAnnotations());
        annotationArgs.add(make().Assign(naming.makeUnquotedIdent("shared"), makeBoolean(pkg.isShared())));
        return makeModelAnnotation(syms().ceylonAtPackageType, annotationArgs.toList());
    }

    List<JCAnnotation> makeAtName(String name) {
        return makeModelAnnotation(syms().ceylonAtNameType, List.<JCExpression>of(make().Literal(name)));
    }

    List<JCAnnotation> makeAtAlias(ProducedType type) {
        String name = serialiseTypeSignature(type);
        return makeModelAnnotation(syms().ceylonAtAliasType, List.<JCExpression>of(make().Literal(name)));
    }

    List<JCAnnotation> makeAtTypeAlias(ProducedType type) {
        String name = serialiseTypeSignature(type);
        return makeModelAnnotation(syms().ceylonAtTypeAliasType, List.<JCExpression>of(make().Literal(name)));
    }

    final JCAnnotation makeAtTypeParameter(String name, java.util.List<ProducedType> satisfiedTypes, java.util.List<ProducedType> caseTypes, 
                                           boolean covariant, boolean contravariant, ProducedType defaultValue) {
        ListBuffer<JCExpression> attributes = new ListBuffer<JCExpression>();
        
        // name
        attributes.add(make().Assign(naming.makeUnquotedIdent("value"), make().Literal(name)));

        // variance
        String variance = "NONE";
        if(covariant)
            variance = "OUT";
        else if(contravariant)
            variance = "IN";
        JCExpression varianceAttribute = make().Assign(naming.makeUnquotedIdent("variance"), 
                make().Select(makeIdent(syms().ceylonVarianceType), names().fromString(variance)));
        attributes.add(varianceAttribute);
        
        // upper bounds
        ListBuffer<JCExpression> upperBounds = new ListBuffer<JCTree.JCExpression>();
        for(ProducedType satisfiedType : satisfiedTypes){
            String type = serialiseTypeSignature(satisfiedType);
            upperBounds.append(make().Literal(type));
        }
        JCExpression satisfiesAttribute = make().Assign(naming.makeUnquotedIdent("satisfies"), 
                make().NewArray(null, null, upperBounds.toList()));
        attributes.add(satisfiesAttribute);
        
        // case types
        ListBuffer<JCExpression> caseTypesExpressions = new ListBuffer<JCTree.JCExpression>();
        if(caseTypes != null){
            for(ProducedType caseType : caseTypes){
                String type = serialiseTypeSignature(caseType);
                caseTypesExpressions.append(make().Literal(type));
            }
        }
        JCExpression caseTypeAttribute = make().Assign(naming.makeUnquotedIdent("caseTypes"), 
                make().NewArray(null, null, caseTypesExpressions.toList()));
        attributes.add(caseTypeAttribute);
        
        if(defaultValue != null){
            attributes.add(make().Assign(naming.makeUnquotedIdent("defaultValue"), make().Literal(serialiseTypeSignature(defaultValue))));
        }
        
        // all done
        return make().Annotation(makeIdent(syms().ceylonAtTypeParameter), attributes.toList());
    }

    List<JCAnnotation> makeAtTypeParameters(List<JCExpression> typeParameters) {
        JCExpression value = make().NewArray(null, null, typeParameters);
        return makeModelAnnotation(syms().ceylonAtTypeParameters, List.of(value));
    }

    List<JCAnnotation> makeAtSequenced() {
        return makeModelAnnotation(syms().ceylonAtSequencedType);
    }
    
    List<JCAnnotation> makeAtFunctionalParameter() {
        return makeModelAnnotation(syms().ceylonAtFunctionalParameterType);
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
        JCExpression extendsValue = null;
        if (extendedType == null) {
            extendsValue = make().Literal("");
        } else if (!extendedType.isExactly(typeFact.getBasicDeclaration().getType())){
            extendsValue = make().Literal(serialiseTypeSignature(extendedType));
        }
        if (extendsValue != null) {
            JCExpression extendsAttribute = make().Assign(naming.makeUnquotedIdent("extendsType"), extendsValue);
            attributes = attributes.prepend(extendsAttribute);
        }
        return makeModelAnnotation(syms().ceylonAtClassType, attributes);
    }

    List<JCAnnotation> makeAtSatisfiedTypes(java.util.List<ProducedType> satisfiedTypes) {
        JCExpression attrib = makeTypesListAttr(satisfiedTypes);
        if (attrib != null) {
            return makeModelAnnotation(syms().ceylonAtSatisfiedTypes, List.of(attrib));
        } else {
            return List.nil();
        }
    }

    List<JCAnnotation> makeAtCaseTypes(java.util.List<ProducedType> caseTypes, ProducedType ofType) {
        List<JCExpression> attribs = List.nil();
        if (ofType != null) {
            JCExpression ofAttr = makeOfTypeAttr(ofType);
            attribs = attribs.append(ofAttr);
        } else {
            if (caseTypes != null && !caseTypes.isEmpty()) {
                JCExpression casesAttr = makeTypesListAttr(caseTypes);
                attribs = attribs.append(casesAttr);
            }
        }
        if (!attribs.isEmpty()) {
            return makeModelAnnotation(syms().ceylonAtCaseTypes, attribs);
        } else {
            return List.nil();
        }
    }

    private JCExpression makeTypesListAttr(java.util.List<ProducedType> types) {
        if(types.isEmpty())
            return null;
        ListBuffer<JCExpression> upperBounds = new ListBuffer<JCTree.JCExpression>();
        for(ProducedType type : types){
            String typeSig = serialiseTypeSignature(type);
            upperBounds.append(make().Literal(typeSig));
        }
        JCExpression caseAttribute = make().Assign(naming.makeUnquotedIdent("value"), 
                make().NewArray(null, null, upperBounds.toList()));
        return caseAttribute;
    }

    private JCExpression makeOfTypeAttr(ProducedType ofType) {
        if(ofType == null)
            return null;
        String typeSig = serialiseTypeSignature(ofType);
        JCExpression ofAttribute = make().Assign(naming.makeUnquotedIdent("of"), 
                make().Literal(typeSig));
        
        return ofAttribute;
    }

    List<JCAnnotation> makeAtIgnore() {
        return makeModelAnnotation(syms().ceylonAtIgnore);
    }

    List<JCAnnotation> makeAtTransient() {
        return makeModelAnnotation(syms().ceylonAtTransientType);
    }

    List<JCAnnotation> makeAtAnnotations(java.util.List<Annotation> annotations) {
        if(annotations == null || annotations.isEmpty())
            return List.nil();
        ListBuffer<JCExpression> array = new ListBuffer<JCTree.JCExpression>();
        for(Annotation annotation : annotations){
            array.append(makeAtAnnotation(annotation));
        }
        JCExpression annotationsAttribute = make().Assign(naming.makeUnquotedIdent("value"), 
                make().NewArray(null, null, array.toList()));
        
        return makeModelAnnotation(syms().ceylonAtAnnotationsType, List.of(annotationsAttribute));
    }

    private JCExpression makeAtAnnotation(Annotation annotation) {
        JCExpression valueAttribute = make().Assign(naming.makeUnquotedIdent("value"), 
                                                    make().Literal(annotation.getName()));
        List<JCExpression> attributes;
        if(!annotation.getPositionalArguments().isEmpty()){
            java.util.List<String> positionalArguments = annotation.getPositionalArguments();
            ListBuffer<JCExpression> array = new ListBuffer<JCTree.JCExpression>();
            for(String val : positionalArguments)
                array.add(make().Literal(val));
            JCExpression argumentsAttribute = make().Assign(naming.makeUnquotedIdent("arguments"), 
                                                            make().NewArray(null, null, array.toList()));
            attributes = List.of(valueAttribute, argumentsAttribute);
        }else if(!annotation.getNamedArguments().isEmpty()){
            Map<String, String> namedArguments = annotation.getNamedArguments();
            ListBuffer<JCExpression> array = new ListBuffer<JCTree.JCExpression>();
            for(Entry<String, String> entry : namedArguments.entrySet()){
                JCExpression argNameAttribute = make().Assign(naming.makeUnquotedIdent("name"), 
                        make().Literal(entry.getKey()));
                JCExpression argValueAttribute = make().Assign(naming.makeUnquotedIdent("value"), 
                        make().Literal(entry.getValue()));

                JCAnnotation namedArg = make().Annotation(makeIdent(syms().ceylonAtNamedArgumentType), 
                                                          List.of(argNameAttribute, argValueAttribute));
                array.add(namedArg);
            }
            JCExpression argumentsAttribute = make().Assign(naming.makeUnquotedIdent("namedArguments"), 
                    make().NewArray(null, null, array.toList()));
            attributes = List.of(valueAttribute, argumentsAttribute);
        }else
            attributes = List.of(valueAttribute);

        return make().Annotation(makeIdent(syms().ceylonAtAnnotationType), attributes);
    }

    List<JCAnnotation> makeAtContainer(ProducedType type) {
        JCExpression classAttribute = make().Assign(naming.makeUnquotedIdent("klass"), 
                                                    makeClassLiteral(type));
        List<JCExpression> attributes = List.of(classAttribute);

        return makeModelAnnotation(syms().ceylonAtContainerType, attributes);
    }

    List<JCAnnotation> makeAtLocalContainer() {
        return makeModelAnnotation(syms().ceylonAtLocalContainerType);
    }

    JCAnnotation makeAtMember(ProducedType type) {
        JCExpression classAttribute = make().Assign(naming.makeUnquotedIdent("klass"), 
                                                    makeClassLiteral(type));
        List<JCExpression> attributes = List.of(classAttribute);

        return make().Annotation(makeIdent(syms().ceylonAtMemberType), attributes);
    }

    List<JCAnnotation> makeAtMembers(List<JCExpression> members) {
        if(members.isEmpty())
            return List.nil();
        JCExpression attr = make().Assign(naming.makeUnquotedIdent("value"), 
                                          make().NewArray(null, null, members));

        return makeModelAnnotation(syms().ceylonAtMembersType, List.of(attr));
    }
    
    private List<JCAnnotation> makeAtAnnotationValue(Type annotationType, String name, JCExpression values) {
        if (name == null) {
            return makeAnnoAnnotation(annotationType, List.<JCExpression>of(values));
        } else {
            return makeAnnoAnnotation(annotationType, List.<JCExpression>of(
                    make().Assign(naming.makeUnquotedIdent("name"), make().Literal(name)),
                    make().Assign(naming.makeUnquotedIdent("value"), values)));
        }
    }
    
    private List<JCAnnotation> makeAtAnnotationExprs(Type annotationType, List<JCExpression> value) {
        return makeAnnoAnnotation(annotationType, value);
    }
    
    List<JCAnnotation> makeAtObjectValue(String name, JCExpression values) {
        return makeAtAnnotationValue(syms().ceylonAtObjectValueType, name, values);
    }
    List<JCAnnotation> makeAtObjectExprs(JCExpression values) {
        return makeAtAnnotationExprs(syms().ceylonAtObjectExprsType, List.<JCExpression>of(values));
    }
    
    List<JCAnnotation> makeAtStringValue(String name, JCExpression values) {
        return makeAtAnnotationValue(syms().ceylonAtStringValueType, name, values);
    }
    List<JCAnnotation> makeAtStringExprs(JCExpression values) {
        return makeAtAnnotationExprs(syms().ceylonAtStringExprsType, List.<JCExpression>of(values));
    }
    
    List<JCAnnotation> makeAtCharacterValue(String name, JCExpression values) {
        return makeAtAnnotationValue(syms().ceylonAtCharacterValueType, name, values);
    }
    List<JCAnnotation> makeAtCharacterExprs(JCExpression values) {
        return makeAtAnnotationExprs(syms().ceylonAtCharacterExprsType, List.<JCExpression>of(values));
    }
    
    List<JCAnnotation> makeAtBooleanValue(String name, JCExpression value) {
        return makeAtAnnotationValue(syms().ceylonAtBooleanValueType, name, value);
    }
    List<JCAnnotation> makeAtBooleanExprs(JCExpression value) {
        return makeAtAnnotationExprs(syms().ceylonAtBooleanExprsType, List.<JCExpression>of(value));
    }
    
    List<JCAnnotation> makeAtFloatValue(String name, JCExpression value) {
        return makeAtAnnotationValue(syms().ceylonAtFloatValueType, name, value);
    }
    List<JCAnnotation> makeAtFloatExprs(JCExpression value) {
        return makeAtAnnotationExprs(syms().ceylonAtFloatExprsType, List.<JCExpression>of(value));
    }
    
    List<JCAnnotation> makeAtIntegerValue(String name, JCExpression value) {
        return makeAtAnnotationValue(syms().ceylonAtIntegerValueType, name, value);
    }
    List<JCAnnotation> makeAtIntegerExprs(JCExpression value) {
        return makeAtAnnotationExprs(syms().ceylonAtIntegerExprsType, List.<JCExpression>of(value));
    }
    
    List<JCAnnotation> makeAtDeclarationValue(String name, JCExpression value) {
        return makeAtAnnotationValue(syms().ceylonAtDeclarationValueType, name, value);
    }
    List<JCAnnotation> makeAtDeclarationExprs(JCExpression value) {
        return makeAtAnnotationExprs(syms().ceylonAtDeclarationExprsType, List.<JCExpression>of(value));
    }
    
    List<JCAnnotation> makeAtParameterValue(JCExpression value) {
        return makeAnnoAnnotation(syms().ceylonAtParameterValueType, List.<JCExpression>of(value));
    }

    /** Determine whether the given declaration requires a 
     * {@code @TypeInfo} annotation 
     */
    private boolean needsJavaTypeAnnotations(Declaration decl) {
        Declaration reqdecl = decl;
        if (reqdecl instanceof MethodOrValue
                && ((MethodOrValue)reqdecl).isParameter()) {
            reqdecl = CodegenUtil.getParameterized(((MethodOrValue)reqdecl));
        }
        if (reqdecl instanceof TypeDeclaration) {
            return true;
        } else { // TypedDeclaration
            return !Decl.isLocal(reqdecl);
        }
    }

    List<JCTree.JCAnnotation> makeJavaTypeAnnotations(TypedDeclaration decl) {
        return makeJavaTypeAnnotations(decl, true);
    }
    
    List<JCTree.JCAnnotation> makeJavaTypeAnnotations(TypedDeclaration decl, boolean handleFunctionalParameter) {
        if(decl == null || decl.getType() == null)
            return List.nil();
        ProducedType type;
        if (decl instanceof Method && ((Method)decl).isParameter() && handleFunctionalParameter) {
            type = getTypeForFunctionalParameter((Method)decl);
        } else if (decl instanceof Functional && Decl.isMpl((Functional)decl)) {
            type = getReturnTypeOfCallable(decl.getProducedTypedReference(null, Collections.<ProducedType>emptyList()).getFullType());
        } else {
            type = decl.getType();
        }
        return makeJavaTypeAnnotations(type, needsJavaTypeAnnotations(decl));
    }

    private List<JCTree.JCAnnotation> makeJavaTypeAnnotations(ProducedType type, boolean required) {
        if (!required)
            return List.nil();
        String name = serialiseTypeSignature(type);
        boolean erased = hasErasure(type);
        // Add the original type to the annotations
        if (!erased) {
            return makeModelAnnotation(syms().ceylonAtTypeInfoType, List.<JCExpression>of(make().Literal(name)));
        } 
        return makeModelAnnotation(syms().ceylonAtTypeInfoType, List.<JCExpression>of(
                make().Assign(naming.makeUnquotedIdent("value"), make().Literal(name)),
                make().Assign(naming.makeUnquotedIdent("erased"), make().Literal(erased))));
    }
    
    private String serialiseTypeSignature(ProducedType type){
        // resolve aliases
        type = type.resolveAliases();
        return type.getProducedTypeQualifiedName();

    }
    
    /*
     * Boxing
     */
    public enum BoxingStrategy {
        UNBOXED, BOXED, INDIFFERENT;
    }

    public boolean canUnbox(ProducedType type){
        // all the rest is boxed
        return isCeylonBasicType(type) || isJavaString(type);
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
        if(type == null)
            return false;
        if (typeFact().isOptionalType(type)) {
            type = type.eliminateNull();
        } 
        return type.getDeclaration() instanceof TypeParameter;
    }
    
    JCExpression unboxType(JCExpression expr, ProducedType exprType) {
        if (isCeylonInteger(exprType)) {
            expr = unboxInteger(expr);
        } else if (isCeylonFloat(exprType)) {
            expr = unboxFloat(expr);
        } else if (isCeylonString(exprType)) {
            expr = unboxString(expr);
        } else if (isCeylonCharacter(exprType)) {
            boolean isJavaCharacter = exprType.getUnderlyingType() != null;
            expr = unboxCharacter(expr, isJavaCharacter);
        } else if (isCeylonBoolean(exprType)) {
            expr = unboxBoolean(expr);
        } else if (isOptional(exprType)) {
            exprType = typeFact().getDefiniteType(exprType);
            if (isCeylonString(exprType)){
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
        } else if (isAnything(exprType)) {
            expr = make().LetExpr(List.<JCStatement>of(make().Exec(expr)), makeNull());
        } else if (isOptional(exprType)) {
            // sometimes, due to interop we will get an unboxed java.lang.String whose Ceylon type
            // is String? or passes for a boxed thing, and if we need to box it well we do
            exprType = typeFact().getDefiniteType(exprType);
            if (isCeylonString(exprType)){
                expr = boxOptionalJavaString(expr);
            }
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
        Naming.SyntheticName name = naming.temp();
        JCExpression type = makeJavaType(typeFact().getStringDeclaration().getType(), JT_NO_PRIMITIVES);
        JCExpression expr = make().Conditional(make().Binary(JCTree.NE, name.makeIdent(), makeNull()), 
                unboxString(name.makeIdent()),
                makeNull());
        return makeLetExpr(name, null, type, value, expr);
    }

    private JCExpression boxOptionalJavaString(JCExpression value){
        Naming.SyntheticName name = naming.temp();
        JCExpression type = makeJavaType(typeFact().getStringDeclaration().getType());
        JCExpression expr = make().Conditional(make().Binary(JCTree.NE, name.makeIdent(), makeNull()), 
                boxString(name.makeIdent()),
                makeNull());
        return makeLetExpr(name, null, type, value, expr);
    }

    private JCTree.JCMethodInvocation unboxCharacter(JCExpression value, boolean isJava) {
        return makeUnboxType(value, isJava ? "charValue" : "intValue");
    }
    
    private JCTree.JCMethodInvocation unboxBoolean(JCExpression value) {
        return makeUnboxType(value, "booleanValue");
    }
    
    private JCTree.JCMethodInvocation makeUnboxType(JCExpression value, String unboxMethodName) {
        return make().Apply(null, makeSelect(value, unboxMethodName), List.<JCExpression>nil());
    }

    /*
     * Sequences
     */
    
    /**
     * Turns a <tt>ceylon.language.Iterable</tt> to a <tt>ceylon.language.Sequential</tt> by invoking 
     * its <tt>getSequence()</tt> method.
     */
    JCExpression iterableToSequential(JCExpression iterable){
        return make().Apply(null, makeSelect(iterable, "getSequence"), List.<JCExpression>nil());
    }

    /**
     * Casts a <tt>ceylon.language.Sequential</tt> type to a <tt>ceylon.language.Sequence</tt> type.
     */
    JCExpression castSequentialToSequence(JCExpression sequentialExpr, ProducedType iteratedType) {
        return make().TypeCast(
                makeJavaType(typeFact().getSequenceType(iteratedType)), sequentialExpr);
    }

    /**
     * Returns a JCExpression along the lines of 
     * {@code new ArraySequence<seqElemType>(list...)}
     * @param elems The elements in the sequence
     * @param seqElemType The sequence type parameter
     * @param makeJavaTypeOpts The option flags to pass to makeJavaType().
     * @return a JCExpression
     * @see #makeSequenceRaw(java.util.List)
     */
    JCExpression makeSequence(List<JCExpression> elems, ProducedType seqElemType, int makeJavaTypeOpts) {
        return make().Apply(List.<JCExpression>of(makeJavaType(seqElemType, JT_TYPE_ARGUMENT)), 
                naming.makeQualIdent(makeJavaType(typeFact().getArraySequenceType(seqElemType), JT_RAW), "instance"), 
                List.<JCExpression>of(makeReifiedTypeArgument(seqElemType),
                make().NewArray(make().Type(syms.objectType), List.<JCExpression>nil(), elems)));
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
        if (list.size() != 1 || !isNull(list.get(0).getTypeModel())) {
            for (Expression expr : list) {
                elems.append(expressionGen().transformExpression(expr, BoxingStrategy.BOXED, seqElemType));
            }
        } else {
            // Resolve the ambiguous situation of being passed a single "null" argument
            elems.append(make().TypeCast(syms().objectType, expressionGen().transformExpression(list.get(0))));
        }
        return makeSequence(elems.toList(), seqElemType, CeylonTransformer.JT_CLASS_NEW);
    }

    /**
     * Makes an iterable literal, for a sequenced argument
     */
    JCExpression makeIterable(Tree.SequencedArgument sequencedArgument, ProducedType seqElemType, int flags) {
        ListBuffer<JCExpression> elems = new ListBuffer<JCExpression>();
        java.util.List<PositionalArgument> list = sequencedArgument.getPositionalArguments();
        int i = list.size();
        boolean spread = false;
        for (Tree.PositionalArgument arg : list) {
            at(arg);
            i--;
            JCExpression jcExpression;
            // last expression can be an Iterable<seqElemType>
            if(arg instanceof Tree.SpreadArgument || arg instanceof Tree.Comprehension){
                // make sure we only have spread/comprehension as last
                if(i != 0){
                    jcExpression = makeErroneous(arg, "compiler bug: spread or comprehension argument is not last in sequence literal");
                }else{
                    ProducedType type = typeFact().getIterableType(seqElemType);
                    spread = true;
                    if(arg instanceof Tree.SpreadArgument){
                        Tree.Expression expr = ((Tree.SpreadArgument) arg).getExpression();
                        // always boxed since it is a sequence
                        jcExpression = expressionGen().transformExpression(expr, BoxingStrategy.BOXED, type);
                    }else{
                        jcExpression = expressionGen().transformComprehension((Comprehension) arg, type);
                    }
                }
            }else if(arg instanceof Tree.ListedArgument){
                Tree.Expression expr = ((Tree.ListedArgument) arg).getExpression();
                // always boxed since we stuff them into a sequence
                jcExpression = expressionGen().transformExpression(expr, BoxingStrategy.BOXED, seqElemType);
            }else{
                jcExpression = makeErroneous(arg, "compiler bug: " + arg.getNodeType() + " is not a supported sequenced argument");
            }
            // the last iterable goes first if spread
            if(i == 0 && spread)
                elems.prepend(jcExpression);
            else
                elems.append(jcExpression);
        }
        // small optimisation if we have only a single element
        if(elems.size() == 1 && spread)
            return elems.first();
        at(sequencedArgument);
        if(spread)
            return makeIterable(elems.toList(), seqElemType, flags | JT_NO_PRIMITIVES);
        else
            return makeSequence(elems.toList(), seqElemType, flags | JT_NO_PRIMITIVES);
    }

    /**
     * Makes an iterable literal, where the first element of elems is an Iterable, and the rest are the start of the
     * iterable.
     */
    JCExpression makeIterable(List<JCExpression> elems, ProducedType seqElemType, int makeJavaTypeOpts) {
        JCExpression elemTypeExpr = makeJavaType(seqElemType, makeJavaTypeOpts);
        elems = elems.prepend(makeReifiedTypeArgument(seqElemType));
        // we delegate to ArrayIterable.instance() so that we can filter out empty Iterables
        return make().Apply(List.<JCExpression>of(elemTypeExpr), makeSelect(makeIdent(syms().ceylonArrayIterableType), "instance"), elems);
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
        return makeSequence(elems, typeFact().getObjectDeclaration().getType(), CeylonTransformer.JT_RAW);
    }
    
    JCExpression makeEmptyAsSequential(boolean needsCast){
        if(needsCast)
            return make().TypeCast(makeJavaType(typeFact().getSequentialDeclaration().getType(), JT_RAW), makeEmpty());
        return makeEmpty();
    }
    
    JCExpression makeEmpty() {
        return make().Apply(
                List.<JCTree.JCExpression>nil(),
                naming.makeLanguageValue("empty"),
                List.<JCTree.JCExpression>nil());
    }
    
    JCExpression makeFinished() {
        return make().Apply(
                List.<JCTree.JCExpression>nil(),
                naming.makeLanguageValue("finished"),
                List.<JCTree.JCExpression>nil());
    }

    /**
     * Turns a sequence into a Java array
     * @param expr the sequence
     * @param sequenceType the (destination) sequence type
     * @param boxingStrategy the boxing strategy for expr
     * @param exprType the (source) expression type
     * @param initialElements the elements to place at the beginning of the Java array
     */
    JCExpression sequenceToJavaArray(JCExpression expr, ProducedType sequenceType, 
                                     BoxingStrategy boxingStrategy, ProducedType exprType,
                                     List<JCTree.JCExpression> initialElements) {
        String methodName = null;
        // find the sequence element type
        ProducedType type = typeFact().getIteratedType(sequenceType);
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
                methodName = "toBooleanArray";
            } else if (isJavaString(type)) {
                methodName = "toJavaStringArray";
            } else if (isCeylonString(type)) {
                return objectVariadicToJavaArray(type, sequenceType, expr, exprType, initialElements);
            }
            if(methodName == null){
                return objectVariadicToJavaArray(type, sequenceType, expr, exprType, initialElements);
            }
            return makeUtilInvocation(methodName, initialElements.prepend(expr), null);
        }else{
            return objectVariadicToJavaArray(type, sequenceType, expr, exprType, initialElements);
        }
    }

    private JCExpression objectVariadicToJavaArray(ProducedType type,
            ProducedType sequenceType, JCExpression expr, ProducedType exprType, List<JCExpression> initialElements) {
        if(typeFact().getSequentialType(exprType) != null){
            return objectSequentialToJavaArray(type, expr, initialElements);
        }
        return objectIterableToJavaArray(type, typeFact().getIterableType(sequenceType), expr, initialElements);
    }

    // This can't be reached anymore since we can't spread iterables anymore ATM
    private JCExpression objectIterableToJavaArray(ProducedType type,
            ProducedType iterableType, JCExpression expr, List<JCExpression> initialElements) {
        JCExpression klass = makeJavaType(type, JT_CLASS_NEW | JT_NO_PRIMITIVES);
        JCExpression klassLiteral = make().Select(klass, names().fromString("class"));
        return makeUtilInvocation("toArray", initialElements.prependList(List.of(expr, klassLiteral)), null);
    }
    
    private JCExpression objectSequentialToJavaArray(ProducedType type, JCExpression expr, List<JCExpression> initialElements) {
        JCExpression klass1 = makeJavaType(type, JT_RAW | JT_NO_PRIMITIVES);
        JCExpression klass2 = makeJavaType(type, JT_CLASS_NEW | JT_NO_PRIMITIVES);
        Naming.SyntheticName seqName = naming.temp().suffixedBy(0);

        ProducedType fixedSizedType = typeFact().getSequentialDeclaration().getProducedType(null, Arrays.asList(type));
        JCExpression seqTypeExpr1 = makeJavaType(fixedSizedType);
        //JCExpression seqTypeExpr2 = makeJavaType(fixedSizedType);

        JCExpression sizeExpr = make().Apply(List.<JCExpression>nil(), 
                make().Select(seqName.makeIdent(), names().fromString("getSize")),
                List.<JCExpression>nil());
        sizeExpr = make().TypeCast(syms().intType, sizeExpr);
        
        // add initial elements if required
        if(!initialElements.isEmpty())
            sizeExpr = make().Binary(JCTree.PLUS, 
                                     sizeExpr,
                                     makeInteger(initialElements.size()));

        JCExpression newArrayExpr = make().NewArray(klass1, List.of(sizeExpr), null);
        JCExpression sequenceToArrayExpr = makeUtilInvocation("toArray",
                initialElements.prependList(List.of(seqName.makeIdent(), newArrayExpr)),
                List.of(klass2));
        
        // since T[] is erased to Sequential<T> we probably need a cast to FixedSized<T>
        //JCExpression castedExpr = make().TypeCast(seqTypeExpr2, expr);
        
        return makeLetExpr(seqName, List.<JCStatement>nil(), seqTypeExpr1, expr, sequenceToArrayExpr);
    }

    // Creates comparisons of expressions against types
    JCExpression makeTypeTest(JCExpression firstTimeExpr, Naming.CName varName, ProducedType testedType, ProducedType expressionType) {
        JCExpression result = null;
        // make sure aliases are resolved
        testedType = testedType.resolveAliases();
        // optimisation when all we're doing is making sure it is not null
        if(expressionType != null
                && testedType.getSupertype(typeFact().getObjectDeclaration()) != null
                && expressionType.isExactly(typeFact().getOptionalType(testedType))){
            JCExpression varExpr = firstTimeExpr != null ? firstTimeExpr : varName.makeIdent();
            return make().Binary(JCTree.NE, varExpr, makeNull());
        }
        if (typeFact().isUnion(testedType)) {
            UnionType union = (UnionType)testedType.getDeclaration();
            for (ProducedType pt : union.getCaseTypes()) {
                JCExpression partExpr = makeTypeTest(firstTimeExpr, varName, pt, expressionType);
                firstTimeExpr = null;
                if (result == null) {
                    result = partExpr;
                } else {
                    result = make().Binary(JCTree.OR, result, partExpr);
                }
            }
        } else if (typeFact().isIntersection(testedType)) {
            IntersectionType union = (IntersectionType)testedType.getDeclaration();
            for (ProducedType pt : union.getSatisfiedTypes()) {
                JCExpression partExpr = makeTypeTest(firstTimeExpr, varName, pt, expressionType);
                firstTimeExpr = null;
                if (result == null) {
                    result = partExpr;
                } else {
                    result = make().Binary(JCTree.AND, result, partExpr);
                }
            }
        } else {
            JCExpression varExpr = firstTimeExpr != null ? firstTimeExpr : varName.makeIdent();
            if (isAnything(testedType)){
                // everything is Void, it's the root of the hierarchy
                return makeIgnoredEvalAndReturn(varExpr, makeBoolean(true));
            } else if (testedType.isExactly(typeFact().getNullDeclaration().getType())){
                // is Null => is null
                return make().Binary(JCTree.EQ, varExpr, makeNull());
            } else if (testedType.isExactly(typeFact().getObjectDeclaration().getType())){
                // is Object => is not null
                return make().Binary(JCTree.NE, varExpr, makeNull());
            } else if (testedType.isExactly(typeFact().getIdentifiableDeclaration().getType())){
                // it's erased
                return makeUtilInvocation("isIdentifiable", List.of(varExpr), null);
            } else if (testedType.isExactly(typeFact().getBasicDeclaration().getType())){
                // it's erased
                return makeUtilInvocation("isBasic", List.of(varExpr), null);
            } else if (testedType.getDeclaration() instanceof NothingType){
                // nothing is Bottom
                return makeIgnoredEvalAndReturn(varExpr, makeBoolean(false));
            } else if ((!testedType.getTypeArguments().isEmpty() || isTypeParameter(testedType))
                        && !canOptimiseReifiedTypeTest(testedType)){
                // requires a little magic
                return makeUtilInvocation("isReified", List.of(varExpr, makeReifiedTypeArgument(testedType)), null);
            } else {
                JCExpression rawTypeExpr = makeJavaType(testedType, JT_NO_PRIMITIVES | JT_RAW);
                result = make().TypeTest(varExpr, rawTypeExpr);
            }
        }
        return result;
    }

    private boolean canOptimiseReifiedTypeTest(ProducedType type) {
        // we can optimise it if we've got a ClassOrInterface with only Anything type parameters
        if(type.getDeclaration() instanceof ClassOrInterface == false)
            return false;
        for(Entry<TypeParameter, ProducedType> ta : type.getTypeArguments().entrySet()){
            if(!ta.getKey().isCovariant() || !isAnything(ta.getValue()))
                return false;
        }
        // they're all void we can optimise
        return true;
    }

    JCExpression makeNonEmptyTest(JCExpression firstTimeExpr) {
        Interface sequence = typeFact().getSequenceDeclaration();
        JCExpression sequenceType = makeJavaType(sequence.getType(), JT_NO_PRIMITIVES | JT_RAW);
        return make().TypeTest(firstTimeExpr, sequenceType);
    }
    
    /**
     * Invokes a static method of the Util helper class
     * @param methodName name of the method
     * @param arguments The arguments to the invocation
     * @param typeArguments The arguments to the method
     * @return the invocation AST
     */
    public JCExpression makeUtilInvocation(String methodName, List<JCExpression> arguments, List<JCExpression> typeArguments) {
        return make().Apply(typeArguments, 
                            make().Select(make().QualIdent(syms().ceylonUtilType.tsym), 
                                          names().fromString(methodName)), 
                            arguments);
    }

    /**
     * Invokes a static method of the Metamodel helper class
     * @param methodName name of the method
     * @param arguments The arguments to the invocation
     * @param typeArguments The arguments to the method
     * @return the invocation AST
     */
    public JCExpression makeMetamodelInvocation(String methodName, List<JCExpression> arguments, List<JCExpression> typeArguments) {
        return make().Apply(typeArguments, 
                            make().Select(make().QualIdent(syms().ceylonMetamodelType.tsym), 
                                          names().fromString(methodName)), 
                            arguments);
    }

    public JCExpression makeTypeDescriptorType(){
        return makeJavaType(syms().ceylonTypeDescriptorType.tsym);
    }

    public JCExpression makeReifiedTypeType(){
        return makeJavaType(syms().ceylonReifiedTypeType.tsym);
    }
    
    public JCExpression makeNothingTypeDescriptor() {
        return make().Select(makeTypeDescriptorType(), 
                names().fromString("NothingType"));

    }

    private LetExpr makeIgnoredEvalAndReturn(JCExpression toEval, JCExpression toReturn){
        // define a variable of type j.l.Object to hold the result of the evaluation
        JCVariableDecl def = makeVar(naming.temp(), make().Type(syms().objectType), toEval);
        // then ignore this result and return something else
        return make().LetExpr(def, toReturn);

    }
    
    /**
     * Makes an 'erroneous' AST node with a message to be logged as an error
     * and (eventually) treated as a compiler bug
     */
    JCExpression makeErroneous(Node node, String message) {
        return makeErroneous(node, message, List.<JCTree>nil());
    }
    
    /**
     * Makes an 'erroneous' AST node with a message to be logged as an error
     * and (eventually) treated as a compiler bug
     */
    JCExpression makeErroneous(Node node, String message, List<? extends JCTree> errs) {
        return makeErr(node, "ceylon.codegen.erroneous", message, errs);
    }
    
    /**
     * Makes an 'erroneous' AST node with a message to be logged as an error
     * but which is <strong>not</strong> treated as a compiler bug
     */
    JCExpression makeError(Node node, String message) {
        return makeError(node, message, List.<JCTree>nil());
    }
    JCExpression makeError(Node node, String message, List<? extends JCTree> errs) {
        return makeErr(node, "ceylon.codegen.error", message, errs);
    }
    private JCExpression makeErr(Node node, String key, String message, List<? extends JCTree> errs) {
        if (node != null) {
            at(node);
        }
        if (message != null) {
            if (node != null) {
                log.error(getPosition(node), key, message);
            } else {
                log.error(key, message);
            }
        }
        return make().Erroneous(errs);
    }
    
    void logError(Node node, String message) {
        log.error(getPosition(node), "ceylon.codegen.error", message);
    }

    List<JCExpression> makeTypeParameterBounds(java.util.List<ProducedType> satisfiedTypes){
        ListBuffer<JCExpression> bounds = new ListBuffer<JCExpression>();
        for (ProducedType t : satisfiedTypes) {
            if (!willEraseToObject(t)) {
                JCExpression bound = makeJavaType(t, AbstractTransformer.JT_NO_PRIMITIVES);
                // if it's a class, we need to move it first as per JLS http://docs.oracle.com/javase/specs/jls/se7/html/jls-4.html#jls-4.4
                if(t.getDeclaration() instanceof Class)
                    bounds.prepend(bound);
                else
                    bounds.append(bound);
            }
        }
        return bounds.toList();
    }
    
    /**
     * Determines whether any of the given type parameters  
     * (not including {@code tp}) has contraints dependent on {@code tp}.  
     * 
     * Partial hack for https://github.com/ceylon/ceylon-compiler/issues/920
     * We need to find if a covariant param has other type parameters with bounds to this one
     * For example if we have "Foo<out A, out B>() given B satisfies A" then we can't generate
     * the following signature: "Foo<? extends Object, ? extends String" because the subtype of
     * String that can satisfy B is not necessarily the subtype of Object that we used for A.
     */
    boolean hasDependentTypeParameters(java.util.List<TypeParameter> tps, TypeParameter tp) {
        boolean isDependedOn = false;
        for(TypeParameter otherTypeParameter : tps){
            // skip this very type parameter
            if(otherTypeParameter == tp)
                continue;
            if(dependsOnTypeParameter(otherTypeParameter, tp)){
                isDependedOn = true;
                break;
            }
        }
        return isDependedOn;
    }

    /**
     * Returns true if the bounds of the type parameter depend on the type parameter itself,
     * like Element given Element satisfies Foo&lt;Element> for example.
     */
    boolean isBoundsSelfDependant(TypeParameter tp){
        return dependsOnTypeParameter(tp, tp);
    }
    
    private boolean dependsOnTypeParameter(TypeParameter tpToCheck, TypeParameter tpToDependOn) {
        for(ProducedType pt : tpToCheck.getSatisfiedTypes()){
            if(dependsOnTypeParameter(pt, tpToDependOn))
                return true;
        }
        return false;
    }

    private boolean dependsOnTypeParameter(ProducedType t, TypeParameter tp) {
        TypeDeclaration decl = t.getDeclaration();
        if(decl instanceof UnionType){
            for(ProducedType pt : decl.getCaseTypes()){
                if(dependsOnTypeParameter(pt, tp)){
                    return true;
                }
            }
        }else if(decl instanceof IntersectionType){
            for(ProducedType pt : decl.getSatisfiedTypes()){
                if(dependsOnTypeParameter(pt, tp)){
                    return true;
                }
            }
        }else if(decl instanceof TypeParameter){
            if(tp == null || tp == decl)
                return true;
        }else if(decl instanceof ClassOrInterface){
            for(ProducedType ta : t.getTypeArgumentList()){
                if(dependsOnTypeParameter(ta, tp)){
                    return true;
                }
            }
        }
        return false;
    }
    
    boolean hasConstrainedTypeParameters(ProducedType type) {
        TypeDeclaration declaration = type.getDeclaration();
        if(declaration instanceof TypeParameter){
            TypeParameter tp = (TypeParameter) declaration;
            return !tp.getSatisfiedTypes().isEmpty();
        }
        if(declaration instanceof UnionType){
            for(ProducedType m : declaration.getCaseTypes())
                if(hasConstrainedTypeParameters(m))
                    return true;
            return false;
        }
        if(declaration instanceof IntersectionType){
            for(ProducedType m : declaration.getSatisfiedTypes())
                if(hasConstrainedTypeParameters(m))
                    return true;
            return false;
        }
        // check its type arguments
        // special case for Callable which has only a single type param in Java
        boolean isCallable = isCeylonCallable(type);
        for(ProducedType typeArg : type.getTypeArgumentList()){
            if(hasConstrainedTypeParameters(typeArg))
                return true;
            // stop after the first type arg for Callable
            if(isCallable)
                break;
        }
        return false;
    }

    private JCTypeParameter makeTypeParameter(String name, java.util.List<ProducedType> satisfiedTypes, boolean covariant, boolean contravariant) {
        return make().TypeParameter(names().fromString(name), makeTypeParameterBounds(satisfiedTypes));
    }

    JCTypeParameter makeTypeParameter(TypeParameter declarationModel) {
        TypeParameter typeParameterForBounds = declarationModel;
        java.util.List<ProducedType> satisfiedTypesForBounds = declarationModel.getSatisfiedTypes();
        // special case for method refinenement where Java doesn't let us refine the parameter bounds
        if(declarationModel.getContainer() instanceof Method){
            Method method = (Method) declarationModel.getContainer();
            Method refinedMethod = (Method) method.getRefinedDeclaration();
            if(method != refinedMethod){
                // find the param index
                int index = method.getTypeParameters().indexOf(declarationModel);
                if(index == -1){
                    log.error("Failed to find type parameter index: "+declarationModel.getName());
                }else if(refinedMethod.getTypeParameters().size() > index){
                    // ignore smaller index than size since the typechecker would have found the error
                    TypeParameter refinedTP = refinedMethod.getTypeParameters().get(index);
                    if(!haveSameBounds(declarationModel, refinedTP)){
                        // find the right instantiation of that type parameter
                        TypeDeclaration methodContainer = (TypeDeclaration) method.getContainer();
                        TypeDeclaration refinedMethodContainer = (TypeDeclaration) refinedMethod.getContainer();
                        // find the supertype that gave us that method and its type arguments
                        Map<TypeParameter, ProducedType> typeArguments = methodContainer.getType().getSupertype(refinedMethodContainer).getTypeArguments();
                        satisfiedTypesForBounds = new ArrayList<ProducedType>(refinedTP.getSatisfiedTypes().size());
                        for(ProducedType satisfiedType : refinedTP.getSatisfiedTypes()){
                            // substitute the refined type parameter bounds with the right type arguments
                            satisfiedTypesForBounds.add(satisfiedType.substitute(typeArguments));
                        }
                        typeParameterForBounds = refinedTP;
                    }
                }
            }
        }
        return makeTypeParameter(declarationModel.getName(), 
                satisfiedTypesForBounds,
                typeParameterForBounds.isCovariant(),
                typeParameterForBounds.isContravariant());
    }
    
    JCTypeParameter makeTypeParameter(Tree.TypeParameterDeclaration param) {
        at(param);
        return makeTypeParameter(param.getDeclarationModel());
    }

    JCAnnotation makeAtTypeParameter(TypeParameter declarationModel) {
        return makeAtTypeParameter(declarationModel.getName(), 
                declarationModel.getSatisfiedTypes(),
                declarationModel.getCaseTypes(),
                declarationModel.isCovariant(),
                declarationModel.isContravariant(),
                declarationModel.getDefaultTypeArgument());
    }
    
    JCAnnotation makeAtTypeParameter(Tree.TypeParameterDeclaration param) {
        at(param);
        return makeAtTypeParameter(param.getDeclarationModel());
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
                typeArgs.append(makeJavaType(type, JT_TYPE_ARGUMENT));
            } else {
                typeArgs.append(makeJavaType(tp.getType(), JT_TYPE_ARGUMENT));
            }
        }
        return typeArgs.toList();
    }
    /**
     * Returns the name of the field in classes which holds the companion 
     * instance.
     */
    final String getCompanionFieldName(Interface def) {
        return naming.getCompanionFieldName(def);
    }
    
    protected int getPosition(Node node) {
        int pos = getMap().getStartPosition(node.getToken().getLine())
                + node.getToken().getCharPositionInLine();
                log.useSource(gen().getFileObject());
        return pos;
    }

    public JCExpression makeClassLiteral(ProducedType type) {
        return makeSelect(makeJavaType(type, JT_NO_PRIMITIVES | JT_RAW | JT_CLASS_LITERAL), "class");
    }

    /**
     * Same as makeClassLiteral but does not use erasure rules
     */
    public JCExpression makeUnerasedClassLiteral(TypeDeclaration declaration) {
        JCExpression className = naming.makeDeclarationName(declaration, DeclNameFlag.QUALIFIED);
        return makeSelect(className, "class");
    }

    public java.util.List<JCExpression> makeReifiedTypeArguments(ProducedReference ref){
        // this is a bit tricky:
        // - for method references (ProducedTypedReference) it's all good
        // - for classes we get a ProducedType which we use to resolve aliases
        // -- UNLESS it's a class with an instantiator, in which case we should not resolve aliases
        //    because the instantiator has the right set of type parameters
        if(ref instanceof ProducedType && !Strategy.generateInstantiator(ref.getDeclaration()))
            ref = ((ProducedType)ref).resolveAliases();
        Declaration declaration = ref.getDeclaration();
        if(!supportsReified(declaration))
            return Collections.emptyList();
        return makeReifiedTypeArguments(getTypeArguments(ref));
    }

    public static boolean supportsReified(Declaration declaration){
        if(declaration instanceof ClassOrInterface){
            // Java constructors don't support reified type arguments
            return Decl.isCeylon((TypeDeclaration) declaration);
        }else if(declaration instanceof Method){
            if (((Method)declaration).isParameter()) {
                // those can never be parameterised
                return false;
            }
            if(Decl.isToplevel(declaration))
                return true;
            // Java methods don't support reified type arguments
            Method m = (Method) CodegenUtil.getTopmostRefinedDeclaration(declaration);
            // See what its container is
            ClassOrInterface container = Decl.getClassOrInterfaceContainer(m);
            // a method which is not a toplevel and is not a class method, must be a method within method and
            // that must be Ceylon so it supports it
            if(container == null)
                return true;
            return supportsReified(container);
        }else{
            throw new RuntimeException("Unhandled declaration type: " + declaration);
        }
    }
    
    private Collection<ProducedType> getTypeArguments(
            ProducedReference producedReference) {
        java.util.List<TypeParameter> typeParameters = getTypeParameters(producedReference);
        java.util.List<ProducedType> typeArguments = new ArrayList<ProducedType>(typeParameters.size());
        for(TypeParameter tp : typeParameters)
            typeArguments.add(producedReference.getTypeArguments().get(tp));
        return typeArguments;
    }

    java.util.List<TypeParameter> getTypeParameters(
            ProducedReference producedReference) {
        Declaration declaration = producedReference.getDeclaration();
        if(declaration instanceof ClassOrInterface)
            return ((ClassOrInterface)declaration).getTypeParameters();
        else
            return ((Method)declaration).getTypeParameters();
    }

    public java.util.List<JCExpression> makeReifiedTypeArguments(
            Collection<ProducedType> typeArguments) {
        java.util.List<JCExpression> ret = new ArrayList<JCExpression>(typeArguments.size());
        for(ProducedType pt : typeArguments){
            ret.add(makeReifiedTypeArgument(pt));
        }
        return ret;
    }

    public JCExpression makeReifiedTypeArgument(ProducedType pt) {
        return makeReifiedTypeArgument(pt.resolveAliases(), false);
    }
    
    private JCExpression makeReifiedTypeArgument(ProducedType pt, boolean qualified) {
        TypeDeclaration declaration = pt.getDeclaration();
        if(declaration instanceof ClassOrInterface){
            // see if we have an alias for it
            if(supportsReifiedAlias((ClassOrInterface) declaration)){
                JCExpression qualifier = naming.makeDeclarationName(declaration, DeclNameFlag.QUALIFIED);
                return makeSelect(qualifier, naming.getTypeDescriptorAliasName());
            }
            // no alias, must build it
            List<JCExpression> typeTestArguments = List.nil();
            JCExpression thisType = makeUnerasedClassLiteral(declaration);
            java.util.List<ProducedType> typeParameters = pt.getTypeArgumentList();
            for(int i=typeParameters.size()-1;i>=0;i--){
                typeTestArguments = typeTestArguments.prepend(makeReifiedTypeArgument(typeParameters.get(i), qualified));
            }
            typeTestArguments = typeTestArguments.prepend(thisType);
            JCExpression classDescriptor = make().Apply(null, makeSelect(makeTypeDescriptorType(), "klass"), typeTestArguments);
            if(pt.getQualifyingType() == null)
                return classDescriptor;
            else{
                return make().Apply(null, makeSelect(makeTypeDescriptorType(), "member"), 
                                                     List.of(makeReifiedTypeArgument(pt.getQualifyingType(), true), classDescriptor));
            }
        }
        if(declaration instanceof TypeParameter){
            TypeParameter tp = (TypeParameter) declaration;
            String name = naming.getTypeArgumentDescriptorName(tp.getName());
            if(!qualified)
                return makeUnquotedIdent(name);
            Scope container = tp.getContainer();
            JCExpression qualifier = null;
            if(container instanceof Class){
                qualifier = naming.makeQualifiedThis(makeJavaType(((Class)container).getType(), JT_RAW));
            }else if(container instanceof Interface){
                qualifier = naming.makeQualifiedThis(makeJavaType(((Interface)container).getType(), JT_COMPANION | JT_RAW));
            }else{
                throw new RuntimeException("Type parameter container not supported yet: " + container);
            }
            return makeSelect(qualifier, name);
        }
        // FIXME: refactor this shite
        if(declaration instanceof UnionType){
            List<JCExpression> typeTestArguments = List.nil();
            java.util.List<ProducedType> typeParameters = ((UnionType)declaration).getCaseTypes();
            for(int i=typeParameters.size()-1;i>=0;i--){
                typeTestArguments = typeTestArguments.prepend(makeReifiedTypeArgument(typeParameters.get(i)));
            }
            return make().Apply(null, makeSelect(makeTypeDescriptorType(), "union"), typeTestArguments);
        }
        if(declaration instanceof IntersectionType){
            List<JCExpression> typeTestArguments = List.nil();
            java.util.List<ProducedType> typeParameters = ((IntersectionType)declaration).getSatisfiedTypes();
            for(int i=typeParameters.size()-1;i>=0;i--){
                typeTestArguments = typeTestArguments.prepend(makeReifiedTypeArgument(typeParameters.get(i)));
            }
            return make().Apply(null, makeSelect(makeTypeDescriptorType(), "intersection"), typeTestArguments);
        }
        if(declaration instanceof NothingType){
            return makeNothingTypeDescriptor();
        }
        throw new RuntimeException("Unsupported type: " + declaration);
    }
    
    public boolean supportsReifiedAlias(ClassOrInterface decl){
        return !decl.isAlias() 
                && !decl.isAnonymous()
                && decl.getTypeParameters().isEmpty()
                && supportsReified(decl)
                && Decl.isToplevel(decl);
    }
    
    boolean isSequencedAnnotation(Class klass) {
        TypeDeclaration meta = typeFact().getSequencedAnnotationDeclaration();
        return meta != null && klass.getType().isSubtypeOf(
                meta.getProducedType(null, 
                Arrays.asList(typeFact().getAnythingDeclaration().getType(),
                typeFact().getNothingDeclaration().getType())));
    }

    private Module getLanguageModule() {
        return loader.getLanguageModule();
    }

    private Module getJDKBaseModule() {
        return loader.getJDKBaseModule();
    }

    ProducedType getGetterInterfaceType(TypedDeclaration attrTypedDecl) {
        ProducedTypedReference typedRef = getTypedReference(attrTypedDecl);
        ProducedTypedReference nonWideningTypedRef = nonWideningTypeDecl(typedRef);
        ProducedType nonWideningType = nonWideningType(typedRef, nonWideningTypedRef);
        
        ProducedType type;
        boolean unboxed = CodegenUtil.isUnBoxed(attrTypedDecl);
        if (unboxed && isCeylonBoolean(nonWideningType)) {
            type = javacCeylonTypeToProducedType(syms().ceylonGetterBooleanType);
        } else if (unboxed && isCeylonInteger(nonWideningType)) {
            type = javacCeylonTypeToProducedType(syms().ceylonGetterLongType);
        } else if (unboxed && isCeylonFloat(nonWideningType)) {
            type = javacCeylonTypeToProducedType(syms().ceylonGetterDoubleType);
        } else if (unboxed && isCeylonCharacter(nonWideningType)) {
            type = javacCeylonTypeToProducedType(syms().ceylonGetterIntType);
        } else {
            type = javacCeylonTypeToProducedType(syms().ceylonGetterType);
            ProducedType typeArg = nonWideningType;
            if (unboxed && isCeylonString(typeArg)) {
                typeArg = javacJavaTypeToProducedType(syms().stringType);
            }
            type = producedType(type.getDeclaration(), typeArg);
        }
        return type;
    }
    
    /**
     * Makes a {@code java.lang.Class<? extends UPPERBOUND>}
     */
    JCExpression makeJavaClassTypeBounded(ProducedType upperBound) {
        return make().TypeApply(make().QualIdent(syms().classType.tsym),
                List.<JCExpression>of(make().Wildcard(make().TypeBoundKind(BoundKind.EXTENDS), 
                            makeJavaType(upperBound))));
    }

    /**
     * If this value is for the hash attribute, turn its long value into an int value by applying (int)(e ^ (e >>> 32))
     */
    public JCExpression convertToIntIfHashAttribute(Declaration model, JCExpression value) {
        if(CodegenUtil.isHashAttribute(model)){
            return convertToIntForHashAttribute(value);
        }
        return value;
    }

    /**
     * Turn this long value into an int value by applying (int)(e ^ (e >>> 32))
     */
    public JCExpression convertToIntForHashAttribute(JCExpression value) {
        SyntheticName tempName = naming.temp("hash");
        JCExpression type = make().Type(syms().longType);
        JCBinary combine = make().Binary(JCTree.BITXOR, makeUnquotedIdent(tempName.asName()), 
                make().Binary(JCTree.USR, makeUnquotedIdent(tempName.asName()), makeInteger(32)));
        return make().TypeCast(syms().intType, makeLetExpr(tempName, null, type, value, combine));
    }
}
