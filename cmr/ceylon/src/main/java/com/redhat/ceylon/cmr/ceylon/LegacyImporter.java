package com.redhat.ceylon.cmr.ceylon;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import com.redhat.ceylon.cmr.api.ArtifactContext;
import com.redhat.ceylon.cmr.api.JDKUtils;
import com.redhat.ceylon.cmr.api.ModuleDependencyInfo;
import com.redhat.ceylon.cmr.api.ModuleInfo;
import com.redhat.ceylon.cmr.api.ModuleQuery;
import com.redhat.ceylon.cmr.api.ModuleSearchResult;
import com.redhat.ceylon.cmr.api.ModuleSearchResult.ModuleDetails;
import com.redhat.ceylon.cmr.api.ModuleVersionQuery;
import com.redhat.ceylon.cmr.api.RepositoryManager;
import com.redhat.ceylon.cmr.impl.CMRJULLogger;
import com.redhat.ceylon.cmr.impl.JarUtils;
import com.redhat.ceylon.cmr.impl.PropertiesDependencyResolver;
import com.redhat.ceylon.cmr.impl.XmlDependencyResolver;
import com.redhat.ceylon.common.ModuleUtil;
import com.redhat.ceylon.common.Versions;
import com.redhat.ceylon.common.log.Logger;


/**
 * Class that can be used to import legacy JAR files as a module into a repository.
 * @author Tako Schotanus
 */
public class LegacyImporter {
    private final File jarFile;
    private final RepositoryManager outRepoman;
    private final RepositoryManager lookupRepoman;
    private final ImporterFeedback feedback;
    private final Logger log;
    
    private File descriptorFile;
    
    private Set<String> jarClassNames;
    private Set<String> externalClasses;
    private Set<Type> checkedTypes;
    private Set<ModuleDependencyInfo> expectedDependencies;
    private boolean hasProblems;
    private boolean hasErrors;
    
    public enum DependencyResults {
        DEP_OK, // The dependency is correct
        DEP_OK_UNUSED, // The dependency was found in the repository but seems unused by the code
        DEP_MARK_SHARED, // The dependency was found but is part of the code's public API and should be marked shared
        DEP_NOT_FOUND, // The dependency was listed in the descriptor file but was not found in the repository
        DEP_CHECK_FAILED, // An error occurred while trying to check the dependency
        DEP_JDK; // The dependency is a Java SDK (JigSaw) module
    }
    
    public enum DependencyErrors {
        DEPERR_INVALID_MODULE_NAME, DEPERR_INVALID_MODULE_VERSION, DEPERR_INVALID_MODULE_DEFAULT;
    }
    
    public static interface ImporterFeedback {
        /**
         * Called before any call to <code>beforeDependency()</code>
         * @throws Exception Any exception thrown by the feedback handler
         */
        void beforeDependencies() throws Exception;
        
        /**
         * Called before any call to <code>dependency()</code> or <code>dependencyError()</code>
         * @param dep The dependency about to be listed
         * @throws Exception Any exception thrown by the feedback handler
         */
        void beforeDependency(ModuleDependencyInfo dep) throws Exception;
        
        /**
         * Marks a dependency that was either found while reading the module descriptor
         * (the <code>result</code> is anything but <code>DEP_JDK</code>) during a call
         * to <code>loadModuleDescriptor()</code> or was detected as being a Java SDK
         * module during a call to <code>listPackages()</code>.
         * @param result The result of the dependency analysis
         * @param dep The dependency curently being handled
         * @throws Exception Any exception thrown by the feedback handler
         */
        void dependency(DependencyResults result, ModuleDependencyInfo dep) throws Exception;
        
        /**
         * Marks an error while trying to analyse the given dependency
         * @param error The type of error detected
         * @param dep The dependency curently being handled
         * @throws Exception Any exception thrown by the feedback handler
         */
        void dependencyError(DependencyErrors error, ModuleDependencyInfo dep) throws Exception;
        
        /**
         * Called after any call to <code>dependency()</code> or <code>dependencyError()</code>
         * @param dep The dependency that was listed
         * @throws Exception Any exception thrown by the feedback handler
         */
        void afterDependency(ModuleDependencyInfo dep) throws Exception;
        
        /**
         * Called after all dependencies have been listed
         * @throws Exception Any exception thrown by the feedback handler
         */
        void afterDependencies() throws Exception;
        
