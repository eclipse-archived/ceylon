package com.redhat.ceylon.compiler.js;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.redhat.ceylon.compiler.typechecker.model.Functional;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.TypeParameter;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ArgumentList;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.NamedArgument;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.NamedArgumentList;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.PositionalArgument;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.SequencedArgument;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.TypeArguments;

/** Generates js code for invocation expression (named and positional). */
public class InvocationGenerator {

    private final GenerateJsVisitor gen;
    private final JsIdentifierNames names;
    private final RetainedVars retainedVars;

    InvocationGenerator(GenerateJsVisitor owner, JsIdentifierNames names, RetainedVars rv) {
        gen = owner;
        this.names = names;
        retainedVars = rv;
    }

    Map<String, String> defineNamedArguments(NamedArgumentList argList) {
        Map<String, String> argVarNames = new HashMap<String, String>();
        for (NamedArgument arg: argList.getNamedArguments()) {
            String paramName = arg.getParameter().getName();
            String varName = names.createTempVariable(paramName);
            argVarNames.put(paramName, varName);
            retainedVars.add(varName);
            gen.out(varName, "=");
            arg.visit(gen);
            gen.out(",");
        }
        SequencedArgument sarg = argList.getSequencedArgument();
        if (sarg!=null) {
            String paramName = sarg.getParameter().getName();
            String varName = names.createTempVariable(paramName);
            argVarNames.put(paramName, varName);
            retainedVars.add(varName);
            gen.out(varName, "=");
            generatePositionalArguments(argList, sarg.getPositionalArguments(), true);
            gen.out(",");
        }
        return argVarNames;
    }

    void applyNamedArguments(NamedArgumentList argList, Functional func,
                Map<String, String> argVarNames, boolean superAccess, TypeArguments targs) {
        boolean firstList = true;
        for (com.redhat.ceylon.compiler.typechecker.model.ParameterList plist : func.getParameterLists()) {
            List<String> argNames = argList.getNamedArgumentList().getArgumentNames();
            boolean first=true;
            if (firstList && superAccess) {
                gen.out(".call(this");
                if (!plist.getParameters().isEmpty()) { gen.out(","); }
            }
            else {
                gen.out("(");
            }
            for (com.redhat.ceylon.compiler.typechecker.model.Parameter p : plist.getParameters()) {
                if (!first) gen.out(",");
                boolean namedArgumentGiven = argNames.contains(p.getName());
                if (namedArgumentGiven) {
                    gen.out(argVarNames.get(p.getName()));
                } else if (p.isSequenced()) {
                    gen.out(GenerateJsVisitor.getClAlias(), "getEmpty()");
                } else if (argList.getSequencedArgument()!=null) {
                    String pname = argVarNames.get(p.getName());
                    gen.out(pname==null ? "undefined" : pname);
                } else if (p.isDefaulted()) {
                    gen.out("undefined");
                } else {
                    //It's an empty Iterable
                    gen.out(GenerateJsVisitor.getClAlias(), "getEmpty()");
                }
                first = false;
            }
            if (targs != null && !targs.getTypeModels().isEmpty()) {
                Map<TypeParameter, ProducedType> invargs = TypeUtils.matchTypeParametersWithArguments(
                        func.getTypeParameters(), targs.getTypeModels());
                if (!first) gen.out(",");
                TypeUtils.printTypeArguments(argList, invargs, gen);
            }
            gen.out(")");
            firstList = false;
        }
    }

    void generatePositionalArguments(ArgumentList that, List<PositionalArgument> args, final boolean forceSequenced) {
        if (!args.isEmpty()) {
            boolean first=true;
            boolean opened=false;
            ProducedType sequencedType=null;
            for (PositionalArgument arg: args) {
                Tree.Expression expr;
                if(arg instanceof Tree.ListedArgument) {
                    if (!first) gen.out(",");
                    expr = ((Tree.ListedArgument) arg).getExpression();
                    if (forceSequenced || (arg.getParameter() != null && arg.getParameter().isSequenced())) {
                        if (sequencedType == null) {
                            sequencedType=expr.getTypeModel();
                        } else {
                            //TODO union with prev
                        }
                        if (!opened) gen.out("[");
                        opened=true;
                    }
                    int boxType = gen.boxUnboxStart(expr.getTerm(), arg.getParameter());
                    arg.visit(gen);
                    gen.boxUnboxEnd(boxType);
                } else if (arg instanceof Tree.SpreadArgument || arg instanceof Tree.Comprehension) {
                    if (arg instanceof Tree.SpreadArgument) {
                        expr = ((Tree.SpreadArgument) arg).getExpression();
                    } else {
                        expr = null;
                    }
                    if (!first) {
                        gen.closeSequenceWithReifiedType(that,
                                gen.getTypeUtils().wrapAsIterableArguments(sequencedType));
                        gen.out(".chain(");
                        sequencedType=null;
                    }
                    if (arg instanceof Tree.SpreadArgument) {
                        int boxType = gen.boxUnboxStart(expr.getTerm(), arg.getParameter());
                        arg.visit(gen);
                        gen.boxUnboxEnd(boxType);
                    } else {
                        ((Tree.Comprehension)arg).visit(gen);
                    }
                    if (!first) {
                        gen.out(",");
                        if (expr == null) {
                            //it's a comprehension
                            TypeUtils.printTypeArguments(that,
                                    gen.getTypeUtils().wrapAsIterableArguments(arg.getTypeModel()), gen);
                        } else {
                            ProducedType spreadType = TypeUtils.findSupertype(gen.getTypeUtils().sequential, expr.getTypeModel());
                            TypeUtils.printTypeArguments(that, spreadType.getTypeArguments(), gen);
                        }
                        gen.out(")");
                    }
                    if (arg instanceof Tree.Comprehension) {
                        break;
                    }
                }
                first = false;
            }
            if (sequencedType != null) {
                gen.closeSequenceWithReifiedType(that,
                        gen.getTypeUtils().wrapAsIterableArguments(sequencedType));
            }
        }
    }

}
