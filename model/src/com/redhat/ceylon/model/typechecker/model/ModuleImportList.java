package com.redhat.ceylon.model.typechecker.model;

import static com.redhat.ceylon.model.typechecker.model.ModelUtil.lookupMember;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.lookupMemberForBackend;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.redhat.ceylon.common.Backends;

public class ModuleImportList implements Scope {

    private Scope container;
    private Unit unit;
    
    private List<Declaration> members = new ArrayList<Declaration>();
    
    public void setContainer(Scope container) {
        this.container = container;
    }
    
    @Override
    public String getQualifiedNameString() {
        return getContainer().getQualifiedNameString();
    }

    @Override
    public Scope getContainer() {
        return container;
    }

    @Override
    public Scope getScope() {
        return container;
    }

    @Override
    public Map<String, DeclarationWithProximity> getMatchingDeclarations(Unit unit, String startingWith, int proximity, Cancellable canceller) {
        return getContainer().getMatchingDeclarations(unit, startingWith, proximity, canceller);
    }

    @Override
    public List<Declaration> getMembers() {
        return members;
    }

    @Override
    public Declaration getMember(String name, List<Type> signature, boolean variadic) {
        return getDirectMember(name, signature, variadic);
    }

    @Override
    public Declaration getMemberOrParameter(Unit unit, String name, List<Type> signature, boolean variadic) {
        Declaration d = getDirectMember(name, signature, variadic);
        if (d != null) {
            return d;
        }
        return getContainer().getMemberOrParameter(unit, name, signature, variadic);
    }

    @Override
    public Declaration getDirectMember(String name, List<Type> signature, boolean variadic) {
        return lookupMember(getMembers(), name, signature, variadic);
    }

    @Override
    public Declaration getDirectMemberForBackend(String name, Backends backends) {
        return lookupMemberForBackend(getMembers(), name, backends);
    }

    @Override
    public boolean isInherited(Declaration d) {
        return false;
    }

    @Override
    public TypeDeclaration getInheritingDeclaration(Declaration d) {
        return null;
    }

    @Override
    public Type getDeclaringType(Declaration d) {
        return null;
    }

    @Override
    public boolean isToplevel() {
        return false;
    }

    @Override
    public Backends getScopedBackends() {
        return getContainer().getScopedBackends();
    }

    @Override
    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }
    
    @Override
    public void addMember(Declaration declaration) {
        members.add(declaration);
    }

}
