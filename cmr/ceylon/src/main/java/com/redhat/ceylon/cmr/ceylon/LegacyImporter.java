package com.redhat.ceylon.cmr.ceylon;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;

import com.redhat.ceylon.cmr.api.ArtifactContext;
import com.redhat.ceylon.cmr.api.ModuleDependencyInfo;
import com.redhat.ceylon.cmr.api.ModuleInfo;
import com.redhat.ceylon.cmr.api.ModuleQuery;
import com.redhat.ceylon.cmr.api.ModuleSearchResult;
import com.redhat.ceylon.cmr.api.ModuleSearchResult.ModuleDetails;
import com.redhat.ceylon.cmr.api.ModuleVersionQuery;
import com.redhat.ceylon.cmr.api.RepositoryManager;
import com.redhat.ceylon.cmr.ceylon.ClassFileScanner.Usage;
import com.redhat.ceylon.cmr.impl.CMRJULLogger;
import com.redhat.ceylon.cmr.impl.JarUtils;
import com.redhat.ceylon.cmr.impl.PropertiesDependencyResolver;
import com.redhat.ceylon.cmr.impl.XmlDependencyResolver;
import com.redhat.ceylon.common.ModuleUtil;
import com.redhat.ceylon.common.Versions;
import com.redhat.ceylon.common.log.Logger;
import com.redhat.ceylon.model.cmr.ArtifactResult;
import com.redhat.ceylon.model.cmr.ImportType;
import com.redhat.ceylon.model.loader.JdkProvider;


/**
 * Class that can be used to import legacy JAR files as a module into a repository.
 * @author Tako Schotanus
 */
public class LegacyImporter {
    private final String moduleName;
    private final String moduleVersion;
    private final File jarFile;
    private final File sourceJarFile;
    private final RepositoryManager outRepoman;
    private final RepositoryManager lookupRepoman;
    private final ImporterFeedback feedback;
    private final Logger log;
    private final JdkProvider jdkProvider;
    
    private File descriptorFile;
    
    private Set<ModuleDependencyInfo> expectedDependencies;
    private Map<String, List<Pattern>> missingDependenciesPackages;
    private boolean descriptorLoaded;
    private boolean hasProblems;
    private boolean hasErrors;
	private ClassFileScanner scanner;
	private boolean ignoreAnnotations;
    
    public enum DependencyResults {
        DEP_OK, // The dependency is correct
        DEP_OK_UNUSED, // The dependency was found in the repository but seems unused by the code
        DEP_MARK_SHARED, // The dependency was found but is part of the code's public API and should be marked shared
        DEP_MARK_UNSHARED, // The dependency was found but is not part of the code's public API and does not have to be marked shared
        DEP_NOT_FOUND, // The dependency was listed in the descriptor file but was not found in the repository
        DEP_CHECK_FAILED, // An error occurred while trying to check the dependency
        DEP_JDK, // The dependency is a Java SDK (JigSaw) module
        DEP_TRANSITIVE_ERROR; // An error occured while resolving transitive shared dependencies
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
         * @param shared True if the package appears in this module's public Api
         * @throws Exception Any exception thrown by the feedback handler
         */
        void packageName(String pkg, boolean shared) throws Exception;
        
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
         * @param shared True if this class is used in this module's public Api 
         * @throws Exception Any exception thrown by the feedback handler
         */
        void className(String cls, boolean shared) throws Exception;
        
        /**
         * Called after listing all separate class names. This can only happen within a call
         * to <code>listPackages()</code> or <code>listClasses()</code>
         * @throws Exception Any exception thrown by the feedback handler
         */
        void afterClasses() throws Exception;
    }
    
    /**
     * Set up the object with the given output and lookup repositories
     * @param moduleName The name for the published module
     * @param moduleVersion The version for the published module
     * @param outputRepository The destination repository to import the JAR into
     * @param lookupRepoman The repository for looking up dependencies
     */
    public LegacyImporter(String moduleName, String moduleVersion, File jarFile, RepositoryManager outputRepository, RepositoryManager lookupRepository) {
        this(moduleName, moduleVersion, jarFile, outputRepository, lookupRepository, null);
    }
    
