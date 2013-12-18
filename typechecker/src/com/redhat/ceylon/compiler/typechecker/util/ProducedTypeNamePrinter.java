package com.redhat.ceylon.compiler.typechecker.util;

import static com.redhat.ceylon.compiler.typechecker.model.Util.isElementOfUnion;

import java.util.List;

import com.redhat.ceylon.compiler.typechecker.model.Class;
import com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Interface;
import com.redhat.ceylon.compiler.typechecker.model.IntersectionType;
import com.redhat.ceylon.compiler.typechecker.model.NothingType;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.TypeParameter;
import com.redhat.ceylon.compiler.typechecker.model.UnionType;
import com.redhat.ceylon.compiler.typechecker.model.Unit;

public class ProducedTypeNamePrinter {

    public static final ProducedTypeNamePrinter DEFAULT = new ProducedTypeNamePrinter(true, true, false, true);

    private boolean printAbbreviated;
    private boolean printTypeParameters;
    private boolean printTypeParameterDetail;
    private boolean printQualifyingType;
    
    public ProducedTypeNamePrinter() {
    }

    public ProducedTypeNamePrinter(boolean printAbbreviated) {
        this(printAbbreviated, true, false, true);
    }

    public ProducedTypeNamePrinter(boolean printAbbreviated, boolean printTypeParameters, boolean printTypeParameterDetail, boolean printQualifyingType) {
        this.printAbbreviated = printAbbreviated;
        this.printTypeParameters = printTypeParameters;
        this.printTypeParameterDetail = printTypeParameterDetail;
        this.printQualifyingType = printQualifyingType;
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

    public String getProducedTypeName(ProducedType pt, Unit unit) {
        if (pt.getDeclaration()==null) {
            return "unknown";
        }
        else {
            if (printAbbreviated()) {
                Unit u = pt.getDeclaration().getUnit();
                if (abbreviateOptional(pt)) {
                    ProducedType dt = pt.eliminateNull();
                    if (!isPrimitiveAbbreviatedType(dt)) {
                        return "<" + getProducedTypeName(dt, unit) + ">?";
                    }
                    else {
                        return getProducedTypeName(dt, unit) + "?";
                    }
                }
                if (abbreviateEmpty(pt)) {
                    return "[]";
                }
                if (abbreviateSequential(pt)) {
                    ProducedType it = u.getIteratedType(pt);
                    if (!isPrimitiveAbbreviatedType(it)) {
                        return "<" + getProducedTypeName(it, unit) + ">[]";
                    }
                    else {
                        return getProducedTypeName(it, unit) + "[]";
                    }
                }
                if (abbreviateSequence(pt)) {
                    ProducedType it = u.getIteratedType(pt);
                    if (!isPrimitiveAbbreviatedType(it)) {
                        return "[<" + getProducedTypeName(it, unit) + ">+]";
                    }
                    else {
                        return "[" + getProducedTypeName(it, unit) + "+]";
                    }
                }
                if (abbreviateIterable(pt)) {
                    ProducedType it = u.getIteratedType(pt);
                    String etn = getProducedTypeName(it, unit);
                    ProducedType nt = pt.getTypeArgumentList().get(1);
                    String many = nt.isNothing() ? "+":"*";
                    if (isPrimitiveAbbreviatedType(it)) {
                        return "{" + etn + many + "}";
                    }
                    else {
                        return "{<" + etn + ">" + many + "}";
                    }
                }
                if (abbreviateEntry(pt)) {
                    return getProducedTypeName(u.getKeyType(pt), unit) + "->"
                            + getProducedTypeName(u.getValueType(pt), unit);
                }
                if (abbreviateCallable(pt)) {
                    List<ProducedType> tal = pt.getTypeArgumentList();
                    ProducedType rt = tal.get(0);
                    String argtypes = argtypes(tal.get(1), unit);
                    if (rt!=null && argtypes!=null) {
                        if (!isPrimitiveAbbreviatedType(rt)) {
                            return "<" + getProducedTypeName(rt, unit) + ">" + 
                                    "(" + argtypes + ")";
                        }
                        else {
                            return getProducedTypeName(rt, unit) + 
                                    "(" + argtypes + ")";
                        }
                    }
                }
                if (abbreviateTuple(pt)) {
                    String argtypes = argtypes(pt, unit);
                    if (argtypes!=null) {
                        return "[" + argtypes + "]";
                    }
                }
            }
            if (pt.getDeclaration() instanceof UnionType) {
                StringBuilder name = new StringBuilder();
                for (ProducedType caseType: pt.getCaseTypes()) {
                    if (caseType==null) {
                        name.append("unknown");
                    }
                    else if (printAbbreviated() && abbreviateEntry(caseType)) {
                        name.append("<").append(getProducedTypeName(caseType, unit)).append(">");
                    }
                    else {
                        name.append(getProducedTypeName(caseType, unit));
                    }
                    name.append("|");
                }
                return name.substring(0,name.length()>0?name.length()-1:0);
            }
            else if (pt.getDeclaration() instanceof IntersectionType) {
                StringBuilder name = new StringBuilder();
                for (ProducedType satisfiedType: pt.getSatisfiedTypes()) {
                    if (satisfiedType==null) {
                        name.append("unknown");
                    }
                    else if (printAbbreviated() && abbreviateEntry(satisfiedType) || 
                            satisfiedType.getDeclaration() instanceof UnionType) {
                        name.append("<").append(getProducedTypeName(satisfiedType, unit)).append(">");
                    }
                    else {
                        name.append(getProducedTypeName(satisfiedType, unit));
                    }
                    name.append("&");
                }
                return name.substring(0,name.length()>0?name.length()-1:0);
            }
            else if (pt.getDeclaration() instanceof TypeParameter) {
                StringBuilder name = new StringBuilder();
                TypeParameter tp = (TypeParameter) pt.getDeclaration();

                if (printTypeParameterDetail() && tp.isContravariant()) {
                    name.append("in ");
                }
                if (printTypeParameterDetail() && tp.isCovariant()) {
                    name.append("out ");
                }

                name.append(getSimpleProducedTypeName(pt, unit));

                if (printTypeParameterDetail() && tp.isDefaulted()) {
                    ProducedType dta = tp.getDefaultTypeArgument();
                    if (dta == null) {
                        name.append("=");
                    }
                    else {
                        name.append(" = ").append(getProducedTypeName(dta, unit));
                    }
                }

                return name.toString();
            }
            else {            
                return getSimpleProducedTypeName(pt, unit);
            }
        }
    }

    public static boolean abbreviateEntry(ProducedType pt) {
        Unit unit = pt.getDeclaration().getUnit();
        if (pt.getDeclaration() instanceof Class &&
                pt.getDeclaration().equals(unit.getEntryDeclaration()) &&
                pt.getTypeArgumentList().size()==2) {
            ProducedType kt = unit.getKeyType(pt);
            ProducedType vt = unit.getValueType(pt);
            return kt!=null && vt!=null; /*&&
                    kt.isPrimitiveAbbreviatedType() && 
                    vt.isPrimitiveAbbreviatedType();*/
        }
        else {
            return false;
        }
    }

    public static boolean abbreviateEmpty(ProducedType pt) {
        if (pt.getDeclaration() instanceof Interface) {
            Unit unit = pt.getDeclaration().getUnit();
            return pt.getDeclaration().equals(unit.getEmptyDeclaration());
        }
        return false;
    }

    public static boolean abbreviateOptional(ProducedType pt) {
        if (pt.getDeclaration() instanceof UnionType) {
            Unit unit = pt.getDeclaration().getUnit();
            UnionType ut = (UnionType) pt.getDeclaration();
            return ut.getCaseTypes().size()==2 &&
                    isElementOfUnion(ut, unit.getNullDeclaration()); /*&&
                    minus(unit.getNullDeclaration()).isPrimitiveAbbreviatedType();*/
        }
        else {
            return false;
        }
    }    

    public static boolean abbreviateTuple(ProducedType pt) {
        return pt.getDeclaration() instanceof Class && 
                pt.getDeclaration().equals(pt.getDeclaration().getUnit()
                        .getTupleDeclaration());
    }

    public static boolean abbreviateCallable(ProducedType pt) {
        return pt.getDeclaration() instanceof Interface &&
                pt.getDeclaration().equals(pt.getDeclaration().getUnit().getCallableDeclaration()) &&
                pt.getTypeArgumentList().size()>0 && pt.getTypeArgumentList().get(0)!=null &&
                //              pt.getTypeArgumentList().get(0).isPrimitiveAbbreviatedType() &&
                pt.getTypeArgumentList().size()==pt.getDeclaration().getTypeParameters().size();
    }

    public static boolean abbreviateSequence(ProducedType pt) {
        if (pt.getDeclaration() instanceof Interface) {
            Unit unit = pt.getDeclaration().getUnit();
            if (pt.getDeclaration().equals(unit.getSequenceDeclaration())) {
                ProducedType et = unit.getIteratedType(pt);
                return et!=null;// && et.isPrimitiveAbbreviatedType();
            }
        }
        return false;
    }

    public static boolean abbreviateSequential(ProducedType pt) {
        if (pt.getDeclaration() instanceof Interface) {
            Unit unit = pt.getDeclaration().getUnit();
            if (pt.getDeclaration().equals(unit.getSequentialDeclaration())) {
                ProducedType et = unit.getIteratedType(pt);
                return et!=null;// && et.isPrimitiveAbbreviatedType();
            }
        }
        return false;
    }

    public static boolean abbreviateIterable(ProducedType pt) {
        if (pt.getDeclaration() instanceof Interface) {
            Unit unit = pt.getDeclaration().getUnit();
            if (pt.getDeclaration().equals(unit.getIterableDeclaration())) {
                ProducedType et = unit.getIteratedType(pt);
                if (et!=null && pt.getTypeArgumentList().size()==2) {
                    ProducedType at = pt.getTypeArgumentList().get(1);
                    if (at!=null) {
                        TypeDeclaration d = at.getDeclaration();
                        return d instanceof NothingType ||
                                d instanceof ClassOrInterface && 
                                d.equals(unit.getNullDeclaration());
                    }
                }// && et.isPrimitiveAbbreviatedType();
            }
        }
        return false;
    }

    private String argtypes(ProducedType args, Unit unit) {
        if (args!=null) {
            Unit u = args.getDeclaration().getUnit();
            boolean defaulted=false;
            if (args.getDeclaration() instanceof UnionType) {
                List<ProducedType> cts = args.getDeclaration().getCaseTypes();
                if (cts.size()==2) {
                    TypeDeclaration lc = cts.get(0).getDeclaration();
                    if (lc instanceof Interface && 
                            lc.equals(u.getEmptyDeclaration())) {
                        args = cts.get(1);
                        defaulted = true;
                    }
                    TypeDeclaration rc = cts.get(1).getDeclaration();
                    if (lc instanceof Interface &&
                            rc.equals(u.getEmptyDeclaration())) {
                        args = cts.get(0);
                        defaulted = true;
                    }
                }
            }
            if (args.getDeclaration() instanceof ClassOrInterface) {
                if (args.getDeclaration().equals(u.getTupleDeclaration())) {
                    List<ProducedType> tal = args.getTypeArgumentList();
                    if (tal.size()>=3) {
                        ProducedType first = tal.get(1);
                        ProducedType rest = tal.get(2);
                        if (first!=null && rest!=null) {
                            String argtype = getProducedTypeName(first, unit);
                            if (rest.getDeclaration() instanceof Interface &&
                                    rest.getDeclaration().equals(u.getEmptyDeclaration())) {
                                return defaulted ? argtype + "=" : argtype;
                            }
                            String argtypes = argtypes(rest, unit);
                            if (argtypes!=null) {
                                return defaulted ? 
                                        argtype + "=, " + argtypes : 
                                            argtype + ", " + argtypes;
                            }
                        }
                    }
                }
                else if (args.getDeclaration().equals(u.getEmptyDeclaration())) {
                    return defaulted ? "=" : "";
                }
                else if (!defaulted && args.getDeclaration().equals(u.getSequentialDeclaration())) {
                    ProducedType elementType = u.getIteratedType(args);
                    if (elementType!=null) {
                        String etn = getProducedTypeName(elementType, unit);
                        if (isPrimitiveAbbreviatedType(elementType)) {
                            return etn + "*";
                        }
                        else {
                            return "<" + etn + ">*";
                        }
                    }
                }
                else if (!defaulted && args.getDeclaration().equals(u.getSequenceDeclaration())) {
                    ProducedType elementType = u.getIteratedType(args);
                    if (elementType!=null) {
                        String etn = getProducedTypeName(elementType, unit);
                        if (isPrimitiveAbbreviatedType(elementType)) {
                            return etn + "+";
                        }
                        else {
                            return "<" + etn + ">+";
                        }
                    }
                }
            }
        }
        return null;
    }

    private boolean isPrimitiveAbbreviatedType(ProducedType pt) {
        if (pt.getDeclaration() instanceof IntersectionType) {
            return false;
        }
        else if (pt.getDeclaration() instanceof UnionType) {
            return abbreviateOptional(pt);
        }
        else {
            return !abbreviateEntry(pt);
        }
    }

    protected String getSimpleProducedTypeName(ProducedType pt, Unit unit) {
        StringBuilder ptn = new StringBuilder();

        if (printQualifyingType()) {
            ProducedType qt = pt.getQualifyingType();
            if (qt != null) {
	            TypeDeclaration qtd = qt.getDeclaration();
				if (qtd instanceof IntersectionType ||
					qtd instanceof UnionType) {
					ptn.append("<");
	            }
                ptn.append(getProducedTypeName(qt, unit));
    			if (qtd instanceof IntersectionType ||
					qtd instanceof UnionType) {
					ptn.append(">");
	            }
    			ptn.append(".");
            }
        }

        ptn.append(getSimpleDeclarationName(pt.getDeclaration(), unit));

        if (printTypeParameters() && !pt.getTypeArgumentList().isEmpty()) {
            ptn.append("<");
            boolean first = true;
            for (ProducedType t: pt.getTypeArgumentList()) {
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
                    ptn.append(getProducedTypeName(t, unit));
                }
            }
            ptn.append(">");
        }
        return ptn.toString();
    }

    protected String getSimpleDeclarationName(Declaration declaration, Unit unit) {
        return declaration.getName(unit);
    }

}