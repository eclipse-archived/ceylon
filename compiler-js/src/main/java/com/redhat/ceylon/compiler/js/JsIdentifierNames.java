package com.redhat.ceylon.compiler.js;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.Module;
import com.redhat.ceylon.compiler.typechecker.model.Scope;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;

/**
 * Manages the identifier names in the JavaScript code generated for a Ceylon
 * compilation unit.
 * 
 * @author Ivo Kasiuk
 */
public class JsIdentifierNames {

    private boolean prototypeStyle = false;
    
    private static long uniqueID = 0;
    private static long nextUID() {
        if (++uniqueID <= 0) {
            uniqueID = 1;
        }
        return uniqueID;
    }
    
    private static Set<String> reservedWords = new HashSet<String>(); 
    
    static {
        //reservedWords.add("abstract");
        reservedWords.add("boolean");
        //reservedWords.add("break");
        reservedWords.add("byte");
        //reservedWords.add("case");
        //reservedWords.add("catch");
        reservedWords.add("char");
        //reservedWords.add("class");
        reservedWords.add("const");
        //reservedWords.add("continue");
        reservedWords.add("debugger");
        reservedWords.add("default");
        reservedWords.add("delete");
        reservedWords.add("do");
        reservedWords.add("double");
        //reservedWords.add("else");
        reservedWords.add("enum");
        reservedWords.add("export");
        reservedWords.add("extends");
        reservedWords.add("false");
        reservedWords.add("final");
        //reservedWords.add("finally");
        reservedWords.add("float");
        //reservedWords.add("for");
        //reservedWords.add("function");
        reservedWords.add("goto");
        //reservedWords.add("if");
        reservedWords.add("implements");
        //reservedWords.add("import");
        //reservedWords.add("in");
        reservedWords.add("instanceof");
        reservedWords.add("int");
        //reservedWords.add("interface");
        reservedWords.add("long");
        reservedWords.add("native");
        reservedWords.add("new");
        reservedWords.add("null");
        reservedWords.add("package");
        reservedWords.add("private");
        reservedWords.add("protected");
        reservedWords.add("public");
        //reservedWords.add("return");
        reservedWords.add("short");
        reservedWords.add("static");
        //reservedWords.add("super");
        //reservedWords.add("switch");
        reservedWords.add("synchronized");
        //reservedWords.add("this");
        //reservedWords.add("throw");
        reservedWords.add("throws");
        reservedWords.add("transient");
        reservedWords.add("true");
        //reservedWords.add("try");
        reservedWords.add("typeof");
        reservedWords.add("var");
        //reservedWords.add("void");
        reservedWords.add("volatile");
        //reservedWords.add("while");
        reservedWords.add("with");
    }
    
    public JsIdentifierNames(boolean prototypeStyle) {
        this.prototypeStyle = prototypeStyle;
    }
    
    /**
     * Determine the identifier name to be used in the generated JavaScript code
     * to represent the given declaration.
     */
    public String name(Declaration decl) {
        return getName(decl, false);
    }
    
    /**
     * Determine the function name to be used in the generated JavaScript code
     * for the getter of the given declaration.
     */
    public String getter(Declaration decl) {
        if (decl == null) { return ""; }
        String name = getName(decl, true);
        return String.format("get%c%s", Character.toUpperCase(name.charAt(0)),
                name.substring(1));
    }
    
    /**
     * Determine the function name to be used in the generated JavaScript code
     * for the setter of the given declaration.
     */
    public String setter(Declaration decl) {
        String name = getName(decl, true);
        return String.format("set%c%s", Character.toUpperCase(name.charAt(0)),
                name.substring(1));
    }
    
    /**
     * Determine the identifier to be used in the generated JavaScript code as
     * an alias for the given package.
     */
    public String moduleAlias(Module pkg) {
        StringBuilder sb = new StringBuilder("$$$");
        for (String s: pkg.getName()) {
            sb.append(s.substring(0,1));
        }
        sb.append(getUID(pkg));
        return sb.toString();
    }
    
    /**
     * Creates a new unique identifier.
     */
    public String createTempVariable(String baseName) {
        return String.format("%s$%d", baseName, nextUID());
    }
    
    /**
     * Creates a new unique identifier.
     */
    public String createTempVariable() {
        return createTempVariable("tmpvar");
    }
    
    /**
     * Determine identifier to be used for the self variable of the given type.
     */
    public String self(TypeDeclaration decl) {
        String name = decl.getName();
        if (!(decl.isShared() || decl.isToplevel())) {
            name = String.format("%s$%d", name, getUID(decl));
        } else {
            name += nestingSuffix(decl);
        }
        return String.format("$$%c%s", Character.toLowerCase(name.charAt(0)),
                    name.substring(1));
    }
    
    /**
     * Returns a disambiguation suffix for the given type. It is guaranteed that
     * the suffixes generated for two different types are different.
     */
    public String typeSuffix(TypeDeclaration typeDecl) {
        return String.format("$$%s$", typeDecl.getQualifiedNameString().replace('.', '$'));
    }
    
    private String nestingSuffix(Declaration decl) {
        String suffix = "";
        if (decl instanceof TypeDeclaration) {
            StringBuilder sb = new StringBuilder();
            Scope scope = decl.getContainer();
            while (scope instanceof TypeDeclaration) {
                sb.append('$');
                sb.append(((TypeDeclaration) scope).getName());
                scope = scope.getContainer();
            }
            suffix = sb.toString();
        }
        return suffix;
    }
    
    public void forceName(Declaration decl, String name) {
        uniqueVarNames.put(decl, name);
    }
    
    private Map<Module, Long> moduleUIDs = new HashMap<Module, Long>();
    private Map<Declaration, Long> uniqueVarIDs = new HashMap<Declaration, Long>();
    private Map<Declaration, String> uniqueVarNames =
            new HashMap<Declaration, String>();
    
    private String getName(Declaration decl, boolean forGetterSetter) {
        if (decl == null) { return null; }
        String name = decl.getName();
        if (!((decl.isShared() || decl.isToplevel())
                && (forGetterSetter || (decl instanceof Method)
                        || (decl instanceof ClassOrInterface)))) {
            name = uniqueVarNames.get(decl);
            if (name == null) {
                String format = (prototypeStyle && decl.isMember()) ? "%s$%d$" : "%s$%d";
                name = String.format(format, decl.getName(), getUID(decl));
            }
        } else {
            String suffix = nestingSuffix(decl);
            if (suffix.length() > 0) {
                name += suffix;
            } else if (!forGetterSetter && reservedWords.contains(name)) {
                name = '$' + name;
            }
        }
        return name;
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
    
}
