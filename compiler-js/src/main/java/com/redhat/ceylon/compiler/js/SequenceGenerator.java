package com.redhat.ceylon.compiler.js;

import java.util.List;
import java.util.Map;

import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.TypeParameter;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;

public class SequenceGenerator {

    static void sequenceEnumeration(final Tree.SequenceEnumeration that, final GenerateJsVisitor gen) {
        final Tree.SequencedArgument sarg = that.getSequencedArgument();
        if (sarg == null) {
            gen.out(GenerateJsVisitor.getClAlias(), "getEmpty()");
        } else {
            final List<Tree.PositionalArgument> positionalArguments = sarg.getPositionalArguments();
            final int lim = positionalArguments.size()-1;
            final boolean spread = isSpread(positionalArguments);
            ProducedType chainedType = null;
            if (spread) {
                if (lim>0) {
                    gen.out("[");
                }
            } else {
                gen.out("[");
            }
            int count=0;
            for (Tree.PositionalArgument expr : positionalArguments) {
                if (count==lim && spread) {
                    if (lim > 0) {
                        ProducedType seqType = TypeUtils.findSupertype(gen.getTypeUtils().iterable, that.getTypeModel());
                        closeSequenceWithReifiedType(that, seqType.getTypeArguments(), gen);
                        gen.out(".chain(");
                        chainedType = TypeUtils.findSupertype(gen.getTypeUtils().iterable, expr.getTypeModel());
                    }
                    count--;
                } else {
                    if (count > 0) {
                        gen.out(",");
                    }
                }
                if (gen.isInDynamicBlock() && expr instanceof Tree.ListedArgument && TypeUtils.isUnknown(expr.getTypeModel())) {
                    TypeUtils.generateDynamicCheck(((Tree.ListedArgument)expr).getExpression(), gen.getTypeUtils().anything.getType(), gen, false);
                } else {
                    expr.visit(gen);
                }
                count++;
            }
            if (chainedType == null) {
                if (!spread) {
                    closeSequenceWithReifiedType(that, that.getTypeModel().getTypeArguments(), gen);
                }
            } else {
                gen.out(",");
                TypeUtils.printTypeArguments(that, chainedType.getTypeArguments(), gen, false);
                gen.out(")");
            }
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

    /** Closes a native array and invokes reifyCeylonType with the specified type parameters. */
    static void closeSequenceWithReifiedType(final Node that, final Map<TypeParameter,ProducedType> types,
            final GenerateJsVisitor gen) {
        gen.out("].reifyCeylonType(");
        TypeUtils.printTypeArguments(that, types, gen, false);
        gen.out(")");
    }

}
