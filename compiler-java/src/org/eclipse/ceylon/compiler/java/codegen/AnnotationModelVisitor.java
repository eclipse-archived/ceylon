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

import static org.eclipse.ceylon.model.typechecker.model.ModelUtil.isBooleanFalse;
import static org.eclipse.ceylon.model.typechecker.model.ModelUtil.isBooleanTrue;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.ceylon.common.Backend;
import org.eclipse.ceylon.compiler.java.codegen.recovery.Errors;
import org.eclipse.ceylon.compiler.typechecker.tree.Node;
import org.eclipse.ceylon.compiler.typechecker.tree.Tree;
import org.eclipse.ceylon.compiler.typechecker.tree.Visitor;
import org.eclipse.ceylon.compiler.typechecker.tree.Tree.AnyMethod;
import org.eclipse.ceylon.compiler.typechecker.tree.Tree.Term;
import org.eclipse.ceylon.model.typechecker.model.Class;
import org.eclipse.ceylon.model.typechecker.model.Declaration;
import org.eclipse.ceylon.model.typechecker.model.Function;
import org.eclipse.ceylon.model.typechecker.model.Parameter;
import org.eclipse.ceylon.model.typechecker.model.Type;
import org.eclipse.ceylon.model.typechecker.model.TypeDeclaration;
import org.eclipse.ceylon.model.typechecker.model.Unit;
import org.eclipse.ceylon.model.typechecker.model.Value;

/**
 * Visitor which inspects annotation constructors.
 * 
 * @author tom
 */
public class AnnotationModelVisitor extends Visitor {
    private java.util.List<AnnotationFieldName> fieldNames = new ArrayList<AnnotationFieldName>();
    /** The annotation constructor we are currently visiting */
    private AnyMethod annotationConstructor;
    /** The instantiation in the body of the constructor, or in its default parameters */
    private AnnotationInvocation instantiation;
    private List<AnnotationInvocation> nestedInvocations;
    private boolean spread;
    private boolean checkingArguments;
    private boolean checkingDefaults;
    private AnnotationTerm term;
    private boolean checkingInvocationPrimary;
    private CollectionLiteralAnnotationTerm elements;
    private Errors errors;
    
    public AnnotationModelVisitor(CeylonTransformer gen) {
        this.errors = gen.errors();
    }

    private void push(AnnotationFieldName parameter) {
        fieldNames.add(parameter);
    }
    
    private AnnotationFieldName pop() {
        return fieldNames.remove(fieldNames.size()-1);
    }
    
    public Parameter parameter() {
        return fieldNames.get(fieldNames.size()-1).getAnnotationField();
    }
    
    @Override
    public void handleException(Exception e, Node node) {
        if (e instanceof RuntimeException) {
            throw (RuntimeException)e;
        } else {
            throw new RuntimeException(e);
        }
    }
    
    public static boolean isAnnotationConstructor(AnyMethod def) {
        return isAnnotationConstructor(def.getDeclarationModel());
    }
    
    public static boolean isAnnotationConstructor(Declaration def) {
        return def.isToplevel()
            && def instanceof Function
            && def.isAnnotation();
    }

    public static boolean isAnnotationClass(Tree.ClassOrInterface def) {
        return isAnnotationClass(def.getDeclarationModel());
    }

    public static boolean isAnnotationClass(Declaration declarationModel) {
        return declarationModel instanceof Class
            && declarationModel.isAnnotation();
    }
    
