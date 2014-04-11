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
package com.redhat.ceylon.tools.importjar;

import java.io.File;
import java.io.IOException;
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
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.redhat.ceylon.cmr.api.ArtifactContext;
import com.redhat.ceylon.cmr.api.JDKUtils;
import com.redhat.ceylon.cmr.api.ModuleInfo;
import com.redhat.ceylon.cmr.api.RepositoryManager;
import com.redhat.ceylon.cmr.ceylon.OutputRepoUsingTool;
import com.redhat.ceylon.cmr.impl.CMRException;
import com.redhat.ceylon.cmr.impl.PropertiesDependencyResolver;
import com.redhat.ceylon.cmr.impl.XmlDependencyResolver;
import com.redhat.ceylon.common.Messages;
import com.redhat.ceylon.common.ModuleUtil;
import com.redhat.ceylon.common.tool.Argument;
import com.redhat.ceylon.common.tool.Description;
import com.redhat.ceylon.common.tool.Option;
import com.redhat.ceylon.common.tool.OptionArgument;
import com.redhat.ceylon.common.tool.RemainingSections;
import com.redhat.ceylon.common.tool.Summary;
import com.redhat.ceylon.common.tool.ToolUsageError;
import com.redhat.ceylon.common.tools.ModuleSpec;

@Summary("Imports a jar file into a Ceylon module repository")
@Description("Imports the given `<jar-file>` using the module name and version " +
        "given by `<module>` into the repository named by the " +
        "`--out` option.\n" +
        "\n" +
        "`<module>` is a module name and version separated with a slash, for example " +
        "`com.example.foobar/1.2.0`.\n" +
        "\n" +
        "`<jar-file>` is the name of the Jar file to import.")
@RemainingSections(
        "## Output repositories" +
        "\n\n" +
        "Output repositories specified with the `--out` option can be file paths, HTTP urls " +
        "to remote servers or can be names of repositories when prepended with a `+` symbol. " +
        "These names refer to repositories defined in the configuration file or can be any of " +
        "the following predefined names `+SYSTEM`, `+CACHE`, `+LOCAL`, `+USER` or `+REMOTE`. " +
        "For more information see http://ceylon-lang.org/documentation/1.0/reference/tool/config")
public class CeylonImportJarTool extends OutputRepoUsingTool {

    private ModuleSpec module;
    private String jarFile;
    private String descriptor;
    private boolean force;
    private boolean dryRun;
    private boolean showClasses;
    
    private Set<String> jarClassNames;
    private Set<String> checkedClassNames;
    private boolean hasErrors;

    public CeylonImportJarTool() {
        super(ImportJarMessages.RESOURCE_BUNDLE);
    }
    
    CeylonImportJarTool(String moduleSpec, String out, String user, String pass, String jarFile, String verbose) {
        super(ImportJarMessages.RESOURCE_BUNDLE);
        setModuleSpec(moduleSpec);
        this.out = out;
        this.user = user;
        this.pass = pass;
        this.jarFile = jarFile;
        this.verbose = verbose;
        initialize();
    }

    @OptionArgument(argumentName="file")
    @Description("Specify a module.xml or module.properties file to be used "
            + "as the module descriptor")
    public void setDescriptor(String descriptor) {
        this.descriptor = descriptor;
    }
    
    @Argument(argumentName="module", multiplicity="1", order=0)
    public void setModuleSpec(String module) {
        setModuleSpec(ModuleSpec.parse(module, 
                ModuleSpec.Option.VERSION_REQUIRED, 
                ModuleSpec.Option.DEFAULT_MODULE_PROHIBITED));
    }
    
    public void setModuleSpec(ModuleSpec module) {
        this.module = module;
    }

    @Argument(argumentName="jar-file", multiplicity="1", order=1)
    public void setFile(String jarFile) {
        this.jarFile = jarFile;
    }
    
    @Option(longName="force")
    @Description("Skips sanity checks and forces publication of the JAR.")
    public void setForce(boolean force) {
        this.force = force;
    }
    
    @Option(longName="dry-run")
    @Description("Performs all the sanity checks but does not publish the JAR.")
    public void setDryRun(boolean dryRun) {
        this.dryRun = dryRun;
    }
    
    @Option(longName="show-classes")
    @Description("Shows all external classes that are not declared as imports instead of their packages only.")
    public void setShowClasses(boolean showClasses) {
        this.showClasses = showClasses;
    }
    
