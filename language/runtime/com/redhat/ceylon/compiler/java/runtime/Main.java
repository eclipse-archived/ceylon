package com.redhat.ceylon.compiler.java.runtime;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.jboss.jandex.AnnotationInstance;
import org.jboss.jandex.AnnotationValue;
import org.jboss.jandex.ClassInfo;
import org.jboss.jandex.DotName;
import org.jboss.jandex.Indexer;

import com.redhat.ceylon.cmr.api.ArtifactResult;
import com.redhat.ceylon.cmr.api.ArtifactResultType;
import com.redhat.ceylon.cmr.api.DependencyResolver;
import com.redhat.ceylon.cmr.api.ModuleInfo;
import com.redhat.ceylon.cmr.api.RepositoryException;
import com.redhat.ceylon.cmr.impl.AbstractArtifactResult;
import com.redhat.ceylon.cmr.impl.Configuration;
import com.redhat.ceylon.cmr.impl.OSGiDependencyResolver;
import com.redhat.ceylon.cmr.impl.PropertiesDependencyResolver;
import com.redhat.ceylon.cmr.impl.XmlDependencyResolver;
import com.redhat.ceylon.common.Versions;
import com.redhat.ceylon.common.tools.ModuleSpec;
import com.redhat.ceylon.common.tools.ModuleSpec.Option;
import com.redhat.ceylon.compiler.java.runtime.metamodel.Metamodel;
import com.redhat.ceylon.compiler.java.tools.JarEntryManifestFileObject.OsgiManifest;

