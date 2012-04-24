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

import static com.sun.tools.javac.code.Flags.ABSTRACT;
import static com.sun.tools.javac.code.Flags.FINAL;
import static com.sun.tools.javac.code.Flags.INTERFACE;
import static com.sun.tools.javac.code.Flags.PRIVATE;
import static com.sun.tools.javac.code.Flags.PROTECTED;
import static com.sun.tools.javac.code.Flags.PUBLIC;
import static com.sun.tools.javac.code.Flags.STATIC;

import java.util.Map;

import com.redhat.ceylon.compiler.java.util.Decl;
import com.redhat.ceylon.compiler.java.util.Strategy;
import com.redhat.ceylon.compiler.java.util.Util;
import com.redhat.ceylon.compiler.typechecker.model.Class;
import com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Getter;
import com.redhat.ceylon.compiler.typechecker.model.Interface;
import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.Parameter;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.Scope;
import com.redhat.ceylon.compiler.typechecker.model.Setter;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.TypeParameter;
import com.redhat.ceylon.compiler.typechecker.model.TypedDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.Value;
import com.redhat.ceylon.compiler.typechecker.tree.CustomTree.MethodDeclaration;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.AttributeDeclaration;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.AttributeGetterDefinition;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.AttributeSetterDefinition;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.DefaultArgument;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.MethodDefinition;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ParameterList;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.TypeParameterList;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.VoidModifier;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCBlock;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCIdent;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCMethodInvocation;
import com.sun.tools.javac.tree.JCTree.JCNewClass;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import com.sun.tools.javac.tree.JCTree.JCTypeApply;
import com.sun.tools.javac.tree.JCTree.JCTypeParameter;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;

/**
 * This transformer deals with class/interface declarations
 */
public class ClassTransformer extends AbstractTransformer {

    public static ClassTransformer getInstance(Context context) {
        ClassTransformer trans = context.get(ClassTransformer.class);
        if (trans == null) {
            trans = new ClassTransformer(context);
            context.put(ClassTransformer.class, trans);
        }
        return trans;
    }

    private ClassTransformer(Context context) {
        super(context);
    }


    
    // FIXME: figure out what insertOverloadedClassConstructors does and port it

    public List<JCTree> transform(final Tree.ClassOrInterface def) {
        final ClassOrInterface model = def.getDeclarationModel();
        if (model.isToplevel()) {
            resetLocals();
        } else if (Decl.isLocal(model)){
            local(model.getContainer());
        }
        final String className;
        if (def instanceof Tree.AnyInterface) {
            className = getFQDeclarationName(model).replaceFirst(".*\\.", "");
        } else {
            className = def.getIdentifier().getText();
        }
        ClassDefinitionBuilder classBuilder = ClassDefinitionBuilder
                .klass(this, Decl.isAncestorLocal(def), className);

        if (def instanceof Tree.AnyClass) {
            ParameterList paramList = ((Tree.AnyClass)def).getParameterList();
            for (Tree.Parameter param : paramList.getParameters()) {
                classBuilder.parameter(param);
                DefaultArgument defaultArgument = param.getDefaultArgument();
                if (defaultArgument != null
                        || param.getDeclarationModel().isSequenced()) {
                    classBuilder.getCompanionBuilder().defs(makeParamDefaultValueMethod(false, def, paramList, param));
                    // Add overloaded constructors for defaulted parameter
                    MethodDefinitionBuilder overloadBuilder = classBuilder.addConstructor();
                    makeOverloadsForDefaultedParameter(true,
                            overloadBuilder,
                            def, paramList, param);
                    
                }
            }
            
            if (model.getType().getQualifyingType() != null
                    && model.getType().getQualifyingType().getDeclaration() instanceof Interface) {
                // TODO 
            }
            
            if (def.getSatisfiedTypes() != null) {
                for (Tree.SimpleType satisfiedType : def.getSatisfiedTypes().getTypes()) {
                    TypeDeclaration decl = satisfiedType.getDeclarationModel();
                    if (!(decl instanceof Interface)) {
                        continue;
                    }
                    Interface iface = (Interface)decl;
                    
                    // For each satisfied interface, instantiate an instance of the 
                    // companion class in the constructor and assign it to a
                    // $Interface$impl field
                    transformInstantiateCompanions(classBuilder,
                            satisfiedType, iface);
                    
                    if (!decl.isToplevel()) {// TODO What about local interfaces?
                        // Generate $outer() impl if implementing an inner interface
                        classBuilder.defs(makeOuterImpl(model, satisfiedType, iface));
                    }
                }
                at(def);
            }
        }
        
        if (def instanceof Tree.AnyInterface) {
            //  Copy all the qualifying type's type parameters into the interface
            ProducedType type = model.getType().getQualifyingType();
            while (type != null) {
                java.util.List<TypeParameter> typeArguments = type.getDeclaration().getTypeParameters();
                if (typeArguments == null) {
                    continue;
                }
                for (TypeParameter typeArgument : typeArguments) {
                    classBuilder.typeParameter(typeArgument);
                }
                type = type.getQualifyingType();
            }
            
            // Build the companion class
            buildCompanion(def, model, classBuilder);   
        }
        
        // Transform the class/interface members
        CeylonVisitor visitor = new CeylonVisitor(gen(), classBuilder);
        def.visitChildren(visitor);

        // If it's a Class without initializer parameters...
        if (Strategy.generateMain(def)) {
            // ... then add a main() method
            classBuilder.body(makeMainForClass(model));
        }
        
        return classBuilder
            .modelAnnotations(model.getAnnotations())
            .modifiers(transformClassDeclFlags(def))
            .satisfies(model.getSatisfiedTypes())
            .caseTypes(model.getCaseTypes())
            .init((List<JCStatement>)visitor.getResult().toList())
            .build();
    }

