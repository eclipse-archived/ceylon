package com.redhat.ceylon.compiler.analyzer;

import java.util.List;

import com.redhat.ceylon.compiler.model.Class;
import com.redhat.ceylon.compiler.model.ClassOrInterface;
import com.redhat.ceylon.compiler.model.Functional;
import com.redhat.ceylon.compiler.model.GenericType;
import com.redhat.ceylon.compiler.model.Interface;
import com.redhat.ceylon.compiler.model.Model;
import com.redhat.ceylon.compiler.model.Package;
import com.redhat.ceylon.compiler.model.Parameter;
import com.redhat.ceylon.compiler.model.Scope;
import com.redhat.ceylon.compiler.model.Type;
import com.redhat.ceylon.compiler.model.Typed;
import com.redhat.ceylon.compiler.tree.Node;
import com.redhat.ceylon.compiler.tree.Tree;
import com.redhat.ceylon.compiler.tree.Tree.Expression;
import com.redhat.ceylon.compiler.tree.Tree.MemberOrType;
import com.redhat.ceylon.compiler.tree.Tree.Return;
import com.redhat.ceylon.compiler.tree.Tree.Term;
import com.redhat.ceylon.compiler.tree.Visitor;

/**
 * Third and final phase of type analysis.
 * Finally visit all expressions and determine their types.
 * Use type inference to assign types to declarations with
 * the local modifier. Finally, assigns types to the 
 * associated model objects of declarations declared using
 * the local modifier.
 * 
 * @author Gavin King
 *
 */
public class ExpressionVisitor extends Visitor {
    
    ClassOrInterface classOrInterface;
    
    public void visit(Tree.ClassOrInterfaceDeclaration that) {
        ClassOrInterface o = classOrInterface;
        classOrInterface = (ClassOrInterface) that.getModelNode();
        super.visit(that);
        classOrInterface = o;
    }
    
    //Type inference for members declared "local":
    
    @Override public void visit(Tree.VariableOrExpression that) {
        super.visit(that);
        if (that.getSpecifierExpression()!=null 
                && that.getVariable()!=null
                && (that.getVariable().getTypeOrSubtype() instanceof Tree.LocalModifier)) {
            setType((Tree.LocalModifier) that.getVariable().getTypeOrSubtype(), 
                    that.getSpecifierExpression(), 
                    that.getVariable());
        }
    }
    
    @Override public void visit(Tree.ValueIterator that) {
        super.visit(that);
        if ((that.getVariable().getTypeOrSubtype() instanceof Tree.LocalModifier)) {
            setType((Tree.LocalModifier) that.getVariable().getTypeOrSubtype(), 
                    that.getSpecifierExpression(), 
                    that.getVariable());
        }
    }
    
    @Override public void visit(Tree.KeyValueIterator that) {
        super.visit(that);
        if ((that.getKeyVariable().getTypeOrSubtype() instanceof Tree.LocalModifier)) {
            setType((Tree.LocalModifier) that.getKeyVariable().getTypeOrSubtype(), 
                    that.getSpecifierExpression(), 
                    that.getKeyVariable());
        }
        if ((that.getValueVariable().getTypeOrSubtype() instanceof Tree.LocalModifier)) {
            setType((Tree.LocalModifier) that.getValueVariable().getTypeOrSubtype(), 
                    that.getSpecifierExpression(), 
                    that.getValueVariable());
        }
    }
    
    @Override public void visit(Tree.AttributeDeclaration that) {
        super.visit(that);
        Tree.TypeOrSubtype at = that.getTypeOrSubtype();
        Tree.SpecifierOrInitializerExpression sie = that.getSpecifierOrInitializerExpression();
        if (at instanceof Tree.LocalModifier) {
            if (sie!=null) {
                setType((Tree.LocalModifier) at, sie, that);
            }
            else {
                that.getErrors().add( new AnalysisError(that, 
                        "Could not infer type of: " + 
                        that.getIdentifier().getText()) );
            }
        }
        else if (sie!=null) {
            Type siet = sie.getExpression().getTypeModel();
            Type attm = at.getTypeModel();
            if ( siet!=null && attm!=null && !siet.isExactly(attm) ) {
                that.getErrors().add( new AnalysisError(sie, 
                        "Attribute specifier or initializer expression not assignable to attribute type: " + 
                        that.getIdentifier().getText()) );
            }
        }
    }

