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

import static com.sun.tools.javac.code.Flags.FINAL;
import static com.sun.tools.javac.code.Flags.INTERFACE;
import static com.sun.tools.javac.code.Flags.PRIVATE;
import static com.sun.tools.javac.code.Flags.PROTECTED;
import static com.sun.tools.javac.code.Flags.PUBLIC;

import com.redhat.ceylon.compiler.java.util.Util;
import com.redhat.ceylon.compiler.typechecker.model.Annotation;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Parameter;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.TypeParameter;
import com.redhat.ceylon.compiler.typechecker.model.TypedDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.Value;
import com.redhat.ceylon.compiler.typechecker.model.ValueParameter;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCAnnotation;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import com.sun.tools.javac.tree.JCTree.JCTypeParameter;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Name;

/**
 * Builder for Java Classes. The specific properties of the "framework" of the
 * class like its name, superclass, interfaces etc can be set directly.
 * There are also three freely definable "zones" where any code can be inserted:
 * the "defs" that go at the top of the class body, the "body" that goes at
 * the bottom and the "init" the goes inside the constructor in the middle.
 * (the reason for these 3 zones is mostly historical, 2 would do just as well)
 * 
 * @author Tako Schotanus
 */
public class ClassDefinitionBuilder {
    private final AbstractTransformer gen;
    
    private final String name;
    
    private long modifiers;
    private long constructorModifiers = -1;
    
    private JCExpression extending;
    private JCStatement superCall;
    
    private final ListBuffer<JCExpression> satisfies = ListBuffer.lb();
    private final ListBuffer<JCExpression> caseTypes = ListBuffer.lb();
    private final ListBuffer<JCTypeParameter> typeParams = ListBuffer.lb();
    private final ListBuffer<JCExpression> typeParamAnnotations = ListBuffer.lb();
    
    private final ListBuffer<JCAnnotation> annotations = ListBuffer.lb();
    
    private final ListBuffer<JCVariableDecl> params = ListBuffer.lb();
    
    private final ListBuffer<MethodDefinitionBuilder> constructors = ListBuffer.lb();
    private final ListBuffer<JCTree> defs = ListBuffer.lb();
    private ClassDefinitionBuilder concreteInterfaceMemberDefs;
    private final ListBuffer<JCTree> body = ListBuffer.lb();
    private final ListBuffer<JCTree> also = ListBuffer.lb();
    private final ListBuffer<JCStatement> init = ListBuffer.lb();

    private boolean ancestorLocal;
    
    private boolean built = false;
    
    private boolean isCompanion = false;

    private ClassDefinitionBuilder containingClassBuilder;

    public static ClassDefinitionBuilder klass(AbstractTransformer gen, boolean ancestorLocal, String name) {
        ClassDefinitionBuilder builder = new ClassDefinitionBuilder(gen, ancestorLocal, name);
        builder.containingClassBuilder = gen.current();
        gen.replace(builder);
        return builder;
    }
    
    public static ClassDefinitionBuilder methodWrapper(AbstractTransformer gen, boolean ancestorLocal, String name, boolean shared) {
        final ClassDefinitionBuilder builder = new ClassDefinitionBuilder(gen, ancestorLocal, name);
        builder.containingClassBuilder = gen.current();
        gen.replace(builder);
        return builder
            .annotations(gen.makeAtMethod())
            .modifiers(FINAL, shared ? PUBLIC : 0)
            .constructorModifiers(PRIVATE);
    }

    private ClassDefinitionBuilder(AbstractTransformer gen, boolean ancestorLocal, String name) {
        this.gen = gen;
        this.name = name;
        this.ancestorLocal = ancestorLocal;
        
        extending = getSuperclass(null);
        annotations(gen.makeAtCeylon());
        if (ancestorLocal) {
            this.annotations.appendList(gen.makeAtIgnore());
        }
    }

    private ClassDefinitionBuilder getTopLevelBuilder() {
        ClassDefinitionBuilder result = this;
        while (result.containingClassBuilder != null) {
            result = result.containingClassBuilder;
        }
        return result;
    }
    
