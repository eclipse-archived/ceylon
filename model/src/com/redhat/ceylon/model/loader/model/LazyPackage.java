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

package com.redhat.ceylon.model.loader.model;

import static com.redhat.ceylon.model.typechecker.model.Util.lookupMember;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.redhat.ceylon.common.JVMModuleUtil;
import com.redhat.ceylon.model.loader.AbstractModelLoader;
import com.redhat.ceylon.model.loader.NamingBase;
import com.redhat.ceylon.model.loader.ModelLoader.DeclarationType;
import com.redhat.ceylon.model.loader.mirror.ClassMirror;
import com.redhat.ceylon.model.typechecker.model.Class;
import com.redhat.ceylon.model.typechecker.model.Declaration;
import com.redhat.ceylon.model.typechecker.model.Module;
import com.redhat.ceylon.model.typechecker.model.Package;
import com.redhat.ceylon.model.typechecker.model.ProducedType;
import com.redhat.ceylon.model.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.model.typechecker.model.Unit;

/**
 * Represents a lazy Package declaration.
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
public class LazyPackage extends Package {
    
    private AbstractModelLoader modelLoader;
    private List<Declaration> compiledDeclarations = new ArrayList<Declaration>(3);
    private Set<Unit> lazyUnits = new HashSet<Unit>();
    private Map<String,Declaration> cache = new HashMap<String,Declaration>();
    
    public LazyPackage(AbstractModelLoader modelLoader){
        this.modelLoader = modelLoader;
    }
    
    @Override
    public Declaration getMember(String name, List<ProducedType> signature, boolean ellipsis) {
        // FIXME: what use is this method in the type checker?
        return getDirectMember(name, signature, ellipsis);
    }
    
    // FIXME: redo this method better: https://github.com/ceylon/ceylon-spec/issues/90
    @Override
    public Declaration getDirectMember(String name, List<ProducedType> signature, boolean ellipsis) {
//        System.err.println("getMember "+name+" "+signature+" "+ellipsis);
        boolean canCache = (signature == null && !ellipsis);
        if(canCache){
            if(cache.containsKey(name)) {
                Declaration cachedDeclaration = cache.get(name);
                if (cachedDeclaration != null || ! modelLoader.searchAgain(this, name)) {
                    return cachedDeclaration;
                }

            }
        }
        Declaration ret = getDirectMemberMemoised(name, signature, ellipsis);
        if(canCache){
            cache.put(name, ret);
        }
        return ret;
    }
    
    private Declaration getDirectMemberMemoised(String name, List<ProducedType> signature, boolean ellipsis) {
        synchronized(modelLoader.getLock()){

            String pkgName = getQualifiedNameString();
            Module module = getModule();
            
            // we need its package ready first
            modelLoader.loadPackage(module, pkgName, false);

            // make sure we iterate over a copy of compiledDeclarations, to avoid lazy loading to modify it and
            // cause a ConcurrentModificationException: https://github.com/ceylon/ceylon-compiler/issues/399
            Declaration d = lookupMember(compiledDeclarations, name, signature, ellipsis);
            if (d != null) {
                return d;
            }

            String className = getQualifiedName(pkgName, name);
            ClassMirror classSymbol = modelLoader.lookupClassMirror(module, className);

            // only get it from the classpath if we're not compiling it, unless
            // it happens to be a java source
            if(classSymbol != null && (!classSymbol.isLoadedFromSource() || classSymbol.isJavaSource())) {
                d = modelLoader.convertToDeclaration(module, className, DeclarationType.VALUE);
                if (d instanceof Class) {
                    Class c = (Class) d;
                    if (c.isAbstraction() && signature != null) {
                        ArrayList<Declaration> list = new ArrayList<Declaration>(c.getOverloads());
                        list.add(c);
                        return lookupMember(list, name, signature, ellipsis);
                    }
                }
                return d;
            }
            d = getDirectMemberFromSource(name);
            
            if (d == null
                    && Character.isLowerCase(name.codePointAt(0))
                    && Character.isUpperCase(Character.toUpperCase(name.codePointAt(0)))) {
                // Might be trying to get an annotation constructor for a Java annotation type
                // So try to find the annotation type with two strategies:
                // - urlDecoder -> UrlDecover and url -> Url
                // - urlDecoder -> URLDecoder and url -> URL
                for(String annotationName : Arrays.asList(NamingBase.capitalize(name), NamingBase.getReverseJavaBeanName(name), NamingBase.capitalize(name).replaceFirst("__(CONSTRUCTOR|TYPE|PACKAGE|FIELD|METHOD|ANNOTATION_TYPE|LOCAL_VARIABLE|PARAMETER|SETTER|GETTER)$", ""))){
                    Declaration possibleAnnotationType = getDirectMember(annotationName, signature, ellipsis);
                    if (possibleAnnotationType != null
                            && possibleAnnotationType instanceof LazyInterface
                            && ((LazyInterface)possibleAnnotationType).isAnnotationType()) {
                        // addMember() will have added a Method if we found an annotation type
                        // so now we can look for the constructor again
                        d = lookupMember(compiledDeclarations, name, signature, ellipsis);
                    }
                }
            }
            return d;
        }
    }

    private Declaration getDirectMemberFromSource(String name) {
        for (Declaration d: super.getMembers()) {
            if (com.redhat.ceylon.model.typechecker.model.Util.isResolvable(d) /* && d.isShared() */ 
            && com.redhat.ceylon.model.typechecker.model.Util.isNamed(name, d)) {
                return d;
            }
        }
        return null;
    }

    public String getQualifiedName(final String pkgName, String name) {
        // no need to quote the name itself as java keywords are lower-cased and we append a _ to every
        // lower-case toplevel so they can never be java keywords
        String className = pkgName.isEmpty() ? name : JVMModuleUtil.quoteJavaKeywords(pkgName) + "." + name;
        return className;
    }
    
    // FIXME: This is only here for wildcard imports, and we should be able to make it lazy like the rest
    // with a bit of work in the typechecker
    // FIXME: redo this method better: https://github.com/ceylon/ceylon-spec/issues/90
    @Override
    public List<Declaration> getMembers() {
        synchronized(modelLoader.getLock()){
            // make sure the package is loaded
            modelLoader.loadPackage(getModule(), getQualifiedNameString(), true);
            List<Declaration> sourceDeclarations = super.getMembers();
            LinkedList<Declaration> ret = new LinkedList<Declaration>();
            ret.addAll(sourceDeclarations);
            ret.addAll(compiledDeclarations);
            return ret;
        }
    }

    @Override
    public void addMember(Declaration declaration) {
        super.addMember(declaration);
        flushCache(declaration);
    }
    
    private void flushCache(Declaration declaration) {
        cache.remove(declaration.getName());
    }

    public void addCompiledMember(Declaration d) {
        synchronized(modelLoader.getLock()){
            flushCache(d);
            compiledDeclarations.add(d);
            if (d instanceof LazyInterface
                    && !((LazyInterface)d).isCeylon()
                    && ((LazyInterface)d).isAnnotationType()) {
                makeInteropAnnotation((LazyInterface)d);
                
            }
            if ((d instanceof LazyClass ||
                    d instanceof LazyInterface)  && d.getUnit().getFilename() != null) {
                lazyUnits.add(d.getUnit());
            }
        }
    }

    /**
     * Adds extra members to the package for annotation interop.
     * For a Java declaration {@code @interface Annotation} we generate 
     * a model corresponding to:
     * <pre>
     *   annotation class Annotation$Proxy(...) satisfies Annotation {
     *       // a `shared` class parameter for each method of Annotation
     *   }
     *   annotation JavaAnnotation javaAnnotation(...) => JavaAnnotation$Proxy(...);
     * </pre>
     * 
     * We also make a {@code *__method}, {@code *__field} etc version for each
     * {@code @Target} program element
     * @param iface The model of the annotation @interface
     */
    private void makeInteropAnnotation(LazyInterface iface) {
        AnnotationProxyClass klass = modelLoader.makeInteropAnnotationClass(iface, this);
        
        compiledDeclarations.add(modelLoader.makeInteropAnnotationConstructor(iface, klass, null, this));
        
        EnumSet<AnnotationTarget> annotationTargets = AnnotationTarget.annotationTargets(klass);
        if (annotationTargets != null) {
            for (OutputElement target : OutputElement.possibleCeylonTargets(annotationTargets)) {
                compiledDeclarations.add(modelLoader.makeInteropAnnotationConstructor(iface, klass,  
                        target, this));
            }
        }
        
        compiledDeclarations.add(klass);
    }

    @Override
    public Iterable<Unit> getUnits() {
        synchronized(modelLoader.getLock()){
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
        synchronized(modelLoader.getLock()){
            for (Declaration d : unit.getDeclarations()) {
                flushCache(d);
                if (d instanceof TypeDeclaration) {
                    ((TypeDeclaration)d).clearProducedTypeCache();
                }
            }
            if (unit.getFilename().endsWith(".class") || unit.getFilename().endsWith(".java")) {
                lazyUnits.remove(unit);
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
