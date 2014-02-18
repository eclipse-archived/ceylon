package com.redhat.ceylon.compiler.typechecker.analyzer;


import static com.redhat.ceylon.compiler.typechecker.analyzer.Util.checkAssignable;
import static com.redhat.ceylon.compiler.typechecker.analyzer.Util.checkAssignableToOneOf;
import static com.redhat.ceylon.compiler.typechecker.analyzer.Util.checkIsExactly;
import static com.redhat.ceylon.compiler.typechecker.analyzer.Util.checkIsExactlyOneOf;
import static com.redhat.ceylon.compiler.typechecker.analyzer.Util.declaredInPackage;
import static com.redhat.ceylon.compiler.typechecker.analyzer.Util.typeDescription;
import static com.redhat.ceylon.compiler.typechecker.analyzer.Util.typeNamesAsIntersection;
import static com.redhat.ceylon.compiler.typechecker.model.Util.addToIntersection;
import static com.redhat.ceylon.compiler.typechecker.model.Util.areConsistentSupertypes;
import static com.redhat.ceylon.compiler.typechecker.model.Util.getSignature;
import static com.redhat.ceylon.compiler.typechecker.model.Util.isAbstraction;
import static com.redhat.ceylon.compiler.typechecker.model.Util.isCompletelyVisible;
import static com.redhat.ceylon.compiler.typechecker.model.Util.isOverloadedVersion;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonMap;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.redhat.ceylon.compiler.typechecker.model.Class;
import com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Element;
import com.redhat.ceylon.compiler.typechecker.model.Functional;
import com.redhat.ceylon.compiler.typechecker.model.Generic;
import com.redhat.ceylon.compiler.typechecker.model.IntersectionType;
import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.MethodOrValue;
import com.redhat.ceylon.compiler.typechecker.model.Module;
import com.redhat.ceylon.compiler.typechecker.model.ModuleImport;
import com.redhat.ceylon.compiler.typechecker.model.Package;
import com.redhat.ceylon.compiler.typechecker.model.Parameter;
import com.redhat.ceylon.compiler.typechecker.model.ParameterList;
import com.redhat.ceylon.compiler.typechecker.model.ProducedReference;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.Setter;
import com.redhat.ceylon.compiler.typechecker.model.TypeAlias;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.TypeParameter;
import com.redhat.ceylon.compiler.typechecker.model.TypedDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.UnionType;
import com.redhat.ceylon.compiler.typechecker.model.Unit;
import com.redhat.ceylon.compiler.typechecker.model.Value;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;

/**
 * Validates some simple rules relating to refinement.
 * 
 * @see TypeHierarchyVisitor for the fancy stuff!
 * 
 * @author Gavin King
 *
 */
public class RefinementVisitor extends Visitor {
    
    private void checkVisibility(Node that, Declaration td, 
            ProducedType type) {
        if (type!=null) {
            Node typeNode = getTypeErrorNode(that);
            if (!isCompletelyVisible(td, type)) {
                typeNode.addError("type of declaration " + td.getName() +
                        " is not visible everywhere declaration is visible: " + 
                        type.getProducedTypeName(that.getUnit()) +
                        " involves an unshared type declaration", 711);
            }
            if (!checkModuleVisibility(td, type)) {
                typeNode.addError("type of declaration " + td.getName() + 
                        " that is visible outside this module comes from an imported module that is not re-exported: " + 
                        type.getProducedTypeName(that.getUnit()) +
                        " involves an unexported type declaration", 712);
            }
        }
    }

    private void checkParameterVisibility(Tree.Declaration that,
            Declaration td, Tree.Parameter tp, Parameter p) {
        ProducedType pt = p.getType();
        if (pt!=null) {
            if (!isCompletelyVisible(td, pt)) {
                getParameterTypeErrorNode(tp)
                    .addError("type of parameter " + p.getName() + " of " + td.getName() +
                        " is not visible everywhere declaration is visible: " + 
                        pt.getProducedTypeName(that.getUnit()) +
                        " involves an unshared type declaration", 710);
            }
            if (!checkModuleVisibility(td, pt)) {
                getParameterTypeErrorNode(tp)
                    .addError("type of parameter " + p.getName() + " of " + td.getName() + 
                        " that is visible outside this module comes from an imported module that is not re-exported: " +
                        pt.getProducedTypeName(that.getUnit()) +
                        " involves an unexported type declaration", 714);
            }
        }
    }

    private Node getParameterTypeErrorNode(Tree.Parameter p) {
        if (p instanceof Tree.ParameterDeclaration) {
            return ((Tree.ParameterDeclaration) p).getTypedDeclaration().getType();
        }
        else {
            return p;
        }
    }
    