    public List<JCTree> build() {
        if (built) {
            throw new IllegalStateException();
        }
        built = true;
        ListBuffer<JCTree> defs = ListBuffer.lb();
        appendDefinitionsTo(defs);
        if (!typeParamAnnotations.isEmpty()) {
            annotations(gen.makeAtTypeParameters(typeParamAnnotations.toList()));
        }
        
        JCTree.JCClassDecl klass = gen.make().ClassDef(
                gen.make().Modifiers(modifiers, gen.filterAnnotations(annotations)),
                gen.names().fromString(Util.quoteIfJavaKeyword(name)),
                typeParams.toList(),
                extending,
                satisfies.toList(),
                defs.toList());
        ListBuffer<JCTree> klasses = ListBuffer.<JCTree>lb();
        
        // Generate a companion class if we're building an interface
        // or the companion actually has some content 
        // (e.g. initializer with defaulted params)
        
        
        if ((modifiers & INTERFACE) != 0) {
            if (this == getTopLevelBuilder()) {
                klasses.appendList(also.toList());
                klasses.append(klass);
                if (hasCompanion()) {
                    klasses.appendList(concreteInterfaceMemberDefs.build());
                }
            } else {
                if (hasCompanion()) {
                    klasses.appendList(concreteInterfaceMemberDefs.build());
                }
                getTopLevelBuilder().also(klass);
            }
        } else {
            klasses.appendList(also.toList());
            if (hasCompanion()) {
                klasses.appendList(concreteInterfaceMemberDefs.build());
            }
            klasses.append(klass);
        }
        
        gen.replace(containingClassBuilder);
        
        return klasses.toList();
    }

    private boolean hasCompanion() {
        return concreteInterfaceMemberDefs != null
                && (((modifiers & INTERFACE) != 0)
                    || !(concreteInterfaceMemberDefs.defs.isEmpty()
                    && concreteInterfaceMemberDefs.init.isEmpty()
                    && concreteInterfaceMemberDefs.body.isEmpty()
                    && concreteInterfaceMemberDefs.constructors.isEmpty()));
    }

    private void also(JCTree also) {
        this.also.append(also);
    }

    private void appendDefinitionsTo(ListBuffer<JCTree> defs) {
        defs.appendList(this.defs);
        if ((modifiers & INTERFACE) == 0) {
            if (superCall != null) {
                init.prepend(superCall);
            }
            if (!isCompanion) {
                createConstructor(init.toList());
            }
            for (MethodDefinitionBuilder builder : constructors) {
                defs.append(builder.build());
            }
        }
        defs.appendList(body);
    }

    private JCExpression getSuperclass(ProducedType extendedType) {
        JCExpression superclass;
        if (extendedType != null) {
            superclass = gen.makeJavaType(extendedType, CeylonTransformer.EXTENDS);
            // simplify if we can
// FIXME superclass.sym can be null
//            if (superclass instanceof JCTree.JCFieldAccess 
//            && ((JCTree.JCFieldAccess)superclass).sym.type == gen.syms.objectType) {
//                superclass = null;
//            }
        } else {
            if ((modifiers & INTERFACE) != 0) {
                // The VM insists that interfaces have java.lang.Object as their superclass
                superclass = gen.makeIdent(gen.syms().objectType);
            } else {
                superclass = null;
            }
        }
        return superclass;
    }

    private List<JCExpression> transformTypesList(java.util.List<ProducedType> types) {
        if (types == null) {
            return List.nil();
        }
        ListBuffer<JCExpression> typesList = new ListBuffer<JCExpression>();
        for (ProducedType t : types) {
            JCExpression jt = gen.makeJavaType(t, CeylonTransformer.SATISFIES);
            if (jt != null) {
                typesList.append(jt);
            }
        }
        return typesList.toList();
    }

    private ClassDefinitionBuilder createConstructor(List<JCStatement> body) {
        long mods = constructorModifiers;
        if (mods == -1) {
            // The modifiers were never explicitly set
            // so we try to come up with some good defaults
            mods = modifiers & (PUBLIC | PRIVATE | PROTECTED);
        }
        addConstructor().modifiers(mods)
            .parameters(params.toList())
            .body(body);
        return this;
    }
    
    public MethodDefinitionBuilder addConstructor() {
        MethodDefinitionBuilder constructor = MethodDefinitionBuilder.constructor(gen, ancestorLocal);
        this.constructors.append(constructor);
        return constructor;
    }
    
    /*
     * Builder methods - they transform the inner state before doing the final construction
     */
    
