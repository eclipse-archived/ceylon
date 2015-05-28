package com.redhat.ceylon.model.typechecker.util;

import static com.redhat.ceylon.model.typechecker.model.Util.isElementOfUnion;

import java.util.List;

import com.redhat.ceylon.model.typechecker.model.Class;
import com.redhat.ceylon.model.typechecker.model.Declaration;
import com.redhat.ceylon.model.typechecker.model.Interface;
import com.redhat.ceylon.model.typechecker.model.Package;
import com.redhat.ceylon.model.typechecker.model.ProducedType;
import com.redhat.ceylon.model.typechecker.model.Scope;
import com.redhat.ceylon.model.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.model.typechecker.model.TypeParameter;
import com.redhat.ceylon.model.typechecker.model.UnionType;
import com.redhat.ceylon.model.typechecker.model.Unit;

public class ProducedTypeNamePrinter {

    public static final ProducedTypeNamePrinter DEFAULT = 
            new ProducedTypeNamePrinter(true, true, false, true, false);

    public static final ProducedTypeNamePrinter ESCAPED = 
            new ProducedTypeNamePrinter(true, true, false, true, true);

    private boolean printAbbreviated;
    private boolean printTypeParameters;
    private boolean printTypeParameterDetail;
    private boolean printQualifyingType;
    private boolean printQualifier;
    private boolean printFullyQualified;
    private boolean escapeLowercased;
    
    public ProducedTypeNamePrinter() {}

    public ProducedTypeNamePrinter(boolean printAbbreviated) {
        this(printAbbreviated, true, false, true, false);
    }

