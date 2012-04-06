package com.redhat.ceylon.compiler.js;

import java.util.HashMap;
import java.util.Map;

import com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.Package;
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
    public String packageAlias(Package pkg) {
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
    public String memberSuffix(TypeDeclaration typeDecl) {
        // TODO: we need to take the qualified path into account!
        return String.format("$%s$", typeDecl.getName());
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
    
    private Map<Package, Long> packageUIDs = new HashMap<Package, Long>();
    private Map<Declaration, Long> uniqueVarIDs = new HashMap<Declaration, Long>();
    private Map<Declaration, String> uniqueVarNames =
            new HashMap<Declaration, String>();
    
    private String getName(Declaration decl, boolean forGetterSetter) {
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
            name += nestingSuffix(decl);
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
    
    private long getUID(Package pkg) {
        Long id = packageUIDs.get(pkg);
        if (id == null) {
            id = nextUID();
            packageUIDs.put(pkg, id);
        }
        return id;
    }
    
}
