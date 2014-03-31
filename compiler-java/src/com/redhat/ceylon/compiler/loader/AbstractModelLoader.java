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
package com.redhat.ceylon.compiler.loader;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.lang.model.type.TypeKind;

import com.redhat.ceylon.cmr.api.ArtifactResult;
import com.redhat.ceylon.cmr.api.JDKUtils;
import com.redhat.ceylon.common.Versions;
import com.redhat.ceylon.compiler.java.codegen.AbstractTransformer;
import com.redhat.ceylon.compiler.java.codegen.AnnotationArgument;
import com.redhat.ceylon.compiler.java.codegen.AnnotationConstructorParameter;
import com.redhat.ceylon.compiler.java.codegen.AnnotationFieldName;
import com.redhat.ceylon.compiler.java.codegen.AnnotationInvocation;
import com.redhat.ceylon.compiler.java.codegen.AnnotationTerm;
import com.redhat.ceylon.compiler.java.codegen.BooleanLiteralAnnotationTerm;
import com.redhat.ceylon.compiler.java.codegen.CharacterLiteralAnnotationTerm;
import com.redhat.ceylon.compiler.java.codegen.CollectionLiteralAnnotationTerm;
import com.redhat.ceylon.compiler.java.codegen.Decl;
import com.redhat.ceylon.compiler.java.codegen.DeclarationLiteralAnnotationTerm;
import com.redhat.ceylon.compiler.java.codegen.FloatLiteralAnnotationTerm;
import com.redhat.ceylon.compiler.java.codegen.IntegerLiteralAnnotationTerm;
import com.redhat.ceylon.compiler.java.codegen.InvocationAnnotationTerm;
import com.redhat.ceylon.compiler.java.codegen.LiteralAnnotationTerm;
import com.redhat.ceylon.compiler.java.codegen.Naming;
import com.redhat.ceylon.compiler.java.codegen.ObjectLiteralAnnotationTerm;
import com.redhat.ceylon.compiler.java.codegen.ParameterAnnotationTerm;
import com.redhat.ceylon.compiler.java.codegen.StringLiteralAnnotationTerm;
import com.redhat.ceylon.compiler.java.util.Timer;
import com.redhat.ceylon.compiler.java.util.Util;
import com.redhat.ceylon.compiler.loader.mirror.AccessibleMirror;
import com.redhat.ceylon.compiler.loader.mirror.AnnotatedMirror;
import com.redhat.ceylon.compiler.loader.mirror.AnnotationMirror;
import com.redhat.ceylon.compiler.loader.mirror.ClassMirror;
import com.redhat.ceylon.compiler.loader.mirror.FieldMirror;
import com.redhat.ceylon.compiler.loader.mirror.MethodMirror;
import com.redhat.ceylon.compiler.loader.mirror.PackageMirror;
import com.redhat.ceylon.compiler.loader.mirror.TypeMirror;
import com.redhat.ceylon.compiler.loader.mirror.TypeParameterMirror;
import com.redhat.ceylon.compiler.loader.mirror.VariableMirror;
import com.redhat.ceylon.compiler.loader.model.FieldValue;
import com.redhat.ceylon.compiler.loader.model.JavaBeanValue;
import com.redhat.ceylon.compiler.loader.model.JavaMethod;
import com.redhat.ceylon.compiler.loader.model.LazyClass;
import com.redhat.ceylon.compiler.loader.model.LazyClassAlias;
import com.redhat.ceylon.compiler.loader.model.LazyContainer;
import com.redhat.ceylon.compiler.loader.model.LazyElement;
import com.redhat.ceylon.compiler.loader.model.LazyInterface;
import com.redhat.ceylon.compiler.loader.model.LazyInterfaceAlias;
import com.redhat.ceylon.compiler.loader.model.LazyMethod;
import com.redhat.ceylon.compiler.loader.model.LazyModule;
import com.redhat.ceylon.compiler.loader.model.LazyPackage;
import com.redhat.ceylon.compiler.loader.model.LazyTypeAlias;
import com.redhat.ceylon.compiler.loader.model.LazyValue;
import com.redhat.ceylon.compiler.typechecker.analyzer.DeclarationVisitor;
import com.redhat.ceylon.compiler.typechecker.analyzer.ModuleManager;
import com.redhat.ceylon.compiler.typechecker.model.Annotation;
import com.redhat.ceylon.compiler.typechecker.model.Class;
import com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Element;
import com.redhat.ceylon.compiler.typechecker.model.Functional;
import com.redhat.ceylon.compiler.typechecker.model.Interface;
import com.redhat.ceylon.compiler.typechecker.model.IntersectionType;
import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.MethodOrValue;
import com.redhat.ceylon.compiler.typechecker.model.Module;
import com.redhat.ceylon.compiler.typechecker.model.ModuleImport;
import com.redhat.ceylon.compiler.typechecker.model.Modules;
import com.redhat.ceylon.compiler.typechecker.model.Package;
import com.redhat.ceylon.compiler.typechecker.model.Parameter;
import com.redhat.ceylon.compiler.typechecker.model.ParameterList;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.Scope;
import com.redhat.ceylon.compiler.typechecker.model.TypeAlias;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.TypeParameter;
import com.redhat.ceylon.compiler.typechecker.model.TypedDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.UnionType;
import com.redhat.ceylon.compiler.typechecker.model.Unit;
import com.redhat.ceylon.compiler.typechecker.model.UnknownType;
import com.redhat.ceylon.compiler.typechecker.model.Value;

