package com.redhat.ceylon.compiler.js;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.redhat.ceylon.compiler.js.util.TypeUtils;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ValueLiteral;
import com.redhat.ceylon.model.typechecker.model.Class;
import com.redhat.ceylon.model.typechecker.model.Declaration;
import com.redhat.ceylon.model.typechecker.model.Function;
import com.redhat.ceylon.model.typechecker.model.Module;
import com.redhat.ceylon.model.typechecker.model.Type;
import com.redhat.ceylon.model.typechecker.model.Scope;
import com.redhat.ceylon.model.typechecker.model.TypeAlias;
import com.redhat.ceylon.model.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.model.typechecker.model.TypeParameter;
import com.redhat.ceylon.model.typechecker.model.ModelUtil;
import com.redhat.ceylon.model.typechecker.model.Value;

public class MetamodelHelper {

    static void generateOpenType(final Tree.MetaLiteral that, final Declaration d, final GenerateJsVisitor gen) {
        final Module m = d.getUnit().getPackage().getModule();
        final boolean isConstructor = d instanceof com.redhat.ceylon.model.typechecker.model.Constructor
                || that instanceof Tree.NewLiteral;
        if (d instanceof TypeParameter == false) {
            if (JsCompiler.isCompilingLanguageModule()) {
                gen.out("$init$");
            } else {
                gen.out(gen.getClAlias());
            }
        }
        if (d instanceof com.redhat.ceylon.model.typechecker.model.Interface) {
            gen.out("OpenInterface$jsint");
        } else if (isConstructor) {
            gen.out("OpenConstructor$jsint");
        } else if (d instanceof Class) {
            gen.out("openClass$jsint");
        } else if (d instanceof Function) {
            gen.out("OpenFunction$jsint");
        } else if (d instanceof Value) {
            gen.out("OpenValue$jsint");
        } else if (d instanceof com.redhat.ceylon.model.typechecker.model.IntersectionType) {
            gen.out("OpenIntersection");
        } else if (d instanceof com.redhat.ceylon.model.typechecker.model.UnionType) {
            gen.out("OpenUnion");
        } else if (d instanceof TypeParameter) {
            generateOpenType(that, ((TypeParameter)d).getDeclaration(), gen);
            gen.out(".getTypeParameterDeclaration('", d.getName(), "')");
            return;
        } else if (d instanceof com.redhat.ceylon.model.typechecker.model.NothingType) {
            gen.out("NothingType");
        } else if (d instanceof TypeAlias) {
            gen.out("OpenAlias$jsint(");
            if (JsCompiler.isCompilingLanguageModule()) {
                gen.out(")(");
            }
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
        //TODO optimize for local declarations
        if (JsCompiler.isCompilingLanguageModule()) {
            gen.out("()");
        }
        gen.out("(", gen.getClAlias());
        final String pkgname = d.getUnit().getPackage().getNameAsString();
        if (Objects.equals(that.getUnit().getPackage().getModule(), d.getUnit().getPackage().getModule())) {
            gen.out("lmp$(ex$,'");
        } else {
            //TODO use $ for language module as well
            gen.out("fmp$('", m.getNameAsString(), "','", m.getVersion(), "','");
        }
        gen.out("ceylon.language".equals(pkgname) ? "$" : pkgname, "'),");
        if (d.isMember() || isConstructor) {
            if (isConstructor) {
                final Class actualClass;
                final String constrName;
                if (d instanceof Class) {
                    actualClass = (Class)d;
                    constrName = "$c$";
                } else {
                    actualClass = (Class)d.getContainer();
                    constrName = gen.getNames().name(d);
                }
                if (actualClass.isMember()) {
                    outputPathToDeclaration(that, actualClass, gen);
                }
                gen.out(gen.getNames().name(actualClass),
                        "_", constrName, ")");
                return;
            } else {
                outputPathToDeclaration(that, d, gen);
            }
        }
        if (d instanceof Value || d.isParameter()) {
            if (!d.isMember()) gen.qualify(that, d);
            gen.out(gen.getNames().getter(d, true), ")");
        } else {
            if (d.isAnonymous()) {
                final String oname = gen.getNames().objectName(d);
                if (d.isToplevel()) {
                    gen.qualify(that, d);
                }
                gen.out("$init$", oname);
                if (!d.isToplevel()) {
                    gen.out("()");
                }
            } else {
                if (!d.isMember()) gen.qualify(that, d);
                gen.out(gen.getNames().name(d));
            }
            gen.out(")");
        }
    }

    static void generateClosedTypeLiteral(final Tree.TypeLiteral that, final GenerateJsVisitor gen) {
        final Type ltype = that.getType().getTypeModel();
        final TypeDeclaration td = ltype.getDeclaration();
        final Map<TypeParameter,Type> targs = that.getType().getTypeModel().getTypeArguments();
        final boolean isConstructor = that instanceof Tree.NewLiteral
                || td instanceof com.redhat.ceylon.model.typechecker.model.Constructor;
        if (ltype.isClass()) {
            if (td.isClassOrInterfaceMember()) {
                gen.out(gen.getClAlias(), "$init$AppliedMemberClass$jsint()(");
            } else {
                gen.out(gen.getClAlias(), "$init$AppliedClass$jsint()(");
            }
            //Tuple is a special case, otherwise gets gen'd as {t:'T'...
            if (that.getUnit().getTupleDeclaration().equals(td)) {
                gen.qualify(that, td);
                gen.out(gen.getNames().name(td));
            } else {
                TypeUtils.outputQualifiedTypename(null, gen.isImported(gen.getCurrentPackage(), td), ltype, gen, false);
            }
            gen.out(",");
            TypeUtils.printTypeArguments(that, that.getTypeModel().getTypeArguments(), gen, false,
                    that.getTypeModel().getVarianceOverrides());
            if (targs != null && !targs.isEmpty()) {
                gen.out(",undefined,");
                TypeUtils.printTypeArguments(that, targs, gen, false,
                        that.getType().getTypeModel().getVarianceOverrides());
            }
            gen.out(")");
        } else if (isConstructor) {
            Class _pc = ltype.isClass() ? (Class)td : (Class)td.getContainer();
            if (_pc.isToplevel()) {
                gen.out(gen.getClAlias(), "$init$AppliedConstructor$jsint()(");
            } else {
                gen.out(gen.getClAlias(), "$init$AppliedMemberConstructor$jsint()(");
            }
            TypeUtils.outputQualifiedTypename(null, gen.isImported(gen.getCurrentPackage(), _pc), _pc.getType(), gen, false);
            gen.out(".", gen.getNames().name(_pc), "_", gen.getNames().name(td), ",");
            TypeUtils.printTypeArguments(that, that.getTypeModel().getTypeArguments(), gen, false,
                    that.getTypeModel().getVarianceOverrides());
            if (targs != null && !targs.isEmpty()) {
                gen.out(",undefined,");
                TypeUtils.printTypeArguments(that, targs, gen, false,
                        that.getType().getTypeModel().getVarianceOverrides());
            }
            gen.out(")");
        } else if (ltype.isInterface()) {
            if (td.isToplevel()) {
                gen.out(gen.getClAlias(), "$init$AppliedInterface$jsint()(");
            } else {
                gen.out(gen.getClAlias(), "$init$AppliedMemberInterface$jsint()(");
            }
            TypeUtils.outputQualifiedTypename(null, gen.isImported(gen.getCurrentPackage(), td), ltype, gen, false);
            gen.out(",");
            TypeUtils.printTypeArguments(that, that.getTypeModel().getTypeArguments(), gen, false,
                    that.getTypeModel().getVarianceOverrides());
            if (targs != null && !targs.isEmpty()) {
                gen.out(",undefined,");
                TypeUtils.printTypeArguments(that, targs, gen, false,
                        that.getType().getTypeModel().getVarianceOverrides());
            }
            gen.out(")");
        } else if (ltype.isNothing()) {
            gen.out(gen.getClAlias(),"nothingType$meta$model()");
        } else if (that instanceof Tree.AliasLiteral) {
            gen.out("/*TODO: applied alias*/");
        } else if (that instanceof Tree.TypeParameterLiteral) {
            gen.out("/*TODO: applied type parameter*/");
        } else {
            gen.out(gen.getClAlias(), "typeLiteral$meta({Type$typeLiteral:");
            TypeUtils.typeNameOrList(that, ltype, gen, false);
            gen.out("})");
        }
    }

    static void generateMemberLiteral(final Tree.MemberLiteral that, final GenerateJsVisitor gen) {
        final com.redhat.ceylon.model.typechecker.model.Reference ref = that.getTarget();
        final Type ltype = that.getType() == null ? null : that.getType().getTypeModel();
        final Declaration d = ref.getDeclaration();
        final Class anonClass = d.isMember()&&d.getContainer() instanceof Class && ((Class)d.getContainer()).isAnonymous()?(Class)d.getContainer():null;
        if (that instanceof Tree.FunctionLiteral || d instanceof Function) {
            gen.out(gen.getClAlias(), d.isMember()?"AppliedMethod$jsint(":"AppliedFunction$jsint(");
            if (anonClass != null) {
                gen.qualify(that, anonClass);
                gen.out(gen.getNames().objectName(anonClass), ".");
            } else if (ltype == null) {
                gen.qualify(that, d);
            } else {
                if (ltype.getDeclaration().isMember()) {
                    outputPathToDeclaration(that, ltype.getDeclaration(), gen);
                } else {
                    gen.qualify(that, ltype.getDeclaration());
                }
                gen.out(gen.getNames().name(ltype.getDeclaration()));
                gen.out(".$$.prototype.");
            }
            if (d instanceof Value) {
                gen.out(gen.getNames().getter(d, true), ",");
            } else {
                gen.out(gen.getNames().name(d),",");
            }
            if (d.isMember()) {
                if (that.getTypeArgumentList()!=null) {
                    List<Type> typeModels = that.getTypeArgumentList().getTypeModels();
                    if (typeModels!=null) {
                        gen.out("[");
                        boolean first=true;
                        for (Type targ : typeModels) {
                            if (first)first=false;else gen.out(",");
                            gen.out(gen.getClAlias(),"typeLiteral$meta({Type$typeLiteral:");
                            TypeUtils.typeNameOrList(that, targ, gen, false);
                            gen.out("})");
                        }
                        gen.out("]");
                        gen.out(",");
                    }
                    else {
                        gen.out("undefined,");
                    }
                } else {
                    gen.out("undefined,");
                }
                TypeUtils.printTypeArguments(that, that.getTypeModel().getTypeArguments(), gen, false,
                        that.getTypeModel().getVarianceOverrides());
            } else {
                TypeUtils.printTypeArguments(that, that.getTypeModel().getTypeArguments(), gen, false,
                        that.getTypeModel().getVarianceOverrides());
                if (ref.getTypeArguments() != null && !ref.getTypeArguments().isEmpty()) {
                    gen.out(",undefined,");
                    TypeUtils.printTypeArguments(that, ref.getTypeArguments(), gen, false,
                            ref.getType().getVarianceOverrides());
                }
            }
            gen.out(")");
        } else if (that instanceof ValueLiteral || d instanceof Value) {
            Value vd = (Value)d;
            if (vd.isMember()) {
                gen.out(gen.getClAlias(), "$init$AppliedAttribute$meta$model()('");
                gen.out(d.getName(), "',");
            } else {
                gen.out(gen.getClAlias(), "$init$AppliedValue$jsint()(undefined,");
            }
            if (anonClass != null) {
                gen.qualify(that, anonClass);
                gen.out(gen.getNames().objectName(anonClass), ".");
            } else if (ltype == null) {
                gen.qualify(that, d);
            } else {
                gen.qualify(that, ltype.getDeclaration());
                gen.out(gen.getNames().name(ltype.getDeclaration()));
                gen.out(".$$.prototype.");
            }
            if (d instanceof Value) {
                gen.out(gen.getNames().getter(d, true),",");
            } else {
                gen.out(gen.getNames().name(d),",");
            }
            TypeUtils.printTypeArguments(that, that.getTypeModel().getTypeArguments(), gen, false,
                    that.getTypeModel().getVarianceOverrides());
            gen.out(")");
        } else {
            gen.out(gen.getClAlias(), "/*TODO:closed member literal*/typeLiteral$meta({Type$typeLiteral:");
            gen.out("{t:");
            if (ltype == null) {
                gen.qualify(that, d);
            } else {
                gen.qualify(that, ltype.getDeclaration());
                gen.out(gen.getNames().name(ltype.getDeclaration()));
                gen.out(".$$.prototype.");
            }
            if (d instanceof Value) {
                gen.out(gen.getNames().getter(d, true));
            } else {
                gen.out(gen.getNames().name(d));
            }
            if (ltype != null && ltype.getTypeArguments() != null && !ltype.getTypeArguments().isEmpty()) {
                gen.out(",a:");
                TypeUtils.printTypeArguments(that, ltype.getTypeArguments(), gen, false,
                        ltype.getVarianceOverrides());
            }
            gen.out("}})");
        }
    }

    static void findModule(final Module m, final GenerateJsVisitor gen) {
        gen.out(gen.getClAlias(), "modules$meta().find('",
                m.getNameAsString(), "','", m.getVersion(), "')");
    }

    static void outputPathToDeclaration(final Node that, final Declaration d, final GenerateJsVisitor gen) {
        final Declaration parent = ModelUtil.getContainingDeclaration(d);
        if (!gen.opts.isOptimize() && parent instanceof TypeDeclaration && ModelUtil.contains((Scope)parent, that.getScope())) {
            gen.out(gen.getNames().self((TypeDeclaration)parent), ".");
        } else {
            Declaration _md = d;
            final ArrayList<Declaration> parents = new ArrayList<>(3);
            while (_md.isMember()) {
                _md=ModelUtil.getContainingDeclaration(_md);
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
                    final String oname = gen.getNames().objectName(_d);
                    if (_d.isToplevel()) {
                        gen.out(oname, ".");
                    } else {
                        gen.out("$init$", oname, "().$$.prototype.");
                    }
                } else {
                    if (!imported)gen.out("$init$");
                    gen.out(gen.getNames().name(_d), imported?".$$.prototype.":"().$$.prototype.");
                }
                imported=true;
            }
        }
    }

}
