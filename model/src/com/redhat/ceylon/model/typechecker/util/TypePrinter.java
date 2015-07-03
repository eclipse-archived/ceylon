package com.redhat.ceylon.model.typechecker.util;

import static com.redhat.ceylon.model.typechecker.model.ModelUtil.isElementOfUnion;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.union;

import java.util.List;

import com.redhat.ceylon.model.typechecker.model.Class;
import com.redhat.ceylon.model.typechecker.model.Declaration;
import com.redhat.ceylon.model.typechecker.model.Interface;
import com.redhat.ceylon.model.typechecker.model.Package;
import com.redhat.ceylon.model.typechecker.model.Scope;
import com.redhat.ceylon.model.typechecker.model.Type;
import com.redhat.ceylon.model.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.model.typechecker.model.TypeParameter;
import com.redhat.ceylon.model.typechecker.model.Unit;

/**
 * Pretty-prints a type expression.
 * @see com.redhat.ceylon.model.loader.TypeParser
 */
public class TypePrinter {

    public static final TypePrinter DEFAULT = 
            new TypePrinter(true, true, false, true, false);

    public static final TypePrinter ESCAPED = 
            new TypePrinter(true, true, false, true, true);

    private final boolean printAbbreviated;
    private final boolean printTypeParameters;
    private final boolean printTypeParameterDetail;
    private final boolean printQualifyingType;
    private final boolean printQualifier;
    private final boolean printFullyQualified;
    private final boolean escapeLowercased;
    
    public TypePrinter() {
        this(false, false, false, false, false, false, false);
    }

    public TypePrinter(boolean printAbbreviated) {
        this(printAbbreviated, true, false, true, false);
    }

    public TypePrinter(boolean printAbbreviated, 
            boolean printTypeParameters, 
            boolean printTypeParameterDetail,
            boolean printQualifyingType,
            boolean escapeLowercased) {
        this(printAbbreviated, 
                printTypeParameters, 
                printTypeParameterDetail, 
                printQualifyingType, 
                escapeLowercased, false, false);
    }
    
    public TypePrinter(boolean printAbbreviated, 
            boolean printTypeParameters, 
            boolean printTypeParameterDetail,
            boolean printQualifyingType,
            boolean escapeLowercased,
            boolean printFullyQualified,
            boolean printQualifier) {
        this.printAbbreviated = printAbbreviated;
        this.printTypeParameters = printTypeParameters;
        this.printTypeParameterDetail = printTypeParameterDetail;
        this.printQualifyingType = printQualifyingType;
        this.escapeLowercased = escapeLowercased;
        this.printFullyQualified = printFullyQualified;
        this.printQualifier = printQualifier;
    }
    
    protected boolean printAbbreviated() {
        return printAbbreviated;
    }

    protected boolean printTypeParameters() {
        return printTypeParameters;
    }

    protected boolean printTypeParameterDetail() {
        return printTypeParameterDetail;
    }

    protected boolean printQualifyingType() {
        return printQualifyingType;
    }

    protected boolean printQualifier() {
        return printQualifier;
    }

    protected boolean printFullyQualified() {
        return printFullyQualified;
    }
    
    protected String lt() {
        return "<";
    }

    protected String gt() {
        return ">";
    }
    
    protected String amp() {
        return "&";
    }

