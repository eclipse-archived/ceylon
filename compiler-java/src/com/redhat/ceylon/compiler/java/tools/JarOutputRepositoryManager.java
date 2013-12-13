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

import static com.redhat.ceylon.compiler.java.tools.JarEntryManifestFileObject.OsgiManifest;

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

import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;

import com.redhat.ceylon.cmr.api.ArtifactContext;
import com.redhat.ceylon.cmr.api.Logger;
import com.redhat.ceylon.cmr.api.RepositoryManager;
import com.redhat.ceylon.cmr.api.SourceArchiveCreator;
import com.redhat.ceylon.cmr.ceylon.CeylonUtils;
import com.redhat.ceylon.cmr.util.JarUtils;
import com.redhat.ceylon.compiler.typechecker.model.Module;
import com.sun.tools.javac.main.OptionName;
import com.sun.tools.javac.util.Log;
import com.sun.tools.javac.util.Options;

public class JarOutputRepositoryManager {
    
    private Map<Module,ProgressiveJar> openJars = new HashMap<>();
    private Log log;
    private Options options;
    private CeyloncFileManager ceyloncFileManager;
    
    JarOutputRepositoryManager(Log log, Options options, CeyloncFileManager ceyloncFileManager){
        this.log = log;
        this.options = options;
        this.ceyloncFileManager = ceyloncFileManager;
    }
    
    public JavaFileObject getFileObject(RepositoryManager repositoryManager, Module module, String fileName, File sourceFile) throws IOException{
        ProgressiveJar progressiveJar = getProgressiveJar(repositoryManager, module);
        return progressiveJar.getJavaFileObject(fileName, sourceFile);
    }
    
    private ProgressiveJar getProgressiveJar(RepositoryManager repositoryManager, Module module) throws IOException {
        ProgressiveJar jarFile = openJars.get(module);
        if(jarFile == null){
            jarFile = new ProgressiveJar(repositoryManager, module, log, options, ceyloncFileManager);
            openJars.put(module, jarFile);
        }
        return jarFile;
    }

    public void flush() throws IOException {
        try{
            for(ProgressiveJar jarFile : openJars.values()){
                jarFile.close();
            }
        }finally{
            // make sure we clear on return and throw, so we don't try to flush again on throw
            openJars.clear();
        }
    }
    
    static class ProgressiveJar {
        private static final String META_INF = "META-INF";
        private static final String MAPPING_FILE = META_INF+"/mapping.txt";
        private File originalJarFile;
        private File outputJarFile;
        private JarOutputStream jarOutputStream;
        final private Set<String> modifiedSourceFiles = new HashSet<>();
        final private Set<String> modifiedResourceFiles = new HashSet<>();
        final private Properties writtenClassesMapping = new Properties(); 
        private Logger cmrLog;
        private Options options;
        private RepositoryManager repoManager;
        private ArtifactContext carContext;
        private SourceArchiveCreator creator;
        private Module module;
        private Set<String> folders = new HashSet<String>();
        private boolean manifestWritten = false;

        public ProgressiveJar(RepositoryManager repoManager, Module module, Log log, Options options, CeyloncFileManager ceyloncFileManager) throws IOException{
            this.options = options;
            this.repoManager = repoManager;
            this.carContext = new ArtifactContext(module.getNameAsString(), module.getVersion(), ArtifactContext.CAR);
            this.cmrLog = new JavacLogger(options, Log.instance(ceyloncFileManager.getContext()));
            this.creator = CeylonUtils.makeSourceArchiveCreator(repoManager, ceyloncFileManager.getLocation(StandardLocation.SOURCE_PATH),
                    module.getNameAsString(), module.getVersion(), options.get(OptionName.VERBOSE) != null, cmrLog);
            this.module = module;
            setupJarOutput();
        }

        private void setupJarOutput() throws IOException {
            File targetJarFile = repoManager.getArtifact(carContext);
            outputJarFile = File.createTempFile("car", ".tmp");
            originalJarFile = targetJarFile;
            jarOutputStream = new JarOutputStream(new FileOutputStream(outputJarFile));
        }

        private Properties getPreviousMapping() throws IOException {
            if (originalJarFile != null) {
                try (JarFile jarFile = new JarFile(originalJarFile)) {
                    JarEntry entry = jarFile.getJarEntry(MAPPING_FILE);
                    if (entry != null) {
                        try (InputStream inputStream = jarFile.getInputStream(entry)) {
                            Properties previousMapping = new Properties();
                            previousMapping.load(inputStream);
                            return previousMapping;
                        }
                    }
                }
            }
            return null;
        }

        public void close() throws IOException {
            Set<String> copiedSourceFiles = creator.copySourceFiles(modifiedSourceFiles);

            if (!manifestWritten) {
                Manifest manifest = new OsgiManifest(module).build();
                writeManifestJarEntry(manifest);
            }

            Properties previousMapping = getPreviousMapping();
            writeMappingJarEntry(previousMapping, getJarFilter(previousMapping, copiedSourceFiles));
            
            JarUtils.finishUpdatingJar(
                    originalJarFile, outputJarFile, carContext, jarOutputStream,
                    getJarFilter(previousMapping, copiedSourceFiles),
                    repoManager, options.get(OptionName.VERBOSE) != null, cmrLog, folders);
            
            String info;
            if(module.isDefault())
                info = module.getNameAsString();
            else
                info = module.getNameAsString() + "/" + module.getVersion();
            cmrLog.info("Created module " + info);
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
                        return modifiedResourceFiles.contains(entryFullName)
                                || entryFullName.equals(MAPPING_FILE)
                                || OsgiManifest.isManifestFileName(entryFullName);
                    }
                }
            };
        }


        private void writeManifestJarEntry(Manifest manifest) {
            try {
                jarOutputStream.putNextEntry(new ZipEntry(OsgiManifest.MANIFEST_FILE_NAME));
                manifest.write(jarOutputStream);
            }
            catch (IOException e) {
                // TODO : log to the right place
            }
            finally {
                try {
                    jarOutputStream.closeEntry();
                }
                catch (IOException ignore) {
                }
            }
        }

        private void writeMappingJarEntry(Properties previousMapping, JarUtils.JarEntryFilter filter) {
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
                folders.add(META_INF+"/");
                jarOutputStream.putNextEntry(new ZipEntry(MAPPING_FILE));
                newMapping.store(jarOutputStream, "");
            }
            catch(IOException e) {
                // TODO : log to the right place
            }
            finally {
                try {
                    jarOutputStream.closeEntry();
                } catch (IOException ignore) {
                }
            }
        }

        public JavaFileObject getJavaFileObject(String fileName, File sourceFile) {
            fileName = fileName.replace(File.separatorChar, '/');
            if (sourceFile != null) {
                modifiedSourceFiles.add(sourceFile.getPath());
                // record the class file we produce so that we don't save it from the original jar
            	addMappingEntry(fileName, JarUtils.toPlatformIndependentPath(creator.getSourcePaths(), sourceFile.getPath()));
            } else {
                modifiedResourceFiles.add(fileName);
                if (OsgiManifest.isManifestFileName(fileName)) {
                    this.manifestWritten = true;
                    return new JarEntryManifestFileObject(outputJarFile.getPath(), jarOutputStream, fileName, module);
                }
            }
            String folder = JarUtils.getFolder(fileName);
            if(folder != null)
                folders.add(folder);
            return new JarEntryFileObject(outputJarFile.getPath(), jarOutputStream, fileName);
        }

        private void addMappingEntry(String className,
                String sourcePath) {
            writtenClassesMapping.put(className, sourcePath);
        }
    }
}
