package com.redhat.ceylon.compiler.typechecker.analyzer;

import static com.redhat.ceylon.compiler.typechecker.tree.TreeUtil.formatPath;
import static com.redhat.ceylon.compiler.typechecker.tree.TreeUtil.hasError;
import static com.redhat.ceylon.compiler.typechecker.tree.TreeUtil.name;
import static com.redhat.ceylon.compiler.typechecker.tree.TreeUtil.unwrapExpressionUntilTerm;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.appliedType;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.getNativeHeader;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.intersectionType;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.isBooleanFalse;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.isBooleanTrue;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.isConstructor;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.isNamed;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.isTypeUnknown;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.unionType;
import static com.redhat.ceylon.model.typechecker.model.SiteVariance.IN;
import static com.redhat.ceylon.model.typechecker.model.SiteVariance.OUT;
import static java.util.Collections.emptyList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.redhat.ceylon.common.Backend;
import com.redhat.ceylon.common.BackendSupport;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ClassBody;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.TypeVariance;
import com.redhat.ceylon.model.cmr.JDKUtils;
import com.redhat.ceylon.model.typechecker.model.Class;
import com.redhat.ceylon.model.typechecker.model.Constructor;
import com.redhat.ceylon.model.typechecker.model.Declaration;
import com.redhat.ceylon.model.typechecker.model.Generic;
import com.redhat.ceylon.model.typechecker.model.Interface;
import com.redhat.ceylon.model.typechecker.model.Module;
import com.redhat.ceylon.model.typechecker.model.ModuleImport;
import com.redhat.ceylon.model.typechecker.model.Package;
import com.redhat.ceylon.model.typechecker.model.Parameter;
import com.redhat.ceylon.model.typechecker.model.ParameterList;
import com.redhat.ceylon.model.typechecker.model.Reference;
import com.redhat.ceylon.model.typechecker.model.Scope;
import com.redhat.ceylon.model.typechecker.model.SiteVariance;
import com.redhat.ceylon.model.typechecker.model.Type;
import com.redhat.ceylon.model.typechecker.model.TypeAlias;
import com.redhat.ceylon.model.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.model.typechecker.model.TypeParameter;
import com.redhat.ceylon.model.typechecker.model.TypedDeclaration;
import com.redhat.ceylon.model.typechecker.model.Unit;
import com.redhat.ceylon.model.typechecker.model.Value;

/**
 * Bucket for some helper methods used by various
 * visitors.
 * 
 * @author Gavin King
 *
 */
public class AnalyzerUtil {
    
    static final List<Type> NO_TYPE_ARGS = emptyList();
    
    static TypedDeclaration getTypedMember(TypeDeclaration td, 
            String name, List<Type> signature, boolean ellipsis, 
            Unit unit) {
        Declaration member = 
                td.getMember(name, unit, signature, ellipsis);
        if (member instanceof TypedDeclaration) {
            return (TypedDeclaration) member;
        }
        else {
            return null;
        }
    }

    static TypeDeclaration getTypeMember(TypeDeclaration td, 
            String name, List<Type> signature, boolean ellipsis, 
            Unit unit) {
        Declaration member = 
                td.getMember(name, unit, signature, ellipsis);
        if (member instanceof TypeDeclaration) {
            return (TypeDeclaration) member;
        }
        else if (member instanceof TypedDeclaration) {
            return anonymousType(name, 
                    (TypedDeclaration) member);
        }
        else {
            return null;
        }
    }

    static TypedDeclaration getTypedDeclaration(Scope scope,
            String name, List<Type> signature, boolean ellipsis,
            Unit unit) {
        Declaration result = 
                scope.getMemberOrParameter(unit, 
                        name, signature, ellipsis);
        if (result == null) {
            if (scope instanceof Declaration) {
                // If we couldn't find the declaration in the current
                // scope and the scope is a native implementation we
                // will try again with its header
                Declaration decl = (Declaration) scope;
                if (decl.isNative() && 
                        !decl.isNativeHeader()) {
                    Scope ds = decl.getScope();
                    result = getNativeHeader(ds, name);
                }
            }
        }
        if (result instanceof TypedDeclaration) {
            return (TypedDeclaration) result;
        }
        else {
            return null;
        }
    }
    
    static TypeDeclaration getTypeDeclaration(Scope scope,
            String name, List<Type> signature, boolean ellipsis,
            Unit unit) {
        Declaration result = 
                scope.getMemberOrParameter(unit, 
                        name, signature, ellipsis);
        if (result == null) {
            if (scope instanceof Declaration) {
                // If we couldn't find the declaration in the current
                // scope and the scope is a native implementation we
                // will try again with its header
                Declaration decl = (Declaration)scope;
                if (decl.isNative() && 
                        !decl.isNativeHeader()) {
                    Scope ds = decl.getScope();
                    result = getNativeHeader(ds, name);
                }
            }
        }
        if (result instanceof TypeDeclaration) {
        	return (TypeDeclaration) result;
        }
        else if (result instanceof TypedDeclaration) {
            return anonymousType(name, 
                    (TypedDeclaration) result);
        }
        else {
        	return null;
        }
    }

