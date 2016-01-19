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

import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.MethodDeclaration;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.PositionalArgument;
import com.redhat.ceylon.model.loader.JvmBackendUtil;
import com.redhat.ceylon.model.loader.model.LazyClass;
import com.redhat.ceylon.model.typechecker.model.Class;
import com.redhat.ceylon.model.typechecker.model.ClassAlias;
import com.redhat.ceylon.model.typechecker.model.ClassOrInterface;
import com.redhat.ceylon.model.typechecker.model.Constructor;
import com.redhat.ceylon.model.typechecker.model.Declaration;
import com.redhat.ceylon.model.typechecker.model.Element;
import com.redhat.ceylon.model.typechecker.model.Functional;
import com.redhat.ceylon.model.typechecker.model.Function;
import com.redhat.ceylon.model.typechecker.model.FunctionOrValue;
import com.redhat.ceylon.model.typechecker.model.Interface;
import com.redhat.ceylon.model.typechecker.model.ModelUtil;
import com.redhat.ceylon.model.typechecker.model.Parameter;
import com.redhat.ceylon.model.typechecker.model.ParameterList;
import com.redhat.ceylon.model.typechecker.model.Type;
import com.redhat.ceylon.model.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.model.typechecker.model.Unit;
import com.redhat.ceylon.model.typechecker.model.Value;

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
        return decl instanceof Function 
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
        if (decl instanceof Function 
                && !Decl.withinInterface(decl)
                && !((Function)decl).isParameter()) {
            return DefaultParameterMethodOwner.SELF;
        }
        if (decl instanceof FunctionOrValue
                && ((FunctionOrValue)decl).isParameter()) {
            decl = (Declaration) decl.getContainer();
        } 
        if (Decl.isConstructor(decl)) {
            decl = Decl.getConstructedClass(decl);
        }
        
        if ((decl instanceof Function || decl instanceof Class) 
                && decl.isToplevel()) {
            // Only top-level methods have static default value methods
            return DefaultParameterMethodOwner.STATIC;
        } else if ((decl instanceof Class) 
                && !decl.isToplevel()
                && !Decl.isLocalNotInitializer(decl)) {
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
        if (decl instanceof FunctionOrValue
                && ((FunctionOrValue)decl).isParameter()) {
            decl = (Element) decl.getContainer();
        }
        if (decl instanceof Declaration && Decl.isConstructor((Declaration)decl)) {
            decl = (Class)Decl.getConstructedClass((Declaration)decl);
        }
        // Only top-level methods have static default value methods
        return ((decl instanceof Function && !((Function)decl).isParameter())
                || decl instanceof Class) 
                && ((Declaration)decl).isToplevel();
    }
    
    public static boolean defaultParameterMethodOnOuter(Tree.Declaration decl) {
        // Only top-level methods and top-level class initializers 
        // have static default value methods
        return defaultParameterMethodOnOuter(decl.getDeclarationModel());
    }
    
    public static boolean defaultParameterMethodOnOuter(Element elem) {
        if (elem instanceof Declaration 
                && Decl.isConstructor((Declaration)elem)) {
            elem = Decl.getConstructedClass((Declaration)elem);
        }
        if (elem instanceof FunctionOrValue
                && ((FunctionOrValue)elem).isParameter()) {
            elem = (Element) ((FunctionOrValue)elem).getContainer();
        }
        
        // Only inner classes have their default value methods on their outer
        return (elem instanceof Class) 
                && !((Class)elem).isToplevel()
                && !Decl.isLocalNotInitializer((Class)elem);
    }
    
    public static boolean defaultParameterMethodOnSelf(Tree.Declaration decl) {
        return defaultParameterMethodOnSelf(decl.getDeclarationModel());
    }
    
    public static boolean defaultParameterMethodOnSelf(Declaration decl) {
        return decl instanceof Function
                && !((Function)decl).isParameter()
                && !Decl.withinInterface(decl);
    }
    
    public static boolean hasDefaultParameterValueMethod(Parameter param) {
        return param.isDefaulted();
    }
    
    public static boolean hasDefaultParameterOverload(Parameter param) {
        return param.isDefaulted() || 
                (param.isSequenced() && !param.isAtLeastOne());
    }
    
    public static boolean hasEmptyDefaultArgument(Parameter param) {
        return (param.isSequenced() && !param.isAtLeastOne());
    }
    
    /**
     * Determines whether the given Class def should have a {@code main()} method generated.
     * I.e. it's a shared concrete top level Class without initializer parameters
     * @param def
     */
    public static boolean generateMain(Tree.ClassOrInterface def) {
        for (Tree.CompilerAnnotation c: def.getCompilerAnnotations()) {
            if (c.getIdentifier().getText().equals("nomain")) {
                return false;
            }
        }
        return def instanceof Tree.AnyClass 
                && Decl.isToplevel(def) 
                && Decl.isShared(def)
                && !Decl.isAbstract(def)
                && !def.getDeclarationModel().isNativeHeader()
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
     * Determines whether the given Function def should have a {@code main()} method generated.
     * I.e. it's a shared top level method without parameters
     * @param def
     */
    public static boolean generateMain(Tree.AnyMethod def) {
        return  Decl.isToplevel(def)
                && Decl.isShared(def)
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
        FunctionOrValue model = parameter.getModel();
        return JvmBackendUtil.createMethod(model);
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
        if (model instanceof Class) {
            Class cls = (Class)model;
            return !cls.isAbstract()
                    && (Decl.isRefinableMemberClass(cls) 
                        || 
                        // If shared, generate an instantiator so that BC is 
                        // preserved should the member class later become refinable
                        (Decl.isCeylon(cls)
                                && model.isMember()
                                && cls.isShared()
                                && !cls.isAnonymous()));
        } else if (Decl.isConstructor(model)) {
            Constructor ctor = Decl.getConstructor(model);
            Class cls = Decl.getConstructedClass(ctor);
            return cls.isMember()
                    && cls.isShared()
                    && ctor.isShared();
        } else {
            return false;
        }
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
    static boolean useBoxedVoid(Function m) {
        return m.isMember() &&
            (m.isDefault() || m.isFormal() || m.isActual()) &&
            m.getType().isAnything() &&
            Decl.isCeylon((TypeDeclaration)m.getRefinedDeclaration().getContainer());
    }

    public static boolean hasDelegatedDpm(Class cls) {
        return Decl.isRefinableMemberClass(cls.getRefinedDeclaration()) &&
            Strategy.defaultParameterMethodOnOuter(cls) &&
            Decl.withinInterface(cls.getRefinedDeclaration());
    }

    /**
     * Determines whether we should inline {@code x^n} as a repeated multiplication
     * ({@code x*x*...*x}) instead of calling {@code x.power(n)}.
     */
    public static boolean inlinePowerAsMultiplication(Tree.PowerOp op) {
        java.lang.Long power;
        try {
            power = ExpressionTransformer.getIntegerLiteralPower(op);
            if (power != null) {
                Unit unit = op.getUnit();
                Type baseType = op.getLeftTerm().getTypeModel();
                // Although the optimisation still works for powers > 64, it ends 
                // up bloating the code (e.g. imagine x^1_000_000_000)
                if (power > 0
                        && power <= 64
                        && baseType.isExactly(unit.getIntegerType())) {
                    return true;
                }
                else if (power > 0
                        && power <= 64
                        && baseType.isExactly(unit.getFloatType())) {
                    return true;
                }
            }
        } catch (ErroneousException e) {
            return false;
        }
        return false;
    }
    /**
     * Whether to use a 
     * {@code LazySwitchingIterable} instead of a {@code LazyInvokingIterable}.
     * The only real consideration here is whether switching would result in 
     * method code that exceeded the limits of the class file format.
     */
    public static boolean preferLazySwitchingIterable(
            java.util.List<PositionalArgument> positionalArguments) {
        return positionalArguments.size() < 128;
    }

    public static boolean generateJpaCtor(
            Tree.ClassOrInterface def) {
        return generateJpaCtor(def.getDeclarationModel());
    }

    static boolean generateJpaCtor(ClassOrInterface declarationModel) {
        if (declarationModel instanceof Class
                && !(declarationModel instanceof ClassAlias)
                && declarationModel.isToplevel()) {
            Class cls = (Class)declarationModel;
            if (cls.getCaseValues() != null && !cls.getCaseValues().isEmpty()) {
                return false;
            }
            if (hasNullaryNonJpaConstructor(cls)) {
                // The class will already have a nullary ctor
                return false;
            }
            
            boolean hasDelegatableSuper = false;
            Class superClass = (Class)cls.getExtendedType().getDeclaration();
            if (superClass instanceof LazyClass
                    && !((LazyClass)superClass).isCeylon()) {
                if (superClass.isAbstraction()) {
                    for (Declaration s : superClass.getOverloads()) {
                        if (s instanceof Class
                                && isNullary((Class)s)) {
                            hasDelegatableSuper = true;
                            break;
                        }
                    }
                } else {
                    // If the superclass is Java then generate a Jpa constructor
                    // if there's a nullary superclass constructor we can call
                    hasDelegatableSuper = isNullary(superClass);
                }
            } else {
                hasDelegatableSuper = hasNullaryNonJpaConstructor(superClass)
                        || hasJpaConstructor(superClass);
            }
            
            boolean constrained = 
                    (cls.getCaseValues() != null 
                    && !cls.getCaseValues().isEmpty())
                    || cls.hasEnumerated() && Decl.hasOnlyValueConstructors(cls);
            
            return hasDelegatableSuper
                    && !constrained;
        } else {
            return false;
        }
    }
    
    private static boolean isNullary(Class superClass) {
        return superClass.getParameterList() != null 
                && superClass.getParameterList().getParameters() != null 
                && superClass.getParameterList().getParameters().isEmpty();
    }
    
    /**
     * Whether the given class has a "JPA constructor". 
     * A JPA constructor is a hidden-from-Ceylon constructor
     * which is used by frameworks like JPA. To be useful to frameworks
     * the JPA constructor must be nullary. A JPA constructor
     * will not be nullary if the class is generic: This makes it non-useful 
     * to JPA, but allows subclasses to delegate to it and have their own
     * nullary JPA constructor.  
     * @param c The class
     * @return
     */
    public static boolean hasJpaConstructor(Class c) {
        if (c instanceof LazyClass
                && !(c instanceof ClassAlias)) {
            // If it's binary it has a JpaConstructor if it says so
            return ((LazyClass)c).hasJpaConstructor();
        } else {
            return generateJpaCtor(c);
        }
    }
    
    /**
     * Will this class have a nullary Java constructor, excluding JPA constructors
     * @param c
     * @return
     */
    protected static boolean hasNullaryNonJpaConstructor(Class c) {
        if (c.isToplevel()
                && !(c instanceof ClassAlias)) {
            List<Parameter> parameters = null;
            if (c.hasConstructors()) {
                Constructor defaultConstructor = Decl.getDefaultConstructor(c);
                if (defaultConstructor != null
                        && defaultConstructor.getParameterList() != null) {
                    parameters = defaultConstructor.getParameterList().getParameters();
                }
            } else if (c.getParameterList() != null ){
                parameters = c.getParameterList().getParameters();
            }
            return parameters != null 
                    && (parameters.isEmpty()
                    || parameters.get(0).isDefaulted()
                    || (parameters.get(0).isSequenced() && !parameters.get(0).isAtLeastOne()));
        }
        return false;
    }

    public static boolean introduceJavaIoSerializable(
            Class cls, Interface ser) {
        if (!(cls instanceof ClassAlias)) {
            if ((Decl.hasOnlyValueConstructors(cls) 
                    || cls.isAnonymous()
                    || (cls.getExtendedType() != null
                    && (cls.getExtendedType().isBasic()
                    || cls.getExtendedType().isObject())))
                    && !cls.getSatisfiedTypes().contains(ser.getType())) {
                return true;
            }
        }
        return false;
    }
    
    /** Should the given class have a readResolve() method added ?
     * @param ser */
    public static boolean addReadResolve(Class cls) {
        if (cls.isAnonymous()
                && cls.isToplevel()) {
            return true;
        }
        return false;
    }

    public static boolean useSerializationProxy(Class model) {
        return model.hasEnumerated()
                && (model.isToplevel() || model.isMember());
    }
    
}
