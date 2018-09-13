/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.compiler.js;

import static org.eclipse.ceylon.compiler.js.util.TypeUtils.generateDynamicCheck;
import static org.eclipse.ceylon.compiler.js.util.TypeUtils.matchTypeParametersWithArguments;
import static org.eclipse.ceylon.compiler.js.util.TypeUtils.printTypeArguments;
import static org.eclipse.ceylon.compiler.js.util.TypeUtils.spreadArrayCheck;
import static org.eclipse.ceylon.compiler.js.util.TypeUtils.typeNameOrList;

import java.util.List;
import java.util.Map;

import org.eclipse.ceylon.compiler.typechecker.tree.Node;
import org.eclipse.ceylon.compiler.typechecker.tree.Tree;
import org.eclipse.ceylon.compiler.typechecker.tree.Tree.PositionalArgument;
import org.eclipse.ceylon.compiler.typechecker.tree.Tree.SequencedArgument;
import org.eclipse.ceylon.compiler.typechecker.tree.Tree.TypeArguments;
import org.eclipse.ceylon.model.typechecker.model.Functional;
import org.eclipse.ceylon.model.typechecker.model.ModelUtil;
import org.eclipse.ceylon.model.typechecker.model.Type;
import org.eclipse.ceylon.model.typechecker.model.TypeParameter;

public class SequenceGenerator {

    static void lazyEnumeration(final List<Tree.PositionalArgument> args, final Node node, final Type seqType,
            final boolean spread, final GenerateJsVisitor gen) {
        Tree.PositionalArgument seqarg = spread ? args.get(args.size()-1) : null;
        if (args.size() == 1 && seqarg instanceof Tree.Comprehension) {
            //Shortcut: just do the comprehension
            seqarg.visit(gen);
            return;
        }
        final String idxvar = gen.getNames().createTempVariable();
        gen.out(gen.getClAlias(), "sarg$(function(", idxvar,"){switch(",idxvar,"){");
        int count=0;
        for (Tree.PositionalArgument expr : args) {
            if (expr == seqarg) {
                gen.out("}return ", gen.getClAlias(), "finished();},function(){return ");
                if (gen.isInDynamicBlock() 
                        && expr instanceof Tree.SpreadArgument
                        && ModelUtil.isTypeUnknown(expr.getTypeModel())) {
                    spreadArrayCheck(
                            ((Tree.SpreadArgument)expr).getExpression(), 
                            gen);
                } else {
                    boxArg(gen, expr);
                }
                gen.out(";},");
            } else {
                gen.out("case ", Integer.toString(count), ":return ");
                boxArg(gen, expr);
                gen.out(";");
            }
            count++;
        }
        if (seqarg == null) {
            gen.out("}return ", gen.getClAlias(), "finished();},undefined,");
        }
        printTypeArguments(node, seqType, gen, false);
        gen.out(")");
    }

    static void sequenceEnumeration(final Tree.SequenceEnumeration that, final GenerateJsVisitor gen) {
        final Tree.SequencedArgument sarg = that.getSequencedArgument();
        if (sarg == null) {
            gen.out(gen.getClAlias(), "empty()");
        } else {
            final List<Tree.PositionalArgument> positionalArgs = sarg.getPositionalArguments();
            final boolean spread = isSpread(positionalArgs);
            final boolean canBeEager = allLiterals(positionalArgs);
            boolean wantsIter = false;
            if (spread || !canBeEager) {
                lazyEnumeration(positionalArgs, that, that.getTypeModel(), spread, gen);
                return;
            } else if (that.getTypeModel().isSequential()) {
                gen.out(gen.getClAlias(), "$arr$sa$([");
            } else {
                gen.out(gen.getClAlias(), "sarg$(", gen.getClAlias(), "$lai$([");
                wantsIter = true;
            }
            int count=0;
            for (Tree.PositionalArgument expr : positionalArgs) {
                if (count > 0) {
                    gen.out(",");
                }
                if (gen.isInDynamicBlock() 
                        && expr instanceof Tree.ListedArgument 
                        && ModelUtil.isTypeUnknown(expr.getTypeModel())
                        && expr.getParameter() != null 
                        && !ModelUtil.isTypeUnknown(expr.getParameter().getType())) {
                    //TODO find out how to test this, if at all possible
                    generateDynamicCheck(
                            ((Tree.ListedArgument)expr).getExpression(),
                            expr.getParameter().getType(), 
                            gen, false, 
                            that.getTypeModel().getTypeArguments());
                } else {
                    boxArg(gen, expr);
                }
                count++;
            }
            closeSequenceWithReifiedType(that, that.getTypeModel().getTypeArguments(), gen, wantsIter);
        }
    }

