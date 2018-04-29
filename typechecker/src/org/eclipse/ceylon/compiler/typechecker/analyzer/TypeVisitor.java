/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.compiler.typechecker.analyzer;

import static java.lang.Integer.parseInt;
import static org.eclipse.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.correctionMessage;
import static org.eclipse.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.getPackageTypeDeclaration;
import static org.eclipse.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.getPackageTypedDeclaration;
import static org.eclipse.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.getTypeArguments;
import static org.eclipse.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.getTypeDeclaration;
import static org.eclipse.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.getTypeMember;
import static org.eclipse.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.getTypedDeclaration;
import static org.eclipse.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.isVeryAbstractClass;
import static org.eclipse.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.memberCorrectionMessage;
import static org.eclipse.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.typeParametersString;
import static org.eclipse.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.unwrapAliasedTypeConstructor;
import static org.eclipse.ceylon.compiler.typechecker.tree.TreeUtil.name;
import static org.eclipse.ceylon.compiler.typechecker.tree.TreeUtil.unwrapExpressionUntilTerm;
import static org.eclipse.ceylon.model.typechecker.model.ModelUtil.appliedType;
import static org.eclipse.ceylon.model.typechecker.model.ModelUtil.getContainingClassOrInterface;
import static org.eclipse.ceylon.model.typechecker.model.ModelUtil.getNativeDeclaration;
import static org.eclipse.ceylon.model.typechecker.model.ModelUtil.getNativeHeader;
import static org.eclipse.ceylon.model.typechecker.model.ModelUtil.intersection;
import static org.eclipse.ceylon.model.typechecker.model.ModelUtil.intersectionOfSupertypes;
import static org.eclipse.ceylon.model.typechecker.model.ModelUtil.isNativeForWrongBackend;
import static org.eclipse.ceylon.model.typechecker.model.ModelUtil.isTypeUnknown;
import static org.eclipse.ceylon.model.typechecker.model.ModelUtil.union;
import static org.eclipse.ceylon.model.typechecker.model.SiteVariance.IN;
import static org.eclipse.ceylon.model.typechecker.model.SiteVariance.OUT;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.ceylon.common.Backends;
import org.eclipse.ceylon.compiler.typechecker.tree.Node;
import org.eclipse.ceylon.compiler.typechecker.tree.Tree;
import org.eclipse.ceylon.compiler.typechecker.tree.Visitor;
import org.eclipse.ceylon.model.typechecker.model.Cancellable;
import org.eclipse.ceylon.model.typechecker.model.Class;
import org.eclipse.ceylon.model.typechecker.model.ClassAlias;
import org.eclipse.ceylon.model.typechecker.model.ClassOrInterface;
import org.eclipse.ceylon.model.typechecker.model.Constructor;
import org.eclipse.ceylon.model.typechecker.model.Declaration;
import org.eclipse.ceylon.model.typechecker.model.Function;
import org.eclipse.ceylon.model.typechecker.model.FunctionOrValue;
import org.eclipse.ceylon.model.typechecker.model.Interface;
import org.eclipse.ceylon.model.typechecker.model.NothingType;
import org.eclipse.ceylon.model.typechecker.model.Parameter;
import org.eclipse.ceylon.model.typechecker.model.Scope;
import org.eclipse.ceylon.model.typechecker.model.Specification;
import org.eclipse.ceylon.model.typechecker.model.Type;
import org.eclipse.ceylon.model.typechecker.model.TypeAlias;
import org.eclipse.ceylon.model.typechecker.model.TypeDeclaration;
import org.eclipse.ceylon.model.typechecker.model.TypeParameter;
import org.eclipse.ceylon.model.typechecker.model.TypedDeclaration;
import org.eclipse.ceylon.model.typechecker.model.Unit;
import org.eclipse.ceylon.model.typechecker.model.UnknownType;
import org.eclipse.ceylon.model.typechecker.model.Value;

/**
 * Second phase of type analysis.
 * Scan the compilation unit looking for literal type 
 * declarations and maps them to the associated model 
 * objects. Also builds up a list of imports for the 
 * compilation unit. Finally, assigns types to the 
 * associated model objects of declarations declared 
 * using an explicit type (this must be done in this 
 * phase, since shared declarations may be used out of 
 * order in expressions).
 * 
 * @author Gavin King
 *
 */
public class TypeVisitor extends Visitor {
    
    private Unit unit;

    private Cancellable cancellable;
    private boolean inDelegatedConstructor;
    private boolean inTypeLiteral;
    private boolean inExtendsOrClassAlias;
    
    public TypeVisitor(Cancellable cancellable) {
        this.cancellable = cancellable;
    }
    
    public TypeVisitor(Unit unit, Cancellable cancellable) {
        this.unit = unit;
        this.cancellable = cancellable;
    }
    
    @Override public void visit(Tree.CompilationUnit that) {
        unit = that.getUnit();
        super.visit(that);
    }
    
    public void visit(Tree.GroupedType that) {
        super.visit(that);
        Tree.StaticType type = that.getType();
        if (type!=null) {
            that.setTypeModel(type.getTypeModel());
        }
    }
    
    @Override
    public void visit(Tree.UnionType that) {
        super.visit(that);
        List<Tree.StaticType> sts = 
                that.getStaticTypes();
        List<Type> types = 
                new ArrayList<Type>
                    (sts.size());
        for (Tree.StaticType st: sts) {
            //can't use addToUnion() here
            Type t = st.getTypeModel();
            if (t!=null) {
                types.add(t);
            }
        }
        Type type = union(types ,unit);
        that.setTypeModel(type);
    }
    
    @Override 
    public void visit(Tree.IntersectionType that) {
        super.visit(that);
        List<Tree.StaticType> sts = 
                that.getStaticTypes();
        List<Type> types = 
                new ArrayList<Type>
                    (sts.size());
        for (Tree.StaticType st: sts) {
            //can't use addToIntersection() here
            Type t = st.getTypeModel();
            if (t!=null) {
                types.add(t);
            }
        }
        Type type = intersection(types, unit);
        that.setTypeModel(type);
    }
    
