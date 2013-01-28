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
import static com.sun.tools.javac.code.Flags.PUBLIC;
import static com.sun.tools.javac.code.Flags.STATIC;
import static com.sun.tools.javac.code.TypeTags.VOID;

import com.redhat.ceylon.compiler.typechecker.model.Annotation;
import com.redhat.ceylon.compiler.typechecker.model.FunctionalParameter;
import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.MethodOrValue;
import com.redhat.ceylon.compiler.typechecker.model.Parameter;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.ProducedTypedReference;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.TypeParameter;
import com.redhat.ceylon.compiler.typechecker.model.TypedDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.Value;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCAnnotation;
import com.sun.tools.javac.tree.JCTree.JCBlock;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import com.sun.tools.javac.tree.JCTree.JCTypeParameter;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Name;

/**
 * Builder for Java Methods. With special pre-definied builders
 * for normal methods, constructors, getters and setters.
 * 
 * @author Tako Schotanus
 */
public class MethodDefinitionBuilder {
    private final AbstractTransformer gen;
    
    private final String name;
    
    private long modifiers;

    private boolean isOverride;
    private boolean isAbstract;
    
    private JCExpression resultTypeExpr;
    private List<JCAnnotation> resultTypeAnnos;
    
    private final ListBuffer<JCAnnotation> annotations = ListBuffer.lb();
    
    private final ListBuffer<JCTypeParameter> typeParams = ListBuffer.lb();
    private final ListBuffer<JCExpression> typeParamAnnotations = ListBuffer.lb();
    
    private final ListBuffer<ParameterDefinitionBuilder> params = ListBuffer.lb();
    
    private ListBuffer<JCStatement> body = ListBuffer.lb();

    private boolean ignoreAnnotations;
    
    private boolean noAnnotations = false;
    
    private boolean built = false;

    public static MethodDefinitionBuilder method(AbstractTransformer gen, boolean isMember, String name) {
        return new MethodDefinitionBuilder(gen, false, isMember ? Naming.getErasedMethodName(name) : Naming.getMethodName(name));
    }
    
    public static MethodDefinitionBuilder method2(AbstractTransformer gen, String name) {
        return new MethodDefinitionBuilder(gen, false, name);
    }
    
    public static MethodDefinitionBuilder callable(AbstractTransformer gen) {
        return systemMethod(gen, Naming.getCallableMethodName());
    }
    
    public static MethodDefinitionBuilder systemMethod(AbstractTransformer gen, String name) {
        MethodDefinitionBuilder builder = new MethodDefinitionBuilder(gen, true, name);
        return builder;
    }
    
    public static MethodDefinitionBuilder constructor(AbstractTransformer gen) {
        return new MethodDefinitionBuilder(gen, false, null);
    }

    public static MethodDefinitionBuilder main(AbstractTransformer gen) {
        MethodDefinitionBuilder mdb = new MethodDefinitionBuilder(gen, false, "main")
            .modifiers(PUBLIC | STATIC);
        ParameterDefinitionBuilder pdb = ParameterDefinitionBuilder.instance(mdb.gen, "args");
        pdb.type(gen.make().TypeArray(gen.make().Type(gen.syms().stringType)), List.<JCAnnotation>nil());
        return mdb.parameter(pdb);
    }
    
    private MethodDefinitionBuilder(AbstractTransformer gen, boolean ignoreAnnotations, String name) {
        this.gen = gen;
        this.name = name;
        this.ignoreAnnotations = ignoreAnnotations;
        resultTypeExpr = makeVoidType();
    }
    
    private ListBuffer<JCAnnotation> getAnnotations() {
        ListBuffer<JCAnnotation> result = ListBuffer.lb();
        if (!noAnnotations) {
            if (!ignoreAnnotations) {
                result.appendList(this.annotations);   
            }
            if (isOverride) {
                result.appendList(gen.makeAtOverride());
            }
            if (ignoreAnnotations) {
                result.appendList(gen.makeAtIgnore());
            } else {
                if (resultTypeAnnos != null) {
                    result.appendList(resultTypeAnnos);
                }
                if(!typeParamAnnotations.isEmpty()) {
                    result.appendList(gen.makeAtTypeParameters(typeParamAnnotations.toList()));
                }
            }
        }else{
            // only those two are preserved
            if (isOverride) {
                result.appendList(gen.makeAtOverride());
            }
            if (ignoreAnnotations) {
                result.appendList(gen.makeAtIgnore());
            }
        }
        return result;
    }
    