    private void transformInstantiateCompanions(
            ClassDefinitionBuilder classBuilder, Tree.SimpleType satisfiedType,
            Interface iface) {
        at(satisfiedType);
        final ListBuffer<JCExpression> state = ListBuffer.<JCExpression>of(makeUnquotedIdent("this"));
        //if (!iface.isToplevel()) {
        //    state.append(makeQualIdent(makeJavaType(iface.getType().getQualifyingType()), "this"));
        //}
        final String fieldName = getCompanionFieldName(iface);
        classBuilder.init(make().Exec(make().Assign(
                makeSelect("this", fieldName),// TODO Use qualified name for quoting? 
                make().NewClass(null, 
                        null, // TODO Type args 
                        makeCompanionType(iface), 
                        state.toList(),  
                        null))));
        
        classBuilder.field(PRIVATE | FINAL, fieldName, 
                makeCompanionType(iface), null, false);
    }

    private JCMethodDecl makeOuterImpl(final ClassOrInterface model,
            Tree.SimpleType satisfiedType, Interface iface) {
        at(satisfiedType);
        
        MethodDefinitionBuilder outerBuilder = MethodDefinitionBuilder.method(gen(), true, true, "$outer");// TODO ancestorLocal
        outerBuilder.annotations(makeAtOverride());
        outerBuilder.annotations(makeAtIgnore());
        outerBuilder.modifiers(FINAL | PUBLIC);
        outerBuilder.resultType(null, makeJavaType(iface.getType().getQualifyingType()));
        Scope container = model.getContainer();
        final JCExpression expr;
        if (container instanceof Class) {
            expr = makeSelect(makeQuotedQualIdentFromString(getFQDeclarationName((Class)container)), "this");
        } else {
            expr = makeNull();//TODO makeQuotedIdent("$outer");// TODO Need to have a $outer field and pass in the outer instance to the ctor
        }
        outerBuilder.body(make().Return(expr));
        JCMethodDecl build = outerBuilder.build();
        return build;
    }

    private void buildCompanion(final Tree.ClassOrInterface def,
            final ClassOrInterface model, ClassDefinitionBuilder classBuilder) {
        at(def);
        // Give the $impl companion a $this field...
        ClassDefinitionBuilder companionBuilder = classBuilder.getCompanionBuilder();
        
        ProducedType thisType = model.getType();
        companionBuilder.field(PRIVATE | FINAL, 
                "$this", 
                makeJavaType(thisType), 
                null, false);
        MethodDefinitionBuilder ctor = companionBuilder.addConstructor();
        
        // ...initialize the $this field from a ctor parameter...
        ctor.parameter(0, "$this", makeJavaType(thisType), null);
        ListBuffer<JCStatement> bodyStatements = ListBuffer.<JCStatement>of(
                make().Exec(
                        make().Assign(
                                makeSelect("this", "$this"), 
                                makeUnquotedIdent("$this"))));
        ctor.body(bodyStatements.toList());
        if (Strategy.needsOuterMethodInCompanion(model)) {
            // interfaces inner to local types have a different type in the 
            // interface and on the $impl because the real outer type is not 
            // visible because the interfaces is hoisted to top level
            final ProducedType outerTypeInterface;
            final ProducedType outerTypeCompanion;
            if (Decl.isAncestorLocal(model)) {
                Scope container = Decl.container(model);
                while (!(container instanceof TypeDeclaration)) {
                    container = container.getContainer();
                }
                outerTypeCompanion = ((TypeDeclaration)container).getType();
                outerTypeInterface = typeFact().getObjectDeclaration().getType();
            } else {
                outerTypeInterface = thisType.getQualifyingType();
                outerTypeCompanion = outerTypeInterface;
            }
            // ...add a $outer() method to the impl
            
            MethodDefinitionBuilder outerBuilderCompanion = MethodDefinitionBuilder.method(gen(), false, true, "$outer");// TODO ancestorLocal
            outerBuilderCompanion.modifiers(PRIVATE | FINAL);
            JCExpression jt = makeJavaType(outerTypeCompanion);
            outerBuilderCompanion.resultType(null, jt);
            boolean requiresCast = false;
            JCExpression expr = makeErroneous();
            Scope container = model.getContainer();
            if (container instanceof Class) {
                expr = makeSelect(makeQuotedQualIdentFromString(getFQDeclarationName((Class)container)), "this");
            } else if (container instanceof Interface) {
                expr = make().Apply(null,// TODO Type args
                        makeSelect("$this", "$outer"),
                        List.<JCExpression>nil());
                requiresCast = Decl.isLocal((Interface)container);
            } else if (Decl.isLocal((model))) {
                while (!(container instanceof TypeDeclaration)) {
                    container = container.getContainer();
                }
                expr = makeSelect(
                                makeQuotedQualIdentFromString(getFQDeclarationName((TypeDeclaration)container)), 
                                "this");
            } else {
                throw new RuntimeException();
            }
            if (requiresCast) {
                expr = make().TypeCast(makeJavaType(outerTypeCompanion), expr);
            }
            outerBuilderCompanion.body(make().Return(expr));
            companionBuilder.defs(outerBuilderCompanion.build());

            // Add an $outer() method to the interface
            MethodDefinitionBuilder outerBuilderInterface = MethodDefinitionBuilder.method(gen(), false, true, "$outer");// TODO ancestorLocal
            outerBuilderInterface.annotations(makeAtIgnore());
            outerBuilderInterface.modifiers(PUBLIC | ABSTRACT);
            outerBuilderInterface.resultType(null, makeJavaType(outerTypeInterface));
            classBuilder.defs(outerBuilderInterface.build());
        }
    }

