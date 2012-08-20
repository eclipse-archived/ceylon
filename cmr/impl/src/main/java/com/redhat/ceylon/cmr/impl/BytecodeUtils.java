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

import org.jboss.jandex.*;

import com.redhat.ceylon.cmr.api.ArtifactLookupVersion;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Byte hacks / utils.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public final class BytecodeUtils {
    private BytecodeUtils() {
    }

    private static final String JAVA = "java";
    private static final DotName MODULE_ANNOTATION = DotName.createSimple("com.redhat.ceylon.compiler.java.metadata.Module");

    /**
     * Read module info from bytecode.
     *
     * @param moduleName the module name
     * @param jarFile    the module jar file
     * @return module info list
     */
    public static List<ModuleInfo> readModuleInformation(final String moduleName, final File jarFile) {
        final ClassInfo moduleClass = getModuleInfo(moduleName, jarFile);
        if (moduleClass == null)
            return Collections.emptyList();

        List<AnnotationInstance> annotations = moduleClass.annotations().get(MODULE_ANNOTATION);
        if (annotations == null || annotations.isEmpty())
            return Collections.emptyList();

        final AnnotationInstance ai = annotations.get(0);
        final AnnotationValue dependencies = ai.value("dependencies");
        if (dependencies == null)
            return Collections.emptyList();

        final AnnotationInstance[] imports = dependencies.asNestedArray();
        if (imports == null || imports.length == 0)
            return Collections.emptyList();

        final List<ModuleInfo> infos = new ArrayList<ModuleInfo>();
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
            throw new RuntimeException("Failed to read index for zip file "+jarFile.getPath(), e);
        }

        final DotName moduleClassName = DotName.createSimple(moduleName + ".module");
        final ClassInfo moduleClass = index.getClassByName(moduleClassName);
        return moduleClass;
    }

    public static void readModuleInfo(String moduleName, File moduleArchive, ArtifactLookupVersion version){
        final ClassInfo moduleClass = getModuleInfo(moduleName, moduleArchive);
        if(moduleClass == null)
            return;
        
        List<AnnotationInstance> annotations = moduleClass.annotations().get(MODULE_ANNOTATION);
        if (annotations == null || annotations.isEmpty())
            return;

        final AnnotationInstance moduleAnnotation = annotations.get(0);
        AnnotationValue doc = moduleAnnotation.value("doc");
        if(doc != null)
            version.setDoc(doc.asString());
        AnnotationValue license = moduleAnnotation.value("license");
        if(license != null)
            version.setLicense(license.asString());
        AnnotationValue by = moduleAnnotation.value("by");
        if(by != null)
            version.setBy(by.asStringArray());
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
