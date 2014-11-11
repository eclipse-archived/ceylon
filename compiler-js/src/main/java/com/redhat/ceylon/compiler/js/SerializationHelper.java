package com.redhat.ceylon.compiler.js;

import java.util.ArrayList;
import java.util.List;

import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Parameter;
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
        final String ac = gen.getNames().createTempVariable();
        gen.out(gen.getNames().self(d), ".ser$$=function(", dc, ")");
        gen.beginBlock();
        gen.out("var ", gen.getNames().self(d), "=this;");
        //Call super.ser$$ if possible
        com.redhat.ceylon.compiler.typechecker.model.Class et = d.getExtendedTypeDeclaration();
        while (et != null && !(et.equals(d.getUnit().getObjectDeclaration()) || et.equals(d.getUnit().getBasicDeclaration()))) {
            if (et.isSerializable()) {
                gen.qualify(node, et);
                gen.out(gen.getNames().name(et), ".$$.prototype.ser$$.call(this,", dc, ");");
                et = null;
                gen.endLine();
            }
        }
        //Get the deconstructor for this class
        gen.out("var ", ac, "=", dc, "(");
        final String classOrMemberClass = d.isMember() ? "MemberClass" : "Class";
        if (JsCompiler.isCompilingLanguageModule()) {
            gen.out("Applied", classOrMemberClass);
        } else {
            gen.out(gen.getClAlias(), "$init$Applied", classOrMemberClass, "$meta$model()");
        }
        gen.out("(", gen.getNames().name(d), ",{Type$Applied", classOrMemberClass, ":{t:",
                gen.getNames().name(d));
        if (!d.getTypeParameters().isEmpty()) {
            gen.out(",a:{");
            boolean first=true;
            for (TypeParameter tp : d.getTypeParameters()) {
                if (first)first=false;else gen.out(",");
                gen.out(tp.getName(), "$", d.getName(), ":this.$$targs$$.", tp.getName(), "$", d.getName());
            }
            gen.out("}");
        }
        gen.out("},Arguments$Applied", classOrMemberClass, ":{t:");
        if (d.getParameterList().getParameters().isEmpty()) {
            gen.out(gen.getClAlias(), "Empty");
        } else {
            gen.out("'T',l:[");
            boolean first=true;
            for (Parameter param : d.getParameterList().getParameters()) {
                if (first)first=false;else gen.out(",");
                TypeUtils.typeNameOrList(node, param.getType(), gen, false);
            }
            gen.out("]");
        }
        gen.out("}");
        if (d.isMember()) {
            TypeDeclaration parentd = Util.getContainingClassOrInterface(d);
            gen.out(",Container$MemberClass:{t:", gen.getNames().name(parentd));
            if (!parentd.getTypeParameters().isEmpty()) {
                gen.out(",a:this.outer$.$$targs$$");
            }
            gen.out("}");
        }
        gen.out("}));");
        gen.endLine();
        //Put the outer instance if it's a member
        if (d.isMember()) {
            gen.out(ac, ".putOuterInstance(this.outer$,{Instance$putOuterInstance:",
                    gen.getNames().name(Util.getContainingDeclaration(d)), "});");
            gen.endLine();
        }
        //Now the type arguments
        for (TypeParameter tp : d.getTypeParameters()) {
            gen.out(ac,  ".putTypeArgument(", gen.getClAlias(), "OpenTypeParam$jsint(",
                    gen.getNames().name(d), ",'", tp.getName(), "$", d.getName(), "'),", gen.getClAlias(),
                    "typeLiteral$meta({Type$typeLiteral:");
            gen.out("this.$$targs$$.", tp.getName(), "$", d.getName(), "}));");
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
            gen.out(ac, ".putValue(", gen.getClAlias(), "OpenValue$jsint(",
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
        if (d.isMember()) {
            gen.out("//TODO getOuterInstance");
            gen.endLine();
        }
        //Call super.deser$$ if possible
        boolean create = true;
        com.redhat.ceylon.compiler.typechecker.model.Class et = d.getExtendedTypeDeclaration();
        while (create && !(et.equals(that.getUnit().getObjectDeclaration()) || et.equals(that.getUnit().getBasicDeclaration()))) {
            if (et.isSerializable()) {
                gen.out(ni, "=");
                gen.qualify(that, et);
                gen.out(gen.getNames().name(et), ".$$.prototype.deser$$.call(this,", dc, ");");
                gen.endLine();
                create = false;
            }
        }
        //Otherwise create a new instance
        if (create) {
            gen.out(ni, "=new ");
            if (d.isMember()) {
                gen.out("/*outerInstance.*/");
            }
            gen.out(typename, ".$$;");
            gen.endLine();
            if (!d.getTypeParameters().isEmpty()) {
                gen.out(ni, ".$$targs$$={");
            }
        }
        boolean first=true;
        for (TypeParameter tp : d.getTypeParameters()) {
            if (!create) {
                gen.out(ni, ".$$targs$$.");
            } else if (first) {
                first=false;
            } else {
                gen.out(",");
            }
            gen.out(tp.getName(), "$", d.getName(), create?":":"=", gen.getClAlias(), "ser$et$(", dc, ".getTypeArgument(",
                    gen.getClAlias(), "OpenTypeParam$jsint(", typename, ",'",
                    tp.getName(), "$", d.getName(), "')))");
            if (!create) {
                gen.endLine(true);
            }
        }
        if (create && !first) {
            gen.out("};");
            gen.endLine();
        }
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
