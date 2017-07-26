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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import com.redhat.ceylon.cmr.api.ModuleDependencyInfo;
import com.redhat.ceylon.cmr.api.ModuleInfo;
import com.redhat.ceylon.cmr.impl.IOUtils;
import com.redhat.ceylon.cmr.impl.XmlDependencyResolver;
import com.redhat.ceylon.langtools.classfile.Attribute;
import com.redhat.ceylon.langtools.classfile.Attributes;
import com.redhat.ceylon.langtools.classfile.ClassFile;
import com.redhat.ceylon.langtools.classfile.ClassWriter;
import com.redhat.ceylon.langtools.classfile.ConstantPool;
import com.redhat.ceylon.langtools.classfile.ConstantPool.CPInfo;
import com.redhat.ceylon.langtools.classfile.ModuleMainClass_attribute;
import com.redhat.ceylon.langtools.classfile.ModulePackages_attribute;
import com.redhat.ceylon.langtools.classfile.Module_attribute;
import com.redhat.ceylon.model.cmr.JDKUtils;
import com.redhat.ceylon.model.typechecker.model.Module;
import com.redhat.ceylon.model.typechecker.model.ModuleImport;
import com.redhat.ceylon.model.typechecker.model.Package;

public class Java9Util {

	private static String sanitiseName(String name) {
		return name.replace('-', '_');
	}

	static class Java9ModuleDescriptor {
		final String name, version, main;
		final Set<String> exportedPackages = new HashSet<String>(), concealedPackages = new HashSet<String>();
		final List<Java9ModuleImport> imports = new LinkedList<Java9ModuleImport>();
		
		Java9ModuleDescriptor(Module module){
			name = sanitiseName(module.getNameAsString());
			version = module.getVersion();
			for(Package pkg : module.getPackages()){
				if(isShared(pkg))
					exportedPackages.add(pkg.getNameAsString());
				else
					concealedPackages.add(pkg.getNameAsString());
			}
            Set<String> importedModules = new HashSet<>();
			for(ModuleImport imp : module.getImports()){
			    // skip optional deps since create-mlib will skip them too
			    if(imp.isOptional())
			        continue;
				String dependency = JDKUtils.getJava9ModuleName(imp.getModule().getNameAsString(), imp.getModule().getVersion());
                // never import a module twice, and due to jdk8->9 aliases this can be the case
                if(!importedModules.add(dependency))
                    continue;
				if(skipModuleImport(dependency))
					continue;
				imports.add(new Java9ModuleImport(dependency, imp.isExport()));
			}
			addImplicitImports();
			main = getMain(module.getNameAsString());
		}
		
		public Java9ModuleDescriptor(String name, String version, ModuleInfo info, Set<String> packages) {
			this.name = sanitiseName(name);
			this.version = version;
			exportedPackages.addAll(packages);
			Set<String> importedModules = new HashSet<>();
			for(ModuleDependencyInfo imp : info.getDependencies()){
                // skip optional deps since create-mlib will skip them too
                if(imp.isOptional())
                    continue;
				String dependency = JDKUtils.getJava9ModuleName(imp.getName(), imp.getVersion());
				// never import a module twice, and due to jdk8->9 aliases this can be the case
				if(!importedModules.add(dependency))
				    continue;
				if(skipModuleImport(dependency))
					continue;
				imports.add(new Java9ModuleImport(dependency, imp.isExport()));
			}
			addImplicitImports();
			main = getMain(name);
		}

		private boolean skipModuleImport(String dependency) {
			// model depends on language but only at runtime, Java 9 adds this dependency at runtime and
			// will barf if it sees it at compile-time, so special-case it
			if(name.equals("com.redhat.ceylon.model") && dependency.equals(Module.LANGUAGE_MODULE_NAME))
				return true;
			// same for slf4j
            if(name.equals("org.slf4j.api") && dependency.equals("org.slf4j.simple"))
                return true;
			return false;
		}

		private String getMain(String module) {
			if(module.equals("com.redhat.ceylon.java.main"))
				return "com.redhat.ceylon.compiler.java.runtime.Main2";
			else 
				return null;
		}

		private boolean isShared(Package pkg) {
			// Special case for the language module where we don't want Ceylon users to access this package, but
			// we need our produced Java code to be allowed to
			if(pkg.getModule().isLanguageModule()){
				String name = pkg.getNameAsString();
				if(name.equals("com.redhat.ceylon.compiler.java.runtime.metamodel")
						|| name.equals("com.redhat.ceylon.compiler.java.runtime")
						|| name.equals("com.redhat.ceylon.compiler.java.metadata")
						|| name.equals("com.redhat.ceylon.compiler.java"))
					return true;
			}
			return pkg.isShared();
		}

