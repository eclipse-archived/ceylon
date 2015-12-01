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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.zip.ZipEntry;

import com.redhat.ceylon.cmr.api.ArtifactContext;
import com.redhat.ceylon.cmr.api.ArtifactCreator;
import com.redhat.ceylon.cmr.api.RepositoryManager;
import com.redhat.ceylon.cmr.ceylon.CeylonUtils;
import com.redhat.ceylon.cmr.impl.ShaSigner;
import com.redhat.ceylon.cmr.util.JarUtils;
import com.redhat.ceylon.cmr.util.JarUtils.JarEntryFilter;
import com.redhat.ceylon.common.Constants;
import com.redhat.ceylon.common.FileUtil;
import com.redhat.ceylon.common.log.Logger;
import com.redhat.ceylon.compiler.java.tools.JarEntryManifestFileObject.OsgiManifest;
import com.redhat.ceylon.javax.tools.JavaFileObject;
import com.redhat.ceylon.javax.tools.StandardLocation;
import com.redhat.ceylon.langtools.source.util.TaskListener;
import com.redhat.ceylon.langtools.tools.javac.main.OptionName;
import com.redhat.ceylon.langtools.tools.javac.util.Log;
import com.redhat.ceylon.langtools.tools.javac.util.Options;
import com.redhat.ceylon.compiler.java.tools.JarEntryManifestFileObject.DefaultModuleManifest;
import com.redhat.ceylon.compiler.java.tools.JarEntryManifestFileObject.CeylonManifest;
import com.redhat.ceylon.model.typechecker.model.Module;

public class JarOutputRepositoryManager {
    
    private Map<Module,ProgressiveJar> openJars = new HashMap<Module, ProgressiveJar>();
    private Log log;
    private Options options;
    private CeyloncFileManager ceyloncFileManager;
    private TaskListener taskListener;
    
