package com.redhat.ceylon.compiler.java.codegen;

import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;

/**
 * A builder for constructing method calls and instantiations
 */
public class CallBuilder {

    private enum Kind {
        APPLY,
        NEW
    }
    
    private final AbstractTransformer gen;
    private Kind kind;
    
    private ListBuffer<JCExpression> typeargs = ListBuffer.<JCExpression>lb();
    private ListBuffer<JCExpression> args = ListBuffer.<JCExpression>lb();

    private JCExpression methodOrClass;
    private JCExpression instantiateQualfier;
    
    private CallBuilder(AbstractTransformer gen) {
        this.gen = gen;
    }
    
    public static CallBuilder instance(AbstractTransformer gen) {
        CallBuilder builder = new CallBuilder(gen);
        return builder;
    }
    
    public CallBuilder invoke(JCExpression fn) {
        this.methodOrClass = fn;
        this.kind = Kind.APPLY;
        return this;
    }
    
    public CallBuilder instantiate(JCExpression cls) {
        return instantiate(null, cls);
    }
    
    public CallBuilder instantiate(JCExpression qualifier, JCExpression cls) {
        this.methodOrClass = cls;
        this.instantiateQualfier = qualifier;
        this.kind = Kind.NEW;
        return this;
    }
    
    public CallBuilder typeArgument(JCExpression expr) {
        this.typeargs.append(expr);
        return this;
    }
    
    public CallBuilder typeArguments(List<JCExpression> typeArguments) {
        this.typeargs.clear();
        this.typeargs.addAll(typeArguments);
        return this;
    }
    
    public CallBuilder prependArgument(JCExpression expr) {
        this.args.prepend(expr);
        return this;
    }
    
    public CallBuilder argument(JCExpression expr) {
        this.args.append(expr);
        return this;
    }
    
    public JCExpression build() {
        switch (kind) {
        case APPLY:
            return gen.make().Apply(this.typeargs.toList(), this.methodOrClass, this.args.toList());
        case NEW:
            return gen.make().NewClass(this.instantiateQualfier, null, this.methodOrClass, this.args.toList(), null);
        default:
            throw Assert.fail();
        }
    }    
}