    /*protected void checkForCycle(Node node, final Declaration decl, final AnnotationInvocation invocation, Set<Declaration> s) {
        Declaration d = decl;
        while (d instanceof Function) {
            if (!s.add(d)) {
                node.addError("recursive annotation constructor: '"+decl.getName()+"' invokes itself");
                break;
            }
            d = ((AnnotationInvocation)((Function)d).getAnnotationConstructor()).getPrimary();
        }
        for (AnnotationConstructorParameter param : invocation.getConstructorParameters()) {
            if (param.getDefaultArgument() instanceof InvocationAnnotationTerm) {
                InvocationAnnotationTerm t = (InvocationAnnotationTerm)param.getDefaultArgument();
                /*if (!s.add(t.getInstantiation().getPrimary())) {
                    node.addError("recursive annotation constructor: '"+decl.getName()+"' invokes itself");
                    break;
                }* /
                checkForCycle(node, t.getInstantiation().getPrimary(), t.getInstantiation(), s);
            }
        }
    }*/
    
    @Override
    public void visit(Tree.MethodDefinition d) {
        if (errors.hasAnyError(d)) {
            return;
        }
        if (isAnnotationConstructor(d)) {
            annotationConstructor = d;
            instantiation = (AnnotationInvocation)d.getDeclarationModel().getAnnotationConstructor();
            
        }
        super.visit(d);
        if (isAnnotationConstructor(d)) {
            instantiation = null;
            annotationConstructor = null;
        }
    }
    
    @Override
    public void visit(Tree.MethodDeclaration d) {
        if (errors.hasAnyError(d)) {
            return;
        }
        if (isAnnotationConstructor(d)
                && d.getSpecifierExpression() != null) {
            annotationConstructor = d;
            instantiation = (AnnotationInvocation)d.getDeclarationModel().getAnnotationConstructor();
        }
        super.visit(d);
        if (isAnnotationConstructor(d)
                && d.getSpecifierExpression() != null) {
            instantiation = null;
            annotationConstructor = null;
        }
    }
    
    @Override
    public void visit(Tree.Statement d) {
        if (annotationConstructor != null) {
            if (!(annotationConstructor instanceof Tree.MethodDefinition 
                        && d instanceof Tree.Return)
                    && !(d instanceof Tree.AttributeDeclaration
                    && ((Tree.AttributeDeclaration)d).getDeclarationModel().isParameter())
                    && !d.equals(annotationConstructor)) {
                d.addError("compiler bug: annotation constructors may only contain a return statement", Backend.Java);
            }
        }
        super.visit(d);
    }
    
    @Override
    public void visit(Tree.AnnotationList al) {
        // Ignore statements in annotation lists
    }
    