    public void transform(AttributeDeclaration decl, ClassDefinitionBuilder classBuilder) {
        boolean useField = Decl.isCaptured(decl);
        String attrName = decl.getIdentifier().getText();

        // Only a non-formal attribute has a corresponding field
        // and if a captured class parameter exists with the same name we skip this part as well
        Parameter p = findParamForAttr(decl);
        boolean createField = (p == null) || (useField && !p.isCaptured());
        if (!Decl.isFormal(decl) && createField) {
            JCExpression initialValue = null;
            if (decl.getSpecifierOrInitializerExpression() != null) {
                Value declarationModel = decl.getDeclarationModel();
                initialValue = expressionGen().transformExpression(decl.getSpecifierOrInitializerExpression().getExpression(), 
                        Util.getBoxingStrategy(declarationModel), 
                        declarationModel.getType());
            }

            int flags = 0;
            TypedDeclaration nonWideningType = nonWideningTypeDecl(decl.getDeclarationModel());
            if (!Util.isUnBoxed(nonWideningType)) {
                flags |= NO_PRIMITIVES;
            }
            JCExpression type = makeJavaType(nonWideningType.getType(), flags);

            int modifiers = (useField) ? transformAttributeFieldDeclFlags(decl) : transformLocalDeclFlags(decl);
            classBuilder.field(modifiers, attrName, type, initialValue, !useField);
        }

        if (useField) {
            classBuilder.defs(makeGetter(decl));
            if (Decl.isMutable(decl)) {
                classBuilder.defs(makeSetter(decl));
            }
        }        
    }
    
    private Parameter findParamForAttr(AttributeDeclaration decl) {
        String attrName = decl.getIdentifier().getText();
    	if (Decl.withinClass(decl)) {
    		Class c = (Class)decl.getDeclarationModel().getContainer();
    		if (!c.getParameterLists().isEmpty()) {
	    		for (Parameter p : c.getParameterLists().get(0).getParameters()) {
	    			if (attrName.equals(p.getName())) {
	    				return p;
	    			}
	    		}
    		}
    	}
		return null;
	}

	public List<JCTree> transform(AttributeSetterDefinition decl) {
        JCBlock body = statementGen().transform(decl.getBlock());
        String name = decl.getIdentifier().getText();
        return AttributeDefinitionBuilder
                /* 
                 * We use the getter as TypedDeclaration here because this is the same type but has a refined
                 * declaration we can use to make sure we're not widening the attribute type.
                 */
            .setter(this, name, decl.getDeclarationModel().getGetter())
            .modifiers(transformAttributeGetSetDeclFlags(decl))
            .isActual(isActual(decl))
            .setterBlock(body)
            .build();
    }

    public List<JCTree> transform(AttributeGetterDefinition decl) {
        String name = decl.getIdentifier().getText();
        JCBlock body = statementGen().transform(decl.getBlock());
        return AttributeDefinitionBuilder
            .getter(this, name, decl.getDeclarationModel())
            .modifiers(transformAttributeGetSetDeclFlags(decl))
            .isActual(Decl.isActual(decl))
            .getterBlock(body)
            .build();
    }

    private int transformClassDeclFlags(Tree.ClassOrInterface cdecl) {
        int result = 0;

        result |= Decl.isShared(cdecl) ? PUBLIC : 0;
        result |= Decl.isAbstract(cdecl) && (cdecl instanceof Tree.AnyClass) ? ABSTRACT : 0;
        result |= (cdecl instanceof Tree.AnyInterface) ? INTERFACE : 0;

        return result;
    }
    
    private int transformOverloadCtorFlags(Tree.ClassOrInterface typeDecl) {
        return transformClassDeclFlags(typeDecl) & (PUBLIC | PRIVATE | PROTECTED);
    }

    private int transformMethodDeclFlags(Tree.AnyMethod def) {
        return transformMethodDeclFlags(def.getDeclarationModel());
    }
    
    private int transformMethodDeclFlags(Method def) {
        int result = 0;

        if (def.isToplevel()) {
            result |= def.isShared() ? PUBLIC : 0;
            result |= STATIC;
        } else if (Decl.isLocal(def)) {
            result |= def.isShared() ? PUBLIC : 0;
        } else {
            result |= def.isShared() || def.getContainer() instanceof Interface ? PUBLIC : PRIVATE;
            result |= def.isFormal() && !def.isDefault() ? ABSTRACT : 0;
            result |= !(def.isFormal() || def.isDefault() || def.getContainer() instanceof Interface) ? FINAL : 0;
        }

        return result;
    }
    
