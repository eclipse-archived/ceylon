package com.redhat.ceylon.compiler.js;

import java.util.ArrayList;
import java.util.Map;

import com.redhat.ceylon.compiler.typechecker.model.Class;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.Module;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.Scope;
import com.redhat.ceylon.compiler.typechecker.model.TypeAlias;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.TypeParameter;
import com.redhat.ceylon.compiler.typechecker.model.Util;
import com.redhat.ceylon.compiler.typechecker.model.Value;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ValueLiteral;

public class MetamodelHelper {

    /** Generate the call to the corresponding open type for the specified literal. */
    static void generateOpenType(final Tree.MetaLiteral that, final GenerateJsVisitor gen) {
        final Declaration d = that.getDeclaration();
        final Module m = d.getUnit().getPackage().getModule();
        gen.out(GenerateJsVisitor.getClAlias(), "$init$Open");
        if (d instanceof com.redhat.ceylon.compiler.typechecker.model.Interface) {
            gen.out("Interface");
        } else if (d instanceof com.redhat.ceylon.compiler.typechecker.model.Class) {
            gen.out("Class");
        } else if (d instanceof Method) {
            gen.out("Function");
        } else if (d instanceof Value) {
            gen.out("Value");
        } else if (d instanceof com.redhat.ceylon.compiler.typechecker.model.IntersectionType) {
            gen.out("Intersection");
        } else if (d instanceof com.redhat.ceylon.compiler.typechecker.model.UnionType) {
            gen.out("Union");
        } else if (d instanceof TypeParameter) {
            gen.out("TypeParam");
        } else if (d instanceof com.redhat.ceylon.compiler.typechecker.model.NothingType) {
            gen.out("NothingType");
        } else if (d instanceof TypeAlias) {
            gen.out("Alias()(");
            if (d.isMember()) {
                //Make the chain to the top-level container
                ArrayList<Declaration> parents = new ArrayList<Declaration>(2);
                Declaration pd = (Declaration)d.getContainer();
                while (pd!=null) {
                    parents.add(0,pd);
                    pd = pd.isMember()?(Declaration)pd.getContainer():null;
                }
                for (Declaration _d : parents) {
                    gen.out(gen.getNames().name(_d), ".$$.prototype.");
                }
            }
            gen.out(gen.getNames().name(d), ")");
            return;
        }
        gen.out("()(", GenerateJsVisitor.getClAlias(), "getModules$meta().find('", m.getNameAsString(),
                "','", m.getVersion(), "').findPackage('", d.getUnit().getPackage().getNameAsString(),
                "'),");
        if (d.isMember()) {
            final Declaration parent = Util.getContainingDeclaration(d);
            if (!gen.opts.isOptimize() && parent instanceof TypeDeclaration && Util.contains((Scope)parent, that.getScope())) {
                gen.out(gen.getNames().self((TypeDeclaration)parent), ".");
            } else {
                Declaration _md = d;
                final ArrayList<Declaration> parents = new ArrayList<>(3);
                while (_md.isMember()) {
                    _md=Util.getContainingDeclaration(_md);
                    parents.add(0, _md);
                }
                boolean first=true;
                boolean imported=false;
                for (Declaration _d : parents) {
                    if (first){
                        imported = gen.qualify(that, _d);
                        first=false;
                    }
                    if (_d.isAnonymous()) {
                        final boolean wasShared=_d.isShared();
                        _d.setShared(true);
                        ((com.redhat.ceylon.compiler.typechecker.model.Class)_d).setAnonymous(false);
                        gen.out(gen.getNames().getter(_d), "().");
                        ((com.redhat.ceylon.compiler.typechecker.model.Class)_d).setAnonymous(true);
                        _d.setShared(wasShared);
                    } else {
                        if (!imported)gen.out("$init$");
                        gen.out(gen.getNames().name(_d), imported?".$$.prototype.":"().$$.prototype.");
                    }
                    imported=true;
                }
            }
        }
        if (d instanceof Value) {
            if (!d.isMember()) gen.qualify(that, d);
            gen.out("$prop$", gen.getNames().getter(d), ")");
        } else {
            if (d.isAnonymous()) {
                final boolean wasShared=d.isShared();
                d.setShared(true);
                ((com.redhat.ceylon.compiler.typechecker.model.Class)d).setAnonymous(false);
                gen.out(GenerateJsVisitor.getClAlias(), "getrtmm$$(");
                if (!d.isMember()) gen.qualify(that, d);
                ((com.redhat.ceylon.compiler.typechecker.model.Class)d).setAnonymous(true);
                d.setShared(wasShared);
                gen.out(gen.getNames().getter(d), ").$t.t");
            } else {
                if (!d.isMember()) gen.qualify(that, d);
                gen.out(gen.getNames().name(d));
            }
            gen.out(")");
        }
    }