		private void addImplicitImports() {
			for(Java9ModuleImport imp : imports){
				if(imp.name.equals("java.base"))
					return;
			}
			imports.add(new Java9ModuleImport("java.base", true));
		}

		int getPackagesSize(){
			return exportedPackages.size() + concealedPackages.size();
		}
	}
	
	static class Java9ModuleImport {
		final String name;
		final boolean exported;
		Java9ModuleImport(String name, boolean exported){
			this.name = sanitiseName(name);
			this.exported = exported;
		}
	}

	public static void writeModuleDescriptor(File outputFolder, Java9ModuleDescriptor module) {
    	ClassWriter classWriter = new ClassWriter();
    	ClassFile classFile = generateModuleDescriptor(module);
        try (OutputStream os = new FileOutputStream(new File(outputFolder, "module-info.class"))){
            classWriter.write(classFile, os);
        } catch (IOException e) {
        	e.printStackTrace();
            throw new RuntimeException(e);
        }
	}

    private static void writeModuleDescriptor(ZipOutputStream zos, Java9ModuleDescriptor module) {
        ClassWriter classWriter = new ClassWriter();
        ClassFile classFile = generateModuleDescriptor(module);
        try {
            zos.putNextEntry(new ZipEntry("module-info.class"));
            classWriter.write(classFile, zos);
            zos.flush();
            zos.closeEntry();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private static ClassFile generateModuleDescriptor(Java9ModuleDescriptor module) {
        CPInfo[] pool = new ConstantPool.CPInfo[1+7
                                                + (module.imports.size() * 2)
                                                + (module.getPackagesSize() * 2)
                                                + (module.main != null ? 3 : 0)];
        ConstantPool constantPool = new ConstantPool(pool);
        
        int cp = 1;
        // 1: this_class name
        pool[cp++] = new ConstantPool.CONSTANT_Utf8_info("module-info");
        // 2: this_class
        pool[cp++] = new ConstantPool.CONSTANT_Class_info(constantPool, 1);
        // 3: module attr
        pool[cp++] = new ConstantPool.CONSTANT_Utf8_info(Attribute.Module);
        // 4: module pkgs attr
        pool[cp++] = new ConstantPool.CONSTANT_Utf8_info(Attribute.ModulePackages);
        // 5: version
        pool[cp++] = new ConstantPool.CONSTANT_Utf8_info(module.version);
        // 6: module name
        pool[cp++] = new ConstantPool.CONSTANT_Utf8_info(module.name);
        // 7: module info
        pool[cp++] = new ConstantPool.CONSTANT_Module_info(constantPool, 6);
        if(module.main != null){
            // 8: main attr
            pool[cp++] = new ConstantPool.CONSTANT_Utf8_info(Attribute.ModuleMainClass);
            // 9: main class name
            pool[cp++] = new ConstantPool.CONSTANT_Utf8_info(module.main.replace('.', '/'));
            // 10: main class
            pool[cp++] = new ConstantPool.CONSTANT_Class_info(constantPool, 9);
        }
        int i=0;
        // now imports
        Module_attribute.RequiresEntry[] requires = new Module_attribute.RequiresEntry[module.imports.size()];
        for(Java9ModuleImport imp : module.imports){
            pool[cp] = new ConstantPool.CONSTANT_Utf8_info(imp.name);
            pool[cp+1] = new ConstantPool.CONSTANT_Module_info(constantPool, cp);
            int flag = 0;
            if(imp.exported)
                flag |= Module_attribute.ACC_TRANSITIVE;
            // FIXME: add version info!
            requires[i] = new Module_attribute.RequiresEntry(cp+1, flag, 0);
            i++;
            cp+=2;
        }
        Module_attribute.ExportsEntry[] exports = new Module_attribute.ExportsEntry[module.exportedPackages.size()];
        i = 0;
        int[] modulePackages = new int[module.exportedPackages.size() + module.concealedPackages.size()];
        int m=0;
        for(String pkg : module.exportedPackages){
            pool[cp] = new ConstantPool.CONSTANT_Utf8_info(pkg.replace('.', '/'));
            pool[cp+1] = new ConstantPool.CONSTANT_Package_info(constantPool, cp);
            exports[i++] = new Module_attribute.ExportsEntry(cp+1, 0, new int[0]);
            modulePackages[m++] = cp+1;
            cp+=2;
        }
        for(String pkg : module.concealedPackages){
            pool[cp] = new ConstantPool.CONSTANT_Utf8_info(pkg.replace('.', '/'));
            pool[cp+1] = new ConstantPool.CONSTANT_Package_info(constantPool, cp);
            modulePackages[m++] = cp+1;
            cp+=2;
        }
        Attribute[] attributesArray = new Attribute[2 + (module.main != null ? 1 : 0)];
        attributesArray[0] = new Module_attribute(3, 
                        7, // module name index
                        0, // flags
                        5, // version
                        requires,
                        exports, 
                        new Module_attribute.OpensEntry[0],
                        new int[0], 
                        new Module_attribute.ProvidesEntry[0]);
        attributesArray[1] = new ModulePackages_attribute(4, modulePackages);
        if(module.main != null)
            attributesArray[2] = new ModuleMainClass_attribute(8, 10);
                
        Attributes attributes = new Attributes(constantPool, attributesArray );
        return new ClassFile(com.redhat.ceylon.langtools.tools.javac.jvm.ClassFile.JAVA_MAGIC,
                com.redhat.ceylon.langtools.tools.javac.jvm.ClassFile.Version.V53.minor,
                com.redhat.ceylon.langtools.tools.javac.jvm.ClassFile.Version.V53.major,
                constantPool,
                new com.redhat.ceylon.langtools.classfile.AccessFlags(com.redhat.ceylon.langtools.classfile.AccessFlags.ACC_MODULE),
                2,
                0,
                new int[0],
                new com.redhat.ceylon.langtools.classfile.Field[0],
                new com.redhat.ceylon.langtools.classfile.Method[0],
                attributes);
    }

    private static final String METAINF_JBOSSMODULES = "META-INF/jbossmodules/";
    private static final String MODULE_XML = "module.xml";

	public static void main(String[] args) throws IOException {
		System.err.println("Add Java9 Module info for "+args[0]);
		// FIXME: check arg
		String jarName = args[0];
		File jarFile = new File(jarName);
		ZipFile zipFile = new ZipFile(jarFile);
		File outFile = File.createTempFile(jarName, ".jar");
		ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(outFile));
		Enumeration<? extends ZipEntry> entries = zipFile.entries();
		Set<String> packages = new HashSet<String>();
		ModuleInfo info = null;
		String moduleName = null, version = null;
		while(entries.hasMoreElements()){
			ZipEntry entry = entries.nextElement();
			String name = entry.getName();
			if(name.equals("module-info.class")){
				System.err.println(" Already has a module info!");
				zipFile.close();
				zos.close();
				outFile.delete();
				return;
			}else if(name.endsWith(".class")){
				int lastSlash = name.lastIndexOf('/');
				// ignore the default package (not sure if we can import or conceal it in Java 9
				if(lastSlash != -1){
					String pkg = name.substring(0, lastSlash).replace('/', '.');
					packages.add(pkg);
				}
			}else if(name.startsWith(METAINF_JBOSSMODULES)
					&& name.endsWith("module.xml")){
				String path = name.substring(METAINF_JBOSSMODULES.length());
				path = path.substring(0, path.length() - MODULE_XML.length() - 1);
	            int p = path.lastIndexOf('/');
	            if (p > 0) {
	                moduleName = path.substring(0, p).replace('/', '.');
	                version = path.substring(p + 1);
	            }
				try(InputStream is = zipFile.getInputStream(entry)){
					// name/version only used when we have an override
					info = XmlDependencyResolver.INSTANCE.resolveFromInputStream(is, null, null, null);
					System.err.println(" Found module descriptor at "+name);
				}
			}
			zos.putNextEntry(entry);
			if(!entry.isDirectory())
				IOUtils.copyStream(zipFile.getInputStream(entry), zos, true, false);
			zos.closeEntry();
		}
		zipFile.close();
		
		if(info != null && moduleName != null && version != null){
			writeModuleDescriptor(zos, new Java9ModuleDescriptor(moduleName, version, info, packages));
		}
		zos.flush();
		zos.close();
		// rename
		jarFile.delete();
        Files.copy(outFile.toPath(), jarFile.toPath(), StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);
        outFile.delete();
	}
}