    public ClassDefinitionBuilder modifiers(long... modifiers) {
        long mods = 0;
        for (long mod : modifiers) {
            mods |= mod;
        }
        this.modifiers = mods;
        if (this.concreteInterfaceMemberDefs != null) {
            this.concreteInterfaceMemberDefs.modifiers((mods & PUBLIC) | FINAL);
        }
        return this;
    }

    public ClassDefinitionBuilder constructorModifiers(long... constructorModifiers) {
        long mods = 0;
        for (long mod : constructorModifiers) {
            mods |= mod;
        }
        this.constructorModifiers = mods;
        return this;
    }

    public ClassDefinitionBuilder typeParameter(String name, java.util.List<ProducedType> satisfiedTypes, boolean covariant, boolean contravariant) {
        ListBuffer<JCExpression> bounds = new ListBuffer<JCExpression>();
        for (ProducedType t : satisfiedTypes) {
            if (!gen.willEraseToObject(t)) {
                bounds.append(gen.makeJavaType(t, AbstractTransformer.NO_PRIMITIVES));
            }
        }
        typeParams.append(typeParam(name, bounds));
        typeParamAnnotations.append(gen.makeAtTypeParameter(name, satisfiedTypes, covariant, contravariant));
        return this;
    }

    private JCTypeParameter typeParam(String name,
            ListBuffer<JCExpression> bounds) {
        return gen.make().TypeParameter(gen.names().fromString(name),
                bounds.toList());
    }

    public ClassDefinitionBuilder typeParameter(TypeParameter declarationModel) {
        return typeParameter(declarationModel.getName(), 
                declarationModel.getSatisfiedTypes(),
                declarationModel.isCovariant(),
                declarationModel.isContravariant());
    }
    
    public ClassDefinitionBuilder typeParameter(Tree.TypeParameterDeclaration param) {
        gen.at(param);
        return typeParameter(param.getDeclarationModel());
    }

    public ClassDefinitionBuilder extending(ProducedType extendingType) {
        this.extending = getSuperclass(extendingType);
        annotations(gen.makeAtClass(extendingType));
        return this;
    }

    public ClassDefinitionBuilder extending(Tree.ExtendedType extendedType) {
        if (extendedType.getInvocationExpression().getPositionalArgumentList() != null) {
            InvocationBuilder builder = InvocationBuilder.forSuperInvocation(gen, extendedType.getInvocationExpression());
            superCall = gen.at(extendedType).Exec(builder.build());
        }
        return extending(extendedType.getType().getTypeModel());
    }
    
    public ClassDefinitionBuilder satisfies(java.util.List<ProducedType> satisfies) {
        this.satisfies.addAll(transformTypesList(satisfies));
        //this.defs.addAll(appendConcreteInterfaceMembers(satisfies));
        annotations(gen.makeAtSatisfiedTypes(satisfies));
        return this;
    }

    public ClassDefinitionBuilder caseTypes(java.util.List<ProducedType> caseTypes) {
        if (caseTypes != null) {
            this.caseTypes.addAll(transformTypesList(caseTypes));
            annotations(gen.makeAtCaseTypes(caseTypes));
        }
        return this;
    }

    public ClassDefinitionBuilder of(ProducedType ofType) {
        if (ofType != null) {
            annotations(gen.makeAtCaseTypes(ofType));
        }
        return this;
    }

    public ClassDefinitionBuilder annotations(List<JCTree.JCAnnotation> annotations) {
        if (ancestorLocal) {
            return this;
        }
        this.annotations.appendList(annotations);
        return this;
    }

