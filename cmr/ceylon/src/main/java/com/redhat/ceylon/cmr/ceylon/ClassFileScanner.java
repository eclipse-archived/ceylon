package com.redhat.ceylon.cmr.ceylon;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.redhat.ceylon.cmr.api.ModuleInfo;
import com.redhat.ceylon.cmr.api.PathFilterParser;
import com.redhat.ceylon.cmr.impl.JarUtils;
import com.redhat.ceylon.langtools.classfile.AccessFlags;
import com.redhat.ceylon.langtools.classfile.Annotation;
import com.redhat.ceylon.langtools.classfile.Attribute;
import com.redhat.ceylon.langtools.classfile.Attributes;
import com.redhat.ceylon.langtools.classfile.BootstrapMethods_attribute;
import com.redhat.ceylon.langtools.classfile.ClassFile;
import com.redhat.ceylon.langtools.classfile.Code_attribute;
import com.redhat.ceylon.langtools.classfile.ConstantPool;
import com.redhat.ceylon.langtools.classfile.ConstantPoolException;
import com.redhat.ceylon.langtools.classfile.Instruction;
import com.redhat.ceylon.langtools.classfile.RuntimeVisibleAnnotations_attribute;
import com.redhat.ceylon.langtools.classfile.Signature;
import com.redhat.ceylon.langtools.classfile.Signature_attribute;
import com.redhat.ceylon.langtools.classfile.Annotation.Annotation_element_value;
import com.redhat.ceylon.langtools.classfile.Annotation.Array_element_value;
import com.redhat.ceylon.langtools.classfile.Annotation.Class_element_value;
import com.redhat.ceylon.langtools.classfile.Annotation.Enum_element_value;
import com.redhat.ceylon.langtools.classfile.Annotation.Primitive_element_value;
import com.redhat.ceylon.langtools.classfile.Annotation.element_value;
import com.redhat.ceylon.langtools.classfile.Annotation.element_value_pair;
import com.redhat.ceylon.langtools.classfile.BootstrapMethods_attribute.BootstrapMethodSpecifier;
import com.redhat.ceylon.langtools.classfile.ConstantPool.CONSTANT_Class_info;
import com.redhat.ceylon.langtools.classfile.ConstantPool.CONSTANT_Double_info;
import com.redhat.ceylon.langtools.classfile.ConstantPool.CONSTANT_Fieldref_info;
import com.redhat.ceylon.langtools.classfile.ConstantPool.CONSTANT_Float_info;
import com.redhat.ceylon.langtools.classfile.ConstantPool.CONSTANT_Integer_info;
import com.redhat.ceylon.langtools.classfile.ConstantPool.CONSTANT_InterfaceMethodref_info;
import com.redhat.ceylon.langtools.classfile.ConstantPool.CONSTANT_InvokeDynamic_info;
import com.redhat.ceylon.langtools.classfile.ConstantPool.CONSTANT_Long_info;
import com.redhat.ceylon.langtools.classfile.ConstantPool.CONSTANT_MethodHandle_info;
import com.redhat.ceylon.langtools.classfile.ConstantPool.CONSTANT_MethodType_info;
import com.redhat.ceylon.langtools.classfile.ConstantPool.CONSTANT_Methodref_info;
import com.redhat.ceylon.langtools.classfile.ConstantPool.CONSTANT_NameAndType_info;
import com.redhat.ceylon.langtools.classfile.ConstantPool.CONSTANT_String_info;
import com.redhat.ceylon.langtools.classfile.ConstantPool.CONSTANT_Utf8_info;
import com.redhat.ceylon.langtools.classfile.ConstantPool.CPInfo;
import com.redhat.ceylon.langtools.classfile.ConstantPool.InvalidIndex;
import com.redhat.ceylon.langtools.classfile.ConstantPool.UnexpectedEntry;
import com.redhat.ceylon.langtools.classfile.Descriptor.InvalidDescriptor;
import com.redhat.ceylon.langtools.classfile.Instruction.TypeKind;
import com.redhat.ceylon.langtools.classfile.Type.ArrayType;
import com.redhat.ceylon.langtools.classfile.Type.ClassSigType;
import com.redhat.ceylon.langtools.classfile.Type.ClassType;
import com.redhat.ceylon.langtools.classfile.Type.MethodType;
import com.redhat.ceylon.langtools.classfile.Type.SimpleType;
import com.redhat.ceylon.langtools.classfile.Type.TypeParamType;
import com.redhat.ceylon.langtools.classfile.Type.Visitor;
import com.redhat.ceylon.model.cmr.PathFilter;
import com.redhat.ceylon.model.loader.JdkProvider;