/**
 * Abstract class of a model loader that can load a model from a compiled Java representation,
 * while being agnostic of the reflection API used to load the compiled Java representation.
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
public abstract class AbstractModelLoader implements ModelCompleter, ModelLoader {

    public static final String JAVA_BASE_MODULE_NAME = "java.base";
    public static final String CEYLON_LANGUAGE = "ceylon.language";
    public static final String CEYLON_LANGUAGE_MODEL = "ceylon.language.meta.model";
    public static final String CEYLON_LANGUAGE_MODEL_DECLARATION = "ceylon.language.meta.declaration";
    
    private static final String TIMER_MODEL_LOADER_CATEGORY = "model loader";
    public static final String JDK_MODULE_VERSION = "7";
    
    public static final String CEYLON_CEYLON_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.Ceylon";
    private static final String CEYLON_MODULE_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.Module";
    private static final String CEYLON_PACKAGE_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.Package";
    public static final String CEYLON_IGNORE_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.Ignore";
    private static final String CEYLON_CLASS_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.Class";
    public static final String CEYLON_NAME_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.Name";
    private static final String CEYLON_SEQUENCED_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.Sequenced";
    private static final String CEYLON_FUNCTIONAL_PARAMETER_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.FunctionalParameter";
    private static final String CEYLON_DEFAULTED_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.Defaulted";
    private static final String CEYLON_SATISFIED_TYPES_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.SatisfiedTypes";
    private static final String CEYLON_CASE_TYPES_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.CaseTypes";
    private static final String CEYLON_TYPE_PARAMETERS = "com.redhat.ceylon.compiler.java.metadata.TypeParameters";
    private static final String CEYLON_TYPE_INFO_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.TypeInfo";
    public static final String CEYLON_ATTRIBUTE_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.Attribute";
    public static final String CEYLON_OBJECT_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.Object";
    public static final String CEYLON_METHOD_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.Method";
    public static final String CEYLON_CONTAINER_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.Container";
    public static final String CEYLON_LOCAL_CONTAINER_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.LocalContainer";
    private static final String CEYLON_MEMBERS_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.Members";
    private static final String CEYLON_ANNOTATIONS_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.Annotations";
    public static final String CEYLON_VALUETYPE_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.ValueType";
    public static final String CEYLON_ALIAS_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.Alias";
    public static final String CEYLON_TYPE_ALIAS_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.TypeAlias";
    private static final String CEYLON_ANNOTATION_INSTANTIATION_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.AnnotationInstantiation";
    private static final String CEYLON_ANNOTATION_INSTANTIATION_ARGUMENTS_MEMBER = "arguments";
    private static final String CEYLON_ANNOTATION_INSTANTIATION_ANNOTATION_MEMBER = "primary";
    
    private static final String CEYLON_ANNOTATION_INSTANTIATION_TREE_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.AnnotationInstantiationTree";
    private static final String CEYLON_STRING_VALUE_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.StringValue";
    private static final String CEYLON_STRING_EXPRS_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.StringExprs";
    private static final String CEYLON_BOOLEAN_VALUE_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.BooleanValue";
    private static final String CEYLON_BOOLEAN_EXPRS_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.BooleanExprs";
    private static final String CEYLON_INTEGER_VALUE_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.IntegerValue";
    private static final String CEYLON_INTEGER_EXPRS_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.IntegerExprs";
    private static final String CEYLON_CHARACTER_VALUE_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.CharacterValue";
    private static final String CEYLON_CHARACTER_EXPRS_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.CharacterExprs";
    private static final String CEYLON_FLOAT_VALUE_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.FloatValue";
    private static final String CEYLON_FLOAT_EXPRS_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.FloatExprs";
    private static final String CEYLON_OBJECT_VALUE_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.ObjectValue";
    private static final String CEYLON_OBJECT_EXPRS_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.ObjectExprs";
    private static final String CEYLON_DECLARATION_VALUE_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.DeclarationValue";
    private static final String CEYLON_DECLARATION_EXPRS_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.DeclarationExprs";
    private static final String CEYLON_TRANSIENT_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.Transient";
    private static final String JAVA_DEPRECATED_ANNOTATION = "java.lang.Deprecated";
    
    private static final String CEYLON_LANGUAGE_ABSTRACT_ANNOTATION = "ceylon.language.AbstractAnnotation$annotation$";
    private static final String CEYLON_LANGUAGE_ACTUAL_ANNOTATION = "ceylon.language.ActualAnnotation$annotation$";
    private static final String CEYLON_LANGUAGE_ANNOTATION_ANNOTATION = "ceylon.language.AnnotationAnnotation$annotation$";
    private static final String CEYLON_LANGUAGE_DEFAULT_ANNOTATION = "ceylon.language.DefaultAnnotation$annotation$";
    private static final String CEYLON_LANGUAGE_FORMAL_ANNOTATION = "ceylon.language.FormalAnnotation$annotation$";
    private static final String CEYLON_LANGUAGE_LATE_ANNOTATION = "ceylon.language.LateAnnotation$annotation$";

    // important that these are with ::
    private static final String CEYLON_LANGUAGE_CALLABLE_TYPE_NAME = "ceylon.language::Callable";
    private static final String CEYLON_LANGUAGE_TUPLE_TYPE_NAME = "ceylon.language::Tuple";
    private static final String CEYLON_LANGUAGE_SEQUENTIAL_TYPE_NAME = "ceylon.language::Sequential";
    private static final String CEYLON_LANGUAGE_SEQUENCE_TYPE_NAME = "ceylon.language::Sequence";
    private static final String CEYLON_LANGUAGE_EMPTY_TYPE_NAME = "ceylon.language::Empty";
    
    private static final TypeMirror OBJECT_TYPE = simpleCeylonObjectType("java.lang.Object");
    private static final TypeMirror CEYLON_OBJECT_TYPE = simpleCeylonObjectType("ceylon.language.Object");
    private static final TypeMirror CEYLON_BASIC_TYPE = simpleCeylonObjectType("ceylon.language.Basic");
    private static final TypeMirror CEYLON_REIFIED_TYPE_TYPE = simpleCeylonObjectType("com.redhat.ceylon.compiler.java.runtime.model.ReifiedType");
    private static final TypeMirror CEYLON_TYPE_DESCRIPTOR_TYPE = simpleCeylonObjectType("com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor");
    
    private static final TypeMirror THROWABLE_TYPE = simpleCeylonObjectType("java.lang.Throwable");
    private static final TypeMirror ERROR_TYPE = simpleCeylonObjectType("java.lang.Error");
    private static final TypeMirror EXCEPTION_TYPE = simpleCeylonObjectType("java.lang.Exception");
    private static final TypeMirror CEYLON_THROWABLE_TYPE = simpleCeylonObjectType("java.lang.Throwable");
    private static final TypeMirror CEYLON_ERROR_TYPE = simpleCeylonObjectType("ceylon.language.Error");
    private static final TypeMirror CEYLON_EXCEPTION_TYPE = simpleCeylonObjectType("ceylon.language.Exception");
    
    private static final TypeMirror STRING_TYPE = simpleJDKObjectType("java.lang.String");
    private static final TypeMirror CEYLON_STRING_TYPE = simpleCeylonObjectType("ceylon.language.String");
    
    private static final TypeMirror PRIM_BOOLEAN_TYPE = simpleJDKObjectType("boolean");
    private static final TypeMirror CEYLON_BOOLEAN_TYPE = simpleCeylonObjectType("ceylon.language.Boolean");
    
    private static final TypeMirror PRIM_BYTE_TYPE = simpleJDKObjectType("byte");
    private static final TypeMirror PRIM_SHORT_TYPE = simpleJDKObjectType("short");

    private static final TypeMirror PRIM_INT_TYPE = simpleJDKObjectType("int");
    private static final TypeMirror PRIM_LONG_TYPE = simpleJDKObjectType("long");
    private static final TypeMirror CEYLON_INTEGER_TYPE = simpleCeylonObjectType("ceylon.language.Integer");
    
    private static final TypeMirror PRIM_FLOAT_TYPE = simpleJDKObjectType("float");
    private static final TypeMirror PRIM_DOUBLE_TYPE = simpleJDKObjectType("double");
    private static final TypeMirror CEYLON_FLOAT_TYPE = simpleCeylonObjectType("ceylon.language.Float");

    private static final TypeMirror PRIM_CHAR_TYPE = simpleJDKObjectType("char");
    private static final TypeMirror CEYLON_CHARACTER_TYPE = simpleCeylonObjectType("ceylon.language.Character");
    
    // this one has no "_" postfix because that's how we look it up
    protected static final String JAVA_LANG_BYTE_ARRAY = "java.lang.ByteArray";
    protected static final String JAVA_LANG_SHORT_ARRAY = "java.lang.ShortArray";
    protected static final String JAVA_LANG_INT_ARRAY = "java.lang.IntArray";
    protected static final String JAVA_LANG_LONG_ARRAY = "java.lang.LongArray";
    protected static final String JAVA_LANG_FLOAT_ARRAY = "java.lang.FloatArray";
    protected static final String JAVA_LANG_DOUBLE_ARRAY = "java.lang.DoubleArray";
    protected static final String JAVA_LANG_CHAR_ARRAY = "java.lang.CharArray";
    protected static final String JAVA_LANG_BOOLEAN_ARRAY = "java.lang.BooleanArray";
    protected static final String JAVA_LANG_OBJECT_ARRAY = "java.lang.ObjectArray";

    // this one has the "_" postfix because that's what we translate it to
    private static final String CEYLON_BYTE_ARRAY = "com.redhat.ceylon.compiler.java.language.ByteArray";
    private static final String CEYLON_SHORT_ARRAY = "com.redhat.ceylon.compiler.java.language.ShortArray";
    private static final String CEYLON_INT_ARRAY = "com.redhat.ceylon.compiler.java.language.IntArray";
    private static final String CEYLON_LONG_ARRAY = "com.redhat.ceylon.compiler.java.language.LongArray";
    private static final String CEYLON_FLOAT_ARRAY = "com.redhat.ceylon.compiler.java.language.FloatArray";
    private static final String CEYLON_DOUBLE_ARRAY = "com.redhat.ceylon.compiler.java.language.DoubleArray";
    private static final String CEYLON_CHAR_ARRAY = "com.redhat.ceylon.compiler.java.language.CharArray";
    private static final String CEYLON_BOOLEAN_ARRAY = "com.redhat.ceylon.compiler.java.language.BooleanArray";
    private static final String CEYLON_OBJECT_ARRAY = "com.redhat.ceylon.compiler.java.language.ObjectArray";

    private static final TypeMirror JAVA_BYTE_ARRAY_TYPE = simpleJDKObjectType("java.lang.ByteArray");
    private static final TypeMirror JAVA_SHORT_ARRAY_TYPE = simpleJDKObjectType("java.lang.ShortArray");
    private static final TypeMirror JAVA_INT_ARRAY_TYPE = simpleJDKObjectType("java.lang.IntArray");
    private static final TypeMirror JAVA_LONG_ARRAY_TYPE = simpleJDKObjectType("java.lang.LongArray");
    private static final TypeMirror JAVA_FLOAT_ARRAY_TYPE = simpleJDKObjectType("java.lang.FloatArray");
    private static final TypeMirror JAVA_DOUBLE_ARRAY_TYPE = simpleJDKObjectType("java.lang.DoubleArray");
    private static final TypeMirror JAVA_CHAR_ARRAY_TYPE = simpleJDKObjectType("java.lang.CharArray");
    private static final TypeMirror JAVA_BOOLEAN_ARRAY_TYPE = simpleJDKObjectType("java.lang.BooleanArray");
    
    private static TypeMirror simpleJDKObjectType(String name) {
        return new SimpleReflType(name, SimpleReflType.Module.JDK, TypeKind.DECLARED);
    }
    private static TypeMirror simpleCeylonObjectType(String name) {
        return new SimpleReflType(name, SimpleReflType.Module.CEYLON, TypeKind.DECLARED);
    }

    protected Map<String, Declaration> valueDeclarationsByName = new HashMap<String, Declaration>();
    protected Map<String, Declaration> typeDeclarationsByName = new HashMap<String, Declaration>();
    protected Map<Package, Unit> unitsByPackage = new HashMap<Package, Unit>();
    protected TypeParser typeParser;
    /** 
     * The type factory 
     * (<strong>should not be used while completing a declaration</strong>)
     */
    protected Unit typeFactory;
    protected final Set<String> loadedPackages = new HashSet<String>();
    protected final Map<String,LazyPackage> packagesByName = new HashMap<String,LazyPackage>();
    protected boolean packageDescriptorsNeedLoading = false;
    protected boolean isBootstrap;
    protected ModuleManager moduleManager;
    protected Modules modules;
    protected Map<String, ClassMirror> classMirrorCache = new HashMap<String, ClassMirror>();
    protected boolean binaryCompatibilityErrorRaised = false;
    protected Timer timer;
    private Map<String,LazyPackage> modulelessPackages = new HashMap<String,LazyPackage>();
    private ParameterNameParser parameterNameParser = new ParameterNameParser(this);
    
    /**
     * Loads a given package, if required. This is mostly useful for the javac reflection impl.
     * 
     * @param the module to load the package from
     * @param packageName the package name to load
     * @param loadDeclarations true to load all the declarations in this package.
     * @return 
     */
    public abstract boolean loadPackage(Module module, String packageName, boolean loadDeclarations);
    
    public boolean searchAgain(Module module, String name) {
        return false;
    }
    
    public boolean searchAgain(LazyPackage lazyPackage, String name) {
        return false;
    }
    
    /**
     * Looks up a ClassMirror by name. Uses cached results, and caches the result of calling lookupNewClassMirror
     * on cache misses.
     * 
     * @param module the module in which we should find the class
     * @param name the name of the Class to load
     * @return a ClassMirror for the specified class, or null if not found.
     */
    public synchronized final ClassMirror lookupClassMirror(Module module, String name){
        timer.startIgnore(TIMER_MODEL_LOADER_CATEGORY);
        try{
            // Java array classes are not where we expect them
            if (JAVA_LANG_OBJECT_ARRAY.equals(name)
                || JAVA_LANG_BOOLEAN_ARRAY.equals(name)
                || JAVA_LANG_BYTE_ARRAY.equals(name)
                || JAVA_LANG_SHORT_ARRAY.equals(name)
                || JAVA_LANG_INT_ARRAY.equals(name)
                || JAVA_LANG_LONG_ARRAY.equals(name)
                || JAVA_LANG_FLOAT_ARRAY.equals(name)
                || JAVA_LANG_DOUBLE_ARRAY.equals(name)
                || JAVA_LANG_CHAR_ARRAY.equals(name)) {
                // turn them into their real class location (get rid of the "java.lang" prefix)
                name = "com.redhat.ceylon.compiler.java.language" + name.substring(9);
                module = getLanguageModule();
            }
            String cacheKey = cacheKeyByModule(module, name);
            // we use containsKey to be able to cache null results
            if(classMirrorCache.containsKey(cacheKey)) {
                ClassMirror cachedMirror = classMirrorCache.get(cacheKey);
                if (cachedMirror != null || ! searchAgain(module, name)) {
                    return cachedMirror;
                }
            }
            ClassMirror mirror = lookupNewClassMirror(module, name);
            // we even cache null results
            classMirrorCache.put(cacheKey, mirror);
            return mirror;
        }finally{
            timer.stopIgnore(TIMER_MODEL_LOADER_CATEGORY);
        }
    }

    protected String cacheKeyByModule(Module module, String name) {
        return getCacheKeyByModule(module, name);
    }

    public static String getCacheKeyByModule(Module module, String name){
        String moduleSignature = module.getSignature();
        StringBuilder buf = new StringBuilder(moduleSignature.length()+1+name.length());
        // '/' is allowed in module version but not in module or class name, so we're good
        return buf.append(moduleSignature).append('/').append(name).toString();
    }

    protected boolean lastPartHasLowerInitial(String name) {
        int index = name.lastIndexOf('.');
        if (index != -1){
            name = name.substring(index+1);
        }
        // remove any possibly quoting char
        name = Naming.stripLeadingDollar(name);
        if(!name.isEmpty()){
            char c = name.charAt(0);
            return Util.isLowerCase(c);
        }
        return false;
    }
    
    /**
     * Looks up a ClassMirror by name. Called by lookupClassMirror on cache misses.
     * 
     * @param module the module in which we should find the given class
     * @param name the name of the Class to load
     * @return a ClassMirror for the specified class, or null if not found.
     */
    protected abstract ClassMirror lookupNewClassMirror(Module module, String name);

    /**
     * Adds the given module to the set of modules from which we can load classes.
     * 
     * @param module the module
     * @param artifact the module's artifact, if any. Can be null. 
     */
    public abstract void addModuleToClassPath(Module module, ArtifactResult artifact);

    /**
     * Returns true if the given module has been added to this model loader's classpath.
     * Defaults to true.
     */
    public boolean isModuleInClassPath(Module module){
        return true;
    }
    
    /**
     * Returns true if the given method is overriding an inherited method (from super class or interfaces).
     */
    protected abstract boolean isOverridingMethod(MethodMirror methodMirror);

    /**
     * Returns true if the given method is overloading an inherited method (from super class or interfaces).
     */
    protected abstract boolean isOverloadingMethod(MethodMirror methodMirror);

    /**
     * Logs a warning.
     */
    protected abstract void logWarning(String message);

    /**
     * Logs a debug message.
     */
    protected abstract void logVerbose(String message);
    
    /**
     * Logs an error
     */
    protected abstract void logError(String message);
    
    public void loadStandardModules(){
        // set up the type factory
        Module languageModule = findOrCreateModule(CEYLON_LANGUAGE, null);
        addModuleToClassPath(languageModule, null);
        Package languagePackage = findOrCreatePackage(languageModule, CEYLON_LANGUAGE);
        typeFactory.setPackage(languagePackage);
        
        // make sure the jdk modules are loaded
        for(String jdkModule : JDKUtils.getJDKModuleNames())
            findOrCreateModule(jdkModule, JDK_MODULE_VERSION);
        for(String jdkOracleModule : JDKUtils.getOracleJDKModuleNames())
            findOrCreateModule(jdkOracleModule, JDK_MODULE_VERSION);
        Module jdkModule = findOrCreateModule(JAVA_BASE_MODULE_NAME, JDK_MODULE_VERSION);
        
        /*
         * We start by loading java.lang and ceylon.language because we will need them no matter what.
         */
        loadPackage(jdkModule, "java.lang", false);
        loadPackage(languageModule, "com.redhat.ceylon.compiler.java.metadata", false);
        loadPackage(languageModule, "com.redhat.ceylon.compiler.java.language", false);

        /*
         * We do not load the ceylon.language module from class files if we're bootstrapping it
         */
        if(!isBootstrap){
//            loadPackage(languageModule, CEYLON_LANGUAGE, true);
//            loadPackage(languageModule, CEYLON_LANGUAGE_MODEL, true);
//            loadPackage(languageModule, CEYLON_LANGUAGE_MODEL_DECLARATION, true);
        }
    }

    /**
     * This is meant to be called if your subclass doesn't call loadStandardModules for whatever reason
     */
    public void setupWithNoStandardModules() {
        Module languageModule = modules.getLanguageModule();
        if(languageModule == null)
            throw new RuntimeException("Assertion failed: language module is null");
        Package languagePackage = languageModule.getPackage(CEYLON_LANGUAGE);
        if(languagePackage == null)
            throw new RuntimeException("Assertion failed: language package is null");
        typeFactory.setPackage(languagePackage);
    }

    enum ClassType {
        ATTRIBUTE, METHOD, OBJECT, CLASS, INTERFACE;
    }
    
    private ClassMirror loadClass(Module module, String pkgName, String className) {
        ClassMirror moduleClass = null;
        try{
            loadPackage(module, pkgName, false);
            moduleClass = lookupClassMirror(module, className);
        }catch(Exception x){
            logVerbose("[Failed to complete class "+className+"]");
        }
        return moduleClass;
    }

    private Declaration convertNonPrimitiveTypeToDeclaration(Module moduleScope, TypeMirror type, Scope scope, DeclarationType declarationType) {
        switch(type.getKind()){
        case VOID:
            return typeFactory.getAnythingDeclaration();
        case ARRAY:
            return ((Class)convertToDeclaration(getLanguageModule(), JAVA_LANG_OBJECT_ARRAY, DeclarationType.TYPE));
        case DECLARED:
            return convertDeclaredTypeToDeclaration(moduleScope, type, declarationType);
        case TYPEVAR:
            return safeLookupTypeParameter(scope, type.getQualifiedName());
        case WILDCARD:
            return typeFactory.getNothingDeclaration();
        // those can't happen
        case BOOLEAN:
        case BYTE:
        case CHAR:
        case SHORT:
        case INT:
        case LONG:
        case FLOAT:
        case DOUBLE:
            // all the autoboxing should already have been done
            throw new RuntimeException("Expected non-primitive type: "+type);
        case ERROR:
            return null;
        default:
            throw new RuntimeException("Failed to handle type "+type);
        }
    }

    private Declaration convertDeclaredTypeToDeclaration(Module moduleScope, TypeMirror type, DeclarationType declarationType) {
        // SimpleReflType does not do declared class so we make an exception for it
        String typeName = type.getQualifiedName();
        if(type instanceof SimpleReflType){
            Module module = null;
            switch(((SimpleReflType) type).getModule()){
            case CEYLON: module = getLanguageModule(); break;
            case JDK : module = getJDKBaseModule(); break;
            }
            return convertToDeclaration(module, typeName, declarationType);
        }
        ClassMirror classMirror = type.getDeclaredClass();
        Module module = findModuleForClassMirror(classMirror);
        if(isImported(moduleScope, module)){
            return convertToDeclaration(module, typeName, declarationType);
        }else{
            logVerbose("Declaration is not from an imported module: "+typeName);
            return null;
        }
    }
    protected Declaration convertToDeclaration(ClassMirror classMirror, DeclarationType declarationType) {
        // avoid ignored classes
        if(classMirror.getAnnotation(CEYLON_IGNORE_ANNOTATION) != null)
            return null;
        // avoid module and package descriptors too
        if(classMirror.getAnnotation(CEYLON_MODULE_ANNOTATION) != null
                || classMirror.getAnnotation(CEYLON_PACKAGE_ANNOTATION) != null)
            return null;
        
        List<Declaration> decls = new ArrayList<Declaration>();
        Module module = findModuleForClassMirror(classMirror);
        boolean[] alreadyExists = new boolean[1];
        Declaration decl = getOrCreateDeclaration(module, classMirror, declarationType,
                decls, alreadyExists);
        
        if (alreadyExists[0]) {
            return decl;
        }
        
        // find its package
        String pkgName = getPackageNameForQualifiedClassName(classMirror);
        
        LazyPackage pkg = findOrCreatePackage(module, pkgName);

        // find/make its Unit
        Unit unit = getCompiledUnit(pkg, classMirror);

        // set all the containers
        for(Declaration d : decls){
        
            // add it to its Unit
            d.setUnit(unit);
            unit.addDeclaration(d);

            setContainer(classMirror, d, pkg);
        }

        return decl;
    }

    protected String getPackageNameForQualifiedClassName(ClassMirror classMirror) {
        String qualifiedName = classMirror.getQualifiedName();
        // Java array classes we pretend come from java.lang
        if(qualifiedName.equals(CEYLON_OBJECT_ARRAY)
                || qualifiedName.equals(CEYLON_BOOLEAN_ARRAY)
                || qualifiedName.equals(CEYLON_BYTE_ARRAY)
                || qualifiedName.equals(CEYLON_SHORT_ARRAY)
                || qualifiedName.equals(CEYLON_INT_ARRAY)
                || qualifiedName.equals(CEYLON_LONG_ARRAY)
                || qualifiedName.equals(CEYLON_FLOAT_ARRAY)
                || qualifiedName.equals(CEYLON_DOUBLE_ARRAY)
                || qualifiedName.equals(CEYLON_CHAR_ARRAY))
            return "java.lang";
        else
            return unquotePackageName(classMirror.getPackage());
    }
    
    private String unquotePackageName(PackageMirror pkg) {
        return pkg.getQualifiedName().replace("$", "");
    }

    private void setContainer(ClassMirror classMirror, Declaration d, LazyPackage pkg) {
        // add it to its package if it's not an inner class
        if(!classMirror.isInnerClass() && !classMirror.isLocalClass()){
            d.setContainer(pkg);
            pkg.addCompiledMember(d);
            DeclarationVisitor.setVisibleScope(d);
        }else if(classMirror.isLocalClass()){
            // set its container to the package for now, but don't add it to the package as a member because it's not
            d.setContainer(pkg);
            ((LazyElement)d).setLocal(true);
        }else if(d instanceof ClassOrInterface || d instanceof TypeAlias){
            // do overloads later, since their container is their abstract superclass's container and
            // we have to set that one first
            if(d instanceof Class == false || !((Class)d).isOverloaded()){
                ClassOrInterface container = getContainer(pkg.getModule(), classMirror);
                d.setContainer(container);
                // let's not trigger lazy-loading
                ((LazyContainer)container).addMember(d);
                DeclarationVisitor.setVisibleScope(d);
                // now we can do overloads
                if(d instanceof Class && ((Class)d).getOverloads() != null){
                    for(Declaration overload : ((Class)d).getOverloads()){
                        overload.setContainer(container);
                        // let's not trigger lazy-loading
                        ((LazyContainer)container).addMember(overload);
                        DeclarationVisitor.setVisibleScope(overload);
                    }
                }
            }
        }
    }

    private ClassOrInterface getContainer(Module module, ClassMirror classMirror) {
        AnnotationMirror containerAnnotation = classMirror.getAnnotation(CEYLON_CONTAINER_ANNOTATION);
        if(containerAnnotation != null){
            TypeMirror javaClassMirror = (TypeMirror)containerAnnotation.getValue("klass");
            String javaClassName = javaClassMirror.getQualifiedName();
            ClassOrInterface containerDecl = (ClassOrInterface) convertToDeclaration(module, javaClassName, DeclarationType.TYPE);
            if(containerDecl == null)
                throw new ModelResolutionException("Failed to load outer type " + javaClassName 
                        + " for inner type " + classMirror.getQualifiedName().toString());
            return containerDecl;
        }else{
            return (ClassOrInterface) convertToDeclaration(classMirror.getEnclosingClass(), DeclarationType.TYPE);
        }
    }

    protected Declaration getOrCreateDeclaration(Module module, ClassMirror classMirror,
            DeclarationType declarationType, List<Declaration> decls, boolean[] alreadyExists) {
        alreadyExists[0] = false;
        Declaration decl = null;
        ClassType type;
        if(classMirror.isCeylonToplevelAttribute()){
            type = ClassType.ATTRIBUTE;
        }else if(classMirror.isCeylonToplevelMethod()){
            type = ClassType.METHOD;
        }else if(classMirror.isCeylonToplevelObject()){
            type = ClassType.OBJECT;
        }else if(classMirror.isInterface()){
            type = ClassType.INTERFACE;
        }else{
            type = ClassType.CLASS;
        }

        String key = classMirror.getCacheKey(module);
        // see if we already have it
        Map<String, Declaration> declarationCache = null;
        switch(type){
        case OBJECT:
            if(declarationType == DeclarationType.TYPE){
                declarationCache = typeDeclarationsByName;
                break;
            }
            // else fall-through to value
        case ATTRIBUTE:
        case METHOD:
            declarationCache = valueDeclarationsByName;
            break;
        case CLASS:
        case INTERFACE:
            declarationCache = typeDeclarationsByName;
        }
        if(declarationCache.containsKey(key)){
            alreadyExists[0] = true;
            return declarationCache.get(key);
        }
        

        checkBinaryCompatibility(classMirror);
        
        boolean isCeylon = classMirror.getAnnotation(CEYLON_CEYLON_ANNOTATION) != null;
        
        // make it
        switch(type){
        case ATTRIBUTE:
            decl = makeToplevelAttribute(classMirror);
            setDeclarationVisibility(decl, classMirror, classMirror, true);
            break;
        case METHOD:
            decl = makeToplevelMethod(classMirror);
            setDeclarationVisibility(decl, classMirror, classMirror, true);
            break;
        case OBJECT:
            // we first make a class
            Declaration objectClassDecl = makeLazyClass(classMirror, null, null, true);
            typeDeclarationsByName.put(key, objectClassDecl);
            decls.add(objectClassDecl);
            // then we make a value for it
            Declaration objectDecl = makeToplevelAttribute(classMirror);
            valueDeclarationsByName.put(key, objectDecl);
            decls.add(objectDecl);
            // which one did we want?
            decl = declarationType == DeclarationType.TYPE ? objectClassDecl : objectDecl;
            setDeclarationVisibility(objectClassDecl, classMirror, classMirror, true);
            setDeclarationVisibility(objectDecl, classMirror, classMirror, true);
            break;
        case CLASS:
            if(classMirror.getAnnotation(CEYLON_ALIAS_ANNOTATION) != null){
                decl = makeClassAlias(classMirror);
                setDeclarationVisibility(decl, classMirror, classMirror, true);
            }else if(classMirror.getAnnotation(CEYLON_TYPE_ALIAS_ANNOTATION) != null){
                decl = makeTypeAlias(classMirror);
                setDeclarationVisibility(decl, classMirror, classMirror, true);
            }else{
                List<MethodMirror> constructors = getClassConstructors(classMirror);
                if (!constructors.isEmpty()) {
                    if (constructors.size() > 1) {
                        decl = makeOverloadedConstructor(constructors, classMirror, decls, isCeylon);
                    } else {
                        // single constructor
                        MethodMirror constructor = constructors.get(0);
                        // if the class and constructor have different visibility, we pretend there's an overload of one
                        // if it's a ceylon class we don't care that they don't match sometimes, like for inner classes
                        // where the constructor is protected because we want to use an accessor, in this case the class
                        // visibility is to be used
                        if(isCeylon || getJavaVisibility(classMirror) == getJavaVisibility(constructor)){
                            decl = makeLazyClass(classMirror, null, constructor, false);
                            setDeclarationVisibility(decl, classMirror, classMirror, isCeylon);
                        }else{
                            decl = makeOverloadedConstructor(constructors, classMirror, decls, isCeylon);
                        }
                    }
                } else if(isCeylon && classMirror.getAnnotation(CEYLON_OBJECT_ANNOTATION) != null) {
                    // objects don't need overloading stuff
                    decl = makeLazyClass(classMirror, null, null, false);
                    setDeclarationVisibility(decl, classMirror, classMirror, isCeylon);
                } else if(getJavaVisibility(classMirror) != JavaVisibility.PRIVATE){
                    Class klass = (Class)makeOverloadedConstructor(constructors, classMirror, decls, isCeylon);
                    decl = klass;
                    LazyClass subdecl = makeLazyClass(classMirror, klass, null, false);
                    // no visibility for subdecl (private)
                    subdecl.setOverloaded(true);
                    klass.getOverloads().add(subdecl);
                    decls.add(subdecl);
                } else {
                    // private class does not need a constructor
                    decl = makeLazyClass(classMirror, null, null, false);
                }
            }
            break;
        case INTERFACE:
            if(classMirror.getAnnotation(CEYLON_ALIAS_ANNOTATION) != null){
                decl = makeInterfaceAlias(classMirror);
            }else{
                decl = makeLazyInterface(classMirror);
            }
            setDeclarationVisibility(decl, classMirror, classMirror, isCeylon);
            break;
        }


        // objects have special handling above
        if(type != ClassType.OBJECT){
            declarationCache.put(key, decl);
            decls.add(decl);
        }
        
        return decl;
    }

    private Declaration makeOverloadedConstructor(List<MethodMirror> constructors, ClassMirror classMirror, List<Declaration> decls, boolean isCeylon) {
        // If the class has multiple constructors we make a copy of the class
        // for each one (each with it's own single constructor) and make them
        // a subclass of the original
        Class supercls = makeLazyClass(classMirror, null, null, false);
        // the abstraction class gets the class modifiers
        setDeclarationVisibility(supercls, classMirror, classMirror, isCeylon);
        supercls.setAbstraction(true);
        List<Declaration> overloads = new ArrayList<Declaration>(constructors.size());
        // all filtering is done in getClassConstructors
        for (MethodMirror constructor : constructors) {
            LazyClass subdecl = makeLazyClass(classMirror, supercls, constructor, false);
            // the subclasses class get the constructor modifiers
            setDeclarationVisibility(subdecl, constructor, classMirror, isCeylon);
            subdecl.setOverloaded(true);
            overloads.add(subdecl);
            decls.add(subdecl);
        }
        supercls.setOverloads(overloads);
        return supercls;
    }

    private void setDeclarationVisibility(Declaration decl, AccessibleMirror mirror, ClassMirror classMirror, boolean isCeylon) {
        if(isCeylon){
            decl.setShared(mirror.isPublic());
        }else{
            decl.setShared(mirror.isPublic() || (mirror.isDefaultAccess() && classMirror.isInnerClass()) || mirror.isProtected());
            decl.setPackageVisibility(mirror.isDefaultAccess());
            decl.setProtectedVisibility(mirror.isProtected());
        }
    }

    private enum JavaVisibility {
        PRIVATE, PACKAGE, PROTECTED, PUBLIC;
    }
    
    private JavaVisibility getJavaVisibility(AccessibleMirror mirror) {
        if(mirror.isPublic())
            return JavaVisibility.PUBLIC;
        if(mirror.isProtected())
            return JavaVisibility.PROTECTED;
        if(mirror.isDefaultAccess())
            return JavaVisibility.PACKAGE;
        return JavaVisibility.PRIVATE;
    }

    private Declaration makeClassAlias(ClassMirror classMirror) {
        return new LazyClassAlias(classMirror, this);
    }

    private Declaration makeTypeAlias(ClassMirror classMirror) {
        return new LazyTypeAlias(classMirror, this);
    }

    private Declaration makeInterfaceAlias(ClassMirror classMirror) {
        return new LazyInterfaceAlias(classMirror, this);
    }

    private void checkBinaryCompatibility(ClassMirror classMirror) {
        // let's not report it twice
        if(binaryCompatibilityErrorRaised)
            return;
        AnnotationMirror annotation = classMirror.getAnnotation(CEYLON_CEYLON_ANNOTATION);
        if(annotation == null)
            return; // Java class, no check
        Integer major = (Integer) annotation.getValue("major");
        if(major == null)
            major = 0;
        Integer minor = (Integer) annotation.getValue("minor");
        if(minor == null)
            minor = 0;
        if(major != Versions.JVM_BINARY_MAJOR_VERSION
                || minor != Versions.JVM_BINARY_MINOR_VERSION){
            logError("Ceylon class " + classMirror.getQualifiedName() + " was compiled by an incompatible version of the Ceylon compiler"
                    +"\nThe class was compiled using "+major+"."+minor+"."
                    +"\nThis compiler supports "+Versions.JVM_BINARY_MAJOR_VERSION+"."+Versions.JVM_BINARY_MINOR_VERSION+"."
                    +"\nPlease try to recompile your module using a compatible compiler."
                    +"\nBinary compatibility will only be supported after Ceylon 1.0.");
            binaryCompatibilityErrorRaised = true;
        }
    }

    private List<MethodMirror> getClassConstructors(ClassMirror classMirror) {
        LinkedList<MethodMirror> constructors = new LinkedList<MethodMirror>();
        boolean isFromJDK = isFromJDK(classMirror);
        for(MethodMirror methodMirror : classMirror.getDirectMethods()){
            // We skip members marked with @Ignore
            if(methodMirror.getAnnotation(CEYLON_IGNORE_ANNOTATION) != null)
                continue;
            if(!methodMirror.isConstructor())
                continue;
            // FIXME: tmp hack to skip constructors that have type params as we don't handle them yet
            if(!methodMirror.getTypeParameters().isEmpty())
                continue;
            // FIXME: temporary, because some private classes from the jdk are
            // referenced in private methods but not available
            if(isFromJDK 
                    && !methodMirror.isPublic()
                    // allow protected because we can subclass them, but not package-private because we can't define
                    // classes in the jdk packages
                    && !methodMirror.isProtected())
                continue;

            // if we are expecting Ceylon code, check that we have enough reified type parameters
            if(classMirror.getAnnotation(AbstractModelLoader.CEYLON_CEYLON_ANNOTATION) != null){
                List<AnnotationMirror> tpAnnotations = getTypeParametersFromAnnotations(classMirror);
                int tpCount = tpAnnotations != null ? tpAnnotations.size() : classMirror.getTypeParameters().size();
                if(!checkReifiedTypeDescriptors(tpCount, classMirror, methodMirror, true))
                    continue;
            }
            
            constructors.add(methodMirror);
        }
        return constructors;
    }

    private boolean checkReifiedTypeDescriptors(int tpCount, ClassMirror container, MethodMirror methodMirror, boolean isConstructor) {
        List<VariableMirror> params = methodMirror.getParameters();
        int actualTypeDescriptorParameters = 0;
        for(VariableMirror param : params){
            if(param.getAnnotation(CEYLON_IGNORE_ANNOTATION) != null && sameType(CEYLON_TYPE_DESCRIPTOR_TYPE, param.getType())){
                actualTypeDescriptorParameters++;
            }else
                break;
        }
        if(tpCount != actualTypeDescriptorParameters){
            if(isConstructor)
                logError("Constructor for '"+container.getQualifiedName()+"' should take "+tpCount
                        +" reified type arguments (TypeDescriptor) but has '"+actualTypeDescriptorParameters+"': skipping constructor.");
            else
                logError("Method '"+container.getQualifiedName()+"."+methodMirror.getName()+"' should take "+tpCount
                    +" reified type arguments (TypeDescriptor) but has '"+actualTypeDescriptorParameters+"': method is invalid.");
            return false;
        }
        return true;
    }

    protected Unit getCompiledUnit(LazyPackage pkg, ClassMirror classMirror) {
        Unit unit = unitsByPackage.get(pkg);
        if(unit == null){
            unit = new Unit();
            unit.setPackage(pkg);
            unitsByPackage.put(pkg, unit);
        }
        return unit;
    }

    protected LazyValue makeToplevelAttribute(ClassMirror classMirror) {
        LazyValue value = new LazyValue(classMirror, this);
        return value;
    }

    protected LazyMethod makeToplevelMethod(ClassMirror classMirror) {
        LazyMethod method = new LazyMethod(classMirror, this);
        return method;
    }
    
    protected LazyClass makeLazyClass(ClassMirror classMirror, Class superClass, MethodMirror constructor, boolean forTopLevelObject) {
        LazyClass klass = new LazyClass(classMirror, this, superClass, constructor, forTopLevelObject);
        klass.setAnonymous(classMirror.getAnnotation(CEYLON_OBJECT_ANNOTATION) != null);
        klass.setAnnotation(classMirror.getAnnotation(CEYLON_LANGUAGE_ANNOTATION_ANNOTATION) != null);
        if(klass.isCeylon())
            klass.setAbstract(classMirror.getAnnotation(CEYLON_LANGUAGE_ABSTRACT_ANNOTATION) != null
                              // for toplevel classes if the annotation is missing we respect the java abstract modifier
                              // for member classes that would be ambiguous between formal and abstract so we don't and require
                              // the model annotation
                              || (!classMirror.isInnerClass() && !classMirror.isLocalClass() && classMirror.isAbstract()));
        else
            klass.setAbstract(classMirror.isAbstract());
        klass.setFormal(classMirror.getAnnotation(CEYLON_LANGUAGE_FORMAL_ANNOTATION) != null);
        klass.setDefault(classMirror.getAnnotation(CEYLON_LANGUAGE_DEFAULT_ANNOTATION) != null);
        klass.setActual(classMirror.getAnnotation(CEYLON_LANGUAGE_ACTUAL_ANNOTATION) != null);
        klass.setFinal(classMirror.isFinal());
        klass.setStaticallyImportable(!klass.isCeylon() && classMirror.isStatic());
        return klass;
    }

    protected LazyInterface makeLazyInterface(ClassMirror classMirror) {
        LazyInterface iface = new LazyInterface(classMirror, this);
        iface.setStaticallyImportable(!iface.isCeylon());
        return iface;
    }

    public synchronized Declaration convertToDeclaration(Module module, String typeName, DeclarationType declarationType) {
        // FIXME: this needs to move to the type parser and report warnings
        //This should be done where the TypeInfo annotation is parsed
        //to avoid retarded errors because of a space after a comma
        typeName = typeName.trim();
        timer.startIgnore(TIMER_MODEL_LOADER_CATEGORY);
        try{
            if ("ceylon.language.Nothing".equals(typeName)) {
                return typeFactory.getNothingDeclaration();
            } else if ("java.lang.Throwable".equals(typeName)) {
                // FIXME: this being here is highly dubious
                return convertToDeclaration(modules.getLanguageModule(), "ceylon.language.Throwable", declarationType);
            } else if ("java.lang.Error".equals(typeName)) {
                // FIXME: this being here is highly dubious
                return convertToDeclaration(modules.getLanguageModule(), "ceylon.language.Error", declarationType);
            } else if ("java.lang.Exception".equals(typeName)) {
                // FIXME: this being here is highly dubious
                return convertToDeclaration(modules.getLanguageModule(), "ceylon.language.Exception", declarationType);
            }
            ClassMirror classMirror = lookupClassMirror(module, typeName);
            if (classMirror == null) {
                // special case when bootstrapping because we may need to pull the decl from the typechecked model
                if(isBootstrap && typeName.startsWith(CEYLON_LANGUAGE+".")){
                    ProducedType languageType = findLanguageModuleDeclarationForBootstrap(typeName);
                    if(languageType != null)
                        return languageType.getDeclaration();
                }
                
                throw new ModelResolutionException("Failed to resolve "+typeName);
            }
            // we only allow source loading when it's java code we're compiling in the same go
            // (well, technically before the ceylon code)
            if(classMirror.isLoadedFromSource() && !classMirror.isJavaSource())
                return null;
            return convertToDeclaration(classMirror, declarationType);
        }finally{
            timer.stopIgnore(TIMER_MODEL_LOADER_CATEGORY);
        }
    }

    private ProducedType newNothingType() {
        return typeFactory.getNothingDeclaration().getType();
    }

    private ProducedType newUnknownType() {
        return new UnknownType(typeFactory).getType();
    }

    protected TypeParameter safeLookupTypeParameter(Scope scope, String name) {
        TypeParameter param = lookupTypeParameter(scope, name);
        if(param == null)
            throw new ModelResolutionException("Type param "+name+" not found in "+scope);
        return param;
    }
    
    private TypeParameter lookupTypeParameter(Scope scope, String name) {
        if(scope instanceof Method){
            Method m = (Method) scope;
            for(TypeParameter param : m.getTypeParameters()){
                if(param.getName().equals(name))
                    return param;
            }
            if (!m.isToplevel()) {
                // look it up in its container
                return lookupTypeParameter(scope.getContainer(), name);
            } else {
                // not found
                return null;
            }
        }else if(scope instanceof ClassOrInterface
                || scope instanceof TypeAlias){
            TypeDeclaration decl = (TypeDeclaration) scope;
            for(TypeParameter param : decl.getTypeParameters()){
                if(param.getName().equals(name))
                    return param;
            }
            if (!decl.isToplevel()) {
                // look it up in its container
                return lookupTypeParameter(scope.getContainer(), name);
            } else {
                // not found
                return null;
            }
        }else
            throw new ModelResolutionException("Type param "+name+" lookup not supported for scope "+scope);
    }
    
    //
    // Packages
    
    public synchronized LazyPackage findExistingPackage(Module module, String pkgName){
        String quotedPkgName = Util.quoteJavaKeywords(pkgName);
        LazyPackage pkg = findCachedPackage(module, quotedPkgName);
        if(pkg != null)
            return pkg;
        // special case for the jdk module
        String moduleName = module.getNameAsString();
        if(AbstractModelLoader.isJDKModule(moduleName)){
            if(JDKUtils.isJDKPackage(moduleName, pkgName) || JDKUtils.isOracleJDKPackage(moduleName, pkgName)){
                return findOrCreatePackage(module, pkgName);
            }
            return null;
        }
        // only create it if it exists
        if(((LazyModule)module).containsPackage(pkgName) && loadPackage(module, pkgName, false)){
            return findOrCreatePackage(module, pkgName);
        }
        return null;
    }
    
    private LazyPackage findCachedPackage(Module module, String quotedPkgName) {
        LazyPackage pkg = packagesByName.get(cacheKeyByModule(module, quotedPkgName));
        if(pkg != null){
            // only return it if it matches the module we're looking for, because if it doesn't we have an issue already logged
            // for a direct dependency on same module different versions logged, so no need to confuse this further
            if(module != null && pkg.getModule() != null && !module.equals(pkg.getModule()))
                return null;
            return pkg;
        }
        return null;
    }

    public synchronized LazyPackage findOrCreatePackage(Module module, final String pkgName) {
        String quotedPkgName = Util.quoteJavaKeywords(pkgName);
        LazyPackage pkg = findCachedPackage(module, quotedPkgName);
        if(pkg != null)
            return pkg;
        // try to find it from the module, perhaps it already got created and we didn't catch it
        if(module instanceof LazyModule){
            pkg = (LazyPackage) ((LazyModule) module).findPackageNoLazyLoading(pkgName);
        }else if(module != null){
            pkg = (LazyPackage) module.getDirectPackage(pkgName);
        }
        boolean isNew = pkg == null;
        if(pkg == null){
            pkg = new LazyPackage(this);
            // FIXME: some refactoring needed
            pkg.setName(pkgName == null ? Collections.<String>emptyList() : Arrays.asList(pkgName.split("\\.")));
        }
        packagesByName.put(cacheKeyByModule(module, quotedPkgName), pkg);

        // only bind it if we already have a module
        if(isNew && module != null){
            pkg.setModule(module);
            if(module instanceof LazyModule)
                ((LazyModule) module).addPackage(pkg);
            else
                module.getPackages().add(pkg);
        }
        
        // only load package descriptors for new packages after a certain phase
        if(packageDescriptorsNeedLoading)
            loadPackageDescriptor(pkg);
        
        return pkg;
    }

    public synchronized void loadPackageDescriptors() {
        for(Package pkg : packagesByName.values()){
            loadPackageDescriptor(pkg);
        }
        packageDescriptorsNeedLoading  = true;
    }

    private void loadPackageDescriptor(Package pkg) {
        // Don't try to load a package descriptor for ceylon.language 
        // if we're bootstrapping
        if (isBootstrap 
                && pkg.getQualifiedNameString().startsWith("ceylon.language")) {
            return;
        }
        
        // let's not load package descriptors for Java modules
        if(pkg.getModule() != null 
                && ((LazyModule)pkg.getModule()).isJava()){
            pkg.setShared(true);
            return;
        }
        String quotedQualifiedName = Util.quoteJavaKeywords(pkg.getQualifiedNameString());
        // FIXME: not sure the toplevel package can have a package declaration
        String className = quotedQualifiedName.isEmpty() ? "package_" : quotedQualifiedName + ".package_";
        logVerbose("[Trying to look up package from "+className+"]");
        Module module = pkg.getModule();
        if(module == null)
            throw new RuntimeException("Assertion failed: module is null for package "+pkg.getNameAsString());
        ClassMirror packageClass = loadClass(module, quotedQualifiedName, className);
        if(packageClass == null){
            logVerbose("[Failed to complete "+className+"]");
            // missing: leave it private
            return;
        }
        // did we compile it from source or class?
        if(packageClass.isLoadedFromSource()){
            // must have come from source, in which case we walked it and
            // loaded its values already
            logVerbose("[We are compiling the package "+className+"]");
            return;
        }
        loadCompiledPackage(packageClass, pkg);
    }

    private void loadCompiledPackage(ClassMirror packageClass, Package pkg) {
        String name = getAnnotationStringValue(packageClass, CEYLON_PACKAGE_ANNOTATION, "name");
        Boolean shared = getAnnotationBooleanValue(packageClass, CEYLON_PACKAGE_ANNOTATION, "shared");
        // FIXME: validate the name?
        if(name == null || name.isEmpty()){
            logWarning("Package class "+pkg.getQualifiedNameString()+" contains no name, ignoring it");
            return;
        }
        if(shared == null){
            logWarning("Package class "+pkg.getQualifiedNameString()+" contains no shared, ignoring it");
            return;
        }
        pkg.setShared(shared);
    }

    public Module lookupModuleByPackageName(String packageName) {
        for(Module module : modules.getListOfModules()){
            // don't try the default module because it will always say yes
            if(module.isDefault())
                continue;
            // skip modules we're not loading things from
            if(module != getLanguageModule()
                    && !isModuleInClassPath(module))
                continue;
            if(module instanceof LazyModule){
                if(((LazyModule)module).containsPackage(packageName))
                    return module;
            }else if(isSubPackage(module.getNameAsString(), packageName)){
                return module;
            }
        }
        if(JDKUtils.isJDKAnyPackage(packageName)
                || JDKUtils.isOracleJDKAnyPackage(packageName)){
            String moduleName = JDKUtils.getJDKModuleNameForPackage(packageName);
            return findModule(moduleName, JDK_MODULE_VERSION);
        }
        if(packageName.startsWith("com.redhat.ceylon.compiler.java.runtime")
                || packageName.startsWith("com.redhat.ceylon.compiler.java.language")
                || packageName.startsWith("com.redhat.ceylon.compiler.java.metadata")){
            return getLanguageModule();
        }
        return modules.getDefaultModule();
    }

    private boolean isSubPackage(String moduleName, String pkgName) {
        return pkgName.equals(moduleName)
                || pkgName.startsWith(moduleName+".");
    }

    //
    // Modules
    
    /**
     * Finds or creates a new module. This is mostly useful to force creation of modules such as jdk
     * or ceylon.language modules.
     */
    protected synchronized Module findOrCreateModule(String moduleName, String version) {
        // FIXME: we don't have any version??? If not this should be private
        boolean isJava = false;
        boolean defaultModule = false;

        // make sure it isn't loaded
        Module module = getLoadedModule(moduleName);
        if(module != null)
            return module;
        
        if(JDKUtils.isJDKModule(moduleName) || JDKUtils.isOracleJDKModule(moduleName)){
            isJava = true;
        }
        
        java.util.List<String> moduleNameList = Arrays.asList(moduleName.split("\\."));
        module = moduleManager.getOrCreateModule(moduleNameList, version);
        // make sure that when we load the ceylon language module we set it to where
        // the typechecker will look for it
        if(moduleName.equals(CEYLON_LANGUAGE)
                 && modules.getLanguageModule() == null){
             modules.setLanguageModule(module);
        }

        // TRICKY We do this only when isJava is true to prevent resetting
        // the value to false by mistake. LazyModule get's created with
        // this attribute to false by default, so it should work
        if (isJava && module instanceof LazyModule) {
            ((LazyModule)module).setJava(true);
        }
        
        // FIXME: this can't be that easy.
        module.setAvailable(true);
        module.setDefault(defaultModule);
        return module;
    }

    public synchronized boolean loadCompiledModule(Module module) {
        if(module.isDefault())
            return false;
        String pkgName = module.getNameAsString();
        if(pkgName.isEmpty())
            return false;
        String moduleClassName = pkgName + ".module_";
        logVerbose("[Trying to look up module from "+moduleClassName+"]");
        ClassMirror moduleClass = loadClass(module, pkgName, moduleClassName);
        if(moduleClass != null){
            // load its module annotation
            return loadCompiledModule(module, moduleClass, moduleClassName);
        }
        // give up
        return false;
    }

    private boolean loadCompiledModule(Module module, ClassMirror moduleClass, String moduleClassName) {
        String name = getAnnotationStringValue(moduleClass, CEYLON_MODULE_ANNOTATION, "name");
        String version = getAnnotationStringValue(moduleClass, CEYLON_MODULE_ANNOTATION, "version");
        if(name == null || name.isEmpty()){
            logWarning("Module class "+moduleClassName+" contains no name, ignoring it");
            return false;
        }
        if(!name.equals(module.getNameAsString())){
            logWarning("Module class "+moduleClassName+" declares an invalid name: "+name+". It should be: "+module.getNameAsString());
            return false;
        }
        if(version == null || version.isEmpty()){
            logWarning("Module class "+moduleClassName+" contains no version, ignoring it");
            return false;
        }
        if(!version.equals(module.getVersion())){
            logWarning("Module class "+moduleClassName+" declares an invalid version: "+version+". It should be: "+module.getVersion());
            return false;
        }
        int major = getAnnotationIntegerValue(moduleClass, CEYLON_CEYLON_ANNOTATION, "major", 0);
        int minor = getAnnotationIntegerValue(moduleClass, CEYLON_CEYLON_ANNOTATION, "minor", 0);
        module.setMajor(major);
        module.setMinor(minor);

        List<AnnotationMirror> imports = getAnnotationArrayValue(moduleClass, CEYLON_MODULE_ANNOTATION, "dependencies");
        if(imports != null){
            for (AnnotationMirror importAttribute : imports) {
                String dependencyName = (String) importAttribute.getValue("name");
                if (dependencyName != null) {
                    String dependencyVersion = (String) importAttribute.getValue("version");

                    Module dependency = moduleManager.getOrCreateModule(ModuleManager.splitModuleName(dependencyName), dependencyVersion);

                    Boolean optionalVal = (Boolean) importAttribute.getValue("optional");

                    Boolean exportVal = (Boolean) importAttribute.getValue("export");

                    ModuleImport moduleImport = moduleManager.findImport(module, dependency);
                    if (moduleImport == null) {
                        boolean optional = optionalVal != null && optionalVal;
                        boolean export = exportVal != null && exportVal;
                        moduleImport = new ModuleImport(dependency, optional, export);
                        module.addImport(moduleImport);
                    }
                }
            }
        }
        
        module.setAvailable(true);
        
        modules.getListOfModules().add(module);
        Module languageModule = modules.getLanguageModule();
        module.setLanguageModule(languageModule);
        if(module != languageModule){
            ModuleImport moduleImport = moduleManager.findImport(module, languageModule);
            if (moduleImport == null) {
                moduleImport = new ModuleImport(languageModule, false, false);
                module.addImport(moduleImport);
            }
        }
        
        return true;
    }  
    
    //
    // Utils for loading type info from the model
    
    @SuppressWarnings("unchecked")
    private <T> List<T> getAnnotationArrayValue(AnnotatedMirror mirror, String type, String field) {
        return (List<T>) getAnnotationValue(mirror, type, field);
    }

    @SuppressWarnings("unchecked")
    private <T> List<T> getAnnotationArrayValue(AnnotatedMirror mirror, String type) {
        return (List<T>) getAnnotationValue(mirror, type);
    }

    private String getAnnotationStringValue(AnnotatedMirror mirror, String type) {
        return getAnnotationStringValue(mirror, type, "value");
    }
    
    private String getAnnotationStringValue(AnnotatedMirror mirror, String type, String field) {
        return (String) getAnnotationValue(mirror, type, field);
    }
    
    private Boolean getAnnotationBooleanValue(AnnotatedMirror mirror, String type, String field) {
        return (Boolean) getAnnotationValue(mirror, type, field);
    }

    private int getAnnotationIntegerValue(AnnotatedMirror mirror, String type, String field, int defaultValue) {
        Integer val = (Integer) getAnnotationValue(mirror, type, field);
        return val != null ? val : defaultValue;
    }
    
    @SuppressWarnings("unchecked")
    private List<TypeMirror> getAnnotationClassValues(AnnotationMirror annotation, String field) {
        return (List<TypeMirror>)annotation.getValue(field);
    }
    
    @SuppressWarnings("unchecked")
    private List<String> getAnnotationStringValues(AnnotationMirror annotation, String field) {
        return (List<String>)annotation.getValue(field);
    }
    
    @SuppressWarnings("unchecked")
    private List<Integer> getAnnotationIntegerValues(AnnotationMirror annotation, String field) {
        return (List<Integer>)annotation.getValue(field);
    }
    
    @SuppressWarnings("unchecked")
    private List<Boolean> getAnnotationBooleanValues(AnnotationMirror annotation, String field) {
        return (List<Boolean>)annotation.getValue(field);
    }
    
    @SuppressWarnings("unchecked")
    private List<Long> getAnnotationLongValues(AnnotationMirror annotation, String field) {
        return (List<Long>)annotation.getValue(field);
    }
    
    @SuppressWarnings("unchecked")
    private List<Double> getAnnotationDoubleValues(AnnotationMirror annotation, String field) {
        return (List<Double>)annotation.getValue(field);
    }
    @SuppressWarnings("unchecked")
    private List<AnnotationMirror> getAnnotationAnnoValues(AnnotationMirror annotation, String field) {
        return (List<AnnotationMirror>)annotation.getValue(field);
    }

    private Object getAnnotationValue(AnnotatedMirror mirror, String type) {
        return getAnnotationValue(mirror, type, "value");
    }
    
    private Object getAnnotationValue(AnnotatedMirror mirror, String type, String fieldName) {
        AnnotationMirror annotation = mirror.getAnnotation(type);
        if(annotation != null){
            return annotation.getValue(fieldName);
        }
        return null;
    }

    //
    // ModelCompleter
    
    @Override
    public synchronized void complete(LazyInterface iface) {
        timer.startIgnore(TIMER_MODEL_LOADER_CATEGORY);
        complete(iface, iface.classMirror);
        timer.stopIgnore(TIMER_MODEL_LOADER_CATEGORY);
    }

    @Override
    public synchronized void completeTypeParameters(LazyInterface iface) {
        timer.startIgnore(TIMER_MODEL_LOADER_CATEGORY);
        completeTypeParameters(iface, iface.classMirror);
        timer.stopIgnore(TIMER_MODEL_LOADER_CATEGORY);
    }

    @Override
    public synchronized void complete(LazyClass klass) {
        timer.startIgnore(TIMER_MODEL_LOADER_CATEGORY);
        complete(klass, klass.classMirror);
        timer.stopIgnore(TIMER_MODEL_LOADER_CATEGORY);
    }

    @Override
    public synchronized void completeTypeParameters(LazyClass klass) {
        timer.startIgnore(TIMER_MODEL_LOADER_CATEGORY);
        completeTypeParameters(klass, klass.classMirror);
        timer.stopIgnore(TIMER_MODEL_LOADER_CATEGORY);
    }

    @Override
    public synchronized void completeTypeParameters(LazyClassAlias lazyClassAlias) {
        timer.startIgnore(TIMER_MODEL_LOADER_CATEGORY);
        completeLazyAliasTypeParameters(lazyClassAlias, lazyClassAlias.classMirror);
        timer.stopIgnore(TIMER_MODEL_LOADER_CATEGORY);
    }
    
    @Override
    public synchronized void completeTypeParameters(LazyInterfaceAlias lazyInterfaceAlias) {
        timer.startIgnore(TIMER_MODEL_LOADER_CATEGORY);
        completeLazyAliasTypeParameters(lazyInterfaceAlias, lazyInterfaceAlias.classMirror);
        timer.stopIgnore(TIMER_MODEL_LOADER_CATEGORY);
    }

    @Override
    public synchronized void completeTypeParameters(LazyTypeAlias lazyTypeAlias) {
        timer.startIgnore(TIMER_MODEL_LOADER_CATEGORY);
        completeLazyAliasTypeParameters(lazyTypeAlias, lazyTypeAlias.classMirror);
        timer.stopIgnore(TIMER_MODEL_LOADER_CATEGORY);
    }

    @Override
    public synchronized void complete(LazyInterfaceAlias alias) {
        timer.startIgnore(TIMER_MODEL_LOADER_CATEGORY);
        completeLazyAlias(alias, alias.classMirror, CEYLON_ALIAS_ANNOTATION);
        timer.stopIgnore(TIMER_MODEL_LOADER_CATEGORY);
    }
    
    @Override
    public synchronized void complete(LazyClassAlias alias) {
        timer.startIgnore(TIMER_MODEL_LOADER_CATEGORY);
        completeLazyAlias(alias, alias.classMirror, CEYLON_ALIAS_ANNOTATION);
        
        // Find the instantiator method
        MethodMirror instantiator = null;
        ClassMirror instantiatorClass = alias.isToplevel() ? alias.classMirror : alias.classMirror.getEnclosingClass();
        for (MethodMirror method : instantiatorClass.getDirectMethods()) {
            // If we're finding things based on their name, shouldn't we 
            // we using Naming to do it?
            if (method.getName().equals(alias.getName() + "$aliased$")) {
                instantiator = method;
                break;
            }
        }
        // Read the parameters from the instantiator, rather than the aliased class
        if (instantiator != null) {
            setParameters(alias, instantiator, true, alias);
        }
        timer.stopIgnore(TIMER_MODEL_LOADER_CATEGORY);
    }

    @Override
    public synchronized void complete(LazyTypeAlias alias) {
        timer.startIgnore(TIMER_MODEL_LOADER_CATEGORY);
        completeLazyAlias(alias, alias.classMirror, CEYLON_TYPE_ALIAS_ANNOTATION);
        timer.stopIgnore(TIMER_MODEL_LOADER_CATEGORY);
    }

    private void completeLazyAliasTypeParameters(TypeDeclaration alias, ClassMirror mirror) {
        // type parameters
        setTypeParameters(alias, mirror);
    }

    private void completeLazyAlias(TypeDeclaration alias, ClassMirror mirror, String aliasAnnotationName) {
        // now resolve the extended type
        AnnotationMirror aliasAnnotation = mirror.getAnnotation(aliasAnnotationName);
        String extendedTypeString = (String) aliasAnnotation.getValue();
        
        ProducedType extendedType = decodeType(extendedTypeString, alias, Decl.getModuleContainer(alias), "alias target");
        alias.setExtendedType(extendedType);
    }

    private void completeTypeParameters(ClassOrInterface klass, ClassMirror classMirror) {
        setTypeParameters(klass, classMirror);
    }

    private void complete(ClassOrInterface klass, ClassMirror classMirror) {
        Map<MethodMirror, List<MethodMirror>> variables = new HashMap<MethodMirror, List<MethodMirror>>();
        boolean isFromJDK = isFromJDK(classMirror);
        boolean isCeylon = (classMirror.getAnnotation(CEYLON_CEYLON_ANNOTATION) != null);
        
        // now that everything has containers, do the inner classes
        if(klass instanceof Class == false || !((Class)klass).isOverloaded()){
            // this will not load inner classes of overloads, but that's fine since we want them in the
            // abstracted super class (the real one)
            addInnerClasses(klass, classMirror);
        }

        // Java classes with multiple constructors get turned into multiple Ceylon classes
        // Here we get the specific constructor that was assigned to us (if any)
        MethodMirror constructor = null;
        if (klass instanceof LazyClass) {
            constructor = ((LazyClass)klass).getConstructor();
        }
            
        // Turn a list of possibly overloaded methods into a map
        // of lists that contain methods with the same name
        Map<String, List<MethodMirror>> methods = new LinkedHashMap<String, List<MethodMirror>>();
        for(MethodMirror methodMirror : classMirror.getDirectMethods()){
            // We skip members marked with @Ignore
            if(methodMirror.getAnnotation(CEYLON_IGNORE_ANNOTATION) != null)
                continue;
            if(methodMirror.isStaticInit())
                continue;
            if(isCeylon && methodMirror.isStatic())
                continue;
            // FIXME: temporary, because some private classes from the jdk are
            // referenced in private methods but not available
            if(isFromJDK && !methodMirror.isPublic())
                continue;
            String methodName = methodMirror.getName();
            List<MethodMirror> homonyms = methods.get(methodName);
            if (homonyms == null) {
                homonyms = new LinkedList<MethodMirror>();
                methods.put(methodName, homonyms);
            }
            homonyms.add(methodMirror);
        }
        
        // Add the methods
        for(List<MethodMirror> methodMirrors : methods.values()){
            boolean isOverloaded = methodMirrors.size() > 1;
            
            List<Declaration> overloads = (isOverloaded) ? new ArrayList<Declaration>(methodMirrors.size()) : null;
            for (MethodMirror methodMirror : methodMirrors) {
                String methodName = methodMirror.getName();
                if(methodMirror.isConstructor() || isInstantiator(methodMirror)) {
                    break;
                } else if(isGetter(methodMirror)) {
                    // simple attribute
                    addValue(klass, methodMirror, getJavaAttributeName(methodName), isCeylon);
                } else if(isSetter(methodMirror)) {
                    // We skip setters for now and handle them later
                    variables.put(methodMirror, methodMirrors);
                } else if(isHashAttribute(methodMirror)) {
                    // ERASURE
                    // Un-erasing 'hash' attribute from 'hashCode' method
                    addValue(klass, methodMirror, "hash", isCeylon);
                } else if(isStringAttribute(methodMirror)) {
                    // ERASURE
                    // Un-erasing 'string' attribute from 'toString' method
                    addValue(klass, methodMirror, "string", isCeylon);
                } else if(!methodMirror.getName().equals("hash")
                        && !methodMirror.getName().equals("string")){
                    // normal method
                    Method m = addMethod(klass, methodMirror, isCeylon, isOverloaded);
                    if (isOverloaded) {
                        overloads.add(m);
                    }
                }
            }
            
            if (overloads != null && !overloads.isEmpty()) {
                // We create an extra "abstraction" method for overloaded methods
                Method abstractionMethod = addMethod(klass, methodMirrors.get(0), false, false);
                abstractionMethod.setAbstraction(true);
                abstractionMethod.setOverloads(overloads);
                abstractionMethod.setType(newUnknownType());
            }
        }

        for(FieldMirror fieldMirror : classMirror.getDirectFields()){
            // We skip members marked with @Ignore
            if(fieldMirror.getAnnotation(CEYLON_IGNORE_ANNOTATION) != null)
                continue;
            if(isCeylon && fieldMirror.isStatic())
                continue;
            // FIXME: temporary, because some private classes from the jdk are
            // referenced in private methods but not available
            if(isFromJDK && !fieldMirror.isPublic())
                continue;
            String name = fieldMirror.getName();
            // skip the field if "we've already got one"
            boolean conflicts = klass.getDirectMember(name, null, false) != null
                    || "equals".equals(name)
                    || "string".equals(name)
                    || "hash".equals(name);
            if (!conflicts) {
                addValue(klass, fieldMirror.getName(), fieldMirror, isCeylon);
            }
        }

        // Having loaded methods and values, we can now set the constructor parameters
        if(constructor != null 
                && (!(klass instanceof LazyClass) || !((LazyClass)klass).isTopLevelObjectType()))
            setParameters((Class)klass, constructor, isCeylon, klass);
        // Now marry-up attributes and parameters)
        if (klass instanceof Class) {
            for (Declaration m : klass.getMembers()) {
                if (Decl.isValue(m)) {
                    Value v = (Value)m;
                    Parameter p = ((Class)klass).getParameter(v.getName());
                    if (p != null) {
                        p.setHidden(true);
                    }
                }
            }
        }

        // Now mark all Values for which Setters exist as variable
        for(Entry<MethodMirror, List<MethodMirror>> setterEntry : variables.entrySet()){
            MethodMirror setter = setterEntry.getKey();
            String name = getJavaAttributeName(setter.getName());
            Declaration decl = klass.getMember(name, null, false);
            boolean foundGetter = false;
            // skip Java fields, which we only get if there is no getter method, in that case just add the setter method
            if (decl instanceof Value && decl instanceof FieldValue == false) {
                Value value = (Value)decl;
                VariableMirror setterParam = setter.getParameters().get(0);
                ProducedType paramType = obtainType(setterParam.getType(), setterParam, klass, Decl.getModuleContainer(klass), VarianceLocation.INVARIANT,
                        "setter '"+setter.getName()+"'", klass);
                // only add the setter if it has exactly the same type as the getter
                if(paramType.isExactly(value.getType())){
                    foundGetter = true;
                    value.setVariable(true);
                    if(decl instanceof JavaBeanValue)
                        ((JavaBeanValue)decl).setSetterName(setter.getName());
                }else
                    logVerbose("Setter parameter type for "+name+" does not match corresponding getter type, adding setter as a method");
            } 
            
            if(!foundGetter){
                // it was not a setter, it was a method, let's add it as such
                addMethod(klass, setter, isCeylon, false);
            }
        }

        // In some cases, where all constructors are ignored, we can end up with no constructor, so
        // pretend we have one which takes no parameters (eg. ceylon.language.String).
        if(klass instanceof Class
                && !((Class) klass).isAbstraction()
                && !klass.isAnonymous()
                && ((Class) klass).getParameterList() == null){
            ((Class) klass).setParameterList(new ParameterList());
        }
        
        setExtendedType(klass, classMirror);
        setSatisfiedTypes(klass, classMirror);
        setCaseTypes(klass, classMirror);
        fillRefinedDeclarations(klass);
        if(isCeylon)
            checkReifiedGenericsForMethods(klass, classMirror);
        setAnnotations(klass, classMirror);
        
        if (!isCeylon 
                && isThrowableSubtype(classMirror)) {
            addMessageValueForJavaThrowable(klass);
        }
    }
    
    private boolean isThrowableSubtype(ClassMirror classMirror) {
        TypeMirror superclassType = classMirror.getSuperclass();
        while(superclassType != null) {
            classMirror =  superclassType.getDeclaredClass();
            if ("java.lang.Throwable".equals(classMirror.getQualifiedName())) {
                return true;
            }
            superclassType = classMirror.getSuperclass();
        }
        return false;
    }
    
    /**
     * The {@code getMessage()} result from a Java Exception type can be 
     * null, so we need to 
     * call {@code setUncheckedNullType()} on the equivalent value, but 
     * when such classes haven't overridden {@code getMessage()} there's no 
     * direct member on which to call that. 
     * So we need to add one in such cases.
     * @param klass
     */
    private void addMessageValueForJavaThrowable(ClassOrInterface klass) {
        Value messageValue = (Value)klass.getDirectMember("message", Collections.<ProducedType>emptyList(), false);
        if (messageValue == null) {
            for (MethodMirror method : lookupClassMirror(getJDKBaseModule(), "java.lang.Throwable").getDirectMethods()) {
                if ("getMessage".equals(method.getName())
                        && method.getParameters().isEmpty()) {
                    addValue(klass, method, "message", false);
                    messageValue = (Value)klass.getDirectMember("message", Collections.<ProducedType>emptyList(), false);
                    break;
                }
            }
        } 
        messageValue.setUncheckedNullType(true);
    }

    private boolean isInstantiator(MethodMirror methodMirror) {
        return methodMirror.getName().endsWith("$aliased$");
    }
    private void checkReifiedGenericsForMethods(ClassOrInterface klass, ClassMirror classMirror) {
        for(Declaration member : klass.getMembers()){
            if(member instanceof JavaMethod == false)
                continue;
            MethodMirror mirror = ((JavaMethod)member).mirror;
            if(AbstractTransformer.supportsReified(member)){
                checkReifiedTypeDescriptors(mirror.getTypeParameters().size(), classMirror, mirror, false);
            }
        }
    }
    
    private boolean isFromJDK(ClassMirror classMirror) {
        String pkgName = unquotePackageName(classMirror.getPackage());
        return JDKUtils.isJDKAnyPackage(pkgName) || JDKUtils.isOracleJDKAnyPackage(pkgName);
    }

    private void setAnnotations(Declaration decl, AnnotatedMirror classMirror) {
        List<AnnotationMirror> annotations = getAnnotationArrayValue(classMirror, CEYLON_ANNOTATIONS_ANNOTATION);
        if(annotations != null) {
            for(AnnotationMirror annotation : annotations){
                decl.getAnnotations().add(readModelAnnotation(annotation));
            }
        }
        // Add a ceylon deprecated("") if it's annotated with java.lang.Deprecated
        // and doesn't already have the ceylon annotation
        if (classMirror.getAnnotation(JAVA_DEPRECATED_ANNOTATION) != null) {
            boolean hasCeylonDeprecated = false;
            for(Annotation a : decl.getAnnotations()) {
                if (a.getName().equals("deprecated")) {
                    hasCeylonDeprecated = true;
                    break;
                }
            }
            if (!hasCeylonDeprecated) {
                Annotation modelAnnotation = new Annotation();
                modelAnnotation.setName("deprecated");
                modelAnnotation.getPositionalArguments().add("");
                decl.getAnnotations().add(modelAnnotation);
            }
        }
    }

    private Annotation readModelAnnotation(AnnotationMirror annotation) {
        Annotation modelAnnotation = new Annotation();
        modelAnnotation.setName((String) annotation.getValue());
        @SuppressWarnings("unchecked")
        List<String> arguments = (List<String>) annotation.getValue("arguments");
        if(arguments != null){
            modelAnnotation.getPositionalArguments().addAll(arguments);
        }else{
            @SuppressWarnings("unchecked")
            List<AnnotationMirror> namedArguments = (List<AnnotationMirror>) annotation.getValue("namedArguments");
            if(namedArguments != null){
                for(AnnotationMirror namedArgument : namedArguments){
                    String argName = (String) namedArgument.getValue("name");
                    String argValue = (String) namedArgument.getValue("value");
                    modelAnnotation.getNamedArguments().put(argName, argValue);
                }
            }
        }
        return modelAnnotation;
    }

    private void addInnerClasses(ClassOrInterface klass, ClassMirror classMirror) {
        AnnotationMirror membersAnnotation = classMirror.getAnnotation(CEYLON_MEMBERS_ANNOTATION);
        if(membersAnnotation == null)
            addInnerClassesFromMirror(klass, classMirror);
        else
            addInnerClassesFromAnnotation(klass, membersAnnotation);
    }

    private void addInnerClassesFromAnnotation(ClassOrInterface klass, AnnotationMirror membersAnnotation) {
        @SuppressWarnings("unchecked")
        List<AnnotationMirror> members = (List<AnnotationMirror>) membersAnnotation.getValue();
        for(AnnotationMirror member : members){
            TypeMirror javaClassMirror = (TypeMirror)member.getValue("klass");
            String javaClassName = javaClassMirror.getQualifiedName();
            Declaration innerDecl = convertToDeclaration(Decl.getModuleContainer(klass), javaClassName, DeclarationType.TYPE);
            if(innerDecl == null)
                throw new ModelResolutionException("Failed to load inner type " + javaClassName 
                        + " for outer type " + klass.getQualifiedNameString());
        }
    }

    /**
     * Allows subclasses to do something to the class name
     */
    protected String assembleJavaClass(String javaClass, String packageName) {
        return javaClass;
    }

    private void addInnerClassesFromMirror(ClassOrInterface klass, ClassMirror classMirror) {
        boolean isJDK = isFromJDK(classMirror);
        for(ClassMirror innerClass : classMirror.getDirectInnerClasses()){
            // We skip members marked with @Ignore
            if(innerClass.getAnnotation(CEYLON_IGNORE_ANNOTATION) != null)
                continue;
            // We skip anonymous inner classes
            if(innerClass.isAnonymous())
                continue;
            // We skip private classes, otherwise the JDK has a ton of unresolved things
            if(isJDK && !innerClass.isPublic())
                continue;
            // convert it
            convertToDeclaration(innerClass, DeclarationType.TYPE);
            // no need to set its container as that's now handled by convertToDeclaration
        }
    }

    private Method addMethod(ClassOrInterface klass, MethodMirror methodMirror, boolean isCeylon, boolean isOverloaded) {
        JavaMethod method = new JavaMethod(methodMirror);
        String methodName = methodMirror.getName();
        
        method.setContainer(klass);
        method.setRealName(methodName);
        method.setUnit(klass.getUnit());
        method.setOverloaded(isOverloaded || isOverloadingMethod(methodMirror));
        ProducedType type = null;
        try{
            setMethodOrValueFlags(klass, methodMirror, method, isCeylon);
        }catch(ModelResolutionException x){
            // collect an error in its type
            type = logModelResolutionException(x, klass, "method '"+methodMirror.getName()+"' (checking if it is an overriding method");
        }
        if(methodName.equals("hash")
                || methodName.equals("string"))
            method.setName(methodName+"_method");
        else
            method.setName(Util.strip(methodName, isCeylon, method.isShared()));
        method.setDefaultedAnnotation(methodMirror.isDefault());

        // type params first
        setTypeParameters(method, methodMirror);

        // now its parameters
        if(isEqualsMethod(methodMirror))
            setEqualsParameters(method, methodMirror);
        else
            setParameters(method, methodMirror, isCeylon, klass);
        
        // and its return type
        // do not log an additional error if we had one from checking if it was overriding
        if(type == null)
            type = obtainType(methodMirror.getReturnType(), methodMirror, method, Decl.getModuleContainer(method), VarianceLocation.COVARIANT,
                              "method '"+methodMirror.getName()+"'", klass);
        method.setType(type);
        method.setUncheckedNullType((!isCeylon && !methodMirror.getReturnType().isPrimitive()) || isUncheckedNull(methodMirror));
        type.setRaw(isRaw(Decl.getModuleContainer(klass), methodMirror.getReturnType()));
        markDeclaredVoid(method, methodMirror);
        markUnboxed(method, methodMirror, methodMirror.getReturnType());
        markTypeErased(method, methodMirror, methodMirror.getReturnType());
        setAnnotations(method, methodMirror);
        
        klass.getMembers().add(method);
        DeclarationVisitor.setVisibleScope(method);

        return method;
    }

    private void fillRefinedDeclarations(ClassOrInterface klass) {
        for(Declaration member : klass.getMembers()){
            // do not trigger a type load (by calling isActual()) for Java inner classes since they
            // can never be actual
            if(member instanceof ClassOrInterface && !Decl.isCeylon((ClassOrInterface)member))
                continue;
            if(member.isActual()){
                member.setRefinedDeclaration(findRefinedDeclaration(klass, member.getName(), getSignature(member), false));
            }
        }
    }

    private List<ProducedType> getSignature(Declaration decl) {
        List<ProducedType> result = null;
        if (decl instanceof Functional) {
            Functional func = (Functional)decl;
            if (func.getParameterLists().size() > 0) {
                List<Parameter> params = func.getParameterLists().get(0).getParameters();
                result = new ArrayList<ProducedType>(params.size());
                for (Parameter p : params) {
                    result.add(p.getType());
                }
            }
        }
        return result;
    }
    
    private Declaration findRefinedDeclaration(ClassOrInterface decl, String name, List<ProducedType> signature, boolean ellipsis) {
        Declaration refinedDeclaration = decl.getRefinedMember(name, signature, ellipsis);
        if(refinedDeclaration == null)
            throw new ModelResolutionException("Failed to find refined declaration for "+name);
        return refinedDeclaration;
    }

    private boolean isStartOfJavaBeanPropertyName(char c){
        return Character.isUpperCase(c) || c == '_'; 
    }
    
    private boolean isGetter(MethodMirror methodMirror) {
        String name = methodMirror.getName();
        boolean matchesGet = name.length() > 3 && name.startsWith("get") 
                && isStartOfJavaBeanPropertyName(name.charAt(3)) 
                && !"getString".equals(name) && !"getHash".equals(name) && !"getEquals".equals(name);
        boolean matchesIs = name.length() > 2 && name.startsWith("is") 
                && isStartOfJavaBeanPropertyName(name.charAt(2)) 
                && !"isString".equals(name) && !"isHash".equals(name) && !"isEquals".equals(name);
        boolean hasNoParams = methodMirror.getParameters().size() == 0;
        boolean hasNonVoidReturn = (methodMirror.getReturnType().getKind() != TypeKind.VOID);
        return (matchesGet || matchesIs) && hasNoParams && hasNonVoidReturn;
    }
    
    private boolean isSetter(MethodMirror methodMirror) {
        String name = methodMirror.getName();
        boolean matchesSet = name.length() > 3 && name.startsWith("set") 
                && isStartOfJavaBeanPropertyName(name.charAt(3))
                && !"setString".equals(name) && !"setHash".equals(name) && !"setEquals".equals(name);
        boolean hasOneParam = methodMirror.getParameters().size() == 1;
        boolean hasVoidReturn = (methodMirror.getReturnType().getKind() == TypeKind.VOID);
        return matchesSet && hasOneParam && hasVoidReturn;
    }

    private boolean isHashAttribute(MethodMirror methodMirror) {
        String name = methodMirror.getName();
        boolean matchesName = "hashCode".equals(name);
        boolean hasNoParams = methodMirror.getParameters().size() == 0;
        return matchesName && hasNoParams;
    }
    
    private boolean isStringAttribute(MethodMirror methodMirror) {
        String name = methodMirror.getName();
        boolean matchesName = "toString".equals(name);
        boolean hasNoParams = methodMirror.getParameters().size() == 0;
        return matchesName && hasNoParams;
    }

    private boolean isEqualsMethod(MethodMirror methodMirror) {
        String name = methodMirror.getName();
        if(!"equals".equals(name)
                || methodMirror.getParameters().size() != 1)
            return false;
        VariableMirror param = methodMirror.getParameters().get(0);
        return sameType(param.getType(), OBJECT_TYPE);
    }

    private void setEqualsParameters(Method decl, MethodMirror methodMirror) {
        ParameterList parameters = new ParameterList();
        decl.addParameterList(parameters);
        Parameter parameter = new Parameter();
        Value value = new Value();
        parameter.setModel(value);
        value.setInitializerParameter(parameter);
        value.setUnit(decl.getUnit());
        value.setContainer((Scope) decl);
        parameter.setName("that");
        value.setName("that");
        value.setType(getNonPrimitiveType(getLanguageModule(), CEYLON_OBJECT_TYPE, decl, VarianceLocation.INVARIANT));
        parameter.setDeclaration((Declaration) decl);
        parameters.getParameters().add(parameter);
    }

    private String getJavaAttributeName(String getterName) {
        if (getterName.startsWith("get") || getterName.startsWith("set")) {
            return getJavaBeanName(getterName.substring(3));
        } else if (getterName.startsWith("is")) {
            // Starts with "is"
            return getJavaBeanName(getterName.substring(2));
        } else {
            throw new RuntimeException("Illegal java getter/setter name");
        }
    }

    private String getJavaBeanName(String name) {
        // See https://github.com/ceylon/ceylon-compiler/issues/340
        // make it lowercase until the first non-uppercase
        char[] newName = name.toCharArray();
        for(int i=0;i<newName.length;i++){
            char c = newName[i];
            if(Character.isLowerCase(c)){
                // if we had more than one upper-case, we leave the last uppercase: getURLDecoder -> urlDecoder
                if(i > 1){
                    newName[i-1] = Character.toUpperCase(newName[i-1]);
                }
                break;
            }
            newName[i] = Character.toLowerCase(c);
        }
        return new String(newName);
    }

    private void addValue(ClassOrInterface klass, String ceylonName, FieldMirror fieldMirror, boolean isCeylon) {
        // make sure it's a FieldValue so we can figure it out in the backend
        Value value = new FieldValue(fieldMirror.getName());
        value.setContainer(klass);
        // use the name annotation if present (used by Java arrays)
        String nameAnnotation = getAnnotationStringValue(fieldMirror, CEYLON_NAME_ANNOTATION);
        value.setName(nameAnnotation != null ? nameAnnotation : ceylonName);
        value.setUnit(klass.getUnit());
        value.setShared(fieldMirror.isPublic() || fieldMirror.isProtected() || fieldMirror.isDefaultAccess());
        value.setProtectedVisibility(fieldMirror.isProtected());
        value.setPackageVisibility(fieldMirror.isDefaultAccess());
        value.setStaticallyImportable(fieldMirror.isStatic());
        // field can't be abstract or interface, so not formal
        // can we override fields? good question. Not really, but from an external point of view?
        // FIXME: figure this out: (default)
        // FIXME: for the same reason, can it be an overriding field? (actual)
        value.setVariable(!fieldMirror.isFinal());
        // figure out if it's an enum subtype in a final static field
        if(fieldMirror.getType().getKind() == TypeKind.DECLARED
                && fieldMirror.getType().getDeclaredClass() != null
                && fieldMirror.getType().getDeclaredClass().isEnum()
                && fieldMirror.isFinal()
                && fieldMirror.isStatic())
            value.setEnumValue(true);
        
        ProducedType type = obtainType(fieldMirror.getType(), fieldMirror, klass, Decl.getModuleContainer(klass), VarianceLocation.INVARIANT,
                "field '"+value.getName()+"'", klass);
        value.setType(type);
        value.setUncheckedNullType((!isCeylon && !fieldMirror.getType().isPrimitive()) || isUncheckedNull(fieldMirror));
        type.setRaw(isRaw(Decl.getModuleContainer(klass), fieldMirror.getType()));

        markUnboxed(value, null, fieldMirror.getType());
        markTypeErased(value, fieldMirror, fieldMirror.getType());
        setAnnotations(value, fieldMirror);
        klass.getMembers().add(value);
        DeclarationVisitor.setVisibleScope(value);
    }
    
    private boolean isRaw(Module module, TypeMirror type) {
        // dirty hack to get rid of bug where calling type.isRaw() on a ceylon type we are going to compile would complete() it, which
        // would try to parse its file. For ceylon types we don't need the class file info we can query it
        // See https://github.com/ceylon/ceylon-compiler/issues/1085
        switch(type.getKind()){
        case ARRAY: // arrays are never raw
        case BOOLEAN:
        case BYTE: 
        case CHAR:
        case DOUBLE:
        case ERROR:
        case FLOAT:
        case INT:
        case LONG:
        case NULL:
        case SHORT:
        case TYPEVAR:
        case VOID:
        case WILDCARD:
            return false;
        case DECLARED:
            ClassMirror klass = type.getDeclaredClass();
            if(klass.isJavaSource()){
                // I suppose this should work
                return type.isRaw();
            }
            List<String> path = new LinkedList<String>();
            String pkgName = klass.getPackage().getQualifiedName();
            String unquotedPkgName = unquotePackageName(klass.getPackage());
            String qualifiedName = klass.getQualifiedName();
            String relativeName = pkgName.isEmpty() ? qualifiedName : qualifiedName.substring(pkgName.length()+1);
            for(String name : relativeName.split("[\\$\\.]")){
                if(!name.isEmpty()){
                    path.add(0, klass.getName());
                }
            }
            if(path.size() > 1){
                // find the proper class mirror for the container
                klass = loadClass(module, pkgName, path.get(0));
                if(klass == null)
                    return false;
            }
            if(!path.isEmpty() && klass.isLoadedFromSource()){
                // we need to find its model
                Scope scope = packagesByName.get(cacheKeyByModule(module, unquotedPkgName));
                if(scope == null)
                    return false;
                for(String name : path){
                    Declaration decl = scope.getDirectMember(name, null, false);
                    if(decl == null)
                        return false;
                    // if we get a value, we want its type
                    if(Decl.isValue(decl)
                            && ((Value)decl).getTypeDeclaration().getName().equals(name))
                        decl = ((Value)decl).getTypeDeclaration();
                    if(decl instanceof TypeDeclaration == false)
                        return false;
                    scope = (TypeDeclaration)decl;
                }
                TypeDeclaration typeDecl = (TypeDeclaration) scope;
                return !typeDecl.getTypeParameters().isEmpty() && type.getTypeArguments().isEmpty();
            }
            try{
                return type.isRaw();
            }catch(Exception x){
                // ignore this exception, it's likely to be due to missing module imports and an unknown type and
                // it will be logged somewhere else
                return false;
            }
        default:
            return false;
        }
    }

    private void addValue(ClassOrInterface klass, MethodMirror methodMirror, String methodName, boolean isCeylon) {
        JavaBeanValue value = new JavaBeanValue();
        value.setGetterName(methodMirror.getName());
        value.setContainer(klass);
        value.setUnit(klass.getUnit());
        ProducedType type = null;
        try{
            setMethodOrValueFlags(klass, methodMirror, value, isCeylon);
        }catch(ModelResolutionException x){
            // collect an error in its type
            type = logModelResolutionException(x, klass, "getter '"+methodName+"' (checking if it is an overriding method");
        }
        value.setName(Util.strip(methodName, isCeylon, value.isShared()));

        // do not log an additional error if we had one from checking if it was overriding
        if(type == null)
            type = obtainType(methodMirror.getReturnType(), methodMirror, klass, Decl.getModuleContainer(klass), VarianceLocation.INVARIANT,
                              "getter '"+methodName+"'", klass);
        value.setType(type);
        // special case for hash attributes which we want to pretend are of type long internally
        if(value.isShared() && methodName.equals("hash"))
            type.setUnderlyingType("long");
        value.setUncheckedNullType((!isCeylon && !methodMirror.getReturnType().isPrimitive()) || isUncheckedNull(methodMirror));
        type.setRaw(isRaw(Decl.getModuleContainer(klass), methodMirror.getReturnType()));

        markUnboxed(value, methodMirror, methodMirror.getReturnType());
        markTypeErased(value, methodMirror, methodMirror.getReturnType());
        setAnnotations(value, methodMirror);
        klass.getMembers().add(value);
        DeclarationVisitor.setVisibleScope(value);
    }

    private boolean isUncheckedNull(AnnotatedMirror methodMirror) {
        Boolean unchecked = getAnnotationBooleanValue(methodMirror, CEYLON_TYPE_INFO_ANNOTATION, "uncheckedNull");
        return unchecked != null && unchecked.booleanValue();
    }

    private void setMethodOrValueFlags(ClassOrInterface klass, MethodMirror methodMirror, MethodOrValue decl, boolean isCeylon) {
        decl.setShared(methodMirror.isPublic() || methodMirror.isProtected() || methodMirror.isDefaultAccess());
        decl.setProtectedVisibility(methodMirror.isProtected());
        decl.setPackageVisibility(methodMirror.isDefaultAccess());
        if(decl instanceof Value){
            setValueTransientLateFlags((Value)decl, methodMirror, isCeylon);
        }
        if(// for class members we rely on abstract bit
           (klass instanceof Class 
                   && methodMirror.isAbstract())
           // Java interfaces are formal
           || (klass instanceof Interface
                   && !((LazyInterface)klass).isCeylon())
           // For Ceylon interfaces we rely on annotation
           || methodMirror.getAnnotation(CEYLON_LANGUAGE_FORMAL_ANNOTATION) != null) {
            decl.setFormal(true);
        } else {
            if (// for class members we rely on final/static bits
                (klass instanceof Class
                        && !methodMirror.isFinal() 
                        && !methodMirror.isStatic())
                // Java interfaces are never final
                || (klass instanceof Interface
                        && !((LazyInterface)klass).isCeylon())
                // For Ceylon interfaces we rely on annotation
                || methodMirror.getAnnotation(CEYLON_LANGUAGE_DEFAULT_ANNOTATION) != null){
                decl.setDefault(true);
            }
        }
        decl.setStaticallyImportable(methodMirror.isStatic());
        if(isOverridingMethod(methodMirror)
                // For Ceylon interfaces we rely on annotation
                || (klass instanceof LazyInterface 
                        && ((LazyInterface)klass).isCeylon()
                        && methodMirror.getAnnotation(CEYLON_LANGUAGE_ACTUAL_ANNOTATION) != null)){
            decl.setActual(true);
        }
    }
    
    private void setValueTransientLateFlags(Value decl, MethodMirror methodMirror, boolean isCeylon) {
        if(isCeylon)
            decl.setTransient(methodMirror.getAnnotation(CEYLON_TRANSIENT_ANNOTATION) != null);
        else
            // all Java getters are transient, fields are not
            decl.setTransient(decl instanceof FieldValue == false);
        decl.setLate(methodMirror.getAnnotation(CEYLON_LANGUAGE_LATE_ANNOTATION) != null);
    }

    private void setExtendedType(ClassOrInterface klass, ClassMirror classMirror) {
        // look at its super type
        TypeMirror superClass = classMirror.getSuperclass();
        ProducedType extendedType;
        
        if(klass instanceof Interface){
            // interfaces need to have their superclass set to Object
            if(superClass == null || superClass.getKind() == TypeKind.NONE)
                extendedType = getNonPrimitiveType(getLanguageModule(), CEYLON_OBJECT_TYPE, klass, VarianceLocation.INVARIANT);
            else
                extendedType = getNonPrimitiveType(Decl.getModule(klass), superClass, klass, VarianceLocation.INVARIANT);
        }else if(klass instanceof Class && ((Class) klass).isOverloaded()){
            // if the class is overloaded we already have it stored
            extendedType = klass.getExtendedType();
        }else{
            String className = classMirror.getQualifiedName();
            String superClassName = superClass == null ? null : superClass.getQualifiedName();
            if(className.equals("ceylon.language.Anything")){
                // ceylon.language.Anything has no super type
                extendedType = null;
            }else if(className.equals("java.lang.Object")){
                // we pretend its superclass is something else, but note that in theory we shouldn't 
                // be seeing j.l.Object at all due to unerasure
                extendedType = getNonPrimitiveType(getLanguageModule(), CEYLON_BASIC_TYPE, klass, VarianceLocation.INVARIANT);
            }else{
                // read it from annotation first
                String annotationSuperClassName = getAnnotationStringValue(classMirror, CEYLON_CLASS_ANNOTATION, "extendsType");
                if(annotationSuperClassName != null && !annotationSuperClassName.isEmpty()){
                    extendedType = decodeType(annotationSuperClassName, klass, Decl.getModuleContainer(klass),
                            "extended type");
                }else{
                    // read it from the Java super type
                    // now deal with type erasure, avoid having Object as superclass
                    if("java.lang.Object".equals(superClassName)){
                        extendedType = getNonPrimitiveType(getLanguageModule(), CEYLON_BASIC_TYPE, klass, VarianceLocation.INVARIANT);
                    } else if(superClass != null){
                        try{
                            extendedType = getNonPrimitiveType(Decl.getModule(klass), superClass, klass, VarianceLocation.INVARIANT);
                        }catch(ModelResolutionException x){
                            extendedType = logModelResolutionException(x, klass, "Error while resolving extended type of "+klass.getQualifiedNameString());
                        }
                    }else{
                        // FIXME: should this be UnknownType?
                        extendedType = null;
                    }
                }
            }
        }
        if(extendedType != null)
            klass.setExtendedType(extendedType);
    }

    private void setParameters(Functional decl, MethodMirror methodMirror, boolean isCeylon, Scope container) {
        ParameterList parameters = new ParameterList();
        parameters.setNamedParametersSupported(isCeylon);
        decl.addParameterList(parameters);
        int parameterCount = methodMirror.getParameters().size();
        int parameterIndex = 0;
        
        for(VariableMirror paramMirror : methodMirror.getParameters()){
            // ignore some parameters
            if(paramMirror.getAnnotation(CEYLON_IGNORE_ANNOTATION) != null)
                continue;
            
            boolean isLastParameter = parameterIndex == parameterCount - 1;
            boolean isVariadic = isLastParameter && methodMirror.isVariadic();
            
            String paramName = getAnnotationStringValue(paramMirror, CEYLON_NAME_ANNOTATION);
            // use whatever param name we find as default
            if(paramName == null)
                paramName = paramMirror.getName();
            
            Parameter parameter = new Parameter();
            parameter.setName(paramName);
            
            TypeMirror typeMirror = paramMirror.getType();
            Module module = Decl.getModuleContainer((Scope) decl);

            ProducedType type;
            if(isVariadic){
                // possibly make it optional
                TypeMirror variadicType = typeMirror.getComponentType();
                // we pretend it's toplevel because we want to get magic string conversion for variadic methods
                type = obtainType(Decl.getModuleContainer((Scope)decl), variadicType, (Scope)decl, TypeLocation.TOPLEVEL, VarianceLocation.CONTRAVARIANT);
                if(!isCeylon && !variadicType.isPrimitive()){
                    // Java parameters are all optional unless primitives
                    ProducedType optionalType = getOptionalType(type, module);
                    optionalType.setUnderlyingType(type.getUnderlyingType());
                    type = optionalType;
                }
                // turn it into a Sequential<T>
                type = typeFactory.getSequentialType(type);
            }else{
                type = obtainType(typeMirror, paramMirror, (Scope) decl, module, VarianceLocation.CONTRAVARIANT,
                        "parameter '"+paramName+"' of method '"+methodMirror.getName()+"'", (Declaration)decl);
                // variadic params may technically be null in Java, but it Ceylon sequenced params may not
                // so it breaks the typechecker logic for handling them, and it will always be a case of bugs
                // in the java side so let's not allow this
                if(!isCeylon && !typeMirror.isPrimitive()){
                    // Java parameters are all optional unless primitives
                    ProducedType optionalType = getOptionalType(type, module);
                    optionalType.setUnderlyingType(type.getUnderlyingType());
                    type = optionalType;
                }
            }
            
            MethodOrValue value = null;
            if (isCeylon && decl instanceof Class){
                // For a functional parameter to a class, we can just lookup the member
                value = (MethodOrValue)((Class)decl).getDirectMember(paramName, null, false);
            } 
            if (value == null) {
                // So either decl is not a Class, 
                // or the method or value member of decl is not shared
                AnnotationMirror functionalParameterAnnotation = paramMirror.getAnnotation(CEYLON_FUNCTIONAL_PARAMETER_ANNOTATION);
                if (functionalParameterAnnotation != null) {
                    // A functional parameter to a method
                    Method method = loadFunctionalParameter((Declaration)decl, paramName, type, (String)functionalParameterAnnotation.getValue());
                    value = method;
                    parameter.setDeclaredAnything(method.isDeclaredVoid());
                } else {
                    // A value parameter to a method
                    value = new Value();
                    value.setType(type);
                }
                
                value.setContainer((Scope) decl);
                DeclarationVisitor.setVisibleScope(value);
                value.setUnit(((Element)decl).getUnit());
                value.setName(paramName);
            }
            value.setInitializerParameter(parameter);
            parameter.setModel(value);

            if(paramMirror.getAnnotation(CEYLON_SEQUENCED_ANNOTATION) != null
                    || isVariadic)
                parameter.setSequenced(true);
            if(paramMirror.getAnnotation(CEYLON_DEFAULTED_ANNOTATION) != null)
                parameter.setDefaulted(true);
            if (parameter.isSequenced() &&
                    // FIXME: store info in Sequenced
                    typeFactory.isNonemptyIterableType(parameter.getType())) {
                parameter.setAtLeastOne(true);
            }
            // if it's variadic, consider the array element type (T[] == T...) for boxing rules
            markUnboxed(value, null, isVariadic ? 
                    paramMirror.getType().getComponentType()
                    : paramMirror.getType());
            parameter.setDeclaration((Declaration) decl);
            setAnnotations(value, paramMirror);
            parameters.getParameters().add(parameter);
            
            parameterIndex++;
        }
    }
    private Method loadFunctionalParameter(Declaration decl, String paramName, ProducedType type, String parameterNames) {
        Method method = new Method();
        method.setName(paramName);
        method.setUnit(decl.getUnit());
        if (parameterNames == null || parameterNames.isEmpty()) {
            // This branch is broken, but it deals with old code which lacked
            // the encoding of parameter names of functional parameters, so we'll keep it until 1.2
            method.setType(getSimpleCallableReturnType(type));
            ParameterList pl = new ParameterList();
            int count = 0;
            for (ProducedType pt : getSimpleCallableArgumentTypes(type)) {
                Parameter p = new Parameter();
                Value v = new Value();
                String name = "arg" + count++;
                p.setName(name);
                v.setName(name);
                v.setType(pt);
                p.setModel(v);
                pl.getParameters().add(p);
            }
            method.addParameterList(pl);
        } else {
            try {
                parameterNameParser.parse(parameterNames, type, method);
            } catch(Exception x){
                logError(x.getClass().getSimpleName() + " while parsing parameter names of "+decl+": " + x.getMessage());
                return method;
            }
        }
        return method;
    }

    List<ProducedType> getSimpleCallableArgumentTypes(ProducedType type) {
        if(type != null
                && type.getDeclaration() instanceof ClassOrInterface
                && type.getDeclaration().getQualifiedNameString().equals(CEYLON_LANGUAGE_CALLABLE_TYPE_NAME)
                && type.getTypeArgumentList().size() >= 2)
            return flattenCallableTupleType(type.getTypeArgumentList().get(1));
        return Collections.emptyList();
    }

    List<ProducedType> flattenCallableTupleType(ProducedType tupleType) {
        if(tupleType != null
                && tupleType.getDeclaration() instanceof ClassOrInterface){
            String declName = tupleType.getDeclaration().getQualifiedNameString();
            if(declName.equals(CEYLON_LANGUAGE_TUPLE_TYPE_NAME)){
                List<ProducedType> tal = tupleType.getTypeArgumentList();
                if(tal.size() >= 3){
                    List<ProducedType> ret = flattenCallableTupleType(tal.get(2));
                    ret.add(0, tal.get(1));
                    return ret;
                }
            }else if(declName.equals(CEYLON_LANGUAGE_EMPTY_TYPE_NAME)){
                return new LinkedList<ProducedType>();
            }else if(declName.equals(CEYLON_LANGUAGE_SEQUENTIAL_TYPE_NAME)){
                LinkedList<ProducedType> ret = new LinkedList<ProducedType>();
                ret.add(tupleType);
                return ret;
            }else if(declName.equals(CEYLON_LANGUAGE_SEQUENCE_TYPE_NAME)){
                LinkedList<ProducedType> ret = new LinkedList<ProducedType>();
                ret.add(tupleType);
                return ret;
            }
        }
        return Collections.emptyList();
    }
    
    ProducedType getSimpleCallableReturnType(ProducedType type) {
        if(type != null
                && type.getDeclaration() instanceof ClassOrInterface
                && type.getDeclaration().getQualifiedNameString().equals(CEYLON_LANGUAGE_CALLABLE_TYPE_NAME)
                && !type.getTypeArgumentList().isEmpty())
            return type.getTypeArgumentList().get(0);
        return newUnknownType();
    }
    
    private ProducedType getOptionalType(ProducedType type, Module moduleScope) {
        // we do not use Unit.getOptionalType because it causes lots of lazy loading that ultimately triggers the typechecker's
        // infinite recursion loop
        List<ProducedType> list = new ArrayList<ProducedType>(2);
        list.add(typeFactory.getNullDeclaration().getType());
        list.add(type);
        UnionType ut = new UnionType(getUnitForModule(moduleScope));
        ut.setCaseTypes(list);
        return ut.getType();
    }
    
    private ProducedType logModelResolutionError(Scope container, String message) {
        return logModelResolutionException((String)null, container, message);
    }

    private ProducedType logModelResolutionException(ModelResolutionException x, Scope container, String message) {
        return logModelResolutionException(x.getMessage(), container, message);
    }
    
    private ProducedType logModelResolutionException(final String exceptionMessage, Scope container, final String message) {
        final Module module = Decl.getModuleContainer(container);
        Runnable errorReporter;
        if(module != null && !module.isDefault()){
            final StringBuilder sb = new StringBuilder();
            sb.append("Error while loading the ").append(module.getNameAsString()).append("/").append(module.getVersion());
            sb.append(" module:\n ");
            sb.append(message);
            
            if(exceptionMessage != null)
                sb.append(":\n ").append(exceptionMessage);
            errorReporter = new Runnable(){
                public void run(){
                    moduleManager.attachErrorToOriginalModuleImport(module, sb.toString());
                }
            };
        }else if(exceptionMessage == null){
            errorReporter = new Runnable(){
                public void run(){
                    logError(message);
                } 
            };
        }else{
            errorReporter = new Runnable(){
                public void run(){
                    logError(message+": "+exceptionMessage);
                } 
            };
        }
        UnknownType ret = new UnknownType(typeFactory);
        ret.setErrorReporter(errorReporter);
        return ret.getType();
    }

    private void markTypeErased(TypedDeclaration decl, AnnotatedMirror typedMirror, TypeMirror type) {
        if (getAnnotationBooleanValue(typedMirror, CEYLON_TYPE_INFO_ANNOTATION, "erased") == Boolean.TRUE) {
            decl.setTypeErased(true);
        } else {
            decl.setTypeErased(sameType(type, OBJECT_TYPE));
        }
        if(hasTypeParameterWithConstraints(type))
            decl.setUntrustedType(true);
    }
    
    private void markDeclaredVoid(Method decl, MethodMirror methodMirror) {
        if (methodMirror.isDeclaredVoid() || 
                getAnnotationBooleanValue(methodMirror, CEYLON_TYPE_INFO_ANNOTATION, "declaredVoid") == Boolean.TRUE) {
            decl.setDeclaredVoid(true);
        }
    }

    private boolean hasTypeParameterWithConstraints(TypeMirror type) {
        switch(type.getKind()){
        case BOOLEAN:
        case BYTE:
        case CHAR:
        case DOUBLE:
        case FLOAT:
        case INT:
        case LONG:
        case SHORT:
        case VOID:
        case WILDCARD:
            return false;
        case ARRAY:
            return hasTypeParameterWithConstraints(type.getComponentType());
        case DECLARED:
            for(TypeMirror ta : type.getTypeArguments()){
                if(hasTypeParameterWithConstraints(ta))
                    return true;
            }
            return false;
        case TYPEVAR:
            TypeParameterMirror typeParameter = type.getTypeParameter();
            return typeParameter != null && hasNonErasedBounds(typeParameter);
        default:
            return false;
        }
    }
    
    private void markUnboxed(TypedDeclaration decl, MethodMirror methodMirror, TypeMirror type) {
        boolean unboxed = false;
        if(type.isPrimitive() 
                || type.getKind() == TypeKind.ARRAY
                || sameType(type, STRING_TYPE)
                || (methodMirror != null && methodMirror.isDeclaredVoid())) {
            unboxed = true;
        }
        decl.setUnboxed(unboxed);
    }

    @Override
    public synchronized void complete(LazyValue value) {
        timer.startIgnore(TIMER_MODEL_LOADER_CATEGORY);
        try{
            MethodMirror meth = null;
            for (MethodMirror m : value.classMirror.getDirectMethods()) {
                // Do not skip members marked with @Ignore, because the getter is supposed to be ignored

                if (m.getName().equals(
                        Naming.getGetterName(value))
                        && m.isStatic() && m.getParameters().size() == 0) {
                    meth = m;
                }
                if (m.getName().equals(
                        Naming.getSetterName(value))
                        && m.isStatic() && m.getParameters().size() == 1) {
                    value.setVariable(true);
                }
            }
            if(meth == null || meth.getReturnType() == null){
                value.setType(logModelResolutionError(value.getContainer(), "Error while resolving toplevel attribute "+value.getQualifiedNameString()+": getter method missing"));
                return;
            }

            value.setType(obtainType(meth.getReturnType(), meth, null, Decl.getModuleContainer(value.getContainer()), VarianceLocation.INVARIANT,
                          "toplevel attribute", value));

            setValueTransientLateFlags(value, meth, true);
            setAnnotations(value, meth);
            markUnboxed(value, meth, meth.getReturnType());
        }finally{
            timer.stopIgnore(TIMER_MODEL_LOADER_CATEGORY);
        }
    }

    @Override
    public synchronized void complete(LazyMethod method) {
        timer.startIgnore(TIMER_MODEL_LOADER_CATEGORY);
        try{
            MethodMirror meth = null;
            String lookupName = method.getName();
            for(MethodMirror m : method.classMirror.getDirectMethods()){
                // We skip members marked with @Ignore
                if(m.getAnnotation(CEYLON_IGNORE_ANNOTATION) != null)
                    continue;

                if(Util.strip(m.getName()).equals(lookupName)){
                    meth = m;
                    break;
                }
            }
            if(meth == null || meth.getReturnType() == null){
                method.setType(logModelResolutionError(method.getContainer(), "Error while resolving toplevel method "+method.getQualifiedNameString()+": static method missing"));
                return;
            }
            if(!meth.isStatic()){
                method.setType(logModelResolutionError(method.getContainer(), "Error while resolving toplevel method "+method.getQualifiedNameString()+": method is not static"));
                return;
            }

            // save the method name
            method.setRealMethodName(meth.getName());
            
            // save the method
            method.setMethodMirror(meth);
            
            // type params first
            setTypeParameters(method, meth);

            // now its parameters
            setParameters(method, meth, true /* toplevel methods are always Ceylon */, method);

            method.setType(obtainType(meth.getReturnType(), meth, method, Decl.getModuleContainer(method), VarianceLocation.COVARIANT,
                               "toplevel method", method));
            method.setDeclaredVoid(meth.isDeclaredVoid());
            markDeclaredVoid(method, meth);
            markUnboxed(method, meth, meth.getReturnType());
            markTypeErased(method, meth, meth.getReturnType());

            method.setAnnotation(meth.getAnnotation(CEYLON_LANGUAGE_ANNOTATION_ANNOTATION) != null);
            setAnnotations(method, meth);
            
            setAnnotationConstructor(method, meth);
        }finally{
            timer.stopIgnore(TIMER_MODEL_LOADER_CATEGORY);
        }
     }

    private void setAnnotationConstructor(LazyMethod method, MethodMirror meth) {
        AnnotationInvocation ai = loadAnnotationInvocation(method, method.classMirror, meth);
        if (ai != null) {
            loadAnnotationConstructorDefaultedParameters(method, meth, ai);
            ai.setConstructorDeclaration(method);
            method.setAnnotationConstructor(ai);
        }
    }
    
    private AnnotationInvocation loadAnnotationInvocation(
            LazyMethod method, 
            AnnotatedMirror annoInstMirror, MethodMirror meth) {
        AnnotationInvocation ai = null;
        AnnotationMirror annotationInvocationAnnotation = null;
        
        List<AnnotationMirror> annotationTree = getAnnotationArrayValue(annoInstMirror, CEYLON_ANNOTATION_INSTANTIATION_TREE_ANNOTATION, "value");
        if (annotationTree != null
                && !annotationTree.isEmpty()) {
            annotationInvocationAnnotation = annotationTree.get(0);
        } else {
            annotationInvocationAnnotation = annoInstMirror.getAnnotation(CEYLON_ANNOTATION_INSTANTIATION_ANNOTATION);
        }
        //stringValueAnnotation = annoInstMirror.getAnnotation(CEYLON_STRING_VALUE_ANNOTATION);
        if (annotationInvocationAnnotation != null) {
            ai = new AnnotationInvocation();
            setPrimaryFromAnnotationInvocationAnnotation(annotationInvocationAnnotation, ai);
            loadAnnotationInvocationArguments(new ArrayList<AnnotationFieldName>(2), method, ai, annotationInvocationAnnotation, annotationTree, annoInstMirror);
        }
        return ai;
    }
    
    private void loadAnnotationInvocationArguments(
            List<AnnotationFieldName> path,
            LazyMethod method,
            AnnotationInvocation ai, AnnotationMirror annotationInvocationAnnotation,
            List<AnnotationMirror> annotationTree, 
            AnnotatedMirror dpm) {
        @SuppressWarnings("unchecked")
        List<Short> argumentCodes = (List<Short>)annotationInvocationAnnotation.getValue(CEYLON_ANNOTATION_INSTANTIATION_ARGUMENTS_MEMBER);
        if(argumentCodes != null){
            for (int ii = 0; ii < argumentCodes.size(); ii++) {
                short code = argumentCodes.get(ii);
                AnnotationArgument argument = new AnnotationArgument();
                Parameter classParameter = ai.getParameters().get(ii);
                argument.setParameter(classParameter);
                path.add(argument);
                argument.setTerm(loadAnnotationArgumentTerm(path, method, ai, classParameter, annotationTree, dpm, code));
                path.remove(path.size()-1);
                ai.getAnnotationArguments().add(argument);
            }
        }
    }
    
    public AnnotationTerm decode(Module moduleScope, List<Parameter> sourceParameters, AnnotationInvocation info, 
            Parameter parameter, 
            AnnotatedMirror dpm, List<AnnotationFieldName> path, int code) {
        AnnotationTerm result;
        if (code == Short.MIN_VALUE) {
            return findLiteralAnnotationTerm(moduleScope, path, parameter, dpm);
        } else if (code < 0) {
            InvocationAnnotationTerm invocation = new InvocationAnnotationTerm();
            result = invocation;
        } else if (code >= 0 && code < 512) {
            ParameterAnnotationTerm parameterArgument = new ParameterAnnotationTerm();
            boolean spread = false;
            if (code >= 256) {
                spread = true;
                code-=256;
            }
            
            parameterArgument.setSpread(spread);
            Parameter sourceParameter = sourceParameters.get(code);
            parameterArgument.setSourceParameter(sourceParameter);
            //result.setTargetParameter(sourceParameter);
            result = parameterArgument;
        } else {
            throw new RuntimeException();
        }
        return result;
    }
    
    private AnnotationTerm loadAnnotationArgumentTerm(
            List<AnnotationFieldName> path,
            LazyMethod method,
            AnnotationInvocation ai, Parameter parameter,
            List<AnnotationMirror> annotationTree,
            AnnotatedMirror dpm,
            short code) {
        if (code < 0 && code != Short.MIN_VALUE) {
            AnnotationMirror i = annotationTree.get(-code);
            AnnotationInvocation nested = new AnnotationInvocation();
            setPrimaryFromAnnotationInvocationAnnotation(i, nested);
            loadAnnotationInvocationArguments(path, method, nested, i, annotationTree, dpm);
            InvocationAnnotationTerm term = new InvocationAnnotationTerm();
            term.setInstantiation(nested);
            return term;
        } else {
            AnnotationTerm term = decode(Decl.getModuleContainer(method), method.getParameterLists().get(0).getParameters(), ai, parameter, dpm, path, code);
            return term;
        }
    }
    private void setPrimaryFromAnnotationInvocationAnnotation(AnnotationMirror annotationInvocationAnnotation,
            AnnotationInvocation ai) {
        TypeMirror annotationType = (TypeMirror)annotationInvocationAnnotation.getValue(CEYLON_ANNOTATION_INSTANTIATION_ANNOTATION_MEMBER);
        ClassMirror annotationClassMirror = annotationType.getDeclaredClass();            
        if (annotationClassMirror.getAnnotation(CEYLON_METHOD_ANNOTATION) != null) {
            ai.setPrimary((Method)convertToDeclaration(annotationClassMirror, DeclarationType.VALUE));
        } else {
            ai.setPrimary((Class)convertToDeclaration(annotationClassMirror, DeclarationType.TYPE));
        }
    }
    
    private void loadAnnotationConstructorDefaultedParameters(
            LazyMethod method, MethodMirror meth, AnnotationInvocation ai) {
        for (Parameter ctorParam : method.getParameterLists().get(0).getParameters()) {
            AnnotationConstructorParameter acp = new AnnotationConstructorParameter();
            acp.setParameter(ctorParam);
            if (ctorParam.isDefaulted()) {
                acp.setDefaultArgument(
                        loadAnnotationConstructorDefaultedParameter(method, meth, ctorParam, acp));
            }
            ai.getConstructorParameters().add(acp);
        }
    }
    private AnnotationTerm loadAnnotationConstructorDefaultedParameter(
            LazyMethod method, 
            MethodMirror meth,
            Parameter ctorParam, AnnotationConstructorParameter acp) {
        // Find the method mirror for the DPM
        for (MethodMirror mm : method.classMirror.getDirectMethods()) {
            if (mm.getName().equals(Naming.getDefaultedParamMethodName(method, ctorParam))) {
                // Create the appropriate AnnotationTerm
                if (mm.getAnnotation(CEYLON_ANNOTATION_INSTANTIATION_ANNOTATION) != null) {
                    // If the DPM has a @AnnotationInstantiation 
                    // then it must be an invocation term so recurse
                    InvocationAnnotationTerm invocationTerm = new InvocationAnnotationTerm();
                    invocationTerm.setInstantiation(loadAnnotationInvocation(method, mm, meth));
                    return invocationTerm;
                } else {
                    return loadLiteralAnnotationTerm(method, ctorParam, mm);
                }
            }
        }
        return null;
    }
    /** 
     * Loads a LiteralAnnotationTerm according to the presence of 
     * <ul>
     * <li>{@code @StringValue} <li>{@code @IntegerValue} <li>etc
     * </ul>
     * @param ctorParam 
     *  
     */
    private LiteralAnnotationTerm loadLiteralAnnotationTerm(LazyMethod method, Parameter parameter, AnnotatedMirror mm) {
        // FIXME: store iterable info somewhere else
        boolean singleValue = !typeFactory.isIterableType(parameter.getType())
            || typeFactory.getStringDeclaration().equals(parameter.getType().getDeclaration());
        AnnotationMirror valueAnnotation = mm.getAnnotation(CEYLON_STRING_VALUE_ANNOTATION);
        if (valueAnnotation != null) {
            return readStringValuesAnnotation(valueAnnotation, singleValue);
        }
        valueAnnotation = mm.getAnnotation(CEYLON_INTEGER_VALUE_ANNOTATION);
        if (valueAnnotation != null) {
            return readIntegerValuesAnnotation(valueAnnotation, singleValue);
        }
        valueAnnotation = mm.getAnnotation(CEYLON_BOOLEAN_VALUE_ANNOTATION);
        if (valueAnnotation != null) { 
            return readBooleanValuesAnnotation(valueAnnotation, singleValue);
        }
        valueAnnotation = mm.getAnnotation(CEYLON_DECLARATION_VALUE_ANNOTATION);
        if (valueAnnotation != null) {
            return readDeclarationValuesAnnotation(valueAnnotation, singleValue);
        }
        valueAnnotation = mm.getAnnotation(CEYLON_OBJECT_VALUE_ANNOTATION);
        if (valueAnnotation != null) {
            return readObjectValuesAnnotation(Decl.getModuleContainer(method), valueAnnotation, singleValue);
        }
        valueAnnotation = mm.getAnnotation(CEYLON_CHARACTER_VALUE_ANNOTATION);
        if (valueAnnotation != null) {
            return readCharacterValuesAnnotation(valueAnnotation, singleValue);
        } 
        valueAnnotation = mm.getAnnotation(CEYLON_FLOAT_VALUE_ANNOTATION);
        if (valueAnnotation != null) {
            return readFloatValuesAnnotation(valueAnnotation, singleValue);
        }
        return null;
    }
    /**
     * Searches the {@code @*Exprs} for one containing a {@code @*Value} 
     * whose {@code name} matches the given namePath returning the first 
     * match, or null.
     */
    private LiteralAnnotationTerm findLiteralAnnotationTerm(Module moduleScope, List<AnnotationFieldName> namePath, Parameter parameter, AnnotatedMirror mm) {
        // FIXME: store info somewhere else
        boolean singeValue = !typeFactory.isIterableType(parameter.getType())
            || typeFactory.getStringDeclaration().equals(parameter.getType().getDeclaration());
        final String name = Naming.getAnnotationFieldName(namePath);
        AnnotationMirror exprsAnnotation = mm.getAnnotation(CEYLON_STRING_EXPRS_ANNOTATION);
        if (exprsAnnotation != null) {
            for (AnnotationMirror valueAnnotation : getAnnotationAnnoValues(exprsAnnotation, "value")) {
                String path = (String)valueAnnotation.getValue("name");
                if (name.equals(path)) {
                    return readStringValuesAnnotation(valueAnnotation, singeValue);
                }
            }
        }
        exprsAnnotation = mm.getAnnotation(CEYLON_INTEGER_EXPRS_ANNOTATION);
        if (exprsAnnotation != null) {
            for (AnnotationMirror valueAnnotation : getAnnotationAnnoValues(exprsAnnotation, "value")) {
                String path = (String)valueAnnotation.getValue("name");
                if (name.equals(path)) {
                    return readIntegerValuesAnnotation(valueAnnotation, singeValue);
                }
            }
        }
        exprsAnnotation = mm.getAnnotation(CEYLON_BOOLEAN_EXPRS_ANNOTATION);
        if (exprsAnnotation != null) {
            for (AnnotationMirror valueAnnotation : getAnnotationAnnoValues(exprsAnnotation, "value")) {
                String path = (String)valueAnnotation.getValue("name");
                if (name.equals(path)) {
                    return readBooleanValuesAnnotation(valueAnnotation, singeValue);
                }
            }
        }
        exprsAnnotation = mm.getAnnotation(CEYLON_DECLARATION_EXPRS_ANNOTATION);
        if (exprsAnnotation != null) {
            for (AnnotationMirror valueAnnotation : getAnnotationAnnoValues(exprsAnnotation, "value")) {
                String path = (String)valueAnnotation.getValue("name");
                if (name.equals(path)) {
                    return readDeclarationValuesAnnotation(valueAnnotation, singeValue);
                }
            }
        }
        exprsAnnotation = mm.getAnnotation(CEYLON_OBJECT_EXPRS_ANNOTATION);
        if (exprsAnnotation != null) {
            for (AnnotationMirror valueAnnotation : getAnnotationAnnoValues(exprsAnnotation, "value")) {
                String path = (String)valueAnnotation.getValue("name");
                if (name.equals(path)) {
                    return readObjectValuesAnnotation(moduleScope, valueAnnotation, singeValue);
                }
            }
        }
        exprsAnnotation = mm.getAnnotation(CEYLON_CHARACTER_EXPRS_ANNOTATION);
        if (exprsAnnotation != null) {
            for (AnnotationMirror valueAnnotation : getAnnotationAnnoValues(exprsAnnotation, "value")) {
                String path = (String)valueAnnotation.getValue("name");
                if (name.equals(path)) {
                    return readCharacterValuesAnnotation(valueAnnotation, singeValue);
                }
            }
        }
        exprsAnnotation = mm.getAnnotation(CEYLON_FLOAT_EXPRS_ANNOTATION);
        if (exprsAnnotation != null) {
            for (AnnotationMirror valueAnnotation : getAnnotationAnnoValues(exprsAnnotation, "value")) {
                String path = (String)valueAnnotation.getValue("name");
                if (name.equals(path)) {
                    return readFloatValuesAnnotation(valueAnnotation, singeValue);
                }
            }
        }
        
        return null;
    }
    private LiteralAnnotationTerm readObjectValuesAnnotation(
            Module moduleScope,
            AnnotationMirror valueAnnotation, boolean singleValue) {
        if (singleValue) {
            TypeMirror klass = getAnnotationClassValues(valueAnnotation, "value").get(0);
            ProducedType type = obtainType(moduleScope, klass, null, null, null);
            ObjectLiteralAnnotationTerm term = new ObjectLiteralAnnotationTerm(type);
            return term;
        } else {
            CollectionLiteralAnnotationTerm result = new CollectionLiteralAnnotationTerm(ObjectLiteralAnnotationTerm.FACTORY);
            for (TypeMirror klass : getAnnotationClassValues(valueAnnotation, "value")) {
                ProducedType type = obtainType(moduleScope, klass, null, null, null);
                result.addElement(new ObjectLiteralAnnotationTerm(type));
            }
            return result;
        }
    }
    private LiteralAnnotationTerm readStringValuesAnnotation(
            AnnotationMirror valueAnnotation, boolean singleValue) {
        if (singleValue) {
            String value = getAnnotationStringValues(valueAnnotation, "value").get(0);
            StringLiteralAnnotationTerm term = new StringLiteralAnnotationTerm(value);
            return term;
        } else {
            CollectionLiteralAnnotationTerm result = new CollectionLiteralAnnotationTerm(StringLiteralAnnotationTerm.FACTORY);
            for (String value : getAnnotationStringValues(valueAnnotation, "value")) {
                result.addElement(new StringLiteralAnnotationTerm(value));
            }
            return result;
        }
    }
    private LiteralAnnotationTerm readIntegerValuesAnnotation(
            AnnotationMirror valueAnnotation, boolean singleValue) {
        if (singleValue) {
            Long value = getAnnotationLongValues(valueAnnotation, "value").get(0);
            IntegerLiteralAnnotationTerm term = new IntegerLiteralAnnotationTerm(value);
            return term;
        } else {
            CollectionLiteralAnnotationTerm result = new CollectionLiteralAnnotationTerm(IntegerLiteralAnnotationTerm.FACTORY);
            for (Long value : getAnnotationLongValues(valueAnnotation, "value")) {
                result.addElement(new IntegerLiteralAnnotationTerm(value));
            }
            return result;
        }
    }
    private LiteralAnnotationTerm readCharacterValuesAnnotation(
            AnnotationMirror valueAnnotation, boolean singleValue) {
        if (singleValue) {
            Integer value = getAnnotationIntegerValues(valueAnnotation, "value").get(0);
            CharacterLiteralAnnotationTerm term = new CharacterLiteralAnnotationTerm(value);
            return term;
        } else {
            CollectionLiteralAnnotationTerm result = new CollectionLiteralAnnotationTerm(CharacterLiteralAnnotationTerm.FACTORY);
            for (Integer value : getAnnotationIntegerValues(valueAnnotation, "value")) {
                result.addElement(new CharacterLiteralAnnotationTerm(value));
            }
            return result;
        }
    }
    private LiteralAnnotationTerm readFloatValuesAnnotation(
            AnnotationMirror valueAnnotation, boolean singleValue) {
        if (singleValue) {
            Double value = getAnnotationDoubleValues(valueAnnotation, "value").get(0);
            FloatLiteralAnnotationTerm term = new FloatLiteralAnnotationTerm(value);
            return term;
        } else {
            CollectionLiteralAnnotationTerm result = new CollectionLiteralAnnotationTerm(FloatLiteralAnnotationTerm.FACTORY);
            for (Double value : getAnnotationDoubleValues(valueAnnotation, "value")) {
                result.addElement(new FloatLiteralAnnotationTerm(value));
            }
            return result;
        }
    }
    private LiteralAnnotationTerm readBooleanValuesAnnotation(
            AnnotationMirror valueAnnotation, boolean singleValue) {
        if (singleValue) {
            boolean value = getAnnotationBooleanValues(valueAnnotation, "value").get(0);
            BooleanLiteralAnnotationTerm term = new BooleanLiteralAnnotationTerm(value);
            return term;
        } else {
            CollectionLiteralAnnotationTerm result = new CollectionLiteralAnnotationTerm(BooleanLiteralAnnotationTerm.FACTORY);
            for (Boolean value : getAnnotationBooleanValues(valueAnnotation, "value")) {
                result.addElement(new BooleanLiteralAnnotationTerm(value));
            }
            return result;
        }
    }
    private LiteralAnnotationTerm readDeclarationValuesAnnotation(
            AnnotationMirror valueAnnotation, boolean singleValue) {
        if (singleValue) {
            String value = getAnnotationStringValues(valueAnnotation, "value").get(0);
            DeclarationLiteralAnnotationTerm term = new DeclarationLiteralAnnotationTerm(value);
            return term;
        } else {
            CollectionLiteralAnnotationTerm result = new CollectionLiteralAnnotationTerm(DeclarationLiteralAnnotationTerm.FACTORY);
            for (String value : getAnnotationStringValues(valueAnnotation, "value")) {
                result.addElement(new DeclarationLiteralAnnotationTerm(value));
            }
            return result;
        }
    }
    
    //
    // Satisfied Types
    
    private List<String> getSatisfiedTypesFromAnnotations(AnnotatedMirror symbol) {
        return getAnnotationArrayValue(symbol, CEYLON_SATISFIED_TYPES_ANNOTATION);
    }
    
    private void setSatisfiedTypes(ClassOrInterface klass, ClassMirror classMirror) {
        List<String> satisfiedTypes = getSatisfiedTypesFromAnnotations(classMirror);
        if(satisfiedTypes != null){
            klass.getSatisfiedTypes().addAll(getTypesList(satisfiedTypes, klass, Decl.getModuleContainer(klass), "satisfied types", klass.getQualifiedNameString()));
        }else{
            for(TypeMirror iface : classMirror.getInterfaces()){
                // ignore ReifiedType interfaces
                if(sameType(iface, CEYLON_REIFIED_TYPE_TYPE))
                    continue;
                try{
                    klass.getSatisfiedTypes().add(getNonPrimitiveType(Decl.getModule(klass), iface, klass, VarianceLocation.INVARIANT));
                }catch(ModelResolutionException x){
                    String classPackageName = unquotePackageName(classMirror.getPackage());
                    if(JDKUtils.isJDKAnyPackage(classPackageName)){
                        if(iface.getKind() == TypeKind.DECLARED){
                            // check if it's a JDK thing
                            ClassMirror ifaceClass = iface.getDeclaredClass();
                            String ifacePackageName = unquotePackageName(ifaceClass.getPackage());
                            if(JDKUtils.isOracleJDKAnyPackage(ifacePackageName)){
                                // just log and ignore it
                                logMissingOracleType(iface.getQualifiedName());
                                continue;
                            }
                        }
                    }
                }
            }
        }
    }

    //
    // Case Types
    
    private List<String> getCaseTypesFromAnnotations(AnnotatedMirror symbol) {
        return getAnnotationArrayValue(symbol, CEYLON_CASE_TYPES_ANNOTATION);
    }
    
    private String getSelfTypeFromAnnotations(AnnotatedMirror symbol) {
        return getAnnotationStringValue(symbol, CEYLON_CASE_TYPES_ANNOTATION, "of");
    }

    private void setCaseTypes(ClassOrInterface klass, ClassMirror classMirror) {
        String selfType = getSelfTypeFromAnnotations(classMirror);
        Module moduleScope = Decl.getModuleContainer(klass);
        if(selfType != null && !selfType.isEmpty()){
            ProducedType type = decodeType(selfType, klass, moduleScope, "self type");
            if(!(type.getDeclaration() instanceof TypeParameter)){
                logError("Invalid type signature for self type of "+klass.getQualifiedNameString()+": "+selfType+" is not a type parameter");
            }else{
                klass.setSelfType(type);
                List<ProducedType> caseTypes = new LinkedList<ProducedType>();
                caseTypes.add(type);
                klass.setCaseTypes(caseTypes);
            }
        } else {
            List<String> caseTypes = getCaseTypesFromAnnotations(classMirror);
            if(caseTypes != null && !caseTypes.isEmpty()){
                klass.setCaseTypes(getTypesList(caseTypes, klass, moduleScope, "case types", klass.getQualifiedNameString()));
            }
        }
    }

    private List<ProducedType> getTypesList(List<String> caseTypes, Scope scope, Module moduleScope, String targetType, String targetName) {
        List<ProducedType> producedTypes = new LinkedList<ProducedType>();
        for(String type : caseTypes){
            producedTypes.add(decodeType(type, scope, moduleScope, targetType));
        }
        return producedTypes;
    }

    //
    // Type parameters loading

    @SuppressWarnings("unchecked")
    private List<AnnotationMirror> getTypeParametersFromAnnotations(AnnotatedMirror symbol) {
        return (List<AnnotationMirror>) getAnnotationValue(symbol, CEYLON_TYPE_PARAMETERS);
    }

    // from our annotation
    @SuppressWarnings("deprecation")
    private void setTypeParametersFromAnnotations(Scope scope, List<TypeParameter> params, AnnotatedMirror mirror, 
            List<AnnotationMirror> typeParameterAnnotations, List<TypeParameterMirror> typeParameterMirrors) {
        // We must first add every type param, before we resolve the bounds, which can
        // refer to type params.
        String selfTypeName = getSelfTypeFromAnnotations(mirror);
        int i=0;
        for(AnnotationMirror typeParamAnnotation : typeParameterAnnotations){
            TypeParameter param = new TypeParameter();
            param.setUnit(((Element)scope).getUnit());
            param.setContainer(scope);
            param.setDeclaration((Declaration) scope);
            // let's not trigger the lazy-loading if we're completing a LazyClass/LazyInterface
            if(scope instanceof LazyContainer)
                ((LazyContainer)scope).addMember(param);
            else // must be a method
                scope.getMembers().add(param);
            param.setName((String)typeParamAnnotation.getValue("value"));
            param.setExtendedType(typeFactory.getAnythingDeclaration().getType());
            if(i < typeParameterMirrors.size()){
                TypeParameterMirror typeParameterMirror = typeParameterMirrors.get(i);
                param.setNonErasedBounds(hasNonErasedBounds(typeParameterMirror));
            }
            
            String varianceName = (String) typeParamAnnotation.getValue("variance");
            if(varianceName != null){
                if(varianceName.equals("IN")){
                    param.setContravariant(true);
                }else if(varianceName.equals("OUT"))
                    param.setCovariant(true);
            }
            
            // If this is a self type param then link it to its type's declaration
            if (param.getName().equals(selfTypeName)) {
                param.setSelfTypedDeclaration((TypeDeclaration)scope);
            }
            
            params.add(param);
            i++;
        }

        Module moduleScope = Decl.getModuleContainer(scope);
        // Now all type params have been set, we can resolve the references parts
        Iterator<TypeParameter> paramsIterator = params.iterator();
        for(AnnotationMirror typeParamAnnotation : typeParameterAnnotations){
            TypeParameter param = paramsIterator.next();
            
            @SuppressWarnings("unchecked")
            List<String> satisfiesAttribute = (List<String>)typeParamAnnotation.getValue("satisfies");
            setListOfTypes(param.getSatisfiedTypes(), satisfiesAttribute, scope, moduleScope, 
                    "type parameter '"+param.getName()+"' satisfied types");

            @SuppressWarnings("unchecked")
            List<String> caseTypesAttribute = (List<String>)typeParamAnnotation.getValue("caseTypes");
            if(caseTypesAttribute != null && !caseTypesAttribute.isEmpty())
                param.setCaseTypes(new LinkedList<ProducedType>());
            setListOfTypes(param.getCaseTypes(), caseTypesAttribute, scope, moduleScope,
                    "type parameter '"+param.getName()+"' case types");

            String defaultValueAttribute = (String)typeParamAnnotation.getValue("defaultValue");
            if(defaultValueAttribute != null && !defaultValueAttribute.isEmpty()){
                ProducedType decodedType = decodeType(defaultValueAttribute, scope, moduleScope, 
                        "type parameter '"+param.getName()+"' defaultValue");
                param.setDefaultTypeArgument(decodedType);
                param.setDefaulted(true);
            }
        }
    }

    private boolean hasNonErasedBounds(TypeParameterMirror typeParameterMirror) {
        List<TypeMirror> bounds = typeParameterMirror.getBounds();
        // if we have at least one bound and not a single Object one
        return bounds.size() > 0
                && (bounds.size() != 1
                   || !sameType(bounds.get(0), OBJECT_TYPE));
    }

    private void setListOfTypes(List<ProducedType> destinationTypeList, List<String> serialisedTypes, Scope scope, Module moduleScope, 
                                String targetType) {
        if(serialisedTypes != null){
            for (String serialisedType : serialisedTypes) {
                ProducedType decodedType = decodeType(serialisedType, scope, moduleScope, targetType);
                destinationTypeList.add(decodedType);
            }
        }
    }

    // from java type info
    @SuppressWarnings("deprecation")
    private void setTypeParameters(Scope scope, List<TypeParameter> params, List<TypeParameterMirror> typeParameters) {
        // We must first add every type param, before we resolve the bounds, which can
        // refer to type params.
        for(TypeParameterMirror typeParam : typeParameters){
            TypeParameter param = new TypeParameter();
            param.setUnit(((Element)scope).getUnit());
            param.setContainer(scope);
            param.setDeclaration((Declaration) scope);
            // let's not trigger the lazy-loading if we're completing a LazyClass/LazyInterface
            if(scope instanceof LazyContainer)
                ((LazyContainer)scope).addMember(param);
            else // must be a method
                scope.getMembers().add(param);
            param.setName(typeParam.getName());
            param.setExtendedType(typeFactory.getAnythingDeclaration().getType());
            params.add(param);
        }
        // Now all type params have been set, we can resolve the references parts
        Iterator<TypeParameter> paramsIterator = params.iterator();
        for(TypeParameterMirror typeParam : typeParameters){
            TypeParameter param = paramsIterator.next();
            List<TypeMirror> bounds = typeParam.getBounds();
            for(TypeMirror bound : bounds){
                ProducedType boundType;
                // we turn java's default upper bound java.lang.Object into ceylon.language.Object
                if(sameType(bound, OBJECT_TYPE)){
                    // avoid adding java's default upper bound if it's just there with no meaning
                    if(bounds.size() == 1)
                        break;
                    boundType = getNonPrimitiveType(getLanguageModule(), CEYLON_OBJECT_TYPE, scope, VarianceLocation.INVARIANT);
                }else
                    boundType = getNonPrimitiveType(Decl.getModuleContainer(scope), bound, scope, VarianceLocation.INVARIANT);
                param.getSatisfiedTypes().add(boundType);
            }
        }
    }

    // method
    private void setTypeParameters(Method method, MethodMirror methodMirror) {
        List<TypeParameter> params = new LinkedList<TypeParameter>();
        method.setTypeParameters(params);
        List<AnnotationMirror> typeParameters = getTypeParametersFromAnnotations(methodMirror);
        if(typeParameters != null) {
            setTypeParametersFromAnnotations(method, params, methodMirror, typeParameters, methodMirror.getTypeParameters());
        } else {
            setTypeParameters(method, params, methodMirror.getTypeParameters());
        }
    }

    // class
    private void setTypeParameters(TypeDeclaration klass, ClassMirror classMirror) {
        List<AnnotationMirror> typeParameters = getTypeParametersFromAnnotations(classMirror);
        List<TypeParameterMirror> mirrorTypeParameters = classMirror.getTypeParameters();
        if(typeParameters != null) {
            if(typeParameters.isEmpty())
                return;
            List<TypeParameter> params = new ArrayList<TypeParameter>(typeParameters.size());
            klass.setTypeParameters(params);
            setTypeParametersFromAnnotations(klass, params, classMirror, typeParameters, mirrorTypeParameters);
        } else {
            if(mirrorTypeParameters.isEmpty())
                return;
            List<TypeParameter> params = new ArrayList<TypeParameter>(mirrorTypeParameters.size());
            klass.setTypeParameters(params);
            setTypeParameters(klass, params, mirrorTypeParameters);
        }
    }        

    //
    // TypeParsing and ModelLoader

    private ProducedType decodeType(String value, Scope scope, Module moduleScope, String targetType) {
        return decodeType(value, scope, moduleScope, targetType, null);
    }
    
    private ProducedType decodeType(String value, Scope scope, Module moduleScope, String targetType, Declaration target) {
        try{
            return typeParser.decodeType(value, scope, moduleScope, getUnitForModule(moduleScope));
        }catch(TypeParserException x){
            String text = formatTypeErrorMessage("Error while parsing type of", targetType, target, scope);
            return logModelResolutionException(x.getMessage(), scope, text);
        }catch(ModelResolutionException x){
            String text = formatTypeErrorMessage("Error while resolving type of", targetType, target, scope);
            return logModelResolutionException(x, scope, text);
        }
    }
    
    private Unit getUnitForModule(Module module) {
        List<Package> packages = module.getPackages();
        if(packages.isEmpty()){
            System.err.println("No package for module "+module.getNameAsString());
            return null;
        }
        Package pkg = packages.get(0);
        if(pkg instanceof LazyPackage == false){
            System.err.println("No lazy package for module "+module.getNameAsString());
            return null;
        }
        Unit unit = getCompiledUnit((LazyPackage) pkg, null);
        if(unit == null){
            System.err.println("No unit for module "+module.getNameAsString());
            return null;
        }
        return unit;
    }
    
    private String formatTypeErrorMessage(String prefix, String targetType, Declaration target, Scope scope) {
        String forTarget;
        if(target != null)
            forTarget = " for "+target.getQualifiedNameString();
        else if(scope != null)
            forTarget = " for "+scope.getQualifiedNameString();
        else
            forTarget = "";
        return prefix+" "+targetType+forTarget;
    }

    /** Warning: only valid for toplevel types, not for type parameters */
    private ProducedType obtainType(TypeMirror type, AnnotatedMirror symbol, Scope scope, Module moduleScope, VarianceLocation variance, 
                                    String targetType, Declaration target) {
        String typeName = getAnnotationStringValue(symbol, CEYLON_TYPE_INFO_ANNOTATION);
        if (typeName != null) {
            ProducedType ret = decodeType(typeName, scope, moduleScope, targetType, target);
            // even decoded types need to fit with the reality of the underlying type
            ret.setUnderlyingType(getUnderlyingType(type, TypeLocation.TOPLEVEL));
            return ret;
        } else {
            try{
                return obtainType(moduleScope, type, scope, TypeLocation.TOPLEVEL, variance);
            }catch(ModelResolutionException x){
                String text = formatTypeErrorMessage("Error while resolving type of", targetType, target, scope);
                return logModelResolutionException(x, scope, text);
            }
        }
    }
    
    private enum TypeLocation {
        TOPLEVEL, TYPE_PARAM;
    }
    
    private enum VarianceLocation {
        /**
         * Used in parameter
         */
        CONTRAVARIANT,
        /**
         * Used in method return value
         */
        COVARIANT,
        /**
         * For field
         */
        INVARIANT;
    }

    private String getUnderlyingType(TypeMirror type, TypeLocation location){
        // don't erase to c.l.String if in a type param location
        if ((sameType(type, STRING_TYPE) && location != TypeLocation.TYPE_PARAM)
            || sameType(type, PRIM_BYTE_TYPE)
            || sameType(type, PRIM_SHORT_TYPE)
            || sameType(type, PRIM_INT_TYPE)
            || sameType(type, PRIM_FLOAT_TYPE)
            || sameType(type, PRIM_CHAR_TYPE)) {
            return type.getQualifiedName();
        }
        return null;
    }
    
    private ProducedType obtainType(Module moduleScope, TypeMirror type, Scope scope, TypeLocation location, VarianceLocation variance) {
        TypeMirror originalType = type;
        // ERASURE
        type = applyTypeMapping(type, location);
        
        ProducedType ret = getNonPrimitiveType(moduleScope, type, scope, variance);
        if (ret.getUnderlyingType() == null) {
            ret.setUnderlyingType(getUnderlyingType(originalType, location));
        }
        return ret;
    }
    
    private TypeMirror applyTypeMapping(TypeMirror type, TypeLocation location) {
        // don't erase to c.l.String if in a type param location
        if (sameType(type, STRING_TYPE) && location != TypeLocation.TYPE_PARAM) {
            return CEYLON_STRING_TYPE;
            
        } else if (sameType(type, PRIM_BOOLEAN_TYPE)) {
            return CEYLON_BOOLEAN_TYPE;
            
        } else if (sameType(type, PRIM_BYTE_TYPE)) {
            return CEYLON_INTEGER_TYPE;
            
        } else if (sameType(type, PRIM_SHORT_TYPE)) {
            return CEYLON_INTEGER_TYPE;
            
        } else if (sameType(type, PRIM_INT_TYPE)) {
            return CEYLON_INTEGER_TYPE;
            
        } else if (sameType(type, PRIM_LONG_TYPE)) {
            return CEYLON_INTEGER_TYPE;
            
        } else if (sameType(type, PRIM_FLOAT_TYPE)) {
            return CEYLON_FLOAT_TYPE;
            
        } else if (sameType(type, PRIM_DOUBLE_TYPE)) {
            return CEYLON_FLOAT_TYPE;
            
        } else if (sameType(type, PRIM_CHAR_TYPE)) {
            return CEYLON_CHARACTER_TYPE;
            
        } else if (sameType(type, OBJECT_TYPE)) {
            return CEYLON_OBJECT_TYPE;
            
        } else if (sameType(type, ERROR_TYPE)) {
            return CEYLON_ERROR_TYPE;
            
        } else if (sameType(type, EXCEPTION_TYPE)) {
            return CEYLON_EXCEPTION_TYPE;
            
        } else if (type.getKind() == TypeKind.ARRAY) {

            TypeMirror ct = type.getComponentType();
            
            if (sameType(ct, PRIM_BOOLEAN_TYPE)) {
                return JAVA_BOOLEAN_ARRAY_TYPE;
            } else if (sameType(ct, PRIM_BYTE_TYPE)) {
                return JAVA_BYTE_ARRAY_TYPE;
            } else if (sameType(ct, PRIM_SHORT_TYPE)) {
                return JAVA_SHORT_ARRAY_TYPE;
            } else if (sameType(ct, PRIM_INT_TYPE)) { 
                return JAVA_INT_ARRAY_TYPE;
            } else if (sameType(ct, PRIM_LONG_TYPE)) { 
                return JAVA_LONG_ARRAY_TYPE;
            } else if (sameType(ct, PRIM_FLOAT_TYPE)) {
                return JAVA_FLOAT_ARRAY_TYPE;
            } else if (sameType(ct, PRIM_DOUBLE_TYPE)) {
                return JAVA_DOUBLE_ARRAY_TYPE;
            } else if (sameType(ct, PRIM_CHAR_TYPE)) {
                return JAVA_CHAR_ARRAY_TYPE;
            } else {
                // object array
                return new SimpleReflType(JAVA_LANG_OBJECT_ARRAY, SimpleReflType.Module.JDK, TypeKind.DECLARED, ct);
            }
        }
        return type;
    }
    
    private boolean sameType(TypeMirror t1, TypeMirror t2) {
        // make sure we deal with arrays which can't have a qualified name
        if(t1.getKind() == TypeKind.ARRAY){
            if(t2.getKind() != TypeKind.ARRAY)
                return false;
            return sameType(t1.getComponentType(), t2.getComponentType());
        }
        if(t2.getKind() == TypeKind.ARRAY)
            return false;
        // the rest should be OK
        return t1.getQualifiedName().equals(t2.getQualifiedName());
    }
    
    @Override
    public Declaration getDeclaration(Module module, String typeName, DeclarationType declarationType) {
        return convertToDeclaration(module, typeName, declarationType);
    }

    private ProducedType getNonPrimitiveType(Module moduleScope, TypeMirror type, Scope scope, VarianceLocation variance) {
        TypeDeclaration declaration = (TypeDeclaration) convertNonPrimitiveTypeToDeclaration(moduleScope, type, scope, DeclarationType.TYPE);
        if(declaration == null){
            throw new ModelResolutionException("Failed to find declaration for "+type.getQualifiedName());
        }
        return applyTypeArguments(moduleScope, declaration, type, scope, variance, TypeMappingMode.NORMAL, null);
    }

    private enum TypeMappingMode {
        NORMAL, GENERATOR
    }
    
    @SuppressWarnings("serial")
    private static class RecursiveTypeParameterBoundException extends RuntimeException {}
    
    private ProducedType applyTypeArguments(Module moduleScope, TypeDeclaration declaration,
                                            TypeMirror type, Scope scope, VarianceLocation variance,
                                            TypeMappingMode mode, Set<TypeDeclaration> rawDeclarationsSeen) {
        List<TypeMirror> javacTypeArguments = type.getTypeArguments();
        if(!javacTypeArguments.isEmpty()){
            // detect recursive bounds that we can't possibly satisfy, such as Foo<T extends Foo<T>>
            if(rawDeclarationsSeen != null && !rawDeclarationsSeen.add(declaration))
                throw new RecursiveTypeParameterBoundException();
            try{
                List<ProducedType> typeArguments = new ArrayList<ProducedType>(javacTypeArguments.size());
                for(TypeMirror typeArgument : javacTypeArguments){
                    // if a single type argument is a wildcard and we are in a covariant location, we erase to Object
                    if(typeArgument.getKind() == TypeKind.WILDCARD){
                        // if contravariant or if it's a ceylon type we use its bound
                        if(variance == VarianceLocation.CONTRAVARIANT || Decl.isCeylon(declaration)){
                            TypeMirror bound = typeArgument.getUpperBound();
                            if(bound == null)
                                bound = typeArgument.getLowerBound();
                            // if it has no bound let's take Object
                            if(bound == null){
                                // add the type arg and move to the next one
                                typeArguments.add(typeFactory.getObjectDeclaration().getType());
                                continue;
                            }
                            typeArgument = bound;
                        } else {
                            ProducedType result = typeFactory.getObjectDeclaration().getType();
                            result.setUnderlyingType(type.getQualifiedName());
                            return result;
                        }
                    }
                    ProducedType producedTypeArgument;
                    if(mode == TypeMappingMode.NORMAL)
                        producedTypeArgument = obtainType(moduleScope, typeArgument, scope, TypeLocation.TYPE_PARAM, variance);
                    else
                        producedTypeArgument = obtainTypeParameterBound(moduleScope, typeArgument, scope, rawDeclarationsSeen);
                    typeArguments.add(producedTypeArgument);
                }
                return declaration.getProducedType(getQualifyingType(declaration), typeArguments);
            }finally{
                if(rawDeclarationsSeen != null)
                    rawDeclarationsSeen.remove(declaration);
            }
        }else if(!declaration.getTypeParameters().isEmpty()){
            // we have a raw type
            ProducedType result;
            if(variance == VarianceLocation.CONTRAVARIANT || Decl.isCeylon(declaration)){
                // detect recursive bounds that we can't possibly satisfy, such as Foo<T extends Foo<T>>
                if(rawDeclarationsSeen == null)
                    rawDeclarationsSeen = new HashSet<TypeDeclaration>();
                if(!rawDeclarationsSeen.add(declaration))
                    throw new RecursiveTypeParameterBoundException();
                try{
                    // generate a compatible bound for each type parameter
                    int count = declaration.getTypeParameters().size();
                    List<ProducedType> typeArguments = new ArrayList<ProducedType>(count);
                    for(TypeParameterMirror tp : type.getDeclaredClass().getTypeParameters()){
                        // FIXME: multiple bounds?
                        if(tp.getBounds().size() == 1){
                            TypeMirror bound = tp.getBounds().get(0);
                            typeArguments.add(obtainTypeParameterBound(moduleScope, bound, declaration, rawDeclarationsSeen));
                        }else
                            typeArguments.add(typeFactory.getObjectDeclaration().getType());
                    }
                    result = declaration.getProducedType(getQualifyingType(declaration), typeArguments);
                }catch(RecursiveTypeParameterBoundException x){
                    // damnit, go for Object
                    result = typeFactory.getObjectDeclaration().getType();
                }finally{
                    rawDeclarationsSeen.remove(declaration);
                }
            }else{
                // covariant raw erases to Object
                result = typeFactory.getObjectDeclaration().getType();
            }
            result.setUnderlyingType(type.getQualifiedName());
            result.setRaw(true);
            return result;
        }
        return declaration.getType();
    }

    private ProducedType obtainTypeParameterBound(Module moduleScope, TypeMirror type, Scope scope, Set<TypeDeclaration> rawDeclarationsSeen) {
        // type variables are never mapped
        if(type.getKind() == TypeKind.TYPEVAR){
            TypeParameterMirror typeParameter = type.getTypeParameter();
            if(!typeParameter.getBounds().isEmpty()){
                IntersectionType it = new IntersectionType(getUnitForModule(moduleScope));
                for(TypeMirror bound : typeParameter.getBounds()){
                    ProducedType boundModel = obtainTypeParameterBound(moduleScope, bound, scope, rawDeclarationsSeen);
                    it.getSatisfiedTypes().add(boundModel);
                }
                return it.getType();
            }else
                // no bound is Object
                return typeFactory.getObjectDeclaration().getType();
        }else{
            TypeMirror mappedType = applyTypeMapping(type, TypeLocation.TYPE_PARAM);

            TypeDeclaration declaration = (TypeDeclaration) convertNonPrimitiveTypeToDeclaration(moduleScope, mappedType, scope, DeclarationType.TYPE);
            if(declaration == null){
                throw new RuntimeException("Failed to find declaration for "+type);
            }

            ProducedType ret = applyTypeArguments(moduleScope, declaration, type, scope, VarianceLocation.CONTRAVARIANT, TypeMappingMode.GENERATOR, rawDeclarationsSeen);
            
            if (ret.getUnderlyingType() == null) {
                ret.setUnderlyingType(getUnderlyingType(type, TypeLocation.TYPE_PARAM));
            }
            return ret;
        }
    }
    
    private ProducedType getQualifyingType(TypeDeclaration declaration) {
        // As taken from ProducedType.getType():
        if (declaration.isMember()) {
            return((ClassOrInterface) declaration.getContainer()).getType();
        }
        return null;
    }

    @Override
    public synchronized ProducedType getType(Module module, String pkgName, String name, Scope scope) {
        if(scope != null){
            TypeParameter typeParameter = lookupTypeParameter(scope, name);
            if(typeParameter != null)
                return typeParameter.getType();
        }
        if(!isBootstrap || !name.startsWith(CEYLON_LANGUAGE)) {
            if(scope != null && pkgName != null){
                Package containingPackage = Decl.getPackageContainer(scope);
                Package pkg = containingPackage.getModule().getPackage(pkgName);
                String relativeName = null;
                String unquotedName = name.replace("$", "");
                if(!pkgName.isEmpty()){
                    if(unquotedName.startsWith(pkgName+"."))
                        relativeName = unquotedName.substring(pkgName.length()+1);
                    // else we don't try it's not in this package
                }else
                    relativeName = unquotedName;
                if(relativeName != null && pkg != null){
                    Declaration declaration = pkg.getDirectMember(relativeName, null, false);
                    // if we get a value, we want its type
                    if(Decl.isValue(declaration)
                            && ((Value)declaration).getTypeDeclaration().getName().equals(relativeName))
                        declaration = ((Value)declaration).getTypeDeclaration();
                    if(declaration instanceof TypeDeclaration)
                        return ((TypeDeclaration)declaration).getType();
                    // if we have something but it's not a type decl, it's a:
                    // - value that's not an object (why would we get its type here?)
                    // - method (doesn't have a type of the same name)
                    if(declaration != null)
                        return null;
                }
            }
            Declaration declaration = convertToDeclaration(module, name, DeclarationType.TYPE);
            if(declaration instanceof TypeDeclaration)
                return ((TypeDeclaration)declaration).getType();
            // we're looking for type declarations, so anything else doesn't work for us
            return null;
        }

        return findLanguageModuleDeclarationForBootstrap(name);
    }

    private ProducedType findLanguageModuleDeclarationForBootstrap(String name) {
        // make sure we don't return anything for ceylon.language
        if(name.equals(CEYLON_LANGUAGE))
            return null;
        
        // we're bootstrapping ceylon.language so we need to return the ProducedTypes straight from the model we're compiling
        Module languageModule = modules.getLanguageModule();
        
        int lastDot = name.lastIndexOf(".");
        if(lastDot == -1)
            return null;
        String pkgName = name.substring(0, lastDot);
        String simpleName = name.substring(lastDot+1);
        // Nothing is a special case with no real decl
        if(name.equals("ceylon.language.Nothing"))
            return newNothingType();

        // find the right package
        Package pkg = languageModule.getDirectPackage(pkgName);
        if(pkg != null){
            Declaration member = pkg.getDirectMember(simpleName, null, false);
            // if we get a value, we want its type
            if(Decl.isValue(member)
                    && ((Value)member).getTypeDeclaration().getName().equals(simpleName)){
                member = ((Value)member).getTypeDeclaration();
            }
            if(member instanceof TypeDeclaration)
                return ((TypeDeclaration)member).getType();
        }
        throw new ModelResolutionException("Failed to look up given type in language module while bootstrapping: "+name);
    }

    public synchronized void removeDeclarations(List<Declaration> declarations) {
        // keep in sync with getOrCreateDeclaration
        for (Declaration decl : declarations) {
            String fqn = decl.getQualifiedNameString().replace("::", ".");
            Module module = Decl.getModuleContainer(decl.getContainer());
            Map<String, Declaration> firstCache = null;
            Map<String, Declaration> secondCache = null;
            if(Decl.isToplevel(decl)){
                if(Decl.isValue(decl)){
                    firstCache = valueDeclarationsByName;
                    if(((Value)decl).getTypeDeclaration().isAnonymous())
                        secondCache = typeDeclarationsByName;
                }else if(Decl.isMethod(decl))
                    firstCache = valueDeclarationsByName;
            }
            if(decl instanceof ClassOrInterface){
                firstCache = typeDeclarationsByName;
            }
            // ignore declarations which we do not cache, like member method/attributes
            String key = cacheKeyByModule(module, fqn);
            if(firstCache != null) {
                if (firstCache.remove(key) == null) {
                    firstCache.remove(key + "_");
                }
                if(secondCache != null)
                    if (secondCache.remove(key) == null) {
                        secondCache.remove(key + "_");
                    }
            }
            
            if (classMirrorCache.remove(key) == null) {
                classMirrorCache.remove(key + "_");
            }
        }
    }

    private static class Stats{
        int loaded, total;
    }

    private int inspectForStats(Map<String,Declaration> cache, Map<Package, Stats> loadedByPackage){
        int loaded = 0;
        for(Declaration decl : cache.values()){
            if(decl instanceof LazyElement){
                Package pkg = getPackage(decl);
                if(pkg == null){
                    logVerbose("[Model loader stats: declaration "+decl.getName()+" has no package. Skipping.]");
                    continue;
                }
                Stats stats = loadedByPackage.get(pkg);
                if(stats == null){
                    stats = new Stats();
                    loadedByPackage.put(pkg, stats);
                }
                stats.total++;
                if(((LazyElement)decl).isLoaded()){
                    loaded++;
                    stats.loaded++;
                }
            }
        }
        return loaded;
    }

    public synchronized void printStats(){
        Map<Package, Stats> loadedByPackage = new HashMap<Package, Stats>();
        int loaded = inspectForStats(typeDeclarationsByName, loadedByPackage)
                + inspectForStats(valueDeclarationsByName, loadedByPackage);
        logVerbose("[Model loader: "+loaded+"(loaded)/"+(typeDeclarationsByName.size()+valueDeclarationsByName.size())+"(total) declarations]");
        for(Entry<Package, Stats> packageEntry : loadedByPackage.entrySet()){
            logVerbose("[ Package "+packageEntry.getKey().getNameAsString()+": "
                    +packageEntry.getValue().loaded+"(loaded)/"+packageEntry.getValue().total+"(total) declarations]");
        }
    }

    private static Package getPackage(Object decl) {
        if(decl == null)
            return null;
        if(decl instanceof Package)
            return (Package) decl;
        return getPackage(((Declaration)decl).getContainer());
    }
    
    protected void logMissingOracleType(String type) {
        logVerbose("Hopefully harmless completion failure in model loader: "+type
                +". This is most likely when the JDK depends on Oracle private classes that we can't find."
                +" As a result some model information will be incomplete.");
    }

    public void setupSourceFileObjects(List<?> treeHolders) {
    }

    public static boolean isJDKModule(String name) {
        return JDKUtils.isJDKModule(name)
                || JDKUtils.isOracleJDKModule(name);
    }
    
    @Override
    public Module getLoadedModule(String moduleName) {
        // FIXME: version?
        for(Module mod : modules.getListOfModules()){
            if(mod.getNameAsString().equals(moduleName))
                return mod;
        }
        return null;
    }

    public Module getLanguageModule() {
        return modules.getLanguageModule();
    }

    public Module findModule(String name, String version){
        if(name.equals(Module.DEFAULT_MODULE_NAME))
            return modules.getDefaultModule();
        for(Module module : modules.getListOfModules()){
            if(module.getNameAsString().equals(name)
                    && (version == null || module.getVersion() == null || version.equals(module.getVersion())))
                return module;
        }
        return null;
    }
    
    public Module getJDKBaseModule() {
        return findModule(JAVA_BASE_MODULE_NAME, JDK_MODULE_VERSION);
    }

    public Module findModuleForFile(File file){
        File path = file.getParentFile();
        while (path != null) {
            String name = path.getPath().replaceAll("[\\\\/]", ".");
            Module m = getLoadedModule(name);
            if (m != null) {
                return m;
            }
            path = path.getParentFile();
        }
        return modules.getDefaultModule();
    }

    protected abstract Module findModuleForClassMirror(ClassMirror classMirror);
    
    protected boolean isTypeHidden(Module module, String qualifiedName){
        return module.getNameAsString().equals(JAVA_BASE_MODULE_NAME)
                && qualifiedName.equals("java.lang.Object");
    }
    
    public synchronized Package findPackage(String quotedPkgName) {
        String pkgName = quotedPkgName.replace("$", "");
        // in theory we only have one package with the same name per module in javac
        for(Package pkg : packagesByName.values()){
            if(pkg.getNameAsString().equals(pkgName))
                return pkg;
        }
        return null;
    }

    /**
     * See explanation in cacheModulelessPackages() below. This is called by LanguageCompiler during loadCompiledModules().
     */
    public synchronized LazyPackage findOrCreateModulelessPackage(String pkgName) {
        LazyPackage pkg = modulelessPackages.get(pkgName);
        if(pkg != null)
            return pkg;
        pkg = new LazyPackage(this);
        // FIXME: some refactoring needed
        pkg.setName(pkgName == null ? Collections.<String>emptyList() : Arrays.asList(pkgName.split("\\.")));
        modulelessPackages.put(pkgName, pkg);
        return pkg;
    }
    
    /**
     * Stef: this sucks balls, but the typechecker wants Packages created before we have any Module set up, including for parsing a module
     * file, and because the model loader looks up packages and caches them using their modules, we can't really have packages before we
     * have modules. Rather than rewrite the typechecker, we create moduleless packages during parsing, which means they are not cached with
     * their modules, and after the loadCompiledModules step above, we fix the package modules. Remains to be done is to move the packages
     * created from their cache to the right per-module cache.
     */
    public synchronized void cacheModulelessPackages(){
        for(LazyPackage pkg : modulelessPackages.values()){
            String quotedPkgName = Util.quoteJavaKeywords(pkg.getQualifiedNameString());
            packagesByName.put(cacheKeyByModule(pkg.getModule(), quotedPkgName), pkg);
        }
        modulelessPackages.clear();
    }

    /**
     * Stef: after a lot of attempting, I failed to make the CompilerModuleManager produce a LazyPackage when the ModuleManager.initCoreModules
     * is called for the default package. Because it is called by the PhasedUnits constructor, which is called by the ModelLoader constructor,
     * which means the model loader is not yet in the context, so the CompilerModuleManager can't obtain it to pass it to the LazyPackage
     * constructor. A rewrite of the logic of the typechecker scanning would fix this, but at this point it's just faster to let it create
     * the wrong default package and fix it before we start parsing anything.
     */
    public synchronized void fixDefaultPackage() {
        Module defaultModule = modules.getDefaultModule();
        Package defaultPackage = defaultModule.getDirectPackage("");
        if(defaultPackage instanceof LazyPackage == false){
            LazyPackage newPkg = findOrCreateModulelessPackage("");
            List<Package> defaultModulePackages = defaultModule.getPackages();
            if(defaultModulePackages.size() != 1)
                throw new RuntimeException("Assertion failed: default module has more than the default package: "+defaultModulePackages.size());
            defaultModulePackages.clear();
            defaultModulePackages.add(newPkg);
            newPkg.setModule(defaultModule);
            defaultPackage.setModule(null);
        }
    }

    public boolean isImported(Module moduleScope, Module importedModule) {
        if(moduleScope == importedModule)
            return true;
        if(isImportedSpecialRules(moduleScope, importedModule))
            return true;
        Set<Module> visited = new HashSet<Module>();
        visited.add(moduleScope);
        for(ModuleImport imp : moduleScope.getImports()){
            if(imp.getModule() == importedModule)
                return true;
            if(imp.isExport() && isImportedTransitively(imp.getModule(), importedModule, visited))
                return true;
        }
        return false;
    }

    private boolean isImportedSpecialRules(Module moduleScope, Module importedModule) {
        String importedModuleName = importedModule.getNameAsString();
        // every Java module imports the JDK
        // ceylon.language imports the JDK
        if((moduleScope.isJava()
                || moduleScope == getLanguageModule())
                && (JDKUtils.isJDKModule(importedModuleName)
                   || JDKUtils.isOracleJDKModule(importedModuleName)))
            return true;
        // everyone imports the language module
        if(importedModule == getLanguageModule())
            return true;
        if(moduleScope == getLanguageModule()){
            // this really sucks, I suppose we should set that up better somewhere else
            if((importedModuleName.equals("com.redhat.ceylon.compiler.java")
                || importedModuleName.equals("com.redhat.ceylon.typechecker")
                || importedModuleName.equals("com.redhat.ceylon.common")
                || importedModuleName.equals("com.redhat.ceylon.module-resolver"))
                && importedModule.getVersion().equals(Versions.CEYLON_VERSION_NUMBER))
                return true;
            if(importedModuleName.equals("org.jboss.modules")
                    && importedModule.getVersion().equals("1.1.3.GA"))
                return true;
        }
        return false;
    }
    
    private boolean isImportedTransitively(Module moduleScope, Module importedModule, Set<Module> visited) {
        if(!visited.add(moduleScope))
            return false;
        for(ModuleImport imp : moduleScope.getImports()){
            // only consider exported transitive deps
            if(!imp.isExport())
                continue;
            if(imp.getModule() == importedModule)
                return true;
            if(isImportedSpecialRules(imp.getModule(), importedModule))
                return true;
            if(isImportedTransitively(imp.getModule(), importedModule, visited))
                return true;
        }
        return false;
    }

    protected boolean isModuleOrPackageDescriptorName(String name) {
        return name.equals("module_") || name.equals("package_");
    }
}
