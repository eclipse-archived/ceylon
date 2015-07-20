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

import static com.redhat.ceylon.compiler.typechecker.tree.TreeUtil.hasUncheckedNulls;
import static com.redhat.ceylon.compiler.typechecker.tree.TreeUtil.isForBackend;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.appliedType;
import static com.sun.tools.javac.code.Flags.FINAL;
import static com.sun.tools.javac.code.Flags.PRIVATE;
import static com.sun.tools.javac.code.Flags.PROTECTED;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedSet;
import java.util.Stack;
import java.util.TreeSet;

import org.antlr.runtime.Token;

import com.redhat.ceylon.ceylondoc.Util;
import com.redhat.ceylon.common.Backend;
import com.redhat.ceylon.common.Versions;
import com.redhat.ceylon.compiler.java.codegen.Naming.DeclNameFlag;
import com.redhat.ceylon.compiler.java.codegen.Naming.SyntheticName;
import com.redhat.ceylon.compiler.java.codegen.recovery.Errors;
import com.redhat.ceylon.compiler.java.codegen.recovery.HasErrorException;
import com.redhat.ceylon.compiler.java.codegen.recovery.LocalizedError;
import com.redhat.ceylon.compiler.java.loader.CeylonModelLoader;
import com.redhat.ceylon.compiler.java.loader.TypeFactory;
import com.redhat.ceylon.compiler.java.tools.CeylonLog;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Comprehension;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.PositionalArgument;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Term;
import com.redhat.ceylon.model.loader.AbstractModelLoader;
import com.redhat.ceylon.model.loader.LanguageAnnotation;
import com.redhat.ceylon.model.loader.NamingBase.Unfix;
import com.redhat.ceylon.model.typechecker.model.Annotation;
import com.redhat.ceylon.model.typechecker.model.Class;
import com.redhat.ceylon.model.typechecker.model.ClassOrInterface;
import com.redhat.ceylon.model.typechecker.model.Constructor;
import com.redhat.ceylon.model.typechecker.model.Declaration;
import com.redhat.ceylon.model.typechecker.model.Function;
import com.redhat.ceylon.model.typechecker.model.FunctionOrValue;
import com.redhat.ceylon.model.typechecker.model.Functional;
import com.redhat.ceylon.model.typechecker.model.Generic;
import com.redhat.ceylon.model.typechecker.model.Interface;
import com.redhat.ceylon.model.typechecker.model.Module;
import com.redhat.ceylon.model.typechecker.model.ModuleImport;
import com.redhat.ceylon.model.typechecker.model.Package;
import com.redhat.ceylon.model.typechecker.model.Parameter;
import com.redhat.ceylon.model.typechecker.model.ParameterList;
import com.redhat.ceylon.model.typechecker.model.Reference;
import com.redhat.ceylon.model.typechecker.model.Scope;
import com.redhat.ceylon.model.typechecker.model.SiteVariance;
import com.redhat.ceylon.model.typechecker.model.Type;
import com.redhat.ceylon.model.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.model.typechecker.model.TypeParameter;
import com.redhat.ceylon.model.typechecker.model.TypedDeclaration;
import com.redhat.ceylon.model.typechecker.model.TypedReference;
import com.redhat.ceylon.model.typechecker.model.Value;
import com.redhat.ceylon.model.typechecker.util.TypePrinter;
import com.sun.tools.javac.code.BoundKind;
import com.sun.tools.javac.code.Symbol.TypeSymbol;
import com.sun.tools.javac.code.Symtab;
import com.sun.tools.javac.code.TypeTags;
import com.sun.tools.javac.main.OptionName;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.Factory;
import com.sun.tools.javac.tree.JCTree.JCAnnotation;
import com.sun.tools.javac.tree.JCTree.JCBinary;
import com.sun.tools.javac.tree.JCTree.JCBlock;
import com.sun.tools.javac.tree.JCTree.JCCase;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCExpressionStatement;
import com.sun.tools.javac.tree.JCTree.JCFieldAccess;
import com.sun.tools.javac.tree.JCTree.JCIdent;
import com.sun.tools.javac.tree.JCTree.JCLiteral;
import com.sun.tools.javac.tree.JCTree.JCMethodInvocation;
import com.sun.tools.javac.tree.JCTree.JCNewArray;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import com.sun.tools.javac.tree.JCTree.JCSwitch;
import com.sun.tools.javac.tree.JCTree.JCThrow;
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
import com.sun.tools.javac.util.Options;
import com.sun.tools.javac.util.Position;
import com.sun.tools.javac.util.Position.LineMap;

/**
 * Base class for all delegating transformers
 */
public abstract class AbstractTransformer implements Transformation {

    private final static TypePrinter typeSerialiser = new TypePrinter(
            true,//printAbbreviated 
            true,//printTypeParameters 
            false,//printTypeParameterDetail 
            true,//printQualifyingType 
            false,//escapeLowercased 
            true,//printFullyQualified 
            true);//printQualifier

    private Context context;
    private TreeMaker make;
    private Names names;
    private Symtab syms;
    private AbstractModelLoader loader;
    private TypeFactory typeFact;
    protected Log log;
    final Naming naming;
    private Errors errors;
    private Stack<java.util.List<TypeParameter>> typeParameterSubstitutions = new Stack<java.util.List<TypeParameter>>();
    protected Map<String, Long> omittedModelAnnotations;

    public boolean simpleAnnotationModels;

    public AbstractTransformer(Context context) {
        this.context = context;
        make = TreeMaker.instance(context);
        names = Names.instance(context);
        syms = Symtab.instance(context);
        loader = CeylonModelLoader.instance(context);
        typeFact = TypeFactory.instance(context);
        log = CeylonLog.instance(context);
        naming = Naming.instance(context);
        simpleAnnotationModels = Options.instance(context).get(OptionName.BOOTSTRAPCEYLON) != null;
    }

    Context getContext() {
        return context;
    }
    