public class ClassFileScanner {

	private File jarFile;
	private boolean isPublicApi;
    private boolean ignoreAnnotations;
    private Set<String> jarClassNames;
    private final JdkProvider jdkProvider;
    Set<String> externalClasses;
    Set<String> publicApiExternalClasses;

	private Visitor<Object, Object> typeVisitor = new Visitor<Object,Object>(){

		@Override
		public Object visitSimpleType(SimpleType type, Object p) {
			// ignore those: primitives and type variables
			return null;
		}

		@Override
		public Object visitArrayType(ArrayType type, Object p) {
			// visit element type
			if(type.elemType != null){
				type.elemType.accept(this, p);
			}
			return null;
		}

		@Override
		public Object visitMethodType(MethodType type, Object p) {
			if(type.returnType != null)
				type.returnType.accept(this, p);
			if(type.paramTypes != null){
				for (com.redhat.ceylon.langtools.classfile.Type paramType : type.paramTypes) {
					paramType.accept(this, p);
				}
			}
			if(type.typeParamTypes != null){
				for (com.redhat.ceylon.langtools.classfile.Type typeParamType : type.typeParamTypes) {
					typeParamType.accept(this, p);
				}
			}
			// Big question: do we need to visit throws too? They are part of public signature but
			// Ceylon does not force to handle them. Question is: will it break type isolation when
			// the method's type is loaded?
			return null;
		}

		@Override
		public Object visitClassSigType(ClassSigType type, Object p) {
			// make sure we visit type parameters and supertypes
			if(type.superclassType != null){
				type.superclassType.accept(this, p);
			}
			if(type.superinterfaceTypes != null){
				for (com.redhat.ceylon.langtools.classfile.Type interf : type.superinterfaceTypes) {
					interf.accept(this, p);
				}
			}
			if(type.typeParamTypes != null){
				for (com.redhat.ceylon.langtools.classfile.Type.TypeParamType typeParam : type.typeParamTypes) {
					typeParam.accept(this, p);
				}
			}
			return null;
		}

		@Override
		public Object visitClassType(ClassType type, Object p) {
			// outer type
			if(type.outerType != null)
				type.outerType.accept(this, p);
			// type params
			if(type.typeArgs != null){
				for (com.redhat.ceylon.langtools.classfile.Type typeArg : type.typeArgs) {
					typeArg.accept(this, p);
				}
			}
			recordBinaryName(type.getBinaryName());
			return null;
		}

		@Override
		public Object visitTypeParamType(TypeParamType type, Object p) {
			// visit bounds
			if(type.classBound != null)
				type.classBound.accept(this, p);
			if(type.interfaceBounds != null){
				for (com.redhat.ceylon.langtools.classfile.Type bound : type.interfaceBounds) {
					bound.accept(this, p);
				}
			}
			return null;
		}

		@Override
		public Object visitWildcardType(com.redhat.ceylon.langtools.classfile.Type.WildcardType type, Object p) {
			// visit bound
			if(type.boundType != null)
				type.boundType.accept(this, p);
			return null;
		}
		
	};
	private com.redhat.ceylon.langtools.classfile.Annotation.element_value.Visitor<Object, ConstantPool> annotationVisitor = 
			new com.redhat.ceylon.langtools.classfile.Annotation.element_value.Visitor<Object, ConstantPool>(){

				@Override
				public Object visitPrimitive(Primitive_element_value ev, ConstantPool p) {
					// ignore
					return null;
				}

				@Override
				public Object visitEnum(Enum_element_value ev, ConstantPool p) {
					try {
						String signature = p.getUTF8Value(ev.type_name_index);
						// FIXME: this is not strictly true that it's in the public API, or even private,
						// as the error will occur only on introspection, not class loading, but if someone
						// looks at that class and tries to instantiate it, he will require module access
						recordFieldSignature(signature);
					} catch (ConstantPoolException e) {
						throw new RuntimeException(e);
					}
					return null;
				}

				@Override
				public Object visitClass(Class_element_value ev, ConstantPool p) {
					try {
						String signature = p.getUTF8Value(ev.class_info_index);
						// FIXME: this is not strictly true that it's in the public API, or even private,
						// as the error will occur only on introspection, not class loading, but if someone
						// looks at that class and tries to instantiate it, he will require module access
						recordFieldSignature(signature);
					} catch (ConstantPoolException e) {
						throw new RuntimeException(e);
					}
					return null;
				}

				@Override
				public Object visitAnnotation(Annotation_element_value ev, ConstantPool p) {
					if(ev.annotation_value != null)
						ClassFileScanner.this.visitAnnotation(ev.annotation_value, p);
					return null;
				}

				@Override
				public Object visitArray(Array_element_value ev, ConstantPool p) {
					// visit element values
					if(ev.values != null){
						for(element_value val : ev.values){
							val.accept(this, p);
						}
					}
					return null;
				}
	};
	protected com.redhat.ceylon.langtools.classfile.ConstantPool.Visitor<Object, ConstantPool> constantPoolVisitor
		= new com.redhat.ceylon.langtools.classfile.ConstantPool.Visitor<Object, ConstantPool>(){

			@Override
			public Object visitClass(CONSTANT_Class_info info, ConstantPool p) {
				try {
					recordBinaryName(info.getName());
				} catch (ConstantPoolException e) {
					throw new RuntimeException(e);
				}
				return null;
			}

			@Override
			public Object visitDouble(CONSTANT_Double_info info, ConstantPool p) {
				return null;
			}

			@Override
			public Object visitFieldref(CONSTANT_Fieldref_info info, ConstantPool p) {
				try {
					p.get(info.class_index).accept(this, p);
					p.get(info.name_and_type_index).accept(this, p);
				} catch (InvalidIndex e) {
					throw new RuntimeException(e);
				}
				return null;
			}

			@Override
			public Object visitFloat(CONSTANT_Float_info info, ConstantPool p) {
				return null;
			}

			@Override
			public Object visitInteger(CONSTANT_Integer_info info, ConstantPool p) {
				return null;
			}

			@Override
			public Object visitInterfaceMethodref(CONSTANT_InterfaceMethodref_info info, ConstantPool p) {
				try {
					p.get(info.class_index).accept(this, p);
					p.get(info.name_and_type_index).accept(this, p);
				} catch (InvalidIndex e) {
					throw new RuntimeException(e);
				}
				return null;
			}

			@Override
			public Object visitInvokeDynamic(CONSTANT_InvokeDynamic_info info, ConstantPool p) {
				try {
					// we don't visit the bootstrap method because we visit it unconditionally already
					p.get(info.name_and_type_index).accept(this, p);
				} catch (InvalidIndex e) {
					throw new RuntimeException(e);
				}
				return null;
			}

			@Override
			public Object visitLong(CONSTANT_Long_info info, ConstantPool p) {
				return null;
			}

			@Override
			public Object visitNameAndType(CONSTANT_NameAndType_info info, ConstantPool p) {
				try {
					String signature = info.getType();
					if(signature.startsWith("("))
						recordMethodSignature(signature);
					else
						recordFieldSignature(signature);
				} catch (ConstantPoolException e) {
					throw new RuntimeException(e);
				}
				return null;
			}

			@Override
			public Object visitMethodref(CONSTANT_Methodref_info info, ConstantPool p) {
				try {
					p.get(info.class_index).accept(this, p);
					p.get(info.name_and_type_index).accept(this, p);
				} catch (InvalidIndex e) {
					throw new RuntimeException(e);
				}
				return null;
			}

			@Override
			public Object visitMethodHandle(CONSTANT_MethodHandle_info info, ConstantPool p) {
				try {
					info.getCPRefInfo().accept(this, p);
				} catch (ConstantPoolException e) {
					throw new RuntimeException(e);
				}
				return null;
			}

			@Override
			public Object visitMethodType(CONSTANT_MethodType_info info, ConstantPool p) {
				try {
					String signature = info.getType();
					recordMethodSignature(signature);
				} catch (ConstantPoolException e) {
					throw new RuntimeException(e);
				}
				return null;
			}

			@Override
			public Object visitString(CONSTANT_String_info info, ConstantPool p) {
				return null;
			}

			@Override
			public Object visitUtf8(CONSTANT_Utf8_info info, ConstantPool p) {
				return null;
			}
		
	};

