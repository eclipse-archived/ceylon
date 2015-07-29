package com.redhat.ceylon.compiler.js;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.redhat.ceylon.compiler.js.util.TypeUtils;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.model.typechecker.model.Declaration;
import com.redhat.ceylon.model.typechecker.model.Type;
import com.redhat.ceylon.model.typechecker.model.Class;
import com.redhat.ceylon.model.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.model.typechecker.model.TypeParameter;
import com.redhat.ceylon.model.typechecker.model.Value;

public class SerializationHelper {

    /** Add serialize method to a class. Can be on the prototype or the instance, depending on the style being used. */
    static void addSerializer(final Node node, final com.redhat.ceylon.model.typechecker.model.Class d,
            final GenerateJsVisitor gen) {}

    /** Add deserialize method to a class. This one resides directly under the class constructor, since it creates
     * an uninitialized instance and adds state to it. */
    static void addDeserializer(final Node that, final com.redhat.ceylon.model.typechecker.model.Class d,
            final GenerateJsVisitor gen) {
        final String cmodel = gen.getNames().createTempVariable();
        final String ni = gen.getNames().self(d);
        final String typename = gen.getNames().name(d);
        //First of all, an instantiator from class model
        gen.out(typename, ".inst$$=function(", cmodel, ")");
        gen.beginBlock();
        if (d.isMember()) {
            gen.out("/*TODO getOuterInstance*/");
        }
        gen.out("var ", ni, "=new ", gen.getNames().name(d), ".$$;");
        gen.endLine();
        //Set type arguments, if any
        boolean first=true;
        for (TypeParameter tp : d.getTypeParameters()) {
            if (first) {
                gen.out(gen.getClAlias(), "set_type_args(", ni, ",{");
                first=false;
            } else {
                gen.out(",");
            }
            gen.out(tp.getName(), "$", d.getName(), ":", cmodel, ".$$targs$$.Type$Class.a.",
                    tp.getName(), "$", d.getName());
        }
        if (!first) {
            gen.out("});");
            gen.endLine();
        }
        setDeserializedTypeArguments(d, d.getExtendedType(), true, that, ni, gen, new HashSet<TypeDeclaration>());
        gen.out("return ", ni, ";");
        gen.endBlockNewLine();

        final List<Value> vals = serializableValues(d);
        //get
        gen.out(typename, ".ser$get$=function(ref,o){var n=ref.attribute.qualifiedName;");
        first=true;
        for (Value v : vals) {
            if (first) {
                first=false;
            } else {
                gen.out("else ");
            }
            gen.out("if(n==='", v.getQualifiedNameString(), "')return o.", name(d, v, gen), ";");
        }
        final Class supertype;
        if (d.getExtendedType() != null && d.getExtendedType().getDeclaration() != null
                && d.getExtendedType().getDeclaration() instanceof Class
                && ((Class)d.getExtendedType().getDeclaration()).isSerializable()) {
            supertype = (Class)d.getExtendedType().getDeclaration();
        } else {
            supertype = null;
        }
        if (!first) {
            gen.out("else ");
        }
        if (supertype != null) {
            gen.out("return ");
            gen.qualify(that, supertype);
            gen.out(gen.getNames().name(supertype), ".ser$get$(ref,o);");
        } else {
            gen.out("throw new TypeError('unknown attribute');");
        }
        gen.out("};");
        //set
        gen.out(typename, ".ser$set$=function(ref,o,i){var n=ref.attribute.qualifiedName;");
        first=true;
        for (Value v : vals) {
            if (first) {
                first=false;
            } else {
                gen.out("else ");
            }
            gen.out("if(n==='", v.getQualifiedNameString(), "')o.", name(d, v, gen), "=i;");
        }
        if (!first) {
            gen.out("else ");
        }
        if (supertype != null) {
            gen.qualify(that, supertype);
            gen.out(gen.getNames().name(supertype), ".ser$get$(ref,o,i);");
        } else {
            gen.out("throw new TypeError('unknown attribute');");
        }
        gen.out("};");
        //References
        if (supertype == null) {
            gen.out(typename, ".ser$refs$=function(o){return [");
        } else {
            gen.out(typename, ".ser$refs$=function(o){var a=",
                    gen.getNames().name(supertype), ".ser$refs$(o);a.push(");
        }
        first=true;
        final String pkgname = d.getUnit().getPackage().getNameAsString();
        for (Value v : vals) {
            if (first) {
                first=false;
            } else {
                gen.out(",");
            }
            gen.out(gen.getClAlias(), "MemberImpl$impl(", gen.getClAlias(), "OpenValue$jsint(",
                    gen.getClAlias(), "lmp$(ex$,'", "ceylon.language".equals(pkgname) ? "$" : pkgname,
                    "'),o.", gen.getNames().getter(v, true), "))");
        }
        if (supertype == null) {
            gen.out("];};");
        } else {
            gen.out(");return a;};");
        }
        gen.endLine();
    }

