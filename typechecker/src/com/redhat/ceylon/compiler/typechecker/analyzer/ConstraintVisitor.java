package com.redhat.ceylon.compiler.typechecker.analyzer;

import java.util.List;

import com.redhat.ceylon.compiler.typechecker.model.Class;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.MethodOrValue;
import com.redhat.ceylon.compiler.typechecker.model.Parameter;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.Unit;
import com.redhat.ceylon.compiler.typechecker.model.Value;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.PositionalArgument;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Term;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;

public class ConstraintVisitor extends Visitor {
    
    private static boolean isIllegalAnnotationParameterType(ProducedType pt) {
        if (pt!=null) {
            TypeDeclaration ptd = pt.getDeclaration();
            Unit unit = pt.getDeclaration().getUnit();
            if (!ptd.isAnnotation() &&
                    // FIXME: temporary
                    !ptd.equals(unit.getAnythingDeclaration()) &&
                    !ptd.equals(unit.getBooleanDeclaration()) &&
                    !ptd.equals(unit.getStringDeclaration()) &&
                    !ptd.equals(unit.getIntegerDeclaration()) &&
                    !ptd.equals(unit.getFloatDeclaration()) &&
                    !ptd.equals(unit.getCharacterDeclaration()) &&
                    !ptd.equals(unit.getIterableDeclaration()) &&
                    !ptd.equals(unit.getSequentialDeclaration()) &&
                    !pt.isSubtypeOf(getDeclarationDeclaration(unit).getType())) {
                return true;
            }
            if (ptd.equals(unit.getIterableDeclaration()) ||
                    ptd.equals(unit.getSequentialDeclaration())) {
                if (isIllegalAnnotationParameterType(unit.getIteratedType(pt))) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private void checkAnnotationParameter(Declaration a, Tree.Parameter pn) {
        Parameter p = pn.getParameterModel();
        ProducedType pt = p.getType();
        if (isIllegalAnnotationParameterType(pt)) {
            pn.addError("illegal annotation parameter type");
        }
        Tree.SpecifierOrInitializerExpression se = null;
        if (pn instanceof Tree.InitializerParameter) {
            se = ((Tree.InitializerParameter) pn).getSpecifierExpression();
        }
        else if (pn instanceof Tree.ParameterDeclaration) {
            Tree.TypedDeclaration td = ((Tree.ParameterDeclaration) pn).getTypedDeclaration();
            if (td instanceof Tree.MethodDeclaration) {
                se = ((Tree.MethodDeclaration) td).getSpecifierExpression();
            }
            else if (td instanceof Tree.AttributeDeclaration) {
                se = ((Tree.AttributeDeclaration) td).getSpecifierOrInitializerExpression();
            }
        }
        if (se!=null) {
            checkAnnotationArgument(a, se.getExpression(), pt);
        }
    }

    private void checkAnnotationArgument(Declaration a, Tree.Expression e, ProducedType pt) {
        if (e!=null) {
            Tree.Term term = e.getTerm();
            if (term instanceof Tree.Literal) {
                //ok
            }
            else if (term instanceof Tree.NegativeOp && 
                    ((Tree.NegativeOp) term).getTerm() instanceof Tree.Literal) {
                //ok
            }
            else if (term instanceof Tree.MetaLiteral) {
                //ok
            }
            else if (term instanceof Tree.Tuple) {
                Tree.SequencedArgument sa = ((Tree.Tuple) term).getSequencedArgument();
                if (sa!=null) {
                    for (PositionalArgument arg: sa.getPositionalArguments()) {
                        if (arg instanceof Tree.ListedArgument){
                            Tree.Expression expression = ((Tree.ListedArgument) arg).getExpression();
                            if (expression!=null) {
                                checkAnnotationArgument(a, expression, arg.getTypeModel());
                            }
                        }
                        else {
                            e.addError("illegal annotation argument: must be a literal value, metamodel reference, annotation instantiation, or parameter reference");
                        }
                    }
                }
            }
            else if (term instanceof Tree.SequenceEnumeration) {
                Tree.SequencedArgument sa = ((Tree.SequenceEnumeration) term).getSequencedArgument();
                if (sa!=null) {
                    for (Tree.PositionalArgument arg: sa.getPositionalArguments()){
                        if (arg instanceof Tree.ListedArgument){
                            Tree.Expression expression = ((Tree.ListedArgument) arg).getExpression();
                            if (expression!=null) {
                                checkAnnotationArgument(a, expression, arg.getTypeModel());
                            }
                        }
                        else {
                            e.addError("illegal annotation argument: must be a literal value, metamodel reference, annotation instantiation, or parameter reference");
                        }
                    }
                }
            }
            else if (term instanceof Tree.InvocationExpression) {
                checkAnnotationInstantiation(null, e, pt);
            }
            else if (term instanceof Tree.BaseMemberExpression) {
                Declaration d = ((Tree.BaseMemberExpression) term).getDeclaration();
                if (a!=null && d.isParameter()) {
                    if (!((MethodOrValue) d).getInitializerParameter().getDeclaration().equals(a)) {
                        e.addError("illegal annotation argument: must be a reference to a parameter of the annotation");
                    }
                }
                else if (d instanceof Value && 
                        (d.equals(d.getUnit().getTrueValueDeclaration())
                      || d.equals(d.getUnit().getFalseValueDeclaration()))) {
                    //ok
                }
                else {
                    e.addError("illegal annotation argument: must be a literal value, metamodel reference, annotation instantiation, or parameter reference");
                }
            }
            else {
                e.addError("illegal annotation argument: must be a literal value, metamodel reference, annotation instantiation, or parameter reference");
            }
        }
    }

    private static TypeDeclaration getAnnotationDeclaration(Unit unit) {
        return (TypeDeclaration) unit.getPackage().getModule()
                .getLanguageModule()
                .getDirectPackage("ceylon.language.model")
                .getMemberOrParameter(unit, "Annotation", null, false);
    }

    private static TypeDeclaration getDeclarationDeclaration(Unit unit) {
        return (TypeDeclaration) unit.getPackage().getModule()
                .getLanguageModule()
                .getDirectPackage("ceylon.language.model.declaration")
                .getMemberOrParameter(unit, "Declaration", null, false);
    }

    @Override
    public void visit(Tree.AnyClass that) {
        super.visit(that);
        Class c = that.getDeclarationModel();
        if (c.isAnnotation()) {
            if (c.isParameterized()) {
                that.addError("annotation class may not be a parameterized type");
            }
            /*if (c.isAbstract()) {
                that.addError("annotation class may not be abstract");
            }*/
            /*if (!c.isFinal()) {
                that.addError("annotation class must be final");
            }*/
            if (!c.getExtendedTypeDeclaration()
                    .equals(that.getUnit().getBasicDeclaration())) {
                that.addError("annotation class must directly extend Basic");
            }
            TypeDeclaration annotationDec = getAnnotationDeclaration(that.getUnit());
            if (!c.inherits(annotationDec)) {
                that.addError("annotation class must be a subtype of Annotation");
            }
            for (Tree.Parameter pn: that.getParameterList().getParameters()) {
                checkAnnotationParameter(c, pn);
            }
        }
    }

    @Override
    public void visit(Tree.AnyMethod that) {
        super.visit(that);
        Method a = that.getDeclarationModel();
        if (a.isAnnotation()) {
            Tree.Type type = that.getType();
            if (type!=null) {
                if (!type.getTypeModel().getDeclaration().isAnnotation()) {
                    type.addError("annotation constructor must return an annotation type");
                }
            }
            List<Tree.ParameterList> pls = that.getParameterLists();
            if (pls.size() == 1) {
                for (Tree.Parameter pn: pls.get(0).getParameters()) {
                    checkAnnotationParameter(a, pn);
                }
            }
            else {
                that.addError("annotation constructor must have exactly one parameter list");
            }
            if (that instanceof Tree.MethodDefinition) {
                Tree.Block block = ((Tree.MethodDefinition) that).getBlock();
                if (block.getStatements().size()==1) {
                    Tree.Statement s = block.getStatements().get(0);
                    if (s instanceof Tree.Return) {
                        Tree.Expression e = ((Tree.Return) s).getExpression();
                        checkAnnotationInstantiation(a, e, a.getType());
                    }
                    else {
                        s.addError("annotation constructor body must return an annotation instance");
                    }
                }
                else {
                    block.addError("annotation constructor body must have exactly one statement");
                }
            }
            else {
                Tree.SpecifierExpression se = ((Tree.MethodDeclaration) that).getSpecifierExpression();
                if (se!=null) {
                    checkAnnotationInstantiation(a, se.getExpression(), a.getType());
                }
            }
        }
    }

    private void checkAnnotationInstantiation(Method a, Tree.Expression e, ProducedType pt) {
        if (e!=null) {
            Term term = e.getTerm();
            if (term instanceof Tree.InvocationExpression) {
                Tree.InvocationExpression ie = (Tree.InvocationExpression) term;
                /*if (!ie.getTypeModel().isExactly(pt)) {
                    ie.addError("annotation constructor must return exactly the annotation type");
                }*/
                if (!(ie.getPrimary() instanceof Tree.BaseTypeExpression)
                        && (!(ie.getPrimary() instanceof Tree.BaseMemberExpression)
                                || !((Tree.BaseMemberExpression)ie.getPrimary()).getDeclaration().isAnnotation())) {
                    term.addError("annotation constructor must return a newly-instantiated annotation");
                }
                checkAnnotationArguments(a, ie);
            }
            else {
                term.addError("annotation constructor must return a newly-instantiated annotation");
            }
        }
    }

    private void checkAnnotationArguments(Method a, Tree.InvocationExpression ie) {
        Tree.PositionalArgumentList pal = ie.getPositionalArgumentList();
        Tree.NamedArgumentList nal = ie.getNamedArgumentList();
        if (pal!=null) {
            checkPositionalArguments(a, pal.getPositionalArguments());
        }
        else {
            checkNamedArguments(a, nal);
            if (nal.getSequencedArgument()!=null) {
                nal.getSequencedArgument().addError("illegal annotation argument");
//                checkPositionlArgument(a, nal.getSequencedArgument().getPositionalArguments());
            }
        }
    }

    private void checkNamedArguments(Method a, Tree.NamedArgumentList nal) {
        for (Tree.NamedArgument na: nal.getNamedArguments()) {
            if (na!=null) {
                if (na instanceof Tree.SpecifiedArgument) {
                    Tree.SpecifierExpression se = ((Tree.SpecifiedArgument) na).getSpecifierExpression();
                    if (se!=null && na.getParameter()!=null) {
                        checkAnnotationArgument(a, se.getExpression(),
                                na.getParameter().getType());
                    }
                }
                else {
                    na.addError("illegal annotation argument");
                }
            }
        }
    }

    private void checkPositionalArguments(Method a, List<Tree.PositionalArgument> pal) {
        for (Tree.PositionalArgument pa: pal) {
            if (pa!=null && pa.getParameter()!=null) {
                if (pa instanceof Tree.ListedArgument) {
                    checkAnnotationArgument(a, ((Tree.ListedArgument) pa).getExpression(),
                            pa.getParameter().getType());
                }
                else if (pa instanceof Tree.SpreadArgument) {
                    checkAnnotationArgument(a, ((Tree.SpreadArgument) pa).getExpression(),
                            pa.getParameter().getType());
                }
                else {
                    pa.addError("illegal annotation argument");
                }
            }
        }
    }
    
    @Override public void visit(Tree.Annotation that) {
        super.visit(that);
        Declaration dec = ((Tree.MemberOrTypeExpression) that.getPrimary()).getDeclaration();
        /*if (dec!=null && !dec.isToplevel()) {
            that.getPrimary().addError("annotation must be a toplevel function reference");
        }*/
        if (dec!=null && !dec.isAnnotation()) {
            that.getPrimary().addError("not an annotation constructor");
        }
        else {
            checkAnnotationArguments(null, (Tree.InvocationExpression) that);
        }
    }
    
}