    @Override
    public void visit(Tree.Parameter p) {
        
        if (annotationConstructor != null) {
            // Find the ACP for this parameter
            AnnotationConstructorParameter acp = instantiation.findConstructorParameter(p.getParameterModel());
            push(acp);
            //super.visit(p);
            Tree.SpecifierOrInitializerExpression defaultArgument = Decl.getDefaultArgument(p);
            if (defaultArgument != null) {
                defaultedParameter(defaultArgument, acp);
            }
            else if (p.getParameterModel().isSequenced() 
                    && !p.getParameterModel().isAtLeastOne()) {
                acp.setDefaultArgument(new CollectionLiteralAnnotationTerm(getCollectionLiteralFactory(p)));
            }
            pop();
        }
        // Ignore statements in parameters
    }
    
    
    public void defaultedParameter(Tree.SpecifierOrInitializerExpression d, AnnotationConstructorParameter annotationConstructorParameter) {
        if (annotationConstructor != null) {
            Declaration t = d.getUnit().getTrueValueDeclaration();
            Declaration f = d.getUnit().getFalseValueDeclaration();
            Term term = d.getExpression().getTerm();
            if (term instanceof Tree.InvocationExpression) {
                Tree.Primary primary = ((Tree.InvocationExpression)term).getPrimary();
                if (primary instanceof Tree.BaseMemberOrTypeExpression
                        && (isAnnotationConstructor( ((Tree.BaseMemberOrTypeExpression)primary).getDeclaration())
                          || isAnnotationClass( ((Tree.BaseMemberOrTypeExpression)primary).getDeclaration()))) {
                    final AnnotationInvocation prevInstantiation = this.instantiation;
                    this.instantiation = new AnnotationInvocation();
                    if (isAnnotationConstructor( ((Tree.BaseMemberOrTypeExpression)primary).getDeclaration())) {
                        Function constructor = (Function)((Tree.BaseMemberOrTypeExpression)primary).getDeclaration();
                        instantiation.setConstructorDeclaration(constructor);
                        for (AnnotationConstructorParameter x : ((AnnotationInvocation)constructor.getAnnotationConstructor()).getConstructorParameters()) {
                            instantiation.addConstructorParameter(x);
                        }
                    }
                    checkingDefaults = true;
                    
                    super.visit(d);
                    
                    annotationConstructorParameter.setDefaultArgument(this.term);
                    
                    this.term = null;
                    checkingDefaults = false;
                    this.instantiation = prevInstantiation;
                } else {
                    errorDefaultedParameter(d);
                }
            } else if (term instanceof Tree.Literal
                    || (term instanceof Tree.NegativeOp && ((Tree.NegativeOp)term).getTerm() instanceof Tree.Literal) 
                    || (term instanceof Tree.BaseMemberExpression
                    && (((Tree.BaseMemberExpression)term).getDeclaration().equals(t)
                        || ((Tree.BaseMemberExpression)term).getDeclaration().equals(f)
                        || ((Tree.BaseMemberExpression)term).getDeclaration().isParameter()
                        || Decl.isAnnotatableCase((Tree.BaseMemberExpression)term)))) {
                checkingDefaults = true;
                
                super.visit(d);
                
                annotationConstructorParameter.setDefaultArgument(this.term);
                this.term = null;
                checkingDefaults = false;
            } else if (term instanceof Tree.Tuple
                    || term instanceof Tree.SequenceEnumeration) {
                // TODO Tuples and SequenceEnumerations of the above cases should also be allowed
                checkingDefaults = true;
                
                super.visit(d);
                
                annotationConstructorParameter.setDefaultArgument(this.term);
                this.term = null;
                checkingDefaults = false;
            } else {
                errorDefaultedParameter(d);
            }
        }
    }

    private void errorDefaultedParameter(Node d) {
        d.addError("compiler bug: only literals, true, false, and annotation class instantiations are permitted as annotation parameter defaults", Backend.Java);
    }
    
    @Override
    public void visit(Tree.InvocationExpression invocation) {
        if (annotationConstructor != null) {
            
            final AnnotationInvocation prevInstantiation = this.instantiation;
            if (this.checkingArguments) {
                this.instantiation = new AnnotationInvocation();
            }
            
            this.checkingInvocationPrimary = true;
            invocation.getPrimary().visit(this);
            this.checkingInvocationPrimary = false;
            
            if (invocation.getPositionalArgumentList() != null) {
                invocation.getPositionalArgumentList().visit(this);
            }
            if (invocation.getNamedArgumentList() != null) {
                invocation.getNamedArgumentList().visit(this);
            }
            InvocationAnnotationTerm invocationTerm = new InvocationAnnotationTerm();
            invocationTerm.setInstantiation(instantiation);
            if (this.checkingArguments) {
                if (this.nestedInvocations == null) {
                    this.nestedInvocations = new ArrayList<AnnotationInvocation>();
                }
                this.nestedInvocations.add(this.instantiation);
                this.instantiation = prevInstantiation;
            }
            this.term = invocationTerm;
        } else {
            super.visit(invocation);
        }
    }
    
    @Override
    public void visit(Tree.NamedArgumentList argumentList) {
        final boolean prevCheckingArguments = this.checkingArguments;
        this.checkingArguments = true;
        super.visit(argumentList);
        this.checkingArguments = prevCheckingArguments;
    }
    
    @Override
    public void visit(Tree.PositionalArgumentList argumentList) {
        final boolean prevCheckingArguments = this.checkingArguments;
        this.checkingArguments = true;
        super.visit(argumentList);
        this.checkingArguments = prevCheckingArguments;
    }
    
