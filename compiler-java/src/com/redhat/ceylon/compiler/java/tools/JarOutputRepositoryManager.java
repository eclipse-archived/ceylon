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

package com.redhat.ceylon.compiler.java.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import com.redhat.ceylon.cmr.api.ArtifactContext;
import com.redhat.ceylon.cmr.api.ArtifactCreator;
import com.redhat.ceylon.cmr.api.RepositoryManager;
import com.redhat.ceylon.cmr.ceylon.CeylonUtils;
import com.redhat.ceylon.cmr.impl.IOUtils;
import com.redhat.ceylon.cmr.impl.ShaSigner;
import com.redhat.ceylon.cmr.util.JarUtils;
import com.redhat.ceylon.cmr.util.JarUtils.JarEntryFilter;
import com.redhat.ceylon.common.Constants;
import com.redhat.ceylon.common.FileUtil;
import com.redhat.ceylon.common.JVMModuleUtil;
import com.redhat.ceylon.common.log.Logger;
import com.redhat.ceylon.compiler.java.loader.CeylonModelLoader;
import com.redhat.ceylon.javax.tools.JavaFileObject;
import com.redhat.ceylon.javax.tools.StandardLocation;
import com.redhat.ceylon.langtools.classfile.Annotation;
import com.redhat.ceylon.langtools.classfile.ClassFile;
import com.redhat.ceylon.langtools.classfile.ConstantPoolException;
import com.redhat.ceylon.langtools.source.util.TaskListener;
import com.redhat.ceylon.langtools.tools.javac.api.MultiTaskListener;
import com.redhat.ceylon.langtools.tools.javac.main.Option;
import com.redhat.ceylon.langtools.tools.javac.util.Log;
import com.redhat.ceylon.langtools.tools.javac.util.Options;
import com.redhat.ceylon.model.loader.AbstractModelLoader;
import com.redhat.ceylon.model.loader.ClassFileUtil;
import com.redhat.ceylon.model.loader.JdkProvider;
import com.redhat.ceylon.model.loader.NamingBase;
import com.redhat.ceylon.model.loader.OsgiUtil;
import com.redhat.ceylon.model.typechecker.model.Class;
import com.redhat.ceylon.model.typechecker.model.ClassOrInterface;
import com.redhat.ceylon.model.typechecker.model.Module;

public class JarOutputRepositoryManager {
    
    private Map<Module,ProgressiveJar> openJars = new HashMap<Module, ProgressiveJar>();
    private Log log;
    private Options options;
    private CeyloncFileManager ceyloncFileManager;
    private MultiTaskListener taskListener;
    private boolean aptRound;
    
    JarOutputRepositoryManager(Log log, Options options, CeyloncFileManager ceyloncFileManager, MultiTaskListener taskListener){
        this.log = log;
        this.options = options;
        this.ceyloncFileManager = ceyloncFileManager;
        this.taskListener = taskListener;
    }
    
    public JavaFileObject getFileObject(RepositoryManager repositoryManager, Module module, String fileName, File sourceFile) throws IOException{
        ProgressiveJar progressiveJar = getProgressiveJar(repositoryManager, module);
        return progressiveJar.getJavaFileObject(fileName, sourceFile);
    }
    
    private ProgressiveJar getProgressiveJar(RepositoryManager repositoryManager, Module module) throws IOException {
        ProgressiveJar jarFile = openJars.get(module);
        if(jarFile == null){
            jarFile = new ProgressiveJar(repositoryManager, module, log, options, ceyloncFileManager, taskListener, aptRound);
            openJars.put(module, jarFile);
        }
        return jarFile;
    }

    public void flush() throws IOException {
        Exception ex = null;
        try{
            for(ProgressiveJar jarFile : openJars.values()){
                try {
                    jarFile.close();
                } catch (Exception e) {
                    ex = e;
                }
            }
        }finally{
            // make sure we clear on return and throw, so we don't try to flush again on throw
            openJars.clear();
        }
        // Not the most elegant solution, we close all JAR files but we only
        // rethrow the last exception (if any)
        if (ex instanceof IOException) {
            throw (IOException)ex;
        } else if (ex instanceof RuntimeException) {
            throw (RuntimeException)ex;
        }
    }
    
