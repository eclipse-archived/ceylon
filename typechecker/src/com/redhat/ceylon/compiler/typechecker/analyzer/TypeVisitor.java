package com.redhat.ceylon.compiler.typechecker.analyzer;

import static com.redhat.ceylon.compiler.typechecker.analyzer.Util.declaredInPackage;
import static com.redhat.ceylon.compiler.typechecker.analyzer.Util.getTypeArguments;
import static com.redhat.ceylon.compiler.typechecker.analyzer.Util.getTypeDeclaration;
import static com.redhat.ceylon.compiler.typechecker.analyzer.Util.getTypeMember;
import static com.redhat.ceylon.compiler.typechecker.analyzer.Util.getTypedDeclaration;
import static com.redhat.ceylon.compiler.typechecker.model.Util.getContainingClassOrInterface;
import static com.redhat.ceylon.compiler.typechecker.model.Util.intersectionOfSupertypes;
import static com.redhat.ceylon.compiler.typechecker.model.Util.isTypeUnknown;
import static com.redhat.ceylon.compiler.typechecker.model.Util.producedType;
import static com.redhat.ceylon.compiler.typechecker.tree.Util.formatPath;
import static com.redhat.ceylon.compiler.typechecker.tree.Util.name;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.redhat.ceylon.compiler.typechecker.model.Class;
import com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Generic;
import com.redhat.ceylon.compiler.typechecker.model.Import;
import com.redhat.ceylon.compiler.typechecker.model.ImportList;
import com.redhat.ceylon.compiler.typechecker.model.Interface;
import com.redhat.ceylon.compiler.typechecker.model.IntersectionType;
import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.MethodOrValue;
import com.redhat.ceylon.compiler.typechecker.model.Module;
import com.redhat.ceylon.compiler.typechecker.model.ModuleImport;
import com.redhat.ceylon.compiler.typechecker.model.Package;
import com.redhat.ceylon.compiler.typechecker.model.Parameter;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.Scope;
import com.redhat.ceylon.compiler.typechecker.model.Specification;
import com.redhat.ceylon.compiler.typechecker.model.TypeAlias;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.TypeParameter;
import com.redhat.ceylon.compiler.typechecker.model.TypedDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.UnionType;
import com.redhat.ceylon.compiler.typechecker.model.Unit;
import com.redhat.ceylon.compiler.typechecker.model.UnknownType;
import com.redhat.ceylon.compiler.typechecker.model.Value;
import com.redhat.ceylon.compiler.typechecker.parser.CeylonLexer;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.BaseMemberExpression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.MemberLiteral;
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
            that.getImportPath().setModel(importedPackage);
            Tree.ImportMemberOrTypeList imtl = that.getImportMemberOrTypeList();
            if (imtl!=null) {
                ImportList il = imtl.getImportList();
                il.setImportedScope(importedPackage);
                Set<String> names = new HashSet<String>();
                for (Tree.ImportMemberOrType member: imtl.getImportMemberOrTypes()) {
                    names.add(importMember(member, importedPackage, il));
                }
                if (imtl.getImportWildcard()!=null) {
                    importAllMembers(importedPackage, names, il);
                } 
                else if (imtl.getImportMemberOrTypes().isEmpty()) {
                    imtl.addError("empty import list");
                }
            }
        }
    }

    private void importAllMembers(Package importedPackage, 
            Set<String> ignoredMembers, ImportList il) {
        for (Declaration dec: importedPackage.getMembers()) {
            if (dec.isShared() && !dec.isAnonymous() && 
                    !ignoredMembers.contains(dec.getName()) &&
                    !isNonimportable(importedPackage, dec.getName())) {
                addWildcardImport(il, dec);
            }
        }
    }

    private void importAllMembers(TypeDeclaration importedType, 
            Set<String> ignoredMembers, ImportList til) {
        for (Declaration dec: importedType.getMembers()) {
            if (dec.isShared() && dec.isStaticallyImportable() && 
                    !dec.isAnonymous() && 
                    !ignoredMembers.contains(dec.getName())) {
                addWildcardImport(til, dec);
            }
        }
    }

    private void addWildcardImport(ImportList il, Declaration dec) {
        if (!hidesToplevel(dec)) {
            Import i = new Import();
            i.setAlias(dec.getName());
            i.setDeclaration(dec);
            i.setWildcardImport(true);
            addWildcardImport(il, dec, i);
        }
    }

    private void addWildcardImport(ImportList il, Declaration dec, Import i) {
    	String alias = i.getAlias();
		if (alias!=null) {
    		Import o = unit.getImport(dec.getName());
    		if (o!=null && o.isWildcardImport()) {
    			i.setAmbiguous(true);
    			o.setAmbiguous(true);
    			if (o.getDeclaration().equals(dec)) {
    				//this case only happens in the IDE,
    				//due to reuse of the Unit
    				unit.getImports().remove(o);
    				il.getImports().remove(o);
    			}
    		}
    		unit.getImports().add(i);
    		il.getImports().add(i);
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
                //default package thinks it can see stuff in
                //all modules in the same source dir
                Set<Module> visited = new HashSet<Module>();
                for (ModuleImport mi: module.getImports()) {
                    if (findModuleInTransitiveImports(mi.getModule(), 
                    		pkg.getModule(), visited)) {
                        return pkg; 
                    }
                }
            }
            path.addError("package not found in dependent modules: " + 
            		nameToImport);
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
    
    private boolean findModuleInTransitiveImports(Module moduleToVisit, 
            Module moduleToFind, Set<Module> visited) {
        if (!visited.add(moduleToVisit))
            return false;
        if (moduleToVisit.equals(moduleToFind))
            return true;
        for (ModuleImport imp : moduleToVisit.getImports()) {
            // skip non-exported modules
            if (!imp.isExport())
                return false;
            if (findModuleInTransitiveImports(imp.getModule(), moduleToFind, visited))
                return true;
        }
        return false;
    }

    private boolean hidesToplevel(Declaration dec) {
        for (Declaration d: unit.getDeclarations()) {
            String n = d.getName();
            if (d.isToplevel() && n!=null && 
                    dec.getName().equals(n)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkForHiddenToplevel(Tree.Identifier id, Import i, Tree.Alias alias) {
        for (Declaration d: unit.getDeclarations()) {
            String n = d.getName();
            if (d.isToplevel() && n!=null && 
                    i.getAlias().equals(n)) {
                if (alias==null) {
                    id.addError("toplevel declaration with this name declared in this unit: " + n);
                }
                else {
                    alias.addError("toplevel declaration with this name declared in this unit: " + n);
                }
                return true;
            }
        }
        return false;
    }

    private void importMembers(Tree.ImportMemberOrType member, Declaration d) {
        Tree.ImportMemberOrTypeList imtl = member.getImportMemberOrTypeList();
        if (imtl!=null) {
        	if (d instanceof TypeDeclaration) {
                Set<String> names = new HashSet<String>();
                ImportList til = imtl.getImportList();
                TypeDeclaration td = (TypeDeclaration) d;
                til.setImportedScope(td);
        		for (Tree.ImportMemberOrType submember: imtl.getImportMemberOrTypes()) {
        			names.add(importMember(submember, td, til));
            	}
                if (imtl.getImportWildcard()!=null) {
                    importAllMembers(td, names, til);
                }
                else if (imtl.getImportMemberOrTypes().isEmpty()) {
                    imtl.addError("empty import list");
                }
            }
        	else {
        		imtl.addError("member alias list must follow a type");
        	}
        }
    }

    private void checkAliasCase(Tree.Alias alias, Declaration d) {
        if (alias!=null) {
            Tree.Identifier id = alias.getIdentifier();
			if (d instanceof TypeDeclaration &&
                    id.getToken().getType()!=CeylonLexer.UIDENTIFIER) {
                id.addError("imported type should have uppercase alias: " +
                    d.getName());
            }
            if (d instanceof TypedDeclaration &&
                    id.getToken().getType()!=CeylonLexer.LIDENTIFIER) {
                id.addError("imported member should have lowercase alias: " +
                    d.getName());
            }
        }
    }

    private String importMember(Tree.ImportMemberOrType member,
            Package importedPackage, ImportList il) {
        Tree.Identifier id = member.getIdentifier();
        if (id==null) {
            return null;
        }
        Import i = new Import();
        member.setImportModel(i);
        Tree.Alias alias = member.getAlias();
        String name = name(id);
        if (alias==null) {
            i.setAlias(name);
        }
        else {
            i.setAlias(name(alias.getIdentifier()));
        }
        if (isNonimportable(importedPackage, name)) {
            id.addError("root type may not be imported");
            return name;
        }        
        Declaration d = importedPackage.getMember(name, null, false);
        if (d==null) {
            id.addError("imported declaration not found: " + 
                    name, 100);
            unit.getUnresolvedReferences().add(id);
        }
        else {
            if (!declaredInPackage(d, unit)) {
                if (!d.isShared()) {
                    id.addError("imported declaration is not shared: " +
                            name, 400);
                }
                if (d.isPackageVisibility()) {
                    id.addError("imported package private declaration is not visible: " +
                            name);
                }
                if (d.isProtectedVisibility()) {
                    id.addError("imported protected declaration is not visible: " +
                            name);
                }
            }
            i.setDeclaration(d);
            member.setDeclarationModel(d);
            if (il.hasImport(d)) {
                id.addError("already imported: " +
                        name);
            }
            if (!checkForHiddenToplevel(id, i, alias)) {
                addImport(member, il, i);
            }
            checkAliasCase(alias, d);
        }
        importMembers(member, d);
        return name;
    }

    private String importMember(Tree.ImportMemberOrType member, 
            TypeDeclaration td, ImportList il) {
    	Tree.Identifier id = member.getIdentifier();
		if (id==null) {
            return null;
        }
        Import i = new Import();
        member.setImportModel(i);
        Tree.Alias alias = member.getAlias();
        String name = name(id);
        if (alias==null) {
            i.setAlias(name);
        }
        else {
            i.setAlias(name(alias.getIdentifier()));
        }
        Declaration m = td.getMember(name, null, false);
        if (m==null) {
            id.addError("imported declaration not found: " + 
                    name + " of " + td.getName(), 100);
            unit.getUnresolvedReferences().add(id);
        }
        else {
            if (!m.isShared()) {
                id.addError("imported declaration is not shared: " +
                        name + " of " + td.getName(), 400);
            }
            if (!declaredInPackage(m, unit)) {
                if (m.isPackageVisibility()) {
                    id.addError("imported package private declaration is not visible: " +
                            name + " of " + td.getName());
                }
                if (m.isProtectedVisibility()) {
                    id.addError("imported protected declaration is not visible: " +
                            name + " of " + td.getName());
                }
            }
            if (!m.isStaticallyImportable()) {
                i.setTypeDeclaration(td);
                if (alias==null) {
                    member.addError("does not specify an alias");
                }
            }
            i.setDeclaration(m);
            member.setDeclarationModel(m);
            if (il.hasImport(m)) {
                id.addError("already imported: " +
                        name + " of " + td.getName());
            }
            if (m.isStaticallyImportable()) {
                if (!checkForHiddenToplevel(id, i, alias)) {
                    addImport(member, il, i);
                }
            }
            else {
                addMemberImport(member, il, i);
            }
            checkAliasCase(alias, m);
        }
        importMembers(member, m);
        //imtl.addError("member aliases may not have member aliases");
        return name;
    }

    private void addImport(Tree.ImportMemberOrType member, ImportList il,
            Import i) {
        String alias = i.getAlias();
        if (alias!=null) {
            Import o = unit.getImport(alias);
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
                member.addError("duplicate import alias: " + alias);
            }
        }
    }
        
    private void addMemberImport(Tree.ImportMemberOrType member, ImportList il,
            Import i) {
    	String alias = i.getAlias();
		if (alias!=null) {
    		if (il.getImport(alias)==null) {
    			unit.getImports().add(i);
    			il.getImports().add(i);
    		}
    		else {
    			member.addError("duplicate member import alias: " + alias);
    		}
    	}
    }
    
    private boolean isNonimportable(Package pkg, String name) {
        return pkg.getQualifiedNameString().equals("java.lang") &&
        		("Object".equals(name) ||
                 "Throwable".equals(name) /*||
                 "Exception".equals(name)*/);
    }

    @Override 
    public void visit(Tree.UnionType that) {
        super.visit(that);
        UnionType ut = new UnionType(unit);
        List<ProducedType> types = new ArrayList<ProducedType>();
        for (Tree.StaticType st: that.getStaticTypes()) {
            //addToUnion( types, st.getTypeModel() );
        	ProducedType t = st.getTypeModel();
			if (t!=null) types.add(t);
        }
        ut.setCaseTypes(types);
        that.setTypeModel(ut.getType());
        //that.setTarget(pt);
    }

    @Override 
    public void visit(Tree.IntersectionType that) {
        super.visit(that);
        IntersectionType it = new IntersectionType(unit);
        List<ProducedType> types = new ArrayList<ProducedType>();
        for (Tree.StaticType st: that.getStaticTypes()) {
            //addToIntersection(types, st.getTypeModel(), unit);
        	ProducedType t = st.getTypeModel();
			if (t!=null) types.add(t);
        }
        it.setSatisfiedTypes(types);
        that.setTypeModel(it.getType());
        //that.setTarget(pt);
    }

    @Override 
    public void visit(Tree.SequenceType that) {
        super.visit(that);
        if (that.getElementType()!=null) {
        	ProducedType et = that.getElementType().getTypeModel();
        	if (et!=null) {
        		that.setTypeModel(unit.getSequentialType(et));
        	}
        }
    }
    
    @Override 
    public void visit(Tree.IterableType that) {
        super.visit(that);
        Tree.Type elem = that.getElementType();
		if (elem!=null) {
        	if (elem instanceof Tree.SequencedType) {
        		ProducedType et = ((Tree.SequencedType) elem).getType().getTypeModel();
        		if (et!=null) {
        			if (((Tree.SequencedType) elem).getAtLeastOne()) {
        				that.setTypeModel(unit.getNonemptyIterableType(et));
        			}
        			else {
        				that.setTypeModel(unit.getIterableType(et));
        			}
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
		that.setTypeModel(producedType(unit.getCallableDeclaration(),
        		that.getReturnType().getTypeModel(),
        		getTupleType(that.getArgumentTypes())));
    }
    
    @Override
    public void visit(Tree.TupleType that) {
        super.visit(that);
		that.setTypeModel(getTupleType(that.getElementTypes()));
    }

	private ProducedType getTupleType(List<Tree.Type> ets) {
		List<ProducedType> args = new ArrayList<ProducedType>();
        boolean sequenced = false;
        boolean atleastone = false;
		int firstDefaulted = -1;
		for (int i=0; i<ets.size(); i++) {
			Tree.Type st = ets.get(i);
			ProducedType arg = st==null ? null : st.getTypeModel();
			if (arg==null) {
				arg = new UnknownType(unit).getType();
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
					atleastone = ((Tree.SequencedType) st).getAtLeastOne();
					arg = ((Tree.SequencedType) st).getType().getTypeModel();
				}
			}
			args.add(arg);
        }
        return unit.getTupleType(args, sequenced, atleastone, firstDefaulted);
	}

    @Override 
    public void visit(Tree.BaseType that) {
        super.visit(that);
        TypeDeclaration type = getTypeDeclaration(that.getScope(), 
                name(that.getIdentifier()), null, false, that.getUnit());
        String name = name(that.getIdentifier());
        if (type==null) {
            that.addError("type declaration does not exist: " + name, 102);
            unit.getUnresolvedReferences().add(that.getIdentifier());
        }
        else {
            if (!type.isVisible(that.getScope())) {
                that.addError("type is not visible: " + name, 400);
            }
            else {
                if (type.isPackageVisibility() && 
                        !declaredInPackage(type, unit)) {
                    that.addError("package private type is not visible: " + name);
                }
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
                    ClassOrInterface oci = (ClassOrInterface) ci.getContainer();
                    that.setTypeModel(intersectionOfSupertypes(oci));
                }
                else {
                    that.addError("super appears in extends for non-member class");
                }
            }
        //}
    }
    
    @Override
    public void visit(MemberLiteral that) {
        super.visit(that);
        if (that.getType()!=null) {
            ProducedType pt = that.getType().getTypeModel();
            if (pt!=null) {
                if (that.getTypeArgumentList()!=null &&
                        isTypeUnknown(pt) && !pt.isUnknown()) {
                    that.getTypeArgumentList()
                            .addError("qualifying type does not fully-specify type arguments");
                }
            }
        }
    }
    
    @Override
    public void visit(Tree.QualifiedType that) {
        if (that.getMetamodel()) {
            that.getOuterType().setMetamodel(true);
        }
        super.visit(that);
        ProducedType pt = that.getOuterType().getTypeModel();
        if (pt!=null) {
            if (that.getMetamodel() && 
                    that.getTypeArgumentList()!=null &&
                    isTypeUnknown(pt) && !pt.isUnknown()) {
                that.getTypeArgumentList()
                        .addError("qualifying type does not fully-specify type arguments");
            }
            TypeDeclaration d = pt.getDeclaration();
			String name = name(that.getIdentifier());
            TypeDeclaration type = getTypeMember(d, name, null, false, unit);
            if (type==null) {
                if (d.isMemberAmbiguous(name, unit, null, false)) {
                    that.addError("member type declaration is ambiguous: " + 
                            name + " for type " + d.getName());
                }
                else {
                    that.addError("member type declaration does not exist: " + 
                            name + " in type " + d.getName(), 100);
                    unit.getUnresolvedReferences().add(that.getIdentifier());
                }
            }
            else {
                if (!type.isVisible(that.getScope())) {
                    that.addError("member type is not visible: " +
                            name + " of type " + d.getName(), 400);
                }
                else {
                    if (type.isProtectedVisibility() && 
                            !declaredInPackage(type, unit)) {
                        that.addError("protected member type is not visible: " +
                                name + " of type " + d.getName());
                    }
                    else if (type.isPackageVisibility() && 
                            !declaredInPackage(type, unit)) {
                        that.addError("package private member type is not visible: " +
                                name + " of type " + d.getName());
                    }
                }
                visitSimpleType(that, pt, type);
            }
        }
    }

    private void visitSimpleType(Tree.SimpleType that, ProducedType ot, TypeDeclaration dec) {
        Tree.TypeArgumentList tal = that.getTypeArgumentList();
        List<ProducedType> ta = getTypeArguments(tal, 
        		dec.getTypeParameters(), ot);
        if (tal!=null) tal.setTypeModels(ta);
        //if (acceptsTypeArguments(dec, ta, tal, that)) {
            ProducedType pt = dec.getProducedType(ot, ta);
//            if (!dec.isAlias()) {
            	//TODO: remove this awful hack which means
            	//      we can't define aliases for types
            	//      with sequenced type parameters
            	dec = pt.getDeclaration();
//            }
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
        Class vtd = unit.getAnythingDeclaration();
        if (vtd!=null) {
		    that.setTypeModel(vtd.getType());
        }
    }

    public void visit(Tree.SequencedType that) {
        super.visit(that);
        ProducedType type = that.getType().getTypeModel();
        if (type!=null) {
        	ProducedType et = that.getAtLeastOne() ? 
        			unit.getSequenceType(type) : 
        			unit.getSequentialType(type);
            that.setTypeModel(et);
        }
    }

    public void visit(Tree.DefaultedType that) {
        super.visit(that);
        ProducedType type = that.getType().getTypeModel();
        if (type!=null) {
            that.setTypeModel(type);
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
            if (t!=null) {
                td.setType(t);
            }
        }
    }
    
    private void defaultSuperclass(Tree.ExtendedType et, TypeDeclaration c) {
        if (et==null) {
            Class iotd = unit.getBasicDeclaration();
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
        Class vd = unit.getAnythingDeclaration();
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
        Class vd = unit.getAnythingDeclaration();
        if (vd!=null) {
		    that.getDeclarationModel().setExtendedType(vd.getType());
        }
        super.visit(that);
        if (that.getTypeSpecifier()!=null) {
        	Tree.StaticType type = that.getTypeSpecifier().getType();
        	if (type!=null) {
				ProducedType t = type.getTypeModel();
        		/*if (t.containsTypeParameters()) {
        			type.addError("default type argument involves type parameters: " + 
        					t.getProducedTypeName());
        		}
        		else {*/
					that.getDeclarationModel().setDefaultTypeArgument(t);
				//}
        	}
        }
    }
    
    @Override
    public void visit(Tree.TypeParameterList that) {
        super.visit(that);
        List<Tree.TypeParameterDeclaration> tpds = that.getTypeParameterDeclarations();
        List<TypeParameter> params = new ArrayList<TypeParameter>();
        for (int i=tpds.size()-1; i>=0; i--) {
            Tree.TypeParameterDeclaration tpd = tpds.get(i);
            if (tpd!=null) {
                TypeParameter tp = tpd.getDeclarationModel();
                if (tp.getDefaultTypeArgument()!=null) {
                    params.add(tp);
                    if (tp.getDefaultTypeArgument().containsTypeParameters(params)) {
                        tpd.getTypeSpecifier().addError("default type argument involves a type parameter not yet declared");
                    }
                }
            }
        }
    }
    
    @Override 
    public void visit(Tree.ClassDeclaration that) {
        super.visit(that);
        Class td = that.getDeclarationModel();
        Tree.ClassSpecifier cs = that.getClassSpecifier();
        if (cs==null) {
            that.addError("missing class body or aliased class reference");
        }
        else {
            Tree.SimpleType ct = cs.getType();
            if (ct==null) {
                that.addError("malformed aliased class");
            }
            else if (!(ct instanceof Tree.StaticType)) {
            	cs.addError("aliased type must be a class");
            }
            /*else if (ct instanceof Tree.QualifiedType) {
            	cs.addError("aliased class may not be a qualified type");
        	}*/
            else {
                ProducedType type = ct.getTypeModel();
                if (type!=null) {
                	/*if (type.containsTypeAliases()) {
                		et.addError("aliased type involves type aliases: " +
                				type.getProducedTypeName());
                	}
                	else*/ if (type.getDeclaration() instanceof Class) {
                    	that.getDeclarationModel().setExtendedType(type);
                    } 
                    else {
                        ct.addError("not a class: " + 
                                type.getDeclaration().getName(unit));
                    }
                    TypeDeclaration etd = ct.getDeclarationModel();
                    if (etd==td) {
                        //TODO: handle indirect circularities!
                        ct.addError("directly aliases itself: " + td.getName());
                        return;
                    }
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
            Tree.StaticType et = that.getTypeSpecifier().getType();
            if (et==null) {
                that.addError("malformed aliased interface");
            }
            else if (!(et instanceof Tree.StaticType)) {
            	that.getTypeSpecifier()
            			.addError("aliased type must be an interface");
            }
            else {
                ProducedType type = et.getTypeModel();
                if (type!=null) {
                	/*if (type.containsTypeAliases()) {
                		et.addError("aliased type involves type aliases: " +
                				type.getProducedTypeName());
                	}
                	else*/ if (type.getDeclaration() instanceof Interface) {
                    	that.getDeclarationModel().setExtendedType(type);
                    } 
                    else {
                        et.addError("not an interface: " + 
                                type.getDeclaration().getName(unit));
                    }
                }
            }
        }
    }
    
    @Override 
    public void visit(Tree.TypeAliasDeclaration that) {
        super.visit(that);
        if (that.getTypeSpecifier()==null) {
            that.addError("missing aliased type");
        }
        else {
            Tree.StaticType et = that.getTypeSpecifier().getType();
            if (et==null) {
                that.addError("malformed aliased type");
            }
            else {
                ProducedType type = et.getTypeModel();
                if (type!=null) {
                	/*if (type.containsTypeAliases()) {
                		et.addError("aliased type involves type aliases: " +
                				type.getProducedTypeName());
                	}
                	else {*/
                		that.getDeclarationModel().setExtendedType(type);
                	//}
                }
            }
        }
    }
    
    @Override
    public void visit(Tree.MethodDeclaration that) {
        super.visit(that);
        Tree.SpecifierExpression sie = that.getSpecifierExpression();
        if (sie==null && that.getType() instanceof Tree.FunctionModifier) {
            that.getType().addError("function must specify an explicit return type or definition");
        }
        Method dec = that.getDeclarationModel();
        if (dec!=null && dec.isParameter() && 
                dec.getInitializerParameter().isHidden()) {
            if (sie!=null) {
                sie.addError("value is an initializer parameter and may not have an initial value: " + 
                        dec.getName());
            }
        }
    }
    
    @Override
    public void visit(Tree.AttributeDeclaration that) {
        super.visit(that);
        Tree.SpecifierOrInitializerExpression sie = that.getSpecifierOrInitializerExpression();
        Value dec = that.getDeclarationModel();
        if (dec!=null && dec.isParameter() && 
                dec.getInitializerParameter().isHidden()) {
            Parameter param = dec.getInitializerParameter();
            param.setSequenced(that.getType() instanceof Tree.SequencedType);
            if (sie!=null) {
                sie.addError("value is an initializer parameter and may not have an initial value: " + 
                        dec.getName());
            }
        }
    }
    
    @Override 
    public void visit(Tree.ExtendedType that) {
        super.visit(that);
        TypeDeclaration td = (TypeDeclaration) that.getScope();
        if (td.isAlias()) {
            that.addError("alias may not extend a type");
            return;
        }
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
                TypeDeclaration etd = et.getDeclarationModel();
                if (etd==td) {
                    //TODO: handle indirect circularities!
                    et.addError("directly extends itself: " + td.getName());
                }
                else if (etd instanceof TypeParameter) {
                    et.addError("directly extends a type parameter: " + 
                            type.getDeclaration().getName(unit));
                }
                else if (etd instanceof Interface) {
                    et.addError("extends an interface: " + 
                            type.getDeclaration().getName(unit));
                }
                else if (etd instanceof TypeAlias) {
                    et.addError("extends a type alias: " + 
                            type.getDeclaration().getName(unit));
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
        if (td.isAlias()) {
            that.addError("alias may not satisfy a type");
            return;
        }
        List<ProducedType> list = new ArrayList<ProducedType>();
        if ( that.getTypes().isEmpty() ) {
            that.addError("missing types in satisfies");
        }
        boolean foundTypeParam = false;
        boolean foundClass = false;
        boolean foundInterface = false;
        for (Tree.StaticType st: that.getTypes()) {
            ProducedType type = st.getTypeModel();
            if (type!=null) {
                TypeDeclaration std = type.getDeclaration();
				if (std==td) {
                    //TODO: handle indirect circularities!
                    st.addError("directly extends itself: " + td.getName());
                    continue;
                }
                if (std instanceof TypeAlias) {
                    st.addError("satisfies a type alias: " + 
                    		type.getDeclaration().getName(unit));
                    continue;
                }
                if (td instanceof TypeParameter) {
            		if (foundTypeParam) {
            			st.addWarning("type parameter upper bounds are not yet supported in combination with other bounds");
            		}
            		else if (std instanceof TypeParameter) {
                		if (foundClass||foundInterface) {
                			st.addWarning("type parameter upper bounds are not yet supported in combination with other bounds");
                		}
                		foundTypeParam = true;
                	}
            		else if (std instanceof Class) {
                		if (foundClass) {
                            st.addWarning("multiple class upper bounds are not yet supported");
                		}
                		foundClass = true;
                	}
            		else if (std instanceof Interface) {
                		foundInterface = true;
                	}
            		else {
            			st.addError("upper bound must be a class, interface, or type parameter");
            			continue;
            		}
                } 
                else {
                    if (std instanceof TypeParameter) {
                        st.addError("directly satisfies type parameter: " + 
                        		type.getDeclaration().getName(unit));
                        continue;
                    }
                    else if (std instanceof Class) {
                        st.addError("satisfies a class: " + 
                        		type.getDeclaration().getName(unit));
                        continue;
                    }
            		else if (!(std instanceof Interface)) {
            			st.addError("satisfied type must be an interface");
            			continue;
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
        TypeDeclaration td = (TypeDeclaration) that.getScope();
        if (td.isAlias()) {
            that.addError("alias may not have cases or a self type");
            return;
        }
    	List<ProducedType> list = new ArrayList<ProducedType>();
        List<BaseMemberExpression> bmes = that.getBaseMemberExpressions();
        if (td instanceof TypeParameter) {
        	if (!bmes.isEmpty()) {
        		that.addError("cases of type parameter must be a types");
        	}
        }
        else {
        	for (Tree.BaseMemberExpression bme: bmes) {
        		//bmes have not yet been resolved
        		TypedDeclaration od = getTypedDeclaration(bme.getScope(), 
        		        name(bme.getIdentifier()), null, false, bme.getUnit());
        		if (od!=null) {
        			ProducedType type = od.getType();
        			if (type!=null) {
        				TypeDeclaration dec = type.getDeclaration();
        				if (dec!=null && !dec.isToplevel() && !dec.isAnonymous()) {
        					bme.addError("case must refer to a toplevel object declaration");
        				}
        				else {
        					list.add(type);
        				}
        			}
        		}
        	}
        }
        List<Tree.StaticType> cts = that.getTypes();
        for (Tree.StaticType st: cts) {
        	ProducedType type = st.getTypeModel();
        	if (type!=null) {
        		TypeDeclaration ctd = type.getDeclaration();
        		if (ctd!=null) {
        			if (ctd instanceof UnionType || ctd instanceof IntersectionType) {
        				st.addError("case type must be a class, interface, or self type");
        			}
        			else if (ctd.equals(td)) {
        				st.addError("directly enumerates itself: " + td.getName());
        				continue;
        			}
        			else if (ctd instanceof TypeParameter) {
        				if (!(td instanceof TypeParameter)) {
        					TypeParameter tp = (TypeParameter) ctd;
        					td.setSelfType(type);
        					if (tp.isSelfType()) {
        						st.addError("type parameter may not act as self type for two different types");
        					}
        					else {
        						tp.setSelfTypedDeclaration(td);
        					}
        					if (cts.size()>1) {
        						st.addError("a type may not have more than one self type");
        					}
        				}
        				else {
        					//TODO: error?!
        				}
        			}
        			else if (!(ctd instanceof ClassOrInterface)) {
        				st.addError("case type must be a class, interface, or self type");
        				continue;
        			}
        			list.add(type);
        		}
        	}
        }
        if (!list.isEmpty()) {
        	if (list.size() == 1 && list.get(0).getDeclaration().isSelfType()) {
        		Scope s = list.get(0).getDeclaration().getContainer();
        		if (s instanceof ClassOrInterface && !((ClassOrInterface) s).isAbstract()) {
        			that.addError("non-abstract class parameterized by self type: " + td.getName(), 905);
        		}
        	}
        	else {
        		if (td instanceof ClassOrInterface && !((ClassOrInterface) td).isAbstract()) {
        			that.addError("non-abstract class has enumerated subtypes: " + td.getName(), 905);
        		}
        	}
        	td.setCaseTypes(list);
        }
    }

    @Override
    public void visit(Tree.InitializerParameter that) {
        super.visit(that);
        //i.e. an attribute initializer parameter
        Parameter p = that.getParameterModel();
        Declaration a = that.getScope().getDirectMember(p.getName(), null, false);
        if (a==null) {
            that.addError("parameter declaration does not exist: " + p.getName());
        }
        else if (!(a instanceof Value && !((Value)a).isTransient()) && 
                !(a instanceof Method)) {
            that.addError("parameter is not a reference value or function: " + p.getName());
        }
        else if (a.isFormal()) {
            that.addError("parameter is a formal attribute: " + p.getName());
        }
        /*else if (a.isDefault()) {
            that.addError("initializer parameter refers to a default attribute: " + 
                    d.getName());
        }*/
        else {
            MethodOrValue mov = (MethodOrValue) a;
            mov.setInitializerParameter(p);
            p.setModel(mov);
        }
        /*if (d.isHidden() && d.getDeclaration() instanceof Method) {
            if (a instanceof Method) {
                that.addWarning("initializer parameters for inner methods of methods not yet supported");
            }
            if (a instanceof Value && ((Value) a).isVariable()) {
                that.addWarning("initializer parameters for variables of methods not yet supported");
            }
        }*/
        if (a instanceof Generic && !((Generic)a).getTypeParameters().isEmpty()) {
            that.addError("parameter declaration has type parameters: " + 
                    p.getName());
        }
        if (p.isDefaulted()) {
            checkDefaultArg(that.getSpecifierExpression(), p);
        }
    }
    
    @Override
    public void visit(Tree.AnyAttribute that) {
    	super.visit(that);
    	if (that.getType() instanceof Tree.SequencedType) {
    		Value v = (Value) that.getDeclarationModel();
			Parameter p = v.getInitializerParameter();
			if (p==null) {
				that.getType().addError("value is not a parameter, so may not be variadic: " +
						v.getName());
			}
			else {
				p.setSequenced(true);
			}
    	}
    }

    @Override
    public void visit(Tree.AnyMethod that) {
    	super.visit(that);
    	if (that.getType() instanceof Tree.SequencedType) {
    		that.getType().addError("function type may not be variadic");
    	}
    }
    
    @Override public void visit(Tree.QualifiedMemberOrTypeExpression that) {
        Tree.Primary p = that.getPrimary();
        if (p instanceof Tree.MemberOrTypeExpression) {
            if (p instanceof Tree.BaseTypeExpression || 
                p instanceof Tree.QualifiedTypeExpression) {
                that.setStaticMethodReference(true);
                ((Tree.MemberOrTypeExpression) p).setStaticMethodReferencePrimary(true);
                if (p instanceof Tree.QualifiedTypeExpression) {
                    Tree.Primary pp = ((Tree.QualifiedTypeExpression) p).getPrimary();
                    if (!(pp instanceof Tree.BaseTypeExpression)
                            && !(pp instanceof Tree.QualifiedTypeExpression)) {
                        that.getPrimary().addError("non-static type expression in static member reference");
                    }
                }
            }
        }
        super.visit(that);
    }

    @Override public void visit(Tree.InvocationExpression that) {
        super.visit(that);
        Tree.Term p = Util.unwrapExpressionUntilTerm(that.getPrimary());
        if (p instanceof Tree.MemberOrTypeExpression) {
            Tree.MemberOrTypeExpression mte = (Tree.MemberOrTypeExpression) p;
            mte.setDirectlyInvoked(true);
        }
    }

    private static Tree.SpecifierOrInitializerExpression getSpecifier(
            Tree.ParameterDeclaration that) {
        Tree.TypedDeclaration d = that.getTypedDeclaration();
        if (d instanceof Tree.AttributeDeclaration) {
            return ((Tree.AttributeDeclaration) d)
                    .getSpecifierOrInitializerExpression();
        }
        else if (d instanceof Tree.MethodDeclaration) {
            return ((Tree.MethodDeclaration) that.getTypedDeclaration())
                    .getSpecifierExpression();
        }
        else {
            return null;
        }
    }
    
    private void checkDefaultArg(Tree.SpecifierOrInitializerExpression se, Parameter p) {
        if (se!=null) {
            if (se.getScope() instanceof Specification) {
                se.addError("parameter of specification statement may not define default value");
            }
            else {
                Declaration d = p.getDeclaration();
                if (d.isActual()) {
                    se.addError("parameter of actual declaration may not define default value: parameter " +
                            p.getName() + " of " + p.getDeclaration().getName());
                }
            }
            /*if (declaration instanceof Method &&
                !declaration.isToplevel() &&
                !(declaration.isClassOrInterfaceMember() && 
                        ((Declaration) declaration.getContainer()).isToplevel())) {
                se.addWarning("default arguments for parameters of inner methods not yet supported");
            }
            if (declaration instanceof Class && 
                    !declaration.isToplevel()) {
                se.addWarning("default arguments for parameters of inner classes not yet supported");
            }*/
                /*else {
                se.addWarning("parameter default values are not yet supported");
            }*/
        }
    }
    
    @Override public void visit(Tree.ParameterDeclaration that) {
        super.visit(that);
        Parameter p = that.getParameterModel();
        if (p.isDefaulted()) {
            if (p.getDeclaration().isParameter()) {
                getSpecifier(that).addError("parameter of callable parameter may not have default argument");
            }
            checkDefaultArg(getSpecifier(that), p);
        }
    }
}
