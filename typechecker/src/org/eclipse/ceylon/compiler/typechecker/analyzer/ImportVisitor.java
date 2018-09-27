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

import static org.eclipse.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.declaredInPackage;
import static org.eclipse.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.importCorrectionMessage;
import static org.eclipse.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.importedPackage;
import static org.eclipse.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.memberCorrectionMessage;
import static org.eclipse.ceylon.compiler.typechecker.tree.TreeUtil.formatPath;
import static org.eclipse.ceylon.compiler.typechecker.tree.TreeUtil.name;
import static org.eclipse.ceylon.model.typechecker.model.ModelUtil.isAnonymousClass;
import static org.eclipse.ceylon.model.typechecker.model.ModelUtil.isResolvable;
import static org.eclipse.ceylon.model.typechecker.model.ModelUtil.isToplevelAnonymousClass;
import static org.eclipse.ceylon.model.typechecker.model.ModelUtil.isToplevelClassConstructor;
import static org.eclipse.ceylon.model.typechecker.model.ModelUtil.notOverloaded;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.ceylon.common.Backends;
import org.eclipse.ceylon.compiler.typechecker.parser.CeylonLexer;
import org.eclipse.ceylon.compiler.typechecker.tree.Tree;
import org.eclipse.ceylon.compiler.typechecker.tree.Visitor;
import org.eclipse.ceylon.model.loader.JvmBackendUtil;
import org.eclipse.ceylon.model.loader.NamingBase;
import org.eclipse.ceylon.model.typechecker.model.Cancellable;
import org.eclipse.ceylon.model.typechecker.model.Declaration;
import org.eclipse.ceylon.model.typechecker.model.Import;
import org.eclipse.ceylon.model.typechecker.model.ImportList;
import org.eclipse.ceylon.model.typechecker.model.ImportScope;
import org.eclipse.ceylon.model.typechecker.model.Package;
import org.eclipse.ceylon.model.typechecker.model.Scope;
import org.eclipse.ceylon.model.typechecker.model.TypeDeclaration;
import org.eclipse.ceylon.model.typechecker.model.TypedDeclaration;
import org.eclipse.ceylon.model.typechecker.model.Unit;
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
public class ImportVisitor extends Visitor {
    
    private Unit unit;
    private Cancellable cancellable;

    public ImportVisitor(Cancellable cancellable) {
        this.cancellable = cancellable;
    }
    
    public ImportVisitor(Unit unit, Cancellable cancellable) {
        this.unit = unit;
        this.cancellable = cancellable;
    }

    @Override public void visit(Tree.CompilationUnit that) {
        unit = that.getUnit();
        super.visit(that);
        HashSet<String> set = new HashSet<String>();
        for (Tree.Import im: that.getImportList().getImports()) {
            Tree.ImportPath ip = im.getImportPath();
            if (ip!=null) {
                String mp = formatPath(ip.getIdentifiers());
                if (!set.add(mp)) {
                    ip.addError("duplicate import: package '" 
                            + mp + "' is already imported");
                }
            }
        }
    }
    
    @Override
    public void visit(Tree.Import that) {
        Backends scopedBackends =
                that.getScope()
                    .getScopedBackends();
        if (!scopedBackends.none() 
         && !scopedBackends.supports(
                 unit.getSupportedBackends())) {
            return;
        }
        
        Tree.ImportPath path = that.getImportPath();
        Package importedPackage = 
                importedPackage(path, unit);
        if (importedPackage!=null) {
            path.setModel(importedPackage);
            Tree.ImportMemberOrTypeList imtl = 
                    that.getImportMemberOrTypeList();
            if (imtl!=null) {
                ImportList il = imtl.getImportList();
                il.setImportedScope(importedPackage);
                Set<String> names = new HashSet<String>();
                List<Tree.ImportMemberOrType> list = 
                        imtl.getImportMemberOrTypes();
                for (Tree.ImportMemberOrType member: list) {
                    names.add(importMember(member, importedPackage, il));
                }
                if (imtl.getImportWildcard()!=null) {
                    importAllMembers(importedPackage, names, il);
                } 
                else if (list.isEmpty()) {
                    imtl.addError("empty import list", 1020);
                }
            }
        }
    }
    
    private ImportScope getImportScope(ImportList importList) {
        Scope scope = importList;
        while (scope instanceof ImportList) {
            scope = scope.getContainer();
        }
        return scope instanceof ImportScope ? 
                (ImportScope) scope : unit;
    }
    
    private void importAllMembers(Package importedPackage, 
            Set<String> ignoredMembers, ImportList il) {
        for (Declaration dec: importedPackage.getMembers()) {
            if (dec.isShared() && 
                    isResolvable(dec) &&
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
                    (isStaticNonGeneric(dec, importedType) || 
                            dec.isConstructor()) && 
                    isResolvable(dec) &&
                    !ignoredMembers.contains(dec.getName())) {
                addWildcardImport(til, dec, importedType);
            }
        }
    }
    