    /***
     * Manages "updating" an existing jar file with the output from 
     * a compilation.*/
    static class ProgressiveJar {
       /* In fact the Java zip/jar APIs don't support updating, so 
        * we have to use temporary files. 
        * 
        * There's also an unspecified requirement that MANIFEST.MF 
        * come first in the archive (#5750). 
        * Since this can be added at any point via a call to
        * getJavaFileObject() we have to:
        * 
        * * Write manifest.jar to hold just the manifest as and when it gets added
        * * Write the rest of the generated files to rest.jar
        * * Then on close() we generate a final.jar by copying the entries from
        *   - manifest.jar and then
        *   - rest.jar
        *   - and then stuff from original.car
        * * And finally rename final.jar to original.car
        */
        private static final String META_INF = "META-INF";
        private static final String FILE_MAPPING = META_INF + "/mapping.txt";
        private static final String FILE_ERRORS = META_INF + "/errors.txt";
        private static final String FILE_HASHES = META_INF + "/hashes.txt";
        private static final String QUOTED_MODULE_DESCRIPTOR = NamingBase.MODULE_DESCRIPTOR_CLASS_NAME + ".class";

        private static final String ANNOTATION_COMPILE_ERROR = "com.redhat.ceylon.compiler.java.metadata.CompileTimeError";
        
        /** Jar we're "updating" (i.e. will copy any entries not added to the output */
        private File originalJarFile;
        
        /** Jar we're actually generating, but doesn't include the MANIFEST.MF */
        private File outputJarTempFolder;
        
        final private Set<String> modifiedSourceFiles = new HashSet<String>();
        final private Set<String> modifiedResourceFilesRel = new HashSet<String>();
        final private Set<String> modifiedResourceFilesFull = new HashSet<String>();
        /** Mapping of class file name to originating source file name*/
        final private Properties writtenClassesMapping = new Properties();
        final private Properties entrySourceMapping = new Properties();
        private Logger cmrLog;
        private Options options;
        private RepositoryManager repoManager;
        private ArtifactContext carContext;
        private ArtifactCreator srcCreator;
        private ArtifactCreator resourceCreator;
        private Module module;
        /** Whether to generate an OSGi-compatible MANIFEST.MF */
        private boolean writeOsgiManifest;
        /** The bundles to treat as "provided", thus omitted from {@code Required-Bundle:} */
        private String osgiProvidedBundles;
        private final String resourceRootPath;
        /** Whether to add a pom.xml and pom.properties in module subdir of {@code META-INF}*/
        private boolean writeMavenManifest;
        /** Whether to add a module-info.class file */
        private boolean writeJava9Module;
        private MultiTaskListener taskListener;
        private JarEntryManifestFileObject manifest;
        private Log log;
		private JdkProvider jdkProvider;
        private Map<ClassOrInterface, Set<Class>> services;
        private boolean validModule;

        public ProgressiveJar(RepositoryManager repoManager, Module module, Log log, 
        		Options options, CeyloncFileManager ceyloncFileManager, 
        		MultiTaskListener taskListener, boolean aptRound) throws IOException{
            this.options = options;
            this.repoManager = repoManager;
            this.carContext = new ArtifactContext(null, module.getNameAsString(), module.getVersion(), ArtifactContext.CAR);
            this.log = log;
            this.cmrLog = new JavacLogger(options, Log.instance(ceyloncFileManager.getContext()));
            AbstractModelLoader modelLoader = CeylonModelLoader.instance(ceyloncFileManager.getContext());
            this.jdkProvider = modelLoader.getJdkProvider();
            this.srcCreator = CeylonUtils.makeSourceArtifactCreator(
                    repoManager,
                    ceyloncFileManager.getLocation(StandardLocation.SOURCE_PATH),
                    module.getNameAsString(), module.getVersion(),
                    options.get(Option.VERBOSE) != null, cmrLog);
            this.resourceCreator = CeylonUtils.makeResourceArtifactCreator(
                    repoManager,
                    ceyloncFileManager.getLocation(StandardLocation.SOURCE_PATH),
                    ceyloncFileManager.getLocation(CeylonLocation.RESOURCE_PATH),
                    options.get(Option.CEYLONRESOURCEROOT),
                    module.getNameAsString(), module.getVersion(),
                    options.get(Option.VERBOSE) != null, cmrLog);
            this.module = module;
            this.writeOsgiManifest = !options.isSet(Option.CEYLONNOOSGI);
            this.osgiProvidedBundles = options.get(Option.CEYLONOSGIPROVIDEDBUNDLES);
            this.writeMavenManifest = !options.isSet(Option.CEYLONNOPOM) && !module.isDefaultModule();
            this.writeJava9Module= options.isSet(Option.CEYLONJIGSAW) && !module.isDefaultModule();
            this.services = module.getServices();
            
            // Determine the special path that signals that the files it contains
            // should be moved to the root of the output JAR/CAR
            String rrp = module.getNameAsString().replace('.', '/');
            if (!rrp.isEmpty() && !rrp.endsWith("/")) {
                rrp = rrp + "/";
            }
            String rootName = options.get(Option.CEYLONRESOURCEROOT);
            if (rootName == null) {
                rootName = Constants.DEFAULT_RESOURCE_ROOT;
            }
            this.resourceRootPath = rrp + rootName + "/";
            this.taskListener = taskListener;
            
            this.originalJarFile = repoManager.getArtifact(carContext);
            this.outputJarTempFolder = FileUtil.makeTempDir("ceylon-compiler-");
            this.validModule = module.isDefaultModule() || aptRound;
        }