    @Override
    public void initialize() {
        setSystemProperties();
        if(jarFile == null || jarFile.isEmpty())
            throw new ImportJarException("error.jarFile.empty");
        File f = new File(jarFile);
        checkReadableFile(f, "error.jarFile");
        if(!f.getName().toLowerCase().endsWith(".jar"))
            throw new ImportJarException("error.jarFile.notJar", new Object[]{f.toString()}, null);
        
        if (descriptor != null) {
            checkReadableFile(new File(descriptor), "error.descriptorFile");
            if(!(descriptor.toLowerCase().endsWith(".xml") ||
                    descriptor.toLowerCase().endsWith(".properties")))
                throw new ImportJarException("error.descriptorFile.badSuffix", new Object[]{descriptor}, null);
        }
    }

    private void checkReadableFile(File f, String keyPrefix) {
        if(!f.exists())
            throw new ImportJarException(keyPrefix + ".doesNotExist", new Object[]{f.toString()}, null);
        if(f.isDirectory())
            throw new ImportJarException(keyPrefix + ".isDirectory", new Object[]{f.toString()}, null);
        if(!f.canRead())
            throw new ImportJarException(keyPrefix + ".notReadable", new Object[]{f.toString()}, null);
    }
    
    // Check the public API for the JAR we're importing and report any problems that are found
    private void checkPublicApi() throws IOException {
        Set<String> externalClasses = gatherExternalClasses();
        
        if (descriptor != null) {
            File descriptorFile = new File(descriptor);
            if (descriptor.toLowerCase().endsWith(".xml")) {
                checkModuleXml(descriptorFile, externalClasses);
            } else if(descriptor.toLowerCase().endsWith(".properties")) {
                checkModuleProperties(descriptorFile, externalClasses);
            }
        }
        
        if (!externalClasses.isEmpty()) {
            hasErrors = true;
            if (!showClasses) {
                Set<String> externalPackages = getPackagesFromClasses(externalClasses);
                if (!externalPackages.isEmpty()) {
                    Set<String> jdkPackages = gatherJdkModules(externalPackages);
                    if (!jdkPackages.isEmpty()) {
                        msg("info.declare.jdk.imports").newline();
                        for (String pkg : jdkPackages) {
                            append("    ").append(pkg).newline();
                        }
                    }
                    if (!externalPackages.isEmpty()) {
                        msg("info.declare.module.imports").newline();
                        for (String pkg : externalPackages) {
                            append("    ").append(pkg).newline();
                        }
                    }
                }
                Set<String> externalDefaultClasses = getDefaultPackageClasses(externalClasses);
                if (!externalDefaultClasses.isEmpty()) {
                    msg("info.declare.class.imports").newline();
                    for (String cls : externalDefaultClasses) {
                        append("    ").append(cls).newline();
                    }
                }
            } else {
                msg("info.declare.class.imports").newline();
                for (String cls : externalClasses) {
                    append("    ").append(cls).newline();
                }
            }
        }
    }

    // Return the set of class names for those types referenced by the
    // public API of the classes in the JAR we're importing and that are
    // not part of the JAR itself
    private Set<String> gatherExternalClasses() {
        checkedClassNames = new HashSet<>();
        HashSet<String> externalClasses = new HashSet<>();
        try {
            File jar = new File(jarFile);
            URLClassLoader cl = new URLClassLoader(new URL[] { jar.toURI().toURL() });
            try {
                jarClassNames = gatherClassnamesFromJar(jar);
                for (String className : jarClassNames) {
                    Class<?> clazz;
                    try {
                        clazz = cl.loadClass(className);
                    } catch (NoClassDefFoundError | ClassNotFoundException e) {
                        handleNotFoundErrors(externalClasses, e);
                        continue;
                    }
                    checkPublicApi(externalClasses, clazz);
                }
            } finally {
                cl.close();
            }
        } catch (IOException e) {
            throw new ImportJarException("error.jarFile.unableToAnalyze", e);
        }
        return externalClasses;
    }
    
