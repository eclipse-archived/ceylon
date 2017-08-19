package com.redhat.ceylon.compiler.java.runtime;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Enumeration;
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

import com.redhat.ceylon.cmr.api.ArtifactContext;
import com.redhat.ceylon.cmr.api.ArtifactOverrides;
import com.redhat.ceylon.cmr.api.DependencyOverride;
import com.redhat.ceylon.cmr.api.DependencyResolver;
import com.redhat.ceylon.cmr.api.ModuleDependencyInfo;
import com.redhat.ceylon.cmr.api.ModuleInfo;
import com.redhat.ceylon.cmr.api.Overrides;
import com.redhat.ceylon.cmr.api.OverridesRuntimeResolver;
import com.redhat.ceylon.cmr.api.PathFilterParser;
import com.redhat.ceylon.cmr.impl.AbstractArtifactResult;
import com.redhat.ceylon.cmr.impl.BytecodeUtils;
import com.redhat.ceylon.cmr.impl.Configuration;
import com.redhat.ceylon.cmr.impl.MavenRepository;
import com.redhat.ceylon.cmr.impl.OSGiDependencyResolver;
import com.redhat.ceylon.cmr.impl.PropertiesDependencyResolver;
import com.redhat.ceylon.cmr.impl.XmlDependencyResolver;
import com.redhat.ceylon.cmr.maven.MavenBackupDependencyResolver;
import com.redhat.ceylon.common.Java9ModuleUtil;
import com.redhat.ceylon.common.ModuleSpec;
import com.redhat.ceylon.common.ModuleSpec.Option;
import com.redhat.ceylon.common.ModuleUtil;
import com.redhat.ceylon.common.Versions;
import com.redhat.ceylon.compiler.java.runtime.metamodel.Metamodel;
import com.redhat.ceylon.langtools.classfile.Annotation;
import com.redhat.ceylon.langtools.classfile.Attribute;
import com.redhat.ceylon.langtools.classfile.ClassFile;
import com.redhat.ceylon.langtools.classfile.ConstantPoolException;
import com.redhat.ceylon.langtools.classfile.RuntimeAnnotations_attribute;
import com.redhat.ceylon.model.cmr.ArtifactResult;
import com.redhat.ceylon.model.cmr.ArtifactResultType;
import com.redhat.ceylon.model.cmr.JDKUtils;
import com.redhat.ceylon.model.cmr.JDKUtils.JDK;
import com.redhat.ceylon.model.cmr.ModuleScope;
import com.redhat.ceylon.model.cmr.PathFilter;
import com.redhat.ceylon.model.cmr.RepositoryException;
import com.redhat.ceylon.model.loader.ClassFileUtil;
import com.redhat.ceylon.model.loader.Java9ModuleReader;
import com.redhat.ceylon.model.loader.Java9ModuleReader.Java9Module;
import com.redhat.ceylon.model.loader.NamingBase;
import com.redhat.ceylon.model.loader.OsgiUtil;

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
    private boolean allowMissingModules;
    private boolean allowMissingSystem;
    private String overrides;
    
    private ClassPath classPath;
    private Set<ClassPath.Module> visited;
	private ClassLoader moduleClassLoader;
    
    public static Main instance() {
        return new Main();
    }
    
    private Main() {
    }
    
    /**
     * When enabled no exceptions will be thrown for not encountering
     * proper module dependency information for the module to be loaded
     * or any of its dependencies. The system modules still need to be
     * present and have proper dependency information.
     * @param allowMissingModules If true no exceptions will be thrown
     * for missing module dependency information
     * @return This object for chaining
     */
    public Main allowMissingModules(boolean allowMissingModules) {
        this.allowMissingModules = allowMissingModules;
        return this;
    }

    /**
     * Sets the overrides file to use for module overrides
     * @param overrides a file path to the module overrides
     * @return This object for chaining
     */
    public Main overrides(String overrides) {
        this.overrides = overrides;
        return this;
    }

    /**
     * When enabled no exceptions will be thrown for not encountering
     * proper module dependency information for the system modules or
     * any of their dependencies.
     * @param allowMissingModules If true no exceptions will be thrown
     * for missing module dependency information
     * @return This object for chaining
     */
    public Main allowMissingSystem(boolean allowMissingSystem) {
        this.allowMissingSystem = allowMissingSystem;
        return this;
    }
    
    static class ClassPath {
        
        private static final String METAINF_JBOSSMODULES = "META-INF/jbossmodules/";
        private static final String METAINF_MAVEN = "META-INF/maven/";
        private static final String MODULE_PROPERTIES = "module.properties";
        private static final String MODULE_XML = "module.xml";
        private static final String POM_XML = "pom.xml";
        private static final String JAVA9_MODULE = "module-info.class";

        @SuppressWarnings("serial")
        public class ModuleNotFoundException extends Exception {

            public ModuleNotFoundException(String string) {
                super(string);
            }

        }

        private enum Type {
            CEYLON, JBOSS_MODULES, MAVEN, OSGi, UNKNOWN, JDK, JAVA9;
        }
        
        static class Dependency extends AbstractArtifactResult {
            public final boolean optional, shared;

            public Dependency(String name, String version, boolean optional, boolean shared) {
                super(null, null, name, version);
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
            
            @Override
            public String groupId() {
                return null;
            }
            
            @Override
            public String artifactId() {
                return null;
            }
        }
        
        static class Module extends AbstractArtifactResult {
            public final File jar;
            public final Type type;
            public final List<Dependency> dependencies = new LinkedList<Dependency>();
            private String groupId;
            private String artifactId;

            public Module(String name, String version, String groupId, String artifactId, Type type, File jar) {
                super(null, null, name, version);
                this.type = type;
                this.jar = jar;
                this.groupId = groupId;
                this.artifactId = artifactId;
            }
            
            public void addDependency(String name, String version, boolean optional, boolean shared) {
                dependencies.add(new Dependency(name, version, optional, shared));
            }
            
            @Override
            public String namespace() {
                return null;
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
                case JDK:
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

            public void setFilter(PathFilter filter) {
                setFilterInternal(filter);
            }

            @Override
            public String groupId() {
                return groupId;
            }

            @Override
            public String artifactId() {
                return artifactId;
            }
        }

        private List<File> potentialJars = new LinkedList<File>();
        private Map<String,Module> modules = new HashMap<String,Module>();
        private Overrides overrides;
        private static DependencyResolver MavenResolver = Configuration.getMavenResolver();
        static{
            if(MavenResolver == null)
                MavenResolver = MavenBackupDependencyResolver.INSTANCE;
        }
        
        private static final Module NO_MODULE = new Module("$$$", "$$$", null, null, Type.UNKNOWN, null);
        
        ClassPath(Overrides overrides){
            String classPath = System.getProperty("java.class.path");
            String[] classPathEntries = classPath.split(File.pathSeparator);
            for(String classPathEntry : classPathEntries){
                File entry = new File(classPathEntry);
                if(entry.isFile()){
                    potentialJars.add(entry);
                }
            }
            if(JDKUtils.jdk.providesVersion(JDK.JDK9.version)){
                String modulePath = System.getProperty("jdk.module.path");
                if(modulePath != null){
                	String[] modulePathEntries = modulePath.split(File.pathSeparator);
                	for(String moduleFolder : modulePathEntries){
                		File folder = new File(moduleFolder);
                		if(folder.isDirectory()){
                			scanFolderForJars(folder);
                		}
                	}
                }

            }
            this.overrides = overrides;
            initJars();
        }
        
        private void scanFolderForJars(File folder) {
            	for(File file : folder.listFiles()){
            		if(file.isFile()) {
                    String name = file.getName();
                    if (name.endsWith(".car")
                		|| name.endsWith(".jar")
                		|| name.endsWith(".zip")) {
                        potentialJars.add(file);
                    }
            		}
            	}
		}

		// for tests
        ClassPath(List<File> potentialJars){
            this.potentialJars = potentialJars;
            initJars();
        }
        
        private List<ZipEntry> findEntries(ZipFile zipFile, String startFolder, String entryName) {
            List<ZipEntry> result = new LinkedList<ZipEntry>();
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                String name = entry.getName();
                if (name.startsWith(startFolder) && name.endsWith(entryName)) {
                    result.add(entry);
                }
            }
            return result;
        }

        private ModuleSpec moduleFromEntry(ZipEntry entry) {
            String fullName = entry.getName();
            if (fullName.startsWith(METAINF_JBOSSMODULES)) {
                fullName = fullName.substring(METAINF_JBOSSMODULES.length());
            }
            if (fullName.endsWith(MODULE_PROPERTIES)) {
                fullName = fullName.substring(0, fullName.length() - MODULE_PROPERTIES.length() - 1);
            } else if (fullName.endsWith(MODULE_XML)) {
                fullName = fullName.substring(0, fullName.length() - MODULE_XML.length() - 1);
            }
            int p = fullName.lastIndexOf('/');
            if (p > 0) {
                String name = fullName.substring(0, p);
                String version = fullName.substring(p + 1);
                if (!name.isEmpty() && !version.isEmpty()) {
                    name = name.replace('/', '.');
                    return new ModuleSpec(null, name, version);
                }
            }
            return null;
        }

        private ModuleSpec moduleFromCeylonRepoFile(File jar) {
            // if it's in a ceylon repo, it's com/foo/1.2/com.foo-1.2.jar
            File parent = jar.getParentFile();
            if(parent == null)
                return null;
            String version = parent.getName();
            String jarName = jar.getName();
            String suffix = "-"+version+".jar";
            if(!jarName.endsWith(suffix))
                return null;
            String modName = jarName.substring(0, jarName.length()-suffix.length());
            return new ModuleSpec(null, modName, version);
        }

        public Module loadModule(String name, String version) throws ModuleNotFoundException{
            return loadModule(name, version, false);
        }

        public Module loadModule(String name, String version, boolean allowMissingModules) throws ModuleNotFoundException{
            // classpath does not allow more than one version of a 
            // module, and Maven modules may be missing version info
            String key = name;
            Module module = modules.get(key);
            if(module != null)
                return module;
            if(JDKUtils.jdk.providesVersion(JDK.JDK9.version)){
            	    // unalias jdk7-8 module names if we're running on jdk9+
            	    name = JDKUtils.getJava9ModuleName(name, version);
            }
            if(JDKUtils.isJDKModule(name) || JDKUtils.isOracleJDKModule(name)){
                module = new Module(name, JDKUtils.jdk.version, null, null, Type.JDK, null);
                modules.put(key, module);
                return module;
            }
            module = searchJars(name, version);
            if (module != null) {
                return module;
            } else {
                if(allowMissingModules){
                    return new Module(name, version, null, null, Type.UNKNOWN, null);
                }else{
                    throw new ModuleNotFoundException("Module "+key+" not found");
                }
            }
        }
        
        // Pre-loads as much modules as possible by going over all potential jar/car files
        // in the class path and trying to determine which modules they contain
        private void initJars() {
            searchJars(null, null);
        }
        
        // Goes over all the potential jar/car files in the class path and if a name and
        // version were given tries to determine if any of them contain that module.
        // If name and version are both `null` it will try to determine the module
        // information from the jar/car file itself.
        private Module searchJars(String name, String version) {
            if(overrides != null){
                ArtifactContext ctx = new ArtifactContext(null, name, version);
                ArtifactContext replacement = overrides.replace(ctx);
                if(replacement != null){
                    name = replacement.getName();
                    version = replacement.getVersion();
                    ctx = replacement;
                }
                if(overrides.isVersionOverridden(ctx)){
                    version = overrides.getVersionOverride(ctx);
                }
            }
            Module module;
            Iterator<File> iterator = potentialJars.iterator();
            while(iterator.hasNext()){
                File file = iterator.next();
                try {
                    if (name == null && version == null) {
                        module = initJar(file);
                    } else {
                        module = loadJar(file, name, version);
                    }
                } catch (IOException e) {
                    // faulty jar
                    iterator.remove();
                    e.printStackTrace();
                    System.err.println("Non-zip jar file in classpath: "+file+". Skipping it next time.");
                    continue;
                }
                if(module != null){
                    if (module != NO_MODULE) {
                        // classpath does not allow more than one version of a module, 
                        // and Maven modules may be missing version info
                        String key = module.name();
//                        System.out.println("Found module: " + key + " -> " + 
//                        module.namespace() + ":" + module.groupId() + ":" + module.artifactId() + ":" + module.version());
                        modules.put(key, module);
                    }
                    iterator.remove();
                    if (name != null || version != null) {
                        return module;
                    }
                }
                else {
                    iterator.remove();
                    System.err.println("Could not determine module metadata for jar in classpath: "+file+". Skipping it next time.");
                }
            }
            return null;
        }
        
        private Module initJar(File file) throws IOException {
            ZipFile zipFile = new ZipFile(file);
            try{
                // Try Ceylon module first
                List<ZipEntry> moduleDescriptors = findEntries(zipFile, "", NamingBase.MODULE_DESCRIPTOR_CLASS_NAME+".class");
                if(moduleDescriptors.size() == 1) {
                    try {
                        return loadCeylonModuleCar(file, zipFile, moduleDescriptors.get(0), null, null);
                    } catch (IOException ex) {
                        // Ignore
                    }
                }
                
                // Try JBoss modules next
                List<ZipEntry> moduleXmls = findEntries(zipFile, METAINF_JBOSSMODULES, MODULE_XML);
                if(moduleXmls.size() == 1) {
                    ModuleSpec mod = moduleFromEntry(moduleXmls.get(0));
                    if (mod != null) {
                        return loadJBossModuleXmlJar(file, zipFile, moduleXmls.get(0), mod.getName(), mod.getVersion());
                    }
                }
                List<ZipEntry> moduleProperties = findEntries(zipFile, METAINF_JBOSSMODULES, MODULE_PROPERTIES);
                if(moduleProperties.size() == 1) {
                    ModuleSpec mod = moduleFromEntry(moduleProperties.get(0));
                    if (mod != null) {
                        return loadJBossModulePropertiesJar(file, zipFile, moduleProperties.get(0), mod.getName(), mod.getVersion());
                    }
                }
                // For Jars that come from Herd, we only have external descriptors
                File moduleXmlExternalDescriptor = new File(file.getParentFile(), MODULE_XML);
                if(moduleXmlExternalDescriptor.exists()){
                    ModuleSpec mod = moduleFromCeylonRepoFile(file);
                    if(mod != null){
                        return loadFromResolver(file, moduleXmlExternalDescriptor, XmlDependencyResolver.INSTANCE, 
                                mod.getName(), mod.getVersion(), Type.JBOSS_MODULES);
                    }
                }
                File modulePropertiesExternalDescriptor = new File(file.getParentFile(), MODULE_PROPERTIES);
                if(modulePropertiesExternalDescriptor.exists()){
                    ModuleSpec mod = moduleFromCeylonRepoFile(file);
                    if(mod != null){
                        return loadFromResolver(file, modulePropertiesExternalDescriptor, PropertiesDependencyResolver.INSTANCE, 
                                mod.getName(), mod.getVersion(), Type.JBOSS_MODULES);
                    }
                }

                // Java 9 module
                List<ZipEntry> java9Module = findEntries(zipFile, "", JAVA9_MODULE);
                if(java9Module.size() == 1) {
                	return loadJava9ModuleJar(file, zipFile, java9Module.get(0), null, null);
                }

                // try Maven
                List<ZipEntry> mavenDescriptors = findEntries(zipFile, METAINF_MAVEN, POM_XML);
                if(mavenDescriptors.size() == 1 && MavenResolver != null) {
                    return loadMavenJar(file, zipFile, mavenDescriptors.get(0), null, null);
                }
                // alternately, try an external pom (for example javax.servlet has no 
                // internal pom, but has crap OSGi metadata, and a valid external pom).
                // If the jar comes straight out of a Maven repo/cache we may have an 
                // external pom file
                if(file.getName().endsWith(".jar")){
                    File externalDescriptor = new File(file.getParentFile(), file.getName().substring(0, file.getName().length()-4)+".pom");
                    if(externalDescriptor.exists()){
                        return loadMavenJar(file, externalDescriptor, null, null);
                    }
                }
                
                // last OSGi
                ZipEntry osgiProperties = zipFile.getEntry(JarFile.MANIFEST_NAME);
                if(osgiProperties != null){
                    Module module = loadOsgiJar(file, zipFile, osgiProperties, null, null);
                    // it's possible we have a MANIFEST but not for the module we're looking for
                    if(module != null)
                        return module;
                }
                
                if (moduleDescriptors.isEmpty() && moduleXmls.isEmpty() && moduleProperties.isEmpty()
                        && mavenDescriptors.isEmpty() && osgiProperties == null) {
                    // There's nothing we can retrieve from this jar
                    // let's return a dummy module so the jar will
                    // get removed from the list of potentials at least
                    return NO_MODULE;
                }
                
                // not found
                return null;
            }finally{
                zipFile.close();
            }
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
                    // If the jar comes straight out of a Maven repo/cache we may have an external pom file
                    String externalDescriptorPath = String.format("%s-%s.pom", artifactId, version);
                    File externalDescriptor = new File(file.getParentFile(), externalDescriptorPath);
                    if(externalDescriptor.exists()){
                    	return loadMavenJar(file, externalDescriptor, name, version);
                    }
                }
                
                // Try Ceylon module first
                String ceylonPath = name.replace('.', '/');
                ZipEntry moduleDescriptor = zipFile.getEntry(ceylonPath+"/"+NamingBase.MODULE_DESCRIPTOR_CLASS_NAME+".class");
                if(moduleDescriptor != null)
                    return loadCeylonModuleCar(file, zipFile, moduleDescriptor, name, version);
                
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
                
                // Java 9 module
                List<ZipEntry> java9Module = findEntries(zipFile, "", JAVA9_MODULE);
                if(java9Module.size() == 1) {
                	return loadJava9ModuleJar(file, zipFile, java9Module.get(0), name, version);
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
                	ClassFile classFile = ClassFile.read(inputStream);
                	RuntimeAnnotations_attribute annotationsAttribute = (RuntimeAnnotations_attribute) classFile.getAttribute(Attribute.RuntimeVisibleAnnotations);
                	Annotation moduleAnnotation = ClassFileUtil.findAnnotation(classFile, annotationsAttribute, com.redhat.ceylon.compiler.java.metadata.Module.class);
                if(moduleAnnotation == null)
                    throw new IOException("Missing module annotation");

                Object moduleName = ClassFileUtil.getAnnotationValue(classFile, moduleAnnotation, "name");
                Object moduleVersion = ClassFileUtil.getAnnotationValue(classFile, moduleAnnotation, "version");
                if(moduleName instanceof String == false 
                		|| moduleVersion instanceof String == false)
                    throw new IOException("Invalid module annotation");
                
                if(name != null && !moduleName.equals(name))
                    throw new IOException("Module name does not match module descriptor");
                if(version != null && !moduleVersion.equals(version))
                    throw new IOException("Module version does not match module descriptor");
                name = (String)moduleName;
                version = (String)moduleVersion;

//                Object moduleLabel = ClassFileUtil.getAnnotationValue(classFile, moduleAnnotation, "label");
//                if(moduleLabel instanceof String == false)
//                    throw new IOException("Invalid module annotation");
//                String label = (String)moduleLabel;
                
                String groupId, artifactId;
                groupId = (String) ClassFileUtil.getAnnotationValue(classFile, moduleAnnotation, "group");
                if(groupId != null){
                    artifactId = (String) ClassFileUtil.getAnnotationValue(classFile, moduleAnnotation, "artifact");
                    if(artifactId == null || artifactId.isEmpty())
                        artifactId = name;
                }else{
                    String[] coordinates = ModuleUtil.getMavenCoordinates(name);
                    groupId = coordinates[0];
                    artifactId = coordinates[1];
                }

                
                Module module = new Module(name, version, groupId, artifactId, Type.CEYLON, file);
                Object moduleDependencies = ClassFileUtil.getAnnotationValue(classFile, moduleAnnotation, "dependencies");
                ArtifactOverrides ao = null;
                if(overrides != null){
                    ao = overrides.getArtifactOverrides(new ArtifactContext(null, name, version));
                }
                if(moduleDependencies instanceof Object[]){
                    int[] binver = BytecodeUtils.getBinaryVersions(classFile);
                    boolean supportsNamespaces = binver != null 
                            && ModuleUtil.supportsImportsWithNamespaces(binver[0], binver[1]);
                    for(Object dependency : (Object[])moduleDependencies){
                    	Annotation dependencyAnnotation = (Annotation) dependency;
                        String depName = (String)ClassFileUtil.getAnnotationValue(classFile, dependencyAnnotation, "name");
                        String depNamespace;
                        if (supportsNamespaces) {
                            depNamespace = (String)ClassFileUtil.getAnnotationValue(classFile, dependencyAnnotation, "namespace");
                            if (depNamespace != null && depNamespace.isEmpty()) {
                                depNamespace = null;
                            }
                        } else {
                            if (ModuleUtil.isMavenModule(depName)) {
                                depNamespace = MavenRepository.NAMESPACE;
                            } else {
                                depNamespace = null;
                            }
                        }
                        String depVersion = (String)ClassFileUtil.getAnnotationValue(classFile, dependencyAnnotation, "version");
                        Boolean optional = (Boolean)ClassFileUtil.getAnnotationValue(classFile, dependencyAnnotation, "optional");
                        if(optional == null)
                        	    optional = false;
                        Boolean export = (Boolean)ClassFileUtil.getAnnotationValue(classFile, dependencyAnnotation, "export");
                        if(export == null)
                        	    export = false;
                        if(depName == null || depVersion == null)
                            throw new IOException("Invalid module import");
                        
                        if(overrides != null){
                            ArtifactContext depCtx = new ArtifactContext(depNamespace, depName, depVersion);
                            ArtifactContext replacement = overrides.replace(depCtx);
                            if(replacement != null){
                                depCtx = replacement;
                                depNamespace = replacement.getNamespace();
                                depName = replacement.getName();
                                depVersion = replacement.getVersion();
                            }
                            if(overrides.isVersionOverridden(depCtx)){
                                depVersion = overrides.getVersionOverride(depCtx);
                                depCtx.setVersion(depVersion);
                            }
                            if(overrides.isRemoved(depCtx))
                                continue;
                            if(ao != null){ 
                                if(ao.isAddedOrUpdated(depCtx))
                                    continue;
                                if(ao.isOptionalOverridden(depCtx))
                                    optional = ao.isOptional(depCtx);
                                if(ao.isShareOverridden(depCtx))
                                    export = ao.isShared(depCtx);
                            }
                        }
                        
                        module.addDependency(depName, depVersion, optional, export);
                    }
                    if(ao != null){
                        for(DependencyOverride dep : ao.getAdd()){
                            module.addDependency(dep.getArtifactContext().getName(), dep.getArtifactContext().getVersion(), 
                                    dep.isOptional(), dep.isShared());
                        }
                    }
                }
                return module;
            } catch (ConstantPoolException e) {
                throw new IOException(e);
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
        		    return loadFromResolver(file, inputStream, dependencyResolver, name, version, moduleType);
            }finally{
                inputStream.close();
            }
        }

        private Module loadFromResolver(File file, File moduleDescriptor, 
        		DependencyResolver dependencyResolver, String name, String version,
        		Type moduleType) throws IOException {
            	InputStream inputStream = new FileInputStream(moduleDescriptor);
            	try{
            		return loadFromResolver(file, inputStream, dependencyResolver, name, version, moduleType);
            	}finally{
            		inputStream.close();
            	}
        }

        private Module loadFromResolver(File file, InputStream inputStream, DependencyResolver dependencyResolver,
				String name, String version, Type moduleType) throws IOException {
        		ModuleInfo moduleInfo = dependencyResolver.resolveFromInputStream(inputStream, name, version, overrides);
        		if (moduleInfo != null) {
        		    if(name != null && moduleInfo.getName() != null && !name.equals(moduleInfo.getName()))
        		        return null;
                    if(version != null && moduleInfo.getVersion() != null && !version.equals(moduleInfo.getVersion()))
                        return null;
        			Module module = new Module(name != null ? name : moduleInfo.getName(), 
        			        version != null ? version : moduleInfo.getVersion(),
        			        moduleInfo.getGroupId(), moduleInfo.getArtifactId(),
        			        moduleType, file);
        			for(ModuleDependencyInfo dep : moduleInfo.getDependencies()){
        			    // skip this nonsense scope
        			    if(dep.getModuleScope() == ModuleScope.TEST)
        			        continue;
        				module.addDependency(dep.getName(), dep.getVersion(), dep.isOptional(), dep.isExport());
        			}
        			if(moduleInfo.getFilter() != null)
        				module.setFilter(PathFilterParser.parse(moduleInfo.getFilter()));
        			return module;
        		}
        		return null;
		}

		private Module loadJBossModuleXmlJar(File file, ZipFile zipFile, ZipEntry moduleXml, String name, String version) throws IOException {
            return loadJBossModuleJar(file, zipFile, moduleXml, XmlDependencyResolver.INSTANCE, name, version);
        }

        private Module loadMavenJar(File file, ZipFile zipFile, ZipEntry moduleDescriptor, String name, String version) throws IOException {
            if(MavenResolver == null)
                return null;
            return loadFromResolver(file, zipFile, moduleDescriptor, MavenResolver, name, version, Type.MAVEN);
        }

        private Module loadMavenJar(File file, File moduleDescriptor, String name, String version) throws IOException {
            if(MavenResolver == null)
                return null;
            return loadFromResolver(file, moduleDescriptor, MavenResolver, name, version, Type.MAVEN);
        }

        private Module loadDefaultJar(File file) throws IOException {
            return new Module("default", null, null, null, Type.CEYLON, file);
        }

        private Module loadJava9ModuleJar(File file, ZipFile zipFile, ZipEntry moduleDescriptor, String name, String version) throws IOException {
        	Java9Module java9Module = Java9ModuleReader.getJava9Module(zipFile, moduleDescriptor);
        	if(java9Module != null)
        		return new Module(java9Module.name, java9Module.version, null, null, Type.JAVA9, file);
        	// or throw?
        	return null;
        }
        
        private Module loadOsgiJar(File file, ZipFile zipFile, ZipEntry moduleDescriptor, String name, String version) throws IOException {
        	String label = null;
            // first verify that it is indeed for the module we're looking for
            InputStream inputStream = zipFile.getInputStream(moduleDescriptor);
            try{
                Manifest manifest = new Manifest(inputStream);
                if (OsgiUtil.DefaultModuleManifest.isDefaultModule(manifest)) {
                    return loadDefaultJar(file);
                }
                Attributes attributes = manifest.getMainAttributes();
                String bundleName = attributes.getValue(OsgiUtil.OsgiManifest.Bundle_SymbolicName);
                String bundleVersion = attributes.getValue(OsgiUtil.OsgiManifest.Bundle_Version);
                if (name != null && version != null) {
                    if(!Objects.equals(name, bundleName)
                            || !Objects.equals(version, bundleVersion))
                        return null;
                } else {
                    name = bundleName;
                    version = bundleVersion;
                }
                label = attributes.getValue(OsgiUtil.OsgiManifest.Bundle_Name); //TODO: what to do with this!?
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
        instance().run(module, version, runClass, arguments);
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
    public void run(String module, String version, String runClass, String... arguments){
        setup(module, version);
        try {
            Class<?> klass = moduleClassLoader.loadClass(runClass);
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
        instance().run(module, version, runClass, arguments);
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
    public void run(String module, String version, Class<?> runClass, String... arguments){
        setup(module, version);
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
        instance().setup(module, version);
    }

    /**
     * Sets up the Ceylon metamodel by adding the specified module to it. This does not run any Ceylon code,
     * nor does it reset the metamodel first. You can repeatedly invoke this method to add new Ceylon modules
     * to the metamodel.
     * 
     * @param module the module name to load.
     * @param version the version to load. Ignored if the module is the default module.
     */
    public void setup(String module, String version){
        Overrides parsedOverrides = Overrides.getDistOverrides();
        if (overrides != null) {
            try {
                parsedOverrides = parsedOverrides.append(overrides);
            } catch (RuntimeException e) {
                throw e;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        Metamodel.resetModuleManager(new OverridesRuntimeResolver(parsedOverrides));
        if(moduleClassLoader == null)
        	setupModuleClassLoader(module);
        if (classPath == null) {
            classPath = new ClassPath(parsedOverrides);
            visited = new HashSet<ClassPath.Module>();
            registerInMetamodel("ceylon.language", Versions.CEYLON_VERSION_NUMBER, false);
            registerInMetamodel("com.redhat.ceylon.common", Versions.CEYLON_VERSION_NUMBER, false);
            registerInMetamodel("com.redhat.ceylon.model", Versions.CEYLON_VERSION_NUMBER, false);
        }
        if(module.equals(com.redhat.ceylon.model.typechecker.model.Module.DEFAULT_MODULE_NAME))
            version = null;
        registerInMetamodel(module, version, false);
    }

    private void setupModuleClassLoader(String module) {
    	if(JDKUtils.jdk.providesVersion(JDK.JDK9.version)){
    		try{
    		Object mod = Java9ModuleUtil.getModule(getClass());
    		// make sure we're running as a module, not from the classpath
    		if(Java9ModuleUtil.isNamedModule(mod)){
    			// also add a read to it
    			Object otherModule = Java9ModuleUtil.findModule(mod, module);
    			if(otherModule == null){
    				// try to load it dynamically
    				otherModule = Java9ModuleUtil.loadModuleDynamically(module);
    			}
    			if(otherModule != null){
                    Class<?> moduleClass = ClassLoader.getSystemClassLoader().loadClass("java.lang.Module");
                    // also add a read to it
                    Method addReads = moduleClass.getMethod("addReads", moduleClass);
                    addReads.invoke(mod, otherModule);
    				moduleClassLoader = Java9ModuleUtil.getClassLoader(otherModule);
    			}
    		}
    		}catch(InvocationTargetException | IllegalAccessException | IllegalArgumentException 
    				| ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException x){
    			throw new RuntimeException(x);
    		}
    		
    	}
    	if(moduleClassLoader == null)
    		moduleClassLoader = ClassLoader.getSystemClassLoader();
	}

	/**
     * Resets the metamodel. This will impact any Ceylon code running on the same ClassLoader, across
     * threads, and will crash them if they are not done running.
     */
    public static void resetMetamodel(){
        instance().reset();
    }
    
    /**
     * Resets the metamodel. This will impact any Ceylon code running on the same ClassLoader, across
     * threads, and will crash them if they are not done running.
     */
    public void reset(){
        Metamodel.resetModuleManager();
    }
    
    private void registerInMetamodel(String name, String version, boolean optional) {
        ClassPath.Module module;
        try {
            module = classPath.loadModule(name, version, allowMissingModules);
        } catch (com.redhat.ceylon.compiler.java.runtime.Main.ClassPath.ModuleNotFoundException e) {
            if(optional)
                return;
            throw new RuntimeException(e);
        }
        if(!visited.add(module))
            return;
        // skip JDK modules which are already in the metamodel
        if(module.type == ClassPath.Type.JDK)
            return;
        Metamodel.loadModule(name, version, module, moduleClassLoader);
        // also register its dependencies
        for(ClassPath.Dependency dep : module.dependencies)
            registerInMetamodel(dep.name(), dep.version(), dep.optional);
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
		// WARNING: keep in sync with Main2
        int idx = 0;
        boolean allowMissingModules = false;
        String overrides = null;
        for(int i=0;i<args.length;i++){
            String arg = args[i];
            if(!arg.startsWith("-"))
                break;
            if(arg.equals("--allow-missing-modules")){
                allowMissingModules = true;
            }else if(arg.equals("--overrides") || arg.equals("--maven-overrides")){
                if(i+1 >= args.length){
                    usage();
                }
                overrides = args[++i];
                idx++;
            }else
                usage();
            idx++;
        }
        if(args.length < (2 + idx)){
            usage();
        }
        ModuleSpec moduleSpec = ModuleSpec.parse(args[idx], Option.VERSION_REQUIRED);
        String version;
        if(moduleSpec.getName().equals(com.redhat.ceylon.model.typechecker.model.Module.DEFAULT_MODULE_NAME))
            version = null;
        else
            version = moduleSpec.getVersion();
        String[] moduleArgs = Arrays.copyOfRange(args, 2 + idx, args.length);
        instance()
            .allowMissingModules(allowMissingModules)
            .overrides(overrides)
            .run(moduleSpec.getName(), version, args[idx + 1], moduleArgs);
    }

    private static void usage() {
        System.err.println("Invalid arguments.");
        System.err.println("Usage: \n");
        System.err.println(Main.class.getName()+" [--allow-missing-modules] [--overrides overridesFile.xml] moduleSpec mainJavaClassName args*");
        System.exit(1);
    }
}