    private int transformOverloadMethodDeclFlags(final Declaration model,
            Tree.AnyMethod meth) {
        int mods = transformMethodDeclFlags(meth);
        if (!Decl.withinInterface((model))) {
            mods |= FINAL;
        }
        return mods;
    }
    
    private int transformOverloadMethodImplFlags(Method method) {
        return (transformMethodDeclFlags(method) & (PUBLIC | PRIVATE | PROTECTED)) | FINAL;
    }

    private int transformAttributeFieldDeclFlags(Tree.AttributeDeclaration cdecl) {
        int result = 0;

        result |= Decl.isMutable(cdecl) ? 0 : FINAL;
        result |= PRIVATE;

        return result;
    }

    private int transformLocalDeclFlags(Tree.AttributeDeclaration cdecl) {
        int result = 0;

        result |= Decl.isMutable(cdecl) ? 0 : FINAL;

        return result;
    }

    private int transformAttributeGetSetDeclFlags(Tree.TypedDeclaration cdecl) {
        TypedDeclaration tdecl = cdecl.getDeclarationModel();
        if (tdecl instanceof Setter) {
            // Spec says: A setter may not be annotated shared, default or 
            // actual. The visibility and refinement modifiers of an attribute 
            // with a setter are specified by annotating the matching getter.
            tdecl = ((Setter)tdecl).getGetter();
        }
        
        int result = 0;

        result |= tdecl.isShared() ? PUBLIC : PRIVATE;
        result |= (tdecl.isFormal() && !tdecl.isDefault()) ? ABSTRACT : 0;
        result |= !(tdecl.isFormal() || tdecl.isDefault()) ? FINAL : 0;

        return result;
    }

    private int transformObjectDeclFlags(Tree.ObjectDefinition cdecl) {
        int result = 0;

        result |= FINAL;
        result |= !Decl.isAncestorLocal(cdecl) && Decl.isShared(cdecl) ? PUBLIC : 0;

        return result;
    }

    private List<JCTree> makeGetter(Tree.AttributeDeclaration decl) {
        at(decl);
        String atrrName = decl.getIdentifier().getText();
        return AttributeDefinitionBuilder
            .getter(this, atrrName, decl.getDeclarationModel())
            .modifiers(transformAttributeGetSetDeclFlags(decl))
            .isActual(Decl.isActual(decl))
            .isFormal(Decl.isFormal(decl))
            .build();
    }

    private List<JCTree> makeSetter(Tree.AttributeDeclaration decl) {
        at(decl);
        String attrName = decl.getIdentifier().getText();
        return AttributeDefinitionBuilder
            .setter(this, attrName, decl.getDeclarationModel())
            .modifiers(transformAttributeGetSetDeclFlags(decl))
            .isActual(isActual(decl))
            .isFormal(Decl.isFormal(decl))
            .build();
    }

    private boolean isActual(Tree.TypedDeclaration decl) {
        boolean actual;
        Declaration refinedDecl = decl.getDeclarationModel().getRefinedDeclaration();
        if (refinedDecl instanceof Value) {
            // If a variable attr is refining a non-variable one then the
            // setter is not overriding anything: We mustn't add an @Override
            Value refinedValue = (Value)refinedDecl;
            actual = refinedValue.isVariable() && Decl.isActual(decl);
        } else if (decl instanceof AttributeSetterDefinition
                && refinedDecl instanceof Getter) {            
            AttributeSetterDefinition setterDecl = (AttributeSetterDefinition)decl;
            Getter refinedGetter = (Getter)refinedDecl;
            actual = refinedGetter.getSetter() != null // The setter might not be refined even if the getter is 
                    && refinedGetter.isDefault() // The setter metadata comes from the getter
                    && setterDecl.getDeclarationModel().getGetter().isActual();
        } else {
            actual = Decl.isActual(decl);
        }
        return actual;
    }
    
    public List<JCTree> transformWrappedMethod(Tree.AnyMethod def) {
        // Generate a wrapper class for the method
        String name = def.getIdentifier().getText();
        ClassDefinitionBuilder builder = ClassDefinitionBuilder.methodWrapper(this, Decl.isAncestorLocal(def), name, Decl.isShared(def));
        builder.body(classGen().transform(def, builder));
        
        // Toplevel method
        if (Strategy.generateMain(def)) {
            // Add a main() method
            builder.body(makeMainForFunction(def.getDeclarationModel()));
        }
        
        List<JCTree> result = builder.build();
        
        if (Decl.isLocal(def)) {
            // Inner method
            JCExpression nameId = makeQuotedIdent(name);
            JCVariableDecl call = at(def).VarDef(
                    make().Modifiers(FINAL),
                    names().fromString(name),
                    nameId,
                    makeNewClass(name, false));
            result = result.append(call);
        } 
        return result;
    }

