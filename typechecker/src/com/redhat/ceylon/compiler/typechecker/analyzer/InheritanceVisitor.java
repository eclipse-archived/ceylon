package com.redhat.ceylon.compiler.typechecker.analyzer;

import static com.redhat.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.checkAssignable;
import static com.redhat.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.checkCasesDisjoint;
import static com.redhat.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.checkIsExactly;
import static com.redhat.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.getTypedDeclaration;
import static com.redhat.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.inLanguageModule;
import static com.redhat.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.inSameModule;
import static com.redhat.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.typeDescription;
import static com.redhat.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.typeNamesAsIntersection;
import static com.redhat.ceylon.compiler.typechecker.tree.TreeUtil.name;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.addToIntersection;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.areConsistentSupertypes;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.canonicalIntersection;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.intersectionOfSupertypes;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.isTypeUnknown;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;
import com.redhat.ceylon.model.typechecker.model.Class;
import com.redhat.ceylon.model.typechecker.model.ClassOrInterface;
import com.redhat.ceylon.model.typechecker.model.Constructor;
import com.redhat.ceylon.model.typechecker.model.Declaration;
import com.redhat.ceylon.model.typechecker.model.Interface;
import com.redhat.ceylon.model.typechecker.model.Scope;
import com.redhat.ceylon.model.typechecker.model.Type;
import com.redhat.ceylon.model.typechecker.model.TypeAlias;
import com.redhat.ceylon.model.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.model.typechecker.model.TypeParameter;
import com.redhat.ceylon.model.typechecker.model.TypedDeclaration;
import com.redhat.ceylon.model.typechecker.model.Unit;
import com.redhat.ceylon.model.typechecker.model.UnknownType;
import com.redhat.ceylon.model.typechecker.model.Value;

/**
 * Enforces a number of rules surrounding inheritance.
 * This work happens during an intermediate phase in 
 * between the second and third phases of type analysis.
 * 
 * @see TypeHierarchyVisitor for more complex stuff
 * 
 * @author Gavin King
 *
 */
public class InheritanceVisitor extends Visitor {
    
    @Override public void visit(Tree.TypeDeclaration that) {
        validateSupertypes(that, that.getDeclarationModel());
        super.visit(that);
    }

    @Override public void visit(Tree.ObjectDefinition that) {
        validateSupertypes(that, 
                that.getDeclarationModel()
                    .getType()
                    .getDeclaration());
        super.visit(that);
        validateEnumeratedSupertypes(that, 
                that.getAnonymousClass());
    }

    @Override public void visit(Tree.ObjectArgument that) {
        validateSupertypes(that, 
                that.getAnonymousClass());
        super.visit(that);
        validateEnumeratedSupertypes(that, 
                that.getAnonymousClass());
    }

    @Override public void visit(Tree.ObjectExpression that) {
        validateSupertypes(that, 
                that.getAnonymousClass());
        super.visit(that);
        validateEnumeratedSupertypes(that, 
                that.getAnonymousClass());
    }

    @Override public void visit(Tree.TypeConstraint that) {
        super.visit(that);
        validateUpperBounds(that, 
                that.getDeclarationModel());
    }

    @Override public void visit(Tree.ClassOrInterface that) {
        super.visit(that);
        validateEnumeratedSupertypeArguments(that, 
                that.getDeclarationModel());
    }

    @Override public void visit(Tree.ClassDefinition that) {
        super.visit(that);
        validateEnumeratedSupertypes(that, 
                that.getDeclarationModel());
    }
    
    @Override public void visit(Tree.InterfaceDefinition that) {
        super.visit(that);
        validateEnumeratedSupertypes(that, 
                that.getDeclarationModel());
    }
    
