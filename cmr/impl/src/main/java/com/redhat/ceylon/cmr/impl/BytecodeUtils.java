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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.redhat.ceylon.cmr.api.AbstractDependencyResolver;
import com.redhat.ceylon.cmr.api.AbstractDependencyResolverAndModuleInfoReader;
import com.redhat.ceylon.cmr.api.ArtifactContext;
import com.redhat.ceylon.cmr.api.DependencyContext;
import com.redhat.ceylon.cmr.api.ModuleDependencyInfo;
import com.redhat.ceylon.cmr.api.ModuleInfo;
import com.redhat.ceylon.cmr.api.ModuleInfoReader;
import com.redhat.ceylon.cmr.api.ModuleVersionArtifact;
import com.redhat.ceylon.cmr.api.ModuleVersionDetails;
import com.redhat.ceylon.cmr.api.Overrides;
import com.redhat.ceylon.cmr.spi.Node;
import com.redhat.ceylon.common.JVMModuleUtil;
import com.redhat.ceylon.common.ModuleUtil;
import com.redhat.ceylon.langtools.classfile.Annotation;
import com.redhat.ceylon.langtools.classfile.ClassFile;
import com.redhat.ceylon.langtools.classfile.ConstantPoolException;
import com.redhat.ceylon.model.cmr.ArtifactResult;
import com.redhat.ceylon.model.loader.ClassFileUtil;
import com.redhat.ceylon.model.typechecker.model.Module;