    @Override
    public void visit(Tree.AnyMethod that) {
        super.visit(that);
        TypedDeclaration td = that.getDeclarationModel();
        for (Tree.ParameterList list: that.getParameterLists()) {
            for (Tree.Parameter tp: list.getParameters()) {
                if (tp!=null) {
                    Parameter p = tp.getParameterModel();
                    if (p.getModel()!=null) {
                        checkParameterVisibility(that, td, tp, p);
                    }
                }
            }
        }
    }

    @Override
    public void visit(Tree.AnyClass that) {
        super.visit(that);
        Class td = that.getDeclarationModel();
        if (that.getParameterList()!=null) {
            for (Tree.Parameter tp: that.getParameterList().getParameters()) {
                if (tp!=null) {
                    Parameter p = tp.getParameterModel();
                    if (p.getModel()!=null) {
                        checkParameterVisibility(that, td, tp, p);
                    }
                }
            }
        }
    }

    private static Module getModule(Element element){
        Package typePackage = element.getUnit().getPackage();
        return typePackage != null ? typePackage.getModule() : null;
    }

    private static boolean inExportedScope(Declaration decl) {
        // if it has a visible scope it's not exported outside the module
        if(decl.getVisibleScope() != null)
            return false;
        // now perhaps its package is not shared
        Package p = decl.getUnit().getPackage();
        return p != null && p.isShared();
    }

    private static boolean checkModuleVisibility(Declaration member, ProducedType pt) {
        if (inExportedScope(member)) {
            Module declarationModule = getModule(member);
            if (declarationModule!=null) {
                return isCompletelyVisibleFromOtherModules(member,pt,declarationModule);
            }
        }
        return true;
    }

    private static boolean isCompletelyVisibleFromOtherModules(Declaration member, 
    		ProducedType pt, Module thisModule) {
        if (pt.getDeclaration() instanceof UnionType) {
            for (ProducedType ct: pt.getDeclaration().getCaseTypes()) {
                if (!isCompletelyVisibleFromOtherModules(member, 
                		ct.substitute(pt.getTypeArguments()), thisModule)) {
                    return false;
                }
            }
            return true;
        }
        else if (pt.getDeclaration() instanceof IntersectionType) {
            for (ProducedType ct: pt.getDeclaration().getSatisfiedTypes()) {
                if (!isCompletelyVisibleFromOtherModules(member, 
                		ct.substitute(pt.getTypeArguments()), thisModule)) {
                    return false;
                }
            }
            return true;
        }
        else {
            if (!isVisibleFromOtherModules(member, thisModule, 
            		pt.getDeclaration())) {
                return false;
            }
            for (ProducedType at: pt.getTypeArgumentList()) {
                if ( at!=null && 
                		!isCompletelyVisibleFromOtherModules(member,at,thisModule) ) {
                    return false;
                }
            }
            return true;
        }
    }

    private static boolean isVisibleFromOtherModules(Declaration member, 
    		Module thisModule, TypeDeclaration type) {
        // type parameters are OK
        if (type instanceof TypeParameter) {
            return true;
        }
        
        Module typeModule = getModule(type);
        if (typeModule!=null && thisModule!=null && 
        		thisModule!=typeModule) {
            // find the module import, but only in exported imports, otherwise it's an error anyways

            // language module stuff is automagically exported
            if (typeModule == thisModule.getLanguageModule()) {
                return true;
            }
            // try to find a direct import first
            for (ModuleImport imp: thisModule.getImports()) {
                if (imp.isExport() && imp.getModule() == typeModule) {
                    // found it
                    return true;
                }
            }
            // then try the more expensive implicit imports
            Set<Module> visited = new HashSet<Module>();
            visited.add(thisModule);
            for (ModuleImport imp : thisModule.getImports()) {
                // now try implicit dependencies
                if (imp.isExport() && 
                		includedImplicitly(imp.getModule(), typeModule, visited)) {
                    // found it
                    return true;
                }
            }
            // couldn't find it
            return false;
        }
        // no module or it does not belong to a module? more likely an error was already reported
        return true;
    }

    private static boolean includedImplicitly(Module importedModule, 
    		Module targetModule, Set<Module> visited) {
        // don't visit them twice
        if (visited.add(importedModule)) {
        	for (ModuleImport imp: importedModule.getImports()){
        		// only consider modules it exported back to us
        		if (imp.isExport()
        				&& (imp.getModule() == targetModule
        				|| includedImplicitly(imp.getModule(), 
        						targetModule, visited))) {
        			return true;
        		}
        	}
        }
        return false;
    }
    
    @Override public void visit(Tree.TypeConstraint that) {
        super.visit(that);
        TypeDeclaration td = that.getDeclarationModel();
        validateUpperBounds(that, td);
    }