    static TypedDeclaration getPackageTypedDeclaration(
            String name, List<Type> signature, boolean ellipsis,
            Unit unit) {
        Declaration result = 
                unit.getPackage()
                    .getMember(name, signature, ellipsis);
        if (result instanceof TypedDeclaration) {
            return (TypedDeclaration) result;
        }
        else {
            return null;
        }
    }
    
    static TypeDeclaration getPackageTypeDeclaration(
            String name, List<Type> signature, boolean ellipsis,
            Unit unit) {
        Declaration result = 
                unit.getPackage()
                    .getMember(name, signature, ellipsis);
        if (result instanceof TypeDeclaration) {
            return (TypeDeclaration) result;
        }
        else if (result instanceof TypedDeclaration) {
            return anonymousType(name, 
                    (TypedDeclaration) result);
        }
        else {
            return null;
        }
    }

    private static TypeDeclaration anonymousType(String name, 
            TypedDeclaration result) {
        Type type = result.getType();
        if (type!=null) {
            TypeDeclaration typeDeclaration = 
                    type.getDeclaration();
            if ((typeDeclaration instanceof Class ||
                typeDeclaration instanceof Constructor) &&
                    typeDeclaration.isAnonymous() &&
                    isNamed(name, typeDeclaration)) {
                return typeDeclaration;
            }
        }
        return null;
    }
    
    /**
     * Get the type arguments specified explicitly in the
     * code, or an empty list if no type arguments were
     * explicitly specified. For missing arguments, use
     * default type arguments.
     * 
     * @param tas the type argument list
     * @param qualifyingType the qualifying type
     * @param typeParameters the list of type parameters
     * 
     * @return a list of type arguments to the given type
     *         parameters
     */
    static List<Type> getTypeArguments(
            Tree.TypeArguments tas,
    		Type qualifyingType, 
    		List<TypeParameter> typeParameters) {
        if (tas instanceof Tree.TypeArgumentList) {
            
            //accumulate substitutions in case we need
            //them below for calculating default args
            Map<TypeParameter,Type> typeArgs = 
                    new HashMap<TypeParameter,Type>();
            Map<TypeParameter,SiteVariance> vars = 
                    new HashMap<TypeParameter,SiteVariance>();
            if (qualifyingType!=null) {
                typeArgs.putAll(qualifyingType.getTypeArguments());
                vars.putAll(qualifyingType.getVarianceOverrides());
            }
            
//            if (tas instanceof Tree.TypeArgumentList) {
            Tree.TypeArgumentList tal = 
                    (Tree.TypeArgumentList) tas;
            int size = typeParameters.size();
            List<Type> typeArguments = 
                    new ArrayList<Type>(size);
            List<Tree.Type> types = tal.getTypes();
            int count = types.size();
            for (int i=0; i<count; i++) {
                Tree.Type type = types.get(i);
                Type t = type.getTypeModel();
                if (t==null) {
                    typeArguments.add(null);
                }
                else {
                    typeArguments.add(t);
                    if (i<size) {
                        TypeParameter tp = 
                                typeParameters.get(i);
                        if (tp.isTypeConstructor()) {
                            setTypeConstructor(type, tp);
                        }
                        typeArgs.put(tp, t);
                        if (type instanceof Tree.StaticType) {
                            Tree.StaticType st = 
                                    (Tree.StaticType) type;
                            TypeVariance tv = 
                                    st.getTypeVariance();
                            if (tv!=null) {
                                boolean contra = 
                                        tv.getText()
                                          .equals("in");
                                vars.put(tp, contra?IN:OUT);
                            }
                        }
                    }
                }
            }
//            }
//            else {
//                List<Type> types = 
//                        tas.getTypeModels();
//                int count = types.size();
//                for (int i=0; i<count; i++) {
//                    Type t = types.get(i);
//                    if (t==null) {
//                        typeArguments.add(null);
//                    }
//                    else {
//                        typeArguments.add(t);
//                        if (i<size) {
//                            TypeParameter tp = 
//                                    typeParameters.get(i);
//                            typeArgMap.put(tp, t);
//                        }
//                    }
//                }
//            }
//            if (!(tas instanceof Tree.InferredTypeArguments)) {
            
            //for missing arguments, use the default args
            for (int i=typeArguments.size(); i<size; i++) {
                TypeParameter tp = typeParameters.get(i);
                Declaration tpd = tp.getDeclaration();
            	Type dta = tp.getDefaultTypeArgument();
                if (dta==null || 
            	        //necessary to prevent stack overflow
            	        //for illegal recursively-defined
            	        //default type argument
            	        dta.involvesDeclaration(tpd)) {
            		break;
            	}
            	else {
            	    Type da = dta.substitute(typeArgs, vars);
            		typeArguments.add(da);
            		typeArgs.put(tp, da);
            	}
//                }
            }
            
            return typeArguments;
        }
        else {
            return emptyList();
        }
    }
    