    @Override
    public void visit(Tree.StringLiteral literal) {
        if (annotationConstructor != null) {
            if (checkingArguments || checkingDefaults){
                LiteralAnnotationTerm argument = new StringLiteralAnnotationTerm(ExpressionTransformer.literalValue(literal));
                appendLiteralArgument(literal, argument);
            }
        }
    }
    
    @Override
    public void visit(Tree.CharLiteral literal) {
        if (annotationConstructor != null) {
            if (checkingArguments || checkingDefaults){
                LiteralAnnotationTerm argument = new CharacterLiteralAnnotationTerm(ExpressionTransformer.literalValue(literal));
                appendLiteralArgument(literal, argument);
            }
        }
    }
    
    @Override
    public void visit(Tree.FloatLiteral literal) {
        if (annotationConstructor != null) {
            if (checkingArguments || checkingDefaults){
                try {
                    LiteralAnnotationTerm argument = new FloatLiteralAnnotationTerm(ExpressionTransformer.literalValue(literal));
                    appendLiteralArgument(literal, argument);
                } catch (ErroneousException e) {
                    // Ignore it: The ExpressionTransformer will produce an error later in codegen
                }
            }
        }
    }
    
    @Override
    public void visit(Tree.NaturalLiteral literal) {
        if (annotationConstructor != null) {
            if (checkingArguments || checkingDefaults){
                try {
                    LiteralAnnotationTerm argument = new IntegerLiteralAnnotationTerm(ExpressionTransformer.literalValue(literal).longValue());
                    appendLiteralArgument(literal, argument);
                } catch (ErroneousException e) {
                    // Ignore it: The ExpressionTransformer will produce an error later in codegen
                }
            }
        }
    }
    
    @Override
    public void visit(Tree.NegativeOp op) {
        if (annotationConstructor != null) {
            if (checkingArguments || checkingDefaults){
                try {
                    if (op.getTerm() instanceof Tree.NaturalLiteral) {
                        LiteralAnnotationTerm argument = new IntegerLiteralAnnotationTerm(ExpressionTransformer.literalValue(op).longValue());
                        appendLiteralArgument(op, argument);
                    } else if (op.getTerm() instanceof Tree.FloatLiteral) {
                        LiteralAnnotationTerm argument = new FloatLiteralAnnotationTerm(-ExpressionTransformer.literalValue((Tree.FloatLiteral)op.getTerm()));
                        appendLiteralArgument(op, argument);
                    }
                } catch (ErroneousException e) {
                    // Ignore it: The ExpressionTransformer will produce an error later in codegen
                }
            }
        }
    }
    
    @Override
    public void visit(Tree.MetaLiteral literal) {
        if (annotationConstructor != null) {
            if (checkingArguments || checkingDefaults){
                LiteralAnnotationTerm argument = new DeclarationLiteralAnnotationTerm(ExpressionTransformer.getSerializedMetaLiteral(literal));
                appendLiteralArgument(literal, argument);
            }
        }
    }
    
    @Override
    public void visit(Tree.Tuple literal) {
        if (annotationConstructor != null) {
            if (checkingArguments || checkingDefaults){
                // Continue the visit to collect the elements
                CollectionLiteralAnnotationTerm prevElements = startCollection(literal);
                literal.visitChildren(this);
                endCollection(prevElements, literal);
            }
        }
    }
    
    @Override
    public void visit(Tree.SequenceEnumeration literal) {
        if (annotationConstructor != null) {
            if (checkingArguments || checkingDefaults){
                CollectionLiteralAnnotationTerm prevElements = startCollection(literal);
                literal.visitChildren(this);
                endCollection(prevElements, literal);
            }
        }
    }

    private void endCollection(CollectionLiteralAnnotationTerm prevElements, Tree.Term t) {
        this.term = this.elements;
        this.elements = prevElements;
        appendLiteralArgument(t, (CollectionLiteralAnnotationTerm)this.term);
    }