    @Override public void visit(Tree.TypeDeclaration that) {
        validateSupertypes(that, that.getDeclarationModel());
        super.visit(that);
    }

    @Override public void visit(Tree.ObjectDefinition that) {
        validateSupertypes(that, 
        		that.getDeclarationModel().getType().getDeclaration());
        super.visit(that);
    }

    @Override public void visit(Tree.ObjectArgument that) {
        validateSupertypes(that, 
        		that.getDeclarationModel().getType().getDeclaration());
        super.visit(that);
    }

    private void validateSupertypes(Tree.StatementOrArgument that, 
    		TypeDeclaration td) {
        List<ProducedType> supertypes = td.getType().getSupertypes();
        if (td instanceof TypeAlias && 
                td.getExtendedType()!=null) {
            supertypes.add(td.getExtendedType());
        }
        for (int i=0; i<supertypes.size(); i++) {
            ProducedType st1 = supertypes.get(i);
            for (int j=i+1; j<supertypes.size(); j++) {
                ProducedType st2 = supertypes.get(j);
                checkSupertypeIntersection(that, td, st1, st2); //note: sets td.inconsistentType by side-effect
            }
        }
        if (!td.isInconsistentType()) {
            for (ProducedType st: supertypes) {
                // don't do this check for ObjectArguments
                if (that instanceof Tree.Declaration) {
                    if (!isCompletelyVisible(td, st)) {
                        that.addError("supertype of type " + td.getName() + 
                                " is not visible everywhere type is visible: " + 
                                st.getProducedTypeName(that.getUnit()) +
                                " involves an unshared type declaration", 713);
                    }
                    if (!checkModuleVisibility(td, st)) {
                        that.addError("supertype of type " + td.getName() + 
                                " that is visible outside this module comes from an imported module that is not re-exported: " +
                                st.getProducedTypeName(that.getUnit()) +
                                " involves an unexported type declaration", 714);
                    }
                }
            }
        }
//        validateMemberRefinement(td, that, unit);
    }

    private void checkSupertypeIntersection(Tree.StatementOrArgument that,
            TypeDeclaration td, ProducedType st1, ProducedType st2) {
        if (st1.getDeclaration().equals(st2.getDeclaration()) /*&& !st1.isExactly(st2)*/) {
            Unit unit = that.getUnit();
            if (!areConsistentSupertypes(st1, st2, unit)) {
                that.addError(typeDescription(td, unit) +
                        " has the same parameterized supertype twice with incompatible type arguments: " +
                        st1.getProducedTypeName(unit) + " & " + 
                        st2.getProducedTypeName(unit));
               td.setInconsistentType(true);
            }
        }
    }

    private void validateUpperBounds(Tree.TypeConstraint that,
            TypeDeclaration td) {
        if (!td.isInconsistentType()) {
            Unit unit = that.getUnit();
            List<ProducedType> upperBounds = td.getSatisfiedTypes();
            List<ProducedType> list = 
            		new ArrayList<ProducedType>(upperBounds.size());
            for (ProducedType st: upperBounds) {
                addToIntersection(list, st, unit);
            }
            IntersectionType it = new IntersectionType(unit);
            it.setSatisfiedTypes(list);
            if (it.canonicalize().getType().isNothing()) {
                that.addError(typeDescription(td, unit) + 
                        " has unsatisfiable upper bound constraints: the constraints " + 
                        typeNamesAsIntersection(upperBounds, unit) + 
                        " cannot be satisfied by any type except Nothing");
            }
        }
    }

    @Override public void visit(Tree.TypedDeclaration that) {
        TypedDeclaration td = that.getDeclarationModel();
        checkVisibility(that, td, td.getType());
        super.visit(that);
    }

    @Override public void visit(Tree.Declaration that) {
        super.visit(that);
        Declaration dec = that.getDeclarationModel();
        if (dec!=null) {
            boolean toplevel = 
            		dec.getContainer() instanceof Package;
            boolean member = 
            		dec.isClassOrInterfaceMember() &&
                    dec.isShared() &&
                    !(dec instanceof TypeParameter); //TODO: what about nested interfaces and abstract classes?!
            
            if (!toplevel && !member && dec.isShared()) {
                that.addError("shared declaration is not a member of a class, interface, or package", 1200);
            }
            
            boolean mayBeShared = 
                    dec instanceof MethodOrValue || 
                    dec instanceof ClassOrInterface ||
                    dec instanceof TypeAlias;
            if (!mayBeShared && dec.isShared()) {
                that.addError("shared declaration is not a function, value, class, interface, or alias", 1200);
            }
            
            boolean mayBeRefined =
                    dec instanceof Value || 
                    dec instanceof Method ||
                    dec instanceof Class;
            if (!mayBeRefined) {
                checkNonrefinableDeclaration(that, dec);
            }
            
            if (!member) {
                checkNonMember(that, dec, mayBeShared);
            }
            
            /*if (!dec.isShared()) {
                checkUnshared(that, dec);
            }*/
            
            if (member) {
                if (!(dec instanceof Setter)) {
                    checkMember(that, dec);
                }
                ClassOrInterface declaringType = 
                		(ClassOrInterface) dec.getContainer();
                Declaration refined = 
                		declaringType.getRefinedMember(dec.getName(), 
                				getSignature(dec), false);
                dec.setRefinedDeclaration(refined);
                if (refined!=null) {
                    if (refined.isPackageVisibility() && 
                            !declaredInPackage(refined, that.getUnit())) {
                        that.addError("refined declaration is not visible: " + 
                                message(refined));
                    }
                }
            }
            
        }
        
    }

