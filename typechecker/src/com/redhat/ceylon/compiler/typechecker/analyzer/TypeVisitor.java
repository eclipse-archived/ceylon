package com.redhat.ceylon.compiler.typechecker.analyzer;

import static com.redhat.ceylon.compiler.typechecker.model.Util.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.redhat.ceylon.compiler.typechecker.context.Context;
import com.redhat.ceylon.compiler.typechecker.model.Class;
import com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Import;
import com.redhat.ceylon.compiler.typechecker.model.Interface;
import com.redhat.ceylon.compiler.typechecker.model.Module;
import com.redhat.ceylon.compiler.typechecker.model.Package;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.TypeParameter;
import com.redhat.ceylon.compiler.typechecker.model.TypedDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.UnionType;
import com.redhat.ceylon.compiler.typechecker.model.Unit;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.util.PrintUtil;

/**
 * Second phase of type analysis.
 * Scan the compilation unit looking for literal type 
 * declarations and maps them to the associated model 
 * objects. Also builds up a list of imports for the 
 * compilation unit. Finally, assigns types to the 
 * associated model objects of declarations declared 
 * using an explicit type (this must be done in
 * this phase, since shared declarations may be used
 * out of order in expressions).
 * 
 * @author Gavin King
 *
 */
public class TypeVisitor extends AbstractVisitor {
    
    private Unit unit;
    private Context context;
    
    public TypeVisitor(Unit u, Context context) {
        unit = u;
        this.context = context;
    }
    
    @Override
    protected Context getContext() {
        return context;
    }
    
    @Override
    public void visit(Tree.Import that) {
        Package importedPackage = getPackage(that.getImportPath());
        if (importedPackage!=null) {
            Set<String> names = new HashSet<String>();
            for (Tree.ImportMemberOrType member: that.getImportMemberOrTypes()) {
                String name = importMember(member, importedPackage);
                names.add(name);
            }
            if (that.getImportWildcard()!=null) {
                importAllMembers(importedPackage, names);
            }
        }
    }

    private void importAllMembers(Package importedPackage, Set<String> ignoredMembers) {
        for (Declaration dec: importedPackage.getMembers()) {
            if (dec.isShared() && !ignoredMembers.contains(dec.getName())) {
                Import i = new Import();
                i.setAlias(dec.getName());
                i.setDeclaration(dec);
                unit.getImports().add(i);
            }
        }
    }

    private Package getPackage(Tree.ImportPath path) {
        Module module = unit.getPackage().getModule();
        for (Package pkg: module.getAllPackages()) {
            if ( hasName(path.getIdentifiers(), pkg) ) {
                return pkg;
            }
        }
        path.addError("Package not found: " + 
                PrintUtil.importNodeToString(path.getIdentifiers()));
        return null;
    }

    private boolean hasName(List<Tree.Identifier> importPath, Package mp) {
        if (mp.getName().size()==importPath.size()) {
            for (int i=0; i<mp.getName().size(); i++) {
                if (!mp.getName().get(i).equals(importPath.get(i).getText())) {
                    return false;
                }
            }
            return true;
        }
        else {
            return false;
        }
    }
    
    private String importMember(Tree.ImportMemberOrType member, Package importedPackage) {
        Import i = new Import();
        Tree.Alias alias = member.getAlias();
        String name = member.getIdentifier().getText();
        if (alias==null) {
            i.setAlias(name);
        }
        else {
            i.setAlias(alias.getIdentifier().getText());
        }
        Declaration d = importedPackage.getMember(name);
        if (d==null) {
            member.getIdentifier().addError("imported declaration not found: " + 
                    name);
        }
        else {
            if (!d.isShared()) {
                member.getIdentifier().addError("imported declaration is not shared: " +
                        name);
            }
            i.setDeclaration(d);
            unit.getImports().add(i);
        }
        return name;
    }
        
    @Override 
    public void visit(Tree.UnionType that) {
        super.visit(that);
        UnionType ut = new UnionType();
        List<ProducedType> types = new ArrayList<ProducedType>();
        for (Tree.StaticType st: that.getStaticTypes()) {
            addToUnion( types, st.getTypeModel() );
        }
        ut.setCaseTypes(types);
        ProducedType pt = ut.getType();
        that.setTypeModel(pt);
        //that.setTarget(pt);
    }

