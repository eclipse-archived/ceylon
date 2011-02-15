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
import com.redhat.ceylon.compiler.tree.Tree.Primary;
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
    
    private ClassOrInterface classOrInterface;
    private Tree.TypeOrSubtype returnType;
    
    public void visit(Tree.ClassOrInterfaceDeclaration that) {
        ClassOrInterface o = classOrInterface;
        classOrInterface = (ClassOrInterface) that.getModelNode();
        super.visit(that);
        classOrInterface = o;
    }
    
    private Tree.TypeOrSubtype beginReturnScope(Tree.TypeOrSubtype t) {
        Tree.TypeOrSubtype ort = returnType;
        returnType = t;
        return ort;
    }
    
    private void endReturnScope(Tree.TypeOrSubtype t) {
        returnType = t;
    }

    @Override public void visit(Tree.AssignOp that) {
        super.visit(that);
        Type rhst = that.getRightTerm().getTypeModel();
        Type lhst = that.getLeftTerm().getTypeModel();
        if ( rhst!=null && lhst!=null && !rhst.isExactly(lhst) ) {
            that.addError("type not assignable");
        }
        //TODO: validate that the LHS really is assignable
        that.setTypeModel(rhst);
        that.setModelNode(rhst);
    }
    
    @Override public void visit(Tree.VariableOrExpression that) {
        super.visit(that);
        if (that.getVariable()!=null) {
            inferType(that.getVariable(), that.getSpecifierExpression());
            checkType(that.getVariable(), that.getSpecifierExpression());
        }
    }
    
    @Override public void visit(Tree.ValueIterator that) {
        super.visit(that);
        //TODO: this is not correct, should infer from arguments to Iterable<V>
        inferType(that.getVariable(), that.getSpecifierExpression());
        checkType(that.getVariable(), that.getSpecifierExpression());
    }

    @Override public void visit(Tree.KeyValueIterator that) {
        super.visit(that);
        //TODO: this is not correct, should infer from arguments to Iterable<Entry<K,V>>
        inferType(that.getKeyVariable(), that.getSpecifierExpression());
        inferType(that.getValueVariable(), that.getSpecifierExpression());
        checkType(that.getKeyVariable(), that.getSpecifierExpression());
        checkType(that.getValueVariable(), that.getSpecifierExpression());
    }
    
    @Override public void visit(Tree.AttributeDeclaration that) {
        super.visit(that);
        inferType(that, that.getSpecifierOrInitializerExpression());
        checkType(that.getTypeOrSubtype(), that.getSpecifierOrInitializerExpression());
    }

    @Override public void visit(Tree.SpecifierStatement that) {
        super.visit(that);
        checkType(that.getMember(), that.getSpecifierExpression());
    }

    private void checkType(Node typedNode, Tree.SpecifierOrInitializerExpression sie) {
        if (sie!=null) {
            Type type = sie.getExpression().getTypeModel();
            if ( type!=null && typedNode.getTypeModel()!=null) {
                if ( !type.isExactly(typedNode.getTypeModel()) ) {
                    sie.addError("Specifier expression not assignable to attribute type");
                }
            }
            else {
                sie.addError("Could not determine assignability of specified expression to attribute type");
            }
        }
    }

    @Override public void visit(Tree.AttributeGetter that) {
        Tree.TypeOrSubtype rt = beginReturnScope(that.getTypeOrSubtype());
        super.visit(that);
        inferType(that, that.getBlock());
        endReturnScope(rt);
    }

    @Override public void visit(Tree.AttributeSetter that) {
        Tree.TypeOrSubtype rt = beginReturnScope(that.getTypeOrSubtype());
        super.visit(that);
        inferType(that, that.getBlock());
        endReturnScope(rt);
    }

    @Override public void visit(Tree.MethodDeclaration that) {
        if (that.getBlock()!=null) {
            Tree.TypeOrSubtype rt = beginReturnScope(that.getTypeOrSubtype());           
            super.visit(that);
            endReturnScope(rt);
        }
        else {
            super.visit(that);
        }
        inferType(that);
    }

    //Type inference for members declared "local":
    
    private void inferType(Tree.TypedDeclaration that, Tree.Block block) {
        if (that.getTypeOrSubtype() instanceof Tree.LocalModifier) {
            if (block!=null) {
                setType((Tree.LocalModifier) that.getTypeOrSubtype(), block, that);
            }
            else {
                that.addError("Could not infer type of: " + 
                        Util.name(that));
            }
        }
    }

    private void inferType(Tree.TypedDeclaration that, Tree.SpecifierOrInitializerExpression spec) {
        if (that==null) {
            System.out.flush();
        }
        if ((that.getTypeOrSubtype() instanceof Tree.LocalModifier)) {
            if (spec!=null) {
                setType((Tree.LocalModifier) that.getTypeOrSubtype(), spec, that);
            }
            else {
                that.addError("Could not infer type of: " + 
                        Util.name(that));
            }
        }
    }
        
    private void inferType(Tree.MethodDeclaration that) {
        if (that.getTypeOrSubtype() instanceof Tree.LocalModifier) {
            if (that.getBlock()!=null) {
                inferType(that, that.getBlock());
            }
            else if (that.getSpecifierExpression()!=null) {
                inferType(that, that.getSpecifierExpression());  //TODO: this is hackish
            }
            else {
                that.addError("Could not infer type of: " + 
                        Util.name(that));
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
        if (d!=null && (d instanceof Tree.Return)) {
            Type t = ((Tree.Return) d).getExpression().getTypeModel();
            local.setTypeModel(t);
            ((Typed) that.getModelNode()).setType(t);
        }
        else {
            local.addError("Could not infer type of: " + 
                    Util.name(that));
        }
    }
    
    @Override public void visit(Tree.Return that) {
        super.visit(that);
        if (returnType==null) {
            that.addError("Could not determine expected return type");
        } 
        else {
            Expression e = that.getExpression();
            if ( returnType instanceof Tree.VoidModifier ) {
                if (e!=null) {
                    that.addError("void methods may not return a value");
                }
            }
            else if ( !(returnType instanceof Tree.LocalModifier) ) {
                if (e==null) {
                    that.addError("non-void methods and getters must return a value");
                }
                else {
                    Type et = returnType.getTypeModel();
                    Type at = e.getTypeModel();
                    if (et!=null && at!=null) {
                        if ( !et.isExactly(at) ) {
                            that.addError("Returned expression not assignable to expected return type");
                        }
                    }
                    else {
                        that.addError("Could not determine assignability of returned expression to expected return type");
                    }
                }
            }
        }
    }
    
    //Primaries:
    
    @Override public void visit(Tree.MemberExpression that) {
        that.getPrimary().visit(this);
        Type pt = that.getPrimary().getTypeModel();
        if (pt!=null) {
            GenericType gt = pt.getGenericType();
            if (gt instanceof Scope) {
                Tree.MemberOrType mt = that.getMemberOrType();
                if (mt instanceof Tree.Member) {
                    Typed member = Util.getDeclaration((Scope) gt, (Tree.Member) mt);
                    if (member==null) {
                        mt.addError("Could not determine target of member reference: " +
                                ((Tree.Member) mt).getIdentifier().getText());
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
                        mt.addError("Could not determine target of member type reference: " +
                                ((Tree.Type) mt).getIdentifier().getText());
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
                        that.addError("Can't use outer on a type parameter");
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
        Primary pr = that.getPrimary();
        if (pr==null) {
            that.addError("malformed expression");
        }
        else {
            Type pt = pr.getTypeModel();
            if (pt!=null) {
                that.setTypeModel(pt); //TODO: this is hackish
                that.setModelNode(pt);
            }
            checkInvocationArguments(that);
        }
    }

    private void checkInvocationArguments(Tree.InvocationExpression that) {
        List<Parameter> pl;
        Model pm = that.getPrimary().getModelNode();
        if (pm!=null) {
            if (pm instanceof Functional) {
                Functional fpm = (Functional) pm;
                List<List<Parameter>> pls = fpm.getParameters();
                if (pls.size()==0) {
                    that.addError("cannot be invoked: " + 
                            fpm.getName());
                    return;
                }
                else {
                    pl = pls.get(0);
                }
            }
            else if (pm instanceof Type) {
                GenericType pgt = ((Type) pm).getGenericType();
                if (pgt==null) {
                    that.addError("could not determine parameter list: " + 
                            ((Type) pm).getProducedTypeName() );
                    return;
                }
                else if (pgt instanceof Class) {
                    pl = ((Class) pgt).getParameters();
                }
                else {
                    that.addError("interface cannot be invoked: " + 
                            pgt.getName());
                    return;
                }
            }
            else {
                that.addError("cannot be invoked");
                return;
            }
            
            Tree.PositionalArgumentList pal = that.getPositionalArgumentList();
            if ( pal!=null ) {
                checkPositionalArguments(pl, pal);
            }
            
            Tree.NamedArgumentList nal = that.getNamedArgumentList();
            if (nal!=null) {
                checkNamedArguments(pl, nal);
            }
            
        }
    }

    private void checkNamedArguments(List<Parameter> pl,
            Tree.NamedArgumentList nal) {
        List<Tree.NamedArgument> na = nal.getNamedArguments();
        if ( pl.size()!=na.size() ) {
            nal.addError("wrong number of arguments");
        }
        //TODO!!
    }

    private void checkPositionalArguments(List<Parameter> pl,
            Tree.PositionalArgumentList pal) {
        List<Tree.PositionalArgument> pa = pal.getPositionalArguments();
        if ( pl.size()!=pa.size() ) {
            pal.addError("wrong number of arguments");
            return;
        }
        for (int i=0; i<pl.size(); i++) {
            Parameter p = pl.get(i);
            Type paramType = p.getType();
            Tree.PositionalArgument a = pa.get(i);
            Type argType = a.getExpression().getTypeModel();
            if (paramType!=null && argType!=null) {
                if (!paramType.isExactly(argType)) {
                    a.addError("argument not assignable to parameter type: " + 
                            p.getName());
                }
            }
            else {
                a.addError("could not determine assignability of argument to parameter: " +
                        p.getName());
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
            that.addError("Could not determine target of member reference: " +
                    that.getIdentifier().getText());
        }
        else {
            Type t = d.getType();
            if (t==null) {
                that.addError("Could not determine type of member reference: " +
                        that.getIdentifier().getText());
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
        Tree.Term term = that.getTerm();
        if (term==null) {
            that.addError("Expression not well formed");
        }
        else {
            Type t = term.getTypeModel();
            if (t==null) {
                that.addError("Could not determine type of expression");
            }
            else {
                that.setTypeModel(t);
            }
        }
    }
    
    @Override public void visit(Tree.Outer that) {
        that.setTypeModel(getOuterType(that, that.getScope()));
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
        that.addError("Can't use outer outside of nested class or interface");
        return null;
    }
    
    @Override public void visit(Tree.Super that) {
        if (classOrInterface==null) {
            that.addError("Can't use super outside a class");
        }
        else if (!(classOrInterface instanceof Class)) {
            that.addError("Can't use super inside an interface");
        }
        else {
            Type t = classOrInterface.getExtendedType();
            //TODO: type arguments
            that.setTypeModel(t);
        }
    }
    
    @Override public void visit(Tree.This that) {
        if (classOrInterface==null) {
            that.addError("Can't use this outside a class or interface");
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
        for (Tree.Expression e: that.getExpressionList().getExpressions()) {
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
