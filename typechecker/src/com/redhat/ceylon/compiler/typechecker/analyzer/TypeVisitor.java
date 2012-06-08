package com.redhat.ceylon.compiler.typechecker.analyzer;

import static com.redhat.ceylon.compiler.typechecker.analyzer.Util.checkTypeBelongsToContainingScope;
import static com.redhat.ceylon.compiler.typechecker.analyzer.Util.getBaseDeclaration;
import static com.redhat.ceylon.compiler.typechecker.analyzer.Util.getTypeArguments;
import static com.redhat.ceylon.compiler.typechecker.model.Util.addToIntersection;
import static com.redhat.ceylon.compiler.typechecker.model.Util.addToUnion;
import static com.redhat.ceylon.compiler.typechecker.model.Util.getContainingClassOrInterface;
import static com.redhat.ceylon.compiler.typechecker.tree.Util.name;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.redhat.ceylon.compiler.typechecker.model.Class;
import com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Functional;
import com.redhat.ceylon.compiler.typechecker.model.Import;
import com.redhat.ceylon.compiler.typechecker.model.ImportList;
import com.redhat.ceylon.compiler.typechecker.model.Interface;
import com.redhat.ceylon.compiler.typechecker.model.IntersectionType;
import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.Module;
import com.redhat.ceylon.compiler.typechecker.model.ModuleImport;
import com.redhat.ceylon.compiler.typechecker.model.Package;
import com.redhat.ceylon.compiler.typechecker.model.Parameter;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.Scope;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.TypeParameter;
import com.redhat.ceylon.compiler.typechecker.model.TypedDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.UnionType;
import com.redhat.ceylon.compiler.typechecker.model.Unit;
import com.redhat.ceylon.compiler.typechecker.model.UnknownType;
import com.redhat.ceylon.compiler.typechecker.model.Value;
import com.redhat.ceylon.compiler.typechecker.model.ValueParameter;
import com.redhat.ceylon.compiler.typechecker.parser.CeylonLexer;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ImportMemberOrTypeList;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.LocalModifier;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.SpecifierExpression;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;

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
            
    @Override public void visit(Tree.CompilationUnit that) {
        unit = that.getUnit();
        super.visit(that);
    }
        
    @Override
    public void visit(Tree.Import that) {
        Package importedPackage = getPackage(that.getImportPath());
        if (importedPackage!=null) {
            that.getImportPath().setPackageModel(importedPackage);
            Tree.ImportMemberOrTypeList imtl = that.getImportMemberOrTypeList();
            if (imtl!=null) {
                ImportList il = imtl.getImportList();
                il.setImportedScope(importedPackage);
                Set<String> names = new HashSet<String>();
                for (Tree.ImportMemberOrType member: imtl.getImportMemberOrTypes()) {
                    names.add(importMember(member, importedPackage, il));
                }
                if (imtl.getImportWildcard()==null) {
                    if (imtl.getImportMemberOrTypes().isEmpty()) {
                        imtl.addError("empty import list");
                    }
                }
                else {
                    importAllMembers(importedPackage, names, il);
                }
            }
        }
    }

    private void importAllMembers(Package importedPackage, Set<String> ignoredMembers, 
            ImportList il) {
        for (Declaration dec: importedPackage.getMembers()) {
            if (dec.isShared() && !dec.isAnonymous() && 
                    !ignoredMembers.contains(dec.getName())) {
                Import i = new Import();
                i.setAlias(dec.getName());
                i.setDeclaration(dec);
                i.setWildcardImport(true);
                addWildcardImport(il, dec, i);
            }
        }
    }

    private void addWildcardImport(ImportList il, Declaration dec, Import i) {
        Import o = unit.getImport(dec.getName());
        if (o==null) {
            unit.getImports().add(i);
            il.getImports().add(i);
        }
        else if (o.isWildcardImport()) {
            unit.getImports().remove(o);
            il.getImports().remove(o);
            if (o.getDeclaration().equals(dec)) {
                //this case only happens in the IDE,
                //due to reuse of the Unit
                unit.getImports().add(i);
                il.getImports().add(i);
            }
        }
    }

    private Package getPackage(Tree.ImportPath path) {
        if (path!=null && !path.getIdentifiers().isEmpty()) {
            String nameToImport = formatPath(path.getIdentifiers());
            Module module = unit.getPackage().getModule();
            Package pkg = module.getPackage(nameToImport);
            if (pkg != null) {
                if (pkg.getModule().equals(module)) {
                    return pkg;
                }
                if (!pkg.isShared()) {
                    path.addError("imported package is not shared: " + 
                            nameToImport);
                }
                //check that the package really does belong to
                //an imported module, to work around bug where
                //default package things it can see stuff in
                //all modules in the same source dir
                for (ModuleImport mi: module.getImports()) {
                    if (mi.getModule().equals(pkg.getModule())) {
                        return pkg; 
                    }
                }
            }
            path.addError("package not found in dependent modules: " + nameToImport);
        }
        return null;
    }

