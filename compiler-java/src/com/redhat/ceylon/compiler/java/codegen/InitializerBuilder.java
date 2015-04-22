package com.redhat.ceylon.compiler.java.codegen;

import static com.sun.tools.javac.code.Flags.PRIVATE;
import static com.sun.tools.javac.code.Flags.PROTECTED;
import static com.sun.tools.javac.code.Flags.PUBLIC;

import com.redhat.ceylon.compiler.typechecker.model.Constructor;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.MemberOrTypeExpression;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.tree.JCTree;
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
    // TODO remove this field
    private JCStatement delegateCall;
    private final ListBuffer<ParameterDefinitionBuilder> params = ListBuffer.lb();
    /** 
     * For classes with parameter lists this is a {@code List<JCStatement>}.
     * For classes with constructors it's a 
     * {@code List<JCStatement|Tree.Constructor>} and this we know which 
     * statements need to be prepended to each transformed constructor body
     * and which need to be appended. 
     */
    private final java.util.List<Object/* JCStatement|Constructor*/> init = new java.util.ArrayList<Object>();
    private final ListBuffer<JCAnnotation> userAnnos = ListBuffer.lb();
    
    public InitializerBuilder(AbstractTransformer gen) {
        this.gen = gen;
    }
    
    /** Only called for classes with parameter lists */
    JCMethodDecl build() {
        if (delegateCall != null/* && !isAlias*/) {
            init.add(0, delegateCall);
        }
        List<JCStatement> body = statementsBetween(null, null);
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
            this.init.add(ctor.getDeclarationModel());
        }
        return this;
    }

    /** 
     * Set the expression used to invoke {@code super()} or {@code this()}.
     * (i.e. delegate to another constructor). 
     */
    public InitializerBuilder delegateCall(JCStatement delegateCall) {
        // TODO remove this method
        this.delegateCall = delegateCall;
        return this;
    }
    

    public boolean isEmptyInit() {
        return init.isEmpty();
    }
    
    List<JCStatement> statementsBetween(Constructor first, Constructor second) {
        ListBuffer<JCStatement> buffer = ListBuffer.lb();
        boolean found = first == null;
        for (Object o : this.init) {
            if (found && o instanceof JCStatement) {
                buffer.add((JCStatement)o);
            } else if (first != null && first.equals(o)){
                found = true;
            } else if (second != null && second.equals(o)){
                break;
            }
        }
        return buffer.toList();
    }
    <T extends JCTree> List<T> copyOf(List<T> list) {
        TreeCopier<?> copier = new TreeCopier(gen.make());
        return copier.copy(list);
    }
    
    public List<ParameterDefinitionBuilder> getParameterList() {
        return params.toList();
    }

    /**
     * Constructs the body of the given constructor
     * @param ctor The constructor whose body we're constructing
     * @param delegatedCtorOrClass The constructor or class this constructor delegates to
     * @param delegateExpr The {@code super()} or {@code this()} constructor delegation
     * @param ctorStmts The statements from the constructors body
     * @return
     */
    public List<JCStatement> getBody(Tree.Constructor that, ClassDefinitionBuilder classBuilder) {
        Constructor ctor = that.getDeclarationModel();
        
        JCStatement delegateExpr;
        Declaration delegatedCtor;
        if (that.getDelegatedConstructor() != null) {
            Tree.InvocationExpression chainedCtorInvocation = that.getDelegatedConstructor().getInvocationExpression();
            delegatedCtor = (((MemberOrTypeExpression)chainedCtorInvocation.getPrimary())).getDeclaration();
            if (delegatedCtor instanceof Constructor
                    && delegatedCtor.getContainer().equals(ctor.getContainer())) {
                delegateExpr = gen.make().Exec(gen.expressionGen().transformThisInvocation(that.getDelegatedConstructor(),
                        chainedCtorInvocation, classBuilder));
            } else {
                delegateExpr = gen.make().Exec(gen.expressionGen().transformSuper(that.getDelegatedConstructor(),
                        chainedCtorInvocation, classBuilder));
            }
        } else {
            // TODO statement execution order when  init statements and implicit super() call
            delegatedCtor = null;
            delegateExpr = null;
        }
        
        /*
         * abstract ctor && extends super class ctor: super(), pre-init, ctor-body
         * abstract ctor && extends abstract ctor: this(), between init, ctor-body
         * concrete ctor && extends super class ctor: super(), pre-init, ctory-body, post-init
         * concrete ctor && extends abstract ctor: this(), between init, ctory-body, post-init
         * 
         * Note we need to take a copy of the statements because they ones in 
         * this.init will be shared between all constructors!
         */
        boolean thisDelegation = delegatedCtor != null && Decl.getConstructedClass(delegatedCtor)
                .equals(
                        ctor
                        .getContainer());
        ListBuffer<JCStatement> stmts = ListBuffer.lb();
        if (delegateExpr != null) {
            stmts.add(delegateExpr);
        }
        if (thisDelegation) {// delegating to abstract
            stmts.addAll(copyOf(statementsBetween((Constructor)delegatedCtor, ctor)));
        } else {// super delegation 
            stmts.addAll(copyOf(statementsBetween(null, ctor)));
        }
        stmts.addAll(gen.statementGen().transformBlock(that.getBlock()));
        if (!ctor.isAbstract()) {
            stmts.addAll(copyOf(statementsBetween(ctor, null)));
        }
        return stmts.toList();
    }



}