    @Override 
    public void visit(Tree.SequenceType that) {
        super.visit(that);
        Tree.StaticType elementType = that.getElementType();
        Tree.NaturalLiteral length = that.getLength();
        Type et = elementType.getTypeModel();
        if (et!=null) {
            Type t;
            if (length==null) {
                t = unit.getSequentialType(et);
            }
            else {
                final int len;
                try {
                    len = parseInt(length.getText());
                }
                catch (NumberFormatException nfe) {
                    length.addError("must be a positive decimal integer");
                    return;
                }
                if (len<1) {
                    length.addError("must be positive");
                    return;
                }
                if (len>1000) {
                    length.addError("may not be greater than 1000");
                    return;
                }
                Class td = unit.getTupleDeclaration();
                t = unit.getEmptyType();
                for (int i=0; i<len; i++) {
                    t = appliedType(td, et, et, t);
                }
            }
            that.setTypeModel(t);
        }
    }
    
    @Override 
    public void visit(Tree.IterableType that) {
        super.visit(that);
        Tree.Type elem = that.getElementType();
        if (elem==null) {
            Type nt = unit.getNothingType();
            that.setTypeModel(unit.getIterableType(nt));
            that.addError("iterable type must have an element type");
        }
        else {
            if (elem instanceof Tree.SequencedType) {
                Tree.SequencedType st = 
                        (Tree.SequencedType) elem;
                Type et = st.getType().getTypeModel();
                if (et!=null) {
                    Type t =
                            st.getAtLeastOne() ?
                                unit.getNonemptyIterableType(et) :
                                unit.getIterableType(et);
                    that.setTypeModel(t);
                }
            }
            else {
                that.addError("malformed iterable type");
            }
        }
    }
    
    @Override
    public void visit(Tree.OptionalType that) {
        super.visit(that);
        List<Type> types = 
                new ArrayList<Type>(2);
        types.add(unit.getNullType());
        Type dt = that.getDefiniteType().getTypeModel();
        if (dt!=null) types.add(dt);
        that.setTypeModel(union(types, unit));
    }
    
    @Override
    public void visit(Tree.EntryType that) {
        super.visit(that);
        Type kt = 
                that.getKeyType().getTypeModel();
        Type vt = 
                that.getValueType()==null ? 
                        new UnknownType(unit).getType() : 
                        that.getValueType().getTypeModel();
        that.setTypeModel(unit.getEntryType(kt, vt));
    }
    
    @Override
    public void visit(Tree.TypeConstructor that) {
        super.visit(that);
        TypeAlias ta = that.getDeclarationModel();
        ta.setExtendedType(that.getType().getTypeModel());
        Type type = ta.getType();
        type.setTypeConstructor(true);
        that.setTypeModel(type);
    }
    
    @Override
    public void visit(Tree.FunctionType that) {
        super.visit(that);
        Tree.StaticType rt = 
                that.getReturnType();
        if (rt!=null) {
            List<Tree.Type> argumentTypes = 
                    that.getArgumentTypes();
            Type tt = getTupleType(argumentTypes, unit);
            Interface cd = unit.getCallableDeclaration();
            Type pt = 
                    appliedType(cd, rt.getTypeModel(), tt);
            that.setTypeModel(pt);
        }
    }
    
    @Override
    public void visit(Tree.TupleType that) {
        super.visit(that);
        List<Tree.Type> elementTypes = 
                that.getElementTypes();
        Type tt = getTupleType(elementTypes, unit);
        that.setTypeModel(tt);
    }

    static Type getTupleType(List<Tree.Type> ets, 
            Unit unit) {
        List<Type> args = 
                new ArrayList<Type>
                    (ets.size());
        boolean sequenced = false;
        boolean atleastone = false;
        int firstDefaulted = -1;
        for (int i=0; i<ets.size(); i++) {
            Tree.Type st = ets.get(i);
            Type arg = st==null ? 
                    null : st.getTypeModel();
            if (arg==null) {
                arg = new UnknownType(unit).getType();
            }
            else if (st instanceof Tree.SpreadType) {
                //currently we only allow a
                //single spread type, but in
                //future we should also allow
                //X, Y, *Zs
                return st.getTypeModel();
            }
            else if (st instanceof Tree.DefaultedType) {
                if (firstDefaulted==-1) {
                    firstDefaulted = i;
                }
            }
            else if (st instanceof Tree.SequencedType) {
                if (i!=ets.size()-1) {
                    st.addError("variant element must occur last in a tuple type");
                }
                else {
                    sequenced = true;
                    Tree.SequencedType sst = 
                            (Tree.SequencedType) st;
                    atleastone = sst.getAtLeastOne();
                    arg = sst.getType().getTypeModel();
                }
                if (firstDefaulted!=-1 && atleastone) {
                    st.addError("nonempty variadic element must occur after defaulted elements in a tuple type");
                }
            }
            else {
                if (firstDefaulted!=-1) {
                    st.addError("required element must occur after defaulted elements in a tuple type");
                }
            }
            args.add(arg);
        }
        return getTupleType(args, sequenced, atleastone, 
                firstDefaulted, unit);
    }

    //Note: quite similar to Unit.getTupleType(), but does 
    //      not canonicalize (since aliases are not yet 
    //      resolvable in this phase)
    private static Type getTupleType(
            List<Type> elemTypes, 
            boolean variadic, boolean atLeastOne, 
            int firstDefaulted,
            Unit unit) {
        Class tupleDeclaration = unit.getTupleDeclaration();
        Type emptyType = unit.getEmptyType();
        Type result = emptyType;
        Type union = unit.getNothingType();
        int last = elemTypes.size()-1;
        for (int i=last; i>=0; i--) {
            Type elemType = elemTypes.get(i);
            //can't use addToUnion() here
            union = 
                    addToUncanonicalizedUnion(unit, 
                            union, elemType);
            if (variadic && i==last) {
                result = atLeastOne ? 
                        unit.getSequenceType(elemType) : 
                        unit.getSequentialType(elemType);
            }
            else {
                result = 
                        appliedType(tupleDeclaration, 
                                union, elemType, result);
                if (firstDefaulted>=0 && i>=firstDefaulted) {
                    //can't use addToUnion() here
                    result = 
                            addToUncanonicalizedUnion(unit, 
                                    result, emptyType);
                }
            }
        }
        return result;
    }

    private static Type addToUncanonicalizedUnion(Unit unit, 
            Type union, Type type) {
        if (union.isNothing() || type.isAnything()) {
            return type;
        }
        else if (union.isAnything() || type.isNothing()) {
            return union;
        }
        else {
            List<Type> pair = new ArrayList<Type>();
            pair.add(type);
            pair.add(union);
            return union(pair, unit);
        }
    }
    