    public List<JCTree> transform(Tree.AnyMethod def, ClassDefinitionBuilder classBuilder) {
        ListBuffer<JCTree> lb = ListBuffer.<JCTree>lb();
        final Method model = def.getDeclarationModel();
        final String methodName = Util.quoteMethodNameIfProperty(model, gen());
        
        java.util.List<ParameterList> parameterLists = def.getParameterLists();
        boolean mpl = parameterLists.size() > 1;
        ProducedType innerResultType = model.getType();
        ProducedType resultType = innerResultType;
        // Transform the method body of the 'inner-most method'
        List<JCStatement> body = null;
        if (def instanceof Tree.MethodDefinition) {
            Scope container = model.getContainer();
            boolean isInterface = container instanceof com.redhat.ceylon.compiler.typechecker.model.Interface;
            if(!isInterface){
                body = statementGen().transform(((Tree.MethodDefinition)def).getBlock()).getStatements();
            }
        } else if (def instanceof MethodDeclaration
                && ((MethodDeclaration) def).getSpecifierExpression() != null) {
            InvocationBuilder specifierBuilder = InvocationBuilder.forSpecifierInvocation(gen(), ((MethodDeclaration) def).getSpecifierExpression(), def.getDeclarationModel());
            if (isVoid(def)) {
                body = List.<JCStatement>of(make().Exec(specifierBuilder.build()));
            } else {
                body = List.<JCStatement>of(make().Return(specifierBuilder.build()));
            }
        }
        
        // Construct all but the outer-most method
        for (int index = parameterLists.size() - 1; index >  0; index--) {
            resultType = gen().typeFact().getCallableType(resultType);
            CallableBuilder cb = CallableBuilder.mpl(gen(), resultType, def.getDeclarationModel().getParameterLists().get(index), body);
            body = List.<JCStatement>of(make().Return(cb.build()));
        }
        
        // Finally construct the outermost method using the body we've built so far
        MethodDefinitionBuilder methodBuilder = MethodDefinitionBuilder.method(this, Decl.isAncestorLocal(def), model.isClassOrInterfaceMember(), 
                methodName);
        
        final ParameterList paramList = parameterLists.get(0);
        
        if (mpl) {
            methodBuilder.resultType(null, makeJavaType(resultType));
        } else {
            methodBuilder.resultType(model);
        }
        
        copyTypeParameters(def, methodBuilder);
        
        for (Tree.Parameter param : paramList.getParameters()) {
            methodBuilder.parameter(param);
            DefaultArgument defaultArgument = param.getDefaultArgument();
                
            if (model.getRefinedDeclaration() == model 
                    && (defaultArgument != null
                        || param.getDeclarationModel().isSequenced())) {
                
                JCMethodDecl defaultValueMethodImpl = makeParamDefaultValueMethod(false, def, paramList, param);
                if (Decl.defaultParameterMethodOnSelf(def)) {
                    lb.add(defaultValueMethodImpl);    
                } else {
                    lb.add(makeParamDefaultValueMethod(true, def, paramList, param));
                    classBuilder.getCompanionBuilder().defs(defaultValueMethodImpl);
                }
                
                MethodDefinitionBuilder overloadBuilder = MethodDefinitionBuilder.method(this, Decl.isAncestorLocal(def), model.isClassOrInterfaceMember(),
                        methodName);
                JCMethodDecl overloadedMethod = makeOverloadsForDefaultedParameter(!Decl.withinInterface(model), overloadBuilder, 
                        def, paramList, param).build();
                lb.prepend(overloadedMethod);    
            }

            if (defaultArgument != null
                    || isLastParameter(paramList, param)) {        
                if (Decl.withinInterface(model)
                        && def instanceof MethodDeclaration
                        && ((MethodDeclaration) def).getSpecifierExpression() == null) {
                    // interface methods without concrete implementation (including 
                    // overloaded versions) on the companion class by delegating to 
                    // $this (for closure purposes)
                    JCMethodDecl result = makeConcreteInterfaceMethodsForClosure(
                            def, methodName, paramList, param);
                    classBuilder.getCompanionBuilder().defs(result);
                }
            }         
        }
        
        if (body != null) {
            methodBuilder.body(body);
        } else {
            methodBuilder.noBody();
        }
        
        if(Util.hasCompilerAnnotation(def, "test")){
            methodBuilder.annotations(List.of(make().Annotation(makeQualIdentFromString("org.junit.Test"), List.<JCTree.JCExpression>nil())));
        }
        
        methodBuilder
            .modifiers(transformMethodDeclFlags(def))
            .isActual(Decl.isActual(def))
            .modelAnnotations(model.getAnnotations());
        
        // Generate an impl for overloaded methods using the $impl instance
        // TODO MPL
        if (Decl.withinInterface(model.getRefinedDeclaration())
                && !Decl.withinInterface(model)) {
            java.util.List<Parameter> parameters = model.getParameterLists().get(0).getParameters();
            for (Parameter p : parameters) {
                if (p.isDefaulted() 
                        || p.isSequenced()) {
                    classBuilder.defs(transformDefaultValueMethodImpl(def, parameters, p));
                    classBuilder.defs(overloadMethodImpl(def, parameters, p));
                }
            }
        }
        
        lb.prepend(methodBuilder.build());
        
        return lb.toList();
    }

    private boolean isVoid(Tree.Declaration def) {
        if (def instanceof Tree.AnyMethod) {
            return gen().isVoid(((Tree.AnyMethod)def).getType().getTypeModel());
        } else if (def instanceof Tree.AnyClass) {
            // Consider classes void since ctors don't require a return statement
            return true;
        }
        throw new RuntimeException();
    }

