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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.MethodDeclaration;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.PositionalArgument;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.SequencedArgument;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Term;
import com.redhat.ceylon.model.loader.JvmBackendUtil;
import com.redhat.ceylon.model.loader.model.LazyClass;
import com.redhat.ceylon.model.typechecker.model.Annotation;
import com.redhat.ceylon.model.typechecker.model.Class;
import com.redhat.ceylon.model.typechecker.model.ClassAlias;
import com.redhat.ceylon.model.typechecker.model.ClassOrInterface;
import com.redhat.ceylon.model.typechecker.model.Constructor;
import com.redhat.ceylon.model.typechecker.model.Declaration;
import com.redhat.ceylon.model.typechecker.model.Function;
import com.redhat.ceylon.model.typechecker.model.Functional;
import com.redhat.ceylon.model.typechecker.model.Interface;
import com.redhat.ceylon.model.typechecker.model.ModelUtil;
import com.redhat.ceylon.model.typechecker.model.Parameter;
import com.redhat.ceylon.model.typechecker.model.ParameterList;
import com.redhat.ceylon.model.typechecker.model.Scope;
import com.redhat.ceylon.model.typechecker.model.Type;
import com.redhat.ceylon.model.typechecker.model.TypeAlias;
import com.redhat.ceylon.model.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.model.typechecker.model.TypeParameter;
import com.redhat.ceylon.model.typechecker.model.Unit;
import com.redhat.ceylon.model.typechecker.model.Value;

/**
 * Utility functions telling you about code generation strategies
 * @see Decl
 */
class Strategy {
    private Strategy() {}
    
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
    
    public static DefaultParameterMethodOwner defaultParameterMethodOwner(final Declaration odecl) {
        Declaration decl = odecl;
        if (defaultParameterMethodOnSelf(decl)) {
            return DefaultParameterMethodOwner.SELF;
        }
        if (decl.isParameter() && decl.getContainer() instanceof Declaration) {
            decl = (Declaration) decl.getContainer();
        } 
        if (Decl.isConstructor(decl)) {
            decl = ModelUtil.getConstructedClass(decl);
        }
        
        if ((decl instanceof Function || decl instanceof Class) 
                && (decl.isToplevel() || decl.isStatic())) {
            // Only top-level methods have static default value methods
            return DefaultParameterMethodOwner.STATIC;
        } else if (isInnerClass(decl)) {
            // Only inner classes have their default value methods on their outer
            return ModelUtil.getClassOrInterfaceContainer(decl, false) instanceof Class ? 
                    DefaultParameterMethodOwner.OUTER : 
                    DefaultParameterMethodOwner.OUTER_COMPANION;
        }
        
        return DefaultParameterMethodOwner.INIT_COMPANION;
    }
    
    
    public static boolean defaultParameterMethodTakesThis(Declaration decl) {
        return decl instanceof Function && decl.isToplevel();
    }
    
    public static boolean defaultParameterMethodStatic(final Declaration odecl) {
        return defaultParameterMethodOwner(odecl) == DefaultParameterMethodOwner.STATIC;
    }
    
    public static boolean defaultParameterMethodOnOuter(final Declaration odecl) {
        return defaultParameterMethodOwner(odecl) == DefaultParameterMethodOwner.OUTER;
    }
    

    private static boolean isInnerClass(Declaration elem) {
        if (elem instanceof Constructor) {
            elem = (Declaration)elem.getContainer();
        }
        return elem instanceof Class
            && !elem.isToplevel()
            && !ModelUtil.isLocalNotInitializer(elem);
    }
    
    public static boolean defaultParameterMethodOnSelf(Declaration decl) {
        return decl instanceof Function
            && !Decl.isConstructor(decl)
            && !decl.isParameter()
            && !decl.isInterfaceMember()
            && !decl.isToplevel()
            && !decl.isStatic();
    }
    
    public static boolean hasDefaultParameterValueMethod(Parameter param) {
        return param.isDefaulted();
    }
    
    public static boolean hasDefaultParameterOverload(Parameter param) {
        return param.isDefaulted() 
            || isCeylonVariadicNeedingEmpty(param);
    }
    
    private static boolean isCeylonVariadicNeedingEmpty(Parameter param){
        if(!param.isSequenced() || param.isAtLeastOne())
            return false;
        // make sure it's not a Java variadic
        if(Decl.isJavaVariadicIncludingInheritance(param))
            return false;
        return true;
    }
    
    public static boolean hasEmptyDefaultArgument(Parameter param) {
        return param.isSequenced() 
            && !param.isAtLeastOne();
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
        ClassOrInterface model = def.getDeclarationModel();
        return def instanceof Tree.AnyClass 
            && model.isToplevel() 
            && model.isShared()
            && !model.isAbstract()
            && !model.isNativeHeader()
            && hasNoRequiredParameters((Class)model);
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
        Function model = def.getDeclarationModel();
        return model.isToplevel()
            && model.isShared()
            && hasNoRequiredParameters(model);
    }
    
