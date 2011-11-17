/*
 * Copyright Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the authors tag. All rights reserved.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU Lesser General Public License, v. 2.1.
 * 
 * This particular file is subject to the "Classpath" exception as provided in the 
 * LICENSE file that accompanied this code.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License,
 * v.2.1 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */

package com.redhat.ceylon.compiler.codegen;

import static com.sun.tools.javac.code.Flags.ABSTRACT;
import static com.sun.tools.javac.code.Flags.FINAL;
import static com.sun.tools.javac.code.Flags.PUBLIC;
import static com.sun.tools.javac.code.Flags.STATIC;
import static com.sun.tools.javac.code.TypeTags.VOID;

import com.redhat.ceylon.compiler.typechecker.model.Parameter;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.TypedDeclaration;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.util.Util;
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
    
    private TypedDeclaration resultType;
    private JCExpression resultTypeExpr;
    
    private final ListBuffer<JCAnnotation> annotations = ListBuffer.lb();
    
    private final ListBuffer<JCTypeParameter> typeParams = ListBuffer.lb();
    
    private final ListBuffer<JCVariableDecl> params = ListBuffer.lb();
    
    private ListBuffer<JCStatement> body = ListBuffer.lb();

    public static MethodDefinitionBuilder method(AbstractTransformer gen, String name) {
        return new MethodDefinitionBuilder(gen, Util.quoteMethodName(name));
    }
    
    public static MethodDefinitionBuilder systemMethod(AbstractTransformer gen, String name) {
        return new MethodDefinitionBuilder(gen, name);
    }
    
    public static MethodDefinitionBuilder constructor(AbstractTransformer gen) {
        return new MethodDefinitionBuilder(gen, null);
    }

    public static MethodDefinitionBuilder main(AbstractTransformer gen) {
        return new MethodDefinitionBuilder(gen, "main")
            .modifiers(PUBLIC | STATIC)
            .parameter(0, "args", gen.make().TypeArray(gen.make().Type(gen.syms().stringType)), List.<JCAnnotation>nil());
    }
    
    private MethodDefinitionBuilder(AbstractTransformer gen, String name) {
        this.gen = gen;
        this.name = name;
        resultTypeExpr = makeResultType(null);
    }
    
    public JCTree.JCMethodDecl build() {
        
        if (isActual) {
            annotations.appendList(gen.makeAtOverride());
        }
        if (resultType != null) {
            annotations.appendList(gen.makeJavaTypeAnnotations(resultType));
        }
        
        return gen.make().MethodDef(
                gen.make().Modifiers(modifiers, annotations.toList()), 
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
        return ((body != null) && ((modifiers & ABSTRACT) == 0)) ? gen.make().Block(0, body.toList()) : null;
    }

    private JCExpression makeResultType(TypedDeclaration resultType) {
        if (resultType == null) {
            return gen.make().TypeIdent(VOID);
        } else {
            return gen.makeJavaType(resultType);
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
        this.annotations.appendList(annotations);
        return this;
    }

    public MethodDefinitionBuilder typeParameter(String name, java.util.List<ProducedType> types) {
        ListBuffer<JCExpression> bounds = new ListBuffer<JCExpression>();
        for (ProducedType t : types) {
            if (!gen.willEraseToObject(t)) {
                bounds.append(gen.makeJavaType(t));
            }
        }
        typeParams.append(gen.make().TypeParameter(gen.names().fromString(name), bounds.toList()));
        return this;
    }

    public MethodDefinitionBuilder typeParameter(Tree.TypeParameterDeclaration param) {
        gen.at(param);
        String name = param.getIdentifier().getText();
        return typeParameter(name, param.getDeclarationModel().getSatisfiedTypes());
    }

    public MethodDefinitionBuilder parameters(List<JCVariableDecl> decls) {
        params.appendList(decls);
        return this;
    }
    
    public MethodDefinitionBuilder parameter(JCVariableDecl decl) {
        params.append(decl);
        return this;
    }
    
    public MethodDefinitionBuilder parameter(long modifiers, String name, TypedDeclaration decl) {
        JCExpression type = gen.makeJavaType(decl);
        List<JCAnnotation> annots = List.nil();
        if (gen.needsAnnotations(decl)) {
            annots = annots.appendList(gen.makeAtName(name));
            if (decl instanceof Parameter && ((Parameter)decl).isSequenced()) {
                annots = annots.appendList(gen.makeAtSequenced());
            }
            annots = annots.appendList(gen.makeJavaTypeAnnotations(decl));
        }
        return parameter(gen.make().VarDef(gen.make().Modifiers(modifiers, annots), gen.names().fromString(name), type, null));
    }
    
    public MethodDefinitionBuilder parameter(long modifiers, String name, JCExpression paramType, List<JCAnnotation> annots) {
        return parameter(gen.make().VarDef(gen.make().Modifiers(modifiers, annots), gen.names().fromString(name), paramType, null));
    }
    
    public MethodDefinitionBuilder parameter(Tree.Parameter param) {
        gen.at(param);
        return parameter(param.getDeclarationModel());
    }

    public MethodDefinitionBuilder parameter(Parameter param) {
        String name = param.getName();
        return parameter(FINAL, name, param);
    }

    public MethodDefinitionBuilder isActual(boolean isActual) {
        this.isActual = isActual;
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

    public MethodDefinitionBuilder resultType(TypedDeclaration resultType) {
        this.resultType = resultType;
        this.resultTypeExpr = makeResultType(resultType);
        return this;
    }

    public MethodDefinitionBuilder resultType(JCExpression resultType) {
        this.resultType = null;
        this.resultTypeExpr = resultType;
        return this;
    }
}
