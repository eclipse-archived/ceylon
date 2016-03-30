package com.redhat.ceylon.compiler.js.loader;

import java.util.List;
import java.util.Map;

import com.redhat.ceylon.common.Backends;
import com.redhat.ceylon.model.typechecker.model.Annotation;
import com.redhat.ceylon.model.typechecker.model.Cancellable;
import com.redhat.ceylon.model.typechecker.model.Declaration;
import com.redhat.ceylon.model.typechecker.model.DeclarationWithProximity;
import com.redhat.ceylon.model.typechecker.model.Import;
import com.redhat.ceylon.model.typechecker.model.Module;
import com.redhat.ceylon.model.typechecker.model.Package;
import com.redhat.ceylon.model.typechecker.model.Scope;
import com.redhat.ceylon.model.typechecker.model.Type;
import com.redhat.ceylon.model.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.model.typechecker.model.Unit;

public abstract class LazyPackage extends Package {

    protected abstract void loadIfNecessary();

    @Override
    public boolean isToplevel() {
        loadIfNecessary();
        return super.isToplevel();
    }

    @Override
    public Module getModule() {
        // not lazy
        return super.getModule();
    }

    @Override
    public void setModule(Module module) {
        super.setModule(module);
    }

    @Override
    public List<String> getName() {
        // not lazy
        return super.getName();
    }

    @Override
    public void setName(List<String> name) {
        super.setName(name);
    }

    @Override
    public Iterable<Unit> getUnits() {
        loadIfNecessary();
        return super.getUnits();
    }

    @Override
    public boolean isShared() {
        loadIfNecessary();
        return super.isShared();
    }

    @Override
    public void setShared(boolean shared) {
        super.setShared(shared);
    }

    @Override
    public List<Declaration> getMembers() {
        loadIfNecessary();
        return super.getMembers();
    }

    @Override
    public Scope getContainer() {
        loadIfNecessary();
        return super.getContainer();
    }

    @Override
    public Scope getScope() {
        loadIfNecessary();
        return super.getScope();
    }

    @Override
    public String getNameAsString() {
        // not lazy
        return super.getNameAsString();
    }

    @Override
    public String toString() {
        loadIfNecessary();
        return super.toString();
    }

    @Override
    public String getQualifiedNameString() {
        // not lazy
        return super.getQualifiedNameString();
    }

    @Override
    public Declaration getMember(String name, List<Type> signature, boolean variadic) {
        loadIfNecessary();
        return super.getMember(name, signature, variadic);
    }

    @Override
    public Declaration getDirectMember(String name, List<Type> signature, boolean variadic) {
        loadIfNecessary();
        return super.getDirectMember(name, signature, variadic);
    }

    @Override
    public Declaration getDirectMemberForBackend(String name, Backends backends) {
        loadIfNecessary();
        return super.getDirectMemberForBackend(name, backends);
    }

    @Override
    public Type getDeclaringType(Declaration d) {
        loadIfNecessary();
        return super.getDeclaringType(d);
    }

    @Override
    public Declaration getMemberOrParameter(Unit unit, String name, List<Type> signature, boolean variadic) {
        loadIfNecessary();
        return super.getMemberOrParameter(unit, name, signature, variadic);
    }

    @Override
    public boolean isInherited(Declaration d) {
        loadIfNecessary();
        return super.isInherited(d);
    }

    @Override
    public TypeDeclaration getInheritingDeclaration(Declaration d) {
        loadIfNecessary();
        return super.getInheritingDeclaration(d);
    }

    @Override
    public Map<String, DeclarationWithProximity> getMatchingDeclarations(Unit unit,
            String startingWith, int proximity, Cancellable canceller) {
        loadIfNecessary();
        return super.getMatchingDeclarations(unit, startingWith, proximity, canceller);
    }

    @Override
    public Map<String, DeclarationWithProximity> getMatchingDirectDeclarations(
            String startingWith, int proximity) {
        loadIfNecessary();
        return super.getMatchingDirectDeclarations(startingWith, proximity);
    }

    @Override
    public Map<String, DeclarationWithProximity> getImportableDeclarations(
            Unit unit, String startingWith, List<Import> imports, int proximity) {
        loadIfNecessary();
        return super.getImportableDeclarations(unit, startingWith, imports, proximity);
    }

    @Override
    public List<Annotation> getAnnotations() {
        // not lazy
        return super.getAnnotations();
    }

    @Override
    public int hashCode() {
        loadIfNecessary();
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        loadIfNecessary();
        return super.equals(obj);
    }

    @Override
    public Unit getUnit() {
        loadIfNecessary();
        return super.getUnit();
    }

    @Override
    public void setUnit(Unit unit) {
        super.setUnit(unit);
    }

    @Override
    public Backends getScopedBackends() {
        loadIfNecessary();
        return super.getScopedBackends();
    }
}
