package com.redhat.ceylon.compiler.analyzer;

import com.redhat.ceylon.compiler.model.Class;
import com.redhat.ceylon.compiler.model.ClassOrInterface;
import com.redhat.ceylon.compiler.model.GenericType;
import com.redhat.ceylon.compiler.model.Interface;
import com.redhat.ceylon.compiler.model.Package;
import com.redhat.ceylon.compiler.model.Scope;
import com.redhat.ceylon.compiler.model.Type;
import com.redhat.ceylon.compiler.model.Typed;
import com.redhat.ceylon.compiler.tree.Node;
import com.redhat.ceylon.compiler.tree.Tree;
import com.redhat.ceylon.compiler.tree.Tree.Directive;
import com.redhat.ceylon.compiler.tree.Tree.Expression;
import com.redhat.ceylon.compiler.tree.Tree.MemberOrType;
import com.redhat.ceylon.compiler.tree.Tree.Return;
import com.redhat.ceylon.compiler.tree.Visitor;

/**
 * Third and final phase of type analysis.
 * Finally visit all expressions and determine their types.
 * Use type inference to assign types to declarations with
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
                    (Typed) that.getVariable().getModelNode());
        }
    }
    
    @Override public void visit(Tree.ValueIterator that) {
        super.visit(that);
        if ((that.getVariable().getTypeOrSubtype() instanceof Tree.LocalModifier)) {
            setType((Tree.LocalModifier) that.getVariable().getTypeOrSubtype(), 
                    that.getSpecifierExpression(), 
                    (Typed) that.getVariable().getModelNode());
        }
    }
    
    @Override public void visit(Tree.KeyValueIterator that) {
        super.visit(that);
        if ((that.getKeyVariable().getTypeOrSubtype() instanceof Tree.LocalModifier)) {
            setType((Tree.LocalModifier) that.getKeyVariable().getTypeOrSubtype(), 
                    that.getSpecifierExpression(), 
                    (Typed) that.getKeyVariable().getModelNode());
        }
        if ((that.getValueVariable().getTypeOrSubtype() instanceof Tree.LocalModifier)) {
            setType((Tree.LocalModifier) that.getValueVariable().getTypeOrSubtype(), 
                    that.getSpecifierExpression(), 
                    (Typed) that.getValueVariable().getModelNode());
        }
    }
    
    @Override public void visit(Tree.AttributeDeclaration that) {
        super.visit(that);
        if (that.getTypeOrSubtype() instanceof Tree.LocalModifier) {
            if ( that.getSpecifierOrInitializerExpression()!=null ) {
                setType((Tree.LocalModifier) that.getTypeOrSubtype(), 
                        that.getSpecifierOrInitializerExpression(),
                        (Typed) that.getModelNode());
            }
            else {
                throw new RuntimeException("Could not infer type of: " + 
                        that.getIdentifier().getText());
            }
        }
        setModelType(that, that.getTypeOrSubtype());
    }

    @Override public void visit(Tree.AttributeGetter that) {
        super.visit(that);
        if (that.getTypeOrSubtype() instanceof Tree.LocalModifier) {
            setType((Tree.LocalModifier) that.getTypeOrSubtype(), 
                    that.getBlock(),
                    (Typed) that.getModelNode());
        }
        setModelType(that, that.getTypeOrSubtype());
    }

    @Override public void visit(Tree.MethodDeclaration that) {
        super.visit(that);
        if (that.getTypeOrSubtype() instanceof Tree.LocalModifier) {
            if (that.getBlock()!=null) {
                setType((Tree.LocalModifier) that.getTypeOrSubtype(), 
                        that.getBlock(),
                        (Typed) that.getModelNode());
            }
            else if ( that.getSpecifierExpression()!=null ) {
                setType((Tree.LocalModifier) that.getTypeOrSubtype(), 
                        that.getSpecifierExpression(),
                        (Typed) that.getModelNode());  //TODO: this is hackish
            }
            else {
                throw new RuntimeException("Could not infer type of: " + 
                        that.getIdentifier().getText());
            }
        }
        setModelType(that, that.getTypeOrSubtype());
    }

    private void setType(Tree.LocalModifier that, 
            Tree.SpecifierOrInitializerExpression s, Typed dec) {
        Type t = s.getExpression().getTypeModel();
        that.setTypeModel(t);
        dec.setType(t);
    }
    
    private void setType(Tree.LocalModifier that, 
            Tree.Block block, Typed dec) {
        Directive d = block.getDirective();
        if (d!=null && (d instanceof Return)) {
            Type t = ((Return) d).getExpression().getTypeModel();
            that.setTypeModel(t);
            dec.setType(t);
        }
        else {
            throw new RuntimeException("Could not infer type of: " +
                    dec.getName());
        }
    }
    
    @Override
    public void visit(Tree.Variable that) {
        super.visit(that);
        setModelType(that, that.getTypeOrSubtype());
    }
    
    private void setModelType(Node that, Tree.TypeOrSubtype type) {
        if (!(type instanceof Tree.LocalModifier)) { //if the type declaration is missing, we do type inference later
            Type t = (Type) type.getModelNode();
            ( (Typed) that.getModelNode() ).setType(t);
        }
    }
    
    //Primaries:
    
    @Override public void visit(Tree.MemberExpression that) {
        that.getPrimary().visit(this);
        GenericType gt = that.getPrimary().getTypeModel().getGenericType();
        if (gt instanceof Scope) {
            MemberOrType mt = that.getMemberOrType();
            if (mt instanceof Tree.Member) {
                Typed member = Util.getDeclaration((Scope) gt, (Tree.Member) mt);
                that.setTypeModel(member.getType());
                //TODO: handle type arguments by substitution
                mt.setModelNode(member);
            }
            else if (mt instanceof Tree.Type) {
                GenericType member = Util.getDeclaration((Scope) gt, (Tree.Type) mt);
                Type t = new Type();
                t.setGenericType(member);
                t.setTreeNode(that);
                //TODO: handle type arguments by substitution
                that.setTypeModel(t);
                mt.setModelNode(member);
            }
            else if (mt instanceof Tree.Outer) {
                if (!(gt instanceof ClassOrInterface)) {
                    throw new RuntimeException("Can't use outer on a type parameter");
                }
                Type t = getOuterType((ClassOrInterface) gt);
                that.setTypeModel(t);
            }
            else {
                //TODO: handle type parameters by looking at
                //      their upper bound constraints 
                //TODO: handle x.outer
                throw new RuntimeException("Not yet supported");
            }
        }
    }
    
    @Override public void visit(Tree.Annotation that) {
        //TODO: ignore annotations for now
    }
    
    @Override public void visit(Tree.InvocationExpression that) {
        super.visit(that);
        that.setTypeModel( that.getPrimary().getTypeModel() ); //TODO: this is hackish
        //TODO: validate argument types are assignable to parameter types
    }
    
    @Override public void visit(Tree.IndexExpression that) {
        super.visit(that);
        //TODO!
    }
    
    @Override public void visit(Tree.PostfixOperatorExpression that) {
        super.visit(that);
        that.setTypeModel( that.getPrimary().getTypeModel() );
    }
        
    //Atoms:
    
    @Override public void visit(Tree.Member that) {
        //TODO: this does not correctly handle methods
        //      and classes which are not subsequently 
        //      invoked (should return the callable type)
        that.setTypeModel( Util.getDeclaration(that).getType() );
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
        that.setTypeModel( that.getTerm().getTypeModel() );
    }
    
    @Override public void visit(Tree.Outer that) {
        Type t = getOuterType( that.getScope() );
        that.setTypeModel(t);
    }

    private Type getOuterType(Scope scope) {
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
        throw new RuntimeException("Can't use outer outside of nested class or interface");
    }
    
    @Override public void visit(Tree.Super that) {
        if (classOrInterface==null) {
            throw new RuntimeException("Can't use super outside a class");
        }
        if (!(classOrInterface instanceof Class)) {
            throw new RuntimeException("Can't use super inside an interface");
        }
        Type t = classOrInterface.getExtendedType();
        //TODO: type arguments
        that.setTypeModel(t);
    }
    
    @Override public void visit(Tree.This that) {
        if (classOrInterface==null) {
            throw new RuntimeException("Can't use this outside a class or interface");
        }
        Type t = new Type();
        t.setGenericType(classOrInterface);
        //TODO: type arguments
        that.setTypeModel(t);
    }
    
    @Override public void visit(Tree.Subtype that) {
        //TODO!
        throw new RuntimeException();
    }
    
    @Override public void visit(Tree.StringTemplate that) {
        super.visit(that);
        //TODO: validate that the subexpression types are Formattable
        Type t = new Type();
        t.setGenericType( (Class) Util.getImportedDeclaration(that.getUnit(), 
                "ceylon.language.String") );
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
                "ceylon.language.Sequence") );
        t.getTypeArguments().add(et);
        that.setTypeModel(t);
    }
    
    @Override public void visit(Tree.StringLiteral that) {
        Type t = new Type();
        t.setGenericType( (Class) Util.getImportedDeclaration(that.getUnit(), 
                "ceylon.language.String") );
        that.setTypeModel(t);
    }
    
    @Override public void visit(Tree.NaturalLiteral that) {
        Type t = new Type();
        t.setGenericType( (Class) Util.getImportedDeclaration(that.getUnit(), 
                "ceylon.language.Natural") );
        that.setTypeModel(t);
    }
    
    @Override public void visit(Tree.FloatLiteral that) {
        Type t = new Type();
        t.setGenericType( (Class) Util.getImportedDeclaration(that.getUnit(), 
                "ceylon.language.Float") );
        that.setTypeModel(t);
    }
    
    @Override public void visit(Tree.CharLiteral that) {
        Type t = new Type();
        t.setGenericType( (Class) Util.getImportedDeclaration(that.getUnit(), 
                "ceylon.language.Character") );
        that.setTypeModel(t);
    }
    
    @Override public void visit(Tree.QuotedLiteral that) {
        Type t = new Type();
        t.setGenericType( (Class) Util.getImportedDeclaration(that.getUnit(), 
                "ceylon.language.Quoted") );
        that.setTypeModel(t);
    }
    
}