    public String print(Type pt, Unit unit) {
        if (pt==null) {
            return "unknown";
        }
        else {
            if (printAbbreviated() && !pt.isTypeAlias()) {
                //TODO: we're going to have to fix this!
                Unit u = pt.getDeclaration().getUnit();
                if (abbreviateOptional(pt)) {
                    Type dt = pt.eliminateNull();
                    String dtn = print(dt, unit);
                    if (isPrimitiveAbbreviatedType(dt)) {
                        return dtn + "?";
                    }
                    else {
                        return lt() + dtn + gt() + "?";
                    }
                }
                if (abbreviateEmpty(pt)) {
                    return "[]";
                }
                if (abbreviateHomoTuple(pt)) {
                    Type et = 
                            u.getSequentialElementType(pt);
                    String etn = print(et, unit);
                    int len = u.getHomogeneousTupleLength(pt);
                    return etn +  "[" + len + "]";
                }
                if (abbreviateSequential(pt)) {
                    Type it = u.getIteratedType(pt);
                    String etn = print(it, unit);
                    if (isPrimitiveAbbreviatedType(it)) {
                        return etn + "[]";
                    }
                    else {
                        return lt() + etn + gt() + "[]";
                    }
                }
                if (abbreviateSequence(pt)) {
                    Type it = u.getIteratedType(pt);
                    String etn = print(it, unit);
                    if (isPrimitiveAbbreviatedType(it) || 
                            it.isUnion() || it.isIntersection()) {
                        return "[" + etn + "+]";
                    }
                    else {
                        return "[" + lt() + etn + gt() + "+]";
                    }
                }
                if (abbreviateIterable(pt)) {
                    Type it = u.getIteratedType(pt);
                    Type nt = pt.getTypeArgumentList().get(1);
                    /*if (it.isNothing() && !nt.isNothing()) {
                    	return "{}";
                    }*/
                    String itn = print(it, unit);
                    String many = nt.isNothing() ? "+" : "*";
                    if (isPrimitiveAbbreviatedType(it) || 
                            it.isUnion() || it.isIntersection()) {
                        return "{" + itn + many + "}";
                    }
                    else {
                        return "{" + lt() + itn + gt() + many + "}";
                    }
                }
                if (abbreviateEntry(pt)) {
                    Type kt = u.getKeyType(pt);
                    Type vt = u.getValueType(pt);
                    return print(kt, unit) + 
                            "-" + gt()
                            + print(vt, unit);
                }
                if (abbreviateCallable(pt)) {
                    List<Type> tal = 
                            pt.getTypeArgumentList();
                    Type rt = tal.get(0);
                    Type at = tal.get(1);
                    if (abbreviateCallableArg(at)) {
                        String paramTypes = 
                                getTupleElementTypeNames(at, unit);
                        if (rt!=null && paramTypes!=null) {
                            String rtn = 
                                    print(rt, unit);
                            if (!isPrimitiveAbbreviatedType(rt)) {
                                rtn = lt() + rtn + gt();
                            }
                            return rtn + "(" + paramTypes + ")";
                        }
                    }
                    else {
                        if (rt!=null && at!=null) {
                            String rtn = 
                                    print(rt, unit);
                            String atn = 
                                    print(at, unit);
                            if (!isPrimitiveAbbreviatedType(at)) {
                                atn = lt() + atn + gt();
                            }
                            if (!isPrimitiveAbbreviatedType(rt)) {
                                rtn = lt() + rtn + gt();
                            }
                            return rtn + "(*" + atn + ")";
                        }
                    }
                }
                if (abbreviateTuple(pt)) {
                    String elemTypes = getTupleElementTypeNames(pt, unit);
                    if (elemTypes!=null) {
                        return "[" + elemTypes + "]";
                    }
                }
            }
            if (pt.isUnion()) {
                StringBuilder name = new StringBuilder();
                boolean first = true;
                for (Type caseType: 
                        pt.getCaseTypes()) {
                    if (first) {
                        first = false;
                    }
                    else {
                        name.append("|");
                    }
                    if (caseType==null) {
                        name.append("unknown");
                    }
                    else if (printAbbreviated() && 
                            abbreviateEntry(caseType)) {
                        name.append(lt())
                            .append(print(caseType, unit))
                            .append(gt());
                    }
                    else {
                        name.append(print(caseType, unit));
                    }
                }
                return name.toString();
            }
            else if (pt.isIntersection()) {
                StringBuilder name = new StringBuilder();
                boolean first = true;
                for (Type satisfiedType: 
                        pt.getSatisfiedTypes()) {
                    if (first) {
                        first = false;
                    }
                    else {
                        name.append(amp());
                    }
                    if (satisfiedType==null) {
                        name.append("unknown");
                    }
                    else if (printAbbreviated() && 
                            abbreviateEntry(satisfiedType) || 
                            satisfiedType.isUnion()) {
                        name.append(lt())
                            .append(print(satisfiedType, unit))
                            .append(gt());
                    }
                    else {
                        name.append(print(satisfiedType, unit));
                    }
                }
                return name.toString();
            }
            else if (pt.isTypeParameter()) {
                StringBuilder name = new StringBuilder();
                TypeParameter tp = 
                        (TypeParameter) 
                            pt.getDeclaration();

                if (printTypeParameterDetail() && 
                        tp.isContravariant()) {
                    name.append("in ");
                }
                if (printTypeParameterDetail() && 
                        tp.isCovariant()) {
                    name.append("out ");
                }

                name.append(getSimpleProducedTypeName(pt, unit));

                if (printTypeParameterDetail() && 
                        tp.isDefaulted()) {
                    Type dta = 
                            tp.getDefaultTypeArgument();
                    if (dta == null) {
                        name.append("=");
                    }
                    else {
                        name.append(" = ")
                            .append(print(dta, unit));
                    }
                }

                return name.toString();
            }
            else {
                TypeDeclaration declaration = 
                        pt.getDeclaration();
                if (declaration.isAlias() && 
                        declaration.isAnonymous()) {
                    StringBuilder name = new StringBuilder();
                    if (pt.isTypeConstructor()) {
                        name.append(lt());
                        TypeParameter tpc = 
                                pt.getTypeConstructorParameter();
                        List<TypeParameter> params = 
                                (tpc == null ? declaration : tpc)
                                .getTypeParameters();
                        for (TypeParameter tp: params) {
                            if (name.length()>lt().length()) {
                                name.append(", ");
                            }
                            if (tp.isCovariant()) {
                                name.append("out ");
                            }
                            if (tp.isContravariant()) {
                                name.append("in ");
                            }
                            printDeclaration(name, tp, 
                                    printFullyQualified(), unit);
                        }
                        name.append(gt());
                        appendConstraintsString(pt, name, unit);
                        name.append(" =").append(gt()).append(" ");
                    }
                    Type aliasedType =
                            declaration.getExtendedType()
                            .substitute(pt);
                    name.append(print(aliasedType, unit));
                    return name.toString();
                }
                else {            
                    return getSimpleProducedTypeName(pt, unit);
                }
            }
        }
    }

