/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.compiler.js.util;

import static org.eclipse.ceylon.model.typechecker.model.ModelUtil.getRealScope;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.ceylon.common.Versions;
import org.eclipse.ceylon.compiler.js.JsCompiler;
import org.eclipse.ceylon.model.typechecker.model.Class;
import org.eclipse.ceylon.model.typechecker.model.ClassOrInterface;
import org.eclipse.ceylon.model.typechecker.model.Constructor;
import org.eclipse.ceylon.model.typechecker.model.Declaration;
import org.eclipse.ceylon.model.typechecker.model.Function;
import org.eclipse.ceylon.model.typechecker.model.FunctionOrValue;
import org.eclipse.ceylon.model.typechecker.model.ModelUtil;
import org.eclipse.ceylon.model.typechecker.model.Module;
import org.eclipse.ceylon.model.typechecker.model.Package;
import org.eclipse.ceylon.model.typechecker.model.Parameter;
import org.eclipse.ceylon.model.typechecker.model.Scope;
import org.eclipse.ceylon.model.typechecker.model.TypeAlias;
import org.eclipse.ceylon.model.typechecker.model.TypeDeclaration;
import org.eclipse.ceylon.model.typechecker.model.TypeParameter;

/**
 * Manages the identifier names in the JavaScript code generated for a Ceylon
 * compilation unit.
 *
 * @author Ivo Kasiuk
 */
public class JsIdentifierNames {
    private final JsCompiler compiler;
    
    private static long uniqueID = 0;
    private static long nextUID() {
        if (++uniqueID <= 0) {
            uniqueID = 1;
        }
        return uniqueID;
    }

    private static Set<String> trueReservedWords = new HashSet<String>();
    private static Set<String> reservedWords = new HashSet<String>();
    private static Set<String> globals = new HashSet<String>();

    static {
        // Identifiers that have to be escaped because they are keywords in
        // JavaScript. We don't have to include identifiers that are also
        // keywords in Ceylon because no such identifiers can occur in Ceylon
        // source code anyway.
        //Language
        trueReservedWords.addAll(Arrays.asList("undefined", "boolean", "byte", "char", "const",
                "debugger", "default", "delete", "do", "double", "enum", "export", "false",
                "final", "float", "goto", "implements", "instanceof", "int", "long",
                "native", "new", "null", "private", "protected", "public", "short", "static",
                "synchronized", "throws", "transient", "true", "typeof", "var", "volatile",
                "with", "abstract", "process", "require", "class", "extends", "import",
                //These are only in strict mode and supposedly break something in the SDK or something
                //"interface", "let", "package", "yield",
                "await", "break", "case", "catch", "continue", "else", "finally", "for",
                "function", "if", "in", "return", "switch", "this", "throw", "try", "while", "with",
                "super"));
        reservedWords.addAll(trueReservedWords);
        //Types
        reservedWords.addAll(Arrays.asList("Date", "Object", "Boolean", "Error", "Number", "RegExp"));
        //JS Object
        reservedWords.addAll(Arrays.asList("hasOwnProperty", "isPrototypeOf", "propertyIsEnumerable"));
        //JS Function
        reservedWords.add("Function");
        reservedWords.addAll(Arrays.asList("call", "arguments", "caller", "apply", "bind", "eval")); //name?
        //JS Number
        reservedWords.addAll(Arrays.asList("toFixed", "valueOf", "toPrecision", "toExponential"));
        //JS String
        reservedWords.add("String");
        reservedWords.addAll(Arrays.asList("charAt", "strike", "fixed", "sub", "charCodeAt",
                "trimLeft", "toLocaleUpperCase", "toUpperCase", "fontsize", "search",
                "toLocaleLowerCase", "small", "big", "fontcolor", "blink", "trim",
                "bold", "match", "substr", "trimRight", "replace", "split", "sup", "link",
                "localeCompare", "valueOf", "substring", "toLowerCase", "italics", "anchor"));
        //JS Array
        reservedWords.add("Array");
        reservedWords.addAll(Arrays.asList("toLocaleString", "splice", "map", "forEach", "reverse",
                "join", "push", "shift", "pop", "sort", "unshift", "reduceRight", "reduce",
                "every", "filter"));
        //String, Array, etc
        reservedWords.addAll(Arrays.asList("length", "toString", "constructor", "prototype",
                "concat", "indexOf", "lastIndexOf", "slice", "get"));
        //Global identifiers. Like reserved words but only affects toplevel declarations, inside the same
        //module
        globals.addAll(Arrays.asList("parseFloat", "uneval", "isFinite", "isNaN", "parseInt",
                "decodeURI", "decodeURIComponent", "encodeURI", "encodeURIComponent",
                "escape", "unescape", "Symbol", "EvalError", "InternalError", "RangeError",
                "ReferenceError", "SyntaxError", "TypeError", "URIError", "Math", "DataView",
                "JSON", "ArrayBuffer"));
    }