    @Override public void visit(Tree.SpecifierStatement that) {
        super.visit(that);
        Tree.SpecifierExpression sie = that.getSpecifierExpression();
        if (sie!=null) {
            Type siet = sie.getExpression().getTypeModel();
            Type mttm = that.getMember().getTypeModel();
            if ( siet!=null && mttm!=null && !siet.isExactly(mttm) ) {
                that.getErrors().add( new AnalysisError(sie, 
                        "Specifier expression not assignable to attribute type: " + 
                        that.getMember().getIdentifier().getText()) );
            }
        }
    }

    @Override public void visit(Tree.AttributeGetter that) {
        super.visit(that);
        if (that.getTypeOrSubtype() instanceof Tree.LocalModifier) {
            setType((Tree.LocalModifier) that.getTypeOrSubtype(), 
                    that.getBlock(),
                    that);
        }
    }

    @Override public void visit(Tree.MethodDeclaration that) {
        super.visit(that);
        if (that.getTypeOrSubtype() instanceof Tree.LocalModifier) {
            if (that.getBlock()!=null) {
                setType((Tree.LocalModifier) that.getTypeOrSubtype(), 
                        that.getBlock(),
                        that);
            }
            else if ( that.getSpecifierExpression()!=null ) {
                setType((Tree.LocalModifier) that.getTypeOrSubtype(), 
                        that.getSpecifierExpression(),
                        that);  //TODO: this is hackish
            }
            else {
                that.getErrors().add( new AnalysisError(that, 
                        "Could not infer type of: " + 
                        that.getIdentifier().getText()) );
            }
        }
    }

    private void setType(Tree.LocalModifier local, 
            Tree.SpecifierOrInitializerExpression s, 
            Tree.TypedDeclaration that) {
        Type t = s.getExpression().getTypeModel();
        local.setTypeModel(t);
        ((Typed) that.getModelNode()).setType(t);
    }
    
    private void setType(Tree.LocalModifier local, 
            Tree.Block block, 
            Tree.TypedDeclaration that) {
        int s = block.getStatements().size();
        Tree.Statement d = s==0 ? null : block.getStatements().get(s-1);
        if (d!=null && (d instanceof Return)) {
            Type t = ((Return) d).getExpression().getTypeModel();
            local.setTypeModel(t);
            ((Typed) that.getModelNode()).setType(t);
        }
        else {
            local.getErrors().add( new AnalysisError(local, 
                    "Could not infer type of: " + 
                    that.getIdentifier().getText()) );
        }
    }
    
    //Primaries:
    
    @Override public void visit(Tree.MemberExpression that) {
        that.getPrimary().visit(this);
        Type pt = that.getPrimary().getTypeModel();
        if (pt!=null) {
            GenericType gt = pt.getGenericType();
            if (gt instanceof Scope) {
                MemberOrType mt = that.getMemberOrType();
                if (mt instanceof Tree.Member) {
                    Typed member = Util.getDeclaration((Scope) gt, (Tree.Member) mt);
                    if (member==null) {
                        that.getErrors().add( new AnalysisError(that, 
                                "Could not determine target of member reference: " +
                                ((Tree.Member) mt).getIdentifier().getText()) );
                    }
                    else {
                        that.setTypeModel(member.getType());
                        //TODO: handle type arguments by substitution
                        mt.setModelNode(member);
                        that.setModelNode(member);
                    }
                }
                else if (mt instanceof Tree.Type) {
                    GenericType member = Util.getDeclaration((Scope) gt, (Tree.Type) mt);
                    if (member==null) {
                        that.getErrors().add( new AnalysisError(that, 
                                "Could not determine target of member type reference: " +
                                ((Tree.Type) mt).getIdentifier().getText()) );
                    }
                    else {
                        Type t = new Type();
                        t.setGenericType(member);
                        t.setTreeNode(that);
                        //TODO: handle type arguments by substitution
                        that.setTypeModel(t);
                        mt.setModelNode(t);
                        that.setModelNode(t);
                    }
                }
                else if (mt instanceof Tree.Outer) {
                    if (!(gt instanceof ClassOrInterface)) {
                        that.getErrors().add( new AnalysisError(that, 
                                "Can't use outer on a type parameter"));
                    }
                    else {
                        Type t = getOuterType(mt, (ClassOrInterface) gt);
                        that.setTypeModel(t);
                        mt.setModelNode(t);
                        that.setModelNode(t);
                    }
                }
                else {
                    //TODO: handle type parameters by looking at
                    //      their upper bound constraints 
                    //TODO: handle x.outer
                    throw new RuntimeException("Not yet supported");
                }
            }
        }
    }
    