    private boolean abbreviateHomoTuple(Type pt) {
        Unit unit = pt.getDeclaration().getUnit();
        return unit.getHomogeneousTupleLength(pt)>1;
    }

    public static boolean abbreviateEntry(Type pt) {
        Unit unit = pt.getDeclaration().getUnit();
        Class ed = unit.getEntryDeclaration();
        if (pt.isClass() &&
                pt.getDeclaration().equals(ed) &&
                pt.getTypeArgumentList().size()==2) {
            Type kt = unit.getKeyType(pt);
            Type vt = unit.getValueType(pt);
            return kt!=null && vt!=null && 
                    (!kt.isClass() ||
                            !kt.getDeclaration().equals(ed)) &&
                    (!vt.isClass() ||
                            !vt.getDeclaration().equals(ed)); /*&&
                    kt.isPrimitiveAbbreviatedType() && 
                    vt.isPrimitiveAbbreviatedType();*/
        }
        else {
            return false;
        }
    }

    public static boolean abbreviateEmpty(Type pt) {
        return pt.isEmpty();
    }

    public static boolean abbreviateOptional(Type pt) {
        if (pt.isUnion()) {
            TypeDeclaration dec = pt.getDeclaration();
            Unit unit = dec.getUnit();
            return pt.getCaseTypes().size()==2 &&
                    isElementOfUnion(pt, unit.getNullDeclaration()); /*&&
                    minus(unit.getNullDeclaration()).isPrimitiveAbbreviatedType();*/
        }
        else {
            return false;
        }
    }    

    public static boolean abbreviateTuple(Type pt) {
        return pt.isTuple() && isTupleTypeWellformed(pt);
    }

    public static boolean abbreviateCallable(Type pt) {
        if (pt.isInterface()) {
            TypeDeclaration dec = pt.getDeclaration();
            Interface callableDeclaration = 
                    dec.getUnit()
                        .getCallableDeclaration();
            return  dec.equals(callableDeclaration) &&
                    pt.getTypeArgumentList().size()==2 && 
                    pt.getTypeArgumentList().get(0)!=null /*&& 
                    abbreviateCallableArg(pt.getTypeArgumentList().get(1))*/;
        }
        else {
            return false;
        }
    }
    
    private static boolean abbreviateCallableArg(Type at) {
        if (at.isUnion()) {
            List<Type> caseTypes = at.getCaseTypes();
            return caseTypes.size()==2 &&
                    abbreviateEmpty(caseTypes.get(0)) &&
                    abbreviateCallableArg(caseTypes.get(1));
        }
        else {
            return abbreviateEmpty(at) || 
                    abbreviateSequence(at) || 
                    abbreviateSequential(at) ||
                    abbreviateTuple(at);
        }
    }

    public static boolean abbreviateSequence(Type pt) {
        if (pt.isInterface()) {
            TypeDeclaration dec = pt.getDeclaration();
            if (dec.isSequence()) {
                Type et = 
                        dec.getUnit().getIteratedType(pt);
                return et!=null;// && et.isPrimitiveAbbreviatedType();
            }
        }
        return false;
    }

