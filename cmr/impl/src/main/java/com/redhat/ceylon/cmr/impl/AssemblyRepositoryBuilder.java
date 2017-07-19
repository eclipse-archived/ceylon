/*
 * Copyright 2011 Red Hat inc. and third party contributors as noted 
 * by the author tags.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.redhat.ceylon.cmr.impl;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import com.redhat.ceylon.cmr.api.CmrRepository;
import com.redhat.ceylon.cmr.api.RepositoryBuilder;
import com.redhat.ceylon.cmr.util.JarUtils;
import com.redhat.ceylon.common.Constants;
import com.redhat.ceylon.common.FileUtil;

/**
 * Repository builder for assemblies
 * 
 * Assemblies in their simplest form are just zipped module repositories.
 * The token for building repositories of this type is:
 * 
 *     assembly:path/to/assembly[!repositoryfolder]
 *     
 * Where the "path/to/assembly" can refer to either a Zip file or a Jar file.
 * There's also an optional repository folder element, separated from the
 * assembly path by an exclamation mark (!) that indicates where in the assembly
 * file the modules are located (if it's not present by default the modules will
 * be assumed to be in the root).
 * 
 * If the assembly contains a META-INF/MANIFEST.MF file then in the absence
 * of a repository folder element in the token the system will also check for
 * the existence of an "X-Ceylon-Assembly-Repository" attribute. If it exists
 * its value will be used instead.
 *
 * @author Tako Schotanus (tako@ceylon-lang.org)
 */
public class AssemblyRepositoryBuilder implements RepositoryBuilder {
    
    private static char SEPARATOR = '!';
    
    @Override
    public String absolute(File cwd, String token) {
        if (token.startsWith("assembly:")) {
            token = token.substring(9);
            String repoFolder = null;
            int p = token.indexOf(SEPARATOR);
            if (p > 0) {
                repoFolder = token.substring(p + 1);
                token = token.substring(0, p);
            }
            File f = FileUtil.absoluteFile(FileUtil.applyCwd(cwd, new File(token)));
            token = f.getAbsolutePath();
            String absToken = "assembly:" + token;
            if (repoFolder != null) {
                absToken += SEPARATOR + repoFolder;
            }
            return absToken;
        } else {
            return null;
        }
    }

    @Override
    public CmrRepository[] buildRepository(String token) throws Exception {
        return buildRepository(token, EMPTY_CONFIG);
    }

    @Override
    public CmrRepository[] buildRepository(String token, RepositoryBuilderConfig config) throws Exception {
        if (token.startsWith("assembly:")) {
            token = token.substring(9);
            // Check if the token has a repo folder element
            String repoFolder = null;
            int p = token.indexOf(SEPARATOR);
            if (p > 0) {
                repoFolder = token.substring(p + 1);
                token = token.substring(0, p);
            }
            
            // Unpack the assembly (if we haven't already done so before)
            File assemblyFile = new File(token);
            File tmpAssemblyFolder = registerAssembly(assemblyFile);
            
            // Read the assembly's MANIFEST (if any)
            File manifestFile = new File(new File(tmpAssemblyFolder, "META-INF"), "MANIFEST.MF");
            if (manifestFile.isFile()) {
                Manifest manifest = new Manifest(new FileInputStream(manifestFile));
                Attributes attrs = manifest.getMainAttributes();
                if (repoFolder == null) {
                    repoFolder = attrs.getValue(Constants.ATTR_ASSEMBLY_REPOSITORY);
                }
            }
            
            // The modules might be an a sub-folder of the assembly, not in the root
            File modulesFolder = tmpAssemblyFolder;
            if (repoFolder != null && !repoFolder.isEmpty()) {
                modulesFolder = new File(modulesFolder, repoFolder);
                if (!modulesFolder.isDirectory()) {
                    throw new IllegalArgumentException("No such repository folder within the assembly: " + repoFolder);
                }
            }
            
            // Now create a common file content store using the unpacked assembly
            FileContentStore cs = new FileContentStore(modulesFolder);
            CmrRepository defRepo =  new DefaultRepository(cs.createRoot());
            
            // Not satisfied with the following heuristics yet, but if we
            // have a specific "modules" folder (so the modules are not in
            // the root of the assembly) then we also check to see if there
            // is a "maven" folder and if so we add a MavenRepository for it.
            // We do the same for "node_modules" and the NpmRepository.
            CmrRepository mvnRepo = null;
            CmrRepository npmRepo = null;
            if (repoFolder != null && !repoFolder.isEmpty()) {
                File mavenFolder = new File(tmpAssemblyFolder, "maven");
                if (mavenFolder.isDirectory()) {
                    // Now create a Maven repository on top of the unpacked assembly
                    mvnRepo = MavenRepositoryBuilder.createMavenRepository(mavenFolder.getAbsolutePath(), config);
                }
                
                File npmFolder = new File(tmpAssemblyFolder, "node_modules");
                if (npmFolder.isDirectory()) {
                    // Now create a NPM repository on top of the unpacked assembly
                    String npmtoken = "npm:" + npmFolder.getAbsolutePath();
                    npmRepo = NpmRepositoryBuilder.createNpmRepository(npmtoken, config.log, config.offline, config.currentDirectory);
                }
            }

            ArrayList<CmrRepository> repos = new ArrayList<CmrRepository>(3);
            repos.add(defRepo);
            if (mvnRepo != null) {
                repos.add(mvnRepo);
            }
            if (npmRepo != null) {
                repos.add(npmRepo);
            }
            return repos.toArray(new CmrRepository[] {});
        } else {
            return null;
        }
    }