    private void checkMember(Tree.Declaration that, Declaration dec) {
        ClassOrInterface ci = (ClassOrInterface) dec.getContainer();
        if (dec.isFormal() && ci instanceof Class) {
            Class c = (Class) ci;
            if (!c.isAbstract() && !c.isFormal()) {
                that.addError("formal member belongs to non-abstract, non-formal class", 1100);
            }
        }
        List<Declaration> others = 
        		ci.getInheritedMembers(dec.getName());
        if (others.isEmpty()) {
            if (dec.isActual()) {
                that.addError("actual member does not refine any inherited member", 1300);
            }
        }
        else {
            boolean found = false;
            for (Declaration refined: others) {
            	ProducedType st = ci.getType()
            			.getSupertype((TypeDeclaration) refined.getContainer());
                if (isAbstraction(refined) || st==null ||
                		isOverloadedVersion(refined) && 
                		!refinesOverloaded(dec, refined, st)) {
                    continue;
                }
                found = true;
                if (dec instanceof Method) {
                    if (!(refined instanceof Method)) {
                        that.addError("refined declaration is not a method: " + 
                                message(refined));
                    }
                }
                else if (dec instanceof Class) {
                    if (!(refined instanceof Class)) {
                        that.addError("refined declaration is not a class: " + 
                                message(refined));
                    }
                }
                else if (dec instanceof TypedDeclaration) {
                    if (refined instanceof Class || 
                        refined instanceof Method) {
                        that.addError("refined declaration is not an attribute: " + 
                                message(refined));
                    }
                    else if (refined instanceof TypedDeclaration) {
                        if ( ((TypedDeclaration) refined).isVariable() && 
                                !((TypedDeclaration) dec).isVariable()) {
                            if (dec instanceof Value) {
                                that.addError("non-variable attribute refines a variable attribute: " + 
                                        message(refined), 804);
                            }
                            else {
                                that.addError("non-variable attribute refines a variable attribute: " + 
                                        message(refined));
                            }
                        }
                    }
                }
                if (!dec.isActual()) {
                    that.addError("non-actual member refines an inherited member: " + 
                            message(refined), 600);
                }
                if (!refined.isDefault() && !refined.isFormal()) {
                    that.addError("member refines a non-default, non-formal member: " + 
                            message(refined), 500);
                }
                if (!ci.isInconsistentType()) {
                    checkRefinedTypeAndParameterTypes(that, dec, ci, refined);
                }
            }
            if (!found) {
                that.addError("actual member does not exactly refine any overloaded inherited member");
            }
        }
    }

    private boolean refinesOverloaded(Declaration dec, 
    		Declaration refined, ProducedType st) {
        Functional fun1 = (Functional) dec;
        Functional fun2 = (Functional) refined;
        if (fun1.getParameterLists().size()!=1 ||
            fun2.getParameterLists().size()!=1) {
            return false;
        }
        List<Parameter> pl1 = fun1.getParameterLists()
        		.get(0).getParameters();
        List<Parameter> pl2 = fun2.getParameterLists()
        		.get(0).getParameters();
        if (pl1.size()!=pl2.size()) {
            return false;
        }
        for (int i=0; i<pl1.size(); i++) {
            Parameter p1 = pl1.get(i);
            Parameter p2 = pl2.get(i);
            if (p1==null || p2==null ||
            		p1.getType()==null || 
            		p2.getType()==null) {
            	return false;
            }
            else {
            	ProducedType p2st = p2.getType()
            			.substitute(st.getTypeArguments());
				if (!p1.getType().isExactly(p2st)) {
                    return false;
            	}
            }
        }
        return true;
    }
    
    static String message(Declaration refined) {
        String container;
        if (refined.getContainer() instanceof Declaration) {
            container = " in " + 
                ((Declaration) refined.getContainer()).getName();
        }
        else {
            container = "";
        }
        return refined.getName() + container;
    }

