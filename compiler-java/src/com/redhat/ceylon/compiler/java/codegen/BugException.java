package com.redhat.ceylon.compiler.java.codegen;

import java.util.Arrays;

import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.sun.tools.javac.tree.JCTree.JCExpression;

/**
 * <p>Thrown when the code generator is in a seemingly impossible situation 
 * (i.e. failed assertion of some kind) when it's not feasible to 
 * return {@link AbstractTransformer#makeErroneous(Node, String) JCErroneous}
 * (because there's no Node to hand, or the method does not return 
 * {@code JCExpression}). Returning {@code JCErroneous} is generally 
 * preferable since it results it just one downstream javac error, whereas 
 * failing to generate a declaration will cause downstream javac errors 
 * for every use site</p>
 * 
 * <h3>At {@code throw} sites</h3>
 * <p>The various {@code static} 
 * factory methods provide convenient ways to create instances for 
 * common "impossible" circumstances.</p>
 * 
 * <h3>At {@code catch} sites</h3>
 * <p>If there is no {@link Node} available at the {@code throw} site, 
 * the exception can be {@linkplain #associate(Node) associated} 
 * with one when it is caught.</p>
 * 
 * <p>{@link #addError(Node)} is used to add an error in to the Ceylon tree 
 * for subsequent reporting via the {@code Log}. 
 * Alternativly instances can be conveniently transformed into 
 * a {@code JCErroneous}. In either case it is only possible to add the err 
 * to the Ceylon tree once.</p> 
 * 
 * @see AbstractTransformer#makeErroneous(Node, String)
 */
public class BugException extends RuntimeException {

    private Node node;
    
    private boolean attached;
    
    public BugException() {
        this(null);
    }
    
    public BugException(String message) {
        this(message, null);
    }
    
    public BugException(String message, Exception cause) {
        this(null, message, cause);
    }
    
    public BugException(Node node, String message) {
        this(node, message, null);
    }
    
    public BugException(Node node, String message, Exception cause) {
        super(message == null ? "assertion failed" : message, cause);
        // This is nasty: When we print the errors out following codegen
        // we don't want to dump the whole stack, just the first frame
        // the factory methods would occupy their own uninformative frame
        // so filter out the first frames until we reach one which is not 
        // from our class
        StackTraceElement[] stackTrace = getStackTrace();
        String myName = BugException.class.getName();
        int from = 0;
        for (StackTraceElement frame : stackTrace) {
            if (!myName.equals(frame.getClassName())) {
                break;
            }
            from++;
        }
        setStackTrace(Arrays.copyOfRange(stackTrace, from, stackTrace.length));
        this.node = node;
    }
    
    /** 
     * Associate this exception with the given node if this exception is 
     * not already associated with a node. 
     */
    public void associate(Node node) {
        if (this.node == null) {
            this.node = node;
        }
    }

    private Node bestNode(Node fallbackNode) {
        return this.node != null ? this.node : fallbackNode;
    }
    
    /**
     * Add an error to the a node. A {@link CodeGenError} 
     * is created with the exception's message and the exception as cause, 
     * and is added to the assoicated node (if there is one), and otherwise 
     * the given node.
     * @param fallbackNode
     */
    public void addError(Node fallbackNode) {
        if (!this.attached) {
            Node bestNode = bestNode(fallbackNode);
            bestNode.addError(new CodeGenError(bestNode, getMessage(), this));
            this.attached = true;
        }
    }
    
    /**
     * Returns an erroneous node based on the exceptions message, having 
     * {@linkplain #addError(Node) added an error to the tree}.
     * @param gen
     * @param fallbackNode
     * @return
     */
    public JCExpression makeErroneous(AbstractTransformer gen, Node fallbackNode) {
        addError(fallbackNode);
        return gen.makeErroneous(bestNode(fallbackNode), getMessage());
    }
    
    /**
     * <p>Used at the end of a {@code if (x instanceof Foo)}/{@code else if (x instanceof Bar)} 
     * chain on Node types</pre>
     */
    public static BugException unhandledNodeCase(Node node) {
        return new BugException("unhandled node type " + node.getNodeType());
    }
    
    /**
     * <p>Used at the end of a {@code if (x instanceof Foo)}/{@code else if (x instanceof Bar)}
     * chain on declaration types</p>
     */
    public static BugException unhandledDeclarationCase(Declaration d) {
        return unhandledDeclarationCase(d, null);
    }
    public static BugException unhandledDeclarationCase(Declaration d, Node node) {
        return new BugException(node, "unhandled declaration " + d.getQualifiedNameString() + " with type " + (d == null ? "null" : d.getClass().getSimpleName()));
    }
    
    /**
     * <p>Used as the default case of an enum {@code switch} or at the end of a 
     * {@code if (x instanceof Foo)}/{@code else if (x instanceof Bar)}
     * chain on enum elements.</p>
     */
    public static <E extends Enum<E>> BugException unhandledEnumCase(Enum<E> element) {
        return new BugException("unhandled enum case " + element);
    }
    
    /**
     * Used at the end of the 
     * {@code if (x instanceof Foo)}/{@code else if (x instanceof Bar)} chain
     */
    public static BugException unhandledCase(Object o) {
        return new BugException("unhandled case " + (o == null ? "null" : o.getClass().toString()));
    }
}
