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
    public String createTempVariable() {
        return String.format("tmpvar$%d", nextUID());
    }
    
    /**
     * Returns a disambiguation suffix for the given type. It is guaranteed that
     * the suffixes generated for two different types are different.
     */
    public String memberSuffix(TypeDeclaration typeDecl, boolean method) {
        // TODO: we need to take the qualified path into account!
        return String.format(method ? "$%s$" : "$%s", typeDecl.getName());
    }
    
    public void forceName(Declaration decl, String name) {
        uniqueVarNames.put(decl, name);
    }
    
    private Map<Declaration, String> uniqueVarNames =
            new HashMap<Declaration, String>();
    
    private String getName(Declaration d, boolean forGetterSetter) {
        String name = d.getName();
        Scope container = d.getContainer();
        if (!((d.isShared() || d.isToplevel())
                && (forGetterSetter || (d instanceof Method)
                        || (d instanceof ClassOrInterface)))) {
        //if (!(forGetterSetter || (d instanceof Parameter) || (d instanceof Method))) {
            name = uniqueVarNames.get(d);
            if (name == null) {
                name = String.format("%s$%d", d.getName(), nextUID());
                uniqueVarNames.put(d, name);
            }
        } else if (prototypeStyle && !d.isShared() && d.isMember()
                    && (container instanceof ClassOrInterface)) {
            name += memberSuffix((ClassOrInterface) container,
                    forGetterSetter || (d instanceof Method));
        }
        return name;
    }
    
    
}