    private void checkRefinedTypeAndParameterTypes(Tree.Declaration that,
            Declaration dec, ClassOrInterface ci, Declaration refined) {
        
    	List<ProducedType> typeArgs;
        if (refined instanceof Generic && dec instanceof Generic) {
            List<TypeParameter> refinedTypeParams = ((Generic) refined).getTypeParameters();
            List<TypeParameter> refiningTypeParams = ((Generic) dec).getTypeParameters();
            checkRefiningMemberTypeParameters(that, refined, 
            		refinedTypeParams, refiningTypeParams);
            typeArgs = checkRefiningMemberUpperBounds(that, ci, refined, 
            		refinedTypeParams, refiningTypeParams);
        }
        else {
        	typeArgs = emptyList();
        }
        
        ProducedReference refinedMember = ci.getType().getTypedReference(refined, typeArgs);
        ProducedReference refiningMember = ci.getType().getTypedReference(dec, typeArgs);
        Declaration refinedMemberDec = refinedMember.getDeclaration();
		Declaration refiningMemberDec = refiningMember.getDeclaration();
		Node typeNode = getTypeErrorNode(that);
		if (refinedMemberIsDynamicallyTyped(refinedMemberDec, refiningMemberDec)) {
        	checkRefiningMemberDynamicallyTyped(refined, refiningMemberDec, typeNode);
        }
		else if (refiningMemberIsDynamicallyTyped(refinedMemberDec, refiningMemberDec)) {
        	checkRefinedMemberDynamicallyTyped(refined, refinedMemberDec, typeNode);
        }
		else if (refinedMemberIsVariable(refinedMemberDec)) {
            checkRefinedMemberTypeExactly(refiningMember, refinedMember, typeNode, refined);
        }
        else {
            //note: this version checks return type and parameter types in one shot, but the
            //resulting error messages aren't as friendly, so do it the hard way instead!
            //checkAssignable(refiningMember.getFullType(), refinedMember.getFullType(), that,
            checkRefinedMemberTypeAssignable(refiningMember, refinedMember, typeNode, refined);
        }
        if (dec instanceof Functional && refined instanceof Functional) {
           checkRefiningMemberParameters(that, dec, refined, refinedMember, refiningMember);
        }
    }

	private void checkRefiningMemberParameters(Tree.Declaration that,
            Declaration dec, Declaration refined,
            ProducedReference refinedMember, ProducedReference refiningMember) {
		List<ParameterList> refiningParamLists = ((Functional) dec).getParameterLists();
		List<ParameterList> refinedParamLists = ((Functional) refined).getParameterLists();
		if (refinedParamLists.size()!=refiningParamLists.size()) {
			that.addError("member must have the same number of parameter lists as refined member: " + 
					message(refined));
		}
		for (int i=0; i<refinedParamLists.size() && i<refiningParamLists.size(); i++) {
			checkParameterTypes(that, getParameterList(that, i), 
					refiningMember, refinedMember, 
					refiningParamLists.get(i), refinedParamLists.get(i));
		}
    }

	private boolean refinedMemberIsVariable(Declaration refinedMemberDec) {
	    return refinedMemberDec instanceof TypedDeclaration &&
                ((TypedDeclaration) refinedMemberDec).isVariable();
    }

	private void checkRefinedMemberDynamicallyTyped(Declaration refined,
            Declaration refinedMemberDec, Node typeNode) {
	    if (!((TypedDeclaration) refinedMemberDec).isDynamicallyTyped()) {
	    	typeNode.addError("member which refines statically typed refined member must also be statically typed: " + 
	    			message(refined));
	    }
    }

	private void checkRefiningMemberDynamicallyTyped(Declaration refined,
            Declaration refiningMemberDec, Node typeNode) {
	    if (!((TypedDeclaration) refiningMemberDec).isDynamicallyTyped()) {
	    	typeNode.addError("member which refines dynamically typed refined member must also be dynamically typed: " + 
	    			message(refined));
	    }
    }

	private boolean refiningMemberIsDynamicallyTyped(
            Declaration refinedMemberDec, Declaration refiningMemberDec) {
	    return refinedMemberDec instanceof TypedDeclaration && 
				refiningMemberDec instanceof TypedDeclaration && 
        		((TypedDeclaration) refiningMemberDec).isDynamicallyTyped();
    }

	private boolean refinedMemberIsDynamicallyTyped(
            Declaration refinedMemberDec, Declaration refiningMemberDec) {
	    return refinedMemberDec instanceof TypedDeclaration && 
				refiningMemberDec instanceof TypedDeclaration && 
        		((TypedDeclaration) refinedMemberDec).isDynamicallyTyped();
    }