        /**
         * Called before listing any Java SDK (JigSaw) modules. This can only happen within a call
         * to <code>listPackages()</code>
         * @throws Exception Any exception thrown by the feedback handler
         */
        void beforeJdkModules() throws Exception;
        
        /**
         * Called after listing all Java SDK (JigSaw) modules. This can only happen within a call
         * to <code>listPackages()</code>
         * @throws Exception Any exception thrown by the feedback handler
         */
        void afterJdkModules() throws Exception;
        
        /**
         * Called before listing any (non-JDK) package names. This can only happen within a call
         * to <code>listPackages()</code>
         * @throws Exception Any exception thrown by the feedback handler
         */
        void beforePackages() throws Exception;
        
        /**
         * Marks a (non-JDK) package that was found while calling either <code>listPackages()</code>.
         * It can be followed by a call to <code>suggestions()</code> if suggestions were enabled
         * @param pkg The name of the package being handled
         * @throws Exception Any exception thrown by the feedback handler
         */
        void packageName(String pkg) throws Exception;
        
        /**
         * Lists any suggestions that were found for the package currently being handled
         * @param pkg The name of the package being handled
         * @param suggestions The list of suggestions (which can be empty)
         * @return The suggestion that should be used in any call to <code>updateModuleDescriptor()</code>
         * @throws Exception Any exception thrown by the feedback handler
         */
        ModuleDependencyInfo suggestions(String pkg, Set<ModuleDetails> suggestions) throws Exception;
        
        /**
         * Called after listing all (non-JDK) package names. This can only happen within a call
         * to <code>listPackages()</code>
         * @throws Exception Any exception thrown by the feedback handler
         */
        void afterPackages() throws Exception;
        
        /**
         * Called before listing any separate class names. This can only happen within a call
         * to <code>listPackages()</code> or <code>listClasses()</code>
         * @throws Exception Any exception thrown by the feedback handler
         */
        void beforeClasses() throws Exception;
        
        /**
         * Marks a single class that was found any not considered part of any packages or modules
         * while calling either <code>listPackages()</code> or <code>listClasses()</code>
         * @param cls The name of the class being handled
         * @throws Exception Any exception thrown by the feedback handler
         */
        void className(String cls) throws Exception;
        
        /**
         * Called after listing all separate class names. This can only happen within a call
         * to <code>listPackages()</code> or <code>listClasses()</code>
         * @throws Exception Any exception thrown by the feedback handler
         */
        void afterClasses() throws Exception;
    }
    
    /**
     * Set up the object with the given output and lookup repositories
     * @param outputRepository The destination repository to import the JAR into
     * @param lookupRepoman The repository for looking up dependencies
     */
    public LegacyImporter(File jarFile, RepositoryManager outputRepository, RepositoryManager lookupRepository) {
        this(jarFile, outputRepository, lookupRepository, null);
    }
    
    /**
     * Set up the object with the given output and lookup repositories and a callback
     * interface to receive feedback on progress and errors
     * @param outputRepository The destination repository to import the JAR into
     * @param lookupRepoman The repository for looking up dependencies
     * @param feedback Instance of ModuleCopycat.CopycatFeedback for receiving feedback
     */
    public LegacyImporter(File jarFile, RepositoryManager outputRepository, RepositoryManager lookupRepository, ImporterFeedback feedback) {
        this(jarFile, outputRepository, lookupRepository, new CMRJULLogger(), feedback);
    }
    
    /**
     * Set up the object with the given output and lookup repositories and a callback
     * interface to receive feedback on progress and errors
     * @param outputRepository The destination repository to import the JAR into
     * @param lookupRepoman The repository for looking up dependencies
     * @param log The logger to use
     * @param feedback Instance of ModuleCopycat.CopycatFeedback for receiving feedback
     */
    public LegacyImporter(File jarFile, RepositoryManager outputRepository, RepositoryManager lookupRepository, Logger log, ImporterFeedback feedback) {
        assert(jarFile != null);
        assert(outputRepository != null);
        assert(lookupRepository != null);
        assert(log != null);
        this.jarFile = jarFile;
        this.outRepoman = outputRepository;
        this.lookupRepoman = lookupRepository;
        this.log = log;
        this.feedback = feedback;
    }
    
    /**
     * Indicates if the JAR has (recoverable) problems or not
     * @return Boolean
     */
    public boolean hasProblems() {
        return hasProblems;
    }

