package com.redhat.ceylon.compiler.js;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.redhat.ceylon.compiler.js.GenerateJsVisitor.GenerateCallback;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Generic;
import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.Module;
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
            if (Module.LANGUAGE_MODULE_NAME.equals(pkgName)) {
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
            final boolean hasTparms = hasTypeParameters(bme);
            if (isCallable && (who != null || hasTparms)) {
                if (hasTparms) {
                    //Method refs with type arguments must be passed as a special function
                    printGenericMethodReference(gen, bme, who, exp);
                } else {
                    //Member methods must be passed as JsCallables
                    gen.out(GenerateJsVisitor.getClAlias(), "JsCallable(/*ESTEMERO*/", who, ",", exp, ")");
                }
            } else {
                gen.out(exp);
            }
        }
    }

    static boolean hasTypeParameters(final Tree.StaticMemberOrTypeExpression expr) {
        return expr.getTypeArguments() != null && !expr.getTypeArguments().getTypeModels().isEmpty();
    }

    /** Create a map with type arguments from the type parameter list in the expression's declaration and the
     *  type argument list in the expression itself. */
    static Map<TypeParameter, ProducedType> createTypeArguments(final Tree.StaticMemberOrTypeExpression expr) {
        List<TypeParameter> tparams = null;
        if (expr.getDeclaration() instanceof Generic) {
            tparams = ((Generic)expr.getDeclaration()).getTypeParameters();
        } else {
            expr.addUnexpectedError("Getting type parameters from unidentified declaration type "
                    + expr.getDeclaration());
            return null;
        }
        final HashMap<TypeParameter, ProducedType> targs = new HashMap<>();
        final Iterator<ProducedType> iter = expr.getTypeArguments().getTypeModels().iterator();
        for (TypeParameter tp : tparams) {
            ProducedType pt = iter.hasNext() ? iter.next() : tp.getDefaultTypeArgument();
            targs.put(tp, pt);
        }
        return targs;
    }

    static void printGenericMethodReference(final GenerateJsVisitor gen,
            final Tree.StaticMemberOrTypeExpression expr, final String who, final String member) {
        //Method refs with type arguments must be passed as a special function
        final String tmpargs = gen.getNames().createTempVariable();
        final String tmpi = gen.getNames().createTempVariable();
        gen.out("function(){var ", tmpargs, "=[];for(var ", tmpi, "=0;",
                tmpi, "<arguments.length;", tmpi, "++)", tmpargs, "[", tmpi, "]=arguments[", tmpi, "];",
                tmpargs, ".push(");
        TypeUtils.printTypeArguments(expr, createTypeArguments(expr), gen, true);
        gen.out(");return ", member, ".apply(", who==null?"null":who, ",", tmpargs, ");}");
    }

    /**
     * Generates a write access to a member, as represented by the given expression.
     * The given callback is responsible for generating the assigned value.
     * If lhs==null and the expression is a BaseMemberExpression
     * then the qualified path is prepended.
     */
    static void generateMemberAccess(Tree.StaticMemberOrTypeExpression expr,
                GenerateCallback callback, String lhs, final GenerateJsVisitor gen) {
        Declaration decl = expr.getDeclaration();
        boolean paren = false;
        String plainName = null;
        if (decl == null && gen.isInDynamicBlock()) {
            plainName = expr.getIdentifier().getText();
        } else if (GenerateJsVisitor.isNative(decl)) {
            // direct access to a native element
            plainName = decl.getName();
        }
        if (plainName != null) {
            if ((lhs != null) && (lhs.length() > 0)) {
                gen.out(lhs, ".");
            }
            gen.out(plainName, "=");
        }
        else {
            boolean protoCall = gen.opts.isOptimize() && (gen.getSuperMemberScope(expr) != null);
            if (gen.accessDirectly(decl) && !(protoCall && gen.defineAsProperty(decl))) {
                // direct access, without setter
                gen.out(gen.memberAccessBase(expr, decl, true, lhs), "=");
            }
            else {
                // access through setter
                gen.out(gen.memberAccessBase(expr, decl, true, lhs),
                        protoCall ? ".call(this," : "(");
                paren = true;
            }
        }
        
        callback.generateValue();
        if (paren) { gen.out(")"); }
    }

    static void generateMemberAccess(final Tree.StaticMemberOrTypeExpression expr,
            final String strValue, final String lhs, final GenerateJsVisitor gen) {
        generateMemberAccess(expr, new GenerateCallback() {
            @Override public void generateValue() { gen.out(strValue); }
        }, lhs, gen);
    }

}