    public static Tree.Statement getLastExecutableStatement(
            Tree.ClassBody that) {
        List<Tree.Statement> statements = 
                that.getStatements();
        Unit unit = that.getUnit();
        for (int i=statements.size()-1; i>=0; i--) {
            Tree.Statement s = statements.get(i);
            if (isExecutableStatement(unit, s) || 
                    s instanceof Tree.Constructor ||
                    s instanceof Tree.Enumerated) {
                return s;
            }
        }
        return null;
    }

    static Tree.Declaration getLastConstructor(Tree.ClassBody that) {
        List<Tree.Statement> statements = that.getStatements();
        for (int i=statements.size()-1; i>=0; i--) {
            Tree.Statement s = statements.get(i);
            if (s instanceof Tree.Constructor ||
                s instanceof Tree.Enumerated) {
                return (Tree.Declaration) s;
            }
        }
        return null;
    }

    static boolean isExecutableStatement(Unit unit, Tree.Statement s) {
        if (s instanceof Tree.SpecifierStatement) {
            //shortcut refinement statements with => aren't really "executable"
            Tree.SpecifierStatement ss = 
                    (Tree.SpecifierStatement) s;
            Tree.SpecifierExpression se = 
                    ss.getSpecifierExpression();
            return !(ss.getRefinement() &&
                    se instanceof Tree.LazySpecifierExpression);
        }
        else if (s instanceof Tree.ExecutableStatement) {
            return true;
        }
        else {
            if (s instanceof Tree.AttributeDeclaration) {
                Tree.AttributeDeclaration ad = 
                        (Tree.AttributeDeclaration) s;
                Tree.SpecifierOrInitializerExpression sie = 
                        ad.getSpecifierOrInitializerExpression();
        		return !(sie==null ||
        		        sie instanceof Tree.LazySpecifierExpression);
            }
            else if (s instanceof Tree.ObjectDefinition) {
                Tree.ObjectDefinition o = 
                        (Tree.ObjectDefinition) s;
                if (o.getExtendedType()!=null) {
                    Type et = 
                            o.getExtendedType()
                                .getType()
                                .getTypeModel();
        			if (et!=null 
                            && !et.isObject()
                            && !et.isBasic()) {
                        return true;
                    }
                }
                ClassBody body = o.getClassBody();
                if (body!=null) {
                    if (getLastExecutableStatement(body)!=null) {
                        return true;
                    }
                }
                return false;
            }
            else {
                return false;
            }
        }
    }
    
    static String typingMessage(Type type, 
            String problem, Type otherType, 
            Unit unit) {
        String unknownTypeError = 
                type.getFirstUnknownTypeError(true);
        String typeName = type.asString(unit);
        String otherTypeName = otherType.asString(unit);
        String expandedTypeName;
        String expandedOtherTypeName;
        if (otherTypeName.equals(typeName)) {
            typeName = type.asQualifiedString();
            otherTypeName = otherType.asQualifiedString();
            expandedTypeName = 
                    type.resolveAliases()
                        .asQualifiedString();
            expandedOtherTypeName = 
                    otherType.resolveAliases()
                        .asQualifiedString();
        }
        else {
            expandedTypeName = 
                    type.resolveAliases()
                        .asString(unit);
            expandedOtherTypeName = 
                    otherType.resolveAliases()
                        .asString(unit);
        }
        StringBuilder sb = new StringBuilder();
        sb.append(": '")
          .append(typeName)
          .append("'");
        if (!typeName.equals(expandedTypeName)) {
            sb.append(" ('")
              .append(expandedTypeName)
              .append("')");
        }
        sb.append(problem);
        sb.append("'")
          .append(otherTypeName)
          .append("'");
        if (!otherTypeName.equals(expandedOtherTypeName)) {
            sb.append(" ('")
              .append(expandedOtherTypeName)
              .append("')");
        }
        if (unknownTypeError!=null) {
            sb.append(": ")
              .append(unknownTypeError);
        }
        return sb.toString();
    }
    
    private static String message(Type type, 
            String problem, Unit unit) {
        String typeName = type.asString(unit);
        String expandedTypeName = 
                type.resolveAliases()
                    .asString(unit);
        StringBuilder sb = new StringBuilder();
        sb.append(": '")
          .append(typeName)
          .append("'");
        if (!typeName.equals(expandedTypeName)) {
            sb.append(" ('")
              .append(expandedTypeName)
              .append("')");
        }
        sb.append(problem);
        return sb.toString();
    }
    
