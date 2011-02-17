package com.redhat.ceylon.compiler.analyzer;

import java.util.List;

import com.redhat.ceylon.compiler.context.Context;
import com.redhat.ceylon.compiler.model.Class;
import com.redhat.ceylon.compiler.model.ClassOrInterface;
import com.redhat.ceylon.compiler.model.Declaration;
import com.redhat.ceylon.compiler.model.Functional;
import com.redhat.ceylon.compiler.model.Interface;
import com.redhat.ceylon.compiler.model.MemberReference;
import com.redhat.ceylon.compiler.model.Package;
import com.redhat.ceylon.compiler.model.Parameter;
import com.redhat.ceylon.compiler.model.ParameterList;
import com.redhat.ceylon.compiler.model.ProducedType;
import com.redhat.ceylon.compiler.model.Scope;
import com.redhat.ceylon.compiler.model.TypeDeclaration;
import com.redhat.ceylon.compiler.model.TypedDeclaration;
import com.redhat.ceylon.compiler.tree.Node;
import com.redhat.ceylon.compiler.tree.Tree;
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
    private Context context;

    public ExpressionVisitor(Context context) {
        this.context = context;
    }
    
    public void visit(Tree.ClassDefinition that) {
        ClassOrInterface o = classOrInterface;
        classOrInterface = (Class) that.getDeclarationModel();
        super.visit(that);
        classOrInterface = o;
    }
    
    public void visit(Tree.InterfaceDefinition that) {
        ClassOrInterface o = classOrInterface;
        classOrInterface = (Interface) that.getDeclarationModel();
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
        ProducedType rhst = that.getRightTerm().getTypeModel();
        ProducedType lhst = that.getLeftTerm().getTypeModel();
        if ( rhst!=null && lhst!=null && !rhst.isExactly(lhst) ) {
            that.addError("type not assignable");
        }
        //TODO: validate that the LHS really is assignable
        that.setTypeModel(rhst);
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
        checkIterableType(that.getVariable().getTypeOrSubtype(), that.getSpecifierExpression());
    }

    @Override public void visit(Tree.KeyValueIterator that) {
        super.visit(that);
        //TODO: this is not correct, should infer from arguments to Iterable<Entry<K,V>>
        inferType(that.getKeyVariable(), that.getSpecifierExpression());
        inferType(that.getValueVariable(), that.getSpecifierExpression());
        checkIterableType(that.getKeyVariable().getTypeOrSubtype(), that.getSpecifierExpression());
        checkIterableType(that.getValueVariable().getTypeOrSubtype(), that.getSpecifierExpression());
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
            ProducedType type = sie.getExpression().getTypeModel();
            if ( type!=null && typedNode.getTypeModel()!=null) {
                if ( !type.isExactly(typedNode.getTypeModel()) ) {
                    sie.addError("Specifier expression not assignable to attribute type");
                }
            }
            else {
                sie.addError("could not determine assignability of specified expression to attribute type");
            }
        }
    }

    private void checkIterableType(Node typedNode, Tree.SpecifierOrInitializerExpression sie) {
        if (sie!=null) {
            ProducedType type = sie.getExpression().getTypeModel();
            if ( type!=null && typedNode.getTypeModel()!=null) {
                //TODO: use subtyping!
                ProducedType it = new ProducedType();
                it.setDeclaration( (TypeDeclaration) Util.getLanguageModuleDeclaration("Iterable", context) );
                it.getTypeArguments().add(typedNode.getTypeModel());
                ProducedType st = new ProducedType();
                st.setDeclaration( (TypeDeclaration) Util.getLanguageModuleDeclaration("Sequence", context) );
                st.getTypeArguments().add(typedNode.getTypeModel());
                if ( !type.isExactly(it) &&  !type.isExactly(st)  ) {
                    sie.addError("specifier expression not assignable to attribute type");
                }
            }
            else {
                sie.addError("could not determine assignability of specified expression to attribute type");
            }
        }
    }

    @Override public void visit(Tree.AttributeGetterDefinition that) {
        Tree.TypeOrSubtype rt = beginReturnScope(that.getTypeOrSubtype());
        super.visit(that);
        inferType(that, that.getBlock());
        endReturnScope(rt);
    }

    @Override public void visit(Tree.AttributeSetterDefinition that) {
        Tree.TypeOrSubtype rt = beginReturnScope(that.getTypeOrSubtype());
        super.visit(that);
        inferType(that, that.getBlock());
        endReturnScope(rt);
    }

    @Override public void visit(Tree.MethodDeclaration that) {
        super.visit(that);
        inferType(that, that.getSpecifierExpression());
    }

    @Override public void visit(Tree.MethodDefinition that) {
        Tree.TypeOrSubtype rt = beginReturnScope(that.getTypeOrSubtype());           
        super.visit(that);
        endReturnScope(rt);
        inferType(that, that.getBlock());
    }

    //Type inference for members declared "local":
    
    private void inferType(Tree.TypedDeclaration that, Tree.Block block) {
        if (that.getTypeOrSubtype() instanceof Tree.LocalModifier) {
            if (block!=null) {
                setType((Tree.LocalModifier) that.getTypeOrSubtype(), block, that);
            }
            else {
                that.addError("could not infer type of: " + 
                        Util.name(that));
            }
        }
    }

    private void inferType(Tree.TypedDeclaration that, Tree.SpecifierOrInitializerExpression spec) {
        if ((that.getTypeOrSubtype() instanceof Tree.LocalModifier)) {
            if (spec!=null) {
                setType((Tree.LocalModifier) that.getTypeOrSubtype(), spec, that);
            }
            else {
                that.addError("could not infer type of: " + 
                        Util.name(that));
            }
        }
    }

    private void setType(Tree.LocalModifier local, 
            Tree.SpecifierOrInitializerExpression s, 
            Tree.TypedDeclaration that) {
        ProducedType t = s.getExpression().getTypeModel();
        local.setTypeModel(t);
        ((TypedDeclaration) that.getDeclarationModel()).setType(t);
    }
    
    private void setType(Tree.LocalModifier local, 
            Tree.Block block, 
            Tree.TypedDeclaration that) {
        int s = block.getStatements().size();
        Tree.Statement d = s==0 ? null : block.getStatements().get(s-1);
        if (d!=null && (d instanceof Tree.Return)) {
            ProducedType t = ((Tree.Return) d).getExpression().getTypeModel();
            local.setTypeModel(t);
            ((TypedDeclaration) that.getDeclarationModel()).setType(t);
        }
        else {
            local.addError("could not infer type of: " + 
                    Util.name(that));
        }
    }
    
    @Override public void visit(Tree.Return that) {
        super.visit(that);
        if (returnType==null) {
            that.addError("could not determine expected return type");
        } 
        else {
            Tree.Expression e = that.getExpression();
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
                    ProducedType et = returnType.getTypeModel();
                    ProducedType at = e.getTypeModel();
                    if (et!=null && at!=null) {
                        if ( !et.isExactly(at) ) {
                            that.addError("returned expression not assignable to expected return type");
                        }
                    }
                    else {
                        that.addError("could not determine assignability of returned expression to expected return type");
                    }
                }
            }
        }
    }
    
    //Primaries:
    
    @Override public void visit(Tree.MemberExpression that) {
        that.getPrimary().visit(this);
        ProducedType pt = that.getPrimary().getTypeModel();
        if (pt!=null) {
            TypeDeclaration gt = pt.getDeclaration();
            if (gt instanceof Scope) {
                Tree.MemberOrType mt = that.getMemberOrType();
                if (mt instanceof Tree.Member) {
                    TypedDeclaration member = Util.getDeclaration((Scope) gt, (Tree.Member) mt, context);
                    if (member==null) {
                        mt.addError("could not determine target of member reference: " +
                                ((Tree.Member) mt).getIdentifier().getText());
                    }
                    else {
                        MemberReference mr = new MemberReference();
                        mr.setDeclaration(member);
                        that.setMemberReference(mr);
                        ProducedType t = member.getType();
                        //TODO: handle type arguments by substitution
                        if (t==null) {
                            mt.addError("could not determine type of member reference");
                        }
                        else {
                            that.setTypeModel(t);
                        }
                    }
                }
                else if (mt instanceof Tree.Type) {
                    TypeDeclaration member = Util.getDeclaration((Scope) gt, (Tree.Type) mt, context);
                    if (member==null) {
                        mt.addError("could not determine target of member type reference: " +
                                ((Tree.Type) mt).getIdentifier().getText());
                    }
                    else {
                        ProducedType t = new ProducedType();
                        t.setDeclaration(member);
                        //TODO: handle type arguments by substitution
                        that.setTypeModel(t);
                        that.setMemberReference(t);
                    }
                }
                else if (mt instanceof Tree.Outer) {
                    if (gt instanceof ClassOrInterface) {
                        ProducedType t = getOuterType(mt, (ClassOrInterface) gt);
                        that.setTypeModel(t);
                        //TODO: some kind of MemberReference
                    }
                    else {
                        that.addError("can't use outer on a type parameter");
                    }
                }
                else {
                    //TODO: handle type parameters by looking at
                    //      their upper bound constraints 
                    //TODO: handle x.outer
                    throw new RuntimeException("not yet supported");
                }
            }
        }
    }
    
    @Override public void visit(Tree.Annotation that) {
        //TODO: ignore annotations for now
    }
    
    @Override public void visit(Tree.InvocationExpression that) {
        super.visit(that);
        Tree.Primary pr = that.getPrimary();
        if (pr==null) {
            that.addError("malformed expression");
        }
        else {
            MemberReference m = pr.getMemberReference();
            if (m==null) {
                that.addError("receiving expression cannot be invoked");
            }
            else {
                Declaration d = m.getDeclaration();
                if (d==null) {
                    that.addError("could not determine type to instantiate");
                }
                else if (d instanceof Functional) {
                    Functional f = (Functional) d;
                    //TODO: type argument substitution
                    that.setTypeModel(f.getType());
                    checkInvocationArguments(that, f);
                }
                else {
                    that.addError("receiving expression cannot be invoked: " + 
                           d.getName());
                }
            }
        }
    }

    private void checkInvocationArguments(Tree.InvocationExpression that, Functional f) {
        List<ParameterList> pls = f.getParameterLists();
        if (pls.isEmpty()) {
            that.addError("receiver does not define a parameter list: " + 
                    f.getName());
        }
        else {
            ParameterList pl = pls.get(0);

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

    private void checkNamedArguments(ParameterList pl,
            Tree.NamedArgumentList nal) {
        List<Tree.NamedArgument> na = nal.getNamedArguments();
        if ( pl.getParameters().size()!=na.size() ) {
            nal.addError("wrong number of arguments");
        }
        //TODO!!
    }

    private void checkPositionalArguments(ParameterList pl,
            Tree.PositionalArgumentList pal) {
        List<Tree.PositionalArgument> pa = pal.getPositionalArguments();
        if ( pl.getParameters().size()!=pa.size() ) {
            pal.addError("wrong number of arguments");
        }
        else {
            for (int i=0; i<pl.getParameters().size(); i++) {
                Parameter p = pl.getParameters().get(i);
                ProducedType paramType = p.getType();
                Tree.PositionalArgument a = pa.get(i);
                ProducedType argType = a.getExpression().getTypeModel();
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
    }
    
    @Override public void visit(Tree.IndexExpression that) {
        super.visit(that);
        //TODO!
    }
    
    @Override public void visit(Tree.PostfixOperatorExpression that) {
        super.visit(that);
        ProducedType pt = that.getPrimary().getTypeModel();
        if (pt!=null) {
            that.setTypeModel(pt);
        }
    }
        
    //Atoms:
    
    @Override public void visit(Tree.Member that) {
        //TODO: this does not correctly handle methods
        //      and classes which are not subsequently 
        //      invoked (should return the callable type)
        TypedDeclaration d = Util.getDeclaration(that, context);
        if (d==null) {
            that.addError("could not determine target of member reference: " +
                    that.getIdentifier().getText());
        }
        else {
            MemberReference mr = new MemberReference();
            mr.setDeclaration(d);
            that.setMemberReference(mr);
            ProducedType t = d.getType();
            if (t==null) {
                that.addError("could not determine type of member reference: " +
                        that.getIdentifier().getText());
            }
            else {
                that.setTypeModel(t);
            }
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
            that.addError("expression not well formed");
        }
        else {
            ProducedType t = term.getTypeModel();
            if (t==null) {
                that.addError("could not determine type of expression");
            }
            else {
                that.setTypeModel(t);
            }
        }
    }
    
    @Override public void visit(Tree.Outer that) {
        that.setTypeModel(getOuterType(that, that.getScope()));
    }

    private ProducedType getOuterType(Node that, Scope scope) {
        Boolean foundInner = false;
        while (!(scope instanceof Package)) {
            if (scope instanceof ClassOrInterface) {
                if (foundInner) {
                    ProducedType t = new ProducedType();
                    t.setDeclaration((ClassOrInterface) scope);
                    //TODO: type arguments
                    return t;
                }
                else {
                    foundInner = true;
                }
            }
            scope = scope.getContainer();
        }
        that.addError("can't use outer outside of nested class or interface");
        return null;
    }
    
    @Override public void visit(Tree.Super that) {
        if (classOrInterface==null) {
            that.addError("can't use super outside a class");
        }
        else if (!(classOrInterface instanceof Class)) {
            that.addError("can't use super inside an interface");
        }
        else {
            ProducedType t = classOrInterface.getExtendedType();
            //TODO: type arguments
            that.setTypeModel(t);
        }
    }
    
    @Override public void visit(Tree.This that) {
        if (classOrInterface==null) {
            that.addError("can't use this outside a class or interface");
        }
        else {
            ProducedType t = new ProducedType();
            t.setDeclaration(classOrInterface);
            //TODO: type arguments
            that.setTypeModel(t);
        }
    }
    
    @Override public void visit(Tree.Subtype that) {
        //TODO!
    }
    
    @Override public void visit(Tree.SequenceEnumeration that) {
        super.visit(that);
        ProducedType et = null; 
        for (Tree.Expression e: that.getExpressionList().getExpressions()) {
            if (et==null) {
                et = e.getTypeModel();
            }
            //TODO: determine the common supertype of all of them
        }
        ProducedType t = new ProducedType();
        t.setDeclaration( (Interface) Util.getLanguageModuleDeclaration("Sequence", context) );
        t.getTypeArguments().add(et);
        that.setTypeModel(t);
    }
    
    @Override public void visit(Tree.StringTemplate that) {
        super.visit(that);
        //TODO: validate that the subexpression types are Formattable
        setLiteralType(that, "String");
    }
    
    @Override public void visit(Tree.StringLiteral that) {
        setLiteralType(that, "String");
    }
    
    @Override public void visit(Tree.NaturalLiteral that) {
        setLiteralType(that, "Natural");
    }
    
    @Override public void visit(Tree.FloatLiteral that) {
        setLiteralType(that, "Float");
    }
    
    @Override public void visit(Tree.CharLiteral that) {
        setLiteralType(that, "Character");
    }
    
    @Override public void visit(Tree.QuotedLiteral that) {
        setLiteralType(that, "Quoted");
    }
    
    private void setLiteralType(Tree.Atom that, String languageType) {
        ProducedType t = new ProducedType();
        t.setDeclaration( (Class) Util.getLanguageModuleDeclaration(languageType, context) );
        that.setTypeModel(t);
    }
    
    @Override
    public void visit(Tree.CompilerAnnotation that) {
        //don't visit the argument       
    }
    
}