    private CollectionLiteralAnnotationTerm startCollection(Tree.Term t) {
//        Unit unit = t.getUnit();
        // Continue the visit to collect the elements
        LiteralAnnotationTerm factory = getCollectionLiteralFactory(t);
        if (factory==null) {
            return null;
        }
        CollectionLiteralAnnotationTerm result = this.elements;
        this.elements = new CollectionLiteralAnnotationTerm(factory);
        return result;
    }

    private LiteralAnnotationTerm getCollectionLiteralFactory(Node err) {
        Unit unit = err.getUnit();
        Type iteratedType = unit.getIteratedType(parameter().getType());
        if (iteratedType.isString()) {
            return StringLiteralAnnotationTerm.FACTORY;
        } else if (iteratedType.isInteger()) {
            return IntegerLiteralAnnotationTerm.FACTORY;
        } else if (iteratedType.isCharacter()) {
            return CharacterLiteralAnnotationTerm.FACTORY;
        } else if (iteratedType.isBoolean()) {
            return BooleanLiteralAnnotationTerm.FACTORY;
        } else if (iteratedType.isFloat()) {
            return FloatLiteralAnnotationTerm.FACTORY;
        } else if (Decl.isAnnotatableCaseType(iteratedType)) {
            return ObjectLiteralAnnotationTerm.FACTORY;
        } else if (Decl.isAnnotationClass(iteratedType.getDeclaration())) {
            err.addError("compiler bug: iterables of annotation classes or annotation constructors not supported as literal " 
            		+ (checkingDefaults ? "defaulted parameters" : "arguments"), Backend.Java);
            return null;
        } else if (iteratedType.isSubtypeOf(((TypeDeclaration)unit.getLanguageModuleDeclarationDeclaration("Declaration")).getType())) {
            return DeclarationLiteralAnnotationTerm.FACTORY;
        } else {
            throw new RuntimeException();
        }
    }
    
    @Override
    public void visit(Tree.Expression term) {
        if (annotationConstructor != null) {
            term.visitChildren(this);
        }
    }
    
    @Override
    public void visit(Tree.Term term) {
        if (annotationConstructor != null && !checkingDefaults) {
            term.addError("compiler bug: unsupported term " + term.getClass().getSimpleName(), Backend.Java);
        }
    }
    
    @Override
    public void visit(Tree.BaseMemberExpression bme) {
        if (annotationConstructor != null) {
            Declaration declaration = bme.getDeclaration();
            if (checkingInvocationPrimary 
                    && isAnnotationConstructor(bme.getDeclaration())) {
                Function ctor = (Function)bme.getDeclaration();
                instantiation.setPrimary(ctor);
                if (ctor.getAnnotationConstructor() != null
                        && ctor.getAnnotationConstructor() != instantiation) {
                    for (AnnotationConstructorParameter p : ((AnnotationInvocation)ctor.getAnnotationConstructor()).getConstructorParameters()) {
                        instantiation.addConstructorParameter(p);
                    }
                }
            } else if (checkingArguments || checkingDefaults) {
                if (declaration instanceof Value && ((Value)declaration).isParameter()) {
                    Value constructorParameter = (Value)declaration;
                    ParameterAnnotationTerm a = new ParameterAnnotationTerm();
                    a.setSpread(spread);
                    // XXX Is this right?
                    a.setSourceParameter(constructorParameter.getInitializerParameter());
                    this.term = a;
                } else if (isBooleanTrue(declaration)) {
                    LiteralAnnotationTerm argument = new BooleanLiteralAnnotationTerm(true);
                    appendLiteralArgument(bme, argument);
                } else if (isBooleanFalse(declaration)) {
                    LiteralAnnotationTerm argument = new BooleanLiteralAnnotationTerm(false);
                    appendLiteralArgument(bme, argument);
                } else if (bme.getUnit().isEmptyType(bme.getTypeModel())
                        && bme.getUnit().isIterableType(bme.getTypeModel())
                        && elements == null) {
                    // If we're dealing with an iterable, empty means empty collection, not object
                    endCollection(startCollection(bme), bme);
                } else if (Decl.isAnnotatableCase(bme)) {
                    LiteralAnnotationTerm argument = new ObjectLiteralAnnotationTerm(bme.getTypeModel());
                    appendLiteralArgument(bme, argument);
                } else {
                    bme.addError("compiler bug: unsupported base member expression in annotation constructor", Backend.Java);
                }
            } else {
                bme.addError("compiler bug: unsupported base member expression in annotation constructor", Backend.Java);
            }
        }
    }
    