    static boolean checkCallable(Type type, Node node, String message) {
        Unit unit = node.getUnit();
        if (isTypeUnknown(type)) {
            addTypeUnknownError(node, type, message);
            return false;
        }
        else if (!unit.isCallableType(type)) {
            if (!hasError(node)) {
                String extra = 
                        message(type, 
                                " is not a subtype of 'Callable'", 
                                unit);
                if (node instanceof Tree.StaticMemberOrTypeExpression) {
                    Tree.StaticMemberOrTypeExpression smte = 
                            (Tree.StaticMemberOrTypeExpression) node;
                    Declaration d = smte.getDeclaration();
                    String name = d.getName();
                    if (d instanceof Interface) {
                        extra = ": '" + name + "' is an interface";
                    }
                    else if (d instanceof TypeAlias) {
                        extra = ": '" + name + "' is a type alias";
                    }
                    else if (d instanceof TypeParameter) {
                        extra = ": '" + name + "' is a type parameter";
                    }
                    else if (d instanceof Value) {
                        extra = ": value '" + name + "' has type '" + 
                                type.asString(unit) + 
                                "' which is not a subtype of 'Callable'";
                    }
                }
                node.addError(message + extra);
            }
            return false;
        }
        else {
            return true;
        }
    }

    static Type checkSupertype(Type type, 
            TypeDeclaration td, Node node, String message) {
        if (isTypeUnknown(type)) {
            addTypeUnknownError(node, type, message);
            return null;
        }
        else {
            Type supertype = type.getSupertype(td);
            if (supertype==null) {
                node.addError(message + 
                        message(type, 
                                " is not a subtype of '" + 
                                        td.getName() + "'", 
                                node.getUnit()));
            }
            return supertype;
        }
    }

    static void checkAssignable(Type type, 
            Type supertype, Node node, String message) {
        if (isTypeUnknown(type)) {
        	addTypeUnknownError(node, type, message);
        }
        else if (isTypeUnknown(supertype)) {
            addTypeUnknownError(node, supertype, message);
        }
        else if (!type.isSubtypeOf(supertype)) {
        	node.addError(message + 
        	        notAssignableMessage(type, supertype, node));
        }
    }

    static void checkAssignableWithWarning(Type type, 
            Type supertype, Node node, String message) {
        if (isTypeUnknown(type)) {
            addTypeUnknownError(node, type, message);
        }
        else if (isTypeUnknown(supertype)) {
            addTypeUnknownError(node, supertype, message);
        }
        else if (!type.isSubtypeOf(supertype)) {
        	node.addUnsupportedError(message + 
        	        notAssignableMessage(type, supertype, node));
        }
    }

    static void checkAssignableToOneOf(Type type, 
    		Type supertype1, Type supertype2, 
            Node node, String message, int code) {
        if (isTypeUnknown(type)) {
            addTypeUnknownError(node, type, message);
        }
        else if (isTypeUnknown(supertype1)) {
            addTypeUnknownError(node, supertype1, message);
        }
        else if (isTypeUnknown(supertype2)) {
            addTypeUnknownError(node, supertype2, message);
        }
        else if (!type.isSubtypeOf(supertype1)
                && !type.isSubtypeOf(supertype2)) {
            node.addError(message + 
                    notAssignableMessage(type, supertype1, node), 
                    code);
        }
    }

    static String notAssignableMessage(Type type,
            Type supertype, Node node) {
        return typingMessage(type, 
                " is not assignable to ", 
                supertype, 
                node.getUnit());
    }

    static void checkAssignable(Type type, 
            Type supertype, Node node, 
            String message, int code) {
        if (isTypeUnknown(type)) {
            addTypeUnknownError(node, type, message);
        }
        else if (isTypeUnknown(supertype)) {
            addTypeUnknownError(node, supertype, message);
        }
        else if (!type.isSubtypeOf(supertype)) {
            node.addError(message + 
                    notAssignableMessage(type, supertype, node), 
                    code);
        }
    }

    /*static void checkAssignable(Type type, Type supertype, 
            TypeDeclaration td, Node node, String message) {
        if (isTypeUnknown(type) || isTypeUnknown(supertype)) {
            addTypeUnknownError(node, message);
        }
        else if (!type.isSubtypeOf(supertype)) {
            node.addError(message + message(type, " is not assignable to ", supertype, node.getUnit()));
        }
    }*/

    static void checkIsExactly(Type type, 
            Type supertype, Node node, String message) {
        if (isTypeUnknown(type)) {
            addTypeUnknownError(node, type, message);
        }
        else if (isTypeUnknown(supertype)) {
            addTypeUnknownError(node, supertype, message);
        }
        else if (!type.isExactly(supertype)) {
            node.addError(message + 
                    notExactlyMessage(type, supertype, node));
        }
    }

    static void checkIsExactly(Type type, 
            Type supertype, Node node, String message, 
            int code) {
        if (isTypeUnknown(type)) {
            addTypeUnknownError(node, type, message);
        }
        else if (isTypeUnknown(supertype)) {
            addTypeUnknownError(node, supertype, message);
        }
        else if (!type.isExactly(supertype)) {
            node.addError(message + 
                    notExactlyMessage(type, supertype, node), 
                    code);
        }
    }