    @Override 
    public void visit(Tree.BaseType that) {
        super.visit(that);
        Tree.Identifier id = that.getIdentifier();
        if (id!=null) {
            String name = name(id);
            Scope scope = that.getScope();
            TypeDeclaration type; 
            if (that.getPackageQualified()) {
                type = getPackageTypeDeclaration(name, 
                        null, false, unit);
            }
            else {
                type = getTypeDeclaration(scope, name, 
                        null, false, unit);
            }
            if (type==null) {
                if (!isNativeForWrongBackend(scope, unit)) {
                    that.addError("type is not defined: '" 
                            + name + "'" 
                            + correctionMessage(name, scope, 
                                    unit, cancellable), 
                            102);
                    unit.setUnresolvedReferences();
                }
            }
            else {
                type = (TypeDeclaration) 
                        handleNativeHeader(type, that);
                Type outerType = 
                        scope.getDeclaringType(type);
                visitSimpleType(that, outerType, type);
            }
        }
    }
    
    public void visit(Tree.SuperType that) {
        //if (inExtendsClause) { //can't appear anywhere else in the tree!
        Scope scope = that.getScope();
        ClassOrInterface ci = 
                getContainingClassOrInterface(scope);
        if (ci!=null) {
            if (scope instanceof Constructor) {
                that.setTypeModel(intersectionOfSupertypes(ci));
            }
            else if (ci.isClassOrInterfaceMember()) {
                ClassOrInterface oci = 
                        (ClassOrInterface) 
                        ci.getContainer();
                that.setTypeModel(intersectionOfSupertypes(oci));
            }
            else {
                that.addError("super appears in extends for non-member class");
            }
        }
        //}
    }
    
    @Override
    public void visit(Tree.MemberLiteral that) {
        super.visit(that);
        if (that.getType()!=null) {
            Type pt = that.getType().getTypeModel();
            if (pt!=null) {
                Tree.TypeArgumentList tal = 
                        that.getTypeArgumentList();
                if (tal!=null &&
                        isTypeUnknown(pt) && 
                        !pt.isUnknown()) {
                    tal.addError("qualifying type does not fully-specify type arguments");
                }
            }
        }
    }
    
    @Override
    public void visit(Tree.QualifiedType that) {
        boolean onl = inTypeLiteral;
        boolean oiea = inExtendsOrClassAlias;
        boolean oidc = inDelegatedConstructor;
        inTypeLiteral = false;
        inExtendsOrClassAlias = false;
        inDelegatedConstructor = false;
        super.visit(that);
        inExtendsOrClassAlias = oiea;
        inDelegatedConstructor = oidc;
        inTypeLiteral = onl;
        
        Tree.StaticType ot = that.getOuterType();        
        Type pt = ot.getTypeModel();
        if (pt!=null) {
            Tree.TypeArgumentList tal = 
                    that.getTypeArgumentList();
            if (that.getMetamodel() && 
                    tal!=null &&
                    isTypeUnknown(pt) && 
                    !pt.isUnknown()) {
                tal.addError("qualifying type does not fully-specify type arguments");
            }
            TypeDeclaration d = pt.getDeclaration();
            Tree.Identifier id = that.getIdentifier();
            if (id!=null) {
                String name = name(id);
                Scope scope = that.getScope();
                TypeDeclaration type = 
                        getTypeMember(d, name, 
                                null, false, unit, scope);
                if (type==null) {
                    if (!isNativeForWrongBackend(scope, unit)) {
                        if (d.isMemberAmbiguous(name, unit, null, false)) {
                            that.addError("member type declaration is ambiguous: '" + 
                                    name + "' for type '" + 
                                    d.getName() + "'");
                        }
                        else {
                            that.addError("member type is not defined: '" 
                                    + name + "' in type '" 
                                    + d.getName() + "'" 
                                    + memberCorrectionMessage(name, d, 
                                            null, unit, cancellable), 
                                    100);
                            unit.setUnresolvedReferences();
                        }
                    }
                }
                else {
                    visitSimpleType(that, pt, type);
                    if (type.isStatic()) {
                        ot.setStaticTypePrimary(true);
                    }
                }
            }
        }
    }
    
    @Override
    public void visit(Tree.TypeLiteral that) {
        inTypeLiteral = true;
        super.visit(that);
        inTypeLiteral = false;
    }

    private void visitSimpleType(Tree.SimpleType that, 
            Type ot, TypeDeclaration dec) {
        if (dec instanceof Constructor 
                //in a metamodel type literal, a constructor
                //is allowed
                && !inTypeLiteral 
                //for an extends clause or aliased class, 
                //either a class with parameters or a 
                //constructor is allowed
                && !inExtendsOrClassAlias 
                && !inDelegatedConstructor) {
            that.addError("constructor is not a type: '" + 
                    dec.getName(unit) + "' is a constructor");
        }
        
        Tree.TypeArgumentList tal = 
                that.getTypeArgumentList();
        
        if (tal!=null) {
            dec = unwrapAliasedTypeConstructor(dec);
        }
        
        List<TypeParameter> params = 
                dec.getTypeParameters();
        List<Type> typeArgs = 
                getTypeArguments(tal, ot, params);
        //Note: we actually *check* these type arguments
        //      later in ExpressionVisitor
        Type pt = dec.appliedType(ot, typeArgs);
        if (tal==null) {
            if (!params.isEmpty()) {
                pt.setTypeConstructor(true);
            }
        }
        else {
            //handled in ExpressionVisitor
            /*if (params.isEmpty()) {
                that.addError("type declaration does not accept type arguments: '" + 
                        dec.getName(unit) + 
                        "' is not a generic type");
            }*/
            tal.setTypeModels(typeArgs);
            List<Tree.Type> args = tal.getTypes();
            for (int i = 0; 
                    i<args.size() && 
                    i<params.size(); 
                    i++) {
                Tree.Type t = args.get(i);
                if (t instanceof Tree.StaticType) {
                    Tree.StaticType st = 
                            (Tree.StaticType) t;
                    Tree.TypeVariance variance = 
                            st.getTypeVariance();
                    if (variance!=null) {
                        TypeParameter p = params.get(i);
                        String var = variance.getText();
                        if (var.equals("out")) {
                            pt.setVariance(p, OUT);
                        }
                        else if (var.equals("in")) {
                            pt.setVariance(p, IN);
                        }
                        if (!p.isInvariant()) {
                            //Type doesn't yet know
                            //how to reason about *runtime*
                            //instantiations of variant types
                            //since they are effectively
                            //invariant
                            variance.addUnsupportedError(
                                    "use-site variant instantiation of declaration-site variant types is not supported: type parameter '" + 
                                    p.getName() + "' of '" + 
                                    dec.getName(unit) + 
                                    "' is declared " + 
                                    (p.isCovariant() ? "covariant" : "contravariant") +
                                    " (remove the '" + var + "')");
                        }
                    }
                }
            }
        }
        that.setTypeModel(pt);
        that.setDeclarationModel(dec);
    }