        private Map<String, Set<String>> getPreviousServices() throws IOException {
            Map<String, Set<String>> result;
            if (originalJarFile != null) {
                result = MetaInfServices.parseAllServices(originalJarFile);
            } else {
                result = new HashMap<String, Set<String>>(0);
            }
            return result;
        }
        
        private Properties getPreviousMapping() throws IOException {
            return JarUtils.getMetaInfProperties(originalJarFile, FILE_MAPPING);
        }

        private Properties getPreviousErrors() throws IOException {
            return JarUtils.getMetaInfProperties(originalJarFile, FILE_ERRORS);
        }

        private Properties getPreviousHashes() throws IOException {
            return JarUtils.getMetaInfProperties(originalJarFile, FILE_HASHES);
        }

        private Manifest getPreviousManifest() throws IOException {
            JarFile jarFile = JarUtils.validJar(originalJarFile);
            if (jarFile != null) {
                try {
                    return jarFile.getManifest();
                } finally {
                    jarFile.close();
                }
            }
            return null;
        }

        public void close() throws IOException {
            try {
                // Create the .src archive
                Set<String> copiedSourceFiles = srcCreator.copy(modifiedSourceFiles);
                resourceCreator.copy(modifiedResourceFilesFull);
                
                Manifest manifestObject = null;
                // Add META-INF/MANIFEST.MF
                if (writeOsgiManifest && manifest == null) {
                    // Copy the previous manifest
                    manifestObject = (module.isDefaultModule() 
                    		? new OsgiUtil.DefaultModuleManifest() 
                                    // using old compiler-generated manifest, so don't worry about conflicts: null logger 
                    	    : new OsgiUtil.OsgiManifest(module, jdkProvider, osgiProvidedBundles, getPreviousManifest(), null)).build();
                } else if (manifest != null && !module.isDefaultModule()) {
                    // Use the added manifest
                    manifestObject = manifest.writeManifest(new JavacLogger(options, log));
                }

                // Add META-INF/.../pom.xml and pom.properties
                if (writeMavenManifest) {
                    writeMavenManifest(outputJarTempFolder, module);
                }

                // Add module-info.class
                if (writeJava9Module && !module.isDefaultModule()) {
                    writeJava9Module(outputJarTempFolder, module);
                }

                // Add META-INF/mapping.txt
                Properties previousMapping = getPreviousMapping();
                JarEntryFilter jarFilter = getJarFilter(previousMapping, copiedSourceFiles);
                writeMappingJarEntry(outputJarTempFolder, previousMapping, jarFilter);
                
                // Write services to the META-INF/services
                writeServicesJarEntry(outputJarTempFolder);
                
                // Add META-INF/errors.txt
                writeErrorsJarEntry(outputJarTempFolder, copiedSourceFiles);
                
                // Add META-INF/hashes.txt
                writeHashesJarEntry(outputJarTempFolder);
                
                // Now add the old jar remains
                if (originalJarFile != null && JarUtils.isValidJar(originalJarFile)) {
                    addOriginalJarFiles(outputJarTempFolder, originalJarFile, jarFilter);
                }

                // We only create the final .car file if we found a module descriptor.
                // Otherwise something has gone seriously wrong during compilation and
                // we would be generating output that does more harm than good
                if (validModule) {
                    File finalCarFile = IOUtils.zipFolder(manifestObject, outputJarTempFolder);
                
                    if (options.isSet(Option.CEYLONPACK200)) {
                        JarUtils.repack(finalCarFile, cmrLog);
                    }
                    File sha1File = ShaSigner.sign(finalCarFile, cmrLog, options.get(Option.VERBOSE) != null);
                    JarUtils.publish(finalCarFile, sha1File, carContext, repoManager, cmrLog);
                    
                    String info;
                    if(module.isDefaultModule())
                        info = module.getNameAsString();
                    else
                        info = module.getNameAsString() + "/" + module.getVersion();
                    cmrLog.info("Created module " + info);
                    if(taskListener != null){
                        for(TaskListener listener : taskListener.getTaskListeners()){
                            if(listener instanceof CeylonTaskListener){
                                ((CeylonTaskListener) listener).moduleCompiled(module.getNameAsString(), module.getVersion());
                            }
                        }
                    }
                }
            } catch (RuntimeException e) {
                throw e;
            } finally {
                FileUtil.deleteQuietly(outputJarTempFolder);
            }
        }

