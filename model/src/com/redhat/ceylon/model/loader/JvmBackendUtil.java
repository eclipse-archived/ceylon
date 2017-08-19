package com.redhat.ceylon.model.loader;

import static com.redhat.ceylon.model.loader.AbstractModelLoader.CEYLON_CEYLON_ANNOTATION;
import static com.redhat.ceylon.model.loader.AbstractModelLoader.CEYLON_NAME_ANNOTATION;
import static com.redhat.ceylon.model.loader.NamingBase.stripLeadingDollar;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.getClassOrInterfaceContainer;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.getSignature;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.isEnumeratedConstructorInLocalVariable;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.isVariadic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import com.redhat.ceylon.common.BooleanUtil;
import com.redhat.ceylon.common.IOUtil;
import com.redhat.ceylon.common.ModuleSpec;
import com.redhat.ceylon.common.ModuleUtil;
import com.redhat.ceylon.model.cmr.ArtifactResult;
import com.redhat.ceylon.model.cmr.ArtifactResultType;
import com.redhat.ceylon.model.cmr.Exclusion;
import com.redhat.ceylon.model.cmr.ModuleScope;
import com.redhat.ceylon.model.cmr.PathFilter;
import com.redhat.ceylon.model.cmr.Repository;
import com.redhat.ceylon.model.cmr.RepositoryException;
import com.redhat.ceylon.model.cmr.VisibilityType;
import com.redhat.ceylon.model.loader.mirror.AnnotatedMirror;
import com.redhat.ceylon.model.loader.mirror.AnnotationMirror;
import com.redhat.ceylon.model.loader.mirror.ClassMirror;
import com.redhat.ceylon.model.typechecker.model.Class;
import com.redhat.ceylon.model.typechecker.model.ClassAlias;
import com.redhat.ceylon.model.typechecker.model.ClassOrInterface;
import com.redhat.ceylon.model.typechecker.model.Constructor;
import com.redhat.ceylon.model.typechecker.model.Declaration;
import com.redhat.ceylon.model.typechecker.model.Function;
import com.redhat.ceylon.model.typechecker.model.FunctionOrValue;
import com.redhat.ceylon.model.typechecker.model.Functional;
import com.redhat.ceylon.model.typechecker.model.Interface;
import com.redhat.ceylon.model.typechecker.model.ModelUtil;
import com.redhat.ceylon.model.typechecker.model.Parameter;
import com.redhat.ceylon.model.typechecker.model.Scope;
import com.redhat.ceylon.model.typechecker.model.Specification;
import com.redhat.ceylon.model.typechecker.model.Type;
import com.redhat.ceylon.model.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.model.typechecker.model.TypedDeclaration;
import com.redhat.ceylon.model.typechecker.model.Value;

public class JvmBackendUtil {
    
    public static boolean isInitialLowerCase(String name) {
        return !name.isEmpty() 
            && isLowerCase(name.codePointAt(0));
    }

    public static boolean isLowerCase(int codepoint) {
        return Character.isLowerCase(codepoint) 
            || codepoint == '_';
    }