    public JCTree.JCMethodDecl build() {
        if (built) {
            throw new IllegalStateException();
        }
        built = true;
        
        ListBuffer<JCVariableDecl> params = ListBuffer.lb();
        for (ParameterDefinitionBuilder pdb : this.params) {
            if (noAnnotations || ignoreAnnotations) {
                pdb.noAnnotations();
            }
            params.append(pdb.build());
        }

        return gen.make().MethodDef(
                gen.make().Modifiers(modifiers, getAnnotations().toList()), 
                makeName(name),
                resultTypeExpr,
                typeParams.toList(), 
                params.toList(),
                List.<JCExpression> nil(),
                makeBody(body),
                null);
    }

    private Name makeName(String name) {
        if (name != null) {
            return gen.names().fromString(name);
        } else {
            return gen.names().init;
        }
    }

    private JCBlock makeBody(ListBuffer<JCStatement> body2) {
        return (!isAbstract && (body != null) && ((modifiers & ABSTRACT) == 0)) ? gen.make().Block(0, body.toList()) : null;
    }

    JCExpression makeVoidType() {
        return gen.make().TypeIdent(VOID);
    }

    JCExpression makeResultType(TypedDeclaration typedDeclaration, ProducedType type, int flags) {
        if (typedDeclaration == null
                || (!(typedDeclaration instanceof FunctionalParameter)
                        && gen.isVoid(type))) {
            if ((typedDeclaration instanceof Method)
                    && ((Method)typedDeclaration).isDeclaredVoid()
                    && !Strategy.useBoxedVoid((Method)typedDeclaration)) {
                return makeVoidType();
            } else {
                return gen.makeJavaType(typedDeclaration, gen.typeFact().getAnythingDeclaration().getType(), flags);
            }
        } else {
            return gen.makeJavaType(typedDeclaration, type, flags);
        }
    }

    /*
     * Builder methods - they transform the inner state before doing the final construction
     */
    
    public MethodDefinitionBuilder modifiers(long... modifiers) {
        long mods = 0;
        for (long mod : modifiers) {
            mods |= mod;
        }
        this.modifiers = mods;
        return this;
    }

    /** 
     * The class will be generated with the {@code @Ignore} annotation only
     */
    public MethodDefinitionBuilder ignoreAnnotations() {
        ignoreAnnotations = true;
        return this;
    }
    
    public MethodDefinitionBuilder noAnnotations() {
        return noAnnotations(true);
    }
    
    public MethodDefinitionBuilder noAnnotations(boolean noAnnotations) {
        this.noAnnotations = noAnnotations;
        return this;
    }
    
    public MethodDefinitionBuilder annotations(List<JCTree.JCAnnotation> annotations) {
        this.annotations.appendList(annotations);
        return this;
    }

    public MethodDefinitionBuilder typeParameter(TypeParameter param) {
        return typeParameter(gen.makeTypeParameter(param), gen.makeAtTypeParameter(param));
    }
    
    private MethodDefinitionBuilder typeParameter(JCTypeParameter tp, JCAnnotation tpAnno) {
        typeParams.append(tp);
        typeParamAnnotations.append(tpAnno);
        return this;
    }

    public MethodDefinitionBuilder parameters(List<ParameterDefinitionBuilder> pdbs) {
        params.appendList(pdbs);
        return this;
    }
    
    public MethodDefinitionBuilder parameter(ParameterDefinitionBuilder pdb) {
        params.append(pdb);
        return this;
    }

    public MethodDefinitionBuilder parameter(long modifiers, String name, TypedDeclaration decl, TypedDeclaration nonWideningDecl, ProducedType nonWideningType, int flags) {
        return parameter(modifiers, name, name, decl, nonWideningDecl, nonWideningType, flags);
    }
    
    private MethodDefinitionBuilder parameter(long modifiers, String name, String aliasedName, TypedDeclaration decl, TypedDeclaration nonWideningDecl, ProducedType nonWideningType, int flags) {
        ParameterDefinitionBuilder pdb = ParameterDefinitionBuilder.instance(gen, name);
        pdb.modifiers(modifiers);
        pdb.aliasName(aliasedName);
        pdb.sequenced(decl instanceof Parameter && ((Parameter)decl).isSequenced());
        pdb.defaulted(decl instanceof Parameter && ((Parameter)decl).isDefaulted());
        pdb.type(paramType(nonWideningDecl, nonWideningType, flags), gen.makeJavaTypeAnnotations(decl));
        return parameter(pdb);
    }

    private JCExpression paramType(TypedDeclaration nonWideningDecl,
            ProducedType nonWideningType, int flags) {
        // keep in sync with gen.willEraseToBestBounds()
        if (gen.typeFact().isUnion(nonWideningType) 
                || gen.typeFact().isIntersection(nonWideningType)) {
            final TypeDeclaration refinedTypeDecl = ((TypedDeclaration)CodegenUtil.getTopmostRefinedDeclaration(nonWideningDecl)).getType().getDeclaration();
            if (refinedTypeDecl instanceof TypeParameter
                    && !refinedTypeDecl.getSatisfiedTypes().isEmpty()) {
                nonWideningType = refinedTypeDecl.getSatisfiedTypes().get(0);
            }
        }
        JCExpression type = gen.makeJavaType(nonWideningDecl, nonWideningType, flags);
        return type;
    }
    