    @Override 
    public void visit(Tree.BaseType that) {
        super.visit(that);
        TypeDeclaration type = getBaseDeclaration(that);
        if (type==null) {
            that.addError("type declaration not found: " + 
                    that.getIdentifier().getText());
        }
        else {
            ProducedType outerType;
            if (type.isMemberType()) {
                outerType = that.getScope().getDeclaringType(type);
            }
            else {
                outerType = null;
            }
            visitSimpleType(that, outerType, type);
        }
    }

    public void visit(Tree.SuperType that) {
        //if (inExtendsClause) { //can't appear anywhere else in the tree!
            ClassOrInterface ci = getContainingClassOrInterface(that.getScope());
            if (ci!=null) {
                if (ci.isClassOrInterfaceMember()) {
                    ClassOrInterface s = (ClassOrInterface) ci.getContainer();
                    ProducedType t = s.getExtendedType();
                    //TODO: type arguments
                    that.setTypeModel(t);
                }
                else {
                    that.addError("super appears in extends for non-member class");
                }
            }
        //}
    }

    public void visit(Tree.QualifiedType that) {
        super.visit(that);
        ProducedType pt = that.getOuterType().getTypeModel();
        if (pt!=null) {
            TypeDeclaration type = (TypeDeclaration) pt.getDeclaration()
                        .getMember(that.getIdentifier().getText());
            if (type==null) {
                that.addError("member type declaration not found: " + 
                        that.getIdentifier().getText());
            }
            else {
                if (!type.isVisible(that.getScope())) {
                    that.addError("member type is not visible: " +
                            that.getIdentifier().getText());
                }
                visitSimpleType(that, pt, type);
            }
        }
    }

    private void visitSimpleType(Tree.SimpleType that, ProducedType ot, TypeDeclaration dec) {
        Tree.TypeArgumentList tal = that.getTypeArgumentList();
        List<ProducedType> typeArguments = getTypeArguments(tal);
        //if (acceptsTypeArguments(dec, typeArguments, tal, that)) {
            ProducedType pt = dec.getProducedType(ot, typeArguments);
            that.setTypeModel(pt);
            that.setDeclarationModel(dec);
        //}
    }
    
    /*private void visitExtendedType(Tree.QualifiedType that, ProducedType ot, TypeDeclaration dec) {
        Tree.TypeArgumentList tal = that.getTypeArgumentList();
        List<ProducedType> typeArguments = getTypeArguments(tal);
        //if (acceptsTypeArguments(dec, typeArguments, tal, that)) {
            ProducedType pt = dec.getProducedType(ot, typeArguments);
            that.setTypeModel(pt);
        //}
    }*/
        
    @Override 
    public void visit(Tree.VoidModifier that) {
        that.setTypeModel(getVoidDeclaration().getType());
    }

    public void visit(Tree.SequencedType that) {
        super.visit(that);
        ProducedType type = that.getType().getTypeModel();
        if (type!=null) {
            that.setTypeModel(getEmptyType(getSequenceType(type)));
        }
    }

    private ProducedType getSequenceType(ProducedType type) {
        return producedType(getSequenceDeclaration(), type);
    }
    
    private ProducedType getEmptyType(ProducedType pt) {
        if (pt==null) {
            return null;
        }
        /*else if (isEmptyType(pt)) {
            //Nothing|Nothing|T == Nothing|T
            return pt;
        }
        else if (pt.getDeclaration() instanceof BottomType) {
            //Nothing|0 == Nothing
            return getEmptyDeclaration().getType();
        }*/
        else {
            UnionType ut = new UnionType();
            List<ProducedType> types = new ArrayList<ProducedType>();
            addToUnion(types,getEmptyDeclaration().getType());
            addToUnion(types,pt);
            ut.setCaseTypes(types);
            return ut.getType();
        }
    }
    
    @Override 
    public void visit(Tree.TypedDeclaration that) {
        super.visit(that);
        setType(that, that.getType(), that.getDeclarationModel());
    }