    static void sequencedArgument(final Tree.SequencedArgument that, final GenerateJsVisitor gen) {
        final List<Tree.PositionalArgument> positionalArguments = that.getPositionalArguments();
        final boolean spread = isSpread(positionalArguments);
        if (!spread) {
            gen.out("[");
        }
        boolean first=true;
        for (Tree.PositionalArgument arg: positionalArguments) {
            if (!first) {
                gen.out(",");
            }
            boxArg(gen, arg);
            first = false;
        }
        if (!spread) {
            gen.out("]");
        }
    }

    /** SpreadOp cannot be a simple function call because we need to reference the object methods directly, so it's a function */
    static void generateSpread(final Tree.QualifiedMemberOrTypeExpression that, final GenerateJsVisitor gen) {
        //Determine if it's a method or attribute
        boolean isMethod = that.getDeclaration() instanceof Functional;
        Type type = that.getTypeModel();
        if (isMethod) {
            gen.out(gen.getClAlias(), "JsCallableList(");
            gen.supervisit(that);
            gen.out(",function(e,a){return ",
                    gen.memberAccess(that, "e"), ".apply(e,a);},");
            TypeArguments typeArgs = that.getTypeArguments();
            if (typeArgs != null 
                    && typeArgs.getTypeModels()!=null
                    && !typeArgs.getTypeModels().isEmpty()) {
                printTypeArguments(that, gen, true, 
                        matchTypeParametersWithArguments(
                                that.getDeclaration().getTypeParameters(),
                                typeArgs.getTypeModels()),
                        null);
            } else {
                gen.out("undefined");
            }
            gen.out(",");
            if (type != null && type.isCallable()) {
                typeNameOrList(that, type.getTypeArgumentList().get(0).getTypeArgumentList().get(0), gen, false);
            } else {
                typeNameOrList(that, type, gen, false);
            }
            gen.out(")");
        } else {
            gen.supervisit(that);
            gen.out(".collect(function(e){return ", gen.memberAccess(that, "e"),
                    ";},{Result$collect:");
            typeNameOrList(that, type.getTypeArgumentList().get(0), gen, false);
            gen.out("})");
        }
    }

    static boolean isSpread(List<Tree.PositionalArgument> args) {
        return !args.isEmpty() 
            && !(args.get(args.size()-1) instanceof Tree.ListedArgument);
    }

    static boolean allLiterals(List<Tree.PositionalArgument> args) {
        for (Tree.PositionalArgument a : args) {
            if (a instanceof Tree.ListedArgument) {
                if (!(((Tree.ListedArgument) a).getExpression().getTerm() 
                        instanceof Tree.Literal)) {
                    return false;
                }
            } else {
                return false;
            }
        }
        return true;
    }
    /** Closes a native array and invokes reifyCeylonType (rt$) with the specified type parameters. */
    static void closeSequenceWithReifiedType(final Node that, final Map<TypeParameter,Type> types,
            final GenerateJsVisitor gen, final boolean wantsIterable) {
        if (wantsIterable) {
            gen.out("]),undefined,{Element$Iterable:");
        } else {
            gen.out("],");
        }
        boolean nonempty=false;
        Type elem = null;
        for (Map.Entry<TypeParameter,Type> e : types.entrySet()) {
            if (e.getKey().getName().equals("Element")) {
                elem = e.getValue();
            } else if (e.getKey().equals(that.getUnit().getIterableDeclaration().getTypeParameters().get(1))) {
                //If it's Nothing, it's nonempty
                nonempty = e.getValue().isNothing();
            }
        }
        if (elem == null) {
            gen.out("/*WARNING no Element found* /");
            elem = that.getUnit().getAnythingType();
        }
        typeNameOrList(that, elem, gen, false);
        if (wantsIterable) {
            gen.out(",Absent$Iterable:{t:", gen.getClAlias(),
                    nonempty?"Nothing}}":"Null}}");

        }
        if (nonempty) {
            gen.out(",1");
        }
        gen.out(")");
    }

    static void tuple(final Tree.Tuple that, final GenerateJsVisitor gen) {
        SequencedArgument sarg = that.getSequencedArgument();
        if (sarg == null) {
            gen.out(gen.getClAlias(), "empty()");
        } else {
            final List<PositionalArgument> positionalArguments = sarg.getPositionalArguments();
            final boolean spread = SequenceGenerator.isSpread(positionalArguments);
            int lim = positionalArguments.size()-1;
            gen.out(gen.getClAlias(), "tpl$([");
            int count = 0;
            for (PositionalArgument expr : positionalArguments) {
                if (!(count==lim && spread)) {
                    if (count > 0) {
                        gen.out(",");
                    }
                    boxArg(gen, expr);
                }
                count++;
            }
            gen.out("]");
            if (spread) {
                gen.out(",");
                positionalArguments.get(lim).visit(gen);
            }
            gen.out(")");
        }
    }

    private static void boxArg(final GenerateJsVisitor gen, PositionalArgument arg) {
        if (arg instanceof Tree.ListedArgument) {
            gen.box(((Tree.ListedArgument) arg).getExpression(), true, false);
        }
        else {
            arg.visit(gen);
        }
    }

}
