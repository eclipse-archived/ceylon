package com.redhat.ceylon.model.loader;

import static com.redhat.ceylon.model.typechecker.model.ModelUtil.intersection;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.union;

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

import com.redhat.ceylon.common.Backend;
import com.redhat.ceylon.common.BooleanUtil;
import com.redhat.ceylon.common.JVMModuleUtil;
import com.redhat.ceylon.common.ModuleUtil;
import com.redhat.ceylon.common.Versions;
import com.redhat.ceylon.model.cmr.ArtifactResult;
import com.redhat.ceylon.model.cmr.JDKUtils;
import com.redhat.ceylon.model.loader.mirror.AccessibleMirror;
import com.redhat.ceylon.model.loader.mirror.AnnotatedMirror;
import com.redhat.ceylon.model.loader.mirror.AnnotationMirror;
import com.redhat.ceylon.model.loader.mirror.ClassMirror;
import com.redhat.ceylon.model.loader.mirror.FieldMirror;
import com.redhat.ceylon.model.loader.mirror.MethodMirror;
import com.redhat.ceylon.model.loader.mirror.PackageMirror;
import com.redhat.ceylon.model.loader.mirror.TypeMirror;
import com.redhat.ceylon.model.loader.mirror.TypeParameterMirror;
import com.redhat.ceylon.model.loader.mirror.VariableMirror;
import com.redhat.ceylon.model.loader.model.AnnotationProxyClass;
import com.redhat.ceylon.model.loader.model.AnnotationProxyMethod;
import com.redhat.ceylon.model.loader.model.FieldValue;
import com.redhat.ceylon.model.loader.model.JavaBeanValue;
import com.redhat.ceylon.model.loader.model.JavaMethod;
import com.redhat.ceylon.model.loader.model.LazyClass;
import com.redhat.ceylon.model.loader.model.LazyClassAlias;
import com.redhat.ceylon.model.loader.model.LazyContainer;
import com.redhat.ceylon.model.loader.model.LazyElement;
import com.redhat.ceylon.model.loader.model.LazyInterface;
import com.redhat.ceylon.model.loader.model.LazyInterfaceAlias;
import com.redhat.ceylon.model.loader.model.LazyFunction;
import com.redhat.ceylon.model.loader.model.LazyModule;
import com.redhat.ceylon.model.loader.model.LazyPackage;
import com.redhat.ceylon.model.loader.model.LazyTypeAlias;
import com.redhat.ceylon.model.loader.model.LazyValue;
import com.redhat.ceylon.model.loader.model.LocalDeclarationContainer;
import com.redhat.ceylon.model.loader.model.OutputElement;
import com.redhat.ceylon.model.loader.model.SetterWithLocalDeclarations;
import com.redhat.ceylon.model.typechecker.model.Annotated;
import com.redhat.ceylon.model.typechecker.model.Annotation;
import com.redhat.ceylon.model.typechecker.model.Class;
import com.redhat.ceylon.model.typechecker.model.ClassOrInterface;
import com.redhat.ceylon.model.typechecker.model.Constructor;
import com.redhat.ceylon.model.typechecker.model.Declaration;
import com.redhat.ceylon.model.typechecker.model.DeclarationCompleter;
import com.redhat.ceylon.model.typechecker.model.Element;
import com.redhat.ceylon.model.typechecker.model.Function;
import com.redhat.ceylon.model.typechecker.model.FunctionOrValue;
import com.redhat.ceylon.model.typechecker.model.Functional;
import com.redhat.ceylon.model.typechecker.model.Interface;
import com.redhat.ceylon.model.typechecker.model.ModelUtil;
import com.redhat.ceylon.model.typechecker.model.Module;
import com.redhat.ceylon.model.typechecker.model.ModuleImport;
import com.redhat.ceylon.model.typechecker.model.Modules;
import com.redhat.ceylon.model.typechecker.model.Package;
import com.redhat.ceylon.model.typechecker.model.Parameter;
import com.redhat.ceylon.model.typechecker.model.ParameterList;
import com.redhat.ceylon.model.typechecker.model.Scope;
import com.redhat.ceylon.model.typechecker.model.Setter;
import com.redhat.ceylon.model.typechecker.model.SiteVariance;
import com.redhat.ceylon.model.typechecker.model.Type;
import com.redhat.ceylon.model.typechecker.model.TypeAlias;
import com.redhat.ceylon.model.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.model.typechecker.model.TypeParameter;
import com.redhat.ceylon.model.typechecker.model.TypedDeclaration;
import com.redhat.ceylon.model.typechecker.model.Unit;
import com.redhat.ceylon.model.typechecker.model.UnknownType;
import com.redhat.ceylon.model.typechecker.model.Value;
import com.redhat.ceylon.model.typechecker.util.ModuleManager;

