package com.redhat.ceylon.model.loader.model;

import static com.redhat.ceylon.model.typechecker.model.ModelUtil.lookupMember;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.lookupMemberForBackend;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

import com.redhat.ceylon.common.Backends;
import com.redhat.ceylon.common.JVMModuleUtil;
import com.redhat.ceylon.model.loader.AbstractModelLoader;
import com.redhat.ceylon.model.loader.ModelLoader.DeclarationType;
import com.redhat.ceylon.model.loader.NamingBase;
import com.redhat.ceylon.model.loader.mirror.ClassMirror;
import com.redhat.ceylon.model.typechecker.model.Class;
import com.redhat.ceylon.model.typechecker.model.Declaration;
import com.redhat.ceylon.model.typechecker.model.LanguageModuleCache;
import com.redhat.ceylon.model.typechecker.model.ModelUtil;
import com.redhat.ceylon.model.typechecker.model.Module;
import com.redhat.ceylon.model.typechecker.model.NothingType;
import com.redhat.ceylon.model.typechecker.model.Package;
import com.redhat.ceylon.model.typechecker.model.Type;
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
    private boolean descriptorLoaded;
    
    public LazyPackage(AbstractModelLoader modelLoader){
        this.modelLoader = modelLoader;
    }
    
    @Override
    public Declaration getMember(String name, List<Type> signature, boolean ellipsis) {
        // FIXME: what use is this method in the type checker?
        if (signature==null 
            && name.equals("Nothing")
            && isLanguagePackage()) {
            LanguageModuleCache languageModuleCache = getModule().getLanguageModuleCache();
            if (languageModuleCache != null) {
                NothingType nothingDeclaration = languageModuleCache.getNothingDeclaration();
                if (nothingDeclaration != null) {
                    return nothingDeclaration;
                }
            }
        }
        return getDirectMember(name, signature, ellipsis);
    }
    
    // FIXME: redo this method better: https://github.com/ceylon/ceylon-spec/issues/90
    @Override
    public Declaration getDirectMember(String name, List<Type> signature, boolean ellipsis) {
        return getDirectMember(name, signature, ellipsis, true);
    }
    
    private Declaration getDirectMember(String name, List<Type> signature, boolean ellipsis, boolean tryAlternates) {
//        System.err.println("getMember "+name+" "+signature+" "+ellipsis);

        boolean canCache = signature == null && !ellipsis;
        if(canCache){
            if(cache.containsKey(name)) {
                Declaration cachedDeclaration = cache.get(name);
                if (! modelLoader.searchAgain(cachedDeclaration, this, name)) {
                    return cachedDeclaration;
                }

            }
        }
        Declaration ret = getDirectMemberMemoised(name, signature, ellipsis, Backends.ANY, tryAlternates);
        if(canCache){
            cache.put(name, ret);
        }
        return ret;
    }
    
    @Override
    public Declaration getDirectMemberForBackend(String name, Backends backends) {
        // FIXME: do we want to cache those calls too? If yes we need to add the backend type in the cache key
        // and invalidate properly in flushCache()
        return getDirectMemberMemoised(name, null, false, backends, true);
    }
    
    private Declaration getDirectMemberMemoised(final String name, final List<Type> signature, final boolean ellipsis, final Backends backends, final boolean tryAlternates) {
        return modelLoader.synchronizedCall(new Callable<Declaration>() {
            @Override
            public Declaration call() throws Exception {
                String pkgName = getQualifiedNameString();
                Module module = getModule();
                
                // we need its package ready first
                modelLoader.loadPackage(module, pkgName, false);

                // make sure we iterate over a copy of compiledDeclarations, to avoid lazy loading to modify it and
                // cause a ConcurrentModificationException: https://github.com/ceylon/ceylon-compiler/issues/399
                Declaration d = !backends.none()
                        ? lookupMemberForBackend(compiledDeclarations, name, backends)
                        : lookupMember(compiledDeclarations, name, signature, ellipsis);
                if (d != null) {
                    return d;
                }

                String className = getQualifiedName(pkgName, name);
                ClassMirror classSymbol = modelLoader.lookupClassMirror(module, className);

                // only get it from the classpath if we're not compiling it, unless
                // it happens to be a java source
                if(classSymbol != null 
                        && (!classSymbol.isLoadedFromSource() || classSymbol.isJavaSource())) {
                    d = modelLoader.convertToDeclaration(module, className, DeclarationType.VALUE);
                    if (d instanceof Class) {
                        Class c = (Class) d;
                        if (c.isAbstraction() && signature != null) {
                            ArrayList<Declaration> list = new ArrayList<Declaration>(c.getOverloads());
                            list.add(c);
                            return !backends.none()
                                    ? lookupMemberForBackend(list, name, backends)
                                    : lookupMember(list, name, signature, ellipsis);
                        }
                    }
                    // if we're not looking for a backend, or we found the right backend, fine
                    // if not, keep looking
                    if (d != null && ModelUtil.isForBackend(d, backends))
                        return d;
                }
                d = getDirectMemberFromSource(name, signature, ellipsis, backends);
                
                if (d == null
                        && tryAlternates
                        && !name.isEmpty()
                        && Character.isLowerCase(name.codePointAt(0))
                        && Character.isUpperCase(Character.toUpperCase(name.codePointAt(0)))) {
                    // Might be trying to get an annotation constructor for a Java annotation type
                    // So try to find the annotation type with two strategies:
                    // - urlDecoder -> UrlDecover and url -> Url
                    // - urlDecoder -> URLDecoder and url -> URL
                    for(String annotationName : Arrays.asList(NamingBase.capitalize(name), NamingBase.getReverseJavaBeanName(name), NamingBase.capitalize(name).replaceFirst("__(CONSTRUCTOR|TYPE|PACKAGE|FIELD|METHOD|ANNOTATION_TYPE|LOCAL_VARIABLE|PARAMETER|SETTER|GETTER)$", ""))){
                        Declaration possibleAnnotationType = getDirectMember(annotationName, signature, ellipsis, false);
                        if (possibleAnnotationType != null
                                && possibleAnnotationType instanceof LazyInterface
                                && ((LazyInterface)possibleAnnotationType).isAnnotationType()) {
                            // addMember() will have added a Function if we found an annotation type
                            // so now we can look for the constructor again
                            d = !backends.none()
                                    ? lookupMemberForBackend(compiledDeclarations, name, backends)
                                    : lookupMember(compiledDeclarations, name, signature, ellipsis);
                        }
                    }
                }
                if (d == null
                        && tryAlternates
                        && !name.isEmpty()
                        && Character.isUpperCase(name.codePointAt(0))
                        && Character.isLowerCase(Character.toLowerCase(name.codePointAt(0)))) {
                    // Might be trying to get a lowercase type with an upper-case pretend name
                    // So try to find the type type with two strategies:
                    // - UrlDecoder -> urlDecover and Url -> url
                    // - URLDecoder -> urlDecoder and URL -> url
                    for(String typeName : Arrays.asList(NamingBase.getJavaBeanName(name))){
                        Declaration possibleLowercaseType = getDirectMember(typeName, signature, ellipsis, false);
                        if (possibleLowercaseType != null
                                && (possibleLowercaseType instanceof LazyInterface
                                        || possibleLowercaseType instanceof LazyClass)) {
                            // addMember() will have added the proper named type if we found an type
                            // so now we can look for the type again
                            d = !backends.none()
                                    ? lookupMemberForBackend(compiledDeclarations, name, backends)
                                    : lookupMember(compiledDeclarations, name, signature, ellipsis);
                        }
                    }
                }
                return d;
            }
        });
    }

    public Declaration getDirectMemberFromSource(String name, List<Type> signature, boolean ellipsis, Backends backends) {
        List<Declaration> sourceDeclarations = super.getMembers();
        return lookupMember(sourceDeclarations, name, signature, ellipsis, false, backends);
    }

    public String getQualifiedName(final String pkgName, String name) {
        // no need to quote the name itself as java keywords are lower-cased and we append a _ to every
        // lower-case toplevel so they can never be java keywords
        return pkgName.isEmpty() ? name : JVMModuleUtil.quoteJavaKeywords(pkgName) + "." + name;
    }
    
    // FIXME: This is only here for wildcard imports, and we should be able to make it lazy like the rest
    // with a bit of work in the typechecker
    // FIXME: redo this method better: https://github.com/ceylon/ceylon-spec/issues/90
    @Override
    public List<Declaration> getMembers() {
        return modelLoader.synchronizedCall(new Callable<List<Declaration>>() {
            @Override
            public List<Declaration> call() throws Exception {
                // make sure the package is loaded
                modelLoader.loadPackage(getModule(), getQualifiedNameString(), true);
                List<Declaration> sourceDeclarations = LazyPackage.super.getMembers();
                LinkedList<Declaration> ret = new LinkedList<Declaration>();
                ret.addAll(sourceDeclarations);
                ret.addAll(compiledDeclarations);
                return ret;
            }
        });
    }

    @Override
    public void addMember(Declaration declaration) {
        super.addMember(declaration);
        flushCache(declaration);
    }
    
    private void flushCache(Declaration declaration) {
        cache.remove(declaration.getName());
    }

    public void addCompiledMember(final Declaration d) {
        modelLoader.synchronizedRun(new Runnable() {
            @Override
            public void run() {
                flushCache(d);
                compiledDeclarations.add(d);
                if (d instanceof LazyInterface
                        && !((LazyInterface)d).isCeylon()
                        && ((LazyInterface)d).isAnnotationType()) {
                    compiledDeclarations.addAll(modelLoader.makeInteropAnnotation((LazyInterface)d, LazyPackage.this));
                    
                }
                if ((d instanceof LazyClass ||
                        d instanceof LazyInterface)  && d.getUnit().getFilename() != null) {
                    lazyUnits.add(d.getUnit());
                }
            }
        });
    }

    @Override
    public Iterable<Unit> getUnits() {
        return modelLoader.synchronizedCall(new Callable<Iterable<Unit>>() {
            @Override
            public Iterable<Unit> call() throws Exception {
                Iterable<Unit> sourceUnits = LazyPackage.super.getUnits();
                LinkedList<Unit> ret = new LinkedList<Unit>();
                for (Unit unit : sourceUnits) {
                    ret.add(unit);
                }
                ret.addAll(lazyUnits);
                return ret;
            }
        });
    }

    @Override
    public void removeUnit(final Unit unit) {
        modelLoader.synchronizedRun(new Runnable() {
            @Override
            public void run() {
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
                    LazyPackage.super.removeUnit(unit);
                }
            }
        });
    }
    
    public void addLazyUnit(final Unit unit) {
        modelLoader.synchronizedRun(new Runnable() {
            @Override
            public void run() {
                lazyUnits.add(unit);
            }
        });
    }

    public void setDescriptorLoaded(boolean loaded) {
        this.descriptorLoaded = loaded;
    }

    public boolean isDescriptorLoaded() {
        return descriptorLoaded;
    }
}
