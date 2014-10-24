package com.redhat.ceylon.compiler.js;

import java.util.ArrayList;
import java.util.List;

import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.TypeParameter;
import com.redhat.ceylon.compiler.typechecker.model.Util;
import com.redhat.ceylon.compiler.typechecker.model.Value;
import com.redhat.ceylon.compiler.typechecker.tree.Node;

public class SerializationHelper {

    /** Add serialize method to a class. Can be on the prototype or the instance, depending on the style being used. */
    static void addSerializer(final Node node, final com.redhat.ceylon.compiler.typechecker.model.Class d,
            final GenerateJsVisitor gen) {
        final String dc = gen.getNames().createTempVariable();
        gen.out(gen.getNames().self(d), ".ser$$=function(", dc, ")");
        gen.beginBlock();
        //Call super.ser$$ if possible
        com.redhat.ceylon.compiler.typechecker.model.Class et = d.getExtendedTypeDeclaration();
        while (et != null && !(et.equals(d.getUnit().getObjectDeclaration()) || et.equals(d.getUnit().getBasicDeclaration()))) {
            if (et.isSerializable()) {
                if (gen.qualify(node, et)) {
                    gen.out(".");
                }
                gen.out(gen.getNames().name(et), ".$$.prototype.ser$$(", dc, ");");
                et = null;
                gen.endLine();
            }
        }
        if (d.isMember()) {
            gen.out(dc, ".putOuterInstance(this.outer$,{Instance$putOuterInstance:",
                    gen.getNames().name(Util.getContainingDeclaration(d)), "});");
            gen.endLine();
        }
        for (TypeParameter tp : d.getTypeParameters()) {
            gen.out(dc,  ".putTypeArgument(", gen.getClAlias(), "OpenTypeParam$jsint(",
                    gen.getNames().name(d), ",'", tp.getName(), "$", d.getName(), "'),", gen.getClAlias(),
                    "typeLiteral$meta({Type$typeLiteral:");
            gen.out("this.$$targs$$.", tp.getName(), "$", d.getName(), "}));");
            gen.endLine();
        }
        String pkgname = d.getUnit().getPackage().getNameAsString();
        if ("ceylon.language".equals(pkgname)) {
            pkgname = "$";
        }
        List<Value> vals = serializableValues(d);
        if (vals.size() > 1) {
            final String pkvar = gen.getNames().createTempVariable();
            gen.out("var ", pkvar, "=lmp$(ex$,'", pkgname, "');");
            gen.endLine();
            pkgname = pkvar;
        } else {
            pkgname = gen.getClAlias() + "lmp$(ex$,'" + pkgname + "')";
        }
        for (Value v : vals) {
            final TypeDeclaration vd = v.getType().getDeclaration();
            gen.out(dc, ".putValue(", gen.getClAlias(), "OpenValue$jsint(",
                    pkgname, ",this.$prop$", gen.getNames().getter(v),")", ",",
                    "this.", gen.getNames().name(v), ",{Instance$putValue:");
            if (vd instanceof TypeParameter && vd.getContainer() == d) {
                gen.out("this.$$targs$$.", vd.getName(), "$", d.getName());
            } else {
                TypeUtils.metamodelTypeNameOrList(d.getUnit().getPackage(), v.getType(), gen);
            }
            gen.out("});");
            gen.endLine();
        }
        //putElement<Instance>(Integer,Instance)
        gen.endBlockNewLine();
    }
    /** Add deserialize method to a class. This one resides directly under the class constructor, since it creates
     * an uninitialized instance and adds state to it. */
    static void addDeserializer(final Node that, final com.redhat.ceylon.compiler.typechecker.model.Class d,
            final GenerateJsVisitor gen) {
        final String dc = gen.getNames().createTempVariable();
        final String ni = gen.getNames().createTempVariable();
        final String typename = gen.getNames().name(d);
        gen.out(typename, ".deser$$=function(", dc, ",", ni, ")");
        gen.beginBlock();
        //Call super.deser$$ if possible
        boolean create = true;
        com.redhat.ceylon.compiler.typechecker.model.Class et = d.getExtendedTypeDeclaration();
        while (create && !(et.equals(that.getUnit().getObjectDeclaration()) || et.equals(that.getUnit().getBasicDeclaration()))) {
            if (et.isSerializable()) {
                gen.out(ni, "=");
                if (gen.qualify(that, et)) {
                    gen.out(".");
                }
                gen.out(gen.getNames().name(et), ".$$.prototype.ser$$(", dc, ");");
                gen.endLine();
                create = false;
            }
        }
        //Otherwise create a new instance
        if (d.isMember()) {
            gen.out("//TODO getOuterInstance");
            gen.endLine();
        }
        if (create) {
            gen.out(ni, "=new ");
            if (d.isMember()) {
                gen.out("/*outerInstance.*/");
            }
            gen.out(typename, ".$$;");
            gen.endLine();
            if (!d.getTypeParameters().isEmpty()) {
                gen.out(ni, ".$$targs$$={};");
                gen.endLine();
            }
        }
        for (TypeParameter tp : d.getTypeParameters()) {
            gen.out(ni, ".$$targs$$.", tp.getName(), "$", d.getName(), "={t:", dc, ".getTypeArgument(",
                    gen.getClAlias(), "OpenTypeParam$jsint(", typename, ",'",
                    tp.getName(), "$", d.getName(), "')).tipo};");
            //TODO type arguments to the type argument itself
            gen.endLine();
        }
        String pkgname = d.getUnit().getPackage().getNameAsString();
        if ("ceylon.language".equals(pkgname)) {
            pkgname = "$";
        }
        List<Value> vals = serializableValues(d);
        if (vals.size() > 1) {
            final String pkvar = gen.getNames().createTempVariable();
            gen.out("var ", pkvar, "=lmp$(ex$,'", pkgname, "');");
            gen.endLine();
            pkgname = pkvar;
        } else {
            pkgname = gen.getClAlias() + "lmp$(ex$,'" + pkgname + "')";
        }
        for (Value v : vals) {
            final TypeDeclaration vd = v.getType().getDeclaration();
            gen.out(ni, ".", gen.getNames().name(v), "_=", dc, ".getValue(", gen.getClAlias(),
                    "OpenValue$jsint(", pkgname, ",", gen.getNames().name(d),".$$.prototype.$prop$",
                    gen.getNames().getter(v),")", ",{Instance$getValue:");
            if (vd instanceof TypeParameter && vd.getContainer() == d) {
                gen.out(ni, ".$$targs$$.", vd.getName(), "$", d.getName());
            } else {
                TypeUtils.metamodelTypeNameOrList(d.getUnit().getPackage(), v.getType(), gen);
            }
            gen.out("});");
            gen.endLine();
        }
        gen.out("return ", ni, ";");
        gen.endBlockNewLine();
    }

    static List<Value> serializableValues(com.redhat.ceylon.compiler.typechecker.model.Class d) {
        ArrayList<Value> vals = new ArrayList<>();
        for (Declaration m : d.getMembers()) {
            if (!m.isFormal() && m instanceof Value && !m.isSetter()) {
                Value v = (Value)m;
                if (!v.isTransient()) {
                    vals.add(v);
                }
            }
        }
        return vals;
    }

}
