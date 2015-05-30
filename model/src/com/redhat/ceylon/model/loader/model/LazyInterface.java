package com.redhat.ceylon.model.loader.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.redhat.ceylon.model.loader.AbstractModelLoader;
import com.redhat.ceylon.model.loader.JvmBackendUtil;
import com.redhat.ceylon.model.loader.ModelCompleter;
import com.redhat.ceylon.model.loader.mirror.ClassMirror;
import com.redhat.ceylon.model.typechecker.model.Annotation;
import com.redhat.ceylon.model.typechecker.model.Declaration;
import com.redhat.ceylon.model.typechecker.model.Interface;
import com.redhat.ceylon.model.typechecker.model.Reference;
import com.redhat.ceylon.model.typechecker.model.Type;
import com.redhat.ceylon.model.typechecker.model.Scope;
import com.redhat.ceylon.model.typechecker.model.TypeParameter;
import com.redhat.ceylon.model.typechecker.model.Unit;

/**
 * Represents a lazy Interface declaration.
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
public class LazyInterface extends Interface implements LazyContainer {

    public final ClassMirror classMirror;
    private ModelCompleter completer;
    private String realName;
    private boolean isStatic;
    private boolean isCeylon;
    private Map<String,Declaration> localDeclarations;
    
    private boolean isLoaded = false;
    private boolean isLoaded2 = false;
    private boolean isTypeParamsLoaded = false;
    private boolean isTypeParamsLoaded2 = false;
    private boolean isAnnotationType = false;
    private boolean local;
    public ClassMirror companionClass;

    @Override
    protected Class<?> getModelClass() {
        return getClass().getSuperclass(); 
    }
    
    public LazyInterface(ClassMirror classMirror, ModelCompleter completer) {
        this.classMirror = classMirror;
        this.completer = completer;
        this.realName = classMirror.getName();
        setName(JvmBackendUtil.getMirrorName(classMirror));
        this.isStatic = classMirror.isStatic();
        this.isAnnotationType  = classMirror.isAnnotationType();
        this.isCeylon = classMirror.getAnnotation(AbstractModelLoader.CEYLON_CEYLON_ANNOTATION) != null;
    }

    @Override
    public boolean isErasedTypeArguments() {
        return !isCeylon();
    }
    
    public boolean isCeylon() {
        return isCeylon;
    }

    public boolean isStatic() {
        return isStatic;
    }
    
    public boolean isAnnotationType() {
        return isAnnotationType;
    }

    public String getRealName() {
        return this.realName;
    }
    
    private void load() {
        if(!isLoaded2){
            synchronized(completer.getLock()){
                loadTypeParams();
                if(!isLoaded){
                    isLoaded = true;
                    completer.complete(this);
                    isLoaded2 = true;
                }
            }
        }
    }

    private void loadTypeParams() {
        if(!isTypeParamsLoaded2){
            synchronized(completer.getLock()){
                if(!isTypeParamsLoaded){
                    isTypeParamsLoaded = true;
                    completer.completeTypeParameters(this);
                    isTypeParamsLoaded2 = true;
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
    public List<Declaration> getMembers() {
        load();
        return super.getMembers();
    }

    @Override
    public Type getType() {
        loadTypeParams();
        return super.getType();
    }

    @Override
    public Type getExtendedType() {
        load();
        return super.getExtendedType();
    }

    @Override
    public List<TypeParameter> getTypeParameters() {
        loadTypeParams();
        return super.getTypeParameters();
    }

    @Override
    public boolean isMember() {
        // NO lazy-loading since this uses getContainer() which is set before lazy-loading
        return super.isMember();
    }

    @Override
    public Type getDeclaringType(Declaration d) {
        load();
        return super.getDeclaringType(d);
    }

    @Override
    public boolean isParameterized() {
        load();
        return super.isParameterized();
    }

    @Override
    public List<Type> getSatisfiedTypes() {
        load();
        return super.getSatisfiedTypes();
    }

    @Override
    public List<Type> getCaseTypes() {
        load();
        return super.getCaseTypes();
    }

    @Override
    public Reference appliedReference(Type pt,
            List<Type> typeArguments) {
        loadTypeParams();
        return super.appliedReference(pt, typeArguments);
    }

    @Override
    public Type appliedType(Type outerType,
            List<Type> typeArguments) {
        loadTypeParams();
        return super.appliedType(outerType, typeArguments);
    }

    @SuppressWarnings("deprecation")
    @Override
    public List<Declaration> getInheritedMembers(String name) {
        load();
        return super.getInheritedMembers(name);
    }

    @Override
    public Declaration getRefinedMember(String name, List<Type> signature, boolean ellipsis) {
        load();
        return super.getRefinedMember(name, signature, ellipsis);
    }

    @Override
    public Declaration getMember(String name, List<Type> signature, boolean ellipsis) {
        load();
        return super.getMember(name, signature, ellipsis);
    }

    @Override
    public Declaration getMemberOrParameter(String name, List<Type> signature, boolean ellipsis) {
        load();
        return super.getMemberOrParameter(name, signature, ellipsis);
    }

    @Override
    public boolean isAlias() {
        // does not require lazy loading since it depends on class
        return super.isAlias();
    }

    @Override
    public Type getSelfType() {
        load();
        return super.getSelfType();
    }

    @Override
    public Scope getVisibleScope() {
        // NO lazy-loading since this uses getContainer() which is set before lazy-loading
        return super.getVisibleScope();
    }

    @Override
    public List<Annotation> getAnnotations() {
        load();
        return super.getAnnotations();
    }

    @Override
    public String getQualifiedNameString() {
        // never need to complete since the container is set before completion
        return super.getQualifiedNameString();
    }

    @Override
    public boolean isActual() {
        load();
        return super.isActual();
    }

    @Override
    public boolean isFormal() {
        load();
        return super.isFormal();
    }

    @Override
    public boolean isDefault() {
        load();
        return super.isDefault();
    }

    @Override
    public boolean isVisible(Scope scope) {
        // NO lazy-loading since this uses getContainer() which is set before lazy-loading
        return super.isVisible(scope);
    }

    @Override
    public boolean isDefinedInScope(Scope scope) {
        load();
        return super.isDefinedInScope(scope);
    }

    @Override
    public boolean isCaptured() {
        load();
        return super.isCaptured();
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
    public Unit getUnit() {
        // this doesn't require to load the model
        return super.getUnit();
    }

    @Override
    public Scope getContainer() {
        // NO lazy-loading since this is set before lazy-loading
        return super.getContainer();
    }

    @Override
    public Declaration getDirectMember(String name, List<Type> signature, boolean ellipsis) {
        load();
        return super.getDirectMember(name, signature, ellipsis);
    }

    @Override
    public Declaration getMemberOrParameter(Unit unit, String name, List<Type> signature, boolean ellipsis) {
        load();
        return super.getMemberOrParameter(unit, name, signature, ellipsis);
    }

    @Override
    public void addMember(Declaration decl) {
        // do this without lazy-loading
        super.addMember(decl);
    }

    @Override
    public boolean isLoaded() {
        return isLoaded;
    }

    @Override
    public void setLocal(boolean local) {
        this.local = local;
    }
    
    @Override
    public boolean isLocal(){
        return this.local ;
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
        load();
        return super.isDeprecated();
    }
}