    /**
     * Indicates if the JAR has (non-recoverable) errors or not
     * @return Boolean
     */
    public boolean hasErrors() {
        return hasErrors;
    }

    /**
     * The descriptor to use, this can either be a <code>module.xml</code> or
     * a <code>module.properties</code> file.
     * If it exists it will be parsed and its contents used to determine which external
     * classes are already covered by the defined dependencies.
     * Regardless of whether the file exists it will be remembered for use with the
     * <code>updateModuleDescripter()</code> method. 
     * @param descriptorFile
     * @throws Exception
     */
    public void loadModuleDescriptor(File descriptorFile) throws Exception {
        this.descriptorFile = descriptorFile;
        
        gatherExternalClasses();
        
        if (descriptorFile != null) {
            if (descriptorFile.exists()) {
                if (descriptorFile.toString().toLowerCase().endsWith(".xml")) {
                    checkModuleXml(descriptorFile);
                } else if(descriptorFile.toString().toLowerCase().endsWith(".properties")) {
                    checkModuleProperties(descriptorFile);
                }
            }
        }
    }
    
    /**
     * Tries to determine which packages belong to which modules. If called with
     * <code>makeSuggestions</code> being <code>false</code> it will just detect
     * Java SDK modules and list any remaining packages. If passed <code>true</code>
     * it will also try to find matching modules in the <code>lookupRepository</code>
     * that was passed in the constructor. And finally it will list any classes
     * that are not in any package.
     * Feedback methods called: <code>beforeJdkModules()</code>, <code>aftrerJdkModules()</code>,
     * <code>dependency()</code>, <code>beforePackages()</code>, <code>afterPackages()</code>,
     * <code>packageName()</code>, <code>beforeClasses()</code>, <code>afterClasses()</code> and
     * <code>className()</code>.
     * @param makeSuggestions
     * @throws Exception
     */
    public void listPackages(boolean makeSuggestions) throws Exception {
        gatherExternalClasses();
        
        if (!externalClasses.isEmpty()) {
            Set<String> externalPackages = getPackagesFromClasses(externalClasses);
            if (!externalPackages.isEmpty()) {
                Set<String> jdkModules = gatherJdkModules(externalPackages);
                if (!jdkModules.isEmpty()) {
                    feedback.beforeJdkModules();
                    for (String mod : jdkModules) {
                        ModuleDependencyInfo dep = new ModuleDependencyInfo(mod, JDKUtils.jdk.version, false, true);
                        feedback.dependency(DependencyResults.DEP_JDK, dep);
                        expectedDependencies.add(dep);
                    }
                    feedback.afterJdkModules();
                }
                if (!externalPackages.isEmpty()) {
                    feedback.beforePackages();
                    for (String pkg : externalPackages) {
                        feedback.packageName(pkg);
                        if (makeSuggestions) {
                            outputSuggestions(pkg);
                        }
                    }
                    hasErrors = true;
                    feedback.afterPackages();
                }
            }
            Set<String> externalDefaultClasses = getDefaultPackageClasses(externalClasses);
            if (!externalDefaultClasses.isEmpty()) {
                feedback.beforeClasses();
                for (String cls : externalDefaultClasses) {
                    feedback.className(cls);
                }
                hasErrors = true;
                feedback.afterClasses();
            }
        }
    }
    
    /**
     * Will iterate over all the external classes (not matched by any dependency defined by
     * a previous call to <code>loadModuleDescriptor()</code>) calling the appropriate feedback
     * methods for each of them.
     * Feedback methods called: <code>beforeClasses()</code>, <code>afterClasses()</code> and
     * <code>className()</code>.
     * @throws Exception Any exception that can be thrown by the feedback handler
     */
    public void listClasses() throws Exception {
        gatherExternalClasses();
        
        if (!externalClasses.isEmpty()) {
            feedback.beforeClasses();
            for (String cls : externalClasses) {
                feedback.className(cls);
            }
            hasErrors = true;
            feedback.afterClasses();
        }
    }
    
    /**
     * Updates (or creates) the module descriptor with the expected dependencies
     * @throws IOException Any exception that occurred during the update
     */
    public void updateModuleDescriptor() throws IOException {
        if (hasProblems) {
            if (descriptorFile != null) {
                if (descriptorFile.toString().toLowerCase().endsWith(".xml")) {
                    updateDescriptorXml();
                } else if (descriptorFile.toString().toLowerCase().endsWith(".properties")) {
                    updateDescriptorProperties();
                }
            }
        }
    }