	private void checkRefiningMemberTypeParameters(Tree.Declaration that,
            Declaration refined, List<TypeParameter> refinedTypeParams,
            List<TypeParameter> refiningTypeParams) {
	    int refiningSize = refiningTypeParams.size();
	    int refinedSize = refinedTypeParams.size();
	    if (refiningSize!=refinedSize) {
	        that.addError("member does not have the same number of type parameters as refined member: " + 
	                    message(refined));
	    }
    }

	private List<ProducedType> checkRefiningMemberUpperBounds(Tree.Declaration that,
            ClassOrInterface ci, Declaration refined,
            List<TypeParameter> refinedTypeParams, 
            List<TypeParameter> refiningTypeParams) {
        int refiningSize = refiningTypeParams.size();
        int refinedSize = refinedTypeParams.size();
	    int max = refiningSize <= refinedSize ? refiningSize : refinedSize;
	    if (max==0) {
	    	return emptyList();
	    }
		List<ProducedType> typeArgs = new ArrayList<ProducedType>(max); 
		for (int i=0; i<max; i++) {
	        TypeParameter refinedTypeParam = refinedTypeParams.get(i);
	        TypeParameter refiningTypeParam = refiningTypeParams.get(i);
	        ProducedType refinedProducedType = refinedTypeParam.getType();
	        for (ProducedType t: refiningTypeParam.getSatisfiedTypes()) {
	            ProducedType bound = 
	            		t.substitute(singletonMap(refiningTypeParam, refinedProducedType));
	            Map<TypeParameter, ProducedType> args = ci.getType()
	                    .getSupertype((TypeDeclaration)refined.getContainer())
	                    .getTypeArguments();
	            //for every type constraint of the refining member, there must
	            //be at least one type constraint of the refined member which
	            //is assignable to it, guaranteeing that the intersection of
	            //the refined member bounds is assignable to the intersection
	            //of the refining member bounds
	            //TODO: would it be better to just form the intersections and
	            //      test assignability directly (the error messages might
	            //      not be as helpful, but it might be less restrictive)
	            boolean ok = false;
	            for (ProducedType st: refinedTypeParam.getSatisfiedTypes()) {
	                if (st.substitute(args).isSubtypeOf(bound)) {
	                    ok = true;
	                }
	            }
	            if (!ok) {
	                that.addError("member type parameter " + refiningTypeParam.getName() +
	                        " has upper bound which refined member type parameter " + 
	                        refinedTypeParam.getName() + " of " + message(refined) + 
	                        " does not satisfy: " + t.getProducedTypeName(that.getUnit()));
	            }
	        }
	        typeArgs.add(refinedProducedType);
	    }
	    return typeArgs;
    }

    private static Node getTypeErrorNode(Node that) {
		if (that instanceof Tree.TypedDeclaration) {
			Tree.Type type = ((Tree.TypedDeclaration) that).getType();
			if (type!=null) {
				return type;
			}
		}
        return that;
    }

    private void checkRefinedMemberTypeAssignable(ProducedReference refiningMember, 
    		ProducedReference refinedMember, Node that, Declaration refined) {
        if (hasUncheckedNullType(refinedMember)) {
            ProducedType optionalRefinedType = refiningMember.getDeclaration()
            		.getUnit().getOptionalType(refinedMember.getType());
            checkAssignableToOneOf(refiningMember.getType(), refinedMember.getType(), 
            		optionalRefinedType, that, 
            		"type of member must be assignable to type of refined member: " + 
            				message(refined));
        }
        else {
            checkAssignable(refiningMember.getType(), refinedMember.getType(), that,
            		"type of member must be assignable to type of refined member: " + 
            				message(refined), 9000);
        }
    }

    private void checkRefinedMemberTypeExactly(ProducedReference refiningMember, 
    		ProducedReference refinedMember, Node that, Declaration refined) {
        if (hasUncheckedNullType(refinedMember)) {
            ProducedType optionalRefinedType = refiningMember.getDeclaration()
            		.getUnit().getOptionalType(refinedMember.getType());
            checkIsExactlyOneOf(refiningMember.getType(), refinedMember.getType(), 
            		optionalRefinedType, that, 
            		"type of member must be exactly the same as type of variable refined member: " + 
            	            message(refined));
        }
        else {
            checkIsExactly(refiningMember.getType(), refinedMember.getType(), that,
            		"type of member must be exactly the same as type of variable refined member: " + 
            	            message(refined), 9000);
        }
    }

    private boolean hasUncheckedNullType(ProducedReference member) {
        return member.getDeclaration() instanceof TypedDeclaration 
                && ((TypedDeclaration)member.getDeclaration()).hasUncheckedNullType();
    }

