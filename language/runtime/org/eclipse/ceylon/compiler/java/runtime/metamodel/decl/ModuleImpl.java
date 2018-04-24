/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.compiler.java.runtime.metamodel.decl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.AnnotatedElement;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.eclipse.ceylon.common.JVMModuleUtil;
import org.eclipse.ceylon.compiler.java.Util;
import org.eclipse.ceylon.compiler.java.language.AbstractCallable;
import org.eclipse.ceylon.compiler.java.language.ByteArrayResource;
import org.eclipse.ceylon.compiler.java.language.FileResource;
import org.eclipse.ceylon.compiler.java.language.ObjectArrayIterable;
import org.eclipse.ceylon.compiler.java.language.ZipResource;
import org.eclipse.ceylon.compiler.java.metadata.Ignore;
import org.eclipse.ceylon.compiler.java.metadata.Name;
import org.eclipse.ceylon.compiler.java.metadata.TypeInfo;
import org.eclipse.ceylon.compiler.java.metadata.TypeParameter;
import org.eclipse.ceylon.compiler.java.metadata.TypeParameters;
import org.eclipse.ceylon.compiler.java.runtime.metamodel.AnnotationBearing;
import org.eclipse.ceylon.compiler.java.runtime.metamodel.Metamodel;
import org.eclipse.ceylon.compiler.java.runtime.model.ReifiedType;
import org.eclipse.ceylon.compiler.java.runtime.model.RuntimeModelLoader;
import org.eclipse.ceylon.compiler.java.runtime.model.RuntimeModuleManager;
import org.eclipse.ceylon.compiler.java.runtime.model.TypeDescriptor;

import org.eclipse.ceylon.compiler.java.runtime.metamodel.decl.ClassOrInterfaceDeclarationImpl;
import org.eclipse.ceylon.compiler.java.runtime.metamodel.decl.ImportImpl;
import org.eclipse.ceylon.compiler.java.runtime.metamodel.decl.ModuleImpl;

import ceylon.language.Empty;
import ceylon.language.Iterable;
import ceylon.language.Null;
import ceylon.language.Resource;
import ceylon.language.Sequential;
import ceylon.language.Tuple;
import ceylon.language.empty_;
import ceylon.language.finished_;
import ceylon.language.impl.BaseIterable;
import ceylon.language.impl.BaseIterator;
import ceylon.language.meta.type_;
import ceylon.language.meta.declaration.Import;
import ceylon.language.meta.declaration.Package;
import ceylon.language.meta.model.ClassOrInterface;

