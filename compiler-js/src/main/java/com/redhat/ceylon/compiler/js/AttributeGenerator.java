package com.redhat.ceylon.compiler.js;

import java.util.Set;

import com.redhat.ceylon.compiler.js.util.TypeUtils;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Expression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.LazySpecifierExpression;
import com.redhat.ceylon.model.typechecker.model.Declaration;
import com.redhat.ceylon.model.typechecker.model.Functional;
import com.redhat.ceylon.model.typechecker.model.Function;
import com.redhat.ceylon.model.typechecker.model.FunctionOrValue;
import com.redhat.ceylon.model.typechecker.model.Scope;
import com.redhat.ceylon.model.typechecker.model.Setter;
import com.redhat.ceylon.model.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.model.typechecker.model.ModelUtil;
import com.redhat.ceylon.model.typechecker.model.Value;

public class AttributeGenerator {

    static void getter(final Tree.AttributeGetterDefinition that, final GenerateJsVisitor gen, final boolean block) {
        if (block) {
            gen.beginBlock();
        }
        gen.initSelf(that);
        gen.visitStatements(that.getBlock().getStatements());
        if (block) {
            gen.endBlock();
        }
    }

    static void setter(final Tree.AttributeSetterDefinition that, final GenerateJsVisitor gen) {
        if (that.getSpecifierExpression() == null) {
            gen.beginBlock();
            gen.initSelf(that);
            gen.visitStatements(that.getBlock().getStatements());
            gen.endBlock();
        } else {
            gen.out("{");
            gen.initSelf(that);
            gen.out("return ");
            if (!gen.isNaturalLiteral(that.getSpecifierExpression().getExpression().getTerm())) {
                that.getSpecifierExpression().visit(gen);
            }
            gen.out(";}");
        }
    }

