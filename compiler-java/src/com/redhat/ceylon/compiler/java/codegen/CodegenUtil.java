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

import static com.redhat.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.isIndirectInvocation;
import static com.redhat.ceylon.compiler.typechecker.tree.TreeUtil.unwrapExpressionUntilTerm;

import java.util.List;
import java.util.Map;

import com.redhat.ceylon.common.BooleanUtil;
import com.redhat.ceylon.compiler.java.codegen.AbstractTransformer.BoxingStrategy;
import com.redhat.ceylon.compiler.java.codegen.Naming.DeclNameFlag;
import com.redhat.ceylon.compiler.typechecker.analyzer.AnalyzerUtil;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.BaseMemberExpression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.CompilerAnnotation;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Term;
import com.redhat.ceylon.model.loader.JvmBackendUtil;
import com.redhat.ceylon.model.loader.NamingBase.Unfix;
import com.redhat.ceylon.model.typechecker.model.Class;
import com.redhat.ceylon.model.typechecker.model.Declaration;
import com.redhat.ceylon.model.typechecker.model.Function;
import com.redhat.ceylon.model.typechecker.model.FunctionOrValue;
import com.redhat.ceylon.model.typechecker.model.Interface;
import com.redhat.ceylon.model.typechecker.model.Package;
import com.redhat.ceylon.model.typechecker.model.Parameter;
import com.redhat.ceylon.model.typechecker.model.Scope;
import com.redhat.ceylon.model.typechecker.model.Specification;
import com.redhat.ceylon.model.typechecker.model.Type;
import com.redhat.ceylon.model.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.model.typechecker.model.TypedDeclaration;
import com.redhat.ceylon.model.typechecker.model.Unit;
import com.redhat.ceylon.model.typechecker.model.Value;

/**
 * Utility functions that are specific to the codegen package
 * @see AnalyzerUtil
 */
public class CodegenUtil {

    
    private CodegenUtil(){}
    

    static boolean isErasedAttribute(String name){
        // ERASURE
        return "hash".equals(name) || "string".equals(name);
    }

    public static boolean isHashAttribute(Declaration model) {
        return model instanceof Value
                && Decl.withinClassOrInterface(model)
                && model.isShared()
                && "hash".equals(model.getName());
    }

    static boolean isUnBoxed(Term node){
        return node.getUnboxed();
    }

    static boolean isUnBoxed(TypedDeclaration decl){
        // null is considered boxed
        return BooleanUtil.isTrue(decl.getUnboxed());
    }

    static void markUnBoxed(Term node) {
        node.setUnboxed(true);
    }

    static BoxingStrategy getBoxingStrategy(Term node) {
        return isUnBoxed(node) ? BoxingStrategy.UNBOXED : BoxingStrategy.BOXED;
    }

    static BoxingStrategy getBoxingStrategy(TypedDeclaration decl) {
        return isUnBoxed(decl) ? BoxingStrategy.UNBOXED : BoxingStrategy.BOXED;
    }

    static boolean isRaw(TypedDeclaration decl){
        Type type = decl.getType();
        return type != null && type.isRaw();
    }

    static boolean isRaw(Term node){
        Type type = node.getTypeModel();
        return type != null && type.isRaw();
    }

    static void markRaw(Term node) {
        Type type = node.getTypeModel();
        if(type != null)
            type.setRaw(true);
    }
    
    static boolean hasTypeErased(Term node){
        return node.getTypeErased();
    }

    static boolean hasTypeErased(TypedDeclaration decl){
        return decl.getTypeErased();
    }

    static void markTypeErased(Term node) {
        markTypeErased(node, true);
    }

    static void markTypeErased(Term node, boolean erased) {
        node.setTypeErased(erased);
    }

    static void markUntrustedType(Term node) {
       node.setUntrustedType(true);
    }

    static boolean hasUntrustedType(Term node) {
        return node.getUntrustedType();
    }

    static boolean hasUntrustedType(TypedDeclaration decl){
        Boolean ret = decl.getUntrustedType();
        return ret != null && ret.booleanValue();
    }

    /**
     * Determines if the given statement or argument has a compiler annotation 
     * with the given name.
     * 
     * @param decl The statement or argument
     * @param name The compiler annotation name
     * @return true if the statement or argument has an annotation with the 
     * given name
     * 
     * @see #hasCompilerAnnotationWithArgument(com.redhat.ceylon.compiler.typechecker.tree.Tree.Declaration, String, String)
     */
    static boolean hasCompilerAnnotation(Tree.StatementOrArgument decl, String name){
        for(CompilerAnnotation annotation : decl.getCompilerAnnotations()){
            if(annotation.getIdentifier().getText().equals(name))
                return true;
        }
        return false;
    }
    