    /** Recursively add all the type arguments from extended and satisfied types. */
    private static void setDeserializedTypeArguments(final com.redhat.ceylon.model.typechecker.model.Class root,
            Type pt, boolean first, final Node that, final String ni, final GenerateJsVisitor gen,
            final Set<TypeDeclaration> decs) {
        if (pt == null) {
            return;
        }
        final boolean start=decs.isEmpty();
        final List<Type> sats = start ? root.getSatisfiedTypes() : pt.getSatisfiedTypes();
        decs.add(root);
        while (pt != null && !root.getUnit().getBasicDeclaration().equals(pt.getDeclaration())) {
            if (!decs.contains(pt.getDeclaration())) {
                for (Map.Entry<TypeParameter,Type> tp : pt.getTypeArguments().entrySet()) {
                    if (first) {
                        gen.out(gen.getClAlias(), "set_type_args(", ni, ",{");
                        first=false;
                    } else {
                        gen.out(",");
                    }
                    gen.out(tp.getKey().getName(), "$", pt.getDeclaration().getName(), ":");
                    TypeUtils.typeNameOrList(that, tp.getValue(), gen, false);
                }
                decs.add(pt.getDeclaration());
                setDeserializedTypeArguments(root, pt, first, that, ni, gen, decs);
            }
            pt = pt.getExtendedType();
        }
        for (Type sat : sats) {
            if (!decs.contains(sat.getDeclaration())) {
                for (Map.Entry<TypeParameter,Type> tp : sat.getTypeArguments().entrySet()) {
                    if (first) {
                        gen.out(gen.getClAlias(), "set_type_args(", ni, ",{");
                        first=false;
                    } else {
                        gen.out(",");
                    }
                    gen.out(tp.getKey().getName(), "$", sat.getDeclaration().getName(), ":");
                    TypeUtils.typeNameOrList(that, tp.getValue(), gen, false);
                }
                decs.add(sat.getDeclaration());
            }
            setDeserializedTypeArguments(root, sat, first, that, ni, gen, decs);
        }
        if (!first && start) {
            gen.out("});");
            gen.endLine();
        }
    }

    static List<Value> serializableValues(com.redhat.ceylon.model.typechecker.model.Class d) {
        ArrayList<Value> vals = new ArrayList<>();
        for (Declaration m : d.getMembers()) {
            if (!m.isFormal() && m instanceof Value && !m.isSetter() && (m.isCaptured() || m.isShared())) {
                Value v = (Value)m;
                if (!v.isTransient()) {
                    vals.add(v);
                }
            }
        }
        return vals;
    }

    static String name(Class d, Value v, GenerateJsVisitor gen) {
        if (v.isParameter()) {
            return gen.getNames().name(d.getParameter(v.getName())) + "_";
        }
        return v.isLate() || v.isCaptured() ? gen.getNames().name(v)+ "_" : gen.getNames().privateName(v);
    }

}