    /**
     * Publishes the JAR and accompanying module descriptor (if defined) to the
     * <code>outputRepository</code> specified in the constructor using the given
     * module name and version.
     * @param moduleName The name for the published module
     * @param moduleVersion The version for the published module
     */
    public void publish(String moduleName, String moduleVersion) {
        ArtifactContext context = new ArtifactContext(moduleName, moduleVersion, ArtifactContext.JAR);
        context.setForceOperation(true);
        outRepoman.putArtifact(context, jarFile);
        
        ShaSigner.signArtifact(outRepoman, context, jarFile, log);
        
        if (descriptorFile != null) {
            ArtifactContext descriptorContext = null;
            if (descriptorFile.toString().toLowerCase().endsWith(".xml")) {
                descriptorContext = new ArtifactContext(moduleName, moduleVersion, ArtifactContext.MODULE_XML);
            } else if (descriptorFile.toString().toLowerCase().endsWith(".properties")) {
                descriptorContext = new ArtifactContext(moduleName, moduleVersion, ArtifactContext.MODULE_PROPERTIES);
            }
            descriptorContext.setForceOperation(true);
            outRepoman.putArtifact(descriptorContext, descriptorFile);
        }
    }
    
    // Check the properties descriptor file for problems and at the same time
    // remove all classes that are found within the imported modules
    // from the given set of external class names
    private void checkModuleProperties(File descriptorFile) throws Exception {
        ModuleInfo dependencies = PropertiesDependencyResolver.INSTANCE.resolveFromFile(descriptorFile);
        checkDependencies(dependencies);
    }

    // Check the XML descriptor file for problems and at the same time
    // remove all classes that are found within the imported modules
    // from the given set of external class names
    private void checkModuleXml(File descriptorFile) throws Exception {
        ModuleInfo dependencies = XmlDependencyResolver.INSTANCE.resolveFromFile(descriptorFile);
        checkDependencies(dependencies);
    }

    // Check the given dependencies for problems and at the same time
    // remove all classes that are found within the imported modules
    // from the given set of external class names
    private void checkDependencies(ModuleInfo dependencies) throws Exception {
        feedback.beforeDependencies();
        if (!dependencies.getDependencies().isEmpty()) {
            TreeSet<ModuleDependencyInfo> sortedDeps = new TreeSet<>(dependencies.getDependencies());
            for (ModuleDependencyInfo dep : sortedDeps) {
                feedback.beforeDependency(dep);
                String name = dep.getName();
                String version = dep.getVersion();
                // missing dep is OK, it can be fixed later, but invalid module/dependency is not OK
                if(name == null || name.isEmpty())
                    feedback.dependencyError(DependencyErrors.DEPERR_INVALID_MODULE_NAME, dep);
                if(ModuleUtil.isDefaultModule(name))
                    feedback.dependencyError(DependencyErrors.DEPERR_INVALID_MODULE_DEFAULT, dep);
                if(version == null || version.isEmpty())
                    feedback.dependencyError(DependencyErrors.DEPERR_INVALID_MODULE_VERSION, dep);
                if (JDKUtils.isJDKModule(name) || JDKUtils.isOracleJDKModule(name)) {
                    if (removeMatchingJdkClasses(name)) {
                        if (dep.isExport()) {
                            feedback.dependency(DependencyResults.DEP_OK, dep);
                        } else {
                            dep = new ModuleDependencyInfo(dep.getName(), dep.getVersion(), dep.isOptional(), true);
                            feedback.dependency(DependencyResults.DEP_MARK_SHARED, dep);
                            hasProblems = true;
                        }
                    } else {
                        if (dep.isExport()) {
                            dep = new ModuleDependencyInfo(dep.getName(), dep.getVersion(), dep.isOptional(), false);
                            feedback.dependency(DependencyResults.DEP_OK_UNUSED, dep);
                        } else {
                            feedback.dependency(DependencyResults.DEP_OK, dep);
                        }
                    }
                } else {
                    ArtifactContext context = new ArtifactContext(name, dep.getVersion(), ArtifactContext.CAR, ArtifactContext.JAR);
                    File artifact = lookupRepoman.getArtifact(context);
                    if (artifact != null && artifact.exists()) {
                        try {
                            Set<String> importedClasses = JarUtils.gatherClassnamesFromJar(artifact);
                            if (removeMatchingClasses(importedClasses)) {
                                if (dep.isExport()) {
                                    feedback.dependency(DependencyResults.DEP_OK, dep);
                                } else {
                                    dep = new ModuleDependencyInfo(dep.getName(), dep.getVersion(), dep.isOptional(), true);
                                    feedback.dependency(DependencyResults.DEP_MARK_SHARED, dep);
                                    hasProblems = true;
                                }
                            } else {
                                if (dep.isExport()) {
                                    dep = new ModuleDependencyInfo(dep.getName(), dep.getVersion(), dep.isOptional(), false);
                                    feedback.dependency(DependencyResults.DEP_OK_UNUSED, dep);
                                } else {
                                    feedback.dependency(DependencyResults.DEP_OK, dep);
                                }
                            }
                        } catch (IOException e) {
                            feedback.dependency(DependencyResults.DEP_CHECK_FAILED, dep);
                            hasErrors = true;
                        }
                    } else {
                        feedback.dependency(DependencyResults.DEP_NOT_FOUND, dep);
                        hasErrors = true;
                    }
                }
                feedback.afterDependency(dep);
                expectedDependencies.add(dep);
            }
        }
        feedback.afterDependencies();
    }
    