    private JCMethodDecl makeConcreteInterfaceMethodsForClosure(
            Tree.AnyMethod def, final String methodName,
            final ParameterList paramList, Tree.Parameter currentParam) {
        final Method model = def.getDeclarationModel();
        MethodDefinitionBuilder delegateBuilder = MethodDefinitionBuilder.method(gen(), false, true, methodName);
        delegateBuilder.modifiers(PRIVATE | FINAL);
        copyTypeParameters(def, delegateBuilder);
        delegateBuilder.resultType(model);
        ListBuffer<JCExpression> arguments = ListBuffer.<JCExpression>lb();
        for (Tree.Parameter p : paramList.getParameters().subList(0, paramList.getParameters().indexOf(currentParam))) {
            delegateBuilder.parameter(p);
            arguments.append(makeQuotedIdent(p.getDeclarationModel().getName()));
        }
        JCExpression expr = make().Apply(
                typeParams(model),
                makeSelect("$this", methodName), 
                arguments.toList());
        if (isVoid(def)) {
            delegateBuilder.body(make().Exec(expr));
        } else {
            delegateBuilder.body(make().Return(expr));
        }
        JCMethodDecl result = delegateBuilder.build();
        return result;
    }

    private boolean isLastParameter(final ParameterList paramList,
            Tree.Parameter param) {
        return paramList.getParameters().indexOf(param) == paramList.getParameters().size();
    }

    private JCMethodDecl transformDefaultValueMethodImpl(Method method,
            java.util.List<Parameter> parameters, Parameter p) {
        String name = Util.getDefaultedParamMethodName(method, p);
        MethodDefinitionBuilder overloadBuilder = MethodDefinitionBuilder.method(gen(), false, true, name);// TODO ancestorLocal
        overloadBuilder.annotations(makeAtOverride());
        overloadBuilder.annotations(makeAtIgnore());
        overloadBuilder.modifiers(transformOverloadMethodImplFlags(method));
        for (TypeParameter tp : method.getTypeParameters()) {
            overloadBuilder.typeParameter(tp);
        }
        overloadBuilder.resultType(null, makeJavaType(p.getType()));
        ListBuffer<JCExpression> args = ListBuffer.<JCExpression>lb(); 
        for (Parameter p2 : parameters.subList(0, parameters.indexOf(p))) {
            overloadBuilder.parameter(p2);
            args.append(makeQuotedIdent(p2.getName()));
        }
        overloadBuilder.body(make().Return(
                make().Apply(typeParams(method),
                        makeDefaultedParamMethodIdent(method, p),
                        args.toList())));
        return overloadBuilder.build();
    }
    
    private JCMethodDecl overloadMethodImpl(
            Method method, java.util.List<Parameter> parameters, Parameter p) {
        MethodDefinitionBuilder overloadBuilder = MethodDefinitionBuilder.method(gen(), false, true, method.getName());// TODO ancestorLocal
        overloadBuilder.annotations(makeAtOverride());
        overloadBuilder.annotations(makeAtIgnore());
        overloadBuilder.modifiers(transformOverloadMethodImplFlags(method));
        
        for (TypeParameter tp : method.getTypeParameters()) {
            overloadBuilder.typeParameter(tp);
        }
        overloadBuilder.resultType(method);
        final ListBuffer<JCExpression> args = ListBuffer.<JCExpression>lb();
        final ListBuffer<JCVariableDecl> vars = ListBuffer.<JCVariableDecl>lb();        
        boolean seen = false;
        // TODO This code is very similar to transformForDefaultedParameter() but
        // operates on model.Parameter not Tree.Parameter
        
        for (Parameter p2 : parameters) {
            if (p2 == p) {
                seen = true;
            }
            if (!seen) {
                args.append(makeQuotedIdent(p2.getName()));
                overloadBuilder.parameter(p2);
            } else {
                String tempName = tempName(p2.getName());
                vars.append(makeVar(
                        tempName, 
                        makeJavaType(p2.getType()), 
                        make().Apply(typeParams(method),
                                makeDefaultedParamMethodIdent(method, p2), 
                            args.toList())));
                args.append(makeQuotedIdent(tempName));
            }
        }
        
        JCExpression invocation = make().Apply(
                typeParams(method),
                makeQuotedIdent(method.getName()),
                args.toList());
        
        if (isVoid(method.getType())) {
            invocation = make().LetExpr(vars.toList(), List.<JCStatement>of(make().Exec(invocation)), makeNull());
            overloadBuilder.body(make().Exec(invocation));
        } else {
            invocation = make().LetExpr(vars.toList(), invocation);
            overloadBuilder.body(make().Return(invocation));
        }
        
        return overloadBuilder.build();
    }