	private com.redhat.ceylon.langtools.classfile.Instruction.KindVisitor<Object, ConstantPool> codeVisitor 
		= new com.redhat.ceylon.langtools.classfile.Instruction.KindVisitor<Object, ConstantPool>(){

			@Override
			public Object visitNoOperands(Instruction instr, ConstantPool p) {
				return null;
			}

			@Override
			public Object visitArrayType(Instruction instr, TypeKind kind, ConstantPool p) {
				return null;
			}

			@Override
			public Object visitBranch(Instruction instr, int offset, ConstantPool p) {
				return null;
			}

			@Override
			public Object visitConstantPoolRef(Instruction instr, int index, ConstantPool p) {
				visitConstantPoolRef(index, p);
				return null;
			}

			private void visitConstantPoolRef(int index, ConstantPool p) {
				try {
					CPInfo entry = p.get(index);
					entry.accept(constantPoolVisitor, p);
				} catch (ConstantPoolException e) {
					throw new RuntimeException(e);
				}
			}

			@Override
			public Object visitConstantPoolRefAndValue(Instruction instr, int index, int value, ConstantPool p) {
				visitConstantPoolRef(index, p);
				return null;
			}

			@Override
			public Object visitLocal(Instruction instr, int index, ConstantPool p) {
				return null;
			}

			@Override
			public Object visitLocalAndValue(Instruction instr, int index, int value, ConstantPool p) {
				return null;
			}

			@Override
			public Object visitLookupSwitch(Instruction instr, int default_, int npairs, int[] matches, int[] offsets,
					ConstantPool p) {
				return null;
			}

			@Override
			public Object visitTableSwitch(Instruction instr, int default_, int low, int high, int[] offsets,
					ConstantPool p) {
				return null;
			}

			@Override
			public Object visitValue(Instruction instr, int value, ConstantPool p) {
				return null;
			}

			@Override
			public Object visitUnknown(Instruction instr, ConstantPool p) {
				return null;
			}
		
	};
	