        private void addOriginalJarFiles(File outputJarTempFolder, File originalJarFile, JarEntryFilter filter) throws IOException {
            try (JarFile j = new JarFile(originalJarFile)) {
                Enumeration<JarEntry> inEntries = j.entries();
                while (inEntries.hasMoreElements()) {
                    JarEntry entry = inEntries.nextElement();
                    String name = entry.getName();
                    // is the file a module descriptor?
                    if (name.equals(QUOTED_MODULE_DESCRIPTOR)
                            || name.endsWith("/" + QUOTED_MODULE_DESCRIPTOR)) {
                        // Mark this module as being valid (enough)
                        validModule = true;
                    }
                    if (filter != null && filter.avoid(name)) {
                        continue;
                    }
                    File targetFile = new File(outputJarTempFolder, name);
                    if (!targetFile.exists()) {// first in wins
                        if(entry.isDirectory())
                            FileUtil.mkdirs(targetFile);
                        else{
                            FileUtil.mkdirs(targetFile.getParentFile());
                            try (InputStream in = j.getInputStream(entry);
                                 OutputStream out = new FileOutputStream(targetFile)) {
                                JarUtils.copy(in, out);
                            }
                        }
                    }
                }
            }
        }

        private JarUtils.JarEntryFilter getJarFilter(final Properties previousMapping, final Set<String> copiedSourceFiles) {
            return new JarUtils.JarEntryFilter() {
                @Override
                public boolean avoid(String entryFullName) {
                    if (entryFullName.endsWith(".class")) {
                        boolean classWasUpdated = writtenClassesMapping.containsKey(entryFullName);
                        if (previousMapping != null) {
                            String sourceFileForClass = previousMapping.getProperty(entryFullName);
                            classWasUpdated = classWasUpdated || copiedSourceFiles.contains(sourceFileForClass);
                        }
                        return classWasUpdated;
                    } else {
                        return modifiedResourceFilesRel.contains(entryFullName)
                            || entryFullName.equals(FILE_MAPPING)
                            || entryFullName.equals(FILE_ERRORS)
                            || entryFullName.equals(FILE_HASHES)
                            || (writeOsgiManifest && OsgiUtil.OsgiManifest.isManifestFileName(entryFullName))
                            || (writeMavenManifest && MavenPomUtil.isMavenDescriptor(entryFullName, module));
                    }
                }
            };
        }

        /** 
         * Add 
         * {@code META-INF/maven/<groupId>/<artifactId>/pom.xml}
         * and {@code META-INF/maven/<groupId>/<artifactId>/pom.properties}
         * entries for the given {@code module}.
         */
        private void writeMavenManifest(File outputFolder, Module module) {
            MavenPomUtil.writeMavenManifest2(outputFolder, module, jdkProvider);
        }

        private void writeJava9Module(File outputFolder, Module module) {
        	Java9Util.writeModuleDescriptor(outputFolder, new Java9Util.Java9ModuleDescriptor(module));
        }