    @Override 
    public void visit(Tree.TypedArgument that) {
        super.visit(that);
        setType(that, that.getType(), that.getDeclarationModel());
    }
        
    private void setType(Node that, Tree.Type type, TypedDeclaration td) {
        if (type==null) {
            that.addError("missing type of declaration: " + td.getName());
        }
        else if (!(type instanceof Tree.LocalModifier)) { //if the type declaration is missing, we do type inference later
            ProducedType t = type.getTypeModel();
            if (t==null) {
                //TODO: this case is temporary until we
                //      add support for sequenced parameters
            }
            else {
                td.setType(t);
            }
        }
    }
    
    private void defaultSuperclass(Tree.ExtendedType et, TypeDeclaration c) {
        if (et==null) {
            //TODO: should be BaseObject, according to the spec!
            c.setExtendedType(getIdentifiableObjectDeclaration().getType());
        }
    }

    @Override 
    public void visit(Tree.ObjectDefinition that) {
        defaultSuperclass(that.getExtendedType(), 
                that.getDeclarationModel().getTypeDeclaration());
        super.visit(that);
    }

    @Override 
    public void visit(Tree.ObjectArgument that) {
        defaultSuperclass(that.getExtendedType(), 
                that.getDeclarationModel().getTypeDeclaration());
        super.visit(that);
    }

    @Override 
    public void visit(Tree.ClassDefinition that) {
        Class c = that.getDeclarationModel();
        Class vd = getVoidDeclaration();
        if (c!=vd) {
            defaultSuperclass(that.getExtendedType(), c);
        }
        super.visit(that);
    }

    @Override 
    public void visit(Tree.InterfaceDefinition that) {
        that.getDeclarationModel().setExtendedType(getObjectDeclaration().getType());
        super.visit(that);
    }

    @Override 
    public void visit(Tree.TypeParameterDeclaration that) {
        that.getDeclarationModel().setExtendedType(getVoidDeclaration().getType());
        super.visit(that);
    }
    
    @Override 
    public void visit(Tree.ClassDeclaration that) {
        super.visit(that);
        if (that.getTypeSpecifier()==null) {
            that.addError("missing class body or aliased class reference");
        }
        else {
            Tree.SimpleType et = that.getTypeSpecifier().getType();
            if (et==null) {
                that.addError("malformed aliased class");
            }
            else {
                ProducedType type = et.getTypeModel();
                if (type!=null) {
                    if (!(type.getDeclaration() instanceof Class)) {
                        et.addError("not a class: " + 
                                type.getDeclaration().getName());
                    }
                    that.getDeclarationModel().setExtendedType(type);
                }
            }
        }
    }
    
    @Override 
    public void visit(Tree.InterfaceDeclaration that) {
        super.visit(that);
        if (that.getTypeSpecifier()==null) {
            that.addError("missing interface body or aliased interface reference");
        }
        else {
            Tree.SimpleType et = that.getTypeSpecifier().getType();
            if (et==null) {
                that.addError("malformed aliased interface");
            }
            else {
                ProducedType type = et.getTypeModel();
                if (type!=null) {
                    if (!(type.getDeclaration() instanceof Interface)) {
                        et.addError("not an interface: " + 
                                type.getDeclaration().getName());
                    }
                    that.getDeclarationModel().setExtendedType(type);
                }
            }
        }
    }
    
    @Override
    public void visit(Tree.MethodDeclaration that) {
        super.visit(that);
        if (that.getSpecifierExpression()==null
                && that.getType() instanceof Tree.FunctionModifier) {
            that.getType().addError("method must specify an explicit return type");
        }
    }
    
    @Override
    public void visit(Tree.AttributeDeclaration that) {
        super.visit(that);
        if (that.getSpecifierOrInitializerExpression()==null
                && that.getType() instanceof Tree.ValueModifier) {
            that.getType().addError("attribute must specify an explicit type");
        }
    }
    