    // Create the set of class names for those types referenced by the
    // public API of the classes in the JAR we're importing and that are
    // not part of the JAR itself
    private void gatherExternalClasses() throws MalformedURLException, IOException {
        if (externalClasses == null) {
            if (!jarFile.isFile()) {
                throw new FileNotFoundException("Could not find " + jarFile);
            }
            checkedTypes = new HashSet<>();
            externalClasses = new TreeSet<>();
            expectedDependencies = new HashSet<>();
            jarClassNames = JarUtils.gatherClassnamesFromJar(jarFile);
            try (URLClassLoader cl = new URLClassLoader(new URL[] { jarFile.toURI().toURL() })) {
                for (String className : jarClassNames) {
                    Class<?> clazz;
                    try {
                        clazz = cl.loadClass(className);
                    } catch (NoClassDefFoundError | ClassNotFoundException e) {
                        handleNotFoundErrors(e);
                        continue;
                    }
                    checkPublicApi(clazz);
                }
            }
        }
    }
    
    // Check the public API of a class for types that are external to the JAR we're importing
    private void checkPublicApi(Class<?> clazz) {
        if (clazz.getModifiers() != Modifier.PUBLIC) {
            // Not interested in any but public classes
            return;
        }
        // Make sure we get an actual class, not an array
        while (clazz.isArray()) {
            clazz = clazz.getComponentType();
        }
        try {
            checkTypes(clazz.getTypeParameters());
        } catch (NoClassDefFoundError | TypeNotPresentException e) {
            handleNotFoundErrors(e);
        }
        try {
            checkAnnotations(clazz.getAnnotations());
        } catch (NoClassDefFoundError | TypeNotPresentException e) {
            handleNotFoundErrors(e);
        }
        try {
            checkType(clazz.getGenericSuperclass());
        } catch (NoClassDefFoundError | TypeNotPresentException e) {
            handleNotFoundErrors(e);
        }
        try {
            Type[] interfaces = clazz.getGenericInterfaces();
            for (Type iface : interfaces) {
                checkType(iface);
            }
        } catch (NoClassDefFoundError | TypeNotPresentException e) {
            handleNotFoundErrors(e);
        }
        try {
            Method[] methods = clazz.getMethods();
            for (Method mth : methods) {
                if ((mth.getModifiers() & (Modifier.PUBLIC | Modifier.PROTECTED)) != 0) {
                    checkTypes(mth.getTypeParameters());
                    checkAnnotations(mth.getAnnotations());
                    checkType(mth.getGenericReturnType());
                    for (Type param : mth.getGenericParameterTypes()) {
                        checkType(param);
                    }
                    checkAnnotations(mth.getParameterAnnotations());
                    for (Type ex : mth.getGenericExceptionTypes()) {
                        checkType(ex);
                    }
                }
            }
        } catch (NoClassDefFoundError | TypeNotPresentException e) {
            handleNotFoundErrors(e);
        }
        try {
            Field[] fields = clazz.getFields();
            for (Field fld : fields) {
                if ((fld.getModifiers() & (Modifier.PUBLIC | Modifier.PROTECTED)) != 0) {
                    checkAnnotations(fld.getAnnotations());
                    checkType(fld.getGenericType());
                }
            }
        } catch (NoClassDefFoundError | TypeNotPresentException e) {
            handleNotFoundErrors(e);
        }
        try {
            Constructor<?>[] constructors = clazz.getConstructors();
            for (Constructor<?> cons : constructors) {
                if ((cons.getModifiers() & (Modifier.PUBLIC | Modifier.PROTECTED)) != 0) {
                    checkTypes(cons.getTypeParameters());
                    checkAnnotations(cons.getAnnotations());
                    for (Type param : cons.getGenericParameterTypes()) {
                        checkType(param);
                    }
                    checkAnnotations(cons.getParameterAnnotations());
                    for (Type ex : cons.getGenericExceptionTypes()) {
                        checkType(ex);
                    }
                }
            }
        } catch (NoClassDefFoundError | TypeNotPresentException e) {
            handleNotFoundErrors(e);
        }
    }
    
