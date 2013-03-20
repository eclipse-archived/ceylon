package com.redhat.ceylon.compiler.typechecker.analyzer;


import static com.redhat.ceylon.compiler.typechecker.analyzer.Util.checkAssignable;
import static com.redhat.ceylon.compiler.typechecker.analyzer.Util.checkAssignableToOneOf;
import static com.redhat.ceylon.compiler.typechecker.analyzer.Util.checkIsExactly;
import static com.redhat.ceylon.compiler.typechecker.analyzer.Util.checkIsExactlyOneOf;
import static com.redhat.ceylon.compiler.typechecker.analyzer.Util.typeDescription;
import static com.redhat.ceylon.compiler.typechecker.analyzer.Util.typeNamesAsIntersection;
import static com.redhat.ceylon.compiler.typechecker.model.Util.addToIntersection;
import static com.redhat.ceylon.compiler.typechecker.model.Util.areConsistentSupertypes;
import static com.redhat.ceylon.compiler.typechecker.model.Util.getSignature;
import static com.redhat.ceylon.compiler.typechecker.model.Util.isCompletelyVisible;
import static com.redhat.ceylon.compiler.typechecker.model.Util.isResolvable;
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
import com.redhat.ceylon.compiler.typechecker.model.FunctionalParameter;
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
import com.redhat.ceylon.compiler.typechecker.model.ValueParameter;
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
    
    private boolean broken=false;
    
    @Override
    public void visit(Tree.AnyMethod that) {
        super.visit(that);
        TypedDeclaration td = that.getDeclarationModel();
        for (Tree.ParameterList list: that.getParameterLists()) {
            for (Tree.Parameter tp: list.getParameters()) {
                if (tp!=null) {
                    Parameter p = tp.getDeclarationModel();
                    checkVisibility(tp, td, p.getType(), "declaration");
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
                    Parameter p = tp.getDeclarationModel();
                    checkVisibility(tp, td, p.getType(), "declaration");
                }
            }
        }
    }

    private Module getModule(Element element){
        Package typePackage = element.getUnit().getPackage();
        return typePackage != null ? typePackage.getModule() : null;
    }

    private boolean inExportedScope(Declaration decl) {
        // if it has a visible scope it's not exported outside the module
        if(decl.getVisibleScope() != null)
            return false;
        // now perhaps its package is not shared
        Package p = decl.getUnit().getPackage();
        return p != null && p.isShared();
    }

    private boolean checkModuleVisibility(Declaration member, ProducedType pt) {
        if(!inExportedScope(member))
            return true;
        Module declarationModule = getModule(member);
        if(declarationModule == null)
            return true;
        return isCompletelyVisibleFromOtherModules(member, pt, declarationModule);
    }

    private boolean isCompletelyVisibleFromOtherModules(Declaration member, ProducedType pt, Module thisModule) {
        if (pt.getDeclaration() instanceof UnionType) {
            for (ProducedType ct: pt.getDeclaration().getCaseTypes()) {
                if ( !isCompletelyVisibleFromOtherModules(member, ct.substitute(pt.getTypeArguments()), thisModule) ) {
                    return false;
                }
            }
            return true;
        }
        else if (pt.getDeclaration() instanceof IntersectionType) {
            for (ProducedType ct: pt.getDeclaration().getSatisfiedTypes()) {
                if ( !isCompletelyVisibleFromOtherModules(member, ct.substitute(pt.getTypeArguments()), thisModule) ) {
                    return false;
                }
            }
            return true;
        }
        else {
            if (!isVisibleFromOtherModules(member, thisModule, pt.getDeclaration())) {
                return false;
            }
            for (ProducedType at: pt.getTypeArgumentList()) {
                if ( at!=null && !isCompletelyVisibleFromOtherModules(member, at, thisModule) ) {
                    return false;
                }
            }
            return true;
        }
    }

    private boolean isVisibleFromOtherModules(Declaration member, Module thisModule, TypeDeclaration type) {
        // type parameters are OK
        if(type instanceof TypeParameter)
            return true;
        
        Module typeModule = getModule(type);
        if(typeModule != null && thisModule != null && thisModule != typeModule){
            // find the module import, but only in exported imports, otherwise it's an error anyways

            // language module stuff is automagically exported
            if(typeModule == thisModule.getLanguageModule())
                return true;
            
            // try to find a direct import first
            for(ModuleImport imp : thisModule.getImports()){
                if(imp.isExport() && imp.getModule() == typeModule){
                    // found it
                    return true;
                }
            }
            // then try the more expensive implicit imports
            Set<Module> visited = new HashSet<Module>();
            visited.add(thisModule);
            for(ModuleImport imp : thisModule.getImports()){
                // now try implicit dependencies
                if(imp.isExport() && includedImplicitely(imp.getModule(), typeModule, visited)){
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

    private boolean includedImplicitely(Module importedModule, Module targetModule, Set<Module> visited) {
        // don't visit them twice
        if(!visited.add(importedModule))
            return false;
        for(ModuleImport imp : importedModule.getImports()){
            // only consider modules it exported back to us
            if(imp.isExport()
                    && (imp.getModule() == targetModule
                        || includedImplicitely(imp.getModule(), targetModule, visited)))
                return true;
        }
        return false;
    }
    
    @Override public void visit(Tree.TypeDeclaration that) {
        boolean ob = broken;
        broken = false;
        TypeDeclaration td = that.getDeclarationModel();
        if (td!=null) {
            validateRefinement(that, td);
        }
        super.visit(that);
        broken = ob;
    }

    @Override public void visit(Tree.ObjectDefinition that) {
        boolean ob = broken;
        broken = false;
        Value v = that.getDeclarationModel();
        if (v!=null) {
            validateRefinement(that, v.getType().getDeclaration());
        }
        super.visit(that);
        broken = ob;
    }

    @Override public void visit(Tree.ObjectArgument that) {
        boolean ob = broken;
        broken = false;
        Value v = that.getDeclarationModel();
        if (v!=null) {
            validateRefinement(that, v.getType().getDeclaration());
        }
        super.visit(that);
        broken = ob;
    }

	private void validateRefinement(Tree.StatementOrArgument that, TypeDeclaration td) {
		List<ProducedType> supertypes = td.getType().getSupertypes();
		if (td instanceof TypeAlias && 
				td.getExtendedType()!=null) {
			supertypes.add(td.getExtendedType());
		}
		for (int i=0; i<supertypes.size(); i++) {
		    ProducedType st1 = supertypes.get(i);
		    for (int j=i+1; j<supertypes.size(); j++) {
		        ProducedType st2 = supertypes.get(j);
		        checkSupertypeIntersection(that, td, st1, st2);
		    }
		}
		Unit unit = that.getUnit();
        if (!broken && td instanceof TypeParameter) {
		    List<ProducedType> list = new ArrayList<ProducedType>();
		    List<ProducedType> upperBounds = td.getSatisfiedTypes();
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
		if (!broken) {
		    Set<String> errors = new HashSet<String>();
		    for (ProducedType st: supertypes) {
		        // don't do this check for ObjectArguments
		        if (that instanceof Tree.Declaration) {
		            if (!isCompletelyVisible(td, st)) {
		                that.addError("supertype of type is not visible everywhere type is visible: " + 
		                        st.getProducedTypeName(unit));
		            }
		            if(!checkModuleVisibility(td, st) ) {
		                that.addError("supertype occurs in a type that is visible outside this module,"
		                        +" but comes from an imported module that is not re-exported: " +
		                        st.getProducedTypeName(unit));
		            }

		        }
		        if (td instanceof ClassOrInterface && 
		                !((ClassOrInterface) td).isAbstract()) {
		            for (Declaration d: st.getDeclaration().getMembers()) {
		                if (d.isShared() && isResolvable(d) && 
		                		!errors.contains(d.getName())) {
		                    Declaration r = td.getMember(d.getName(), null, false);
		                    if (r==null || !r.refines(d) && 
		                    		//squash bogus error when there is a dupe declaration
		                    		!r.getContainer().equals(td)) {
		                        //TODO: This seems to dupe some checks that are already 
		                        //      done in TypeHierarchyVisitor, resulting in
		                        //      multiple errors
		                    	//TODO: figure out which other declaration causes the
		                    	//      problem and display it to the user!
		                    	if (r==null) {
		                    		that.addError("member " + d.getName() +
		                    				" is inherited ambiguously by " + td.getName() +
		                    				" from " + st.getDeclaration().getName() +  
		                    				" and another unrelated supertype");
		                    	}
		                    	else {
		                    		that.addError("member " + d.getName() + 
		                    				" is inherited ambiguously by " + td.getName() +
		                    				" from " + st.getDeclaration().getName() +  
		                    				" and another subtype of " + ((TypeDeclaration) r.getContainer()).getName() + 
		                    				" and so must be refined by " + td.getName(), 350);
		                    	}
		                        errors.add(d.getName());
		                    }
		                    /*else if (!r.getContainer().equals(td)) { //the case where the member is actually declared by the current type is handled by checkRefinedTypeAndParameterTypes()
		                        //TODO: I think this case never occurs, because getMember() always
		                        //      returns null in the case of an ambiguity
		                        List<ProducedType> typeArgs = new ArrayList<ProducedType>();
		                        if (d instanceof Generic) {
		                            for (TypeParameter refinedTypeParam: ((Generic) d).getTypeParameters()) {
		                                typeArgs.add(refinedTypeParam.getType());
		                            }
		                        }
		                        ProducedType t = td.getType().getTypedReference(r, typeArgs).getType();
		                        ProducedType it = st.getTypedReference(d, typeArgs).getType();
		                        checkAssignable(t, it, that, "type of member " + d.getName() + 
		                                " must be assignable to all types inherited from instantiations of " +
		                                st.getDeclaration().getName());
		                    }*/
		                }
		            }
		        }
		    }
		}
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
                broken = true;
            }
        }
    }

    @Override public void visit(Tree.TypedDeclaration that) {
        TypedDeclaration td = that.getDeclarationModel();
        checkVisibility(that, td, td.getType(), "declaration");
        super.visit(that);
    }

    private void checkVisibility(Tree.TypedDeclaration that, Declaration td, ProducedType type, String typeName) {
        if (type!=null) {
            if(!isCompletelyVisible(td, type) ) {
                that.getType().addError("type of " + typeName + " is not visible everywhere declaration is visible: " 
                        + td.getName());
            }
            if(!checkModuleVisibility(td, type) ) {
                that.getType().addError("type occurs in a " + typeName + " that is visible outside this module,"
                        +" but comes from an imported module that is not re-exported: "
                        + td.getName());
            }
        }
    }

    @Override public void visit(Tree.Declaration that) {
        super.visit(that);
        Declaration dec = that.getDeclarationModel();
        if (dec!=null) {
            boolean toplevel = dec.getContainer() instanceof Package;
            boolean member = dec.isClassOrInterfaceMember() &&
                    dec.isShared() &&
                    !(dec instanceof TypeParameter); //TODO: what about nested interfaces and abstract classes?!
            
            if (!toplevel && !member && dec.isShared()) {
                that.addError("shared declaration is not a member of a class, interface, or package", 1200);
            }
            
            boolean mayBeShared = 
                    dec instanceof MethodOrValue || 
                    dec instanceof ClassOrInterface ||
                    dec instanceof TypeAlias ||
                    dec instanceof Parameter;
            if (!mayBeShared && dec.isShared()) {
                that.addError("shared declaration is not a function, value, class, interface, or alias", 1200);
            }
            
            boolean mayBeRefined =
                    dec instanceof Value || 
                    dec instanceof Method ||
                    dec instanceof Class ||
                    dec instanceof Parameter;
            if (!mayBeRefined) {
                checkNonrefinableDeclaration(that, dec);
            }

            if (!member) {
                checkNonMember(that, dec);
            }
            
            /*if (!dec.isShared()) {
                checkUnshared(that, dec);
            }*/
            
            if (member) {
                if (!(dec instanceof Setter)) {
                    checkMember(that, dec);
                }
                ClassOrInterface declaringType = (ClassOrInterface) dec.getContainer();
                Declaration refined = declaringType.getRefinedMember(dec.getName(), 
                		getSignature(dec), false);
                dec.setRefinedDeclaration(refined);
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
        List<Declaration> others = ci.getInheritedMembers( dec.getName() );
        if (others.isEmpty()) {
            if (dec.isActual()) {
                that.addError("actual member does not refine any inherited member", 1300);
            }
        }
        else {
            for (Declaration refined: others) {
                if (dec instanceof Method || 
                		dec instanceof FunctionalParameter) {
                    if (!(refined instanceof Method) && 
                    		!(refined instanceof FunctionalParameter)) {
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
                    	refined instanceof Method ||
                    	refined instanceof FunctionalParameter) {
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
                if (!broken) checkRefinedTypeAndParameterTypes(that, dec, ci, refined);
            }
        }
    }
    
    static String message(Declaration refined) {
        return refined.getName() + " in " + 
        		((Declaration) refined.getContainer()).getName();
    }

    private void checkRefinedTypeAndParameterTypes(Tree.Declaration that,
            Declaration dec, ClassOrInterface ci, Declaration refined) {
        List<ProducedType> typeArgs = new ArrayList<ProducedType>();
        if (refined instanceof Generic && dec instanceof Generic) {
            List<TypeParameter> refinedTypeParams = ((Generic) refined).getTypeParameters();
            List<TypeParameter> refiningTypeParams = ((Generic) dec).getTypeParameters();
            int refiningSize = refiningTypeParams.size();
            int refinedSize = refinedTypeParams.size();
            if (refiningSize!=refinedSize) {
                that.addError("member does not have the same number of type parameters as refined member: " + 
                            message(refined));
            }
            for (int i=0; i<(refiningSize<=refinedSize ? refiningSize : refinedSize); i++) {
                TypeParameter refinedTypeParam = refinedTypeParams.get(i);
                TypeParameter refiningTypeParam = refiningTypeParams.get(i);
                ProducedType refinedProducedType = refinedTypeParam.getType();
				for (ProducedType t: refiningTypeParam.getSatisfiedTypes()) {
                	ProducedType bound = t.substitute(singletonMap(refiningTypeParam, refinedProducedType));
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
        }
        ProducedReference refinedMember = ci.getType().getTypedReference(refined, typeArgs);
        ProducedReference refiningMember = ci.getType().getTypedReference(dec, typeArgs);
        if (refinedMember.getDeclaration() instanceof TypedDeclaration &&
                ((TypedDeclaration) refinedMember.getDeclaration()).isVariable()) {
            checkRefinedMemberTypeExactly(refiningMember, refinedMember, that,
                    "type of member must be exactly the same as type of variable refined member: " + 
                    message(refined));
        }
        else {
            //note: this version checks return type and parameter types in one shot, but the
            //resulting error messages aren't as friendly, so do it the hard way instead!
            //checkAssignable(refiningMember.getFullType(), refinedMember.getFullType(), that,
            checkRefinedMemberTypeAssignable(refiningMember, refinedMember, that,
                    "type of member must be assignable to type of refined member: " + 
                    message(refined));
        }
        if (dec instanceof Functional && refined instanceof Functional) {
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
    }

    private void checkRefinedMemberTypeAssignable(ProducedReference refiningMember, ProducedReference refinedMember, 
            Tree.Declaration that, String message) {
        if (hasUncheckedNullType(refinedMember)) {
            ProducedType optionalRefinedType = refiningMember.getDeclaration().getUnit().getOptionalType(refinedMember.getType());
            checkAssignableToOneOf(refiningMember.getType(), refinedMember.getType(), optionalRefinedType, that,
                    message);
        }
        else{
            checkAssignable(refiningMember.getType(), refinedMember.getType(), that,
                    message);
        }
    }

    private void checkRefinedMemberTypeExactly(ProducedReference refiningMember, ProducedReference refinedMember, 
            Tree.Declaration that, String message) {
        if (hasUncheckedNullType(refinedMember)) {
            ProducedType optionalRefinedType = refiningMember.getDeclaration().getUnit().getOptionalType(refinedMember.getType());
            checkIsExactlyOneOf(refiningMember.getType(), refinedMember.getType(), optionalRefinedType, that,
                    message);
        }
        else{
            checkIsExactly(refiningMember.getType(), refinedMember.getType(), that,
                    message);
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

    private void checkNonMember(Tree.Declaration that, Declaration dec) {
    	if (!dec.isClassOrInterfaceMember()) {
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
    	else if (!dec.isShared()) {
    		if (dec.isActual()) {
    			that.addError("actual declaration must be shared: " + dec.getName(), 1301);
    		}
    		if (dec.isFormal()) {
    			that.addError("formal declaration must be shared: " + dec.getName(), 1302);
    		}
    		if (dec.isDefault()) {
    			that.addError("default declaration must be shared: " + dec.getName(), 1303);
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

    private void checkParameterTypes(Tree.Declaration that, Tree.ParameterList pl,
            ProducedReference member, ProducedReference refinedMember,
            ParameterList params, ParameterList refinedParams) {
        if (params.getParameters().size()!=refinedParams.getParameters().size()) {
           that.addError("member does not have the same number of parameters as the member it refines: " + 
        		   member.getDeclaration().getName());
        }
        else {
            for (int i=0; i<params.getParameters().size(); i++) {
                Parameter rparam = refinedParams.getParameters().get(i);
                ProducedType refinedParameterType = refinedMember.getTypedParameter(rparam).getFullType();
                Parameter param = params.getParameters().get(i);
                ProducedType parameterType = member.getTypedParameter(param).getFullType();
                Tree.Parameter p = pl.getParameters().get(i);
                if (p!=null) {
                    Tree.Type type = p.getType(); //some kind of syntax error
                    if (type!=null) {
                        if (refinedParameterType==null || parameterType==null) {
                            type.addError("could not determine if parameter type is the same as the corresponding parameter of refined member: " +
                            		param.getName() + " of " + member.getDeclaration().getName());
                        }
                        else {
                            //TODO: consider type parameter substitution!!!
                            checkIsExactlyForInterop(refinedMember, 
                                    refinedParams.isNamedParametersSupported(), 
                                    parameterType, refinedParameterType, type,
                                    "type of parameter " + param.getName() + " of " + 
                                    member.getDeclaration().getName() +
                                    " is different to type of corresponding parameter " +
                                    rparam.getName() + " of refined member");
                        }
                    }
                }
                param.setDefaulted(rparam.isDefaulted());
            }
        }
    }

    private void checkIsExactlyForInterop(ProducedReference refinedMember, boolean isCeylon,  
            ProducedType parameterType, ProducedType refinedParameterType, Tree.Type type, String message) {
        if(isCeylon){
            // it must be a Ceylon method
            checkIsExactly(parameterType, refinedParameterType, type, message);
        }else{
            // we're refining a Java method
            ProducedType refinedDefiniteType = refinedMember.getDeclaration().getUnit().getDefiniteType(refinedParameterType);
            checkIsExactlyOneOf(parameterType, refinedParameterType, refinedDefiniteType, type, message);
        }
    }

    private static Tree.ParameterList getParameterList(Tree.Declaration that, int i) {
        Tree.ParameterList pl=null;
        if (that instanceof Tree.AnyMethod) {
            pl = ((Tree.AnyMethod) that).getParameterLists().get(i);
        }
        else if (that instanceof Tree.AnyClass) {
            pl = ((Tree.AnyClass) that).getParameterList();
        }
        else if (that instanceof Tree.FunctionalParameterDeclaration) {
            pl = ((Tree.FunctionalParameterDeclaration) that).getParameterLists().get(i);
        }
        return pl;
    }
    
    @Override
    public void visit(Tree.ParameterList that) {
    	super.visit(that);
        boolean foundSequenced = false;
        boolean foundDefault = false;
        ParameterList pl = that.getModel();
        for (Tree.Parameter p: that.getParameters()) {
            if (p!=null) {
                if (p.getDefaultArgument()!=null) {
                    if (foundSequenced) {
                    	p.getDefaultArgument()
                            .addError("defaulted parameter must occur before variadic parameter");
                    }
                    foundDefault = true;
                    if (!pl.isFirst()) {
                        p.getDefaultArgument()
                            .addError("only the first parameter list may have defaulted parameters");
                    }
                }
                else if (isSequenced(p)) {
                	Tree.Type st = p.getType();
                    if (foundSequenced) {
						st.addError("parameter list may have at most one variadic parameter");
                    }
                    foundSequenced = true;
                    if (!pl.isFirst()) {
                    	st.addError("only the first parameter list may have a variadic parameter");
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

	private boolean isSequenced(Tree.Parameter p) {
		return (p.getDeclarationModel() instanceof ValueParameter) &&
				((ValueParameter) p.getDeclarationModel()).isSequenced();
	}
    
}