        /** 
         * Add {@code META-INF/services/} entries for each of the services 
         * we've seen, plus all those which were already present 
         * (incremental compilation).
         * @throws IOException 
         */
        private void writeServicesJarEntry(File outputFolder) throws IOException {
            
            // First convert declarations into their java names
            Map<String, Set<String>> compiledServices = new HashMap<String, Set<String>>(services.size());
            for (Map.Entry<ClassOrInterface, Set<Class>> entry : services.entrySet()) {
                ClassOrInterface service = entry.getKey();
                Set<Class> impls = entry.getValue();
                HashSet<String> implNames = new HashSet<String>(impls.size());
                for (Class impl : impls) {
                    // TODO quoting
                    implNames.add(JVMModuleUtil.quoteJavaKeywords(impl.getQualifiedNameString().replace("::", ".")));
                }
                // TODO quoting (binary name)
                compiledServices.put(JVMModuleUtil.quoteJavaKeywords(service.getQualifiedNameString().replace("::", ".")), implNames);
            }
            
            // Now figure out which of the previous services we need to keep
            Map<String, Set<String>> previousServices = getPreviousServices();
            
            // Find out which service interfaces have been deleted, and delete those keys from previousServices
            // Find out which service implementations have been deleted, and delete those values from previousServices
            // Now remove all the old service implementations which we compiled from the values of previousServices
            // Now add all the service implementations which we compiled
            
            // TODO for now 
            for (Map.Entry<String, Set<String>> e : compiledServices.entrySet()) {
                Set<String> set = previousServices.get(e.getKey());
                if (set == null) {
                    previousServices.put(e.getKey(), e.getValue());
                } else {
                    set.addAll(e.getValue());
                }
            }
            
            FileUtil.mkdirs(new File(outputFolder, META_INF+"/services/"));
            
            MetaInfServices.writeAllServices(outputFolder, previousServices);
        }

        /** 
         * Add a {@code META-INF/mapping.txt} entry
         * which records which source files generated which .class files
         */
        private void writeMappingJarEntry(File outputFolder, Properties previousMapping, JarUtils.JarEntryFilter filter) {
            Properties newMapping = new Properties();
            newMapping.putAll(writtenClassesMapping);
            if (previousMapping != null) {
                // Add the previous mapping entries that are not related to an updated source file 
                for (String classFullName : previousMapping.stringPropertyNames()) {
                    if (!filter.avoid(classFullName)) {
                        newMapping.setProperty(classFullName, previousMapping.getProperty(classFullName));
                    }
                }
            }
            // Write the mapping file to the Jar
            FileUtil.mkdirs(new File(outputFolder, META_INF));
            try (OutputStream os = new FileOutputStream(new File(outputFolder, FILE_MAPPING))) {
                newMapping.store(os, "List of all class files and their originating source files");
            } catch (IOException e) {
                // TODO : log to the right place
            }
        }

        /** 
         * Add a {@code META-INF/errors.txt} entry
         * which records which .class files have compile errors
         * (ie the classes have CompileTimeError annotations)
         */
        private void writeErrorsJarEntry(File outputFolder, Set<String> copiedSourceFiles) throws IOException {
            Properties newErrors = new Properties();
            // Check the new class files for errors
            for (String classFullName : writtenClassesMapping.stringPropertyNames()) {
                if (hasErrors(new File(outputFolder, classFullName))) {
                    String sourceFile = writtenClassesMapping.getProperty(classFullName);
                    newErrors.setProperty(classFullName, sourceFile);
                }
            }
            Properties previousErrors = getPreviousErrors();
            if (previousErrors != null) {
                // Add the previous error entries that are not related to an updated source file
                for (String classFullName : previousErrors.stringPropertyNames()) {
                    String sourceFile = previousErrors.getProperty(classFullName);
                    if (!copiedSourceFiles.contains(sourceFile)) {
                        newErrors.setProperty(classFullName, sourceFile);
                    }
                }
            }
            // Write the errors file to the Jar
            FileUtil.mkdirs(new File(outputFolder, META_INF));
            try (OutputStream os = new FileOutputStream(new File(outputFolder, FILE_ERRORS))) {
                newErrors.store(os, "List of class files that have compile errors");
            } catch(IOException e) {
                // TODO : log to the right place
            }
        }