    // Check the public API of a class for types that are external to the JAR we're importing
    private void checkPublicApi(Set<String> externalClasses, Class<?> clazz) {
        if (clazz.getModifiers() != Modifier.PUBLIC) {
            // Not interested in any but public classes
            return;
        }
        // Make sure we get an actual class, not an array
        while (clazz.isArray()) {
            clazz = clazz.getComponentType();
        }
        try {
            checkTypes(externalClasses, clazz.getTypeParameters());
        } catch (NoClassDefFoundError | TypeNotPresentException e) {
            handleNotFoundErrors(externalClasses, e);
        }
        try {
            checkAnnotations(externalClasses, clazz.getAnnotations());
        } catch (NoClassDefFoundError | TypeNotPresentException e) {
            handleNotFoundErrors(externalClasses, e);
        }
        try {
            checkType(externalClasses, clazz.getGenericSuperclass());
        } catch (NoClassDefFoundError | TypeNotPresentException e) {
            handleNotFoundErrors(externalClasses, e);
        }
        try {
            Type[] interfaces = clazz.getGenericInterfaces();
            for (Type iface : interfaces) {
                checkType(externalClasses, iface);
            }
        } catch (NoClassDefFoundError | TypeNotPresentException e) {
            handleNotFoundErrors(externalClasses, e);
        }
        try {
            Method[] methods = clazz.getMethods();
            for (Method mth : methods) {
                checkTypes(externalClasses, mth.getTypeParameters());
                checkAnnotations(externalClasses, mth.getAnnotations());
                checkType(externalClasses, mth.getGenericReturnType());
                for (Type param : mth.getGenericParameterTypes()) {
                    checkType(externalClasses, param);
                }
                checkAnnotations(externalClasses, mth.getParameterAnnotations());
                for (Type ex : mth.getGenericExceptionTypes()) {
                    checkType(externalClasses, ex);
                }
            }
        } catch (NoClassDefFoundError | TypeNotPresentException e) {
            handleNotFoundErrors(externalClasses, e);
        }
        try {
            Field[] fields = clazz.getFields();
            for (Field fld : fields) {
                checkAnnotations(externalClasses, fld.getAnnotations());
                checkType(externalClasses, fld.getGenericType());
            }
        } catch (NoClassDefFoundError | TypeNotPresentException e) {
            handleNotFoundErrors(externalClasses, e);
        }
        try {
            Constructor<?>[] constructors = clazz.getConstructors();
            for (Constructor<?> cons : constructors) {
                checkTypes(externalClasses, cons.getTypeParameters());
                checkAnnotations(externalClasses, cons.getAnnotations());
                for (Type param : cons.getGenericParameterTypes()) {
                    checkType(externalClasses, param);
                }
                checkAnnotations(externalClasses, cons.getParameterAnnotations());
                for (Type ex : cons.getGenericExceptionTypes()) {
                    checkType(externalClasses, ex);
                }
            }
        } catch (NoClassDefFoundError | TypeNotPresentException e) {
            handleNotFoundErrors(externalClasses, e);
        }
    }
    
    private void checkAnnotations(Set<String> externalClasses, Annotation[][] annotations) {
        for (Annotation[] annos : annotations) {
            checkAnnotations(externalClasses, annos);
        }
    }

    private void checkAnnotations(Set<String> externalClasses, Annotation[] annotations) {
        for (Annotation anno : annotations) {
            checkType(externalClasses, anno.annotationType());
        }
    }

    private void checkTypes(Set<String> externalClasses, Type[] types) {
        for (Type t : types) {
            checkType(externalClasses, t);
        }
    }
    
    // Check if the type is external to the JAR we're importing, if so add it to the given set
    private void checkType(Set<String> externalClasses, Type type) {
        if (type != null) {
            if (type instanceof Class) {
                checkClass(externalClasses, (Class<?>)type);
            } else if (type instanceof GenericArrayType) {
                checkType(externalClasses, ((GenericArrayType) type).getGenericComponentType());
            } else if (type instanceof ParameterizedType) {
                checkType(externalClasses, ((ParameterizedType) type).getOwnerType());
                for (Type t : ((ParameterizedType) type).getActualTypeArguments()) {
                    checkType(externalClasses, t);
                }
            } else if (type instanceof TypeVariable) {
                Type[] bounds;
                try {
                    bounds = ((TypeVariable<?>) type).getBounds();
                } catch (NoClassDefFoundError | TypeNotPresentException e) {
                    handleNotFoundErrors(externalClasses, e);
                    return;
                }
                for (Type b : bounds) {
                    checkType(externalClasses, b);
                }
            } else if (type instanceof WildcardType) {
                Type[] lower;
                try {
                    lower = ((WildcardType) type).getLowerBounds();
                } catch (NoClassDefFoundError | TypeNotPresentException e) {
                    handleNotFoundErrors(externalClasses, e);
                    return;
                }
                for (Type b : lower) {
                    checkType(externalClasses, b);
                }
                Type[] upper;
                try {
                    upper = ((WildcardType) type).getUpperBounds();
                } catch (NoClassDefFoundError | TypeNotPresentException e) {
                    handleNotFoundErrors(externalClasses, e);
                    return;
                }
                for (Type b : upper) {
                    checkType(externalClasses, b);
                }
            } else {
                System.err.println("Handling of type not implemented for " + type.getClass().getName());
            }
        }
    }