    public static boolean generateThisDelegates(Tree.AnyMethod def) {
        return def.getDeclarationModel().isInterfaceMember()
            && def instanceof MethodDeclaration
            && ((MethodDeclaration) def).getSpecifierExpression() == null;
    }

    public static boolean needsOuterMethodInCompanion(ClassOrInterface model) {
        return model.isClassOrInterfaceMember();
    }
    
    public static boolean useField(Value attr) {
        return !attr.isInterfaceMember() && ModelUtil.isCaptured(attr) 
            || attr.isStatic();
    }
    
    
    public static boolean createField(Parameter p, Value v) {
        return !v.isInterfaceMember() 
            && (p == null || useField(v));
    }
    
    /**
     * Determines whether a method wrapping the Callable should be generated 
     * for a FunctionalParameter 
     */
    static boolean createMethod(Parameter parameter) {
        return JvmBackendUtil.createMethod(parameter.getModel());
    }

    /**
     * Non-{@code shared} concrete interface members are 
     * only defined/declared on the companion class, not on the transformed 
     * interface itself.
     */
    public static boolean onlyOnCompanion(Declaration model) {
        return model.isInterfaceMember()
            && (model instanceof ClassOrInterface || !model.isShared());
    }
    
    static boolean generateInstantiator(Declaration model) {
        if (model instanceof Class) {
            Class cls = (Class)model;
            return !cls.isAbstract()
                && !cls.isStatic()
                && (Decl.isRefinableMemberClass(cls) 
                    // If shared, generate an instantiator so that BC is 
                    // preserved should the member class later become refinable
                    || ModelUtil.isCeylonDeclaration(cls)
                            && model.isMember()
                            && cls.isShared()
                            && !cls.isAnonymous());
        } else if (Decl.isConstructor(model)) {
            Constructor ctor = ModelUtil.getConstructor(model);
            Class cls = ModelUtil.getConstructedClass(ctor);
            return cls.isMember()
                && cls.isShared()
                && !cls.isStatic()
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
        return m.isMember() 
            && (m.isDefault() || m.isFormal() || m.isActual()) 
            && m.getType().isAnything() 
            && ModelUtil.isCeylonDeclaration((TypeDeclaration)m.getRefinedDeclaration().getContainer());
    }

    public static boolean hasDelegatedDpm(Class cls) {
        return Decl.isRefinableMemberClass(cls.getRefinedDeclaration()) 
            && Strategy.defaultParameterMethodOnOuter(cls) 
            && cls.getRefinedDeclaration().isInterfaceMember();
    }

    /**
     * Determines whether we should inline {@code x^n} as a repeated multiplication
     * ({@code x*x*...*x}) instead of calling {@code x.power(n)}.
     */
    public static boolean inlinePowerAsMultiplication(Tree.PowerOp op) {
        java.lang.Number power_;
        try {
            power_ = ExpressionTransformer.getIntegerLiteralPower(op);
            if (power_ != null) {
                long power = power_.longValue();
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
            for (Annotation annotation : cls.getAnnotations()) {
                Declaration annoDecl = cls.getUnit().getImportedDeclaration(annotation.getName(), null, false);
                if (annoDecl != null && annoDecl.getQualifiedNameString().equals("java.lang::nonbean")) {
                    return false;
                }
            }

            boolean hasDelegatableSuper = false;
            Class superClass = (Class)cls.getExtendedType().getDeclaration();
            if (superClass instanceof LazyClass
                    && !((LazyClass)superClass).isCeylon()) {
                if (superClass.isAbstraction()) {
                    for (Declaration s : superClass.getOverloads()) {
                        if (s instanceof Class && isNullary((Class)s)) {
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
                hasDelegatableSuper = 
                        hasNullaryNonJpaConstructor(superClass)
                        || hasJpaConstructor(superClass);
            }
            
            boolean constrained = 
                    cls.getCaseValues() != null && !cls.getCaseValues().isEmpty()
                    || cls.hasEnumerated() && Decl.hasOnlyValueConstructors(cls);
            
            return hasDelegatableSuper && !constrained;
        } else {
            return false;
        }
    }
    
    private static boolean isNullary(Class superClass) {
        ParameterList parameterList = superClass.getParameterList();
        return parameterList != null 
            && parameterList.getParameters() != null 
            && parameterList.getParameters().isEmpty();
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
                Constructor defaultConstructor = c.getDefaultConstructor();
                if (defaultConstructor != null
                        && defaultConstructor.getParameterList() != null) {
                    parameters = defaultConstructor.getParameterList().getParameters();
                }
            } else if (c.getParameterList() != null ) {
                parameters = c.getParameterList().getParameters();
            }
            return parameters != null 
                && (parameters.isEmpty()
                    || parameters.get(0).isDefaulted()
                    || parameters.get(0).isSequenced() && !parameters.get(0).isAtLeastOne());
        }
        return false;
    }

    public static boolean introduceJavaIoSerializable(
            Class cls, Interface ser) {
        return !(cls instanceof ClassAlias)
            && (Decl.hasOnlyValueConstructors(cls) 
                || cls.isAnonymous()
                || (cls.getExtendedType() != null
                    && (cls.getExtendedType().isBasic()
                     || cls.getExtendedType().isObject())))
            && !cls.getSatisfiedTypes().contains(ser.getType());
    }
    
    /** Should the given class have a readResolve() method added ?
     * @param ser */
    public static boolean addReadResolve(Class cls) {
        return cls.isAnonymous() && cls.isToplevel();
    }

    public static boolean useSerializationProxy(Class model) {
        return model.hasEnumerated()
            && (model.isToplevel() || model.isMember());
    }
    
    public static List<TypeParameter> getEffectiveTypeParameters(Declaration decl) {
        return getEffectiveTypeParameters(decl, decl);
    }
    
    /**
     * Like {@link ModelUtil#isCeylonDeclaration(Declaration)}, but checks 
     * toplevel decl
     */
    private static boolean isCeylon(Declaration d) {
        while (!d.isToplevel()) {
            d = (Declaration)Decl.getFirstDeclarationContainer((Scope)d);
        }
        return ModelUtil.isCeylonDeclaration(d);
    }
    
    private static List<TypeParameter> getEffectiveTypeParameters(Declaration original, Declaration decl) {
        if (Decl.isConstructor(original)) {
            original = ModelUtil.getConstructedClass(original);
        }
        if (Decl.isConstructor(decl)) {
            decl = ModelUtil.getConstructedClass(decl);
        }
        Scope container = decl.getContainer();
        if (decl instanceof Value) {
            if (decl.isStatic()) {
                return getEffectiveTypeParameters(original, (Declaration)container);
            } else {
                return Collections.emptyList();
            }
        }
        if (decl instanceof Function) {
            if (original instanceof ClassAlias
                    || decl.isStatic() && isCeylon(decl)) {
                ArrayList<TypeParameter> copyDown = new ArrayList<TypeParameter>(getEffectiveTypeParameters(original, (Declaration)container));
                copyDown.addAll(decl.getTypeParameters());
                return copyDown;
            } else {
                return decl.getTypeParameters();
            }
        } else if (decl instanceof ClassAlias) {
            // TODO
            /*if (container instanceof Declaration) {
                ArrayList<TypeParameter> copyDown = new ArrayList<TypeParameter>(getEffectiveTypeParameters(original, (Declaration)container));
                copyDown.addAll(((Class)decl).getTypeParameters());
                return copyDown;
            } else*/ {
                return ((ClassAlias)decl).getTypeParameters();
            }
        } else if (decl instanceof Class) {
            if (((Class) decl).isStatic()
                    && ((Class)decl).isMember()
                    && isCeylon(decl)) {// TODO and isCeylon
                ArrayList<TypeParameter> copyDown = new ArrayList<TypeParameter>(getEffectiveTypeParameters(original, (Declaration)container));
                copyDown.addAll(((Class)decl).getTypeParameters());
                return copyDown;
            } else {
                return ((Class)decl).getTypeParameters();
            }
        } else if (decl instanceof Interface) {
            /*if (((Interface) decl).isMember()) {
                ArrayList<TypeParameter> copyDown = new ArrayList<TypeParameter>(getEffectiveTypeParameters(original, (Declaration)container));
                copyDown.addAll(((Interface)decl).getTypeParameters());
                return copyDown;
            } else*/ {
                return ((Interface)decl).getTypeParameters();
            }
        } else if (decl instanceof TypeAlias) {
            return ((TypeAlias)decl).getTypeParameters();
        } else {
            throw BugException.unhandledDeclarationCase((Declaration)decl);
        }
    }
    
    /**
     * Return true if all the ListedArguments in the given list are compile time constants
     * (or at least literals): In this case we will use a ConstantIterable instead 
     * of an anonymous subclass of LazyIterable.
     */
    public static boolean useConstantIterable(SequencedArgument sequencedArgument) {
        Unit unit = sequencedArgument.getUnit();
        boolean allCtc = true;
        for (Tree.PositionalArgument pa : sequencedArgument.getPositionalArguments()) {
            if (pa instanceof Tree.ListedArgument) {
                Term term = ((Tree.ListedArgument)pa).getExpression().getTerm();
                if (term instanceof Tree.StringLiteral
                        || term instanceof Tree.NaturalLiteral
                        || term instanceof Tree.FloatLiteral
                        || term instanceof Tree.CharLiteral
                        || (term instanceof Tree.NegativeOp
                            && (((Tree.NegativeOp)term).getTerm() instanceof Tree.NaturalLiteral
                             || ((Tree.NegativeOp)term).getTerm() instanceof Tree.FloatLiteral))
                        ||(term instanceof Tree.BaseMemberExpression
                            && (((Tree.BaseMemberExpression)term).getDeclaration().equals(unit.getTrueValueDeclaration())
                             || ((Tree.BaseMemberExpression)term).getDeclaration().equals(unit.getFalseValueDeclaration())))) {
                } else {
                    allCtc = false;
                    break;
                }
            }
        }
        return allCtc;
    }
    
}