        /** 
         * Determines if the given .class file has compile errors
         * (ie the class has a CompileTimeError annotation)
         */
        private boolean hasErrors(File classFile) {
            try (InputStream stream = new FileInputStream(classFile)) {
                ClassFile cls = ClassFile.read(stream);
                Annotation annot = ClassFileUtil.findAnnotation(cls, ANNOTATION_COMPILE_ERROR);
                return annot != null;
            } catch (ConstantPoolException e) {
                return true;
            } catch (IOException e) {
                return true;
            }
        }

        /** 
         * Add a {@code META-INF/hashes.txt} entry
         * which records the SHA1 hashes for all input files
         */
        private void writeHashesJarEntry(File outputFolder) throws IOException {
            Properties newHashes = new Properties();
            // Add the SHA1 hashes for all the new/updated (re)source files
            for (String entryName : entrySourceMapping.stringPropertyNames()) {
                File file = new File(entrySourceMapping.getProperty(entryName));
                newHashes.setProperty(entryName, ShaSigner.sha1(file));
            }
            Properties previousHashes = getPreviousHashes();
            if (previousHashes != null) {
                // Add the previous hash entries that are not related to an updated (re)source file
                for (String inputFile : previousHashes.stringPropertyNames()) {
                    if (!entrySourceMapping.keySet().contains(inputFile)) {
                        String hash = previousHashes.getProperty(inputFile);
                        newHashes.setProperty(inputFile, hash);
                    }
                }
            }
            // Write the errors file to the Jar
            FileUtil.mkdirs(new File(outputFolder, META_INF));
            try (OutputStream os = new FileOutputStream(new File(outputFolder, FILE_HASHES))) {
                newHashes.store(os, "List of input (re)source files and their SHA1 hashes");
            } catch(IOException e) {
                // TODO : log to the right place
            }
        }
        
        public JavaFileObject getJavaFileObject(String fileName, File sourceFile) {
            String quotedFileName = JVMModuleUtil.quoteJavaKeywordsInFilename(fileName);
            String entryName = handleResourceRoot(quotedFileName);
            if (sourceFile != null) {
                modifiedSourceFiles.add(sourceFile.getPath());
                // record the class file we produce so that we don't save it from the original jar
                String sourcePath = JarUtils.toPlatformIndependentPath(srcCreator.getPaths(), sourceFile.getPath());
                writtenClassesMapping.put(entryName, sourcePath);
                entrySourceMapping.put(sourcePath, sourceFile.getPath());
                // is the file a module descriptor?
                if (Constants.MODULE_DESCRIPTOR.equals(sourceFile.getName())) {
                    // Mark this module as being valid (enough)
                    validModule = true;
                }
            } else {
                modifiedResourceFilesRel.add(entryName);
                File full = FileUtil.applyPath(resourceCreator.getPaths(), fileName);
                // this can be null for APT-generated resources
                if (full != null) {
                    modifiedResourceFilesFull.add(full.getPath());
                    String resourcePath = JarUtils.toPlatformIndependentPath(resourceCreator.getPaths(), full.getPath());
                    entrySourceMapping.put(handleResourceRoot(resourcePath), full.getPath());
                }
                // FIXME: surely this can be written much easily
                if (OsgiUtil.CeylonManifest.isManifestFileName(entryName) && 
                        (module.isDefaultModule() || writeOsgiManifest)) {
                    manifest = new JarEntryManifestFileObject(outputJarTempFolder, entryName, 
                    		module, osgiProvidedBundles, jdkProvider);
                    return manifest;
                }
            }
            return new JarEntryFileObject(outputJarTempFolder, entryName);
        }

        private String handleResourceRoot(String entryName) {
            if (!resourceRootPath.isEmpty() && entryName.startsWith(resourceRootPath)) {
                // Files in the special "resource root path" get moved
                // to the root of the output JAR/CAR
                entryName = entryName.substring(resourceRootPath.length());
            }
            return entryName;
        }
    }

    public void setAptRound(boolean b) {
        // make sure we mark all future modules as valid
        this.aptRound = b;
        // and past ones too
        for (ProgressiveJar jar : openJars.values()) {
            jar.validModule = b;
        }
    }
}
