package com.redhat.ceylon.compiler.analyzer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.redhat.ceylon.compiler.context.Context;
import com.redhat.ceylon.compiler.model.Class;
import com.redhat.ceylon.compiler.model.ClassOrInterface;
import com.redhat.ceylon.compiler.model.Functional;
import com.redhat.ceylon.compiler.model.Interface;
import com.redhat.ceylon.compiler.model.Package;
import com.redhat.ceylon.compiler.model.Parameter;
import com.redhat.ceylon.compiler.model.ParameterList;
import com.redhat.ceylon.compiler.model.ProducedReference;
import com.redhat.ceylon.compiler.model.ProducedType;
import com.redhat.ceylon.compiler.model.ProducedTypedReference;
import com.redhat.ceylon.compiler.model.Scope;
import com.redhat.ceylon.compiler.model.TypeDeclaration;
import com.redhat.ceylon.compiler.model.TypedDeclaration;
import com.redhat.ceylon.compiler.model.Value;
import com.redhat.ceylon.compiler.tree.Node;
import com.redhat.ceylon.compiler.tree.Tree;
import com.redhat.ceylon.compiler.tree.Tree.BinaryOperatorExpression;
import com.redhat.ceylon.compiler.tree.Tree.LowerBound;
import com.redhat.ceylon.compiler.tree.Tree.Primary;
import com.redhat.ceylon.compiler.tree.Tree.SpecifierExpression;
import com.redhat.ceylon.compiler.tree.Tree.Term;
import com.redhat.ceylon.compiler.tree.Tree.TypeArgumentList;
import com.redhat.ceylon.compiler.tree.Tree.UpperBound;
import com.redhat.ceylon.compiler.tree.Tree.Variable;
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
    
    public void visit(Tree.ObjectDeclaration that) {
        ClassOrInterface o = classOrInterface;
        classOrInterface = (Class) ((Value) that.getDeclarationModel()).getType().getDeclaration();
        super.visit(that);
        classOrInterface = o;
    }
    
    public void visit(Tree.ObjectArgument that) {
        ClassOrInterface o = classOrInterface;
        classOrInterface = (Class) ((Value) that.getDeclarationModel()).getType().getDeclaration();
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

    @Override public void visit(Tree.Variable that) {
        super.visit(that);
        if (that.getSpecifierExpression()!=null) {
            inferType(that, that.getSpecifierExpression());
            checkType(that, that.getSpecifierExpression());
        }
    }
    
    @Override public void visit(Tree.IsCondition that) {
        if (that.getVariable()!=null) {
            visit(that.getVariable().getSpecifierExpression());
        }
        if (that.getExpression()!=null) {
            visit(that.getExpression());
        }
    }
    
    @Override public void visit(Tree.ExistsCondition that) {
        ProducedType t = null;
        Node n = that;
        Variable v = that.getVariable();
        TypeDeclaration ot = (TypeDeclaration) Util.getLanguageModuleDeclaration("Optional", context);
        if (v!=null) {
            SpecifierExpression se = v.getSpecifierExpression();
            visit(se);
            inferType(v, se, ot);
            checkType(v, se, ot);
            t = se.getExpression().getTypeModel();
            n = v;
        }
        if (that.getExpression()!=null) {
            visit(that.getExpression());
            t = that.getExpression().getTypeModel();
            n = that.getExpression();
        }
        if (t==null) {
            n.addError("could not determine if expression is of optional type");
        }
        else if (t.getDeclaration()!=ot) {
            n.addError("expression is not of optional type");
        }
    }

    @Override public void visit(Tree.BooleanCondition that) {
        super.visit(that);
        if (that.getExpression()!=null) {
            ProducedType t = that.getExpression().getTypeModel();
            if (t==null) {
                that.addError("could not determine if expression is of boolean type");
            }
            else {
                ProducedType bt = getLanguageType("Boolean");
                if (!bt.isSupertypeOf(t)) {
                    that.addError("expression is not of boolean type");
                }
            }
        }
    }

    @Override public void visit(Tree.ValueIterator that) {
        super.visit(that);
        TypeDeclaration it = (TypeDeclaration) Util.getLanguageModuleDeclaration("Iterable", context);
        inferType(that.getVariable(), that.getSpecifierExpression(), it);
        checkType(that.getVariable(), that.getSpecifierExpression(), it);
    }

    @Override public void visit(Tree.KeyValueIterator that) {
        super.visit(that);
        //TODO: infer type from arguments to Iterable<Entry<K,V>>
    }
    
    @Override public void visit(Tree.AttributeDeclaration that) {
        super.visit(that);
        inferType(that, that.getSpecifierOrInitializerExpression());
        checkType(that, that.getSpecifierOrInitializerExpression());
    }

    @Override public void visit(Tree.SpecifierStatement that) {
        super.visit(that);
        checkType(that.getMember(), that.getSpecifierExpression());
    }

    @Override public void visit(Tree.Parameter that) {
        super.visit(that);
        checkType(that, that.getSpecifierExpression());
    }

    private void checkType(ProducedType dt, Tree.SpecifierOrInitializerExpression sie) {
        if (sie!=null) {
            ProducedType et = sie.getExpression().getTypeModel();
            if ( et!=null && dt!=null) {
                if ( !dt.isSupertypeOf(et) ) {
                    sie.addError("specifier expression not assignable to expected type: " + 
                            et.getProducedTypeName() + " is not " + 
                            dt.getProducedTypeName());
                }
            }
            else {
                sie.addError("could not determine assignability of specified expression to expected type");
            }
        }
    }

    private void checkType(Tree.Member td, Tree.SpecifierOrInitializerExpression sie) {
        checkType(td.getTypeModel(), sie);
    }
    
    private void checkType(Tree.TypedDeclaration td, Tree.SpecifierOrInitializerExpression sie) {
        checkType(td.getTypeOrSubtype().getTypeModel(), sie);
    }
    
    private void checkType(Tree.Variable var, Tree.SpecifierExpression se, TypeDeclaration otd) {
        ProducedType vt = var.getTypeOrSubtype().getTypeModel();
        ProducedType t = otd.getProducedType(Collections.singletonList(vt));
        checkType(t, se);
    }

    @Override public void visit(Tree.AttributeGetterDefinition that) {
        Tree.TypeOrSubtype rt = beginReturnScope(that.getTypeOrSubtype());
        super.visit(that);
        inferType(that, that.getBlock());
        endReturnScope(rt);
    }

    @Override public void visit(Tree.AttributeArgument that) {
        Tree.TypeOrSubtype rt = beginReturnScope(that.getTypeOrSubtype());
        super.visit(that);
        //TODO: inferType(that, that.getBlock());
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

    @Override public void visit(Tree.MethodArgument that) {
        Tree.TypeOrSubtype rt = beginReturnScope(that.getTypeOrSubtype());           
        super.visit(that);
        endReturnScope(rt);
        //TODO: inferType(that, that.getBlock());
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
        if (that.getTypeOrSubtype() instanceof Tree.LocalModifier) {
            if (spec!=null) {
                setType((Tree.LocalModifier) that.getTypeOrSubtype(), spec, that);
            }
            else {
                that.addError("could not infer type of: " + 
                        Util.name(that));
            }
        }
    }

    private void inferType(Tree.Variable that, Tree.SpecifierExpression se, TypeDeclaration td) {
        if (that.getTypeOrSubtype() instanceof Tree.LocalModifier) {
            if (se!=null) {
                setTypeFromTypeArgument((Tree.LocalModifier) that.getTypeOrSubtype(), se, that, td);
            }
            else {
                that.addError("could not infer type of: " + 
                        Util.name(that));
            }
        }
    }

    private void setTypeFromTypeArgument(Tree.LocalModifier local, 
            Tree.SpecifierExpression s, 
            Tree.Variable that,
            TypeDeclaration td) {
        ProducedType ot = s.getExpression().getTypeModel();
        //TODO: search for the correct type to look for the argument of!
        if (ot!=null && ot.getTypeArguments().size()==1) {
            ProducedType t = ot.getTypeArguments().values().iterator().next();
            local.setTypeModel(t);
            ((TypedDeclaration) that.getDeclarationModel()).setType(t);
        }
        else {
            that.addError("could not infer type of: " + 
                    Util.name(that));
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
                        if ( !et.isSupertypeOf(at) ) {
                            that.addError("returned expression not assignable to expected return type: " +
                                    at.getProducedTypeName() + " is not " +
                                    et.getProducedTypeName());
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
                    handleMemberReference(that, pt, gt, (Tree.Member) mt);
                }
                else if (mt instanceof Tree.Type) {
                    handleMemberTypeReference(that, pt, gt, (Tree.Type) mt);
                }
                else if (mt instanceof Tree.Outer) {
                    handleOuterReference(that, gt, mt);
                }
                else {
                    that.addError("not a valid member reference");
                }
            }
        }
    }

    private void handleOuterReference(Tree.MemberExpression that, TypeDeclaration gt,
            Tree.MemberOrType mt) {
        if (gt instanceof ClassOrInterface) {
            that.setTypeModel(getOuterType(mt, (ClassOrInterface) gt));
            //TODO: some kind of MemberReference
        }
        else {
            that.addError("can't use outer on a type parameter");
        }
    }

    private void handleMemberTypeReference(Tree.MemberExpression that,
            ProducedType pt, TypeDeclaration gt, Tree.Type tt) {
        TypeDeclaration member = Util.getDeclaration((Scope) gt, tt, context);
        if (member==null) {
            tt.addError("could not determine target of member type reference: " +
                    tt.getIdentifier().getText());
        }
        else {
            List<ProducedType> typeArgs = getTypeArguments(tt, tt.getTypeArgumentList());
            if (!com.redhat.ceylon.compiler.model.Util.acceptsArguments(member, typeArgs)) {
                tt.addError("member type does not accept the given type arguments");
            }
            else {
                ProducedType t = pt.getTypeMember(member, typeArgs);
                that.setTypeModel(t);
                that.setMemberReference(t);
            }
        }
    }

    private void handleMemberReference(Tree.MemberExpression that,
            ProducedType pt, TypeDeclaration gt, Tree.Member m) {
        TypedDeclaration member = Util.getDeclaration((Scope) gt, m, context);
        if (member==null) {
            m.addError("could not determine target of member reference: " +
                    m.getIdentifier().getText());
        }
        else {
            List<ProducedType> typeArgs = getTypeArguments(m, m.getTypeArgumentList());
            if (!com.redhat.ceylon.compiler.model.Util.acceptsArguments(member, typeArgs)) {
                m.addError("member does not accept the given type arguments");
            }
            else {
                ProducedTypedReference ptr = getMemberProducedReference(member, pt, m, typeArgs);
                if (ptr==null) {
                    m.addError("member not found: " + 
                            member.getName() + " of type " + 
                            pt.getDeclaration().getName());
                }
                else {
                    ProducedType t = ptr.getType();
                    that.setMemberReference(ptr);
                    that.setTypeModel(t);
                }
            }
        }
    }

    private ProducedTypedReference getMemberProducedReference(
            TypedDeclaration member, ProducedType pt, Tree.Member m,
            List<ProducedType> typeArgs) {
        if (member.getContainer() instanceof ClassOrInterface) {
            //TODO: we should try each class/interface that contains
            //      the current class/interface in turn until we
            //      get to toplevel scope (the package)
            //      ... or, well, perhaps not!!
            return pt.getTypedMember(member, typeArgs);
        }
        else {
            //TODO: should we even remove this one?
            return member.getProducedTypedReference(typeArgs);
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
            ProducedReference m = pr.getMemberReference();
            if (m==null || !m.isFunctional()) {
                that.addError("receiving expression cannot be invoked");
            }
            else {
                that.setTypeModel(m.getType());
                List<ParameterList> pls = ((Functional) m.getDeclaration()).getParameterLists();
                checkInvocationArguments(that, m, pls);
            }
        }
    }

    private void checkInvocationArguments(Tree.InvocationExpression that, ProducedReference pr, 
            List<ParameterList> pls) {
        if (pls.isEmpty()) {
            that.addError("receiver does not define a parameter list");
        }
        else {
            ParameterList pl = pls.get(0);
            
            Tree.PositionalArgumentList pal = that.getPositionalArgumentList();
            if ( pal!=null ) {
                checkPositionalArguments(pl, pr, pal);
            }
            
            Tree.NamedArgumentList nal = that.getNamedArgumentList();
            if (nal!=null) {
                checkNamedArguments(pl, pr, nal);
            }
        }
    }

    private void checkNamedArguments(ParameterList pl, ProducedReference pr, 
            Tree.NamedArgumentList nal) {
        List<Tree.NamedArgument> na = nal.getNamedArguments();        
        Set<Parameter> foundParameters = new HashSet<Parameter>();
        
        for (Tree.NamedArgument a: na) {
            Parameter p = getMatchingParameter(pl, a);
            if (p==null) {
                a.addError("no matching parameter for named argument: " + 
                        Util.name(a));
            }
            else {
                foundParameters.add(p);
                checkNamedArgument(a, pr, p);
            }
        }
        
        Tree.SequencedArgument sa = nal.getSequencedArgument();
        if (sa!=null) {
            Parameter sp = getSequencedParameter(pl);
            if (sp==null) {
                sa.addError("no matching sequenced parameter");
            }
            else {
                foundParameters.add(sp);
            }
            //TODO: check type!
        }
            
        for (Parameter p: pl.getParameters()) {
            if (!foundParameters.contains(p) && !p.isDefaulted()) {
                nal.addError("missing named argument to parameter: " + 
                        p.getName());
            }
        }
    }

    private void checkNamedArgument(Tree.NamedArgument a, ProducedReference pr, 
            Parameter p) {
        if (p.getType()==null) {
            a.addError("parameter type not known: " + Util.name(a));
        }
        else {
            ProducedType argType = null;
            if (a instanceof Tree.SpecifiedArgument) {
                argType = ((Tree.SpecifiedArgument) a).getSpecifierExpression().getExpression().getTypeModel();
            }
            else if (a instanceof Tree.TypedArgument) {
                argType = ((Tree.TypedArgument) a).getTypeOrSubtype().getTypeModel();
            }
            if (argType==null) {
                a.addError("could not determine assignability of argument to parameter: " +
                        p.getName());
            }
            else {
                ProducedType paramType = pr.getTypedParameter(p).getType();
                if ( !paramType.getType().isSupertypeOf(argType) ) {
                    a.addError("named argument not assignable to parameter type: " + 
                            Util.name(a) + " since " +
                            argType.getProducedTypeName() + " is not " +
                            paramType.getProducedTypeName());
                }
            }
        }
    }
    
    private Parameter getMatchingParameter(ParameterList pl, Tree.NamedArgument na) {
        for (Parameter p: pl.getParameters()) {
            if (p.getName().equals(na.getIdentifier().getText())) {
                return p;
            }
        }
        return null;
    }

    private Parameter getSequencedParameter(ParameterList pl) {
        int s = pl.getParameters().size();
        if (s==0) return null;
        Parameter p = pl.getParameters().get(s-1);
        if (p.isSequenced()) {
            return p;
        }
        else {
            return null;
        }
    }

    private void checkPositionalArguments(ParameterList pl, ProducedReference r, 
            Tree.PositionalArgumentList pal) {
        List<Tree.PositionalArgument> args = pal.getPositionalArguments();
        List<Parameter> params = pl.getParameters();
        for (int i=0; i<params.size(); i++) {
            Parameter p = params.get(i);
            if (i>=args.size()) {
                if (!p.isDefaulted() && !p.isSequenced()) {
                    pal.addError("no argument to parameter: " + p.getName());
                }
            }
            else {
                Tree.PositionalArgument a = args.get(i);
                Tree.Expression e = a.getExpression();
                if (e==null) {
                    //TODO: this case is temporary until we get support for SPECIAL_ARGUMENTs
                }
                else {
                    ProducedType argType = e.getTypeModel();
                    ProducedType paramType = r.getTypedParameter(p).getType();
                    if (paramType!=null && argType!=null) {
                        if (!paramType.isSupertypeOf(argType)) {
                            a.addError("argument not assignable to parameter type: " + 
                                    p.getName() + " since " +
                                    argType.getProducedTypeName() + " is not " +
                                    paramType.getProducedTypeName());
                        }
                    }
                    else {
                        a.addError("could not determine assignability of argument to parameter: " +
                                p.getName());
                    }
                }
            }
        }
        //TODO: sequenced arguments!
        for (int i=params.size(); i<args.size(); i++) {
            args.get(i).addError("no matching parameter for argument");
        }
    }
    
    @Override public void visit(Tree.IndexExpression that) {
        super.visit(that);
        ProducedType pt = type(that);
        if (pt==null) {
            that.addError("could not determine type of receiver");
        }
        else {
            Interface s = (Interface) Util.getLanguageModuleDeclaration("Correspondence", context);
            ProducedType st = pt.getSupertype(s);
            if (st==null) {
                that.getPrimary().addError("receiving type of an index expression must be a Correspondence");
            }
            else {
                List<ProducedType> args = st.getTypeArgumentList();
                ProducedType kt = args.get(0);
                ProducedType vt = args.get(1);
                LowerBound lb = that.getLowerBound();
                if (lb==null) {
                    that.addError("missing lower bound");
                }
                else {
                    ProducedType lbt = lb.getExpression().getTypeModel();
                    if (lbt!=null) {
                        if (!kt.isSupertypeOf(lbt)) {
                            lb.addError("index must be of type: " +
                                    kt.getProducedTypeName());
                        }
                    }
                }
                ClassOrInterface rtd;
                UpperBound ub = that.getUpperBound();
                if (ub==null) {
                    rtd = (Class) Util.getLanguageModuleDeclaration("Optional", context);
                }
                else {
                    rtd = (Interface) Util.getLanguageModuleDeclaration("Sequence", context);
                    ProducedType ubt = ub.getExpression().getTypeModel();
                    if (ubt!=null) {
                        if (!kt.isSupertypeOf(ubt)) {
                            ub.addError("index must be of type: " +
                                    kt.getProducedTypeName());
                        }
                    }
                }
                ProducedType ot = rtd.getProducedType( Collections.singletonList(vt) );
                that.setTypeModel(ot);
            }
        }
    }
    
    @Override public void visit(Tree.PostfixOperatorExpression that) {
        super.visit(that);
        ProducedType pt = type(that);
        that.setTypeModel(pt);
    }

    private ProducedType type(Tree.PostfixExpression that) {
        Primary p = that.getPrimary();
        return p==null ? null : p.getTypeModel();
    }
    
    @Override public void visit(Tree.PrefixOperatorExpression that) {
        super.visit(that);
        ProducedType pt = type(that);
        that.setTypeModel(pt);
    }
    
    @Override public void visit(Tree.SumOp that) {
        super.visit( (BinaryOperatorExpression) that );
        ProducedType lhst = leftType(that);
        if (lhst!=null) {
            //take into account overloading of + operator
            if (lhst.isSubtypeOf(getLanguageType("String"))) {
                visitBinaryOperator(that, "String");
            }
            else {
                visitBinaryOperator(that, "Numeric");
            }
        }
    }

    private void visitComparisonOperator(Tree.BinaryOperatorExpression that, String type) {
        ProducedType lhst = leftType(that);
        ProducedType rhst = rightType(that);
        if ( rhst!=null && lhst!=null ) {
            ProducedType nt = lhst.getSupertype( (TypeDeclaration) Util.getLanguageModuleDeclaration(type, context) );
            if (nt==null) {
                that.getLeftTerm().addError("must be of type: " + type);
            }
            else {
                that.setTypeModel( getLanguageType("Boolean") );            
                if (!nt.isSupertypeOf(rhst)) {
                    that.getRightTerm().addError("must be of type: " + nt.getProducedTypeName());
                }
            }
        }
    }
    
    private void visitBinaryOperator(Tree.BinaryOperatorExpression that, String type) {
        ProducedType lhst = leftType(that);
        ProducedType rhst = rightType(that);
        if ( rhst!=null && lhst!=null ) {
            ProducedType nt = lhst.getSupertype( (TypeDeclaration) Util.getLanguageModuleDeclaration(type, context) );
            if (nt==null) {
                that.getLeftTerm().addError("must be of type: " + type);
            }
            else {
                ProducedType t = nt.getTypeArguments().isEmpty() ? 
                        nt : nt.getTypeArguments().values().iterator().next();
                that.setTypeModel(t);
                if (!nt.isSupertypeOf(rhst)) {
                    that.getRightTerm().addError("must be of type: " + nt.getProducedTypeName());
                }
            }
        }
    }

    private void visitDefaultOperator(Tree.BinaryOperatorExpression that) {
        ProducedType lhst = leftType(that);
        ProducedType rhst = rightType(that);
        if ( rhst!=null && lhst!=null ) {
            TypeDeclaration otd = (TypeDeclaration) Util.getLanguageModuleDeclaration("Optional", context);
            that.setTypeModel(rhst);
            ProducedType nt = rhst.getSupertype(otd);
            if (nt==null) {
                ProducedType ot = otd.getProducedType(Collections.singletonList(rhst));
                if (!lhst.isSubtypeOf(ot)) {
                    that.getLeftTerm().addError("must be of type: " + ot.getProducedTypeName());
                }
            }
            else {
                if (!lhst.isSubtypeOf(rhst)) {
                    that.getRightTerm().addError("must be of type: " + rhst.getProducedTypeName());
                }
            }
        }
    }
    
    private void visitUnaryOperator(Tree.UnaryOperatorExpression that, String type) {
        ProducedType t = type(that);
        if ( t!=null ) {
            ProducedType nt = t.getSupertype( (TypeDeclaration) Util.getLanguageModuleDeclaration(type, context) );
            if (nt==null) {
                that.getTerm().addError("must be of type: " + type);
            }
            else {
                ProducedType at = nt.getTypeArguments().isEmpty() ? 
                        nt : nt.getTypeArguments().values().iterator().next();
                that.setTypeModel(at);
            }
        }
    }

    private void visitFormatOperator(Tree.UnaryOperatorExpression that) {
        //TODO: reenable once we have extensions:
        /*ProducedType t = that.getTerm().getTypeModel();
        if ( t!=null ) {
            if ( !getLanguageType("Formattable").isSupertypeOf(t) ) {
                that.getTerm().addError("must be of type: Formattable");
            }
        }*/
        that.setTypeModel( getLanguageType("String") );
    }
    
    private void visitAssignOperator(Tree.AssignOp that) {
        ProducedType rhst = rightType(that);
        ProducedType lhst = leftType(that);
        if ( rhst!=null && lhst!=null ) {
            if ( !rhst.isSubtypeOf(lhst) ) {
                that.getRightTerm().addError("must be of type " +
                        lhst.getProducedTypeName());
            }
        }
        //TODO: validate that the LHS really is assignable
        that.setTypeModel(rhst);
    }
    
    private ProducedType rightType(Tree.BinaryOperatorExpression that) {
        Term rt = that.getRightTerm();
        return rt==null? null : rt.getTypeModel();
    }

    private ProducedType leftType(Tree.BinaryOperatorExpression that) {
        Term lt = that.getLeftTerm();
        return lt==null ? null : lt.getTypeModel();
    }
    
    private ProducedType type(Tree.UnaryOperatorExpression that) {
        Term t = that.getTerm();
        return t==null ? null : t.getTypeModel();
    }
    
    @Override public void visit(Tree.ArithmeticOp that) {
        super.visit(that);
        visitBinaryOperator(that, "Numeric");
    }
        
    @Override public void visit(Tree.BitwiseOp that) {
        super.visit(that);
        visitBinaryOperator(that, "Slots");
    }
        
    @Override public void visit(Tree.LogicalOp that) {
        super.visit(that);
        visitBinaryOperator(that, "Boolean");
    }
        
    @Override public void visit(Tree.EqualityOp that) {
        super.visit(that);
        visitComparisonOperator(that, "Equality");
    }
        
    @Override public void visit(Tree.ComparisonOp that) {
        super.visit(that);
        visitComparisonOperator(that, "Comparable");
    }
        
    @Override public void visit(Tree.IdenticalOp that) {
        super.visit(that);
        visitComparisonOperator(that, "IdentifiableObject");
    }
        
    @Override public void visit(Tree.DefaultOp that) {
        super.visit(that);
        visitDefaultOperator(that);
    }
        
    @Override public void visit(Tree.NegativeOp that) {
        super.visit(that);
        visitUnaryOperator(that, "Numeric");
    }
        
    @Override public void visit(Tree.FlipOp that) {
        super.visit(that);
        visitUnaryOperator(that, "Slots");
    }
        
    @Override public void visit(Tree.NotOp that) {
        super.visit(that);
        visitUnaryOperator(that, "Boolean");
    }
        
    @Override public void visit(Tree.AssignOp that) {
        super.visit(that);
        visitAssignOperator(that);
    }
        
    @Override public void visit(Tree.ArithmeticAssignmentOp that) {
        super.visit(that);
        visitBinaryOperator(that, "Numeric");
    }
        
    @Override public void visit(Tree.LogicalAssignmentOp that) {
        super.visit(that);
        visitBinaryOperator(that, "Boolean");
    }
        
    @Override public void visit(Tree.BitwiseAssignmentOp that) {
        super.visit(that);
        visitBinaryOperator(that, "Slots");
    }
        
    @Override public void visit(Tree.FormatOp that) {
        super.visit(that);
        visitFormatOperator(that);
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
            if ( that.getIdentifier().getText().equals("getIt") ) {
                that.getIdentifier();
            }
            List<ProducedType> typeArgs = getTypeArguments(that, that.getTypeArgumentList());
            if (!com.redhat.ceylon.compiler.model.Util.acceptsArguments(d, typeArgs)) {
                that.addError("does not accept the given type arguments");
            }
            else {
                ProducedReference pr = getProducedReference(d, classOrInterface, typeArgs);
                that.setMemberReference(pr);
                ProducedType t = pr.getType();
                if (t==null) {
                    that.addError("could not determine type of member reference: " +
                            that.getIdentifier().getText());
                }
                else {
                    that.setTypeModel(t);
                }
            }
        }
    }

    private ProducedTypedReference getProducedReference(TypedDeclaration d, Scope scope,
            List<ProducedType> typeArgs) {
        //TODO: we need to try each class/interface that contains
        //      the current class/interface in turn until we
        //      get to toplevel scope (the package)
        //TODO: I think we even need to try control blocks and
        //      method/attribute bodies!
        if ( d.getContainer() instanceof ClassOrInterface ) {
            //look for it as a declared or inherited 
            //member of the current class or interface
            while ( !(scope instanceof Package) ) {
                if (scope instanceof ClassOrInterface) {
                    ProducedTypedReference pr = ((ClassOrInterface) scope).getType().getTypedMember(d, typeArgs);
                    if (pr!=null) {
                        return pr;
                    }
                }
                scope = scope.getContainer();
            }
            //we will definitely find it, so this never occurs:
            throw new RuntimeException("member not found");
        }
        else {
            //it must be a member of an outer scope
            return d.getProducedTypedReference(typeArgs);
        }
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
                    return ((ClassOrInterface) scope).getType();
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
            that.setTypeModel(classOrInterface.getType());
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
        if (et!=null) {
            Interface std = (Interface) Util.getLanguageModuleDeclaration("Sequence", context);
            that.setTypeModel(std.getProducedType(Collections.singletonList(et)));
        }
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
        ProducedType t = getLanguageType(languageType);
        that.setTypeModel(t);
    }

    private ProducedType getLanguageType(String languageType) {
        Class td = (Class) Util.getLanguageModuleDeclaration(languageType, context);
        return td.getType();
    }
    
    @Override
    public void visit(Tree.CompilerAnnotation that) {
        //don't visit the argument       
    }

    //copy/pasted from TypeVisitor!
    private List<ProducedType> getTypeArguments(Tree.MemberOrType that, TypeArgumentList tal) {
        List<ProducedType> typeArguments = new ArrayList<ProducedType>();
        if (tal!=null) {
            for (Tree.TypeOrSubtype ta: tal.getTypeOrSubtypes()) {
                ProducedType t = ta.getTypeModel();
                if (t==null) {
                    ta.addError("could not resolve type argument");
                    return null;
                }
                else {
                    typeArguments.add(t);
                }
            }
        }
        return typeArguments;
    }

}
