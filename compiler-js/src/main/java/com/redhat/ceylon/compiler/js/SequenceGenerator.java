package com.redhat.ceylon.compiler.js;

import java.util.List;
import java.util.Map;

import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.TypeParameter;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.PositionalArgument;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.SequencedArgument;

public class SequenceGenerator {

    static void lazyEnumeration(final List<Tree.PositionalArgument> args, final Node node, final ProducedType seqType,
            final boolean spread, final GenerateJsVisitor gen) {
        final String idxvar = gen.getNames().createTempVariable();
        gen.out(GenerateJsVisitor.getClAlias(), "sarg$(function(", idxvar,"){switch(",idxvar,"){");
        int count=0;
        Tree.PositionalArgument seqarg = spread ? args.get(args.size()-1) : null;
        for (Tree.PositionalArgument expr : args) {
            if (expr == seqarg) {
                gen.out("}return ", GenerateJsVisitor.getClAlias(), "getFinished();},function(){return ");
                expr.visit(gen);
                gen.out(";},");
            } else {
                gen.out("case ", Integer.toString(count), ":return ");
                expr.visit(gen);
                gen.out(";");
            }
            count++;
        }
        if (seqarg == null) {
            gen.out("}return ", GenerateJsVisitor.getClAlias(), "getFinished();},undefined,");
        }
        TypeUtils.printTypeArguments(node, seqType.getTypeArguments(), gen, false);
        gen.out(")");
    }

    static void sequenceEnumeration(final Tree.SequenceEnumeration that, final GenerateJsVisitor gen) {
        final Tree.SequencedArgument sarg = that.getSequencedArgument();
        if (sarg == null) {
            gen.out(GenerateJsVisitor.getClAlias(), "getEmpty()");
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
                if (gen.isInDynamicBlock() && expr instanceof Tree.ListedArgument && TypeUtils.isUnknown(expr.getTypeModel())) {
                    TypeUtils.generateDynamicCheck(((Tree.ListedArgument)expr).getExpression(), gen.getTypeUtils().anything.getType(), gen, false);
                } else {
                    expr.visit(gen);
                }
                count++;
            }
            closeSequenceWithReifiedType(that, that.getTypeModel().getTypeArguments(), gen);
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
        boolean isMethod = that.getDeclaration() instanceof Method;
        //Define a function
        gen.out("(function()");
        gen.beginBlock();
        if (gen.opts.isComment() && !gen.opts.isMinify()) {
            gen.out("//SpreadOp");
            gen.location(that);
            gen.endLine();
        }
        //Declare an array to store the values/references
        String tmplist = gen.getNames().createTempVariable();
        gen.out("var ", tmplist, "=[]"); gen.endLine(true);
        //Get an iterator
        String iter = gen.getNames().createTempVariable();
        gen.out("var ", iter, "=");
        gen.supervisit(that);
        gen.out(".iterator()"); gen.endLine(true);
        //Iterate
        String elem = gen.getNames().createTempVariable();
        gen.out("var ", elem); gen.endLine(true);
        gen.out("while((", elem, "=", iter, ".next())!==", GenerateJsVisitor.getClAlias(), "getFinished())");
        gen.beginBlock();
        //Add value or reference to the array
        gen.out(tmplist, ".push(");
        if (isMethod) {
            gen.out("{o:", elem, ", f:", gen.memberAccess(that, elem), "}");
        } else {
            gen.out(gen.memberAccess(that, elem));
        }
        gen.out(");");
        gen.endBlockNewLine();
        //Gather arguments to pass to the callable
        //Return the array of values or a Callable with the arguments
        gen.out("return ", GenerateJsVisitor.getClAlias());
        if (isMethod) {
            gen.out("JsCallableList(", tmplist, ");");
        } else {
            gen.out("ArraySequence(", tmplist, ",");
            TypeUtils.printTypeArguments(that, that.getTypeModel().getTypeArguments(), gen, true);
            gen.out(");");
        }
        gen.endBlock();
        gen.out("())");
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
    /** Closes a native array and invokes reifyCeylonType with the specified type parameters. */
    static void closeSequenceWithReifiedType(final Node that, final Map<TypeParameter,ProducedType> types,
            final GenerateJsVisitor gen) {
        gen.out("].reifyCeylonType(");
        boolean nonempty=false;
        ProducedType elem = null;
        for (Map.Entry<TypeParameter,ProducedType> e : types.entrySet()) {
            if (e.getKey().getName().equals("Element")) {
                elem = e.getValue();
            } else if (e.getKey().equals(gen.getTypeUtils().iterable.getTypeParameters().get(1))) {
                //If it's Nothing, it's nonempty
                nonempty = "ceylon.language::Nothing".equals(e.getValue().getProducedTypeQualifiedName());
            }
        }
        if (elem == null) {
            gen.out("/*WARNING no Element found* /");
            elem = gen.getTypeUtils().anything.getType();
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
            gen.out(GenerateJsVisitor.getClAlias(), "getEmpty()");
        } else {
            final List<PositionalArgument> positionalArguments = sarg.getPositionalArguments();
            final boolean spread = SequenceGenerator.isSpread(positionalArguments);
            int lim = positionalArguments.size()-1;
            gen.out(GenerateJsVisitor.getClAlias(), "tpl$([");
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
            gen.out("],");
            if (that.getTypeModel().getProducedTypeQualifiedName().startsWith("ceylon.language::Tuple")) {
                TypeUtils.outputTypeList(that, that.getTypeModel(), gen, false);
            } else {
                //Let the function calculate it at runtime
                gen.out("undefined");
            }
            if (spread) {
                gen.out(",");
                positionalArguments.get(lim).visit(gen);
            }
            gen.out(")");
        }
    }

}
