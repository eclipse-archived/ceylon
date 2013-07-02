package com.redhat.ceylon.compiler.js;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Functional;
import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.TypeParameter;
import com.redhat.ceylon.compiler.typechecker.model.UnionType;
import com.redhat.ceylon.compiler.typechecker.model.Util;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;

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

    void generateInvocation(Tree.InvocationExpression that) {
        if (that.getNamedArgumentList()!=null) {
            Tree.NamedArgumentList argList = that.getNamedArgumentList();
            if (gen.isInDynamicBlock() && that.getPrimary() instanceof Tree.MemberOrTypeExpression && ((Tree.MemberOrTypeExpression)that.getPrimary()).getDeclaration() == null) {
                final String fname = names.createTempVariable();
                gen.out("(", fname, "=");
                //Call a native js constructor passing a native js object as parameter
                that.getPrimary().visit(gen);
                gen.out(",", fname, ".$$===undefined?new ", fname, "(");
                nativeObject(argList);
                gen.out("):", fname, "(");
                nativeObject(argList);
                gen.out("))");
            } else {
                gen.out("(");
                Map<String, String> argVarNames = defineNamedArguments(argList);
                that.getPrimary().visit(gen);
                Tree.TypeArguments targs = that.getPrimary() instanceof Tree.BaseMemberOrTypeExpression ?
                        ((Tree.BaseMemberOrTypeExpression)that.getPrimary()).getTypeArguments() : null;
                if (that.getPrimary() instanceof Tree.MemberOrTypeExpression) {
                    Tree.MemberOrTypeExpression mte = (Tree.MemberOrTypeExpression) that.getPrimary();
                    if (mte.getDeclaration() instanceof Functional) {
                        Functional f = (Functional) mte.getDeclaration();
                        applyNamedArguments(argList, f, argVarNames, gen.getSuperMemberScope(mte)!=null, targs);
                    }
                }
                gen.out(")");
            }
        }
        else {
            Tree.PositionalArgumentList argList = that.getPositionalArgumentList();
            Tree.Primary typeArgSource = that.getPrimary();
            //In nested invocation expressions, use the first invocation as source for type arguments
            while (typeArgSource instanceof Tree.InvocationExpression) {
                typeArgSource = ((Tree.InvocationExpression)typeArgSource).getPrimary();
            }
            Tree.TypeArguments targs = typeArgSource instanceof Tree.StaticMemberOrTypeExpression
                    ? ((Tree.StaticMemberOrTypeExpression)typeArgSource).getTypeArguments() : null;
            if (gen.isInDynamicBlock() && that.getPrimary() instanceof Tree.BaseTypeExpression
                    && ((Tree.BaseTypeExpression)that.getPrimary()).getDeclaration() == null) {
                gen.out("(");
                //Could be a dynamic object, or a Ceylon one
                //We might need to call "new" so we need to get all the args to pass directly later
                final List<String> argnames = generatePositionalArguments(
                        argList, argList.getPositionalArguments(), false, true);
                if (!argnames.isEmpty()) {
                    gen.out(",");
                }
                final String fname = names.createTempVariable();
                gen.out(fname,"=");
                that.getPrimary().visit(gen);
                String fuckingargs = "";
                if (!argnames.isEmpty()) {
                    fuckingargs = argnames.toString().substring(1);
                    fuckingargs = fuckingargs.substring(0, fuckingargs.length()-1);
                }
                gen.out(",", fname, ".$$===undefined?new ", fname, "(", fuckingargs, "):", fname, "(", fuckingargs, "))");
                //TODO we lose type args for now
                return;
            } else {
                that.getPrimary().visit(gen);
                if (gen.opts.isOptimize() && (gen.getSuperMemberScope(that.getPrimary()) != null)) {
                    gen.out(".call(this");
                    if (!argList.getPositionalArguments().isEmpty()) {
                        gen.out(",");
                    }
                } else {
                    gen.out("(");
                }
                generatePositionalArguments(argList, argList.getPositionalArguments(), false, false);
            }
            if (targs != null && targs.getTypeModels() != null && !targs.getTypeModels().isEmpty()) {
                if (argList.getPositionalArguments().size() > 0) {
                    gen.out(",");
                }
                Declaration bmed = ((Tree.StaticMemberOrTypeExpression)typeArgSource).getDeclaration();
                if (bmed instanceof Functional) {
                    if (((Functional) bmed).getParameterLists().get(0).getParameters().size() > argList.getPositionalArguments().size()
                            // has no comprehension
                            && (argList.getPositionalArguments().isEmpty()
                                || argList.getPositionalArguments().get(argList.getPositionalArguments().size()-1) instanceof Tree.Comprehension == false)) {
                        gen.out("undefined,");
                    }
                    if (targs != null && targs.getTypeModels() != null && !targs.getTypeModels().isEmpty()) {
                        Map<TypeParameter, ProducedType> invargs = TypeUtils.matchTypeParametersWithArguments(
                                ((Functional) bmed).getTypeParameters(), targs.getTypeModels());
                        if (invargs != null) {
                            TypeUtils.printTypeArguments(typeArgSource, invargs, gen);
                        } else {
                            gen.out("/*TARGS != TPARAMS!!!! WTF?????*/");
                        }
                    }
                }
            }
            gen.out(")");
        }
    }

    /** Generates the code to evaluate the expressions in the named argument list and assign them
     * to variables, then returns a map with the parameter names and the corresponding variable names. */
    Map<String, String> defineNamedArguments(Tree.NamedArgumentList argList) {
        Map<String, String> argVarNames = new HashMap<String, String>();
        for (Tree.NamedArgument arg: argList.getNamedArguments()) {
            com.redhat.ceylon.compiler.typechecker.model.Parameter p = arg.getParameter();
            final String paramName;
            if (p == null && gen.isInDynamicBlock()) {
                paramName = arg.getIdentifier().getText();
            } else {
                paramName = arg.getParameter().getName();
            }
            String varName = names.createTempVariable(paramName);
            argVarNames.put(paramName, varName);
            retainedVars.add(varName);
            gen.out(varName, "=");
            arg.visit(gen);
            gen.out(",");
        }
        Tree.SequencedArgument sarg = argList.getSequencedArgument();
        if (sarg!=null) {
            String paramName = sarg.getParameter().getName();
            String varName = names.createTempVariable(paramName);
            argVarNames.put(paramName, varName);
            retainedVars.add(varName);
            gen.out(varName, "=");
            generatePositionalArguments(argList, sarg.getPositionalArguments(), true, false);
            gen.out(",");
        }
        return argVarNames;
    }

    void applyNamedArguments(Tree.NamedArgumentList argList, Functional func,
                Map<String, String> argVarNames, boolean superAccess, Tree.TypeArguments targs) {
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

    /** Generate a list of PositionalArguments, optionally assigning a variable name to each one
     * and returning the variable names. */
    List<String> generatePositionalArguments(Tree.ArgumentList that, List<Tree.PositionalArgument> args,
            final boolean forceSequenced, final boolean generateVars) {
        if (!args.isEmpty()) {
            final List<String> argvars = new ArrayList<String>(args.size());
            boolean first=true;
            boolean opened=false;
            ProducedType sequencedType=null;
            for (Tree.PositionalArgument arg: args) {
                Tree.Expression expr;
                if (arg instanceof Tree.ListedArgument) {
                    if (!first) gen.out(",");
                    expr = ((Tree.ListedArgument) arg).getExpression();
                    ProducedType exprType = expr.getTypeModel();
                    boolean dyncheck = gen.isInDynamicBlock() && !TypeUtils.isUnknown(arg.getParameter()) && exprType.isUnknown();
                    if (forceSequenced || (arg.getParameter() != null && arg.getParameter().isSequenced())) {
                        if (dyncheck) {
                            //We don't have a real type so get the one declared in the parameter
                            exprType = arg.getParameter().getType();
                        }
                        if (sequencedType == null) {
                            sequencedType=exprType;
                        } else {
                            ArrayList<ProducedType> cases = new ArrayList<ProducedType>(2);
                            Util.addToUnion(cases, sequencedType);
                            Util.addToUnion(cases, exprType);
                            if (cases.size() > 1) {
                                UnionType ut = new UnionType(that.getUnit());
                                ut.setCaseTypes(cases);
                                sequencedType = ut.getType();
                            } else {
                                sequencedType = cases.get(0);
                            }
                        }
                        if (!opened) {
                            if (generateVars) {
                                final String argvar = names.createTempVariable();
                                argvars.add(argvar);
                                gen.out(argvar, "=");
                            }
                            gen.out("[");
                        }
                        opened=true;
                    } else if (generateVars) {
                        final String argvar = names.createTempVariable();
                        argvars.add(argvar);
                        gen.out(argvar, "=");
                    }
                    int boxType = gen.boxUnboxStart(expr.getTerm(), arg.getParameter());
                    if (dyncheck) {
                        TypeUtils.generateDynamicCheck(((Tree.ListedArgument) arg).getExpression(),
                                arg.getParameter().getType(), gen);
                    } else {
                        arg.visit(gen);
                    }
                    if (boxType == 4) {
                        gen.out(",");
                        //Add parameters
                        describeMethodParameters(expr.getTerm());
                        gen.out(",");
                        TypeUtils.printTypeArguments(arg, arg.getTypeModel().getTypeArguments(), gen);
                    }
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
                        if (boxType == 4) {
                            gen.out(",");
                            describeMethodParameters(expr.getTerm());
                            gen.out(",");
                            TypeUtils.printTypeArguments(arg, arg.getTypeModel().getTypeArguments(), gen);
                        }
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
            return argvars;
        }
        return Collections.emptyList();
    }

    /** Generate the code to create a native js object. */
    void nativeObject(Tree.NamedArgumentList argList) {
        if (argList.getSequencedArgument() == null) {
            gen.out("{");
            boolean first = true;
            for (Tree.NamedArgument arg : argList.getNamedArguments()) {
                if (first) { first = false; } else { gen.out(","); }
                gen.out(arg.getIdentifier().getText(), ":");
                arg.visit(gen);
            }
            gen.out("}");
        } else {
            String arr = null;
            if (argList.getNamedArguments().size() > 0) {
                gen.out("function()");
                gen.beginBlock();
                arr = names.createTempVariable();
                gen.out("var ", arr, "=");
            }
            gen.out("[");
            boolean first = true;
            for (Tree.PositionalArgument arg : argList.getSequencedArgument().getPositionalArguments()) {
                if (first) { first = false; } else { gen.out(","); }
                arg.visit(gen);
            }
            gen.out("]");
            if (argList.getNamedArguments().size() > 0) {
                gen.endLine(true);
                for (Tree.NamedArgument arg : argList.getNamedArguments()) {
                    gen.out(arr, ".", arg.getIdentifier().getText(), "=");
                    arg.visit(gen);
                    gen.endLine(true);
                }
                gen.out("return ", arr, ";");
                gen.endBlock();
                gen.out("()");
            }
        }
    }

    private void describeMethodParameters(Tree.Term term) {
        Method _m = null;
        if (term instanceof Tree.FunctionArgument) {
            _m = (((Tree.FunctionArgument)term).getDeclarationModel());
        } else if (term instanceof Tree.MemberOrTypeExpression) {
            if (((Tree.MemberOrTypeExpression)term).getDeclaration() instanceof Method) {
                _m = (Method)((Tree.MemberOrTypeExpression)term).getDeclaration();
            }
        } else if (term instanceof Tree.InvocationExpression) {
            gen.getTypeUtils().encodeTupleAsParameterListForRuntime(term.getTypeModel(), gen);
            return;
        } else {
            gen.out("/*Callable EXPR of type ", term.getClass().getName(), "*/");
        }
        if (_m == null) {
            gen.out("[]");
        } else {
            TypeUtils.encodeParameterListForRuntime(_m.getParameterLists().get(0), gen);
        }
    }

}