	public ClassFileScanner(File jarFile, boolean ignoreAnnotations, JdkProvider jdkProvider) throws IOException{
		this.jdkProvider = jdkProvider;
        externalClasses = new TreeSet<>();
        publicApiExternalClasses = new TreeSet<>();
        jarClassNames = JarUtils.gatherClassnamesFromJar(jarFile);
        this.jarFile = jarFile;
        this.ignoreAnnotations = ignoreAnnotations;
	}

    protected void recordFieldSignature(String signature) {
		String name = binaryNameToClassName(true, signature);
		recordTypeNameUsage(name);
	}

	protected void recordBinaryName(String binaryName) {
		String name = binaryNameToClassName(false, binaryName);
		recordTypeNameUsage(name);
	}

    private void recordTypeNameUsage(String name) {
		if(name != null && !jarClassNames.contains(name)){
			if(name.startsWith("javax.inject"))
				"".toString();
			externalClasses.add(name);
			if(isPublicApi)
				publicApiExternalClasses.add(name);
		}
	}

	// Check the public API of a class for types that are external to the JAR we're importing
    private void checkPublicApi(ClassFile classFile) throws ConstantPoolException, InvalidDescriptor {
    	boolean publicType = classFile.access_flags.is(AccessFlags.ACC_PUBLIC);
    	isPublicApi = publicType;
    	// traverse type parameter bounds, superclass, interfaces
    	Signature_attribute signatureAttribute = (Signature_attribute) classFile.getAttribute(Attribute.Signature);
    	if(signatureAttribute != null){
    		Signature signature = signatureAttribute.getParsedSignature();
    		com.redhat.ceylon.langtools.classfile.Type type = signature.getType(classFile.constant_pool);
    		type.accept(typeVisitor, null);
    	}else{
    		// pre-1.5 I suppose, check stuff manually
    		if(classFile.super_class != 0){
    			String binaryName = classFile.getSuperclassName();
    			recordBinaryName(binaryName);
    		}
    		for (int i = 0; i < classFile.interfaces.length; i++) {
    			String binaryName = classFile.getInterfaceName(i);
    			recordBinaryName(binaryName);
			}
    	}
    	visitAnnotations(classFile.attributes, classFile.constant_pool);
    	for (com.redhat.ceylon.langtools.classfile.Field field : classFile.fields) {
    		// a field is public is its type is public and it is public/protected
    		isPublicApi = publicType && 
    				(field.access_flags.is(AccessFlags.ACC_PUBLIC)
        			 || field.access_flags.is(AccessFlags.ACC_PROTECTED));
			signatureAttribute = (Signature_attribute) field.attributes.get(Attribute.Signature);
			if(signatureAttribute != null){
				Signature signature = signatureAttribute.getParsedSignature();
				com.redhat.ceylon.langtools.classfile.Type type = signature.getType(classFile.constant_pool);
				type.accept(typeVisitor, null);
			}else{
				String signature = field.descriptor.getValue(classFile.constant_pool);
    			recordFieldSignature(signature);
			}
	    	visitAnnotations(field.attributes, classFile.constant_pool);
		}
    	for (com.redhat.ceylon.langtools.classfile.Method method : classFile.methods) {
    		// a method is public is its type is public and it is public/protected
    		isPublicApi = publicType && 
    				(method.access_flags.is(AccessFlags.ACC_PUBLIC)
        			 || method.access_flags.is(AccessFlags.ACC_PROTECTED));
			signatureAttribute = (Signature_attribute) method.attributes.get(Attribute.Signature);
			if(signatureAttribute != null){
				Signature signature = signatureAttribute.getParsedSignature();
				com.redhat.ceylon.langtools.classfile.Type type = signature.getType(classFile.constant_pool);
				type.accept(typeVisitor, null);
			}else{
				String signature = method.descriptor.getValue(classFile.constant_pool);
				recordMethodSignature(signature);
			}
	    	visitAnnotations(method.attributes, classFile.constant_pool);
	    	// code is never public
	    	isPublicApi = false;
	    	visitCode(method.attributes, classFile.constant_pool);
		}
    	// boostrap methods are code/private
    	isPublicApi = false;
    	visitBootstrapMethods(classFile.attributes, classFile.constant_pool);
	}