    static void generateAttributeGetter(final Tree.AnyAttribute attributeNode, final FunctionOrValue decl,
            final Tree.SpecifierOrInitializerExpression expr, final String param, final GenerateJsVisitor gen,
            final Set<Declaration> directAccess) {
        final boolean asProp = defineAsProperty(decl) && (gen.isCaptured(decl) || decl.isToplevel());
        final String varName;
        if (asProp) {
            varName = decl.isShared() ? gen.getNames().name(decl) + "_" : gen.getNames().privateName(decl);
        } else {
            varName = gen.getNames().name(decl);
        }
        final boolean initVal = decl.isToplevel() && !TypeUtils.isNativeExternal(decl);
        boolean stitch = TypeUtils.isNativeExternal(decl);
        gen.out("var ", varName);
        if (expr != null) {
            if (initVal) {
                gen.out(";function $valinit$", varName, "(){");
                gen.out("if(", varName, "===", gen.getClAlias(), "INIT$)");
                gen.generateThrow(gen.getClAlias()+"InitializationError",
                        "Cyclic initialization trying to read the value of '" +
                        decl.getName() + "' before it was set", attributeNode==null?expr:attributeNode);
                gen.endLine(true);
                gen.out("if(", varName, "===undefined){",
                        varName, "=", gen.getClAlias(), "INIT$;", varName, "=");
            } else {
                gen.out("=");
            }
            boolean genatr=true;
            if (stitch) {
                genatr=!gen.stitchNative(decl, attributeNode);
                if (!genatr) {
                    gen.spitOut("Stitching in native attribute " + decl.getQualifiedNameString()
                            + ", ignoring Ceylon declaration");
                }
                stitch=false;
            }
            if (genatr) {
                int boxType = gen.boxStart(expr.getExpression().getTerm());
                if (gen.isInDynamicBlock() && ModelUtil.isTypeUnknown(expr.getExpression().getTypeModel())
                        && !ModelUtil.isTypeUnknown(decl.getType())) {
                    TypeUtils.generateDynamicCheck(expr.getExpression(), decl.getType(), gen, false,
                            expr.getExpression().getTypeModel().getTypeArguments());
                } else {
                    expr.visit(gen);
                }
                if (boxType == 4) {
                    //Pass Callable argument types
                    gen.out(",");
                    if (decl instanceof Function) {
                        //Add parameters
                        TypeUtils.encodeParameterListForRuntime(true, attributeNode, ((Function)decl).getFirstParameterList(), gen);
                    } else {
                        //Type of value must be Callable
                        //And the Args Type Parameters is a Tuple
                        TypeUtils.encodeCallableArgumentsAsParameterListForRuntime(attributeNode,
                                expr.getExpression().getTypeModel(), gen);
                    }
                    gen.out(",");
                    TypeUtils.printTypeArguments(expr, expr.getExpression().getTypeModel().getTypeArguments(), gen, false,
                            expr.getExpression().getTypeModel().getVarianceOverrides());
                }
                gen.boxUnboxEnd(boxType);
            }
            if (initVal) {
                gen.out("};return ", varName, ";}");
            }
        } else if (param != null) {
            gen.out("=", param);
        } else if (stitch) {
            gen.out("=");
            gen.stitchNative(decl, attributeNode);
            stitch=false;
        }
        gen.endLine(true);
        if (decl instanceof Function) {
            if (decl.isClassOrInterfaceMember() && gen.isCaptured(decl)) {
                gen.beginNewLine();
                gen.outerSelf(decl);
                gen.out(".", varName, "=", varName);
                gen.endLine(true);
            }
        } else {
            if (gen.isCaptured(decl) || decl.isToplevel()) {
                final boolean isLate = decl.isLate();
                if (asProp) {
                    gen.out(gen.getClAlias(), "atr$(");
                    gen.outerSelf(decl);
                    gen.out(",'", gen.getNames().name(decl), "',function(){");
                    if (isLate) {
                        gen.generateUnitializedAttributeReadCheck(varName, gen.getNames().name(decl));
                    }
                    if (initVal) {
                        gen.out("return $valinit$", varName, "();}");
                    } else {
                        gen.out("return ", varName, ";}");
                    }
                    if (decl.isVariable() || isLate) {
                        final String par = gen.getNames().createTempVariable();
                        gen.out(",function(", par, "){");
                        gen.generateImmutableAttributeReassignmentCheck(decl, varName, gen.getNames().name(decl));
                        gen.out("return ", varName, "=", par, ";}");
                    } else {
                        gen.out(",undefined");
                    }
                    gen.out(",");
                    if (attributeNode == null) {
                        TypeUtils.encodeForRuntime(expr, decl, gen);
                    } else {
                        TypeUtils.encodeForRuntime(decl, attributeNode.getAnnotationList(), gen);
                    }
                    gen.out(")");
                    gen.endLine(true);
                }
                else {
                    gen.out(GenerateJsVisitor.function, gen.getNames().getter(decl, false),"(){return ");
                    if (initVal) {
                        gen.out("$valinit$", varName, "();}");
                    } else if (stitch) {
                        gen.stitchNative(decl, attributeNode);
                        gen.out(";}");
                        gen.spitOut("Stitching in native attribute " + decl.getQualifiedNameString()
                                + ", ignoring Ceylon declaration");
                    } else {
                        gen.out(varName, ";}");
                    }
                    gen.endLine();
                    gen.shareGetter(decl);
                }
            } else {
                if (decl.isMember() && gen.qualify(expr, decl)) {
                    gen.out(varName, "=", varName, ";");
                }
                directAccess.add(decl);
            }
        }
    }

    /** Generates a setter function. This is only needed for toplevel variable attributes. s*/
    static boolean generateAttributeSetter(final Tree.AnyAttribute that, final Value d,
            final GenerateJsVisitor gen) {
        if (!d.isToplevel()) {
            return false;
        }
        final String varName = gen.getNames().name(d);
        String paramVarName = gen.getNames().createTempVariable();
        gen.out(GenerateJsVisitor.function, gen.getNames().setter(d), "(", paramVarName, "){");
        gen.generateImmutableAttributeReassignmentCheck(d, varName, gen.getNames().name(d));
        gen.out("if(", varName, "===undefined||",varName,"===",gen.getClAlias(), "INIT$)$valinit$", varName, "();");
        gen.out("return ", varName, "=", paramVarName, ";}");
        gen.endLine(true);
        gen.shareSetter(d);
        return true;
    }

