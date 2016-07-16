import ceylon.language.meta.declaration {
    ValueDeclaration
}
import ceylon.language.meta.model {
    Value
}

// general constants

String languageVersion = "1.2.3";
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

// meta type values
"Meta type for an attribute or toplevel value."
compilerConstant ("METATYPE_ATTRIBUTE")
shared String attributeMetaType = "a";