    private void visitBootstrapMethods(Attributes attributes, ConstantPool constant_pool) throws InvalidIndex {
    	BootstrapMethods_attribute bootstrapMethods = (BootstrapMethods_attribute) attributes.get(Attribute.BootstrapMethods);
    	if(bootstrapMethods != null && bootstrapMethods.bootstrap_method_specifiers != null){
    		for(BootstrapMethodSpecifier specifier : bootstrapMethods.bootstrap_method_specifiers){
    			constant_pool.get(specifier.bootstrap_method_ref).accept(constantPoolVisitor, constant_pool);
    			if(specifier.bootstrap_arguments != null){
    				for(int arg : specifier.bootstrap_arguments){
    					constant_pool.get(arg).accept(constantPoolVisitor, constant_pool);
    				}
    			}
    		}
    	}
	}

	private void visitCode(Attributes attributes, ConstantPool constant_pool) {
    	Code_attribute code = (Code_attribute) attributes.get(Attribute.Code);
    	if(code != null){
    		for(Instruction instr : code.getInstructions()){
    			instr.accept(codeVisitor, constant_pool);
    		}
    	}
	}

	private void visitAnnotations(Attributes attributes, ConstantPool constant_pool) throws ConstantPoolException {
		if(ignoreAnnotations)
			return;
    	RuntimeVisibleAnnotations_attribute annotations = (RuntimeVisibleAnnotations_attribute) attributes.get(Attribute.RuntimeVisibleAnnotations);
    	if(annotations != null){
    		for (Annotation annotation : annotations.annotations) {
    			visitAnnotation(annotation, constant_pool);
			}
    	}
	}