    /*private void checkUnshared(Tree.Declaration that, Declaration dec) {
        if (dec.isActual()) {
            that.addError("actual member is not shared", 701);
        }
        if (dec.isFormal()) {
            that.addError("formal member is not shared", 702);
        }
        if (dec.isDefault()) {
            that.addError("default member is not shared", 703);
        }
    }*/

    private void checkNonrefinableDeclaration(Tree.Declaration that,
            Declaration dec) {
        if (dec.isActual()) {
            that.addError("actual declaration is not a method, getter, reference attribute, or class", 1301);
        }
        if (dec.isFormal()) {
            that.addError("formal declaration is not a method, getter, reference attribute, or class", 1302);
        }
        if (dec.isDefault()) {
            that.addError("default declaration is not a method, getter, reference attribute, or class", 1303);
        }
    }

    private void checkNonMember(Tree.Declaration that, Declaration dec, boolean mayBeShared) {
        if (!dec.isClassOrInterfaceMember() && mayBeShared) {
            if (dec.isActual()) {
                that.addError("actual declaration is not a member of a class or interface: " + dec.getName(), 1301);
            }
            if (dec.isFormal()) {
                that.addError("formal declaration is not a member of a class or interface: " + dec.getName(), 1302);
            }
            if (dec.isDefault()) {
                that.addError("default declaration is not a member of a class or interface: " + dec.getName(), 1303);
            }
        }
        else if (!dec.isShared() && mayBeShared) {
            if (dec.isActual()) {
                that.addError("actual declaration must be shared: " + dec.getName(), 701);
            }
            if (dec.isFormal()) {
                that.addError("formal declaration must be shared: " + dec.getName(), 702);
            }
            if (dec.isDefault()) {
                that.addError("default declaration must be shared: " + dec.getName(), 703);
            }
        }
        else {
            if (dec.isActual()) {
                that.addError("declaration may not be actual: " + dec.getName(), 1301);
            }
            if (dec.isFormal()) {
                that.addError("declaration may not be formal: " + dec.getName(), 1302);
            }
            if (dec.isDefault()) {
                that.addError("declaration may not be default: " + dec.getName(), 1303);
            }
        }
    }
    
    private static String containerName(ProducedReference member) {
        return ((Declaration) member.getDeclaration().getContainer()).getName();
    }

    private void checkParameterTypes(Tree.Declaration that, Tree.ParameterList pl,
            ProducedReference member, ProducedReference refinedMember,
            ParameterList params, ParameterList refinedParams) {
        List<Parameter> paramsList = params.getParameters();
		List<Parameter> refinedParamsList = refinedParams.getParameters();
		if (paramsList.size()!=refinedParamsList.size()) {
           handleWrongParameterListLength(that, member, refinedMember);
        }
        else {
            for (int i=0; i<paramsList.size(); i++) {
                Parameter rparam = refinedParamsList.get(i);
                Parameter param = paramsList.get(i);
                ProducedType refinedParameterType = 
                		refinedMember.getTypedParameter(rparam).getFullType();
                ProducedType parameterType = 
                		member.getTypedParameter(param).getFullType();
                Tree.Parameter parameter = pl.getParameters().get(i);
                Node typeNode = parameter;
                if (parameter instanceof Tree.ParameterDeclaration) {
                	Tree.Type type = ((Tree.ParameterDeclaration) parameter)
                			.getTypedDeclaration().getType();
                	if (type!=null) {
                		typeNode = type;
                	}
                }
                if (parameter!=null) {
            		if (rparam.getModel().isDynamicallyTyped()) {
                    	checkRefiningParameterDynamicallyTyped(member,
                                refinedMember, param, typeNode);
                    }
            		else if (param.getModel().isDynamicallyTyped()) {
                    	checkRefinedParameterDynamicallyTyped(member,
                                refinedMember, rparam, param, typeNode);
                    }
            		else if (refinedParameterType==null || parameterType==null) {
            			handleUnknownParameterType(member, refinedMember,
                                param, typeNode);
                    }
                    else {
                        checkRefiningParameterType(member, refinedMember,
                                refinedParams, rparam, refinedParameterType,
                                param, parameterType, typeNode);
                    }
                }
                param.setDefaulted(rparam.isDefaulted());
            }
        }
    }

	private void handleWrongParameterListLength(Tree.Declaration that,
            ProducedReference member, ProducedReference refinedMember) {
	    that.addError("member does not have the same number of parameters as the member it refines: " + 
                   member.getDeclaration().getName() + 
                   " declared by " + containerName(member) +
                   " refining " + refinedMember.getDeclaration().getName() +
                   " declared by " + containerName(refinedMember), 9100);
    }