    static void addGetterAndSetterToPrototype(final TypeDeclaration outer, final Tree.AttributeDeclaration that,
            final GenerateJsVisitor gen) {
        final Value d = that.getDeclarationModel();
        if (!gen.opts.isOptimize()||d.isToplevel()||d.getTypeDeclaration()==null) return;
        gen.comment(that);
        final String atname = d.isShared() ? gen.getNames().name(d) + "_" :
            gen.getNames().privateName(d);
        if (d.isFormal()) {
            generateAttributeMetamodel(that, false, false, gen);
        } else if (that.getSpecifierOrInitializerExpression() == null) {
            gen.defineAttribute(gen.getNames().self(outer), gen.getNames().name(d));
            gen.out("{");
            if (TypeUtils.isNativeExternal(d)) {
                //this is for native member attribute declaration with no value
                gen.stitchNative(d, that);
            } else {
                //Just return the private value #451
                if (d.isLate()) {
                    gen.generateUnitializedAttributeReadCheck("this."+atname, gen.getNames().name(d));
                }
                gen.out("return this.", atname , ";");
            }
            gen.out("},");
            Tree.AttributeSetterDefinition setterDef = null;
            boolean setterDefined=false;
            if (d.isVariable() || d.isLate()) {
                setterDef = gen.associatedSetterDefinition(d);
                if (setterDef == null) {
                    setterDefined = true;
                    final String par = gen.getNames().createTempVariable();
                    gen.out("function(", par, "){");
                    gen.generateImmutableAttributeReassignmentCheck(d, "this."+atname, gen.getNames().name(d));
                    gen.out("return this.", atname, "=", par, ";}");
                } else if (!gen.stitchNative(setterDef.getDeclarationModel(), that)) {
                    gen.out("function(", gen.getNames().name(
                            setterDef.getDeclarationModel().getParameter()), ")");
                    setter(setterDef, gen);
                    setterDefined = true;
                }
            }
            if (!setterDefined) {
                gen.out("undefined");
            }
            gen.out(",");
            TypeUtils.encodeForRuntime(d, that.getAnnotationList(), gen);
            if (setterDef != null) {
                gen.out(",");
                TypeUtils.encodeForRuntime(setterDef.getDeclarationModel(), that.getAnnotationList(), gen);
            }
            gen.out(")");
            gen.endLine(true);
        } else {
            com.redhat.ceylon.model.typechecker.model.Parameter param = null;
            if (d.isParameter()) {
                param = ((Functional)d.getContainer()).getParameter(d.getName());
            }
            if ((that.getSpecifierOrInitializerExpression() != null) || d.isVariable() || param != null
                    || d.isLate() || TypeUtils.isNativeExternal(d)) {
                if (that.getSpecifierOrInitializerExpression()
                                instanceof LazySpecifierExpression) {
                    // attribute is defined by a lazy expression ("=>" syntax)
                    gen.defineAttribute(gen.getNames().self(outer), gen.getNames().name(d));
                    gen.beginBlock();
                    gen.initSelf(that);
                    Expression expr = that.getSpecifierOrInitializerExpression().getExpression();
                    boolean stitch = TypeUtils.isNativeExternal(d);
                    if (stitch) {
                        stitch=gen.stitchNative(d, that);
                        if (stitch) {
                            gen.spitOut("Stitching in native getter " + d.getQualifiedNameString() +
                                    ", ignoring Ceylon declaration");
                        }
                    }
                    if (!stitch) {
                        gen.out("return ");
                        if (!gen.isNaturalLiteral(expr.getTerm())) {
                            final int boxType = gen.boxStart(expr.getTerm());
                            expr.visit(gen);
                            gen.endLine(true);
                            if (boxType == 4) gen.out("/*TODO: callable targs 3*/");
                            gen.boxUnboxEnd(boxType);
                        }
                    }
                    gen.endBlock();
                    Tree.AttributeSetterDefinition setterDef = null;
                    if (d.isVariable()) {
                        setterDef = gen.associatedSetterDefinition(d);
                        if (setterDef != null) {
                            gen.out(",function(", gen.getNames().name(setterDef.getDeclarationModel().getParameter()), ")");
                            setter(setterDef, gen);
                        }
                    }
                    if (setterDef == null) {
                        gen.out(",undefined");
                    }
                    gen.out(",");
                    TypeUtils.encodeForRuntime(d, that.getAnnotationList(), gen);
                    if (setterDef != null) {
                        gen.out(",");
                        TypeUtils.encodeForRuntime(setterDef.getDeclarationModel(), that.getAnnotationList(), gen);
                    }
                    gen.out(")");
                    gen.endLine(true);
                }
                else {
                    final String privname = param == null ? gen.getNames().privateName(d) : gen.getNames().name(param)+"_";
                    gen.defineAttribute(gen.getNames().self(outer), gen.getNames().name(d));
                    gen.out("{");
                    if (d.isLate()) {
                        gen.generateUnitializedAttributeReadCheck("this."+privname, gen.getNames().name(d));
                    }
                    if (TypeUtils.isNativeExternal(d)) {
                        if (gen.stitchNative(d, that)) {
                            gen.spitOut("Stitching in native getter " + d.getQualifiedNameString() +
                                    ", ignoring Ceylon declaration");
                        } else {
                            gen.out("return ");
                            that.getSpecifierOrInitializerExpression().getExpression().visit(gen);
                            gen.out(";");
                        }
                        gen.out("}");
                    } else {
                        gen.out("return this.", privname, ";}");
                    }
                    if (d.isVariable() || d.isLate()) {
                        final String pname = gen.getNames().createTempVariable();
                        gen.out(",function(", pname, "){");
                        gen.generateImmutableAttributeReassignmentCheck(d, "this."+privname, gen.getNames().name(d));
                        gen.out("return this.", privname,
                                "=", pname, ";}");
                    } else {
                        gen.out(",undefined");
                    }
                    gen.out(",");
                    TypeUtils.encodeForRuntime(d, that.getAnnotationList(), gen);
                    gen.out(")");
                    gen.endLine(true);
                }
            }
        }
    }

