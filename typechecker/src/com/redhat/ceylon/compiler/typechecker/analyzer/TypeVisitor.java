package com.redhat.ceylon.compiler.typechecker.analyzer;

import static com.redhat.ceylon.compiler.typechecker.analyzer.Util.getContainingClassOrInterface;
import static com.redhat.ceylon.compiler.typechecker.analyzer.Util.getDeclaration;
import static com.redhat.ceylon.compiler.typechecker.analyzer.Util.getDeclaringType;
import static com.redhat.ceylon.compiler.typechecker.analyzer.Util.getExternalDeclaration;
import static com.redhat.ceylon.compiler.typechecker.analyzer.Util.getLanguageModuleDeclaration;
import static com.redhat.ceylon.compiler.typechecker.analyzer.Util.getMemberDeclaration;
import static com.redhat.ceylon.compiler.typechecker.analyzer.Util.getTypeArguments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.redhat.ceylon.compiler.typechecker.context.Context;
import com.redhat.ceylon.compiler.typechecker.model.Class;
import com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Import;
import com.redhat.ceylon.compiler.typechecker.model.Interface;
import com.redhat.ceylon.compiler.typechecker.model.Module;
import com.redhat.ceylon.compiler.typechecker.model.Package;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.Scope;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.TypeParameter;
import com.redhat.ceylon.compiler.typechecker.model.TypedDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.UnionType;
import com.redhat.ceylon.compiler.typechecker.model.Unit;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;
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
public class TypeVisitor extends Visitor {
    
    private Unit unit;
    private Context context;
    private boolean inExtendsClause = false;

    public TypeVisitor(Unit u, Context context) {
        unit = u;
        this.context = context;
    }
    