	private void checkRefiningParameterType(ProducedReference member,
            ProducedReference refinedMember, ParameterList refinedParams,
            Parameter rparam, ProducedType refinedParameterType,
            Parameter param, ProducedType parameterType, Node typeNode) {
	    //TODO: consider type parameter substitution!!!
	    checkIsExactlyForInterop(refinedMember, 
	            refinedParams.isNamedParametersSupported(), 
	            parameterType, refinedParameterType, typeNode,
	            "type of parameter " + param.getName() + " of " + 
	                    member.getDeclaration().getName() +
	                    " declared by " + containerName(member) +
	                    " is different to type of corresponding parameter " +
	                    rparam.getName() + " of refined member " + 
	                    refinedMember.getDeclaration().getName() + " of " +
	                    containerName(refinedMember), 9200);
    }

	private void handleUnknownParameterType(ProducedReference member,
            ProducedReference refinedMember, Parameter param, Node typeNode) {
	    typeNode.addError("could not determine if parameter type is the same as the corresponding parameter of refined member: " +
	            param.getName() + " of " + member.getDeclaration().getName() + 
	            " declared by " + containerName(member) +
	            " refining " + refinedMember.getDeclaration().getName() +
	            " declared by " + containerName(refinedMember));
    }

	private void checkRefinedParameterDynamicallyTyped(
            ProducedReference member, ProducedReference refinedMember,
            Parameter rparam, Parameter param, Node typeNode) {
	    if (!rparam.getModel().isDynamicallyTyped()) {
	    	typeNode.addError("parameter which refines statically typed parameter must also be statically typed: " + 
	    			param.getName() + " of " + member.getDeclaration().getName() + 
	                " declared by " + containerName(member) +
	                " refining " + refinedMember.getDeclaration().getName() +
	                " declared by " + containerName(refinedMember));
	    }
    }

	private void checkRefiningParameterDynamicallyTyped(
            ProducedReference member, ProducedReference refinedMember,
            Parameter param, Node typeNode) {
	    if (!param.getModel().isDynamicallyTyped()) {
	    	typeNode.addError("parameter which refines dynamically typed parameter must also be dynamically typed: " + 
	    			param.getName() + " of " + member.getDeclaration().getName() + 
	                " declared by " + containerName(member) +
	                " refining " + refinedMember.getDeclaration().getName() +
	                " declared by " + containerName(refinedMember));
	    }
    }

    private void checkIsExactlyForInterop(ProducedReference refinedMember, boolean isCeylon,  
            ProducedType parameterType, ProducedType refinedParameterType, Node node, 
            String message, int code) {
        if (isCeylon) {
            // it must be a Ceylon method
            checkIsExactly(parameterType, refinedParameterType, node, message, 9200);
        }
        else {
            // we're refining a Java method
            ProducedType refinedDefiniteType = refinedMember.getDeclaration().getUnit()
            		.getDefiniteType(refinedParameterType);
            checkIsExactlyOneOf(parameterType, refinedParameterType, refinedDefiniteType, 
            		node, message);
        }
    }

    private static Tree.ParameterList getParameterList(Tree.Declaration that, int i) {
        if (that instanceof Tree.AnyMethod) {
            return ((Tree.AnyMethod) that).getParameterLists().get(i);
        }
        else if (that instanceof Tree.AnyClass) {
            return ((Tree.AnyClass) that).getParameterList();
        }
        else {
            return null;
        }
    }
    
    @Override
    public void visit(Tree.ParameterList that) {
        super.visit(that);
        boolean foundSequenced = false;
        boolean foundDefault = false;
        ParameterList pl = that.getModel();
        for (Tree.Parameter p: that.getParameters()) {
            if (p!=null) {
                Parameter pm = p.getParameterModel();
                if (pm.isDefaulted()) {
                    if (foundSequenced) {
                        p.addError("defaulted parameter must occur before variadic parameter");
                    }
                    foundDefault = true;
                    if (!pl.isFirst()) {
                        p.addError("only the first parameter list may have defaulted parameters");
                    }
                }
                else if (pm.isSequenced()) {
                    if (foundSequenced) {
                        p.addError("parameter list may have at most one variadic parameter");
                    }
                    foundSequenced = true;
                    if (!pl.isFirst()) {
                        p.addError("only the first parameter list may have a variadic parameter");
                    }
                    if (foundDefault && 
                            pm.isAtLeastOne()) {
                        p.addError("parameter list with defaulted parameters may not have a nonempty variadic parameter");
                    }
                }
                else {
                    if (foundDefault) {
                        p.addError("required parameter must occur before defaulted parameters");
                    }
                    if (foundSequenced) {
                        p.addError("required parameter must occur before variadic parameter");
                    }
                }
            }
        }
    }
    
}
