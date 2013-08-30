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

import java.util.List;

import com.redhat.ceylon.compiler.typechecker.model.Class;
import com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Element;
import com.redhat.ceylon.compiler.typechecker.model.Functional;
import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.MethodOrValue;
import com.redhat.ceylon.compiler.typechecker.model.Parameter;
import com.redhat.ceylon.compiler.typechecker.model.ParameterList;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.Value;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.MethodDeclaration;

/**
 * Utility functions telling you about code generation strategies
 * @see Decl
 */
class Strategy {
    private Strategy() {}
    
    public static boolean defaultParameterMethodTakesThis(Tree.Declaration decl) {
        return defaultParameterMethodTakesThis(decl.getDeclarationModel());
    }
    
    public static boolean defaultParameterMethodTakesThis(Declaration decl) {
        return decl instanceof Method 
                && decl.isToplevel();
    }
    
    enum DefaultParameterMethodOwner {
        /** 
         * DPM should be a member of an init companion (used for local class 
         * initializers with defaulted parameters) 
         */
        INIT_COMPANION,
        /** 
         * DPM should be static in the top level class
         */
        STATIC,
        /** 
         * DPM should be a member of the outer class 
         */
        OUTER,
        /** 
         * DPM should be a member of the outer interface's companion 
         */
        OUTER_COMPANION,
        /** 
         * DPM should be a member of the current class/interface 
         */
        SELF
    }
    
    public static DefaultParameterMethodOwner defaultParameterMethodOwner(Declaration decl) {
        if (decl instanceof Method 
                && !Decl.withinInterface(decl)
                && !((Method)decl).isParameter()) {
            return DefaultParameterMethodOwner.SELF;
        }
        if (decl instanceof MethodOrValue
                && ((MethodOrValue)decl).isParameter()) {
            decl = (Declaration) ((MethodOrValue)decl).getContainer();
        }
        
        if ((decl instanceof Method || decl instanceof Class) 
                && decl.isToplevel()) {
            // Only top-level methods have static default value methods
            return DefaultParameterMethodOwner.STATIC;
        } else if ((decl instanceof Class) 
                && !decl.isToplevel()
                && !Decl.isLocal(decl)) {
            // Only inner classes have their default value methods on their outer
            return Decl.getClassOrInterfaceContainer(decl, false) instanceof Class ? DefaultParameterMethodOwner.OUTER : DefaultParameterMethodOwner.OUTER_COMPANION;
        }
        
        return DefaultParameterMethodOwner.INIT_COMPANION;
    }
    
    public static boolean defaultParameterMethodStatic(Tree.Declaration decl) {
        // Only top-level methods and top-level class initializers 
        // have static default value methods
        return defaultParameterMethodStatic(decl.getDeclarationModel());
    }
    
    public static boolean defaultParameterMethodStatic(Element decl) {
        if (decl instanceof MethodOrValue
                && ((MethodOrValue)decl).isParameter()) {
            decl = (Element) ((MethodOrValue)decl).getContainer();
        }
        // Only top-level methods have static default value methods
        return ((decl instanceof Method && !((Method)decl).isParameter())
                || decl instanceof Class) 
                && ((Declaration)decl).isToplevel();
    }
    
    public static boolean defaultParameterMethodOnOuter(Tree.Declaration decl) {
        // Only top-level methods and top-level class initializers 
        // have static default value methods
        return defaultParameterMethodOnOuter(decl.getDeclarationModel());
    }
    
    public static boolean defaultParameterMethodOnOuter(Element elem) {
        if (elem instanceof MethodOrValue
                && ((MethodOrValue)elem).isParameter()) {
            elem = (Element) ((MethodOrValue)elem).getContainer();
        }
        // Only inner classes have their default value methods on their outer
        return (elem instanceof Class) 
                && !((Class)elem).isToplevel()
                && !Decl.isLocal((Class)elem);
    }
    
    public static boolean defaultParameterMethodOnSelf(Tree.Declaration decl) {
        return defaultParameterMethodOnSelf(decl.getDeclarationModel());
    }
    
    public static boolean defaultParameterMethodOnSelf(Declaration decl) {
        return decl instanceof Method
                && !((Method)decl).isParameter()
                && !Decl.withinInterface(decl);
    }
    