/**
 * Abstract class of a model loader that can load a model from a compiled Java representation,
 * while being agnostic of the reflection API used to load the compiled Java representation.
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
public abstract class AbstractModelLoader implements ModelCompleter, ModelLoader, DeclarationCompleter {

    public static final String JAVA_BASE_MODULE_NAME = "java.base";
    public static final String CEYLON_LANGUAGE = "ceylon.language";
    public static final String CEYLON_LANGUAGE_MODEL = "ceylon.language.meta.model";
    public static final String CEYLON_LANGUAGE_MODEL_DECLARATION = "ceylon.language.meta.declaration";
    public static final String CEYLON_LANGUAGE_SERIALIZATION = "ceylon.language.serialization";
    
    private static final String TIMER_MODEL_LOADER_CATEGORY = "model loader";
    
    public static final String CEYLON_CEYLON_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.Ceylon";
    private static final String CEYLON_MODULE_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.Module";
    private static final String CEYLON_PACKAGE_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.Package";
    public static final String CEYLON_IGNORE_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.Ignore";
    private static final String CEYLON_CLASS_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.Class";
    private static final String CEYLON_ENUMERATED_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.Enumerated";
    //private static final String CEYLON_CONSTRUCTOR_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.Constructor";
    //private static final String CEYLON_PARAMETERLIST_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.ParameterList";
    public static final String CEYLON_NAME_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.Name";
    private static final String CEYLON_SEQUENCED_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.Sequenced";
    private static final String CEYLON_FUNCTIONAL_PARAMETER_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.FunctionalParameter";
    private static final String CEYLON_DEFAULTED_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.Defaulted";
    private static final String CEYLON_SATISFIED_TYPES_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.SatisfiedTypes";
    private static final String CEYLON_CASE_TYPES_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.CaseTypes";
    private static final String CEYLON_TYPE_PARAMETERS = "com.redhat.ceylon.compiler.java.metadata.TypeParameters";
    private static final String CEYLON_TYPE_INFO_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.TypeInfo";
    public static final String CEYLON_ATTRIBUTE_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.Attribute";
    public static final String CEYLON_SETTER_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.Setter";
    public static final String CEYLON_OBJECT_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.Object";
    public static final String CEYLON_METHOD_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.Method";
    public static final String CEYLON_CONTAINER_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.Container";
    public static final String CEYLON_LOCAL_CONTAINER_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.LocalContainer";
    public static final String CEYLON_LOCAL_DECLARATION_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.LocalDeclaration";
    public static final String CEYLON_LOCAL_DECLARATIONS_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.LocalDeclarations";
    private static final String CEYLON_MEMBERS_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.Members";
    private static final String CEYLON_ANNOTATIONS_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.Annotations";
    public static final String CEYLON_VALUETYPE_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.ValueType";
    public static final String CEYLON_ALIAS_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.Alias";
    public static final String CEYLON_TYPE_ALIAS_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.TypeAlias";
    public static final String CEYLON_ANNOTATION_INSTANTIATION_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.AnnotationInstantiation";
    public static final String CEYLON_ANNOTATION_INSTANTIATION_ARGUMENTS_MEMBER = "arguments";
    public static final String CEYLON_ANNOTATION_INSTANTIATION_ANNOTATION_MEMBER = "primary";
    
    public static final String CEYLON_ANNOTATION_INSTANTIATION_TREE_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.AnnotationInstantiationTree";
    public static final String CEYLON_STRING_VALUE_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.StringValue";
    public static final String CEYLON_STRING_EXPRS_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.StringExprs";
    public static final String CEYLON_BOOLEAN_VALUE_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.BooleanValue";
    public static final String CEYLON_BOOLEAN_EXPRS_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.BooleanExprs";
    public static final String CEYLON_INTEGER_VALUE_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.IntegerValue";
    public static final String CEYLON_INTEGER_EXPRS_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.IntegerExprs";
    public static final String CEYLON_CHARACTER_VALUE_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.CharacterValue";
    public static final String CEYLON_CHARACTER_EXPRS_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.CharacterExprs";
    public static final String CEYLON_FLOAT_VALUE_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.FloatValue";
    public static final String CEYLON_FLOAT_EXPRS_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.FloatExprs";
    public static final String CEYLON_OBJECT_VALUE_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.ObjectValue";
    public static final String CEYLON_OBJECT_EXPRS_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.ObjectExprs";
    public static final String CEYLON_DECLARATION_VALUE_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.DeclarationValue";
    public static final String CEYLON_DECLARATION_EXPRS_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.DeclarationExprs";
    private static final String CEYLON_TRANSIENT_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.Transient";
    private static final String CEYLON_DYNAMIC_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.Dynamic";
    private static final String JAVA_DEPRECATED_ANNOTATION = "java.lang.Deprecated";
    
    static final String CEYLON_LANGUAGE_ABSTRACT_ANNOTATION = "ceylon.language.AbstractAnnotation$annotation$";
    static final String CEYLON_LANGUAGE_ACTUAL_ANNOTATION = "ceylon.language.ActualAnnotation$annotation$";
    static final String CEYLON_LANGUAGE_ANNOTATION_ANNOTATION = "ceylon.language.AnnotationAnnotation$annotation$";
    static final String CEYLON_LANGUAGE_DEFAULT_ANNOTATION = "ceylon.language.DefaultAnnotation$annotation$";
    static final String CEYLON_LANGUAGE_FORMAL_ANNOTATION = "ceylon.language.FormalAnnotation$annotation$";
    static final String CEYLON_LANGUAGE_SHARED_ANNOTATION = "ceylon.language.SharedAnnotation$annotation$";
    static final String CEYLON_LANGUAGE_LATE_ANNOTATION = "ceylon.language.LateAnnotation$annotation$";
    static final String CEYLON_LANGUAGE_SEALED_ANNOTATION = "ceylon.language.SealedAnnotation$annotation$";
    static final String CEYLON_LANGUAGE_VARIABLE_ANNOTATION = "ceylon.language.VariableAnnotation$annotation$";
    static final String CEYLON_LANGUAGE_FINAL_ANNOTATION = "ceylon.language.FinalAnnotation$annotation$";
    static final String CEYLON_LANGUAGE_NATIVE_ANNOTATION = "ceylon.language.NativeAnnotation$annotation$";
    static final String CEYLON_LANGUAGE_OPTIONAL_ANNOTATION = "ceylon.language.OptionalAnnotation$annotation$";
    static final String CEYLON_LANGUAGE_SERIALIZABLE_ANNOTATION = "ceylon.language.SerializableAnnotation$annotation$";
    
    static final String CEYLON_LANGUAGE_DOC_ANNOTATION = "ceylon.language.DocAnnotation$annotation$";
    static final String CEYLON_LANGUAGE_THROWS_ANNOTATIONS = "ceylon.language.ThrownExceptionAnnotation$annotations$";
    static final String CEYLON_LANGUAGE_THROWS_ANNOTATION = "ceylon.language.ThrownExceptionAnnotation$annotation$";
    static final String CEYLON_LANGUAGE_AUTHORS_ANNOTATION = "ceylon.language.AuthorsAnnotation$annotation$";
    static final String CEYLON_LANGUAGE_SEE_ANNOTATIONS = "ceylon.language.SeeAnnotation$annotations$";
    static final String CEYLON_LANGUAGE_SEE_ANNOTATION = "ceylon.language.SeeAnnotation$annotation$";
    static final String CEYLON_LANGUAGE_DEPRECATED_ANNOTATION = "ceylon.language.DeprecationAnnotation$annotation$";
    static final String CEYLON_LANGUAGE_SUPPRESS_WARNINGS_ANNOTATION = "ceylon.language.SuppressWarningsAnnotation$annotation$";
    static final String CEYLON_LANGUAGE_LICENSE_ANNOTATION = "ceylon.language.LicenseAnnotation$annotation$";
    static final String CEYLON_LANGUAGE_TAGS_ANNOTATION = "ceylon.language.TagsAnnotation$annotation$";

    // important that these are with ::
    private static final String CEYLON_LANGUAGE_CALLABLE_TYPE_NAME = "ceylon.language::Callable";
    private static final String CEYLON_LANGUAGE_TUPLE_TYPE_NAME = "ceylon.language::Tuple";
    private static final String CEYLON_LANGUAGE_SEQUENTIAL_TYPE_NAME = "ceylon.language::Sequential";
    private static final String CEYLON_LANGUAGE_SEQUENCE_TYPE_NAME = "ceylon.language::Sequence";
    private static final String CEYLON_LANGUAGE_EMPTY_TYPE_NAME = "ceylon.language::Empty";
    
    private static final TypeMirror OBJECT_TYPE = simpleCeylonObjectType("java.lang.Object");
    private static final TypeMirror ANNOTATION_TYPE = simpleCeylonObjectType("java.lang.annotation.Annotation");
    private static final TypeMirror CEYLON_OBJECT_TYPE = simpleCeylonObjectType("ceylon.language.Object");
    private static final TypeMirror CEYLON_ANNOTATION_TYPE = simpleCeylonObjectType("ceylon.language.Annotation");
    private static final TypeMirror CEYLON_CONSTRAINED_ANNOTATION_TYPE = simpleCeylonObjectType("ceylon.language.ConstrainedAnnotation");
//    private static final TypeMirror CEYLON_FUNCTION_DECLARATION_TYPE = simpleCeylonObjectType("ceylon.language.meta.declaration.FunctionDeclaration");
    private static final TypeMirror CEYLON_FUNCTION_OR_VALUE_DECLARATION_TYPE = simpleCeylonObjectType("ceylon.language.meta.declaration.FunctionOrValueDeclaration");
    private static final TypeMirror CEYLON_VALUE_DECLARATION_TYPE = simpleCeylonObjectType("ceylon.language.meta.declaration.ValueDeclaration");
    private static final TypeMirror CEYLON_ALIAS_DECLARATION_TYPE = simpleCeylonObjectType("ceylon.language.meta.declaration.AliasDeclaration");
    private static final TypeMirror CEYLON_CLASS_OR_INTERFACE_DECLARATION_TYPE = simpleCeylonObjectType("ceylon.language.meta.declaration.ClassOrInterfaceDeclaration");
    private static final TypeMirror CEYLON_CONSTRUCTOR_DECLARATION_TYPE = simpleCeylonObjectType("ceylon.language.meta.declaration.ConstructorDeclaration");
    private static final TypeMirror CEYLON_ANNOTATED_TYPE = simpleCeylonObjectType("ceylon.language.Annotated");
    private static final TypeMirror CEYLON_BASIC_TYPE = simpleCeylonObjectType("ceylon.language.Basic");
    private static final TypeMirror CEYLON_REIFIED_TYPE_TYPE = simpleCeylonObjectType("com.redhat.ceylon.compiler.java.runtime.model.ReifiedType");
    private static final TypeMirror CEYLON_SERIALIZABLE_TYPE = simpleCeylonObjectType("com.redhat.ceylon.compiler.java.runtime.serialization.Serializable");
    private static final TypeMirror CEYLON_TYPE_DESCRIPTOR_TYPE = simpleCeylonObjectType("com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor");
    
    private static final TypeMirror THROWABLE_TYPE = simpleCeylonObjectType("java.lang.Throwable");
//    private static final TypeMirror ERROR_TYPE = simpleCeylonObjectType("java.lang.Error");
    private static final TypeMirror EXCEPTION_TYPE = simpleCeylonObjectType("java.lang.Exception");
    private static final TypeMirror CEYLON_THROWABLE_TYPE = simpleCeylonObjectType("java.lang.Throwable");
    private static final TypeMirror CEYLON_EXCEPTION_TYPE = simpleCeylonObjectType("ceylon.language.Exception");
    
    private static final TypeMirror STRING_TYPE = simpleJDKObjectType("java.lang.String");
    private static final TypeMirror CEYLON_STRING_TYPE = simpleCeylonObjectType("ceylon.language.String");
    
    private static final TypeMirror PRIM_BOOLEAN_TYPE = simpleJDKObjectType("boolean");
    private static final TypeMirror CEYLON_BOOLEAN_TYPE = simpleCeylonObjectType("ceylon.language.Boolean");
    
    private static final TypeMirror PRIM_BYTE_TYPE = simpleJDKObjectType("byte");
    private static final TypeMirror CEYLON_BYTE_TYPE = simpleCeylonObjectType("ceylon.language.Byte");
    
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

    public final Object getLock(){
        return this;
    }

    /**
     * To be redefined by subclasses if they don't need local declarations.
     */
    protected boolean needsLocalDeclarations(){
        return true;
    }

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
    public final ClassMirror lookupClassMirror(Module module, String name) {
        synchronized(getLock()){
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
        name = NamingBase.stripLeadingDollar(name);
        if(!name.isEmpty()){
            int codepoint = name.codePointAt(0);
            return JvmBackendUtil.isLowerCase(codepoint);
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
        Timer nested = timer.nestedTimer();
        nested.startTask("load ceylon.language");
        Module languageModule = loadLanguageModuleAndPackage();
        
        nested.endTask();
        
        nested.startTask("load JDK");
        // make sure the jdk modules are loaded
        loadJDKModules();
        Module jdkModule = findOrCreateModule(JAVA_BASE_MODULE_NAME, JDKUtils.jdk.version);
        nested.endTask();
        
        /*
         * We start by loading java.lang and ceylon.language because we will need them no matter what.
         */
        nested.startTask("load standard packages");
        loadPackage(jdkModule, "java.lang", false);
        loadPackage(languageModule, "com.redhat.ceylon.compiler.java.metadata", false);
        loadPackage(languageModule, "com.redhat.ceylon.compiler.java.language", false);
        nested.endTask();
    }
    protected Module loadLanguageModuleAndPackage() {
        Module languageModule = findOrCreateModule(CEYLON_LANGUAGE, null);
        addModuleToClassPath(languageModule, null);
        Package languagePackage = findOrCreatePackage(languageModule, CEYLON_LANGUAGE);
        typeFactory.setPackage(languagePackage);
        
        // make sure the language module has its real dependencies added, because we need them in the classpath
        // otherwise we will get errors on the Util and Metamodel calls we insert
        // WARNING! Make sure this list is always the same as the one in /ceylon-runtime/dist/repo/ceylon/language/_version_/module.xml
        languageModule.addImport(new ModuleImport(findOrCreateModule("com.redhat.ceylon.compiler.java", Versions.CEYLON_VERSION_NUMBER), false, false, Backend.Java));
        languageModule.addImport(new ModuleImport(findOrCreateModule("com.redhat.ceylon.compiler.js", Versions.CEYLON_VERSION_NUMBER), false, false, Backend.Java));
        languageModule.addImport(new ModuleImport(findOrCreateModule("com.redhat.ceylon.common", Versions.CEYLON_VERSION_NUMBER), false, false, Backend.Java));
        languageModule.addImport(new ModuleImport(findOrCreateModule("com.redhat.ceylon.model", Versions.CEYLON_VERSION_NUMBER), false, false, Backend.Java));
        languageModule.addImport(new ModuleImport(findOrCreateModule("com.redhat.ceylon.module-resolver", Versions.CEYLON_VERSION_NUMBER), false, false, Backend.Java));
        languageModule.addImport(new ModuleImport(findOrCreateModule("com.redhat.ceylon.typechecker", Versions.CEYLON_VERSION_NUMBER), false, false, Backend.Java));
        languageModule.addImport(new ModuleImport(findOrCreateModule("org.jboss.modules", "1.3.3.Final"), false, false, Backend.Java));
        languageModule.addImport(new ModuleImport(findOrCreateModule("org.jboss.jandex", "1.0.3.Final"), false, false, Backend.Java));
        
        return languageModule;
    }
    protected void loadJDKModules() {
        for(String jdkModule : JDKUtils.getJDKModuleNames())
            findOrCreateModule(jdkModule, JDKUtils.jdk.version);
        for(String jdkOracleModule : JDKUtils.getOracleJDKModuleNames())
            findOrCreateModule(jdkOracleModule, JDKUtils.jdk.version);
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
            if(module != null && isFlatClasspath() && isMavenModule(moduleScope))
                return convertToDeclaration(module, typeName, declarationType);
            String error = "Declaration '" + typeName + "' could not be found in module '" + moduleScope.getNameAsString() 
                    + "' or its imported modules";
            if(module != null && !module.isDefault())
                error += " but was found in the non-imported module '"+module.getNameAsString()+"'";
            return logModelResolutionException(null, moduleScope, error).getDeclaration();
        }
    }
    
    public Declaration convertToDeclaration(Module module, ClassMirror classMirror, DeclarationType declarationType) {
        // avoid ignored classes
        if(classMirror.getAnnotation(CEYLON_IGNORE_ANNOTATION) != null)
            return null;
        // avoid local interfaces that were pulled to the toplevel if required
        if(classMirror.getAnnotation(CEYLON_LOCAL_CONTAINER_ANNOTATION) != null
                && !needsLocalDeclarations())
            return null;
        // avoid Ceylon annotations
        if(classMirror.getAnnotation(CEYLON_CEYLON_ANNOTATION) != null
                && classMirror.isAnnotationType())
            return null;
        // avoid module and package descriptors too
        if(classMirror.getAnnotation(CEYLON_MODULE_ANNOTATION) != null
                || classMirror.getAnnotation(CEYLON_PACKAGE_ANNOTATION) != null)
            return null;
        
        // find its package
        String pkgName = getPackageNameForQualifiedClassName(classMirror);
        
        if (pkgName.equals("java.lang")) {
            module = getJDKBaseModule();
        }
        
        List<Declaration> decls = new ArrayList<Declaration>();
        boolean[] alreadyExists = new boolean[1];
        Declaration decl = getOrCreateDeclaration(module, classMirror, declarationType,
                decls, alreadyExists);
        
        if (alreadyExists[0]) {
            return decl;
        }

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

    public String getPackageNameForQualifiedClassName(String pkg, String qualifiedName){
        // Java array classes we pretend come from java.lang
        if(qualifiedName.startsWith(CEYLON_OBJECT_ARRAY)
                || qualifiedName.startsWith(CEYLON_BOOLEAN_ARRAY)
                || qualifiedName.startsWith(CEYLON_BYTE_ARRAY)
                || qualifiedName.startsWith(CEYLON_SHORT_ARRAY)
                || qualifiedName.startsWith(CEYLON_INT_ARRAY)
                || qualifiedName.startsWith(CEYLON_LONG_ARRAY)
                || qualifiedName.startsWith(CEYLON_FLOAT_ARRAY)
                || qualifiedName.startsWith(CEYLON_DOUBLE_ARRAY)
                || qualifiedName.startsWith(CEYLON_CHAR_ARRAY))
            return "java.lang";
        else
            return unquotePackageName(pkg);
        
    }
    
    protected String getPackageNameForQualifiedClassName(ClassMirror classMirror) {
        return getPackageNameForQualifiedClassName(classMirror.getPackage().getQualifiedName(), classMirror.getQualifiedName());
    }
    
    private String unquotePackageName(PackageMirror pkg) {
        return unquotePackageName(pkg.getQualifiedName());
    }

    private String unquotePackageName(String pkg) {
        return JvmBackendUtil.removeChar('$', pkg);
    }

    private void setContainer(ClassMirror classMirror, Declaration d, LazyPackage pkg) {
        // add it to its package if it's not an inner class
        if(!classMirror.isInnerClass() && !classMirror.isLocalClass()){
            d.setContainer(pkg);
            d.setScope(pkg);
            pkg.addCompiledMember(d);
            if(d instanceof LazyInterface && ((LazyInterface) d).isCeylon()){
                setInterfaceCompanionClass(d, null, pkg);
            }
            ModelUtil.setVisibleScope(d);
        }else if(classMirror.isLocalClass() && !classMirror.isInnerClass()){
            // set its container to the package for now, but don't add it to the package as a member because it's not
            Scope localContainer = getLocalContainer(pkg, classMirror, d);
            if(localContainer != null){
                d.setContainer(localContainer);
                d.setScope(localContainer);
                // do not add it as member, it has already been registered by getLocalContainer
            }else{
                d.setContainer(pkg);
                d.setScope(pkg);
            }
            ((LazyElement)d).setLocal(true);
        }else if(d instanceof ClassOrInterface || d instanceof TypeAlias){
            // do overloads later, since their container is their abstract superclass's container and
            // we have to set that one first
            if(d instanceof Class == false || !((Class)d).isOverloaded()){
                ClassOrInterface container = getContainer(pkg.getModule(), classMirror);
                d.setContainer(container);
                d.setScope(container);
                if(d instanceof LazyInterface && ((LazyInterface) d).isCeylon()){
                    setInterfaceCompanionClass(d, container, pkg);
                }
                // let's not trigger lazy-loading
                ((LazyContainer)container).addMember(d);
                ModelUtil.setVisibleScope(d);
                // now we can do overloads
                if(d instanceof Class && ((Class)d).getOverloads() != null){
                    for(Declaration overload : ((Class)d).getOverloads()){
                        overload.setContainer(container);
                        overload.setScope(container);
                        // let's not trigger lazy-loading
                        ((LazyContainer)container).addMember(overload);
                        ModelUtil.setVisibleScope(overload);
                    }
                }
            }
        }
    }

    private void setInterfaceCompanionClass(Declaration d, ClassOrInterface container, LazyPackage pkg) {
        // find its companion class in its real container
        ClassMirror containerMirror = null;
        if(container instanceof LazyClass){
            containerMirror = ((LazyClass) container).classMirror;
        }else if(container instanceof LazyInterface){
            // container must be a LazyInterface, as TypeAlias doesn't contain anything
            containerMirror = ((LazyInterface)container).companionClass;
            if(containerMirror == null){
                throw new ModelResolutionException("Interface companion class for "+container.getQualifiedNameString()+" not set up");
            }
        }
        String companionName;
        if(containerMirror != null)
            companionName = containerMirror.getFlatName() + "$" + NamingBase.suffixName(NamingBase.Suffix.$impl, d.getName());
        else{
            // toplevel
            String p = pkg.getNameAsString();
            companionName = "";
            if(!p.isEmpty())
                companionName =  p + ".";
            companionName +=  NamingBase.suffixName(NamingBase.Suffix.$impl, d.getName());
        }
        ClassMirror companionClass = lookupClassMirror(pkg.getModule(), companionName);
        if(companionClass == null){
            ((Interface)d).setCompanionClassNeeded(false);
        }
        ((LazyInterface)d).companionClass = companionClass;
    }
    
    private Scope getLocalContainer(Package pkg, ClassMirror classMirror, Declaration declaration) {
        AnnotationMirror localContainerAnnotation = classMirror.getAnnotation(CEYLON_LOCAL_CONTAINER_ANNOTATION);
        String qualifier = getAnnotationStringValue(classMirror, CEYLON_LOCAL_DECLARATION_ANNOTATION, "qualifier");
        
        // deal with types local to functions in the body of toplevel non-lazy attributes, whose container is ultimately the package
        Boolean isPackageLocal = getAnnotationBooleanValue(classMirror, CEYLON_LOCAL_DECLARATION_ANNOTATION, "isPackageLocal");
        if(BooleanUtil.isTrue(isPackageLocal)){
            // make sure it still knows it's a local
            declaration.setQualifier(qualifier);
            return null;
        }

        LocalDeclarationContainer methodDecl = null;
        // we get a @LocalContainer annotation for local interfaces
        if(localContainerAnnotation != null){
            methodDecl = (LocalDeclarationContainer) findLocalContainerFromAnnotationAndSetCompanionClass(pkg, (Interface) declaration, localContainerAnnotation);
        }else{
            // all the other cases stay where they belong
            MethodMirror method = classMirror.getEnclosingMethod();
            if(method == null)
                return null;
            
            // see where that method belongs
            ClassMirror enclosingClass = method.getEnclosingClass();
            while(enclosingClass.isAnonymous()){
                // this gives us the method in which the anonymous class is, which should be the one we're looking for
                method = enclosingClass.getEnclosingMethod();
                if(method == null)
                    return null;
                // and the method's containing class
                enclosingClass = method.getEnclosingClass();
            }
            
            // if we are in a setter class, the attribute is declared in the getter class, so look for its declaration there
            TypeMirror getterClass = (TypeMirror) getAnnotationValue(enclosingClass, CEYLON_SETTER_ANNOTATION, "getterClass");
            boolean isSetter = false;
            // we use void.class as default value
            if(getterClass != null && !getterClass.isPrimitive()){
                enclosingClass = getterClass.getDeclaredClass();
                isSetter = true;
            }
            
            String javaClassName = enclosingClass.getQualifiedName();
            
            // make sure we don't go looking in companion classes
            if(javaClassName.endsWith(NamingBase.Suffix.$impl.name()))
                javaClassName = javaClassName.substring(0, javaClassName.length() - 5);
            
            // find the enclosing declaration
            Declaration enclosingClassDeclaration = convertToDeclaration(pkg.getModule(), javaClassName, DeclarationType.TYPE);
            if(enclosingClassDeclaration instanceof ClassOrInterface){
                ClassOrInterface containerDecl = (ClassOrInterface) enclosingClassDeclaration;
                // now find the method's declaration 
                // FIXME: find the proper overload if any
                String name = method.getName();
                if(method.isConstructor() || name.startsWith(NamingBase.Prefix.$default$.toString())){
                    methodDecl = (LocalDeclarationContainer) containerDecl;
                }else{
                    // this is only for error messages
                    String type;
                    // lots of special cases
                    if(isStringAttribute(method)){
                        name = "string";
                        type = "attribute";
                    }else if(isHashAttribute(method)){
                        name = "hash";
                        type = "attribute";
                    }else if(isGetter(method)) {
                        // simple attribute
                        name = getJavaAttributeName(name);
                        type = "attribute";
                    }else if(isSetter(method)) {
                        // simple attribute
                        name = getJavaAttributeName(name);
                        type = "attribute setter";
                        isSetter = true;
                    }else{
                        type = "method";
                    }
                    // strip any escaping or private suffix
                    // it can be foo$priv$canonical so get rid of that one first
                    if (name.endsWith(NamingBase.Suffix.$canonical$.toString())) {
                        name = name.substring(0, name.length()-11);
                    }
                    name = JvmBackendUtil.strip(name, true, method.isPublic() || method.isProtected() || method.isDefaultAccess());
                    if(name.indexOf('$') > 0){
                        // may be a default parameter expression? get the method name which is first
                        name = name.substring(0, name.indexOf('$'));
                    }

                    methodDecl = (LocalDeclarationContainer) containerDecl.getDirectMember(name, null, false);

                    if(methodDecl == null)
                        throw new ModelResolutionException("Failed to load outer "+type+" " + name 
                                + " for local type " + classMirror.getQualifiedName().toString());

                    // if it's a setter we wanted, let's get it
                    if(isSetter){
                        LocalDeclarationContainer setter = (LocalDeclarationContainer) ((Value)methodDecl).getSetter();
                        if(setter == null)
                            throw new ModelResolutionException("Failed to load outer "+type+" " + name 
                                    + " for local type " + classMirror.getQualifiedName().toString());
                        methodDecl = setter;
                    }
                }
            }else if(enclosingClassDeclaration instanceof LazyFunction){
                // local and toplevel methods
                methodDecl = (LazyFunction)enclosingClassDeclaration;
            }else if(enclosingClassDeclaration instanceof LazyValue){
                // local and toplevel attributes
                if(enclosingClassDeclaration.isToplevel() && method.getName().equals(NamingBase.Unfix.set_.name()))
                    isSetter = true;
                if(isSetter){
                    LocalDeclarationContainer setter = (LocalDeclarationContainer) ((LazyValue)enclosingClassDeclaration).getSetter();
                    if(setter == null)
                        throw new ModelResolutionException("Failed to toplevel attribute setter " + enclosingClassDeclaration.getName() 
                                + " for local type " + classMirror.getQualifiedName().toString());
                    methodDecl = setter;
                }else
                    methodDecl = (LazyValue)enclosingClassDeclaration;
            }else{
                throw new ModelResolutionException("Unknown container type " + enclosingClassDeclaration 
                        + " for local type " + classMirror.getQualifiedName().toString());
            }
        }

        // we have the method, now find the proper local qualifier if any
        if(qualifier == null)
            return null;
        declaration.setQualifier(qualifier);
        methodDecl.addLocalDeclaration(declaration);
        return methodDecl;
    }
    
    private Scope findLocalContainerFromAnnotationAndSetCompanionClass(Package pkg, Interface declaration, AnnotationMirror localContainerAnnotation) {
        @SuppressWarnings("unchecked")
        List<String> path = (List<String>) localContainerAnnotation.getValue("path");
        // we start at the package
        Scope scope = pkg;
        for(String name : path){
            scope = (Scope) getDirectMember(scope, name);
        }
        String companionClassName = (String) localContainerAnnotation.getValue("companionClassName");
        if(companionClassName == null || companionClassName.isEmpty()){
            declaration.setCompanionClassNeeded(false);
            return scope;
        }
        ClassMirror container;
        Scope javaClassScope;
        if(scope instanceof TypedDeclaration && ((TypedDeclaration) scope).isMember())
            javaClassScope = scope.getContainer();
        else
            javaClassScope = scope;
        
        if(javaClassScope instanceof LazyInterface){
            container = ((LazyInterface)javaClassScope).companionClass;
        }else if(javaClassScope instanceof LazyClass){
            container = ((LazyClass) javaClassScope).classMirror;
        }else if(javaClassScope instanceof LazyValue){
            container = ((LazyValue) javaClassScope).classMirror;
        }else if(javaClassScope instanceof LazyFunction){
            container = ((LazyFunction) javaClassScope).classMirror;
        }else if(javaClassScope instanceof SetterWithLocalDeclarations){
            container = ((SetterWithLocalDeclarations) javaClassScope).classMirror;
        }else{
            throw new ModelResolutionException("Unknown scope class: "+javaClassScope);
        }
        String qualifiedCompanionClassName = container.getQualifiedName() + "$" + companionClassName;
        ClassMirror companionClassMirror = lookupClassMirror(pkg.getModule(), qualifiedCompanionClassName);
        if(companionClassMirror == null)
            throw new ModelResolutionException("Could not find companion class mirror: "+qualifiedCompanionClassName);
        ((LazyInterface)declaration).companionClass = companionClassMirror;
        return scope;
    }
    
    /**
     * Looks for a direct member of type ClassOrInterface. We're not using Class.getDirectMember()
     * because it skips object types and we want them.
     */
    public static Declaration getDirectMember(Scope container, String name) {
        if(name.isEmpty())
            return null;
        boolean wantsSetter = false;
        if(name.startsWith(NamingBase.Suffix.$setter$.name())){
            wantsSetter = true;
            name = name.substring(8);
        }
            
        if(Character.isDigit(name.charAt(0))){
            // this is a local type we have a different accessor for it
            return ((LocalDeclarationContainer)container).getLocalDeclaration(name);
        }
        // don't even try using getDirectMember except on Package,
        // because it will fail miserably during completion, since we
        // will for instance have only anonymous types first, before we load their anonymous values, and
        // when we go looking for them we won't be able to find them until we add their anonymous values,
        // which is too late
        if(container instanceof Package){
            // don't use Package.getMembers() as it loads the whole package
            Declaration result = container.getDirectMember(name, null, false);
            return selectTypeOrSetter(result, wantsSetter);
        }else{
            // must be a Declaration
            for(Declaration member : container.getMembers()){
                if(!member.getName().equals(name))
                    continue;
                Declaration result = selectTypeOrSetter(member, wantsSetter);
                if(result != null)
                    return result;
            }
        }
        // not found
        return null;
    }
    
    private static Declaration selectTypeOrSetter(Declaration member, boolean wantsSetter) {
        // if we found a type or a method/value we're good to go
        if (member instanceof ClassOrInterface
                || member instanceof Constructor
                || member instanceof Function) {
            return member;
        }
        // if it's a Value return its object type by preference, the member otherwise
        if (member instanceof Value){
            TypeDeclaration typeDeclaration = ((Value) member).getTypeDeclaration();
            if(typeDeclaration instanceof Class && typeDeclaration.isAnonymous())
                return typeDeclaration;
            // did we want the setter?
            if(wantsSetter)
                return ((Value)member).getSetter();
            // must be a non-object value
            return member;
        }
        return null;
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
            return (ClassOrInterface) convertToDeclaration(module, classMirror.getEnclosingClass(), DeclarationType.TYPE);
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
        
        try{
            // make it
            switch(type){
            case ATTRIBUTE:
                decl = makeToplevelAttribute(classMirror);
                setDeclarationVisibilityAndDeprecation(decl, classMirror, classMirror, classMirror, true);
                break;
            case METHOD:
                decl = makeToplevelMethod(classMirror);
                setDeclarationVisibilityAndDeprecation(decl, classMirror, classMirror, classMirror, true);
                break;
            case OBJECT:
                // we first make a class
                Declaration objectClassDecl = makeLazyClass(classMirror, null, null);
                typeDeclarationsByName.put(key, objectClassDecl);
                decls.add(objectClassDecl);
                // then we make a value for it, if it's not an inline object expr
                if(objectClassDecl.isNamed()){
                    Declaration objectDecl = makeToplevelAttribute(classMirror);
                    valueDeclarationsByName.put(key, objectDecl);
                    decls.add(objectDecl);
                    // which one did we want?
                    decl = declarationType == DeclarationType.TYPE ? objectClassDecl : objectDecl;
                    setDeclarationVisibilityAndDeprecation(objectDecl, classMirror, classMirror, classMirror, true);
                }else{
                    decl = objectClassDecl;
                }
                setDeclarationVisibilityAndDeprecation(objectClassDecl, classMirror, classMirror, classMirror, true);
                break;
            case CLASS:
                if(classMirror.getAnnotation(CEYLON_ALIAS_ANNOTATION) != null){
                    decl = makeClassAlias(classMirror);
                    setDeclarationVisibilityAndDeprecation(decl, classMirror, classMirror, classMirror, true);
                }else if(classMirror.getAnnotation(CEYLON_TYPE_ALIAS_ANNOTATION) != null){
                    decl = makeTypeAlias(classMirror);
                    setDeclarationVisibilityAndDeprecation(decl, classMirror, classMirror, classMirror, true);
                }else{
                    final List<MethodMirror> constructors = getClassConstructors(classMirror);
                    if (!constructors.isEmpty()) {
                        Boolean hasConstructors = hasConstructors(classMirror);
                        if (constructors.size() > 1) {
                            if (hasConstructors == null || !hasConstructors) {
                                decl = makeOverloadedConstructor(constructors, classMirror, decls, isCeylon);
                            } else {
                                decl = makeLazyClass(classMirror, null, null);
                                setDeclarationVisibilityAndDeprecation(decl, classMirror, classMirror, classMirror, isCeylon);
                            }
                        } else {
                            if (hasConstructors == null || !hasConstructors) {
                                // single constructor
                                MethodMirror constructor = constructors.get(0);
                                // if the class and constructor have different visibility, we pretend there's an overload of one
                                // if it's a ceylon class we don't care that they don't match sometimes, like for inner classes
                                // where the constructor is protected because we want to use an accessor, in this case the class
                                // visibility is to be used
                                if(isCeylon || getJavaVisibility(classMirror) == getJavaVisibility(constructor)){
                                    decl = makeLazyClass(classMirror, null, constructor);
                                    setDeclarationVisibilityAndDeprecation(decl, classMirror, classMirror, classMirror, isCeylon);
                                }else{
                                    decl = makeOverloadedConstructor(constructors, classMirror, decls, isCeylon);
                                }
                            } else {
                                decl = makeLazyClass(classMirror, null, null);
                                setDeclarationVisibilityAndDeprecation(decl, classMirror, classMirror, classMirror, isCeylon);
                            }
                        }
                    } else if(isCeylon && classMirror.getAnnotation(CEYLON_OBJECT_ANNOTATION) != null) {
                        // objects don't need overloading stuff
                        decl = makeLazyClass(classMirror, null, null);
                        setDeclarationVisibilityAndDeprecation(decl, classMirror, classMirror, classMirror, isCeylon);
                    } else if(getJavaVisibility(classMirror) != JavaVisibility.PRIVATE){
                        Class klass = (Class)makeOverloadedConstructor(constructors, classMirror, decls, isCeylon);
                        decl = klass;
                        LazyClass subdecl = makeLazyClass(classMirror, klass, null);
                        // no visibility for subdecl (private)
                        subdecl.setOverloaded(true);
                        klass.getOverloads().add(subdecl);
                        decls.add(subdecl);
                    } else {
                        // private class does not need a constructor
                        decl = makeLazyClass(classMirror, null, null);
                    }
                    if (!isCeylon) {
                        setSealedFromConstructorMods(decl, constructors);
                    }
                }
                break;
            case INTERFACE:
                if(classMirror.getAnnotation(CEYLON_ALIAS_ANNOTATION) != null){
                    decl = makeInterfaceAlias(classMirror);
                }else{
                    decl = makeLazyInterface(classMirror);
                }
                setDeclarationVisibilityAndDeprecation(decl, classMirror, classMirror, classMirror, isCeylon);
                break;
            }
        }catch(ModelResolutionException x){
            // FIXME: this may not be the best thing to do, perhaps we should have an erroneous Class,Interface,Function
            // etc, like javac's model does?
            decl = logModelResolutionException(x, null, "Failed to load declaration "+classMirror).getDeclaration();
        }

        // objects have special handling above
        if(type != ClassType.OBJECT){
            declarationCache.put(key, decl);
            decls.add(decl);
        }
        
        return decl;
    }
    
    /** Returns:
     * <ul>
     * <li>true if the class has named constructors ({@code @Class(...constructors=true)}).</li>
     * <li>false if the class has an initializer constructor.</li>
     * <li>null if the class lacks {@code @Class} (i.e. is not a Ceylon class).</li>
     * </ul>
     * @param classMirror
     * @return
     */
    private Boolean hasConstructors(ClassMirror classMirror) {
        AnnotationMirror a = classMirror.getAnnotation(CEYLON_CLASS_ANNOTATION);
        Boolean hasConstructors;
        if (a != null) {
            hasConstructors = (Boolean)a.getValue("constructors");
            if (hasConstructors == null) {
                hasConstructors = false;
            }
        } else {
            hasConstructors = null;
        }
        return hasConstructors;
    }
    
    private boolean isDefaultNamedCtor(ClassMirror classMirror,
            MethodMirror ctor) {
        return classMirror.getName().equals(getCtorName(ctor));
    }
    
    private String getCtorName(MethodMirror ctor) {
        AnnotationMirror nameAnno = ctor.getAnnotation(CEYLON_NAME_ANNOTATION);
        if (nameAnno != null) {
            return (String)nameAnno.getValue();
        } else {
            return null;
        }
    }
    
    private void setSealedFromConstructorMods(Declaration decl,
            final List<MethodMirror> constructors) {
        boolean effectivelySealed = true;
        for (MethodMirror ctor : constructors) {
            if (ctor.isPublic() || ctor.isProtected()) {
                effectivelySealed = false;
                break;
            }
        }
        if (effectivelySealed && decl instanceof Class) {
            Class type = (Class)decl;
            type.setSealed(effectivelySealed);
            if (type.getOverloads() != null) {
                for (Declaration oload : type.getOverloads()) {
                    ((Class)oload).setSealed(effectivelySealed);
                }
            }
        }
    }

    private Declaration makeOverloadedConstructor(List<MethodMirror> constructors, ClassMirror classMirror, List<Declaration> decls, boolean isCeylon) {
        // If the class has multiple constructors we make a copy of the class
        // for each one (each with it's own single constructor) and make them
        // a subclass of the original
        Class supercls = makeLazyClass(classMirror, null, null);
        // the abstraction class gets the class modifiers
        setDeclarationVisibilityAndDeprecation(supercls, classMirror, classMirror, classMirror, isCeylon);
        supercls.setAbstraction(true);
        List<Declaration> overloads = new ArrayList<Declaration>(constructors.size());
        // all filtering is done in getClassConstructors
        for (MethodMirror constructor : constructors) {
            LazyClass subdecl = makeLazyClass(classMirror, supercls, constructor);
            // the subclasses class get the constructor modifiers
            setDeclarationVisibilityAndDeprecation(subdecl, constructor, constructor, classMirror, isCeylon);
            subdecl.setOverloaded(true);
            overloads.add(subdecl);
            decls.add(subdecl);
        }
        supercls.setOverloads(overloads);
        return supercls;
    }

    private void setDeclarationVisibilityAndDeprecation(Declaration decl, AccessibleMirror mirror, AnnotatedMirror annotatedMirror, ClassMirror classMirror, boolean isCeylon) {
        if(isCeylon){
            // when we're in a local type somewhere we must turn public declarations into package or protected ones, so
            // we have to check the shared annotation
            decl.setShared(mirror.isPublic() || annotatedMirror.getAnnotation(CEYLON_LANGUAGE_SHARED_ANNOTATION) != null);
        }else{
            decl.setShared(mirror.isPublic() || (mirror.isDefaultAccess() && classMirror.isInnerClass()) || mirror.isProtected());
            decl.setPackageVisibility(mirror.isDefaultAccess());
            decl.setProtectedVisibility(mirror.isProtected());
        }
        decl.setDeprecated(isDeprecated(annotatedMirror));
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
        if(!Versions.isJvmBinaryVersionSupported(major.intValue(), minor.intValue())){
            logError("Ceylon class " + classMirror.getQualifiedName() + " was compiled by an incompatible version of the Ceylon compiler"
                    +"\nThe class was compiled using "+major+"."+minor+"."
                    +"\nThis compiler supports "+Versions.JVM_BINARY_MAJOR_VERSION+"."+Versions.JVM_BINARY_MINOR_VERSION+"."
                    +"\nPlease try to recompile your module using a compatible compiler."
                    +"\nBinary compatibility will only be supported after Ceylon 1.2.");
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
                if(!checkReifiedTypeDescriptors(tpCount, classMirror.getQualifiedName(), methodMirror, true))
                    continue;
            }
            
            constructors.add(methodMirror);
        }
        return constructors;
    }

    private boolean checkReifiedTypeDescriptors(int tpCount, String containerName, MethodMirror methodMirror, boolean isConstructor) {
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
                logError("Constructor for '"+containerName+"' should take "+tpCount
                        +" reified type arguments (TypeDescriptor) but has '"+actualTypeDescriptorParameters+"': skipping constructor.");
            else
                logError("Function '"+containerName+"."+methodMirror.getName()+"' should take "+tpCount
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

    protected LazyFunction makeToplevelMethod(ClassMirror classMirror) {
        LazyFunction method = new LazyFunction(classMirror, this);
        return method;
    }
    
    protected LazyClass makeLazyClass(ClassMirror classMirror, Class superClass, MethodMirror initOrDefaultConstructor) {
        LazyClass klass = new LazyClass(classMirror, this, superClass, initOrDefaultConstructor);
        AnnotationMirror objectAnnotation = classMirror.getAnnotation(CEYLON_OBJECT_ANNOTATION);
        if(objectAnnotation != null){
            klass.setAnonymous(true);
            // isFalse will only consider non-null arguments, and we default to true if null
            if(BooleanUtil.isFalse((Boolean) objectAnnotation.getValue("named")))
                klass.setNamed(false);
        }
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
        // hack to make Throwable sealed until ceylon/ceylon.language#408 is fixed
        klass.setSealed(classMirror.getAnnotation(CEYLON_LANGUAGE_SEALED_ANNOTATION) != null
                || CEYLON_LANGUAGE.equals(classMirror.getPackage().getQualifiedName()) && "Throwable".equals(classMirror.getName()));
        boolean actual = classMirror.getAnnotation(CEYLON_LANGUAGE_ACTUAL_ANNOTATION) != null;
        klass.setActual(actual);
        klass.setActualCompleter(this);
        klass.setFinal(classMirror.isFinal());
        klass.setStaticallyImportable(!klass.isCeylon() && classMirror.isStatic());
        return klass;
    }

    protected LazyInterface makeLazyInterface(ClassMirror classMirror) {
        LazyInterface iface = new LazyInterface(classMirror, this);
        iface.setSealed(classMirror.getAnnotation(CEYLON_LANGUAGE_SEALED_ANNOTATION) != null);
        iface.setDynamic(classMirror.getAnnotation(CEYLON_DYNAMIC_ANNOTATION) != null);
        iface.setStaticallyImportable(!iface.isCeylon());
        return iface;
    }

    public Declaration convertToDeclaration(Module module, String typeName, DeclarationType declarationType)  {
        synchronized(getLock()){
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
                } else if ("java.lang.Exception".equals(typeName)) {
                    // FIXME: this being here is highly dubious
                    return convertToDeclaration(modules.getLanguageModule(), "ceylon.language.Exception", declarationType);
                } else if ("java.lang.Annotation".equals(typeName)) {
                    // FIXME: this being here is highly dubious
                    // here we prefer Annotation over ConstrainedAnnotation but that's fine
                    return convertToDeclaration(modules.getLanguageModule(), "ceylon.language.Annotation", declarationType);
                }
                ClassMirror classMirror;
                try{
                    classMirror = lookupClassMirror(module, typeName);
                }catch(NoClassDefFoundError x){
                    // FIXME: this may not be the best thing to do. If the class is not there we don't know what type of declaration
                    // to return, but perhaps if we use annotation scanner rather than reflection we can figure it out, at least
                    // in cases where the supertype is missing, which throws in reflection at class load.
                    return logModelResolutionException(x.getMessage(), module, "Unable to load type "+typeName).getDeclaration();
                }
                if (classMirror == null) {
                    // special case when bootstrapping because we may need to pull the decl from the typechecked model
                    if(isBootstrap && typeName.startsWith(CEYLON_LANGUAGE+".")){
                        Declaration languageDeclaration = findLanguageModuleDeclarationForBootstrap(typeName);
                        if(languageDeclaration != null)
                            return languageDeclaration;
                    }

                    throw new ModelResolutionException("Failed to resolve "+typeName);
                }
                // we only allow source loading when it's java code we're compiling in the same go
                // (well, technically before the ceylon code)
                if(classMirror.isLoadedFromSource() && !classMirror.isJavaSource())
                    return null;
                return convertToDeclaration(module, classMirror, declarationType);
            }finally{
                timer.stopIgnore(TIMER_MODEL_LOADER_CATEGORY);
            }
        }
    }

    private Type newUnknownType() {
        return new UnknownType(typeFactory).getType();
    }

    protected TypeParameter safeLookupTypeParameter(Scope scope, String name) {
        TypeParameter param = lookupTypeParameter(scope, name);
        if(param == null)
            throw new ModelResolutionException("Type param "+name+" not found in "+scope);
        return param;
    }
    
    private TypeParameter lookupTypeParameter(Scope scope, String name) {
        if(scope instanceof Function){
            Function m = (Function) scope;
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
        }else if (scope instanceof Constructor) {
            return lookupTypeParameter(scope.getContainer(), name);
        }else if(scope instanceof Value
                || scope instanceof Setter){
            Declaration decl = (Declaration) scope;
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
    
    public LazyPackage findExistingPackage(Module module, String pkgName) {
        synchronized(getLock()){
            String quotedPkgName = JVMModuleUtil.quoteJavaKeywords(pkgName);
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

    public LazyPackage findOrCreatePackage(Module module, final String pkgName)  {
        synchronized(getLock()){
            String quotedPkgName = JVMModuleUtil.quoteJavaKeywords(pkgName);
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
                pkg.setName(Arrays.asList(pkgName.split("\\.")));
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
    }

    public void loadPackageDescriptors()  {
        synchronized(getLock()){
            for(Package pkg : packagesByName.values()){
                loadPackageDescriptor(pkg);
            }
            packageDescriptorsNeedLoading  = true;
        }
    }

    private void loadPackageDescriptor(Package pkg) {
        // Don't try to load a package descriptor for ceylon.language 
        // if we're bootstrapping
        if (isBootstrap 
                && pkg.getQualifiedNameString().startsWith(CEYLON_LANGUAGE)) {
            return;
        }
        
        // let's not load package descriptors for Java modules
        if(pkg.getModule() != null 
                && ((LazyModule)pkg.getModule()).isJava()){
            pkg.setShared(true);
            return;
        }
        String quotedQualifiedName = JVMModuleUtil.quoteJavaKeywords(pkg.getQualifiedNameString());
        // FIXME: not sure the toplevel package can have a package declaration
        String className = quotedQualifiedName.isEmpty() 
                ? NamingBase.PACKAGE_DESCRIPTOR_CLASS_NAME 
                : quotedQualifiedName + "." + NamingBase.PACKAGE_DESCRIPTOR_CLASS_NAME;
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
            if(!ModelUtil.equalModules(module,getLanguageModule())
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
            return findModule(moduleName, JDKUtils.jdk.version);
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
    protected Module findOrCreateModule(String moduleName, String version)  {
        synchronized(getLock()){
            boolean isJdk = false;
            boolean defaultModule = false;

            // make sure it isn't loaded
            Module module = getLoadedModule(moduleName, version);
            if(module != null)
                return module;

            if(JDKUtils.isJDKModule(moduleName) || JDKUtils.isOracleJDKModule(moduleName)){
                isJdk = true;
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
            if (isJdk && module instanceof LazyModule) {
                ((LazyModule)module).setJava(true);
                module.setNative(Backend.Java.nativeAnnotation);
            }

            // FIXME: this can't be that easy.
            if(isJdk)
                module.setAvailable(true);
            module.setDefault(defaultModule);
            return module;
        }
    }

    public boolean loadCompiledModule(Module module)  {
        synchronized(getLock()){
            if(module.isDefault())
                return false;
            String pkgName = module.getNameAsString();
            if(pkgName.isEmpty())
                return false;
            String moduleClassName = pkgName + "." + NamingBase.MODULE_DESCRIPTOR_CLASS_NAME;
            logVerbose("[Trying to look up module from "+moduleClassName+"]");
            ClassMirror moduleClass = loadClass(module, pkgName, moduleClassName);
            if(moduleClass == null){
                // perhaps we have an old module?
                String oldModuleClassName = pkgName + "." + NamingBase.OLD_MODULE_DESCRIPTOR_CLASS_NAME;
                logVerbose("[Trying to look up older module descriptor from "+oldModuleClassName+"]");
                ClassMirror oldModuleClass = loadClass(module, pkgName, oldModuleClassName);
                // keep it only if it has a module annotation, otherwise it could be a normal value
                if(oldModuleClass != null && oldModuleClass.getAnnotation(CEYLON_MODULE_ANNOTATION) != null)
                    moduleClass = oldModuleClass;
            }
            if(moduleClass != null){
                // load its module annotation
                return loadCompiledModule(module, moduleClass, moduleClassName);
            }
            // give up
            return false;
        }
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

        setAnnotations(module, moduleClass);
        
        List<AnnotationMirror> imports = getAnnotationArrayValue(moduleClass, CEYLON_MODULE_ANNOTATION, "dependencies");
        if(imports != null){
            for (AnnotationMirror importAttribute : imports) {
                String dependencyName = (String) importAttribute.getValue("name");
                if (dependencyName != null) {
                    String dependencyVersion = (String) importAttribute.getValue("version");

                    Module dependency = moduleManager.getOrCreateModule(ModuleManager.splitModuleName(dependencyName), dependencyVersion);

                    Boolean optionalVal = (Boolean) importAttribute.getValue("optional");

                    Boolean exportVal = (Boolean) importAttribute.getValue("export");

                    String backend = null; // TODO (String) importAttribute.getValue("native");

                    ModuleImport moduleImport = moduleManager.findImport(module, dependency);
                    if (moduleImport == null) {
                        boolean optional = optionalVal != null && optionalVal;
                        boolean export = exportVal != null && exportVal;
                        moduleImport = new ModuleImport(dependency, optional, export, backend);
                        module.addImport(moduleImport);
                    }
                }
            }
        }
        
        module.setAvailable(true);
        
        modules.getListOfModules().add(module);
        Module languageModule = modules.getLanguageModule();
        module.setLanguageModule(languageModule);
        if(!ModelUtil.equalModules(module, languageModule)){
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
    private List<String> getAnnotationStringValues(AnnotationMirror annotation, String field) {
        return (List<String>)annotation.getValue(field);
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
    public void complete(LazyInterface iface)  {
        synchronized(getLock()){
            timer.startIgnore(TIMER_MODEL_LOADER_CATEGORY);
            complete(iface, iface.classMirror);
            timer.stopIgnore(TIMER_MODEL_LOADER_CATEGORY);
        }
    }

    @Override
    public void completeTypeParameters(LazyInterface iface)  {
        synchronized(getLock()){
            timer.startIgnore(TIMER_MODEL_LOADER_CATEGORY);
            completeTypeParameters(iface, iface.classMirror);
            timer.stopIgnore(TIMER_MODEL_LOADER_CATEGORY);
        }
    }

    @Override
    public void complete(LazyClass klass)  {
        synchronized(getLock()){
            timer.startIgnore(TIMER_MODEL_LOADER_CATEGORY);
            complete(klass, klass.classMirror);
            timer.stopIgnore(TIMER_MODEL_LOADER_CATEGORY);
        }
    }

    @Override
    public void completeTypeParameters(LazyClass klass)  {
        synchronized(getLock()){
            timer.startIgnore(TIMER_MODEL_LOADER_CATEGORY);
            completeTypeParameters(klass, klass.classMirror);
            timer.stopIgnore(TIMER_MODEL_LOADER_CATEGORY);
        }
    }

    @Override
    public void completeTypeParameters(LazyClassAlias lazyClassAlias)  {
        synchronized(getLock()){
            timer.startIgnore(TIMER_MODEL_LOADER_CATEGORY);
            completeLazyAliasTypeParameters(lazyClassAlias, lazyClassAlias.classMirror);
            timer.stopIgnore(TIMER_MODEL_LOADER_CATEGORY);
        }
    }
    
    @Override
    public void completeTypeParameters(LazyInterfaceAlias lazyInterfaceAlias)  {
        synchronized(getLock()){
            timer.startIgnore(TIMER_MODEL_LOADER_CATEGORY);
            completeLazyAliasTypeParameters(lazyInterfaceAlias, lazyInterfaceAlias.classMirror);
            timer.stopIgnore(TIMER_MODEL_LOADER_CATEGORY);
        }
    }

    @Override
    public void completeTypeParameters(LazyTypeAlias lazyTypeAlias)  {
        synchronized(getLock()){
            timer.startIgnore(TIMER_MODEL_LOADER_CATEGORY);
            completeLazyAliasTypeParameters(lazyTypeAlias, lazyTypeAlias.classMirror);
            timer.stopIgnore(TIMER_MODEL_LOADER_CATEGORY);
        }
    }

    @Override
    public void complete(LazyInterfaceAlias alias)  {
        synchronized(getLock()){
            timer.startIgnore(TIMER_MODEL_LOADER_CATEGORY);
            completeLazyAlias(alias, alias.classMirror, CEYLON_ALIAS_ANNOTATION);
            timer.stopIgnore(TIMER_MODEL_LOADER_CATEGORY);
        }
    }
    
    @Override
    public void complete(LazyClassAlias alias)  {
        synchronized(getLock()){
            timer.startIgnore(TIMER_MODEL_LOADER_CATEGORY);
            completeLazyAlias(alias, alias.classMirror, CEYLON_ALIAS_ANNOTATION);

            String constructorName = (String)alias.classMirror.getAnnotation(CEYLON_ALIAS_ANNOTATION).getValue("constructor");
            if (constructorName != null 
                    && !constructorName.isEmpty()) {
                Declaration constructor = alias.getExtendedType().getDeclaration().getMember(constructorName, null, false);
                if (constructor instanceof TypeDeclaration) {
                    alias.setConstructor((TypeDeclaration)constructor);
                } else {
                    logError("class aliased constructor " + constructorName + " which is no longer a constructor of " + alias.getExtendedType().getDeclaration().getQualifiedNameString());
                }
            }
            
            // Find the instantiator method
            MethodMirror instantiator = null;
            ClassMirror instantiatorClass = alias.isToplevel() ? alias.classMirror : alias.classMirror.getEnclosingClass();
            String aliasName = NamingBase.getAliasInstantiatorMethodName(alias);
            for (MethodMirror method : instantiatorClass.getDirectMethods()) {
                if (method.getName().equals(aliasName)) {
                    instantiator = method;
                    break;
                }
            }
            // Read the parameters from the instantiator, rather than the aliased class
            if (instantiator != null) {
                setParameters(alias, alias.classMirror, instantiator, true, alias);
            }
            timer.stopIgnore(TIMER_MODEL_LOADER_CATEGORY);
        }
    }

    @Override
    public void complete(LazyTypeAlias alias)  {
        synchronized(getLock()){
            timer.startIgnore(TIMER_MODEL_LOADER_CATEGORY);
            completeLazyAlias(alias, alias.classMirror, CEYLON_TYPE_ALIAS_ANNOTATION);
            timer.stopIgnore(TIMER_MODEL_LOADER_CATEGORY);
        }
    }

    private void completeLazyAliasTypeParameters(TypeDeclaration alias, ClassMirror mirror) {
        // type parameters
        setTypeParameters(alias, mirror, true);
    }

    private void completeLazyAlias(TypeDeclaration alias, ClassMirror mirror, String aliasAnnotationName) {
        // now resolve the extended type
        AnnotationMirror aliasAnnotation = mirror.getAnnotation(aliasAnnotationName);
        String extendedTypeString = (String) aliasAnnotation.getValue();
        
        Type extendedType = decodeType(extendedTypeString, alias, ModelUtil.getModuleContainer(alias), "alias target");
        alias.setExtendedType(extendedType);
    }

    private void completeTypeParameters(ClassOrInterface klass, ClassMirror classMirror) {
        boolean isCeylon = classMirror.getAnnotation(CEYLON_CEYLON_ANNOTATION) != null;
        setTypeParameters(klass, classMirror, isCeylon);
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
        
        // Set up enumerated constructors before looking at getters,
        // because the type of the getter is the constructor's type
        Boolean hasConstructors = hasConstructors(classMirror);
        if (hasConstructors != null && hasConstructors) {
            ((Class)klass).setConstructors(true);
            for (MethodMirror ctor : getClassConstructors(classMirror)) {
                addConstructor((Class)klass, classMirror, ctor);
            }
        }
        
        // Turn a list of possibly overloaded methods into a map
        // of lists that contain methods with the same name
        Map<String, List<MethodMirror>> methods = new LinkedHashMap<String, List<MethodMirror>>();
        collectMethods(classMirror.getDirectMethods(), methods, isCeylon, isFromJDK);

        if(isCeylon && klass instanceof LazyInterface && JvmBackendUtil.isCompanionClassNeeded(klass)){
            ClassMirror companionClass = ((LazyInterface)klass).companionClass;
            if(companionClass != null)
                collectMethods(companionClass.getDirectMethods(), methods, isCeylon, isFromJDK);
            else
                logWarning("CompanionClass missing for "+klass);
        }

        // Add the methods
        for(List<MethodMirror> methodMirrors : methods.values()){
            boolean isOverloaded = isMethodOverloaded(methodMirrors);
            
            List<Declaration> overloads = null;
            for (MethodMirror methodMirror : methodMirrors) {
                String methodName = methodMirror.getName();
                // same tests as in isMethodOverloaded()
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
                    Function m = addMethod(klass, methodMirror, classMirror, isCeylon, isOverloaded);
                    if (m.isOverloaded()) {
                        overloads = overloads == null ? new ArrayList<Declaration>(methodMirrors.size()) :  overloads;
                        overloads.add(m);
                    }
                }
            }
            
            if (overloads != null && !overloads.isEmpty()) {
                // We create an extra "abstraction" method for overloaded methods
                Function abstractionMethod = addMethod(klass, methodMirrors.get(0), classMirror, isCeylon, false);
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
            if(isFromJDK && !fieldMirror.isPublic() && !fieldMirror.isProtected())
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
                && !isDefaultNamedCtor(classMirror, constructor)
                && (!(klass instanceof LazyClass) || !((LazyClass)klass).isAnonymous()))
            setParameters((Class)klass, classMirror, constructor, isCeylon, klass);
        
        // Now marry-up attributes and parameters)
        if (klass instanceof Class) {
            for (Declaration m : klass.getMembers()) {
                if (JvmBackendUtil.isValue(m)) {
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
            // make sure we handle private postfixes
            name = JvmBackendUtil.strip(name, isCeylon, setter.isPublic());
            Declaration decl = klass.getMember(name, null, false);
            boolean foundGetter = false;
            // skip Java fields, which we only get if there is no getter method, in that case just add the setter method
            if (decl instanceof JavaBeanValue) {
                JavaBeanValue value = (JavaBeanValue)decl;
                // only add the setter if it has the same visibility as the getter
                if (setter.isPublic() && value.mirror.isPublic()
                        || setter.isProtected() && value.mirror.isProtected()
                        || setter.isDefaultAccess() && value.mirror.isDefaultAccess()
                        || (!setter.isPublic() && !value.mirror.isPublic()
                        && !setter.isProtected() && !value.mirror.isProtected()
                        && !setter.isDefaultAccess() && !value.mirror.isDefaultAccess())) {
                    VariableMirror setterParam = setter.getParameters().get(0);
                    Type paramType = obtainType(setterParam.getType(), setterParam, klass, ModelUtil.getModuleContainer(klass), VarianceLocation.INVARIANT,
                            "setter '"+setter.getName()+"'", klass);
                    // only add the setter if it has exactly the same type as the getter
                    if(paramType.isExactly(value.getType())){
                        foundGetter = true;
                        value.setVariable(true);
                        value.setSetterName(setter.getName());
                        if(value.isTransient()){
                            // must be a real setter
                            makeSetter(value, null);
                        }
                    }else
                        logVerbose("Setter parameter type for "+name+" does not match corresponding getter type, adding setter as a method");
                } else {
                    logVerbose("Setter visibility for "+name+" does not match corresponding getter visibility, adding setter as a method");
                }
            }
            
            if(!foundGetter){
                // it was not a setter, it was a method, let's add it as such
                addMethod(klass, setter, classMirror, isCeylon, false);
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
        setAnnotations(klass, classMirror);
        
        // local declarations come last, because they need all members to be completed first
        if(!klass.isAlias()){
            ClassMirror containerMirror = classMirror;
            if(klass instanceof LazyInterface){
                ClassMirror companionClass = ((LazyInterface) klass).companionClass;
                if(companionClass != null)
                    containerMirror = companionClass;
            }
            addLocalDeclarations((LazyContainer) klass, containerMirror, classMirror);
        }
        
        if (!isCeylon) {
            // In java, a class can inherit a public member from a non-public supertype
            for (Declaration d : klass.getMembers()) {
                if (d.isShared()) {
                    d.setVisibleScope(null);
                }
            }
        }
    }

    private void addConstructor(Class klass, ClassMirror classMirror, MethodMirror ctor) {
        boolean isCeylon = (classMirror.getAnnotation(CEYLON_CEYLON_ANNOTATION) != null);
        Constructor constructor = new Constructor();
        constructor.setName(getCtorName(ctor));
        constructor.setContainer(klass);
        constructor.setScope(klass);
        constructor.setUnit(klass.getUnit());
        constructor.setExtendedType(klass.getType());
        setDeclarationVisibilityAndDeprecation(constructor, ctor, ctor, classMirror, isCeylon);
        if (ctor.getAnnotation(CEYLON_ENUMERATED_ANNOTATION) != null) {
            constructor.setAnonymous(true);
            klass.setEnumerated(true);
        }
        setAnnotations(constructor, ctor);
        setParameters(constructor, classMirror, ctor, true, klass);
        klass.addMember(constructor);
    }
    
    private boolean isMethodOverloaded(List<MethodMirror> methodMirrors) {
        // it's overloaded if we have more than one method (non constructor/value)
        boolean one = false;
        for (MethodMirror methodMirror : methodMirrors) {
            // same tests as in complete(ClassOrInterface klass, ClassMirror classMirror)
            if(methodMirror.isConstructor() 
                    || isInstantiator(methodMirror)
                    || isGetter(methodMirror)
                    || isSetter(methodMirror)
                    || isHashAttribute(methodMirror)
                    || isStringAttribute(methodMirror)
                    || methodMirror.getName().equals("hash")
                    || methodMirror.getName().equals("string")){
                break;
            }
            if(one)
                return true;
            one = true;
        }
        return false;
    }

    private void collectMethods(List<MethodMirror> methodMirrors, Map<String,List<MethodMirror>> methods,
                                boolean isCeylon, boolean isFromJDK) {
        for(MethodMirror methodMirror : methodMirrors){
            // We skip members marked with @Ignore
            if(methodMirror.getAnnotation(CEYLON_IGNORE_ANNOTATION) != null)
                continue;
            if(methodMirror.isStaticInit())
                continue;
            if(isCeylon && methodMirror.isStatic()
                    && methodMirror.getAnnotation(CEYLON_ENUMERATED_ANNOTATION) == null)
                continue;
            // FIXME: temporary, because some private classes from the jdk are
            // referenced in private methods but not available
            if(isFromJDK && !methodMirror.isPublic() && !methodMirror.isProtected())
                continue;
            String methodName = methodMirror.getName();
            List<MethodMirror> homonyms = methods.get(methodName);
            if (homonyms == null) {
                homonyms = new LinkedList<MethodMirror>();
                methods.put(methodName, homonyms);
            }
            homonyms.add(methodMirror);
        }
    }
    
    private void addLocalDeclarations(LocalDeclarationContainer container, ClassMirror classContainerMirror, AnnotatedMirror annotatedMirror) {
        if(!needsLocalDeclarations())
            return;
        AnnotationMirror annotation = annotatedMirror.getAnnotation(CEYLON_LOCAL_DECLARATIONS_ANNOTATION);
        if(annotation == null)
            return;
        List<String> values = getAnnotationStringValues(annotation, "value");
        String parentClassName = classContainerMirror.getQualifiedName();
        Package pkg = ModelUtil.getPackageContainer(container);
        Module module = pkg.getModule();
        for(String scope : values){
            // assemble the name with the parent
            String name;
            if(scope.startsWith("::")){
                // interface pulled to toplevel
                name = pkg.getNameAsString() + "." + scope.substring(2);
            }else{
                name = parentClassName;
                name += "$" + scope;
            }
            Declaration innerDecl = convertToDeclaration(module, name, DeclarationType.TYPE);
            if(innerDecl == null)
                throw new ModelResolutionException("Failed to load local type " + name
                        + " for outer type " + container.getQualifiedNameString());
        }
    }

    private boolean isInstantiator(MethodMirror methodMirror) {
        return methodMirror.getName().endsWith("$aliased$");
    }
    
    private boolean isFromJDK(ClassMirror classMirror) {
        String pkgName = unquotePackageName(classMirror.getPackage());
        return JDKUtils.isJDKAnyPackage(pkgName) || JDKUtils.isOracleJDKAnyPackage(pkgName);
    }

    private void setAnnotations(Annotated annotated, AnnotatedMirror classMirror) {
        if (classMirror.getAnnotation(CEYLON_ANNOTATIONS_ANNOTATION) != null) {
            // If the class has @Annotations then use it (in >=1.2 only ceylon.language does)
            Long mods = (Long)getAnnotationValue(classMirror, CEYLON_ANNOTATIONS_ANNOTATION, "modifiers");
            if (mods != null) {
                // If there is a modifiers value then use it to load the modifiers
                for (LanguageAnnotation mod : LanguageAnnotation.values()) {
                    if (mod.isModifier()) {
                        if ((mod.mask & mods) != 0) {
                            annotated.getAnnotations().addAll(mod.makeFromCeylonAnnotation(null));
                        }
                    }
                }
                
            }
            // Load anything else the long way, reading the @Annotation(name=...)
            List<AnnotationMirror> annotations = getAnnotationArrayValue(classMirror, CEYLON_ANNOTATIONS_ANNOTATION);
            if(annotations != null) {
                for(AnnotationMirror annotation : annotations){
                    annotated.getAnnotations().add(readModelAnnotation(annotation));
                }
            }
        } else {
            // If the class lacks @Annotations then set the modifier annotations
            // according to the presence of @Shared$annotation etc
            for (LanguageAnnotation mod : LanguageAnnotation.values()) {
                if (classMirror.getAnnotation(mod.annotationType) != null) {
                    annotated.getAnnotations().addAll(mod.makeFromCeylonAnnotation(classMirror.getAnnotation(mod.annotationType)));
                }
            }
            // Hack for anonymous classes where the getter method has the annotations, 
            // but the typechecker wants them on the Class model. 
            if ((annotated instanceof Class)
                    && ((Class)annotated).isAnonymous()) {
                Class clazz = (Class)annotated;
                Declaration objectValue = clazz.getContainer().getDirectMember(clazz.getName(), null, false);
                if (objectValue != null) {
                    annotated.getAnnotations().addAll(objectValue.getAnnotations());
                }
                
            }
        }

        boolean hasCeylonDeprecated = false;
        for(Annotation a : annotated.getAnnotations()) {
            if (a.getName().equals("deprecated")) {
                hasCeylonDeprecated = true;
                break;
            }
        }

        // Add a ceylon deprecated("") if it's annotated with java.lang.Deprecated
        // and doesn't already have the ceylon annotation
        if (classMirror.getAnnotation(JAVA_DEPRECATED_ANNOTATION) != null) {
            if (!hasCeylonDeprecated) {
                Annotation modelAnnotation = new Annotation();
                modelAnnotation.setName("deprecated");
                modelAnnotation.getPositionalArguments().add("");
                annotated.getAnnotations().add(modelAnnotation);
                hasCeylonDeprecated = true;
            }
        }
        
        // Set "native" annotation
        String nativeBackend = getAnnotationStringValue(classMirror, CEYLON_LANGUAGE_NATIVE_ANNOTATION, "backend");
        if (nativeBackend != null) {
            if (annotated instanceof Declaration) {
                Declaration decl = (Declaration)annotated;
                decl.setNativeBackend(nativeBackend);
                List<Declaration> al = getOverloads(decl);
                if (al == null) {
                    al = new ArrayList<Declaration>(3);
                    al.add(decl);
                    setOverloads(decl, al);
                }
            } else if (annotated instanceof Module) {
                ((Module)annotated).setNative(nativeBackend);
            }
        }
    }

    private boolean isDeprecated(AnnotatedMirror classMirror){
        if (classMirror.getAnnotation(JAVA_DEPRECATED_ANNOTATION) != null)
            return true;
        if (classMirror.getAnnotation(CEYLON_ANNOTATIONS_ANNOTATION) != null) {
            // Load anything else the long way, reading the @Annotation(name=...)
            List<AnnotationMirror> annotations = getAnnotationArrayValue(classMirror, CEYLON_ANNOTATIONS_ANNOTATION);
            if(annotations != null) {
                for(AnnotationMirror annotation : annotations){
                    String name = (String) annotation.getValue();
                    if(name != null && name.equals("deprecated"))
                        return true;
                }
            }
            return false;
        } else {
            // If the class lacks @Annotations then set the modifier annotations
            // according to the presence of @Shared$annotation etc
            return classMirror.getAnnotation(LanguageAnnotation.DEPRECATED.annotationType) != null;
        }
    }
    
    public static List<Declaration> getOverloads(Declaration decl) {
        if (decl instanceof Function) {
            return ((Function)decl).getOverloads();
        }
        else if (decl instanceof Value) {
            return ((Value)decl).getOverloads();
        }
        else if (decl instanceof Class) {
            return ((Class)decl).getOverloads();
        }
        return Collections.emptyList();
    }
    
    public static void setOverloads(Declaration decl, List<Declaration> overloads) {
        if (decl instanceof Function) {
            ((Function)decl).setOverloads(overloads);
        }
        else if (decl instanceof Value) {
            ((Value)decl).setOverloads(overloads);
        }
        else if (decl instanceof Class) {
            ((Class)decl).setOverloads(overloads);
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
            String javaClassName;
            // void.class is the default value, I guess it's a primitive?
            if(javaClassMirror != null && !javaClassMirror.isPrimitive()){
                javaClassName = javaClassMirror.getQualifiedName();
            }else{
                // we get the class name as a string
                String name = (String)member.getValue("javaClassName");
                ClassMirror container = null;
                if(klass instanceof LazyClass){
                    container = ((LazyClass) klass).classMirror;
                }else if(klass instanceof LazyInterface){
                    if(((LazyInterface) klass).isCeylon())
                        container = ((LazyInterface) klass).companionClass;
                    else
                        container = ((LazyInterface) klass).classMirror;
                }
                if(container == null)
                    throw new ModelResolutionException("Unknown container type: " + klass 
                            + " when trying to load inner class " + name);
                javaClassName = container.getQualifiedName()+"$"+name;
            }
            Declaration innerDecl = convertToDeclaration(ModelUtil.getModuleContainer(klass), javaClassName, DeclarationType.TYPE);
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
        Module module = ModelUtil.getModule(klass);
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
            convertToDeclaration(module, innerClass, DeclarationType.TYPE);
            // no need to set its container as that's now handled by convertToDeclaration
        }
    }

    private Function addMethod(ClassOrInterface klass, MethodMirror methodMirror, ClassMirror classMirror, 
                             boolean isCeylon, boolean isOverloaded) {
        
        JavaMethod method = new JavaMethod(methodMirror);
        String methodName = methodMirror.getName();
        
        method.setContainer(klass);
        method.setScope(klass);
        method.setRealName(methodName);
        method.setUnit(klass.getUnit());
        method.setOverloaded(isOverloaded || isOverloadingMethod(methodMirror));
        Type type = null;
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
            method.setName(JvmBackendUtil.strip(methodName, isCeylon, method.isShared()));
        method.setDefaultedAnnotation(methodMirror.isDefault());

        // type params first
        setTypeParameters(method, methodMirror, isCeylon);

        // and its return type
        // do not log an additional error if we had one from checking if it was overriding
        if(type == null)
            type = obtainType(methodMirror.getReturnType(), methodMirror, method, ModelUtil.getModuleContainer(method), VarianceLocation.COVARIANT,
                              "method '"+methodMirror.getName()+"'", klass);
        method.setType(type);
        
        // now its parameters
        if(isEqualsMethod(methodMirror))
            setEqualsParameters(method, methodMirror);
        else
            setParameters(method, classMirror, methodMirror, isCeylon, klass);
        
        method.setUncheckedNullType((!isCeylon && !methodMirror.getReturnType().isPrimitive()) || isUncheckedNull(methodMirror));
        type.setRaw(isRaw(ModelUtil.getModuleContainer(klass), methodMirror.getReturnType()));
        markDeclaredVoid(method, methodMirror);
        markUnboxed(method, methodMirror, methodMirror.getReturnType());
        markTypeErased(method, methodMirror, methodMirror.getReturnType());
        markUntrustedType(method, methodMirror, methodMirror.getReturnType());
        method.setDeprecated(isDeprecated(methodMirror));
        setAnnotations(method, methodMirror);
        
        klass.addMember(method);
        ModelUtil.setVisibleScope(method);
        
        addLocalDeclarations(method, classMirror, methodMirror);

        return method;
    }

    private List<Type> getSignature(Declaration decl) {
        List<Type> result = null;
        if (decl instanceof Functional) {
            Functional func = (Functional)decl;
            if (func.getParameterLists().size() > 0) {
                List<Parameter> params = func.getFirstParameterList().getParameters();
                result = new ArrayList<Type>(params.size());
                for (Parameter p : params) {
                    result.add(p.getType());
                }
            }
        }
        return result;
    }
    
    private boolean isStartOfJavaBeanPropertyName(int codepoint){
        return (codepoint == Character.toUpperCase(codepoint)) || codepoint == '_'; 
    }

    private boolean isNonGenericMethod(MethodMirror methodMirror){
        return !methodMirror.isConstructor() 
                && methodMirror.getTypeParameters().isEmpty();
    }
    
    private boolean isGetter(MethodMirror methodMirror) {
        if(!isNonGenericMethod(methodMirror))
            return false;
        String name = methodMirror.getName();
        boolean matchesGet = name.length() > 3 && name.startsWith("get") 
                && isStartOfJavaBeanPropertyName(name.codePointAt(3)) 
                && !"getString".equals(name) && !"getHash".equals(name) && !"getEquals".equals(name);
        boolean matchesIs = name.length() > 2 && name.startsWith("is") 
                && isStartOfJavaBeanPropertyName(name.codePointAt(2)) 
                && !"isString".equals(name) && !"isHash".equals(name) && !"isEquals".equals(name);
        boolean hasNoParams = methodMirror.getParameters().size() == 0;
        boolean hasNonVoidReturn = (methodMirror.getReturnType().getKind() != TypeKind.VOID);
        boolean hasBooleanReturn = (methodMirror.getReturnType().getKind() == TypeKind.BOOLEAN);
        return (matchesGet && hasNonVoidReturn || matchesIs && hasBooleanReturn) && hasNoParams;
    }
    
    private boolean isSetter(MethodMirror methodMirror) {
        if(!isNonGenericMethod(methodMirror))
            return false;
        String name = methodMirror.getName();
        boolean matchesSet = name.length() > 3 && name.startsWith("set") 
                && isStartOfJavaBeanPropertyName(name.codePointAt(3))
                && !"setString".equals(name) && !"setHash".equals(name) && !"setEquals".equals(name);
        boolean hasOneParam = methodMirror.getParameters().size() == 1;
        boolean hasVoidReturn = (methodMirror.getReturnType().getKind() == TypeKind.VOID);
        return matchesSet && hasOneParam && hasVoidReturn;
    }

    private boolean isHashAttribute(MethodMirror methodMirror) {
        if(!isNonGenericMethod(methodMirror)
                || methodMirror.isStatic())
            return false;
        String name = methodMirror.getName();
        boolean matchesName = "hashCode".equals(name);
        boolean hasNoParams = methodMirror.getParameters().size() == 0;
        return matchesName && hasNoParams;
    }
    
    private boolean isStringAttribute(MethodMirror methodMirror) {
        if(!isNonGenericMethod(methodMirror)
                || methodMirror.isStatic())
            return false;
        String name = methodMirror.getName();
        boolean matchesName = "toString".equals(name);
        boolean hasNoParams = methodMirror.getParameters().size() == 0;
        return matchesName && hasNoParams;
    }

    private boolean isEqualsMethod(MethodMirror methodMirror) {
        if(!isNonGenericMethod(methodMirror)
                || methodMirror.isStatic())
            return false;
        String name = methodMirror.getName();
        if(!"equals".equals(name)
                || methodMirror.getParameters().size() != 1)
            return false;
        VariableMirror param = methodMirror.getParameters().get(0);
        return sameType(param.getType(), OBJECT_TYPE);
    }

    private void setEqualsParameters(Function decl, MethodMirror methodMirror) {
        ParameterList parameters = new ParameterList();
        decl.addParameterList(parameters);
        Parameter parameter = new Parameter();
        Value value = new Value();
        parameter.setModel(value);
        value.setInitializerParameter(parameter);
        value.setUnit(decl.getUnit());
        value.setContainer((Scope) decl);
        value.setScope((Scope) decl);
        parameter.setName("that");
        value.setName("that");
        value.setType(getNonPrimitiveType(getLanguageModule(), CEYLON_OBJECT_TYPE, decl, VarianceLocation.INVARIANT));
        parameter.setDeclaration((Declaration) decl);
        parameters.getParameters().add(parameter);
        decl.addMember(value);
    }

    private String getJavaAttributeName(String getterName) {
        if (getterName.startsWith("get") || getterName.startsWith("set")) {
            return NamingBase.getJavaBeanName(getterName.substring(3));
        } else if (getterName.startsWith("is")) {
            // Starts with "is"
            return NamingBase.getJavaBeanName(getterName.substring(2));
        } else {
            throw new RuntimeException("Illegal java getter/setter name");
        }
    }

    private void addValue(ClassOrInterface klass, String ceylonName, FieldMirror fieldMirror, boolean isCeylon) {
        // make sure it's a FieldValue so we can figure it out in the backend
        Value value = new FieldValue(fieldMirror.getName());
        value.setContainer(klass);
        value.setScope(klass);
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
        
        Type type = obtainType(fieldMirror.getType(), fieldMirror, klass, ModelUtil.getModuleContainer(klass), VarianceLocation.INVARIANT,
                "field '"+value.getName()+"'", klass);
        if (value.isEnumValue()) {
            Class enumValueType = new Class();
            enumValueType.setJavaEnum(true);
            enumValueType.setAnonymous(true);
            enumValueType.setExtendedType(type);
            enumValueType.setContainer(value.getContainer());
            enumValueType.setScope(value.getContainer());
            enumValueType.setDeprecated(value.isDeprecated());
            enumValueType.setName(value.getName());
            enumValueType.setFinal(true);
            enumValueType.setUnit(value.getUnit());
            enumValueType.setStaticallyImportable(value.isStaticallyImportable());
            value.setType(enumValueType.getType());
            value.setUncheckedNullType(false);
        } else {
            value.setType(type);
            value.setUncheckedNullType((!isCeylon && !fieldMirror.getType().isPrimitive()) || isUncheckedNull(fieldMirror));
        }
        type.setRaw(isRaw(ModelUtil.getModuleContainer(klass), fieldMirror.getType()));

        markUnboxed(value, null, fieldMirror.getType());
        markTypeErased(value, fieldMirror, fieldMirror.getType());
        markUntrustedType(value, fieldMirror, fieldMirror.getType());
        value.setDeprecated(isDeprecated(fieldMirror));
        setAnnotations(value, fieldMirror);
        klass.addMember(value);
        ModelUtil.setVisibleScope(value);
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
                    if(JvmBackendUtil.isValue(decl)
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
        JavaBeanValue value = new JavaBeanValue(methodMirror);
        value.setGetterName(methodMirror.getName());
        value.setContainer(klass);
        value.setScope(klass);
        value.setUnit(klass.getUnit());
        Type type = null;
        try{
            setMethodOrValueFlags(klass, methodMirror, value, isCeylon);
        }catch(ModelResolutionException x){
            // collect an error in its type
            type = logModelResolutionException(x, klass, "getter '"+methodName+"' (checking if it is an overriding method");
        }
        value.setName(JvmBackendUtil.strip(methodName, isCeylon, value.isShared()));

        // do not log an additional error if we had one from checking if it was overriding
        if(type == null)
            type = obtainType(methodMirror.getReturnType(), methodMirror, klass, ModelUtil.getModuleContainer(klass), VarianceLocation.INVARIANT,
                              "getter '"+methodName+"'", klass);
        value.setType(type);
        // special case for hash attributes which we want to pretend are of type long internally
        if(value.isShared() && methodName.equals("hash"))
            type.setUnderlyingType("long");
        value.setUncheckedNullType((!isCeylon && !methodMirror.getReturnType().isPrimitive()) || isUncheckedNull(methodMirror));
        type.setRaw(isRaw(ModelUtil.getModuleContainer(klass), methodMirror.getReturnType()));

        markUnboxed(value, methodMirror, methodMirror.getReturnType());
        markTypeErased(value, methodMirror, methodMirror.getReturnType());
        markUntrustedType(value, methodMirror, methodMirror.getReturnType());
        value.setDeprecated(isDeprecated(methodMirror));
        setAnnotations(value, methodMirror);
        klass.addMember(value);
        ModelUtil.setVisibleScope(value);
    }

    private boolean isUncheckedNull(AnnotatedMirror methodMirror) {
        Boolean unchecked = getAnnotationBooleanValue(methodMirror, CEYLON_TYPE_INFO_ANNOTATION, "uncheckedNull");
        return unchecked != null && unchecked.booleanValue();
    }

    private void setMethodOrValueFlags(final ClassOrInterface klass, final MethodMirror methodMirror, final FunctionOrValue decl, boolean isCeylon) {
        decl.setShared(methodMirror.isPublic() || methodMirror.isProtected() || methodMirror.isDefaultAccess());
        decl.setProtectedVisibility(methodMirror.isProtected());
        decl.setPackageVisibility(methodMirror.isDefaultAccess());
        if(decl instanceof Value){
            setValueTransientLateFlags((Value)decl, methodMirror, isCeylon);
        }
        if(// for class members we rely on abstract bit
           (klass instanceof Class 
                   && methodMirror.isAbstract())
           // Trust the abstract bit for Java interfaces, but not for Ceylon ones
           || (klass instanceof Interface
                   && !((LazyInterface)klass).isCeylon()
                   && methodMirror.isAbstract())
           // For Ceylon interfaces we rely on annotation
           || methodMirror.getAnnotation(CEYLON_LANGUAGE_FORMAL_ANNOTATION) != null) {
            decl.setFormal(true);
        } else {
            if (// for class members we rely on final/static bits
                (klass instanceof Class
                        && !klass.isFinal() // a final class necessarily has final members
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
        decl.setStaticallyImportable(methodMirror.isStatic() && methodMirror.getAnnotation(CEYLON_ENUMERATED_ANNOTATION) == null);

        decl.setActualCompleter(this);
    }
    
    @Override
    public void completeActual(Declaration decl){
        Scope container = decl.getContainer();

        if(container instanceof ClassOrInterface){
            ClassOrInterface klass = (ClassOrInterface) container;
            
            decl.setRefinedDeclaration(decl);
            // we never consider Interface and other stuff, since we never register the actualCompleter for them
            if(decl instanceof Class){
                // Java member classes are never actual 
                if(!JvmBackendUtil.isCeylon((Class)decl))
                    return;
                // we already set the actual bit for member classes, we just need the refined decl
                if(decl.isActual()){
                    Declaration refined = klass.getRefinedMember(decl.getName(), getSignature(decl), false);
                    decl.setRefinedDeclaration(refined);
                }
            }else{ // Function or Value
                MethodMirror methodMirror;
                if(decl instanceof JavaBeanValue)
                    methodMirror = ((JavaBeanValue) decl).mirror;
                else if(decl instanceof JavaMethod)
                    methodMirror = ((JavaMethod) decl).mirror;
                else
                    throw new ModelResolutionException("Unknown type of declaration: "+decl+": "+decl.getClass().getName());
                
                decl.setRefinedDeclaration(decl);
                // For Ceylon interfaces we rely on annotation
                if(klass instanceof LazyInterface
                        && ((LazyInterface)klass).isCeylon()){
                    boolean actual = methodMirror.getAnnotation(CEYLON_LANGUAGE_ACTUAL_ANNOTATION) != null;
                    decl.setActual(actual);
                    if(actual){
                        Declaration refined = klass.getRefinedMember(decl.getName(), getSignature(decl), false);
                        decl.setRefinedDeclaration(refined);
                    }
                }else{
                    if(isOverridingMethod(methodMirror)){
                        decl.setActual(true);
                        Declaration refined = klass.getRefinedMember(decl.getName(), getSignature(decl), false);
                        decl.setRefinedDeclaration(refined);
                    }
                }
                
                // now that we know the refined declaration, we can check for reified type param support
                // for Ceylon methods
                if(decl instanceof JavaMethod && JvmBackendUtil.isCeylon(klass)){
                    if(!methodMirror.getTypeParameters().isEmpty()
                            // because this requires the refined decl, we defer this check until we've set it, to not trigger
                            // lazy loading just to check.
                            && JvmBackendUtil.supportsReified(decl)){
                        checkReifiedTypeDescriptors(methodMirror.getTypeParameters().size(), 
                                container.getQualifiedNameString(), methodMirror, false);
                    }
                }
            }
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
        Type extendedType;
        
        if(klass instanceof Interface){
            // interfaces need to have their superclass set to Object
            if(superClass == null || superClass.getKind() == TypeKind.NONE)
                extendedType = getNonPrimitiveType(getLanguageModule(), CEYLON_OBJECT_TYPE, klass, VarianceLocation.INVARIANT);
            else
                extendedType = getNonPrimitiveType(ModelUtil.getModule(klass), superClass, klass, VarianceLocation.INVARIANT);
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
                    extendedType = decodeType(annotationSuperClassName, klass, ModelUtil.getModuleContainer(klass),
                            "extended type");
                }else{
                    // read it from the Java super type
                    // now deal with type erasure, avoid having Object as superclass
                    if("java.lang.Object".equals(superClassName)){
                        extendedType = getNonPrimitiveType(getLanguageModule(), CEYLON_BASIC_TYPE, klass, VarianceLocation.INVARIANT);
                    } else if(superClass != null){
                        try{
                            extendedType = getNonPrimitiveType(ModelUtil.getModule(klass), superClass, klass, VarianceLocation.INVARIANT);
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

    private Type getJavaAnnotationExtendedType(ClassOrInterface klass, ClassMirror classMirror) {
        TypeDeclaration constrainedAnnotation = (TypeDeclaration) convertNonPrimitiveTypeToDeclaration(getLanguageModule(), CEYLON_CONSTRAINED_ANNOTATION_TYPE, klass, DeclarationType.TYPE);
        AnnotationMirror target = classMirror.getAnnotation("java.lang.annotation.Target");
        Set<Type> types = new HashSet<Type>();
        if(target != null){
            @SuppressWarnings("unchecked")
            List<String> values = (List<String>) target.getValue();
            for(String value : values){
                switch(value){
                case "TYPE":
                    TypeDeclaration decl = (TypeDeclaration) convertNonPrimitiveTypeToDeclaration(getLanguageModule(), CEYLON_CLASS_OR_INTERFACE_DECLARATION_TYPE, klass, DeclarationType.TYPE);
                    types.add(decl.getType());
                    decl = (TypeDeclaration) convertNonPrimitiveTypeToDeclaration(getLanguageModule(), CEYLON_ALIAS_DECLARATION_TYPE, klass, DeclarationType.TYPE);
                    types.add(decl.getType());
                    break;
                case "ANNOTATION_TYPE":
                    decl = (TypeDeclaration) convertNonPrimitiveTypeToDeclaration(getLanguageModule(), CEYLON_CLASS_OR_INTERFACE_DECLARATION_TYPE, klass, DeclarationType.TYPE);
                    types.add(decl.getType());
                    break;
                case "CONSTRUCTOR":
                    decl = (TypeDeclaration) convertNonPrimitiveTypeToDeclaration(getLanguageModule(), CEYLON_CONSTRUCTOR_DECLARATION_TYPE, klass, DeclarationType.TYPE);
                    types.add(decl.getType());
                    break;
                case "METHOD":
                    // method annotations may be applied to shared members which are turned into getter methods
                case "PARAMETER":
                    decl = (TypeDeclaration) convertNonPrimitiveTypeToDeclaration(getLanguageModule(), CEYLON_FUNCTION_OR_VALUE_DECLARATION_TYPE, klass, DeclarationType.TYPE);
                    types.add(decl.getType());
                    break;
                case "FIELD":
                case "LOCAL_VARIABLE":
                    decl = (TypeDeclaration) convertNonPrimitiveTypeToDeclaration(getLanguageModule(), CEYLON_VALUE_DECLARATION_TYPE, klass, DeclarationType.TYPE);
                    types.add(decl.getType());
                    break;
                default:
                    // all other values are ambiguous or have no mapping
                }
            }
        }
        Module module = ModelUtil.getModuleContainer(klass);
        Type annotatedType;
        if(types.size() == 1)
            annotatedType = types.iterator().next();
        else if(types.isEmpty()){
            TypeDeclaration decl;
            if(target == null){
                // default is anything
                decl = (TypeDeclaration) convertNonPrimitiveTypeToDeclaration(getLanguageModule(), CEYLON_ANNOTATED_TYPE, klass, DeclarationType.TYPE);
            }else{
                // we either had an empty set which means cannot be used as annotation in Java (only as annotation member)
                // or that we only had unmappable targets
                decl = typeFactory.getNothingDeclaration();
            }
            annotatedType = decl.getType();
        }else{
            List<Type> list = new ArrayList<Type>(types.size());
            list.addAll(types);
            annotatedType = union(list, getUnitForModule(module));
        }
        Type constrainedType = constrainedAnnotation.appliedType(null, Arrays.asList(klass.getType(), getOptionalType(klass.getType(), module), annotatedType));
        return constrainedType;
    }
    
    private void setParameters(Functional decl, ClassMirror classMirror, MethodMirror methodMirror, boolean isCeylon, Scope container) {
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
            Module module = ModelUtil.getModuleContainer((Scope) decl);

            Type type;
            if(isVariadic){
                // possibly make it optional
                TypeMirror variadicType = typeMirror.getComponentType();
                // we pretend it's toplevel because we want to get magic string conversion for variadic methods
                type = obtainType(ModelUtil.getModuleContainer((Scope)decl), variadicType, (Scope)decl, TypeLocation.TOPLEVEL, VarianceLocation.CONTRAVARIANT);
                if(!isCeylon && !variadicType.isPrimitive()){
                    // Java parameters are all optional unless primitives
                    Type optionalType = getOptionalType(type, module);
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
                    Type optionalType = getOptionalType(type, module);
                    optionalType.setUnderlyingType(type.getUnderlyingType());
                    type = optionalType;
                }
            }
            
            FunctionOrValue value = null;
            boolean lookedup = false;
            if (isCeylon && decl instanceof Class){
                // For a functional parameter to a class, we can just lookup the member
                value = (FunctionOrValue)((Class)decl).getDirectMember(paramName, null, false);
                lookedup = value != null;
            } 
            if (value == null) {
                // So either decl is not a Class, 
                // or the method or value member of decl is not shared
                AnnotationMirror functionalParameterAnnotation = paramMirror.getAnnotation(CEYLON_FUNCTIONAL_PARAMETER_ANNOTATION);
                if (functionalParameterAnnotation != null) {
                    // A functional parameter to a method
                    Function method = loadFunctionalParameter((Declaration)decl, paramName, type, (String)functionalParameterAnnotation.getValue());
                    value = method;
                    parameter.setDeclaredAnything(method.isDeclaredVoid());
                } else {
                    // A value parameter to a method
                    value = new Value();
                    value.setType(type);
                }
                
                value.setContainer((Scope) decl);
                value.setScope((Scope) decl);
                ModelUtil.setVisibleScope(value);
                value.setUnit(((Element)decl).getUnit());
                value.setName(paramName);
            }else{
                // Ceylon 1.1 had a bug where TypeInfo for functional parameters included the full CallableType on the method
                // rather than just the method return type, so we try to detect this and fix it
                if(value instanceof Function 
                        && isCeylon1Dot1(classMirror)){
                    Type newType = getSimpleCallableReturnType(value.getType());
                    if(!newType.isUnknown())
                        value.setType(newType);
                }
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
            value.setDeprecated(isDeprecated(paramMirror));
            setAnnotations(value, paramMirror);
            parameters.getParameters().add(parameter);
            if (!lookedup) {
                parameter.getDeclaration().getMembers().add(parameter.getModel());
            }
            
            parameterIndex++;
        }
        if (decl instanceof Function) {
            // Multiple parameter lists
            AnnotationMirror functionalParameterAnnotation = methodMirror.getAnnotation(CEYLON_FUNCTIONAL_PARAMETER_ANNOTATION);
            if (functionalParameterAnnotation != null) {
                parameterNameParser.parseMpl((String)functionalParameterAnnotation.getValue(), ((Function)decl).getType().getFullType(), (Function)decl);
            }
        }
    }

    private boolean isCeylon1Dot1(ClassMirror classMirror) {
        AnnotationMirror annotation = classMirror.getAnnotation(CEYLON_CEYLON_ANNOTATION);
        if(annotation == null)
            return false;
        Integer major = (Integer) annotation.getValue("major");
        if(major == null)
            major = 0;
        Integer minor = (Integer) annotation.getValue("minor");
        if(minor == null)
            minor = 0;
        return major == Versions.V1_1_BINARY_MAJOR_VERSION && minor == Versions.V1_1_BINARY_MINOR_VERSION;
    }
    
    private Function loadFunctionalParameter(Declaration decl, String paramName, Type type, String parameterNames) {
        Function method = new Function();
        method.setName(paramName);
        method.setUnit(decl.getUnit());
        if (parameterNames == null || parameterNames.isEmpty()) {
            // This branch is broken, but it deals with old code which lacked
            // the encoding of parameter names of functional parameters, so we'll keep it until 1.2
            method.setType(getSimpleCallableReturnType(type));
            ParameterList pl = new ParameterList();
            int count = 0;
            for (Type pt : getSimpleCallableArgumentTypes(type)) {
                Parameter p = new Parameter();
                Value v = new Value();
                String name = "arg" + count++;
                p.setName(name);
                v.setName(name);
                v.setType(pt);
                v.setContainer(method);
                v.setScope(method);
                p.setModel(v);
                v.setInitializerParameter(p);
                pl.getParameters().add(p);
                method.addMember(v);
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

    List<Type> getSimpleCallableArgumentTypes(Type type) {
        if(type != null
                && type.isClassOrInterface()
                && type.getDeclaration().getQualifiedNameString().equals(CEYLON_LANGUAGE_CALLABLE_TYPE_NAME)
                && type.getTypeArgumentList().size() >= 2)
            return flattenCallableTupleType(type.getTypeArgumentList().get(1));
        return Collections.emptyList();
    }

    List<Type> flattenCallableTupleType(Type tupleType) {
        if(tupleType != null
                && tupleType.isClassOrInterface()){
            String declName = tupleType.getDeclaration().getQualifiedNameString();
            if(declName.equals(CEYLON_LANGUAGE_TUPLE_TYPE_NAME)){
                List<Type> tal = tupleType.getTypeArgumentList();
                if(tal.size() >= 3){
                    List<Type> ret = flattenCallableTupleType(tal.get(2));
                    ret.add(0, tal.get(1));
                    return ret;
                }
            }else if(declName.equals(CEYLON_LANGUAGE_EMPTY_TYPE_NAME)){
                return new LinkedList<Type>();
            }else if(declName.equals(CEYLON_LANGUAGE_SEQUENTIAL_TYPE_NAME)){
                LinkedList<Type> ret = new LinkedList<Type>();
                ret.add(tupleType);
                return ret;
            }else if(declName.equals(CEYLON_LANGUAGE_SEQUENCE_TYPE_NAME)){
                LinkedList<Type> ret = new LinkedList<Type>();
                ret.add(tupleType);
                return ret;
            }
        }
        return Collections.emptyList();
    }
    
    Type getSimpleCallableReturnType(Type type) {
        if(type != null
                && type.isClassOrInterface()
                && type.getDeclaration().getQualifiedNameString().equals(CEYLON_LANGUAGE_CALLABLE_TYPE_NAME)
                && !type.getTypeArgumentList().isEmpty())
            return type.getTypeArgumentList().get(0);
        return newUnknownType();
    }
    
    private Type getOptionalType(Type type, Module moduleScope) {
        if(type.isUnknown())
            return type;
        // we do not use Unit.getOptionalType because it causes lots of lazy loading that ultimately triggers the typechecker's
        // infinite recursion loop
        List<Type> list = new ArrayList<Type>(2);
        list.add(typeFactory.getNullType());
        list.add(type);
        return union(list, getUnitForModule(moduleScope));
    }
    
    private Type logModelResolutionError(Scope container, String message) {
        return logModelResolutionException((String)null, container, message);
    }

    private Type logModelResolutionException(ModelResolutionException x, Scope container, String message) {
        return logModelResolutionException(x.getMessage(), container, message);
    }
    
    private Type logModelResolutionException(final String exceptionMessage, Scope container, final String message) {
        final Module module = ModelUtil.getModuleContainer(container);
        return logModelResolutionException(exceptionMessage, module, message);
    }
    
    private Type logModelResolutionException(final String exceptionMessage, Module module, final String message) {
        UnknownType.ErrorReporter errorReporter;
        if(module != null && !module.isDefault()){
            final StringBuilder sb = new StringBuilder();
            sb.append("Error while loading the ").append(module.getNameAsString()).append("/").append(module.getVersion());
            sb.append(" module:\n ");
            sb.append(message);
            
            if(exceptionMessage != null)
                sb.append(":\n ").append(exceptionMessage);
            errorReporter = makeModelErrorReporter(module, sb.toString());
        }else if(exceptionMessage == null){
            errorReporter = makeModelErrorReporter(message);
        }else{
            errorReporter = makeModelErrorReporter(message+": "+exceptionMessage);
        }
        UnknownType ret = new UnknownType(typeFactory);
        ret.setErrorReporter(errorReporter);
        return ret.getType();
    }

    /**
     * To be overridden by subclasses
     */
    protected UnknownType.ErrorReporter makeModelErrorReporter(String message) {
        return new LogErrorRunnable(this, message);
    }
    
    /**
     * To be overridden by subclasses
     */
    protected abstract UnknownType.ErrorReporter makeModelErrorReporter(Module module, String message);

    private static class LogErrorRunnable extends UnknownType.ErrorReporter {

        private AbstractModelLoader modelLoader;

        public LogErrorRunnable(AbstractModelLoader modelLoader, String message) {
            super(message);
            this.modelLoader = modelLoader;
        }

        @Override
        public void reportError() {
            modelLoader.logError(getMessage());
        }
    }

    private void markTypeErased(TypedDeclaration decl, AnnotatedMirror typedMirror, TypeMirror type) {
        if (BooleanUtil.isTrue(getAnnotationBooleanValue(typedMirror, CEYLON_TYPE_INFO_ANNOTATION, "erased"))) {
            decl.setTypeErased(true);
        } else {
            decl.setTypeErased(sameType(type, OBJECT_TYPE));
        }
    }
    
    private void markUntrustedType(TypedDeclaration decl, AnnotatedMirror typedMirror, TypeMirror type) {
        if (BooleanUtil.isTrue(getAnnotationBooleanValue(typedMirror, CEYLON_TYPE_INFO_ANNOTATION, "untrusted"))) {
            decl.setUntrustedType(true);
        }
    }
    
    private void markDeclaredVoid(Function decl, MethodMirror methodMirror) {
        if (methodMirror.isDeclaredVoid() || 
                BooleanUtil.isTrue(getAnnotationBooleanValue(methodMirror, CEYLON_TYPE_INFO_ANNOTATION, "declaredVoid"))) {
            decl.setDeclaredVoid(true);
        }
    }

    /*private boolean hasTypeParameterWithConstraints(TypeMirror type) {
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
    }*/
    
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
    public void complete(LazyValue value)  {
        synchronized(getLock()){
            timer.startIgnore(TIMER_MODEL_LOADER_CATEGORY);
            try{
                MethodMirror meth = null;
                String getterName = NamingBase.getGetterName(value);
                String setterName = NamingBase.getSetterName(value);
                boolean toplevel = value.isToplevel();
                for (MethodMirror m : value.classMirror.getDirectMethods()) {
                    // Do not skip members marked with @Ignore, because the getter is supposed to be ignored

                    if (m.getName().equals(getterName)
                            && (!toplevel || m.isStatic()) 
                            && m.getParameters().size() == 0) {
                        meth = m;
                    }
                    if (m.getName().equals(setterName)
                            && (!toplevel || m.isStatic()) 
                            && m.getParameters().size() == 1) {
                        value.setVariable(true);
                    }
                }
                if(meth == null || meth.getReturnType() == null){
                    value.setType(logModelResolutionError(value.getContainer(), "Error while resolving toplevel attribute "+value.getQualifiedNameString()+": getter method missing"));
                    return;
                }

                value.setType(obtainType(meth.getReturnType(), meth, null, ModelUtil.getModuleContainer(value.getContainer()), VarianceLocation.INVARIANT,
                        "toplevel attribute", value));

                setValueTransientLateFlags(value, meth, true);
                setAnnotations(value, meth);
                markUnboxed(value, meth, meth.getReturnType());

                TypeMirror setterClass = (TypeMirror) getAnnotationValue(value.classMirror, CEYLON_ATTRIBUTE_ANNOTATION, "setterClass");
                // void.class is the default value, I guess it's a primitive?
                if(setterClass != null && !setterClass.isPrimitive()){
                    ClassMirror setterClassMirror = setterClass.getDeclaredClass();
                    value.setVariable(true);
                    SetterWithLocalDeclarations setter = makeSetter(value, setterClassMirror);
                    // adding local scopes should be done last, when we have the setter, because it may be needed by container chain
                    addLocalDeclarations(value, value.classMirror, value.classMirror);
                    addLocalDeclarations(setter, setterClassMirror, setterClassMirror);
                }else if(value.isToplevel() && value.isTransient() && value.isVariable()){
                    makeSetter(value, value.classMirror);
                    // all local scopes for getter/setter are declared in the same class
                    // adding local scopes should be done last, when we have the setter, because it may be needed by container chain
                    addLocalDeclarations(value, value.classMirror, value.classMirror);
                }else{
                    // adding local scopes should be done last, when we have the setter, because it may be needed by container chain
                    addLocalDeclarations(value, value.classMirror, value.classMirror);
                }
            }finally{
                timer.stopIgnore(TIMER_MODEL_LOADER_CATEGORY);
            }
        }
    }

    private SetterWithLocalDeclarations makeSetter(Value value, ClassMirror classMirror) {
        SetterWithLocalDeclarations setter = new SetterWithLocalDeclarations(classMirror);
        setter.setContainer(value.getContainer());
        setter.setScope(value.getContainer());
        setter.setType(value.getType());
        setter.setName(value.getName());
        value.setSetter(setter);
        setter.setGetter(value);
        return setter;
    }

    @Override
    public void complete(LazyFunction method)  {
        synchronized(getLock()){
            timer.startIgnore(TIMER_MODEL_LOADER_CATEGORY);
            try{
                MethodMirror meth = null;
                String lookupName = method.getName();
                for(MethodMirror m : method.classMirror.getDirectMethods()){
                    // We skip members marked with @Ignore
                    if(m.getAnnotation(CEYLON_IGNORE_ANNOTATION) != null)
                        continue;

                    if(NamingBase.stripLeadingDollar(m.getName()).equals(lookupName)){
                        meth = m;
                        break;
                    }
                }
                if(meth == null || meth.getReturnType() == null){
                    method.setType(logModelResolutionError(method.getContainer(), "Error while resolving toplevel method "+method.getQualifiedNameString()+": static method missing"));
                    return;
                }
                // only check the static mod for toplevel classes
                if(!method.classMirror.isLocalClass() && !meth.isStatic()){
                    method.setType(logModelResolutionError(method.getContainer(), "Error while resolving toplevel method "+method.getQualifiedNameString()+": method is not static"));
                    return;
                }

                // save the method name
                method.setRealMethodName(meth.getName());

                // save the method
                method.setMethodMirror(meth);

                // type params first
                setTypeParameters(method, meth, true);

                method.setType(obtainType(meth.getReturnType(), meth, method, ModelUtil.getModuleContainer(method), VarianceLocation.COVARIANT,
                        "toplevel method", method));
                method.setDeclaredVoid(meth.isDeclaredVoid());
                markDeclaredVoid(method, meth);
                markUnboxed(method, meth, meth.getReturnType());
                markTypeErased(method, meth, meth.getReturnType());
                markUntrustedType(method, meth, meth.getReturnType());

             // now its parameters
                setParameters(method, method.classMirror, meth, true /* toplevel methods are always Ceylon */, method);
                
                method.setAnnotation(meth.getAnnotation(CEYLON_LANGUAGE_ANNOTATION_ANNOTATION) != null);
                setAnnotations(method, meth);

                setAnnotationConstructor(method, meth);

                addLocalDeclarations(method, method.classMirror, method.classMirror);
            }finally{
                timer.stopIgnore(TIMER_MODEL_LOADER_CATEGORY);
            }
        }
     }

    // for subclasses
    protected abstract void setAnnotationConstructor(LazyFunction method, MethodMirror meth);

    public AnnotationProxyMethod makeInteropAnnotationConstructor(LazyInterface iface,
            AnnotationProxyClass klass, OutputElement oe, Package pkg){
        String ctorName = oe == null ? NamingBase.getJavaBeanName(iface.getName()) : NamingBase.getDisambigAnnoCtorName(iface, oe);
        AnnotationProxyMethod ctor = new AnnotationProxyMethod();
        ctor.setAnnotationTarget(oe);
        ctor.setProxyClass(klass);
        ctor.setContainer(pkg);
        ctor.setAnnotation(true);
        ctor.setName(ctorName);
        ctor.setShared(iface.isShared());
        Annotation annotationAnnotation2 = new Annotation();
        annotationAnnotation2.setName("annotation");
        ctor.getAnnotations().add(annotationAnnotation2);
        ctor.setType(((TypeDeclaration)iface).getType());
        ctor.setUnit(iface.getUnit());
        
        ParameterList ctorpl = new ParameterList();
        ctorpl.setPositionalParametersSupported(false);
        ctor.addParameterList(ctorpl);
        
        List<Parameter> ctorParams = new ArrayList<Parameter>();
        for (Declaration member : iface.getMembers()) {
            boolean isValue = member.getName().equals("value");
            if (member instanceof JavaMethod) {
                JavaMethod m = (JavaMethod)member;
                
                
                Parameter ctorParam = new Parameter();
                ctorParams.add(ctorParam);
                Value value = new Value();
                ctorParam.setModel(value);
                value.setInitializerParameter(ctorParam);
                ctorParam.setDeclaration(ctor);
                value.setContainer(klass);
                value.setScope(klass);
                ctorParam.setDefaulted(m.isDefaultedAnnotation());
                value.setName(member.getName());
                ctorParam.setName(member.getName());
                value.setType(annotationParameterType(iface.getUnit(), m));
                value.setUnboxed(true);
                value.setUnit(iface.getUnit());
                if(isValue)
                    ctorpl.getParameters().add(0, ctorParam);
                else
                    ctorpl.getParameters().add(ctorParam);
                ctor.addMember(value);
            }
        }
        makeInteropAnnotationConstructorInvocation(ctor, klass, ctorParams);
        return ctor;
    }

    /**
     * For subclasses that provide compilation to Java annotations.
     */
    protected abstract void makeInteropAnnotationConstructorInvocation(AnnotationProxyMethod ctor, AnnotationProxyClass klass, List<Parameter> ctorParams);
    
    /**
     * <pre>
     *   annotation class Annotation$Proxy(...) satisfies Annotation {
     *       // a `shared` class parameter for each method of Annotation
     *   }
     * </pre>
     * @param iface The model of the annotation @interface
     * @return The annotation class for the given interface
     */
    public AnnotationProxyClass makeInteropAnnotationClass(
            LazyInterface iface, Package pkg) {
        AnnotationProxyClass klass = new AnnotationProxyClass(iface);
        klass.setContainer(pkg);
        klass.setScope(pkg);
        klass.setName(iface.getName()+"$Proxy");
        klass.setShared(iface.isShared());
        klass.setAnnotation(true);
        Annotation annotationAnnotation = new Annotation();
        annotationAnnotation.setName("annotation");
        klass.getAnnotations().add(annotationAnnotation);
        klass.getSatisfiedTypes().add(iface.getType());
        klass.setUnit(iface.getUnit());
        ParameterList classpl = new ParameterList();
        klass.addParameterList(classpl);
        klass.setScope(pkg);
        
        for (Declaration member : iface.getMembers()) {
            boolean isValue = member.getName().equals("value");
            if (member instanceof JavaMethod) {
                JavaMethod m = (JavaMethod)member;
                Parameter klassParam = new Parameter();
                Value value = new Value();
                klassParam.setModel(value);
                value.setInitializerParameter(klassParam);
                klassParam.setDeclaration(klass);
                value.setContainer(klass);
                value.setScope(klass);
                value.setName(member.getName());
                klassParam.setName(member.getName());
                value.setType(annotationParameterType(iface.getUnit(), m));
                value.setUnboxed(true);
                value.setUnit(iface.getUnit());
                if(isValue)
                    classpl.getParameters().add(0, klassParam);
                else
                    classpl.getParameters().add(klassParam);
                klass.addMember(value);
            }
        }
        return klass;
    }

    private Type annotationParameterType(Unit unit, JavaMethod m) {
        Type type = m.getType();
        if (JvmBackendUtil.isJavaArray(type.getDeclaration())) {
            String name = type.getDeclaration().getQualifiedNameString();
            final Type elementType;
            String underlyingType = null;
            if(name.equals("java.lang::ObjectArray")){
                Type eType = type.getTypeArgumentList().get(0);
                String elementTypeName = eType.getDeclaration().getQualifiedNameString();
                if ("java.lang::String".equals(elementTypeName)) {
                    elementType = unit.getStringType();
                } else if ("java.lang::Class".equals(elementTypeName)
                        || "java.lang.Class".equals(eType.getUnderlyingType())) {
                    // Two cases because the types 
                    // Class[] and Class<?>[] are treated differently by 
                    // AbstractModelLoader.obtainType()
                    
                    // TODO Replace with metamodel ClassOrInterface type
                    // once we have support for metamodel references
                    elementType = unit.getAnythingType();
                    underlyingType = "java.lang.Class";
                } else {
                    elementType = eType;   
                }
                // TODO Enum elements
            } else if(name.equals("java.lang::LongArray")) {
                elementType = unit.getIntegerType();
            } else if (name.equals("java.lang::ByteArray")) {
                elementType = unit.getByteType();
            } else if (name.equals("java.lang::ShortArray")) {
                elementType = unit.getIntegerType();
                underlyingType = "short";
            } else if (name.equals("java.lang::IntArray")){
                elementType = unit.getIntegerType();
                underlyingType = "int";
            } else if(name.equals("java.lang::BooleanArray")){
                elementType = unit.getBooleanType();
            } else if(name.equals("java.lang::CharArray")){
                elementType = unit.getCharacterType();
                underlyingType = "char";
            } else if(name.equals("java.lang::DoubleArray")) {
                elementType = unit.getFloatType();
            } else if (name.equals("java.lang::FloatArray")){
                elementType = unit.getFloatType();
                underlyingType = "float";
            } else {
                throw new RuntimeException();
            }
            elementType.setUnderlyingType(underlyingType);
            Type iterableType = unit.getIterableType(elementType);
            return iterableType;
        } else if ("java.lang::Class".equals(type.getDeclaration().getQualifiedNameString())) {
            // TODO Replace with metamodel ClassOrInterface type
            // once we have support for metamodel references
            return unit.getAnythingType();
        } else {
            return type;
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
            klass.getSatisfiedTypes().addAll(getTypesList(satisfiedTypes, klass, ModelUtil.getModuleContainer(klass), "satisfied types", klass.getQualifiedNameString()));
        }else{
            if(classMirror.isAnnotationType())
                // this only happens for Java annotations since Ceylon annotations are ignored
                // turn @Target into a subtype of ConstrainedAnnotation
                klass.getSatisfiedTypes().add(getJavaAnnotationExtendedType(klass, classMirror));

            for(TypeMirror iface : classMirror.getInterfaces()){
                // ignore generated interfaces
                if(sameType(iface, CEYLON_REIFIED_TYPE_TYPE) 
                        || sameType(iface, CEYLON_SERIALIZABLE_TYPE))
                    continue;
                try{
                    klass.getSatisfiedTypes().add(getNonPrimitiveType(ModelUtil.getModule(klass), iface, klass, VarianceLocation.INVARIANT));
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
        if (classMirror.isEnum()) {
            ArrayList<Type> caseTypes = new ArrayList<Type>();
            for (Declaration member : klass.getMembers()) {
                if (member instanceof FieldValue
                        && ((FieldValue) member).isEnumValue()) {
                    caseTypes.add(((FieldValue)member).getType());
                }
            }
            klass.setCaseTypes(caseTypes);
        } else {
            String selfType = getSelfTypeFromAnnotations(classMirror);
            Module moduleScope = ModelUtil.getModuleContainer(klass);
            if(selfType != null && !selfType.isEmpty()){
                Type type = decodeType(selfType, klass, moduleScope, "self type");
                if(!type.isTypeParameter()){
                    logError("Invalid type signature for self type of "+klass.getQualifiedNameString()+": "+selfType+" is not a type parameter");
                }else{
                    klass.setSelfType(type);
                    List<Type> caseTypes = new LinkedList<Type>();
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
    }

    private List<Type> getTypesList(List<String> caseTypes, Scope scope, Module moduleScope, String targetType, String targetName) {
        List<Type> producedTypes = new LinkedList<Type>();
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
            param.setScope(scope);
            ModelUtil.setVisibleScope(param);
            param.setDeclaration((Declaration) scope);
            // let's not trigger the lazy-loading if we're completing a LazyClass/LazyInterface
            if(scope instanceof LazyContainer)
                ((LazyContainer)scope).addMember(param);
            else // must be a method
                scope.addMember(param);
            param.setName((String)typeParamAnnotation.getValue("value"));
            param.setExtendedType(typeFactory.getAnythingType());
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

        Module moduleScope = ModelUtil.getModuleContainer(scope);
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
                param.setCaseTypes(new LinkedList<Type>());
            setListOfTypes(param.getCaseTypes(), caseTypesAttribute, scope, moduleScope,
                    "type parameter '"+param.getName()+"' case types");

            String defaultValueAttribute = (String)typeParamAnnotation.getValue("defaultValue");
            if(defaultValueAttribute != null && !defaultValueAttribute.isEmpty()){
                Type decodedType = decodeType(defaultValueAttribute, scope, moduleScope, 
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

    private void setListOfTypes(List<Type> destinationTypeList, List<String> serialisedTypes, Scope scope, Module moduleScope, 
                                String targetType) {
        if(serialisedTypes != null){
            for (String serialisedType : serialisedTypes) {
                Type decodedType = decodeType(serialisedType, scope, moduleScope, targetType);
                destinationTypeList.add(decodedType);
            }
        }
    }

    // from java type info
    private void setTypeParameters(Scope scope, List<TypeParameter> params, List<TypeParameterMirror> typeParameters, boolean isCeylon) {
        // We must first add every type param, before we resolve the bounds, which can
        // refer to type params.
        for(TypeParameterMirror typeParam : typeParameters){
            TypeParameter param = new TypeParameter();
            param.setUnit(((Element)scope).getUnit());
            param.setContainer(scope);
            param.setScope(scope);
            ModelUtil.setVisibleScope(param);
            param.setDeclaration((Declaration) scope);
            // let's not trigger the lazy-loading if we're completing a LazyClass/LazyInterface
            if(scope instanceof LazyContainer)
                ((LazyContainer)scope).addMember(param);
            else // must be a method
                scope.addMember(param);
            param.setName(typeParam.getName());
            param.setExtendedType(typeFactory.getAnythingType());
            params.add(param);
        }
        boolean needsObjectBounds = !isCeylon && scope instanceof Function;
        // Now all type params have been set, we can resolve the references parts
        Iterator<TypeParameter> paramsIterator = params.iterator();
        for(TypeParameterMirror typeParam : typeParameters){
            TypeParameter param = paramsIterator.next();
            List<TypeMirror> bounds = typeParam.getBounds();
            for(TypeMirror bound : bounds){
                Type boundType;
                // we turn java's default upper bound java.lang.Object into ceylon.language.Object
                if(sameType(bound, OBJECT_TYPE)){
                    // avoid adding java's default upper bound if it's just there with no meaning,
                    // especially since we do not want it for types
                    if(bounds.size() == 1)
                        break;
                    boundType = getNonPrimitiveType(getLanguageModule(), CEYLON_OBJECT_TYPE, scope, VarianceLocation.INVARIANT);
                }else
                    boundType = getNonPrimitiveType(ModelUtil.getModuleContainer(scope), bound, scope, VarianceLocation.INVARIANT);
                param.getSatisfiedTypes().add(boundType);
            }
            if(needsObjectBounds && param.getSatisfiedTypes().isEmpty()){
                Type boundType = getNonPrimitiveType(getLanguageModule(), CEYLON_OBJECT_TYPE, scope, VarianceLocation.INVARIANT);
                param.getSatisfiedTypes().add(boundType);
            }
        }
    }

    // method
    private void setTypeParameters(Function method, MethodMirror methodMirror, boolean isCeylon) {
        List<TypeParameter> params = new LinkedList<TypeParameter>();
        method.setTypeParameters(params);
        List<AnnotationMirror> typeParameters = getTypeParametersFromAnnotations(methodMirror);
        if(typeParameters != null) {
            setTypeParametersFromAnnotations(method, params, methodMirror, typeParameters, methodMirror.getTypeParameters());
        } else {
            setTypeParameters(method, params, methodMirror.getTypeParameters(), isCeylon);
        }
    }

    // class
    private void setTypeParameters(TypeDeclaration klass, ClassMirror classMirror, boolean isCeylon) {
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
            setTypeParameters(klass, params, mirrorTypeParameters, isCeylon);
        }
    }        

    //
    // TypeParsing and ModelLoader

    private Type decodeType(String value, Scope scope, Module moduleScope, String targetType) {
        return decodeType(value, scope, moduleScope, targetType, null);
    }
    
    private Type decodeType(String value, Scope scope, Module moduleScope, String targetType, Declaration target) {
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
    private Type obtainType(TypeMirror type, AnnotatedMirror symbol, Scope scope, Module moduleScope, VarianceLocation variance, 
                                    String targetType, Declaration target) {
        String typeName = getAnnotationStringValue(symbol, CEYLON_TYPE_INFO_ANNOTATION);
        if (typeName != null) {
            Type ret = decodeType(typeName, scope, moduleScope, targetType, target);
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
    
    public Type obtainType(Module moduleScope, TypeMirror type, Scope scope, TypeLocation location, VarianceLocation variance) {
        TypeMirror originalType = type;
        // ERASURE
        type = applyTypeMapping(type, location);
        
        Type ret = getNonPrimitiveType(moduleScope, type, scope, variance);
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
            return CEYLON_BYTE_TYPE;
            
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
            
        } else if (sameType(type, THROWABLE_TYPE)) {
            return CEYLON_THROWABLE_TYPE;
            
        } else if (sameType(type, EXCEPTION_TYPE)) {
            return CEYLON_EXCEPTION_TYPE;
            
        } else if (sameType(type, ANNOTATION_TYPE)) {
            // here we prefer Annotation over ConstrainedAnnotation but that's fine
            return CEYLON_ANNOTATION_TYPE;
            
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

    private Type getNonPrimitiveType(Module moduleScope, TypeMirror type, Scope scope, VarianceLocation variance) {
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
    
    private Type applyTypeArguments(Module moduleScope, TypeDeclaration declaration,
                                            TypeMirror type, Scope scope, VarianceLocation variance,
                                            TypeMappingMode mode, Set<TypeDeclaration> rawDeclarationsSeen) {
        List<TypeMirror> javacTypeArguments = type.getTypeArguments();
        boolean hasTypeParameters = !declaration.getTypeParameters().isEmpty();
        boolean hasTypeArguments = !javacTypeArguments.isEmpty();
        boolean isRaw = !hasTypeArguments && hasTypeParameters;
        // if we have type arguments or type parameters (raw)
        if(hasTypeArguments || isRaw){
            // if it's raw we will need the map anyways
            if(rawDeclarationsSeen == null)
                rawDeclarationsSeen = new HashSet<TypeDeclaration>();
            // detect recursive bounds that we can't possibly satisfy, such as Foo<T extends Foo<T>>
            if(rawDeclarationsSeen != null && !rawDeclarationsSeen.add(declaration))
                throw new RecursiveTypeParameterBoundException();
            try{
                List<Type> typeArguments = new ArrayList<Type>(javacTypeArguments.size());
                List<TypeParameter> typeParameters = declaration.getTypeParameters();
                List<TypeParameterMirror> typeParameterMirrors = null;
                // SimpleReflType for Object and friends don't have a type, but don't need one
                if(type.getDeclaredClass() != null)
                    typeParameterMirrors = type.getDeclaredClass().getTypeParameters();
                Map<TypeParameter,SiteVariance> siteVarianceMap = null;
                int len = hasTypeArguments ? javacTypeArguments.size() : typeParameters.size();
                for(int i=0 ; i<len ; i++){
                    TypeParameter typeParameter = null;
                    if(i < typeParameters.size())
                        typeParameter = typeParameters.get(i);
                    Type producedTypeArgument = null;
                    // do we have a type argument?
                    TypeMirror typeArgument = null;
                    SiteVariance siteVariance = null;
                    if(hasTypeArguments){
                        typeArgument = javacTypeArguments.get(i);
                        // if a single type argument is a wildcard and we are in a covariant location, we erase to Object
                        if(typeArgument.getKind() == TypeKind.WILDCARD){
                            
                            TypeMirror bound = typeArgument.getUpperBound();
                            if(bound != null){
                                siteVariance = SiteVariance.OUT;
                            } else {
                                bound = typeArgument.getLowerBound();
                                if(bound != null){
                                    // it has a lower bound
                                    siteVariance = SiteVariance.IN;
                                }
                            }
                            // use the bound in any case
                            typeArgument = bound;
                        }
                    }
                    // if we have no type argument, or if it's a wildcard with no bound, use the type parameter bounds if we can
                    if(typeArgument == null && typeParameterMirrors != null && i < typeParameterMirrors.size()){
                        TypeParameterMirror typeParameterMirror = typeParameterMirrors.get(i);
                        // FIXME: multiple bounds?
                        if(typeParameterMirror.getBounds().size() == 1){
                            // make sure we don't go overboard
                            if(rawDeclarationsSeen == null){
                                rawDeclarationsSeen = new HashSet<TypeDeclaration>();
                                // detect recursive bounds that we can't possibly satisfy, such as Foo<T extends Foo<T>>
                                if(!rawDeclarationsSeen.add(declaration))
                                    throw new RecursiveTypeParameterBoundException();
                            }
                            TypeMirror bound = typeParameterMirror.getBounds().get(0);
                            try{
                                producedTypeArgument = obtainTypeParameterBound(moduleScope, bound, declaration, rawDeclarationsSeen);
                                siteVariance = SiteVariance.OUT;
                            }catch(RecursiveTypeParameterBoundException x){
                                // damnit, go for Object later
                            }
                        }                                        
                    }

                    // if we have no type argument, or it was a wildcard with no bounds and we could not use the type parameter bounds,
                    // let's fall back to "out Object"
                    if(typeArgument == null && producedTypeArgument == null){
                        producedTypeArgument = typeFactory.getObjectType();
                        siteVariance = SiteVariance.OUT;
                    }

                    // record use-site variance if required
                    if(!JvmBackendUtil.isCeylon(declaration) && siteVariance != null){
                        // lazy alloc
                        if(siteVarianceMap == null)
                            siteVarianceMap = new HashMap<TypeParameter,SiteVariance>();
                        siteVarianceMap.put(typeParameter, siteVariance);
                    }
                    
                    // in some cases we may already have a produced type argument we can use. if not let's fetch it
                    if(producedTypeArgument == null){
                        if(mode == TypeMappingMode.NORMAL)
                            producedTypeArgument = obtainType(moduleScope, typeArgument, scope, TypeLocation.TYPE_PARAM, variance);
                        else
                            producedTypeArgument = obtainTypeParameterBound(moduleScope, typeArgument, scope, rawDeclarationsSeen);
                    }
                    typeArguments.add(producedTypeArgument);
                }
                Type qualifyingType = null;
                if(type.getQualifyingType() != null){
                    qualifyingType = getNonPrimitiveType(moduleScope, type.getQualifyingType(), scope, variance);
                }
                Type ret = declaration.appliedType(qualifyingType, typeArguments);
                if(siteVarianceMap != null){
                    ret.setVarianceOverrides(siteVarianceMap);
                }
                ret.setUnderlyingType(type.getQualifiedName());
                ret.setRaw(isRaw);

                return ret;
            }finally{
                if(rawDeclarationsSeen != null)
                    rawDeclarationsSeen.remove(declaration);
            }
        }
        // we have no type args, but perhaps we have a qualifying type which has some?
        if(type.getQualifyingType() != null){
            // that one may have type arguments
            Type qualifyingType = getNonPrimitiveType(moduleScope, type.getQualifyingType(), scope, variance);
            Type ret = declaration.appliedType(qualifyingType, Collections.<Type>emptyList());
            ret.setUnderlyingType(type.getQualifiedName());
            ret.setRaw(isRaw);
            return ret;
        }
        // no type arg and no qualifying type
        return declaration.getType();
    }

    private Type obtainTypeParameterBound(Module moduleScope, TypeMirror type, Scope scope, Set<TypeDeclaration> rawDeclarationsSeen) {
        // type variables are never mapped
        if(type.getKind() == TypeKind.TYPEVAR){
            TypeParameterMirror typeParameter = type.getTypeParameter();
            if(!typeParameter.getBounds().isEmpty()){
                List<Type> bounds = new ArrayList<Type>(typeParameter.getBounds().size());
                for(TypeMirror bound : typeParameter.getBounds()){
                    Type boundModel = obtainTypeParameterBound(moduleScope, bound, scope, rawDeclarationsSeen);
                    bounds.add(boundModel);
                }
                return intersection(bounds, getUnitForModule(moduleScope));
            }else
                // no bound is Object
                return typeFactory.getObjectType();
        }else{
            TypeMirror mappedType = applyTypeMapping(type, TypeLocation.TYPE_PARAM);

            TypeDeclaration declaration = (TypeDeclaration) convertNonPrimitiveTypeToDeclaration(moduleScope, mappedType, scope, DeclarationType.TYPE);
            if(declaration == null){
                throw new RuntimeException("Failed to find declaration for "+type);
            }
            if(declaration instanceof UnknownType)
                return declaration.getType();

            Type ret = applyTypeArguments(moduleScope, declaration, type, scope, VarianceLocation.CONTRAVARIANT, TypeMappingMode.GENERATOR, rawDeclarationsSeen);
            
            if (ret.getUnderlyingType() == null) {
                ret.setUnderlyingType(getUnderlyingType(type, TypeLocation.TYPE_PARAM));
            }
            return ret;
        }
    }
    
    /*private Type getQualifyingType(TypeDeclaration declaration) {
        // As taken from Type.getType():
        if (declaration.isMember()) {
            return((ClassOrInterface) declaration.getContainer()).getType();
        }
        return null;
    }*/

    @Override
    public Type getType(Module module, String pkgName, String name, Scope scope)  {
        Declaration decl = getDeclaration(module, pkgName, name, scope);
        if(decl == null)
            return null;
        if(decl instanceof TypeDeclaration)
            return ((TypeDeclaration) decl).getType();
        // it's a method or non-object value, but it's not a type
        return null;
    }

    @Override
    public Declaration getDeclaration(Module module, String pkgName, String name, Scope scope)  {
        synchronized(getLock()){
            if(scope != null){
                TypeParameter typeParameter = lookupTypeParameter(scope, name);
                if(typeParameter != null)
                    return typeParameter;
            }
            if(!isBootstrap || !name.startsWith(CEYLON_LANGUAGE)) {
                if(scope != null && pkgName != null){
                    Package containingPackage = ModelUtil.getPackageContainer(scope);
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
                        if(JvmBackendUtil.isValue(declaration)
                                && ((Value)declaration).getTypeDeclaration().getName().equals(relativeName))
                            declaration = ((Value)declaration).getTypeDeclaration();
                        if(declaration != null)
                            return declaration;
                    }
                }
                return convertToDeclaration(module, name, DeclarationType.TYPE);
            }

            return findLanguageModuleDeclarationForBootstrap(name);
        }
    }

    private Declaration findLanguageModuleDeclarationForBootstrap(String name) {
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
            return typeFactory.getNothingDeclaration();

        // find the right package
        Package pkg = languageModule.getDirectPackage(pkgName);
        if(pkg != null){
            Declaration member = pkg.getDirectMember(simpleName, null, false);
            // if we get a value, we want its type
            if(JvmBackendUtil.isValue(member)
                    && ((Value)member).getTypeDeclaration().getName().equals(simpleName)){
                member = ((Value)member).getTypeDeclaration();
            }
            if(member != null)
                return member;
        }
        throw new ModelResolutionException("Failed to look up given type in language module while bootstrapping: "+name);
    }

    public void removeDeclarations(List<Declaration> declarations)  {
        synchronized(getLock()){
            // keep in sync with getOrCreateDeclaration
            for (Declaration decl : declarations) {
                String fqn = decl.getQualifiedNameString().replace("::", ".");
                Module module = ModelUtil.getModuleContainer(decl.getContainer());
                Map<String, Declaration> firstCache = null;
                Map<String, Declaration> secondCache = null;
                if(decl.isToplevel()){
                    if(JvmBackendUtil.isValue(decl)){
                        firstCache = valueDeclarationsByName;
                        TypeDeclaration typeDeclaration = ((Value)decl).getTypeDeclaration();
                        if (typeDeclaration != null) {
                            if(typeDeclaration.isAnonymous()) {
                                secondCache = typeDeclarationsByName;
                            }
                        } else {
                            // The value declaration has probably not been fully loaded yet.
                            // => still try to clean the second cache also, just in case it is an anonymous object
                            secondCache = typeDeclarationsByName;
                        }
                    }else if(JvmBackendUtil.isMethod(decl))
                        firstCache = valueDeclarationsByName;
                }
                if(decl instanceof ClassOrInterface){
                    firstCache = typeDeclarationsByName;
                }
                // ignore declarations which we do not cache, like member method/attributes
                String key = cacheKeyByModule(module, fqn);
                if(firstCache != null) {
                    firstCache.remove(key);
                    firstCache.remove(key + "_");

                    if(secondCache != null) {
                        secondCache.remove(key);
                        secondCache.remove(key + "_");
                    }
                }

                classMirrorCache.remove(key);
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

    public void printStats() {
        synchronized(getLock()){
            Map<Package, Stats> loadedByPackage = new HashMap<Package, Stats>();
            int loaded = inspectForStats(typeDeclarationsByName, loadedByPackage)
                    + inspectForStats(valueDeclarationsByName, loadedByPackage);
            logVerbose("[Model loader: "+loaded+"(loaded)/"+(typeDeclarationsByName.size()+valueDeclarationsByName.size())+"(total) declarations]");
            for(Entry<Package, Stats> packageEntry : loadedByPackage.entrySet()){
                logVerbose("[ Package "+packageEntry.getKey().getNameAsString()+": "
                        +packageEntry.getValue().loaded+"(loaded)/"+packageEntry.getValue().total+"(total) declarations]");
            }
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
    public Module getLoadedModule(String moduleName, String version) {
        return findModule(moduleName, version);
    }

    public Module getLanguageModule() {
        return modules.getLanguageModule();
    }

    public Module findModule(String name, String version){
        return moduleManager.findLoadedModule(name, version);
    }
    
    public Module getJDKBaseModule() {
        return findModule(JAVA_BASE_MODULE_NAME, JDKUtils.jdk.version);
    }

    public Module findModuleForFile(File file){
        File path = file.getParentFile();
        while (path != null) {
            String name = path.getPath().replaceAll("[\\\\/]", ".");
            // FIXME: this would load any version of this module
            Module m = getLoadedModule(name, null);
            if (m != null) {
                return m;
            }
            path = path.getParentFile();
        }
        return modules.getDefaultModule();
    }

    public abstract Module findModuleForClassMirror(ClassMirror classMirror);
    
    protected boolean isTypeHidden(Module module, String qualifiedName){
        return module.getNameAsString().equals(JAVA_BASE_MODULE_NAME)
                && qualifiedName.equals("java.lang.Object");
    }
    
    public Package findPackage(String quotedPkgName)  {
        synchronized(getLock()){
            String pkgName = quotedPkgName.replace("$", "");
            // in theory we only have one package with the same name per module in javac
            for(Package pkg : packagesByName.values()){
                if(pkg.getNameAsString().equals(pkgName))
                    return pkg;
            }
            return null;
        }
    }

    /**
     * See explanation in cacheModulelessPackages() below. This is called by LanguageCompiler during loadCompiledModules().
     */
    public LazyPackage findOrCreateModulelessPackage(String pkgName)  {
        synchronized(getLock()){
            LazyPackage pkg = modulelessPackages.get(pkgName);
            if(pkg != null)
                return pkg;
            pkg = new LazyPackage(this);
            // FIXME: some refactoring needed
            pkg.setName(pkgName == null ? Collections.<String>emptyList() : Arrays.asList(pkgName.split("\\.")));
            modulelessPackages.put(pkgName, pkg);
            return pkg;
        }
    }
    
    /**
     * Stef: this sucks balls, but the typechecker wants Packages created before we have any Module set up, including for parsing a module
     * file, and because the model loader looks up packages and caches them using their modules, we can't really have packages before we
     * have modules. Rather than rewrite the typechecker, we create moduleless packages during parsing, which means they are not cached with
     * their modules, and after the loadCompiledModules step above, we fix the package modules. Remains to be done is to move the packages
     * created from their cache to the right per-module cache.
     */
    public void cacheModulelessPackages() {
        synchronized(getLock()){
            for(LazyPackage pkg : modulelessPackages.values()){
                String quotedPkgName = JVMModuleUtil.quoteJavaKeywords(pkg.getQualifiedNameString());
                if (pkg.getModule() != null) {
                    packagesByName.put(cacheKeyByModule(pkg.getModule(), quotedPkgName), pkg);
                }
            }
            modulelessPackages.clear();
        }
    }

    /**
     * Stef: after a lot of attempting, I failed to make the CompilerModuleManager produce a LazyPackage when the ModuleManager.initCoreModules
     * is called for the default package. Because it is called by the PhasedUnits constructor, which is called by the ModelLoader constructor,
     * which means the model loader is not yet in the context, so the CompilerModuleManager can't obtain it to pass it to the LazyPackage
     * constructor. A rewrite of the logic of the typechecker scanning would fix this, but at this point it's just faster to let it create
     * the wrong default package and fix it before we start parsing anything.
     */
    public void fixDefaultPackage()  {
        synchronized(getLock()){
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
    }

    public boolean isImported(Module moduleScope, Module importedModule) {
        if(ModelUtil.equalModules(moduleScope, importedModule))
            return true;
        if(isImportedSpecialRules(moduleScope, importedModule))
            return true;
        boolean isMaven = isAutoExportMavenDependencies() && isMavenModule(moduleScope);
        if(isMaven && isMavenModule(importedModule))
            return true;
        Set<Module> visited = new HashSet<Module>();
        visited.add(moduleScope);
        for(ModuleImport imp : moduleScope.getImports()){
            if(ModelUtil.equalModules(imp.getModule(), importedModule))
                return true;
            if((imp.isExport() || isMaven) && isImportedTransitively(imp.getModule(), importedModule, visited))
                return true;
        }
        return false;
    }

    private boolean isMavenModule(Module moduleScope) {
        return moduleScope.isJava() && ModuleUtil.isMavenModule(moduleScope.getNameAsString());
    }
    
    private boolean isImportedSpecialRules(Module moduleScope, Module importedModule) {
        String importedModuleName = importedModule.getNameAsString();
        // every Java module imports the JDK
        // ceylon.language imports the JDK
        if((moduleScope.isJava()
                || ModelUtil.equalModules(moduleScope, getLanguageModule()))
                && (JDKUtils.isJDKModule(importedModuleName)
                   || JDKUtils.isOracleJDKModule(importedModuleName)))
            return true;
        // everyone imports the language module
        if(ModelUtil.equalModules(importedModule, getLanguageModule()))
            return true;
        if(ModelUtil.equalModules(moduleScope, getLanguageModule())){
            // this really sucks, I suppose we should set that up better somewhere else
            if((importedModuleName.equals("com.redhat.ceylon.compiler.java")
                || importedModuleName.equals("com.redhat.ceylon.typechecker")
                || importedModuleName.equals("com.redhat.ceylon.common")
                || importedModuleName.equals("com.redhat.ceylon.model")
                || importedModuleName.equals("com.redhat.ceylon.module-resolver"))
                && importedModule.getVersion().equals(Versions.CEYLON_VERSION_NUMBER))
                return true;
            if(importedModuleName.equals("org.jboss.modules")
                    && importedModule.getVersion().equals("1.3.3.Final"))
                return true;
        }
        return false;
    }
    
    private boolean isImportedTransitively(Module moduleScope, Module importedModule, Set<Module> visited) {
        if(!visited.add(moduleScope))
            return false;
        boolean isMaven = isAutoExportMavenDependencies() && isMavenModule(moduleScope);
        for(ModuleImport imp : moduleScope.getImports()){
            // only consider exported transitive deps
            if(!imp.isExport() && !isMaven)
                continue;
            if(ModelUtil.equalModules(imp.getModule(), importedModule))
                return true;
            if(isImportedSpecialRules(imp.getModule(), importedModule))
                return true;
            if(isImportedTransitively(imp.getModule(), importedModule, visited))
                return true;
        }
        return false;
    }

    protected boolean isModuleOrPackageDescriptorName(String name) {
        return name.equals(NamingBase.MODULE_DESCRIPTOR_CLASS_NAME) || name.equals(NamingBase.PACKAGE_DESCRIPTOR_CLASS_NAME);
    }
    
    protected void loadJavaBaseArrays(){
        convertToDeclaration(getJDKBaseModule(), JAVA_LANG_OBJECT_ARRAY, DeclarationType.TYPE);
        convertToDeclaration(getJDKBaseModule(), JAVA_LANG_BOOLEAN_ARRAY, DeclarationType.TYPE);
        convertToDeclaration(getJDKBaseModule(), JAVA_LANG_BYTE_ARRAY, DeclarationType.TYPE);
        convertToDeclaration(getJDKBaseModule(), JAVA_LANG_SHORT_ARRAY, DeclarationType.TYPE);
        convertToDeclaration(getJDKBaseModule(), JAVA_LANG_INT_ARRAY, DeclarationType.TYPE);
        convertToDeclaration(getJDKBaseModule(), JAVA_LANG_LONG_ARRAY, DeclarationType.TYPE);
        convertToDeclaration(getJDKBaseModule(), JAVA_LANG_FLOAT_ARRAY, DeclarationType.TYPE);
        convertToDeclaration(getJDKBaseModule(), JAVA_LANG_DOUBLE_ARRAY, DeclarationType.TYPE);
        convertToDeclaration(getJDKBaseModule(), JAVA_LANG_CHAR_ARRAY, DeclarationType.TYPE);
    }
    
    /**
     * To be overridden by subclasses, defaults to false.
     */
    protected boolean isAutoExportMavenDependencies(){
        return false;
    }

    /**
     * To be overridden by subclasses, defaults to false.
     */
    protected boolean isFlatClasspath(){
        return false;
    }
}
