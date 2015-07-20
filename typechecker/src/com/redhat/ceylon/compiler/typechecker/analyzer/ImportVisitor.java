package com.redhat.ceylon.compiler.typechecker.analyzer;

import static com.redhat.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.declaredInPackage;
import static com.redhat.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.importedPackage;
import static com.redhat.ceylon.compiler.typechecker.tree.TreeUtil.formatPath;
import static com.redhat.ceylon.compiler.typechecker.tree.TreeUtil.name;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.isConstructor;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.isToplevelAnonymousClass;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.isToplevelClassConstructor;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.notOverloaded;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.redhat.ceylon.common.Backend;
import com.redhat.ceylon.common.BackendSupport;
import com.redhat.ceylon.compiler.typechecker.context.TypecheckerUnit;
import com.redhat.ceylon.compiler.typechecker.parser.CeylonLexer;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;
import com.redhat.ceylon.model.typechecker.model.Declaration;
import com.redhat.ceylon.model.typechecker.model.Import;
import com.redhat.ceylon.model.typechecker.model.ImportList;
import com.redhat.ceylon.model.typechecker.model.Package;
import com.redhat.ceylon.model.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.model.typechecker.model.TypedDeclaration;
import com.redhat.ceylon.model.typechecker.model.Value;

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
public class ImportVisitor extends Visitor {
    
    private TypecheckerUnit unit;
    private final BackendSupport backendSupport;

    private Backend inBackend = null;
    
    public ImportVisitor(BackendSupport backendSupport) {
        this.backendSupport = backendSupport;
    }
    
    public ImportVisitor(TypecheckerUnit unit, 
            BackendSupport backendSupport) {
        this.unit = unit;
        this.backendSupport = backendSupport;
        String nat = 
                unit.getPackage()
                    .getModule()
                    .getNativeBackend();
        inBackend = Backend.fromAnnotation(nat);
    }
    
    @Override public void visit(Tree.CompilationUnit that) {
        unit = that.getUnit();
        Backend ib = inBackend;
        String nat = 
                unit.getPackage()
                    .getModule()
                    .getNativeBackend();
        inBackend = Backend.fromAnnotation(nat);
        super.visit(that);
        inBackend = ib;
        HashSet<String> set = new HashSet<String>();
        for (Tree.Import im: that.getImportList().getImports()) {
            Tree.ImportPath ip = im.getImportPath();
            if (ip!=null) {
                String mp = formatPath(ip.getIdentifiers());
                if (!set.add(mp)) {
                    ip.addError("duplicate import: '" + mp + "'");
                }
            }
        }
    }
    
