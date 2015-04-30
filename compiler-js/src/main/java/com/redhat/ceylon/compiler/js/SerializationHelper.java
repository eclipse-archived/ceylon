package com.redhat.ceylon.compiler.js;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.redhat.ceylon.compiler.js.util.TypeUtils;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.model.typechecker.model.Declaration;
import com.redhat.ceylon.model.typechecker.model.ProducedType;
import com.redhat.ceylon.model.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.model.typechecker.model.TypeParameter;
import com.redhat.ceylon.model.typechecker.model.Util;
import com.redhat.ceylon.model.typechecker.model.Value;

public class SerializationHelper {

    /** Add serialize method to a class. Can be on the prototype or the instance, depending on the style being used. */
    static void addSerializer(final Node node, final com.redhat.ceylon.model.typechecker.model.Class d,
            final GenerateJsVisitor gen) {
        final String dc = gen.getNames().createTempVariable();
        gen.out(gen.getNames().self(d), ".ser$$=function(", dc, ")");
        gen.beginBlock();
        gen.out("var ", gen.getNames().self(d), "=this;");
        //Call super.ser$$ if possible
        com.redhat.ceylon.model.typechecker.model.Class et = d.getExtendedTypeDeclaration();
        while (et != null && !(et.equals(d.getUnit().getObjectDeclaration()) || et.equals(d.getUnit().getBasicDeclaration()))) {
            if (et.isSerializable()) {
                gen.qualify(node, et);
                gen.out(gen.getNames().name(et), ".$$.prototype.ser$$.call(this,", dc, ");");
                et = null;
                gen.endLine();
            } else {
                et = et.getExtendedTypeDeclaration();
            }
        }
        gen.endLine();
        //Put the outer instance if it's a member
        if (d.isMember()) {
            gen.out(dc, ".putOuterInstance(this.outer$,{Instance$putOuterInstance:",
                    gen.getNames().name(Util.getContainingDeclaration(d)), "});");
            gen.endLine();
        }
        //Get the type's package
        String pkgname = d.getUnit().getPackage().getNameAsString();
        if ("ceylon.language".equals(pkgname)) {
            pkgname = "$";
        }
        //Serialize each value
        List<Value> vals = serializableValues(d);
        if (vals.size() > 1) {
            final String pkvar = gen.getNames().createTempVariable();
            gen.out("var ", pkvar, "=", gen.getClAlias(), "lmp$(ex$,'", pkgname, "');");
            gen.endLine();
            pkgname = pkvar;
        } else {
            pkgname = gen.getClAlias() + "lmp$(ex$,'" + pkgname + "')";
        }
        for (Value v : vals) {
            final TypeDeclaration vd = v.getType().getDeclaration();
            gen.out(dc, ".putValue(", gen.getClAlias(), "OpenValue$jsint(",
                    pkgname, ",this.", gen.getNames().getter(v, true),")", ",",
                    "this.", gen.getNames().name(v), ",{Instance$putValue:");
            if (vd instanceof TypeParameter && vd.getContainer() == d) {
                gen.out("this.$$targs$$.", vd.getName(), "$", d.getName());
            } else {
                TypeUtils.typeNameOrList(node, v.getType(), gen, false);
            }
            gen.out("});");
            gen.endLine();
        }
        gen.endBlockNewLine();
    }
    /** Add deserialize method to a class. This one resides directly under the class constructor, since it creates
     * an uninitialized instance and adds state to it. */
    static void addDeserializer(final Node that, final com.redhat.ceylon.model.typechecker.model.Class d,
            final GenerateJsVisitor gen) {
        final String dc = gen.getNames().createTempVariable();
        final String cmodel = gen.getNames().createTempVariable();
        final String ni = gen.getNames().self(d);
        final String typename = gen.getNames().name(d);
        //First of all, an instantiator from class model
        gen.out(typename, ".inst$$=function(", cmodel, ")");
        gen.beginBlock();
        if (d.isMember()) {
            gen.out("//TODO getOuterInstance");
            gen.endLine();
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

        //Now, the deserializer
        gen.out(typename, ".deser$$=function(", dc, ",", cmodel, ",", ni, ")");
        gen.beginBlock();
        //Call super.deser$$ if possible
        boolean create = true;
        com.redhat.ceylon.model.typechecker.model.Class et = d.getExtendedTypeDeclaration();
        while (create && !(et.equals(that.getUnit().getObjectDeclaration()) || et.equals(that.getUnit().getBasicDeclaration()))) {
            if (et.isSerializable()) {
                gen.qualify(that, et);
                gen.out(gen.getNames().name(et), ".deser$$(", dc, ",", cmodel, ",", ni, ");");
                gen.endLine();
                create = false;
            } else {
                et = et.getExtendedTypeDeclaration();
            }
        }
        //Get the current package and module
        String pkgname = d.getUnit().getPackage().getNameAsString();
        if ("ceylon.language".equals(pkgname)) {
            pkgname = "$";
        }
        List<Value> vals = serializableValues(d);
        if (vals.size() > 1) {
            final String pkvar = gen.getNames().createTempVariable();
            gen.out("var ", pkvar, "=", gen.getClAlias(), "lmp$(ex$,'", pkgname, "');");
            gen.endLine();
            pkgname = pkvar;
        } else {
            pkgname = gen.getClAlias() + "lmp$(ex$,'" + pkgname + "')";
        }
        //Deserialize each value
        final String atvar;
        if (vals.isEmpty()) {
            atvar = null;
        } else {
            atvar = gen.getNames().createTempVariable();
            gen.out("var ", atvar);
        }
        first=true;
        for (Value v : vals) {
            final TypeDeclaration vd = v.getType().getDeclaration();
            final String valname;
            if (v.isParameter()) {
                valname = gen.getNames().name(d.getParameter(v.getName())) + "_";
            } else {
                valname = v.isLate() || v.isCaptured() ? gen.getNames().name(v)+ "_" : gen.getNames().privateName(v);
            }
            if (first) {
                first = false;
            } else {
                gen.out(atvar);
            }
            gen.out("=", dc, ".getValue(", gen.getClAlias(),
                    "OpenValue$jsint(", pkgname, ",", ni, ".",
                    gen.getNames().getter(v, true),")", ",{Instance$getValue:");
            if (vd instanceof TypeParameter && vd.getContainer() == d) {
                gen.out(ni, ".$$targs$$.", vd.getName(), "$", d.getName());
            } else {
                TypeUtils.typeNameOrList(that, v.getType(), gen, false);
            }
            gen.out("});");
            gen.endLine();
            gen.out(ni, ".", valname, "=", gen.getClAlias(), "is$(", atvar, ",{t:",
                    gen.getClAlias(), "Reference$serialization})?",
                    atvar, ".leak():", atvar, ";");
            gen.endLine();
        }
        gen.endBlockNewLine();
    }

    /** Recursively add all the type arguments from extended and satisfied types. */
    private static void setDeserializedTypeArguments(final com.redhat.ceylon.model.typechecker.model.Class root,
            ProducedType pt, boolean first, final Node that, final String ni, final GenerateJsVisitor gen,
            final Set<TypeDeclaration> decs) {
        if (pt == null) {
            return;
        }
        final boolean start=decs.isEmpty();
        final List<ProducedType> sats = start ? root.getSatisfiedTypes() : pt.getSatisfiedTypes();
        decs.add(root);
        while (pt != null && !root.getUnit().getBasicDeclaration().equals(pt.getDeclaration())) {
            if (!decs.contains(pt.getDeclaration())) {
                for (Map.Entry<TypeParameter,ProducedType> tp : pt.getTypeArguments().entrySet()) {
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
        for (ProducedType sat : sats) {
            if (!decs.contains(sat.getDeclaration())) {
                for (Map.Entry<TypeParameter,ProducedType> tp : sat.getTypeArguments().entrySet()) {
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
            if (!m.isFormal() && m instanceof Value && !m.isSetter() && m.isCaptured()) {
                Value v = (Value)m;
                if (!v.isTransient()) {
                    vals.add(v);
                }
            }
        }
        return vals;
    }

}