    @Override 
    public void visit(Tree.VoidModifier that) {
        Class vtd = unit.getAnythingDeclaration();
        if (vtd!=null) {
            that.setTypeModel(vtd.getType());
        }
    }

    @Override 
    public void visit(Tree.SequencedType that) {
        super.visit(that);
        Type type = that.getType().getTypeModel();
        if (type!=null) {
            Type et = 
                    that.getAtLeastOne() ? 
                        unit.getSequenceType(type) : 
                        unit.getSequentialType(type);
            that.setTypeModel(et);
        }
    }

    @Override 
    public void visit(Tree.DefaultedType that) {
        super.visit(that);
        Type type = that.getType().getTypeModel();
        if (type!=null) {
            that.setTypeModel(type);
        }
    }

    @Override 
    public void visit(Tree.SpreadType that) {
        super.visit(that);
        Tree.Type t = that.getType();
        if (t!=null) {
            Type type = t.getTypeModel();
            if (type!=null) {
                that.setTypeModel(type);
            }
        }
    }

    @Override 
    public void visit(Tree.TypedDeclaration that) {
        super.visit(that);
        Tree.Type type = that.getType();
        TypedDeclaration dec = that.getDeclarationModel();
        setType(that, type, dec);
        if (dec instanceof FunctionOrValue) {
            FunctionOrValue mv = (FunctionOrValue) dec;
            if (dec.isLate() && 
                    mv.isParameter()) {
                that.addError("parameter may not be annotated late");
            }
        }
    }

    @Override 
    public void visit(Tree.TypedArgument that) {
        super.visit(that);
        setType(that, that.getType(), 
                that.getDeclarationModel());
    }
    
    private void setType(Node that, Tree.Type type, 
            TypedDeclaration td) {
        if (type==null) {
            that.addError("missing type of declaration: '" + 
                    td.getName() + "'");
        }
        else if (!(type instanceof Tree.LocalModifier)) { //if the type declaration is missing, we do type inference later
            Type t = type.getTypeModel();
            if (t!=null) {
                td.setType(t);
            }
        }
    }
    
    @Override 
    public void visit(Tree.ObjectDefinition that) {
        Class o = that.getAnonymousClass();
        o.setExtendedType(unit.getBasicType());
        o.getSatisfiedTypes().clear();
        super.visit(that);
        Type type = o.getType();
        that.getDeclarationModel().setType(type);
        that.getType().setTypeModel(type);
    }

    @Override 
    public void visit(Tree.ObjectArgument that) {
        Class o = that.getAnonymousClass();
        o.setExtendedType(unit.getBasicType());
        o.getSatisfiedTypes().clear();
        super.visit(that);
        Type type = o.getType();
        that.getDeclarationModel().setType(type);
        that.getType().setTypeModel(type);
    }

    @Override 
    public void visit(Tree.ObjectExpression that) {
        Class o = that.getAnonymousClass();
        o.setExtendedType(unit.getBasicType());
        o.getSatisfiedTypes().clear();
        super.visit(that);
    }
    
    @Override 
    public void visit(Tree.ClassDefinition that) {
        Class cd = that.getDeclarationModel();
        if (!isVeryAbstractClass(that, unit)) {
            cd.setExtendedType(unit.getBasicType());
        }
        else {
            cd.setExtendedType(null);
        }
        cd.getSatisfiedTypes().clear();
        super.visit(that);
        Tree.ParameterList pl = that.getParameterList();
        if (pl!=null) {
            if (cd.hasConstructors()) {
                pl.addError("class with parameters may not declare constructors: class '" + 
                        cd.getName() + 
                        "' has a parameter list and a constructor", 1002);
            }
            else if (cd.hasEnumerated()) {
                pl.addError("class with parameters may not declare constructors: class '" + 
                        cd.getName() + 
                        "' has a parameter list and a value constructor", 1003);
            }
            else if (cd.hasStaticMembers()) {
                pl.addError("class with parameters may not declare static members: class '" + 
                        cd.getName() + 
                        "' has a parameter list and a static member", 1003);
            }
        }
        else {
            if (!cd.hasConstructors() && 
                !cd.hasEnumerated()) {
                // No parameter list and no constructors or enumerated
                // normally means something is wrong
                boolean error = true;
                // Check if the declaration is a native implementation
                if (cd.isNativeImplementation()) {
                    Declaration hdr = getNativeHeader(cd);
                    // Check that it has a native header
                    if (hdr instanceof Class) {
                        Class hcd = (Class) hdr;
                        // And finally check if the header has any
                        // constructors or enumerated
                        if (hcd.hasConstructors() || 
                            hcd.hasEnumerated()) {
                            // In that case there's no error
                            error = false;
                        }
                    }
                }
                if (error) {
                    that.addError("class must have a parameter list or at least one constructor: class '" + 
                            cd.getName() + 
                            "' has neither parameter list nor constructor", 
                            1001);
                }
            }
        }
    }
    
    @Override 
    public void visit(Tree.InterfaceDefinition that) {
        Interface id = that.getDeclarationModel();
        id.setExtendedType(null);
        id.getSatisfiedTypes().clear();
        Class od = unit.getObjectDeclaration();
        if (od!=null) {
            id.setExtendedType(od.getType());
        }
        super.visit(that);
    }

