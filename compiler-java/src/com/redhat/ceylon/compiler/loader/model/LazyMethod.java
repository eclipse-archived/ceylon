/*
 * Copyright Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the authors tag. All rights reserved.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License version 2.
 * 
 * This particular file is subject to the "Classpath" exception as provided in the 
 * LICENSE file that accompanied this code.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */

package com.redhat.ceylon.compiler.loader.model;

import java.util.List;
import java.util.Map;

import com.redhat.ceylon.compiler.java.util.Util;
import com.redhat.ceylon.compiler.loader.ModelCompleter;
import com.redhat.ceylon.compiler.loader.mirror.ClassMirror;
import com.redhat.ceylon.compiler.loader.mirror.MethodMirror;
import com.redhat.ceylon.compiler.typechecker.model.Annotation;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.DeclarationKind;
import com.redhat.ceylon.compiler.typechecker.model.DeclarationWithProximity;
import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.ParameterList;
import com.redhat.ceylon.compiler.typechecker.model.ProducedReference;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.ProducedTypedReference;
import com.redhat.ceylon.compiler.typechecker.model.Scope;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.TypeParameter;
import com.redhat.ceylon.compiler.typechecker.model.TypedDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.Unit;

/**
 * Represents a lazy toplevel method declaration.
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
public class LazyMethod extends Method implements LazyElement {

    private MethodMirror methodMirror;
    public final ClassMirror classMirror;
    private ModelCompleter completer;
    private String realName;
    private String realMethodName;
    
    private boolean isLoaded = false;
    private boolean isLoaded2 = false;
    private boolean defaultedAnnotation;
    
    public LazyMethod(ClassMirror classMirror, ModelCompleter completer) {
        this.classMirror = classMirror;
        this.completer = completer;
        this.realName = classMirror.getName();
        setName(Util.getMirrorName(classMirror));
    }

    public void setMethodMirror(MethodMirror methorMirror) {
        this.methodMirror = methorMirror;
    }
    
    public MethodMirror getMethodMirror(){
        load();
        return this.methodMirror;
    }

    public String getRealName() {
        return this.realName;
    }

    public void setRealMethodName(String name) {
        this.realMethodName = name;
    }

    public String getRealMethodName(){
        return this.realMethodName;
    }
    
    private void load() {
        if(!isLoaded2){
            synchronized(completer){
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
    public ProducedType getType() {
        load();
        return super.getType();
    }
    
    @Override
    public boolean isDeclaredVoid() {
        load();
        return super.isDeclaredVoid();
    }

    @Override
    public boolean isParameterized() {
        load();
        return super.isParameterized();
    }

    @Override
    public List<TypeParameter> getTypeParameters() {
        load();
        return super.getTypeParameters();
    }

    @Override
    public List<ParameterList> getParameterLists() {
        load();
        return super.getParameterLists();
    }
    
    @Override
    public Object getAnnotationConstructor() {
        load();
        return super.getAnnotationConstructor();
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
    public ProducedTypedReference getProducedTypedReference(ProducedType qualifyingType, List<ProducedType> typeArguments) {
        load();
        return super.getProducedTypedReference(qualifyingType, typeArguments);
    }

    @Override
    public ProducedReference getProducedReference(ProducedType pt, List<ProducedType> typeArguments) {
        load();
        return super.getProducedReference(pt, typeArguments);
    }

    @Override
    public boolean isMember() {
        load();
        return super.isMember();
    }

    @Override
    public boolean isVariable() {
        load();
        return super.isVariable();
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
    public List<Annotation> getAnnotations() {
        load();
        return super.getAnnotations();
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
    public Declaration getRefinedDeclaration() {
        load();
        return super.getRefinedDeclaration();
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
    public boolean isToplevel() {
        load();
        return super.isToplevel();
    }

    @Override
    public boolean isClassMember() {
        load();
        return super.isClassMember();
    }

    @Override
    public boolean isInterfaceMember() {
        load();
        return super.isInterfaceMember();
    }

    @Override
    public boolean isClassOrInterfaceMember() {
        load();
        return super.isClassOrInterfaceMember();
    }

    @Override
    public boolean refines(Declaration other) {
        load();
        return super.refines(other);
    }

    @Override
    public Unit getUnit() {
        load();
        return super.getUnit();
    }

    @Override
    public List<Declaration> getMembers() {
        load();
        return super.getMembers();
    }

    @Override
    protected Declaration getMemberOrParameter(String name, List<ProducedType> signature, boolean ellipsis) {
        load();
        return super.getMemberOrParameter(name, signature, ellipsis);
    }

    @Override
    public Declaration getMember(String name, List<ProducedType> signature, boolean ellipsis) {
        load();
        return super.getMember(name, signature, ellipsis);
    }

    @Override
    public Declaration getDirectMember(String name, List<ProducedType> signature, boolean ellipsis) {
        load();
        return super.getDirectMember(name, signature, ellipsis);
    }

    @Override
    public ProducedType getDeclaringType(Declaration d) {
        load();
        return super.getDeclaringType(d);
    }

    @Override
    public Declaration getMemberOrParameter(Unit unit, String name, List<ProducedType> signature, boolean ellipsis) {
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
    public boolean isAnnotation() {
        load();
        return super.isAnnotation();
    }
    
    @Override
    public boolean isLoaded() {
        return isLoaded;
    }
}
