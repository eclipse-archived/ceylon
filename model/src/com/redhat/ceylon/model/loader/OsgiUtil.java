package com.redhat.ceylon.model.loader;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.redhat.ceylon.common.Backend;
import com.redhat.ceylon.common.log.Logger;
import com.redhat.ceylon.model.typechecker.model.ModelUtil;
import com.redhat.ceylon.model.typechecker.model.Module;
import com.redhat.ceylon.model.typechecker.model.ModuleImport;
import com.redhat.ceylon.model.typechecker.model.Package;

public class OsgiUtil {
    
    public abstract static class CeylonManifest {
        public static final String MANIFEST_FILE_NAME = "META-INF/MANIFEST.MF";
        public static boolean isManifestFileName(String fileName) {
            return MANIFEST_FILE_NAME.equalsIgnoreCase(fileName);
        }
        public Manifest build() {
            Manifest manifest = new Manifest();
            Attributes main = manifest.getMainAttributes();

            main.put(Attributes.Name.MANIFEST_VERSION, "1.0");
            return manifest;
        }
    }
    
    /**
     * The non-OSGi-compliant MANIFEST.MF we generate for the default module.
     */
    public static class DefaultModuleManifest extends CeylonManifest {
        public static final Attributes.Name Ceylon_Default_Module = new Attributes.Name("Ceylon-Default-Module");
        public static boolean isDefaultModule(Manifest manifest) {
            return "true".equals(manifest.getMainAttributes().get(Ceylon_Default_Module));
        }
        public Manifest build() {
            Manifest manifest = super.build();
            Attributes main = manifest.getMainAttributes();
            main.put(Ceylon_Default_Module, "true");
            return manifest;
        }
    }
    
    /**
     * The OSGi-compliant MANIFEST.MF we generate for non-default modules.
     */
    public static class OsgiManifest extends CeylonManifest {
        
        public static final Attributes.Name Bundle_SymbolicName = new Attributes.Name("Bundle-SymbolicName"); 
        public static final Attributes.Name Bundle_Version = new Attributes.Name("Bundle-Version"); 
        public static final Attributes.Name Bundle_ManifestVersion = new Attributes.Name("Bundle-ManifestVersion"); 
        public static final Attributes.Name Export_Package = new Attributes.Name("Export-Package"); 
        public static final Attributes.Name Require_Bundle = new Attributes.Name("Require-Bundle");
        public static final Attributes.Name Bundle_RequiredExecutionEnvironment = new Attributes.Name("Bundle-RequiredExecutionEnvironment");
        public static final Attributes.Name Require_Capability = new Attributes.Name("Require-Capability");
        public static final Attributes.Name Bundle_ActivationPolicy = new Attributes.Name("Bundle-ActivationPolicy");
        public static final Attributes.Name Bundle_Activator = new Attributes.Name("Bundle-Activator");
        private static SimpleDateFormat formatter = new SimpleDateFormat("'v'yyyyMMdd-HHmm");
        static {
            formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
        }
        
        private String osgiProvidedBundles;

        private final Manifest originalManifest;
        private final Module module;

        private Logger log;
        private JdkProvider jdkProvider;

        public OsgiManifest(Module module, JdkProvider jdkProvider, String osgiProvidedBundles) {
            this(module, jdkProvider, osgiProvidedBundles, null, null);
        }

        public OsgiManifest(Module module, JdkProvider jdkProvider, String osgiProvidedBundles, Manifest originalManifest, Logger log) {
            this.module = module;
            this.originalManifest = originalManifest;
            this.osgiProvidedBundles = osgiProvidedBundles;
            this.log = log;
            this.jdkProvider = jdkProvider;
        }

        private String toOSGIBundleVersion(String ceylonVersion) {
            return OsgiUtil.toOSGIBundleVersion(ceylonVersion);
        }

        public Manifest build() {
            Manifest manifest = super.build();
            Attributes main = manifest.getMainAttributes();
            main.put(Bundle_ManifestVersion, "2");

            main.put(Bundle_SymbolicName, module.getNameAsString());
            main.put(Bundle_Version, toOSGIBundleVersion(module.getVersion()));

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

            applyActivationPolicyNonLazy(main);

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
                main.put(Require_Capability, getRequireCapabilityJavaSE("7"));
                return;
            }