    private void validateSupertypes(Node that, 
            TypeDeclaration td) {
        if (!(td instanceof TypeAlias)) {
            List<Type> supertypes = 
                    td.getType().getSupertypes();
            for (int i=0; i<supertypes.size(); i++) {
                Type st1 = supertypes.get(i);
                for (int j=i+1; j<supertypes.size(); j++) {
                    Type st2 = supertypes.get(j);
                    //Note: sets td.inconsistentType by side-effect
                    checkSupertypeIntersection(that, 
                            td, st1, st2); 
                }
            }
        }
    }
    private void checkSupertypeIntersection(Node that,
            TypeDeclaration td, 
            Type st1, Type st2) {
        TypeDeclaration st1d = st1.getDeclaration();
        TypeDeclaration st2d = st2.getDeclaration();
        if (st1d.equals(st2d) /*&& !st1.isExactly(st2)*/) {
            Unit unit = that.getUnit();
            if (!areConsistentSupertypes(st1, st2, unit)) {
                that.addError(typeDescription(td, unit) +
                        " has the same parameterized supertype twice with incompatible type arguments: '" +
                        st1.asString(unit) + " & " + 
                        st2.asString(unit) + "'");
               td.setInconsistentType(true);
            }
        }
    }

    private void validateUpperBounds(Tree.TypeConstraint that,
            TypeDeclaration td) {
        if (!td.isInconsistentType()) {
            Unit unit = that.getUnit();
            List<Type> upperBounds = 
                    td.getSatisfiedTypes();
            List<Type> list = 
                    new ArrayList<Type>
                        (upperBounds.size());
            for (Type st: upperBounds) {
                addToIntersection(list, st, unit);
            }
            if (canonicalIntersection(list, unit).isNothing()) {
                that.addError(typeDescription(td, unit) + 
                        " has unsatisfiable upper bound constraints: the constraints '" + 
                        typeNamesAsIntersection(upperBounds, unit) + 
                        "' cannot be satisfied by any type except 'Nothing'");
            }
        }
    }

    private void validateEnumeratedSupertypes(Node that, ClassOrInterface d) {
        Type type = d.getType();
        Unit unit = that.getUnit();
        for (Type supertype: type.getSupertypes()) {
            if (!type.isExactly(supertype)) {
                TypeDeclaration std = supertype.getDeclaration();
                List<Type> cts = std.getCaseTypes();
                if (cts!=null && 
                        !cts.isEmpty()) {
                    if (cts.size()==1 && 
                            cts.get(0).getDeclaration()
                                .isSelfType()) {
                        continue;
                    }
                    List<Type> types =
                            new ArrayList<Type>
                                (cts.size());
                    for (Type ct: cts) {
                        TypeDeclaration ctd = 
                                ct.resolveAliases()
                                    .getDeclaration();
                        Type cst = type.getSupertype(ctd);
                        if (cst!=null) {
                            types.add(cst);
                        }
                    }
                    if (types.isEmpty()) {
                        that.addError("not a subtype of any case of enumerated supertype: '" + 
                                d.getName(unit) + "' is a subtype of '" + 
                                std.getName(unit) + "'");
                    }
                    else if (types.size()>1) {
                        StringBuilder sb = new StringBuilder();
                        for (Type pt: types) {
                            sb.append("'")
                              .append(pt.asString(unit))
                              .append("' and ");
                        }
                        sb.setLength(sb.length()-5);
                        that.addError("concrete type is a subtype of multiple cases of enumerated supertype '" +
                                std.getName(unit) + "': '" + 
                                d.getName(unit) + "' is a subtype of " + sb);
                    }
                }
            }
        }
    }

    private void validateEnumeratedSupertypeArguments(
            Node that, ClassOrInterface classOrInterface) {
        //note: I hate doing the whole traversal here, but
        //      it is the only way to get the error in the 
        //      right place (see the note in visit(CaseTypes) 
        //      for more)
        Type type = classOrInterface.getType();
        for (Type supertype: type.getSupertypes()) { //traverse the entire supertype hierarchy of the declaration
            if (!type.isExactly(supertype)) {
                List<Type> cts = 
                        supertype.getDeclaration()
                            .getCaseTypes();
                if (cts!=null) {
                    for (Type ct: cts) {
                        if (ct.getDeclaration()
                                .equals(classOrInterface)) { //the declaration is a case of the current enumerated supertype
                            validateEnumeratedSupertypeArguments(
                                    that, classOrInterface, 
                                    supertype);
                            break;
                        }
                    }
                }
            }
        }
    }