    public static String getName(List<String> parts){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < parts.size(); i++) {
            sb.append(parts.get(i));
            if (i < parts.size() - 1) {
                sb.append('.');
            }
        }
        return sb.toString();
    }

    public static String getMirrorName(AnnotatedMirror mirror) {
        String name;
        AnnotationMirror annot = mirror.getAnnotation(CEYLON_NAME_ANNOTATION);
        if (annot != null) {
            name = (String)annot.getValue();
        } else {
            name = mirror.getName();
            name = name.isEmpty() ? name : stripLeadingDollar(name);
            if (mirror instanceof ClassMirror
                    && isInitialLowerCase(name)
                    && name.endsWith("_")
                    && mirror.getAnnotation(CEYLON_CEYLON_ANNOTATION) != null) {
                name = name.substring(0, name.length()-1);
            }
        }
        return name;
    }

    public static boolean isSubPackage(String moduleName, String pkgName) {
        return pkgName.equals(moduleName)
            || pkgName.startsWith(moduleName+".");
    }

    /**
     * Removes the given character from the given string. More efficient than using String.replace
     * which uses regexes.
     */
    public static String removeChar(char c, String string) {
        int nextChar = string.indexOf(c);
        if(nextChar == -1)
            return string;
        int start = 0;
        StringBuilder ret = new StringBuilder(string.length()-1);// we remove at least one
        while(nextChar != -1){
            ret.append(string, start, nextChar);
            start = nextChar+1;
            nextChar = string.indexOf(c, start);
        }
        // don't forget the end part
        ret.append(string, start, string.length());
        return ret.toString();
    }

    public static String strip(String name, boolean isCeylon, boolean isShared) {
        String stripped = stripLeadingDollar(name);
        String privSuffix = NamingBase.Suffix.$priv$.name();
        if(isCeylon && !isShared && name.endsWith(privSuffix))
            return stripped.substring(0, stripped.length() - privSuffix.length());
        return stripped;
    }

    /**
     * Determines whether the declaration is a non-transient non-parameter value 
     * (not a getter)
     * @param decl The declaration
     * @return true if the declaration is a value
     */
    public static boolean isValue(Declaration decl) {
        return decl instanceof Value
            && !((Value)decl).isParameter()
            && !((Value)decl).isTransient();
    }

    /**
     * Determines whether the declaration's is a method
     * @param decl The declaration
     * @return true if the declaration is a method
     */
    public static boolean isMethod(Declaration decl) {
        return decl instanceof Function
            && !((Function)decl).isParameter();
    }

    public static Declaration getTopmostRefinedDeclaration(Declaration decl){
        return getTopmostRefinedDeclaration(decl, null);
    }

    public static Declaration getTopmostRefinedDeclaration(Declaration decl, 
            Map<Function, Function> methodOverrides){
        if (decl instanceof FunctionOrValue
                && ((FunctionOrValue)decl).isParameter()
                && decl.getContainer() instanceof Class) {
            // Parameters in a refined class are not considered refinements themselves
            // We have in find the refined attribute
            Class c = (Class)decl.getContainer();
            boolean isAlias = c.isAlias();
            boolean isActual = c.isActual();
            // aliases and actual classes actually do refine their extended type parameters so the same rules apply wrt
            // boxing and stuff
            if (isAlias || isActual) {
                Functional ctor = null;
                int index = c.getParameterList().getParameters().indexOf(findParamForDecl(((TypedDeclaration)decl)));
                // ATM we only consider aliases if we're looking at aliases, and same for actual, not mixing the two.
                // Note(Stef): not entirely sure about that one, what about aliases of actual classes?
                while ((isAlias && c.isAlias())
                        || (isActual && c.isActual())) {
                    ctor = (isAlias && c.isAlias()) ? (Functional)((ClassAlias)c).getConstructor() : c;
                    Type et = c.getExtendedType();
                    c = et!=null && et.isClass() ? (Class)et.getDeclaration() : null;
                    // handle compile errors
                    if(c == null)
                        return null;
                }
                if (isActual) {
                    ctor = c;
                }
                // be safe
                if(ctor == null 
                        || ctor.getParameterLists() == null
                        || ctor.getParameterLists().isEmpty()
                        || ctor.getFirstParameterList() == null
                        || ctor.getFirstParameterList().getParameters() == null
                        || ctor.getFirstParameterList().getParameters().size() <= index)
                    return null;
                decl = ctor.getFirstParameterList().getParameters().get(index).getModel();
            }
            if (decl.isShared()) {
                Declaration refinedDecl = 
                        c.getRefinedMember(decl.getName(), getSignature(decl), isVariadic(decl));
                if(refinedDecl != null && !ModelUtil.equal(refinedDecl, decl)) {
                    return getTopmostRefinedDeclaration(refinedDecl, methodOverrides);
                }
            }
            return decl;
        } else if(decl instanceof FunctionOrValue
                && ((FunctionOrValue)decl).isParameter() // a parameter
                && ((decl.getContainer() instanceof Function && !(((Function)decl.getContainer()).isParameter())) // that's not parameter of a functional parameter 
                        || decl.getContainer() instanceof Specification // or is a parameter in a specification
                        || (decl.getContainer() instanceof Function  
                            && ((Function)decl.getContainer()).isParameter() 
                            && createMethod((Function)decl.getContainer())))) {// or is a class functional parameter
            // Parameters in a refined method are not considered refinements themselves
            // so we have to look up the corresponding parameter in the container's refined declaration
            Functional func = (Functional)getParameterized((FunctionOrValue)decl);
            if(func == null)
                return decl;
            Declaration kk = getTopmostRefinedDeclaration((Declaration)func, methodOverrides);
            // error recovery
            if(kk instanceof Functional == false)
                return decl;
            Functional refinedFunc = (Functional) kk;
            // shortcut if the functional doesn't override anything
            if (ModelUtil.equal((Declaration)refinedFunc, (Declaration)func)) {
                return decl;
            }
            if (func.getParameterLists().size() != refinedFunc.getParameterLists().size()) {
                // invalid input
                return decl;
            }
            for (int ii = 0; ii < func.getParameterLists().size(); ii++) {
                if (func.getParameterLists().get(ii).getParameters().size() != refinedFunc.getParameterLists().get(ii).getParameters().size()) {
                    // invalid input
                    return decl;
                }
                // find the index of the parameter in the declaration
                int index = 0;
                for (Parameter px : func.getParameterLists().get(ii).getParameters()) {
                    if (px.getModel() == null || px.getModel().equals(decl)) {
                        // And return the corresponding parameter from the refined declaration
                        return refinedFunc.getParameterLists().get(ii).getParameters().get(index).getModel();
                    }
                    index++;
                }
                continue;
            }
        }else if(methodOverrides != null
                && decl instanceof Function
                && ModelUtil.equal(decl.getRefinedDeclaration(), decl)
                && decl.getContainer() instanceof Specification
                && ((Specification)decl.getContainer()).getDeclaration() instanceof Function
                && ((Function) ((Specification)decl.getContainer()).getDeclaration()).isShortcutRefinement()
                // we do all the previous ones first because they are likely to filter out false positives cheaper than the
                // hash lookup we do next to make sure it is really one of those cases
                && methodOverrides.containsKey(decl)){
            // special case for class X() extends T(){ m = function() => e; } which we inline
            decl = methodOverrides.get(decl);
        }
        Declaration refinedDecl = decl.getRefinedDeclaration();
        if(refinedDecl != null && !ModelUtil.equal(refinedDecl, decl))
            return getTopmostRefinedDeclaration(refinedDecl);
        return decl;
    }

    public static Parameter findParamForDecl(TypedDeclaration decl) {
        String attrName = decl.getName();
        return findParamForDecl(attrName, decl);
    }
    
    public static Parameter findParamForDecl(String attrName, TypedDeclaration decl) {
        Parameter result = null;
        if (decl.getContainer() instanceof Functional) {
            Functional f = (Functional)decl.getContainer();
            result = f.getParameter(attrName);
        }
        return result;
    }

    public static Declaration getParameterized(FunctionOrValue methodOrValue) {
        if (!methodOrValue.isParameter()) {
            return null;
        }
        Scope scope = methodOrValue.getContainer();
        if (scope instanceof Specification) {
            return ((Specification)scope).getDeclaration();
        } else if (scope instanceof Declaration) {
            return (Declaration)scope;
        } 
        return null;
    }

    public static boolean createMethod(FunctionOrValue model) {
        return model instanceof Function
            && model.isParameter()
            && model.isClassMember()
            && ModelUtil.isCaptured(model);
    }

    public static boolean supportsReified(Declaration declaration){
        if(declaration instanceof ClassOrInterface){
            // Java constructors don't support reified type arguments
            return ModelUtil.isCeylonDeclaration((TypeDeclaration) declaration);
        }else if(declaration instanceof Function){
            if (((Function)declaration).isParameter()) {
                // those can never be parameterised
                return false;
            }
            if(declaration.isToplevel())
                return true;
            // Java methods don't support reified type arguments
            Function m = (Function) getTopmostRefinedDeclaration(declaration);
            // See what its container is
            ClassOrInterface container = getClassOrInterfaceContainer(m);
            // a method which is not a toplevel and is not a class method, must be a method within method and
            // that must be Ceylon so it supports it
            if(container == null)
                return true;
            return supportsReified(container);
        }else if(declaration instanceof Constructor){
            // Java constructors don't support reified type arguments
            return ModelUtil.isCeylonDeclaration((Constructor) declaration);
        }else{
            return false;
        }
    }
    
    public static boolean isCompanionClassNeeded(TypeDeclaration decl) {
        return decl instanceof Interface 
            && BooleanUtil.isNotFalse(((Interface)decl).isCompanionClassNeeded());
    }

    /**
     * Determines whether the given attribute should be accessed and assigned 
     * via a {@code VariableBox}
     */
    public static boolean isBoxedVariable(TypedDeclaration attr) {
        return (ModelUtil.isNonTransientValue(attr)
                && ModelUtil.isLocalNotInitializer(attr)
                && (attr.isVariable() && attr.isCaptured()
                        // self-captured objects must also be boxed like variables
                        || attr.isSelfCaptured()))
            || attr instanceof Value 
                && isEnumeratedConstructorInLocalVariable((Value) attr);
    }

    private static String getArrayName(TypeDeclaration decl) {
        if(decl instanceof Class == false)
            return null;
        return decl.getQualifiedNameString();
    }
    
    public static boolean isJavaArray(TypeDeclaration decl) {
        String name = getArrayName(decl);
        return "java.lang::ObjectArray".equals(name)
            || "java.lang::ByteArray".equals(name)
            || "java.lang::ShortArray".equals(name)
            || "java.lang::IntArray".equals(name)
            || "java.lang::LongArray".equals(name)
            || "java.lang::FloatArray".equals(name)
            || "java.lang::DoubleArray".equals(name)
            || "java.lang::BooleanArray".equals(name)
            || "java.lang::CharArray".equals(name);
    }
    
    public static boolean isJavaBooleanArray(TypeDeclaration decl) {
        return "java.lang::BooleanArray".equals(getArrayName(decl));
    }
    
    public static boolean isJavaByteArray(TypeDeclaration decl) {
        return "java.lang::ByteArray".equals(getArrayName(decl));
    }
    
    public static boolean isJavaShortArray(TypeDeclaration decl) {
        return "java.lang::ShortArray".equals(getArrayName(decl));
    }
    
    public static boolean isJavaIntArray(TypeDeclaration decl) {
        return "java.lang::IntArray".equals(getArrayName(decl));
    }
    
    public static boolean isJavaLongArray(TypeDeclaration decl) {
        return "java.lang::LongArray".equals(getArrayName(decl));
    }
    
    public static boolean isJavaFloatArray(TypeDeclaration decl) {
        return "java.lang::FloatArray".equals(getArrayName(decl));
    }
    
    public static boolean isJavaDoubleArray(TypeDeclaration decl) {
        return "java.lang::DoubleArray".equals(getArrayName(decl));
    }
    
    public static boolean isJavaCharArray(TypeDeclaration decl) {
        return "java.lang::CharArray".equals(getArrayName(decl));
    }
    
    public static SortedSet<String> listPackages(File jar, PathFilter pathFilter) throws IOException {
        SortedSet<String> packages = new TreeSet<String>();
        // jarless modules don't have packages
        if(ModuleUtil.isMavenJarlessModule(jar))
            return packages;
        try(ZipFile zf = new ZipFile(jar)){
            Enumeration<? extends ZipEntry> entries = zf.entries();
            while(entries.hasMoreElements()){
                ZipEntry entry = entries.nextElement();
                if(entry.isDirectory())
                    continue;
                String pkg = getPackageForPath(entry.getName(), pathFilter);
                if(pkg != null)
                    packages.add(pkg);
            }
        }
        return packages;
    }

    private static String getPackageForPath(String path, PathFilter pathFilter) {
        if(definesPackage(path)){
            int sep = path.lastIndexOf('/');
            if(sep != -1)
                path = path.substring(0, sep);
            else
                path = "";// default package
            String pkg = path;
            // make sure we unquote any package part
            pkg = pkg.replace("$", "");
            String pathQuery;
            if(path.isEmpty())
                pathQuery = pkg;
            else
                pathQuery = pkg+"/";
            if(pathFilter == null || pathFilter.accept(pathQuery)){
                pkg = pkg.replace('/', '.');
                return pkg;
            }
        }
        return null;
    }
    
    public static boolean definesPackage(String path) {
        return path.toLowerCase().endsWith(".class")
            // skip Java 9 module descriptors
            && !path.equals("module-info.class");
    }

    public static void writeStaticMetamodel(File outputFolder, List<ArtifactResult> entries, JdkProvider jdkProvider, String metaInfEntry) throws IOException {
        File meta = new File(outputFolder, metaInfEntry + "/ceylon");
        meta.mkdirs();
        File metamodel = new File(meta, "metamodel");
        try(FileWriter ret = new FileWriter(metamodel)){
            writeStaticMetamodel(ret, entries, jdkProvider, Collections.<String>emptySet());
        }
    }

    public static void writeStaticMetamodel(File outputFolder, List<ArtifactResult> entries, JdkProvider jdkProvider) throws IOException {
        writeStaticMetamodel(outputFolder, entries, jdkProvider, "META-INF");
        
    }

    public static void writeStaticMetamodel(ZipOutputStream outputZip, Set<String> added, 
            List<ArtifactResult> entries, JdkProvider jdkProvider,
            Set<String> providedModules) throws IOException {
        if(added.add("META-INF/")){
            ZipEntry entry = new ZipEntry("META-INF/");
            outputZip.putNextEntry(entry);
        }
        if(added.add("META-INF/ceylon/")){
            ZipEntry entry = new ZipEntry("META-INF/ceylon/");
            outputZip.putNextEntry(entry);
        }
        outputZip.putNextEntry(new ZipEntry("META-INF/ceylon/metamodel"));
        Writer ret = new OutputStreamWriter(outputZip);

        writeStaticMetamodel(ret, entries, jdkProvider, providedModules);
    }

    private static void writeStaticMetamodel(Writer ret, List<ArtifactResult> entries, JdkProvider jdkProvider, Set<String> providedModules) throws IOException {
        if(jdkProvider.isAlternateJdk()){
            for (String jdkModule : jdkProvider.getJDKModuleNames()) {
                ret.write("="+jdkModule+"/"+jdkProvider.getJDKVersion()+"\n");
                for (String pkg : jdkProvider.getJDKPackages(jdkModule)) {
                    ret.write("@"+pkg+"\n");
                }
            }
        }

        for(ArtifactResult entry : entries){
            writeStaticMetamodel(ret, entry, jdkProvider, providedModules);
        }
        ret.flush();
    }

    private static void writeStaticMetamodel(Writer metamodelOs, ArtifactResult entry, JdkProvider jdkProvider, Set<String> providedModules) throws IOException {
        if(entry.version() != null)
            metamodelOs.write("="+entry.name()+"/"+entry.version()+"\n");
        else // default module
            metamodelOs.write("="+entry.name()+"\n");
        // skip dependencies of provided modules as they're not trusted
        if(!providedModules.contains(entry.name())){
            for (ArtifactResult dep : entry.dependencies()) {
                if(dep.exported())
                    metamodelOs.write("+");
                if(dep.optional())
                    metamodelOs.write("?");
                metamodelOs.write(dep.name()+"/"+dep.version()+"\n");
            }
        }
        listPackages(metamodelOs, entry.name(), entry.artifact(), jdkProvider);
    }

    private static void listPackages(Writer metamodelOs, String name, File artifact, JdkProvider jdkProvider) throws ZipException, IOException {
        List<String> jdkPackageList = null;
        if(name.equals(jdkProvider.getJdkContainerModuleName())){
            jdkPackageList = jdkProvider.getJDKPackageList();
        }
        Set<String> packages = JvmBackendUtil.listPackages(artifact, null);
        for (String pkg : packages) {
            if(jdkPackageList != null && jdkPackageList.contains(pkg))
                continue;
            metamodelOs.write("@"+pkg+"\n");
        }
    }

    public static void loadStaticMetamodel(InputStream is, List<String> dexEntries, StaticMetamodelLoader staticMetamodelLoader) {
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            String line;
            ModuleSpec module = null;
            SortedSet<String> packages = new TreeSet<>();
            List<ArtifactResult> imports = new LinkedList<ArtifactResult>();
            while((line = reader.readLine()) != null){
                if(line.startsWith("=")){
                    if(module != null)
                        finishLoadingModule(module, packages, imports, dexEntries, staticMetamodelLoader);
                    module = ModuleSpec.parse(line.substring(1));
                    packages.clear();
                    imports.clear();
                    continue;
                }
                boolean _optional = false;
                boolean _shared = false;
                if(line.startsWith("?")){
                    _optional = true;
                    line = line.substring(1);
                }
                if(line.startsWith("+")){
                    _shared = true;
                    line = line.substring(1);
                }
                // SERIOUSLY!!
                final boolean optional = _optional;
                final boolean shared = _shared;
                if(line.startsWith("@")){
                    packages.add(line.substring(1));
                    continue;
                }
                // it's an import
                ModuleSpec importSpec = ModuleSpec.parse(line);
                final String namespace = ModuleUtil.getNamespaceFromUri(importSpec.getName());
                final String name = ModuleUtil.getModuleNameFromUri(importSpec.getName());
                final String version = importSpec.getVersion();
                imports.add(new ArtifactResult(){
                    @Override
                    public String namespace() {
                        return namespace;
                    }

                    @Override
                    public String name() {
                        return name;
                    }

                    @Override
                    public String version() {
                        return version;
                    }

                    @Override
                    public boolean optional() {
                        return optional;
                    }

                    @Override
                    public boolean exported() {
                        return shared;
                    }

                    @Override
                    public ArtifactResultType type() {
                        // Is this important?
                        return ArtifactResultType.OTHER;
                    }

                    @Override
                    public VisibilityType visibilityType() {
                        return VisibilityType.STRICT;
                    }

                    @Override
                    public File artifact() throws RepositoryException {
                        return null;
                    }

                    @Override
                    public PathFilter filter() {
                        return null;
                    }

                    @Override
                    public List<ArtifactResult> dependencies() throws RepositoryException {
                        return null;
                    }

                    @Override
                    public String artifactId() {
                        return ModuleUtil.getMavenArtifactIdIfMavenModule(name);
                    }
                    
                    @Override
                    public String groupId() {
                        return ModuleUtil.getMavenGroupIdIfMavenModule(name);
                    }
                    
                    @Override
                    public String classifier() {
                        return ModuleUtil.getMavenClassifierIfMavenModule(name);
                    }
                    
                    @Override
                    public String repositoryDisplayString() {
                        return "Android dependency";
                    }

                    @Override
                    public Repository repository() {
                        return null;
                    }

                    @Override
                    public ModuleScope moduleScope() {
                        return ModuleScope.COMPILE;
                    }

                    @Override
                    public List<Exclusion> getExclusions() {
                        return null;
                    }
                    
                });
            }
            if(module != null)
                finishLoadingModule(module, packages, imports, dexEntries, staticMetamodelLoader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void finishLoadingModule(ModuleSpec module, 
            SortedSet<String> packages, 
            List<ArtifactResult> dependencies, 
            final List<String> dexEntries, 
            StaticMetamodelLoader staticMetamodelLoader) {
        final SortedSet<String> packagesCopy = new TreeSet<>(packages);
        final List<ArtifactResult> dependenciesCopy = new ArrayList<>(dependencies);
        
        final String namespace = ModuleUtil.getNamespaceFromUri(module.getName());
        final String name = ModuleUtil.getModuleNameFromUri(module.getName());
        final String version = module.getVersion();
        ArtifactResult artifact = new ContentAwareArtifactResult() {
            
            @Override
            public VisibilityType visibilityType() {
                return VisibilityType.STRICT;
            }
            
            @Override
            public String version() {
                return version;
            }
            
            @Override
            public ArtifactResultType type() {
                return ArtifactResultType.OTHER;
            }
            
            @Override
            public String repositoryDisplayString() {
                return "Android repo";
            }
            
            @Override
            public Repository repository() {
                return null;
            }
            
            @Override
            public String namespace() {
                return namespace;
            }

            @Override
            public String name() {
                return name;
            }
            
            @Override
            public String artifactId() {
                return ModuleUtil.getMavenArtifactIdIfMavenModule(name);
            }
            
            @Override
            public String groupId() {
                return ModuleUtil.getMavenGroupIdIfMavenModule(name);
            }
            
            @Override
            public String classifier() {
                return ModuleUtil.getMavenClassifierIfMavenModule(name);
            }
            
            @Override
            public boolean exported() {
                return false;
            }

            @Override
            public boolean optional () {
                return false;
            }
            
            @Override
            public PathFilter filter() {
                return null;
            }
            
            @Override
            public List<ArtifactResult> dependencies() throws RepositoryException {
                return dependenciesCopy;
            }
            
            @Override
            public File artifact() throws RepositoryException {
                return null;
            }
            
            @Override
            public Collection<String> getPackages() {
                return packagesCopy;
            }
            
            @Override
            public List<String> getFileNames(String path) {
                return getAndroidFileNames(path);
            }
            
            private List<String> getAndroidFileNames(String path) {
                path = path+"/";
                List<String> ret = new LinkedList<>();
                for(String entry : dexEntries){
                    if(entry.startsWith(path)){
                        String part = entry.substring(path.length());
                        // no folders and no subfolders
                        if(!part.isEmpty() && part.indexOf('/') == -1)
                            ret.add(entry);
                    }
                }
                return ret;
            }

            @Override
            public Collection<String> getEntries() {
                return getAndroidEntries();
            }
            
            private Collection<String> getAndroidEntries() {
                List<String> ret = new LinkedList<>();
                for(String entry : dexEntries){
                    for (String pkg : packagesCopy) {
                        String path = pkg.replace('.', '/')+"/";
                        if(entry.startsWith(path)){
                            ret.add(entry);
                            break;
                        }
                    }
                }
                return ret;
            }

            @Override
            public byte[] getContents(String path) {
                // this is used by the metamodel module resource system
                try{
                    return IOUtil.readStream(getClass().getClassLoader().getResourceAsStream(path));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            
            @Override
            public URI getContentUri(String path) {
                try {
                    return getClass().getClassLoader().getResource(path).toURI();
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            }
            
            @Override
            public String toString() {
                return "StaticMetamodelArtifact for module "+name+"/"+version;
            }

            @Override
            public ModuleScope moduleScope() {
                return ModuleScope.COMPILE;
            }

            @Override
            public List<Exclusion> getExclusions() {
                return null;
            }
        };
        staticMetamodelLoader.loadModule(module.getName(), module.getVersion(), artifact);
    }

    public static boolean isStaticMetamodel(java.lang.Class<?> fromClass) {
        URL res = fromClass.getResource("/META-INF/ceylon/metamodel");
        if (res == null) {
            res = fromClass.getResource("/ANDROID-META-INF/ceylon/metamodel");
        }
        return res != null;
    }

    public static InputStream getStaticMetamodelInputStream(java.lang.Class<?> fromClass) {
        InputStream stream = fromClass.getResourceAsStream("/META-INF/ceylon/metamodel");
        if (stream == null) {
            stream = fromClass.getResourceAsStream("/ANDROID-META-INF/ceylon/metamodel");
        }
        return stream;
    }

    public static List<String> getCurrentJarEntries() {
        URL url = JvmBackendUtil.class.getProtectionDomain().getCodeSource().getLocation();
        Set<String> entries = new HashSet<>();
        if(url.getProtocol().equals("vfs")){
            // It looks very much like we're on WildFly, so use JBoss VFS to get all our war's lib files
            // This will miss provided modules, but the static metamodel should be able to handle that
            try {
                java.lang.Class<?> virtualFileClass = java.lang.Class.forName("org.jboss.vfs.VirtualFile");
                java.lang.Class<?> vfsClass = java.lang.Class.forName("org.jboss.vfs.VFS");
                Method getChild = vfsClass.getMethod("getChild", URL.class);
                // Fix any improper spaces in the path part
                // See https://github.com/ceylon/ceylon-sdk/issues/629
                URL properURL = new URL(url.getProtocol(), url.getHost(), url.getPort(), url.getPath().replace(" ", "%20"));
                Object thisJar = getChild.invoke(null, properURL);
                Method getParent = virtualFileClass.getMethod("getParent");
                Object libDir = getParent.invoke(thisJar);
                if(libDir != null){
                    // Should be the lib/ folder
                    Method getChildren = virtualFileClass.getMethod("getChildren");
                    Method openStream = virtualFileClass.getMethod("openStream");
                    Method getName = virtualFileClass.getMethod("getName");
                    @SuppressWarnings("unchecked")
                    List<Object> children = (List<Object>) getChildren.invoke(libDir);
                    for(Object child : children){
                        String name = (String) getName.invoke(child);
                        if(name.toLowerCase().endsWith(".jar")){
                            InputStream stream = (InputStream) openStream.invoke(child);
                            scanZipFile(stream, entries);
                        }
                    }
                }
            } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | MalformedURLException e) {
                throw new RuntimeException("Failed to read current fat jar list of entries", e);
            }
        }else{
            try {
                scanZipFile(url.openStream(), entries);
            } catch (IOException e) {
                throw new RuntimeException("Failed to read current fat jar list of entries", e);
            }
        }
        return new ArrayList<>(entries);
    }
    
    private static void scanZipFile(InputStream is, Set<String> entries){
        // on JBoss VFS, the stream is already a ZipInputStream in the case of jars
        ZipInputStream zipFile1 = is instanceof ZipInputStream ? (ZipInputStream)is : new ZipInputStream(is);
        try(ZipInputStream zipFile = zipFile1){
            ZipEntry entry;
            while((entry = zipFile.getNextEntry()) != null){
                if(!entry.isDirectory())
                    entries.add(entry.getName());
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read current fat jar list of entries", e);
        }
    }
}