/**
 * Byte hacks / utils.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public final class BytecodeUtils extends AbstractDependencyResolverAndModuleInfoReader {
    public static BytecodeUtils INSTANCE = new BytecodeUtils();

    private BytecodeUtils() {
    }

    private static final String MODULE_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.Module";
    private static final String PACKAGE_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.Package";
    private static final String CEYLON_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.Ceylon";
    private static final String IGNORE_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.Ignore";
    private static final String LOCAL_CONTAINER_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.LocalContainer";

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
    	ClassFile moduleInfo = readModuleInfo(moduleName, jarFile);
    	if(moduleInfo == null)
    		return null;
    	Annotation ai = ClassFileUtil.findAnnotation(moduleInfo, MODULE_ANNOTATION);
        if (ai == null)
            return null;
        final String version = (String) ClassFileUtil.getAnnotationValue(moduleInfo, ai, "version");
        if(version == null)
            return null;
        
        final Object[] dependencies = (Object[]) ClassFileUtil.getAnnotationValue(moduleInfo, ai, "dependencies");
        if (dependencies == null)
            return new ModuleInfo(moduleName, version, null, Collections.<ModuleDependencyInfo>emptySet());

        final Set<ModuleDependencyInfo> infos = new LinkedHashSet<ModuleDependencyInfo>();

        for (Object im : dependencies) {
        	Annotation dep = (Annotation) im;
        	final String name = (String) ClassFileUtil.getAnnotationValue(moduleInfo, dep, "name");
        	final ModuleDependencyInfo mi = new ModuleDependencyInfo(
        			name,
        			(String) ClassFileUtil.getAnnotationValue(moduleInfo, dep, "version"),
        			asBoolean(moduleInfo, dep, "optional"),
        			asBoolean(moduleInfo, dep, "export"));
        	infos.add(mi);
        }
        ModuleInfo ret = new ModuleInfo(moduleName, version, null, infos);
        if(overrides != null)
            ret = overrides.applyOverrides(moduleName, version, ret);
        return ret;
    }

    private static List<ClassFile> readClassFiles(final File jarFile) {
        try {
            try(JarFile jar = new JarFile(jarFile)){
            	List<ClassFile> ret = new ArrayList<ClassFile>(jar.size());
                Enumeration<JarEntry> entries = jar.entries();
                while (entries.hasMoreElements()) {
                    JarEntry entry = entries.nextElement();
                    String name = entry.getName().toLowerCase();
                    if(name.endsWith(".class")){
                        try(InputStream stream = jar.getInputStream(entry)){
                            try {
								ret.add(ClassFile.read(stream));
							} catch (ConstantPoolException e) {
								throw new RuntimeException(e);
							}
                        }
                    }
                }
            	return ret;
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read class file for module " + jarFile.getPath(), e);
        }
    }

    private static ClassFile readModuleInfo(String moduleName, final File jarFile) {
		// default module has no module descriptor
		if(Module.DEFAULT_MODULE_NAME.equals(moduleName))
			return null;
        try {
            try(JarFile jar = new JarFile(jarFile)){
            	String modulePath = getModulePath(moduleName);
            	String name1 = modulePath+"/$module_.class";
            	JarEntry entry = jar.getJarEntry(name1);
            	if(entry == null){
            		String name2 = modulePath+"/module_.class";
            		entry = jar.getJarEntry(name2);
            	}
            	if(entry != null){
            		try(InputStream stream = jar.getInputStream(entry)){
            			return ClassFile.read(stream);
            		} catch (ConstantPoolException e) {
            			throw new RuntimeException(e);
					}
            	}
            	return null;
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read class file for module " + jarFile.getPath(), e);
        }
    }

    private static String getModulePath(String moduleName) {
        String quotedModuleName = JVMModuleUtil.quoteJavaKeywords(moduleName);
		return quotedModuleName.replace('.', '/');
	}

    @Override
    public int[] getBinaryVersions(String moduleName, String moduleVersion, File moduleArchive) {
    	ClassFile moduleInfo = readModuleInfo(moduleName, moduleArchive);
    	if(moduleInfo == null)
    		return null;
    	Annotation ceylonAnnotation = ClassFileUtil.findAnnotation(moduleInfo, CEYLON_ANNOTATION);
        if (ceylonAnnotation == null)
            return null;
        
        int major = asInt(moduleInfo, ceylonAnnotation, "major");
        int minor = asInt(moduleInfo, ceylonAnnotation, "minor");
        return new int[]{major, minor};
    }

    @Override
    public ModuleVersionDetails readModuleInfo(String moduleName, String moduleVersion, File moduleArchive, boolean includeMembers, Overrides overrides) {
    	ClassFile moduleInfo = readModuleInfo(moduleName, moduleArchive);
    	if(moduleInfo == null)
    		return null;
    	Annotation moduleAnnotation = ClassFileUtil.findAnnotation(moduleInfo, MODULE_ANNOTATION);
        if (moduleAnnotation == null)
            return null;
        
        String doc = (String)ClassFileUtil.getAnnotationValue(moduleInfo, moduleAnnotation, "doc");
        String license = (String)ClassFileUtil.getAnnotationValue(moduleInfo, moduleAnnotation, "license");
        Object[] by = (Object[])ClassFileUtil.getAnnotationValue(moduleInfo, moduleAnnotation, "by");
        Object[] dependencies = (Object[])ClassFileUtil.getAnnotationValue(moduleInfo, moduleAnnotation, "dependencies");
        String type = ArtifactContext.getSuffixFromFilename(moduleArchive.getName());
        
    	Annotation ceylonAnnotation = ClassFileUtil.findAnnotation(moduleInfo, CEYLON_ANNOTATION);
        if (ceylonAnnotation == null)
            return null;

        int major = asInt(moduleInfo, ceylonAnnotation, "major");
        int minor = asInt(moduleInfo, ceylonAnnotation, "minor");

        ModuleVersionDetails mvd = new ModuleVersionDetails(moduleName, getVersionFromFilename(moduleName, moduleArchive.getName()));
        mvd.setDoc(doc);
        mvd.setLicense(license);
        if (by != null) {
        	for(Object author : by){
        		mvd.getAuthors().add((String)author);
        	}
        }
        mvd.getDependencies().addAll(getDependencies(moduleInfo, dependencies, moduleName, mvd.getVersion(), overrides));
        ModuleVersionArtifact mva = new ModuleVersionArtifact(type, major, minor);
        mvd.getArtifactTypes().add(mva);
        
        if (includeMembers) {
            mvd.setMembers(getMembers(moduleArchive));
        }
        
        return mvd;
    }

    private Set<String> getMembers(File moduleArchive) {
        HashSet<String> members = new HashSet<>(); 
        for (ClassFile cls : readClassFiles(moduleArchive)) {
            if (shouldAddMember(cls)) {
                try {
					members.add(classNameToDeclName(cls.getName().replace('/', '.')));
				} catch (ConstantPoolException e) {
					throw new RuntimeException(e);
				}
            }
        }
        return members;
    }

    private boolean shouldAddMember(ClassFile cls) {
        // ignore what we must ignore
        if (ClassFileUtil.findAnnotation(cls, IGNORE_ANNOTATION) != null) {
            return false;
        }
        // ignore module and package descriptors
        if (ClassFileUtil.findAnnotation(cls, MODULE_ANNOTATION) != null 
        		|| ClassFileUtil.findAnnotation(cls, PACKAGE_ANNOTATION) != null) {
            return false;
        }
        // ignore local types
        if (ClassFileUtil.findAnnotation(cls, LOCAL_CONTAINER_ANNOTATION) != null) {
            return false;
        }
        return true;
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

    private static Set<ModuleDependencyInfo> getDependencies(ClassFile classFile, Object[] dependencies, 
    		String module, String version, Overrides overrides) {
    	
        Set<ModuleDependencyInfo> result = new HashSet<ModuleDependencyInfo>(dependencies.length);
        for (Object depObject : dependencies) {
        	Annotation dep = (Annotation) depObject;
            String depName = (String)ClassFileUtil.getAnnotationValue(classFile, dep, "name");
            String depVersion = (String)ClassFileUtil.getAnnotationValue(classFile, dep, "version");
            boolean export = asBoolean(classFile, dep, "export");
            boolean optional = asBoolean(classFile, dep, "optional");
            
            result.add(new ModuleDependencyInfo(depName, depVersion, optional, export));
        }
        if(overrides != null)
            return overrides.applyOverrides(module, version, new ModuleInfo(module, version, null, result)).getDependencies();
        return result;
    }
    
    public boolean matchesModuleInfo(String moduleName, String moduleVersion, File moduleArchive, String query, Overrides overrides) {
    	ClassFile moduleInfo = readModuleInfo(moduleName, moduleArchive);
    	if(moduleInfo == null)
    		return false;
    	Annotation moduleAnnotation = ClassFileUtil.findAnnotation(moduleInfo, MODULE_ANNOTATION);
        if (moduleAnnotation == null)
            return false;
        
        String version = (String)ClassFileUtil.getAnnotationValue(moduleInfo, moduleAnnotation, "version");
        if (version == null)
            return false;
        String doc = (String)ClassFileUtil.getAnnotationValue(moduleInfo, moduleAnnotation, "doc");
        if (doc != null && matches(doc, query))
            return true;
        String license = (String)ClassFileUtil.getAnnotationValue(moduleInfo, moduleAnnotation, "license");
        if (license != null && matches(license, query))
            return true;
        Object[] by = (Object[])ClassFileUtil.getAnnotationValue(moduleInfo, moduleAnnotation, "by");
        if (by != null) {
            for (Object author : by) {
                if (matches((String)author, query))
                    return true;
            }
        }
        Object[] dependencies = (Object[])ClassFileUtil.getAnnotationValue(moduleInfo, moduleAnnotation, "dependencies");
        if (dependencies != null) {
            for (ModuleDependencyInfo dep : getDependencies(moduleInfo, dependencies, moduleName, version, overrides)) {
                if (matches(dep.getModuleName(), query))
                    return true;
            }
        }
        return false;
    }

    private static boolean matches(String string, String query) {
        return string.toLowerCase().contains(query);
    }

    private static boolean asBoolean(ClassFile classFile, Annotation annotation, String name) {
    	Boolean ret = (Boolean) ClassFileUtil.getAnnotationValue(classFile, annotation, name);
        return (ret != null) && ret.booleanValue();
    }

    private static int asInt(ClassFile classFile, Annotation annotation, String name) {
    	Integer ret = (Integer) ClassFileUtil.getAnnotationValue(classFile, annotation, name);
        return (ret != null) ? ret.intValue() : 0;
    }
}
