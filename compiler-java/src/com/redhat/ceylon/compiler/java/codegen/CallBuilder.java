package com.redhat.ceylon.compiler.java.codegen;

import com.redhat.ceylon.compiler.java.codegen.Naming.SyntheticName;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCStatement;
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
    
    public enum ArgumentHandling {
        NORMAL,
        ARGUMENTS_ALIASED,
        ARGUMENTS_EVAL_FIRST
    }
    
    private static final String MISSING_TYPE = "Type expression required when evaluateArgumentsFirst()";
    
    private final AbstractTransformer gen;
    private Kind kind;
    
    private ListBuffer<JCExpression> typeargs = ListBuffer.<JCExpression>lb();
    /** The transformed argument expressions and their transformed type expressions */
    private ListBuffer<ExpressionAndType> argumentsAndTypes = ListBuffer.<ExpressionAndType>lb();

    private JCExpression methodOrClass;
    private ExpressionAndType instantiateQualfier;
    private ArgumentHandling argumentHandling = ArgumentHandling.NORMAL;
    private Naming.SyntheticName basename;
    private boolean built = false;
    
    private List<JCStatement> primaryAndArguments;
    
    private CallBuilder(AbstractTransformer gen) {
        this.gen = gen;
    }
    
    public static CallBuilder instance(AbstractTransformer gen) {
        CallBuilder builder = new CallBuilder(gen);
        return builder;
    }
    
    public CallBuilder invoke(JCExpression fn) {
        this.methodOrClass = fn;
        this.instantiateQualfier = null;
        this.kind = Kind.APPLY;
        return this;
    }
    
    public CallBuilder instantiate(JCExpression cls) {
        return instantiate(null, cls);
    }
    
    public CallBuilder instantiate(ExpressionAndType qualifier, JCExpression cls) {
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
    
    public CallBuilder argument(JCExpression expr) {
        this.argumentAndType(new ExpressionAndType(expr, null));
        return this;
    }
    
    public CallBuilder argumentAndType(ExpressionAndType argumentAndType) {
        this.argumentsAndTypes.append(argumentAndType);
        return this;
    }
    
    public CallBuilder arguments(List<JCExpression> args) {
        for (JCExpression arg : args) {
            this.argument(arg);
        }
        return this;
    }
    
    public CallBuilder argumentsAndTypes(List<ExpressionAndType> argsAndTypes) {
        this.argumentsAndTypes.clear();
        this.argumentsAndTypes.addAll(argsAndTypes);
        return this;
    }
    
    /**
     * Determine whether a Let expression should be used to evaluate qualifier 
     * and arguments <strong>prior</strong> to evaluating a
     * {@code super} invocation or instantiation. The JVM prohibits a backward 
     * branch (i.e. loop) when an uninitialized reference is on the operand 
     * stack.
     * @see "#929"
     */
    public CallBuilder argumentHandling(ArgumentHandling argumentHandling, Naming.SyntheticName basename) {
        if (built) {
            throw new RuntimeException();
        }
        this.argumentHandling = argumentHandling;
        this.basename = basename;
        return this;
    }
    
    public JCExpression build() {
        if (built) {
            throw new RuntimeException();
        }
        built = true;
        JCExpression result;
        List<JCExpression> arguments;
        final JCExpression newEncl;
        if (argumentHandling != ArgumentHandling.NORMAL) {
            primaryAndArguments = List.<JCStatement>nil();
            arguments = List.<JCExpression>nil();
            if (instantiateQualfier != null 
                    && instantiateQualfier.expression != null) {
                if (instantiateQualfier.type == null) {
                    throw Assert.fail(MISSING_TYPE);
                }
                SyntheticName qualName = getQualifierName(basename);
                primaryAndArguments = List.<JCStatement>of(gen.makeVar(qualName, 
                        instantiateQualfier.type, 
                        instantiateQualfier.expression));
                newEncl = qualName.makeIdent();
            } else {
                newEncl = null;
            }
            int argumentNum = 0;
            for (ExpressionAndType argumentAndType : argumentsAndTypes) {
                SyntheticName name = getArgumentName(basename, argumentNum);
                if (argumentAndType.type == null) {
                    throw Assert.fail(MISSING_TYPE);
                }
                if (argumentHandling == ArgumentHandling.ARGUMENTS_ALIASED
                        || (argumentHandling == ArgumentHandling.ARGUMENTS_EVAL_FIRST && kind == Kind.NEW)) {
                    primaryAndArguments = primaryAndArguments.append(gen.makeVar(name, 
                            argumentAndType.type, 
                            argumentAndType.expression));
                }
                arguments = arguments.append(name.makeIdent());
                argumentNum++;
            }
            
        } else {
            newEncl = this.instantiateQualfier != null ? this.instantiateQualfier.expression : null;
            primaryAndArguments = null;
            arguments = ExpressionAndType.toExpressionList(this.argumentsAndTypes);
        }
        switch (kind) {
        case APPLY:
            result = gen.make().Apply(this.typeargs.toList(), this.methodOrClass, arguments);
            break;
        case NEW:
            result = gen.make().NewClass(newEncl, null, this.methodOrClass, arguments, null);
            break;
        
        default:
            throw Assert.fail();
        }
        if (this.argumentHandling == ArgumentHandling.ARGUMENTS_EVAL_FIRST) { 
            result = gen.make().LetExpr(primaryAndArguments, result);    
        }
        
        return result;
    }

    private SyntheticName getArgumentName(Naming.SyntheticName basename, int argumentNum) {
        SyntheticName name = basename.suffixedBy("$arg$"+argumentNum);
        return name;
    }

    private SyntheticName getQualifierName(Naming.SyntheticName basename) {
        SyntheticName qualName = basename.suffixedBy("$qual");
        return qualName;
    }
    
    public List<JCStatement> getPrimaryAndArguments() {
        if (!built) {
            throw new RuntimeException();
        }
        return primaryAndArguments;
    }
}
