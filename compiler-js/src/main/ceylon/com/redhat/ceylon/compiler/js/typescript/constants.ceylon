import ceylon.language.meta.declaration {
    ValueDeclaration
}
import ceylon.language.meta.model {
    Value
}
import ceylon.json {
    JsonArray,
    JsonObject
}

// general constants

String languageVersion = "1.3.1";
String binaryVersion = "9.1";

// JSON keys and values

"Annotation to record the corresponding constant in the JS compiler
 (mostly located in `MetamodelGenerator`)."
annotation final class CompilerConstantAnnotation(
    """The constant name, or `""` to indicate that the JS compiler
       has no constant for this key (hard-coded)."""
    // TODO change to optional type one #3956 is implemented
    shared String compilerConstant)
        satisfies OptionalAnnotation<CompilerConstantAnnotation,ValueDeclaration,Value<String>> {}
annotation CompilerConstantAnnotation compilerConstant(String constant)
        => CompilerConstantAnnotation(constant);

// module keys
"Key for the module name in the toplevel object."
compilerConstant ("")
shared String moduleNameKey = "$mod-name";
"Key for the module version in the toplevel object."
compilerConstant ("")
shared String moduleVersionKey = "$mod-version";
"Key for the binary version in the toplevel object."
see (`value binaryVersion`)
compilerConstant ("")
shared String moduleBinaryVersionKey = "$mod-bin";
"Key for the dependency array in the toplevel object."
compilerConstant ("")
shared String moduleDependenciesKey = "$mod-deps";

// declaration / type keys
"Key for the packed annotations of a declaration."
compilerConstant ("KEY_PACKED_ANNS")
shared String packedAnnotationsKey = "pa";
"Key for the type of a declaration."
compilerConstant ("KEY_TYPE")
shared String typeKey = "$t";
"Key for the meta type of a declaration (class, interface, function, value, etc.)."
compilerConstant ("KEY_METATYPE")
shared String metaTypeKey = "mt";
"Key for the name of a declaration."
compilerConstant ("KEY_NAME")
shared String nameKey = "nm";
"Key for the dynamic flag of a declaration."
compilerConstant ("KEY_DYNAMIC")
shared String dynamicKey = "dyn";
"Key for the flags of a function or method."
compilerConstant ("KEY_FLAGS")
shared String flagsKey = "$ff";
"Key for the parameters of a function, method, class or constructor."
compilerConstant ("KEY_PARAMS")
shared String parametersKey = "ps";
"Key to indicate that a parameter has a default value."
compilerConstant ("KEY_DEFAULT")
shared String defaultKey = "def";
"Key for the attributes of a class or interface."
compilerConstant ("KEY_ATTRIBUTES")
shared String attributesKey = "$at";
"Key for the methods of a class or interface."
compilerConstant ("KEY_METHODS")
shared String methodsKey = "$m";
"Key for the module of a type."
compilerConstant ("KEY_MODULE")
shared String moduleKey = "md";
"Key for the package of a type."
compilerConstant ("KEY_PACKAGE")
shared String packageKey = "pk";

// meta type values
"Meta type for an attribute or toplevel value."
compilerConstant ("METATYPE_ATTRIBUTE")
shared String attributeMetaType = "a";
"Meta type for a method or toplevel function."
compilerConstant ("METATYPE_METHOD")
shared String methodMetaType = "m";
"Meta type for a parameter."
compilerConstant ("METATYPE_PARAMETER")
shared String parameterMetaType = "prm";
"Meta type for a class."
compilerConstant ("METATYPE_CLASS")
shared String classMetaType = "c";
"Meta type for a interface."
compilerConstant ("METATYPE_INTERFACE")
shared String interfaceMetaType = "i";

// module / package values
"Abbreviation for the current module or package."
shared String currentModuleOrPackage = ".";
"Abbreviation for the language module or package."
shared String languageModuleOrPackage = "$";

// commonly used objects

JsonObject stringTypeForModel = JsonObject {
    moduleKey->languageModuleOrPackage,
    packageKey->languageModuleOrPackage,
    nameKey->"String"
};
JsonObject floatTypeForModel = JsonObject {
    moduleKey->languageModuleOrPackage,
    packageKey->languageModuleOrPackage,
    nameKey->"Float"
};
JsonObject integerTypeForModel = JsonObject {
    moduleKey->languageModuleOrPackage,
    packageKey->languageModuleOrPackage,
    nameKey->"Integer"
};
JsonObject floatOrIntegerTypeForModel = JsonObject {
    "comp"->"u",
    "l" -> JsonArray { floatTypeForModel, integerTypeForModel }
};
JsonObject booleanTypeForModel = JsonObject {
    moduleKey->languageModuleOrPackage,
    packageKey->languageModuleOrPackage,
    nameKey->"Boolean"
};
JsonObject anythingTypeForModel = JsonObject {
    moduleKey->languageModuleOrPackage,
    packageKey->languageModuleOrPackage,
    nameKey->"Anything"
};