    /**
     * Determines whether the given Class def should have a {@code main()} method generated.
     * I.e. it's a concrete top level Class without initializer parameters
     * @param def
     */
    public static boolean generateMain(Tree.ClassOrInterface def) {
        return def instanceof Tree.AnyClass 
                && Decl.isToplevel(def) 
                && !Decl.isAbstract(def)
                && hasNoRequiredParameters((Class)def.getDeclarationModel());
    }
    
    /**
     * Returns true if the given functional takes no parameters or accepts only defaulted params
     */
    private static boolean hasNoRequiredParameters(Functional model) {
        List<ParameterList> parameterLists = model.getParameterLists();
        if(parameterLists == null || parameterLists.size() != 1)
            return false;
        ParameterList parameterList = parameterLists.get(0);
        if(parameterList == null)
            return false;
        List<Parameter> parameters = parameterList.getParameters();
        if(parameters == null)
            return false;
        if(parameters.isEmpty())
            return true;
        // if the first one is optional, all others have to be too
        return parameters.get(0).isDefaulted();
    }

    /**
     * Determines whether the given Method def should have a {@code main()} method generated.
     * I.e. it's a top level method without parameters
     * @param def
     */
    public static boolean generateMain(Tree.AnyMethod def) {
        return  Decl.isToplevel(def) 
                && hasNoRequiredParameters(def.getDeclarationModel());
    }
    
    public static boolean generateThisDelegates(Tree.AnyMethod def) {
        return Decl.withinInterface(def.getDeclarationModel())
            && def instanceof MethodDeclaration
            && ((MethodDeclaration) def).getSpecifierExpression() == null;
    }

    public static boolean needsOuterMethodInCompanion(ClassOrInterface model) {
        return Decl.withinClassOrInterface(model);
    }
    
    public static boolean useField(Value attr) {
        return !Decl.withinInterface(attr) && Decl.isCaptured(attr);
    }
    
    
    public static boolean createField(Parameter p, Value v) {
        return !Decl.withinInterface(v) 
                && (p == null 
                        || (useField(v)));
    }
    
    /**
     * Determines whether a method wrapping the Callable should be generated 
     * for a FunctionalParameter 
     */
    static boolean createMethod(Tree.Parameter parameter) {
        return createMethod(parameter.getParameterModel());
    }
    
    /**
     * Determines whether a method wrapping the Callable should be generated 
     * for a FunctionalParameter 
     */
    static boolean createMethod(Parameter parameter) {
        MethodOrValue model = parameter.getModel();
        return createMethod(model);
    }

    static boolean createMethod(MethodOrValue model) {
        return model instanceof Method
                && model.isParameter()
                && model.isClassMember()
                && (model.isShared() || model.isCaptured());
    }
    
    /**
     * Non-{@code shared} concrete interface members are 
     * only defined/declared on the companion class, not on the transformed 
     * interface itself.
     */
    public static boolean onlyOnCompanion(Declaration model) {
        return Decl.withinInterface(model)
                && (model instanceof ClassOrInterface
                        || !Decl.isShared(model));
    }
    
    static boolean generateInstantiator(Declaration model) {
        return (Decl.isRefinableMemberClass(model) 
                    && !((Class)model).isAbstract()) 
                || 
                // If shared, generate an instantiator so that BC is 
                // preserved should the member class later become refinable
                (model instanceof Class 
                    && model.isMember()
                    && model.isShared()
                    && !model.isAnonymous()
                    && !((Class)model).isAbstract()
                    && Decl.isCeylon((Class)model));
    }
    
    /**
     * Does the given declaration have an instantiator that is declared to
     * return Object (so needs a typecast when invoked).
     */
    static boolean isInstantiatorUntyped(Declaration model) {
        return generateInstantiator(model) && Decl.isAncestorLocal(model);
    }
    
    /** 
     * Determines whether a {@code void} Ceylon method should be declared to 
     * return {@code void} or {@code java.lang.Object} (the erasure of 
     * {@code ceylon.language.Anything}) in Java. 
     * If the method can be refined, 
     * (but was not itself refined from a Java {@code void} method), or is 
     * {@code actual} then {@code java.lang.Object} should be used.
     */
    static boolean useBoxedVoid(Method m) {
        return m.isMember() &&
            (m.isDefault() || m.isFormal() || m.isActual()) &&
            m.getUnit().getAnythingDeclaration().equals(m.getType().getDeclaration()) &&
            Decl.isCeylon((TypeDeclaration)m.getRefinedDeclaration().getContainer());
    }
    
}