    public MethodDefinitionBuilder parameter(Parameter paramDecl, ProducedType paramType, int mods, int flags) {
        String name = paramDecl.getName();
        return parameter(mods, name, paramDecl, paramDecl, paramType, flags);
    }
    
    public MethodDefinitionBuilder parameter(Parameter param, int flags) {
        String paramName = param.getName();
        String aliasedName = paramName;
        MethodOrValue mov = CodegenUtil.findMethodOrValueForParam(param);
        int mods = 0;
        if (!(mov instanceof Value) || !mov.isVariable() || mov.isCaptured()) {
            mods |= FINAL;
        }
        if (mov instanceof Method
                || mov instanceof Value && mov.isVariable() && mov.isCaptured()) {
            aliasedName = Naming.getAliasedParameterName(param);
        }
        TypedDeclaration nonWideningDecl;
        ProducedType nonWideningType;
        if (mov instanceof Value) {
            ProducedTypedReference typedRef = gen.getTypedReference(mov);
            ProducedTypedReference nonWideningTypedRef = gen.nonWideningTypeDecl(typedRef);
            nonWideningType = gen.nonWideningType(typedRef, nonWideningTypedRef);
            nonWideningDecl = nonWideningTypedRef.getDeclaration();
        }else{
            nonWideningType = param.getType();
            nonWideningDecl = param;
        }
        return parameter(mods, paramName, aliasedName, param, nonWideningDecl, nonWideningType, flags);
    }

    public MethodDefinitionBuilder isOverride(boolean isOverride) {
        this.isOverride = isOverride;
        return this;
    }
    
    public MethodDefinitionBuilder isAbstract(boolean isAbstract) {
        this.isAbstract = isAbstract;
        return this;
    }
    
    public MethodDefinitionBuilder body(JCStatement statement) {
        if (statement != null) {
            this.body.append(statement);
        }
        return this;
    }
    
    public MethodDefinitionBuilder body(List<JCStatement> body) {
        if (body != null) {
            this.body.appendList(body);
        }
        return this;
    }

    MethodDefinitionBuilder noBody() {
        this.body = null;
        return this;
    }

    public MethodDefinitionBuilder block(JCBlock block) {
        if (block != null) {
            body.clear();
            return body(block.getStatements());
        } else {
            return noBody();
        }
    }

    public MethodDefinitionBuilder resultType(Method method, int flags) {
        ProducedTypedReference typedRef = gen.getTypedReference(method);
        ProducedTypedReference nonWideningTypedRef = gen.nonWideningTypeDecl(typedRef);
        ProducedType nonWideningType = gen.nonWideningType(typedRef, nonWideningTypedRef);
        return resultType(makeResultType(nonWideningTypedRef.getDeclaration(), nonWideningType, flags), method);
    }
    
    public MethodDefinitionBuilder resultTypeNonWidening(ProducedTypedReference typedRef, ProducedType returnType, int flags){
        ProducedTypedReference nonWideningTypedRef = gen.nonWideningTypeDecl(typedRef);
        returnType = gen.nonWideningType(typedRef, nonWideningTypedRef);
        return resultType(makeResultType(nonWideningTypedRef.getDeclaration(), returnType, flags), typedRef.getDeclaration());

    }
    
    public MethodDefinitionBuilder resultType(TypedDeclaration resultType, ProducedType type, int flags) {
        return resultType(makeResultType(resultType, type, flags), resultType);
    }

    public MethodDefinitionBuilder resultType(JCExpression resultType, TypedDeclaration typeDecl) {
        return resultType(gen.makeJavaTypeAnnotations(typeDecl), resultType);
    }
    
    public MethodDefinitionBuilder resultType(List<JCAnnotation> resultTypeAnnos, JCExpression resultType) {
        this.resultTypeAnnos = resultTypeAnnos;
        this.resultTypeExpr = resultType;
        return this;
    }

    public MethodDefinitionBuilder modelAnnotations(java.util.List<Annotation> annotations) {
        annotations(gen.makeAtAnnotations(annotations));
        return this;
    }
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getAnnotations()).append(' ');
        sb.append(Flags.toString(this.modifiers)).append(' ');
        sb.append(resultTypeExpr).append(' ');
        sb.append(name).append('(');
        int i = 0;
        for (ParameterDefinitionBuilder param : params) {
            sb.append(param);
            if (i < params.count -1) {
                sb.append(',');
            }
        }
        sb.append(')');
        return sb.toString();
    }

    
}