    private MethodDefinitionBuilder makeOverloadsForDefaultedParameter(
            boolean body,
            MethodDefinitionBuilder overloadBuilder,
            Tree.Declaration def,
            Tree.ParameterList paramList,
            Tree.Parameter currentParam) {
        at(currentParam);
        final Declaration model = def.getDeclarationModel();
        overloadBuilder.annotations(makeAtIgnore());
        
        final JCExpression methName;
        if (def instanceof Tree.AnyMethod) {
            Tree.AnyMethod meth = (Tree.AnyMethod)def;
            long mods = transformOverloadMethodDeclFlags(model, meth);
            overloadBuilder.modifiers(mods);
            methName = makeQuotedIdent(Util.quoteMethodNameIfProperty((Method)model, gen()));
            overloadBuilder.resultType((Method)model);
        } else if (def instanceof Tree.ClassOrInterface) {
            Tree.ClassOrInterface typeDecl = (Tree.ClassOrInterface)def;
            overloadBuilder.modifiers(transformOverloadCtorFlags(typeDecl));
            methName = makeUnquotedIdent("this");
        } else {
            throw new RuntimeException();
        }
        
        // TODO MPL
        if (def instanceof Tree.AnyMethod) {
            copyTypeParameters((Tree.AnyMethod)def, overloadBuilder);
        }

        // TODO Some simple default expressions (e.g. literals, null and 
        // base expressions it might be worth inlining the expression rather 
        // than calling the default value method.
        // TODO This really belongs in the invocation builder
        
        ListBuffer<JCExpression> args = ListBuffer.<JCExpression>lb();
        ListBuffer<JCVariableDecl> vars = ListBuffer.<JCVariableDecl>lb();
        
        final String companionInstanceName = tempName("$impl$");
        if (def instanceof Tree.AnyClass) {
            
            vars.append(makeVar(companionInstanceName, 
                    makeCompanionType((Class)def.getDeclarationModel()),
                    make().NewClass(null, // TODO encl == null ???
                            null,
                            makeCompanionType((Class)def.getDeclarationModel()),
                            List.<JCExpression>nil(), null)));
        }
        
        boolean useDefault = false;
        for (Tree.Parameter param2 : paramList.getParameters()) {
            if (param2 == currentParam) {
                useDefault = true;
            }
            if (useDefault) {
                String methodName = Util.getDefaultedParamMethodName(def.getDeclarationModel(), param2.getDeclarationModel());
                JCExpression defaultValueMethodName;
                if (Decl.defaultParameterMethodOnSelf(def)) {
                    defaultValueMethodName = gen().makeQuotedIdent(methodName);
                } else {
                    defaultValueMethodName = gen().makeQuotedQualIdent(makeQuotedIdent(companionInstanceName), methodName);
                }
                String varName = tempName("$"+param2.getIdentifier().getText()+"$");
                vars.append(makeVar(varName, 
                        makeJavaType(param2.getDeclarationModel().getType()), 
                        make().Apply(List.<JCExpression>nil(), defaultValueMethodName, 
                                ListBuffer.<JCExpression>lb().appendList(args).toList())));
                args.add(makeUnquotedIdent(varName));
            } else {
                overloadBuilder.parameter(param2);
                args.add(makeQuotedIdent(param2.getIdentifier().getText()));
            }
        }
        
        // TODO Type args on method call
        if (body) {
            JCExpression invocation = make().Apply(List.<JCExpression>nil(),
                    methName, args.toList());
               
            if (isVoid(def)) {
                invocation = make().LetExpr(vars.toList(), List.<JCStatement>of(make().Exec(invocation)), makeNull());
                overloadBuilder.body(make().Exec(invocation));
            } else {
                invocation = make().LetExpr(vars.toList(), invocation);
                overloadBuilder.body(make().Return(invocation));
            }
        } else {
            overloadBuilder.noBody();
        }
        
        return overloadBuilder;
    }


    
    public JCMethodDecl transformConcreteInterfaceMember(MethodDefinition def, ProducedType type) {
        MethodDefinitionBuilder methodBuilder = MethodDefinitionBuilder.method(this, Decl.isAncestorLocal(def), true, Util.quoteMethodNameIfProperty(def.getDeclarationModel(), gen()));
        
        for (Tree.Parameter param : def.getParameterLists().get(0).getParameters()) {
            methodBuilder.parameter(param);
        }

        copyTypeParameters(def, methodBuilder);
        
        if (!isVoid(def)) {
            methodBuilder.resultType(def.getDeclarationModel());
        }
        
        // FIXME: this needs rewriting to map non-qualified refs to $this
        JCBlock body = statementGen().transform(def.getBlock());
        methodBuilder.block(body);
                
        return methodBuilder
            .modifiers(transformMethodDeclFlags(def))
            .isActual(Decl.isActual(def))
            .build();
    }

    /**
     * Creates a (possibly abstract) method for retrieving the value for a 
     * defaulted parameter
     */
    private JCMethodDecl makeParamDefaultValueMethod(boolean noBody, Tree.Declaration container, 
            Tree.ParameterList params, Tree.Parameter currentParam) {
        at(currentParam);
        Parameter parameter = currentParam.getDeclarationModel();
        String name = Util.getDefaultedParamMethodName(container.getDeclarationModel(), parameter );
        MethodDefinitionBuilder methodBuilder = MethodDefinitionBuilder.method(this, Decl.isAncestorLocal(currentParam), true, name);
        
        int modifiers = noBody ? PUBLIC | ABSTRACT : FINAL;
        if (container.getDeclarationModel().isShared()) {
            modifiers |= PUBLIC;
        } else if (!container.getDeclarationModel().isToplevel()
                && !noBody){
            modifiers |= PRIVATE;
        }
        if (Decl.defaultParameterMethodStatic(container)) {
            modifiers |= STATIC;
        }
        methodBuilder.modifiers(modifiers);
        
        if (container instanceof Tree.AnyMethod) {
            copyTypeParameters((Tree.AnyMethod)container, methodBuilder);
        }
        
        // Add any of the preceding parameters as parameters to the method
        for (Tree.Parameter p : params.getParameters()) {
            if (p == currentParam) {
                break;
            }
            methodBuilder.parameter(p);
        }

        // The method's return type is the same as the parameter's type
        methodBuilder.resultType(parameter);

        // The implementation of the method
        if (noBody) {
            methodBuilder.noBody();
        } else {
            JCExpression expr = expressionGen().transform(currentParam);
            JCBlock body = at(currentParam).Block(0, List.<JCStatement> of(at(currentParam).Return(expr)));
            methodBuilder.block(body);
        }

        return methodBuilder.build();
    }