    @Override
    public void visit(Tree.Import that) {
        Package importedPackage = 
                importedPackage(that.getImportPath(), 
                        backendSupport);
        if (importedPackage!=null) {
            that.getImportPath().setModel(importedPackage);
            Tree.ImportMemberOrTypeList imtl = 
                    that.getImportMemberOrTypeList();
            if (imtl!=null) {
                ImportList il = imtl.getImportList();
                il.setImportedScope(importedPackage);
                Set<String> names = new HashSet<String>();
                for (Tree.ImportMemberOrType member: 
                        imtl.getImportMemberOrTypes()) {
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
            if (dec.isShared() && 
                    !dec.isAnonymous() && 
                    !ignoredMembers.contains(dec.getName()) &&
                    !isNonimportable(importedPackage, dec.getName())) {
                addWildcardImport(il, dec);
            }
        }
    }
    
    private void importAllMembers(TypeDeclaration importedType, 
            Set<String> ignoredMembers, ImportList til) {
        for (Declaration dec: importedType.getMembers()) {
            if (dec.isShared() && 
                    (dec.isStaticallyImportable() || 
                            isConstructor(dec)) && 
                    !dec.isAnonymous() && 
                    !ignoredMembers.contains(dec.getName())) {
                addWildcardImport(til, dec, importedType);
            }
        }
    }
    
    private void addWildcardImport(ImportList il, 
            Declaration dec) {
        if (!hidesToplevel(dec)) {
            Import i = new Import();
            i.setAlias(dec.getName());
            i.setDeclaration(dec);
            i.setWildcardImport(true);
            addWildcardImport(il, dec, i);
        }
    }
    
    private void addWildcardImport(ImportList il, 
            Declaration dec, TypeDeclaration td) {
        if (!hidesToplevel(dec)) {
            Import i = new Import();
            i.setAlias(dec.getName());
            i.setDeclaration(dec);
            i.setWildcardImport(true);
            i.setTypeDeclaration(td);
            addWildcardImport(il, dec, i);
        }
    }
    
    private void addWildcardImport(ImportList il, 
            Declaration dec, Import i) {
        if (notOverloaded(dec)) {
            String alias = i.getAlias();
            if (alias!=null) {
                Import o = unit.getImport(dec.getName());
                if (o!=null && o.isWildcardImport()) {
                    if (o.getDeclaration().equals(dec) || 
                            dec.isNativeHeader()) {
                        //this case only happens in the IDE,
                        //due to reuse of the Unit
                        unit.getImports().remove(o);
                        il.getImports().remove(o);
                    }
                    else if (!dec.isNative()) {
                        i.setAmbiguous(true);
                        o.setAmbiguous(true);
                    }
                }
                unit.getImports().add(i);
                il.getImports().add(i);
            }
        }
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
    
    private boolean checkForHiddenToplevel(Tree.Identifier id, 
            Import i, Tree.Alias alias) {
        for (Declaration d: unit.getDeclarations()) {
            String n = d.getName();
            Declaration idec = i.getDeclaration();
            if (d.isToplevel() && n!=null && 
                    i.getAlias().equals(n) &&
                    !idec.equals(d) && 
                    //it is legal to import an object declaration 
                    //in the current package without providing an
                    //alias:
                    !isLegalAliasFreeImport(d, idec)) {
                if (alias==null) {
                    id.addError("toplevel declaration with this name declared in this unit: '" + n + "'");
                }
                else {
                    alias.addError("toplevel declaration with this name declared in this unit: '" + n + "'");
                }
                return true;
            }
        }
        return false;
    }

    private static boolean isLegalAliasFreeImport
            (Declaration dec, Declaration importedDec) {
        if (importedDec instanceof Value) {
            Value value = (Value) importedDec;
            TypeDeclaration td = value.getTypeDeclaration();
            return td.isObjectClass() && td.equals(dec);
        }
        else {
            return false;
        }
    }
    
    private void importMembers(Tree.ImportMemberOrType member, 
            Declaration d) {
        Tree.ImportMemberOrTypeList imtl = 
                member.getImportMemberOrTypeList();
        if (imtl!=null) {
            if (d instanceof Value) {
                Value v = (Value) d;
                TypeDeclaration td = v.getTypeDeclaration();
                if (td.isObjectClass()) {
                    d = td;
                }
            }
            if (d instanceof TypeDeclaration) {
                Set<String> names = new HashSet<String>();
                ImportList til = imtl.getImportList();
                TypeDeclaration td = (TypeDeclaration) d;
                til.setImportedScope(td);
                List<Tree.ImportMemberOrType> imts = 
                        imtl.getImportMemberOrTypes();
                for (Tree.ImportMemberOrType imt: imts) {
                    names.add(importMember(imt, td, til));
                }
                if (imtl.getImportWildcard()!=null) {
                    importAllMembers(td, names, til);
                }
                else if (imts.isEmpty()) {
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
            int tt = id.getToken().getType();
            if (d instanceof TypeDeclaration &&
                    tt!=CeylonLexer.UIDENTIFIER) {
                id.addError("imported type should have uppercase alias: '" +
                        d.getName() + "'");
            }
            else if (d instanceof TypedDeclaration &&
                    tt!=CeylonLexer.LIDENTIFIER) {
                id.addError("imported member should have lowercase alias: '" +
                        d.getName() + "'");
            }
        }
    }
    
    private String importMember(Tree.ImportMemberOrType member,
            Package importedPackage, ImportList il) {
        Tree.Identifier id = 
                member.getIdentifier();
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
        Declaration d = 
                importedPackage.getMember(name, null, false);
        if (d==null) {
            id.addError("imported declaration not found: '" + 
                    name + "'", 
                    100);
            unit.getUnresolvedReferences().add(id);
        }
        else {
            if (!declaredInPackage(d, unit)) {
                if (!d.isShared()) {
                    id.addError("imported declaration is not shared: '" +
                            name + "'", 
                            400);
                }
                else if (d.isPackageVisibility()) {
                    id.addError("imported package private declaration is not visible: '" +
                            name + "'");
                }
                else if (d.isProtectedVisibility()) {
                    id.addError("imported protected declaration is not visible: '" +
                            name + "'");
                }
            }
            i.setDeclaration(d);
            member.setDeclarationModel(d);
            if (il.hasImport(d)) {
                id.addError("already imported: '" + name + "'");
            }
            else if (!checkForHiddenToplevel(id, i, alias)) {
                addImport(member, il, i);
            }
            checkAliasCase(alias, d);
        }
        if (d!=null) {
            importMembers(member, d);
        }
        return name;
    }
    
    private String importMember(Tree.ImportMemberOrType member, 
            TypeDeclaration td, ImportList il) {
        Tree.Identifier id = 
                member.getIdentifier();
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
            id.addError("imported declaration not found: '" + 
                    name + "' of '" + 
                    td.getName() + "'", 
                    100);
            unit.getUnresolvedReferences().add(id);
        }
        else {
            List<Declaration> members = 
                    m.getContainer().getMembers();
            for (Declaration d: members) {
                String dn = d.getName();
                if (dn!=null &&
                        dn.equals(name) && 
                        !d.sameKind(m) &&
                        !d.isAnonymous()) {
                    //crazy interop cases like isOpen() + open()
                    id.addError("ambiguous member declaration: '" +
                            name + "' of '" + 
                            td.getName() + "'");
                    return null;
                }
            }
            if (!m.isShared()) {
                id.addError("imported declaration is not shared: '" +
                        name + "' of '" + 
                        td.getName() + "'", 
                        400);
            }
            else if (!declaredInPackage(m, unit)) {
                if (m.isPackageVisibility()) {
                    id.addError("imported package private declaration is not visible: '" +
                            name + "' of '" + 
                            td.getName() + "'");
                }
                else if (m.isProtectedVisibility()) {
                    id.addError("imported protected declaration is not visible: '" +
                            name + "' of '" + 
                            td.getName() + "'");
                }
            }
            i.setTypeDeclaration(td);
            if (!m.isStaticallyImportable() && 
                    !isToplevelClassConstructor(td, m) &&
                    !isToplevelAnonymousClass(m.getContainer())) {
                if (alias==null) {
                    member.addError("does not specify an alias");
                }
            }
            i.setDeclaration(m);
            member.setDeclarationModel(m);
            if (il.hasImport(m)) {
                id.addError("already imported: '" +
                        name + "' of '" + td.getName() + "'");
            }
            else {
                if (m.isStaticallyImportable() ||
                        isToplevelClassConstructor(td, m) ||
                        isToplevelAnonymousClass(m.getContainer())) {
                    if (!checkForHiddenToplevel(id, i, alias)) {
                        addImport(member, il, i);
                    }
                }
                else {
                    addMemberImport(member, il, i);
                }
            }
            checkAliasCase(alias, m);
        }
        if (m!=null) {
            importMembers(member, m);
        }
        //imtl.addError("member aliases may not have member aliases");
        return name;
    }

    private void addImport(Tree.ImportMemberOrType member, 
            ImportList il, Import i) {
        String alias = i.getAlias();
        if (alias!=null) {
            Map<String, String> mods = unit.getModifiers();
            if (mods.containsKey(alias) && 
                    mods.get(alias).equals(alias)) {
                //spec says you can't hide a language modifier
                //unless the modifier itself has an alias
                //(this is perhaps a little heavy-handed)
                member.addError("import hides a language modifier: '" + 
                        alias + "'");
            }
            else {
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
                    member.addError("duplicate import alias: '" + 
                            alias + "'");
                }
            }
        }
    }
    
    private void addMemberImport(Tree.ImportMemberOrType member, 
            ImportList il, Import i) {
        String alias = i.getAlias();
        if (alias!=null) {
            if (il.getImport(alias)==null) {
                unit.getImports().add(i);
                il.getImports().add(i);
            }
            else {
                member.addError("duplicate member import alias: '" + 
                        alias + "'");
            }
        }
    }
    
    private boolean isNonimportable(Package pkg, String name) {
        return pkg.getQualifiedNameString().equals("java.lang") &&
                ("Object".equals(name) ||
                 "Throwable".equals(name) ||
                 "Exception".equals(name));
    }
    
}