    @Override 
    public void visit(Tree.ExtendedType that) {
        super.visit(that);
        TypeDeclaration td = (TypeDeclaration) that.getScope();
        Tree.SimpleType et = that.getType();
        if (et==null) {
            that.addError("malformed extended type");
        }
        else {
            /*if (et instanceof Tree.QualifiedType) {
                Tree.QualifiedType st = (Tree.QualifiedType) et;
                ProducedType pt = st.getOuterType().getTypeModel();
                if (pt!=null) {
                    TypeDeclaration superclass = (TypeDeclaration) getMemberDeclaration(pt.getDeclaration(), st.getIdentifier(), context);
                    if (superclass==null) {
                        that.addError("member type declaration not found: " + 
                                st.getIdentifier().getText());
                    }
                    else {
                        visitExtendedType(st, pt, superclass);
                    }
                }
            }*/
            ProducedType type = et.getTypeModel();
            if (type!=null) {
                if (et instanceof Tree.QualifiedType) {
                    if ( !(((Tree.QualifiedType) et).getOuterType() instanceof Tree.SuperType) ) {
                        checkTypeBelongsToContainingScope(type, td.getContainer(), et);
                    }
                }
                Tree.Primary pr = that.getInvocationExpression().getPrimary();
                if (pr instanceof Tree.ExtendedTypeExpression) {
                    pr.setTypeModel(type);
                    pr.setDeclaration(type.getDeclaration());
                    ( (Tree.ExtendedTypeExpression) pr).setTarget(type);
                }
                if (type.getDeclaration() instanceof TypeParameter) {
                    et.addError("directly extends a type parameter: " + 
                            type.getProducedTypeName());
                }
                else if (type.getDeclaration() instanceof Interface) {
                    et.addError("extends an interface: " + 
                            type.getProducedTypeName());
                }
                else {
                    td.setExtendedType(type);
                }
            }
        }
    }
    
    @Override 
    public void visit(Tree.SatisfiedTypes that) {
        super.visit(that);
        TypeDeclaration td = (TypeDeclaration) that.getScope();
        List<ProducedType> list = new ArrayList<ProducedType>();
        if ( that.getTypes().isEmpty() ) {
            that.addError("missing types in satisfies");
        }
        for (Tree.StaticType t: that.getTypes()) {
            ProducedType type = t.getTypeModel();
            if (type!=null) {
                if (!(td instanceof TypeParameter)) {
                    if (type.getDeclaration() instanceof TypeParameter) {
                        t.addError("directly satisfies type parameter: " + 
                                type.getProducedTypeName());
                        continue;
                    }
                    if (type.getDeclaration() instanceof Class) {
                        t.addError("satisfies a class: " + 
                                type.getProducedTypeName());
                        continue;
                    }
                    if (t instanceof Tree.QualifiedType) {
                        checkTypeBelongsToContainingScope(type, td.getContainer(), t);
                    }
                }
                list.add(type);
            }
        }
        td.setSatisfiedTypes(list);
    }

    
    
    /*@Override 
    public void visit(Tree.TypeConstraint that) {
        super.visit(that);
        if (that.getSelfType()!=null) {
            TypeDeclaration td = (TypeDeclaration) that.getSelfType().getScope();
            TypeParameter tp = that.getDeclarationModel();
            td.setSelfType(tp.getType());
            if (tp.isSelfType()) {
                that.addError("type parameter may not act as self type for two different types");
            }
            else {
                tp.setSelfTypedDeclaration(td);
            }
        }
    }*/

    @Override 
    public void visit(Tree.CaseTypes that) {
        super.visit(that);
        if (that.getTypes()!=null) {
            for (Tree.SimpleType st: that.getTypes()) {
                if (st.getTypeModel().getDeclaration() instanceof TypeParameter) {
                    TypeDeclaration td = (TypeDeclaration) that.getScope();
                    if (!(td instanceof TypeParameter)) {
                        if (st.getTypeModel()!=null) {
                            TypeParameter tp = (TypeParameter) st.getTypeModel().getDeclaration();
                            td.setSelfType(st.getTypeModel());
                            if (tp.isSelfType()) {
                                st.addError("type parameter may not act as self type for two different types");
                            }
                            else {
                                tp.setSelfTypedDeclaration(td);
                            }
                            if (that.getTypes().size()>1) {
                                st.addError("a type may not have more than one self type");
                            }
                        }
                    }
                }
            }
        }
    }
    
}