    private void validateEnumeratedSupertypeArguments(
            Node that, TypeDeclaration type, 
            Type supertype) {
        List<TypeParameter> params = 
                supertype.getDeclaration()
                    .getTypeParameters();
        Map<TypeParameter, Type> typeArguments = 
                supertype.getTypeArguments();
        for (TypeParameter param: params) {
            Type arg = typeArguments.get(param); //the type argument that the declaration (indirectly) passes to the enumerated supertype
            if (arg!=null) {
                validateEnumeratedSupertypeArgument(that, 
                        type, supertype, param, arg);
            }
        }
    }

    private void validateEnumeratedSupertypeArgument(
            Node that, TypeDeclaration type, 
            Type supertype, TypeParameter tp, 
            Type arg) {
        Unit unit = that.getUnit();
        if (arg.isTypeParameter()) {
            TypeParameter atp = 
                    (TypeParameter) 
                        arg.getDeclaration();
            if (atp.getDeclaration().equals(type)) { //the argument is a type parameter of the declaration
                //check that the variance of the argument 
                //type parameter is the same as the type 
                //parameter of the enumerated supertype
                if (tp.isCovariant() && !atp.isCovariant()) {
                    that.addError("argument to covariant type parameter of enumerated supertype must be covariant: " + 
                            typeDescription(tp, unit));
                }
                if (tp.isContravariant() && 
                        !atp.isContravariant()) {
                    that.addError("argument to contravariant type parameter of enumerated supertype must be contravariant: " + 
                            typeDescription(tp, unit));
                }
            }
            else {
                that.addError("argument to type parameter of enumerated supertype must be a type parameter of '" +
                        type.getName() + "': " + 
                        typeDescription(tp, unit));
            }
        }
        else if (tp.isCovariant()) {
            if (!(arg.isNothing())) {
                //TODO: let it be the union of the lower bounds on p
                that.addError("argument to covariant type parameter of enumerated supertype must be a type parameter or 'Nothing': " + 
                        typeDescription(tp, unit));
            }
        }
        else if (tp.isContravariant()) {
            List<Type> sts = tp.getSatisfiedTypes();
            //TODO: do I need to do type arg substitution here??
            Type ub = intersectionOfSupertypes(tp);
            if (!(arg.isExactly(ub))) {
                that.addError("argument to contravariant type parameter of enumerated supertype must be a type parameter or '" + 
                        typeNamesAsIntersection(sts, unit) + "': " + 
                        typeDescription(tp, unit));
            }
        }
        else {
            that.addError("argument to type parameter of enumerated supertype must be a type parameter: " + 
                    typeDescription(tp, unit));
        }
    }
    