    // Create a parameter for the constructor
    private ClassDefinitionBuilder parameter(String name, Parameter decl, boolean isSequenced, boolean isDefaulted) {
        JCExpression type;
        Value attr = CodegenUtil.findAttrForParam(decl);
        if (attr != null) {
            TypedDeclaration nonWideningTypeDeclaration = gen.nonWideningTypeDecl(attr);
            ProducedType paramType = gen.nonWideningType(attr, nonWideningTypeDeclaration);
            type = gen.makeJavaType(nonWideningTypeDeclaration, paramType, 0);
        } else {
            ProducedType paramType = decl.getType();
            type = gen.makeJavaType(decl, paramType, 0);
        }
        
        List<JCAnnotation> annots = List.nil();
        if (gen.needsAnnotations(decl)) {
            annots = annots.appendList(gen.makeAtName(name));
            if (isSequenced) {
                annots = annots.appendList(gen.makeAtSequenced());
            }
            if (isDefaulted) {
                annots = annots.appendList(gen.makeAtDefaulted());
            }
            annots = annots.appendList(gen.makeJavaTypeAnnotations(decl));
        }
        JCVariableDecl var = gen.make().VarDef(gen.make().Modifiers(0, annots), gen.names().fromString(name), type, null);
        params.append(var);

        if (decl.isCaptured()) {
            JCVariableDecl localVar = gen.make().VarDef(gen.make().Modifiers(FINAL | PRIVATE), gen.names().fromString(name), type , null);
            defs.append(localVar);
            init.append(gen.make().Exec(gen.make().Assign(gen.makeSelect("this", name), gen.makeUnquotedIdent(name))));
        } else if ((decl instanceof ValueParameter) 
                        && ((ValueParameter)decl).isHidden()
                        && (decl.getContainer() instanceof TypeDeclaration)) {
            Declaration member = ((TypeDeclaration)decl.getContainer()).getMember(decl.getName(), null);
            if (member instanceof Value 
                    && Strategy.createField((ValueParameter)decl, (Value)member)) {
                // The field itself is created by the ClassTransformer
                init.append(gen.make().Exec(gen.make().Assign(gen.makeSelect("this", name), gen.makeUnquotedIdent(name))));
            }
        }
        return this;
    }
    
    public ClassDefinitionBuilder parameter(Tree.Parameter param) {
        gen.at(param);
        return parameter(param.getDeclarationModel());
    }
    
    public ClassDefinitionBuilder parameter(Parameter param) {
        String name = param.getName();
        return parameter(name, param, param.isSequenced(), param.isDefaulted());
    }
    
    public ClassDefinitionBuilder defs(JCTree statement) {
        if (statement != null) {
            this.defs.append(statement);
        }
        return this;
    }
    
    public ClassDefinitionBuilder defs(List<JCTree> defs) {
        if (defs != null) {
            this.defs.appendList(defs);
        }
        return this;
    }
    
    public ClassDefinitionBuilder body(JCTree statement) {
        if (statement != null) {
            this.body.append(statement);
        }
        return this;
    }
    
    public ClassDefinitionBuilder body(List<JCTree> body) {
        if (body != null) {
            this.body.appendList(body);
        }
        return this;
    }
    
    public ClassDefinitionBuilder init(JCStatement statement) {
        if (statement != null) {
            this.init.append(statement);
        }
        return this;
    }
    
    public ClassDefinitionBuilder init(List<JCStatement> init) {
        if (init != null) {
            this.init.appendList(init);
        }
        return this;
    }

    public ClassDefinitionBuilder getCompanionBuilder(Declaration decl) {
        if (concreteInterfaceMemberDefs == null) {
            concreteInterfaceMemberDefs = new ClassDefinitionBuilder(gen, ancestorLocal, gen.getCompanionClassName(decl).replaceFirst(".*\\.", ""))
                .annotations(gen.makeAtIgnore());
            concreteInterfaceMemberDefs.isCompanion = true;
        }
        return concreteInterfaceMemberDefs;
    }

    public ClassDefinitionBuilder field(int modifiers, String attrName, JCExpression type, JCExpression initialValue, boolean isLocal) {
        Name attrNameNm = gen.names().fromString(attrName);
        if (!isLocal) {
            // A shared or captured attribute gets turned into a class member
            defs(gen.make().VarDef(gen.make().Modifiers(modifiers, List.<JCTree.JCAnnotation>nil()), attrNameNm, type, null));
            if (initialValue != null) {
                // The attribute's initializer gets moved to the constructor
                // because it might be using locals of the initializer
                init(gen.make().Exec(gen.make().Assign(gen.makeSelect("this", attrName), initialValue)));
            }
        } else {
            // Otherwise it's local to the constructor
            init(gen.make().VarDef(gen.make().Modifiers(modifiers, List.<JCTree.JCAnnotation>nil()), attrNameNm, type, initialValue));
        }
        return this;
    }

    public ClassDefinitionBuilder method(Tree.AnyMethod method) {
        defs(gen.classGen().transform(method, this));
        return this;
    }

    public ClassDefinitionBuilder modelAnnotations(java.util.List<Annotation> annotations) {
        annotations(gen.makeAtAnnotations(annotations));
        return this;
    }
}