    // Check if the class is external to the JAR we're importing, if so add it to the given set
    private void checkClass(Set<String> externalClasses, Class<?> clazz) {
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
        if (checkedClassNames.contains(name)) {
            // Already checked it
            return;
        }
        checkedClassNames.add(name);
//        String pkgName = clazz.getPackage().getName();
//        if (JDKUtils.isJDKAnyPackage(pkgName) || JDKUtils.isOracleJDKAnyPackage(pkgName)) {
//            externalClasses.add(name);
//            return;
//        }
        // FIXME Do we need to do more?
        externalClasses.add(name);
    }

    // Extract the name of the class that couldn't be loaded and add it to the given set
    private void handleNotFoundErrors(Set<String> notFound, Throwable th) {
        // This is very brittle because it depends on the message only containing the class name
        if (th instanceof TypeNotPresentException
                && (th.getCause() instanceof ClassNotFoundException
                        || th.getCause() instanceof NoClassDefFoundError)) {
            th = th.getCause();
        }
        String name = th.getMessage().replace('/', '.');
        if (name.startsWith("L") && name.endsWith(";")) {
            name = name.substring(1, name.length() - 1);
        }
        if (notFound.add(name)) {
            log.debug("NOT FOUND " + name);
        }
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

    // Return the set of fully qualified names for all the classes
    // in the JAR pointed to by the given file
    private Set<String> gatherClassnamesFromJar(File jar) throws IOException {
        HashSet<String> names = new HashSet<>();
        JarFile zf = new JarFile(jar);
        try {
            Enumeration<? extends JarEntry> entries = zf.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                if (!entry.isDirectory() && entry.getName().endsWith(".class")) {
                    String name = entry.getName();
                    String className = name.substring(0, name.length() - 6).replace('/', '.');
                    names.add(className);
                }
            }
        } finally {
            zf.close();
        }
        return names;
    }

    // Publish the JAR to the specified or default output repository
    public void publish() {
        RepositoryManager outputRepository = getOutputRepositoryManager();

        ArtifactContext context = new ArtifactContext(module.getName(), module.getVersion(), ArtifactContext.JAR);
        context.setForceOperation(true);
        ArtifactContext descriptorContext = null;
        if (descriptor != null) {
            if (descriptor.toLowerCase().endsWith(".xml")) {
                descriptorContext = new ArtifactContext(module.getName(), module.getVersion(), ArtifactContext.MODULE_XML);
            } else if (descriptor.toLowerCase().endsWith(".properties")) {
                descriptorContext = new ArtifactContext(module.getName(), module.getVersion(), ArtifactContext.MODULE_PROPERTIES);
            }
            descriptorContext.setForceOperation(true);
        }
        try{
            outputRepository.putArtifact(context, new File(jarFile));
            String sha1 = ShaSigner.sha1(jarFile, log);
            if(sha1 != null){
                File shaFile = ShaSigner.writeSha1(sha1, log);
                if(shaFile != null){
                    try{
                        ArtifactContext sha1Context = context.getSha1Context();
                        outputRepository.putArtifact(sha1Context, shaFile);
                    }finally{
                        shaFile.delete();
                    }
                }
            }
            
            if (descriptorContext != null) {
                outputRepository.putArtifact(descriptorContext, new File(descriptor));
            }
        }catch(CMRException x){
            throw new ImportJarException("error.failedWriteArtifact", new Object[]{context, x.getLocalizedMessage()}, x);
        }catch(Exception x){
            // FIXME: remove when the whole CMR is using CMRException
            throw new ImportJarException("error.failedWriteArtifact", new Object[]{context, x.getLocalizedMessage()}, x);
        }
    }
    
    // Check the properties descriptor file for problems and at the same time
    // remove all classes that are found within the imported modules
    // from the given set of external class names
    private void checkModuleProperties(File descriptorFile, Set<String> externalClasses) throws IOException {
        try{
            Set<ModuleInfo> dependencies = PropertiesDependencyResolver.INSTANCE.resolveFromFile(descriptorFile);
            checkDependencies(dependencies, externalClasses);
        }catch(ImportJarException x){
            throw x;
        }catch(IOException x){
            throw x;
        }catch(Exception x){
            throw new ImportJarException("error.descriptorFile.invalid.properties", new Object[]{descriptorFile.getPath()}, x);
        }
    }

