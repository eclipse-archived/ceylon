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

import java.io.*;
import java.net.URI;
import java.util.LinkedHashSet;
import java.util.jar.Attributes;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.zip.ZipEntry;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.NestingKind;
import javax.tools.JavaFileObject;

import com.redhat.ceylon.compiler.typechecker.model.Module;
import com.redhat.ceylon.compiler.typechecker.model.ModuleImport;
import com.redhat.ceylon.compiler.typechecker.model.Package;

public class JarEntryManifestFileObject implements JavaFileObject {

    private JarOutputStream jarFile;
    private String fileName;
    private String jarFileName;
    private Module module;

    public JarEntryManifestFileObject(String jarFileName, JarOutputStream jarFile, String fileName, Module module) {
        super();
        this.jarFileName = jarFileName;
        this.jarFile = jarFile;
        this.fileName = fileName;
        this.module = module;
    }

    /*
     * This is the only method used in the class, the rest is just there to satisfy
     * the type system.
     */
    @Override
    public OutputStream openOutputStream() throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        return new FilterOutputStream(baos){
            @Override
            public void close() throws IOException {
                writeManifestJarEntry(readManifest(baos));
            }
        };
    }

    private void writeManifestJarEntry(Manifest originalManifest) throws IOException {
        Manifest manifest = new OsgiManifest(module, originalManifest).build();
        jarFile.putNextEntry(new ZipEntry(fileName));
        manifest.write(jarFile);
    }

    private Manifest readManifest(ByteArrayOutputStream baos) throws IOException {
        return new Manifest(new ByteArrayInputStream(baos.toByteArray()));
    }

    @Override
    public int hashCode() {
        return jarFileName.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof JarEntryManifestFileObject) {
            JarEntryManifestFileObject r = (JarEntryManifestFileObject)obj;
            return jarFileName.equals(r.jarFileName);
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return jarFileName+":"+fileName;
    }
    
    //
    // All the following methods are just boilerplate and never called
    
    @Override
    public URI toUri() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public InputStream openInputStream() throws IOException {
        return null;
    }

    @Override
    public Reader openReader(boolean ignoreEncodingErrors)
            throws IOException {
        return null;
    }

    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors)
            throws IOException {
        return null;
    }

    @Override
    public Writer openWriter() throws IOException {
        return null;
    }

    @Override
    public long getLastModified() {
        return 0;
    }

    @Override
    public boolean delete() {
        return false;
    }

    @Override
    public Kind getKind() {
        return null;
    }

    @Override
    public boolean isNameCompatible(String simpleName, Kind kind) {
        return false;
    }

    @Override
    public NestingKind getNestingKind() {
        return null;
    }

    @Override
    public Modifier getAccessLevel() {
        return null;
    }

    public static class OsgiManifest {
        public static final String MANIFEST_FILE_NAME = "META-INF/MANIFEST.MF";
        
        public static final Attributes.Name Bundle_SymbolicName = new Attributes.Name("Bundle-SymbolicName"); 
        public static final Attributes.Name Bundle_Version = new Attributes.Name("Bundle-Version"); 
        public static final Attributes.Name Bundle_ManifestVersion = new Attributes.Name("Bundle-ManifestVersion"); 
        public static final Attributes.Name Export_Package = new Attributes.Name("Export-Package"); 
        public static final Attributes.Name Require_Bundle = new Attributes.Name("Require-Bundle");
        public static final Attributes.Name Bundle_RequiredExecutionEnvironment = new Attributes.Name("Bundle-RequiredExecutionEnvironment");
        public static final Attributes.Name Require_Capability = new Attributes.Name("Require-Capability");

        public static boolean isManifestFileName(String fileName) {
            return MANIFEST_FILE_NAME.equalsIgnoreCase(fileName);
        }

        private final Manifest originalManifest;
        private final Module module;

        public OsgiManifest(Module module) {
            this(module, null);
        }

        public OsgiManifest(Module module, Manifest originalManifest) {
            this.module = module;
            this.originalManifest = originalManifest;
        }

        public Manifest build() {
            Manifest manifest = new Manifest();
            Attributes main = manifest.getMainAttributes();

            main.put(Attributes.Name.MANIFEST_VERSION, "1.0");
            main.put(Bundle_ManifestVersion, "2");

            main.put(Bundle_SymbolicName, module.getNameAsString());
            main.put(Bundle_Version, module.getVersion());

            String exportPackageValue = getExportPackage();
            if (exportPackageValue.length() > 0) {
                main.put(Export_Package, exportPackageValue);
            }

            // We use Require-Bundle for declaring dependencies as this
            // maps more closely to Ceylon module dependency model
            String requireBundleValue = getRequireBundle();
            if (requireBundleValue.length() > 0) {
                main.put(Require_Bundle, requireBundleValue);
            }

            // Get all the attributes and entries from original manifest
            appendOriginalManifest(manifest, main);

            // Declare dependency on specific JavaSE version
            if (!isJavaCapabilityRequired(main)) {
                applyRequireJavaCapability(main);
            }

            return manifest;
        }

        private boolean isLanguageModule() {
            return module.equals(module.getLanguageModule());
        }

        private boolean isJavaCapabilityRequired(Attributes main) {
            return main.containsKey(Bundle_RequiredExecutionEnvironment) || main.containsKey(Require_Capability);
        }

        private void applyRequireJavaCapability(Attributes main) {
            if (isLanguageModule()) {
                main.put(Require_Capability, getRequireCapabilityJavaSE("1.7"));
                return;
            }

            for (ModuleImport moduleImport : module.getImports()) {
                Module importedModule = moduleImport.getModule();
                if (importedModule.isJava()) {
                    String version = importedModule.getVersion();
                    main.put(Require_Capability, getRequireCapabilityJavaSE(version));
                    break;
                }
            }


        }

        private String getRequireCapabilityJavaSE(String version) {
            return new StringBuilder()
                   .append("osgi.ee;").append("filter:=").append('"')
                    .append("(&")
                     .append("(osgi.ee=JavaSE)")
                     .append("(version>=").append(getJavaVersion(version)).append(")")
                    .append(")")
                   .append('"')
                   .toString();
        }

        private void appendOriginalManifest(Manifest manifest, Attributes main) {
            if (originalManifest != null) {
                Attributes attributes = originalManifest.getMainAttributes();
                for (Object key : attributes.keySet()) {
                    if (!main.containsKey(key)) {
                        main.put(key, attributes.get(key));
                    }
                }

                manifest.getEntries().putAll(originalManifest.getEntries());
            }
        }

        private String getJavaVersion(String jvmVersion) {
            return "1."+jvmVersion;
        }

        /**
         * Composes OSGi Require-Bundle header content.
         */
        private String getRequireBundle() {
            StringBuilder requires = new StringBuilder();
            if (!isLanguageModule()) {
                for (ModuleImport anImport : module.getImports()) {
                    Module m = anImport.getModule();
                    if (!m.isJava() && !m.equals(module)) {
                        if (requires.length() > 0) {
                            requires.append(",");
                        }

                        requires.append(m.getNameAsString())
                                .append(";bundle-version=").append(m.getVersion());

                        if (anImport.isExport()) {
                            requires.append(";visibility:=reexport");
                        }
                        if (anImport.isOptional()) {
                            requires.append(";resolution:=optional");
                        }
                    }
                }
            }
            return requires.toString();
        }

        /**
         * Composes OSGi Export-Package header content.
         */
        private String getExportPackage() {
            boolean hasSharedPackages = false;
            StringBuilder exportPackage = new StringBuilder();
            for (Package pkg : module.getPackages()) {
                if (pkg.isShared()) {
                    if (hasSharedPackages) {
                        exportPackage.append(";");
                    }
                    exportPackage.append(pkg.getNameAsString());
                    //TODO : should we analyze package uses as well?
                    hasSharedPackages = true;
                }
            }
            if (hasSharedPackages) {
                // Ceylon has no separate versioning of packages, so all
                // packages implicitly inherit their respective module version
                exportPackage.append(";version=").append(module.getVersion());
            }
            return exportPackage.toString();
        }
    }
}