    static void checkIsExactlyOneOf(Type type, 
    		Type supertype1, Type supertype2, 
            Node node, String message) {
        if (isTypeUnknown(type)) {
            addTypeUnknownError(node, type, message);
        }
        else if (isTypeUnknown(supertype1)) {
            addTypeUnknownError(node, supertype1, message);
        }
        else if (isTypeUnknown(supertype2)) {
            addTypeUnknownError(node, supertype2, message);
        }
        else if (!type.isExactly(supertype1)
                && !type.isExactly(supertype2)) {
            node.addError(message + 
                    notExactlyMessage(type, supertype1, node));
        }
    }

    static String notExactlyMessage(Type type, 
            Type supertype, Node node) {
        return typingMessage(type, 
                " is not exactly ", 
                supertype, 
                node.getUnit());
    }
    
    private static void addTypeUnknownError(Node node, 
            Type type, String message) {
        if (!hasError(node)) {
            node.addError(message + 
                    ": type cannot be determined" +
                    getTypeUnknownError(type));
        }
    }
    
    static String getTypeUnknownError(Type type) {
        if (type == null) {
            return "";
        }
        else {
            String error = type.getFirstUnknownTypeError();
            if (error != null) {
                return ": " + error;
            }
            else {
                return "";
            }
        }
    }

    static boolean inLanguageModule(Unit unit) {
        return unit.getPackage()
                .getQualifiedNameString()
                .startsWith(Module.LANGUAGE_MODULE_NAME);
    }

    static String typeDescription(TypeDeclaration td, Unit unit) {
        String name = td.getName();
        if (td instanceof TypeParameter) {
            Declaration container = 
                    (Declaration) td.getContainer();
            return "type parameter '" + name + "' of '" + 
                    container.getName(unit) + "'";
        }
        else {
            return "type '" + name + "'";
        }
    }

    static String typeNamesAsIntersection(
            List<Type> upperBounds, Unit unit) {
        if (upperBounds.isEmpty()) {
            return "Anything";
        }
        StringBuilder sb = new StringBuilder();
        for (Type st: upperBounds) {
            sb.append(st.asString(unit)).append(" & ");
        }
        if (sb.toString().endsWith(" & ")) {
            sb.setLength(sb.length()-3);
        }
        return sb.toString();
    }

    static boolean isAlwaysSatisfied(Tree.ConditionList cl) {
        if (cl==null) return false;
        for (Tree.Condition c: cl.getConditions()) {
            if (c instanceof Tree.BooleanCondition) {
                Tree.BooleanCondition bc = 
                        (Tree.BooleanCondition) c;
                Tree.Expression ex = bc.getExpression();
                if (ex!=null) {
                    Tree.Term term = 
                            unwrapExpressionUntilTerm(ex);
                    //TODO: take into account conjunctions/disjunctions
                    if (term instanceof Tree.BaseMemberExpression) {
                        Tree.BaseMemberExpression bme = 
                                (Tree.BaseMemberExpression) 
                                    term;
                        Declaration d = bme.getDeclaration();
                        if (isBooleanTrue(d)) {
                            continue;
                        }
                    }
                }
            }
            return false;
        }
        return true;
    }