    private void checkAnnotations(Annotation[][] annotations) {
        for (Annotation[] annos : annotations) {
            checkAnnotations(annos);
        }
    }

    private void checkAnnotations(Annotation[] annotations) {
        for (Annotation anno : annotations) {
            checkType(anno.annotationType());
        }
    }

    private void checkTypes(Type[] types) {
        for (Type t : types) {
            checkType(t);
        }
    }
    
    // Check if the type is external to the JAR we're importing, if so add it to the given set
    private void checkType(Type type) {
        if (type != null) {
            if (checkedTypes.contains(type)) {
                return;
            }
            checkedTypes.add(type);
            if (type instanceof Class) {
                checkClass((Class<?>)type);
            } else if (type instanceof GenericArrayType) {
                checkType(((GenericArrayType) type).getGenericComponentType());
            } else if (type instanceof ParameterizedType) {
                checkType(((ParameterizedType) type).getOwnerType());
                for (Type t : ((ParameterizedType) type).getActualTypeArguments()) {
                    checkType(t);
                }
            } else if (type instanceof TypeVariable) {
                Type[] bounds;
                try {
                    bounds = ((TypeVariable<?>) type).getBounds();
                } catch (NoClassDefFoundError | TypeNotPresentException e) {
                    handleNotFoundErrors(e);
                    return;
                }
                for (Type b : bounds) {
                    checkType(b);
                }
            } else if (type instanceof WildcardType) {
                Type[] lower;
                try {
                    lower = ((WildcardType) type).getLowerBounds();
                } catch (NoClassDefFoundError | TypeNotPresentException e) {
                    handleNotFoundErrors(e);
                    return;
                }
                for (Type b : lower) {
                    checkType(b);
                }
                Type[] upper;
                try {
                    upper = ((WildcardType) type).getUpperBounds();
                } catch (NoClassDefFoundError | TypeNotPresentException e) {
                    handleNotFoundErrors(e);
                    return;
                }
                for (Type b : upper) {
                    checkType(b);
                }
            } else {
                System.err.println("Handling of type not implemented for " + type.getClass().getName());
            }
        }
    }

    // Check if the class is external to the JAR we're importing, if so add it to the given set
    private void checkClass(Class<?> clazz) {
        // Make sure we get an actual class, not an array
        while (clazz.isArray()) {
            clazz = clazz.getComponentType();
        }
        if (clazz.isPrimitive()) {
            // No need to check primitives
            return;
        }
        String name = clazz.getName();
        if (jarClassNames.contains(name)) {
            // Internal to the JAR so it's okay
            return;
        }
        // FIXME Do we need to do more?
        externalClasses.add(name);
    }

    // Extract the name of the class that couldn't be loaded and add it to the given set
    private void handleNotFoundErrors(Throwable orgth) {
        // This is very brittle because it depends on the message only containing the class name
        Throwable th = orgth;
        if (th instanceof TypeNotPresentException
                && (th.getCause() instanceof ClassNotFoundException
                        || th.getCause() instanceof NoClassDefFoundError)) {
            th = th.getCause();
        }
        String name = th.getMessage().replace('/', '.');
        if (name.startsWith("L") && name.endsWith(";")) {
            name = name.substring(1, name.length() - 1);
        }
        if (externalClasses.add(name)) {
            log.debug("NOT FOUND " + name);
        }
    }

