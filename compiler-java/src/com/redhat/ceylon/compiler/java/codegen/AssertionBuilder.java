package com.redhat.ceylon.compiler.java.codegen;

import java.util.Iterator;

import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.langtools.tools.javac.tree.JCTree;
import com.redhat.ceylon.langtools.tools.javac.tree.JCTree.JCExpression;
import com.redhat.ceylon.langtools.tools.javac.tree.JCTree.JCThrow;
import com.redhat.ceylon.langtools.tools.javac.tree.JCTree.Tag;
import com.redhat.ceylon.langtools.tools.javac.util.DiagnosticSource;
import com.redhat.ceylon.langtools.tools.javac.util.List;
import com.redhat.ceylon.langtools.tools.javac.util.ListBuffer;
import com.redhat.ceylon.langtools.tools.javac.util.Log;

/**
 * Helper for constructing {@code throw AssertionException()} statements
 * @author tom
 */
class AssertionBuilder {
    
    private String docText;
    private JCExpression fragment = null;
    private ListBuffer<ConditionDescription> conditions = new ListBuffer<ConditionDescription>();
    private Node node;
    private AbstractTransformer gen;
    private JCExpression wrapedException;
    private JCExpression violatedIs;
    private JCExpression violatedBinOp;
    

    public AssertionBuilder(AbstractTransformer gen, Node node) {
        this.gen = gen;
        this.node = node;
    }
    
    private JCExpression newline() {
        return gen.make().Apply(null, 
                gen.makeQualIdent(gen.makeIdent(gen.syms().systemType), "lineSeparator"), 
                List.<JCExpression>nil());
    }
    
    public AssertionBuilder append(JCExpression expr) {
        fragment = expr;
        return this;
    }
    
    private Tree.Annotation getAnnotation(Tree.AnnotationList al, String name) {
        if (al!=null) {
            for (Tree.Annotation a: al.getAnnotations()) {
                Tree.BaseMemberExpression p = (Tree.BaseMemberExpression) a.getPrimary();
                if (p!=null) {
                    if ( p.getIdentifier().getText().equals(name) ) {
                        return a;
                    }
                }
            }
        }
        return null;
    }
    
    private String getDocAnnotationText(Tree.Assertion ass) {
        String docText = null;
        Tree.Annotation doc = getAnnotation(ass.getAnnotationList(), "doc");
        if (doc != null) {
            Tree.Expression expression = null;
            if (doc.getPositionalArgumentList() != null) {
                Tree.PositionalArgument arg = doc.getPositionalArgumentList().getPositionalArguments().get(0);
                if(arg instanceof Tree.ListedArgument)
                    expression = ((Tree.ListedArgument) arg).getExpression();
                else
                    throw new BugException(arg, "argument to doc annotation cannot be a spread argument or comprehension: " + arg);
            } else if (doc.getNamedArgumentList() != null) {
                Tree.SpecifiedArgument arg = (Tree.SpecifiedArgument)doc.getNamedArgumentList().getNamedArguments().get(0);
                expression = arg.getSpecifierExpression().getExpression();
            } else {
                // Impossible on a well-formed tree
                return null;
            }
            Tree.Literal literal = (Tree.Literal)expression.getTerm();
            docText = literal.getText();
        } else if (ass.getAnnotationList() != null
                && ass.getAnnotationList().getAnonymousAnnotation() != null) {
            docText = ass.getAnnotationList().getAnonymousAnnotation().getStringLiteral().getText();
        }
        return docText;
    }
    
    public AssertionBuilder assertionDoc(Tree.Assertion ass) {
        return assertionDoc(getDocAnnotationText(ass));
    }
    
    public AssertionBuilder assertionDoc(String docText) {
        this.docText = docText;
        return this;
    }
    
    private AssertionBuilder appendCondition(ConditionDescription cond) {
        this.conditions.add(cond);
        return this;
    }
    
    public AssertionBuilder appendViolatedCondition(String sourceCode) {
        return appendCondition(new ConditionDescription("violated", sourceCode));
    }
    