    public static boolean isTrueReservedWord(String token) {
        return trueReservedWords.contains(token);
    }

    public static boolean isReservedWord(String token) {
        return reservedWords.contains(token);
    }

    public JsIdentifierNames(JsCompiler compiler) {
        this.compiler = compiler;
    }
    
    /**
     * Determine the identifier name to be used in the generated JavaScript code
     * to represent the given declaration.
     */
    public String name(Declaration decl) {
        return getName(decl, false, false);
    }

    public String name(Parameter param) {
        if (param == null) { return null; }
        String name = param.getName();
        FunctionOrValue decl = param.getModel();
        final boolean nonLocal = 
                   decl.isShared() && decl.isMember()
                || decl.isToplevel() && decl instanceof Function;
        if (nonLocal) {
            // The identifier might be accessed from other .js files, so it must
            // be reliably reproducible. In most cases simply using the original
            // name is ok because otherwise it would result in a name collision in
            // Ceylon too. We just have to take care of a few exceptions:
            String suffix = nestingSuffix(decl, false);
            if (suffix.length() > 0) {
                // nested type
                name += suffix;
            } else if (reservedWords.contains(name)) {
                // JavaScript keyword
                name = "$_" + name;
            }
        }
        else {
            // The identifier will not be used outside the generated .js file,
            // so we can simply disambiguate it with a numeric ID.
            name = uniquePrivateName(decl, false);
        }
        return JsUtils.escapeStringLiteral(name);
    }

    public String valueName(FunctionOrValue d) {
        return d.isShared() && !d.isActual() ? 
                name(d) + "_" : 
                privateName(d);
    }

    /**
     * Determine a secondary, private identifier name for the given declaration.
     */
    public String privateName(Declaration decl) {
        return getName(decl, false, true);
    }

    public String objectName(Declaration decl) {
        String name = getName(decl, true, false);
        if (reservedWords.contains(name)) {
            name = "$_" + name;
        }
        if (decl.isToplevel()) {
            //TODO remove this shit when we break bincompat again
            final int binMajor = decl.getUnit().getPackage().getModule().getJsMajor();
            if (binMajor > 0 && binMajor < Versions.JS_BINARY_MAJOR_VERSION) {
                return String.format("get%c%s()",
                        Character.toUpperCase(name.charAt(0)), name.substring(1));
            }
            return String.format("%s()", name);
        }
        return name;
    }

    /**
     * Determine the function name to be used in the generated JavaScript code
     * for the getter of the given declaration.
     * @param decl The declaration whose name we have to generate.
     * @param forMetamodel indicates whether the name is for metamodel purposes ($prop$getBla)
     */
    public String getter(Declaration decl, boolean forMetamodel) {
        if (decl == null) { return ""; }
        String name = getName(decl, true, false);
        //TODO remove this shit when we break bincompat again
        final int binMajor = decl.getUnit().getPackage().getModule().getJsMajor();
        if (!forMetamodel && !decl.isClassOrInterfaceMember() 
                && (binMajor == 0 || binMajor == Versions.JS_BINARY_MAJOR_VERSION)) {
            return reservedWords.contains(name) ? "$_" + name : name;
        }
        return String.format("%sget%c%s", forMetamodel?"$prop$":"",
                Character.toUpperCase(name.charAt(0)),
                name.substring(1));
    }

    /**
     * Determine the function name to be used in the generated JavaScript code
     * for the setter of the given declaration.
     */
    public String setter(Declaration decl) {
        String name = getName(decl, true, false);
        final Module mod = decl.getUnit().getPackage().getModule();
        if (mod.getJsMajor() > 0 
                && (mod.getJsMajor() < 9 
                        || (mod.getJsMajor() == 9 && mod.getJsMinor() < 1))) {
            return String.format("set%c%s", 
                    Character.toUpperCase(name.charAt(0)),
                    name.substring(1));
        }
        return String.format("set$%s", name);
    }