    public ProducedTypeNamePrinter(boolean printAbbreviated, 
            boolean printTypeParameters, 
            boolean printTypeParameterDetail,
            boolean printQualifyingType,
            boolean escapeLowercased) {
        this.printAbbreviated = printAbbreviated;
        this.printTypeParameters = printTypeParameters;
        this.printTypeParameterDetail = printTypeParameterDetail;
        this.printQualifyingType = printQualifyingType;
        this.escapeLowercased = escapeLowercased;
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

    public String getProducedTypeName(ProducedType pt, Unit unit) {
        if (pt==null) {
            return "unknown";
        }
        else {
            if (printAbbreviated() && !pt.isTypeAlias()) {
                //TODO: we're going to have to fix this!
                Unit u = pt.getDeclaration().getUnit();
                if (abbreviateOptional(pt)) {
                    ProducedType dt = pt.eliminateNull();
                    String dtn = getProducedTypeName(dt, unit);
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
                    ProducedType et = 
                            u.getSequentialElementType(pt);
                    String etn = getProducedTypeName(et, unit);
                    int len = u.getHomogeneousTupleLength(pt);
                    return etn +  "[" + len + "]";
                }
                if (abbreviateSequential(pt)) {
                    ProducedType it = u.getIteratedType(pt);
                    String etn = getProducedTypeName(it, unit);
                    if (isPrimitiveAbbreviatedType(it)) {
                        return etn + "[]";
                    }
                    else {
                        return lt() + etn + gt() + "[]";
                    }
                }
                if (abbreviateSequence(pt)) {
                    ProducedType it = u.getIteratedType(pt);
                    String etn = getProducedTypeName(it, unit);
                    if (isPrimitiveAbbreviatedType(it) || 
                            it.isUnion() || it.isIntersection()) {
                        return "[" + etn + "+]";
                    }
                    else {
                        return "[" + lt() + etn + gt() + "+]";
                    }
                }
                if (abbreviateIterable(pt)) {
                    ProducedType it = u.getIteratedType(pt);
                    ProducedType nt = pt.getTypeArgumentList().get(1);
                    if (it.isNothing() && !nt.isNothing()) {
                    	return "{}";
                    }
                    String itn = getProducedTypeName(it, unit);
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
                    ProducedType kt = u.getKeyType(pt);
                    ProducedType vt = u.getValueType(pt);
                    return getProducedTypeName(kt, unit) + 
                            "-" + gt()
                            + getProducedTypeName(vt, unit);
                }
                if (abbreviateCallable(pt)) {
                    List<ProducedType> tal = 
                            pt.getTypeArgumentList();
                    ProducedType rt = tal.get(0);
                    ProducedType at = tal.get(1);
                    if (abbreviateCallableArg(at)) {
                        String paramTypes = 
                                getTupleElementTypeNames(at, unit);
                        if (rt!=null && paramTypes!=null) {
                            String rtn = 
                                    getProducedTypeName(rt, unit);
                            if (!isPrimitiveAbbreviatedType(rt)) {
                                rtn = lt() + rtn + gt();
                            }
                            return rtn + "(" + paramTypes + ")";
                        }
                    }
                    else {
                        if (rt!=null && at!=null) {
                            String rtn = 
                                    getProducedTypeName(rt, unit);
                            String atn = 
                                    getProducedTypeName(at, unit);
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
                for (ProducedType caseType: 
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
                            .append(getProducedTypeName(caseType, unit))
                            .append(gt());
                    }
                    else {
                        name.append(getProducedTypeName(caseType, unit));
                    }
                }
                return name.toString();
            }
            else if (pt.isIntersection()) {
                StringBuilder name = new StringBuilder();
                boolean first = true;
                for (ProducedType satisfiedType: 
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
                            .append(getProducedTypeName(satisfiedType, unit))
                            .append(gt());
                    }
                    else {
                        name.append(getProducedTypeName(satisfiedType, unit));
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
                    ProducedType dta = 
                            tp.getDefaultTypeArgument();
                    if (dta == null) {
                        name.append("=");
                    }
                    else {
                        name.append(" = ")
                            .append(getProducedTypeName(dta, unit));
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
                    ProducedType aliasedType =
                            declaration.getExtendedType()
                            .substitute(pt);
                    name.append(getProducedTypeName(aliasedType, unit));
                    return name.toString();
                }
                else {            
                    return getSimpleProducedTypeName(pt, unit);
                }
            }
        }
    }

    private boolean abbreviateHomoTuple(ProducedType pt) {
        Unit unit = pt.getDeclaration().getUnit();
        return unit.getHomogeneousTupleLength(pt)>1;
    }

    public static boolean abbreviateEntry(ProducedType pt) {
        Unit unit = pt.getDeclaration().getUnit();
        Class ed = unit.getEntryDeclaration();
        if (pt.isClass() &&
                pt.getDeclaration().equals(ed) &&
                pt.getTypeArgumentList().size()==2) {
            ProducedType kt = unit.getKeyType(pt);
            ProducedType vt = unit.getValueType(pt);
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

    public static boolean abbreviateEmpty(ProducedType pt) {
        if (pt.isInterface()) {
            TypeDeclaration dec = pt.getDeclaration();
            Unit unit = dec.getUnit();
            return dec.equals(unit.getEmptyDeclaration());
        }
        return false;
    }

    public static boolean abbreviateOptional(ProducedType pt) {
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

    public static boolean abbreviateTuple(ProducedType pt) {
        TypeDeclaration dec = pt.getDeclaration();
        Unit unit = dec.getUnit();
        return pt.isClass() && 
                dec.equals(unit.getTupleDeclaration()) &&
                        isTupleTypeWellformed(pt);
    }

    public static boolean abbreviateCallable(ProducedType pt) {
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
    
    private static boolean abbreviateCallableArg(ProducedType at) {
        if (at.isUnion()) {
            List<ProducedType> caseTypes = at.getCaseTypes();
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

    public static boolean abbreviateSequence(ProducedType pt) {
        if (pt.isInterface()) {
            TypeDeclaration dec = pt.getDeclaration();
            Unit unit = dec.getUnit();
            if (dec.equals(unit.getSequenceDeclaration())) {
                ProducedType et = unit.getIteratedType(pt);
                return et!=null;// && et.isPrimitiveAbbreviatedType();
            }
        }
        return false;
    }

    public static boolean abbreviateSequential(ProducedType pt) {
        if (pt.isInterface()) {
            TypeDeclaration ptd = pt.getDeclaration();
            Unit unit = ptd.getUnit();
            Interface sd = unit.getSequentialDeclaration();
            if (ptd.equals(sd)) {
                ProducedType et = unit.getIteratedType(pt);
                return et!=null;// && et.isPrimitiveAbbreviatedType();
            }
        }
        return false;
    }

    public static boolean abbreviateIterable(ProducedType pt) {
        if (pt.isInterface()) {
            TypeDeclaration ptd = pt.getDeclaration();
            Unit unit = ptd.getUnit();
            Interface id = unit.getIterableDeclaration();
            if (ptd.equals(id)) {
                ProducedType et = unit.getIteratedType(pt);
                List<ProducedType> typeArgs = 
                        pt.getTypeArgumentList();
                if (et!=null && typeArgs.size()==2) {
                    ProducedType at = typeArgs.get(1);
                    if (at!=null) {
                        Class nd = unit.getNullDeclaration();
                        return at.isNothing() ||
                                at.isClassOrInterface() && 
                                at.getDeclaration().equals(nd);
                    }
                }// && et.isPrimitiveAbbreviatedType();
            }
        }
        return false;
    }
    
    private static boolean isTupleTypeWellformed(ProducedType args) {
        if (args==null || 
                args.getTypeArgumentList().size()<3) {
            return false;
        }
        Unit unit = args.getDeclaration().getUnit();
        List<ProducedType> elemtypes = 
                unit.getTupleElementTypes(args);
        if (unit.isTupleLengthUnbounded(args)) {
            int lastIndex = elemtypes.size()-1;
            ProducedType last = elemtypes.get(lastIndex);
            elemtypes.set(lastIndex, 
                    unit.getSequentialElementType(last));
        }
        if (elemtypes==null) {
            return false;
        }
        UnionType ut = new UnionType(unit);
        ut.setCaseTypes(elemtypes);
        ProducedType t = ut.getType();
        ProducedType typeArg = 
                args.getTypeArgumentList().get(0);
        if (typeArg==null) {
            return false;
        }
        else {
            return t.isExactly(typeArg);
        }
    }

    private String getTupleElementTypeNames(ProducedType args, 
            Unit unit) {
        if (args!=null) {
            Unit u = args.getDeclaration().getUnit();
            boolean defaulted=false;
            Interface ed = u.getEmptyDeclaration();
            if (args.isUnion()) {
                List<ProducedType> cts = 
                        args.getCaseTypes();
                if (cts.size()==2) {
                    TypeDeclaration lc = 
                            cts.get(0).getDeclaration();
                    if (lc instanceof Interface && 
                            lc.equals(ed)) {
                        args = cts.get(1);
                        defaulted = true;
                    }
                    TypeDeclaration rc = 
                            cts.get(1).getDeclaration();
                    if (lc instanceof Interface &&
                            rc.equals(ed)) {
                        args = cts.get(0);
                        defaulted = true;
                    }
                }
            }
            if (args.isClassOrInterface()) {
                Interface sld = u.getSequentialDeclaration();
                Interface scd = u.getSequenceDeclaration();
                Class td = u.getTupleDeclaration();
                if (args.getDeclaration().equals(td)) {
                    List<ProducedType> tal = 
                            args.getTypeArgumentList();
                    if (tal.size()>=3) {
                        ProducedType first = tal.get(1);
                        ProducedType rest = tal.get(2);
                        if (first!=null && rest!=null) {
                            String argtype = 
                                    getProducedTypeName(first, unit);
                            if (rest.isInterface() &&
                                    rest.getDeclaration()
                                        .equals(ed)) {
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
                else if (args.getDeclaration().equals(ed)) {
                    return defaulted ? "=" : "";
                }
                else if (!defaulted && 
                        args.getDeclaration().equals(sld)) {
                    ProducedType elementType = 
                            u.getIteratedType(args);
                    if (elementType!=null) {
                        String elemtype = 
                                getProducedTypeName(elementType, unit);
                        if (isPrimitiveAbbreviatedType(elementType)) {
                            return elemtype + "*";
                        }
                        else {
                            return lt() + elemtype + gt() + "*";
                        }
                    }
                }
                else if (!defaulted && 
                        args.getDeclaration().equals(scd)) {
                    ProducedType elementType = 
                            u.getIteratedType(args);
                    if (elementType!=null) {
                        String elemtype = 
                                getProducedTypeName(elementType, unit);
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

    private boolean isPrimitiveAbbreviatedType(ProducedType pt) {
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

    protected String getSimpleProducedTypeName(ProducedType pt, 
            Unit unit) {
        StringBuilder ptn = new StringBuilder();

        boolean fullyQualified = printFullyQualified();
        if (printQualifyingType()) {
            ProducedType qt = pt.getQualifyingType();
            if (qt != null) {
				boolean isComplex = 
				        qt.isIntersection() || 
				        qt.isUnion();
                if (isComplex) {
					ptn.append(lt());
	            }
                ptn.append(getProducedTypeName(qt, unit));
    			if (isComplex) {
					ptn.append(gt());
	            }
    			ptn.append(".");
    			fullyQualified = false;
            }
        }

        TypeDeclaration ptd = pt.getDeclaration();
        printDeclaration(ptn, ptd, fullyQualified, unit);
        
        List<ProducedType> args = pt.getTypeArgumentList();
        List<TypeParameter> params = ptd.getTypeParameters();
        if (!pt.isTypeConstructor() && 
                printTypeParameters() && 
                !args.isEmpty()) {
            ptn.append(lt());
            boolean first = true;
            for (int i=0; i<args.size()&&i<params.size(); i++) {
                ProducedType t = args.get(i);
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
                    ptn.append(getProducedTypeName(t, unit));
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

    private void appendConstraintsString(ProducedType pt,
            StringBuilder result, Unit unit) {
        TypeParameter tpc = 
                pt.getTypeConstructorParameter();
        List<TypeParameter> params = 
                tpc==null ?
                    pt.getDeclaration()
                        .getTypeParameters() :
                    tpc.getTypeParameters();
        for (TypeParameter tp: params) {
            List<ProducedType> sts = tp.getSatisfiedTypes();
            List<ProducedType> cts = tp.getCaseTypes();
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
                    for (ProducedType ct: cts) {
                        if (first) {
                            first = false;
                        }
                        else {
                            result.append("|");
                        }
                        result.append(getProducedTypeName(ct,unit));
                    }
                }
                if (hasUpperBounds) {
                    result.append(" satisfies ");
                    boolean first = true;
                    for (ProducedType st: sts) {
                        if (first) {
                            first = false;
                        }
                        else {
                            result.append(amp());
                        }
                        result.append(getProducedTypeName(st,unit));
                    }
                }
            }
        }
    }

}