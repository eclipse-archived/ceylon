package com.redhat.ceylon.compiler.js;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.redhat.ceylon.compiler.js.util.JsIdentifierNames;
import com.redhat.ceylon.compiler.js.util.RetainedVars;
import com.redhat.ceylon.compiler.js.util.TypeUtils;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.model.typechecker.model.Class;
import com.redhat.ceylon.model.typechecker.model.Constructor;
import com.redhat.ceylon.model.typechecker.model.Declaration;
import com.redhat.ceylon.model.typechecker.model.Function;
import com.redhat.ceylon.model.typechecker.model.Functional;
import com.redhat.ceylon.model.typechecker.model.Interface;
import com.redhat.ceylon.model.typechecker.model.ModelUtil;
import com.redhat.ceylon.model.typechecker.model.Parameter;
import com.redhat.ceylon.model.typechecker.model.ParameterList;
import com.redhat.ceylon.model.typechecker.model.SiteVariance;
import com.redhat.ceylon.model.typechecker.model.Type;
import com.redhat.ceylon.model.typechecker.model.TypeParameter;
import com.redhat.ceylon.model.typechecker.model.TypedDeclaration;
import com.redhat.ceylon.model.typechecker.model.UnionType;
import com.redhat.ceylon.model.typechecker.model.Value;

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

    private Map<TypeParameter,Type> getTypeArguments(Tree.Primary typeArgSource) {
        if (typeArgSource instanceof Tree.StaticMemberOrTypeExpression) {
            Tree.StaticMemberOrTypeExpression smote = (Tree.StaticMemberOrTypeExpression) typeArgSource;
            if (smote.getDeclaration() instanceof Constructor &&
                    !((Functional)smote.getDeclaration().getContainer()).getTypeParameters().isEmpty()) {
                return smote.getTarget().getTypeArguments();
            } else if (smote.getDeclaration() instanceof Functional) {
                Map<TypeParameter,Type> targs = TypeUtils.matchTypeParametersWithArguments(
                        ((Functional)smote.getDeclaration()).getTypeParameters(),
                        smote.getTypeArguments() == null ? null :
                        smote.getTypeArguments().getTypeModels());
                if (targs == null) {
                    gen.out("/*TARGS != TPARAMS!!!! WTF?????*/");
                }
                return targs;
            }
        }
        return null;
    }

    public void generateInvocation(Tree.InvocationExpression that) {
        final Tree.Primary typeArgSource = that.getPrimary();
        if (that.getNamedArgumentList()!=null) {
            Tree.NamedArgumentList argList = that.getNamedArgumentList();
            if (gen.isInDynamicBlock() && typeArgSource instanceof Tree.MemberOrTypeExpression
                    && ((Tree.MemberOrTypeExpression)typeArgSource).getDeclaration() == null) {
                final String fname = names.createTempVariable();
                gen.out("(", fname, "=");
                //Call a native js constructor passing a native js object as parameter
                if (typeArgSource instanceof Tree.QualifiedTypeExpression) {
                    BmeGenerator.generateQte((Tree.QualifiedTypeExpression)typeArgSource, gen, true);
                } else {
                    typeArgSource.visit(gen);
                }
                gen.out(",", fname, ".$$===undefined?new ", fname, "(");
                nativeObject(argList);
                gen.out("):", fname, "(");
                nativeObject(argList);
                gen.out("))");
            } else {
                gen.out("(");
                Map<String, String> argVarNames = defineNamedArguments(typeArgSource, argList);
                if (typeArgSource instanceof Tree.BaseMemberExpression) {
                    BmeGenerator.generateBme((Tree.BaseMemberExpression)typeArgSource, gen, true);
                } else if (typeArgSource instanceof Tree.QualifiedTypeExpression) {
                    BmeGenerator.generateQte((Tree.QualifiedTypeExpression)typeArgSource, gen, true);
                } else {
                    typeArgSource.visit(gen);
                }
                if (typeArgSource instanceof Tree.MemberOrTypeExpression) {
                    Tree.MemberOrTypeExpression mte = (Tree.MemberOrTypeExpression) typeArgSource;
                    if (mte.getDeclaration() instanceof Functional) {
                        Functional f = (Functional) mte.getDeclaration();
                        Map<TypeParameter, Type> targs = getTypeArguments(typeArgSource);
                        applyNamedArguments(argList, f, argVarNames, gen.getSuperMemberScope(mte)!=null, targs);
                    }
                }
                gen.out(")");
            }
        }
        else {
            final Tree.PositionalArgumentList argList = that.getPositionalArgumentList();
            final Map<TypeParameter, Type> targs = getTypeArguments(typeArgSource);
            if (gen.isInDynamicBlock() && typeArgSource instanceof Tree.BaseTypeExpression
                    && ((Tree.BaseTypeExpression)typeArgSource).getDeclaration() == null) {
                gen.out("(");
                //Could be a dynamic object, or a Ceylon one
                //We might need to call "new" so we need to get all the args to pass directly later
                final List<String> argnames = generatePositionalArguments(typeArgSource,
                        argList, argList.getPositionalArguments(), false, true);
                if (!argnames.isEmpty()) {
                    gen.out(",");
                }
                final String fname = names.createTempVariable();
                gen.out(fname,"=");
                if (typeArgSource instanceof Tree.QualifiedTypeExpression) {
                    BmeGenerator.generateQte((Tree.QualifiedTypeExpression)typeArgSource, gen, true);
                } else {
                    typeArgSource.visit(gen);
                }
                String fuckingargs = "";
                if (!argnames.isEmpty()) {
                    fuckingargs = argnames.toString().substring(1);
                    fuckingargs = fuckingargs.substring(0, fuckingargs.length()-1);
                }
                gen.out(",", fname, ".$$===undefined?new ", fname, "(", fuckingargs, "):", fname, "(", fuckingargs, "))");
                //TODO we lose type args for now
                return;
            } else {
                if (typeArgSource instanceof Tree.BaseMemberExpression) {
                    final Tree.BaseMemberExpression _bme = (Tree.BaseMemberExpression)typeArgSource;
                    if (gen.isInDynamicBlock() && _bme.getDeclaration() != null &&
                            "ceylon.language::print".equals(_bme.getDeclaration().getQualifiedNameString())) {
                        Tree.PositionalArgument printArg =  that.getPositionalArgumentList().getPositionalArguments().get(0);
                        if (ModelUtil.isTypeUnknown(printArg.getTypeModel())) {
                            gen.out(gen.getClAlias(), "pndo$(/*DYNAMIC arg*/"); //#397
                            printArg.visit(gen);
                            gen.out(")");
                            return;
                        }
                    }
                    BmeGenerator.generateBme(_bme, gen, true);
                } else if (typeArgSource instanceof Tree.QualifiedTypeExpression) {
                    BmeGenerator.generateQte((Tree.QualifiedTypeExpression)typeArgSource, gen, true);
                } else {
                    typeArgSource.visit(gen);
                }

                if (gen.opts.isOptimize() && (gen.getSuperMemberScope(typeArgSource) != null)) {
                    gen.out(".call(", names.self(ModelUtil.getContainingClassOrInterface(typeArgSource.getScope())));
                    if (!argList.getPositionalArguments().isEmpty()) {
                        gen.out(",");
                    }
                } else {
                    gen.out("(");
                }
                //Check if args have params
                boolean fillInParams = !argList.getPositionalArguments().isEmpty();
                for (Tree.PositionalArgument arg : argList.getPositionalArguments()) {
                    fillInParams &= arg.getParameter() == null;
                }
                if (fillInParams) {
                    //Get the callable and try to assign params from there
                    Interface cd = that.getUnit().getCallableDeclaration();
                    final Type ed = that.getUnit().getEmptyType();
                    Class td = that.getUnit().getTupleDeclaration();
                    Type callable = typeArgSource.getTypeModel().getSupertype(cd);
                    if (callable != null) {
                        //This is a tuple with the arguments to the callable
                        //(can be union with empty if first param is defaulted)
                        Type callableArgs = callable.getTypeArgumentList().get(1);
                        boolean isUnion=false;
                        if (callableArgs.isUnion()) {
                            if (callableArgs.getCaseTypes().size() == 2) {
                                callableArgs = callableArgs.minus(ed);
                            }
                            isUnion=callableArgs.isUnion();
                        }
                        //This is the type of the first argument
                        boolean isSequenced = !(isUnion || td.equals(
                                callableArgs.getDeclaration()));
                        Type argtype = isUnion ? callableArgs :
                            callableArgs.isTypeParameter() ? callableArgs :
                            callableArgs.getTypeArgumentList().get(
                                isSequenced ? 0 : 1);
                        Parameter p = null;
                        int c = 0;
                        for (Tree.PositionalArgument arg : argList.getPositionalArguments()) {
                            if (p == null) {
                                p = new Parameter();
                                p.setName("arg"+c);
                                p.setDeclaration(typeArgSource.getTypeModel().getDeclaration());
                                Value v = new Value();
                                v.setContainer(that.getPositionalArgumentList().getScope());
                                v.setType(argtype);
                                p.setModel(v);
                                if (callableArgs == null || isSequenced) {
                                    p.setSequenced(true);
                                } else if (!isSequenced) {
                                    Type next = isUnion ? null : callableArgs.getTypeArgumentList().get(2);
                                    if (next != null && next.getSupertype(td) == null) {
                                        //It's not a tuple, so no more regular parms. It can be:
                                        //empty|tuple if defaulted params
                                        //empty if no more params
                                        //sequential if sequenced param
                                        if (next.isUnion()) {
                                            //empty|tuple
                                            callableArgs = next.minus(ed);
                                            isSequenced = !td.equals(callableArgs.getDeclaration());
                                            argtype = callableArgs.getTypeArgumentList().get(isSequenced ? 0 : 1);
                                        } else {
                                            //we'll bet on sequential (if it's empty we don't care anyway)
                                            argtype = next;
                                            callableArgs = null;
                                        }
                                    } else {
                                        //If it's a tuple then there are more params
                                        callableArgs = next;
                                        argtype = callableArgs == null ? null : callableArgs.getTypeArgumentList().get(1);
                                    }
                                }
                            }
                            arg.setParameter(p);
                            c++;
                            if (!p.isSequenced()) {
                                p = null;
                            }
                        }
                    }
                }
                generatePositionalArguments(typeArgSource, argList, argList.getPositionalArguments(), false, false);
            }
            if (targs != null && !targs.isEmpty()
                    && typeArgSource instanceof Tree.StaticMemberOrTypeExpression
                    && ((Tree.StaticMemberOrTypeExpression)typeArgSource).getDeclaration() instanceof Functional) {
                if (argList.getPositionalArguments().size() > 0) {
                    gen.out(",");
                }
                Functional bmed = (Functional)((Tree.StaticMemberOrTypeExpression)typeArgSource).getDeclaration();
                //If there are fewer arguments than there are parameters...
                final int argsSize = argList.getPositionalArguments().size();
                int paramArgDiff = ((Functional) bmed).getFirstParameterList().getParameters().size() - argsSize;
                if (paramArgDiff > 0) {
                    final Tree.PositionalArgument parg = argsSize > 0 ? argList.getPositionalArguments().get(argsSize-1) : null;
                    if (parg instanceof Tree.Comprehension || parg instanceof Tree.SpreadArgument) {
                        paramArgDiff--;
                    }
                    for (int i=0; i < paramArgDiff; i++) {
                        gen.out("undefined,");
                    }
                }
                if (targs != null && !targs.isEmpty()) {
                    TypeUtils.printTypeArguments(typeArgSource, targs, gen, false, null);
                }
            }
            gen.out(")");
        }
    }

    /** Generates the code to evaluate the expressions in the named argument list and assign them
     * to variables, then returns a map with the parameter names and the corresponding variable names. */
    Map<String, String> defineNamedArguments(Tree.Primary primary, Tree.NamedArgumentList argList) {
        Map<String, String> argVarNames = new HashMap<String, String>();
        for (Tree.NamedArgument arg: argList.getNamedArguments()) {
            Parameter p = arg.getParameter();
            final String paramName;
            if (p == null && gen.isInDynamicBlock()) {
                paramName = arg.getIdentifier().getText();
            } else {
                paramName = arg.getParameter().getName();
            }
            String varName = names.createTempVariable();
            argVarNames.put(paramName, varName);
            retainedVars.add(varName);
            gen.out(varName, "=");
            if (arg instanceof Tree.MethodArgument) {
                Tree.MethodArgument marg = (Tree.MethodArgument)arg;
                gen.out(gen.getClAlias(), "$JsCallable(");
                FunctionHelper.methodArgument(marg, gen);
                gen.out(",");
                //Add parameters
                TypeUtils.encodeParameterListForRuntime(true, arg, marg.getParameterLists().get(0).getModel(), gen);
                gen.out(",");
                Type margType = marg.getDeclarationModel().getType().getFullType();
                TypeUtils.printTypeArguments(arg, margType.getTypeArguments(), gen, false,
                        margType.getVarianceOverrides());
                gen.boxUnboxEnd(4);
            } else {
                arg.visit(gen);
            }
            gen.out(",");
        }
        Tree.SequencedArgument sarg = argList.getSequencedArgument();
        if (sarg!=null) {
            String paramName = sarg.getParameter().getName();
            String varName = names.createTempVariable();
            argVarNames.put(paramName, varName);
            retainedVars.add(varName);
            gen.out(varName, "=");
            generatePositionalArguments(primary, argList, sarg.getPositionalArguments(), true, false);
            gen.out(",");
        }
        return argVarNames;
    }

    void applyNamedArguments(Tree.NamedArgumentList argList, Functional func,
                Map<String, String> argVarNames, boolean superAccess, Map<TypeParameter, Type> targs) {
        final ParameterList plist = func.getFirstParameterList();
        boolean first=true;
        if (superAccess) {
            gen.out(".call(this");
            if (!plist.getParameters().isEmpty()) { gen.out(","); }
        }
        else {
            gen.out("(");
        }
        for (Parameter p : plist.getParameters()) {
            if (!first) gen.out(",");
            final String vname = argVarNames.get(p.getName());
            if (vname == null) {
                if (p.isDefaulted()) {
                    gen.out("undefined");
                } else {
                    gen.out(gen.getClAlias(), "empty()");
                }
            } else {
                gen.out(vname);
            }
            first = false;
        }
        if (targs != null && !targs.isEmpty()) {
            if (!first){
                gen.out(",");
            }
            TypeUtils.printTypeArguments(argList, targs, gen, false, null);
        }
        gen.out(")");
    }

    /** Generate a list of PositionalArguments, optionally assigning a variable name to each one
     * and returning the variable names. */
    List<String> generatePositionalArguments(final Tree.Primary primary, final Tree.ArgumentList that,
            final List<Tree.PositionalArgument> args,
            final boolean forceSequenced, final boolean generateVars) {
        if (args.isEmpty()) {
            return Collections.emptyList();
        }
        final List<String> argvars = new ArrayList<String>(args.size());
        boolean first=true;
        boolean opened=false;
        Type sequencedType=null;
        for (Tree.PositionalArgument arg : args) {
            Tree.Expression expr;
            final Parameter pd = arg.getParameter();
            if (arg instanceof Tree.ListedArgument) {
                if (!first) gen.out(",");
                expr = ((Tree.ListedArgument) arg).getExpression();
                Type exprType = expr.getTypeModel();
                boolean dyncheck = gen.isInDynamicBlock() && pd != null && !ModelUtil.isTypeUnknown(pd.getType())
                        && exprType.containsUnknowns();
                if (forceSequenced || (pd != null && pd.isSequenced())) {
                    if (dyncheck) {
                        //We don't have a real type so get the one declared in the parameter
                        exprType = pd.getType();
                    }
                    if (sequencedType == null) {
                        sequencedType=exprType;
                    } else {
                        ArrayList<Type> cases = new ArrayList<Type>(2);
                        ModelUtil.addToUnion(cases, sequencedType);
                        ModelUtil.addToUnion(cases, exprType);
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
                final int boxType = pd==null?0:gen.boxUnboxStart(expr.getTerm(), pd.getModel());
                if (dyncheck) {
                    Map<TypeParameter,Type> targs = null;
                    if (primary instanceof Tree.MemberOrTypeExpression) {
                        targs = ((Tree.MemberOrTypeExpression)primary).getTarget().getTypeArguments();
                    }
                    TypeUtils.generateDynamicCheck(expr, pd.getType(), gen, false, targs);
                } else {
                    arg.visit(gen);
                }
                if (boxType == 4) {
                    gen.out(",");
                    //Add parameters
                    describeMethodParameters(expr.getTerm());
                    gen.out(",");
                    TypeUtils.printTypeArguments(arg, arg.getTypeModel().getTypeArguments(), gen, false,
                            arg.getTypeModel().getVarianceOverrides());
                }
                gen.boxUnboxEnd(boxType);
            } else if (arg instanceof Tree.SpreadArgument || arg instanceof Tree.Comprehension) {
                if (arg instanceof Tree.SpreadArgument) {
                    expr = ((Tree.SpreadArgument) arg).getExpression();
                } else {
                    expr = null;
                }
                boolean chained=false;
                if (opened) {
                    SequenceGenerator.closeSequenceWithReifiedType(that,
                            TypeUtils.wrapAsIterableArguments(sequencedType), gen);
                    gen.out(".chain(");
                    sequencedType=null;
                    chained=true;
                } else if (!first) {
                    gen.out(",");
                }
                if (arg instanceof Tree.SpreadArgument) {
                    TypedDeclaration td = pd == null ? null : pd.getModel();
                    int boxType = gen.boxUnboxStart(expr.getTerm(), td);
                    if (boxType == 4) {
                        arg.visit(gen);
                        gen.out(",");
                        describeMethodParameters(expr.getTerm());
                        gen.out(",");
                        TypeUtils.printTypeArguments(arg, arg.getTypeModel().getTypeArguments(), gen, false,
                                arg.getTypeModel().getVarianceOverrides());
                    } else if (pd == null) {
                        if (gen.isInDynamicBlock() && primary instanceof Tree.MemberOrTypeExpression
                                && ((Tree.MemberOrTypeExpression)primary).getDeclaration() == null
                                && arg.getTypeModel() != null && arg.getTypeModel().getDeclaration().inherits((
                                        that.getUnit().getTupleDeclaration()))) {
                            //Spread dynamic parameter
                            Type tupleType = arg.getTypeModel();
                            Type targ = tupleType.getTypeArgumentList().get(2);
                            arg.visit(gen);
                            gen.out(".$_get(0)");
                            int i = 1;
                            while (!targ.isSubtypeOf(that.getUnit().getEmptyType())) {
                                gen.out(",");
                                arg.visit(gen);
                                gen.out(".$_get("+(i++)+")");
                                targ = targ.getTypeArgumentList().get(2);
                            }
                        } else {
                            arg.visit(gen);
                        }
                    } else if (pd.isSequenced()) {
                        arg.visit(gen);
                    } else {
                        final String specialSpreadVar = gen.getNames().createTempVariable();
                        gen.out("(", specialSpreadVar, "=");
                        args.get(args.size()-1).visit(gen);
                        gen.out(".sequence(),");
                        if (pd.isDefaulted()) {
                            gen.out(gen.getClAlias(), "nn$(",
                                    specialSpreadVar, ".$_get(0))?", specialSpreadVar,
                                    ".$_get(0):undefined)");
                        } else {
                            gen.out(specialSpreadVar, ".$_get(0))");
                        }
                        //Find out if there are more params
                        final List<Parameter> moreParams;
                        final Declaration pdd = pd.getDeclaration();
                        boolean found = false;
                        if (pdd instanceof Function) {
                            moreParams = ((Function)pdd).getFirstParameterList().getParameters();
                        } else if (pdd instanceof Class) {
                            moreParams = ((Class)pdd).getParameterList().getParameters();
                        } else {
                            //Check the parameters of the primary (obviously a callable, so this is a Tuple)
                            List<Parameter> cparms = TypeUtils.convertTupleToParameters(
                                    primary.getTypeModel().getTypeArgumentList().get(1));
                            cparms.remove(0);
                            moreParams = cparms;
                            found = true;
                        }
                        if (moreParams != null) {
                            int c = 1;
                            for (Parameter restp : moreParams) {
                                if (found) {
                                    final String cs=Integer.toString(c++);
                                    if (restp.isDefaulted()) {
                                        gen.out(",", gen.getClAlias(), "nn$(", specialSpreadVar,
                                                ".$_get(", cs, "))?", specialSpreadVar, ".$_get(", cs, "):undefined");
                                    } else {
                                        gen.out(",", specialSpreadVar, ".$_get(", cs, ")");
                                    }
                                } else {
                                    found = restp.equals(pd);
                                }
                            }
                        }
                    }
                    gen.boxUnboxEnd(boxType);
                } else {
                    ((Tree.Comprehension)arg).visit(gen);
                }
                if (opened) {
                    gen.out(",");
                    Map<TypeParameter,Type> _targs;
                    Map<TypeParameter, SiteVariance> _vo;
                    if (expr == null) {
                        //it's a comprehension
                        _targs = TypeUtils.wrapAsIterableArguments(arg.getTypeModel());
                        _vo = null;
                    } else {
                        Type spreadType = TypeUtils.findSupertype(
                                that.getUnit().getSequentialDeclaration(),
                                expr.getTypeModel());
                        if (spreadType == null) {
                            //Go directly to Iterable
                            spreadType = TypeUtils.findSupertype(that.getUnit().getIterableDeclaration(),
                                    expr.getTypeModel());
                        }
                        _targs = spreadType.getTypeArguments();
                        _vo = spreadType.getVarianceOverrides();
                    }
                    if (chained) {
                        Type[] _tlist = new Type[2];
                        for (TypeParameter tp : _targs.keySet()) {
                            if ("Element".equals(tp.getName())) {
                                _tlist[0] = _targs.get(tp);
                            } else if ("Absent".equals(tp.getName())) {
                                _tlist[1] = _targs.get(tp);
                            }
                        }
                        if (_tlist[1] == null) {
                            _tlist[1] = that.getUnit().getNothingType();
                        }
                        Function cdec = (Function)that.getUnit().getIterableDeclaration().getMember("chain", null, false);
                        _targs = TypeUtils.matchTypeParametersWithArguments(cdec.getTypeParameters(), Arrays.asList(_tlist));
                    }
                    TypeUtils.printTypeArguments(that, _targs, gen, false, _vo);
                    gen.out(")");
                }
                if (arg instanceof Tree.Comprehension) {
                    break;
                }
            }
            first = false;
        }
        if (sequencedType != null) {
            final Map<TypeParameter,Type> seqtargs;
            if (forceSequenced && args.size() > 0) {
                seqtargs = that.getUnit().getNonemptyIterableType(sequencedType).getTypeArguments();
            } else {
                seqtargs = TypeUtils.wrapAsIterableArguments(sequencedType);
            }
            SequenceGenerator.closeSequenceWithReifiedType(primary,
                    seqtargs, gen);
        }
        return argvars;
    }

    /** Generate the code to create a native js object. */
    void nativeObject(Tree.NamedArgumentList argList) {
        final List<Tree.NamedArgument> nargs = argList.getNamedArguments();
        if (argList.getSequencedArgument() == null) {
            gen.out("{");
            boolean first = true;
            for (Tree.NamedArgument arg : nargs) {
                if (first) { first = false; } else { gen.out(","); }
                String argName = arg.getIdentifier().getText();
                if (JsIdentifierNames.isReservedWord(argName)) {
                    gen.out("$_");
                }
                gen.out(argName, ":");
                arg.visit(gen);
            }
            gen.out("}");
        } else {
            String arr = null;
            boolean isComp = false;
            boolean isSpread = argList.getSequencedArgument() != null &&
                    !argList.getSequencedArgument().getPositionalArguments().isEmpty() &&
                    argList.getSequencedArgument().getPositionalArguments().get(
                            argList.getSequencedArgument().getPositionalArguments().size() -1) instanceof Tree.SpreadArgument;
            if (nargs.size() > 0) {
                gen.out("function()");
                gen.beginBlock();
                arr = names.createTempVariable();
                gen.out("var ", arr, "=");
            } else {
                isComp = argList.getSequencedArgument().getPositionalArguments().size() == 1
                        && argList.getSequencedArgument().getPositionalArguments().get(0) instanceof Tree.Comprehension;
            }
            if (isComp) {
                gen.out(gen.getClAlias(), "nfor$(");
            } else if (isSpread) {
                gen.out(gen.getClAlias(), "tpl$([");
            } else {
                gen.out("[");
            }
            boolean first = true;
            for (Tree.PositionalArgument arg : argList.getSequencedArgument().getPositionalArguments()) {
                if (arg instanceof Tree.SpreadArgument) {
                    gen.out("],undefined,");
                    arg.visit(gen);
                    gen.out(").nativeArray()");
                } else {
                    if (first) { first = false; } else { gen.out(","); }
                    arg.visit(gen);
                }
            }
            if (isComp) {
                gen.out(")");
            } else if (!isSpread) {
                gen.out("]");
            }
            if (nargs.size() > 0) {
                gen.endLine(true);
                for (Tree.NamedArgument arg : nargs) {
                    gen.out(arr, ".", arg.getIdentifier().getText(), "=");
                    arg.visit(gen);
                    gen.endLine(true);
                }
                gen.out("return ", arr,";");
                gen.endBlock();
                gen.out("()");
            }
        }
    }

    void describeMethodParameters(Tree.Term term) {
        ParameterList plist = null;
        if (term instanceof Tree.FunctionArgument) {
            plist = ((Function)(((Tree.FunctionArgument)term).getDeclarationModel())).getFirstParameterList();
        } else if (term instanceof Tree.MemberOrTypeExpression) {
            Tree.MemberOrTypeExpression mote = (Tree.MemberOrTypeExpression)term;
            if (mote.getStaticMethodReference()) {
                plist = new ParameterList();
                Parameter param = new Parameter();
                plist.getParameters().add(param);
                final Value pm = new Value();
                pm.setType(mote.getTarget().getQualifyingType());
                param.setModel(pm);
                param.setName("_0");
            } else if (mote.getDeclaration() instanceof Function) {
                plist = ((Function)((Tree.MemberOrTypeExpression)term).getDeclaration()).getFirstParameterList();
            }
        } else if (term instanceof Tree.InvocationExpression) {
            TypeUtils.encodeCallableArgumentsAsParameterListForRuntime(term, term.getTypeModel(), gen);
            return;
        } else {
            gen.out("/*WARNING4 Callable EXPR of type ", term.getClass().getName(), "*/");
        }
        if (plist == null) {
            gen.out("[]");
        } else {
            TypeUtils.encodeParameterListForRuntime(true, term, plist, gen);
        }
    }

}