    /**
     * Determine the identifier to be used in the generated JavaScript code as
     * an alias for the given package.
     */
    public String moduleAlias(Module pkg) {
        if (compiler.isCompilingLanguageModule() 
                && pkg.getLanguageModule()==pkg) {
            //If we're compiling the language module, omit the package name
            return "";
        }
        StringBuilder sb = new StringBuilder("m$");
        sb.append(Long.toString(getUID(pkg), 36));
        return sb.toString();
    }

    /** Creates a new unique identifier. */
    public String createTempVariable() {
        return "$"+Long.toString(nextUID(),36);
    }

    /**
     * Determine the identifier to be used for the self variable of the given type.
     */
    public String self(TypeDeclaration decl) {
        if (decl instanceof Constructor) {
            decl = (TypeDeclaration) decl.getContainer();
        }
        String name = JsUtils.escapeStringLiteral(decl.getName());
        if (decl.isShared() || decl.isToplevel()) {
            name += nestingSuffix(decl, true);
        } else {
            // The identifier will not be used outside the generated .js file,
            // so we can simply disambiguate it with a numeric ID.
            name = "$" + Long.toString(getUID(decl), 36);
        }
        return String.format("%c%s$", 
                Character.toLowerCase(name.charAt(0)),
                name.substring(1));
    }

    /**
     * Returns a disambiguation suffix for the given scope. It is guaranteed that
     * the suffixes generated for two different scopes are different.
     */
    public String scopeSuffix(Scope scope) {
        return String.format("$%s", 
                scope.getQualifiedNameString()
                    .replace("::","$")
                    .replace('.', '$'));
    }

    /**
     * Generates a disambiguation suffix if the given declaration is a nested
     * declaration whose name may collide with other declarations.
     * Currently this is required only for types which are nested inside other
     * types.
     */
    private String nestingSuffix(Declaration decl, final boolean forSelf) {
        String suffix = "";
        if (decl instanceof TypeDeclaration 
                && (forSelf || !decl.isAnonymous())
                && !ModelUtil.isConstructor(decl)) {
            // The generated suffix consists of the names of the enclosing types.
            StringBuilder sb = new StringBuilder();
            // Use the original declaration if it's an overriden class: an overriding
            // member must have the same name as the member it overrides.
            Scope scope = getRealScope(originalDeclaration(decl).getContainer());
            while (scope instanceof TypeDeclaration) {
                sb.append('$');
                sb.append(((TypeDeclaration) scope).getName().replaceAll("#", ""));
                scope = scope.getContainer();
            }
            suffix = sb.toString();
        }
        return suffix;
    }

    public void forceName(Declaration decl, String name) {
        if (name == null) {
            uniqueVarNames.remove(decl);
        } else {
            uniqueVarNames.put(decl, name);
        }
    }

    private Map<Module, Long> moduleUIDs = new HashMap<>();
    private Map<Declaration, Long> uniqueVarIDs = new HashMap<>();
    private Map<Declaration, String> uniqueVarNames =
            new HashMap<>();

