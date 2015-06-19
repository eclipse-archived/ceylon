package com.redhat.ceylon.compiler.java.runtime.metamodel;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.AnnotatedElement;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import ceylon.language.Resource;
import ceylon.language.Sequential;
import ceylon.language.meta.declaration.Import;
import ceylon.language.meta.declaration.Package;

import com.redhat.ceylon.compiler.java.Util;
import com.redhat.ceylon.compiler.java.language.ByteArrayResource;
import com.redhat.ceylon.compiler.java.language.FileResource;
import com.redhat.ceylon.compiler.java.language.ObjectArrayIterable;
import com.redhat.ceylon.compiler.java.language.ZipResource;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.runtime.model.ReifiedType;
import com.redhat.ceylon.compiler.java.runtime.model.RuntimeModelLoader;
import com.redhat.ceylon.compiler.java.runtime.model.RuntimeModuleManager;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

public class FreeModule implements ceylon.language.meta.declaration.Module,
        AnnotationBearing,
        ReifiedType {

    @Ignore
    public static final TypeDescriptor $TypeDescriptor$ = TypeDescriptor.klass(FreeModule.class);
    private static final java.lang.annotation.Annotation[] NO_ANNOTATION = new java.lang.annotation.Annotation[0];
    protected com.redhat.ceylon.model.typechecker.model.Module declaration;
    private Sequential<? extends Package> packages;
    private Sequential<? extends Import> dependencies;
    
    public FreeModule(com.redhat.ceylon.model.typechecker.model.Module declaration) {
        this.declaration = declaration;
    }

    @Override
    @Ignore
    public java.lang.annotation.Annotation[] $getJavaAnnotations$() {
        if(declaration.isDefault() || declaration.isJava())
            return NO_ANNOTATION;
        return Metamodel.getJavaClass(declaration).getAnnotations();
    }
    
    @Override
    @Ignore
    public boolean $isAnnotated$(java.lang.Class<? extends java.lang.annotation.Annotation> annotationType) {
        if(declaration.isDefault() || declaration.isJava())
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
            List<com.redhat.ceylon.model.typechecker.model.Package> modelPackages = declaration.getPackages();
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
        com.redhat.ceylon.model.typechecker.model.Package pkg = declaration.getDirectPackage(name);
        return pkg == null ? null : Metamodel.getOrCreateMetamodel(pkg);
    }

    @Override
    @TypeInfo("ceylon.language::Null|ceylon.language.meta.declaration::Package")
    public Package findImportedPackage(@Name("name") String name) {
        com.redhat.ceylon.model.typechecker.model.Package pkg = declaration.getPackage(name);
        return pkg == null ? null : Metamodel.getOrCreateMetamodel(pkg);
    }

    @Override
    @TypeInfo("ceylon.language::Sequential<ceylon.language.meta.declaration::Import>")
    public Sequential<? extends Import> getDependencies() {
        // no need to synchronise as concurrent invocations should get the same array back
        if(this.dependencies == null){
            List<com.redhat.ceylon.model.typechecker.model.ModuleImport> modelImports = declaration.getImports();
            //FreeImport[] imports = new FreeImport[modelImports.size()];
            ArrayList<FreeImport> sb = new ArrayList<FreeImport>(modelImports.size());
            for(com.redhat.ceylon.model.typechecker.model.ModuleImport moduleImport : modelImports){
                sb.add(new FreeImport(this, moduleImport));
            }
            FreeImport[] array = sb.toArray(new FreeImport[0]);
    		this.dependencies = new ObjectArrayIterable<FreeImport>(Import.$TypeDescriptor$, array).sequence();
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
