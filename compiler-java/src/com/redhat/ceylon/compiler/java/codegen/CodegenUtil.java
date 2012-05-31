package com.redhat.ceylon.compiler.java.codegen;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;

import com.redhat.ceylon.compiler.typechecker.model.Class;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Interface;
import com.redhat.ceylon.compiler.typechecker.model.Package;
import com.redhat.ceylon.compiler.typechecker.model.Scope;

class CodegenUtil {

    public static enum NameFlag {
        /** 
         * A qualified name. 
         * <li>For a top level this includes the package name.
         * <li>For an inner this includes the package name and the qualifying type names
         * <li>For a (possibly indirect) local this includes the qualifying type names 
         */
        QUALIFIED,
        /** The name of the companion type of this thing */
        COMPANION
    }
    
    private CodegenUtil(){}
    
    /**
     * Generates a Java type name for the given declaration
     * @param gen Something which knows about local declarations
     * @param decl The declaration
     * @param options Option flags
     */
    static String declName(LocalId gen, final Declaration decl, NameFlag... options) {
        EnumSet<NameFlag> flags = EnumSet.noneOf(NameFlag.class);
        flags.addAll(Arrays.asList(options));
        java.util.List<Scope> l = new java.util.ArrayList<Scope>();
        Scope s = (Scope)decl;
        do {
            l.add(s);
            s = s.getContainer();
        } while (!(s instanceof Package));
        Collections.reverse(l);
        StringBuilder sb = new StringBuilder();
        if (flags.contains(NameFlag.QUALIFIED)) {
            Package pkg = (Package)s;
            final String pname = pkg.getQualifiedNameString();
            sb.append('.').append(pname);
            if (!pname.isEmpty()) {
                sb.append('.');
            }
        }
        for (int ii = 0; ii < l.size(); ii++) {
            Scope scope = l.get(ii);
            final boolean last = ii == l.size() - 1;
            if (scope instanceof Class) {
                Class klass = (Class)scope;
                sb.append(klass.getName());
                if (flags.contains(NameFlag.COMPANION)
                        && last) {
                    sb.append("$impl");
                }
            } else if (scope instanceof Interface) {
                Interface iface = (Interface)scope;
                sb.append(iface.getName());
                if ((decl instanceof Class) || 
                        flags.contains(NameFlag.COMPANION)) {
                    sb.append("$impl");
                }
            } else if (Decl.isLocalScope(scope)) {
                if (flags.contains(NameFlag.COMPANION)
                    || !(decl instanceof Interface)) {
                    sb.setLength(0);
                } else
                if (flags.contains(NameFlag.QUALIFIED)
                        || (decl instanceof Interface)) {
                    Scope nonLocal = scope;
                    while (!(nonLocal instanceof Declaration)) {
                        nonLocal = nonLocal.getContainer();
                    }
                    if (decl instanceof Interface) {
                        sb.append(((Declaration)nonLocal).getName()).append('$').append(gen.getLocalId(scope)).append('$');
                    } else {
                        sb.append(((Declaration)nonLocal).getName()).append("$").append(gen.getLocalId(scope)).append('.');
                    }
                }
                continue;
            }
            if (!last) {
                if (decl instanceof Interface) {
                    sb.append(flags.contains(NameFlag.COMPANION) ? '.' : '$');
                } else {
                    sb.append('.');
                }
            }
        }
        return sb.toString();
    }
    
}