	/**
     * Set up the object with the given output and lookup repositories and a callback
     * interface to receive feedback on progress and errors
     * @param moduleName The name for the published module
     * @param moduleVersion The version for the published module
     * @param outputRepository The destination repository to import the JAR into
     * @param lookupRepoman The repository for looking up dependencies
     * @param feedback Instance of ModuleCopycat.CopycatFeedback for receiving feedback
     */
    public LegacyImporter(String moduleName, String moduleVersion, File jarFile, RepositoryManager outputRepository, RepositoryManager lookupRepository, ImporterFeedback feedback) {
        this(moduleName, moduleVersion, jarFile, null, outputRepository, lookupRepository, new CMRJULLogger(), feedback);
    }
    
    /**
     * Set up the object with the given output and lookup repositories and a callback
     * interface to receive feedback on progress and errors
     * @param moduleName The name for the published module
     * @param moduleVersion The version for the published module
     * @param outputRepository The destination repository to import the JAR into
     * @param lookupRepoman The repository for looking up dependencies
     * @param log The logger to use
     * @param feedback Instance of ModuleCopycat.CopycatFeedback for receiving feedback
     */
    public LegacyImporter(String moduleName, String moduleVersion, File jarFile, File sourceJarFile, RepositoryManager outputRepository, RepositoryManager lookupRepository, Logger log, ImporterFeedback feedback) {
        assert(jarFile != null);
        assert(outputRepository != null);
        assert(lookupRepository != null);
        assert(log != null);
        this.moduleName = moduleName;
        this.moduleVersion = moduleVersion;
        this.jarFile = jarFile;
        this.sourceJarFile = sourceJarFile;
        this.outRepoman = outputRepository;
        this.lookupRepoman = lookupRepository;
        this.log = log;
        this.feedback = feedback;
        // FIXME: this probably needs to be an option
        this.jdkProvider = new JdkProvider();
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

    public LegacyImporter missingDependenciesPackages(Map<String,List<String>> missingDependenciesPackages){
    	if(missingDependenciesPackages == null)
    		return this;
    	this.missingDependenciesPackages = new HashMap<>();
    	for(Map.Entry<String,List<String>> entry : missingDependenciesPackages.entrySet()){
    		List<Pattern> patterns = new ArrayList<>(entry.getValue().size());
    		for(String pkg : entry.getValue()){
    			// match on the package regex plus whatever name of the class
    			String regex = wildcardToRegex(pkg);
    			Pattern pattern = Pattern.compile(regex);
				patterns.add(pattern);
    		}
			this.missingDependenciesPackages.put(entry.getKey(), patterns );
    	}
    	return this;
    }
    
    public LegacyImporter moduleDescriptor(File descriptorFile) {
        this.descriptorFile = descriptorFile;
        return this;
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
    public LegacyImporter loadModuleDescriptor() throws Exception {
        if (descriptorFile != null && !descriptorLoaded) {
        	ModuleInfo moduleInfo = null;
            if (descriptorFile.exists()) {
                if (descriptorFile.toString().toLowerCase().endsWith(".xml")) {
                    moduleInfo = XmlDependencyResolver.INSTANCE.resolveFromFile(descriptorFile, moduleName, 
                    		moduleVersion, lookupRepoman.getOverrides());
                } else if(descriptorFile.toString().toLowerCase().endsWith(".properties")) {
                    moduleInfo = PropertiesDependencyResolver.INSTANCE.resolveFromFile(descriptorFile, moduleName, 
                    		moduleVersion, lookupRepoman.getOverrides());
                }
                descriptorLoaded = true;
            }
            gatherExternalClasses(moduleInfo);
            if(moduleInfo != null)
            	checkModuleDescriptor(moduleInfo);
        }
        return this;
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
    public LegacyImporter listPackages(boolean makeSuggestions) throws Exception {
        gatherExternalClasses(null);
        
        if (scanner.hasExternalClasses()) {
            Set<String> externalPackages = scanner.getExternalPackages();
            Set<String> publicApiExternalPackages = scanner.getPublicApiExternalPackages();
            if (!externalPackages.isEmpty()) {
                Set<String> jdkModules = scanner.gatherJdkModules(externalPackages);
                Set<String> publicApiJdkModules = scanner.gatherJdkModules(publicApiExternalPackages);
                // the java.base import is always implied, let's not complain about it, unless we need to share it
                if(jdkModules.contains("java.base") && !publicApiJdkModules.contains("java.base"))
                	jdkModules.remove("java.base");
                if (!jdkModules.isEmpty()) {
                    feedback.beforeJdkModules();
                    for (String mod : jdkModules) {
                        ModuleDependencyInfo dep = new ModuleDependencyInfo(mod, jdkProvider.getJDKVersion(), 
                        		false, publicApiJdkModules.contains(mod));
                        feedback.dependency(DependencyResults.DEP_JDK, dep);
                        expectedDependencies.add(dep);
                    }
                    feedback.afterJdkModules();
                }
                if (!externalPackages.isEmpty()) {
                    feedback.beforePackages();
                    for (String pkg : externalPackages) {
                        feedback.packageName(pkg, publicApiExternalPackages.contains(pkg));
                        if (makeSuggestions) {
                            outputSuggestions(pkg);
                        }
                    }
                    hasErrors = true;
                    feedback.afterPackages();
                }
            }
            Set<String> externalDefaultClasses = scanner.getDefaultPackageClasses();
            Set<String> publicApiExternalDefaultClasses = scanner.getPublicApiDefaultPackageClasses();
            if (!externalDefaultClasses.isEmpty()) {
                feedback.beforeClasses();
                for (String cls : externalDefaultClasses) {
                    feedback.className(cls, publicApiExternalDefaultClasses.contains(cls));
                }
                hasErrors = true;
                feedback.afterClasses();
            }
        }
        
        return this;
    }
    
    /**
     * Will iterate over all the external classes (not matched by any dependency defined by
     * a previous call to <code>loadModuleDescriptor()</code>) calling the appropriate feedback
     * methods for each of them.
     * Feedback methods called: <code>beforeClasses()</code>, <code>afterClasses()</code> and
     * <code>className()</code>.
     * @throws Exception Any exception that can be thrown by the feedback handler
     */
    public LegacyImporter listClasses() throws Exception {
        gatherExternalClasses(null);
        
        if (scanner.hasExternalClasses()) {
            feedback.beforeClasses();
            for (String cls : scanner.getExternalClasses()) {
                feedback.className(cls, scanner.getPublicApiExternalClasses().contains(cls));
            }
            hasErrors = true;
            feedback.afterClasses();
        }
        
        return this;
    }
    
    /**
     * Updates (or creates) the module descriptor with the expected dependencies
     * @throws IOException Any exception that occurred during the update
     */
    public LegacyImporter updateModuleDescriptor() throws IOException {
        if (hasProblems) {
            if (descriptorFile != null) {
                if (descriptorFile.toString().toLowerCase().endsWith(".xml")) {
                    updateDescriptorXml();
                } else if (descriptorFile.toString().toLowerCase().endsWith(".properties")) {
                    updateDescriptorProperties();
                }
            }
        }
        return this;
    }

    /**
     * Publishes the JAR and accompanying module descriptor (if defined) to the
     * <code>outputRepository</code> specified in the constructor using the given
     * module name and version.
     */
    public LegacyImporter publish() {
        ArtifactContext context = new ArtifactContext(moduleName, moduleVersion, ArtifactContext.JAR);
        context.setForceOperation(true);
        outRepoman.putArtifact(context, jarFile);
        
        ShaSigner.signArtifact(outRepoman, context, jarFile, log);
        
        if (sourceJarFile != null) {
            ArtifactContext sourceJarContext = new ArtifactContext(moduleName, moduleVersion, ArtifactContext.LEGACY_SRC);
            context.setForceOperation(true);
            outRepoman.putArtifact(sourceJarContext, sourceJarFile);
            
            ShaSigner.signArtifact(outRepoman, sourceJarContext, sourceJarFile, log);
        }
        
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
        
        return this;
    }
    
    // Check the given dependencies for problems and at the same time
    // remove all classes that are found within the imported modules
    // from the given set of external class names
    private void checkModuleDescriptor(ModuleInfo dependencies) throws Exception {
        feedback.beforeDependencies();
        Set<String> visited = new HashSet<String>();
        checkDependencies(dependencies.getDependencies(), visited);
        feedback.afterDependencies();
    }
    
    private void checkDependencies(Set<ModuleDependencyInfo> dependencies, Set<String> visited) throws Exception {
        if (!dependencies.isEmpty()) {
            TreeSet<ModuleDependencyInfo> sortedDeps = new TreeSet<>(dependencies);
            for (ModuleDependencyInfo dep : sortedDeps) {
            	
            	// skip already-visited dependencies based on name/version key
            	if(!visited.add(dep.getModuleName()))
            		continue;
            	
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
                
                Usage usage = null;
                if (jdkProvider.isJDKModule(name)) {
                	usage = scanner.removeMatchingJdkClasses(name);
                } else {
                    ArtifactContext context = new ArtifactContext(name, dep.getVersion(), ArtifactContext.CAR, ArtifactContext.JAR);
                    ArtifactResult result = lookupRepoman.getArtifactResult(context);
                    File artifact = result != null ? result.artifact() : null;
                    if (artifact != null && artifact.exists()) {
                        try {
                            Set<String> importedClasses = JarUtils.gatherClassnamesFromJar(artifact);
                            addTransitiveDependenciesClasses(result, importedClasses, visited, dep);
                            usage = scanner.removeMatchingClasses(importedClasses);
                        } catch (IOException e) {
                            feedback.dependency(DependencyResults.DEP_CHECK_FAILED, dep);
                            hasErrors = true;
                        }
                    } else {
                        if(dep.isOptional()){
                        	String key = ModuleUtil.makeModuleName(dep.getName(), dep.getVersion());
                        	if(missingDependenciesPackages != null && missingDependenciesPackages.containsKey(key)){
                        		List<Pattern> packages = missingDependenciesPackages.get(key);
                        		usage = scanner.removeMatchingPackages(packages);
                        	}
                        }
                        if(usage == null){
                        	feedback.dependency(DependencyResults.DEP_NOT_FOUND, dep);
                        	hasErrors = true;
                        }
                    }
                }
                if(usage != null){
                    switch(usage) {
                    case Used:
                        if (!dep.isExport()) {
                            feedback.dependency(DependencyResults.DEP_OK, dep);
                        } else {
                            dep = new ModuleDependencyInfo(dep.getName(), dep.getVersion(), dep.isOptional(), false);
                            feedback.dependency(DependencyResults.DEP_MARK_UNSHARED, dep);
                        }
                        break;
                    case UsedInPublicApi:
                        if (dep.isExport()) {
                            feedback.dependency(DependencyResults.DEP_OK, dep);
                        } else {
                            dep = new ModuleDependencyInfo(dep.getName(), dep.getVersion(), dep.isOptional(), true);
                            feedback.dependency(DependencyResults.DEP_MARK_SHARED, dep);
                            hasProblems = true;
                        }
                        break;
                    default:
                    	// not used at all
                    	dep = new ModuleDependencyInfo(dep.getName(), dep.getVersion(), dep.isOptional(), false);
                    	feedback.dependency(DependencyResults.DEP_OK_UNUSED, dep);
                    }
                }
                feedback.afterDependency(dep);
                expectedDependencies.add(dep);
            }
        }
	}

	private void addTransitiveDependenciesClasses(ArtifactResult result, Set<String> classes, Set<String> visited, ModuleDependencyInfo originalDependency) throws Exception {
		log.info("Visiting transitive dependencies for "+result.name()+"/"+result.version());
		try{
		for(ArtifactResult dep : result.dependencies()){
			// skip non-shared dependencies
			if(dep.importType() == ImportType.EXPORT){
				// skip already-visited dependencies
				if(!visited.add(dep.name()+"/"+dep.version()))
					continue;
				/* FIXME: this is just wrong:
				 * - We should check for JDK
				 * - We should report errors with transitive paths
				 * - Finding a dep transitively puts it in the visited set and prevents toplevel imports from
				 *   being checked
				 */
				// skip JDK checks
                if (jdkProvider.isJDKModule(dep.name()))
                	continue;
				log.info(" dep "+dep.name()+"/"+dep.version());
				// look it up
				ArtifactContext context = new ArtifactContext(dep.name(), dep.version(), ArtifactContext.CAR, ArtifactContext.JAR);
				ArtifactResult depResult = lookupRepoman.getArtifactResult(context);
                File artifact = depResult != null ? depResult.artifact() : null;
        		log.info("Result: "+depResult);
                if (artifact != null && artifact.exists()) {
                    try {
                        Set<String> importedClasses = JarUtils.gatherClassnamesFromJar(artifact);
                        classes.addAll(importedClasses);
                        addTransitiveDependenciesClasses(depResult, classes, visited, originalDependency);
                    }catch(IOException x){
                    	feedback.dependency(DependencyResults.DEP_TRANSITIVE_ERROR, originalDependency);
                    	hasErrors = true;
                    }
                }else{
                	feedback.dependency(DependencyResults.DEP_TRANSITIVE_ERROR, originalDependency);
                	hasErrors = true;
                }
			}
		}
		}catch(Throwable t){
			t.printStackTrace();
		}
	}

	private static String wildcardToRegex(String wildcard){
		// ? -> .
		// * -> [^.]*
		// ** -> .*
    	StringBuffer strbuf = new StringBuffer(wildcard.length());
    	char[] chars = wildcard.toCharArray();
    	for (int i = 0; i < chars.length; i++) {
    		char c = chars[i];
    		if(c == '?'){
    			strbuf.append('.');
    			continue;
    		}
    		if(c == '*'){
    			if(i+1 < chars.length){
    				char next = chars[i+1];
    				if(next == '*'){
    	    			strbuf.append(".*");
    	    			i++;
    					continue;
    				}
    			}
    			strbuf.append("[^.]*");
    			continue;
    		}
    		// quote
    		strbuf.append("\\Q").append(c).append("\\E");
		}
    	// special case if we end with .** we want to turn it into (\..*)? to make the last dot optional
    	String ret = strbuf.toString();
    	if(ret.endsWith("\\Q.\\E.*"))
    		ret = ret.substring(0, ret.length()-7)+"(\\Q.\\E.*)?";
    	return ret;
    }
    
	// Create the set of class names for those types referenced by the
    // public API of the classes in the JAR we're importing and that are
    // not part of the JAR itself
    private void gatherExternalClasses(ModuleInfo moduleInfo) throws MalformedURLException, IOException {
        if (scanner == null) {
            if (!jarFile.isFile()) {
                throw new FileNotFoundException("Could not find " + jarFile);
            }
            scanner = new ClassFileScanner(jarFile, ignoreAnnotations, jdkProvider);
            expectedDependencies = new HashSet<>();
            scanner.scan(moduleInfo);
        }
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
        query.setJvmBinaryMajor(Versions.JVM_BINARY_MAJOR_VERSION);
        query.setJvmBinaryMinor(Versions.JVM_BINARY_MINOR_VERSION);
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

	public boolean isIgnoreAnnotations() {
		return ignoreAnnotations;
	}

	public void setIgnoreAnnotations(boolean ignoreAnnotations) {
		this.ignoreAnnotations = ignoreAnnotations;
	}
}
