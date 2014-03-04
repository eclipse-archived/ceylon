package com.redhat.ceylon.compiler.java.codegen;

import com.redhat.ceylon.compiler.java.codegen.Naming.Suffix;
import com.redhat.ceylon.compiler.java.codegen.Naming.SyntheticName;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;
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
        NEW,
        ARRAY_READ,
        ARRAY_WRITE,
        NEW_ARRAY,
        FIELD_READ
    }
    
    public static final int CB_ALIAS_ARGS = 1<<0;
    public static final int CB_LET = 1<<1;
    
    private static final String MISSING_TYPE = "Type expression required when evaluateArgumentsFirst()";
    
    private final AbstractTransformer gen;
    private Kind kind;
    
    private ListBuffer<JCExpression> typeargs = ListBuffer.<JCExpression>lb();
    /** The transformed argument expressions and their transformed type expressions */
    private ListBuffer<ExpressionAndType> argumentsAndTypes = ListBuffer.<ExpressionAndType>lb();

    private JCExpression methodOrClass;
    private ExpressionAndType instantiateQualfier;
    private int cbOpts;
    private Naming.SyntheticName basename;
    private boolean built = false;
    
    private final ListBuffer<JCStatement> statements = ListBuffer.<JCStatement>lb();
    private boolean voidMethod;
    
    private boolean haveLocation = false;
    private Node location;
    private JCClassDecl classDefs;
    private JCExpression arrayInstanceReifiedType;
    private JCExpression arrayInstanceCast;
    private int arrayInstanceDimensions;
    
    private CallBuilder(AbstractTransformer gen) {
        this.gen = gen;
    }
    
    public static CallBuilder instance(AbstractTransformer gen) {
        CallBuilder builder = new CallBuilder(gen);
        return builder;
    }

    public CallBuilder location(Node at) {
        haveLocation = true;
        this.location = at;
        return this;
    }
    
    public CallBuilder arrayRead(JCExpression expr) {
        this.methodOrClass = expr;
        this.instantiateQualfier = null;
        this.kind = Kind.ARRAY_READ;
        return this;
    }

    public CallBuilder arrayWrite(JCExpression expr) {
        this.methodOrClass = expr;
        this.instantiateQualfier = null;
        this.kind = Kind.ARRAY_WRITE;
        return this;
    }

    public CallBuilder invoke(JCExpression fn) {
        this.methodOrClass = fn;
        this.instantiateQualfier = null;
        this.kind = Kind.APPLY;
        return this;
    }

    public CallBuilder fieldRead(JCExpression expr) {
        this.methodOrClass = expr;
        this.instantiateQualfier = null;
        this.kind = Kind.FIELD_READ;
        return this;
    }

    public CallBuilder instantiate(JCExpression cls) {
        return instantiate(null, cls);
    }
    
    public CallBuilder instantiate(ExpressionAndType qualifier, JCExpression cls) {
        return instantiate(qualifier, cls, null);
    }
    
    public CallBuilder instantiate(ExpressionAndType qualifier, JCExpression cls, JCClassDecl classDefs) {
        this.methodOrClass = cls;
        this.classDefs = classDefs;
        this.instantiateQualfier = qualifier;
        this.kind = Kind.NEW;
        return this;
    }

    public CallBuilder javaArrayInstance(JCExpression type) {
        this.methodOrClass = type;
        this.instantiateQualfier = null;
        this.kind = Kind.NEW_ARRAY;
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

    public CallBuilder prependArgumentAndType(ExpressionAndType argumentAndType) {
        this.argumentsAndTypes = this.argumentsAndTypes.prepend(argumentAndType);
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
    public CallBuilder argumentHandling(int cbOpts, Naming.SyntheticName basename) {
        if (built) {
            throw new RuntimeException();
        }
        this.cbOpts = cbOpts;
        this.basename = basename;
        return this;
    }
    
    public int getArgumentHandling() {
        return cbOpts;
    }
    
    public CallBuilder appendStatement(JCStatement stmt) {
        this.statements.append(stmt);
        return this;
    }

    public List<JCStatement> getStatements() {
        if (!built) {
            throw new RuntimeException();
        }
        return statements.toList();
    }
    
    public JCExpression build() {
        if (built) {
            throw new RuntimeException();
        }
        built = true;
        JCExpression result;
        List<JCExpression> arguments;
        final JCExpression newEncl;
        
        if ((cbOpts & CB_ALIAS_ARGS) != 0) {
            if (instantiateQualfier != null 
                    && instantiateQualfier.expression != null) {
                if (instantiateQualfier.type == null) {
                    throw Assert.fail(MISSING_TYPE);
                }
                SyntheticName qualName = getQualifierName(basename);
                appendStatement(gen.makeVar(qualName, 
                        instantiateQualfier.type, 
                        instantiateQualfier.expression));
                newEncl = qualName.makeIdent();
            } else {
                newEncl = null;
            }
            arguments = List.<JCExpression>nil();
            int argumentNum = 0;
            for (ExpressionAndType argumentAndType : argumentsAndTypes) {
                SyntheticName name = getArgumentName(basename, argumentNum);
                if (argumentAndType.type == null) {
                    throw Assert.fail(MISSING_TYPE);
                }
                if ((cbOpts & CB_ALIAS_ARGS) != 0) {
                    appendStatement(gen.makeVar(name, 
                            argumentAndType.type, 
                            argumentAndType.expression));
                }
                arguments = arguments.append(name.makeIdent());
                argumentNum++;
            }
            
        } else {
            newEncl = this.instantiateQualfier != null ? this.instantiateQualfier.expression : null;
            arguments = ExpressionAndType.toExpressionList(this.argumentsAndTypes);
        }
        if (haveLocation) {
            gen.at(this.location);
        }
        switch (kind) {
        case APPLY:
            result = gen.make().Apply(this.typeargs.toList(), this.methodOrClass, arguments);
            break;
        case NEW:
            result = gen.make().NewClass(newEncl, null, this.methodOrClass, arguments, classDefs);
            break;
        case ARRAY_READ:
            result = gen.make().Indexed(this.methodOrClass, arguments.head);
            break;
        case ARRAY_WRITE:
            result = gen.make().Assign(gen.make().Indexed(this.methodOrClass, arguments.head), arguments.tail.head);
            break;
        case NEW_ARRAY:
            // methodOrClass must be a ArrayType, so we get the element type out
            JCExpression elementTypeExpr = ((JCTree.JCArrayTypeTree)this.methodOrClass).elemtype;
            if(arrayInstanceReifiedType == null){
                result = gen.make().NewArray(elementTypeExpr, List.of(arguments.head), null);
                if(arrayInstanceCast != null){
                    result = gen.make().TypeCast(arrayInstanceCast, result);
                }
            }else{
                List<JCExpression> dimensions = List.nil();
                if(arrayInstanceDimensions > 1){
                    for(int i=1;i<arrayInstanceDimensions;i++){
                        dimensions = dimensions.prepend(gen.makeInteger(0));
                    }
                }
                dimensions = dimensions.prepend(arguments.head);
                dimensions = dimensions.prepend(arrayInstanceReifiedType);
                result = gen.makeUtilInvocation("makeArray", dimensions, null);
            }
            if(arguments.tail.nonEmpty()){
                // must fill it
                result = gen.makeUtilInvocation("fillArray", List.of(result, arguments.tail.head), null);
            }
            break;
        case FIELD_READ:
            result = this.methodOrClass;
            break;
        
        default:
            throw Assert.fail();
        }
        if ((cbOpts & CB_LET) != 0) {
            if (voidMethod) {
                result = gen.make().LetExpr(statements.toList().append(gen.make().Exec(result)), gen.makeNull());
            } else if (!statements.isEmpty()) {
                result = gen.make().LetExpr(statements.toList(), result);
            }
        }
        
        return result;
    }

    private SyntheticName getArgumentName(Naming.SyntheticName basename, int argumentNum) {
        SyntheticName name = basename.suffixedBy(Suffix.$arg$, argumentNum);
        return name;
    }

    private SyntheticName getQualifierName(Naming.SyntheticName basename) {
        SyntheticName qualName = basename.suffixedBy(Suffix.$qual$);
        return qualName;
    }

    public void voidMethod(boolean voidMethod) {
        this.voidMethod = voidMethod;
    }

    public void javaArrayInstanceIsGeneric(JCExpression reifiedType, int dimensions) {
        this.arrayInstanceReifiedType = reifiedType;
        this.arrayInstanceDimensions = dimensions;
    }

    public void javaArrayInstanceNeedsCast(JCExpression requiredType) {
        this.arrayInstanceCast = requiredType;
    }
}
