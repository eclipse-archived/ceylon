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

import com.redhat.ceylon.compiler.java.util.Util;
import com.redhat.ceylon.compiler.typechecker.model.Annotation;
import com.redhat.ceylon.compiler.typechecker.model.FunctionalParameter;
import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.Parameter;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.TypeParameter;
import com.redhat.ceylon.compiler.typechecker.model.TypedDeclaration;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
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

    private boolean isActual;
    private boolean isFormal;
    
    private JCExpression resultTypeExpr;
    private List<JCAnnotation> resultTypeAnnos;
    
    private final ListBuffer<JCAnnotation> annotations = ListBuffer.lb();
    
    private final ListBuffer<JCTypeParameter> typeParams = ListBuffer.lb();
    private final ListBuffer<JCExpression> typeParamAnnotations = ListBuffer.lb();
    
    private final ListBuffer<JCVariableDecl> params = ListBuffer.lb();
    
    private ListBuffer<JCStatement> body = ListBuffer.lb();

    private boolean ancestorLocal;
    
    private boolean built = false;

    public static MethodDefinitionBuilder method(AbstractTransformer gen, boolean ancestorLocal, boolean isMember, String name) {
        return new MethodDefinitionBuilder(gen, ancestorLocal, isMember ? Util.quoteMethodName(name) : Util.quoteIfJavaKeyword(name));
    }
    
    public static MethodDefinitionBuilder systemMethod(AbstractTransformer gen, boolean ancestorLocal, String name) {
        return new MethodDefinitionBuilder(gen, ancestorLocal, name);
    }
    
    public static MethodDefinitionBuilder constructor(AbstractTransformer gen, boolean ancestorLocal) {
        return new MethodDefinitionBuilder(gen, ancestorLocal, null);
    }

    public static MethodDefinitionBuilder main(AbstractTransformer gen, boolean ancestorLocal) {
        return new MethodDefinitionBuilder(gen, ancestorLocal, "main")
            .modifiers(PUBLIC | STATIC)
            .parameter(0, "args", gen.make().TypeArray(gen.make().Type(gen.syms().stringType)), List.<JCAnnotation>nil());
    }
    
    private MethodDefinitionBuilder(AbstractTransformer gen, boolean ancestorLocal, String name) {
        this.gen = gen;
        this.name = name;
        this.ancestorLocal = ancestorLocal;
        resultTypeExpr = makeVoidType();
    }
    
    public JCTree.JCMethodDecl build() {
        if (built) {
            throw new IllegalStateException();
        }
        built = true;
        if (isActual) {
            this.annotations.appendList(gen.makeAtOverride());
        }
        if (resultTypeAnnos != null) {
            annotations(resultTypeAnnos);
        }
        if(!typeParamAnnotations.isEmpty())
            annotations(gen.makeAtTypeParameters(typeParamAnnotations.toList()));

        return gen.make().MethodDef(
                gen.make().Modifiers(modifiers, gen.filterAnnotations(annotations)), 
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
        return (!isFormal && (body != null) && ((modifiers & ABSTRACT) == 0)) ? gen.make().Block(0, body.toList()) : null;
    }

    JCExpression makeVoidType() {
        return gen.make().TypeIdent(VOID);
    }

    JCExpression makeResultType(TypedDeclaration typedDeclaration, ProducedType type) {
        if (typedDeclaration == null
                || (!(typedDeclaration instanceof FunctionalParameter)
                        && gen.isVoid(type))) {
            return makeVoidType();
        } else {
            return gen.makeJavaType(typedDeclaration, type);
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

    public MethodDefinitionBuilder annotations(List<JCTree.JCAnnotation> annotations) {
        if (ancestorLocal) {
            return this;
        }
        this.annotations.appendList(annotations);
        return this;
    }

    public MethodDefinitionBuilder typeParameter(Tree.TypeParameterDeclaration param) {
        gen.at(param);
        return typeParameter(gen.makeTypeParameter(param), gen.makeAtTypeParameter(param));
    }
    
    public MethodDefinitionBuilder typeParameter(TypeParameter param) {
        return typeParameter(gen.makeTypeParameter(param), gen.makeAtTypeParameter(param));
    }
    
    public MethodDefinitionBuilder typeParameter(JCTypeParameter tp, JCAnnotation tpAnno) {
        typeParams.append(tp);
        typeParamAnnotations.append(tpAnno);
        return this;
    }

    public MethodDefinitionBuilder parameters(List<JCVariableDecl> decls) {
        params.appendList(decls);
        return this;
    }
    
    public MethodDefinitionBuilder parameter(JCVariableDecl decl) {
        params.append(decl);
        return this;
    }
    
    public MethodDefinitionBuilder parameter(long modifiers, String name, TypedDeclaration decl, TypedDeclaration nonWideningDecl, ProducedType nonWideningType) {
        JCExpression type = gen.makeJavaType(nonWideningDecl, nonWideningType);
        List<JCAnnotation> annots = List.nil();
        if (gen.needsAnnotations(decl)) {
            annots = annots.appendList(gen.makeAtName(name));
            if (decl instanceof Parameter && ((Parameter)decl).isSequenced()) {
                annots = annots.appendList(gen.makeAtSequenced());
            }
            if (decl instanceof Parameter && ((Parameter)decl).isDefaulted()) {
                annots = annots.appendList(gen.makeAtDefaulted());
            }
            annots = annots.appendList(gen.makeJavaTypeAnnotations(decl));
        }
        return parameter(gen.make().VarDef(gen.make().Modifiers(modifiers, annots), gen.names().fromString(name), type, null));
    }
    
    public MethodDefinitionBuilder parameter(long modifiers, String name, JCExpression paramType, List<JCAnnotation> annots) {
        if (annots == null) {
            annots = List.nil();
        }
        return parameter(gen.make().VarDef(gen.make().Modifiers(modifiers, annots), gen.names().fromString(name), paramType, null));
    }
    
    public MethodDefinitionBuilder parameter(Tree.Parameter param) {
        gen.at(param);
        return parameter(param.getDeclarationModel());
    }

    public MethodDefinitionBuilder parameter(Parameter param) {
        String name = param.getName();
        return parameter(FINAL, name, param, param, param.getType());
    }

    public MethodDefinitionBuilder isActual(boolean isActual) {
        this.isActual = isActual;
        return this;
    }
    
    public MethodDefinitionBuilder isFormal(boolean isFormal) {
        this.isFormal = isFormal;
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

    public MethodDefinitionBuilder resultType(Method method) {
        TypedDeclaration nonWideningTypeDecl = gen.nonWideningTypeDecl(method);
        ProducedType nonWideningType = gen.nonWideningType(method, nonWideningTypeDecl);
        return resultType(makeResultType(nonWideningTypeDecl, nonWideningType), method);
    }

    public MethodDefinitionBuilder resultType(TypedDeclaration resultType) {
        return resultType(makeResultType(resultType, resultType.getType()), resultType);
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
}