    @Override
    public void visit(Tree.TypeParameterDeclaration that) {
        TypeParameter p = that.getDeclarationModel();
        p.setExtendedType(null);
        p.getSatisfiedTypes().clear();
        Class vd = unit.getAnythingDeclaration();
        if (vd!=null) {
            p.setExtendedType(vd.getType());
        }
        
        super.visit(that);
        
        Tree.TypeSpecifier ts = that.getTypeSpecifier();
        if (ts!=null) {
            Tree.StaticType type = ts.getType();
            if (type!=null) {
                Type dta = type.getTypeModel();
                Declaration dec = p.getDeclaration();
                if (dta!=null && 
                        dta.involvesDeclaration(dec)) {
                    type.addError("default type argument involves parameterized type: '" + 
                            dta.asString(unit) + 
                            "' involves '" + 
                            dec.getName(unit) + 
                            "'");
                    dta = null;
                }
                p.setDefaultTypeArgument(dta);
            }
        }
        
    }
    
    @Override
    public void visit(Tree.TypeParameterList that) {
        super.visit(that);
        List<Tree.TypeParameterDeclaration> tpds = 
                that.getTypeParameterDeclarations();
        List<TypeParameter> params = 
                new ArrayList<TypeParameter>
                    (tpds.size());
        for (int i=tpds.size()-1; i>=0; i--) {
            Tree.TypeParameterDeclaration tpd = tpds.get(i);
            if (tpd!=null) {
                TypeParameter tp = 
                        tpd.getDeclarationModel();
                Type dta = tp.getDefaultTypeArgument();
                if (dta!=null) {
                    params.add(tp);
                    if (dta.involvesTypeParameters(params)) {
                        tpd.getTypeSpecifier()
                            .addError("default type argument involves a type parameter not yet declared");
                    }
                }
            }
        }
    }
    
    @Override 
    public void visit(Tree.ClassDeclaration that) {
        ClassAlias td = 
                (ClassAlias) 
                    that.getDeclarationModel();
        td.setExtendedType(null);
        super.visit(that);
        Tree.ClassSpecifier cs = that.getClassSpecifier();
        if (cs==null) {
            if (!td.isNativeHeader()) {
                that.addError("missing class body or aliased class reference");
            }
        }
        else {
            Tree.ExtendedType et = 
                    that.getExtendedType();
            if (et!=null) {
                et.addError("class alias may not extend a type");
            }
            Tree.SatisfiedTypes sts = 
                    that.getSatisfiedTypes();
            if (sts!=null) {
                sts.addError("class alias may not satisfy a type");
            }
            Tree.CaseTypes cts = 
                    that.getCaseTypes();
            if (cts!=null) {
                that.addError("class alias may not have cases or a self type");
            }
            Tree.SimpleType ct = cs.getType();
            if (ct==null) {
//                that.addError("malformed aliased class");
            }
            else if (!(ct instanceof Tree.StaticType)) {
                ct.addError("aliased type must be a class");
            }
            else {
                Type type = ct.getTypeModel();
                if (type!=null && !type.isUnknown()) {
                    TypeDeclaration dec = 
                            type.getDeclaration();
                    td.setConstructor(dec);
                    if (dec instanceof Constructor) {
                        if (dec.isValueConstructor()) {
                            ct.addError("aliases a value constructor");
                        }
                        else if (dec.isAbstract()) {
                            ct.addError("aliases a partial constructor: '" +
                                    dec.getName(unit) + 
                                    "' is declared abstract");
                        }
                        if (td.isShared() && !dec.isShared()) {
                            ct.addError("shared alias of an unshared constructor: '" +
                                    dec.getName(unit) + 
                                    "' is not shared");
                        }
                        type = type.getExtendedType();
                        dec = dec.getExtendedType()
                                .getDeclaration();
                    }
                    if (dec instanceof Class) {
                        td.setExtendedType(type);
                    }
                    else {
                        ct.addError("not a class: '" + 
                                dec.getName(unit) + "'");
                    }
                    TypeDeclaration etd = 
                            ct.getDeclarationModel();
                    if (etd==td) {
                        ct.addError("directly aliases itself: '" + 
                                td.getName() + "'");
                    }
                }
            }
        }
    }
    
    @Override 
    public void visit(Tree.InterfaceDeclaration that) {
        Interface id = that.getDeclarationModel();
        id.setExtendedType(null);
        super.visit(that);
        Tree.TypeSpecifier typeSpecifier = 
                that.getTypeSpecifier();
        if (typeSpecifier==null) {
            if (!id.isNativeHeader()) {
                that.addError("missing interface body or aliased interface reference");
            }
        }
        else {
            Tree.SatisfiedTypes sts = 
                    that.getSatisfiedTypes();
            if (sts!=null) {
                sts.addError("interface alias may not satisfy a type");
            }
            Tree.CaseTypes cts = 
                    that.getCaseTypes();
            if (cts!=null) {
                that.addError("class alias may not have cases or a self type");
            }
            Tree.StaticType et = 
                    typeSpecifier.getType();
            if (et==null) {
//                that.addError("malformed aliased interface");
            }
            else if (!(et instanceof Tree.StaticType)) {
                typeSpecifier.addError("aliased type must be an interface");
            }
            else {
                Type type = et.getTypeModel();
                if (type!=null && !type.isUnknown()) {
                    TypeDeclaration dec = 
                            type.getDeclaration();
                    if (dec instanceof Interface) {
                        id.setExtendedType(type);
                    } 
                    else {
                        et.addError("not an interface: '" + 
                                dec.getName(unit) + 
                                "'");
                    }
                }
            }
        }
    }
    
    @Override 
    public void visit(Tree.TypeAliasDeclaration that) {
        TypeAlias ta = that.getDeclarationModel();
        ta.setExtendedType(null);
        super.visit(that);
        Tree.TypeSpecifier typeSpecifier = 
                that.getTypeSpecifier();
        if (typeSpecifier==null) {
            if (!ta.isNativeHeader()) {
                that.addError("missing aliased type");
            }
        }
        else {
            Tree.StaticType et = typeSpecifier.getType();
            if (et==null) {
                that.addError("malformed aliased type");
            }
            else {
                ta.setExtendedType(et.getTypeModel());
            }
        }
    }
    
    private boolean isInitializerParameter(FunctionOrValue dec) {
        return dec!=null 
            && dec.isParameter() 
            && dec.getInitializerParameter()
                .isHidden();
    }
    