    @Override
    public void visit(Tree.Import that) {
        Package importedPackage = getPackage(that.getImportPath());
        if (importedPackage!=null) {
            for (Tree.ImportMemberOrType member: that.getImportMemberOrTypes()) {
                importMember(member, importedPackage);
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
    
    private void importMember(Tree.ImportMemberOrType member, Package importedPackage) {
        Import i = new Import();
        Tree.Alias alias = member.getAlias();
        if (alias==null) {
            i.setAlias(member.getIdentifier().getText());
        }
        else {
            i.setAlias(alias.getIdentifier().getText());
        }
        Declaration d = getExternalDeclaration(importedPackage, member.getIdentifier(), context);
        if (d==null) {
            member.getIdentifier().addError("imported declaration not found: " + 
                    member.getIdentifier().getText());
        }
        else {
            if (!d.isShared()) {
                member.getIdentifier().addError("imported declaration is not shared: " +
                        member.getIdentifier().getText());
            }
            i.setDeclaration(d);
            unit.getImports().add(i);
        }
    }
        
    @Override 
    public void visit(Tree.UnionType that) {
        super.visit(that);
        UnionType ut = new UnionType();
        List<ProducedType> types = new ArrayList<ProducedType>();
        for (Tree.StaticType st: that.getStaticTypes()) {
            types.add( st.getTypeModel() );
        }
        ut.setCaseTypes(types);
        ProducedType pt = ut.getType();
        that.setTypeModel(pt);
        that.setTarget(pt);
    }

    @Override 
    public void visit(Tree.BaseType that) {
        super.visit(that);
        TypeDeclaration d = (TypeDeclaration) getDeclaration(that.getScope(), that.getUnit(), that.getIdentifier(), context);
        if (d==null) {
            that.addError("type declaration not found: " + 
                    that.getIdentifier().getText());
        }
        else {
            ProducedType outerType;
            if (d.isMemberType()) {
                outerType = getDeclaringType(that, d);
            }
            else {
                outerType = null;
            }
            visitType(that, outerType, d);
        }
    }

    public void visit(Tree.QualifiedType that) {
        super.visit(that);
        ProducedType pt = that.getOuterType().getTypeModel();
        if (pt!=null) {
            TypeDeclaration d = (TypeDeclaration) getMemberDeclaration(pt.getDeclaration(), that.getIdentifier(), context);
            if (d==null) {
                that.addError("member type declaration not found: " + 
                        that.getIdentifier().getText());
            }
            else {
                visitType(that, pt, d);
            }
        }
    }

    private void visitType(Tree.SimpleType that, ProducedType ot, TypeDeclaration d) {
        List<ProducedType> typeArguments = getTypeArguments(that.getTypeArgumentList());
        //if (acceptsTypeArguments(d, typeArguments, that.getTypeArgumentList(), that)) {
            ProducedType pt = d.getProducedType(ot, typeArguments);
            that.setTypeModel(pt);
            that.setTarget(pt);
            /*if (typeArguments!=null) {
                typeArguments.add(pt);
            }*/
        //}
    }
    
    private void visitType(Tree.TypeExpression that, ProducedType ot, TypeDeclaration d) {
        List<ProducedType> typeArguments = getTypeArguments(that.getTypeArgumentList());
        //if (acceptsTypeArguments(d, typeArguments, that.getTypeArgumentList(), that)) {
            ProducedType pt = d.getProducedType(ot, typeArguments);
            that.setTypeModel(pt);
            that.setTarget(pt);
            /*if (typeArguments!=null) {
                typeArguments.add(pt);
            }*/
        //}
    }
    
    @Override public void visit(Tree.Super that) {
        if (inExtendsClause) {
            ClassOrInterface ci = getContainingClassOrInterface(that);
            Scope s = ci.getContainer();
            if (s instanceof ClassOrInterface) {
                ProducedType t = ((ClassOrInterface) s).getExtendedType();
                //TODO: type arguments
                that.setTypeModel(t);
            }
            else {
                that.addError("super appears in extends for non-member class");
            }
        }
    }
    
    @Override 
    public void visit(Tree.VoidModifier that) {
        that.setTypeModel(getVoidDeclaration().getType());
    }

    public void visit(Tree.SequencedType that) {
        super.visit(that);
        ProducedType type = that.getType().getTypeModel();
        if (type!=null) {
            that.setTypeModel(getSequenceDeclaration()
                    .getProducedType(null, Collections.singletonList(type)));
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
    
    private void defaultSuperclass(Tree.ExtendedType et, Class c) {
        if (et==null) {
            //TODO: should be BaseObject, according to the spec!
            c.setExtendedType(getIdentifiableObjectDeclaration().getType());
        }
    }

    @Override 
    public void visit(Tree.ObjectDefinition that) {
        Class c = (Class) that.getDeclarationModel().getTypeDeclaration();
        if (c==null) {
            //TODO: this case is temporary until we get aliases
        }
        else {
            defaultSuperclass(that.getExtendedType(), c);
        }
        super.visit(that);
    }

    @Override 
    public void visit(Tree.AnyClass that) {
        Class c = that.getDeclarationModel();
        if (c==null) {
            //TODO: this case is temporary until we get aliases
        }
        else {
            Class vd = getVoidDeclaration();
            if (c!=vd) {
                defaultSuperclass(that.getExtendedType(), c);
            }
        }
        super.visit(that);
    }

    @Override 
    public void visit(Tree.AnyInterface that) {
        Interface i = that.getDeclarationModel();
        if (i==null) {
            //TODO: this case is temporary until we get aliases
        }
        else {
            i.setExtendedType(getObjectDeclaration().getType());
        }
        super.visit(that);
    }

    @Override 
    public void visit(Tree.TypeParameterDeclaration that) {
        that.getDeclarationModel().setExtendedType(getVoidDeclaration().getType());
        super.visit(that);
    }
    
    @Override 
    public void visit(Tree.ExtendedType that) {
        inExtendsClause = true;
        super.visit(that);
        inExtendsClause = false;
        TypeDeclaration td = (TypeDeclaration) that.getScope();
        Tree.Primary et = that.getType();
        if (et==null) {
            that.addError("malformed extended type");
        }
        else {
            if (et instanceof Tree.TypeExpression) {
                Tree.TypeExpression st = (Tree.TypeExpression) et;
                ProducedType pt = st.getPrimary().getTypeModel();
                if (pt!=null) {
                    TypeDeclaration d = (TypeDeclaration) getMemberDeclaration(pt.getDeclaration(), st.getIdentifier(), context);
                    if (d==null) {
                        that.addError("member type declaration not found: " + 
                                st.getIdentifier().getText());
                    }
                    else {
                        visitType(st, pt, d);
                    }
                }
            }
            ProducedType type = et.getTypeModel();
            if (type!=null) {
                if (type.getDeclaration() instanceof TypeParameter) {
                    et.addError("directly extends a type parameter");
                }
                if (type.getDeclaration() instanceof Interface) {
                    et.addError("extends an interface");
                }
                td.setExtendedType(type);
            }
        }
    }
    
    @Override 
    public void visit(Tree.SatisfiedTypes that) {
        super.visit(that);
        TypeDeclaration td = (TypeDeclaration) that.getScope();
        List<ProducedType> list = new ArrayList<ProducedType>();
        if (that!=null) {
            for (Tree.StaticType t: that.getTypes()) {
                ProducedType type = t.getTypeModel();
                if (type!=null) {
                    if (!(td instanceof TypeParameter)) {
                        if (type.getDeclaration() instanceof TypeParameter) {
                            t.addError("directly satisfies type parameter");
                        }
                        if (type.getDeclaration() instanceof Class) {
                            t.addError("satisfies a class");
                        }
                    }
                    list.add(type);
                }
            }
        }
        td.setSatisfiedTypes(list);
    }
    
    private Interface getSequenceDeclaration() {
        return (Interface) getLanguageModuleDeclaration("Sequence", context);
    }

    private Class getObjectDeclaration() {
        return (Class) getLanguageModuleDeclaration("Object", context);
    }

    private Class getVoidDeclaration() {
        return (Class) getLanguageModuleDeclaration("Void", context);
    }
    
    private Class getIdentifiableObjectDeclaration() {
        return (Class) getLanguageModuleDeclaration("IdentifiableObject", context);
    }
    
}
