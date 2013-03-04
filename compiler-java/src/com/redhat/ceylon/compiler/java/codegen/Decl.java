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

import com.redhat.ceylon.compiler.java.util.Util;
import com.redhat.ceylon.compiler.loader.model.FieldValue;
import com.redhat.ceylon.compiler.loader.model.LazyClass;
import com.redhat.ceylon.compiler.loader.model.LazyInterface;
import com.redhat.ceylon.compiler.typechecker.model.Class;
import com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface;
import com.redhat.ceylon.compiler.typechecker.model.ControlBlock;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Element;
import com.redhat.ceylon.compiler.typechecker.model.Functional;
import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.MethodOrValue;
import com.redhat.ceylon.compiler.typechecker.model.NamedArgumentList;
import com.redhat.ceylon.compiler.typechecker.model.Package;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.Scope;
import com.redhat.ceylon.compiler.typechecker.model.Setter;
import com.redhat.ceylon.compiler.typechecker.model.Specification;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.TypedDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.Value;
import com.redhat.ceylon.compiler.typechecker.model.Getter;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;

/**
 * Utility functions telling you about Ceylon declarations
 * @see Strategy
 */
public class Decl {
    private Decl() {
    }

    /**
     * Returns the declaration's container
     * @param decl The declaration
     * @return the declaration's container
     */
    public static Scope container(Tree.Declaration decl) {
        return container(decl.getDeclarationModel());
    }

    /**
     * Returns the declaration's container
     * @param decl The declaration
     * @return the declaration's container
     */
    public static Scope container(Declaration decl) {
        return decl.getContainer();
    }
    
    /**
     * Determines whether the declaration's is a getter (a transient value)
     * @param decl The declaration
     * @return true if the declaration is a getter
     */
    public static boolean isGetter(Declaration decl) {
        return (decl instanceof Value) && ((Value)decl).isTransient();
//        return (decl instanceof Getter);
    }

    /**
     * Determines whether the declaration's is a non-transient value (not a getter)
     * @param decl The declaration
     * @return true if the declaration is a value
     */
    public static boolean isValue(Declaration decl) {
        return (decl instanceof Value) && !((Value)decl).isTransient();
//        return (decl instanceof Value) && !(decl instanceof Getter);
    }

    /**
     * Determines whether the declaration's containing scope is a method
     * @param decl The declaration
     * @return true if the declaration is within a method
     */
    public static boolean withinMethod(Tree.Declaration decl) {
        return withinMethod(decl.getDeclarationModel());
    }
    
    /**
     * Determines whether the declaration's containing scope is a getter
     * @param decl The declaration
     * @return true if the declaration is within a getter
     */
    public static boolean withinGetter(Tree.Declaration decl) {
        return withinGetter(decl.getDeclarationModel());
    }
    
    /**
     * Determines whether the declaration's containing scope is a setter
     * @param decl The declaration
     * @return true if the declaration is within a setter
     */
    public static boolean withinSetter(Tree.Declaration decl) {
        return withinSetter(decl.getDeclarationModel());
    }

    /**
     * Determines whether the declaration's containing scope is a method
     * @param decl The declaration
     * @return true if the declaration is within a method
     */
    public static boolean withinMethod(Declaration decl) {
        return container(decl) instanceof Method;
    }
    
    /**
     * Determines whether the declaration's containing scope is a getter
     * @param decl The declaration
     * @return true if the declaration is within a getter
     */
    public static boolean withinGetter(Declaration decl) {
        Scope s = container(decl);
        return isGetter((Declaration)s);
    }
    
    /**
     * Determines whether the declaration's containing scope is a setter
     * @param decl The declaration
     * @return true if the declaration is within a setter
     */
    public static boolean withinSetter(Declaration decl) {
        return container(decl) instanceof Setter;
    }
    
    /**
     * Determines whether the declaration's containing scope is a package
     * @param decl The declaration
     * @return true if the declaration is within a package
     */
    public static boolean withinPackage(Tree.Declaration decl) {
        return container(decl) instanceof com.redhat.ceylon.compiler.typechecker.model.Package;
    }
    
    /**
     * Determines whether the declaration's containing scope is a class
     * @param decl The declaration
     * @return true if the declaration is within a class
     */
    public static boolean withinClass(Tree.Declaration decl) {
        return container(decl) instanceof com.redhat.ceylon.compiler.typechecker.model.Class;
    }
    