    static boolean isNeverSatisfied(Tree.ConditionList cl) {
        if (cl==null) return false;
        for (Tree.Condition c: cl.getConditions()) {
            if (c instanceof Tree.BooleanCondition) {
                Tree.BooleanCondition bc = 
                        (Tree.BooleanCondition) c;
                Tree.Expression ex = 
                        bc.getExpression();
                if (ex!=null) {
                    Tree.Term term = 
                            unwrapExpressionUntilTerm(ex);
                    //TODO: take into account conjunctions/disjunctions
                    if (term instanceof Tree.BaseMemberExpression) {
                        Tree.BaseMemberExpression bme = 
                                (Tree.BaseMemberExpression) 
                                    term;
                        Declaration d = bme.getDeclaration();
                        if (isBooleanFalse(d)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
    
    static boolean isAtLeastOne(Tree.ForClause forClause) {
        Tree.ForIterator fi = forClause.getForIterator();
        if (fi!=null) {
            Tree.SpecifierExpression se = 
                    fi.getSpecifierExpression();
            if (se!=null) {
                Tree.Expression e = se.getExpression();
                if (e!=null) {
                    Unit unit = forClause.getUnit();
                    Type at = 
                            unit.getAnythingType();
                    Type neit = 
                            unit.getNonemptyIterableType(at);
                    Type t = e.getTypeModel();
                    return t!=null && t.isSubtypeOf(neit);
                }
            }
        }
        return false;
    }

    static boolean declaredInPackage(Declaration dec, Unit unit) {
        return dec.getUnit().getPackage().equals(unit.getPackage());
    }

    public static boolean isIndirectInvocation(Tree.InvocationExpression that) {
        return isIndirectInvocation(that.getPrimary());
    }

    private static boolean isIndirectInvocation(Tree.Primary primary) {
        Tree.Term term = unwrapExpressionUntilTerm(primary);
        if (term instanceof Tree.MemberOrTypeExpression) {
            Tree.MemberOrTypeExpression mte = 
                    (Tree.MemberOrTypeExpression) term;
            return isIndirectInvocation(mte);
        }
        else {
           return true;
        }
    }

    private static boolean isIndirectInvocation(
            Tree.MemberOrTypeExpression that) {
        Reference prf = that.getTarget();
        if (prf==null) {
            return true;
        }
        else {
            Declaration d = prf.getDeclaration();
            if (!prf.isFunctional() || 
                    //type parameters are not really callable 
                    //even though they are Functional
                    d instanceof TypeParameter) {
                return true;
            }
            if (that.getStaticMethodReference()) {
                if (d.isStaticallyImportable() || 
                        isConstructor(d)) {
                    Tree.QualifiedMemberOrTypeExpression qmte = 
                            (Tree.QualifiedMemberOrTypeExpression) 
                                that;
                    return isIndirectInvocation(qmte.getPrimary());
                }
                else {
                    return true;
                }
            }
            return false;
        }
    }
    
    static String message(Declaration dec) {
        String qualifier;
        Scope container = dec.getContainer();
        if (container instanceof Declaration) {
            Declaration cd = (Declaration) container;
            qualifier = " in '" + cd.getName() + "'";
        }
        else {
            qualifier = "";
        }
        return "'" + dec.getName() + "'" + qualifier;
    }
    
    static Node getParameterTypeErrorNode(Tree.Parameter p) {
        if (p instanceof Tree.ParameterDeclaration) {
            Tree.ParameterDeclaration pd = 
                    (Tree.ParameterDeclaration) p;
            return pd.getTypedDeclaration().getType();
        }
        else {
            return p;
        }
    }
    
    public static Node getTypeErrorNode(Node that) {
        if (that instanceof Tree.TypedDeclaration) {
            Tree.TypedDeclaration td = 
                    (Tree.TypedDeclaration) that;
            Tree.Type type = td.getType();
            if (type!=null) {
                return type;
            }
        }
        if (that instanceof Tree.TypedArgument) {
            Tree.TypedArgument ta = 
                    (Tree.TypedArgument) that;
            Tree.Type type = ta.getType();
            if (type!=null) {
                return type;
            }
        }
        if (that instanceof Tree.FunctionArgument) {
            Tree.FunctionArgument fa = 
                    (Tree.FunctionArgument) that;
            Tree.Type type = fa.getType();
            if (type!=null && type.getToken()!=null) {
                return type;
            }
        }
        return that;
    }

    static void checkIsExactlyForInterop(Unit unit, 
            boolean isCeylon,  
            Type parameterType, 
            Type refinedParameterType, 
            Node node, String message) {
        if (isCeylon) {
            // it must be a Ceylon method
            checkIsExactly(parameterType, 
                    refinedParameterType, 
                    node, message, 9200);
        }
        else {
            // we're refining a Java method
            Type refinedDefiniteType = 
                    unit.getDefiniteType(
                            refinedParameterType);
            checkIsExactlyOneOf(parameterType, 
                    refinedParameterType, 
                    refinedDefiniteType, 
            		node, message);
        }
    }

    public static Type getTupleType(
            List<Tree.PositionalArgument> es, 
            Unit unit, 
            boolean requireSequential) {
        Type result = unit.getEmptyType();
        Type ut = unit.getNothingType();
        Class td = unit.getTupleDeclaration();
        Interface id = unit.getIterableDeclaration();
        for (int i=es.size()-1; i>=0; i--) {
            Tree.PositionalArgument a = es.get(i);
            Type t = a.getTypeModel();
            if (t!=null) {
                Type et = t; //unit.denotableType(t);
                if (a instanceof Tree.SpreadArgument) {
                    /*if (requireSequential) { 
                        checkSpreadArgumentSequential((Tree.SpreadArgument) a, et);
                    }*/
                    ut = unit.getIteratedType(et);
                    result = spreadType(et, unit, requireSequential);
                }
                else if (a instanceof Tree.Comprehension) {
                    ut = et;
                    Tree.Comprehension c = (Tree.Comprehension) a;
                    Tree.InitialComprehensionClause icc = 
                            c.getInitialComprehensionClause();
                    result = icc.getPossiblyEmpty() ? 
                            unit.getSequentialType(et) : 
                            unit.getSequenceType(et);
                    if (!requireSequential) {
                        Type it = 
                                appliedType(id, et, 
                                        icc.getFirstTypeModel());
                        result = intersectionType(result, it, unit);
                    }
                }
                else {
                    ut = unionType(ut, et, unit);
                    result = appliedType(td, ut, et, result);
                }
            }
        }
        return result;
    }
    
    static Type spreadType(Type et, Unit unit,
            boolean requireSequential) {
        if (et==null) return null;
        if (requireSequential) {
            if (unit.isSequentialType(et)) {
                //if (et.isTypeParameter()) {
                    return et;
                /*}
                else {
                    // if it's already a subtype of Sequential, erase 
                    // out extraneous information, like that it is a
                    // String, just keeping information about what
                    // kind of tuple it is
                    List<Type> elementTypes = unit.getTupleElementTypes(et);
                    boolean variadic = unit.isTupleLengthUnbounded(et);
                    boolean atLeastOne = unit.isTupleVariantAtLeastOne(et);
                    int minimumLength = unit.getTupleMinimumLength(et);
                    if (variadic) {
                        Type spt = elementTypes.get(elementTypes.size()-1);
                        elementTypes.set(elementTypes.size()-1, unit.getIteratedType(spt));
                    }
                    return unit.getTupleType(elementTypes, variadic, 
                            atLeastOne, minimumLength);
                }*/
            }
            else {
                // transform any Iterable into a Sequence without
                // losing the information that it is nonempty, in
                // the case that we know that for sure
                Type it = unit.getIteratedType(et);
                Type st = 
                        unit.isNonemptyIterableType(et) ?
                                unit.getSequenceType(it) :
                                unit.getSequentialType(it);
                // unless this is a tuple constructor, remember
                // the original Iterable type arguments, to
                // account for the possibility that the argument
                // to Absent is a type parameter
                //return intersectionType(et.getSupertype(unit.getIterableDeclaration()), st, unit);
                // for now, just return the sequential type:
                return st;
            }
        }
        else {
            return et;
        }
    }

    static boolean setTypeConstructor(Tree.Type t,
            TypeParameter typeParam) {
        Type pt = t.getTypeModel();
        if (pt == null) {
            return false;
        }
        else {
            /*if (t instanceof Tree.IntersectionType) {
                Tree.IntersectionType it = 
                        (Tree.IntersectionType) t;
                for (Tree.StaticType st: it.getStaticTypes()) {
                    if (setTypeConstructor(st,typeParam)) {
                        pt.setTypeConstructor(true);
                        pt.setTypeConstructorParameter(typeParam);
                    }
                }
            }
            else if (t instanceof Tree.UnionType) {
                Tree.UnionType it = 
                        (Tree.UnionType) t;
                for (Tree.StaticType st: it.getStaticTypes()) {
                    if (setTypeConstructor(st,typeParam)) {
                        pt.setTypeConstructor(true);
                        pt.setTypeConstructorParameter(typeParam);
                    }
                }
            }
            else*/ 
            if (t instanceof Tree.SimpleType) {
                Tree.SimpleType s = 
                        (Tree.SimpleType) t;
                if (s.getTypeArgumentList()==null) {
                    if (typeParam!=null || 
                            isGeneric(s.getDeclarationModel())) {
                        pt.setTypeConstructor(true);
                        pt.setTypeConstructorParameter(typeParam);
                    }
                }
            }
            return pt.isTypeConstructor();
        }
    }

    static boolean isGeneric(Declaration member) {
        if (member instanceof Generic) {
            Generic g = (Generic) member;
            return !g.getTypeParameters().isEmpty();
        }
        else {
            return false;
        }
    }

    static boolean inSameModule(TypeDeclaration etd, Unit unit) {
        return etd.getUnit().getPackage().getModule()
        		.equals(unit.getPackage().getModule());
    }

    static void checkCasesDisjoint(Type type, Type other,
            Node node) {
        if (!isTypeUnknown(type) && !isTypeUnknown(other)) {
            Unit unit = node.getUnit();
            Type it = 
                    intersectionType(type.resolveAliases(), 
                            other.resolveAliases(), unit);
            if (!it.isNothing()) {
                node.addError("cases are not disjoint: '" + 
                        type.asString(unit) + "' and '" + 
                        other.asString(unit) + "'");
            }
        }
    }

    static Parameter getMatchingParameter(ParameterList pl, 
            Tree.NamedArgument na, 
            Set<Parameter> foundParameters) {
        Tree.Identifier id = na.getIdentifier();
        if (id==null) {
            for (Parameter p: pl.getParameters()) {
                if (!foundParameters.contains(p)) {
                    return p;
                }
            }
        }
        else {
            String name = name(id);
            for (Parameter p: pl.getParameters()) {
                if (p.getName()!=null &&
                        p.getName().equals(name)) {
                    return p;
                }
            }
        }
        return null;
    }

    static Parameter getUnspecifiedParameter(Reference pr,
            ParameterList pl, 
            Set<Parameter> foundParameters) {
        for (Parameter p: pl.getParameters()) {
            Type t = pr==null ? 
                    p.getType() : 
                    pr.getTypedParameter(p)
                        .getFullType();
            if (t!=null) {
                t = t.resolveAliases();
                if (!foundParameters.contains(p) &&
                        p.getDeclaration().getUnit()
                            .isIterableParameterType(t)) {
                    return p;
                }
            }
        }
        return null;
    }

    static boolean involvesTypeParams(Declaration dec, 
            Type type) {
        if (isGeneric(dec)) {
            Generic g = (Generic) dec;
            return type.involvesTypeParameters(g);
        }
        else {
            return false;
        }
    }

    static TypeDeclaration unwrapAliasedTypeConstructor(
            TypeDeclaration dec) {
        TypeDeclaration d = dec;
        while (!isGeneric(d) && d.isAlias()) {
            Type et = d.getExtendedType();
            if (et==null) break;
            et = et.resolveAliases();
            d = et.getDeclaration();
            if (et.isTypeConstructor() && isGeneric(d)) {
                return d;
            }
        }
        return dec;
    }

    static Type unwrapAliasedTypeConstructor(Type type) {
        TypeDeclaration d = type.getDeclaration();
        while (!isGeneric(d) && d.isAlias()) {
            Type et = d.getExtendedType();
            if (et==null) break;
            d = et.getDeclaration();
            et = et.resolveAliases();
            if (et.isTypeConstructor() && isGeneric(d)) {
                return et;
            }
        }
        return type;
    }

    static Package importedPackage(Tree.ImportPath path, 
                BackendSupport backendSupport) {
            if (path!=null && 
                    !path.getIdentifiers().isEmpty()) {
                String nameToImport = 
                        formatPath(path.getIdentifiers());
                Module module = 
                        path.getUnit()
                            .getPackage()
                            .getModule();
                Package pkg = module.getPackage(nameToImport);
                if (pkg != null) {
                    if (pkg.getModule().equals(module)) {
                        return pkg;
                    }
                    if (!pkg.isShared()) {
                        path.addError("imported package is not shared: '" + 
                                nameToImport + "'", 402);
                    }
    //                if (module.isDefault() && 
    //                        !pkg.getModule().isDefault() &&
    //                        !pkg.getModule().getNameAsString()
    //                            .equals(Module.LANGUAGE_MODULE_NAME)) {
    //                    path.addError("package belongs to a module and may not be imported by default module: " +
    //                            nameToImport);
    //                }
                    //check that the package really does belong to
                    //an imported module, to work around bug where
                    //default package thinks it can see stuff in
                    //all modules in the same source dir
                    Set<Module> visited = new HashSet<Module>();
                    for (ModuleImport mi: module.getImports()) {
                        if (findModuleInTransitiveImports(
                                mi.getModule(), 
                                pkg.getModule(), 
                                visited)) {
                            return pkg; 
                        }
                    }
                }
                else {
                    for (ModuleImport mi: module.getImports()) {
                        if (mi.isNative()) {
                            Backend backend = 
                                    Backend.fromAnnotation(
                                            mi.getNativeBackend());
                            String name = 
                                    mi.getModule()
                                        .getNameAsString();
                            if (!backendSupport.supportsBackend(backend) && 
                                    (nameToImport.equals(name) || 
                                     nameToImport.startsWith(name + "."))) {
                                return null;
                            }
                            if (!backendSupport.supportsBackend(Backend.Java) && 
                                    (JDKUtils.isJDKAnyPackage(nameToImport) || 
                                     JDKUtils.isOracleJDKAnyPackage(nameToImport))) {
                                return null;
                            }
                        }
                    }
                }
                String help;
                if (module.isDefault()) {
                    help = " (define a module and add module import to its module descriptor)";
                }
                else {
                    help = " (add module import to module descriptor of '" +
                            module.getNameAsString() + "')";
                }
                path.addError("package not found in imported modules: '" + 
                        nameToImport + "'" + help, 7000);
            }
            return null;
        }

    static Module importedModule(Tree.ImportPath path) {
        if (path!=null && 
                !path.getIdentifiers().isEmpty()) {
            String nameToImport = 
                    formatPath(path.getIdentifiers());
            Module module = 
                    path.getUnit()
                        .getPackage()
                        .getModule();
            Package pkg = module.getPackage(nameToImport);
            if (pkg != null) {
                Module mod = pkg.getModule();
                if (!pkg.getNameAsString()
                        .equals(mod.getNameAsString())) {
                    path.addError("not a module: '" + 
                            nameToImport + "'");
                    return null;
                }
                if (mod.equals(module)) {
                    return mod;
                }
                //check that the package really does belong to
                //an imported module, to work around bug where
                //default package thinks it can see stuff in
                //all modules in the same source dir
                Set<Module> visited = new HashSet<Module>();
                for (ModuleImport mi: module.getImports()) {
                    Module m = mi.getModule();
                    if (findModuleInTransitiveImports(m, mod, 
                            visited)) {
                        return mod; 
                    }
                }
            }
            path.addError("module not found in imported modules: '" + 
                    nameToImport + "'", 7000);
        }
        return null;
    }

    private static boolean findModuleInTransitiveImports(
            Module moduleToVisit, Module moduleToFind, 
            Set<Module> visited) {
        if (!visited.add(moduleToVisit)) {
            return false;
        }
        else if (moduleToVisit.equals(moduleToFind)) {
            return true;
        }
        else {
            for (ModuleImport imp: moduleToVisit.getImports()) {
                // skip non-exported modules
                if (imp.isExport() &&
                        findModuleInTransitiveImports(
                                imp.getModule(), 
                                moduleToFind, visited)) {
                    return true;
                }
            }
            return false;
        }
    }

}