    /**
     * Gets the source code corresponding to the given node
     */
    private String getSourceCode(Node node) {
        StringBuilder sb = new StringBuilder();
        DiagnosticSource source = new DiagnosticSource(gen.gen().getFileObject(), Log.instance(gen.gen().getContext()));
        int startLine = node.getToken().getLine();
        int endLine = node.getEndToken().getLine();
        for (int lineNumber = startLine; lineNumber <= endLine; lineNumber++) {
            int startPos = gen.getMap().getPosition(lineNumber, 1);
            String line = source.getLine(startPos);
            if (lineNumber == endLine) {
                line = line.substring(0,  node.getEndToken().getCharPositionInLine() + node.getEndToken().getText().length());
            }
            if (lineNumber == startLine) {
                line = line.substring(node.getToken().getCharPositionInLine());
            }
            sb.append(line).append("\n");
        }
        return sb.substring(0, sb.length() - 1);
    }
    
    class ConditionDescription {
        private final String state;
        private final String sourceCode;
        public ConditionDescription(String state, String sourceCode) {
            super();
            this.state = state;
            this.sourceCode = sourceCode;
        }
        
        public JCExpression build(JCExpression prefix) {
            JCExpression result = prefix;
            result = result == null ? newline() : gen.make().Binary(Tag.PLUS, result, newline());
            result = gen.make().Binary(Tag.PLUS, result, gen.make().Literal("\t" + state+ " "));
            result = gen.make().Binary(Tag.PLUS, result, gen.make().Literal(sourceCode));
            return result;
        }
    }
    
    /**
     * Add an exception that the AssertionError should wrap.
     */
    public AssertionBuilder wrapException(JCExpression wrapedException) {
        this.wrapedException = wrapedException;
        return this;
    }
    
    public AssertionBuilder appendViolatedCondition(Tree.Condition condition) {
        return appendViolatedCondition(getSourceCode(condition));
    }
    
    public AssertionBuilder appendUnviolatedCondition(Tree.Condition condition) {
        return appendCondition(new ConditionDescription("unviolated", getSourceCode(condition)));
    }
    
    public AssertionBuilder appendUntestedCondition(Tree.Condition condition) {
        return appendCondition(new ConditionDescription("untested", getSourceCode(condition)));
    }
    
    public JCExpression buildPart() {
        JCExpression result = fragment;
        Iterator<ConditionDescription> iter = conditions.iterator();
        while (iter.hasNext()) {
            result = iter.next().build(result);
        }
        return result;
    }
    public JCExpression buildMessage() {
        JCExpression result = gen.make().Literal("Assertion failed");
        if (docText != null) {
            result = gen.at(node).Binary(JCTree.Tag.PLUS, result, gen.make().Literal(": " + docText));
        }
        result = gen.at(node).Binary(JCTree.Tag.PLUS, result, buildPart());
        if (violatedIs != null) {
            result = gen.at(node).Binary(JCTree.Tag.PLUS, result, violatedIs);
        }
        if (violatedBinOp != null) {
            result = gen.at(node).Binary(JCTree.Tag.PLUS, result, violatedBinOp);
        }
        return result;
    }
    
    public JCThrow buildThrow() {
        return gen.makeThrowAssertionException(buildMessage(), wrapedException != null ? gen.make().TypeCast(gen.make().Type(gen.syms().throwableType), wrapedException) : null);
    }

    /** 
     * Add extra information to the exception message when an {@code is} 
     * condition or operation violates the assertion. 
     */
    public AssertionBuilder violatedIs(boolean negated, JCExpression $reified$Type, JCExpression violatedIs) {
        this.violatedIs = violatedIs != null ? gen.utilInvocation().assertIsFailed(negated, $reified$Type, violatedIs) : null;
        return this;
    }

    public AssertionBuilder violatedBinOp(JCExpression leftName, JCExpression rightName) {
        this.violatedBinOp = gen.utilInvocation().assertBinOpFailed(leftName, rightName);
        return this;
    }
    
}