    /**
     * Determines whether the declaration's containing scope is an interface
     * @param decl The declaration
     * @return true if the declaration is within an interface
     */
    public static boolean withinInterface(Tree.Declaration decl) {
        return container(decl) instanceof com.redhat.ceylon.compiler.typechecker.model.Interface;
    }
    
    public static boolean withinInterface(Declaration decl) {
        return container(decl) instanceof com.redhat.ceylon.compiler.typechecker.model.Interface;
    }
    
    /**
     * Determines whether the declaration's containing scope is a class or interface
     * @param decl The declaration
     * @return true if the declaration is within a class or interface
     */
    public static boolean withinClassOrInterface(Tree.Declaration decl) {
        return withinClassOrInterface(decl.getDeclarationModel());
    }
    
    /**
     * Determines whether the declaration's containing scope is a class or interface
     * @param decl The declaration
     * @return true if the declaration is within a class or interface
     */
    public static boolean withinClassOrInterface(Declaration decl) {
        return container(decl) instanceof com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface;
    }
    
    public static boolean isShared(Tree.Declaration decl) {
        return isShared(decl.getDeclarationModel());
    }
    
    public static boolean isShared(Declaration decl) {
        return decl.isShared();
    }
    
    public static boolean isCaptured(Tree.Declaration decl) {
        return isCaptured(decl.getDeclarationModel());
    }
    
    public static boolean isCaptured(Declaration decl) {
        // Shared elements are implicitely captured although the typechecker doesn't mark them that way
        return decl.isCaptured() || decl.isShared();
    }

    public static boolean isAbstract(Tree.ClassOrInterface decl) {
        return decl.getDeclarationModel().isAbstract();
    }

    public static boolean isDefault(Tree.Declaration decl) {
        return decl.getDeclarationModel().isDefault();
    }

    public static boolean isFormal(Tree.Declaration decl) {
        return decl.getDeclarationModel().isFormal();
    }

    public static boolean isActual(Tree.Declaration decl) {
        return isActual(decl.getDeclarationModel());
    }
    
    public static boolean isActual(Declaration decl) {
        return decl.isActual();
    }

    public static boolean isVariable(Tree.AttributeDeclaration decl) {
        return decl.getDeclarationModel().isVariable();
    }
    
    public static boolean isLate(Tree.AttributeDeclaration decl) {
        return isLate(decl.getDeclarationModel());
    }

    public static boolean isLate(Value model) {
        return model.isLate();
    }
    
    public static boolean isToplevel(Tree.Declaration decl) {
        return isToplevel(decl.getDeclarationModel());
    }
    
    public static boolean isToplevel(Declaration decl) {
        return decl.isToplevel();
    }
    
    /**
     * Determines whether the declaration is local to a method,
     * getter, setter or class initializer.
     * @param decl The declaration
     * @return true if the declaration is local
     */
    public static boolean isLocal(Tree.Declaration decl) {
        return isLocal(decl.getDeclarationModel());
    }

    /**
     * Determines whether the declaration is local to a method,
     * getter, setter or class initializer.
     * @param decl The declaration
     * @return true if the declaration is local
     */
    public static boolean isLocal(Declaration decl) {
        return isLocalScope(decl.getContainer());
    }
    
    public static boolean isLocalScope(Scope scope) {
        return scope instanceof MethodOrValue 
                || scope instanceof ControlBlock
                || scope instanceof NamedArgumentList
                || scope instanceof Specification;
    }
    
    /**
     * Determines whether the declaration is local or a descendant of a 
     * local. 
     * @param decl The declaration
     * @return true if the decl is local or descendant from a local
     */
    public static boolean isAncestorLocal(Tree.Declaration decl) {
        return isAncestorLocal(decl.getDeclarationModel());
    }
    
    /**
     * Determines whether the declaration is local or a descendant of a 
     * local. 
     * @param decl The declaration
     * @return true if the decl is local or descendant from a local
     */
    public static boolean isAncestorLocal(Declaration decl) {
        Scope container = decl.getContainer();
        while (container != null) {
            if (container instanceof MethodOrValue
                    || container instanceof ControlBlock) {
                return true;
            }
            container = container.getContainer();
        }
        return false;
    }
        
    public static boolean isClassAttribute(Declaration decl) {
        return (withinClassOrInterface(decl))
                && (Decl.isValue(decl) || decl instanceof Setter)
                && (decl.isCaptured() || decl.isShared());
    }