    // Check the XML descriptor file for problems and at the same time
    // remove all classes that are found within the imported modules
    // from the given set of external class names
    private void checkModuleXml(File descriptorFile, Set<String> externalClasses) throws IOException {
        try{
            Set<ModuleInfo> dependencies = XmlDependencyResolver.INSTANCE.resolveFromFile(descriptorFile);
            checkDependencies(dependencies, externalClasses);
        }catch(ImportJarException x){
            throw x;
        }catch(IOException x){
            throw x;
        }catch(Exception x){
            throw new ImportJarException("error.descriptorFile.invalid.xml", new Object[]{descriptorFile.getPath(), x.getMessage()}, x);
        }
    }

    // Check the given dependencies for problems and at the same time
    // remove all classes that are found within the imported modules
    // from the given set of external class names
    private void checkDependencies(Set<ModuleInfo> dependencies, Set<String> externalClasses) throws IOException {
        if (!dependencies.isEmpty()) {
            msg("info.checkingDependencies").newline();
            TreeSet<ModuleInfo> sortedDeps = new TreeSet<>(dependencies);
            for (ModuleInfo dep : sortedDeps) {
                String name = dep.getName();
                String version = dep.getVersion();
                // missing dep is OK, it can be fixed later, but invalid module/dependency is not OK
                if(name == null || name.isEmpty())
                    throw new ImportJarException("error.descriptorFile.invalid.module", new Object[]{name}, null);
                if(ModuleUtil.isDefaultModule(name))
                    throw new ImportJarException("error.descriptorFile.invalid.module.default");
                if(version == null || version.isEmpty())
                    throw new ImportJarException("error.descriptorFile.invalid.module.version", new Object[]{version}, null);
                append("    ").append(dep).append(" ... [");
                if (JDKUtils.isJDKModule(name) || JDKUtils.isOracleJDKModule(name)) {
                    if (removeMatchingJdkClasses(externalClasses, name)) {
                        if (dep.isExport()) {
                            msg("info.ok");
                        } else {
                            msg("error.markShared");
                            hasErrors = true;
                        }
                    } else {
                        if (dep.isExport()) {
                            msg("info.okButUnused");
                        } else {
                            msg("info.ok");
                        }
                    }
                } else {
                    ArtifactContext context = new ArtifactContext(name, dep.getVersion(), ArtifactContext.CAR, ArtifactContext.JAR);
                    File artifact = getRepositoryManager().getArtifact(context);
                    if (artifact != null && artifact.exists()) {
                        try {
                            Set<String> importedClasses = gatherClassnamesFromJar(artifact);
                            if (removeMatchingClasses(externalClasses, importedClasses)) {
                                if (dep.isExport()) {
                                    msg("info.ok");
                                } else {
                                    msg("error.markShared");
                                    hasErrors = true;
                                }
                            } else {
                                if (dep.isExport()) {
                                    msg("info.okButUnused");
                                } else {
                                    msg("info.ok");
                                }
                            }
                        } catch (IOException e) {
                            msg("error.checkFailed");
                            hasErrors = true;
                        }
                    } else {
                        msg("error.notFound");
                        hasErrors = true;
                    }
                }
                append("]").newline();
            }
        }
    }

    // Remove all classes that are found within the given set of
    // imported classes from the given set of external classes
    private boolean removeMatchingClasses(Set<String> externalClasses, Set<String> importedClasses) {
        boolean matchesFound = false;
        for (String className : importedClasses) {
            matchesFound |= externalClasses.remove(className);
        }
        return matchesFound;
    }

    // Remove all classes that are part of the given JDK module
    // from the given set of external classes
    private boolean removeMatchingJdkClasses(Set<String> externalClasses, String jdkModule) {
        Set<String> toBeRemoved = new HashSet<>();
        for (String className : externalClasses) {
            String pkgName = getPackageFromClass(className);
            if (JDKUtils.isJDKPackage(jdkModule, pkgName)) {
                toBeRemoved.add(className);
            }
        }
        externalClasses.removeAll(toBeRemoved);
        return !toBeRemoved.isEmpty();
    }

    @Override
    public void run() throws IOException {
        if (!force) {
            checkPublicApi();
        }
        if (!hasErrors) {
            msg("info.noProblems");
            if (!dryRun) {
                msg("info.noProblems.publishing").newline();
                publish();
                msg("info.ok");
            }
            append(".").newline();
        } else {
            throw new ToolUsageError(Messages.msg(ImportJarMessages.RESOURCE_BUNDLE, "error.problemsFound"));
        }
    }
}