package com.redhat.ceylon.compiler.codegen;

import static com.sun.tools.javac.code.Flags.ABSTRACT;
import static com.sun.tools.javac.code.Flags.FINAL;
import static com.sun.tools.javac.code.TypeTags.VOID;

import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
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
    private final CeylonTransformer gen;
    
    private final String name;
    
    private long modifiers;

    private boolean isActual;
    
    private ProducedType resultType;
    
    private final ListBuffer<JCAnnotation> annotations = ListBuffer.lb();
    
    private final ListBuffer<JCTypeParameter> typeParams = ListBuffer.lb();
    
    private final ListBuffer<JCVariableDecl> params = ListBuffer.lb();
    
    private ListBuffer<JCStatement> body = ListBuffer.lb();

    public static MethodDefinitionBuilder method(CeylonTransformer gen, String name) {
        return new MethodDefinitionBuilder(gen, name);
    }
    
    public static MethodDefinitionBuilder constructor(CeylonTransformer gen) {
        return new MethodDefinitionBuilder(gen, null);
    }
    
    public static MethodDefinitionBuilder getter(CeylonTransformer gen, String name, ProducedType attrType) {
        return method(gen, Util.getGetterName(name))
            .resultType(attrType);
    }
    
    public static MethodDefinitionBuilder setter(CeylonTransformer gen, String name, ProducedType attrType) {
        return method(gen, Util.getSetterName(name))
            .parameter(0, name, attrType);
    }
    
    private MethodDefinitionBuilder(CeylonTransformer gen, String name) {
        this.gen = gen;
        this.name = name;
    }
    
    public JCTree.JCMethodDecl build() {
        
        if (isActual) {
            annotations.appendList(gen.makeAtOverride());
        }
        if (resultType != null) {
            annotations.appendList(gen.makeJavaTypeAnnotations(resultType, true));
        }
        
        return gen.make().MethodDef(
                gen.make().Modifiers(modifiers, annotations.toList()), 
                makeName(name),
                makeResultType(resultType),
                typeParams.toList(), 
                params.toList(),
                List.<JCExpression> nil(),
                makeBody(body),
                null);
    }

    private Name makeName(String name) {
        if (name != null) {
            return gen.names.fromString(name);
        } else {
            return gen.names.init;
        }
    }

    private JCBlock makeBody(ListBuffer<JCStatement> body2) {
        return ((body != null) && ((modifiers & ABSTRACT) == 0)) ? gen.make().Block(0, body.toList()) : null;
    }

    private JCExpression makeResultType(ProducedType resultType) {
        if (resultType == null) {
            return gen.make().TypeIdent(VOID);
        } else {
            return gen.makeJavaType(resultType, false);
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
            if (!gen.willErase(t)) {
                bounds.append(gen.makeJavaType(t, false));
            }
        }
        typeParams.append(gen.make().TypeParameter(gen.names.fromString(name), bounds.toList()));
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
    
    public MethodDefinitionBuilder parameter(long modifiers, String name, ProducedType paramType) {
        JCExpression type = gen.makeJavaType(paramType, false);
        List<JCAnnotation> annots = gen.makeJavaTypeAnnotations(paramType, true);
        return parameter(gen.make().VarDef(gen.make().Modifiers(modifiers, annots), gen.names.fromString(name), type, null));
    }
    
    public MethodDefinitionBuilder parameter(Tree.Parameter param) {
        gen.at(param);
        String name = param.getIdentifier().getText();
        ProducedType paramType = gen.actualType(param);
        return parameter(FINAL, name, paramType);
    }

    public MethodDefinitionBuilder isActual(boolean isActual) {
        this.isActual = isActual;
        return this;
    }
    
    public MethodDefinitionBuilder body(JCStatement statement) {
        this.body.append(statement);
        return this;
    }
    
    public MethodDefinitionBuilder body(List<JCStatement> body) {
        this.body.appendList(body);
        return this;
    }

    private MethodDefinitionBuilder noBody() {
        this.body = null;
        return this;
    }

    public MethodDefinitionBuilder block(JCBlock block) {
        if (block != null) {
            return body(block.getStatements());
        } else {
            return noBody();
        }
    }

    public MethodDefinitionBuilder resultType(ProducedType resultType) {
        this.resultType = resultType;
        return this;
    }
}