    @Override 
    public void visit(Tree.ExtendedType that) {
        super.visit(that);
        
        TypeDeclaration td = 
                (TypeDeclaration) 
                    that.getScope();
        if (!td.isAlias()) {
            Tree.SimpleType et = that.getType();
            if (et!=null) {
                Tree.InvocationExpression ie = 
                        that.getInvocationExpression();
                Class clazz = (Class) td;
                boolean hasConstructors = 
                        clazz.hasConstructors() || 
                        clazz.hasEnumerated();
                boolean anonymous = clazz.isAnonymous();
                if (ie==null) { 
                    if (!hasConstructors || anonymous) {
                        et.addError("missing instantiation arguments");
                    }
                }
                else {
                    if (hasConstructors && !anonymous) {
                        et.addError("unnecessary instantiation arguments");
                    }
                }
                
                Unit unit = that.getUnit();

                Type type = et.getTypeModel();
                if (type!=null) {
                    checkSelfTypes(et, td, type);
                    checkExtensionOfMemberType(et, td, type);
                    //checkCaseOfSupertype(et, td, type);
                    Type ext = 
                            td.getExtendedType();
                    TypeDeclaration etd = 
                            ext==null ? null :
                                ext.getDeclaration();
                    TypeDeclaration aetd = 
                            type.getDeclaration();
                    if (aetd instanceof Constructor &&
                            aetd.isAbstract()) {
                        et.addError("extends a partial constructor: '" +
                                aetd.getName(unit) + 
                                "' is declared abstract");
                    }
                    while (etd!=null && etd.isAlias()) {
                        Type etdet = 
                                etd.getExtendedType();
                        etd = etdet == null ? null :
                            etdet.getDeclaration();
                    }
                    if (etd!=null) {
                        if (etd.isFinal()) {
                            et.addError("extends a final class: '" + 
                                    etd.getName(unit) + 
                                    "' is declared final");
                        }
                        if (etd.isSealed() && 
                                !inSameModule(etd, unit)) {
                            String moduleName = 
                                    etd.getUnit()
                                        .getPackage()
                                        .getModule()
                                        .getNameAsString();
                            et.addError("extends a sealed class in a different module: '" +
                                    etd.getName(unit) + 
                                    "' in '" + moduleName + 
                                    "' is sealed");
                        }
                    }
                }
                checkSupertypeVarianceAnnotations(et);
            }
        }
    }

    @Override 
    public void visit(Tree.SatisfiedTypes that) {
        super.visit(that);
        TypeDeclaration td = 
                (TypeDeclaration) 
                    that.getScope();
        if (td.isAlias()) {
            return;
        }
        Set<TypeDeclaration> set = 
                new HashSet<TypeDeclaration>();
        if (td.getSatisfiedTypes().isEmpty()) {
            return; //handle undecidable case
        }
        
        Unit unit = that.getUnit();
        
        for (Tree.StaticType t: that.getTypes()) {
            Type type = t.getTypeModel();
            if (!isTypeUnknown(type)) {
                type = type.resolveAliases();
                TypeDeclaration dec = type.getDeclaration();
                if (td instanceof ClassOrInterface &&
                        !inLanguageModule(unit)) {
                    if (unit.isCallableType(type)) {
                        t.addError("satisfies 'Callable'");
                    }
                    TypeDeclaration cad = 
                            unit.getConstrainedAnnotationDeclaration();
                    if (dec.equals(cad)) {
                        t.addError("directly satisfies 'ConstrainedAnnotation'");
                    }
                }
                if (!set.add(dec)) {
                    //this error is not really truly necessary
                    //but the spec says it is an error, and
                    //the backend doesn't like it
                    t.addError("duplicate satisfied type: '" + 
                            dec.getName(unit) + "' of '" + 
                            td.getName() + "'");
                }
                if (td instanceof ClassOrInterface) {
                    TypeDeclaration std = 
                            dec;
                    if (std.isSealed() && 
                            !inSameModule(std, unit)) {
                        String moduleName = 
                                std.getUnit()
                                    .getPackage()
                                    .getModule()
                                    .getNameAsString();
                        t.addError("satisfies a sealed interface in a different module: '" +
                                std.getName(unit) + "' in '" + 
                                moduleName + "'");
                    }
                }
                checkSelfTypes(t, td, type);
                checkExtensionOfMemberType(t, td, type);
                /*if (!(td instanceof TypeParameter)) {
                    checkCaseOfSupertype(t, td, type);
                }*/
            }
            if (t instanceof Tree.SimpleType) {
                Tree.SimpleType st = (Tree.SimpleType) t;
                checkSupertypeVarianceAnnotations(st);
            }
        }
    }

