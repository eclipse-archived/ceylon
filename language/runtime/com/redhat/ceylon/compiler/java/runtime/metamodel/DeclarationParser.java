package com.redhat.ceylon.compiler.java.runtime.metamodel;

import ceylon.language.meta.declaration.ClassOrInterfaceDeclaration;
import ceylon.language.meta.declaration.Declaration;
import ceylon.language.meta.declaration.FunctionDeclaration;
import ceylon.language.meta.declaration.Module;
import ceylon.language.meta.declaration.Package;
import ceylon.language.meta.declaration.ValueDeclaration;

/**
 * <p>A reusable but non-threadsafe parser for the serialized form of 
 * Declarations used for annotations.</p>
 * 
 * <pre> 
 * ref              ::= version? module ;
 *                      // note: version is optional to support looking up the
 *                      // runtime version of a package, once we support this
 * version          ::= ':' SENTINEL ANYCHAR* SENTINEL ;
 * module           ::= dottedIdent package? ;
 * dottedIdent      ::= ident ('.' ident)* ;
 * package          ::= ':' ( relativePackage | absolutePackage ) ? ( ':' declaration ) ? ;
 *                      // note: if no absolute or relative package given, it's the 
 *                      // root package of the module
 * relativePackage  ::= dottedIdent ;
 * absolutePackage  ::= '.' dottedIdent ;
 *                      // note: to suport package names which don't start 
 *                      // with the module name
 * declaration      ::= type | function | value ;
 * type             ::= class | interface ;
 * class            ::= 'C' ident ( '.' member )?
 * interface        ::= 'I' ident ( '.' member )?
 * member           ::= declaration ;
 * function         ::= 'F' ident ;
 * value            ::= 'V' ident ;
 * </pre>
 * TODO Alias
 */

class DeclarationParser {
    private int i = 0;
    private String ref;
    
    private RuntimeException parseError(String msg) {
        throw new RuntimeException(msg + " at index " + i + ": " + ref);
    }
    
    private RuntimeException unexpectedToken () {
        return parseError("Unexpected token");
    }

    private boolean atEnd() {
        return i == ref.length();
    }
    
    private boolean at(char token) {
        if (!atEnd() && ref.charAt(i) == token) {
            i += 1;
            return true;
        }
        return false;
    }
    
    public Declaration ref(String ref) {
        i = 0;
        this.ref = ref;
        String version = version();
        Declaration module = module(version);
        return module;
    }
    
    private String version() {
        if (!at(':')) {
            return null;
        }
        // Because a Ceylon version can contain *any* character 
        // the next character we read is the sentinel and 
        // the version is everything after that, until that sentinal next 
        // occurs
        char sentinal = ref.charAt(i);
        i++;
        int start = i;
        while(i < ref.length()) {
            if (ref.charAt(i) == sentinal) {
                i++;
                return ref.substring(start, i-1);
            }
            i++;
        }
        throw parseError("Unterminated version");
    }

    private Declaration module(String version) {
        String moduleName = moduleName();
        if (moduleName == null || moduleName.isEmpty()) {
            throw parseError("Missing module name");
        }
        Module module = makeModule(moduleName, version);
        if (atEnd()) {
            return module;
        }
        return package_(module);
        
    }
    
    private String moduleName() {
        return dottedIdent();
    }
    
    private boolean atIdent() {
        int start = i;
        while (i < ref.length()) {
            char ch = ref.charAt(i);
            if (!(Character.isLetter(ch)
                    || Character.isDigit(ch)
                    || ch == '_')) {
                break;
            }
            i++;
        }
        return i > start;
    }
    
    private String ident() {
        int start = i;
        if (atIdent()) {
            return ref.substring(start, i);
        }
        return null;
    }
    
    private String dottedIdent() {
        int start = i;
        while (atIdent()) {
            if (atEnd() || !at('.')) {
                break;
            }
        }
        return ref.substring(start, i);
    }
    
    private Declaration package_(Module module) {
        if (!at(':')) {
            throw parseError("Expecting a colon at start of package");
        }
        String packageName;
        if (at(':')) {
            packageName = module.getName();
        } else if (at('.')) {
           packageName = packageName();
        } else {
            packageName = module.getName() + '.' + packageName();
        }
        Package package_ = makePackage(module, packageName);
        if (atEnd()) {
            return package_;
        } 
        return declaration(package_);
    }

    private String packageName() {
        return dottedIdent();
    }
    
    private Declaration declaration(Declaration packageOrType) {
        Declaration result = type(packageOrType);
        if (result == null) {
            result = function(packageOrType);
        }
        if (result == null) {
            result = value(packageOrType);
        }
        if (result == null) {
            throw parseError("Expected type or function or value");
        }
        return result;
    }
    