    static void generateClosedTypeLiteral(final Tree.TypeLiteral that, final GenerateJsVisitor gen) {
        final ProducedType ltype = that.getType().getTypeModel();
        final TypeDeclaration td = ltype.getDeclaration();
        if (td instanceof com.redhat.ceylon.compiler.typechecker.model.Class) {
            if (Util.getContainingClassOrInterface(td.getContainer()) == null) {
                gen.out(GenerateJsVisitor.getClAlias(), "$init$AppliedClass$meta$model()(");
            } else {
                gen.out(GenerateJsVisitor.getClAlias(), "$init$AppliedMemberClass$meta$model()(");
            }
            TypeUtils.outputQualifiedTypename(gen.isImported(gen.getCurrentPackage(), td), ltype, gen, false);
            gen.out(",");
            TypeUtils.printTypeArguments(that, that.getTypeModel().getTypeArguments(), gen, false);
            final Map<TypeParameter,ProducedType> targs = that.getType().getTypeModel().getTypeArguments();
            if (targs != null && !targs.isEmpty()) {
                gen.out(",undefined,");
                TypeUtils.printTypeArguments(that, targs, gen, false);
            }
            gen.out(")");
        } else if (td instanceof com.redhat.ceylon.compiler.typechecker.model.Interface) {
            if (td.isToplevel()) {
                gen.out(GenerateJsVisitor.getClAlias(), "$init$AppliedInterface$meta$model()(");
            } else {
                gen.out(GenerateJsVisitor.getClAlias(), "$init$AppliedMemberInterface$meta$model()(");
            }
            TypeUtils.outputQualifiedTypename(gen.isImported(gen.getCurrentPackage(), td), ltype, gen, false);
            gen.out(",");
            TypeUtils.printTypeArguments(that, that.getTypeModel().getTypeArguments(), gen, false);
            final Map<TypeParameter,ProducedType> targs = that.getType().getTypeModel().getTypeArguments();
            if (targs != null && !targs.isEmpty()) {
                gen.out(",undefined,");
                TypeUtils.printTypeArguments(that, targs, gen, false);
            }
            gen.out(")");
        } else if (td instanceof com.redhat.ceylon.compiler.typechecker.model.NothingType) {
            gen.out(GenerateJsVisitor.getClAlias(),"getNothingType$meta$model()");
        } else if (that instanceof Tree.AliasLiteral) {
            gen.out("/*TODO: applied alias*/");
        } else if (that instanceof Tree.TypeParameterLiteral) {
            gen.out("/*TODO: applied type parameter*/");
        } else {
            gen.out(GenerateJsVisitor.getClAlias(), "/*TODO: closed type literal", that.getClass().getName(),"*/typeLiteral$meta({Type$typeLiteral:");
            TypeUtils.typeNameOrList(that, ltype, gen, false);
            gen.out("})");
        }
    }