    @Override
    public void visit(Tree.MethodDeclaration that) {
        super.visit(that);
        Tree.SpecifierExpression sie = 
                that.getSpecifierExpression();
        Function dec = that.getDeclarationModel();
        if (isInitializerParameter(dec)) {
            if (sie!=null) {
                sie.addError("function is an initializer parameter and may not have an initial value: '" + 
                        dec.getName() + "'");
            }
        }
    }
    
    @Override
    public void visit(Tree.MethodDefinition that) {
        super.visit(that);
        Function dec = that.getDeclarationModel();
        if (isInitializerParameter(dec)) {
            that.getBlock()
                .addError("function is an initializer parameter and may not have a body: '" + 
                        dec.getName() + "'");
        }
    }
    
    @Override
    public void visit(Tree.AttributeDeclaration that) {
        super.visit(that);
        Tree.SpecifierOrInitializerExpression sie = 
                that.getSpecifierOrInitializerExpression();
        Value dec = that.getDeclarationModel();
        if (isInitializerParameter(dec)) {
            Parameter param = dec.getInitializerParameter();
            Tree.Type type = that.getType();
            if (type instanceof Tree.SequencedType) {
                param.setSequenced(true);
                Tree.SequencedType st = 
                        (Tree.SequencedType) type;
                param.setAtLeastOne(st.getAtLeastOne());
            }
            if (sie!=null) {
                sie.addError("value is an initializer parameter and may not have an initial value: '" + 
                        dec.getName() + "'");
            }
        }
    }
    
    @Override
    public void visit(Tree.AttributeGetterDefinition that) {
        super.visit(that);
        Value dec = that.getDeclarationModel();
        if (isInitializerParameter(dec)) {
            that.getBlock()
                .addError("value is an initializer parameter and may not have a body: '" + 
                        dec.getName() + "'");
        }
    }
    
    void checkExtendedTypeExpression(Tree.Type type) {
        if (type instanceof Tree.QualifiedType) {
            Tree.QualifiedType qualifiedType = 
                    (Tree.QualifiedType) type;
            Tree.StaticType outerType = 
                    qualifiedType.getOuterType();
            if (!(outerType instanceof Tree.SuperType)) {
                TypeDeclaration otd = 
                        qualifiedType.getDeclarationModel();
                if (otd!=null) {
                    if (otd.isStatic() || 
                            otd instanceof Constructor) {
                        checkExtendedTypeExpression(outerType);
                    }
                    else {
                        outerType.addError("illegal qualifier in constructor delegation (must be super)");
                    }
                }
            }
        }
    }
    
    private static void inheritedType(Tree.StaticType st) {
        if (st instanceof Tree.SimpleType) {
            ((Tree.SimpleType) st).setInherited(true);
            if (st instanceof Tree.QualifiedType) {
                inheritedType(((Tree.QualifiedType) st).getOuterType());
            }
        }
    }

    @Override 
    public void visit(Tree.DelegatedConstructor that) {
        inDelegatedConstructor = true;
        super.visit(that);
        inDelegatedConstructor = false;
        checkExtendedTypeExpression(that.getType());
        inheritedType(that.getType());
    }

    @Override 
    public void visit(Tree.ClassSpecifier that) {
        inExtendsOrClassAlias = true;
        super.visit(that);
        inExtendsOrClassAlias = false;
        checkExtendedTypeExpression(that.getType());
        inheritedType(that.getType());
    }
    
    @Override 
    public void visit(Tree.ExtendedType that) {
        inExtendsOrClassAlias = 
                that.getInvocationExpression()!=null;
        super.visit(that);
        inExtendsOrClassAlias = false;
        inheritedType(that.getType());
        checkExtendedTypeExpression(that.getType());
        TypeDeclaration td = 
                (TypeDeclaration) 
                    that.getScope();
        if (!td.isAlias()) {
            Tree.SimpleType et = that.getType();
            if (et!=null) {
                Type type = et.getTypeModel();
                if (type!=null) {
                    TypeDeclaration etd = 
                            et.getDeclarationModel();
                    if (etd!=null && 
                            !(etd instanceof UnknownType)) {
                        if (etd instanceof Constructor) {
                            type = type.getExtendedType();
                            etd = etd.getExtendedType()
                                    .getDeclaration();
                        }
                        if (type.isTypeConstructor()) {
                            et.addError("missing type arguments of generic type: '" + 
                                    etd.getName(unit) +
                                    "' has type parameters " +
                                    typeParametersString(etd) +
                                    " (add missing type argument list)");
                        }
                        else if (etd==td) {
                            //unnecessary, handled by SupertypeVisitor
//                          et.addError("directly extends itself: '" + 
//                                  td.getName() + "'");
                        }
                        else if (etd instanceof TypeParameter) {
                            et.addError("directly extends a type parameter: '" + 
                                    type.getDeclaration().getName(unit) + 
                                    "'");
                        }
                        else if (etd instanceof Interface) {
                            et.addError("extends an interface: '" + 
                                    type.getDeclaration().getName(unit) + 
                                    "'");
                        }
                        else if (etd instanceof TypeAlias) {
                            et.addError("extends a type alias: '" + 
                                    type.getDeclaration().getName(unit) + 
                                    "'");
                        }
                        else if (etd instanceof NothingType) {
                            et.addError("extends the bottom type 'Nothing'");
                        }
                        else {
                            td.setExtendedType(type);
                        }
                    }
                }
            }
        }
    }
    