    Errors errors() {
        if (this.errors == null) {
            this.errors = Errors.instance(context);
        }
        return errors;
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
    

    boolean blocked = false;
    int block() {
        gen().blocked = true;
        return make.pos;
    }
    int unblock() {
        gen().blocked = false;
        return make.pos;
    }
    protected void _at(int pos) {
        if (!gen().blocked) {
            make.at(pos);
        }
    }
    
    @Override
    public Factory at(Node node) {
        if (node == null) {
            _at(Position.NOPOS);
            
        }
        else {
            Token token = node.getToken();
            if (token != null) {
                int tokenStartPosition = getMap().getStartPosition(token.getLine()) + token.getCharPositionInLine();
                _at(tokenStartPosition);
                if (javaPositionsRetriever != null) {
                    javaPositionsRetriever.addCeylonNode(tokenStartPosition, node);
                }
            }
        }
        return make();
    }
    
    public Factory at(Node node, Token token) {
        if (token == null) {
            _at(Position.NOPOS);
            
        }
        else {
            if (token != null) {
                int tokenStartPosition = getMap().getStartPosition(token.getLine()) + token.getCharPositionInLine();
                _at(tokenStartPosition);
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
            _at(pos);
        }
    }
    
    /**
     * Returns an AutoCloseable whose {@link SavedPosition#close()} will 
     * restore the current position, and sets the position to the given value
     */
    public SavedPosition savePosition(int at) {
        SavedPosition saved = new SavedPosition(make.pos);
        _at(at);
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
        _at(Position.NOPOS);
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

    JCExpression makeIdent(com.sun.tools.javac.code.Type type) {
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
    
    JCExpression makeByte(byte i) {
        return make().Literal(Byte.valueOf(i));
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
    
    JCExpression makeDefaultExprForType(Type type) {
        if (canUnbox(type)) {
            if (isCeylonBoolean(type)) {
                return makeBoolean(false);
            } else if (isCeylonFloat(type)  && type.getUnderlyingType() == null) {
                return make().Literal(0.0);
            } else if ("float".equals(type.getUnderlyingType())) {
                return make().Literal((float)0.0);
            }  else if (isCeylonInteger(type) && type.getUnderlyingType() == null) {
                return makeLong(0);
            } else if ("int".equals(type.getUnderlyingType())) {
                return make().Literal(0);
            } else if ("short".equals(type.getUnderlyingType())) {
                return make().TypeCast(make().Type(syms().shortType), make().Literal(0));
            } else if (isCeylonCharacter(type)) {
                return make().Literal(0);
            } else if (isCeylonByte(type)) {
                return makeByte((byte)0);
            }
        }
        // The default value cannot be seen from the Ceylon code, so it's
        // OK to assign it to null even though it may not be an 
        // optional type
        return makeNull();
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
                names().fromString(Naming.quoteLocalValueName(varName)),
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
            Type type = declarationModel.getType();
            JCStatement transStat;
            HasErrorException error = errors().getFirstExpressionErrorAndMarkBrokenness(expression.getExpression());
            if (error != null) {
                transStat = this.makeThrowUnresolvedCompilationError(error);
            } else {
                transStat = make().Return(expressionGen().transformExpression(expression.getExpression(), boxing, type));
            }
            stats = List.<JCStatement>of(transStat);
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
            Type type = declarationModel.getType();
            JCStatement transStmt;
            HasErrorException error = errors().getFirstExpressionErrorAndMarkBrokenness(expression.getExpression());
            if (error != null) {
                transStmt = this.makeThrowUnresolvedCompilationError(error);
            } else {
                transStmt = make().Exec(expressionGen().transformExpression(expression.getExpression(), BoxingStrategy.INDIFFERENT, type));
            }
            stats = List.<JCStatement>of(transStmt);
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
        } else if (unboxed && isCeylonByte(declarationModel.getType())) {
            boxClass = make().Type(syms().ceylonVariableBoxByteType);
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
        return Decl.equal(decl, typeFact.getBooleanTrueDeclaration());
    }
    
    boolean isBooleanFalse(Declaration decl) {
        return Decl.equal(decl, typeFact.getBooleanFalseDeclaration());
    }
    
    boolean isNullValue(Declaration decl) {
        return Decl.equal(decl, typeFact.getNullValueDeclaration());
    }
    
    /**
     * Determines whether the given type is optional.
     */
    boolean isOptional(Type type) {
        // Note we don't use typeFact().isOptionalType(type) because
        // that implements a stricter test used in the type checker.
        return typeFact().getNullValueDeclaration().getType().isSubtypeOf(type);
    }
    
    boolean isNull(Type type) {
        return type.getSupertype(typeFact.getNullDeclaration()) != null;
    }

    boolean isNullValue(Type type) {
        return type.getSupertype(typeFact.getNullValueDeclaration().getTypeDeclaration()) != null;
    }

    public static boolean isAnything(Type type) {
        return CodegenUtil.isVoid(type);
    }

    private boolean isObject(Type type) {
        return typeFact.getObjectType().isExactly(type);
    }
    
    private boolean isBasic(Type type) {
        return typeFact.getBasicType().isExactly(type);
    }
    
    private boolean isIdentifiable(Type type) {
        return typeFact.getIdentifiableType().isExactly(type);
    }
    
    public boolean isAlias(Type type) {
        return type.getDeclaration().isAlias() || typeFact.getDefiniteType(type).getDeclaration().isAlias();
    }

    /**
     * Simplifies a Ceylon type, eliminating certain things which Java cannot be represented in a Java type. 
     * The following simplifications are made:
     * <table><tbody>
     * <tr><th>Ceylon Type</th><th>result</th></tr>
     * <tr><td>T?</td><td>T</td></tr>
     * <tr><td>T&Object</td><td>T</td></tr>
     * <tr><td>T&Identifiable</td><td>T</td></tr>
     * <tr><td>T&Basic</td><td>T</td></tr>
     * </tbody></table>
     */
    Type simplifyType(Type orgType) {
        if(orgType == null)
            return null;
        Type type = orgType.resolveAliases();
        if (isOptional(type)) {
            // For an optional type T?:
            //  - The Ceylon type T? results in the Java type T
            type = typeFact().getDefiniteType(type);
            if (type.getUnderlyingType() != null) {
                // A definite type should not have its underlyingType set so we make a copy
                type = type.withoutUnderlyingType();
            }
        }
        
        if (type.isUnion() && type.getCaseTypes().size() == 1) {
            // Special case when the Union contains only a single CaseType
            // FIXME This is not correct! We might lose information about type arguments!
            type = type.getCaseTypes().get(0);
        } else if (type.isIntersection()) {
            java.util.List<Type> satisfiedTypes = type.getSatisfiedTypes();
            if (satisfiedTypes.size() == 1) {
                // Special case when the Intersection contains only a single SatisfiedType
                // FIXME This is not correct! We might lose information about type arguments!
                type = satisfiedTypes.get(0);
            } else if (satisfiedTypes.size() == 2) {
                // special case for T? simplified as T&Object
                if (isObject(satisfiedTypes.get(1)) || isBasic(satisfiedTypes.get(1)) || isIdentifiable(satisfiedTypes.get(0))) {
                    type = satisfiedTypes.get(0);
                } else if (isObject(satisfiedTypes.get(0)) || isBasic(satisfiedTypes.get(0)) || isIdentifiable(satisfiedTypes.get(0))) {
                    type = satisfiedTypes.get(1);
                }
            }
        }
        if(isTrueFalseUnion(type))
            type = typeFact().getBooleanType();
        else if(containsJavaEnumInUnion(type))
            type = typeFact().denotableType(type);
        if (type.getDeclaration() instanceof Constructor) {
            type = type.getExtendedType();
        }
        return type;
    }

    private boolean containsJavaEnumInUnion(Type type) {
        if(!type.isUnion())
            return false;
        for(Type caseType : type.getCaseTypes()){
            if(caseType.isClass()
                    && caseType.getDeclaration().isJavaEnum())
                return true;
        }
        return false;
    }

    TypedReference getTypedReference(TypedDeclaration decl){
        java.util.List<Type> typeArgs = Collections.<Type>emptyList();
        if (decl instanceof Function) {
            // For methods create type arguments for any type parameters it might have
            Function m = (Function)decl;
            if (!m.getTypeParameters().isEmpty()) {
                typeArgs = new ArrayList<Type>(m.getTypeParameters().size());
                for (TypeParameter p: m.getTypeParameters()) {
                    Type pt = p.getType();
                    typeArgs.add(pt);
                }
            }
        }
        
        if(decl.getContainer() instanceof TypeDeclaration){
            TypeDeclaration containerDecl = (TypeDeclaration) decl.getContainer();
            return containerDecl.getType().getTypedMember(decl, typeArgs);
        }
        return decl.appliedTypedReference(null, typeArgs);
    }
    
    TypedReference nonWideningTypeDecl(TypedReference typedReference) {
        return nonWideningTypeDecl(typedReference, typedReference.getQualifyingType());
    }
    
    TypedReference nonWideningTypeDecl(TypedReference typedReference, Type currentType) {
        TypedReference refinedTypedReference = getRefinedDeclaration(typedReference, currentType);
        if(refinedTypedReference != null){
            /*
             * We are widening if the type:
             * - is not object
             * - is erased to object
             * - refines a declaration that is not erased to object
             */
            Type declType = typedReference.getType();
            Type refinedDeclType = refinedTypedReference.getType();
            if(declType == null || refinedDeclType == null)
                return typedReference;
            boolean isWidening = isWidening(declType, refinedDeclType);
            
            if(!isWidening){
                // make sure we get the instantiated refined decl
                if(refinedDeclType.getDeclaration() instanceof TypeParameter
                        && !(declType.getDeclaration() instanceof TypeParameter))
                    refinedDeclType = nonWideningType(typedReference, refinedTypedReference);
                if (!typedReference.getTypeArguments().containsKey(simplifyType(refinedDeclType).getDeclaration())) {
                    isWidening = isWideningTypeArguments(declType, refinedDeclType, true);
                }
            }
            // note that we don't use the type erased info to determine the refined decl, as we do
            // in isWideningTypeDecl(), because we get around needing that by using raw types if
            // required in actual implementations
            
            if(isWidening)
                return refinedTypedReference;
        }
        return typedReference;
    }

    public boolean isWideningTypeDecl(TypedDeclaration typedDeclaration) {
        TypedReference typedReference = getTypedReference(typedDeclaration);
        return isWideningTypeDecl(typedReference, typedReference.getQualifyingType());
    }

    public boolean isWideningTypeDecl(TypedReference typedReference, Type currentType) {
        TypedReference refinedTypedReference = getRefinedDeclaration(typedReference, currentType);
        if(refinedTypedReference == null)
            return false;
        /*
         * We are widening if the type:
         * - is not object
         * - is erased to object
         * - refines a declaration that is not erased to object
         */
        Type declType = typedReference.getType();
        Type refinedDeclType = refinedTypedReference.getType();
        if(declType == null || refinedDeclType == null)
            return false;
        if(isWidening(declType, refinedDeclType))
            return true;

        // make sure we get the instantiated refined decl
        if(refinedDeclType.getDeclaration() instanceof TypeParameter
                && !(declType.getDeclaration() instanceof TypeParameter))
            refinedDeclType = nonWideningType(typedReference, refinedTypedReference);
        if(isWideningTypeArguments(declType, refinedDeclType, true))
            return true;
        
        if(CodegenUtil.hasTypeErased(refinedTypedReference.getDeclaration())
                && !willEraseToObject(declType))
           return true;

        return false;
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
    private TypedReference getRefinedDeclaration(TypedReference typedReference, Type currentType) {
        TypedDeclaration decl = typedReference.getDeclaration();
        TypedDeclaration modelRefinedDecl = (TypedDeclaration)decl.getRefinedDeclaration();
        Type referenceQualifyingType = typedReference.getQualifyingType();
        boolean forMixinMethod = 
                currentType != null
                && decl.getContainer() instanceof ClassOrInterface
                && referenceQualifyingType != null
                && !Decl.equal(referenceQualifyingType.getDeclaration(), currentType.getDeclaration());
        // quick exit
        if (Decl.equal(decl, modelRefinedDecl) && !forMixinMethod)
            return null;
        // modelRefinedDecl exists, but perhaps it's the toplevel refinement and not the one Java will look at
        if(!forMixinMethod)
            modelRefinedDecl = getFirstRefinedDeclaration(decl);
        TypeDeclaration qualifyingDeclaration = currentType.getDeclaration();
        if(qualifyingDeclaration instanceof ClassOrInterface){
            // if both are returning unboxed void we're good
            if(Decl.isUnboxedVoid(decl)
                    && Decl.isUnboxedVoid(modelRefinedDecl))
                return null;
            // only try to find better if we're erasing to Object and we're not returning a type param
            if(willEraseToObject(typedReference.getType())
                        || isWideningTypeArguments(decl.getType(), modelRefinedDecl.getType(), true)
                    && !isTypeParameter(typedReference.getType())){
                ClassOrInterface declaringType = (ClassOrInterface) qualifyingDeclaration;
                Set<TypedDeclaration> refinedMembers = getRefinedMembers(declaringType, decl.getName(), 
                        com.redhat.ceylon.model.typechecker.model.ModelUtil.getSignature(decl), false);
                // now we must select a different refined declaration if we refine it more than once
                if(refinedMembers.size() > (forMixinMethod ? 0 : 1)){
                    // first case
                    for(TypedDeclaration refinedDecl : refinedMembers){
                        // get the type reference to see if any eventual type param is instantiated in our inheritance of this type/method
                        TypedReference refinedTypedReference = getRefinedTypedReference(typedReference, refinedDecl);
                        // if it is not instantiated, that's the one we're looking for
                        if(isTypeParameter(refinedTypedReference.getType()))
                            return refinedTypedReference;
                    }
                    // second case
                    for(TypedDeclaration refinedDecl : refinedMembers){
                        // get the type reference to see if any eventual type param is instantiated in our inheritance of this type/method
                        TypedReference refinedTypedReference = getRefinedTypedReference(typedReference, refinedDecl);
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
                        Type extendedType = declaringType.getExtendedType();
                        if(extendedType != null){
                            TypedReference refinedTypedReference = getRefinedTypedReference(extendedType, modelRefinedDecl);
                            Type refinedType = refinedTypedReference.getType();
                            if(!isTypeParameter(refinedType)
                                    && !willEraseToObject(refinedType))
                                return refinedTypedReference;
                        }
                        // then satisfied interfaces
                        for(Type satisfiedType : declaringType.getSatisfiedTypes()){
                            TypedReference refinedTypedReference = getRefinedTypedReference(satisfiedType, modelRefinedDecl);
                            Type refinedType = refinedTypedReference.getType();
                            if(!isTypeParameter(refinedType)
                                    && !willEraseToObject(refinedType))
                                return refinedTypedReference;
                        }
                    }
                }
            }
            /*
             * Now there's another crazy case:
             * 
             * interface Top<out Element> {
             *  Top<Element> ret => nothing;
             * }
             * interface Left satisfies Top<Integer> {}
             * interface Right satisfies Top<String> {}
             * class Bottom() satisfies Left&Right {}
             * 
             * Where Bottom.ret does not exist and is typed as returning Integer&String which is Nothing, erased to Object,
             * and we look at what it refines and find only a single definition Top.ret typed as returning Integer&String (Nothing),
             * so we think there's no widening, but Java will only see Top<Integer>.ret from Left, and that's the one we want
             * to use for determining widening.
             * See https://github.com/ceylon/ceylon-compiler/issues/1765
             */
            Type firstInstantiation = isInheritedWithDifferentTypeArguments(modelRefinedDecl.getContainer(), currentType);
            if(firstInstantiation != null){
                TypedReference firstInstantiationTypedReference = getRefinedTypedReference(firstInstantiation, modelRefinedDecl);
                Type firstInstantiationType = firstInstantiationTypedReference.getType();
                if(isWidening(decl.getType(), firstInstantiationType)
                        || isWideningTypeArguments(decl.getType(), firstInstantiationType, true))
                    return firstInstantiationTypedReference;

            }
        }
        return getRefinedTypedReference(typedReference, modelRefinedDecl);
    }

    protected Type isInheritedWithDifferentTypeArguments(Scope container, Type currentType) {
        // only interfaces can be inherited twice
        if(container instanceof Interface == false)
            return null;
        if(currentType.getDeclaration() instanceof ClassOrInterface == false)
            return null;
        Interface iface = (Interface) container;
        // if we have no type parameter there's no problem
        if(iface.getTypeParameters().isEmpty())
            return null;
        
        Type[] arg = new Type[1];
        return findFirstInheritedTypeIfInheritedTwiceWithDifferentTypeArguments(iface, currentType, arg);
    }

    private Type findFirstInheritedTypeIfInheritedTwiceWithDifferentTypeArguments(Interface iface, Type currentType, Type[] found) {
        if(Decl.equal(currentType.getDeclaration(), iface)){
            if(found[0] == null){
                // first time we find it, just record it
                found[0] = currentType;
                // stop there
                return null;
            }else if(found[0].isExactly(currentType)){
                // we already found the same type, ignore it and stop there
                return null;
            }else{
                // we found a second type, let's return the first one found
                return found[0];
            }
        }
        // first extended type
        Type extendedType = currentType.getExtendedType();
        if(extendedType != null){
            Type ret = findFirstInheritedTypeIfInheritedTwiceWithDifferentTypeArguments(iface, extendedType, found);
            // stop there if we found a result
            if(ret != null)
                return ret;
        }
        // then satisfied interfaces
        for(Type satisfiedType : currentType.getSatisfiedTypes()){
            Type ret = findFirstInheritedTypeIfInheritedTwiceWithDifferentTypeArguments(iface, satisfiedType, found);
            // stop there if we found a result
            if(ret != null)
                return ret;
        }
        // not found
        return null;
    }

    private TypedDeclaration getFirstRefinedDeclaration(TypedDeclaration decl) {
        if(decl.getContainer() instanceof ClassOrInterface == false)
            return decl;
        java.util.List<Type> signature = com.redhat.ceylon.model.typechecker.model.ModelUtil.getSignature(decl);
        ClassOrInterface container = (ClassOrInterface) decl.getContainer();
        HashSet<TypeDeclaration> visited = new HashSet<TypeDeclaration>();
        // start looking for it, but skip this type, only lookup upwards of it
        TypedDeclaration firstRefinedDeclaration = getFirstRefinedDeclaration(container, decl, signature, visited, true);
        // only keep the first refined decl if its type can be trusted: if it is not itself widening
        if(firstRefinedDeclaration != null){
            if(CodegenUtil.hasUntrustedType(firstRefinedDeclaration))
                firstRefinedDeclaration = getFirstRefinedDeclaration(firstRefinedDeclaration);
        }
        return firstRefinedDeclaration != null ? firstRefinedDeclaration : decl;
    }

    private TypedDeclaration getFirstRefinedDeclaration(TypeDeclaration typeDecl, TypedDeclaration decl, 
            java.util.List<Type> signature, HashSet<TypeDeclaration> visited,
            boolean skipType) {
        if(!visited.add(typeDecl))
            return null;
        if(!skipType){
            TypedDeclaration member = (TypedDeclaration) typeDecl.getDirectMember(decl.getName(), signature, false);
            if(member != null)
                return member;
        }
        // look up
        // first look in super types
        if(typeDecl.getExtendedType() != null){
            TypedDeclaration refinedDecl = getFirstRefinedDeclaration(typeDecl.getExtendedType().getDeclaration(), decl, signature, visited, false);
            if(refinedDecl != null && refinedDecl.isShared())
                return refinedDecl;
        }
        // look in interfaces
        for(Type interf : typeDecl.getSatisfiedTypes()){
            TypedDeclaration refinedDecl = getFirstRefinedDeclaration(interf.getDeclaration(), decl, signature, visited, false);
            if(refinedDecl != null && refinedDecl.isShared())
                return refinedDecl;
        }
        // not found
        return null;
    }

    // Finds all member declarations (original and refinements) with the
    // given name and signature within the given type and it's super
    // classes and interfaces
    public Set<TypedDeclaration> getRefinedMembers(TypeDeclaration decl,
            String name, 
            java.util.List<Type> signature, boolean ellipsis) {
        Set<TypedDeclaration> ret = new HashSet<TypedDeclaration>();
        collectRefinedMembers(decl.getType(), name, signature, ellipsis, 
                new HashSet<TypeDeclaration>(), ret, true);
        return ret;
    }

    private void collectRefinedMembers(Type currentType, String name, 
            java.util.List<Type> signature, boolean ellipsis,
            java.util.Set<TypeDeclaration> visited, Set<TypedDeclaration> ret,
            boolean ignoreFirst) {
        TypeDeclaration decl = currentType.getDeclaration();
        if (visited.contains(decl)) {
            return;
        }
        else {
            visited.add(decl);
            Type et = currentType.getExtendedType();
            if (et!=null) {
                collectRefinedMembers(et, name, signature, ellipsis, visited, ret, false);
            }
            for (Type st: currentType.getSatisfiedTypes()) {
                collectRefinedMembers(st, name, signature, ellipsis, visited, ret, false);
            }
            // we're collecting refined members, not the refining one
            if(!ignoreFirst){
                TypedDeclaration found = (TypedDeclaration) decl.getDirectMember(name, signature, ellipsis);
                if(found instanceof Function){
                    // do not trust getDirectMember because if you ask it for [Integer,String] and it has [Integer,E] it does not
                    // know that E=String and will not make it match, and will just return any member when there is overloading,
                    // including one with signature [String] when you asked for [Integer,String]
                    java.util.List<Type> typedSignature = getTypedSignature(currentType, found);
                    if(typedSignature != null
                            && hasMatchingSignature(signature, typedSignature))
                        ret.add(found);
                }
            }
        }
    }

    private java.util.List<Type> getTypedSignature(Type currentType, TypedDeclaration found) {
        // check that its signature is compatible
        java.util.List<ParameterList> parameterLists = ((Function) found).getParameterLists();
        if(parameterLists == null || parameterLists.isEmpty())
            return null;
        // only consider first param list
        java.util.List<Parameter> parameters = parameterLists.get(0).getParameters();
        if(parameters == null)
            return null;
        TypedReference typedMember = currentType.getTypedMember(found, Collections.<Type>emptyList());
        if(typedMember == null)
            return null;
        java.util.List<Type> typedSignature = new ArrayList<Type>(parameters.size());
        for(Parameter p : parameters){
            Type parameterType = typedMember.getTypedParameter(p).getFullType();
            typedSignature.add(parameterType);
        }
        return typedSignature;
    }

    private boolean hasMatchingSignature(java.util.List<Type> signature, java.util.List<Type> typedSignature) {
        if(signature.size() != typedSignature.size())
            return false;
        for(int i=0;i<signature.size();i++){
            Type signatureArg = signature.get(i);
            Type typedSignatureArg = typedSignature.get(i);
            if(signatureArg != null
                    && typedSignatureArg != null
                    && !com.redhat.ceylon.model.typechecker.model.ModelUtil.matches(signatureArg, typedSignatureArg, typeFact()))
                return false;
        }
        return true;
    }

    private TypedReference getRefinedTypedReference(TypedReference typedReference, 
            TypedDeclaration refinedDeclaration) {
        TypeDeclaration refinedContainer = (TypeDeclaration)refinedDeclaration.getContainer();

        Type refinedContainerType = typedReference.getQualifyingType().getSupertype(refinedContainer);
        ArrayList<Type> typeArgs = new ArrayList<Type>();
        if (typedReference.getDeclaration() instanceof Generic) {
            for (TypeParameter tp : ((Generic)typedReference.getDeclaration()).getTypeParameters()) {
                typeArgs.add(typedReference.getTypeArguments().get(tp));
            }
        }
        return refinedDeclaration.appliedTypedReference(refinedContainerType, typeArgs);
    }

    private TypedReference getRefinedTypedReference(Type qualifyingType, 
                                                            TypedDeclaration refinedDeclaration) {
        TypeDeclaration refinedContainer = (TypeDeclaration)refinedDeclaration.getContainer();

        Type refinedContainerType = qualifyingType.getSupertype(refinedContainer);
        return refinedDeclaration.appliedTypedReference(refinedContainerType, Collections.<Type>emptyList());
    }

    public boolean isWidening(Type declType, Type refinedDeclType) {
        return !isCeylonObject(declType)
                && willEraseToObject(declType)
                && !willEraseToObject(refinedDeclType);
    }

    private boolean isWideningTypeArguments(Type declType, Type refinedDeclType, boolean allowSubtypes) {
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
            
            if((willEraseToObject(refinedDeclType))){
                // if we refine something that erases to object, and:
                // - we don't erase to object -> we can't possibly be widening, or
                // - similarly if we both erase to object we're not widening
                return false;
            }

            // if we have exactly the same type don't bother finding a common ancestor
            if(!declType.isExactly(refinedDeclType)){
                // check if we can form an informed decision
                if(refinedDeclType.getDeclaration() == null)
                    return true;
                // find the instantiation of the refined decl type in the decl type
                // special case for optional types: let's find the definite type since
                // in java they are equivalent
                Type definiteType = typeFact().getDefiniteType(refinedDeclType);
                if(definiteType != null)
                    refinedDeclType = definiteType;
                declType = declType.getSupertype(refinedDeclType.getDeclaration());
                // could not find common type, we must be widening somehow
                if(declType == null)
                    return true;
            }
        }
        Map<TypeParameter, Type> typeArguments = declType.getTypeArguments();
        Map<TypeParameter, Type> refinedTypeArguments = refinedDeclType.getTypeArguments();
        java.util.List<TypeParameter> typeParameters = declType.getDeclaration().getTypeParameters();
        for(TypeParameter tp : typeParameters){
            Type typeArgument = typeArguments.get(tp);
            if(typeArgument == null)
                return true; // something fishy here
            Type refinedTypeArgument = refinedTypeArguments.get(tp);
            if(refinedTypeArgument == null)
                return true; // something fishy here
            // check if the type arg is widening due to erasure
            if(isWidening(typeArgument, refinedTypeArgument))
                return true;
            // check if we are refining a covariant param which we must "fix" because it is dependend on, like Tuple's first TP
            if(declType.isCovariant(tp)
                    && hasDependentTypeParameters(typeParameters, tp)
                    && !typeArgument.isExactly(refinedTypeArgument)
                    // it is not widening if we refine Object with a TP, though
                    && !(willEraseToObject(refinedTypeArgument)
                            && (isTypeParameter(typeArgument)
                                    // it is also not widening if we erase both to Object
                                    || willEraseToObject(typeArgument))
                            )
                    )
                return true;
            // check if the type arg is a subtype, or if its type args are widening
            if(isWideningTypeArguments(typeArgument, refinedTypeArgument, tp.isCovariant()))
                return true;
        }
        // so far so good
        return false;
    }

    public boolean haveSameBounds(TypeParameter tp, TypeParameter refinedTP) {
        java.util.List<Type> satTP = tp.getSatisfiedTypes();
        java.util.List<Type> satRefinedTP = new LinkedList<Type>();
        satRefinedTP.addAll(refinedTP.getSatisfiedTypes());
        // same number of bounds
        if(satTP.size() != satRefinedTP.size())
            return false;
        // make sure all the bounds are the same
        OUT:
        for(Type satisfiedType : satTP){
            for(Type refinedSatisfiedType : satRefinedTP){
                // if we found it, remove it from the second list to not match it again
                if(satisfiedType.isExactly(refinedSatisfiedType)){
                    satRefinedTP.remove(refinedSatisfiedType);
                    continue OUT;
                }
            }
            // not found
            return false;
        }
        // all bounds are equal
        return true;
    }

    Type nonWideningType(TypedReference declaration, TypedReference refinedDeclaration){
        final Reference pr;
        if (declaration.equals(refinedDeclaration)) {
            pr = declaration;
        } else {
            Type refinedType = refinedDeclaration.getType();
            // if the refined type is a method TypeParam, use the original decl that will be more correct,
            // since it may have changed name
            if(refinedType.getDeclaration() instanceof TypeParameter
                    && refinedType.getDeclaration().getContainer() instanceof Function){
                // find its index in the refined declaration
                TypeParameter refinedTypeParameter = (TypeParameter) refinedType.getDeclaration();
                Function refinedMethod = (Function) refinedTypeParameter.getContainer();
                int i=0;
                for(TypeParameter tp : refinedMethod.getTypeParameters()){
                    if(tp.getName().equals(refinedTypeParameter.getName()))
                        break;
                    i++;
                }
                if(i >= refinedMethod.getTypeParameters().size()){
                    throw new BugException("can't find type parameter "+refinedTypeParameter.getName()+" in its container "+refinedMethod.getName());
                }
                // the refining method type parameter should be at the same index
                if(declaration.getDeclaration() instanceof Function == false)
                    throw new BugException("refining declaration is not a method: "+declaration);
                Function refiningMethod = (Function) declaration.getDeclaration();
                if(i >= refiningMethod.getTypeParameters().size()){
                    throw new BugException("refining method does not have enough type parameters to refine "+refinedMethod.getName());
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

    private Type javacCeylonTypeToProducedType(com.sun.tools.javac.code.Type t) {
        return loader().getType(getLanguageModule(), t.tsym.packge().getQualifiedName().toString(), t.tsym.getQualifiedName().toString(), null);
    }

    private Type javacJavaTypeToProducedType(com.sun.tools.javac.code.Type t) {
        return loader().getType(getJDKBaseModule(), t.tsym.packge().getQualifiedName().toString(), t.tsym.getQualifiedName().toString(), null);
    }

    /**
     * Determines if a type will be erased to java.lang.Object once converted to Java
     * @param type
     * @return
     */
    boolean willEraseToObject(Type type) {
        if(type == null)
            return false;
        type = simplifyType(type);
        if (type.isUnion() || type.isIntersection()) {
            // Any Unions and Intersections erase to Object as well
            // except for the ones that erase to Sequential
            return true;
        }
        TypeDeclaration decl = type.getDeclaration();
        // All the following types either are Object or erase to Object
        if (Decl.equal(decl, typeFact.getObjectDeclaration())
                || Decl.equal(decl, typeFact.getIdentifiableDeclaration())
                || Decl.equal(decl, typeFact.getBasicDeclaration())
                || Decl.equal(decl, typeFact.getNullDeclaration())
                || Decl.equal(decl, typeFact.getNullValueDeclaration().getTypeDeclaration())
                || Decl.equal(decl, typeFact.getAnythingDeclaration())
                || type.isNothing()) {
            return true;
        }
        return false;
    }
    
    boolean willEraseToPrimitive(Type type) {
        return (isCeylonBoolean(type)
                || isCeylonInteger(type)
                || isCeylonFloat(type)
                || isCeylonCharacter(type)
                || isCeylonByte(type));
    }
    
    boolean willEraseToAnnotation(Type type) {
        type = simplifyType(type);
        return type != null 
                && (type.isExactly(typeFact.getAnnotationDeclaration().getType())
                        || (type.getDeclaration() instanceof ClassOrInterface 
                                && type.getDeclaration().equals(typeFact.getConstrainedAnnotationDeclaration())));
    }

    boolean willEraseToException(Type type) {
        type = simplifyType(type);
        return type != null && type.isExactly(typeFact.getExceptionType());
    }
    
    boolean willEraseToThrowable(Type type) {
        type = simplifyType(type);
        TypeDeclaration decl = type.getDeclaration();
        return Decl.equal(decl, typeFact.getThrowableDeclaration());
    }
    
    boolean willEraseToSequence(Type type) {
        type = simplifyType(type);
        TypeDeclaration decl = type.getDeclaration();
        return Decl.equal(decl, typeFact.getTupleDeclaration());
    }
    
    // keep in sync with MethodDefinitionBuilder.paramType()
    public boolean willEraseToBestBounds(Parameter param) {
        return willEraseToBestBounds(param.getModel());
    }
    
    public boolean willEraseToBestBounds(FunctionOrValue paramModel) {
        Type type = paramModel.getType();
        if (typeFact().isUnion(type) 
                || typeFact().isIntersection(type)) {
            final Type refinedType = ((TypedDeclaration)CodegenUtil.getTopmostRefinedDeclaration(paramModel)).getType();
            if (refinedType.isTypeParameter()
                    && !refinedType.getSatisfiedTypes().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    boolean hasErasure(Type type) {
        return type==null ? false : hasErasureResolved(type.resolveAliases());
    }
    
    private boolean hasErasureResolved(Type type) {
        if(type == null)
            return false;
        if(type.isUnion()){
            java.util.List<Type> caseTypes = type.getCaseTypes();
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
        if(type.isIntersection()){
            java.util.List<Type> satisfiedTypes = type.getSatisfiedTypes();
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
        if(type.isTypeParameter()){
            // consider type parameters with non-erased bounds as erased to force a cast
            // see https://github.com/ceylon/ceylon-compiler/issues/1327
            for(Type bound : type.getSatisfiedTypes()){
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
        for(Type pt : type.getTypeArgumentList()){
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
    boolean isTurnedToRaw(Type type){
        return type==null ? false : isTurnedToRawResolved(type.resolveAliases());
    }
    
    private boolean isTurnedToRawResolved(Type type) {
        // if we don't have type arguments we can't be raw
        if(type.getTypeArguments().isEmpty())
            return false;

        // we only go raw if any type param is an erased union/intersection
        // start with type but consider ever qualifying type
        Type singleType = type;
        do{
            // special case for Callable where we stop after the first type param
            boolean isCallable = isCeylonCallable(singleType);
            TypeDeclaration declaration = singleType.getDeclaration();
            Map<TypeParameter, Type> typeArguments = singleType.getTypeArguments();
            for(TypeParameter tp : declaration.getTypeParameters()){
                Type ta = typeArguments.get(tp);
                // skip invalid input
                if(tp == null || ta == null)
                    return false;

                // see makeTypeArgs: Nothing in contravariant position causes a raw type
                if(singleType.isContravariant(tp) && ta.isNothing())
                    return true;
                
                if (isErasedUnionOrIntersection(ta)) {
                    return true;
                }

                // Callable really has a single type arg in Java
                if(isCallable)
                    break;
                // don't recurse
            }
            // move on to next qualifying type
        }while((singleType = singleType.getQualifyingType()) != null);
        
        // we're only raw if every type param is an erased union/intersection
        return false;
    }

    private boolean isErasedUnionOrIntersection(Type producedType) {
        if(producedType.isUnion()){
            java.util.List<Type> caseTypes = producedType.getCaseTypes();
            // special case for optional types
            if(caseTypes.size() == 2){
                if(isNull(caseTypes.get(0))){
                    return isErasedUnionOrIntersection(caseTypes.get(1));
                }else if(isNull(caseTypes.get(1))){
                    return isErasedUnionOrIntersection(caseTypes.get(0));
                }
            }
            // it is erased
            return true;
        }
        if(producedType.isIntersection()){
            java.util.List<Type> satisfiedTypes = producedType.getSatisfiedTypes();
            // special case for non-optional types
            if(satisfiedTypes.size() == 2){
                if(isObject(satisfiedTypes.get(0))){
                    return isErasedUnionOrIntersection(satisfiedTypes.get(1));
                }else if(isObject(satisfiedTypes.get(1))){
                    return isErasedUnionOrIntersection(satisfiedTypes.get(0));
                }
            }
            // it is erased
            return true;
        }
        // we found something which is not erased entirely
        return false;
    }

    boolean isCeylonString(Type type) {
        return type != null && type.isExactly(typeFact.getStringType());
    }
    
    boolean isCeylonBoolean(Type type) {
        TypeDeclaration declaration = type.getDeclaration();
        return declaration != null
                && (type.isExactly(typeFact.getBooleanType())
                        || isBooleanTrue(declaration)
                        || Decl.equal(declaration, typeFact.getBooleanTrueClassDeclaration())
                        || isBooleanFalse(declaration)
                        || Decl.equal(declaration, typeFact.getBooleanFalseClassDeclaration())
                        || isTrueFalseUnion(type));
    }
    
    private boolean isTrueFalseUnion(Type type) {
        if(type.isUnion() && type.getCaseTypes().size() == 2){
            for(Type t : type.getCaseTypes()){
                if(!isCeylonBoolean(t))
                    return false;
            }
            return true;
        }
        return false;
    }

    boolean isCeylonInteger(Type type) {
        return type != null && type.isExactly(typeFact.getIntegerType());
    }
    
    boolean isCeylonFloat(Type type) {
        return type != null && type.isExactly(typeFact.getFloatType());
    }
    
    boolean isCeylonCharacter(Type type) {
        return type != null && type.isExactly(typeFact.getCharacterType());
    }

    boolean isCeylonByte(Type type) {
        return type != null && type.isExactly(typeFact.getByteType());
    }

    boolean isCeylonArray(Type type) {
        return type.getSupertype(typeFact.getArrayDeclaration()) != null;
    }
    
    boolean isCeylonObject(Type type) {
        return type != null && type.isExactly(typeFact.getObjectType());
    }
    
    boolean isCeylonBasicType(Type type) {
        return (isCeylonString(type)
                || isCeylonBoolean(type)
                || isCeylonInteger(type)
                || isCeylonFloat(type)
                || isCeylonCharacter(type)
                || isCeylonByte(type));
    }
    
    boolean isCeylonCallable(Type type) {
        // only say yes for exactly Callable, as this is mostly used for erasure of its second type parameter
        // but we want subtypes of Callable such as the metamodel to have those extra type parameters ATM
        return Decl.equal(type.getDeclaration(), typeFact.getCallableDeclaration());
//        return type.getDeclaration().getUnit().isCallableType(type);
    }

    boolean isCeylonCallableSubtype(Type type) {
        return typeFact().isCallableType(type);
    }

    boolean isExactlySequential(Type type) {
        return typeFact().getDefiniteType(type).isSequential();
    }
    
    boolean isCeylonMetamodelDeclaration(Type type) {
        return type.isSubtypeOf(typeFact().getMetamodelDeclarationDeclaration().getType());
    }

    boolean isCeylonSequentialMetamodelDeclaration(Type type) {
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
    
    /* Do not perform any simplifications/reductions on the type */
    private static final int __JT_FULL_TYPE = 1 << 10;
    
    /** For use when generating a type argument. Implies {@code JT_NO_PRIMITIVES} */
    static final int JT_TYPE_ARGUMENT = JT_NO_PRIMITIVES | __JT_FULL_TYPE;

    /** For use when we want a value type class. */
    static final int JT_VALUE_TYPE = 1 << 11;
    
    /** Generates the Java type of the companion class of the given class */
    static final int JT_ANNOTATION = 1 << 12;
    /** Generates the Java type of the companion class of the given class */
    static final int JT_ANNOTATIONS = 1 << 13;

    /** Do not resolve aliases, useful if we want a class literal pointing to the alias class itself. */
    static final int JT_CLASS_LITERAL = 1 << 14;
    
    /** For use when generating a narrower type for a refinement, for example when the
     * parameter of an overriding method is of type T<U|V> while the original was T<E>
     */
    static final int JT_NARROWED = __JT_FULL_TYPE;
    
    static final int JT_IS = 1 << 15;

    /**
     * This function is used solely for method return types and parameters 
     */
    JCExpression makeJavaType(TypedDeclaration typeDecl, Type type, int flags) {
        if (typeDecl instanceof Function
                && ((Function)typeDecl).isParameter()) {
            Function p = (Function)typeDecl;
            Type pt = type;
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
    
    JCExpression makeJavaType(Type producedType) {
        return makeJavaType(producedType, 0);
    }

    public boolean rawParameters(Declaration d) {
        if (d.isMember()
                && rawSupertype(((ClassOrInterface)d.getContainer()).getType(), JT_SATISFIES | JT_EXTENDS)) {
            return true;
        } else {
            return false;
        }
    }
    
    /** 
     * Determine whether the given type, when appearing in 
     * an {@code extends} or {@code implements} clause, should be made raw.
     * This can happen because the type itself must be made raw, or because 
     * any of its supertypes were made raw.
     * See #1875.
     */
    public boolean rawSupertype(Type ceylonType, int flags) {
        if (ceylonType == null
                || (flags & (JT_SATISFIES | JT_EXTENDS)) == 0) {
            return false;
        }
        Type simpleType = simplifyType(ceylonType);
        Map<TypeParameter, Type> tas = simpleType.getTypeArguments();
        java.util.List<TypeParameter> tps = simpleType.getDeclaration().getTypeParameters();
        for (TypeParameter tp : tps) {
            Type ta = tas.get(tp);
            // error handling
            if(ta == null)
                continue;
            
            // Null will claim to be optional, but if we get its non-null type we will land with Nothing, which is not what
            // we want, so we make sure it's not Null
            if (isOptional(ta) && !isNull(ta)) {
                // For an optional type T?:
                // - The Ceylon type Foo<T?> results in the Java type Foo<T>.
                ta = getNonNullType(ta);
            }
            // In a type argument Foo<X&Object> or Foo<X?> transform to just Foo<X>
            ta = simplifyType(ta);
            if (typeFact().isUnion(ta) || typeFact().isIntersection(ta)) {
                // For any other union type U|V (U nor V is Optional):
                // - The Ceylon type Foo<U|V> results in the raw Java type Foo.
                // For any other intersection type U&V:
                // - The Ceylon type Foo<U&V> results in the raw Java type Foo.
                // use raw types if:
                // - we're not in a type argument (when used as type arguments raw types have more constraint than at the toplevel)
                //   or we're in an extends or satisfies and the type parameter is a self type
                // Note: it used to be we used raw types when calling constructors, but that was wrong as it did not
                // conform with where raw types would be used between expressions and constructors
                if(((flags & (JT_EXTENDS | JT_SATISFIES)) != 0 && tp.getSelfTypedDeclaration() != null)){
                    // A bit ugly, but we need to escape from the loop and create a raw type, no generics
                    return true;
                } else if ((flags & (__JT_FULL_TYPE | JT_EXTENDS | JT_SATISFIES)) == 0) {
                    return true;
                }
                // otherwise just go on
            }
            
            if(!tp.getSatisfiedTypes().isEmpty()){
                boolean needsCastForBounds = false;
                for(Type bound : tp.getSatisfiedTypes()){
                    bound = bound.substitute(simpleType);
                    needsCastForBounds |= expressionGen().needsCast(ta, bound, false, false, false);
                }
                if(needsCastForBounds){
                    // replace with the first bound
                    ta = tp.getSatisfiedTypes().get(0).substitute(simpleType);
                    if(tp.getSatisfiedTypes().size() > 1
                            || isBoundsSelfDependant(tp)
                            || willEraseToObject(ta)
                            // we should reject it for all non-covariant types, unless we're in satisfies/extends
                            || ((flags & (JT_SATISFIES | JT_EXTENDS)) == 0 && !simpleType.isCovariant(tp))){
                        // A bit ugly, but we need to escape from the loop and create a raw type, no generics
                        return true;
                    }
                }
            }
            
            if (ta.isNothing()
                    // if we're in a type argument, extends or satisfies already, union and intersection types should 
                    // use the same erasure rules as bottom: prefer wildcards
                    || ((flags & (__JT_FULL_TYPE | JT_EXTENDS | JT_SATISFIES)) != 0
                        && (typeFact().isUnion(ta) || typeFact().isIntersection(ta)))) {
                // For the bottom type Bottom:
                if ((flags & (JT_CLASS_NEW)) != 0) {
                    // - The Ceylon type Foo<Bottom> or Foo<erased_type> appearing in an instantiation
                    //   clause results in the Java raw type Foo
                    // A bit ugly, but we need to escape from the loop and create a raw type, no generics
                    return true;
                }
            }
        }
        // deal with supertypes which were raw
        for (Type superType : ceylonType.getSatisfiedTypes()) {
            if (rawSupertype(superType, flags)) {
                return true;
            }
        }
        return rawSupertype(ceylonType.getExtendedType(), flags);
    }
    
    JCExpression makeJavaType(final Type ceylonType, final int flags) {
        Type type = ceylonType;
        if(type == null || type.isUnknown())
            return make().Erroneous();
        
        if (type.getDeclaration() instanceof Constructor) {
            type = type.getExtendedType();
        }
        
        // resolve aliases
        if((flags & JT_CLASS_LITERAL) == 0)
            type = type.resolveAliases();
        
        if ((flags & __JT_RAW_TP_BOUND) != 0
                && type.isTypeParameter()) {
            type = type.getExtendedType();    
        }
        
        if(type.isUnion()){
            for (Type pt : type.getCaseTypes()){
                if(pt.getDeclaration().isAnonymous()){
                    // found one, let's try to make it simpler
                    Type simplerType = typeFact().denotableType(type);
                    if(!simplerType.isNothing()
                            && !simplerType.isUnion()){
                        type = simplerType;
                    } else if (isCeylonBoolean(simplifyType(simplerType))) {
                        type = simplerType;
                    }
                    break;
                }
            }
        }
        if(type.getDeclaration().isJavaEnum()){
            type = type.getExtendedType();
        }
        
        if (type.isTypeConstructor()) {
            return make().QualIdent(syms().ceylonAbstractTypeConstructorType.tsym);
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
        } else if (willEraseToAnnotation(type)) {
            return make().Type(syms().annotationType);
        } else if (willEraseToException(type)) {
            if ((flags & JT_CLASS_NEW) != 0
                    || (flags & JT_EXTENDS) != 0) {
                return makeIdent(syms().ceylonExceptionType);
            } else {
                return make().Type(syms().exceptionType);
            }
        } else if (willEraseToThrowable(type)) {
            if ((flags & JT_CLASS_NEW) != 0
                    || (flags & JT_EXTENDS) != 0) {
                return makeIdent(syms().throwableType);
            } else {
                return make().Type(syms().throwableType);
            }
        } else if (willEraseToSequence(type)) {
            if ((flags & (JT_CLASS_NEW | JT_EXTENDS | JT_IS)) == 0) {
                Type typeArg = simplifyType(type).getTypeArgumentList().get(0);
                Type seqType = typeFact.getSequenceType(typeArg);
                if (typeFact.isOptionalType(type)) {
                    type = typeFact.getOptionalType(seqType);
                } else {
                    type = seqType;
                }
            }
        } else if ((flags & (JT_SATISFIES | JT_EXTENDS | JT_NO_PRIMITIVES | JT_CLASS_NEW)) == 0
                && ((isCeylonBasicType(type) && !isOptional(type)) || isJavaString(type))) {
            if (isCeylonString(type) || isJavaString(type)) {
                return make().Type(syms().stringType);
            } else if (isCeylonBoolean(type)) {
                return make().TypeIdent(TypeTags.BOOLEAN);
            } else if (isCeylonInteger(type)) {
                if ("short".equals(type.getUnderlyingType())) {
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
            } else if (isCeylonByte(type)) {
                return make().TypeIdent(TypeTags.BYTE);
            }
        } else if (isCeylonBoolean(type)
                && !isTypeParameter(type)) {
                //&& (flags & TYPE_ARGUMENT) == 0){
            // special case to get rid of $true and $false types
            type = typeFact.getBooleanType();
        } else if ((flags & JT_VALUE_TYPE) == 0 && isJavaArray(type)){
            return getJavaArrayElementType(type, flags);
        }
        
        JCExpression jt = null;
        
        Type simpleType;
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
        
        java.util.List<Reference> qualifyingTypes = null;
        Reference qType = simpleType;
        boolean hasTypeParameters = false;
        while (qType != null) {
            hasTypeParameters |= !qType.getTypeArguments().isEmpty();
            if(qualifyingTypes != null)
                qualifyingTypes.add(qType);
            Declaration typeDeclaration = qType.getDeclaration();
            // local interfaces that are pulled to the toplevel need to cross containing methods to find
            // all the containing type parameters that it captures
            if((Decl.isLocal(typeDeclaration) || !typeDeclaration.isNamed()) // local or anonymous
                    && needsQualifyingTypeArgumentsFromLocalContainers
                    && typeDeclaration instanceof ClassOrInterface){
                Declaration container = Decl.getDeclarationScope(typeDeclaration.getContainer());
                while (container instanceof Function) {
                    qType = ((Function)container).getReference();
                    if (qualifyingTypes == null) { 
                        qualifyingTypes = new java.util.ArrayList<Reference>();
                        qualifyingTypes.add(simpleType);
                    }
                    hasTypeParameters = true;
                    qualifyingTypes.add(qType);
                    container = Decl.getDeclarationScope(container.getContainer());
                }
                if (container instanceof TypeDeclaration) {
                    qType = ((TypeDeclaration)container).getType();
                }else {
                    qType = null;
                }
            }else if(typeDeclaration.isNamed()){ // avoid anonymous types which may pretend that they have a qualifying type
                qType = qType.getQualifyingType();
                if(qType != null && qType.getDeclaration() instanceof ClassOrInterface == false){
                    // sometimes the typechecker throws qualifying intersections at us and
                    // we can't make anything of them, since some members may be unrelated to
                    // the qualified declaration. This happens with "extends super.Foo()"
                    // for example. See https://github.com/ceylon/ceylon-compiler/issues/1478
                    qType = ((Type)qType).getSupertype((TypeDeclaration) typeDeclaration.getContainer());
                }
            }else{
                // skip local declaration containers
                qType = null;
            }
            // delayed allocation if we have a qualifying type
            if(qualifyingTypes == null && qType != null){
                qualifyingTypes = new java.util.ArrayList<Reference>();
                qualifyingTypes.add(simpleType);
            }
        }
        int firstQualifyingTypeWithTypeParameters = qualifyingTypes != null ? qualifyingTypes.size() - 1 : 0;
        // find the first static one, from the right to the left
        if(qualifyingTypes != null){
            for(Reference pt : qualifyingTypes){
                Declaration declaration = pt.getDeclaration();
                if(declaration instanceof TypeDeclaration && Decl.isStatic((TypeDeclaration)declaration)){
                    break;
                }
                firstQualifyingTypeWithTypeParameters--;
            }
            if(firstQualifyingTypeWithTypeParameters < 0)
                firstQualifyingTypeWithTypeParameters = 0;
            // put them in outer->inner order
            Collections.reverse(qualifyingTypes);
        }
        
        if (((flags & JT_RAW) == 0) && hasTypeParameters
                && !rawSupertype(ceylonType, flags)) {
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
                java.util.Map<TypeParameter, Type> qualifyingTypeArguments = new java.util.HashMap<TypeParameter, Type>();
                collectQualifyingTypeArguments(qualifyingTypeParameters, qualifyingTypeArguments, qualifyingTypes);
                
                ListBuffer<JCExpression> typeArgs = makeTypeArgs(isCeylonCallable(simpleType), 
                        flags, 
                        qualifyingTypeArguments, qualifyingTypeParameters, simpleType);
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
                    for (Reference qualifyingType : qualifyingTypes) {
                        jt = makeParameterisedType(qualifyingType.getType(), type, flags, jt, qualifyingTypes, firstQualifyingTypeWithTypeParameters, index);
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
            if (isCeylonCallable(type) && 
                    (flags & JT_CLASS_NEW) != 0) {
                jt = makeIdent(syms().ceylonAbstractCallableType);
            } else if(tdecl instanceof TypeParameter)
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
            Map<TypeParameter, Type> qualifyingTypeArguments, 
            java.util.List<Reference> qualifyingTypes) {
        // make sure we only add type parameters with the same name once, as duplicates are erased from the target interface
        // since they cannot be accessed
        Set<String> names = new HashSet<String>();
        // walk the qualifying types backwards to make sure we only add a TP with the same name once and the outer one wins
        for (int i = qualifyingTypes.size()-1 ; i >= 0 ; i--) {
            Reference qualifiedType = qualifyingTypes.get(i);
            Map<TypeParameter, Type> tas = qualifiedType.getTypeArguments();
            java.util.List<TypeParameter> tps = ((Generic)qualifiedType.getDeclaration()).getTypeParameters();
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
            Declaration declaration = qualifiedType.getDeclaration();
            if(Decl.isLocal(declaration)){
                Scope scope = declaration.getContainer();
                // collect every container method until the next type or package
                java.util.List<Function> methods = new LinkedList<Function>();
                while(scope != null
                        && scope instanceof ClassOrInterface == false
                        && scope instanceof Package == false){
                    if(scope instanceof Function){
                        methods.add((Function) scope);
                    }
                    scope = scope.getContainer();
                }
                // methods are sorted inner to outer, which is the order we're following here for types
                for(Function method : methods){
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

    protected static final class MultidimensionalArray {
        public final int dimension;
        public final Type type;
        MultidimensionalArray(int dimension, Type type){
            this.dimension = dimension;
            this.type = type;
        }
    }

    protected MultidimensionalArray getMultiDimensionalArrayInfo(Type type) {
        int dimension = 0;
        while(isJavaObjectArray(type)){
            type = type.getTypeArgumentList().get(0);
            dimension++;
        }
        if(dimension == 0)
            return null;
        return new MultidimensionalArray(dimension, type);
    }

    public boolean isJavaArray(Type type) {
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

    public boolean isJavaObjectArray(Type type) {
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

    private JCExpression getJavaArrayElementType(Type type, int flags) {
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
            Type elementType = type.getTypeArgumentList().get(0);
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
    
    boolean isJavaEnumType(Type type) {
        Module jdkBaseModule = loader().getJDKBaseModule();
        Package javaLang = jdkBaseModule.getPackage("java.lang");
        TypeDeclaration enumDecl = (TypeDeclaration)javaLang.getDirectMember("Enum", null, false);
        if (type.isClass() && type.getDeclaration().isAnonymous()) {
            type = type.getExtendedType();
        }
        return type.isSubtypeOf(enumDecl.appliedType(null, Collections.singletonList(type)));
    }

    public JCExpression makeParameterisedType(Type type, Type generalType, final int flags, 
            JCExpression qualifyingExpression, java.util.List<Reference> qualifyingTypes, 
            int firstQualifyingTypeWithTypeParameters, int index) {
        JCExpression baseType;
        TypeDeclaration tdecl = type.getDeclaration();
        ListBuffer<JCExpression> typeArgs = null;
        if(index >= firstQualifyingTypeWithTypeParameters) {
            int taFlags = flags;
            if (qualifyingTypes != null && index < qualifyingTypes.size()) {
                // The qualifying types before the main one should
                // have type parameters with proper variance
                taFlags &= ~(JT_EXTENDS | JT_SATISFIES);
            }
            typeArgs = makeTypeArgs( 
                    type,
                    taFlags);
        }
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
                    && firstQualifyingTypeWithTypeParameters == 0
                    && (flags & JT_NON_QUALIFIED) == 0) {
                baseType = naming.makeCompanionClassName(tdecl);
            } else {
                baseType = naming.makeDeclarationName(tdecl, jtFlagsToDeclNameOpts(flags));
            }
            
        } else {
            baseType = naming.makeTypeDeclarationExpression(qualifyingExpression, tdecl, 
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
            Type simpleType, 
            int flags) {
        Map<TypeParameter, Type> tas = simpleType.getTypeArguments();
        java.util.List<TypeParameter> tps = simpleType.getDeclaration().getTypeParameters();
        

        return makeTypeArgs(isCeylonCallable(simpleType), flags, tas, tps, simpleType);
    }

    private ListBuffer<JCExpression> makeTypeArgs(boolean isCeylonCallable,
            int flags, Map<TypeParameter, Type> tas,
            java.util.List<TypeParameter> tps, Type simpleType) {
        ListBuffer<JCExpression> typeArgs = new ListBuffer<JCExpression>();
        
        for (TypeParameter tp : tps) {
            Type ta = tas.get(tp);
            // error handling
            if(ta == null)
                continue;
            
            boolean isDependedOn = hasDependentTypeParameters(tps, tp);
            
            // record whether we were initially working with Anything, because getNonNullType turns it into Object
            // and we need to treat "in Anything" specially below
            boolean isAnything = isAnything(ta);
            
            // Null will claim to be optional, but if we get its non-null type we will land with Nothing, which is not what
            // we want, so we make sure it's not Null
            if (isOptional(ta) && !isNull(ta)) {
                // For an optional type T?:
                // - The Ceylon type Foo<T?> results in the Java type Foo<T>.
                ta = getNonNullType(ta);
            }
            // In a type argument Foo<X&Object> or Foo<X?> transform to just Foo<X>
            ta = simplifyType(ta);
            if (typeFact().isUnion(ta) || typeFact().isIntersection(ta)) {
                // For any other union type U|V (U nor V is Optional):
                // - The Ceylon type Foo<U|V> results in the raw Java type Foo.
                // For any other intersection type U&V:
                // - The Ceylon type Foo<U&V> results in the raw Java type Foo.
                // use raw types if:
                // - we're not in a type argument (when used as type arguments raw types have more constraint than at the toplevel)
                //   or we're in an extends or satisfies and the type parameter is a self type
                // Note: it used to be we used raw types when calling constructors, but that was wrong as it did not
                // conform with where raw types would be used between expressions and constructors
                if(((flags & (JT_EXTENDS | JT_SATISFIES)) != 0 && tp.getSelfTypedDeclaration() != null)){
                    // A bit ugly, but we need to escape from the loop and create a raw type, no generics
                    if ((flags & (JT_EXTENDS | JT_SATISFIES)) != 0) throw new BugException("rawSupertype() should prevent this method going raw when JT_EXTENDS | JT_SATISFIES");
                    typeArgs = null;
                    break;
                } else if ((flags & (__JT_FULL_TYPE | JT_EXTENDS | JT_SATISFIES)) == 0) {
                    if ((flags & (JT_EXTENDS | JT_SATISFIES)) != 0) throw new BugException("rawSupertype() should prevent this method going raw when JT_EXTENDS | JT_SATISFIES");
                    typeArgs = null;
                    break;
                }
                // otherwise just go on
            }
            if (isCeylonBoolean(ta)
                    && !isTypeParameter(ta)) {
                ta = typeFact.getBooleanType();
            } 
            JCExpression jta;
            
            if(!tp.getSatisfiedTypes().isEmpty()){
                boolean needsCastForBounds = false;
                for(Type bound : tp.getSatisfiedTypes()){
                    bound = bound.substitute(tas, null);
                    needsCastForBounds |= expressionGen().needsCast(ta, bound, false, false, false);
                }
                if(needsCastForBounds){
                    // replace with the first bound
                    ta = tp.getSatisfiedTypes().get(0).substitute(tas, null);
                    if(tp.getSatisfiedTypes().size() > 1
                            || isBoundsSelfDependant(tp)
                            || willEraseToObject(ta)
                            // we should reject it for all non-covariant types, unless we're in satisfies/extends
                            || ((flags & (JT_SATISFIES | JT_EXTENDS)) == 0 && !simpleType.isCovariant(tp))){
                        if ((flags & (JT_EXTENDS | JT_SATISFIES)) != 0) throw new BugException("rawSupertype() should prevent this method going raw when JT_EXTENDS | JT_SATISFIES");
                        // A bit ugly, but we need to escape from the loop and create a raw type, no generics
                        typeArgs = null;
                        break;
                    }
                }
            }
            
            if (ta.isNothing()
                    // if we're in a type argument, extends or satisfies already, union and intersection types should 
                    // use the same erasure rules as bottom: prefer wildcards
                    || ((flags & (__JT_FULL_TYPE | JT_EXTENDS | JT_SATISFIES)) != 0
                        && (typeFact().isUnion(ta) || typeFact().isIntersection(ta)))) {
                // For the bottom type Bottom:
                if ((flags & (JT_CLASS_NEW)) != 0) {
                    // - The Ceylon type Foo<Bottom> or Foo<erased_type> appearing in an instantiation
                    //   clause results in the Java raw type Foo
                    // A bit ugly, but we need to escape from the loop and create a raw type, no generics
                    if ((flags & (JT_EXTENDS | JT_SATISFIES)) != 0) throw new BugException("rawSupertype() should prevent this method going raw when JT_EXTENDS | JT_SATISFIES");
                    typeArgs = null;
                    break;
                } else {
                    // - The Ceylon type Foo<Bottom> appearing in an extends or satisfies location results in the Java type
                    //   Foo<Object> (see https://github.com/ceylon/ceylon-compiler/issues/633 for why)
                    if((flags & (JT_SATISFIES | JT_EXTENDS)) != 0){
                        if (ta.isNothing()) {
                            jta = make().Type(syms().objectType);
                        } else {
                            if (!tp.getSatisfiedTypes().isEmpty()) {
                                // union or intersection: Use the common upper bound of the types
                                jta = makeJavaType(tp.getSatisfiedTypes().get(0), JT_TYPE_ARGUMENT);
                            } else {
                                jta = make().Type(syms().objectType);
                            }
                        }
                    }else if (ta.isNothing()){
                        // - The Ceylon type Foo<Bottom> appearing anywhere else results in the Java type
                        // - Foo if Foo is contravariant in T (see https://github.com/ceylon/ceylon-compiler/issues/1042), or
                        // - Foo<? extends Object> if Foo is covariant in T and not depended on by other type params
                        // - Foo<Object> otherwise
                        // this is more correct than Foo<?> because a method returning Foo<?> could never override a method returning Foo<Object>
                        // see https://github.com/ceylon/ceylon-compiler/issues/1003
                        if (simpleType.isContravariant(tp)) {
                            typeArgs = null;
                            break;
                        } else if (tp.isCovariant() && !isDependedOn) {
                            // DO NOT trust use-site covariance for Nothing, because we consider "out Nothing" to be the same
                            // as "Nothing". Only look at declaration-site covariance
                            jta = make().Wildcard(make().TypeBoundKind(BoundKind.EXTENDS), make().Type(syms().objectType));
                        } else {
                            jta = make().Type(syms().objectType);
                        }
                    }else{
                        // - The Ceylon type Foo<T> appearing anywhere else results in the Java type
                        // - Foo<T> if Foo is invariant in T,
                        // - Foo<? extends T> if Foo is covariant in T, or
                        // - Foo<? super T> if Foo is contravariant in T
                        if (((flags & JT_CLASS_NEW) == 0) && simpleType.isContravariant(tp)) {
                            jta = make().Wildcard(make().TypeBoundKind(BoundKind.SUPER), makeJavaType(ta, JT_TYPE_ARGUMENT));
                        } else if (((flags & JT_CLASS_NEW) == 0) && simpleType.isCovariant(tp) && !isDependedOn) {
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
                    if (((flags & JT_CLASS_NEW) == 0) 
                            && simpleType.isContravariant(tp)
                            && (!isAnything || tp.isContravariant())) {
                        // DO NOT trust use-site contravariance for Anything, because we consider "in Anything" to be the same
                        // as "Anything". Only look at declaration-site contravariance
                        jta = make().Wildcard(make().TypeBoundKind(BoundKind.SUPER), makeJavaType(ta, JT_TYPE_ARGUMENT));
                    } else if (((flags & JT_CLASS_NEW) == 0) && simpleType.isCovariant(tp) && !isDependedOn) {
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
        return typeArgs;
    }

    boolean hasSubstitutedBounds(Type pt){
        TypeDeclaration declaration = pt.getDeclaration();
        java.util.List<TypeParameter> tps = declaration.getTypeParameters();
        final Map<TypeParameter, Type> tas = pt.getTypeArguments();
        boolean isCallable = isCeylonCallable(pt);
        for(TypeParameter tp : tps){
            Type ta = tas.get(tp);
            // error recovery
            if(ta == null)
                continue;
            if(!tp.getSatisfiedTypes().isEmpty()){
                for(Type bound : tp.getSatisfiedTypes()){
                    bound = bound.substitute(pt);
                    if(expressionGen().needsCast(ta, bound, false, false, false))
                        return true;
                }
            }
            if(hasSubstitutedBounds(ta))
                return true;
            // Callable ignores type parameters after the first
            if(isCallable)
                break;
        }
        return false;
    }

    protected Type getNonNullType(Type pt) {
        // typeFact().getDefiniteType() intersects with Object, which isn't 
        // always right for working with the java type system.
        if (pt.isAnything()) {
            pt = typeFact().getObjectType();
        }
        else {
            pt = pt.eliminateNull();
        }
        return pt;
    }
    
    private boolean isJavaString(Type type) {
        return "java.lang.String".equals(type.getUnderlyingType());
    }
    
    private ClassDefinitionBuilder ccdb;
    
    public ClassDefinitionBuilder current() {
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
    Type getReturnTypeOfCallable(Type typeModel) {
        if (!isCeylonCallableSubtype(typeModel)) {
            throw new BugException("expected Callable<...>, but was " + typeModel);
        } else if (typeFact().getNothingType().isExactly(typeModel)) {
            return typeFact().getNothingType();
        }
        Type ct = typeModel.getSupertype(typeFact().getCallableDeclaration());
        return ct.getTypeArgumentList().get(0);
    }
    
    Type getParameterTypeOfCallable(Type callableType, int parameter) {
        if (!isCeylonCallableSubtype(callableType)) {
            throw new BugException("expected Callable<...>, but was " + callableType);
        }
        Type tuple = typeFact().getCallableTuple(callableType);
        if(tuple != null){
            java.util.List<Type> elementTypes = typeFact().getTupleElementTypes(tuple);
            if(elementTypes.size() > parameter){
                return elementTypes.get(parameter);
            }
        }
        return typeFact().getUnknownType();
    }
    
    int getNumParameterLists(Type typeModel) {
        int result = 0;
        while (isCeylonCallableSubtype(typeModel)) {
            result++;
            typeModel = getReturnTypeOfCallable(typeModel);
        }
        return result;
    }
    
    /**
     * Returns true if any part of the given Callable is unknown, like Callable&lt;Ret,Args>
     */
    boolean isUnknownArgumentsCallable(Type callableType) {
        if (typeFact().getNothingType().isExactly(callableType)) {
            return false;
        }
        Type args = typeFact().getCallableTuple(callableType);
        return isUnknownTuple(args);
    }
    
    private boolean isUnknownTuple(Type args) {
        if (args.isTypeParameter()) {
            return true;
        } else if (args.isUnion()){
            /* Callable<R,A>&Callable<R,B> is the same as Callable<R,A|B> so 
             * for a union if either A or B is known then the union is known
             */
            java.util.List<Type> caseTypes = args.getCaseTypes();
            if(caseTypes == null || caseTypes.size() < 2)
                return true;
            for (int ii = 0; ii < caseTypes.size(); ii++) {
                if (!isUnknownTuple(caseTypes.get(ii))) {
                    return false;
                }
            }// all unknown
            return true;
        } else if (args.isIntersection()) {
            /* Callable<R,A>|Callable<R,B> is the same as Callable<R,A&B> so 
             * for an intersection if both A and B are known then the intersection is known
             */
            java.util.List<Type> caseTypes = args.getSatisfiedTypes();
            if(caseTypes == null || caseTypes.size() < 2)
                return true;
            for (int ii = 0; ii < caseTypes.size(); ii++) {
                if (isUnknownTuple(caseTypes.get(ii))) {
                    return true;
                }
            }
            return false;
        } else if (args.isNothing()) {
            return true;
        } else if(args.isClassOrInterface()) {
            TypeDeclaration declaration = args.getDeclaration();
            String name = declaration.getQualifiedNameString();
            if(name.equals("ceylon.language::Tuple")){
                Type rest = args.getTypeArgumentList().get(2);
                return isUnknownTuple(rest);
            }
            if(name.equals("ceylon.language::Empty")){
                return false;
            }
            if(name.equals("ceylon.language::Sequential")
               || name.equals("ceylon.language::Sequence")){
                return false;
            }
        } else if (args.isTypeAlias()) {
            return isUnknownTuple(args.resolveAliases());
        }
        return true;
        
    }

    int getNumParametersOfCallable(Type callableType) {
        if (typeFact().getNothingType().isExactly(callableType)) {
            return 0;
        }
        Type tuple = typeFact().getCallableTuple(callableType);
        int simpleNumParametersOfCallable = getSimpleNumParametersOfCallable(tuple);
        if(simpleNumParametersOfCallable != -1)
            return simpleNumParametersOfCallable;
        int count = 0;
        while (tuple != null) {
            Type tst = typeFact().nonemptyArgs(tuple).getSupertype(typeFact().getTupleDeclaration());
            if (tst!=null) {
                java.util.List<Type> tal = tst.getTypeArgumentList();
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
    
    private int getSimpleNumParametersOfCallable(Type args) {
        // can be a defaulted tuple of Empty|Tuple
        if(args.isUnion()){
            java.util.List<Type> caseTypes = args.getCaseTypes();
            if(caseTypes == null || caseTypes.size() != 2)
                return -1;
            Type caseA = caseTypes.get(0);
            TypeDeclaration caseADecl = caseA.getDeclaration();
            Type caseB = caseTypes.get(1);
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
        if(!args.isClassOrInterface())
            return -1;
        TypeDeclaration declaration = args.getDeclaration();
        String name = declaration.getQualifiedNameString();
        if(name.equals("ceylon.language::Tuple")){
            Type rest = args.getTypeArgumentList().get(2);
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

    boolean isVariadicCallable(Type callableType) {
        if (typeFact().getNothingType().isExactly(callableType)) {
            return true;
        }
        Type tuple = typeFact().getCallableTuple(callableType);
        return typeFact().isTupleOfVariadicCallable(tuple);
    }

    public int getMinimumParameterCountForCallable(Type callableType) {
        Type tuple = typeFact().getCallableTuple(callableType);
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
    
    Type getTypeForParameter(Parameter parameter, Reference producedReference, int flags) {
        /* this method is bogus: It's really trying to answer 
         * "what's the type of the java declaration of the given parameter", 
         * but using the ceylon type system to do so. 
         */
        boolean functional = parameter.getModel() instanceof Function;
        if (producedReference == null) {
            return parameter.getType();
        }
        final TypedReference producedTypedReference = producedReference.getTypedParameter(parameter);
        final Type type = functional ? producedTypedReference.getFullType() : producedTypedReference.getType();
        final TypedDeclaration producedParameterDecl = producedTypedReference.getDeclaration();
        final Type declType = producedParameterDecl.getType();
        // be more resilient to upstream errors
        if(declType == null)
            return typeFact.getUnknownType();
        if(isJavaVariadic(parameter) && (flags & TP_SEQUENCED_TYPE) == 0){
            // type of param must be Iterable<T>
            Type elementType = typeFact.getIteratedType(type);
            if(elementType == null){
                log.error("ceylon", "Invalid type for Java variadic parameter: "+type.asQualifiedString());
                return type;
            }
            return elementType;
        }
        if (declType.isClassOrInterface()) {
            return type;
        } else if ((declType.isTypeParameter())
                && (flags & TP_TO_BOUND) != 0) {
            if(!declType.getSatisfiedTypes().isEmpty()){
                // use upper bound
                Type upperBound = declType.getSatisfiedTypes().get(0);
                // make sure we apply the type arguments
                upperBound = substituteTypeArgumentsForTypeParameterBound(producedReference, upperBound);
                Type self = upperBound.getDeclaration().getSelfType();
                if (self != null) {
                    // make sure we apply the type arguments
                    Type selfUpperBound = self.substitute(upperBound);
                    if (!willEraseToObject(selfUpperBound)
                            && (willEraseToObject(type) || expressionGen().needsCast(type, selfUpperBound, false, false, false))) {
                        return selfUpperBound;
                    }
                } 
                if (!willEraseToObject(upperBound)
                        && (willEraseToObject(type) || expressionGen().needsCast(type, upperBound, false, false, false))) {
                    return upperBound;
                }
            }
        }
        return type;
    }

    protected Type substituteTypeArgumentsForTypeParameterBound(
            Reference target, Type bound) {
        Declaration declaration = target.getDeclaration();
        if(declaration.getContainer() instanceof ClassOrInterface){
            Type targetType = target.getQualifyingType();
            // static methods have a container but do not capture type parameters
            if(targetType != null
                    && !declaration.isStaticallyImportable()){
                ClassOrInterface methodContainer = (ClassOrInterface) declaration.getContainer();
                Type supertype = targetType.getSupertype(methodContainer);
                // we need type arguments that may come from the method container
                bound = bound.substitute(supertype);
            }
        }
        // and those that may come from the method call itself
        return bound.substitute(target.getTypeArguments(), null);
    }


    private boolean isJavaVariadic(Parameter parameter) {
        return parameter.isSequenced()
                && parameter.getDeclaration() instanceof Function
                && isJavaMethod((Function) parameter.getDeclaration());
    }

    boolean isJavaMethod(Function method) {
        ClassOrInterface container = Decl.getClassOrInterfaceContainer(method);
        return container != null && !Decl.isCeylon(container);
    }
    
    boolean isJavaCtor(Class cls) {
        return !Decl.isCeylon(cls);
    }

    Type getTypeForFunctionalParameter(Function fp) {
        return fp.appliedTypedReference(null, java.util.Collections.<Type>emptyList()).getFullType();
    }
    
    /*
     * Annotation generation
     */
    
    List<JCAnnotation> makeAtCompileTimeError() {
        return List.of(make().Annotation(makeIdent(syms().ceylonAtCompileTimeErrorType), List.<JCExpression> nil()));
    }
    
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

    private List<JCAnnotation> makeModelAnnotation(com.sun.tools.javac.code.Type annotationType, List<JCExpression> annotationArgs) {
        if ((gen().disableAnnotations & CeylonTransformer.DISABLE_MODEL_ANNOS) != 0)
            return List.nil();
        return List.of(make().Annotation(makeIdent(annotationType), annotationArgs));
    }
    
    private List<JCAnnotation> makeAnnoAnnotation(com.sun.tools.javac.code.Type annotationType, List<JCExpression> annotationArgs) {
        return List.of(make().Annotation(makeIdent(annotationType), annotationArgs));
    }

    private List<JCAnnotation> makeModelAnnotation(com.sun.tools.javac.code.Type annotationType) {
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

    List<JCAnnotation> makeAtDynamic() {
        return makeModelAnnotation(syms().ceylonAtDynamicType);
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
            if (!isForBackend(dependency.getNativeBackend(), Backend.Java)) {
                continue;
            }
            Module dependencyModule = dependency.getModule();
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
    
    List<JCAnnotation> makeAtEnumerated() {
        return makeModelAnnotation(syms().ceylonAtEnumeratedType, List.<JCExpression>nil());
    }

    List<JCAnnotation> makeAtAlias(Type type, Constructor constructor) {
        
        List<JCExpression> attributes = List.<JCExpression>nil();
        
        if (constructor != null
                && !Decl.isDefaultConstructor(constructor)) {
            attributes = attributes.prepend(make().Assign(naming.makeUnquotedIdent("constructor"), make().Literal(constructor.getName())));
        }
        
        String name = serialiseTypeSignature(type);
        attributes = attributes.prepend(make().Assign(naming.makeUnquotedIdent("value"), make().Literal(name)));
        return makeModelAnnotation(syms().ceylonAtAliasType, attributes);
    }

    List<JCAnnotation> makeAtTypeAlias(Type type) {
        String name = serialiseTypeSignature(type);
        return makeModelAnnotation(syms().ceylonAtTypeAliasType, List.<JCExpression>of(make().Literal(name)));
    }

    final JCAnnotation makeAtTypeParameter(String name, java.util.List<Type> satisfiedTypes, java.util.List<Type> caseTypes, 
                                           boolean covariant, boolean contravariant, Type defaultValue) {
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
        for(Type satisfiedType : satisfiedTypes){
            String type = serialiseTypeSignature(satisfiedType);
            upperBounds.append(make().Literal(type));
        }
        JCExpression satisfiesAttribute = make().Assign(naming.makeUnquotedIdent("satisfies"), 
                make().NewArray(null, null, upperBounds.toList()));
        attributes.add(satisfiesAttribute);
        
        // case types
        ListBuffer<JCExpression> caseTypesExpressions = new ListBuffer<JCTree.JCExpression>();
        if(caseTypes != null){
            for(Type caseType : caseTypes){
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
    
    List<JCAnnotation> makeAtFunctionalParameter(String value) {
        return makeModelAnnotation(syms().ceylonAtFunctionalParameterType, 
                List.<JCExpression>of(make().Literal(value)));
    }

    List<JCAnnotation> makeAtDefaulted() {
        return makeModelAnnotation(syms().ceylonAtDefaultedType);
    }

    List<JCAnnotation> makeAtAttribute(JCExpression setterClass) {
        List<JCExpression> attributes = List.nil();
        if (setterClass != null) {
            JCExpression setterClassAttribute = make().Assign(naming.makeUnquotedIdent("setterClass"), setterClass);
            attributes = attributes.prepend(setterClassAttribute);
        }
        return makeModelAnnotation(syms().ceylonAtAttributeType, attributes);
    }

    List<JCAnnotation> makeAtSetter(JCExpression setterClass) {
        List<JCExpression> attributes = List.nil();
        if (setterClass != null) {
            JCExpression setterClassAttribute = make().Assign(naming.makeUnquotedIdent("getterClass"), setterClass);
            attributes = attributes.prepend(setterClassAttribute);
        }
        return makeModelAnnotation(syms().ceylonAtSetterType, attributes);
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
    
    List<JCAnnotation> makeAtClass(Type thisType, Type extendedType, boolean hasConstructors) {
        
        boolean isBasic = true;
        boolean isIdentifiable = true;
        boolean isAnything = extendedType == null 
                && thisType != null 
                && thisType.isExactly(typeFact().getAnythingType());
        if(isAnything){
            // special for Anything
            isBasic = isIdentifiable = false;
        }else if(thisType != null){
            isBasic = thisType.getSupertype(typeFact.getBasicDeclaration()) != null;
            // if isBasic, then isIdentifiable remains true
            if(!isBasic)
                isIdentifiable = thisType.getSupertype(typeFact.getIdentifiableDeclaration()) != null;
        }
        
        String extendedTypeSig = null;
        if (isAnything) {
            extendedTypeSig = "";
        } else if (extendedType != null && !extendedType.isExactly(typeFact.getBasicType())){
            extendedTypeSig = serialiseTypeSignature(extendedType);
        }
        
        List<JCExpression> attributes = List.nil();
        if (extendedTypeSig != null) {
            JCExpression extendsAttribute = make().Assign(naming.makeUnquotedIdent("extendsType"), make().Literal(extendedTypeSig));
            attributes = attributes.prepend(extendsAttribute);
        }
        
        if (!isBasic) {
            JCExpression basicAttribute = make().Assign(naming.makeUnquotedIdent("basic"), makeBoolean(false));
            attributes = attributes.prepend(basicAttribute);
        }
        if (!isIdentifiable) {
            JCExpression identifiableAttribute = make().Assign(naming.makeUnquotedIdent("identifiable"), makeBoolean(false));
            attributes = attributes.prepend(identifiableAttribute);
        }
        if (hasConstructors) {
            JCExpression constructorsAttribute = make().Assign(naming.makeUnquotedIdent("constructors"), makeBoolean(true));
            attributes = attributes.prepend(constructorsAttribute);
        }
        return makeModelAnnotation(syms().ceylonAtClassType, attributes);
    }

    List<JCAnnotation> makeAtSatisfiedTypes(java.util.List<Type> satisfiedTypes) {
        JCExpression attrib = makeTypesListAttr(satisfiedTypes);
        if (attrib != null) {
            return makeModelAnnotation(syms().ceylonAtSatisfiedTypes, List.of(attrib));
        } else {
            return List.nil();
        }
    }

    List<JCAnnotation> makeAtCaseTypes(java.util.List<Type> caseTypes, Type ofType) {
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

    private JCExpression makeTypesListAttr(java.util.List<Type> types) {
        if(types.isEmpty())
            return null;
        ListBuffer<JCExpression> upperBounds = new ListBuffer<JCTree.JCExpression>();
        for(Type type : types){
            String typeSig = serialiseTypeSignature(type);
            upperBounds.append(make().Literal(typeSig));
        }
        JCExpression caseAttribute = make().Assign(naming.makeUnquotedIdent("value"), 
                make().NewArray(null, null, upperBounds.toList()));
        return caseAttribute;
    }

    private JCExpression makeOfTypeAttr(Type ofType) {
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
    
    List<JCAnnotation> makeAtConstructorName(String name, boolean delegation) {
        List<JCExpression> ass = List.<JCExpression>of(
                make().Assign(naming.makeUnquotedIdent("value"), make().Literal(name == null ? "" : name)));
        if (delegation) {
            ass = ass.prepend(make().Assign(naming.makeUnquotedIdent("delegation"), make().Literal(true)));
        } 
        return makeModelAnnotation(syms().ceylonAtConstructorName, ass);
    }
    
    List<JCAnnotation> makeAtNoInitCheck() {
        return List.<JCAnnotation> of(make().Annotation(makeIdent(syms().ceylonAtNoInitCheckType), List.<JCExpression> nil()));
    }

    List<JCAnnotation> makeAtTransient() {
        return makeModelAnnotation(syms().ceylonAtTransientType);
    }

    protected void initModelAnnotations() {
        if (gen().omittedModelAnnotations == null) {
            HashMap<String, Long> map = new HashMap<String, Long>();
            for (LanguageAnnotation mod : LanguageAnnotation.values()) {
                map.put(mod.name, mod.mask);
            }
            gen().omittedModelAnnotations = map;
        }
    }
    
    /** 
     * Whether the given {@code Annotation} should be included in the 
     * {@code @Annotations} model annotation.
     * */
    boolean isOmittedModelAnnotation(Annotation annotation) {
        initModelAnnotations();
        return !gen().omittedModelAnnotations.containsKey(annotation.getName());
    }
    
    Long getModelModifierMask(Annotation annotation) {
        initModelAnnotations();
        return gen().omittedModelAnnotations.get(annotation.getName());
    }
    
    List<JCAnnotation> makeAtAnnotations(java.util.List<Annotation> annotations) {
        if(!simpleAnnotationModels || annotations == null || annotations.isEmpty())
            return List.nil();
        long modifiers = 0;
        ListBuffer<JCExpression> array = new ListBuffer<JCTree.JCExpression>();
        for(Annotation annotation : annotations){
            if (isOmittedModelAnnotation(annotation)) {
                continue;
            }
            Long mask = getModelModifierMask(annotation);
            if (mask != null && mask != 0){
                modifiers |= mask;
            } else {
                array.append(makeAtAnnotation(annotation));
            }
        }
        if (modifiers == 0 && array.isEmpty()) {
            return List.<JCAnnotation>nil();
        }
        List<JCExpression> annotationsAndMods = List.<JCExpression>nil();
        if (!array.isEmpty()) {
            annotationsAndMods = annotationsAndMods.prepend(make().Assign(naming.makeUnquotedIdent("value"), 
                        make().NewArray(null, null, array.toList())));
        }
        if (modifiers != 0L) {
            annotationsAndMods = annotationsAndMods.prepend(
                    make().Assign(naming.makeUnquotedIdent("modifiers"), 
                            make().Literal(modifiers)));
        }
        return makeModelAnnotation(syms().ceylonAtAnnotationsType, annotationsAndMods);
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

    List<JCAnnotation> makeAtContainer(Type type) {
        JCExpression classAttribute = make().Assign(naming.makeUnquotedIdent("klass"), 
                                                    makeClassLiteral(type));
        List<JCExpression> attributes = List.of(classAttribute);

        return makeModelAnnotation(syms().ceylonAtContainerType, attributes);
    }

    List<JCAnnotation> makeAtLocalDeclaration(String qualifier, boolean skipContainerClass) {
        List<JCExpression> attributes = List.nil();
        if(qualifier != null && !qualifier.isEmpty()){
            JCExpression scopeAttribute = make().Assign(naming.makeUnquotedIdent("qualifier"), 
                                                        make().Literal(qualifier));
            attributes = List.of(scopeAttribute);
        }
        if(skipContainerClass){
            JCExpression skipAttribute = make().Assign(naming.makeUnquotedIdent("isPackageLocal"), 
                                                        make().Literal(true));
            attributes = attributes.prepend(skipAttribute);
        }
        return makeModelAnnotation(syms().ceylonAtLocalDeclarationType, attributes);
    }

    JCAnnotation makeAtMember(Type type) {
        JCExpression classAttribute = make().Assign(naming.makeUnquotedIdent("klass"), 
                                                    makeClassLiteral(type));
        List<JCExpression> attributes = List.of(classAttribute);

        return make().Annotation(makeIdent(syms().ceylonAtMemberType), attributes);
    }

    JCAnnotation makeAtMember(String typeName) {
        JCExpression classAttribute = make().Assign(naming.makeUnquotedIdent("javaClassName"), 
                                                    make().Literal(typeName));
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
    
    private List<JCAnnotation> makeAtLocalDeclarations(Set<String> localDeclarations, Set<Interface> localInterfaces) {
        if(localDeclarations.isEmpty() && localInterfaces.isEmpty())
            return List.nil();
        ListBuffer<JCExpression> array = new ListBuffer<JCTree.JCExpression>();
        // sort them to get the same behaviour on every JDK
        SortedSet<String> sortedNames = new TreeSet<String>();
        sortedNames.addAll(localDeclarations);
        for(Interface iface : localInterfaces){
            sortedNames.add("::"+naming.makeTypeDeclarationName(iface));
        }
        
        for(String val : sortedNames)
            array.add(make().Literal(val));
        JCExpression attr = make().Assign(naming.makeUnquotedIdent("value"), 
                                          make().NewArray(null, null, array.toList()));

        return makeModelAnnotation(syms().ceylonAtLocalDeclarationsType, List.of(attr));
    }

    protected List<JCAnnotation> makeAtLocalContainer(List<String> path, String companionClassName) {
        if(path.isEmpty())
            return List.nil();
        ListBuffer<JCExpression> array = new ListBuffer<JCTree.JCExpression>();
        for(String val : path)
            array.add(make().Literal(val));

        JCExpression pathAttr = make().Assign(naming.makeUnquotedIdent("path"), 
                                          make().NewArray(null, null, array.toList()));
        JCExpression companionAttr = make().Assign(naming.makeUnquotedIdent("companionClassName"), 
                                                   make().Literal(companionClassName == null ? "" : companionClassName));

        return makeModelAnnotation(syms().ceylonAtLocalContainerType, List.of(pathAttr, companionAttr));
    }

    protected List<JCAnnotation> makeAtLocalDeclarations(Node tree) {
        return makeAtLocalDeclarations(tree, null);
    }

    protected List<JCAnnotation> makeAtLocalDeclarations(Node tree1, Node tree2) {
        LocalTypeVisitor visitor = new LocalTypeVisitor();
        visitor.startFrom(tree1);
        if(tree2 != null){
            visitor.startFrom(tree2);
        }
        java.util.Set<String> locals = visitor.getLocals();
        java.util.Set<Interface> localInterfaces = visitor.getLocalInterfaces();
        return makeAtLocalDeclarations(locals, localInterfaces);
    }

    private List<JCAnnotation> makeAtAnnotationValue(com.sun.tools.javac.code.Type annotationType, String name, JCExpression values) {
        if (name == null) {
            return makeAnnoAnnotation(annotationType, List.<JCExpression>of(values));
        } else {
            return makeAnnoAnnotation(annotationType, List.<JCExpression>of(
                    make().Assign(naming.makeUnquotedIdent("name"), make().Literal(name)),
                    make().Assign(naming.makeUnquotedIdent("value"), values)));
        }
    }
    
    private List<JCAnnotation> makeAtAnnotationExprs(com.sun.tools.javac.code.Type annotationType, List<JCExpression> value) {
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
        Declaration reqdecl;
        if (decl instanceof FunctionOrValue
                && ((FunctionOrValue)decl).isParameter()) {
            reqdecl = CodegenUtil.getParameterized(((FunctionOrValue)decl));
        } else {
            reqdecl = decl;
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
        Type type;
        if (decl instanceof Function && ((Function)decl).isParameter() && handleFunctionalParameter) {
            type = getTypeForFunctionalParameter((Function)decl);
        } else if (decl instanceof Functional && Decl.isMpl((Functional)decl)) {
            type = getReturnTypeOfCallable(decl.appliedTypedReference(null, Collections.<Type>emptyList()).getFullType());
        } else {
            type = decl.getType();
        }
        boolean declaredVoid = decl instanceof Function && Strategy.useBoxedVoid((Function)decl) && Decl.isUnboxedVoid(decl);
        
        return makeJavaTypeAnnotations(type, declaredVoid, 
                CodegenUtil.hasTypeErased(decl),
                CodegenUtil.hasUntrustedType(decl),
                needsJavaTypeAnnotations(decl));
    }

    private List<JCTree.JCAnnotation> makeJavaTypeAnnotations(Type type, boolean declaredVoid, 
                                                              boolean hasTypeErased, boolean untrusted, boolean required) {
        if (!required)
            return List.nil();
        String name = serialiseTypeSignature(type);
        boolean erased = hasTypeErased || hasErasure(type);
        // Add the original type to the annotations
        ListBuffer<JCExpression> annotationArgs = ListBuffer.<JCExpression>lb();
        annotationArgs.add(
                make().Assign(naming.makeUnquotedIdent("value"), make().Literal(name)));
        if (erased) {
            annotationArgs.add(
                    make().Assign(naming.makeUnquotedIdent("erased"), make().Literal(erased)));
        }
        if (declaredVoid) {
            annotationArgs.add(
                    make().Assign(naming.makeUnquotedIdent("declaredVoid"), make().Literal(declaredVoid)));
        }
        if (untrusted) {
            annotationArgs.add(
                    make().Assign(naming.makeUnquotedIdent("untrusted"), make().Literal(untrusted)));
        }
        return makeModelAnnotation(syms().ceylonAtTypeInfoType, annotationArgs.toList());
    }
    
    private String serialiseTypeSignature(Type type){
        // resolve aliases
        type = type.resolveAliases();
        return typeSerialiser.print(type, typeFact);
    }
    
    /*
     * Boxing
     */
    public enum BoxingStrategy {
        UNBOXED, BOXED, INDIFFERENT;
    }

    public boolean canUnbox(Type type){
        // all the rest is boxed
        return isCeylonBasicType(type) || isJavaString(type);
    }
    
    JCExpression boxUnboxIfNecessary(JCExpression javaExpr, Tree.Term expr,
            Type exprType,
            BoxingStrategy boxingStrategy) {
        boolean exprBoxed = !CodegenUtil.isUnBoxed(expr);
        return boxUnboxIfNecessary(javaExpr, exprBoxed, exprType, boxingStrategy);
    }
    
    JCExpression boxUnboxIfNecessary(JCExpression javaExpr, boolean exprBoxed,
            Type exprType,
            BoxingStrategy boxingStrategy) {
        return boxUnboxIfNecessary(javaExpr, exprBoxed, exprType, boxingStrategy, exprType);
    }
    
    JCExpression boxUnboxIfNecessary(JCExpression javaExpr, boolean exprBoxed,
            Type exprType,
            BoxingStrategy boxingStrategy, Type expectedType) {
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
            if (exprType.getDeclaration() instanceof TypeParameter) {
                exprType = expectedType;
            }
            javaExpr = unboxType(javaExpr, exprType);
        }
        return javaExpr;
    }
    
    boolean isTypeParameter(Type type) {
        if(type == null)
            return false;
        if (typeFact().isOptionalType(type)) {
            type = type.eliminateNull();
        } 
        return type.getDeclaration() instanceof TypeParameter;
    }
    
    JCExpression unboxType(JCExpression expr, Type exprType) {
        exprType = typeFact().denotableType(exprType);
        if (isCeylonInteger(exprType)) {
            expr = unboxInteger(expr);
        } else if (isCeylonFloat(exprType)) {
            expr = unboxFloat(expr);
        } else if (isCeylonString(exprType)) {
            expr = unboxString(expr);
        } else if (isCeylonCharacter(exprType)) {
            expr = unboxCharacter(expr);
        } else if (isCeylonByte(exprType)) {
            expr = unboxByte(expr);
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

    JCExpression boxType(JCExpression expr, Type exprType) {
        exprType = typeFact().denotableType(exprType);
        if (isCeylonInteger(exprType)) {
            expr = boxInteger(expr);
        } else if (isCeylonFloat(exprType)) {
            expr = boxFloat(expr);
        } else if (isCeylonString(exprType)) {
            expr = boxString(expr);
        } else if (isCeylonCharacter(exprType)) {
            expr = boxCharacter(expr);
        } else if (isCeylonByte(exprType)) {
            expr = boxByte(expr);
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
    
    private JCTree.JCMethodInvocation boxByte(JCExpression value) {
        return makeBoxType(value, syms().ceylonByteType);
    }
    
    private JCTree.JCMethodInvocation boxBoolean(JCExpression value) {
        return makeBoxType(value, syms().ceylonBooleanType);
    }
    
    private JCTree.JCMethodInvocation makeBoxType(JCExpression value, com.sun.tools.javac.code.Type type) {
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
        JCExpression type = makeJavaType(typeFact().getStringType(), JT_NO_PRIMITIVES);
        JCExpression expr = make().Conditional(make().Binary(JCTree.NE, name.makeIdent(), makeNull()), 
                unboxString(name.makeIdent()),
                makeNull());
        return makeLetExpr(name, null, type, value, expr);
    }

    private JCExpression boxOptionalJavaString(JCExpression value){
        Naming.SyntheticName name = naming.temp();
        JCExpression type = makeJavaType(typeFact().getStringType());
        JCExpression expr = make().Conditional(make().Binary(JCTree.NE, name.makeIdent(), makeNull()), 
                boxString(name.makeIdent()),
                makeNull());
        return makeLetExpr(name, null, type, value, expr);
    }

    private JCTree.JCMethodInvocation unboxCharacter(JCExpression value) {
        return makeUnboxType(value, "intValue");
    }
    
    private JCTree.JCMethodInvocation unboxByte(JCExpression value) {
        return makeUnboxType(value, "byteValue");
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
        return make().Apply(null, makeSelect(iterable, "sequence"), List.<JCExpression>nil());
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
    JCExpression makeSequence(List<JCExpression> elems, Type seqElemType, int makeJavaTypeOpts) {
        return make().TypeCast(makeJavaType(typeFact().getSequenceType(seqElemType), JT_RAW), 
                utilInvocation().sequentialInstance(null,
                    makeReifiedTypeArgument(seqElemType),
                    makeEmptyAsSequential(false),
                    elems));
    }
    
    /**
     * Makes a lazy iterable literal, for a sequenced argument to a named invocation 
     * (<code>f{foo=""; expr1, expr2, *expr3}</code>) or
     * for an iterable instantiation (<code>{expr1, expr2, *expr3}</code>)
     */
    JCExpression makeLazyIterable(Tree.SequencedArgument sequencedArgument, 
            Type seqElemType, Type absentType, 
            int flags) {
        java.util.List<PositionalArgument> list = sequencedArgument.getPositionalArguments();
        int i = 0;
        ListBuffer<JCStatement> returns = new ListBuffer<JCStatement>();
        boolean spread = false;
        boolean old = expressionGen().withinSyntheticClassBody(true);
        try{
            for (Tree.PositionalArgument arg : list) {
                at(arg);
                JCExpression jcExpression;
                // last expression can be an Iterable<seqElemType>
                if(arg instanceof Tree.SpreadArgument || arg instanceof Tree.Comprehension){
                    // make sure we only have spread/comprehension as last
                    if(i != list.size()-1){
                        jcExpression = makeErroneous(arg, "compiler bug: spread or comprehension argument is not last in sequence literal");
                    }else{
                        Type type = typeFact().getIterableType(seqElemType);
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
                at(arg);
                // the last iterable goes first if spread
                returns.add(make().Return(jcExpression));
                i++;
            }
            at(sequencedArgument);
            if (Strategy.preferLazySwitchingIterable(sequencedArgument.getPositionalArguments())) {
                // use a LazySwitchingIterable
                MethodDefinitionBuilder mdb = MethodDefinitionBuilder.systemMethod(this, Unfix.$evaluate$.toString());
                mdb.isOverride(true);
                mdb.modifiers(PROTECTED | FINAL);
                mdb.resultType(null, make().Type(syms().objectType));
                mdb.parameter(ParameterDefinitionBuilder.systemParameter(this, Unfix.$index$.toString())
                        .type(make().Type(syms().intType), null));
                JCSwitch swtch;
                try (SavedPosition sp = noPosition()) {
                    ListBuffer<JCCase> cases = ListBuffer.<JCCase>lb();
                
                    i = 0;
                    for (JCStatement e : returns) {
                        cases.add(make().Case(make().Literal(i++), List.<JCStatement>of(e)));
                    }
                    cases.add(make().Case(null, List.<JCStatement>of(make().Return(makeNull()))));
                    swtch = make().Switch(naming.makeUnquotedIdent(Unfix.$index$), cases.toList());
                }
                mdb.body(swtch);

                return make().NewClass(null, 
                        List.<JCExpression>nil(),//of(makeJavaType(seqElemType), makeJavaType(absentType)),
                        make().TypeApply(make().QualIdent(syms.ceylonLazyIterableType.tsym),
                                List.<JCExpression>of(makeJavaType(seqElemType, JT_TYPE_ARGUMENT), makeJavaType(absentType, JT_TYPE_ARGUMENT))), 
                                List.of(makeReifiedTypeArgument(seqElemType),// td, 
                                        makeReifiedTypeArgument(absentType),//td
                                        make().Literal(list.size()),// numMethods
                                        make().Literal(spread)),// spread), 
                                        make().AnonymousClassDef(make().Modifiers(FINAL), 
                                                List.<JCTree>of(mdb.build())));
            } else {
                // use a LazyInvokingIterable
                ListBuffer<JCTree> methods = new ListBuffer<JCTree>();
                MethodDefinitionBuilder mdb = MethodDefinitionBuilder.systemMethod(this, Unfix.$lookup$.toString());
                mdb.isOverride(true);
                mdb.modifiers(PROTECTED | FINAL);
                mdb.resultType(null, naming.makeQualIdent(make().Type(syms().methodHandlesType), "Lookup"));
                mdb.body(make().Return(make().Apply(List.<JCExpression>nil(), 
                        naming.makeQualIdent(make().Type(syms().methodHandlesType), "lookup"), 
                        List.<JCExpression>nil())));
                methods.add(mdb.build());

                mdb = MethodDefinitionBuilder.systemMethod(this, Unfix.$invoke$.toString());
                mdb.isOverride(true);
                mdb.modifiers(PROTECTED | FINAL);
                mdb.resultType(null, make().Type(syms().objectType));
                mdb.parameter(ParameterDefinitionBuilder.systemParameter(this, "handle")
                        .type(make().Type(syms().methodHandleType), null));
                mdb.body(make().Return(make().Apply(List.<JCExpression>nil(), 
                        naming.makeQualIdent(naming.makeUnquotedIdent("handle"), "invokeExact"), 
                        List.<JCExpression>of(naming.makeThis()))));
                methods.add(mdb.build());
                i = 0;
                for (JCStatement expr : returns) {
                    mdb = MethodDefinitionBuilder.systemMethod(this, "$"+i);
                    i++;
                    mdb.modifiers(PRIVATE | FINAL);
                    mdb.resultType(null, make().Type(syms().objectType));
                    mdb.body(expr);
                    methods.add(mdb.build());
                }
                return make().NewClass(null, 
                        List.<JCExpression>nil(),//of(makeJavaType(seqElemType), makeJavaType(absentType)),
                        make().TypeApply(make().QualIdent(syms.ceylonLazyInvokingIterableType.tsym),
                                List.<JCExpression>of(makeJavaType(seqElemType, JT_TYPE_ARGUMENT), makeJavaType(absentType, JT_TYPE_ARGUMENT))), 
                                List.of(makeReifiedTypeArgument(seqElemType),// td, 
                                        makeReifiedTypeArgument(absentType),//td
                                        make().Literal(list.size()),// numMethods
                                        make().Literal(spread)),// spread), 
                                        make().AnonymousClassDef(make().Modifiers(FINAL), 
                                                methods.toList()));
            }
        } finally {
            expressionGen().withinSyntheticClassBody(old);
        }
    }

    /**
     * Makes an iterable literal, where the first element of elems is an Iterable, and the rest are the start of the
     * iterable.
     */
    JCExpression makeIterable(List<JCExpression> elems, Type seqElemType, int makeJavaTypeOpts) {
        JCExpression elemTypeExpr = makeJavaType(seqElemType, makeJavaTypeOpts);
        elems = elems.prepend(makeReifiedTypeArgument(seqElemType));
        // we delegate to ArrayIterable.instance() so that we can filter out empty Iterables
        return make().Apply(List.<JCExpression>of(elemTypeExpr), makeSelect(makeIdent(syms().ceylonArrayIterableType), "instance"), elems);
    }
    
    JCExpression makeEmptyAsSequential(boolean needsCast){
        if(needsCast)
            return make().TypeCast(makeJavaType(typeFact().getSequentialDeclaration().getType(), JT_RAW), makeEmpty());
        return makeEmpty();
    }
    
    JCExpression makeLanguageValue(String valueName) {
        return make().Apply(
                List.<JCTree.JCExpression>nil(),
                naming.makeLanguageValue(valueName),
                List.<JCTree.JCExpression>nil());
    }
    
    JCExpression makeLanguageSerializationValue(String valueName) {
        return make().Apply(
                List.<JCTree.JCExpression>nil(),
                naming.makeLanguageSerializationValue(valueName),
                List.<JCTree.JCExpression>nil());
    }
    
    JCExpression makeEmpty() {
        return makeLanguageValue("empty");
    }
    
    JCExpression makeFinished() {
        return makeLanguageValue("finished");
    }

    /**
     * Turns a sequence into a Java array
     * @param expr the sequence
     * @param sequenceType the (destination) sequence type
     * @param boxingStrategy the boxing strategy for expr
     * @param exprType the (source) expression type
     * @param initialElements the elements to place at the beginning of the Java array
     */
    JCExpression sequenceToJavaArray(
            SimpleInvocation invocation,
            JCExpression expr, Type sequenceType, 
                                     BoxingStrategy boxingStrategy, Type exprType,
                                     List<JCTree.JCExpression> initialElements) {
        // find the sequence element type
        Type type = typeFact().getIteratedType(sequenceType);
        if(boxingStrategy == BoxingStrategy.UNBOXED){
            if(isCeylonInteger(type)){
                if("short".equals(type.getUnderlyingType()))
                    return utilInvocation().toShortArray(expr, initialElements);
                else if("int".equals(type.getUnderlyingType()))
                    return utilInvocation().toIntArray(expr, initialElements);
                else
                    return utilInvocation().toLongArray(expr, initialElements);
            }else if(isCeylonFloat(type)){
                if("float".equals(type.getUnderlyingType()))
                    return utilInvocation().toFloatArray(expr, initialElements);
                else
                    return utilInvocation().toDoubleArray(expr, initialElements);
            } else if (isCeylonCharacter(type)) {
                if ("char".equals(type.getUnderlyingType()))
                    return utilInvocation().toCharArray(expr, initialElements);
                // else it must be boxed, right?
            } else if (isCeylonByte(type)) {
                return utilInvocation().toByteArray(expr, initialElements);
            } else if (isCeylonBoolean(type)) {
                return utilInvocation().toBooleanArray(expr, initialElements);
            } else if (isJavaString(type)) {
                return utilInvocation().toJavaStringArray(expr, initialElements);
            } else if (isCeylonString(type)) {
                return objectVariadicToJavaArray(invocation, type, exprType, expr, initialElements);
            }
            return objectVariadicToJavaArray(invocation, type, exprType, expr, initialElements);
        }else{
            return objectVariadicToJavaArray(invocation, type, exprType, expr, initialElements);
        }
    }

    private JCExpression objectVariadicToJavaArray(
            SimpleInvocation invocation,
            Type type,
            Type exprType,
            JCExpression expr,
            List<JCExpression> initialElements) {
        // The type that the java varargs parameter erases to: 
        // this is the type of the array which we need to construct
        java.util.List<Parameter> pl = ((Functional)invocation.getPrimaryDeclaration()).getFirstParameterList().getParameters();
        Parameter varargsParameter = pl.get(pl.size()-1);
        Type arrayType = simplifyType(typeFact().getIteratedType(varargsParameter.getType()));
        while (arrayType.getDeclaration() instanceof TypeParameter) {
            TypeParameter tp = (TypeParameter)arrayType.getDeclaration();
            arrayType = tp.getSatisfiedTypes().get(0);
        }
        
        // we could have a <X>variadic(X&Object), so we need to pick a type which satisfies the bound
        Type castType = simplifyType(type);
        if (typeFact().isIntersection(castType)) {
            for (Type t : castType.getSatisfiedTypes()) {
                if (t.isSubtypeOf(arrayType)) {
                    castType = t;
                    break;
                }
            }
        }
        
        Naming.SyntheticName seqName = naming.temp().suffixedBy(0);
        
        Type sequentialType = typeFact().getSequentialDeclaration().appliedType(null, Arrays.asList(type));
        JCExpression seqTypeExpr1 = makeJavaType(sequentialType);
        //JCExpression seqTypeExpr2 = makeJavaType(fixedSizedType);
        
        JCExpression sizeExpr = make().Apply(List.<JCExpression>nil(), 
                make().Select(seqName.makeIdent(), names().fromString("getSize")),
                List.<JCExpression>nil());
        sizeExpr = utilInvocation().toInt(sizeExpr);
        
        // add initial elements if required
        if(!initialElements.isEmpty())
            sizeExpr = make().Binary(JCTree.PLUS, 
                                     sizeExpr,
                                     makeInteger(initialElements.size()));
        
        JCExpression array = make().NewArray(makeJavaType(arrayType, JT_RAW | JT_NO_PRIMITIVES), List.of(sizeExpr), null);
        if (!arrayType.isExactly(castType)) {
            array = make().TypeCast(
                    make().TypeArray(makeJavaType(castType, JT_CLASS_NEW | JT_NO_PRIMITIVES)),
                    array);
        }
        JCExpression sequenceToArrayExpr = utilInvocation().toArray(
                seqName.makeIdent(), array, initialElements,
                makeJavaType(castType, JT_CLASS_NEW | JT_NO_PRIMITIVES));
        
        // since T[] is erased to Sequential<T> we probably need a cast to FixedSized<T>
        //JCExpression castedExpr = make().TypeCast(seqTypeExpr2, expr);
        
        return //make().TypeCast(
                //make().TypeArray(makeJavaType(arrayType)),
                makeLetExpr(seqName, List.<JCStatement>nil(), seqTypeExpr1, expr, sequenceToArrayExpr)/*)*/;
    }

    /** 
     * Abstraction over how we transform a {@code is} type test 
     */
    interface TypeTestTransformation<R> {
        /** 
         * Combine the results of two other type tests using AND or OR, 
         * depending on the {@code op} parameter 
         */
        public R andOr(R a, R b, int op);
        public R not(R a);
        /** Make a type test using that just evaluates as the given result */
        public R eval(JCExpression varExpr, boolean result);
        /** 
         * Make a type test using {@code == null} or {@code != null}, 
         * depending on the {@code op} parameter 
         */
        public R nullTest(JCExpression varExpr, int op);
        /** Make a type test using {@code Util.isIdentifiable()} */
        public R isIdentifiable(JCExpression varExpr);
        /** Make a type test using {@code Util.isBasic()} */
        public R isBasic(JCExpression varExpr);
        /** Make a type test using {@code instanceof} */
        public R isInstanceof(JCExpression varExpr, Type testedType);
        /** Make a type test using {@code Util.isReified()} */
        public R isReified(JCExpression varExpr, Type testedType);
        /** ceylon.language.true_.get().equals(expr) */
        public R isTrue(JCExpression expr);
        /** ceylon.language.false_.get().equals(expr) */
        public R isFalse(JCExpression expr);
        
    }
    /**
     * A type test transformation that builds a tree for evaluating the type test
     * @see PerfTypeTestTransformation
     */
    class JavacTypeTestTransformation implements TypeTestTransformation<JCExpression> {

        @Override
        public JCExpression andOr(JCExpression a, JCExpression b, int op) {
            return make().Binary(op, a, b);
        }
        
        @Override
        public JCExpression not(JCExpression a) {
            return make().Unary(JCTree.NOT, a);
        }

        @Override
        public JCExpression eval(JCExpression varExpr, boolean result) {
            return makeIgnoredEvalAndReturn(varExpr, makeBoolean(result));
        }

        @Override
        public JCExpression nullTest(JCExpression varExpr, int op) {
            return make().Binary(op, varExpr, makeNull());
        }

        @Override
        public JCExpression isIdentifiable(JCExpression varExpr) {
            return utilInvocation().isIdentifiable(varExpr);
        }

        @Override
        public JCExpression isBasic(JCExpression varExpr) {
            return utilInvocation().isBasic(varExpr);
        }

        @Override
        public JCExpression isInstanceof(JCExpression varExpr,
                Type testedType) {
            JCExpression rawTypeExpr = makeJavaType(testedType, JT_NO_PRIMITIVES | JT_RAW | JT_IS);
            return make().TypeTest(varExpr, rawTypeExpr);
        }

        @Override
        public JCExpression isReified(JCExpression varExpr,
                Type testedType) {
            return utilInvocation().isReified(varExpr, testedType);
        }

        @Override
        public JCExpression isTrue(JCExpression expr) {
            return make().Apply(null,
                    naming.makeQualIdent(makeLanguageValue("true"), "equals"),
                    List.<JCExpression>of(expr));
        }

        @Override
        public JCExpression isFalse(JCExpression expr) {
            return make().Apply(null,
                    naming.makeQualIdent(makeLanguageValue("false"), "equals"),
                    List.<JCExpression>of(expr));
        }
    }
    JavacTypeTestTransformation javacTypeTester = null;
    JavacTypeTestTransformation javacTypeTester() {
        if (this.javacTypeTester == null) {
            this.javacTypeTester = new JavacTypeTestTransformation();
        }
        return this.javacTypeTester;
    }
    /**
     * A type test transformation that estimates whether the real type test 
     * transformation (@link JavacTypeTester} produce a test which is 
     * expensive (anything involving reification of inspecting annotations)
     * or cheap (just involving instanceof, != null, == null, and similar). 
     */
    class PerfTypeTestTransformation implements TypeTestTransformation<Boolean> {

        @Override
        public Boolean andOr(Boolean aIsCheap, Boolean bIsCheap, int op) {
            // cheap only if both halves are cheap
            return aIsCheap.booleanValue() && bIsCheap.booleanValue() ? Boolean.TRUE : Boolean.FALSE;
        }
        
        @Override
        public Boolean not(Boolean a) {
            return a;
        }
        
        @Override
        public Boolean eval(JCExpression varExpr, boolean result) {
            return Boolean.TRUE;
        }

        @Override
        public Boolean nullTest(JCExpression varExpr, int op) {
            // != null and == null are always cheap
            return Boolean.TRUE;
        }

        @Override
        public Boolean isIdentifiable(JCExpression varExpr) {
            // Util.isIdentifiable() is expensive
            return Boolean.FALSE;
        }

        @Override
        public Boolean isBasic(JCExpression varExpr) {
            // Util.isBasic() is expensive
            return Boolean.FALSE;
        }

        @Override
        public Boolean isInstanceof(JCExpression varExpr,
                Type testedType) {
            // instanceof is cheap
            return Boolean.TRUE;
        }

        @Override
        public Boolean isReified(JCExpression varExpr, Type testedType) {
            // Util.isReified() is expensive
            return Boolean.FALSE;
        }

        @Override
        public Boolean isTrue(JCExpression expr) {
            return Boolean.TRUE;
        }

        @Override
        public Boolean isFalse(JCExpression expr) {
            return Boolean.TRUE;
        }
        
    }
    PerfTypeTestTransformation perfTypeTester = null;
    PerfTypeTestTransformation perfTypeTester() {
        if (this.perfTypeTester == null) {
            this.perfTypeTester = new PerfTypeTestTransformation();
        }
        return this.perfTypeTester;
    }
    
    /** 
     * Creates comparisons of expressions against types, used for {@code is} 
     * conditions ({@code is X e}), the {@code is} operator ({@code e is X})
     * and {@code is} cases ({@code case (is X)})
     */
    JCExpression makeTypeTest(JCExpression firstTimeExpr, Naming.CName varName, Type testedType, Type expressionType) {
        return makeTypeTest(javacTypeTester(), firstTimeExpr, varName, testedType, expressionType);
    }
    /**
     * Determines whether the given type test generated by 
     * {@link #makeTypeTest(JCExpression, com.redhat.ceylon.compiler.java.codegen.Naming.CName, Type, Type)} 
     * will be "cheap" or "expensive"
     */
    boolean isTypeTestCheap(JCExpression firstTimeExpr, Naming.CName varName, Type testedType, Type expressionType) {
        return makeTypeTest(perfTypeTester(), firstTimeExpr, varName, testedType, expressionType);
    }
    
    JCExpression makeOptimizedTypeTest( 
            JCExpression firstTimeExpr, Naming.CName varName, Type testedType, Type expressionType) {
        // If the type test is expensive and we can figure out a 
        // "complement type" whose type test is cheap we can invert the test.
        //TypeDeclaration widerDeclaration = expressionType.getDeclaration();
        if (!isTypeTestCheap(firstTimeExpr, varName, testedType, expressionType)) {
            //if (widerDeclaration instanceof UnionType
            //        || widerDeclaration instanceof ClassOrInterface) {
                // we've got a X|Y and we're testing for X
                // or parhaps a A|B|C|D and we're testing for C|D
                java.util.List<Type> cases = expressionType.getCaseTypes();
                if (cases != null) {
                    java.util.List<Type> copiedCases = new ArrayList<>(cases.size());
                    copiedCases.addAll(cases);
                    cases = copiedCases;
                    if ((testedType.isClassOrInterface()
                            || testedType.isTypeParameter())
                            && cases.remove(testedType)) { 
                    } else if (testedType.isUnion()) {
                        for (Type ct : testedType.getCaseTypes()) {
                            if (!cases.remove(ct)) {
                                cases = null;
                                break;
                            }
                        }
                    } else {
                        cases = null;
                    }                
                    if (cases != null) {
                        Type complementType = typeFact().getNothingType();
                        for (Type ct : cases) {
                            complementType = com.redhat.ceylon.model.typechecker.model.ModelUtil.unionType(complementType, ct, typeFact());
                        }
                        if (/*typeFact().getLanguageModuleDeclaration("Finished").equals(complementType.getDeclaration())
                                ||*/ com.redhat.ceylon.model.typechecker.model.ModelUtil.intersectionType(complementType, testedType, typeFact()).isNothing()) {
                            return make().Unary(JCTree.NOT, makeTypeTest(firstTimeExpr, varName, complementType, expressionType));
                        }
                    }
                }
            //}
        }
        return makeTypeTest(firstTimeExpr, varName, testedType, expressionType);
    }
    
    private <R> R makeTypeTest(TypeTestTransformation<R> typeTester, 
            JCExpression firstTimeExpr, Naming.CName varName, 
            Type testedType, Type expressionType) {
        R result = null;
        // make sure aliases are resolved
        testedType = testedType.resolveAliases();
        // optimisation when all we're doing is making sure it is not null
        if(expressionType != null
                && testedType.getSupertype(typeFact().getObjectDeclaration()) != null
                && expressionType.isExactly(typeFact().getOptionalType(testedType))){
            JCExpression varExpr = firstTimeExpr != null ? firstTimeExpr : varName.makeIdent();
            return typeTester.nullTest(varExpr, JCTree.NE);
        }
        TypeDeclaration declaration = testedType.getDeclaration();
        if (declaration instanceof ClassOrInterface) {
            JCExpression varExpr = firstTimeExpr != null ? firstTimeExpr : varName.makeIdent();
            if (isAnything(testedType)){
                // everything is Void, it's the root of the hierarchy
                return typeTester.eval(varExpr, true);
            } else if (testedType.isExactly(typeFact().getNullType())){
                // is Null => is null
                return typeTester.nullTest(varExpr, JCTree.EQ);
            } else if (testedType.isExactly(typeFact().getObjectType())){
                // is Object => is not null
                return typeTester.nullTest(varExpr, JCTree.NE);
            } else if (testedType.isExactly(typeFact().getIdentifiableType())){
                // it's erased
                return typeTester.isIdentifiable(varExpr);
            } else if (testedType.getDeclaration().equals(typeFact().getTrueValueDeclaration().getTypeDeclaration())) {
                return typeTester.isTrue(varExpr);
            } else if (testedType.getDeclaration().equals(typeFact().getFalseValueDeclaration().getTypeDeclaration())) {
                return typeTester.isFalse(varExpr);
            } else if (testedType.isExactly(typeFact().getBasicType())){
                // it's erased
                return typeTester.isBasic(varExpr);
            } else if (testedType.getDeclaration().getQualifiedNameString().equals("java.lang::Error")){
                // need to exclude AssertionError
                return typeTester.andOr(typeTester.isInstanceof(varExpr, testedType), 
                        typeTester.not(typeTester.isInstanceof(varName.makeIdent(), typeFact().getAssertionErrorDeclaration().getType())), 
                        JCTree.AND);
            } else if (!hasTypeArguments(testedType)) {
                // non-generic Class or interface, use instanceof
                return typeTester.isInstanceof(varExpr, testedType);
            } else {// generic class or interface...
                if (declaration.getSelfType() != null 
                        && declaration.getSelfType().getDeclaration() instanceof TypeParameter // of TypeArg
                        && declaration.getSelfType().isSubtypeOf(declaration.getType()) // given TypeArg satisfies SelfType<TypeArg>
                        ){
                    Type selfTypeArg = testedType.getTypeArguments().get(declaration.getSelfType().getDeclaration());
                    if(selfTypeArg.getDeclaration() instanceof ClassOrInterface) {
                        // first check if the type is inhabited or not
                        if(selfTypeArg.getDeclaration().inherits(declaration)){
                            // "is SelfType<ClassOrInterface>" can be written "is ClassOrInterface" 
                            return makeTypeTest(typeTester, firstTimeExpr, varName, selfTypeArg, expressionType);
                        }else{
                            // always false, for example Comparable<Anything> is uninhabited because Anything does not inherit from Comparable
                            return typeTester.eval(varExpr, false);
                        }
                    }
                    // if not, keep trying
                } 
                if (canOptimiseReifiedTypeTest(testedType)) {
                    // Use an instanceof
                    return typeTester.isInstanceof(varExpr, testedType);
                } else {
                    // Have to use a reified test
                    if (!Decl.equal(declaration, expressionType.getDeclaration())
                            && canUseFastFailTypeTest(testedType)) {
                        // do a cheap instanceof test to try to shortcircuit the expensive
                        // Util.isReified()
                        
                        // XXX Possible future optimization: When the `is` is a condition 
                        // in an `assert` we expect the result to be true, so 
                        // instanceof shortcircuit doesn't achieve anything
                        return typeTester.andOr(
                                typeTester.isInstanceof(varExpr, testedType),
                                typeTester.isReified(varName.makeIdent(), testedType), JCTree.AND);
                    } else {
                        return typeTester.isReified(varExpr, testedType);
                    }
                }
            }
        } else if (typeFact().isUnion(testedType)) {
            for (Type pt : testedType.getCaseTypes()) {
                R partExpr = makeTypeTest(typeTester, firstTimeExpr, varName, pt, expressionType);
                firstTimeExpr = null;
                if (result == null) {
                    result = partExpr;
                } else {
                    result = typeTester.andOr(result, partExpr, JCTree.OR);
                }
            }
            return result;
        } else if (typeFact().isIntersection(testedType)) {
            for (Type pt : testedType.getSatisfiedTypes()) {
                R partExpr = makeTypeTest(typeTester, firstTimeExpr, varName, pt, expressionType);
                firstTimeExpr = null;
                if (result == null) {
                    result = partExpr;
                } else {
                    result = typeTester.andOr(result, partExpr, JCTree.AND);
                }
            }
            return result;
        } else if (testedType.isNothing()){
            // nothing is Bottom
            JCExpression varExpr = firstTimeExpr != null ? firstTimeExpr : varName.makeIdent();
            return typeTester.eval(varExpr, false);
        } else if (declaration instanceof TypeParameter) {
            JCExpression varExpr = firstTimeExpr != null ? firstTimeExpr : varName.makeIdent();
            if (!reifiableUpperBounds((TypeParameter)declaration, expressionType).isEmpty()) {
                // If we're testing against a type parameter with  
                // class or interface upper bounds we can again shortcircuit the 
                // Util.isReified() using instanceof against the bounds
                result = typeTester.isReified(varName.makeIdent(), testedType);
                Iterator<Type> iterator = reifiableUpperBounds((TypeParameter)declaration, expressionType).iterator();
                while (iterator.hasNext()) {
                    Type type = iterator.next();
                    ClassOrInterface c = ((ClassOrInterface)type.resolveAliases().getDeclaration());
                    result = typeTester.andOr(
                            typeTester.isInstanceof(iterator.hasNext() ? varName.makeIdent() : varExpr, c.getType()),
                            result, JCTree.AND);
                }
                return result;
            } else {
                return typeTester.isReified(varExpr, testedType);
            }
        } else {
            throw BugException.unhandledDeclarationCase(declaration);
        }
    }
    
    /**
     * Returns the upper bounds of the given type parameter which are 
     * java reifiable types (and can thus be used in an {@code instanceof})
     */
    private java.util.List<Type> reifiableUpperBounds(
            TypeParameter testedType, Type expressionType) {
        ArrayList<Type> result = new ArrayList<Type>();
        for (Type type: testedType.getSatisfiedTypes()) {
            if (type.getDeclaration() instanceof ClassOrInterface // reified, so we can use instanceof
                    && !willEraseToObject(type) // no point doing instanceof Object
                    && !type.isSupertypeOf(expressionType)) { // no point doing instanceof 
                result.add(type);
            }
        }
        return result;
    }

    /**
     * Determine whether we can use a plain {@code instanceof} instead of 
     * a full {@code Util.isReified()} for a {@code is} test
     */
    private boolean canOptimiseReifiedTypeTest(Type type) {
        if(isJavaArray(type)){
            if(isJavaObjectArray(type)){
                MultidimensionalArray multiArray = getMultiDimensionalArrayInfo(type);
                // we can test, even if not fully reified in Java
                return multiArray.type.getDeclaration() instanceof ClassOrInterface;
            }else{
                // primitive array we can test
                return true;
            }
        }
        // we can optimise it if we've got a ClassOrInterface with only Anything type parameters
        if(type.getDeclaration() instanceof ClassOrInterface == false)
            return false;
        for(Entry<TypeParameter, Type> entry : type.getTypeArguments().entrySet()){
            TypeParameter tp = entry.getKey();
            if(!type.isCovariant(tp)) {
                return false;
            }
            java.util.List<Type> bounds = tp.getSatisfiedTypes();
            Type ta = entry.getValue();
            if ((bounds == null || bounds.isEmpty()) && !isAnything(ta)) {
                return false;
            }
            for (Type bound : bounds) {
                if (!ta.isSupertypeOf(bound)) {
                    return false;
                }
            }
        }
        // they're all Anything (or supertypes of their upper bound) we can optimise, unless we have a container with type arguments
        Type qualifyingType = type.getQualifyingType();
        if(qualifyingType == null
            // ignore qualifying types of static java declarations
            && (Decl.isCeylon(type.getDeclaration())
                    || !type.getDeclaration().isStaticallyImportable())){
            Declaration declaration = type.getDeclaration();
            do{
                // it may be contained in a function or value, and we want its type
                Declaration enclosingDeclaration = getDeclarationContainer(declaration);
                if(enclosingDeclaration instanceof TypedDeclaration){
                    // must be in scope
                    if(enclosingDeclaration instanceof Generic 
                            && !((Generic) enclosingDeclaration).getTypeParameters().isEmpty())
                        return false;
                    // look up the containers
                    declaration = enclosingDeclaration;
                }else if(enclosingDeclaration instanceof TypeDeclaration){
                    // must be in scope
                    // we can't optimise if that container has type arguments as they are not provided
                    if(enclosingDeclaration instanceof Generic 
                            && !((Generic) enclosingDeclaration).getTypeParameters().isEmpty())
                        return false;
                    // look up the containers
                    declaration = enclosingDeclaration;
                }else{
                    // that's fucked up
                    break;
                }
                // go up every containing typed declaration
            }while(declaration != null);
            // we can optimise!
            return true;
        }else if(qualifyingType != null){
            // we can only optimise if the qualifying type can also be optimised
            return canOptimiseReifiedTypeTest(qualifyingType);
        }else{
            // we can optimise!
            return true;
        }
    }

    private boolean canUseFastFailTypeTest(Type type) {
        if(type.getDeclaration() instanceof ClassOrInterface == false)
            return false;
        boolean isRaw = !type.getDeclaration().getTypeParameters().isEmpty();
        Type qualifyingType = type.getQualifyingType();
        if(qualifyingType == null
            // ignore qualifying types of static java declarations
            && (Decl.isCeylon(type.getDeclaration())
                    || !type.getDeclaration().isStaticallyImportable())){
            Declaration declaration = type.getDeclaration();
            boolean local = false;
            do{
                // getDeclarationContainer will skip some containers we don't want to consider, so it's not good
                // for checking locality, rely on isLocal for that.
                local |= Decl.isLocal(declaration);
                // it may be contained in a function or value, and we want its type
                Declaration enclosingDeclaration = getDeclarationContainer(declaration);
                if(enclosingDeclaration instanceof TypedDeclaration){
                    local = true;
                    // look up the containers
                    declaration = enclosingDeclaration;
                }else if(enclosingDeclaration instanceof TypeDeclaration){
                    // must be in scope
                    // we can't do instanceof on a local whose outer types contain type parameters, unless the local is raw
                    if(enclosingDeclaration instanceof Generic
                            && local
                            && !isRaw
                            && !((Generic) enclosingDeclaration).getTypeParameters().isEmpty())
                        return false;
                    // look up the containers
                    declaration = enclosingDeclaration;
                }else{
                    // that's fucked up
                    break;
                }
                // go up every containing typed declaration
            }while(declaration != null);
            // we can fast-fail!
            return true;
        }else if(qualifyingType != null){
            // we can only fast-fail if the qualifying type can also be fast-failed
            return canUseFastFailTypeTest(qualifyingType);
        }else{
            // we can fast-fail!
            return true;
        }
    }

    JCExpression makeNonEmptyTest(JCExpression firstTimeExpr) {
        Interface sequence = typeFact().getSequenceDeclaration();
        JCExpression sequenceType = makeJavaType(sequence.getType(), JT_NO_PRIMITIVES | JT_RAW);
        return make().TypeTest(firstTimeExpr, sequenceType);
    }
    
    private RuntimeUtil utilInvocation = null;

    RuntimeUtil utilInvocation() {
        if (utilInvocation == null) {
            utilInvocation = new RuntimeUtil(this);
        }
        return utilInvocation;
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
    
    public JCExpression makeSerializableType(){
        return makeJavaType(syms().ceylonSerializableType.tsym);
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
     * 
     * @see BugException
     */
    JCExpression makeErroneous(Node node, String message) {
        return makeErroneous(node, message, List.<JCTree>nil());
    }
    
    /**
     * Makes an 'erroneous' AST node with a message to be logged as an error
     * and (eventually) treated as a compiler bug.
     * 
     * @see BugException
     */
    JCExpression makeErroneous(Node node, String message, List<? extends JCTree> errs) {
        return makeErr(node, "ceylon.codegen.erroneous", message, errs);
    }
    private JCExpression makeErr(Node node, String key, String message, List<? extends JCTree> errs) {
        if (node != null) {
            at(node);
        }
        if (message != null) {
            if (node != null) {
                node.addError(new CodeGenError(node, message, Backend.Java, null));
            } else {
                log.error(key, message);
            }
        }
        return make().Erroneous(errs);
    }
    
    List<JCExpression> makeTypeParameterBounds(java.util.List<Type> satisfiedTypes){
        ListBuffer<JCExpression> bounds = new ListBuffer<JCExpression>();
        for (Type t : satisfiedTypes) {
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
            if(Decl.equal(otherTypeParameter, tp))
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
        for(Type pt : tpToCheck.getSatisfiedTypes()){
            if(dependsOnTypeParameter(pt, tpToDependOn))
                return true;
        }
        return false;
    }

    private boolean dependsOnTypeParameter(Type t, TypeParameter tp) {
        if(t.isUnion()){
            for(Type pt : t.getCaseTypes()){
                if(dependsOnTypeParameter(pt, tp)){
                    return true;
                }
            }
        }else if(t.isIntersection()){
            for(Type pt : t.getSatisfiedTypes()){
                if(dependsOnTypeParameter(pt, tp)){
                    return true;
                }
            }
        }else if(t.isTypeParameter()){
            if (tp == null || Decl.equal(tp, t.getDeclaration()))
                return true;
        }else if(t.isClassOrInterface()){
            for(Type ta : t.getTypeArgumentList()){
                if(dependsOnTypeParameter(ta, tp)){
                    return true;
                }
            }
        }
        return false;
    }
    
    boolean hasConstrainedTypeParameters(Parameter parameter) {
        Type type = parameter.getType();
        return hasConstrainedTypeParameters(type);
    }
    private boolean hasConstrainedTypeParameters(Type type) {
        if(type.isTypeParameter()){
            TypeParameter tp = (TypeParameter) type.getDeclaration();
            return !tp.getSatisfiedTypes().isEmpty() && !tp.isContravariant();
        }
        if(type.isUnion()){
            for(Type m : type.getCaseTypes())
                if(hasConstrainedTypeParameters(m))
                    return true;
            return false;
        }
        if(type.isIntersection()){
            for(Type m : type.getSatisfiedTypes())
                if(hasConstrainedTypeParameters(m))
                    return true;
            return false;
        }
        // check its type arguments
        // special case for Callable which has only a single type param in Java
        boolean isCallable = isCeylonCallable(type);
        for(Type typeArg : type.getTypeArgumentList()){
            if(hasConstrainedTypeParameters(typeArg))
                return true;
            // stop after the first type arg for Callable
            if(isCallable)
                break;
        }
        return false;
    }

    boolean containsTypeParameter(Type type) {
        if(type.isTypeParameter()){
            return true;
        }
        if(type.isUnion()){
            for(Type m : type.getCaseTypes())
                if(containsTypeParameter(m))
                    return true;
            return false;
        }
        if(type.isIntersection()){
            for(Type m : type.getSatisfiedTypes())
                if(containsTypeParameter(m))
                    return true;
            return false;
        }
        // check its type arguments
        // special case for Callable which has only a single type param in Java
        boolean isCallable = isCeylonCallable(type);
        for(Type typeArg : type.getTypeArgumentList()){
            if(containsTypeParameter(typeArg))
                return true;
            // stop after the first type arg for Callable
            if(isCallable)
                break;
        }
        return false;
    }

    boolean hasDependentCovariantTypeParameters(Type type) {
        if(type.isUnion()){
            for(Type m : type.getCaseTypes())
                if(hasDependentCovariantTypeParameters(m))
                    return true;
            return false;
        }
        if(type.isIntersection()){
            for(Type m : type.getSatisfiedTypes())
                if(hasDependentCovariantTypeParameters(m))
                    return true;
            return false;
        }
        // check its type arguments
        // special case for Callable which has only a single type param in Java
        boolean isCallable = isCeylonCallable(type);
        // check if any type parameter is dependent on and covariant
        TypeDeclaration declaration = type.getDeclaration();
        java.util.List<TypeParameter> typeParams = declaration.getTypeParameters();
        Map<TypeParameter, Type> typeArguments = type.getTypeArguments();
        for(TypeParameter typeParam : typeParams){
            Type typeArg = typeArguments.get(typeParam);
            if(type.isCovariant(typeParam)
                    && hasDependentTypeParameters(typeParams, typeParam)){
                // see if the type argument in question contains type parameters and is erased to Object
                if(containsTypeParameter(typeArg) && willEraseToObject(typeArg))
                    return true;
            }
            // now check if we the type argument has the same problem
            if(hasDependentCovariantTypeParameters(typeArg))
                return true;            
            // stop after the first type arg for Callable
            if(isCallable)
                break;
        }
        return false;
    }

    private JCTypeParameter makeTypeParameter(String name, java.util.List<Type> satisfiedTypes, boolean covariant, boolean contravariant) {
        return make().TypeParameter(names().fromString(name), makeTypeParameterBounds(satisfiedTypes));
    }

    JCTypeParameter makeTypeParameter(TypeParameter declarationModel, java.util.List<Type> satisfiedTypesForBounds) {
        TypeParameter typeParameterForBounds = declarationModel;
        if (satisfiedTypesForBounds == null) {
            satisfiedTypesForBounds = declarationModel.getSatisfiedTypes();
        }
        // special case for method refinenement where Java doesn't let us refine the parameter bounds
        if(declarationModel.getContainer() instanceof Function){
            Function method = (Function) declarationModel.getContainer();
            Function refinedMethod = (Function) method.getRefinedDeclaration();
            if (!Decl.equal(method, refinedMethod)) {
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
                        Type supertype = methodContainer.getType().getSupertype(refinedMethodContainer);
                        satisfiedTypesForBounds = new ArrayList<Type>(refinedTP.getSatisfiedTypes().size());
                        for(Type satisfiedType : refinedTP.getSatisfiedTypes()){
                            // substitute the refined type parameter bounds with the right type arguments
                            satisfiedTypesForBounds.add(satisfiedType.substitute(supertype));
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
        return makeTypeParameter(param.getDeclarationModel(), null);
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
    
    final List<JCExpression> typeArguments(Functional method) {
        if (method instanceof Generic) {            
            return typeArguments(((Generic)method).getTypeParameters(), method.getType().getTypeArguments());
        }
        else {
            return ListBuffer.<JCExpression>lb().toList();
        }
    }
    
    final List<JCExpression> typeArguments(Tree.ClassOrInterface type) {
        return typeArguments(type.getDeclarationModel().getTypeParameters(), type.getDeclarationModel().getType().getTypeArguments());
    }
    
    final List<JCExpression> typeArguments(java.util.List<TypeParameter> typeParameters, Map<TypeParameter, Type> typeArguments) {
        ListBuffer<JCExpression> typeArgs = ListBuffer.<JCExpression>lb();
        for (TypeParameter tp : typeParameters) {
            Type type = typeArguments.get(tp);
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

    public JCExpression makeClassLiteral(Type type) {
        return makeClassLiteral(type, 0);
    }

    public JCExpression makeClassLiteral(Type type, int extraFlags) {
        return makeSelect(makeJavaType(type, JT_NO_PRIMITIVES | JT_RAW | JT_CLASS_LITERAL | extraFlags), "class");
    }

    /**
     * Same as makeClassLiteral but does not use erasure rules
     */
    public JCExpression makeUnerasedClassLiteral(TypeDeclaration declaration) {
        JCExpression className = naming.makeDeclarationName(declaration, DeclNameFlag.QUALIFIED);
        return makeSelect(className, "class");
    }

    public java.util.List<JCExpression> makeReifiedTypeArguments(Reference ref){
        ref = resolveAliasesForReifiedTypeArguments(ref);
        Declaration declaration = ref.getDeclaration();
        if(!supportsReified(declaration))
            return Collections.emptyList();
        return makeReifiedTypeArguments(getTypeArguments(ref));
    }

    Reference resolveAliasesForReifiedTypeArguments(Reference ref) {
        // this is a bit tricky:
        // - for method references (TypedReference) it's all good
        // - for classes we get a Type which we use to resolve aliases
        // -- UNLESS it's a class with an instantiator, in which case we should not resolve aliases
        //    because the instantiator has the right set of type parameters
        if(ref instanceof Type && !Strategy.generateInstantiator(ref.getDeclaration()))
            return ((Type)ref).resolveAliases();
        return ref;
    }

    public static boolean supportsReified(Declaration declaration){
        if(declaration instanceof ClassOrInterface){
            // Java constructors don't support reified type arguments
            return Decl.isCeylon((TypeDeclaration) declaration);
        }else if(Decl.isConstructor(declaration)){
            // Java constructors don't support reified type arguments
            return Decl.isCeylon(Decl.getConstructor(declaration));
        }else if(declaration instanceof Function){
            if (((Function)declaration).isParameter()) {
                // those can never be parameterised
                return false;
            }
            if(Decl.isToplevel(declaration))
                return true;
            // Java methods don't support reified type arguments
            Function m = (Function) CodegenUtil.getTopmostRefinedDeclaration(declaration);
            // See what its container is
            ClassOrInterface container = Decl.getClassOrInterfaceContainer(m);
            // a method which is not a toplevel and is not a class method, must be a method within method and
            // that must be Ceylon so it supports it
            if(container == null)
                return true;
            return supportsReified(container);
        }else{
            throw BugException.unhandledDeclarationCase(declaration);
        }
    }
    
    private java.util.List<Type> getTypeArguments(
            Reference producedReference) {
        java.util.List<TypeParameter> typeParameters = getTypeParameters(producedReference);
        java.util.List<Type> typeArguments = new ArrayList<Type>(typeParameters.size());
        for(TypeParameter tp : typeParameters)
            typeArguments.add(producedReference.getTypeArguments().get(tp));
        return typeArguments;
    }

    private java.util.List<Type> getTypeArguments(
            Function method) {
        java.util.List<TypeParameter> typeParameters = method.getTypeParameters();
        java.util.List<Type> typeArguments = new ArrayList<Type>(typeParameters.size());
        for(TypeParameter tp : typeParameters)
            typeArguments.add(tp.getType());
        return typeArguments;
    }

    java.util.List<TypeParameter> getTypeParameters(
            Reference producedReference) {
        Declaration declaration = producedReference.getDeclaration();
        if(declaration instanceof ClassOrInterface)
            return ((ClassOrInterface)declaration).getTypeParameters();
        else if(Decl.isConstructor(declaration))
            return (Decl.getConstructedClass(declaration)).getTypeParameters();
        else
            return ((Function)declaration).getTypeParameters();
    }

    public List<JCExpression> makeReifiedTypeArguments(
            java.util.List<Type> typeArguments) {
        // same as makeReifiedTypeArgumentsResolved(typeArguments, false) but resolve each element
        List<JCExpression> ret = List.nil();
        for(int i=typeArguments.size()-1;i>=0;i--){
            ret = ret.prepend(makeReifiedTypeArgumentResolved(typeArguments.get(i).resolveAliases(), false));
        }
        return ret;
    }
    
    private List<JCExpression> makeReifiedTypeArgumentsResolved(
            java.util.List<Type> typeArguments, boolean qualified) {
        List<JCExpression> ret = List.nil();
        for(int i=typeArguments.size()-1;i>=0;i--){
            ret = ret.prepend(makeReifiedTypeArgumentResolved(typeArguments.get(i), qualified));
        }
        return ret;
    }

    public JCExpression makeReifiedTypeArgument(Type pt) {
        return makeReifiedTypeArgumentResolved(pt.resolveAliases(), false);
    }
    
    private JCExpression makeReifiedTypeArgumentResolved(Type pt, boolean qualified) {
        if(pt.isUnion()){
            // FIXME: refactor this shite
            List<JCExpression> typeTestArguments = List.nil();
            java.util.List<Type> typeParameters = pt.getCaseTypes();
            if(typeParameters.size() == 2){
                Type alternative = null;
                if(typeParameters.get(0).isEmpty())
                    alternative = typeParameters.get(1);
                else if(typeParameters.get(1).isEmpty())
                    alternative = typeParameters.get(0);
                if(alternative != null && alternative.isTuple()){
                    JCExpression tupleType = makeTupleTypeDescriptor(alternative, true);
                    if(tupleType != null)
                        return tupleType;
                }
            }
            for(int i=typeParameters.size()-1;i>=0;i--){
                typeTestArguments = typeTestArguments.prepend(makeReifiedTypeArgument(typeParameters.get(i)));
            }
            return make().Apply(null, makeSelect(makeTypeDescriptorType(), "union"), typeTestArguments);
        } else if(pt.isIntersection()){
            List<JCExpression> typeTestArguments = List.nil();
            java.util.List<Type> typeParameters = pt.getSatisfiedTypes();
            for(int i=typeParameters.size()-1;i>=0;i--){
                typeTestArguments = typeTestArguments.prepend(makeReifiedTypeArgument(typeParameters.get(i)));
            }
            return make().Apply(null, makeSelect(makeTypeDescriptorType(), "intersection"), typeTestArguments);
        } else if(pt.isNothing()){
            return makeNothingTypeDescriptor();
        }
        
        TypeDeclaration declaration = pt.getDeclaration();
        if(declaration instanceof Constructor){
            pt = pt.getExtendedType();
            declaration = pt.getDeclaration();
        }
        if(pt.isClassOrInterface()){
            if(declaration.isJavaEnum()){
                pt = pt.getExtendedType();
                declaration = pt.getDeclaration();
            }
            // see if we have an alias for it
            if(supportsReifiedAlias((ClassOrInterface) declaration)){
                JCExpression qualifier = naming.makeDeclarationName(declaration, DeclNameFlag.QUALIFIED);
                return makeSelect(qualifier, naming.getTypeDescriptorAliasName());
            }
            if(pt.isTuple()){
                JCExpression tupleType = makeTupleTypeDescriptor(pt, false);
                if(tupleType != null)
                    return tupleType;
            }
            // no alias, must build it
            List<JCExpression> typeTestArguments = makeReifiedTypeArgumentsResolved(pt.getTypeArgumentList(), qualified);
            JCExpression thisType = makeUnerasedClassLiteral(declaration);
            // do we have variance overrides?
            Map<TypeParameter, SiteVariance> varianceOverrides = pt.getVarianceOverrides();
            if(!varianceOverrides.isEmpty()){
                // we need to pass them as second argument then, in an array
                ListBuffer<JCExpression> varianceElements = new ListBuffer<JCExpression>();
                for(TypeParameter typeParameter : declaration.getTypeParameters()){
                    SiteVariance useSiteVariance = varianceOverrides.get(typeParameter);
                    String selector;
                    if(useSiteVariance != null){
                        switch(useSiteVariance){
                        case IN:
                            selector = "IN";
                            break;
                        case OUT:
                            selector = "OUT";
                            break;
                        default:
                            selector = "NONE";
                            break;
                        }
                    }else{
                        selector = "NONE";
                    }
                    JCExpression varianceElement = make().Select(makeIdent(syms().ceylonVarianceType), names().fromString(selector));
                    varianceElements.append(varianceElement);
                }
                JCNewArray varianceArray = make().NewArray(makeIdent(syms().ceylonVarianceType), List.<JCExpression>nil(), varianceElements.toList());
                typeTestArguments = typeTestArguments.prepend(varianceArray);
            }
            typeTestArguments = typeTestArguments.prepend(thisType);
            JCExpression classDescriptor = make().Apply(null, makeSelect(makeTypeDescriptorType(), "klass"), typeTestArguments);
            Type qualifyingType = pt.getQualifyingType();
            JCExpression containerType = null;
            if(qualifyingType == null
                    // ignore qualifying types of static java declarations
                    && (Decl.isCeylon(declaration)
                            || !declaration.isStaticallyImportable())){
                // it may be contained in a function or value, and we want its type
                Declaration enclosingDeclaration = getDeclarationContainer(declaration);
                if(enclosingDeclaration instanceof TypedDeclaration)
                    containerType = makeTypedDeclarationTypeDescriptorResolved((TypedDeclaration) enclosingDeclaration);
                else if(enclosingDeclaration instanceof TypeDeclaration){
                    qualifyingType = ((TypeDeclaration) enclosingDeclaration).getType();
                }
            }
            if (qualifyingType != null && 
                    qualifyingType.getDeclaration() instanceof Constructor) {
                qualifyingType = qualifyingType.getQualifyingType();
            }
            if(qualifyingType != null){
                containerType = makeReifiedTypeArgumentResolved(qualifyingType, true);
            }
            if(containerType == null){
                return classDescriptor;
            }else{
                return make().Apply(null, makeSelect(makeTypeDescriptorType(), "member"), 
                                                     List.of(containerType, classDescriptor));
            }
        } else if(pt.isTypeParameter()){
            TypeParameter tp = (TypeParameter) declaration;
            String name = naming.getTypeArgumentDescriptorName(tp);
            if(!qualified || isTypeParameterSubstituted(tp))
                return makeUnquotedIdent(name);
            Scope container = tp.getContainer();
            JCExpression qualifier = null;
            if(container instanceof Class){
                qualifier = naming.makeQualifiedThis(makeJavaType(((Class)container).getType(), JT_RAW));
            }else if(container instanceof Interface){
                qualifier = naming.makeQualifiedThis(makeJavaType(((Interface)container).getType(), JT_COMPANION | JT_RAW));
            }else if(container instanceof Function){
                // name must be a unique name, as returned by getTypeArgumentDescriptorName
                return makeUnquotedIdent(name);
            }else{
                throw BugException.unhandledCase(container);
            }
            return makeSelect(qualifier, name);
        } else {
            throw BugException.unhandledDeclarationCase(declaration);
        }
    }
    
    private JCExpression makeTupleTypeDescriptor(Type pt, boolean firstElementOptional) {
        java.util.List<Type> tupleElementTypes = typeFact().getTupleElementTypes(pt);
        boolean isVariadic = typeFact().isTupleLengthUnbounded(pt);
        boolean atLeastOne = false;
        boolean needsRestSplit = false;
        Type restType = null;
        
        if(isVariadic){
            // unwrap the last element
            restType = tupleElementTypes.get(tupleElementTypes.size()-1);
            // types like Tuple<X,X,Anything> are invalid but allowed by the typechecker
            // in this case we have variadic=true and restType.isUnknown so just refuse
            // to optimise
            if(restType.isUnknown())
                return null;
            tupleElementTypes.set(tupleElementTypes.size()-1, typeFact.getSequentialElementType(restType));
            atLeastOne = restType.getDeclaration().inherits(typeFact().getSequenceDeclaration());
            // the last rest element may be a type param, in which case we resolve it at runtime
            needsRestSplit = !restType.getDeclaration().equals(typeFact.getSequenceDeclaration())
                    && !restType.getDeclaration().equals(typeFact.getSequentialDeclaration());
        }
        
        int firstDefaulted;
        if(!firstElementOptional){
            // only do this crazy computation if the first element is not optional (case of []|[A] which is a union type really)
            int minimumLength = typeFact().getTupleMinimumLength(pt);
            // ignore atLeastOne in the computation
            // [A,B=] -> 1
            // [A=,B*] -> 0
            // [A,B+] -> 2
            // [B*] -> 0
            // [B+] -> 1
            if(atLeastOne)
                minimumLength--;
            // [A,B=] -> 1
            // [A=,B*] -> 0
            // [A,B+] -> 1
            // [B*] -> 0
            // [B+] -> 0

            // [A,B=] -> 2
            // [A=,B*] -> 1
            // [A,B+] -> 1
            // [B*] -> 0
            // [B+] -> 0
            int nonVariadicParams = tupleElementTypes.size();
            if(isVariadic)
                nonVariadicParams--;

            // [A,B=] -> 2!=1 -> 1
            // [A=,B*] -> 1!=0 -> 0
            // [A,B+] -> 1==1 -> -1
            // [B*] -> 0==0 -> -1
            // [B+] -> 0==0 -> -1
            firstDefaulted = nonVariadicParams != minimumLength ? minimumLength : -1;
        }else{
            firstDefaulted = 0;
        }

        JCExpression restTypeDescriptor = null;
        JCExpression restElementTypeDescriptor = null;
        if(needsRestSplit){
            Type restElementType = tupleElementTypes.get(tupleElementTypes.size()-1);
            tupleElementTypes.remove(tupleElementTypes.size()-1);
            restTypeDescriptor = makeReifiedTypeArgumentResolved(restType, false);
            restElementTypeDescriptor = makeReifiedTypeArgumentResolved(restElementType, false);
        }
        
        ListBuffer<JCExpression> args = new ListBuffer<JCExpression>();
        if(needsRestSplit){
            args.append(restTypeDescriptor);
            args.append(restElementTypeDescriptor);
        }else{
            args.append(makeBoolean(isVariadic));
            args.append(makeBoolean(atLeastOne));
        }
        args.append(makeInteger(firstDefaulted));
        for(Type element : tupleElementTypes){
            args.append(makeReifiedTypeArgumentResolved(element, false));
        }
        JCExpression tupleDescriptor = make().Apply(null, makeSelect(makeTypeDescriptorType(), 
                needsRestSplit ? "tupleWithRest": "tuple"), args.toList());
        return tupleDescriptor;
    }

    protected boolean hasTypeArguments(Type type){
        if(!type.getTypeArguments().isEmpty())
            return true;
        Type qualifyingType = type.getQualifyingType();
        if(qualifyingType == null){
            Declaration declaration = type.getDeclaration();
            do{
                // it may be contained in a function or value, and we want its type
                Declaration enclosingDeclaration = getDeclarationContainer(declaration);
                if(enclosingDeclaration instanceof TypedDeclaration){
                    // must be in scope
                    if(enclosingDeclaration instanceof Generic 
                            && !((Generic) enclosingDeclaration).getTypeParameters().isEmpty())
                        return true;
                    // look up the containers
                    declaration = enclosingDeclaration;
                }else if(enclosingDeclaration instanceof TypeDeclaration){
                    // must be in scope, recurse
                    return hasTypeArguments(((TypeDeclaration) enclosingDeclaration).getType());
                }else{
                    // that's fucked up
                    break;
                }
                // go up every containing typed declaration
            }while(declaration != null);
        }else
            return hasTypeArguments(qualifyingType);
        // did not find any
        return false;
    }
    
    private JCExpression makeTypedDeclarationTypeDescriptorResolved(TypedDeclaration declaration) {
        // figure out the method name
        String methodName = declaration.getPrefixedName();
        List<JCExpression> arguments;
        if(declaration instanceof Function)
            arguments = makeReifiedTypeArgumentsResolved(getTypeArguments((Function)declaration), true);
        else
            arguments = List.nil();
        if(declaration.isToplevel()){
            JCExpression getterClassNameExpr;
            if(declaration instanceof Function){
                getterClassNameExpr = naming.makeName(declaration, Naming.NA_FQ | Naming.NA_WRAPPER);
            }else{
                String getterClassName = Naming.getAttrClassName(declaration, 0);
                getterClassNameExpr = naming.makeUnquotedIdent(getterClassName);
            }
            arguments = arguments.prepend(makeSelect(getterClassNameExpr, "class"));
        }else
            arguments = arguments.prepend(make().Literal(methodName));

        JCMethodInvocation typedDeclarationDescriptor = make().Apply(null, makeSelect(makeTypeDescriptorType(), "functionOrValue"), 
                                                                     arguments);
        // see if the declaration has a container too
        Declaration enclosingDeclaration = getDeclarationContainer(declaration);
        JCExpression containerType = null;
        if(enclosingDeclaration instanceof TypedDeclaration)
            containerType = makeTypedDeclarationTypeDescriptorResolved((TypedDeclaration) enclosingDeclaration);
        else if(enclosingDeclaration instanceof TypeDeclaration){
            Type qualifyingType = ((TypeDeclaration) enclosingDeclaration).getType();
            containerType = makeReifiedTypeArgumentResolved(qualifyingType, true);
        }
        if(containerType == null){
            return typedDeclarationDescriptor;
        }else{
            return make().Apply(null, makeSelect(makeTypeDescriptorType(), "member"), 
                                                 List.of(containerType, typedDeclarationDescriptor));
        }
    }

    JCExpressionStatement makeReifiedTypeParameterAssignment(
            TypeParameter param) {
        String descriptorName = naming.getTypeArgumentDescriptorName(param);
        return make().Exec(make().Assign(
                naming.makeQualIdent(naming.makeThis(), descriptorName), 
                naming.makeQualIdent(null, descriptorName)));
    }

    JCVariableDecl makeReifiedTypeParameterVarDecl(TypeParameter param, boolean isCompanion) {
        String descriptorName = naming.getTypeArgumentDescriptorName(param);
        long flags = PRIVATE;
        if(!isCompanion)
            flags |= FINAL;
        List<JCAnnotation> annotations = makeAtIgnore();
        JCVariableDecl localVar = make().VarDef(make().Modifiers(flags, annotations), names().fromString(descriptorName), 
                makeTypeDescriptorType(), null);
        return localVar;
    }
    
    protected Declaration getDeclarationContainer(Declaration declaration) {
        // Here we can use getContainer, we don't care about scopes
        Scope container = declaration.getContainer();
        while(container != null){
            if(container instanceof Package)
                return null;
            if(container instanceof Declaration){
                if(Decl.skipContainer(container)){
                    // skip it and go on
                }else{
                    return (Declaration) container;
                }
            }
            container = container.getContainer();
        }
        // did not find anything
        return null;
    }

    public boolean supportsReifiedAlias(ClassOrInterface decl){
        return !decl.isAlias() 
                && decl.getTypeParameters().isEmpty()
                && supportsReified(decl)
                && Decl.isToplevel(decl)
                // those are not allowed because we can't have statics in them (for a reason I can't understand but
                // is not worth wasting time on)
                && !Decl.isTopLevelObjectExpressionType(decl);
    }
    
    boolean isSequencedAnnotation(Class klass) {
        TypeDeclaration meta = typeFact().getSequencedAnnotationDeclaration();
        return meta != null && klass.getType().isSubtypeOf(
                meta.appliedType(null, 
                Arrays.asList(typeFact().getAnythingType(), typeFact().getNothingType())));
    }

    private Module getLanguageModule() {
        return loader.getLanguageModule();
    }

    private Module getJDKBaseModule() {
        return loader.getJDKBaseModule();
    }

    Type getGetterInterfaceType(TypedDeclaration attrTypedDecl) {
        TypedReference typedRef = getTypedReference(attrTypedDecl);
        TypedReference nonWideningTypedRef = nonWideningTypeDecl(typedRef);
        Type nonWideningType = nonWideningType(typedRef, nonWideningTypedRef);
        
        Type type;
        boolean unboxed = CodegenUtil.isUnBoxed(attrTypedDecl);
        if (unboxed && isCeylonBoolean(nonWideningType)) {
            type = javacCeylonTypeToProducedType(syms().ceylonGetterBooleanType);
        } else if (unboxed && isCeylonInteger(nonWideningType)) {
            type = javacCeylonTypeToProducedType(syms().ceylonGetterLongType);
        } else if (unboxed && isCeylonFloat(nonWideningType)) {
            type = javacCeylonTypeToProducedType(syms().ceylonGetterDoubleType);
        } else if (unboxed && isCeylonCharacter(nonWideningType)) {
            type = javacCeylonTypeToProducedType(syms().ceylonGetterIntType);
        } else if (unboxed && isCeylonByte(nonWideningType)) {
            type = javacCeylonTypeToProducedType(syms().ceylonGetterByteType);
        } else {
            type = javacCeylonTypeToProducedType(syms().ceylonGetterType);
            Type typeArg = nonWideningType;
            if (unboxed && isCeylonString(typeArg)) {
                typeArg = javacJavaTypeToProducedType(syms().stringType);
            }
            type = appliedType(type.getDeclaration(), typeArg);
        }
        return type;
    }
    
    /**
     * Makes a {@code java.lang.Class<? extends UPPERBOUND>}
     */
    JCExpression makeJavaClassTypeBounded(Type upperBound) {
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

    /**
    * If we satisfy a Foo<T> and T is variant, we implement Foo<T> but our impl
    * has type Foo<? extends T> or Foo<? super T> (to allow for refining it more than once).
    * So if we call a method which includes this Foo type in an invariant location, we will
    * think we get a Foo<? extends T> but in reality we get a Foo<? extends capture#X of ? extends T> which
    * is not the same thing and does not work in invariant locations.
    * Note that this can't happen for real invariant locations since the typechecker will prevent it, but it
    * happens when we must fix variant locations due to type parameter dependences like in Tuple.
    * 
    * See https://github.com/ceylon/ceylon-compiler/issues/1550
    * 
    * @param declaration the type declaration which we should check has variant type parameters and appears in an invariant
    *                    position in the given type.
    * @param type the type in which to check for the given declaration, in an invariant position.
    * @return true if all these conditions are met.
    */
    public boolean needsRawCastForMixinSuperCall(TypeDeclaration declaration, Type type) {
        return !declaration.getTypeParameters().isEmpty()
                && hasVariantTypeParameters(declaration)
                && declarationAppearsInInvariantPosition(declaration, type.resolveAliases());
    }
    
    private boolean declarationAppearsInInvariantPosition(TypeDeclaration declaration, Type type) {
        if(type.isUnion()){
            for(Type pt : type.getCaseTypes()){
                if(declarationAppearsInInvariantPosition(declaration, pt))
                    return true;
            }
            return false;
        }
        if(type.isIntersection()){
            for(Type pt : type.getSatisfiedTypes()){
                if(declarationAppearsInInvariantPosition(declaration, pt))
                    return true;
            }
            return false;
        }
        if(type.isClassOrInterface()){
            TypeDeclaration typeDeclaration = type.getDeclaration();
            java.util.List<TypeParameter> typeParameters = typeDeclaration.getTypeParameters();
            Map<TypeParameter, Type> typeArguments = type.getTypeArguments();
            for(TypeParameter tp : typeParameters){
                Type typeArgument = typeArguments.get(tp);
                if(tp.isInvariant()
                        || hasDependentTypeParameters(typeParameters, tp)){
                    if(Decl.equal(typeArgument.getDeclaration(), declaration)){
                        return true;
                    }
                }
                if(declarationAppearsInInvariantPosition(declaration, typeArgument))
                    return true;
            }
        }
        return false;
    }
    
    private boolean hasVariantTypeParameters(TypeDeclaration declaration) {
        for(TypeParameter tp : declaration.getTypeParameters()){
            if(!tp.isInvariant())
                return true;
        }
        return false;
    }

    protected Type getOptionalTypeForInteropIfAllowed(Type expectedType, Type termType, Term term){
        // make sure we do not insert null checks if we're going to allow testing for null
        if(expectedType != null
                && hasUncheckedNulls(term)
                && !isOptional(termType)){
            // get rid of null-check if we accept an optional type on the LHS
            return typeFact().getOptionalType(termType);
        }
        return termType;
    }
    
    public JCThrow makeThrowUnresolvedCompilationError(String exceptionMessage) {
        return make().Throw(make().NewClass(null, 
                List.<JCExpression>nil(),
                make().QualIdent(syms().ceylonUnresolvedCompilationErrorType.tsym),
                List.<JCExpression>of(make().Literal(exceptionMessage)), null));
    }
    
    public JCThrow makeThrowUnresolvedCompilationError(LocalizedError error) {
        String errorMessage = error.getErrorMessage().getMessage();
        at(error.getNode());
        return makeThrowUnresolvedCompilationError(errorMessage != null ? errorMessage : "compiler bug: error with unknown message");
    }
    
    protected void addTypeParameterSubstitution(java.util.List<TypeParameter> typeParameters) {
        typeParameterSubstitutions.push(typeParameters);
    }

    protected void popTypeParameterSubstitution() {
        typeParameterSubstitutions.pop();
    }

    private boolean isTypeParameterSubstituted(TypeParameter tp) {
        for(java.util.List<TypeParameter> list : typeParameterSubstitutions){
            for(TypeParameter tp2 : list){
                if(tp2.equals(tp))
                    return true;
            }
        }
        return false;
    }
    
    JCThrow makeThrowAssertionException(JCExpression messageExpr) {
        JCExpression exception = make().NewClass(null, null,
                makeIdent(syms().ceylonAssertionErrorType),
                List.<JCExpression>of(messageExpr),
                null);
        return make().Throw(exception);
    }
}
