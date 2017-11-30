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
package org.eclipse.ceylon.compiler.java.codegen;

import org.eclipse.ceylon.compiler.java.codegen.Naming.SyntheticName;
import org.eclipse.ceylon.compiler.typechecker.tree.Node;
import org.eclipse.ceylon.langtools.tools.javac.code.Flags;
import org.eclipse.ceylon.langtools.tools.javac.tree.JCTree;
import org.eclipse.ceylon.langtools.tools.javac.tree.JCTree.JCClassDecl;
import org.eclipse.ceylon.langtools.tools.javac.tree.JCTree.JCExpression;
import org.eclipse.ceylon.langtools.tools.javac.tree.JCTree.JCStatement;
import org.eclipse.ceylon.langtools.tools.javac.util.List;
import org.eclipse.ceylon.langtools.tools.javac.util.ListBuffer;
import org.eclipse.ceylon.model.loader.NamingBase.Suffix;
import org.eclipse.ceylon.model.typechecker.model.Declaration;
import org.eclipse.ceylon.model.typechecker.model.Type;

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
        NEW_ARRAY_WITH,
        FIELD_READ
    }
    
    public static final int CB_ALIAS_ARGS = 1<<0;
    public static final int CB_LET = 1<<1;
    
    private static final String MISSING_TYPE = "Type expression required when evaluateArgumentsFirst()";
    
    private final AbstractTransformer gen;
    private Kind kind;
    
    private ListBuffer<JCExpression> typeargs = new ListBuffer<JCExpression>();
    /** The transformed argument expressions and their transformed type expressions */
    private ListBuffer<ExpressionAndType> argumentsAndTypes = new ListBuffer<ExpressionAndType>();

    private JCExpression methodOrClass;
    private ExpressionAndType instantiateQualfier;
    private int cbOpts;
    private Naming.SyntheticName basename;
    private boolean built = false;
    
    private final ListBuffer<JCStatement> statements = new ListBuffer<JCStatement>();
    private boolean voidMethod;
    
    private boolean haveLocation = false;
    private Node location;
    private JCClassDecl classDefs;
    private JCExpression arrayInstanceReifiedType;
    private JCExpression arrayInstanceCast;
    private int arrayInstanceDimensions;
    private boolean arrayWriteNeedsCast;
    private Type arrayType;
    
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
    
    public CallBuilder arrayWith(Type arrayType, JCExpression type) {
        this.methodOrClass = type;
        this.instantiateQualfier = null;
        this.kind = Kind.NEW_ARRAY_WITH;
        this.arrayType = arrayType; 
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
            throw new BugException("already built");
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
            throw new BugException("not yet built");
        }
        return statements.toList();
    }
    
    public JCExpression build() {
        if (built) {
            throw new BugException("already built");
        }
        built = true;
        JCExpression result;
        List<JCExpression> arguments;
        final JCExpression newEncl;
        
        if ((cbOpts & CB_ALIAS_ARGS) != 0) {
            if (instantiateQualfier != null 
                    && instantiateQualfier.expression != null) {
                if (instantiateQualfier.type == null) {
                    throw new BugException(MISSING_TYPE);
                }
                SyntheticName qualName = getQualifierName(basename);
                appendStatement(gen.makeVar(Flags.FINAL, qualName, 
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
                    throw new BugException(MISSING_TYPE);
                }
                if ((cbOpts & CB_ALIAS_ARGS) != 0) {
                    appendStatement(gen.makeVar(Flags.FINAL, name, 
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
            {
                JCExpression array;
                if(arrayWriteNeedsCast)
                    array = gen.make().TypeCast(gen.make().TypeArray(gen.make().Type(gen.syms().objectType)), this.methodOrClass);
                else
                    array = this.methodOrClass;
                result = gen.make().Assign(gen.make().Indexed(array, arguments.head), arguments.tail.head);
            }
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
                result = gen.utilInvocation().makeArray(dimensions);
            }
            if(arguments.tail.nonEmpty()){
                // must fill it
                result = gen.utilInvocation().fillArray(List.of(result, arguments.tail.head));
            }
            break;
        case NEW_ARRAY_WITH:
            Declaration d = arrayType.getDeclaration();
            if (d.equals(gen.typeFact().getJavaLongArrayDeclaration())) {
                result = gen.utilInvocation().toLongArray(arguments.head, arguments.tail);
            } else if (d.equals(gen.typeFact().getJavaIntArrayDeclaration())) {
                result = gen.utilInvocation().toIntArray(arguments.head, arguments.tail);
            } else if (d.equals(gen.typeFact().getJavaShortArrayDeclaration())) {
                result = gen.utilInvocation().toShortArray(arguments.head, arguments.tail);
            } else if (d.equals(gen.typeFact().getJavaCharArrayDeclaration())) {
                result = gen.utilInvocation().toCharArray(arguments.head, arguments.tail);
            } else if (d.equals(gen.typeFact().getJavaByteArrayDeclaration())) {
                result = gen.utilInvocation().toByteArray(arguments.head, arguments.tail);
            } else if (d.equals(gen.typeFact().getJavaBooleanArrayDeclaration())) {
                result = gen.utilInvocation().toBooleanArray(arguments.head, arguments.tail);
            } else if (d.equals(gen.typeFact().getJavaFloatArrayDeclaration())) {
                result = gen.utilInvocation().toFloatArray(arguments.head, arguments.tail);
            } else if (d.equals(gen.typeFact().getJavaDoubleArrayDeclaration())) {
                result = gen.utilInvocation().toDoubleArray(arguments.head, arguments.tail);
            } else {
                // it must be an ObjectArray
                
                result = gen.utilInvocation().toArray(arguments.tail.head,
                        // don't use makeClassLiteral, because it doesn't c.l.Object -> j.l.Object
                        gen.makeSelect(gen.makeJavaType(arrayType.getTypeArgumentList().get(0), AbstractTransformer.JT_NO_PRIMITIVES), "class"),
                        arguments.tail.tail);
            }
            break;
        case FIELD_READ:
            result = this.methodOrClass;
            break;
        
        default:
            throw BugException.unhandledEnumCase(kind);
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

    public void javaArrayWriteNeedsCast(boolean arrayWriteNeedsCast) {
        this.arrayWriteNeedsCast = arrayWriteNeedsCast;
    }
}