    public static boolean isOverloaded(Declaration decl) {
        if (decl instanceof Functional) {
            return ((Functional)decl).isOverloaded();
        }
        return false;
    }

    public static boolean isJavaField(Declaration decl) {
        return decl instanceof FieldValue;
    }

    public static boolean isStatic(TypeDeclaration declaration) {
        if(declaration instanceof LazyClass){
            return ((LazyClass)declaration).isStatic();
        }
        if(declaration instanceof LazyInterface){
            return ((LazyInterface)declaration).isStatic();
        }
        return false;
    }

    public static boolean isCeylon(TypeDeclaration declaration) {
        if(declaration instanceof LazyClass){
            return ((LazyClass)declaration).isCeylon();
        }
        if(declaration instanceof LazyInterface){
            return ((LazyInterface)declaration).isCeylon();
        }
        // if it's not one of those it must be from source (Ceylon)
        return true;
    }

    public static boolean isDeferredOrParamInitialized(Tree.AnyMethod def) {
        return !Decl.isFormal(def)
            && def instanceof Tree.MethodDeclaration
            && ((Tree.MethodDeclaration)def).getSpecifierExpression() == null;
    }

    /**
     * Is the declaration a method declared to return {@code void} 
     * (as opposed to a {@code Anything})
     */
    public static boolean isUnboxedVoid(Declaration decl) {
        return Util.isUnboxedVoid(decl);
    }
    
    public static boolean isMpl(Functional decl) {
        return decl.getParameterLists().size() > 1;
    }
    
    public static ClassOrInterface getClassOrInterfaceContainer(Element decl){
        return getClassOrInterfaceContainer(decl, true);
    }
    
    public static ClassOrInterface getClassOrInterfaceContainer(Element decl, boolean includingDecl){
        if (!includingDecl) {
            decl = (Element) decl.getContainer();
        }
        // stop when null or when it's a ClassOrInterface
        while(decl != null
                && !(decl instanceof ClassOrInterface)){
            // stop if the container is not an Element
            if(!(decl.getContainer() instanceof Element))
                return null;
            decl = (Element) decl.getContainer();
        }
        return (ClassOrInterface) decl;
    }

    public static Package getPackageContainer(Scope scope){
        // stop when null or when it's a Package
        while(scope != null
                && !(scope instanceof Package)){
            // stop if the container is not a Scope
            if(!(scope.getContainer() instanceof Scope))
                return null;
            scope = (Scope) scope.getContainer();
        }
        return (Package) scope;
    }

    public static boolean isValueTypeDecl(Tree.Term decl) {
        if (decl != null){
            return isValueTypeDecl(decl.getTypeModel());
        }
        return false;
    }
    
    public static boolean isValueTypeDecl(TypedDeclaration decl) {
        if (decl != null){
            return isValueTypeDecl(decl.getType());
        }
        return false;
    }

    private static boolean isValueTypeDecl(ProducedType type) {
        type = type.resolveAliases();
        if ((type != null) && type.getDeclaration() instanceof LazyClass) {
            return ((LazyClass)type.getDeclaration()).isValueType();
        }
        return false;
    }

    static boolean isRefinableMemberClass(Declaration model) {
        return model instanceof Class 
                && model.isMember()
                && (model.isFormal() || model.isDefault())
                && !model.isAnonymous()
                && isCeylon((Class)model);
    }
    
    public static String className(Declaration decl) {
        return decl.getQualifiedNameString().replace("::", ".");
    }

    public static Tree.Term unwrapExpressionsUntilTerm(Tree.Term term) {
        while (term instanceof Tree.Expression) {
            term = ((Tree.Expression)term).getTerm();
        }
        return term;
    }
    
    /**
     * Determines whether the given attribute should be accessed and assigned 
     * via a {@code VariableBox}
     */
    public static boolean isBoxedVariable(Tree.AttributeDeclaration attr) {
        return isBoxedVariable(attr.getDeclarationModel());
    }
    
    /**
     * Determines whether the given attribute should be accessed and assigned 
     * via a {@code VariableBox}
     */
    public static boolean isBoxedVariable(TypedDeclaration attr) {
        return Decl.isValue(attr)
                && isLocal(attr)
                && attr.isVariable()
                && attr.isCaptured();
    }
}
