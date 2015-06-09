package com.redhat.ceylon.model.loader.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.redhat.ceylon.model.loader.JvmBackendUtil;
import com.redhat.ceylon.model.loader.ModelCompleter;
import com.redhat.ceylon.model.loader.mirror.ClassMirror;
import com.redhat.ceylon.model.typechecker.model.Annotation;
import com.redhat.ceylon.model.typechecker.model.Declaration;
import com.redhat.ceylon.model.typechecker.model.DeclarationKind;
import com.redhat.ceylon.model.typechecker.model.DeclarationWithProximity;
import com.redhat.ceylon.model.typechecker.model.Reference;
import com.redhat.ceylon.model.typechecker.model.Type;
import com.redhat.ceylon.model.typechecker.model.TypedReference;
import com.redhat.ceylon.model.typechecker.model.Scope;
import com.redhat.ceylon.model.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.model.typechecker.model.TypedDeclaration;
import com.redhat.ceylon.model.typechecker.model.Unit;
import com.redhat.ceylon.model.typechecker.model.Value;

/**
 * Represents a lazy toplevel attribute declaration.
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
public class LazyValue extends Value implements LazyElement, LocalDeclarationContainer {
    
    public ClassMirror classMirror;
    private ModelCompleter completer;
    private String realName;

    private Map<String,Declaration> localDeclarations;

    private boolean isLoaded = false;
    private boolean isLoaded2 = false;

    @Override
    protected Class<?> getModelClass() {
        return getClass().getSuperclass(); 
    }
    
    public LazyValue(ClassMirror classMirror, ModelCompleter completer) {
        this.classMirror = classMirror;
        this.completer = completer;
        this.realName = classMirror.getName();
        setName(JvmBackendUtil.getMirrorName(classMirror));
    }

    public String getRealName() {
        return this.realName;
    }

    private void load() {
        if(!isLoaded2){
            synchronized(completer.getLock()){
                if(!isLoaded){
                    isLoaded = true;
                    completer.complete(this);
                    isLoaded2 = true;
                }
            }
        }
    }
    
    @Override
    public String toString() {
        if (!isLoaded) {
            return "UNLOADED:" + super.toString();
        }
        return super.toString();
    }

    @Override
    public boolean isVariable() {
        load();
        return super.isVariable();
    }
    
    @Override
    public Type getType() {
        load();
        return super.getType();
    }

    @Override
    public boolean isLoaded() {
        return isLoaded;
    }

    @Override
    public List<Annotation> getAnnotations() {
        load();
        return super.getAnnotations();
    }

    @Override
    public void setVariable(boolean variable) {
        load();
        super.setVariable(variable);
    }

    @Override
    public boolean isCaptured() {
        load();
        return super.isCaptured();
    }

    @Override
    public void setCaptured(boolean local) {
        load();
        super.setCaptured(local);
    }

    @Override
    public DeclarationKind getDeclarationKind() {
        load();
        return super.getDeclarationKind();
    }

    @Override
    public TypeDeclaration getTypeDeclaration() {
        load();
        return super.getTypeDeclaration();
    }

    @Override
    public void setType(Type type) {
        load();
        super.setType(type);
    }

    @Override
    public TypedReference appliedTypedReference(Type qualifyingType, 
            List<Type> typeArguments, boolean assignment) {
        load();
        return super.appliedTypedReference(qualifyingType, typeArguments, assignment);
    }

    @Override
    public Reference appliedReference(Type pt, List<Type> typeArguments) {
        load();
        return super.appliedReference(pt, typeArguments);
    }

    @Override
    public boolean isMember() {
        load();
        return super.isMember();
    }

    @Override
    public Map<String, DeclarationWithProximity> getMatchingDeclarations(Unit unit, String startingWith, int proximity) {
        load();
        return super.getMatchingDeclarations(unit, startingWith, proximity);
    }

    @Override
    public TypedDeclaration getOriginalDeclaration() {
        load();
        return super.getOriginalDeclaration();
    }

    @Override
    public void setOriginalDeclaration(TypedDeclaration originalDeclaration) {
        load();
        super.setOriginalDeclaration(originalDeclaration);
    }

    @Override
    public Boolean getUnboxed() {
        load();
        return super.getUnboxed();
    }

    @Override
    public Scope getVisibleScope() {
        load();
        return super.getVisibleScope();
    }

    @Override
    public void setVisibleScope(Scope visibleScope) {
        load();
        super.setVisibleScope(visibleScope);
    }

    @Override
    public boolean isParameterized() {
        load();
        return super.isParameterized();
    }

    @Override
    public String getQualifiedNameString() {
        load();
        return super.getQualifiedNameString();
    }

    @Override
    public boolean isActual() {
        load();
        return super.isActual();
    }

    @Override
    public void setActual(boolean actual) {
        load();
        super.setActual(actual);
    }

    @Override
    public boolean isFormal() {
        load();
        return super.isFormal();
    }

    @Override
    public void setFormal(boolean formal) {
        load();
        super.setFormal(formal);
    }

    @Override
    public boolean isDefault() {
        load();
        return super.isDefault();
    }

    @Override
    public void setDefault(boolean def) {
        load();
        super.setDefault(def);
    }

    @Override
    public String getNativeBackend() {
        load();
        return super.getNativeBackend();
    }
    
    @Override
    public void setNativeBackend(String backend) {
        load();
        super.setNativeBackend(backend);
    }

    @Override
    public Declaration getRefinedDeclaration() {
        load();
        return super.getRefinedDeclaration();
    }

    @Override
    public void setRefinedDeclaration(Declaration refinedDeclaration) {
        load();
        super.setRefinedDeclaration(refinedDeclaration);
    }

    @Override
    public boolean isVisible(Scope scope) {
        load();
        return super.isVisible(scope);
    }

    @Override
    public boolean isDefinedInScope(Scope scope) {
        load();
        return super.isDefinedInScope(scope);
    }

    @Override
    public boolean isToplevel() {
        // NO lazy-loading since this uses getContainer() which is set before lazy-loading
        return super.isToplevel();
    }

    @Override
    public boolean isClassMember() {
        // NO lazy-loading since this uses getContainer() which is set before lazy-loading
        return super.isClassMember();
    }

    @Override
    public boolean isInterfaceMember() {
        // NO lazy-loading since this uses getContainer() which is set before lazy-loading
        return super.isInterfaceMember();
    }

    @Override
    public boolean isClassOrInterfaceMember() {
        // NO lazy-loading since this uses getContainer() which is set before lazy-loading
        return super.isClassOrInterfaceMember();
    }

    @Override
    public boolean equals(Object object) {
        // NO lazy-loading since this uses only things set before lazy-loading
        return super.equals(object);
    }

    @Override
    public int hashCode() {
        // NO lazy-loading since this uses only things set before lazy-loading
        return super.hashCode();
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean refines(Declaration other) {
        load();
        return super.refines(other);
    }

    @Override
    public List<Declaration> getMembers() {
        load();
        return super.getMembers();
    }

    @Override
    protected Declaration getMemberOrParameter(String name, List<Type> signature, boolean ellipsis) {
        load();
        return super.getMemberOrParameter(name, signature, ellipsis);
    }

    @Override
    public Declaration getMember(String name, List<Type> signature, boolean ellipsis) {
        load();
        return super.getMember(name, signature, ellipsis);
    }

    @Override
    public Declaration getDirectMember(String name, List<Type> signature, boolean ellipsis) {
        load();
        return super.getDirectMember(name, signature, ellipsis);
    }

    @Override
    public Type getDeclaringType(Declaration d) {
        load();
        return super.getDeclaringType(d);
    }

    @Override
    public Declaration getMemberOrParameter(Unit unit, String name, List<Type> signature, boolean ellipsis) {
        load();
        return super.getMemberOrParameter(unit, name, signature, ellipsis);
    }

    @Override
    public boolean isInherited(Declaration d) {
        load();
        return super.isInherited(d);
    }

    @Override
    public TypeDeclaration getInheritingDeclaration(Declaration d) {
        load();
        return super.getInheritingDeclaration(d);
    }
    
    @Override
    public void addMember(Declaration declaration) {
        // do this without lazy-loading
        super.addMember(declaration);
    }

    @Override
    public boolean isLocal() {
        // FIXME: this may be wrong now, but is it used?
        return false;
    }

    @Override
    public void setLocal(boolean local) {
    }

    @Override
    public Declaration getLocalDeclaration(String name) {
        load();
        if(localDeclarations == null)
            return null;
        return localDeclarations.get(name);
    }

    @Override
    public void addLocalDeclaration(Declaration declaration) {
        if(localDeclarations == null)
            localDeclarations = new HashMap<String, Declaration>();
        localDeclarations.put(declaration.getPrefixedName(), declaration);
    }

    @Override
    public boolean isDeprecated() {
        // requires no lazy-loading
        return super.isDeprecated();
    }
}