    JarOutputRepositoryManager(Log log, Options options, CeyloncFileManager ceyloncFileManager, TaskListener taskListener){
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
            jarFile = new ProgressiveJar(repositoryManager, module, log, options, ceyloncFileManager, taskListener);
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
        if (ex instanceof IOException) {
            throw (IOException)ex;
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
        private static final String MAPPING_FILE = META_INF+"/mapping.txt";
        /** Jar we're "updating" (i.e. will copy any entries not added to the output */
        private File originalJarFile;
        /** Jar we're actually generating, but doesn't include the MANIFEST.MF */
        private File outputJarFile;
        /** Output stream for the {@link #outputJarFile} */
        private JarOutputStream jarOutputStream;
        final private Set<String> modifiedSourceFiles = new HashSet<String>();
        final private Set<String> modifiedResourceFilesRel = new HashSet<String>();
        final private Set<String> modifiedResourceFilesFull = new HashSet<String>();
        /** Mapping of class file name to originating source file name*/
        final private Properties writtenClassesMapping = new Properties(); 
        private Logger cmrLog;
        private Options options;
        private RepositoryManager repoManager;
        private ArtifactContext carContext;
        private ArtifactCreator srcCreator;
        private ArtifactCreator resourceCreator;
        final private Set<String> foldersToAdd = new HashSet<String>();
        private Module module;
        /** Whether to generate an OSGi-compatible MANIFEST.MF */
        private boolean writeOsgiManifest;
        /** The bundles to treat as "provided", thus omitted from {@code Required-Bundle:} */
        private String osgiProvidedBundles;
        private final String resourceRootPath;
        /** Whether to add a pom.xml and pom.properties in module subdir of {@code META-INF}*/
        private boolean writeMavenManifest;
        private TaskListener taskListener;
        private JarEntryManifestFileObject manifest;
        private Log log;

        public ProgressiveJar(RepositoryManager repoManager, Module module, Log log, Options options, CeyloncFileManager ceyloncFileManager, TaskListener taskListener) throws IOException{
            this.options = options;
            this.repoManager = repoManager;
            this.carContext = new ArtifactContext(module.getNameAsString(), module.getVersion(), ArtifactContext.CAR);
            this.log = log;
            this.cmrLog = new JavacLogger(options, Log.instance(ceyloncFileManager.getContext()));
            this.srcCreator = CeylonUtils.makeSourceArtifactCreator(
                    repoManager,
                    ceyloncFileManager.getLocation(StandardLocation.SOURCE_PATH),
                    module.getNameAsString(), module.getVersion(),
                    options.get(OptionName.VERBOSE) != null, cmrLog);
            this.resourceCreator = CeylonUtils.makeResourceArtifactCreator(
                    repoManager,
                    ceyloncFileManager.getLocation(StandardLocation.SOURCE_PATH),
                    ceyloncFileManager.getLocation(CeylonLocation.RESOURCE_PATH),
                    options.get(OptionName.CEYLONRESOURCEROOT),
                    module.getNameAsString(), module.getVersion(),
                    options.get(OptionName.VERBOSE) != null, cmrLog);
            this.module = module;
            this.writeOsgiManifest = !options.isSet(OptionName.CEYLONNOOSGI);
            this.osgiProvidedBundles = options.get(OptionName.CEYLONOSGIPROVIDEDBUNDLES);
            this.writeMavenManifest = !options.isSet(OptionName.CEYLONNOPOM) && !module.isDefault();
            
            // Determine the special path that signals that the files it contains
            // should be moved to the root of the output JAR/CAR
            String rrp = module.getNameAsString().replace('.', '/');
            if (!rrp.isEmpty() && !rrp.endsWith("/")) {
                rrp = rrp + "/";
            }
            String rootName = options.get(OptionName.CEYLONRESOURCEROOT);
            if (rootName == null) {
                rootName = Constants.DEFAULT_RESOURCE_ROOT;
            }
            this.resourceRootPath = rrp + rootName + "/";
            this.taskListener = taskListener;
            
            this.originalJarFile = repoManager.getArtifact(carContext);
            this.outputJarFile = File.createTempFile("ceylon-compiler-", ".car");
            this.jarOutputStream = new JarOutputStream(new FileOutputStream(outputJarFile));
        }

        private Properties getPreviousMapping() throws IOException {
            if (originalJarFile != null) {
                JarFile jarFile = null;
                jarFile = new JarFile(originalJarFile);
                try {
                    JarEntry entry = jarFile.getJarEntry(MAPPING_FILE);
                    if (entry != null) {
                        InputStream inputStream = jarFile.getInputStream(entry);
                        try {
                            Properties previousMapping = new Properties();
                            previousMapping.load(inputStream);
                            return previousMapping;
                        } finally {
                            inputStream.close();
                        }
                    }
                } finally {
                    jarFile.close();
                }
            }
            return null;
        }

        private Manifest getPreviousManifest() throws IOException {
            if (originalJarFile != null) {
                JarFile jarFile = null;
                jarFile = new JarFile(originalJarFile);
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
                
                jarOutputStream.flush();
                jarOutputStream.close();
                
                // Create a jar with the MANIFEST.MF first
                File metaFirstFile = File.createTempFile("compile", "car");
                JarOutputStream manifestFirst = new JarOutputStream(new FileOutputStream(metaFirstFile));
                
                // Add META-INF/
                Set<String> foldersAdded = new HashSet<String>();
                JarUtils.makeFolder(foldersAdded, manifestFirst, META_INF+"/");
                
                // Add META-INF/MANIFEST.MF
                if (writeOsgiManifest && manifest == null) {
                    // Copy the previous manifest
                    Manifest manifest = (module.isDefault() ? new DefaultModuleManifest() : new OsgiManifest(module, getPreviousManifest(), osgiProvidedBundles, null)).build();
                    writeManifestJarEntry(manifestFirst, manifest);
                } else if (manifest != null && !module.isDefault()) {
                    // Use the added manifest
                    manifest.writeManifest(manifestFirst, log);
                }
                
                // Add META-INF/.../pom.xml and pom.properties
                if (writeMavenManifest) {
                    writeMavenManifest(foldersAdded, manifestFirst, module);
                }
                
                // Add META-INF/mapping.txt
                Properties previousMapping = getPreviousMapping();
                JarEntryFilter jarFilter = getJarFilter(previousMapping, copiedSourceFiles);
                writeMappingJarEntry(manifestFirst, foldersAdded, previousMapping, jarFilter);
                
                manifestFirst.close();
                
                File finalCarFile = File.createTempFile("ceylon-compiler-", ".car");
                JarCat jc = new JarCat(finalCarFile);
                jc.cat(metaFirstFile);
                jc.cat(outputJarFile);
                jc.cat(originalJarFile, jarFilter);
                jc.close();
                FileUtil.deleteQuietly(metaFirstFile);
            
                if (options.isSet(OptionName.CEYLONPACK200)) {
                    JarUtils.repack(finalCarFile, cmrLog);
                }
                File sha1File = ShaSigner.sign(finalCarFile, cmrLog, options.get(OptionName.VERBOSE) != null);
                JarUtils.publish(finalCarFile, sha1File, carContext, repoManager, cmrLog);
                
                String info;
                if(module.isDefault())
                    info = module.getNameAsString();
                else
                    info = module.getNameAsString() + "/" + module.getVersion();
                cmrLog.info("Created module " + info);
                if(taskListener instanceof CeylonTaskListener){
                    ((CeylonTaskListener) taskListener).moduleCompiled(module.getNameAsString(), module.getVersion());
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (RuntimeException e) {
                throw e;
            } finally {
                FileUtil.deleteQuietly(outputJarFile);
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
                                || entryFullName.equals(MAPPING_FILE)
                                || (writeOsgiManifest && OsgiManifest.isManifestFileName(entryFullName))
                                || (writeMavenManifest && MavenPomUtil.isMavenDescriptor(entryFullName, module));
                    }
                }
            };
        }

        /** Add a {@code META-INF/MANIFEST.MF} entry using the given {@code manifest} */
        private static void writeManifestJarEntry(JarOutputStream out, Manifest manifest) {
            try {
                out.putNextEntry(new ZipEntry(OsgiManifest.MANIFEST_FILE_NAME));
                manifest.write(out);
            }
            catch (IOException e) {
                // TODO : log to the right place
            }
            finally {
                try {
                    out.closeEntry();
                }
                catch (IOException ignore) {
                }
            }
        }
        
        /** 
         * Add 
         * {@code META-INF/maven/<groupId>/<artifactId>/pom.xml}
         * and {@code META-INF/maven/<groupId>/<artifactId>/pom.properties}
         * entries for the given {@code module}.
         * @param manifestFirst 
         */
        private void writeMavenManifest(Set<String> foldersAlreadyAdded, JarOutputStream manifestFirst, Module module) {
            MavenPomUtil.writeMavenManifest2(manifestFirst, module, foldersAlreadyAdded);
        }

        /** 
         * Add a {@code META-INF/mapping.txt} entry
         * which records which source files generated which .class files
         * @param outputStream 
         */
        private void writeMappingJarEntry(JarOutputStream outputStream, Set<String> foldersAlreadyAdded, Properties previousMapping, JarUtils.JarEntryFilter filter) {
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
            try {
                JarUtils.makeFolder(foldersAlreadyAdded, outputStream, META_INF+"/");
                outputStream.putNextEntry(new ZipEntry(MAPPING_FILE));
                newMapping.store(outputStream, "");
            }
            catch(IOException e) {
                // TODO : log to the right place
            }
            finally {
                try {
                    outputStream.closeEntry();
                } catch (IOException e) {
                }
            }
        }

        public JavaFileObject getJavaFileObject(String fileName, File sourceFile) {
            String entryName = fileName.replace(File.separatorChar, '/');
            
            if (!resourceRootPath.isEmpty() && entryName.startsWith(resourceRootPath)) {
                // Files in the special "resource root path" get moved
                // to the root of the output JAR/CAR
                entryName = entryName.substring(resourceRootPath.length());
            }
            
            String folder = JarUtils.getFolder(entryName);
            if (folder != null) {
                foldersToAdd.add(folder);
            }

            if (sourceFile != null) {
                modifiedSourceFiles.add(sourceFile.getPath());
                // record the class file we produce so that we don't save it from the original jar
            	addMappingEntry(entryName, JarUtils.toPlatformIndependentPath(srcCreator.getPaths(), sourceFile.getPath()));
            } else {
                modifiedResourceFilesRel.add(entryName);
                modifiedResourceFilesFull.add(FileUtil.applyPath(resourceCreator.getPaths(), fileName).getPath());
                if (CeylonManifest.isManifestFileName(entryName) && 
                        (module.isDefault() || writeOsgiManifest)) {
                    manifest = new JarEntryManifestFileObject(outputJarFile.getPath(), entryName, module, osgiProvidedBundles);
                    return manifest;
                }
            }
            return new JarEntryFileObject(outputJarFile.getPath(), jarOutputStream, entryName);
        }

        private void addMappingEntry(String className,
                String sourcePath) {
            writtenClassesMapping.put(className, sourcePath);
        }
    }
}
