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

import java.util.Collections;

import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.model.typechecker.model.Annotation;
import com.redhat.ceylon.model.typechecker.model.Declaration;
import com.redhat.ceylon.model.typechecker.model.Function;
import com.redhat.ceylon.model.typechecker.model.FunctionOrValue;
import com.redhat.ceylon.model.typechecker.model.Package;
import com.redhat.ceylon.model.typechecker.model.Parameter;
import com.redhat.ceylon.model.typechecker.model.ParameterList;
import com.redhat.ceylon.model.typechecker.model.Type;
import com.redhat.ceylon.model.typechecker.model.TypedReference;
import com.redhat.ceylon.model.typechecker.model.Scope;
import com.redhat.ceylon.model.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.model.typechecker.model.TypeParameter;
import com.redhat.ceylon.model.typechecker.model.TypedDeclaration;
import com.redhat.ceylon.model.typechecker.model.Value;
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
public class MethodDefinitionBuilder 
        implements ParameterizedBuilder<MethodDefinitionBuilder> {
    private final AbstractTransformer gen;
    
    private final String name;
    private String realName;
    
    private long modifiers;

    private boolean isOverride;
    private boolean isAbstract;
    private boolean isTransient;
    
    private JCExpression resultTypeExpr;
    private List<JCAnnotation> resultTypeAnnos;
    
    private final ListBuffer<JCAnnotation> userAnnotations = ListBuffer.lb();
    private final ListBuffer<JCAnnotation> modelAnnotations = ListBuffer.lb();
    
    private final ListBuffer<JCTypeParameter> typeParams = ListBuffer.lb();
    private final ListBuffer<JCExpression> typeParamAnnotations = ListBuffer.lb();
    
    private final ListBuffer<ParameterDefinitionBuilder> params = ListBuffer.lb();
    
    private ListBuffer<JCStatement> body = ListBuffer.lb();

    private int annotationFlags = Annotations.MODEL_AND_USER;
    
    private boolean built = false;

    private JCExpression defaultValue;

    private boolean haveLocation = false;
    private Node location;

    public static MethodDefinitionBuilder method(AbstractTransformer gen, Function method) {
        return new MethodDefinitionBuilder(gen, false, gen.naming.selector(method));
    }
    
    public static MethodDefinitionBuilder method(AbstractTransformer gen, TypedDeclaration decl, int namingOptions) {
        return new MethodDefinitionBuilder(gen, false, Naming.selector(decl, namingOptions));
    }
    
    public static MethodDefinitionBuilder getter(AbstractTransformer gen, TypedDeclaration attr, boolean indirect) {
        MethodDefinitionBuilder mdb = new MethodDefinitionBuilder(gen, false, Naming.getGetterName(attr, indirect));
        if (Naming.isAmbiguousGetterName(attr)) {
            mdb.realName(attr.getName());
        }
        return mdb;
    }
    
    public static MethodDefinitionBuilder setter(AbstractTransformer gen, TypedDeclaration attr) {
        MethodDefinitionBuilder mdb = new MethodDefinitionBuilder(gen, false, Naming.getSetterName(attr));
        if (Naming.isAmbiguousGetterName(attr)) {
            mdb.realName(attr.getName());
        }
        return mdb;
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
        ParameterDefinitionBuilder pdb = ParameterDefinitionBuilder.systemParameter(mdb.gen, "args");
        pdb.type(gen.make().TypeArray(gen.make().Type(gen.syms().stringType)), List.<JCAnnotation>nil());
        return mdb.parameter(pdb);
    }
    
    private MethodDefinitionBuilder(AbstractTransformer gen, boolean ignoreAnnotations, String name) {
        this.gen = gen;
        this.name = name;
        if (ignoreAnnotations) {
            this.annotationFlags = Annotations.ignore(this.annotationFlags);
        }
        resultTypeExpr = makeVoidType();
    }
    
    public MethodDefinitionBuilder realName(String realName) {
        this.realName = realName;
        return this;
    }
    
    private ListBuffer<JCAnnotation> getAnnotations() {
        ListBuffer<JCAnnotation> result = ListBuffer.lb();
        if (Annotations.includeUser(this.annotationFlags)) {
            result.appendList(this.userAnnotations);
        }
        if (Annotations.includeModel(this.annotationFlags)) {
            result.appendList(this.modelAnnotations);   
        }
        if (isOverride) {
            result.appendList(gen.makeAtOverride());
        }
        if (Annotations.includeIgnore(this.annotationFlags)) {
            result.appendList(gen.makeAtIgnore());
        }
        if (Annotations.includeModel(this.annotationFlags)) {
            if (resultTypeAnnos != null) {
                result.appendList(resultTypeAnnos);
            }
            if(!typeParamAnnotations.isEmpty()) {
                result.appendList(gen.makeAtTypeParameters(typeParamAnnotations.toList()));
            }
            if (isTransient) {
                result.appendList(gen.makeAtTransient());
            }
            if(realName != null){
                result.appendList(gen.makeAtName(realName));
            }
        }
        return result;
    }
    
    public MethodDefinitionBuilder location(Node at) {
        this.haveLocation = true;
        this.location = at;
        return this;
    }
    
    public JCTree.JCMethodDecl build() {
        if (built) {
            throw new BugException("already built");
        }
        built = true;
        if (haveLocation) {
            gen.at(location);
        }
        ListBuffer<JCVariableDecl> params = ListBuffer.lb();
        for (ParameterDefinitionBuilder pdb : this.params) {
            if (!Annotations.includeModel(this.annotationFlags)) {
                pdb.noModelAnnotations();
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
                defaultValue);
    }

    private Name makeName(String name) {
        if (name != null) {
            return gen.names().fromString(Naming.quoteMethodName(name));
        } else {
            return gen.names().init;
        }
    }

    private JCBlock makeBody(ListBuffer<JCStatement> body) {
        for (ParameterDefinitionBuilder pdb : params) {
            if (pdb.requiresBoxedVariableDecl()) {
                body.prepend(pdb.buildBoxedVariableDecl());
            }
        }
        return (!isAbstract && (body != null) && ((modifiers & ABSTRACT) == 0)) ? gen.make().Block(0, body.toList()) : null;
    }

    JCExpression makeVoidType() {
        return gen.make().TypeIdent(VOID);
    }

    JCExpression makeResultType(TypedDeclaration typedDeclaration, Type type, int flags) {
        if (typedDeclaration == null
                || ((!(typedDeclaration instanceof Function) || !((Function)typedDeclaration).isParameter())
                        && AbstractTransformer.isAnything(type))) {
            if ((typedDeclaration instanceof Function)
                    && ((Function)typedDeclaration).isDeclaredVoid()
                    && !Strategy.useBoxedVoid((Function)typedDeclaration)) {
                return makeVoidType();
            } else {
                return gen.makeJavaType(typedDeclaration, gen.typeFact().getAnythingType(), flags);
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
     * {@code @Ignore} and no model annotations.
     */
    public MethodDefinitionBuilder ignoreModelAnnotations() {
        this.annotationFlags = Annotations.ignore(annotationFlags);
        return this;
    }
    
    /** No model annotations, but possibly still {@link @Ignore} and user annotations. */
    public MethodDefinitionBuilder noModelAnnotations() {
        this.annotationFlags = Annotations.noModel(annotationFlags);
        return this;
    }
    
    /** No annotations at all (including {@code @Ignore}). */
    public MethodDefinitionBuilder noAnnotations() {
        this.annotationFlags = 0;
        return this;
    }
    
    public MethodDefinitionBuilder annotationFlags(int annotationFlags) {
        this.annotationFlags = annotationFlags;
        return this;
    }
    
    public MethodDefinitionBuilder modelAnnotations(List<JCTree.JCAnnotation> modelAnnotations) {
        this.modelAnnotations.appendList(modelAnnotations);
        return this;
    }
    
    public MethodDefinitionBuilder userAnnotations(List<JCTree.JCAnnotation> annotations) {
        this.userAnnotations.appendList(annotations);
        return this;
    }

    public MethodDefinitionBuilder typeParameter(TypeParameter param, java.util.List<Type> producedBounds) {
        return typeParameter(gen.makeTypeParameter(param, producedBounds), gen.makeAtTypeParameter(param));
    }
    
    public MethodDefinitionBuilder typeParameter(TypeParameter param) {
        return typeParameter(param, null);
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

    public MethodDefinitionBuilder parameter(long modifiers, 
            java.util.List<Annotation> annos, 
            String name, 
            Parameter decl, 
            TypedDeclaration nonWideningDecl, 
            Type nonWideningType, 
            int flags, boolean canWiden) {
        return parameter(modifiers, annos, null, name, name, decl, nonWideningDecl, nonWideningType, flags);
    }
    
    private MethodDefinitionBuilder parameter(long modifiers, 
            java.util.List<Annotation> modelAnnotations, List<JCAnnotation> userAnnotations,
            String name, String aliasedName, 
            Parameter decl, TypedDeclaration nonWideningDecl, Type nonWideningType, 
            int flags) {
        ParameterDefinitionBuilder pdb = ParameterDefinitionBuilder.explicitParameter(gen, decl);
        pdb.modifiers(modifiers);
        pdb.modelAnnotations(modelAnnotations);
        pdb.userAnnotations(userAnnotations);
        pdb.aliasName(aliasedName);
        pdb.sequenced(decl.isSequenced());
        pdb.defaulted(decl.isDefaulted());
        if (isParamTypeLocalToMethod(decl,
                nonWideningType)) {
            pdb.type(gen.make().Type(gen.syms().objectType), gen.makeJavaTypeAnnotations(decl.getModel()));
        } else {
            pdb.type(paramType(gen, nonWideningDecl, nonWideningType, flags), gen.makeJavaTypeAnnotations(decl.getModel()));
        }
        return parameter(pdb);
    }

    private boolean isParamTypeLocalToMethod(Parameter parameter,
            Type nonWideningType) {
        // error recovery
        if(nonWideningType == null)
            return false;
        if (parameter.getModel().getTypeErased()) {
            return false;
        }
        Declaration method = parameter.getDeclaration();
        TypeDeclaration paramTypeDecl = nonWideningType.getDeclaration();
        if (paramTypeDecl instanceof TypeParameter
                && Decl.equalScopeDecl(paramTypeDecl.getContainer(), method)) {
            return false;
        }
        Scope scope = paramTypeDecl.getContainer();
        while (scope != null && !(scope instanceof Package)) {
            if (Decl.equalScopeDecl(scope, method)) {
                return true;
            }
            scope = scope.getContainer();
        }
        return false;
    }

    static JCExpression paramType(AbstractTransformer gen, TypedDeclaration nonWideningDecl,
            Type nonWideningType, int flags) {
        return gen.makeJavaType(nonWideningDecl, nonWideningType, flags);
    }
    
    public MethodDefinitionBuilder parameter(Parameter paramDecl, 
            Type paramType, int mods, int flags, boolean canWiden) {
        String name = paramDecl.getName();
        return parameter(mods, paramDecl.getModel().getAnnotations(), 
                name, paramDecl, paramDecl.getModel(), paramType, flags, canWiden);
    }
    
    static class NonWideningParam {
        public final int flags;
        public final Type nonWideningType;
        public final TypedDeclaration nonWideningDecl;
        NonWideningParam(int mods, Type nonWideningType, TypedDeclaration nonWideningDecl){
            this.flags = mods;
            this.nonWideningType = nonWideningType;
            this.nonWideningDecl = nonWideningDecl;
        }
    }
    
    enum WideningRules {
        // Stef: let me be very clear that I've no idea what those two are for
        NONE, CAN_WIDEN, 
        // this is so we get widening code enabled for mixin bridge methods
        FOR_MIXIN;
    }

    public MethodDefinitionBuilder parameter(Parameter param,  
            List<JCAnnotation> userAnnotations, int flags, WideningRules wideningRules) {
        return parameter(param, null, userAnnotations, flags, wideningRules);
    }
    
    public MethodDefinitionBuilder parameter(Parameter param, TypedReference typedRef, 
            List<JCAnnotation> userAnnotations, int flags, WideningRules wideningRules) {
        String paramName = param.getName();
        String aliasedName = Naming.getAliasedParameterName(param);
        FunctionOrValue mov = CodegenUtil.findMethodOrValueForParam(param);
        if(typedRef == null)
            typedRef = gen.getTypedReference(mov);
        int mods = 0;
        if (!Decl.isNonTransientValue(mov) || !mov.isVariable() || mov.isCaptured()) {
            mods |= FINAL;
        }
        NonWideningParam nonWideningParam = getNonWideningParam(typedRef, wideningRules);
        flags |= nonWideningParam.flags;
        return parameter(mods, param.getModel().getAnnotations(), userAnnotations, paramName, aliasedName, param, 
                nonWideningParam.nonWideningDecl, nonWideningParam.nonWideningType, flags);
    }

    public NonWideningParam getNonWideningParam(FunctionOrValue mov, 
            WideningRules wideningRules) {
        return getNonWideningParam(gen.getTypedReference(mov), wideningRules);
    }
    
    public NonWideningParam getNonWideningParam(TypedReference typedRef, 
            WideningRules wideningRules) {
        TypedDeclaration nonWideningDecl = null;
        int flags = 0;
        Type nonWideningType;
        FunctionOrValue mov = (FunctionOrValue) typedRef.getDeclaration();
        if (Decl.isValue(mov)) {
            TypedReference nonWideningTypedRef = gen.nonWideningTypeDecl(typedRef);
            nonWideningType = gen.nonWideningType(typedRef, nonWideningTypedRef);
            nonWideningDecl = nonWideningTypedRef.getDeclaration();
        }else{
            // Stef: So here's the thing. I know this is wrong for Function where we should do getFullType(), BUT
            // lots of methods call this and then feed the output into AT.makeJavaType(TypedDeclaration typeDecl, Type type, int flags)
            // which adds the Callable type, so if we fix it here we have to remove it from there and there's lots of callers of that
            // function which rely on its behaviour and frankly I've had enough of this refactoring, so a few callers of this function
            // have to add the Callable back. It sucks, yeah, but so far it works, which is amazing enough that I don't want to touch it
            // any more. More ambitious/courageous people are welcome to fix this properly.
            nonWideningType = typedRef.getType();
            nonWideningDecl = mov;
        }
        if(!CodegenUtil.isUnBoxed(nonWideningDecl))
            flags |= AbstractTransformer.JT_NO_PRIMITIVES;
        
        // make sure we don't accidentally narrow value parameters that would be erased in the topmost declaration
        if(wideningRules != WideningRules.NONE
                && mov instanceof Value){
            TypedDeclaration refinedParameter = (TypedDeclaration)CodegenUtil.getTopmostRefinedDeclaration(mov);
            // mixin bridge methods have the same rules as when refining stuff except they are their own refined decl
            if(wideningRules == WideningRules.FOR_MIXIN || !Decl.equal(refinedParameter, mov)){
                Type refinedParameterType;
                // we don't have to use produced typed references with type params applied here because we want to know the
                // erasure status of the compilation of the refined parameter, so it's OK if we end up with unbound type parameters
                // in the refined parameter type
                if(refinedParameter instanceof Function)
                    refinedParameterType = refinedParameter.appliedTypedReference(null, Collections.<Type>emptyList()).getFullType();
                else
                    refinedParameterType = refinedParameter.getType();
                // if the supertype method itself got erased to Object, we can't do better than this
                if(gen.willEraseToObject(refinedParameterType) && !gen.willEraseToBestBounds(mov))
                    nonWideningType = gen.typeFact().getObjectType();
                else if (CodegenUtil.isRaw(refinedParameter)) {
                    flags |= AbstractTransformer.JT_RAW;
                } else {
                    flags |= AbstractTransformer.JT_NARROWED;
                }
            }
        }
        // keep in sync with gen.willEraseToBestBounds()
        if (wideningRules != WideningRules.NONE
                && (gen.typeFact().isUnion(nonWideningType) 
                        || gen.typeFact().isIntersection(nonWideningType))) {
            final Type refinedType = ((TypedDeclaration)CodegenUtil.getTopmostRefinedDeclaration(nonWideningDecl)).getType();
            if (refinedType.isTypeParameter()
                    && !refinedType.getSatisfiedTypes().isEmpty()) {
                nonWideningType = refinedType.getSatisfiedTypes().get(0);
                // Could be parameterized, and type param won't be in scope, so have to go raw
                flags |= AbstractTransformer.JT_RAW;
            }
        }
        // this is to be done on the parameter's containing method, to see if that method must have raw parameters
        if (mov.isParameter()
                && mov.getContainer() instanceof Declaration
                && gen.rawParameters((Declaration) mov.getContainer())) {
            flags |= AbstractTransformer.JT_RAW;
        }
        return new NonWideningParam(flags, nonWideningType, nonWideningDecl);
    }

    public MethodDefinitionBuilder isOverride(boolean isOverride) {
        this.isOverride = isOverride;
        return this;
    }
    
    public MethodDefinitionBuilder isAbstract(boolean isAbstract) {
        this.isAbstract = isAbstract;
        return this;
    }
    
    public MethodDefinitionBuilder isTransient(boolean trans) {
        this.isTransient = trans;
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

    public MethodDefinitionBuilder resultType(Function method, int flags) {
        if (method.isParameter()) {
            if (Decl.isUnboxedVoid(method) && !Strategy.useBoxedVoid(method)) {
                return resultType(gen.makeJavaTypeAnnotations(method, false), gen.make().Type(gen.syms().voidType));
            } else {
                Parameter parameter = method.getInitializerParameter();
                Type resultType = parameter.getType();
                for (int ii = 1; ii < method.getParameterLists().size(); ii++) {
                    resultType = gen.typeFact().getCallableType(resultType);
                }
                return resultType(gen.makeJavaType(resultType, CodegenUtil.isUnBoxed(method) ? 0 : AbstractTransformer.JT_NO_PRIMITIVES), method);
            }
        }
        TypedReference typedRef = gen.getTypedReference(method);
        TypedReference nonWideningTypedRef = gen.nonWideningTypeDecl(typedRef);
        Type nonWideningType = gen.nonWideningType(typedRef, nonWideningTypedRef);
        if(method.isActual()
                && CodegenUtil.hasTypeErased(method))
            flags |= AbstractTransformer.JT_RAW;
        return resultType(makeResultType(nonWideningTypedRef.getDeclaration(), nonWideningType, flags), method);
    }
    
    public MethodDefinitionBuilder resultTypeNonWidening(Type currentType, TypedReference typedRef, 
            Type returnType, int flags){
        TypedReference nonWideningTypedRef = gen.nonWideningTypeDecl(typedRef, currentType);
        returnType = gen.nonWideningType(typedRef, nonWideningTypedRef);
        return resultType(makeResultType(nonWideningTypedRef.getDeclaration(), returnType, flags), typedRef.getDeclaration());

    }
    
    public MethodDefinitionBuilder resultType(TypedDeclaration resultType, Type type, int flags) {
        return resultType(makeResultType(resultType, type, flags), resultType);
    }

    public MethodDefinitionBuilder resultType(JCExpression resultType, TypedDeclaration typeDecl) {
        return resultType(gen.makeJavaTypeAnnotations(typeDecl, false), resultType);
    }
    
    public MethodDefinitionBuilder resultType(List<JCAnnotation> resultTypeAnnos, JCExpression resultType) {
        this.resultTypeAnnos = resultTypeAnnos;
        this.resultTypeExpr = resultType;
        return this;
    }

    public MethodDefinitionBuilder modelAnnotations(java.util.List<Annotation> annotations) {
        modelAnnotations(gen.makeAtAnnotations(annotations));
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

    public MethodDefinitionBuilder reifiedTypeParameters(java.util.List<TypeParameter> typeParams) {
        for(TypeParameter typeParam : typeParams)
            reifiedTypeParameter(typeParam);
        return this;
    }

    public MethodDefinitionBuilder reifiedTypeParameter(TypeParameter param) {
        String descriptorName = gen.naming.getTypeArgumentDescriptorName(param);
        ParameterDefinitionBuilder pdb = ParameterDefinitionBuilder.implicitParameter(gen, descriptorName);
        pdb.type(gen.makeTypeDescriptorType(), List.<JCAnnotation>nil());
        pdb.modifiers(FINAL);
        if(!Annotations.includeModel(this.annotationFlags))
            pdb.noUserOrModelAnnotations();
        else
            pdb.ignored();
        parameter(pdb);

        return this;
    }

    public MethodDefinitionBuilder reifiedTypeParametersFromModel(java.util.List<TypeParameter> typeParameters) {
        for(TypeParameter typeParam : typeParameters)
            reifiedTypeParameter(typeParam);
        return this;
    }

    public void defaultValue(JCExpression defaultValue) {
        this.defaultValue = defaultValue;
    }

    public void mpl(java.util.List<ParameterList> parameterLists) {
        StringBuilder sb = new StringBuilder();
        for (int ii = 1; ii < parameterLists.size(); ii++) {
            ParameterList parameterList = parameterLists.get(ii);
            ParameterDefinitionBuilder.functionalParameters(sb, parameterList);
        }
        modelAnnotations(gen.makeAtFunctionalParameter(sb.toString()));
    }
}
