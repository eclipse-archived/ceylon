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

import static com.redhat.ceylon.compiler.typechecker.model.Util.lookupMember;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.redhat.ceylon.compiler.java.util.Util;
import com.redhat.ceylon.compiler.loader.AbstractModelLoader;
import com.redhat.ceylon.compiler.loader.ModelLoader.DeclarationType;
import com.redhat.ceylon.compiler.loader.mirror.ClassMirror;
import com.redhat.ceylon.compiler.typechecker.model.Class;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Package;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.Unit;

/**
 * Represents a lazy Package declaration.
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
public class LazyPackage extends Package {
    
    private AbstractModelLoader modelLoader;
    private List<Declaration> compiledDeclarations = new LinkedList<Declaration>();
    private Set<Unit> lazyUnits = new HashSet<Unit>();
    
    public LazyPackage(AbstractModelLoader modelLoader){
        this.modelLoader = modelLoader;
    }
    
    @Override
    public Declaration getMember(String name, List<ProducedType> signature, boolean ellipsis) {
        // FIXME: what use is this method in the type checker?
        return getDirectMember(name, signature, ellipsis);
    }
    
    @Override
    public Declaration getDirectMemberOrParameter(String name, List<ProducedType> signature, boolean ellipsis) {
        // FIXME: what's the difference?
        return getDirectMember(name, signature, ellipsis);
    }
    
    // FIXME: redo this method better: https://github.com/ceylon/ceylon-spec/issues/90
    @Override
    public Declaration getDirectMember(String name, List<ProducedType> signature, boolean ellipsis) {
        synchronized(modelLoader){

            String pkgName = getQualifiedNameString();

            // we need its package ready first
            modelLoader.loadPackage(pkgName, false);

            // make sure we iterate over a copy of compiledDeclarations, to avoid lazy loading to modify it and
            // cause a ConcurrentModificationException: https://github.com/ceylon/ceylon-compiler/issues/399
            Declaration d = lookupMember(copy(compiledDeclarations), name, signature, ellipsis);
            if (d != null) {
                return d;
            }

            String className = getQualifiedName(pkgName, name);
            ClassMirror classSymbol = modelLoader.lookupClassMirror(className);

            // only get it from the classpath if we're not compiling it, unless
            // it happens to be a java source
            if(classSymbol != null && (!classSymbol.isLoadedFromSource() || classSymbol.isJavaSource())) {
                d = modelLoader.convertToDeclaration(className, DeclarationType.VALUE);
                if (d instanceof Class) {
                    if ( ((Class) d).isAbstraction()) {
                        // make sure we iterate over a copy of compiledDeclarations, to avoid lazy loading to modify it and
                        // cause a ConcurrentModificationException: https://github.com/ceylon/ceylon-compiler/issues/399
                        return lookupMember(copy(compiledDeclarations), name, signature, ellipsis);
                    }
                }
                return d;
            }
            return getDirectMemberFromSource(name);
        }
    }

    private List<Declaration> copy(List<Declaration> list) {
        List<Declaration> ret = new ArrayList<Declaration>(list.size());
        ret.addAll(list);
        return ret;
    }

    private Declaration getDirectMemberFromSource(String name) {
        for (Declaration d: super.getMembers()) {
            if (com.redhat.ceylon.compiler.typechecker.model.Util.isResolvable(d) /* && d.isShared() */ 
            && com.redhat.ceylon.compiler.typechecker.model.Util.isNamed(name, d)) {
                return d;
            }
        }
        return null;
    }

    private String getQualifiedName(final String pkgName, String name) {
        // FIXME: some refactoring needed
        String className = pkgName.isEmpty() ? name : Util.quoteJavaKeywords(pkgName) + "." + name;
        return className;
    }
    
    // FIXME: This is only here for wildcard imports, and we should be able to make it lazy like the rest
    // with a bit of work in the typechecker
    // FIXME: redo this method better: https://github.com/ceylon/ceylon-spec/issues/90
    @Override
    public List<Declaration> getMembers() {
        synchronized(modelLoader){
            // make sure the package is loaded
            modelLoader.loadPackage(getQualifiedNameString(), true);
            List<Declaration> sourceDeclarations = super.getMembers();
            LinkedList<Declaration> ret = new LinkedList<Declaration>();
            ret.addAll(sourceDeclarations);
            ret.addAll(compiledDeclarations);
            return ret;
        }
    }

    public void addMember(Declaration d) {
        synchronized(modelLoader){
            compiledDeclarations.add(d);
            if (d instanceof LazyClass && d.getUnit().getFilename() != null) {
                lazyUnits.add(d.getUnit());
            }
        }
    }

    
    @Override
    public Iterable<Unit> getUnits() {
        synchronized(modelLoader){
            Iterable<Unit> sourceUnits = super.getUnits();
            LinkedList<Unit> ret = new LinkedList<Unit>();
            for (Unit unit : sourceUnits) {
                ret.add(unit);
            }
            ret.addAll(lazyUnits);
            return ret;
        }
    }

    @Override
    public void removeUnit(Unit unit) {
        synchronized(modelLoader){
            if (lazyUnits.remove(unit)) {
                for (Declaration d : unit.getDeclarations()) {
                    compiledDeclarations.remove(d);
                    // TODO : remove the declaration from the declaration map in AbstractModelLoader
                }
                modelLoader.removeDeclarations(unit.getDeclarations());
            } else {
                super.removeUnit(unit);
            }
        }
    }
}