    private String getName(Declaration decl, boolean forGetterSetter, boolean priv) {
        if (decl == null) { return null; }
        String name = decl.getName();
        if (name == null && ModelUtil.isConstructor(decl)) {
            return "$c$";
        }
        if (name.startsWith("anonymous#")) {
            name="anon$" + name.substring(10);
        }
        if (decl.isDynamic()) {
            return JsUtils.escapeStringLiteral(decl.getName());
        }
        boolean nonLocal = !priv;
        if (nonLocal) {
            // check if it's a shared member or a toplevel function
            nonLocal = decl.isMember() ? 
                    decl.isShared() || decl instanceof TypeDeclaration :
                    decl.isToplevel() 
                        && (forGetterSetter 
                                || decl instanceof Function
                                || decl instanceof ClassOrInterface 
                                || decl instanceof TypeAlias);
        }
        if (nonLocal && decl instanceof Class
                && ((Class)decl).isAnonymous() && !forGetterSetter) {
            // A lower-case class name belongs to an object and is not public.
            nonLocal = false;
        }
        if (nonLocal) {
            // The identifier might be accessed from other .js files, so it must
            // be reliably reproducible. In most cases simply using the original
            // name is ok because otherwise it would result in a name collision in
            // Ceylon too. We just have to take care of a few exceptions:
            String suffix = nestingSuffix(decl, false);
            if (suffix.length() > 0) {
                // nested type
                name += suffix;
            } else if ((!forGetterSetter 
                        && !ModelUtil.isConstructor(decl) 
                        && reservedWords.contains(name))
                    || isJsGlobal(decl)) {
                // JavaScript keyword or global declaration
                name = "$_" + name;
            }
        }
        else {
            // The identifier will not be used outside the generated .js file,
            // so we can simply disambiguate it with a numeric ID.
            name = uniquePrivateName(decl, priv);
        }
        //Fix #204 - same top-level declarations in different packages
        final Package declPkg = decl.getUnit().getPackage();
        Module declMod = declPkg.getModule();
        if (decl.isToplevel() 
                && !declPkg.equals(declMod.getRootPackage())) {
            final Package root = declMod.getRootPackage();
            //rootPackage can be null when compiling from IDE
            String rootName = root == null ?
                    (declMod.isDefaultModule() ? "" : declMod.getNameAsString()) :
                    root.getNameAsString();
            String pkgName = declPkg.getNameAsString();
            rootName = pkgName.substring(rootName.length()).replaceAll("\\.", "\\$");
            if (!rootName.isEmpty() && rootName.charAt(0) != '$') {
                rootName = '$' + rootName;
            }
            name += rootName;
        }
        if (decl instanceof TypeAlias) {
            name+="()";
        }
        return JsUtils.escapeStringLiteral(name);
    }

    private String uniquePrivateName(Declaration d, boolean priv) {
        String name = uniqueVarNames.get(d);
        if (name != null) {
            return name;
        }
        if (d.isClassOrInterfaceMember() 
                && !compiler.isCompilingLanguageModule()) {
            final String containerName = 
                    d.getUnit().getPackage().getModule().getNameAsString();
            return String.format(priv ? "$%s$%s_" : "$%s$%s",
                    Integer.toString(Math.abs(containerName.hashCode()),36),
                    Long.toString(getUID(d), 36));
        }
        return String.format(priv ? "$%s_" : "$%s", Long.toString(getUID(d), 36));
    }

    private Declaration originalDeclaration(Declaration decl) {
        Declaration refinedDecl = decl;
        while (true) {
            Declaration d = refinedDecl.getRefinedDeclaration();
            if (d == null || d == refinedDecl) break;
            refinedDecl = d;
        }
        return refinedDecl;
    }

    private long getUID(Declaration decl) {
        Long id = uniqueVarIDs.get(decl);
        if (id == null) {
            id = nextUID();
            uniqueVarIDs.put(decl, id);
        }
        return id;
    }

    private long getUID(Module pkg) {
        Long id = moduleUIDs.get(pkg);
        if (id == null) {
            id = nextUID();
            moduleUIDs.put(pkg, id);
        }
        return id;
    }

    /** The name for the argument that holds the type parameters of a method. */
    public String typeArgsParamName(Function m) {
        return "$" + Long.toString(Math.abs(m.getQualifiedNameString().hashCode()), 36) + "$";
    }

    public String typeParameterName(TypeParameter tp) {
        String cname;
        if (tp.getDeclaration().isAnonymous()) {
            cname = name(tp.getDeclaration());
            //hack to trim parens off keys 
            //for type params of aliases
            if (cname.endsWith("()")) {
                cname=cname.substring(0, cname.length()-2);
            }
        } else {
            cname = tp.getDeclaration().getName();
        }
        return tp.getName() + "$" + cname;
    }

    public String valueConstructorName(TypeDeclaration d) {
        final TypeDeclaration c = (TypeDeclaration)d.getContainer();
        return name(c) + constructorSeparator(d) 
            + name(c.getDirectMember(d.getName(), null, false));
    }

    public String constructorSeparator(Declaration c) {
        final Module mod = c.getUnit().getPackage().getModule();
        if (mod.getJsMajor() > 0 
                && (mod.getJsMajor() < 9 
                        || mod.getJsMajor() == 9 && mod.getJsMinor() < 1)) {
            return "_";
        }
        return "$c_";
    }

    public boolean isJsGlobal(Declaration d) {
        return d.isToplevel() 
            && globals.contains(d.getName()) 
            && d.getUnit().getPackage().getModule().getJsMajor()==0;
    }
}