    public List<JCTree> transformObject(Tree.ObjectDefinition def, ClassDefinitionBuilder containingClassBuilder) {
        String name = def.getIdentifier().getText();
        ClassDefinitionBuilder objectClassBuilder = ClassDefinitionBuilder.klass(this, Decl.isAncestorLocal(def), name);
        
        CeylonVisitor visitor = new CeylonVisitor(gen(), objectClassBuilder);
        def.visitChildren(visitor);

        TypeDeclaration decl = def.getDeclarationModel().getType().getDeclaration();

        if (Decl.isToplevel(def)) {
            objectClassBuilder.body(makeObjectGlobal(def, def.getDeclarationModel().getQualifiedNameString()).toList());
        }

        List<JCTree> result = objectClassBuilder
            .annotations(makeAtObject())
            .modelAnnotations(def.getDeclarationModel().getAnnotations())
            .modifiers(transformObjectDeclFlags(def))
            .constructorModifiers(PRIVATE)
            .satisfies(decl.getSatisfiedTypes())
            .init((List<JCStatement>)visitor.getResult().toList())
            .build();
        
        if (Decl.isLocal(def)) {
            result = result.append(makeLocalIdentityInstance(name, false));
        } else if (Decl.withinClassOrInterface(def)) {
            boolean visible = Decl.isCaptured(def);
            int modifiers = FINAL | ((visible) ? PRIVATE : 0);
            JCExpression type = makeJavaType(def.getType().getTypeModel());
            JCExpression initialValue = makeNewClass(makeJavaType(def.getType().getTypeModel()), List.<JCTree.JCExpression>nil());
            containingClassBuilder.field(modifiers, name, type, initialValue, !visible);
            
            if (visible) {
                result = result.appendList(AttributeDefinitionBuilder
                    .getter(this, name, def.getDeclarationModel())
                    .modifiers(transformAttributeGetSetDeclFlags(def))
                    .isActual(Decl.isActual(def))
                    .build());
            }
        }
        
        return result;
    }

    private ListBuffer<JCTree> makeObjectGlobal(Tree.ObjectDefinition decl, String generatedClassName) {
        ListBuffer<JCTree> defs = ListBuffer.lb();
        AttributeDefinitionBuilder builder = AttributeDefinitionBuilder
                .wrapped(this, decl.getIdentifier().getText(), decl.getDeclarationModel())
                .immutable()
                .initialValue(makeNewClass(generatedClassName, true))
                .is(PUBLIC, Decl.isShared(decl))
                .is(STATIC, true);

        builder.appendDefinitionsTo(defs);
        return defs;
    }
    
    /**
     * Makes a {@code main()} method which calls the given top-level method
     * @param def
     */
    private JCMethodDecl makeMainForClass(final ClassOrInterface model) {
        at(null);
        JCExpression nameId = makeQuotedFQIdent(model.getQualifiedNameString());
        JCNewClass expr = make().NewClass(null, null, nameId, List.<JCTree.JCExpression>nil(), null);
        return makeMainMethod(model, expr);
    }
    
    /**
     * Makes a {@code main()} method which calls the given top-level method
     * @param method
     */
    private JCMethodDecl makeMainForFunction(Method method) {
        at(null);
        String name = method.getName();
        String path = method.getQualifiedNameString();
        path += "." + name;
        JCExpression qualifiedName = makeQuotedFQIdent(path);
        JCMethodDecl mainMethod = makeMainMethod(method, make().Apply(null, qualifiedName, List.<JCTree.JCExpression>nil()));
        return mainMethod;
    }
    
    /** 
     * Makes a {@code main()} method which calls the given callee 
     * (a no-args method or class)
     * @param decl
     * @param callee
     */
    private JCMethodDecl makeMainMethod(Declaration decl, JCExpression callee) {
        // Add a main() method
        MethodDefinitionBuilder methbuilder = MethodDefinitionBuilder
                .main(this, Decl.isAncestorLocal(decl))
                .annotations(makeAtIgnore());
        // Add call to process.setupArguments
        JCExpression argsId = makeUnquotedIdent("args");
        JCMethodInvocation processExpr = make().Apply(null, makeFQIdent("ceylon", "language", "process", "getProcess"), List.<JCTree.JCExpression>nil());
        methbuilder.body(make().Exec(make().Apply(null, makeSelect(processExpr, "setupArguments"), List.<JCTree.JCExpression>of(argsId))));
        // Add call to toplevel method
        methbuilder.body(make().Exec(callee));
        return methbuilder.build();
    }
    
    void copyTypeParameters(Tree.AnyMethod def, MethodDefinitionBuilder methodBuilder) {
        if (def.getTypeParameterList() != null) {
            for (Tree.TypeParameterDeclaration t : def.getTypeParameterList().getTypeParameterDeclarations()) {
                methodBuilder.typeParameter(t);
            }
        }
    }
}
