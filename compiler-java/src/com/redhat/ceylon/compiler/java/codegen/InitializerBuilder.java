package com.redhat.ceylon.compiler.java.codegen;

import static com.sun.tools.javac.code.Flags.FINAL;
import static com.sun.tools.javac.code.Flags.ABSTRACT;
import static com.sun.tools.javac.code.Flags.PRIVATE;
import static com.sun.tools.javac.code.Flags.PROTECTED;
import static com.sun.tools.javac.code.Flags.PUBLIC;

import javax.management.RuntimeErrorException;

import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.tree.TreeCopier;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import com.sun.tools.javac.tree.JCTree.JCThrow;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;

public class InitializerBuilder implements ParameterizedBuilder<InitializerBuilder> {

    private final AbstractTransformer gen;
    private long modifiers = 0;
    private JCStatement superCall;
    private final ListBuffer<ParameterDefinitionBuilder> params = ListBuffer.lb();
    private final ListBuffer<JCStatement> init = ListBuffer.lb();
    
    public InitializerBuilder(AbstractTransformer gen) {
        this.gen = gen;
    }
    
    JCMethodDecl build() {
        if (superCall != null/* && !isAlias*/) {
            init.prepend(superCall);
        }
        List<JCStatement> body = init.toList();
        int index = 0;
        for (JCStatement stmt : body) {
            if (stmt instanceof JCThrow) {
                ListBuffer<JCStatement> filtered = ListBuffer.<JCStatement>lb();
                filtered.addAll(body.subList(0, index+1));
                body = filtered.toList();
                break;
            }
            index++;
        }
        MethodDefinitionBuilder constructor = MethodDefinitionBuilder.constructor(gen);
        constructor.modifiers(modifiers)
            .parameters(params.toList())
            .body(body);
        return constructor.build();
    }
    

    public InitializerBuilder modifiers(long mods) {
        if ((mods & ~(PUBLIC|PRIVATE|PROTECTED)) != 0) {
            throw new BugException("illegal modifier for constructor " + Flags.toString(mods));
        }
        this.modifiers = mods;
        return this;
    }
    

    // Create a parameter for the constructor
    public InitializerBuilder parameter(ParameterDefinitionBuilder pdb) {
        params.append(pdb);
        return this;
    }
    
    public InitializerBuilder init(JCStatement statement) {
        if (statement != null) {
            this.init.append(statement);
        }
        return this;
    }
    
    public InitializerBuilder init(List<JCStatement> init) {
        if (init != null) {
            this.init.appendList(init);
        }
        return this;
    }

    /** Set the expression used to invoke {@code super()} */
    public InitializerBuilder superCall(JCStatement superCall) {
        this.superCall = superCall;
        return this;
    }

    public boolean isEmptyInit() {
        return init.isEmpty();
    }

    public List<JCStatement> getBodyCopy() {
        TreeCopier copier = new TreeCopier(gen.make());
        return copier.copy(this.init.toList());
    }



}
