package com.redhat.ceylon.compiler.js;

import java.util.List;
import java.util.Map;

import com.redhat.ceylon.compiler.js.util.TypeUtils;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.PositionalArgument;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.SequencedArgument;
import com.redhat.ceylon.model.typechecker.model.Functional;
import com.redhat.ceylon.model.typechecker.model.Generic;
import com.redhat.ceylon.model.typechecker.model.Type;
import com.redhat.ceylon.model.typechecker.model.TypeParameter;
import com.redhat.ceylon.model.typechecker.model.ModelUtil;

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
                if (gen.isInDynamicBlock() && expr instanceof Tree.SpreadArgument
                        && ModelUtil.isTypeUnknown(expr.getTypeModel())) {
                    TypeUtils.spreadArrayCheck(((Tree.SpreadArgument)expr).getExpression(), gen);
                } else {
                    expr.visit(gen);
                }
                gen.out(";},");
            } else {
                gen.out("case ", Integer.toString(count), ":return ");
                expr.visit(gen);
                gen.out(";");
            }
            count++;
        }
        if (seqarg == null) {
            gen.out("}return ", gen.getClAlias(), "finished();},undefined,");
        }
        TypeUtils.printTypeArguments(node, seqType.getTypeArguments(), gen, false, seqType.getVarianceOverrides());
        gen.out(")");
    }

    static void sequenceEnumeration(final Tree.SequenceEnumeration that, final GenerateJsVisitor gen) {
        final Tree.SequencedArgument sarg = that.getSequencedArgument();
        if (sarg == null) {
            gen.out(gen.getClAlias(), "empty()");
        } else {
            final List<Tree.PositionalArgument> positionalArguments = sarg.getPositionalArguments();
            final boolean spread = isSpread(positionalArguments);
            final boolean canBeEager = allLiterals(positionalArguments);
            if (spread || !canBeEager) {
                lazyEnumeration(positionalArguments, that, that.getTypeModel(), spread, gen);
                return;
            } else {
                gen.out("[");
            }
            int count=0;
            for (Tree.PositionalArgument expr : positionalArguments) {
                if (count > 0) {
                    gen.out(",");
                }
                if (gen.isInDynamicBlock() && expr instanceof Tree.ListedArgument && ModelUtil.isTypeUnknown(expr.getTypeModel())
                        && expr.getParameter() != null && !ModelUtil.isTypeUnknown(expr.getParameter().getType())) {
                    //TODO find out how to test this, if at all possible
                    TypeUtils.generateDynamicCheck(((Tree.ListedArgument)expr).getExpression(),
                            expr.getParameter().getType(), gen, false, that.getTypeModel().getTypeArguments());
                } else {
                    expr.visit(gen);
                }
                count++;
            }
            closeSequenceWithReifiedType(that, that.getTypeModel().getTypeArguments(), gen, true);
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
            if (arg instanceof Tree.ListedArgument) {
                ((Tree.ListedArgument) arg).getExpression().visit(gen);
            } else if(arg instanceof Tree.SpreadArgument) {
                ((Tree.SpreadArgument) arg).getExpression().visit(gen);
            } else {// comprehension
                arg.visit(gen);
            }
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
        if (isMethod) {
            gen.out(gen.getClAlias(), "JsCallableList(");
            gen.supervisit(that);
            gen.out(",function(e,a){return ",
                    gen.memberAccess(that, "e"), ".apply(e,a);}");
            if (that.getTypeArguments() != null && that.getTypeArguments().getTypeModels()!=null
                    && !that.getTypeArguments().getTypeModels().isEmpty()) {
                gen.out(",");
                TypeUtils.printTypeArguments(that, TypeUtils.matchTypeParametersWithArguments(
                        ((Generic)that.getDeclaration()).getTypeParameters(),
                        that.getTypeArguments().getTypeModels()), gen, true, null);
            }
            gen.out(")");
        } else {
            gen.supervisit(that);
            gen.out(".collect(function(e){return ", gen.memberAccess(that, "e"),
                    ";},{Result$collect:");
            TypeUtils.typeNameOrList(that, that.getTypeModel().getTypeArgumentList().get(0), gen, false);
            gen.out("})");
        }
    }

    static boolean isSpread(List<Tree.PositionalArgument> args) {
        return !args.isEmpty() && args.get(args.size()-1) instanceof Tree.ListedArgument == false;
    }

    static boolean allLiterals(List<Tree.PositionalArgument> args) {
        for (Tree.PositionalArgument a : args) {
            if (a instanceof Tree.ListedArgument) {
                if (((Tree.ListedArgument) a).getExpression().getTerm() instanceof Tree.Literal == false) {
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
            final GenerateJsVisitor gen, boolean wantsIterable) {
        if(wantsIterable)
            gen.out("].rt$(");
        else
            gen.out("].$sa$(");
        boolean nonempty=false;
        Type elem = null;
        for (Map.Entry<TypeParameter,Type> e : types.entrySet()) {
            if (e.getKey().getName().equals("Element")) {
                elem = e.getValue();
            } else if (e.getKey().equals(that.getUnit().getIterableDeclaration().getTypeParameters().get(1))) {
                //If it's Nothing, it's nonempty
                nonempty = "ceylon.language::Nothing".equals(e.getValue().asQualifiedString());
            }
        }
        if (elem == null) {
            gen.out("/*WARNING no Element found* /");
            elem = that.getUnit().getAnythingType();
        }
        TypeUtils.typeNameOrList(that, elem, gen, false);
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
                    expr.visit(gen);
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

}