    @Override 
    public void visit(Tree.CaseTypes that) {
        super.visit(that);
        
        //this forces every case to be a subtype of the
        //enumerated type, so that we can make use of the
        //enumerated type is equivalent to its cases
        TypeDeclaration td = 
                (TypeDeclaration) 
                    that.getScope();
        
        //TODO: get rid of this awful hack:
        List<Type> cases = td.getCaseTypes();
        td.setCaseTypes(null);
        
        if (td instanceof TypeParameter) {
            for (Tree.StaticType t: that.getTypes()) {
                for (Tree.StaticType ot: that.getTypes()) {
                    if (t==ot) break;
                    checkCasesDisjoint(
                            t.getTypeModel(), 
                            ot.getTypeModel(), 
                            ot);
                }
            }
        }
        else {
            collectCaseTypes(that, td);
            collectCaseValues(that, td);
        }
        
        //TODO: get rid of this awful hack:
        td.setCaseTypes(cases);
    }

    void collectCaseValues(Tree.CaseTypes that, 
            TypeDeclaration td) {
        Unit unit = that.getUnit();
        Set<Declaration> valueSet = 
                new HashSet<Declaration>();
        for (Tree.BaseMemberExpression bme: 
                that.getBaseMemberExpressions()) {
            TypedDeclaration value = 
                    getTypedDeclaration(bme.getScope(), 
                            name(bme.getIdentifier()), 
                            null, false, unit);
            if (value!=null) {
                if (value!=null && !valueSet.add(value)) {
                    //this error is not really truly necessary
                    bme.addError("duplicate case: '" + 
                            value.getName(unit) + 
                            "' of '" + td.getName() + "'");
                }
                Type type = value.getType();
                if (type!=null) {
                    TypeDeclaration caseDec = 
                            type.getDeclaration();
                    if (caseDec instanceof Constructor) {
                        Scope scope = caseDec.getContainer();
                        if (scope instanceof Class) {
                            //enumerated singleton constructors
                            Constructor cons = 
                                    (Constructor) caseDec;
                            Class c = (Class) scope;
                            if (!c.isToplevel()) {
                                bme.addError("case must be a value constructor of a toplevel class: '" + 
                                        c.getName(unit) + 
                                        "' is not toplevel");
                            }
                            else if (!cons.getParameterLists().isEmpty()) {
                                bme.addError("case must be a value constructor of a toplevel class: '" + 
                                        cons.getName(unit) + 
                                        "' is not a value constructor");
                            }
                            /*else if (!c.inherits(unit.getIdentifiableDeclaration())) {
                                bme.addError("case must be a value constructor of an identifiable class: '" + 
                                        c.getName(unit) + 
                                        "' is not a subtype of 'Identifiable'");
                            }*/
                        }
                    }
                    else {
                        //enumerated anonymous subclasses
                        if (!caseDec.isObjectClass()) {
                            bme.addError("case must be a toplevel anonymous class: '" + 
                                    value.getName(unit) + 
                                    "' is not an anonymous class");
                        }
                        else if (!value.isToplevel()) {
                            bme.addError("case must be a toplevel anonymous class: '" + 
                                    value.getName(unit) + 
                                    "' is not toplevel");
                        }
                    }
                    if (checkDirectSubtype(td, bme, type)) {
                        checkAssignable(type, td.getType(), bme, 
                                getCaseTypeExplanation(td, type));
                    }
                }
            }
        }
    }