/**
 * <p>
 * Main entry point when you want to initialise the Ceylon system when you are not running
 * JBoss modules and have all the relevant Ceylon libraries in your JVM classpath. This
 * will allow you to initialize the metamodel with {@link Main#setupMetamodel()}, which you 
 * only need to do once before you execute code from a Ceylon module. Once initialized, the 
 * metamodel will remain useable until you reset it, from every thread and user of the system 
 * ClassLoader. You can repeatedly call {@link Main#setupMetamodel()} to add new Ceylon modules 
 * to the metamodel.
 * </p>
 * <p>
 * If you need to reset the metamodel you can call {@link Main#resetMetamodel()}, but again beware
 * that the metamodel is static to the system ClassLoader, so it may be shared by other threads,
 * and any Ceylon code that continues to run after you reset it will likely fail at some point.
 * </p>
 * <p>
 * You can also directly run Ceylon programs by using {@link Main#runModule(String, String, Class, String...)}
 * or {@link Main#runModule(String, String, String, String...)}, which will first initialize the
 * metamodel, then run the given Java Class’s main method (which sets up the <code>process.arguments</code>
 * and executes the given toplevel Ceylon class or method.
 * </p>
 * 
 * <p>
 * <b>WARNING:</b> this API is not supported and will be superseded by an official API later.
 * </p>
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
public class Main {
    
    static class ClassPath {
        
        @SuppressWarnings("serial")
        public class ModuleNotFoundException extends Exception {

            public ModuleNotFoundException(String string) {
                super(string);
            }

        }

        private enum Type {
            CEYLON, JBOSS_MODULES, MAVEN, OSGi, UNKNOWN;
        }
        
        static class Dependency extends AbstractArtifactResult {
            public final boolean optional, shared;

            public Dependency(String name, String version, boolean optional, boolean shared) {
                super(name, version);
                this.optional = optional;
                this.shared = shared;
            }

            @Override
            public String toString() {
                StringBuilder b = new StringBuilder();
                b.append("Import{ name = ").append(name());
                b.append(", version = ").append(version());
                b.append(", optional = ").append(optional);
                b.append(", shared = ").append(shared);
                b.append(" }");
                return b.toString();
            }

            @Override
            public boolean equals(Object obj) {
                if(obj == null)
                    return false;
                if(obj == this)
                    return true;
                if(obj instanceof Dependency == false)
                    return false;
                Dependency other = (Dependency) obj;
                return Objects.equals(name(), other.name())
                        && Objects.equals(version(), other.version())
                        && optional == other.optional
                        && shared == other.shared;
            }

            @Override
            public int hashCode() {
                int ret = 17;
                ret = (ret * 23) + (name() != null ? name().hashCode() : 0);
                ret = (ret * 23) + (version() != null ? version().hashCode() : 0);
                ret = (ret * 23) + (optional ? 1 : 0);
                ret = (ret * 23) + (shared ? 1 : 0);
                return ret;
            }
            
            @Override
            public ArtifactResultType type() {
                throw new UnsupportedOperationException();
            }

            @Override
            public List<ArtifactResult> dependencies() throws RepositoryException {
                throw new UnsupportedOperationException();
            }

            @Override
            public String repositoryDisplayString() {
                throw new UnsupportedOperationException();
            }

            @Override
            protected File artifactInternal() {
                throw new UnsupportedOperationException();
            }
        }
        
        static class Module extends AbstractArtifactResult {
            public final File jar;
            public final Type type;
            public final List<Dependency> dependencies = new LinkedList<Dependency>();

            public Module(String name, String version, Type type, File jar) {
                super(name, version);
                this.type = type;
                this.jar = jar;
            }
            
            public void addDependency(String name, String version, boolean optional, boolean shared) {
                dependencies.add(new Dependency(name, version, optional, shared));
            }
            
            @Override
            public int hashCode() {
                int ret = 31;
                ret = 37 * ret + name().hashCode();
                ret = 37 * ret + (version() != null ? version().hashCode() : 0);
                return ret;
            }
            
            @Override
            public boolean equals(Object obj) {
                if(obj == null)
                    return false;
                if(obj == this)
                    return true;
                if(obj instanceof Module == false)
                    return false;
                Module other = (Module) obj;
                return name().equals(other.name())
                        && Objects.equals(version(), other.version());
            }
            
            @Override
            public String toString() {
                StringBuilder b = new StringBuilder();
                b.append("Module{ name = ").append(name());
                b.append(", version = ").append(version());
                b.append(", jar = ").append(jar);
                b.append(", type = ").append(type);
                b.append(", dependencies = [");
                boolean once = true;
                for(Dependency dep : dependencies){
                    if(once)
                        once = false;
                    else
                        b.append(", ");
                    b.append(dep);
                }
                b.append(" ] }");
                return b.toString();
            }

            @Override
            public ArtifactResultType type() {
                switch(type){
                case CEYLON:
                case JBOSS_MODULES:
                case OSGi:
                    return ArtifactResultType.CEYLON;
                case MAVEN:
                    return ArtifactResultType.MAVEN;
                case UNKNOWN:
                default:
                    return ArtifactResultType.OTHER;
                }
            }

            @SuppressWarnings({ "rawtypes", "unchecked" })
            @Override
            public List<ArtifactResult> dependencies() throws RepositoryException {
                return (List)dependencies;
            }

            @Override
            public String repositoryDisplayString() {
                return name()+"/"+version();
            }

            @Override
            protected File artifactInternal() {
                return jar;
            }
        }

        private List<File> potentialJars = new LinkedList<File>();
        private Map<String,Module> modules = new HashMap<String,Module>();
        private static DependencyResolver MavenResolver = getResolver(Configuration.MAVEN_RESOLVER_CLASS);
        
        ClassPath(){
            String classPath = System.getProperty("java.class.path");
            String[] classPathEntries = classPath.split(File.pathSeparator);
            for(String classPathEntry : classPathEntries){
                File entry = new File(classPathEntry);
                if(entry.isFile()){
                    potentialJars.add(entry);
                }
            }
        }
        
        // for tests
        ClassPath(List<File> potentialJars){
            this.potentialJars = potentialJars;
        }
        
        private static DependencyResolver getResolver(String className) {
            try {
                ClassLoader cl = Configuration.class.getClassLoader();
                return (DependencyResolver) cl.loadClass(className).newInstance();
            } catch (Throwable t) {
                return null;
            }
        }

        public Module loadModule(String name, String version) throws ModuleNotFoundException{
            String key = name + "/" + version;
            Module module = modules.get(key);
            if(module != null)
                return module;
            Iterator<File> iterator = potentialJars.iterator();
            while(iterator.hasNext()){
                File file = iterator.next();
                try {
                    module = loadJar(file, name, version);
                } catch (IOException e) {
                    // faulty jar
                    iterator.remove();
                    e.printStackTrace();
                    System.err.println("Non-zip jar file in classpath: "+file+". Skipping it next time.");
                    continue;
                }
                if(module != null){
                    modules.put(key, module);
                    iterator.remove();
                    return module;
                }
            }
            throw new ModuleNotFoundException("Module "+key+" not found");
        }
        
        private Module loadJar(File file, String name, String version) throws IOException {
            ZipFile zipFile = new ZipFile(file);
            try{
                // Modules that have a : MUST be Maven modules
                int mavenSeparator = name.indexOf(":");
                if(mavenSeparator != -1){
                    String groupId = name.substring(0, mavenSeparator);
                    String artifactId = name.substring(mavenSeparator+1);
                    String descriptorPath = String.format("META-INF/maven/%s/%s/pom.xml", groupId, artifactId);
                    ZipEntry mavenDescriptor = zipFile.getEntry(descriptorPath);
                    if(mavenDescriptor != null){
                        return loadMavenJar(file, zipFile, mavenDescriptor, name, version);
                    }
                }
                
                // Try Ceylon module first
                String ceylonPath = name.replace('.', '/');
                ZipEntry moduleDescriptor = zipFile.getEntry(ceylonPath+"/module_.class");
                if(moduleDescriptor != null)
                    return loadCeylonModuleCar(file, zipFile, moduleDescriptor, name, version);
                
                // Special case for Ceylon default module
                if(name.equals(com.redhat.ceylon.compiler.typechecker.model.Module.DEFAULT_MODULE_NAME)
                        && version == null
                        && file.getName().equalsIgnoreCase("default.car"))
                    return new Module(name, null, Type.CEYLON, file);
                
                // JBoss modules next
                ZipEntry moduleXml = zipFile.getEntry("META-INF/jbossmodules/"+ceylonPath+"/"+version+"/module.xml");
                if(moduleXml != null)
                    return loadJBossModuleXmlJar(file, zipFile, moduleXml, name, version);
                ZipEntry moduleProperties = zipFile.getEntry("META-INF/jbossmodules/"+ceylonPath+"/"+version+"/module.properties");
                if(moduleProperties != null)
                    return loadJBossModulePropertiesJar(file, zipFile, moduleProperties, name, version);
                
                // try other combinations for Maven
                if(MavenResolver != null){
                    // the case with : has already been taken care of first
                    int lastDot = name.lastIndexOf('.');
                    while(lastDot != -1){
                        String groupId = name.substring(0, lastDot);
                        String artifactId = name.substring(lastDot+1);
                        String descriptorPath = String.format("META-INF/maven/%s/%s/pom.xml", groupId, artifactId);
                        ZipEntry mavenDescriptor = zipFile.getEntry(descriptorPath);
                        if(mavenDescriptor != null){
                            return loadMavenJar(file, zipFile, mavenDescriptor, name, version);
                        }
                        lastDot = name.lastIndexOf('.', lastDot - 1);
                    }
                }
                
                // last OSGi
                ZipEntry osgiProperties = zipFile.getEntry(JarFile.MANIFEST_NAME);
                if(osgiProperties != null){
                    Module module = loadOsgiJar(file, zipFile, osgiProperties, name, version);
                    // it's possible we have a MANIFEST but not for the module we're looking for
                    if(module != null)
                        return module;
                }
                
                // not found
                return null;
            }finally{
                zipFile.close();
            }
        }

        private Module loadCeylonModuleCar(File file, ZipFile zipFile, ZipEntry moduleDescriptor, String name, String version) throws IOException {
            InputStream inputStream = zipFile.getInputStream(moduleDescriptor);
            try{
                Indexer indexer = new Indexer();
                ClassInfo classInfo = indexer.index(inputStream);
                if(classInfo == null)
                    throw new IOException("Failed to read class info");
                
                Map<DotName, List<AnnotationInstance>> annotations = classInfo.annotations();
                DotName moduleAnnotationName = DotName.createSimple(com.redhat.ceylon.compiler.java.metadata.Module.class.getName());
                List<AnnotationInstance> moduleAnnotations = annotations.get(moduleAnnotationName);
                if(moduleAnnotations == null || moduleAnnotations.size() != 1)
                    throw new IOException("Missing module annotation: "+annotations);
                AnnotationInstance moduleAnnotation = moduleAnnotations.get(0);
                
                AnnotationValue moduleName = moduleAnnotation.value("name");
                AnnotationValue moduleVersion = moduleAnnotation.value("version");
                if(moduleName == null || moduleVersion == null)
                    throw new IOException("Invalid module annotation");
                if(!moduleName.asString().equals(name))
                    throw new IOException("Module name does not match module descriptor");
                if(!moduleVersion.asString().equals(version))
                    throw new IOException("Module version does not match module descriptor");
                
                Module module = new Module(name, version, Type.CEYLON, file);
                AnnotationValue moduleDependencies = moduleAnnotation.value("dependencies");
                if(moduleDependencies != null){
                    for(AnnotationInstance dependency : moduleDependencies.asNestedArray()){
                        AnnotationValue importName = dependency.value("name");
                        AnnotationValue importVersion = dependency.value("version");
                        AnnotationValue importOptional = dependency.value("optional");
                        AnnotationValue importExport = dependency.value("export");
                        if(importName == null || importVersion == null)
                            throw new IOException("Invalid module import");
                        boolean export = importExport != null ? importExport.asBoolean() : false;
                        boolean optional = importOptional != null ? importOptional.asBoolean() : false;
                        module.addDependency(importName.asString(), importVersion.asString(), optional, export);
                    }
                }
                return module;
            }finally{
                inputStream.close();
            }
        }

        private Module loadJBossModulePropertiesJar(File file, ZipFile zipFile, ZipEntry moduleProperties, String name, String version) throws IOException {
            return loadJBossModuleJar(file, zipFile, moduleProperties, PropertiesDependencyResolver.INSTANCE, name, version);
        }

        private Module loadJBossModuleJar(File file, ZipFile zipFile, ZipEntry moduleDescriptor, 
                DependencyResolver dependencyResolver, String name, String version) throws IOException {
            return loadFromResolver(file, zipFile, moduleDescriptor, dependencyResolver, name, version, Type.JBOSS_MODULES);
        }
        
        private Module loadFromResolver(File file, ZipFile zipFile, ZipEntry moduleDescriptor, 
                                        DependencyResolver dependencyResolver, String name, String version,
                                        Type moduleType) throws IOException {
            InputStream inputStream = zipFile.getInputStream(moduleDescriptor);
            try{
                Set<ModuleInfo> moduleDependencies = dependencyResolver.resolveFromInputStream(inputStream);
                Module module = new Module(name, version, moduleType, file);
                for(ModuleInfo dep : moduleDependencies){
                    module.addDependency(dep.getName(), dep.getVersion(), dep.isOptional(), dep.isExport());
                }
                return module;
            }finally{
                inputStream.close();
            }
        }

        private Module loadJBossModuleXmlJar(File file, ZipFile zipFile, ZipEntry moduleXml, String name, String version) throws IOException {
            return loadJBossModuleJar(file, zipFile, moduleXml, XmlDependencyResolver.INSTANCE, name, version);
        }

        private Module loadMavenJar(File file, ZipFile zipFile, ZipEntry moduleDescriptor, String name, String version) throws IOException {
            return loadFromResolver(file, zipFile, moduleDescriptor, MavenResolver, name, version, Type.MAVEN);
        }

        private Module loadOsgiJar(File file, ZipFile zipFile, ZipEntry moduleDescriptor, String name, String version) throws IOException {
            // first verify that it is indeed for the module we're looking for
            InputStream inputStream = zipFile.getInputStream(moduleDescriptor);
            try{
                Manifest manifest = new Manifest(inputStream);
                Attributes attributes = manifest.getMainAttributes();
                if(!Objects.equals(name, attributes.getValue(OsgiManifest.Bundle_SymbolicName))
                        || !Objects.equals(version, attributes.getValue(OsgiManifest.Bundle_Version)))
                    return null;
                
            }finally{
                inputStream.close();
            }
            return loadFromResolver(file, zipFile, moduleDescriptor, OSGiDependencyResolver.INSTANCE, name, version, Type.OSGi);
        }
    }
    
    /**
     * Sets up the metamodel for the specified module and execute the <code>main</code> method on the
     * specified Java class name representing a toplevel Ceylon class or method.
     * 
     * @param module the module name to initialise in the metamodel
     * @param version the module version
     * @param runClass the Java class name representing a toplevel Ceylon class or method
     * @param arguments the arguments to pass to the Ceylon program
     * 
     * @throws RuntimeException if anything wrong happens
     */
    public static void runModule(String module, String version, String runClass, String... arguments){
        setupMetamodel(module, version);
        try {
            Class<?> klass = ClassLoader.getSystemClassLoader().loadClass(runClass);
            invokeMain(klass, arguments);
        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException 
                 | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sets up the metamodel for the specified module and execute the <code>main</code> method on the
     * specified Java class representing a toplevel Ceylon class or method.
     * 
     * @param module the module name to initialise in the metamodel
     * @param version the module version
     * @param runClass the Java class representing a toplevel Ceylon class or method
     * @param arguments the arguments to pass to the Ceylon program
     * 
     * @throws RuntimeException if anything wrong happens
     */
    public static void runModule(String module, String version, Class<?> runClass, String... arguments){
        setupMetamodel(module, version);
        try {
            invokeMain(runClass, arguments);
        } catch (NoSuchMethodException | SecurityException 
                 | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private static void invokeMain(Class<?> klass, String[] arguments) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Method main = klass.getMethod("main", String[].class);
        main.invoke(null, (Object)arguments);
    }

    /**
     * Sets up the Ceylon metamodel by adding the specified module to it. This does not run any Ceylon code,
     * nor does it reset the metamodel first. You can repeatedly invoke this method to add new Ceylon modules
     * to the metamodel.
     * 
     * @param module the module name to load.
     * @param version the version to load. Ignored if the module is the default module.
     */
    public static void setupMetamodel(String module, String version){
        ClassPath classPath = new ClassPath();
        Set<ClassPath.Module> visited = new HashSet<ClassPath.Module>();
        registerInMetamodel(classPath, "ceylon.language", Versions.CEYLON_VERSION_NUMBER, visited, false);
        registerInMetamodel(classPath, "com.redhat.ceylon.typechecker", Versions.CEYLON_VERSION_NUMBER, visited, false);
        registerInMetamodel(classPath, "com.redhat.ceylon.common", Versions.CEYLON_VERSION_NUMBER, visited, false);
        registerInMetamodel(classPath, "com.redhat.ceylon.module-resolver", Versions.CEYLON_VERSION_NUMBER, visited, false);
        registerInMetamodel(classPath, "com.redhat.ceylon.compiler.java", Versions.CEYLON_VERSION_NUMBER, visited, false);
        if(module.equals(com.redhat.ceylon.compiler.typechecker.model.Module.DEFAULT_MODULE_NAME))
            version = null;
        registerInMetamodel(classPath, module, version, visited, false);
    }

    /**
     * Resets the metamodel. This will impact any Ceylon code running on the same ClassLoader, across
     * threads, and will crash them if they are not done running.
     */
    public static void resetMetamodel(){
        Metamodel.resetModuleManager();
    }
    
    private static void registerInMetamodel(ClassPath classPath, String name, String version, Set<ClassPath.Module> visited, boolean optional) {
        ClassPath.Module module;
        try {
            module = classPath.loadModule(name, version);
        } catch (com.redhat.ceylon.compiler.java.runtime.Main.ClassPath.ModuleNotFoundException e) {
            if(optional)
                return;
            throw new RuntimeException(e);
        }
        if(!visited.add(module))
            return;
        Metamodel.loadModule(name, version, module, ClassLoader.getSystemClassLoader());
        // also register its dependencies
        for(ClassPath.Dependency dep : module.dependencies)
            registerInMetamodel(classPath, dep.name(), dep.version(), visited, dep.optional);
    }

    /**
     * <p>
     * Main entry point, invoke with: <code>moduleSpec</code> <code>mainJavaClassName</code> <code>args*</code>.
     * </p>
     * <p>
     * <b>WARNING:</b> this code will call @{link {@link System#exit(int)} if the arguments
     * are incorrect or missing. This is really only intended to be called from the <code>java</code>
     * command-line. All it does is parse the arguments and invoke 
     * @{link {@link Main#runModule(String, String, String, String...)}.
     * </p>
     */
    public static void main(String[] args) {
        if(args.length < 2){
            System.err.println("Invalid arguments.");
            System.err.println("Usage: \n");
            System.err.println(Main.class.getName()+" moduleSpec mainJavaClassName args*");
            System.exit(1);
        }
        ModuleSpec moduleSpec = ModuleSpec.parse(args[0], Option.VERSION_REQUIRED);
        String version;
        if(moduleSpec.getName().equals(com.redhat.ceylon.compiler.typechecker.model.Module.DEFAULT_MODULE_NAME))
            version = null;
        else
            version = moduleSpec.getVersion();
        runModule(moduleSpec.getName(), version, args[1]);
    }
}