	private void visitAnnotation(Annotation annotation, ConstantPool constant_pool) {
		// in theory this is a field signature
		String annotationTypeSignature;
		try {
			annotationTypeSignature = constant_pool.getUTF8Value(annotation.type_index);
		} catch (InvalidIndex | UnexpectedEntry e) {
			throw new RuntimeException(e);
		}
		recordFieldSignature(annotationTypeSignature);
		for (element_value_pair elementValuePair : annotation.element_value_pairs) {
			elementValuePair.value.accept(annotationVisitor, constant_pool);
		}
	}

	private void recordMethodSignature(String signature) {
		// Format is (P1P2...)R
        int i = 0;
        while (i < signature.length()) {
            switch (signature.charAt(i++)) {
                case '(':
                case ')':
                case '[':
                case 'B':
                case 'C':
                case 'D':
                case 'F':
                case 'I':
                case 'J':
                case 'S':
                case 'Z':
                case 'V':
                	// skip
                	continue;

                case 'L':
                    int sep = signature.indexOf(';', i);
                    if (sep == -1)
                        throw new RuntimeException("Invalid signature: "+signature);
                    recordBinaryName(signature.substring(i, sep));
                    i = sep + 1;
                    break;

                default:
                    throw new RuntimeException("Invalid signature: "+signature);
            }
        }
	}

	/**
	 * Binary name is: [[I, [[Lcom/foo/Bar; or com/foo/Bar.
	 * Signature is: I, [[I, [[Lcom/foo/Bar; or Lcom/foo/Bar;.
	 */
	private String binaryNameToClassName(boolean isSignature, String name) {
    	// IMPORTANT: this turns [[Lcom/foo/Bar$Gee; into com.foo.Bar$Gee and keeps the dollar sign
    	// otherwise we lose the package separator which we need later
        while (name.startsWith("[")) {
            name = name.substring(1, name.length());
            // binary names with array require a following signature
            isSignature = true;
        }
        // in theory there's only one there
        if (name.startsWith("L") && name.endsWith(";")) {
            name = name.substring(1, name.length() - 1);
        }else if(isSignature){
        	if(name.equals("B")
        		|| name.equals("C")
        		|| name.equals("D")
        		|| name.equals("F")
        		|| name.equals("I")
        		|| name.equals("J")
        		|| name.equals("S")
        		|| name.equals("Z")
        		|| name.equals("V")
        			){
        		// B C D F I J S Z V
        		// not a reference type
        		return null;
        	}else{
        		throw new RuntimeException("Don't know how to handle binary type: "+name);
        	}
        }else{
        	// name is already a binary name, we're good
        }
        name = name.replace('/', '.');
        return name;
	}

