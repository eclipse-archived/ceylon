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
import static com.sun.tools.javac.code.Flags.PUBLIC;
import static com.sun.tools.javac.code.Flags.STATIC;

import com.redhat.ceylon.compiler.java.util.Decl;
import com.redhat.ceylon.compiler.java.util.Util;
import com.redhat.ceylon.compiler.typechecker.model.Class;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Getter;
import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.Parameter;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.Scope;
import com.redhat.ceylon.compiler.typechecker.model.Setter;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;
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
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCMethodInvocation;
import com.sun.tools.javac.tree.JCTree.JCNewClass;
import com.sun.tools.javac.tree.JCTree.JCStatement;
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
        String className = def.getIdentifier().getText();
        ClassDefinitionBuilder classBuilder = ClassDefinitionBuilder
                .klass(this, Decl.isAncestorLocal(def), className);

        if (def instanceof Tree.AnyClass) {
            ParameterList paramList = ((Tree.AnyClass)def).getParameterList();
            for (Tree.Parameter param : paramList.getParameters()) {
                classBuilder.parameter(param);
                // Does the parameter have a default value?
                if (param.getDefaultArgument() != null &&  param.getDefaultArgument().getSpecifierExpression() != null) {
                    classBuilder.getCompanionBuilder().defs(transformDefaultedParameter(false, param, def, paramList));
                }
            }
            
            for (Tree.Parameter param : paramList.getParameters()) {
                DefaultArgument defaultArgument = param.getDefaultArgument();
                if (defaultArgument != null) {
                    MethodDefinitionBuilder overloadBuilder = classBuilder.addConstructor();
                    transformForDefaultedParameter(
                            overloadBuilder,
                            def, def.getDeclarationModel(), true, paramList, param);
                    
                }
            }
        }
        
        if (def instanceof Tree.AnyInterface) {
            ClassDefinitionBuilder companionBuilder = classBuilder.getCompanionBuilder();
            MethodDefinitionBuilder ctor = companionBuilder.addConstructor();
            
            ProducedType thisType = def.getDeclarationModel().getType();
            ctor.parameter(0, "$this", makeJavaType(thisType), null);
            ListBuffer<JCStatement> bodyStatements = ListBuffer.<JCStatement>of(
                    make().Exec(
                            make().Assign(
                                    makeSelect("this", "$this"), 
                                    makeUnquotedIdent("$this"))));
            companionBuilder.field(PRIVATE | FINAL, 
                    "$this", 
                    makeJavaType(thisType), 
                    null, false);
            if (!def.getDeclarationModel().isToplevel()) {
                ProducedType outerType = thisType.getQualifyingType();
                ctor.parameter(0, "$outer", makeJavaType(outerType), null);
                bodyStatements.append(
                        make().Exec(
                                make().Assign(
                                        makeSelect("this", "$outer"), 
                                        makeUnquotedIdent("$outer"))));
                companionBuilder.field(PRIVATE | FINAL, 
                        "$outer", 
                        makeJavaType(outerType), 
                        null, false);
                
            }
            ctor.body(bodyStatements.toList());
        }
        
        
        CeylonVisitor visitor = new CeylonVisitor(gen(), classBuilder);
        def.visitChildren(visitor);

        // Check if it's a Class without initializer parameters
        if (def instanceof Tree.AnyClass && Decl.isToplevel(def) && !Decl.isAbstract(def)) {
            com.redhat.ceylon.compiler.typechecker.model.Class c = (com.redhat.ceylon.compiler.typechecker.model.Class) def.getDeclarationModel();
            if (c.getParameterList().getParameters().isEmpty()) {
                // Add a main() method
                at(null);
                JCExpression nameId = makeQuotedFQIdent(c.getQualifiedNameString());
                JCNewClass expr = make().NewClass(null, null, nameId, List.<JCTree.JCExpression>nil(), null);
                classBuilder.body(makeMainMethod(def.getDeclarationModel(), expr));
            }
        }
        
        return classBuilder
            .modelAnnotations(def.getDeclarationModel().getAnnotations())
            .modifiers(transformClassDeclFlags(def))
            .satisfies(def.getDeclarationModel().getSatisfiedTypes())
            .caseTypes(def.getDeclarationModel().getCaseTypes())
            .init((List<JCStatement>)visitor.getResult().toList())
            .build();
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

    private int transformMethodDeclFlags(Tree.AnyMethod def) {
        int result = 0;

        if (Decl.isToplevel(def)) {
            result |= Decl.isShared(def) ? PUBLIC : 0;
            result |= STATIC;
        } else if (Decl.isLocal(def)) {
            result |= Decl.isShared(def) ? PUBLIC : 0;
        } else {
            result |= Decl.isShared(def) ? PUBLIC : PRIVATE;
            result |= (Decl.isFormal(def) && !Decl.isDefault(def)) ? ABSTRACT : 0;
            result |= !(Decl.isFormal(def) || Decl.isDefault(def)) ? FINAL : 0;
        }

        return result;
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
        JCExpression nameId = makeQuotedIdent(name);
        ClassDefinitionBuilder builder = ClassDefinitionBuilder.methodWrapper(this, Decl.isAncestorLocal(def), name, Decl.isShared(def));
        builder.body(classGen().transform(def, builder));
        if (Decl.isLocal(def)) {
            // Inner method
            List<JCTree> result = builder.build();
            JCVariableDecl call = at(def).VarDef(
                    make().Modifiers(FINAL),
                    names().fromString(name),
                    nameId,
                    makeNewClass(name, false));
            return result.append(call);
        } else {
            // Toplevel method
            if (!def.getParameterLists().isEmpty() && def.getParameterLists().get(0).getParameters().isEmpty()) {
                // Add a main() method
                at(null);
                String path = def.getDeclarationModel().getQualifiedNameString();
                path += "." + name;
                JCExpression qualifiedName = makeQuotedFQIdent(path);
                builder.body(makeMainMethod(def.getDeclarationModel(), make().Apply(null, qualifiedName, List.<JCTree.JCExpression>nil())));
            }
            return builder.build();                
        }
    }

    public List<JCTree> transform(Tree.AnyMethod def, ClassDefinitionBuilder classBuilder) {
        ListBuffer<JCTree> lb = ListBuffer.<JCTree>lb();
        Method model = def.getDeclarationModel();
        
        java.util.List<ParameterList> parameterLists = def.getParameterLists();
        boolean mpl = parameterLists.size() > 1;
        ProducedType innerResultType = model.getType();
        ProducedType resultType = innerResultType;
        boolean isVoid = gen().isVoid(def.getType().getTypeModel());
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
            if (isVoid) {
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
                Util.quoteMethodNameIfProperty(model, gen()));
        
        ParameterList paramList = parameterLists.get(0);
        
        if (mpl) {
            methodBuilder.resultType(null, makeJavaType(resultType));
        } else {
            methodBuilder.resultType(model);
        }
        
        if (def.getTypeParameterList() != null) {
            for (Tree.TypeParameterDeclaration t : def.getTypeParameterList().getTypeParameterDeclarations()) {
                methodBuilder.typeParameter(t);
            }
        }
        
        for (Tree.Parameter param : paramList.getParameters()) {
            methodBuilder.parameter(param);
            // Does the parameter have a default value?
            if (param.getDefaultArgument() != null &&  param.getDefaultArgument().getSpecifierExpression() != null) {
                JCMethodDecl defaultValueMethodImpl = transformDefaultedParameter(false, param, def, paramList);
                if (Decl.defaultParameterMethodOnSelf(def)) {
                    lb.add(defaultValueMethodImpl);
                } else {
                    lb.add(transformDefaultedParameter(true, param, def, paramList));
                    classBuilder.getCompanionBuilder().defs(defaultValueMethodImpl);
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
        
        for (Tree.Parameter param : paramList.getParameters()) {
            DefaultArgument defaultArgument = param.getDefaultArgument();
            if (defaultArgument != null) {
                MethodDefinitionBuilder overloadBuilder = MethodDefinitionBuilder.method(this, Decl.isAncestorLocal(def), model.isClassOrInterfaceMember(),
                        Util.quoteMethodNameIfProperty(model, gen()));
                JCMethodDecl overloadedMethod = transformForDefaultedParameter(overloadBuilder, 
                        def, model, isVoid, paramList, param).build();
                lb.prepend(overloadedMethod);
            }
        }
        
        lb.prepend(methodBuilder.build());
        
        return lb.toList();
    }
    
    private MethodDefinitionBuilder transformForDefaultedParameter(
            MethodDefinitionBuilder overloadBuilder,
            Tree.Declaration def,
            Declaration model, boolean isVoid, ParameterList paramList,
            Tree.Parameter param) {
        
        final TypeParameterList typeParameterList;
        final JCExpression methName;
        if (def instanceof Tree.AnyMethod) {
            Tree.AnyMethod meth = (Tree.AnyMethod)def;
            long mods = transformMethodDeclFlags(meth);
            if (!Decl.withinInterface((model))) {
                mods |= FINAL;
            }
            overloadBuilder.modifiers(mods);
            typeParameterList = meth.getTypeParameterList();
            methName = makeQuotedIdent(Util.quoteMethodNameIfProperty((Method)model, gen()));
        } else if (def instanceof Tree.ClassOrInterface) {
            Tree.ClassOrInterface typeDecl = (Tree.ClassOrInterface)def;
            overloadBuilder.modifiers(transformClassDeclFlags(typeDecl) & (PUBLIC | PRIVATE | Flags.PROTECTED));
            typeParameterList = typeDecl.getTypeParameterList();
            methName = makeUnquotedIdent("this");
        } else {
            throw new RuntimeException();
        }
        
        overloadBuilder.annotations(makeAtIgnore());
        if (model instanceof Method) {
            overloadBuilder.resultType((Method)model);
        }
        // TODO MPL
        if (typeParameterList != null) {
            for (Tree.TypeParameterDeclaration t : typeParameterList.getTypeParameterDeclarations()) {
                overloadBuilder.typeParameter(t);
            }
        }
        ListBuffer<JCExpression> args = ListBuffer.<JCExpression>lb();
        
        for (Tree.Parameter param2 : paramList.getParameters()) {
            if (param2 == param) {
                break;
            }        
            args.add(makeQuotedIdent(param2.getIdentifier().getText()));
            
            overloadBuilder.parameter(param2);
        }
        // TODO Some simple default expressions (e.g. literals, null and 
        // base expressions it might be worth inlining the expression rather 
        // than calling the default value method.
        // TODO We should call the no-defaults method directly using a Let
        // rather than having the overloaded call each other in sequence
        // (imagine a recursive method with many defaulted parameters -- we're 
        // wasting stack space)
        // TODO This really belongs in the invocation builder
        String methodName = Util.getDefaultedParamMethodName(def.getDeclarationModel(), param.getDeclarationModel());
        
        Declaration container = param.getDeclarationModel().getDeclaration().getRefinedDeclaration();
        if (!container.isToplevel()) {
            container = (Declaration)container.getContainer();
        }
        String className = Util.getCompanionClassName(container.getName());
        JCExpression defaultValueMethodName;
        if (Decl.defaultParameterMethodOnSelf(def)) {
            defaultValueMethodName = gen().makeQuotedIdent(methodName);
        } else {
            defaultValueMethodName = gen().makeQuotedQualIdent(gen().makeQuotedFQIdent(container.getQualifiedNameString()), className, methodName);
        }
        
        args.add(make().Apply(List.<JCExpression>nil(), defaultValueMethodName, 
                ListBuffer.<JCExpression>lb().appendList(args).toList()));
        
        // TODO Type args on method call
        JCMethodInvocation call = make().Apply(List.<JCExpression>nil(),
                methName, args.toList());
        if (isVoid) {
            overloadBuilder.body(make().Exec(call));
        } else {
            overloadBuilder.body(make().Return(call));
        }
        
        return overloadBuilder;
    }

    public JCMethodDecl transformConcreteInterfaceMember(MethodDefinition def, ProducedType type) {
        MethodDefinitionBuilder methodBuilder = MethodDefinitionBuilder.method(this, Decl.isAncestorLocal(def), true, Util.quoteMethodNameIfProperty(def.getDeclarationModel(), gen()));
        
        JCExpression typeExpr = makeJavaType(type);
        methodBuilder.parameter(FINAL, "$this", typeExpr, List.<JCTree.JCAnnotation>nil());
        for (Tree.Parameter param : def.getParameterLists().get(0).getParameters()) {
            methodBuilder.parameter(param);
        }

        if (def.getTypeParameterList() != null) {
            for (Tree.TypeParameterDeclaration t : def.getTypeParameterList().getTypeParameterDeclarations()) {
                methodBuilder.typeParameter(t);
            }
        }

        if (!(def.getType() instanceof VoidModifier)) {
            methodBuilder.resultType(def.getDeclarationModel());
        }
        
        // FIXME: this needs rewriting to map non-qualified refs to $this
        JCBlock body = statementGen().transform(def.getBlock());
        methodBuilder.block(body);
                
        return methodBuilder
            .modifiers(transformMethodDeclFlags(def) | STATIC)
            .isActual(Decl.isActual(def))
            .build();
    }

    // Creates a method to retrieve the value for a defaulted parameter
    private JCMethodDecl transformDefaultedParameter(boolean abstract_, Tree.Parameter param, 
            Tree.Declaration container, Tree.ParameterList params) {
        Parameter parameter = param.getDeclarationModel();
        String name = Util.getDefaultedParamMethodName(container.getDeclarationModel(), parameter );
        MethodDefinitionBuilder methodBuilder = MethodDefinitionBuilder.method(this, Decl.isAncestorLocal(param), true, name);
        
        int modifiers = abstract_ ? ABSTRACT : FINAL;
        if (container.getDeclarationModel().isShared()) {
            modifiers |= PUBLIC;
        } else if (!container.getDeclarationModel().isToplevel()){
            modifiers |= PRIVATE;
        }
        if (Decl.defaultParameterMethodStatic(container)) {
            modifiers |= STATIC;
        }
        methodBuilder.modifiers(modifiers);
        
        if (container instanceof Tree.AnyMethod) {
            if (((Tree.AnyMethod)container).getTypeParameterList() != null) {
                for (Tree.TypeParameterDeclaration t : ((Tree.AnyMethod)container).getTypeParameterList().getTypeParameterDeclarations()) {
                    methodBuilder.typeParameter(t);
                }
            }
        }
        
        /*if (!Decl.defaultParameterMethodOnSelf(container)) {
            ProducedType thisType = getThisType(container);
            methodBuilder.parameter(0, "$this", makeJavaType(thisType), null);
        }*/
        
        // Add any of the preceding parameters as parameters to the method
        for (Tree.Parameter p : params.getParameters()) {
            if (p == param) {
                break;
            }
            methodBuilder.parameter(p);
        }

        // The method's return type is the same as the parameter's type
        methodBuilder.resultType(parameter);

        // The implementation of the method
        if (!abstract_) {
            JCExpression expr = expressionGen().transform(param);
            JCBlock body = at(param).Block(0, List.<JCStatement> of(at(param).Return(expr)));
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
}