    public static boolean abbreviateSequential(Type pt) {
        if (pt.isInterface()) {
            TypeDeclaration ptd = pt.getDeclaration();
            if (ptd.isSequential()) {
                Type et = 
                        ptd.getUnit().getIteratedType(pt);
                return et!=null;// && et.isPrimitiveAbbreviatedType();
            }
        }
        return false;
    }

    public static boolean abbreviateIterable(Type pt) {
        if (pt.isInterface()) {
            TypeDeclaration ptd = pt.getDeclaration();
            if (ptd.isIterable()) {
                Type et = 
                        ptd.getUnit().getIteratedType(pt);
                List<Type> typeArgs = 
                        pt.getTypeArgumentList();
                if (et!=null && typeArgs.size()==2) {
                    Type at = typeArgs.get(1);
                    if (at!=null) {
                        return at.isNothing() || at.isNull();
                    }
                }// && et.isPrimitiveAbbreviatedType();
            }
        }
        return false;
    }
    
    private static boolean isTupleTypeWellformed(Type args) {
        if (args==null || 
                args.getTypeArgumentList().size()<3) {
            return false;
        }
        Unit unit = args.getDeclaration().getUnit();
        List<Type> elemtypes = 
                unit.getTupleElementTypes(args);
        if (unit.isTupleLengthUnbounded(args)) {
            int lastIndex = elemtypes.size()-1;
            Type last = elemtypes.get(lastIndex);
            elemtypes.set(lastIndex, 
                    unit.getSequentialElementType(last));
        }
        if (elemtypes==null) {
            return false;
        }
        Type t = union(elemtypes, unit);
        Type typeArg = 
                args.getTypeArgumentList().get(0);
        if (typeArg==null) {
            return false;
        }
        else {
            return t.isExactly(typeArg);
        }
    }

    private String getTupleElementTypeNames(Type args, 
            Unit unit) {
        if (args!=null) {
            Unit u = args.getDeclaration().getUnit();
            boolean defaulted=false;
            if (args.isUnion()) {
                List<Type> cts = 
                        args.getCaseTypes();
                if (cts.size()==2) {
                    Type lc = cts.get(0);
                    if (lc.isEmpty()) {
                        args = cts.get(1);
                        defaulted = true;
                    }
                    Type rc = cts.get(1);
                    if (rc.isEmpty()) {
                        args = cts.get(0);
                        defaulted = true;
                    }
                }
            }
            if (args.isClassOrInterface()) {
                if (args.isTuple()) {
                    List<Type> tal = 
                            args.getTypeArgumentList();
                    if (tal.size()>=3) {
                        Type first = tal.get(1);
                        Type rest = tal.get(2);
                        if (first!=null && rest!=null) {
                            String argtype = 
                                    print(first, unit);
                            if (rest.isEmpty()) {
                                return defaulted ? 
                                        argtype + "=" : argtype;
                            }
                            String argtypes = 
                                    getTupleElementTypeNames(rest, unit);
                            if (argtypes!=null) {
                                return defaulted ? 
                                        argtype + "=, " + argtypes : 
                                            argtype + ", " + argtypes;
                            }
                        }
                    }
                }
                else if (args.isEmpty()) {
                    return defaulted ? "=" : "";
                }
                else if (!defaulted && args.isSequential()) {
                    Type elementType = 
                            u.getIteratedType(args);
                    if (elementType!=null) {
                        String elemtype = 
                                print(elementType, unit);
                        if (isPrimitiveAbbreviatedType(elementType)) {
                            return elemtype + "*";
                        }
                        else {
                            return lt() + elemtype + gt() + "*";
                        }
                    }
                }
                else if (!defaulted && args.isSequence()) {
                    Type elementType = 
                            u.getIteratedType(args);
                    if (elementType!=null) {
                        String elemtype = 
                                print(elementType, unit);
                        if (isPrimitiveAbbreviatedType(elementType)) {
                            return elemtype + "+";
                        }
                        else {
                            return lt() + elemtype + gt() + "+";
                        }
                    }
                }
            }
        }
        return null;
    }

    private boolean isPrimitiveAbbreviatedType(Type pt) {
        if (pt.isIntersection()) {
            return false;
        }
        else if (pt.isUnion()) {
            return abbreviateOptional(pt);
        }
        else {
            return !abbreviateEntry(pt);
        }
    }

