package com.redhat.ceylon.compiler.js;

import java.util.HashMap;
import java.util.Map;

import com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.Package;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;

/**
 * Manages the identifier names in the JavaScript code generated for a Ceylon
 * compilation unit.
 * 
 * @author Ivo Kasiuk
 */
public class JsIdentifierNames {

    //private boolean prototypeStyle = false;
    
    private static long uniqueID = 0;
    private static long nextUID() {
        if (++uniqueID <= 0) {
            uniqueID = 1;
        }
        return uniqueID;
    }
    
    public JsIdentifierNames(boolean prototypeStyle) {
        //this.prototypeStyle = prototypeStyle;
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
        //TODO: improve this! Currently it can give the same result for different packages.
        StringBuilder sb = new StringBuilder("$$$");
        for (String s: pkg.getName()) {
            sb.append(s.substring(0,1));
        }
        sb.append(pkg.getQualifiedNameString().length());
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
    public String self(TypeDeclaration d) {
        String name = d.getName();
        if (!(d.isShared() || d.isToplevel())) {
            name = String.format("%s$%d", name, getUID(d));
        }
        // TODO: shared or toplevel types probably also need a suffix in some cases
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
    
    public void forceName(Declaration decl, String name) {
        uniqueVarNames.put(decl, name);
    }
    
    private Map<Declaration, Long> uniqueVarIDs = new HashMap<Declaration, Long>();
    private Map<Declaration, String> uniqueVarNames =
            new HashMap<Declaration, String>();
    
    private String getName(Declaration d, boolean forGetterSetter) {
        String name = d.getName();
        if (!((d.isShared() || d.isToplevel())
                && (forGetterSetter || (d instanceof Method)
                        || (d instanceof ClassOrInterface)))) {
            name = uniqueVarNames.get(d);
            if (name == null) {
                name = String.format("%s$%d", d.getName(), getUID(d));
            }
        }
        // TODO: shared or toplevel functions/types probably also need a suffix in some cases
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
    
}
