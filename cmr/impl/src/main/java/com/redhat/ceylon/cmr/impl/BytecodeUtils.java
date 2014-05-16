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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.redhat.ceylon.cmr.api.AbstractDependencyResolver;
import com.redhat.ceylon.cmr.api.ArtifactContext;
import com.redhat.ceylon.cmr.api.ArtifactResult;
import com.redhat.ceylon.cmr.api.DependencyContext;
import com.redhat.ceylon.cmr.api.ModuleInfo;
import com.redhat.ceylon.cmr.api.ModuleVersionArtifact;
import com.redhat.ceylon.cmr.api.ModuleVersionDetails;
import com.redhat.ceylon.cmr.spi.Node;
import com.redhat.ceylon.common.ModuleUtil;
import org.jboss.jandex.AnnotationInstance;
import org.jboss.jandex.AnnotationValue;
import org.jboss.jandex.ClassInfo;
import org.jboss.jandex.DotName;
import org.jboss.jandex.Index;
import org.jboss.jandex.IndexReader;
import org.jboss.jandex.Indexer;
import org.jboss.jandex.JarIndexer;

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
    private static final DotName CEYLON_ANNOTATION = DotName.createSimple("com.redhat.ceylon.compiler.java.metadata.Ceylon");

    public Set<ModuleInfo> resolve(DependencyContext context) {
        if (context.ignoreInner()) {
            return null;
        }

        final ArtifactResult result = context.result();
        return readModuleInformation(result.name(), result.artifact());
    }

    public Set<ModuleInfo> resolveFromFile(File file) {
        throw new UnsupportedOperationException("Operation not supported for .car files");
    }

    public Set<ModuleInfo> resolveFromInputStream(InputStream stream) {
        throw new UnsupportedOperationException("Operation not supported for .car files");
    }

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
    private static Set<ModuleInfo> readModuleInformation(final String moduleName, final File jarFile) {
        Index index = readModuleIndex(jarFile);
        final AnnotationInstance ai = getAnnotation(index, moduleName, MODULE_ANNOTATION);
        if (ai == null)
            return null;
        final AnnotationValue dependencies = ai.value("dependencies");
        if (dependencies == null)
            return Collections.emptySet();

        final AnnotationInstance[] imports = dependencies.asNestedArray();
        if (imports == null || imports.length == 0)
            return Collections.emptySet();

        final Set<ModuleInfo> infos = new LinkedHashSet<ModuleInfo>();
        for (AnnotationInstance im : imports) {
            final String name = asString(im, "name");
            final ModuleInfo mi = new ModuleInfo(
                    name,
                    asString(im, "version"),
                    asBoolean(im, "optional"),
                    asBoolean(im, "export"));
            infos.add(mi);
        }
        return infos;
    }

    private static Index readModuleIndex(final File jarFile) {
        final Index index;
        try {
            // TODO -- remove this with new Jandex release
            final File indexFile = new File(jarFile.getAbsolutePath().replace(".jar", "-jar") + ".idx");
            // remove the index file if it is older than the jar file
            if (indexFile.exists() && indexFile.lastModified() < jarFile.lastModified())
                indexFile.delete();
            if (indexFile.exists() == false) {
                JarIndexer.createJarIndex(jarFile, new Indexer(), false, false, false);
            }

            try (InputStream stream = new FileInputStream(indexFile)) {
                index = new IndexReader(stream).read();
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read index for module " + jarFile.getPath(), e);
        }
        return index;
    }

    private static ClassInfo getModuleInfo(final Index index, final String moduleName) {
        final DotName moduleClassName = DotName.createSimple(moduleName + ".module_");
        return index.getClassByName(moduleClassName);
    }

    public int[] getBinaryVersions(String moduleName, File moduleArchive) {
        Index index = readModuleIndex(moduleArchive);
        final AnnotationInstance ceylonAnnotation = getAnnotation(index, moduleName, CEYLON_ANNOTATION);
        if (ceylonAnnotation == null)
            return null;

        AnnotationValue majorAnnotation = ceylonAnnotation.value("major");
        AnnotationValue minorAnnotation = ceylonAnnotation.value("minor");

        int major = majorAnnotation != null ? majorAnnotation.asInt() : 0;
        int minor = minorAnnotation != null ? minorAnnotation.asInt() : 0;
        return new int[]{major, minor};
    }

    public ModuleVersionDetails readModuleInfo(String moduleName, File moduleArchive, boolean includeMembers) {
        Index index = readModuleIndex(moduleArchive);
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

        ModuleVersionDetails mvd = new ModuleVersionDetails(getVersionFromFilename(moduleName, moduleArchive.getName()));
        mvd.setDoc(doc != null ? doc.asString() : null);
        mvd.setLicense(license != null ? license.asString() : null);
        if (by != null) {
            mvd.getAuthors().addAll(Arrays.asList(by.asStringArray()));
        }
        mvd.getDependencies().addAll(asModInfos(dependencies));
        ModuleVersionArtifact mva = new ModuleVersionArtifact(type, majorVer != null ? majorVer.asInt() : null, minorVer != null ? minorVer.asInt() : null);
        mvd.getArtifactTypes().add(mva);
        
        if (includeMembers) {
            mvd.setMembers(getMembers(index));
        }
        
        return mvd;
    }

    private Set<String> getMembers(Index index) {
        HashSet<String> members = new HashSet<>(); 
        for (ClassInfo cls : index.getKnownClasses()) {
            members.add(cls.name().toString());
        }
        return members;
    }

    private static String getVersionFromFilename(String moduleName, String name) {
        if (!ModuleUtil.isDefaultModule(moduleName)) {
            String type = ArtifactContext.getSuffixFromFilename(name);
            return name.substring(moduleName.length() + 1, name.length() - type.length());
        } else {
            return "";
        }
    }

    private static List<ModuleInfo> asModInfos(AnnotationValue dependencies) {
        AnnotationInstance[] deps = dependencies.asNestedArray();
        List<ModuleInfo> result = new ArrayList<ModuleInfo>(deps.length);
        for (AnnotationInstance dep : deps) {
            AnnotationValue name = dep.value("name");
            AnnotationValue version = dep.value("version");
            AnnotationValue export = dep.value("export");
            AnnotationValue optional = dep.value("optional");
            
            result.add(new ModuleInfo(name.asString(), version.asString(),
                    (export != null) && export.asBoolean(),
                    (optional != null) && optional.asBoolean()));
        }
        return result;
    }
    
    public boolean matchesModuleInfo(String moduleName, File moduleArchive, String query) {
        Index index = readModuleIndex(moduleArchive);
        final AnnotationInstance moduleAnnotation = getAnnotation(index, moduleName, MODULE_ANNOTATION);
        if (moduleAnnotation == null)
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
            for (ModuleInfo dep : asModInfos(dependencies)) {
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