    void collectCaseTypes(Tree.CaseTypes that, TypeDeclaration td) {
        Set<TypeDeclaration> typeSet = 
                new HashSet<TypeDeclaration>();
        for (Tree.StaticType ct: that.getTypes()) {
            Type type = ct.getTypeModel();
            if (!isTypeUnknown(type)) {
                type = type.resolveAliases();
                TypeDeclaration ctd = type.getDeclaration();
                if (!typeSet.add(ctd)) {
                    //this error is not really truly necessary
                    Unit unit = that.getUnit();
                    ct.addError("duplicate case type: '" + 
                            ctd.getName(unit) + "' of '" + 
                            td.getName() + "'");
                }
                if (!(ctd instanceof TypeParameter)) {
                    //it's not a self type
                    if (checkDirectSubtype(td, ct, type)) {
                        checkAssignable(type, td.getType(), ct,
                                getCaseTypeExplanation(td, type));
                    }
                    //note: this is a better, faster way to call 
                    //      validateEnumeratedSupertypeArguments()
                    //      but unfortunately it winds up displaying
                    //      the error on the wrong node, confusing
                    //      the user
                    /*
                    Type supertype = 
                            type.getDeclaration()
                                .getType()
                                .getSupertype(td);
                    validateEnumeratedSupertypeArguments(t, 
                            type.getDeclaration(), supertype);
                    */
                }
                checkCaseType(td, ct, ctd);
            }
        }
    }

    void checkCaseType(TypeDeclaration type, 
            Tree.StaticType ct,
            TypeDeclaration caseTypeDec) {
        if (caseTypeDec instanceof ClassOrInterface && 
                ct instanceof Tree.SimpleType) {
            Tree.SimpleType t = (Tree.SimpleType) ct;
            Tree.TypeArgumentList tal = 
                    t.getTypeArgumentList();
            if (tal!=null) {
                List<Tree.Type> args = 
                        tal.getTypes();
                List<TypeParameter> typeParameters = 
                        caseTypeDec.getTypeParameters();
                for (int i=0; 
                        i<args.size() && 
                        i<typeParameters.size(); 
                        i++) {
                    Tree.Type arg = args.get(i);
                    TypeParameter typeParameter = 
                            caseTypeDec.getTypeParameters()
                                .get(i);
                    Type argType = 
                            arg.getTypeModel();
                    if (argType!=null) {
                        TypeDeclaration argTypeDec = 
                                argType.getDeclaration();
                        if (argType.isTypeParameter()) {
                            TypeParameter tp = 
                                    (TypeParameter) 
                                        argTypeDec;
                            if (!tp.getDeclaration()
                                    .equals(type)) {
                                arg.addError("type argument is not a type parameter of the enumerated type: '" +
                                        argTypeDec.getName() + 
                                        "' is not a type parameter of '" + 
                                        type.getName());
                            }
                        }
                        else if (typeParameter.isCovariant()) {
                            checkAssignable(
                                    typeParameter.getType(), 
                                    argType, arg, 
                                    "type argument not an upper bound of the type parameter");
                        }
                        else if (typeParameter.isContravariant()) {
                            checkAssignable(argType, 
                                    typeParameter.getType(), arg, 
                                    "type argument not a lower bound of the type parameter");
                        }
                        else {
                            arg.addError("type argument is not a type parameter of the enumerated type: '" +
                                    argTypeDec.getName() + "'");
                        }
                    }
                }
            }
        }
    }