    @Override 
    public void visit(Tree.SatisfiedTypes that) {
        super.visit(that);
        TypeDeclaration td = 
                (TypeDeclaration) 
                    that.getScope();
        if (td.isAlias()) {
            return;
        }
        List<Tree.StaticType> types = that.getTypes();
        List<Type> list = 
                new ArrayList<Type>
                    (types.size());
        if (types.isEmpty()) {
            that.addError("missing types in satisfies");
        }
        boolean foundTypeParam = false;
        boolean foundClass = false;
        boolean foundInterface = false;
        for (Tree.StaticType st: types) {
            inheritedType(st);
            Type type = st.getTypeModel();
            if (type!=null) {
                TypeDeclaration std = type.getDeclaration();
                if (type.isTypeConstructor()) {
                    st.addError("missing type arguments of generic type: '" + 
                            std.getName(unit) +
                            "' has type parameters " +
                            typeParametersString(std) +
                            " (add missing type argument list)");
                }
                else if (std!=null && 
                        !(std instanceof UnknownType)) {
                    if (std==td) {
                        //unnecessary, handled by SupertypeVisitor
//                      st.addError("directly extends itself: '" + 
//                              td.getName() + "'");
                    }
                    else if (std instanceof NothingType) {
                        st.addError("satisfies the bottom type 'Nothing'");
                    }
                    else if (std instanceof TypeAlias) {
                        st.addError("satisfies a type alias: '" + 
                                std.getName(unit) + 
                                "'");
                    }
                    else if (std instanceof Constructor) {
                        //nothing to do
                    }
                    else if (td instanceof TypeParameter) {
                        if (foundTypeParam) {
                            st.addUnsupportedError("type parameter upper bounds are not yet supported in combination with other bounds");
                        }
                        else if (std instanceof TypeParameter) {
                            if (foundClass||foundInterface) {
                                st.addUnsupportedError("type parameter upper bounds are not yet supported in combination with other bounds");
                            }
                            foundTypeParam = true;
                            list.add(type);
                        }
                        else if (std instanceof Class) {
                            if (foundClass) {
                                st.addUnsupportedError("multiple class upper bounds are not yet supported");
                            }
                            foundClass = true;
                            list.add(type);
                        }
                        else if (std instanceof Interface) {
                            foundInterface = true;
                            list.add(type);
                        }
                        else {
                            st.addError("upper bound must be a class, interface, or type parameter");
                        }
                    } 
                    else {
                        if (std instanceof TypeParameter) {
                            st.addError("directly satisfies type parameter: '" + 
                                    std.getName(unit) + "'");
                        }
                        else if (std instanceof Class) {
                            st.addError("satisfies a class: '" + 
                                    std.getName(unit) + "'");
                        }
                        else if (std instanceof Interface) {
                            if (td.isDynamic() && 
                                    !std.isDynamic()) {
                                st.addError("dynamic interface satisfies a non-dynamic interface: '" + 
                                        std.getName(unit) + "'");
                            }
                            else {
                                list.add(type);
                            }
                        }
                        else {
                            st.addError("satisfied type must be an interface");
                        }
                    }
                }
            }
        }
        td.setSatisfiedTypes(list);
    }
    
    @Override 
    public void visit(Tree.CaseTypes that) {
        super.visit(that);
        TypeDeclaration td = 
                (TypeDeclaration) 
                    that.getScope();
        List<Tree.StaticMemberOrTypeExpression> bmes = 
                that.getBaseMemberExpressions();
        List<Tree.StaticType> cts = that.getTypes();
        List<TypedDeclaration> caseValues = 
                new ArrayList<TypedDeclaration>
                    (bmes.size());
        List<Type> caseTypes = 
                new ArrayList<Type>
                    (bmes.size()+cts.size());
        if (td instanceof TypeParameter) {
            if (!bmes.isEmpty()) {
                that.addError("cases of type parameter must be a types");
            }
        }
        else {
            for (Tree.StaticMemberOrTypeExpression bme: bmes) {
                //bmes have not yet been resolved
                String name = name(bme.getIdentifier());
                TypedDeclaration od = 
                        bme instanceof Tree.BaseMemberExpression ?
                        getTypedDeclaration(bme.getScope(), name, 
                                null, false, unit) :
                        getPackageTypedDeclaration(name, 
                                null, false, unit);
                if (od!=null) {
                    caseValues.add(od);
                    Type type = od.getType();
                    if (type!=null) {
                        caseTypes.add(type);
                    }
                }
            }
        }
        for (Tree.StaticType ct: cts) {
            inheritedType(ct);
            Type type = ct.getTypeModel();
            if (!isTypeUnknown(type)) {
                if (type.isUnion() || 
                    type.isIntersection() ||
                    type.isNothing()) {
                    //union/intersection types don't have equals()
                    if (td instanceof TypeParameter) {
                        ct.addError("enumerated bound must be a class or interface type");
                    }
                    else {
                        ct.addError("case type must be a class, interface, or self type");
                    }
                }
                else {
                    TypeDeclaration ctd = type.getDeclaration();
                    if (ctd.equals(td)) {
                        ct.addError("directly enumerates itself: '" + 
                                td.getName() + "'");
                    }
                    else if (type.isClassOrInterface()) {
                        caseTypes.add(type);
                    }
                    else if (type.isTypeParameter()) {
                        if (td instanceof TypeParameter) {
                            caseTypes.add(type);
                        }
                        else {
                            TypeParameter tp = 
                                    (TypeParameter) ctd;
                            td.setSelfType(type);
                            if (tp.isSelfType()) {
                                ct.addError("type parameter may not act as self type for two different types");
                            }
                            else {
                                tp.setSelfTypedDeclaration(td);
                                caseTypes.add(type);
                            }
                            if (cts.size()>1) {
                                ct.addError("a type may not have more than one self type");
                            }
                        }
                    }
                    else {
                        if (td instanceof TypeParameter) {
                            ct.addError("enumerated bound must be a class or interface type");
                        }
                        else {
                            ct.addError("case type must be a class, interface, or self type");
                        }
                    }
                }
            }
        }
        if (!caseTypes.isEmpty()) {
            TypeDeclaration first = 
                    caseTypes.get(0)
                        .getDeclaration();
            if (caseTypes.size() == 1 
                    && first.isSelfType()) {
                //for a type family, the type that declares 
                //the type parameter may not be the same 
                //type for which it acts as a self type
                Scope scope = first.getContainer();
                if (scope instanceof ClassOrInterface) {
                    ClassOrInterface ci = 
                            (ClassOrInterface) scope;
                    if (!ci.isAbstract()) {
                        Tree.StaticType ct = cts.get(0);
                        if (ci.equals(td)) {
                            ct.addError("concrete class parameterized by self type: '" 
                                    + ci.getName() 
                                    + "' is not abstract but has the self type '" 
                                    + first.getName() + 
                                    "' (make '" + ci.getName() + "' abstract)",
                                    905);
                        }
                        else {
                            //type family
                            ct.addError("concrete class parameterized by self type: '"
                                    + ci.getName() 
                                    + "' is not abstract but declares the self type '" 
                                    + first.getName() 
                                    + "' of '" + td.getName() 
                                    + "' (make '" + ci.getName() + "' abstract)",
                                    905);
                        }
                    }
                }
            }
            else {
                if (td instanceof ClassOrInterface) {
                    ClassOrInterface ci =
                            (ClassOrInterface) td;
                    if (!ci.isAbstract()) {
                        Class c = (Class) ci;
                        if (!c.hasEnumerated()) {
                            that.addError("concrete class has enumerated subtypes: " + 
                                    "enumerated class '" + ci.getName() + "' is not abstract" +
                                    " (make '" + ci.getName() + "' abstract)",
                                    905);
                        }
                    }
                }
            }
            td.setCaseTypes(caseTypes);
            td.setCaseValues(caseValues);
        }
    }

