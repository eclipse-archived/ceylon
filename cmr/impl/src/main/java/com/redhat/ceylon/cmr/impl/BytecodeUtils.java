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
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.redhat.ceylon.cmr.api.ArtifactResult;
import com.redhat.ceylon.cmr.api.DependencyResolver;
import com.redhat.ceylon.cmr.api.ModuleInfo;
import com.redhat.ceylon.cmr.spi.Node;
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
public final class BytecodeUtils implements DependencyResolver {
    static BytecodeUtils INSTANCE = new BytecodeUtils();

    public static interface ModuleInfoCallback {
        public void storeInfo(String doc, String license, String[] authors);
    }

    private BytecodeUtils() {
    }

    private static final String JAVA = "java";
    private static final DotName MODULE_ANNOTATION = DotName.createSimple("com.redhat.ceylon.compiler.java.metadata.Module");
    private static final DotName CEYLON_ANNOTATION = DotName.createSimple("com.redhat.ceylon.compiler.java.metadata.Ceylon");

    public Set<ModuleInfo> resolve(ArtifactResult result) {
        return readModuleInformation(result.name(), result.artifact());
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
    public static Set<ModuleInfo> readModuleInformation(final String moduleName, final File jarFile) {
        final ClassInfo moduleClass = getModuleInfo(moduleName, jarFile);
        if (moduleClass == null)
            return null;

        List<AnnotationInstance> annotations = moduleClass.annotations().get(MODULE_ANNOTATION);
        if (annotations == null || annotations.isEmpty())
            return Collections.emptySet();

        final AnnotationInstance ai = annotations.get(0);
        final AnnotationValue dependencies = ai.value("dependencies");
        if (dependencies == null)
            return Collections.emptySet();

        final AnnotationInstance[] imports = dependencies.asNestedArray();
        if (imports == null || imports.length == 0)
            return Collections.emptySet();

        final Set<ModuleInfo> infos = new LinkedHashSet<ModuleInfo>();
        for (AnnotationInstance im : imports) {
            final String name = asString(im, "name");
            if (JAVA.equalsIgnoreCase(name) == false) {
                final ModuleInfo mi = new ModuleInfo(
                        name,
                        asString(im, "version"),
                        asBoolean(im, "optional"),
                        asBoolean(im, "export"));
                infos.add(mi);
            }
        }
        return infos;
    }

    private static ClassInfo getModuleInfo(final String moduleName,
                                           final File jarFile) {
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

            final InputStream stream = new FileInputStream(indexFile);
            try {
                index = new IndexReader(stream).read();
            } finally {
                stream.close();
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read index for zip file " + jarFile.getPath(), e);
        }

        final DotName moduleClassName = DotName.createSimple(moduleName + ".module_");
        return index.getClassByName(moduleClassName);
    }

    public static int[] getBinaryVersions(String moduleName, File moduleArchive) {
        final ClassInfo moduleClass = getModuleInfo(moduleName, moduleArchive);
        if (moduleClass == null)
            return null;

        List<AnnotationInstance> annotations = moduleClass.annotations().get(CEYLON_ANNOTATION);
        if (annotations == null || annotations.isEmpty())
            return null;

        final AnnotationInstance moduleAnnotation = annotations.get(0);
        AnnotationValue majorAnnotation = moduleAnnotation.value("major");
        AnnotationValue minorAnnotation = moduleAnnotation.value("minor");

        int major = majorAnnotation != null ? majorAnnotation.asInt() : 0;
        int minor = minorAnnotation != null ? minorAnnotation.asInt() : 0;
        return new int[]{major, minor};
    }


    public static void readModuleInfo(String moduleName, File moduleArchive, ModuleInfoCallback callback) {
        final ClassInfo moduleClass = getModuleInfo(moduleName, moduleArchive);
        if (moduleClass == null)
            return;

        List<AnnotationInstance> annotations = moduleClass.annotations().get(MODULE_ANNOTATION);
        if (annotations == null || annotations.isEmpty())
            return;

        final AnnotationInstance moduleAnnotation = annotations.get(0);
        AnnotationValue doc = moduleAnnotation.value("doc");
        AnnotationValue license = moduleAnnotation.value("license");
        AnnotationValue by = moduleAnnotation.value("by");

        callback.storeInfo(doc != null ? doc.asString() : null,
                license != null ? license.asString() : null,
                by != null ? by.asStringArray() : null);
    }

    public static boolean matchesModuleInfo(String moduleName, File moduleArchive, String query) {
        final ClassInfo moduleClass = getModuleInfo(moduleName, moduleArchive);
        if (moduleClass == null)
            return false;

        List<AnnotationInstance> annotations = moduleClass.annotations().get(MODULE_ANNOTATION);
        if (annotations == null || annotations.isEmpty())
            return false;

        final AnnotationInstance moduleAnnotation = annotations.get(0);
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

}
