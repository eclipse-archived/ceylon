package com.redhat.ceylon.compiler.codegen;

import static com.sun.tools.javac.code.Flags.FINAL;
import static com.sun.tools.javac.code.Flags.INTERFACE;
import static com.sun.tools.javac.code.Flags.PRIVATE;
import static com.sun.tools.javac.code.Flags.PROTECTED;
import static com.sun.tools.javac.code.Flags.PUBLIC;

import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.util.Util;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCAnnotation;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import com.sun.tools.javac.tree.JCTree.JCTypeParameter;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;

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
    private final CeylonTransformer gen;
    
    private final String name;
    
    private long modifiers;
    private long constructorModifiers = -1;
    
    private ProducedType extending;
    private final ListBuffer<ProducedType> satisfies = ListBuffer.lb();
    private final ListBuffer<JCTypeParameter> typeParams = ListBuffer.lb();
    
    private final ListBuffer<JCAnnotation> annotations = ListBuffer.lb();
    
    private final ListBuffer<JCVariableDecl> params = ListBuffer.lb();
    
    private final ListBuffer<JCTree> defs = ListBuffer.lb();
    private final ListBuffer<JCTree> body = ListBuffer.lb();
    private final ListBuffer<JCStatement> init = ListBuffer.lb();
    
    public static ClassDefinitionBuilder klass(CeylonTransformer gen, String name) {
        return new ClassDefinitionBuilder(gen, name);
    }
    
    private ClassDefinitionBuilder(CeylonTransformer gen, String name) {
        this.gen = gen;
        this.name = name;
        
        annotations(gen.makeAtCeylon());
    }

    public boolean existsParam(String name) {
        for (JCTree decl : params) {
            if (decl instanceof JCVariableDecl) {
                JCVariableDecl var = (JCVariableDecl)decl;
                if (var.name.toString().equals(name)) {
                    return true;
                }
            }
        }
        return false;
    }

    public JCTree.JCClassDecl build() {
        ListBuffer<JCTree> defs = ListBuffer.lb();
        appendDefinitionsTo(defs);
        return gen.make().ClassDef(
                gen.make().Modifiers(modifiers, annotations.toList()),
                gen.names.fromString(Util.quoteIfJavaKeyword(name)),
                typeParams.toList(),
                getSuperclass(extending),
                transformSatisfiedTypes(satisfies.toList()),
                defs.toList());
    }

    private void appendDefinitionsTo(ListBuffer<JCTree> defs) {
        defs.appendList(this.defs);
        if ((modifiers & INTERFACE) == 0) {
            defs.append(createConstructor());
        }
        defs.appendList(body);
    }

    private JCTree getSuperclass(ProducedType extendedType) {
        JCExpression superclass;
        if (extendedType != null) {
            superclass = gen.makeJavaType(extendedType, CeylonTransformer.SATISFIES_OR_EXTENDS);
            // simplify if we can
            if (superclass instanceof JCTree.JCFieldAccess 
            && ((JCTree.JCFieldAccess)superclass).sym.type == gen.syms.objectType) {
                superclass = null;
            }
        } else {
            if ((modifiers & INTERFACE) != 0) {
                // The VM insists that interfaces have java.lang.Object as their superclass
                superclass = gen.makeIdent(gen.syms.objectType);
            } else {
                superclass = null;
            }
        }
        return superclass;
    }

    private List<JCExpression> transformSatisfiedTypes(List<ProducedType> list) {
        if (list == null) {
            return List.nil();
        }

        ListBuffer<JCExpression> satisfies = new ListBuffer<JCExpression>();
        for (ProducedType t : list) {
            satisfies.append(gen.makeJavaType(t, CeylonTransformer.SATISFIES_OR_EXTENDS));
        }
        return satisfies.toList();
    }

    private JCMethodDecl createConstructor() {
        long mods = constructorModifiers;
        if (mods == -1) {
            // The modifiers were never explicitly set
            // so we try to come up with some good defaults
            mods = modifiers & (PUBLIC | PRIVATE | PROTECTED);
        }
        return MethodDefinitionBuilder
            .constructor(gen)
            .modifiers(mods)
            .parameters(params.toList())
            .body(init.toList())
            .build();
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

    public ClassDefinitionBuilder typeParameter(String name, java.util.List<ProducedType> types) {
        ListBuffer<JCExpression> bounds = new ListBuffer<JCExpression>();
        for (ProducedType t : types) {
            if (!gen.willErase(t)) {
                bounds.append(gen.makeJavaType(t));
            }
        }
        typeParams.append(gen.make().TypeParameter(gen.names.fromString(name), bounds.toList()));
        return this;
    }

    public ClassDefinitionBuilder typeParameter(Tree.TypeParameterDeclaration param) {
        gen.at(param);
        String name = param.getIdentifier().getText();
        return typeParameter(name, param.getDeclarationModel().getSatisfiedTypes());
    }

    public ClassDefinitionBuilder extending(ProducedType extending) {
        this.extending = extending;
        return this;
    }

    public ClassDefinitionBuilder extending(Tree.ExtendedType extendedType) {
        if (extendedType.getInvocationExpression().getPositionalArgumentList() != null) {
            List<JCExpression> args = List.<JCExpression> nil();

            for (Tree.PositionalArgument arg : extendedType.getInvocationExpression().getPositionalArgumentList().getPositionalArguments())
                args = args.append(gen.expressionGen.transformArg(arg));

            init(gen.at(extendedType).Exec(gen.make().Apply(List.<JCExpression> nil(), gen.make().Ident(gen.names._super), args)));
        }
        return extending(extendedType.getType().getTypeModel());
    }
    
    public ClassDefinitionBuilder satisfies(java.util.List<ProducedType> satisfies) {
        this.satisfies.addAll(satisfies);
        return this;
    }

    public ClassDefinitionBuilder annotations(List<JCTree.JCAnnotation> annotations) {
        this.annotations.appendList(annotations);
        return this;
    }

    public ClassDefinitionBuilder parameter(String name, ProducedType paramType, boolean isCaptured) {
        // Create a parameter for the constructor
        JCExpression type = gen.makeJavaType(paramType);
        List<JCAnnotation> annots = gen.makeAtName(name);
        annots = annots.appendList(gen.makeJavaTypeAnnotations(paramType, true));
        JCVariableDecl var = gen.make().VarDef(gen.make().Modifiers(0, annots), gen.names.fromString(name), type, null);
        params.append(var);
        
        // Check if the parameter is used outside of the initializer
        if (isCaptured) {
            // If so we create a field for it initializing it with the parameter's value
            JCVariableDecl localVar = gen.make().VarDef(gen.make().Modifiers(FINAL | PRIVATE), gen.names.fromString(name), type , null);
            defs.append(localVar);
            init.append(gen.make().Exec(gen.make().Assign(gen.makeSelect("this", localVar.getName().toString()), gen.make().Ident(var.getName()))));
        }
        
        return this;
    }
    
    public ClassDefinitionBuilder parameter(Tree.Parameter param) {
        gen.at(param);
        String name = param.getIdentifier().getText();
        return parameter(name, param.getType().getTypeModel(), param.getDeclarationModel().isCaptured());
    }
    
    public ClassDefinitionBuilder defs(JCTree statement) {
        this.defs.append(statement);
        return this;
    }
    
    public ClassDefinitionBuilder defs(List<JCTree> defs) {
        this.defs.appendList(defs);
        return this;
    }
    
    public ClassDefinitionBuilder body(JCTree statement) {
        this.body.append(statement);
        return this;
    }
    
    public ClassDefinitionBuilder body(List<JCTree> body) {
        this.body.appendList(body);
        return this;
    }
    
    public ClassDefinitionBuilder init(JCStatement statement) {
        this.init.append(statement);
        return this;
    }
    
    public ClassDefinitionBuilder init(List<JCStatement> init) {
        this.init.appendList(init);
        return this;
    }

}