    private void addWildcardImport(ImportList il, 
            Declaration dec) {
        if (!hidesToplevel(dec, il)) {
            Import i = new Import();
            i.setAlias(dec.getName());
            i.setDeclaration(dec);
            i.setWildcardImport(true);
            addWildcardImport(il, dec, i);
        }
    }
    
    private void addWildcardImport(ImportList il, 
            Declaration dec, TypeDeclaration td) {
        if (!hidesToplevel(dec, il)) {
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
                ImportScope scope = getImportScope(il);
                Import o = scope.getImport(dec.getName());
                if (o!=null && o.isWildcardImport()) {
                    if (o.getDeclaration().equals(dec) || 
                            dec.isNativeHeader()) {
                        //this case only happens in the IDE,
                        //due to reuse of the Unit
                        scope.removeImport(o);
                        il.getImports().remove(o);
                    }
                    else if (!dec.isNative()) {
                        i.setAmbiguous(true);
                        o.setAmbiguous(true);
                    }
                }
                scope.addImport(i);
                il.getImports().add(i);
            }
        }
    }
    
    private boolean hidesToplevel(Declaration dec, ImportList il) {
        ImportScope scope = getImportScope(il); 
        for (Declaration d: scope.getMembers()) {
            String n = d.getName();
            if (d.isToplevel() && n!=null && 
                    dec.getName().equals(n)) {
                return true;
            }
        }
        return false;
    }
    
    private boolean checkForHiddenToplevel(Tree.Identifier id, 
            Import i, Tree.Alias alias, ImportList il) {
        ImportScope scope = getImportScope(il); 
        for (Declaration d: scope.getMembers()) {
            String n = d.getName();
            Declaration idec = i.getDeclaration();
            if (n!=null && 
                    i.getAlias().equals(n) &&
                    !idec.equals(d) && 
                    //it is legal to import an object declaration 
                    //in the current package without providing an
                    //alias:
                    !isLegalAliasFreeImport(d, idec)) {
                String qn = d.getQualifiedNameString();
                String message = scope instanceof Unit ?
                        "toplevel declaration with this name declared in this unit" :
                        "declaration with this name declared in this scope";
                if (alias==null) {
                    String iqn = idec.getQualifiedNameString();
                    id.addError(message + ": imported '" 
                            + iqn + "' would hide '" + qn + 
                            "' (add an alias to the import)");
                }
                else {
                    alias.addError(message + ": imported '" 
                            + n + "' would hide '" + qn + 
                            "' (choose a different alias for the import)");
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
                    imtl.addError("empty import list", 1020);
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
                        d.getName() + "' is a type declaration");
            }
            else if (d instanceof TypedDeclaration &&
                    tt!=CeylonLexer.LIDENTIFIER) {
                id.addError("imported member should have lowercase alias: '" +
                        d.getName() + "' is not a type declaration");
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
            String al = name(alias.getIdentifier());
            if (name.equals(al)) {
                alias.addUsageWarning(
                        Warning.redundantImportAlias, 
                        "redundant import alias");
            }
            i.setAlias(al);
        }
        if (isNonimportable(importedPackage, name)) {
            id.addError("root type may not be imported: '" +
                    name + "' in '" + 
                    importedPackage.getNameAsString() + 
                    "' is represented by '" + 
                    name + "' in 'ceylon.language'");
            return name;
        }        
        Declaration d = 
                importedPackage.getMember(name, null, false);
        if (d == null) {
            String newName = adaptJavaName(name);
            d = importedPackage.getMember(newName, null, false);
            // only do this for Java declarations we fudge
            if (d!=null && !d.isJava()) {
                d = null;
            }
        }
        if (d==null) {
            id.addError("imported declaration not found: '" 
                    + name + "'" 
                    + importCorrectionMessage(name, importedPackage, 
                            unit, cancellable), 
                    100);
            unit.setUnresolvedReferences();
        }
        else {
            if (!declaredInPackage(d, unit)) {
                if (!d.isShared()) {
                    id.addError("imported declaration is not visible: '" +
                            name + "' is not shared", 
                            400);
                }
                else if (!d.withinRestrictions(unit)) {
                    id.addError("imported declaration is not visible: '" +
                            name + "' is restricted");
                }
                else if (d.isPackageVisibility()) {
                    id.addError("imported declaration is not visible: '" +
                            name + "' is package private");
                }
                else if (d.isProtectedVisibility()) {
                    id.addError("imported declaration is not visible: '" +
                            name + "' is protected");
                }
            }
            i.setDeclaration(d);
            member.setDeclarationModel(d);
            if (il.hasImport(d)) {
                id.addError("already imported: '" + name + "'");
            }
            else if (!checkForHiddenToplevel(id, i, alias, il)) {
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
        if (m == null && td.isJava()) {
            String newName = adaptJavaName(name);
            m = td.getMember(newName, null, false);
        }
        if (m==null) {
            id.addError("imported declaration not found: '" 
                    + name + "' of '" 
                    + td.getName() + "'" 
                    + memberCorrectionMessage(name, td, 
                            null, unit, cancellable), 
                    100);
            unit.setUnresolvedReferences();
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
                            td.getName() + 
                            "' is ambiguous");
                    return null;
                }
            }
            if (!m.isShared()) {
                id.addError("imported declaration is not visible: '" +
                        name + "' of '" + 
                        td.getName() + 
                        "' is not shared", 
                        400);
            }
            else if (!m.withinRestrictions(unit)) {
                id.addError("imported declaration is not visible: '" +
                        name + "' of '" + 
                        td.getName() + 
                        "' is restricted", 
                        400);
            }
            else if (!declaredInPackage(m, unit)) {
                if (m.isPackageVisibility()) {
                    id.addError("imported declaration is not visible: '" +
                            name + "' of '" + 
                            td.getName() + 
                            "' is package private");
                }
                else if (m.isProtectedVisibility()) {
                    id.addError("imported declaration is not visible: '" +
                            name + "' of '" + 
                            td.getName() + 
                            "' is protected");
                }
            }
            i.setTypeDeclaration(td);
            if (!isStaticNonGeneric(m, td) && 
                    !isToplevelClassConstructor(td, m) &&
                    !isToplevelAnonymousClass(m.getContainer())) {
                if (alias==null) {
                    if (m.isStatic()) {
                        member.addError("illegal static import: static member '" + 
                                name + "' belongs to the generic type '" + 
                                td.getName() + "'");
                    }
                    else if (m.isConstructor()) {
                        member.addError("illegal static import: '" + 
                                td.getName() + "' is not a toplevel class");
                    }
                    else if (isAnonymousClass(m.getContainer())) {
                        member.addError("illegal static import: '" + 
                                td.getName() + "' is not a toplevel anonymous class");
                    }
                    else {
                        member.addError("illegal static import: '" + 
                                name + "' is not static");
                    }
                }
            }
            i.setDeclaration(m);
            member.setDeclarationModel(m);
            if (il.hasImport(m)) {
                id.addError("duplicate import: '" +
                        name + "' of '" + td.getName() + 
                        "' is already imported");
            }
            else {
                if (isStaticNonGeneric(m, td) ||
                        isToplevelClassConstructor(td, m) ||
                        isToplevelAnonymousClass(m.getContainer())) {
                    if (!checkForHiddenToplevel(id, i, alias, il)) {
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

    private static String adaptJavaName(String name) {
        return JvmBackendUtil.isInitialLowerCase(name) ? 
                NamingBase.capitalize(name) : 
                NamingBase.getJavaBeanName(name);
    }
    
    private boolean isStaticNonGeneric(Declaration dec, 
            TypeDeclaration outer) {
        return dec.isStatic() 
            && (outer.isJava() || !outer.isParameterized()); 
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
                //instead, it should be a warning
                member.addUsageWarning(Warning.hidesLanguageModifier,
                        "import hides a language modifier: '" + 
                        alias + "' is a language modifier");
            }
            else {
                ImportScope scope = getImportScope(il);
                Import o = scope.getImport(alias);
                if (o==null) {
                    scope.addImport(i);
                    il.getImports().add(i);
                }
                else if (o.isWildcardImport()) {
                    scope.removeImport(o);
                    il.getImports().remove(o);
                    scope.addImport(i);
                    il.getImports().add(i);
                }
                else {
                    member.addError("duplicate import alias: '" + 
                            alias + "' is already used");
                }
            }
        }
    }
    
    private void addMemberImport(Tree.ImportMemberOrType member, 
            ImportList il, Import i) {
        String alias = i.getAlias();
        if (alias!=null) {
            if (il.getImport(alias)==null) {
                getImportScope(il).addImport(i);
                il.getImports().add(i);
            }
            else {
                member.addError("duplicate member import alias: '" + 
                        alias + "' is already used");
            }
        }
    }
    
    private static boolean isNonimportable(Package pkg, String name) {
        String pname = pkg.getQualifiedNameString();
        return pname.equals("java.lang")
                && ("Object".equals(name) 
                 || "Throwable".equals(name) 
                 || "Exception".equals(name)) 
            || pname.equals("java.lang.annotation")
                && "Annotation".equals(name);
    }
    
}