    public static boolean defineAsProperty(Declaration d) {
        // for now, only define member attributes as properties, not toplevel attributes
        return d.isMember() && d instanceof FunctionOrValue && !(d instanceof Function);
    }

    /** Generate runtime metamodel info for an attribute declaration or definition. */
    static void generateAttributeMetamodel(final Tree.TypedDeclaration that,
            final boolean addGetter, final boolean addSetter, GenerateJsVisitor gen) {
        //No need to define all this for local values
        Scope _scope = that.getScope();
        while (_scope != null) {
            //TODO this is bound to change for local decl metamodel
            if (_scope instanceof Declaration) {
                if (_scope instanceof Function)return;
                else break;
            }
            _scope = _scope.getContainer();
        }
        Declaration d = that.getDeclarationModel();
        if (d instanceof Setter) d = ((Setter)d).getGetter();
        final String pname = gen.getNames().getter(d, false);
        final String pnameMeta = gen.getNames().getter(d, true);
        if (!gen.isGeneratedAttribute(d)) {
            if (d.isToplevel()) {
                gen.out("var ");
            } else if (gen.outerSelf(d)) {
                gen.out(".");
            }
            //issue 297 this is only needed in some cases
            gen.out(pnameMeta, "={$crtmm$:");
            TypeUtils.encodeForRuntime(d, that.getAnnotationList(), gen);
            gen.out("}"); gen.endLine(true);
            if (d.isToplevel()) {
                gen.out("ex$.", pnameMeta, "=", pnameMeta);
                gen.endLine(true);
            }
            gen.addGeneratedAttribute(d);
        }
        if (addGetter) {
            if (!d.isToplevel()) {
                if (gen.outerSelf(d))gen.out(".");
            }
            gen.out(pnameMeta, ".get=");
            if (gen.isCaptured(d) && !defineAsProperty(d)) {
                gen.out(pname);
                gen.endLine(true);
                gen.out(pname, ".$crtmm$=", pnameMeta, ".$crtmm$");
            } else {
                if (d.isToplevel()) {
                    gen.out(pname);
                } else {
                    gen.out("function(){return ", gen.getNames().name(d), "}");
                }
            }
            gen.endLine(true);
        }
        if (addSetter) {
            final String pset = gen.getNames().setter(d instanceof Setter ? ((Setter)d).getGetter() : d);
            if (!d.isToplevel()) {
                if (gen.outerSelf(d))gen.out(".");
            }
            gen.out(pnameMeta, ".set=", pset);
            gen.endLine(true);
            gen.out("if(", pset, ".$crtmm$===undefined)", pset, ".$crtmm$=", pnameMeta, ".$crtmm$");
            gen.endLine(true);
        }
    }

}