    private Declaration function(Declaration packageOrType) {
        if (!at('F')) {
            return null;
        }
        String fn = ident();
        if (fn != null && atEnd()) {
            return makeFunction(packageOrType, fn);
        } else {
            throw unexpectedToken();
        }
    }
    
    private Declaration value(Declaration packageOrType) {
        if (!at('V')) {
            return null;
        }
        String fn = ident();
        if (fn != null && atEnd()) {
            return makeValue(packageOrType, fn);
        } else {
            throw unexpectedToken();
        }
    }

    private Declaration type(Declaration packageOrType) {
        ClassOrInterfaceDeclaration result = class_(packageOrType);
        if (result == null) {
            result = interface_(packageOrType);
        }
        
        if (result != null && at('.')) {
            return declaration(result);
        }
        return result;
    }

    private ClassOrInterfaceDeclaration class_(Declaration packageOrType) {
        if (!at('C')) {
            return null;
        }
        String fn = ident();
        if (fn != null) {
            return makeClassOrInterface(packageOrType, fn);
        }
        throw unexpectedToken();
    }
    
    private ClassOrInterfaceDeclaration interface_(Declaration packageOrType) {
        if (!at('I')) {
            return null;
        }
        String fn = ident();
        if (fn != null) {
            return makeClassOrInterface(packageOrType, fn);
        }
        throw unexpectedToken();
    }
    
    private RuntimeException metamodelError(String msg) {
        return new RuntimeException(msg);
    }
    
    private RuntimeException metamodelNotFound(String msg) {
        return metamodelError(msg);
    }
    
    protected Module makeModule(String moduleName, String version) {
        if (version == null) {
            throw metamodelError("Runtime versions not yet supported");
        }
        Module module = ceylon.language.meta.modules_.get_().find(moduleName, version);
        if (module == null) {
            throw metamodelNotFound("Could not find module: " + moduleName + ", version: " + version);
        }
        return module;
    }
    
    protected Package makePackage(Module module, String packageName) {
        Package package_ = module.findPackage(packageName);
        if (package_ == null) {
            throw metamodelNotFound("Could not find package: " + packageName + " of module: " + module.getName() + ", version: " + module.getVersion());
        }
        return package_;
    }
    
    // .ceylon.language.meta.model.typeLiteral_.typeLiteral(.ceylon.language.Anything.$TypeDescriptor)
    protected ClassOrInterfaceDeclaration makeClassOrInterface(Declaration packageOrType, String typeName) {
        final ClassOrInterfaceDeclaration result;
        if (packageOrType instanceof Package) {
            result = ((Package)packageOrType).getClassOrInterface(typeName);
        } else if (packageOrType instanceof ClassOrInterfaceDeclaration) {
            result = ((ClassOrInterfaceDeclaration)packageOrType).<ClassOrInterfaceDeclaration>getMemberDeclaration(ClassOrInterfaceDeclaration.$TypeDescriptor, typeName);
        } else {
            throw metamodelError("Unexpected container " + packageOrType.getClass() + " for type " + typeName);
        }
        if (result == null) {
            throw metamodelNotFound("Could not find type: " + typeName + " in " + packageOrType.getName());
        }
        return result;
    }
    
    protected FunctionDeclaration makeFunction(Declaration packageOrType, String fn) {
        final FunctionDeclaration result;
        if (packageOrType instanceof Package) {
            result = ((Package)packageOrType).getFunction(fn);
        } else if (packageOrType instanceof ClassOrInterfaceDeclaration) {
            result = ((ClassOrInterfaceDeclaration)packageOrType).<FunctionDeclaration>getMemberDeclaration(FunctionDeclaration.$TypeDescriptor, fn);
        } else {
            throw metamodelError("Unexpected container " + packageOrType.getClass() + " for function " + fn);
        }
        if (result == null) {
            throw metamodelNotFound("Could not find function: " + fn + " in " + packageOrType.getName());
        }
        return result;
    }
    
    
    protected ValueDeclaration makeValue(Declaration packageOrType, String val) {
        final ValueDeclaration result;
        if (packageOrType instanceof Package) {
            result = ((Package)packageOrType).getValue(val);
        } else if (packageOrType instanceof ClassOrInterfaceDeclaration) {
            result = ((ClassOrInterfaceDeclaration)packageOrType).<ValueDeclaration>getMemberDeclaration(ValueDeclaration.$TypeDescriptor, val);
        } else {
            throw metamodelError("Unexpected container " + packageOrType.getClass() + " for value " + val);
        }
        if (result == null) {
            throw metamodelNotFound("Could not find value: " + val + " in " + packageOrType.getName());
        }
        return result;
    }

}