    @Override public void visit(Tree.Annotation that) {
        //TODO: ignore annotations for now
    }
    
    @Override public void visit(Tree.InvocationExpression that) {
        super.visit(that);
        Type pt = that.getPrimary().getTypeModel();
        if (pt!=null) {
            that.setTypeModel(pt); //TODO: this is hackish
            that.setModelNode(pt);
        }
        List<Parameter> pl;
        Model pm = that.getPrimary().getModelNode();
        if (pm!=null) {
            if (pm instanceof Functional) {
                Functional fpm = (Functional) pm;
                List<List<Parameter>> pls = fpm.getParameters();
                if (pls.size()==0) {
                    that.getErrors().add( new AnalysisError(that, 
                            "cannot be invoked: " + 
                            fpm.getName()) );
                    return;
                }
                else {
                    pl = pls.get(0);
                }
            }
            else if (pm instanceof Type) {
                GenericType pgt = ((Type) pm).getGenericType();
                if (pgt==null) {
                    that.getErrors().add( new AnalysisError( that, 
                            "could not determine parameter list: " + 
                            ((Type) pm).getProducedTypeName() ) );
                    return;
                }
                else if (pgt instanceof Class) {
                    pl = ((Class) pgt).getParameters();
                }
                else {
                    that.getErrors().add( new AnalysisError(that, 
                            "interface cannot be invoked: " + 
                            pgt.getName()) );
                    return;
                }
            }
            else {
                that.getErrors().add( new AnalysisError(that, 
                        "cannot be invoked") );
                return;
            }
            Tree.PositionalArgumentList pal = that.getPositionalArgumentList();
            if ( pal!=null ) {
                List<Tree.PositionalArgument> pa = pal.getPositionalArguments();
                if ( pl.size()!=pa.size() ) {
                    that.getErrors().add( new AnalysisError(that, 
                            "wrong number of arguments") );
                }
            }
            Tree.NamedArgumentList nal = that.getNamedArgumentList();
            if (nal!=null) {
                List<Tree.NamedArgument> na = nal.getNamedArguments();
                if ( pl.size()!=na.size() ) {
                    that.getErrors().add( new AnalysisError(that, 
                            "wrong number of arguments") );
                }
            }
        }
    }
    
    @Override public void visit(Tree.IndexExpression that) {
        super.visit(that);
        //TODO!
    }
    
    @Override public void visit(Tree.PostfixOperatorExpression that) {
        super.visit(that);
        Type pt = that.getPrimary().getTypeModel();
        if (pt!=null) {
            that.setTypeModel(pt);
        }
    }
        
    //Atoms:
    
    @Override public void visit(Tree.Member that) {
        //TODO: this does not correctly handle methods
        //      and classes which are not subsequently 
        //      invoked (should return the callable type)
        Typed d = Util.getDeclaration(that);
        if (d==null) {
            that.getErrors().add( new AnalysisError(that, 
                    "Could not determine target of member reference: " +
                    that.getIdentifier().getText()) );
        }
        else {
            Type t = d.getType();
            if (t==null) {
                that.getErrors().add( new AnalysisError(that, 
                        "Could not determine type of member reference: " +
                        that.getIdentifier().getText()) );
            }
            else {
                that.setTypeModel(t);
            }
            that.setModelNode(d);
        }
    }
    
    @Override public void visit(Tree.Type that) {
        //TODO: this does not correctly handle methods
        //      and classes which are not subsequently 
        //      invoked (should return the callable type)
        //that.setType( (Type) that.getModelNode() );
    }
    
