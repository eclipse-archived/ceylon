package com.redhat.ceylon.compiler.js;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.redhat.ceylon.common.Backend;
import com.redhat.ceylon.compiler.js.GenerateJsVisitor.GenerateCallback;
import com.redhat.ceylon.compiler.js.util.TypeUtils;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.TypeArguments;
import com.redhat.ceylon.model.typechecker.model.ClassOrInterface;
import com.redhat.ceylon.model.typechecker.model.Constructor;
import com.redhat.ceylon.model.typechecker.model.Declaration;
import com.redhat.ceylon.model.typechecker.model.Functional;
import com.redhat.ceylon.model.typechecker.model.Generic;
import com.redhat.ceylon.model.typechecker.model.Module;
import com.redhat.ceylon.model.typechecker.model.Type;
import com.redhat.ceylon.model.typechecker.model.TypeParameter;
import com.redhat.ceylon.model.typechecker.model.ModelUtil;
import com.redhat.ceylon.model.typechecker.model.TypedDeclaration;

public class BmeGenerator {

    static void generateBme(final Tree.BaseMemberExpression bme, final GenerateJsVisitor gen) {
        final boolean forInvoke = bme.getDirectlyInvoked();
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
            if (forInvoke && TypeUtils.isConstructor(decl)) {
                Constructor cd = TypeUtils.getConstructor(decl);
                if (!gen.qualify(bme, cd)) {
                    gen.out(gen.getNames().name((Declaration)cd.getContainer()), "_");
                }
                gen.out(gen.getNames().name(cd));
                return;
            }
        }
        String exp = gen.memberAccess(bme, null);
        if (decl == null && gen.isInDynamicBlock()) {
            if ("undefined".equals(exp)) {
                gen.out(exp);
            } else {
                gen.out("(typeof ", exp, "==='undefined'||", exp, "===null?");
                gen.generateThrow(null, "Undefined or null reference: " + exp, bme);
                gen.out(":", exp, ")");
            }
        } else {
            final boolean isCallable = !forInvoke && (decl instanceof Functional
                    || bme.getUnit().getCallableDeclaration().equals(bme.getTypeModel().getDeclaration()));
            String who = isCallable && decl.isMember() ? gen.getMember(bme, decl, null) : null;
            if (who == null || who.isEmpty()) {
                //We may not need to wrap this in certain cases
                ClassOrInterface cont = ModelUtil.getContainingClassOrInterface(bme.getScope());
                who = cont == null ? "0" : gen.getNames().self(cont);
            }
            final boolean hasTparms = hasTypeParameters(bme);
            if (isCallable && (who != null || hasTparms)) {
                if (hasTparms) {
                    //Function refs with type arguments must be passed as a special function
                    printGenericMethodReference(gen, bme, who, exp);
                } else {
                    //Member methods must be passed as JsCallables
                    gen.out(gen.getClAlias(), "JsCallable(", who, ",", exp, ")");
                }
            } else {
                gen.out(exp);
            }
        }
    }

    static boolean hasTypeParameters(final Tree.StaticMemberOrTypeExpression expr) {
        return expr.getTypeArguments() != null && expr.getTypeArguments().getTypeModels() != null
                && !expr.getTypeArguments().getTypeModels().isEmpty();
    }

    /** Create a map with type arguments from the type parameter list in the expression's declaration and the
     *  type argument list in the expression itself. */
    static Map<TypeParameter, Type> createTypeArguments(final Tree.StaticMemberOrTypeExpression expr) {
        List<TypeParameter> tparams = null;
        Declaration declaration = expr.getDeclaration();
        if (declaration instanceof Generic) {
            tparams = ((Generic)declaration).getTypeParameters();
        }
        else if (declaration instanceof TypedDeclaration &&
                ((TypedDeclaration)declaration).getType()!=null &&
                ((TypedDeclaration)declaration).getType().isTypeConstructor()) {
            tparams = ((TypedDeclaration)declaration).getType().getDeclaration().getTypeParameters();
        }
        else {
            expr.addUnexpectedError("Getting type parameters from unidentified declaration type "
                    + declaration, Backend.JavaScript);
            return null;
        }
        final HashMap<TypeParameter, Type> targs = new HashMap<>();
        TypeArguments typeArguments = expr.getTypeArguments();
        if (typeArguments!=null) {
            List<Type> typeModels = typeArguments.getTypeModels();
            if (typeModels!=null) {
                final Iterator<Type> iter = typeModels.iterator();
                for (TypeParameter tp : tparams) {
                    Type pt = iter.hasNext() ? iter.next() : tp.getDefaultTypeArgument();
                    targs.put(tp, pt);
                }
            }
        }
        return targs;
    }

    static void printGenericMethodReference(final GenerateJsVisitor gen,
            final Tree.StaticMemberOrTypeExpression expr, final String who, final String member) {
        //Function refs with type arguments must be passed as a special function
        gen.out(gen.getClAlias(), "JsCallable(", who, ",", member, ",");
        TypeUtils.printTypeArguments(expr, createTypeArguments(expr), gen, true,
                expr.getTypeModel().getVarianceOverrides());
        gen.out(")");
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
        } else if (TypeUtils.isNativeJs(decl)) {
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
            if (gen.accessDirectly(decl) && !(protoCall && AttributeGenerator.defineAsProperty(decl))) {
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

    static void generateQte(final Tree.QualifiedTypeExpression that, final GenerateJsVisitor gen) {
        if (that.getDirectlyInvoked() && that.getMemberOperator() instanceof Tree.SafeMemberOp==false) {
            if (gen.isInDynamicBlock() && that.getDeclaration() == null) {
                gen.out("new ");
            }
            Tree.Primary prim = that.getPrimary();
            if (prim instanceof Tree.BaseMemberExpression) {
                generateBme((Tree.BaseMemberExpression)prim, gen);
            } else if (prim instanceof Tree.QualifiedTypeExpression) {
                generateQte((Tree.QualifiedTypeExpression)prim, gen);
            } else {
                prim.visit(gen);
            }
            if (gen.isInDynamicBlock() && that.getDeclaration() == null) {
                gen.out(".", that.getIdentifier().getText());
            } else if (TypeUtils.isConstructor(that.getDeclaration())) {
                gen.out("_", gen.getNames().name(that.getDeclaration()));
            } else {
                gen.out(".", gen.getNames().name(that.getDeclaration()));
            }
        } else {
            FunctionHelper.generateCallable(that, gen.getNames().name(that.getDeclaration()), gen);
        }
    }

}