    enum Usage {
    	Unused, Used, UsedInPublicApi;

		public static Usage fromBooleans(boolean used, boolean usedInPublicApi) {
			return usedInPublicApi ? Usage.UsedInPublicApi : (used ? Usage.Used : Usage.Unused);
		}
    }
    
	// Remove all classes that are found within the given set of
    // imported classes from the given set of external classes
    Usage removeMatchingClasses(Set<String> importedClasses) {
        boolean used = externalClasses.removeAll(importedClasses);
        boolean usedInPublicApi = publicApiExternalClasses.removeAll(importedClasses);
        return Usage.fromBooleans(used, usedInPublicApi);
    }

    // Remove all classes that are part of the given JDK module
    // from the given set of external classes
    Usage removeMatchingJdkClasses(String jdkModule) {
    	Iterator<String> iterator = externalClasses.iterator();
    	boolean used = false;
    	boolean usedInPublicApi = false;
        while (iterator.hasNext()) {
        	String className = iterator.next();
            String pkgName = getPackageFromClass(className);
            if (jdkProvider.isJDKPackage(jdkModule, pkgName)) {
            	iterator.remove();
            	used = true;
            	usedInPublicApi |= publicApiExternalClasses.remove(className);
            }
        }
        return Usage.fromBooleans(used, usedInPublicApi);
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
    Set<String> gatherJdkModules(Set<String> packages) {
        Set<String> jdkModules = new TreeSet<>();
        Set<String> newPackages = new HashSet<>();
        for (String pkg : packages) {
            String mod = jdkProvider.getJDKModuleNameForPackage(pkg);
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

	public void scan(ModuleInfo moduleInfo) throws IOException {
        PathFilter pathFilter = null;
    	if(moduleInfo.getFilter() != null){
            pathFilter = PathFilterParser.parse(moduleInfo.getFilter());
    	}

        try(ZipFile zf = new ZipFile(jarFile)){
        	Enumeration<? extends ZipEntry> entries = zf.entries();
        	while(entries.hasMoreElements()){
        		ZipEntry entry = entries.nextElement();
        		if(entry.isDirectory() || !entry.getName().toLowerCase().endsWith(".class"))
        			continue;
        		if(pathFilter != null && !pathFilter.accept(entry.getName()))
        			continue;
        		try(InputStream is  = zf.getInputStream(entry)){
        			try {
						ClassFile classFile = ClassFile.read(is);
						isPublicApi = false;
						checkPublicApi(classFile);
					} catch (ConstantPoolException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvalidDescriptor e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
        		}
        	}
        }
	}

    public Usage removeMatchingPackages(List<Pattern> patterns) {
    	boolean used = false;
    	boolean usedInPublicApi = false;
    	for(Pattern pattern : patterns){
    		Iterator<String> it = externalClasses.iterator();
    		while(it.hasNext()){
    			String klass = it.next();
    			String pkg = getPackageFromClass(klass);
    			if(pattern.matcher(pkg).matches()){
    				it.remove();
    				used = true;
                	usedInPublicApi |= publicApiExternalClasses.remove(klass);
    			}
    		}
    	}
    	return Usage.fromBooleans(used, usedInPublicApi);
	}

	public boolean hasExternalClasses() {
		return !externalClasses.isEmpty();
	}

	public Set<String> getExternalPackages() {
		return getPackagesFromClasses(externalClasses);
	}

	public Set<String> getPublicApiExternalPackages() {
		return getPackagesFromClasses(publicApiExternalClasses);
	}

	public Set<String> getDefaultPackageClasses() {
		return getDefaultPackageClasses(externalClasses);
	}

	public Set<String> getPublicApiDefaultPackageClasses() {
		return getDefaultPackageClasses(publicApiExternalClasses);
	}

	public Set<String> getExternalClasses() {
		return externalClasses;
	}

	public Set<String> getPublicApiExternalClasses() {
		return publicApiExternalClasses;
	}
}
