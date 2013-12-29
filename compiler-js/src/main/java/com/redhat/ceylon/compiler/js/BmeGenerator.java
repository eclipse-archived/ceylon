package com.redhat.ceylon.compiler.js;

import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;

public class BmeGenerator {

    static void generateBme(final Tree.BaseMemberExpression bme, final GenerateJsVisitor gen, final boolean forInvoke) {
        Declaration decl = bme.getDeclaration();
        if (decl != null) {
            String name = decl.getName();
            String pkgName = decl.getUnit().getPackage().getQualifiedNameString();

            // map Ceylon true/false/null directly to JS true/false/null
            if ("ceylon.language".equals(pkgName)) {
                if ("true".equals(name) || "false".equals(name) || "null".equals(name)) {
                    gen.out(name);
                    return;
                }
            }
        }
        String exp = gen.memberAccess(bme, null);
        if (decl == null && gen.isInDynamicBlock()) {
            gen.out("(typeof ", exp, "==='undefined'||", exp, "===null?");
            gen.generateThrow("Undefined or null reference: " + exp, bme);
            gen.out(":", exp, ")");
        } else {
            final boolean isCallable = !forInvoke && decl instanceof Method && decl.isMember()
                    && gen.getTypeUtils().callable.equals(bme.getTypeModel().getDeclaration());
            String who = null;
            if (isCallable) {
                who = gen.getMember(bme, decl, null);
                if (who != null && who.length() > 0) {
                    gen.out(GenerateJsVisitor.getClAlias(), "JsCallable(/*ESTEMERO*/", who, ",");
                } else {
                    who = null;
                }
            }
            gen.out(exp);
            if (who != null) {
                gen.out(")");
            }
        }
    }

}