    @Override
    public void visit(Tree.InitializerParameter that) {
        super.visit(that);
        Parameter p = that.getParameterModel();
        String name = p.getName();
        Declaration a = 
                that.getScope()
                    .getDirectMember(name, null, false);
        if (a==null) {
            //Now done in ExpressionVisitor!
//            that.addError("parameter is not defined: '" + p.getName() + "'");
        }
        else if (!isLegalParameter(a)) {
            that.addError("parameter is not a reference value or function: '" + 
                    name + "' is not a value or function");
        }
        else {
            if (a.isFormal()) {
                that.addError("parameter is a formal attribute: '" + 
                        name + "' is annotated 'formal'", 320);
            }
            FunctionOrValue mov = (FunctionOrValue) a;
            if (mov.getInitializerParameter()!=null) {
                that.addError("duplicate parameter: '" + 
                        name + "' already occurs in the parameter list");
            }
            else {
                mov.setInitializerParameter(p);
                p.setModel(mov);
            }
        }
        
        if (p.isDefaulted()) {
            checkDefaultArg(that.getSpecifierExpression(), p);
        }
    }

    public boolean isLegalParameter(Declaration a) {
        if (a instanceof Value) {
            Value v = (Value) a;
            if (v.isTransient()) {
                return false;
            }
            else {
                TypeDeclaration td = v.getTypeDeclaration();
                return td==null || !td.isObjectClass();
            }
        }
        else if (a instanceof Function) {
            return true;
        }
        else {
            return false;
        }
    }
    
    @Override
    public void visit(Tree.AnyAttribute that) {
        super.visit(that);
        Tree.Type type = that.getType();
        if (type instanceof Tree.SequencedType) {
            Value v = (Value) that.getDeclarationModel();
            Parameter p = v.getInitializerParameter();
            if (p==null) {
                type.addError("value is not a parameter, so may not be variadic: '" +
                        v.getName() + "'");
            }
            else {
                p.setSequenced(true);
            }
        }
    }

    @Override
    public void visit(Tree.AnyMethod that) {
        super.visit(that);
        Tree.Type type = that.getType();
        if (type instanceof Tree.SequencedType) {
            type.addError("function type may not be variadic");
        }
    }
    
    @Override 
    public void visit(Tree.QualifiedMemberOrTypeExpression that) {
        Tree.Primary primary = that.getPrimary();
        if (primary instanceof Tree.MemberOrTypeExpression) {
            Tree.MemberOrTypeExpression mte = 
                    (Tree.MemberOrTypeExpression) primary;
            if (mte instanceof Tree.BaseTypeExpression || 
                mte instanceof Tree.QualifiedTypeExpression) {
                that.setStaticMethodReference(true);
                mte.setStaticMethodReferencePrimary(true);
                if (that.getDirectlyInvoked()) {
                    mte.setDirectlyInvoked(true);
                }
            }
            if (that.getIndirectlyInvoked()) {
                mte.setIndirectlyInvoked(true);
            }
        }
        if (primary instanceof Tree.Package) {
            Tree.Package pack = (Tree.Package) primary;
            pack.setQualifier(true);
        }
        super.visit(that);
    }

    @Override 
    public void visit(Tree.InvocationExpression that) {
        Tree.Term primary = 
                unwrapExpressionUntilTerm(that.getPrimary());
        if (primary instanceof Tree.MemberOrTypeExpression) {
            Tree.MemberOrTypeExpression mte = 
                    (Tree.MemberOrTypeExpression) primary;
            mte.setDirectlyInvoked(true);
            mte.setIndirectlyInvoked(true);
        }
        super.visit(that);
    }

    private static Tree.SpecifierOrInitializerExpression 
    getSpecifier(Tree.ParameterDeclaration that) {
        Tree.TypedDeclaration dec = 
                that.getTypedDeclaration();
        if (dec instanceof Tree.AttributeDeclaration) {
            Tree.AttributeDeclaration ad = 
                    (Tree.AttributeDeclaration) dec;
            return ad.getSpecifierOrInitializerExpression();
        }
        else if (dec instanceof Tree.MethodDeclaration) {
            Tree.MethodDeclaration md = 
                    (Tree.MethodDeclaration) dec;
            return md.getSpecifierExpression();
        }
        else {
            return null;
        }
    }
    
    private void checkDefaultArg(
            Tree.SpecifierOrInitializerExpression se, 
            Parameter p) {
        if (se!=null) {
            if (se.getScope()
                    .getContainer() 
                        instanceof Specification) {
                se.addError("parameter of specification statement may not define default value");
            }
            else {
                Declaration d = p.getDeclaration();
                if (d.isActual()) {
                    se.addError("parameter of actual declaration may not define default value: parameter '" +
                            p.getName() + "' of '" + 
                            p.getDeclaration().getName() + 
                            "'");
                }
            }
        }
    }
    
    @Override public void visit(Tree.ParameterDeclaration that) {
        super.visit(that);
        Parameter p = that.getParameterModel();
        if (p.isDefaulted()) {
            if (p.getDeclaration().isParameter()) {
                getSpecifier(that)
                    .addError("parameter of callable parameter may not have default argument");
            }
            checkDefaultArg(getSpecifier(that), p);
        }
    }
    
    private Declaration handleNativeHeader(Declaration hdr,
            Node that) {
        if (hdr.isNativeHeader()) {
            Scope scope = that.getScope();
            if (scope == hdr) {
                scope = scope.getScope();
            }
            Backends inBackends = scope.getScopedBackends();
            Backends backends =
                    inBackends.none() ?
                            unit.getSupportedBackends() :
                            inBackends;
            Declaration impl =
                    getNativeDeclaration(hdr, backends);
            return inBackends == null || impl==null ? 
                    hdr : impl;
        }
        return hdr;
    }
    
}