    /** 
     * Records <strong>either</strong> 
     * a literal argument to the annotation class instantiation:
     * <pre>
     *    ... => AnnotationClass("", 1, true, 1.0, 'x')
     * </pre>
     * <strong>Or</strong> a literal default argument in an annotation constructor:
     * <pre>
     *     AnnotationClass ctor(String s="", Integer i=1,
     *             Boolean b=true, Float f=1.0,
     *             Character c='x') => ...
     * </pre>
     * Literal is in the Javac sense.
     */
    private LiteralAnnotationTerm appendLiteralArgument(Node bme, LiteralAnnotationTerm argument) {
        if (spread) {
            bme.addError("compiler bug: spread static arguments not supported", Backend.Java);
        }
        if (this.elements != null) {
           this.elements.addElement(argument);
        } else {
            this.term = argument;
        }
        return argument;
    }
    
    @Override
    public void visit(Tree.BaseTypeExpression bte) {
        if (annotationConstructor != null) {
            if (isAnnotationClass(bte.getDeclaration())) {
                instantiation.setPrimary((Class)bte.getDeclaration());
            } else {
                bte.addError("compiler bug: not an annotation class", Backend.Java);
            }
        }
    }
    
    @Override
    public void visit(Tree.PositionalArgument argument) {
        if (annotationConstructor != null
                && this.elements == null) {
            argument.addError("compiler bug: unsupported positional argument", Backend.Java);
        }
        super.visit(argument);
    }
    
    @Override
    public void visit(Tree.SpreadArgument argument) {
        if (annotationConstructor != null
                && this.elements == null) {
            spread = true;
            argument.getExpression().visit(this);
            AnnotationArgument aa = new AnnotationArgument();
            aa.setParameter(argument.getParameter());
            aa.setTerm(this.term);
            instantiation.getAnnotationArguments().add(aa);
            this.term = null;
            spread = false;
        } else {
            super.visit(argument);
        }
    }
    
    @Override
    public void visit(Tree.ListedArgument argument) {
        if (annotationConstructor != null
                && this.elements == null) {
            AnnotationArgument aa = new AnnotationArgument();
            aa.setParameter(argument.getParameter());
            push(aa);
            argument.getExpression().visit(this);
            aa.setTerm(this.term);
            instantiation.getAnnotationArguments().add(aa);
            this.term = null;
            pop();
        } else {
            super.visit(argument);
        }
    }
    
    @Override
    public void visit(Tree.NamedArgument argument) {
        if (annotationConstructor != null
                && this.elements == null) {
            argument.addError("compiler bug: unsupported named argument", Backend.Java);
        }
        super.visit(argument);
    }
    
    @Override
    public void visit(Tree.SpecifiedArgument argument) {
        if (annotationConstructor != null
                && this.elements == null) {
            AnnotationArgument aa = new AnnotationArgument();
            aa.setParameter(argument.getParameter());
            push(aa);
            argument.getSpecifierExpression().visit(this);
            
            aa.setTerm(this.term);
            instantiation.getAnnotationArguments().add(aa);
            this.term = null;
            pop();
        } else {
            super.visit(argument);
        }
    }
}