    static boolean hasCompilerAnnotationNoArgument(Tree.StatementOrArgument decl, String name){
        for (CompilerAnnotation annotation : decl.getCompilerAnnotations()) {
            if (annotation.getIdentifier().getText().equals(name)) {
                if (annotation.getStringLiteral() == null) {
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * Determines if the given statement or argument has a compiler annotation 
     * with the given name and with the given argument.
     * 
     * @param decl The statement or argument
     * @param name The compiler annotation name
     * @param argument The compiler annotation's argument
     * @return true if the statement or argument has an annotation with the 
     * given name and argument value
     * 
     * @see #hasCompilerAnnotation(com.redhat.ceylon.compiler.typechecker.tree.Tree.Declaration, String)
     */
    static boolean hasCompilerAnnotationWithArgument(Tree.StatementOrArgument decl, String name, String argument){
        for (CompilerAnnotation annotation : decl.getCompilerAnnotations()) {
            if (annotation.getIdentifier().getText().equals(name)) {
                if (annotation.getStringLiteral() == null) {
                    continue;
                } 
                String text = annotation.getStringLiteral().getText();
                if (text == null) {
                    continue;
                }
                if (text.equals(argument)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private static String getCompilerAnnotationArgument(Iterable<Tree.CompilerAnnotation> compilerAnnotations, String name) {
        for (CompilerAnnotation annotation : compilerAnnotations) {
            if (annotation.getIdentifier().getText().equals(name)) {
                if (annotation.getStringLiteral() == null) {
                    continue;
                } 
                String text = annotation.getStringLiteral().getText();
                return text;
            }
        }
        return null;
    }
    
    static String getCompilerAnnotationArgument(Tree.StatementOrArgument def, 
            String name) {
        return getCompilerAnnotationArgument(def.getCompilerAnnotations(), name);
    }
    
    /**
     * Returns the compiler annotation with the given name on the 
     * given compilation unit, or null iff the unit lacks that annotation.  
     */
    static String getCompilerAnnotationArgument(Tree.CompilationUnit def, 
            String name) {
        return getCompilerAnnotationArgument(def.getCompilerAnnotations(), name);
    }


    static boolean isDirectAccessVariable(Term term) {
        if(!(term instanceof BaseMemberExpression) || !term.getUnboxed())
            return false;
        Declaration decl = ((BaseMemberExpression)term).getDeclaration();
        if(decl == null) // typechecker error
            return false;
        // make sure we don't try to optimise things which can't be optimised
        return Decl.isValue(decl)
                && !decl.isToplevel()
                && !decl.isClassOrInterfaceMember()
                && !decl.isCaptured()
                && !decl.isShared();
    }

    static Declaration getTopmostRefinedDeclaration(Declaration decl){
        return JvmBackendUtil.getTopmostRefinedDeclaration(decl);
    }

    static Declaration getTopmostRefinedDeclaration(Declaration decl, Map<Function, Function> methodOverrides){
        return JvmBackendUtil.getTopmostRefinedDeclaration(decl, methodOverrides);
    }
    
    static Parameter findParamForDecl(Tree.TypedDeclaration decl) {
        String attrName = decl.getIdentifier().getText();
        return findParamForDecl(attrName, decl.getDeclarationModel());
    }
    
    static Parameter findParamForDecl(TypedDeclaration decl) {
        return JvmBackendUtil.findParamForDecl(decl);
    }
    
    static Parameter findParamForDecl(String attrName, TypedDeclaration decl) {
        return JvmBackendUtil.findParamForDecl(attrName, decl);
    }
    
    static FunctionOrValue findMethodOrValueForParam(Parameter param) {
        return param.getModel();
    }

    static boolean isVoid(Type type) {
        return type != null && type.getDeclaration() != null
                && type.getDeclaration().getUnit().getAnythingType().isExactly(type);    
    }


    public static boolean canOptimiseMethodSpecifier(Term expression, Function m) {
        if(expression instanceof Tree.FunctionArgument)
            return true;
        if(expression instanceof Tree.BaseMemberOrTypeExpression == false)
            return false;
        Declaration declaration = ((Tree.BaseMemberOrTypeExpression)expression).getDeclaration();
        // methods are fine because they are constant
        if(declaration instanceof Function)
            return true;
        // toplevel constructors are fine
        if(declaration instanceof Class)
            return true;
        // parameters are constant too
        if(declaration instanceof FunctionOrValue
                && ((FunctionOrValue)declaration).isParameter())
            return true;
        // the rest we can't know: we can't trust attributes that could be getters or overridden
        // we can't trust even toplevel attributes that could be made variable in the future because of
        // binary compat.
        return false;
    }
    
    public static Declaration getParameterized(Parameter parameter) {
        return parameter.getDeclaration();
    }
    
    public static Declaration getParameterized(FunctionOrValue methodOrValue) {
        return JvmBackendUtil.getParameterized(methodOrValue);
    }
    
    public static boolean isContainerFunctionalParameter(Declaration declaration) {
        Scope containerScope = declaration.getContainer();
        Declaration containerDeclaration;
        if (containerScope instanceof Specification) {
            containerDeclaration = ((Specification)containerScope).getDeclaration();
        } else if (containerScope instanceof Declaration) {
            containerDeclaration = (Declaration)containerScope;
        } else {
            throw BugException.unhandledCase(containerScope);
        }
        return containerDeclaration instanceof Function
                && ((Function)containerDeclaration).isParameter();
    }
    
    public static boolean isMemberReferenceInvocation(Tree.InvocationExpression expr) {
        Tree.Term p = unwrapExpressionUntilTerm(expr.getPrimary());
        if (isIndirectInvocation(expr, true)
                && p instanceof Tree.QualifiedMemberOrTypeExpression) {
            Tree.QualifiedMemberOrTypeExpression primary = (Tree.QualifiedMemberOrTypeExpression)p;
            Tree.Term q = unwrapExpressionUntilTerm(primary.getPrimary());
            if (q instanceof Tree.BaseTypeExpression
                    || q instanceof Tree.QualifiedTypeExpression) {
                return true;
            }
        }
        return false;
    }


    /**
     * Returns true if the given produced type is a type parameter or has type arguments which
     * are type parameters.
     */
    public static boolean containsTypeParameter(Type type) {
        if(type.isTypeParameter())
            return true;
        for(Type pt : type.getTypeArgumentList()){
            if(containsTypeParameter(pt)){
                return true;
            }
        }
        if(type.isIntersection()){
            List<Type> types = type.getSatisfiedTypes();
            for(int i=0,l=types.size();i<l;i++){
                if(containsTypeParameter(types.get(i)))
                    return true;
            }
            return false;
        }
        if(type.isUnion()){
            List<Type> types = type.getCaseTypes();
            for(int i=0,l=types.size();i<l;i++){
                if(containsTypeParameter(types.get(i)))
                    return true;
            }
            return false;
        }
        return false;
    }

    public static boolean isCompanionClassNeeded(TypeDeclaration decl) {
        return decl instanceof Interface 
                && BooleanUtil.isNotFalse(((Interface)decl).isCompanionClassNeeded());
    }
    
    /** 
     * <p>Returns the fully qualified name java name of the given declaration, 
     * for example
     * {@code ceylon.language.sum_.sum} or {@code my.package.Outer.Inner}.
     * for any toplevel or externally visible Ceylon declaration.</p>
     * 
     * <p>Used by the IDE to support finding/renaming Ceylon declarations 
     * called from Java.</p>
     * */
    public static String getJavaNameOfDeclaration(Declaration decl) {
        Scope s = decl.getScope();
        while (!(s instanceof Package)) {
            if (!(s instanceof TypeDeclaration)) {
                throw new IllegalArgumentException();
            }
            s = s.getContainer();
        }
        String result;
        Naming n = new Naming(null, null);
        if (decl instanceof TypeDeclaration) {
            result = n.makeTypeDeclarationName((TypeDeclaration)decl, DeclNameFlag.QUALIFIED);
            result = result.substring(1);// remove initial .
            if (decl.isAnonymous()) {
                result += "." + Unfix.get_.toString();
            }
        } else if (decl instanceof TypedDeclaration) {
            if (decl.isToplevel()) {
                result = n.getName((TypedDeclaration)decl, Naming.NA_FQ | Naming.NA_WRAPPER | Naming.NA_MEMBER);
                result = result.substring(1);// remove initial .
            } else {
                Scope container = decl.getContainer();
                if (container instanceof TypeDeclaration) {
                    String qualifier = getJavaNameOfDeclaration((TypeDeclaration)container);
                    result = qualifier+n.getName((TypedDeclaration)decl, Naming.NA_MEMBER);
                } else {
                    throw new IllegalArgumentException();
                }
            }
        } else {
            throw new RuntimeException();
        }
        return result;
    }

    public static boolean needsLateInitField(TypedDeclaration attrType, Unit unit) {
        return attrType.isLate()
                // must be unboxed (except String)
                && ((isUnBoxed(attrType) && !attrType.getType().isString())
                    // or must be optional
                    || unit.isOptionalType(attrType.getType()));
    }


    public static boolean hasDelegatingConstructors(Tree.ClassOrInterface def) {
        if(def instanceof Tree.ClassDefinition == false)
            return false;
        for (Tree.Statement stmt : ((Tree.ClassDefinition)def).getClassBody().getStatements()) {
            if (stmt instanceof Tree.Constructor) {
                Tree.Constructor ctor = (Tree.Constructor)stmt;
                // find every constructor which delegates to another constructor
                if (ctor.getDelegatedConstructor() != null
                        && ctor.getDelegatedConstructor().getInvocationExpression() != null) {
                    if (ctor.getDelegatedConstructor().getInvocationExpression().getPrimary() instanceof Tree.ExtendedTypeExpression) {
                        Tree.ExtendedTypeExpression ete = (Tree.ExtendedTypeExpression)ctor.getDelegatedConstructor().getInvocationExpression().getPrimary();
                        // are we delegating to a constructor (not a supertype) of the same class (this class)?
                        if (Decl.isConstructor(ete.getDeclaration())
                                && Decl.getConstructedClass(ete.getDeclaration()).equals(def.getDeclarationModel())) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}