    // Remove all classes that are found within the given set of
    // imported classes from the given set of external classes
    private boolean removeMatchingClasses(Set<String> importedClasses) {
        boolean matchesFound = false;
        for (String className : importedClasses) {
            matchesFound |= externalClasses.remove(className);
        }
        return matchesFound;
    }

    // Remove all classes that are part of the given JDK module
    // from the given set of external classes
    private boolean removeMatchingJdkClasses(String jdkModule) {
        Set<String> toBeRemoved = new HashSet<>();
        for (String className : externalClasses) {
            String pkgName = getPackageFromClass(className);
            if (JDKUtils.isJDKPackage(jdkModule, pkgName) || JDKUtils.isOracleJDKPackage(jdkModule, pkgName)) {
                toBeRemoved.add(className);
            }
        }
        externalClasses.removeAll(toBeRemoved);
        return !toBeRemoved.isEmpty();
    }
    
    // Given a set of class names return the set of their package names
    // (excluding those classes that aren't in any packages)
    private Set<String> getPackagesFromClasses(Set<String> classes) {
        Set<String> packages = new TreeSet<>();
        for (String className : classes) {
            String pkg = getPackageFromClass(className);
            if (!pkg.isEmpty()) {
                packages.add(pkg);
            }
        }
        return packages;
    }

    // Given a fully qualified class name return it's package
    // (or an empty string if it's not part of any package)
    private String getPackageFromClass(String className) {
        int p = className.lastIndexOf('.');
        if (p >= 0) {
            return className.substring(0, p);
        } else {
            return "";
        }
    }
    
    // Given a set of class names returns the set of those that aren't in any package
    private Set<String> getDefaultPackageClasses(Set<String> classes) {
        Set<String> defclasses = new TreeSet<>();
        for (String className : classes) {
            int p = className.lastIndexOf('.');
            if (p < 0) {
                defclasses.add(className);
            }
        }
        return defclasses;
    }
    
    // From a list of package names we extract the ones that
    // belong to a JDK module (removing them from the original
    // list) and we return the list of JDK modules we found
    private Set<String> gatherJdkModules(Set<String> packages) {
        Set<String> jdkModules = new TreeSet<>();
        Set<String> newPackages = new HashSet<>();
        for (String pkg : packages) {
            String mod = JDKUtils.getJDKModuleNameForPackage(pkg);
            if (mod != null) {
                jdkModules.add(mod);
            } else {
                newPackages.add(pkg);
            }
        }
        packages.clear();
        packages.addAll(newPackages);
        return jdkModules;
    }

    private void outputSuggestions(String pkg) throws Exception {
        ModuleDependencyInfo dep = null;
        Set<ModuleDetails> suggestions = findSuggestions(pkg);
        if (!suggestions.isEmpty()) {
            dep = feedback.suggestions(pkg, suggestions);
            if (dep != null) {
                expectedDependencies.add(dep);
                hasProblems = true;
            }
        }
    }

    private Set<ModuleDetails> findSuggestions(String pkg) {
        Set<ModuleDetails> suggestions = new TreeSet<>();
        ModuleVersionQuery query = new ModuleVersionQuery("", null, ModuleQuery.Type.JVM);
        query.setBinaryMajor(Versions.JVM_BINARY_MAJOR_VERSION);
        query.setBinaryMinor(Versions.JVM_BINARY_MINOR_VERSION);
        query.setMemberName(pkg);
        query.setMemberSearchExact(true);
        query.setMemberSearchPackageOnly(true);
        ModuleSearchResult result = lookupRepoman.completeModules(query);
        for (ModuleDetails mvd : result.getResults()) {
            suggestions.add(mvd);
        }
        return suggestions;
    }

    private void updateDescriptorProperties() throws IOException {
        Properties deps = new Properties();
        for (ModuleDependencyInfo mdi : expectedDependencies) {
            String key = mdi.getName();
            String val = mdi.getVersion();
            if (mdi.isExport()) {
                key = "+" + key;
            }
            if (mdi.isOptional()) {
                key = key + "?";
            }
            deps.setProperty(key, val);
        }
        try (OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(descriptorFile), "UTF-8")) {
            deps.store(out, "Generated by 'ceylon import-jar'");
        }
    }

    private void updateDescriptorXml() throws IOException {
        throw new RuntimeException("Updating of .xml module descriptors not supported");
    }
}
