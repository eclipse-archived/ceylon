package com.redhat.ceylon.compiler.java.codegen;

import static com.sun.tools.javac.code.Flags.PRIVATE;
import static com.sun.tools.javac.code.Flags.PROTECTED;
import static com.sun.tools.javac.code.Flags.PUBLIC;

import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.tree.JCTree.JCAnnotation;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import com.sun.tools.javac.tree.JCTree.JCThrow;
import com.sun.tools.javac.tree.TreeCopier;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;

public class InitializerBuilder implements ParameterizedBuilder<InitializerBuilder> {

    private final AbstractTransformer gen;
    private long modifiers = 0;
    private JCStatement superCall;
    private final ListBuffer<ParameterDefinitionBuilder> params = ListBuffer.lb();
    /** 
     * For classes with parameter lists this is a {@code List<JCStatement>}.
     * For classes with constructors it's a 
     * {@code List<JCStatement|Tree.Constructor>} and this we know which 
     * statements need to be prepended to each transformed constructor body
     * and which need to be appended. 
     */
    private final java.util.List<Object/* JCStatement|Tree.Constructor*/> init = new java.util.ArrayList<Object>();
    private final ListBuffer<JCAnnotation> userAnnos = ListBuffer.lb();
    
    public InitializerBuilder(AbstractTransformer gen) {
        this.gen = gen;
    }
    
    /** Only called for classes with parameter lists */
    JCMethodDecl build() {
        if (superCall != null/* && !isAlias*/) {
            init.add(0, superCall);
        }
        List<JCStatement> body = List.from(init.toArray(new JCStatement[init.size()]));
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
            .userAnnotations(userAnnos.toList())
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
    
    public InitializerBuilder userAnnotations(List<JCAnnotation> annos) {
        this.userAnnos.addAll(annos);
        return this;
    }
    

    // Create a parameter for the constructor
    public InitializerBuilder parameter(ParameterDefinitionBuilder pdb) {
        params.append(pdb);
        return this;
    }
    
    public InitializerBuilder init(JCStatement statement) {
        if (statement != null) {
            this.init.add(statement);
        }
        return this;
    }
    
    public InitializerBuilder init(List<JCStatement> init) {
        if (init != null) {
            this.init.addAll(init);
        }
        return this;
    }
    
    public InitializerBuilder constructor(
            com.redhat.ceylon.compiler.typechecker.tree.Tree.Constructor ctor) {
        if (ctor != null) {
            this.init.add(ctor);
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
    
    List<JCStatement> statementsBefore(Tree.Constructor ctor) {
        ListBuffer<JCStatement> result = ListBuffer.lb();
        for (Object o : this.init) {
            if (o instanceof JCStatement) {
                result.add((JCStatement)o);
            } else if (ctor.equals(o)){
                break;
            }
        }
        return result.toList();
    }
    
    List<JCStatement> statementsAfter(Tree.Constructor ctor) {
        ListBuffer<JCStatement> result = ListBuffer.lb();
        boolean found = false;
        for (Object o : this.init) {
            if (found && o instanceof JCStatement) {
                result.add((JCStatement)o);
            } else if (ctor.equals(o)){
                found = true;
            }
        }
        return result.toList();
    }
    
    public List<JCStatement> getPreBodyCopy(Tree.Constructor ctor) {
        TreeCopier<Object> copier = new TreeCopier<Object>(gen.make());
        List<JCStatement> body = statementsBefore(ctor);
        if (superCall != null) {
            body = body.prepend(superCall);
        }
        return copier.copy(body);
    }
    
    public List<JCStatement> getPostBodyCopy(Tree.Constructor ctor) {
        TreeCopier<Object> copier = new TreeCopier<Object>(gen.make());
        List<JCStatement> body = statementsAfter(ctor);
        return copier.copy(body);
    }
    
    public List<ParameterDefinitionBuilder> getParameterList() {
        return params.toList();
    }



}
