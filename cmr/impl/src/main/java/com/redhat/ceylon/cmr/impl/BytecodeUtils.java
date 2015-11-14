/*
 * Copyright 2011 Red Hat inc. and third party contributors as noted
 * by the author tags.
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
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.jboss.jandex.AnnotationInstance;
import org.jboss.jandex.AnnotationValue;
import org.jboss.jandex.ClassInfo;
import org.jboss.jandex.DotName;
import org.jboss.jandex.Index;
import org.jboss.jandex.Indexer;

import com.redhat.ceylon.cmr.api.AbstractDependencyResolver;
import com.redhat.ceylon.cmr.api.ArtifactContext;
import com.redhat.ceylon.cmr.api.DependencyContext;
import com.redhat.ceylon.cmr.api.ModuleDependencyInfo;
import com.redhat.ceylon.cmr.api.ModuleInfo;
import com.redhat.ceylon.cmr.api.ModuleVersionArtifact;
import com.redhat.ceylon.cmr.api.ModuleVersionDetails;
import com.redhat.ceylon.cmr.api.Overrides;
import com.redhat.ceylon.cmr.spi.Node;
import com.redhat.ceylon.common.JVMModuleUtil;
import com.redhat.ceylon.common.ModuleUtil;
import com.redhat.ceylon.model.cmr.ArtifactResult;

/**
 * Byte hacks / utils.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public final class BytecodeUtils extends AbstractDependencyResolver implements ModuleInfoReader {
    public static BytecodeUtils INSTANCE = new BytecodeUtils();

    private BytecodeUtils() {
    }

    private static final DotName MODULE_ANNOTATION = DotName.createSimple("com.redhat.ceylon.compiler.java.metadata.Module");
    private static final DotName PACKAGE_ANNOTATION = DotName.createSimple("com.redhat.ceylon.compiler.java.metadata.Package");
    private static final DotName CEYLON_ANNOTATION = DotName.createSimple("com.redhat.ceylon.compiler.java.metadata.Ceylon");
    private static final DotName IGNORE_ANNOTATION = DotName.createSimple("com.redhat.ceylon.compiler.java.metadata.Ignore");
    private static final DotName LOCAL_CONTAINER_ANNOTATION = DotName.createSimple("com.redhat.ceylon.compiler.java.metadata.LocalContainer");

    @Override
    public ModuleInfo resolve(DependencyContext context, Overrides overrides) {
        if (context.ignoreInner()) {
            return null;
        }

        final ArtifactResult result = context.result();
        return readModuleInformation(result.name(), result.artifact(), overrides);
    }

    @Override
    public ModuleInfo resolveFromFile(File file, String name, String version, Overrides overrides) {
        throw new UnsupportedOperationException("Operation not supported for .car files");
    }

    @Override
    public ModuleInfo resolveFromInputStream(InputStream stream, String name, String version, Overrides overrides) {
        throw new UnsupportedOperationException("Operation not supported for .car files");
    }

    @Override
    public Node descriptor(Node artifact) {
        return null; // artifact is a descriptor
    }

    /**
     * Read module info from bytecode.
     *
     * @param moduleName the module name
     * @param jarFile    the module jar file
     * @return module info list
     */
    private static ModuleInfo readModuleInformation(final String moduleName, final File jarFile, Overrides overrides) {
        Index index = readModuleIndex(jarFile, false);
        final AnnotationInstance ai = getAnnotation(index, moduleName, MODULE_ANNOTATION);
        if (ai == null)
            return null;
        final AnnotationValue version = ai.value("version");
        if(version == null)
            return null;
        
        final AnnotationValue dependencies = ai.value("dependencies");
        if (dependencies == null)
            return new ModuleInfo(null, Collections.<ModuleDependencyInfo>emptySet());

        final Set<ModuleDependencyInfo> infos = new LinkedHashSet<ModuleDependencyInfo>();

        final AnnotationInstance[] imports = dependencies.asNestedArray();
        if (imports != null){
            for (AnnotationInstance im : imports) {
                final String name = asString(im, "name");
                final ModuleDependencyInfo mi = new ModuleDependencyInfo(
                        name,
                        asString(im, "version"),
                        asBoolean(im, "optional"),
                        asBoolean(im, "export"));
                infos.add(mi);
            }
        }
        ModuleInfo ret = new ModuleInfo(null, infos);
        if(overrides != null)
            ret = overrides.applyOverrides(moduleName, version.asString(), ret);
        return ret;
    }

    private static Index readModuleIndex(final File jarFile, boolean everything) {
        try {
            try(JarFile jar = new JarFile(jarFile)){
                Enumeration<JarEntry> entries = jar.entries();
                Indexer indexer = new Indexer();
                while (entries.hasMoreElements()) {
                    JarEntry entry = entries.nextElement();
                    String name = entry.getName().toLowerCase();
                    if(everything && name.endsWith(".class")
                            || name.endsWith("/module_.class")
                            || name.endsWith("/$module_.class")){
                        try(InputStream stream = jar.getInputStream(entry)){
                            indexer.index(stream);
                        }
                    }
                }
                return indexer.complete();
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read index for module " + jarFile.getPath(), e);
        }
    }
    
    private static ClassInfo getModuleInfo(final Index index, final String moduleName) {
        // we need to escape any java keyword from the package list
        String quotedModuleName = JVMModuleUtil.quoteJavaKeywords(moduleName);
        DotName moduleClassName = DotName.createSimple(quotedModuleName + ".$module_");
        ClassInfo ret = index.getClassByName(moduleClassName);
        if(ret == null){
            // read previous module descriptor name
            moduleClassName = DotName.createSimple(quotedModuleName + ".module_");
            ret = index.getClassByName(moduleClassName);
        }
        return ret;
    }

    @Override
    public int[] getBinaryVersions(String moduleName, String moduleVersion, File moduleArchive) {
        Index index = readModuleIndex(moduleArchive, false);
        final AnnotationInstance ceylonAnnotation = getAnnotation(index, moduleName, CEYLON_ANNOTATION);
        if (ceylonAnnotation == null)
            return null;

        AnnotationValue majorAnnotation = ceylonAnnotation.value("major");
        AnnotationValue minorAnnotation = ceylonAnnotation.value("minor");

        int major = majorAnnotation != null ? majorAnnotation.asInt() : 0;
        int minor = minorAnnotation != null ? minorAnnotation.asInt() : 0;
        return new int[]{major, minor};
    }

    @Override
    public ModuleVersionDetails readModuleInfo(String moduleName, String moduleVersion, File moduleArchive, boolean includeMembers, Overrides overrides) {
        Index index = readModuleIndex(moduleArchive, true);
        final AnnotationInstance moduleAnnotation = getAnnotation(index, moduleName, MODULE_ANNOTATION);
        if (moduleAnnotation == null)
            return null;
        
        AnnotationValue doc = moduleAnnotation.value("doc");
        AnnotationValue license = moduleAnnotation.value("license");
        AnnotationValue by = moduleAnnotation.value("by");
        AnnotationValue dependencies = moduleAnnotation.value("dependencies");
        String type = ArtifactContext.getSuffixFromFilename(moduleArchive.getName());
        
        final AnnotationInstance ceylonAnnotation = getAnnotation(index, moduleName, CEYLON_ANNOTATION);
        if (ceylonAnnotation == null)
            return null;

        AnnotationValue majorVer = ceylonAnnotation.value("major");
        AnnotationValue minorVer = ceylonAnnotation.value("minor");

        ModuleVersionDetails mvd = new ModuleVersionDetails(moduleName, getVersionFromFilename(moduleName, moduleArchive.getName()));
        mvd.setDoc(doc != null ? doc.asString() : null);
        mvd.setLicense(license != null ? license.asString() : null);
        if (by != null) {
            mvd.getAuthors().addAll(Arrays.asList(by.asStringArray()));
        }
        mvd.getDependencies().addAll(getDependencies(dependencies, moduleName, mvd.getVersion(), overrides));
        ModuleVersionArtifact mva = new ModuleVersionArtifact(type, majorVer != null ? majorVer.asInt() : 0, minorVer != null ? minorVer.asInt() : 0);
        mvd.getArtifactTypes().add(mva);
        
        if (includeMembers) {
            mvd.setMembers(getMembers(index));
        }
        
        return mvd;
    }

    private Set<String> getMembers(Index index) {
        HashSet<String> members = new HashSet<>(); 
        for (ClassInfo cls : index.getKnownClasses()) {
            if (shouldAddMember(cls)) {
                members.add(classNameToDeclName(cls.name().toString()));
            }
        }
        return members;
    }

    private boolean shouldAddMember(ClassInfo cls) {
        // ignore what we must ignore
        if (getClassAnnotation(cls, IGNORE_ANNOTATION) != null) {
            return false;
        }
        // ignore module and package descriptors
        if (getClassAnnotation(cls, MODULE_ANNOTATION) != null || getClassAnnotation(cls, PACKAGE_ANNOTATION) != null) {
            return false;
        }
        // ignore local types
        if (getClassAnnotation(cls, LOCAL_CONTAINER_ANNOTATION) != null) {
            return false;
        }
        return true;
    }
    
    private AnnotationInstance getClassAnnotation(ClassInfo cls, DotName annoName) {
        List<AnnotationInstance> annos = cls.annotations().get(annoName);
        if (annos != null) {
            // Just return the first one we can find on the class itself
            for (AnnotationInstance anno : annos) {
                if (anno.target() == cls) {
                    return anno;
                }
            }
        }
        return null;
    }
    
    // Returns a fully qualified declaration name making sure that
    // package name and member name are separated by "::"
    private static String classNameToDeclName(String clsName) {
        int lastDot = clsName.lastIndexOf('.');
        String packageName = lastDot != -1 ? clsName.substring(0, lastDot) : "";
        String simpleName = lastDot != -1 ? clsName.substring(lastDot+1) : clsName;
        // ceylon names have mangling for interface members that we pull to toplevel
        simpleName = simpleName.replace("$impl$", ".");
        // turn any dollar sep into a dot
        simpleName = simpleName.replace('$', '.');
        // remove any dollar prefixes and trailing underscores
        return unquotedDeclName(packageName, simpleName);
    }
    
    // Given a fully qualified package and member name returns the full and
    // unquoted declaration name stripped of all special symbols like '$' and '_'
    private static String unquotedDeclName(String pkg, String member) {
        if (pkg != null && !pkg.isEmpty()) {
            return unquoteName(pkg, false) + "::" + unquoteName(member, true);
        } else {
            return unquoteName(member, true);
        }
    }
    
    // Given a name consisting of parts separated by dots returns the unquoted
    // version stripped of all special symbols like '$' and '_'
    private static String unquoteName(String s, boolean stripTrailingUnderscore) {
        if (s != null) {
            String[] parts = JVMModuleUtil.unquoteJavaKeywords(s.split("\\."));
            String name = parts[parts.length - 1];
            if (stripTrailingUnderscore && !name.isEmpty() && Character.isLowerCase(name.charAt(0)) && name.charAt(name.length()-1) == '_') {
                name = name.substring(0, name.length()-1);
            }
            parts[parts.length - 1] = name;
            s = JVMModuleUtil.join(".", parts);
        }
        return s;
    }
    
    private static String getVersionFromFilename(String moduleName, String name) {
        if (!ModuleUtil.isDefaultModule(moduleName)) {
            String type = ArtifactContext.getSuffixFromFilename(name);
            return name.substring(moduleName.length() + 1, name.length() - type.length());
        } else {
            return "";
        }
    }

    private static Set<ModuleDependencyInfo> getDependencies(AnnotationValue dependencies, String module, String version, Overrides overrides) {
        AnnotationInstance[] deps = dependencies.asNestedArray();
        Set<ModuleDependencyInfo> result = new HashSet<ModuleDependencyInfo>(deps.length);
        for (AnnotationInstance dep : deps) {
            AnnotationValue depName = dep.value("name");
            AnnotationValue depVersion = dep.value("version");
            AnnotationValue export = dep.value("export");
            AnnotationValue optional = dep.value("optional");
            
            result.add(new ModuleDependencyInfo(depName.asString(), depVersion.asString(),
                    (optional!= null) && optional.asBoolean(),
                    (export != null) && export.asBoolean()));
        }
        if(overrides != null)
            return overrides.applyOverrides(module, version, new ModuleInfo(null, result)).getDependencies();
        return result;
    }
    
    public boolean matchesModuleInfo(String moduleName, String moduleVersion, File moduleArchive, String query, Overrides overrides) {
        Index index = readModuleIndex(moduleArchive, false);
        final AnnotationInstance moduleAnnotation = getAnnotation(index, moduleName, MODULE_ANNOTATION);
        if (moduleAnnotation == null)
            return false;
        AnnotationValue version = moduleAnnotation.value("version");
        if (version == null)
            return false;
        AnnotationValue doc = moduleAnnotation.value("doc");
        if (doc != null && matches(doc.asString(), query))
            return true;
        AnnotationValue license = moduleAnnotation.value("license");
        if (license != null && matches(license.asString(), query))
            return true;
        AnnotationValue by = moduleAnnotation.value("by");
        if (by != null) {
            for (String author : by.asStringArray()) {
                if (matches(author, query))
                    return true;
            }
        }
        AnnotationValue dependencies = moduleAnnotation.value("dependencies");
        if (dependencies != null) {
            for (ModuleDependencyInfo dep : getDependencies(dependencies, moduleName, version.asString(), overrides)) {
                if (matches(dep.getModuleName(), query))
                    return true;
            }
        }
        return false;
    }

    private static boolean matches(String string, String query) {
        return string.toLowerCase().contains(query);
    }

    private static String asString(AnnotationInstance ai, String name) {
        final AnnotationValue av = ai.value(name);
        if (av == null)
            throw new IllegalArgumentException("Missing required annotation attribute: " + name);
        return av.asString();
    }

    private static boolean asBoolean(AnnotationInstance ai, String name) {
        final AnnotationValue av = ai.value(name);
        return (av != null) && av.asBoolean();
    }

    private static AnnotationInstance getAnnotation(Index index, String moduleName, DotName annotationName) {
        final ClassInfo moduleClass = getModuleInfo(index, moduleName);
        if (moduleClass == null)
            return null;
        
        List<AnnotationInstance> annotations = moduleClass.annotations().get(annotationName);
        if (annotations == null || annotations.isEmpty())
            return null;
        
        return annotations.get(0);
    }
}