            for (ModuleImport moduleImport : module.getImports()) {
                Module importedModule = moduleImport.getModule();
                if (jdkProvider.isJDKModule(importedModule.getNameAsString())) {
                    // FIXME Hard-coding version 7 for now because we don't officially
                    // support Java 8 yet and compiling with that compiler generates
                    // the wrong requirements
                    // String version = importedModule.getVersion();
                    main.put(Require_Capability, getRequireCapabilityJavaSE("7"));
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

        private void applyActivationPolicyNonLazy(Attributes main) {
            main.put(Bundle_ActivationPolicy, "lazy;exclude:=\"*\"");
        }

        private void appendOriginalManifest(Manifest manifest, Attributes main) {
            if (originalManifest != null) {
                Attributes attributes = originalManifest.getMainAttributes();
                for (Object key : attributes.keySet()) {
                    if (!main.containsKey(key)) {
                        main.put(key, attributes.get(key));
                    } else if (log != null) {
                        Object oldValue = main.get(key);
                        Object newValue = attributes.get(key);
                        if(!Objects.equals(oldValue, newValue))
                            log.warning("manifest attribute provided by compiler: ignoring value from '"+key+"' for module '" + module.getNameAsString() +"'");
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
            List<String> providedBundles = null;
            if (osgiProvidedBundles != null && !osgiProvidedBundles.isEmpty()) {
                providedBundles = Arrays.asList(osgiProvidedBundles.split("(,| )+"));
            }
            
            StringBuilder requires = new StringBuilder();
            boolean distImportAlreadyFound = false;
            for (ModuleImport anImport : module.getImports()) {
                if (!ModelUtil.isForBackend(anImport.getNativeBackends(), Backend.Java)) {
                    continue;
                }
                Module m = anImport.getModule();
                String moduleName = m.getNameAsString();
                
                if (providedBundles != null && providedBundles.contains(moduleName)) {
                    continue;
                }
                
                if ("com.redhat.ceylon.dist".equals(moduleName)) {
                    distImportAlreadyFound = true;
                }
                if (!jdkProvider.isJDKModule(moduleName)
                        && !m.equals(module)) {
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
            if (! distImportAlreadyFound) {
                if (requires.length() > 0) {
                    requires.append(",");
                }
                requires.append("com.redhat.ceylon.dist")
                .append(";bundle-version=").append(module.getLanguageModule().getVersion()).append(";visibility:=reexport");
            }
            return requires.toString();
        }

        /**
         * Composes OSGi Export-Package header content.
         */
        private String getExportPackage() {
            boolean alreadyOne = false;
            StringBuilder exportPackage = new StringBuilder();
            for (Package pkg : module.getPackages()) {
                if (pkg.isShared() || isLanguageModule()) {
                    if (alreadyOne) {
                        exportPackage.append(",");
                    }
                    exportPackage.append(pkg.getNameAsString());
                    // Ceylon has no separate versioning of packages, so all
                    // packages implicitly inherit their respective module version
                    exportPackage.append(";version=").append(module.getVersion());
                    //TODO : should we analyze package uses as well?
                    alreadyOne = true;
                }
            }
            if (isLanguageModule()) {
                if (alreadyOne) {
                    exportPackage.append(",");
                }
                exportPackage.append("com.redhat.ceylon.dist.osgi");
                exportPackage.append(";version=").append(module.getVersion());
            }
            return exportPackage.toString();
        }
    }

    private static HashMap<String, Integer> ceylonQualifiers;
    static {
        ceylonQualifiers = new HashMap<String, Integer>();
        ceylonQualifiers.put("alpha", 0);
        ceylonQualifiers.put("a", 0);
        ceylonQualifiers.put("beta", 1);
        ceylonQualifiers.put("b", 1);
        ceylonQualifiers.put("milestone", 2);
        ceylonQualifiers.put("m", 2);
        ceylonQualifiers.put("rc", 3);
        ceylonQualifiers.put("cr", 3);
        ceylonQualifiers.put("snapshot", 4);
        ceylonQualifiers.put("ga", 5);
        ceylonQualifiers.put("final", 5);
        ceylonQualifiers.put("sp", 6);
    }
    private static int QUALIFIER_OTHER = 6; // Same as largest index in above list
    
    public static String toOSGIBundleVersion(String ceylonVersion) {
        // Insert a "." between digits and letters
        StringBuffer buf = new StringBuffer();
        Pattern p = Pattern.compile("\\d\\pL|\\pL\\d");
        Matcher m = p.matcher(ceylonVersion);
        while (m.find()) {
            String found = m.group();
            assert(found.length() == 2);
            m.appendReplacement(buf, found.charAt(0) + "." + found.charAt(1));
        }
        m.appendTail(buf);
        
        // Split on dots and dashes
        String[] versionParts = buf.toString().split("[\\.-]");

        // Now iterator over all the parts applying the following rules:
        // - any empty part is treated as "0"
        // - any number in the first 3 positions are added as-is
        // - any number after that gets padded with leading zeros to a length of 3 and prefixed with the letter "n"
        // - when a non-number is encountered in the first 3 positions enough "0" parts are added to have at least 3 numbers at the start
        // - a non-number gets lookup in the qualifier table and if found replaced by its index
        // - when not found we add the part prefixed with the largest index in the qualifier table and a dash
        boolean inOsgiQualifier = false;
        boolean lastWasQualifier = false;
        ArrayList<String> resultParts = new ArrayList<String>();
        for (int i = 0; i < versionParts.length; i++) {
            String part = versionParts[i];
            if (part.isEmpty()) {
                part = "0";
            }
            inOsgiQualifier = inOsgiQualifier || i >= 3;
            if (isNumber(part)) {
                if (inOsgiQualifier) {
                    resultParts.add(String.format("n%03d", Integer.parseInt(part)));
                } else {
                    resultParts.add(part);
                }
                lastWasQualifier = false;
            } else {
                if (!inOsgiQualifier) {
                    // We need our version to start with at least 3 numbers
                    for (int j = i; j < 3; j++) {
                        resultParts.add("0");
                    }
                    inOsgiQualifier = true;
                }
                Integer idx = ceylonQualifiers.get(part.toLowerCase());
                if (idx != null) {
                    resultParts.add(idx.toString());
                } else {
                    resultParts.add(QUALIFIER_OTHER + "-" + part);
                }
                lastWasQualifier = true;
            }
        }
        // We need our version to start with at least 3 numbers
        for (int j = resultParts.size(); j < 3; j++) {
            resultParts.add("0");
        }
        
        if (!lastWasQualifier) {
            // If we didn't terminate with a qualifier let's treat as if it was "final"
            resultParts.add(ceylonQualifiers.get("final").toString());
        }
        
        // Now join all the resulting parts together. The first 4
        // elements get separated by dots, the rest by dashes
        StringBuffer result = new StringBuffer();
        for (int i=0; i < resultParts.size(); i++) {
            String part = resultParts.get(i);
            if (i > 3) {
                result.append("-");
            } else if (i > 0) {
                result.append(".");
            }
            result.append(part);
        }
        
        return result.toString();
    }

    private static boolean isNumber(String txt) {
        return txt.matches("\\d+");
    }
}