    @Override 
    public void visit(Tree.DelegatedConstructor that) {
        super.visit(that);
        
        TypeDeclaration constructor = 
                (TypeDeclaration) 
                    that.getScope();
        Scope container = constructor.getContainer();
        Tree.SimpleType type = that.getType();
        if (type!=null &&
                constructor instanceof Constructor &&
                container instanceof Class) {
            Class containingClass = (Class) container;
            Type et = 
                    containingClass.getExtendedType();
            if (et!=null) {
                Unit unit = that.getUnit();
                Type extendedType = 
                        containingClass.getExtendedType();
                Type constructedType = 
                        type.getTypeModel();
                Declaration delegate = 
                        type.getDeclarationModel();
                TypeDeclaration superclass = 
                        et.getDeclaration();
                if (superclass instanceof Constructor) {
                    superclass = 
                            superclass.getExtendedType()
                                .getDeclaration();
                }
                if (delegate instanceof Constructor) {
                    Constructor c = (Constructor) delegate;
                    if (c.equals(constructor)) {
                        type.addError("constructor delegates to itself: '" +
                                c.getName() + "'");
                    }
                    Type delegatedType = 
                            c.getExtendedType();
                    TypeDeclaration delegated =
                            delegatedType == null ? null :
                                delegatedType.getDeclaration();
                    if (superclass.equals(delegated)) {
                        checkIsExactly(
                                constructedType.getExtendedType(), 
                                extendedType, type, 
                                "type arguments must match type arguments in extended class expression");
                    }
                    else if (containingClass.equals(delegated)) {
                        if (type instanceof Tree.QualifiedType) {
                            Tree.QualifiedType qt = 
                                    (Tree.QualifiedType) 
                                        type;
                            checkIsExactly(
                                    constructedType.getQualifyingType(), 
                                    containingClass.getType(), 
                                    qt.getOuterType(), 
                                    "type arguments must be the type parameters of this class");
                        }
                    }
                    else {
                        type.addError("not a constructor of the immediate superclass: '" +
                                delegate.getName(unit) + 
                                "' is not a constructor of '" + 
                                superclass.getName(unit) + "'");
                    }
                }
                else if (delegate instanceof Class) {
                    if (superclass.equals(delegate)) {
                        checkIsExactly(constructedType, 
                                extendedType, type, 
                                "type arguments must match type arguments in extended class expression");
                    }
                    else if (containingClass.equals(delegate)) {
                        checkIsExactly(constructedType, 
                                containingClass.getType(), type, 
                                "type arguments must be the type parameters of this class");
                    }
                    else {
                        type.addError("does not instantiate the immediate superclass: '" +
                                delegate.getName(unit) + "' is not '" + 
                                superclass.getName(unit) + "'");
                    }
                }
            }
        }
    }

    private static boolean checkDirectSubtype(TypeDeclaration td, 
            Node node, Type type) {
        boolean found = false;
        TypeDeclaration ctd = type.getDeclaration();
        if (td instanceof Interface) {
            for (Type st: ctd.getSatisfiedTypes()) {
                if (st!=null && 
                        st.resolveAliases()
                            .getDeclaration()
                            .equals(td)) {
                    found = true;
                }
            }
        }
        else if (td instanceof Class) {
            Type et = ctd.getExtendedType();
            if (et!=null && 
                    et.resolveAliases()
                        .getDeclaration()
                        .equals(td)) {
                found = true;
            }
        }
        if (!found) {
            node.addError("case type is not a direct subtype of enumerated type: " + 
                    ctd.getName(node.getUnit()));
        }
        return found;
    }

    private String getCaseTypeExplanation(TypeDeclaration td, 
            Type type) {
        String message = "case type must be a subtype of enumerated type";
        if (!td.getTypeParameters().isEmpty() &&
                type.getDeclaration().inherits(td)) {
            message += " for every type argument of the generic enumerated type";
        }
        return message;
    }

    private void checkExtensionOfMemberType(Node that, 
            TypeDeclaration td, Type type) {
        Type qt = type.getQualifyingType();
        if (qt!=null && td instanceof ClassOrInterface) {
            Unit unit = that.getUnit();
            TypeDeclaration d = type.getDeclaration();
            if (d.isStaticallyImportable() ||
                    d instanceof Constructor) {
                checkExtensionOfMemberType(that, td, qt);
            }
            else {
                Scope s = td;
                while (s!=null) {
                    s = s.getContainer();
                    if (s instanceof TypeDeclaration) {
                        TypeDeclaration otd = 
                                (TypeDeclaration) s;
                        if (otd.getType().isSubtypeOf(qt)) {
                            return;
                        }
                    }
                }
                that.addError("qualifying type '" + qt.asString(unit) + 
                        "' of supertype '" + type.asString(unit) + 
                        "' is not an outer type or supertype of any outer type of '" +
                        td.getName(unit) + "'");
            }
        }
    }
    