public class ModuleImpl implements ceylon.language.meta.declaration.Module,
        AnnotationBearing,
        ReifiedType {

    @Ignore
    public static final TypeDescriptor $TypeDescriptor$ = TypeDescriptor.klass(ModuleImpl.class);
    private static final java.lang.annotation.Annotation[] NO_ANNOTATION = new java.lang.annotation.Annotation[0];
    protected org.eclipse.ceylon.model.typechecker.model.Module declaration;
    private Sequential<? extends Package> packages;
    private Sequential<? extends Import> dependencies;
    
    public ModuleImpl(org.eclipse.ceylon.model.typechecker.model.Module declaration) {
        this.declaration = declaration;
    }

    @Override
    @Ignore
    public java.lang.annotation.Annotation[] $getJavaAnnotations$() {
        if(declaration.isDefaultModule() || declaration.isJava())
            return NO_ANNOTATION;
        return Metamodel.getJavaClass(declaration).getAnnotations();
    }
    
    @Override
    @Ignore
    public boolean $isAnnotated$(java.lang.Class<? extends java.lang.annotation.Annotation> annotationType) {
        if(declaration.isDefaultModule() || declaration.isJava())
            return false;
        final AnnotatedElement element = Metamodel.getJavaClass(declaration);;
        return element != null ? element.isAnnotationPresent(annotationType) : false;
    }
    
    @Override
    public <AnnotationType extends java.lang.annotation.Annotation> boolean annotated(TypeDescriptor reifed$AnnotationType) {
        return Metamodel.isAnnotated(reifed$AnnotationType, this);
    }

    @Override
    @TypeInfo("ceylon.language::Sequential<Annotation>")
    @TypeParameters(@TypeParameter(value = "Annotation", satisfies = "ceylon.language::Annotation"))
    public <Annotation extends java.lang.annotation.Annotation> Sequential<? extends Annotation> annotations(@Ignore TypeDescriptor $reifiedAnnotation) {
        return Metamodel.annotations($reifiedAnnotation, this);
    }

    @Override
    @TypeInfo("ceylon.language::Sequential<ceylon.language.meta.declaration::Package>")
    public Sequential<? extends Package> getMembers() {
        // no need to synchronise as concurrent invocations should get the same array back
        if(this.packages == null){
            List<org.eclipse.ceylon.model.typechecker.model.Package> modelPackages = declaration.getPackages();
            Package[] packages = new Package[modelPackages.size()];
            for(int i=0;i<packages.length;i++){
                packages[i] = Metamodel.getOrCreateMetamodel(modelPackages.get(i));
            }
            this.packages = Util.sequentialWrapper(Package.$TypeDescriptor$, packages);
        }
        return this.packages;
    }

    @Override
    @TypeInfo("ceylon.language::Null|ceylon.language.meta.declaration::Package")
    public Package findPackage(@Name("name") String name) {
        org.eclipse.ceylon.model.typechecker.model.Package pkg = declaration.getDirectPackage(name);
        return pkg == null ? null : Metamodel.getOrCreateMetamodel(pkg);
    }

    @Override
    @TypeInfo("ceylon.language::Null|ceylon.language.meta.declaration::Package")
    public Package findImportedPackage(@Name("name") String name) {
        org.eclipse.ceylon.model.typechecker.model.Package pkg = declaration.getPackage(name);
        return pkg == null ? null : Metamodel.getOrCreateMetamodel(pkg);
    }

    @Override
    @TypeInfo("ceylon.language::Sequential<ceylon.language.meta.declaration::Import>")
    public Sequential<? extends Import> getDependencies() {
        // no need to synchronise as concurrent invocations should get the same array back
        if(this.dependencies == null){
            List<org.eclipse.ceylon.model.typechecker.model.ModuleImport> modelImports = declaration.getImports();
            //FreeImport[] imports = new FreeImport[modelImports.size()];
            ArrayList<ImportImpl> sb = new ArrayList<ImportImpl>(modelImports.size());
            for(org.eclipse.ceylon.model.typechecker.model.ModuleImport moduleImport : modelImports){
                sb.add(new ImportImpl(this, moduleImport));
            }
            ImportImpl[] array = sb.toArray(new ImportImpl[0]);
            this.dependencies = new ObjectArrayIterable<ImportImpl>(Import.$TypeDescriptor$, array).sequence();
        }
        return this.dependencies;
    }

    @Override
    @TypeInfo("ceylon.language::Resource")
    public Resource resourceByPath(@Name("path") String path) {
        String fullPath = path;
        if (!fullPath.startsWith("/")) {
            String modPath = ("default".equals(getName())) ? "" : getName().replace('.', '/') + "/";
            fullPath = modPath + path;
        } else {
            fullPath = fullPath.substring(1);
        }
        fullPath = JVMModuleUtil.quoteJavaKeywordsInFilename(fullPath);
        
        // First lets ask the module manager for the contents of the resource
        RuntimeModuleManager moduleManager = Metamodel.getModuleManager();
        if (moduleManager != null) {
            RuntimeModelLoader modelLoader = moduleManager.getModelLoader();
            if (modelLoader != null) {
                byte[] contents = modelLoader.getContents(declaration, fullPath);
                if (contents != null) {
                    URI uri = modelLoader.getContentUri(declaration, fullPath);
                    return new ByteArrayResource(contents, uri);
                }
            }
        }
        
        // Second let's see if we can find the on-disk location of the module
        String moduleUnitFullPath = declaration.getUnit().getFullPath();
        if (moduleUnitFullPath != null) {
            final File car = new File(moduleUnitFullPath);
            // Then let's look inside the car
            try (ZipFile zip = new ZipFile(car)) {
                ZipEntry e = zip.getEntry(fullPath);
                if (e != null && !e.isDirectory()) {
                    return new ZipResource(car, fullPath);
                }
            } catch (IOException ex) {
                throw new ceylon.language.Exception(new ceylon.language.String(
                        "Searching for resource " + path), ex);
            }

            // And finally as a fall-back let's look in the module's resource dir...
            final File target = new File(new File(car.getParentFile(), "module-resources"), fullPath);
            if (target.exists() && target.isFile() && target.canRead()) {
                return new FileResource(target);
            }
        }
        //One last shot: we might be in a fat jar
        try (InputStream stream = getClass().getClassLoader().getResourceAsStream(fullPath)) {
            if (stream != null) {
                byte[] buf = new byte[16384];
                ByteArrayOutputStream bout = new ByteArrayOutputStream();
                int bytesRead = stream.read(buf);
                while (bytesRead > 0) {
                    bout.write(buf,0,bytesRead);
                    bytesRead = stream.read(buf);
                }
                return new ByteArrayResource(bout.toByteArray(), new URI("classpath:" + fullPath));
            }
        } catch (IOException | URISyntaxException ex) {
            throw new ceylon.language.Exception(new ceylon.language.String(
                    "Searching for resource " + path), ex);
        }
        
        return null;
    }

    @Override
    @TypeInfo("ceylon.language::String")
    public String getName() {
        return declaration.getNameAsString();
    }

    @Override
    public String getQualifiedName() {
        return getName();
    }

    @Override
    @TypeInfo("ceylon.language::String")
    public String getVersion() {
        return declaration.getVersion();
    }
    
    @Override 
    @TypeInfo("{Service*}")
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public <Service> ceylon.language.Iterable<? extends Service, ? extends Object> 
            findServiceProviders(
                    final TypeDescriptor $reified$Service, 
                    ClassOrInterface<? extends Service> service) {
        // TODO Don't we have something to wrap java iterables in the language module?
        class It extends BaseIterable<Service,Object> {
            private static final long serialVersionUID = -5781842837386813638L;
            private final ServiceLoader<Service> sl;
            public It(ServiceLoader<Service> sl) {
                super($reified$Service, Null.$TypeDescriptor$);
                this.sl = sl;
            }
            public ceylon.language.Iterator<? extends Service> iterator() {
                return new BaseIterator<Service>($reified$Service) {
                    private static final long serialVersionUID = 6837239340643106746L;
                    java.util.Iterator<Service> it = sl.iterator();
                    @Override
                    public Object next() {
                        if (it.hasNext()) {
                            return it.next();
                        }
                        return finished_.get_();
                    }
                };
            }
        }
        
        java.lang.Class<Service> klass = (java.lang.Class)Metamodel.getJavaClass(((ClassOrInterfaceDeclarationImpl)service.getDeclaration()).declaration);
        ClassLoader classLoader = Metamodel.getClassLoader(this.declaration);
        if (classLoader==null) {
            return (Iterable<? extends Service, ? extends Object>) empty_.get_();
        }
        ServiceLoader<Service> moduleServices = ServiceLoader.<Service>load(klass, classLoader);
        ServiceLoader<Service> extensionServices = ServiceLoader.loadInstalled(klass);
        ceylon.language.Iterable<? extends Service,? extends Object> services = new It(moduleServices).<Service,Object>chain($reified$Service, Null.$TypeDescriptor$, new It(extensionServices));
        // now have to filter the services to return only those which satisfy 
        // the service type (reified) not merely the java.lang.Class
        return services.filter(new AbstractCallable<ceylon.language.Boolean>(ceylon.language.Boolean.$TypeDescriptor$, 
                TypeDescriptor.klass(Tuple.class, $reified$Service, $reified$Service, Empty.$TypeDescriptor$), 
                "Boolean(Service)", (short)-1) {
            private static final long serialVersionUID = 2096737170221762201L;
            public ceylon.language.Boolean $call$(java.lang.Object arg0) {
                Service service = (Service)arg0;
                return ceylon.language.Boolean.instance(
                        type_.type(null, service).subtypeOf(
                                Metamodel.getAppliedMetamodel($reified$Service)));
            }
        });
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 37 * result + getName().hashCode();
        String version = getVersion();
        result = 37 * result + (version == null ? 0 : version.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(Object obj) {
        if(obj == null)
            return false;
        if(obj == this)
            return true;
        if(obj instanceof ceylon.language.meta.declaration.Module == false)
            return false;
        ceylon.language.meta.declaration.Module other = (ceylon.language.meta.declaration.Module) obj;
        if(!Util.eq(other.getVersion(), getVersion()))
            return false;
        return getName().equals(other.getName());
    }

    @Override
    public String toString() {
        return "module " + getName() + "/" + getVersion();
    }

    @Ignore
    @Override
    public TypeDescriptor $getType$() {
        return $TypeDescriptor$;
    }
}