    /**
     * Unpacks the given assembly to a temporary folder and returns a
     * <code>File</code> reference to that folder. If the assembly was
     * already previously registered it won't be unpacked again but
     * the reference to the folder will be returned immediately.
     * The temporary folder is automatically deleted when the JVM exits.
     * 
     * @param assembly <code>File</code> reference to a Ceylon Assembly file
     * @return <code>File</code> reference to the temporary folder where the
     * assembly was unpacked
     * @throws Exception There was either something wrong with the assembly
     * or something went wrong during the unpacking
     */
    public static File registerAssembly(File assembly) throws Exception {
        assembly = assembly.getAbsoluteFile();
        File tmpAssemblyFolder = findAssembly(assembly);
        if (tmpAssemblyFolder == null) {
            if (!assembly.isFile()) {
                throw new IllegalArgumentException("Assembly does not exist or is not a file: " + assembly);
            }
            if (!JarUtils.isValidJar(assembly)) {
                throw new IllegalArgumentException("File exists but is not a proper assembly: " + assembly);
            }
            try {
                // Create a temp folder to extract our assembly into
                tmpAssemblyFolder = Files.createTempDirectory("ceylon-assembly-").toFile();
                // Make sure we clean up the temp folder when the JVM exits
                deleteOnExit(tmpAssemblyFolder);
                // Extract the assembly into the temp folder
                IOUtils.extractArchive(assembly, tmpAssemblyFolder);
                // We remember which assemblies we opened so we don't fill up
                // the temp space with unnecessary duplicates
                storeAssembly(assembly, tmpAssemblyFolder);
            } catch (Exception ex) {
                FileUtil.deleteQuietly(tmpAssemblyFolder);
                throw ex;
            }
        }
        return tmpAssemblyFolder;
    }

    // The normal File.deleteOnExit() doesn't work for directories that might
    // not be empty, so we need to add a shutdown hook to do it ourselves
    private static void deleteOnExit(final File repo) {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                FileUtil.deleteQuietly(repo);
            }
        });
    }
    
    // These following methods are a bit of a hackish way to keep track of which
    // assemblies have already been unpacked for this JVM. Storing this information
    // in a static map for example doesn't work because of all the class loader
    // protections JBoss Modules introduces.
    
    private static File findAssembly(File assembly) {
        String tmpFolderPath = System.getProperty(assemblyPropertyName(assembly));
        if (tmpFolderPath != null) {
            return new File(tmpFolderPath);
        } else {
            return null;
        }
    }
    
    private static void storeAssembly(File assembly, File tmpFolder) {
        System.setProperty(assemblyPropertyName(assembly), tmpFolder.getPath());
    }
    
    private static String assemblyPropertyName(File assembly) {
        return "$ceylon$assembly$" + assembly.getPath();
    }
}