    @Override public void visit(Tree.Expression that) {
        //i.e. this is a parenthesized expression
        super.visit(that);
        Term term = that.getTerm();
        if (term==null) {
            that.getErrors().add( new AnalysisError(that, 
            "Expression not well formed") );
        }
        else {
            Type t = term.getTypeModel();
            if (t==null) {
                that.getErrors().add( new AnalysisError(that, 
                        "Could not determine type of expression") );
            }
            else {
                that.setTypeModel(t);
            }
        }
    }
    
    @Override public void visit(Tree.Outer that) {
        Type t = getOuterType(that, that.getScope());
        that.setTypeModel(t);
    }

    private Type getOuterType(Node that, Scope scope) {
        Boolean foundInner = false;
        while (!(scope instanceof Package)) {
            if (scope instanceof ClassOrInterface) {
                if (foundInner) {
                    Type t = new Type();
                    t.setGenericType((ClassOrInterface) scope);
                    //TODO: type arguments
                    return t;
                }
                else {
                    foundInner = true;
                }
            }
            scope = scope.getContainer();
        }
        that.getErrors().add( new AnalysisError(that, 
                "Can't use outer outside of nested class or interface"));
        return null;
    }
    
    @Override public void visit(Tree.Super that) {
        if (classOrInterface==null) {
            that.getErrors().add( new AnalysisError(that, 
                    "Can't use super outside a class"));
        }
        else if (!(classOrInterface instanceof Class)) {
            that.getErrors().add( new AnalysisError(that, 
                    "Can't use super inside an interface"));
        }
        else {
            Type t = classOrInterface.getExtendedType();
            //TODO: type arguments
            that.setTypeModel(t);
        }
    }
    
    @Override public void visit(Tree.This that) {
        if (classOrInterface==null) {
            that.getErrors().add( new AnalysisError(that, 
                    "Can't use this outside a class or interface"));
        }
        else {
            Type t = new Type();
            t.setGenericType(classOrInterface);
            //TODO: type arguments
            that.setTypeModel(t);
            that.setModelNode(t);
        }
    }
    
    @Override public void visit(Tree.Subtype that) {
        //TODO!
    }
    
    @Override public void visit(Tree.StringTemplate that) {
        super.visit(that);
        //TODO: validate that the subexpression types are Formattable
        Type t = new Type();
        t.setGenericType( (Class) Util.getImportedDeclaration(that.getUnit(), 
                "String") );
        that.setTypeModel(t);
    }
    
    @Override public void visit(Tree.SequenceEnumeration that) {
        super.visit(that);
        Type et = null; 
        for (Expression e: that.getExpressionList().getExpressions()) {
            if (et==null) {
                et = e.getTypeModel();
            }
            //TODO: determine the common supertype of all of them
        }
        Type t = new Type();
        t.setGenericType( (Interface) Util.getImportedDeclaration(that.getUnit(), 
                "Sequence") );
        t.getTypeArguments().add(et);
        that.setTypeModel(t);
    }
    
    @Override public void visit(Tree.StringLiteral that) {
        Type t = new Type();
        t.setGenericType( (Class) Util.getImportedDeclaration(that.getUnit(), 
                "String") );
        that.setTypeModel(t);
    }
    
    @Override public void visit(Tree.NaturalLiteral that) {
        Type t = new Type();
        t.setGenericType( (Class) Util.getImportedDeclaration(that.getUnit(), 
                "Natural") );
        that.setTypeModel(t);
    }
    
    @Override public void visit(Tree.FloatLiteral that) {
        Type t = new Type();
        t.setGenericType( (Class) Util.getImportedDeclaration(that.getUnit(), 
                "Float") );
        that.setTypeModel(t);
    }
    
    @Override public void visit(Tree.CharLiteral that) {
        Type t = new Type();
        t.setGenericType( (Class) Util.getImportedDeclaration(that.getUnit(), 
                "Character") );
        that.setTypeModel(t);
    }
    
    @Override public void visit(Tree.QuotedLiteral that) {
        Type t = new Type();
        t.setGenericType( (Class) Util.getImportedDeclaration(that.getUnit(), 
                "Quoted") );
        that.setTypeModel(t);
    }
    
    @Override
    public void visit(Tree.CompilerAnnotation that) {
        //don't visit arg       
    }
    
}
