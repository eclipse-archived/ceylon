package com.redhat.ceylon.compiler.js;

import java.util.HashMap;
import java.util.Iterator;

import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.TypeParameter;
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
            final boolean isCallable = !forInvoke && decl instanceof Method
                    && gen.getTypeUtils().callable.equals(bme.getTypeModel().getDeclaration());
            String who = isCallable && decl.isMember() ? gen.getMember(bme, decl, null) : null;
            if (who != null && who.isEmpty()) {
                who=null;
            }
            final boolean hasTparms = bme.getTypeArguments() != null && !bme.getTypeArguments().getTypeModels().isEmpty();
            if (isCallable && (who != null || hasTparms)) {
                if (hasTparms) {
                    //Method refs with type arguments must be passed as a special function
                    final String tmpargs = gen.getNames().createTempVariable();
                    final String tmpi = gen.getNames().createTempVariable();
                    gen.out("function(){var ", tmpargs, "=[];for(var ", tmpi, "=0;",
                            tmpi, "<arguments.length;", tmpi, "++)", tmpargs, "[", tmpi, "]=arguments[", tmpi, "];",
                            tmpargs, ".push(");
                    //Create type arguments map
                    HashMap<TypeParameter, ProducedType> targs = new HashMap<>();
                    Iterator<ProducedType> iter = bme.getTypeArguments().getTypeModels().iterator();
                    for (TypeParameter tp : ((Method)decl).getTypeParameters()) {
                        ProducedType pt = iter.hasNext() ? iter.next() : tp.getDefaultTypeArgument();
                        targs.put(tp, pt);
                    }
                    TypeUtils.printTypeArguments(bme, targs, gen, true);
                    gen.out(");return ", exp, ".apply(", who==null?"null":who, ",", tmpargs, ");}");
                } else {
                    //Member methods must be passed as JsCallables
                    gen.out(GenerateJsVisitor.getClAlias(), "JsCallable(/*ESTEMERO*/", who, ",", exp, ")");
                }
            } else {
                gen.out(exp);
            }
        }
    }

}