//    private boolean hasName(List<Tree.Identifier> importPath, Package mp) {
//        if (mp.getName().size()==importPath.size()) {
//            for (int i=0; i<mp.getName().size(); i++) {
//                if (!mp.getName().get(i).equals(name(importPath.get(i)))) {
//                    return false;
//                }
//            }
//            return true;
//        }
//        else {
//            return false;
//        }
//    }
    
    private static String formatPath(List<Tree.Identifier> nodes) {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (Node node: nodes) {
            if (first) {
                first = false;
            }
            else {
                sb.append(".");
            }
            sb.append(node.getText());
        }
        return sb.toString();
    }
    
    private String importMember(Tree.ImportMemberOrType member, Package importedPackage, 
            ImportList il) {
        if (member.getIdentifier()==null) {
            return null;
        }
        Import i = new Import();
        member.setImportModel(i);
        Tree.Alias alias = member.getAlias();
        String name = name(member.getIdentifier());
        if (alias==null) {
            i.setAlias(name);
        }
        else {
            i.setAlias(name(alias.getIdentifier()));
        }
        for (Declaration d: unit.getDeclarations()) {
            String n = d.getName();
            if (d.isToplevel() && n!=null && 
                    i.getAlias().equals(n)) {
                if (alias==null) {
                    member.getIdentifier()
                        .addError("toplevel declaration with this name declared in this unit: " + n);
                }
                else {
                    alias.addError("toplevel declaration with this name declared in this unit: " + n);
                }
            }
        }
        Declaration d = importedPackage.getMember(name, null);
        if (d==null) {
            member.getIdentifier().addError("imported declaration not found: " + 
                    name, 100);
            unit.getUnresolvedReferences().add(member.getIdentifier());
        }
        else if (isNonimportable(d)) {
            member.getIdentifier().addError("root type may not be imported");
        }
        else {
            if (!d.isShared() && !d.getUnit().getPackage().equals(unit.getPackage())) {
                member.getIdentifier().addError("imported declaration is not shared: " +
                        name, 400);
            }
            if (d.isProtectedVisibility()) {
                member.getIdentifier().addError("imported declaration is not visible: " +
                        name);
            }
            i.setDeclaration(d);
            member.setDeclarationModel(d);
            if (il.hasImport(d)) {
                member.getIdentifier().addError("already imported: " +
                        name);
            }
            addImport(member, il, i);
            checkAliasCase(alias, d);
        }
        Tree.ImportMemberOrTypeList imtl = member.getImportMemberOrTypeList();
        if (imtl!=null) {
            if (imtl.getImportMemberOrTypes().isEmpty()) {
                imtl.addError("empty import list");
            }
        	if (d instanceof TypeDeclaration) {
                ImportList til = imtl.getImportList();
                til.setImportedScope((TypeDeclaration) d);
        		for (Tree.ImportMemberOrType submember: imtl.getImportMemberOrTypes()) {
        			importMember(submember, (TypeDeclaration) d, til);
            	}
            }
        	else {
        		imtl.addError("member alias list must follow a type");
        	}
        }
        return name;
    }

    private void checkAliasCase(Tree.Alias alias, Declaration d) {
        if (alias!=null) {
            if (d instanceof TypeDeclaration &&
                    alias.getIdentifier().getToken().getType()!=CeylonLexer.UIDENTIFIER) {
                alias.getIdentifier().addError("imported type should have uppercase alias: " +
                    d.getName());
            }
            if (d instanceof TypedDeclaration &&
                    alias.getIdentifier().getToken().getType()!=CeylonLexer.LIDENTIFIER) {
                alias.getIdentifier().addError("imported member should have lowercase alias: " +
                    d.getName());
            }
        }
    }

    private void importMember(Tree.ImportMemberOrType member, TypeDeclaration d, 
            ImportList il) {
        if (member.getIdentifier()==null) {
            return;
        }
        Import i = new Import();
        member.setImportModel(i);
        Tree.Alias alias = member.getAlias();
        String name = name(member.getIdentifier());
        if (alias==null) {
            i.setAlias(name);
        }
        else {
            i.setAlias(name(alias.getIdentifier()));
        }
        Declaration m = d.getMember(name, null);
        if (m==null) {
            member.getIdentifier().addError("imported declaration not found: " + 
                    name + " of " + d.getName(), 100);
            unit.getUnresolvedReferences().add(member.getIdentifier());
        }
        else {
            if (!m.isShared()) {
                member.getIdentifier().addError("imported declaration is not shared: " +
                        name + " of " + d.getName(), 400);
            }
            if (m.isProtectedVisibility()) {
                member.getIdentifier().addError("imported declaration is not visible: " +
                        name + " of " + d.getName());
            }
            if (!m.isStaticallyImportable()) {
                i.setTypeDeclaration(d);
                if (alias==null) {
                    member.addError("does not specify an alias");
                }
            }
            i.setDeclaration(m);
            member.setDeclarationModel(m);
            if (il.hasImport(m)) {
                member.getIdentifier().addError("already imported: " +
                        name + " of " + d.getName());
            }
            if (m.isStaticallyImportable()) {
                addImport(member, il, i);
            }
            else {
                addMemberImport(member, il, i);
            }
            checkAliasCase(alias, m);
        }
        ImportMemberOrTypeList imtl = member.getImportMemberOrTypeList();
        if (imtl!=null) {
            imtl.addError("member aliases may not have member aliases");
        }
    }

    private void addMemberImport(Tree.ImportMemberOrType member, ImportList il,
            Import i) {
        if (il.getImport(i.getAlias())==null) {
            unit.getImports().add(i);
            il.getImports().add(i);
        }
        else {
            member.addError("duplicate member import alias: " + i.getAlias());
        }
    }
    
    private boolean isNonimportable(Declaration d) {
        String name = d.getQualifiedNameString();
        return "java.lang.Object".equals(name) ||
                "java.lang.Exception".equals(name);
    }

    private void addImport(Tree.ImportMemberOrType member, ImportList il,
            Import i) {
        Import o = unit.getImport(i.getAlias());
        if (o==null) {
            unit.getImports().add(i);
            il.getImports().add(i);
        }
        else if (o.isWildcardImport()) {
            unit.getImports().remove(o);
            il.getImports().remove(o);
            unit.getImports().add(i);
            il.getImports().add(i);
        }
        else {
            member.addError("duplicate import alias: " + i.getAlias());
        }
    }
        
    @Override 
    public void visit(Tree.UnionType that) {
        super.visit(that);
        UnionType ut = new UnionType(unit);
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
    public void visit(Tree.IntersectionType that) {
        super.visit(that);
        IntersectionType it = new IntersectionType(unit);
        List<ProducedType> types = new ArrayList<ProducedType>();
        for (Tree.StaticType st: that.getStaticTypes()) {
            addToIntersection(types, st.getTypeModel(), unit);
        }
        it.setSatisfiedTypes(types);
        that.setTypeModel(it.getType());
        //that.setTarget(pt);
    }

    @Override 
    public void visit(Tree.SequenceType that) {
        super.visit(that);
        ProducedType et = that.getElementType().getTypeModel();
        if (et!=null) {
            that.setTypeModel(unit.getEmptyType(unit.getSequenceType(et)));
        }
    }
    
    @Override
    public void visit(Tree.OptionalType that) {
        super.visit(that);
        ProducedType dt = that.getDefiniteType().getTypeModel();
        if (dt!=null) {
            that.setTypeModel(unit.getOptionalType(dt));
        }
    }
    
    @Override
    public void visit(Tree.EntryType that) {
        super.visit(that);
        ProducedType kt = that.getKeyType().getTypeModel();
        ProducedType vt = that.getValueType()==null ? 
                new UnknownType(unit).getType() : 
                that.getValueType().getTypeModel();
        that.setTypeModel(unit.getEntryType(kt, vt));
    }
    
    @Override
    public void visit(Tree.FunctionType that) {
        super.visit(that);
        List<ProducedType> args = new ArrayList<ProducedType>();
        args.add(that.getReturnType().getTypeModel());
        for (Tree.StaticType st: that.getArgumentTypes()) {
            args.add(st.getTypeModel());
        }
        that.setTypeModel(unit.getCallableDeclaration()
                .getProducedType(null, args));
    }
    
    @Override 
    public void visit(Tree.BaseType that) {
        super.visit(that);
        TypeDeclaration type = getBaseDeclaration(that);
        if (type==null) {
            that.addError("type declaration does not exist or is ambiguous: " + 
                    name(that.getIdentifier()), 102);
            unit.getUnresolvedReferences().add(that.getIdentifier());
        }
        else {
            if (!type.isVisible(that.getScope())) {
                that.addError("type is not visible: " +
                        name(that.getIdentifier()), 400);
            }
            ProducedType outerType = that.getScope().getDeclaringType(type);
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
            TypeDeclaration d = pt.getDeclaration();
			TypeDeclaration type = (TypeDeclaration) d.getMember(name(that.getIdentifier()), unit, null);
            if (type==null) {
                that.addError("member type declaration does not exist or is ambiguous: " + 
                        name(that.getIdentifier()) +
                        " in type " + d.getName(), 100);
                unit.getUnresolvedReferences().add(that.getIdentifier());
            }
            else {
                if (!type.isVisible(that.getScope())) {
                    that.addError("member type is not visible: " +
                            name(that.getIdentifier()) +
                            " of type " + d.getName(), 400);
                }
                visitSimpleType(that, pt, type);
            }
        }
    }

    private void visitSimpleType(Tree.SimpleType that, ProducedType ot, TypeDeclaration dec) {
        Tree.TypeArgumentList tal = that.getTypeArgumentList();
        List<ProducedType> ta = getTypeArguments(tal);
        if (tal!=null) tal.setTypeModels(ta);
        //if (acceptsTypeArguments(dec, ta, tal, that)) {
            ProducedType pt = dec.getProducedType(ot, ta);
            if (!dec.isAlias()) {
            	//TODO: remove this awful hack which means
            	//      we can't define aliases for types
            	//      with sequenced type parameters
            	dec = pt.getDeclaration();
            }
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
        Class vtd = unit.getVoidDeclaration();
        if (vtd!=null) {
		    that.setTypeModel(vtd.getType());
        }
    }

    public void visit(Tree.SequencedType that) {
        super.visit(that);
        ProducedType type = that.getType().getTypeModel();
        if (type!=null) {
            that.setTypeModel(unit.getIterableType(type));
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
        
    /*@Override 
    public void visit(Tree.FunctionArgument that) {
        super.visit(that);
        setType(that, that.getType(), that.getDeclarationModel());
    }*/
        
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
            Class iotd = unit.getIdentifiableObjectDeclaration();
            if (iotd!=null) {
			    c.setExtendedType(iotd.getType());
            }
        }
    }

    @Override 
    public void visit(Tree.ObjectDefinition that) {
        defaultSuperclass(that.getExtendedType(), 
                that.getAnonymousClass());
        super.visit(that);
    }

    @Override 
    public void visit(Tree.ObjectArgument that) {
        defaultSuperclass(that.getExtendedType(), 
                that.getAnonymousClass());
        super.visit(that);
    }

    @Override 
    public void visit(Tree.ClassDefinition that) {
        Class c = that.getDeclarationModel();
        Class vd = unit.getVoidDeclaration();
        if (c!=vd) {
            defaultSuperclass(that.getExtendedType(), c);
        }
        super.visit(that);
    }

    @Override 
    public void visit(Tree.InterfaceDefinition that) {
        Class od = unit.getObjectDeclaration();
        if (od!=null) {
		    that.getDeclarationModel().setExtendedType(od.getType());
        }
        super.visit(that);
    }

    @Override
    public void visit(Tree.TypeParameterDeclaration that) {
        Class vd = unit.getVoidDeclaration();
        if (vd!=null) {
		    that.getDeclarationModel().setExtendedType(vd.getType());
        }
        super.visit(that);
    }
    
    @Override
    public void visit(Tree.SequencedTypeParameterDeclaration that) {
        Class vd = unit.getVoidDeclaration();
        if (vd!=null) {
            that.getDeclarationModel().setExtendedType(vd.getType());
        }
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
        SpecifierExpression sie = that.getSpecifierExpression();
        if (sie==null
                && that.getType() instanceof Tree.FunctionModifier) {
            that.getType().addError("method must specify an explicit return type or definition");
        }
        TypedDeclaration dec = that.getDeclarationModel();
        if (dec!=null) {
            Scope s = dec.getContainer();
            if (s instanceof Functional) {
                Parameter param = ((Functional) s).getParameter( dec.getName() );
                if (param instanceof ValueParameter && 
                        ((ValueParameter) param).isHidden()) {
                    ProducedType ft = dec.getProducedReference(null, 
                            Collections.<ProducedType>emptyList()).getFullType();
                    param.setType(ft);
                    if (sie!=null) {
                        sie.addError("has matching initializer parameter: " + dec.getName());
                    }
                }
            }
        }
    }
    
    @Override
    public void visit(Tree.AttributeDeclaration that) {
        super.visit(that);
        Tree.SpecifierOrInitializerExpression sie = that.getSpecifierOrInitializerExpression();
        TypedDeclaration dec = that.getDeclarationModel();
        if (dec!=null) {
            Scope s = dec.getContainer();
            if (s instanceof Functional) {
                Parameter param = ((Functional) s).getParameter( dec.getName() );
                if (param instanceof ValueParameter && 
                        ((ValueParameter) param).isHidden()) {
                    param.setType(dec.getType());
                    if (sie!=null) {
                        sie.addError("has matching initializer parameter: " + dec.getName());
                    }
                }
            }
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
                if (type.getDeclaration()==td) {
                    //TODO: handle indirect circularities!
                    et.addError("directly extends itself: " + td.getName());
                    return;
                }
                if (!isExtendable(type) && !inLanguageModule(that)) {
                    et.addError("directly extends a special language type: " +
                        type.getProducedTypeName());
                }
                if (et instanceof Tree.QualifiedType) {
                    if ( !(((Tree.QualifiedType) et).getOuterType() instanceof Tree.SuperType) ) {
                        checkTypeBelongsToContainingScope(type, td.getContainer(), et);
                    }
                }
                if (that.getInvocationExpression()!=null) {
                    //TODO: it would probably be better to leave
                    //      all this following  stuff to 
                    //ExpressionVisitor.visit(ExtendedTypeExpression)
                    Tree.Primary pr = that.getInvocationExpression().getPrimary();
                    if (pr instanceof Tree.ExtendedTypeExpression) {
                        pr.setTypeModel(type);
                        Tree.ExtendedTypeExpression ete = (Tree.ExtendedTypeExpression) pr;
                        ete.setDeclaration(type.getDeclaration());
                        ete.setTarget(type);
                    }
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

    private boolean inLanguageModule(Tree.ExtendedType that) {
        return that.getUnit().getPackage().getQualifiedNameString()
                .startsWith("ceylon.language");
    }
    
    private boolean isExtendable(ProducedType type) {
        TypeDeclaration d = type.getDeclaration();
        return !d.equals(unit.getBooleanDeclaration()) &&
                !d.equals(unit.getCharacterDeclaration()) &&
                !d.equals(unit.getIntegerDeclaration()) &&
                !d.equals(unit.getFloatDeclaration()) &&
                !d.equals(unit.getEntryDeclaration()) &&
                !d.equals(unit.getRangeDeclaration()) &&
                !d.equals(unit.getStringDeclaration());
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
                if (type.getDeclaration()==td) {
                    //TODO: handle indirect circularities!
                    t.addError("directly extends itself: " + td.getName());
                    continue;
                }
                if (type.isCallable()) {
                    t.addError("directly satisfies Callable");
                }
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
            TypeDeclaration td = (TypeDeclaration) that.getScope();
            List<ProducedType> list = new ArrayList<ProducedType>();
            for (Tree.BaseMemberExpression bme: that.getBaseMemberExpressions()) {
                //bmes have not yet been resolved
                TypedDeclaration od = getBaseDeclaration(bme, null);
                if (od!=null) {
                    ProducedType type = od.getType();
                    TypeDeclaration dec = type.getDeclaration();
                    if (!dec.isToplevel() && !dec.isAnonymous()) {
                        bme.addError("case must refer to a toplevel object declaration");
                    }
                    else {
                        list.add(type);
                    }
                }
            }
            for (Tree.SimpleType st: that.getTypes()) {
                ProducedType type = st.getTypeModel();
                if (type!=null) {
                    if (type.getDeclaration() instanceof TypeParameter) {
                        if (!(td instanceof TypeParameter)) {
                            TypeParameter tp = (TypeParameter) type.getDeclaration();
                            td.setSelfType(type);
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
                        else {
                            //TODO: error?!
                        }
                    }
                    else {
                        list.add(type);
                    }
                    /*if (type.getDeclaration() instanceof Interface) {
                        st.addWarning("interface cases are not yet supported");
                    }*/
                }
            }
            if (!list.isEmpty()) {
                if (td instanceof ClassOrInterface && !((ClassOrInterface) td).isAbstract()) {
                    that.addError("non-abstract class has enumerated subtypes: " + td.getName(), 900);
                }
                td.setCaseTypes(list);
            }
        }
    }
    
    @Override
    public void visit(Tree.ValueParameterDeclaration that) {
        super.visit(that);
        if (that.getType() instanceof LocalModifier) {
            //i.e. an attribute initializer parameter
            ValueParameter d = that.getDeclarationModel();
            Declaration a = that.getScope().getDirectMember(d.getName(), null);
            if (a==null) {
                that.addError("attribute does not exist: " + d.getName());
            }
            else if (!(a instanceof Value) && !(a instanceof Method)) {
                that.addError("not a simple attribute or method: " + d.getName());
            }
            else if (a.isFormal()) {
                that.addError("initializer parameter refers to a formal attribute: " + 
                        d.getName());
            }
        }
    }
    
}