    protected String getSimpleProducedTypeName(Type pt, 
            Unit unit) {
        StringBuilder ptn = new StringBuilder();

        boolean fullyQualified = printFullyQualified();
        if (printQualifyingType()) {
            Type qt = pt.getQualifyingType();
            if (qt != null) {
				boolean isComplex = 
				        qt.isIntersection() || 
				        qt.isUnion();
                if (isComplex) {
					ptn.append(lt());
	            }
                ptn.append(print(qt, unit));
    			if (isComplex) {
					ptn.append(gt());
	            }
    			ptn.append(".");
    			fullyQualified = false;
            }
        }

        TypeDeclaration ptd = pt.getDeclaration();
        printDeclaration(ptn, ptd, fullyQualified, unit);
        
        List<Type> args = pt.getTypeArgumentList();
        List<TypeParameter> params = ptd.getTypeParameters();
        if (!pt.isTypeConstructor() && 
                printTypeParameters() && 
                !args.isEmpty()) {
            ptn.append(lt());
            boolean first = true;
            for (int i=0; i<args.size()&&i<params.size(); i++) {
                Type t = args.get(i);
                TypeParameter p = params.get(i);
                if (first) {
                    first = false;
                }
                else {
                    ptn.append(",");
                }
                if (t==null) {
                    ptn.append("unknown");
                }
                else {
                    if (!p.isCovariant() && 
                            pt.isCovariant(p)) {
                        ptn.append("out ");
                    }
                    if (!p.isContravariant() && 
                            pt.isContravariant(p)) {
                        ptn.append("in ");
                    }
                    ptn.append(print(t, unit));
                }
            }
            ptn.append(gt());
        }
        return ptn.toString();
    }

    private void printDeclaration(StringBuilder ptn, 
            Declaration declaration, boolean fullyQualified, 
            Unit unit) {
        // type parameters are not fully qualified
        if (fullyQualified && 
                !(declaration instanceof TypeParameter)) {
            Scope container = declaration.getContainer();
            while(container != null
                    && !(container instanceof Package)
                    && !(container instanceof Declaration)) {
                container = container.getContainer();
            }
            if (container != null) {
                if (container instanceof Package) {
                    String q = container.getQualifiedNameString();
                    if (!q.isEmpty()) {
                        ptn.append(q).append("::");
                    }
                }
                else {
                    printDeclaration(ptn, 
                            (Declaration) container, 
                            fullyQualified, unit);
                    ptn.append(".");
                }
            }
        }
        if (printQualifier()) {
            String qualifier = declaration.getQualifier();
            if(qualifier != null)
                ptn.append(qualifier);
        }
        ptn.append(getSimpleDeclarationName(declaration, unit));
    }

    protected String getSimpleDeclarationName(
            Declaration declaration, Unit unit) {
        String name = declaration.getName(unit);
        if (escapeLowercased) {
            int firstCodePoint = name.codePointAt(0);
            if (!Character.isUpperCase(firstCodePoint)) {
                name = "\\I" + name;
            }
        }
        return name;
    }

    private void appendConstraintsString(Type pt,
            StringBuilder result, Unit unit) {
        TypeParameter tpc = 
                pt.getTypeConstructorParameter();
        List<TypeParameter> params = 
                tpc==null ?
                    pt.getDeclaration()
                        .getTypeParameters() :
                    tpc.getTypeParameters();
        for (TypeParameter tp: params) {
            List<Type> sts = tp.getSatisfiedTypes();
            List<Type> cts = tp.getCaseTypes();
            boolean hasEnumeratedBounds = 
                    cts!=null && !cts.isEmpty();
            boolean hasUpperBounds = !sts.isEmpty();
            if (hasUpperBounds || hasEnumeratedBounds) {
                result.append(" given ");
                printDeclaration(result, tp, 
                        printFullyQualified(), unit);
                if (hasEnumeratedBounds) {
                    result.append(" of ");
                    boolean first = true;
                    for (Type ct: cts) {
                        if (first) {
                            first = false;
                        }
                        else {
                            result.append("|");
                        }
                        result.append(print(ct,unit));
                    }
                }
                if (hasUpperBounds) {
                    result.append(" satisfies ");
                    boolean first = true;
                    for (Type st: sts) {
                        if (first) {
                            first = false;
                        }
                        else {
                            result.append(amp());
                        }
                        result.append(print(st,unit));
                    }
                }
            }
        }
    }

}