    static void generateMemberLiteral(final Tree.MemberLiteral that, final GenerateJsVisitor gen) {
        final com.redhat.ceylon.compiler.typechecker.model.ProducedReference ref = that.getTarget();
        final ProducedType ltype = that.getType() == null ? null : that.getType().getTypeModel();
        final Declaration d = ref.getDeclaration();
        final Class anonClass = d.isMember()&&d.getContainer() instanceof Class && ((Class)d.getContainer()).isAnonymous()?(Class)d.getContainer():null;
        if (that instanceof Tree.FunctionLiteral || d instanceof Method) {
            gen.out(GenerateJsVisitor.getClAlias(), d.isMember()&&anonClass==null?"AppliedMethod$meta$model(":"AppliedFunction$meta$model(");
            if (ltype == null) {
                if (anonClass != null) {
                    gen.qualify(that, anonClass);
                    final String ancname = gen.getNames().name(anonClass);
                    final int dolpos = ancname.lastIndexOf('$');
                    gen.out("get", ancname.substring(0,1).toUpperCase(), ancname.substring(1,dolpos), "().");
                } else {
                    gen.qualify(that, d);
                }
            } else {
                gen.qualify(that, ltype.getDeclaration());
                gen.out(gen.getNames().name(ltype.getDeclaration()));
                gen.out(".$$.prototype.");
            }
            if (d instanceof Value) {
                gen.out("$prop$", gen.getNames().getter(d), ",");
            } else {
                gen.out(gen.getNames().name(d),",");
            }
            if (d.isMember()&&anonClass==null) {
                if (that.getTypeArgumentList()!=null) {
                    gen.out("[");
                    boolean first=true;
                    for (ProducedType targ : that.getTypeArgumentList().getTypeModels()) {
                        if (first)first=false;else gen.out(",");
                        gen.out(GenerateJsVisitor.getClAlias(),"typeLiteral$meta({Type$typeLiteral:");
                        TypeUtils.typeNameOrList(that, targ, gen, false);
                        gen.out("})");
                    }
                    gen.out("]");
                    gen.out(",");
                } else {
                    gen.out("undefined,");
                }
                TypeUtils.printTypeArguments(that, that.getTypeModel().getTypeArguments(), gen, false);
            } else {
                TypeUtils.printTypeArguments(that, that.getTypeModel().getTypeArguments(), gen, false);
                if (anonClass != null) {
                    gen.qualify(that, anonClass);
                    final String ancname = gen.getNames().name(anonClass);
                    final int dolpos = ancname.lastIndexOf('$');
                    gen.out(",get", ancname.substring(0,1).toUpperCase(), ancname.substring(1,dolpos), "()");
                }
                if (ref.getTypeArguments() != null && !ref.getTypeArguments().isEmpty()) {
                    if (anonClass == null) {
                        gen.out(",undefined");
                    }
                    gen.out(",");
                    TypeUtils.printTypeArguments(that, ref.getTypeArguments(), gen, false);
                }
            }
            gen.out(")");
        } else if (that instanceof ValueLiteral || d instanceof Value) {
            Value vd = (Value)d;
            if (vd.isMember() && anonClass==null) {
                gen.out(GenerateJsVisitor.getClAlias(), "$init$AppliedAttribute$meta$model()('");
                gen.out(d.getName(), "',");
            } else {
                gen.out(GenerateJsVisitor.getClAlias(), "$init$AppliedValue$meta$model()(");
                if (anonClass == null) {
                    gen.out("undefined");
                } else {
                    gen.qualify(that, anonClass);
                    final String ancname = gen.getNames().name(anonClass);
                    final int dolpos = ancname.lastIndexOf('$');
                    gen.out("get", ancname.substring(0,1).toUpperCase(), ancname.substring(1,dolpos), "()");
                }
                gen.out(",");
            }
            if (ltype == null) {
                if (anonClass != null) {
                    gen.qualify(that, anonClass);
                    final String ancname = gen.getNames().name(anonClass);
                    final int dolpos = ancname.lastIndexOf('$');
                    gen.out("get", ancname.substring(0,1).toUpperCase(), ancname.substring(1,dolpos), "().");
                } else {
                    gen.qualify(that, d);
                }
            } else {
                gen.qualify(that, ltype.getDeclaration());
                gen.out(gen.getNames().name(ltype.getDeclaration()));
                gen.out(".$$.prototype.");
            }
            if (d instanceof Value) {
                gen.out("$prop$", gen.getNames().getter(d),",");
            } else {
                gen.out(gen.getNames().name(d),",");
            }
            TypeUtils.printTypeArguments(that, that.getTypeModel().getTypeArguments(), gen, false);
            gen.out(")");
        } else {
            gen.out(GenerateJsVisitor.getClAlias(), "/*TODO:closed member literal*/typeLiteral$meta({Type$typeLiteral:");
            gen.out("{t:");
            if (ltype == null) {
                gen.qualify(that, d);
            } else {
                gen.qualify(that, ltype.getDeclaration());
                gen.out(gen.getNames().name(ltype.getDeclaration()));
                gen.out(".$$.prototype.");
            }
            if (d instanceof Value) {
                gen.out("$prop$", gen.getNames().getter(d));
            } else {
                gen.out(gen.getNames().name(d));
            }
            if (ltype != null && ltype.getTypeArguments() != null && !ltype.getTypeArguments().isEmpty()) {
                gen.out(",a:");
                TypeUtils.printTypeArguments(that, ltype.getTypeArguments(), gen, false);
            }
            gen.out("}})");
        }
    }

    static void findModule(final Module m, final GenerateJsVisitor gen) {
        gen.out(GenerateJsVisitor.getClAlias(), "getModules$meta().find('",
                m.getNameAsString(), "','", m.getVersion(), "')");
    }

}