    private void checkSelfTypes(Tree.StaticType that, 
            TypeDeclaration td, Type type) {
        if (!(td instanceof TypeParameter)) { //TODO: is this really ok?!
            List<TypeParameter> params = 
                    type.getDeclaration()
                        .getTypeParameters();
            List<Type> args = 
                    type.getTypeArgumentList();
            Unit unit = that.getUnit();
            for (int i=0; i<params.size(); i++) {
                TypeParameter param = params.get(i);
                if ( param.isSelfType() && args.size()>i ) {
                    Type arg = args.get(i);
                    if (arg==null) {
                        arg = new UnknownType(unit).getType(); 
                    }
                    TypeDeclaration std = 
                            param.getSelfTypedDeclaration();
                    Type at;
                    TypeDeclaration mtd;
                    if (param.getContainer().equals(std)) {
                        at = td.getType();
                        mtd = td;
                    }
                    else {
                        //TODO: lots wrong here?
                        mtd = (TypeDeclaration) 
                                td.getMember(std.getName(), 
                                        null, false);
                        at = mtd==null ? null : mtd.getType();
                    }
                    if (at!=null && !at.isSubtypeOf(arg) && 
                            !(mtd.getSelfType()!=null && 
                              mtd.getSelfType().isExactly(arg))) {
                        String help = "";
                        TypeDeclaration ad = arg.getDeclaration();
                        if (ad instanceof TypeParameter) {
                            TypeParameter tp = (TypeParameter) ad;
                            if (tp.getDeclaration().equals(td)) {
                                help = " (try making " + ad.getName() + 
                                        " a self type of " + td.getName() + ")";
                            }
                        }
                        else if (ad instanceof Interface) {
                            help = " (try making " + td.getName() + 
                                    " satisfy " + ad.getName() + ")";
                        }
                        else if (ad instanceof Class && td instanceof Class) {
                            help = " (try making " + td.getName() + 
                                    " extend " + ad.getName() + ")";
                        }
                        that.addError("type argument does not satisfy self type constraint on type parameter '" +
                                param.getName() + "' of '" + 
                                type.getDeclaration().getName(unit) + "': '" +
                                arg.asString(unit) + 
                                "' is not a supertype or self type of '" + 
                                td.getName(unit) + "'" + help);
                    }
                }
            }
        }
    }

    private void checkSupertypeVarianceAnnotations(Tree.SimpleType et) {
        Tree.TypeArgumentList tal = 
                et.getTypeArgumentList();
        if (tal!=null) {
            for (Tree.Type t: tal.getTypes()) {
                if (t instanceof Tree.StaticType) {
                    Tree.StaticType st = (Tree.StaticType) t;
                    Tree.TypeVariance variance = 
                            st.getTypeVariance();
                    if (variance!=null) {
                        variance.addError("supertype expression may not specify variance");
                    }
                }
            }
        }
    }
    
    @Override
    public void visit(Tree.Enumerated that) {
        super.visit(that);
        Value v = that.getDeclarationModel();
        Scope container = v.getContainer();
        if (container instanceof Class) {
            Class cl = (Class) container;
            List<TypedDeclaration> caseValues = 
                    cl.getCaseValues();
            if (caseValues!=null 
                    && !caseValues.contains(v) && 
                    !cl.isAbstract()) {
                that.addError("value constructor does not occur in of clause of non-abstract enumerated class: '" +
                        v.getName() + 
                        "' is not listed in the of clause of '" + 
                        cl.getName() + "'");
            }
        }
    }
    
    @Override
    public void visit(Tree.Constructor that) {
        super.visit(that);
        Constructor c = that.getConstructor();
        Scope container = 
                c.getContainer();
        if (container instanceof Class) {
            Class cl = (Class) container;
            List<TypedDeclaration> caseValues = 
                    cl.getCaseValues();
            if (caseValues!=null && 
                    !c.isAbstract() &&
                    !cl.isAbstract()) {
                that.addError("non-abstract enumerated class may not have non-partial callable constructor");
            }
        }
    }

}
