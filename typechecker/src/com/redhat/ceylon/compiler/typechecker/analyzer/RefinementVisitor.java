package com.redhat.ceylon.compiler.typechecker.analyzer;


import static com.redhat.ceylon.compiler.typechecker.analyzer.DeclarationVisitor.setVisibleScope;
import static com.redhat.ceylon.compiler.typechecker.analyzer.ExpressionVisitor.getRefinedMember;
import static com.redhat.ceylon.compiler.typechecker.analyzer.Util.checkAssignable;
import static com.redhat.ceylon.compiler.typechecker.analyzer.Util.checkAssignableToOneOf;
import static com.redhat.ceylon.compiler.typechecker.analyzer.Util.checkIsExactly;
import static com.redhat.ceylon.compiler.typechecker.analyzer.Util.checkIsExactlyForInterop;
import static com.redhat.ceylon.compiler.typechecker.analyzer.Util.checkIsExactlyOneOf;
import static com.redhat.ceylon.compiler.typechecker.analyzer.Util.declaredInPackage;
import static com.redhat.ceylon.compiler.typechecker.analyzer.Util.getTypeErrorNode;
import static com.redhat.ceylon.compiler.typechecker.analyzer.Util.getTypedDeclaration;
import static com.redhat.ceylon.compiler.typechecker.analyzer.Util.message;
import static com.redhat.ceylon.compiler.typechecker.tree.Util.name;
import static com.redhat.ceylon.model.typechecker.model.Util.getInheritedDeclarations;
import static com.redhat.ceylon.model.typechecker.model.Util.getInterveningRefinements;
import static com.redhat.ceylon.model.typechecker.model.Util.getNativeDeclaration;
import static com.redhat.ceylon.model.typechecker.model.Util.getRealScope;
import static com.redhat.ceylon.model.typechecker.model.Util.getSignature;
import static com.redhat.ceylon.model.typechecker.model.Util.hasNativeImplementation;
import static com.redhat.ceylon.model.typechecker.model.Util.isOverloadedVersion;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.redhat.ceylon.common.Backend;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;
import com.redhat.ceylon.model.typechecker.model.Annotation;
import com.redhat.ceylon.model.typechecker.model.Class;
import com.redhat.ceylon.model.typechecker.model.ClassOrInterface;
import com.redhat.ceylon.model.typechecker.model.Constructor;
import com.redhat.ceylon.model.typechecker.model.Declaration;
import com.redhat.ceylon.model.typechecker.model.Functional;
import com.redhat.ceylon.model.typechecker.model.Generic;
import com.redhat.ceylon.model.typechecker.model.LazyProducedType;
import com.redhat.ceylon.model.typechecker.model.Method;
import com.redhat.ceylon.model.typechecker.model.Overloadable;
import com.redhat.ceylon.model.typechecker.model.Package;
import com.redhat.ceylon.model.typechecker.model.Parameter;
import com.redhat.ceylon.model.typechecker.model.ParameterList;
import com.redhat.ceylon.model.typechecker.model.ProducedReference;
import com.redhat.ceylon.model.typechecker.model.ProducedType;
import com.redhat.ceylon.model.typechecker.model.Scope;
import com.redhat.ceylon.model.typechecker.model.Setter;
import com.redhat.ceylon.model.typechecker.model.SiteVariance;
import com.redhat.ceylon.model.typechecker.model.Specification;
import com.redhat.ceylon.model.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.model.typechecker.model.TypeParameter;
import com.redhat.ceylon.model.typechecker.model.TypedDeclaration;
import com.redhat.ceylon.model.typechecker.model.Unit;
import com.redhat.ceylon.model.typechecker.model.Value;

/**
 * Validates some simple rules relating to refinement.
 * 
 * @see TypeHierarchyVisitor for the fancy stuff!
 * 
 * @author Gavin King
 *
 */
public class RefinementVisitor extends Visitor {
        
    @Override
    public void visit(Tree.AnyMethod that) {
        super.visit(that);
        inheritDefaultedArguments(that.getDeclarationModel());
    }

    @Override
    public void visit(Tree.AnyClass that) {
        super.visit(that);
        inheritDefaultedArguments(that.getDeclarationModel());
    }

    private void inheritDefaultedArguments(Declaration d) {
        Declaration rd = d.getRefinedDeclaration();
        if (rd!=d && 
                rd instanceof Functional && 
                d instanceof Functional) {
            List<ParameterList> tdpls = 
                    ((Functional) d).getParameterLists();
            List<ParameterList> rdpls = 
                    ((Functional) rd).getParameterLists();
            if (!tdpls.isEmpty() && !rdpls.isEmpty()) {
                List<Parameter> tdps = 
                        tdpls.get(0).getParameters();
                List<Parameter> rdps = 
                        rdpls.get(0).getParameters();
                for (int i=0; i<tdps.size() && i<rdps.size(); i++) {
                    Parameter tdp = tdps.get(i);
                    Parameter rdp = rdps.get(i);
                    if (tdp!=null && rdp!=null) {
                        tdp.setDefaulted(rdp.isDefaulted());
                    }
                }
            }
        }
    }

    @Override public void visit(Tree.Declaration that) {
        super.visit(that);
        
        Declaration dec = that.getDeclarationModel();
        if (dec!=null) {
            
            boolean mayBeShared = 
                    dec.isToplevel() || 
                    dec.isClassOrInterfaceMember();
            if (dec.isShared() && !mayBeShared) {
                that.addError("shared declaration is not a member of a class, interface, or package: " +
                        message(dec), 1200);
            }
            
            boolean mayBeRefined =
                    dec instanceof Value || 
                    dec instanceof Method ||
                    dec instanceof Class;
            if (!mayBeRefined) {
                checkNonrefinableDeclaration(that, dec);
            }
            
            boolean member = 
                    dec.isClassOrInterfaceMember() &&
                    dec.isShared() &&
                    !(dec instanceof TypeParameter); //TODO: what about nested interfaces and abstract classes?!            
            if (member) {
                checkMember(that, dec);
            }
            else {
                checkNonMember(that, dec);
                if (isOverloadedVersion(dec)) {
                    that.addError("name is not unique in scope: '" + 
                            dec.getName() + "'");
                }
            }
            
            boolean isNativeWithImplementation = 
                    hasNativeImplementation(dec);
            if (isNativeWithImplementation) {
                checkNative(that, dec);
            }
        }
        
    }

    private void checkNative(Tree.Declaration that, Declaration dec) {
        // Find the header first (if it exists)
        Declaration abstraction = getNativeDeclaration(dec, Backend.None);
        if (abstraction == null) {
            // Abstraction-less native implementation, check it's not shared
            if (dec.isShared()) {
                that.addError("native implementation must have a header: " + 
                        dec.getName());
            }
            // If there's no abstraction we just compare to the first implementation in the list
            Overloadable overloadable = (Overloadable) dec;
            Declaration firstImpl = 
                    overloadable.getOverloads().get(0);
            if (dec != firstImpl) {
                checkSameDeclaration(that, dec, firstImpl);
            }
        }
        else {
            if (dec != abstraction) {
                checkSameDeclaration(that, dec, abstraction);
            }
        }
    }
    
    private void checkSameDeclaration(Tree.Declaration that, 
            Declaration dec, Declaration abstraction) {
        if (dec.getClass() == abstraction.getClass()) {
            if (dec instanceof Method) {
                checkSameMethod(that, 
                        (Method) dec, 
                        (Method) abstraction);
            }
            else if (dec instanceof Value) {
                checkSameValue(that, 
                        (Value) dec, 
                        (Value) abstraction);
            }
            else if (dec instanceof Class) {
                checkSameClass(that, 
                        (Class) dec, 
                        (Class) abstraction);
            }
        }
        else {
            that.addError("native declarations not of same type: " + 
                    message(dec));
        }
    }
    
    private void checkSameClass(Tree.Declaration that, 
            Class dec, Class abstraction) {
        if (dec.isShared() && !abstraction.isShared()) {
            that.addError("native abstraction is not shared: " + 
                    message(dec));
        }
        if (!dec.isShared() && abstraction.isShared()) {
            that.addError("native abstraction is shared: " + 
                    message(dec));
        }
        if (dec.isAbstract() && !abstraction.isAbstract()) {
            that.addError("native abstraction is not abstract: " + 
                    message(dec));
        }
        if (!dec.isAbstract() && abstraction.isAbstract()) {
            that.addError("native abstraction is abstract: " + 
                    message(dec));
        }
        if (dec.isFinal() && !abstraction.isFinal()) {
            that.addError("native abstraction is not final: " + 
                    message(dec));
        }
        if (!dec.isFinal() && abstraction.isFinal()) {
            that.addError("native abstraction is final: " + 
                    message(dec));
        }
        if (dec.isSealed() && !abstraction.isSealed()) {
            that.addError("native abstraction is not sealed: " + 
                    message(dec));
        }
        if (!dec.isSealed() && abstraction.isSealed()) {
            that.addError("native abstraction is sealed: " + 
                    message(dec));
        }
        if (dec.isAnnotation() && !abstraction.isAnnotation()) {
            that.addError("native abstraction is not an annotation type: " + 
                    message(dec));
        }
        if (!dec.isAnnotation() && abstraction.isAnnotation()) {
            that.addError("native abstraction is an annotation type: " + 
                    message(dec));
        }
        ProducedType dext = dec.getExtendedType();
        ProducedType aext = abstraction.getExtendedType();
        if ((dext != null && aext == null)
                || (dext == null && aext != null)
                || !dext.isExactly(aext)) {
            that.addError("native classes do not extend the same type: " + 
                    message(dec));
        }
        List<ProducedType> dst = dec.getSatisfiedTypes();
        List<ProducedType> ast = abstraction.getSatisfiedTypes();
        if (dst.size() != ast.size() || !dst.containsAll(ast)) {
            that.addError("native classes do not satisfy the same interfaces: " + 
                    message(dec));
        }
        // FIXME probably not the right tests
        checkClassParameters(that, 
                dec, abstraction, 
                dec.getReference(), 
                abstraction.getReference(), 
                true);
        checkRefiningMemberTypeParameters(that, 
                dec, abstraction,
                dec.getTypeParameters(), 
                abstraction.getTypeParameters(), 
                true);
        // TODO check shared members
    }
    
    private void checkSameMethod(Tree.Declaration that, 
            Method dec, Method abstraction) {
        if (!dec.getType().isExactly(abstraction.getType())) {
            that.addError("native implementation must have the same return type as native abstraction: " + 
                    message(dec) + " must have the type '" + 
                    abstraction.getType().getProducedTypeName(that.getUnit()) + "'");
        }
        if (dec.isShared() && !abstraction.isShared()) {
            that.addError("native abstraction is not shared: " + 
                    message(dec));
        }
        if (!dec.isShared() && abstraction.isShared()) {
            that.addError("native abstraction is shared: " + 
                    message(dec));
        }
        if (dec.isAnnotation() && !abstraction.isAnnotation()) {
            that.addError("native abstraction is not an annotation constructor: " + 
                    message(dec));
        }
        if (!dec.isAnnotation() && abstraction.isAnnotation()) {
            that.addError("native abstraction is an annotation constructor: " + 
                    message(dec));
        }
        // FIXME probably not the right tests
        checkRefiningMemberParameters(that, dec, abstraction, 
                dec.getReference(), 
                abstraction.getReference(), 
                true);
        checkRefiningMemberTypeParameters(that, dec, abstraction,
                dec.getTypeParameters(), 
                abstraction.getTypeParameters(), 
                true);
    }
    
    private void checkSameValue(Tree.Declaration that, 
            Value dec, Value abstraction) {
        if (!dec.getType().isExactly(abstraction.getType())) {
            that.addError("native implementation must have the same type as native abstraction: " + 
                    message(dec) + " must have the type '" + 
                    abstraction.getType().getProducedTypeName(that.getUnit()) + "'");
        }
        if (dec.isShared() && !abstraction.isShared()) {
            that.addError("native abstraction is not shared: " + 
                    message(dec));
        }
        if (!dec.isShared() && abstraction.isShared()) {
            that.addError("native abstraction is shared: " + 
                    message(dec));
        }
        if (dec.isVariable() && !abstraction.isVariable()) {
            that.addError("native abstraction is not variable: " + 
                    message(dec));
        }
        if (!dec.isVariable() && abstraction.isVariable()) {
            that.addError("native abstraction is variable: " + 
                    message(dec));
        }
    }
    
    private void checkClassParameters(Tree.Declaration that,
            Declaration dec, Declaration refined,
            ProducedReference refinedMember, 
            ProducedReference refiningMember,
            boolean forNative) {
        List<ParameterList> refiningParamLists = 
                ((Functional) dec).getParameterLists();
        List<ParameterList> refinedParamLists = 
                ((Functional) refined).getParameterLists();
        if (refinedParamLists.size()!=refiningParamLists.size()) {
            that.addError("native classes must have the same number of parameter lists: " + 
                    message(dec));
        }
        for (int i=0; 
                i<refinedParamLists.size() && 
                i<refiningParamLists.size(); 
                i++) {
            checkParameterTypes(that, 
                    getParameterList(that, i), 
                    refiningMember, refinedMember, 
                    refiningParamLists.get(i), 
                    refinedParamLists.get(i),
                    forNative);
        }
    }

    private void checkMember(Tree.Declaration that, Declaration dec) {
        String name = dec.getName();
        if (name==null) {
            return;
        }
        if (dec instanceof Constructor) {
            return;
        }
        if (dec instanceof Setter) {
            Value getter = ((Setter) dec).getGetter();
            Declaration rd = getter.getRefinedDeclaration();
            dec.setRefinedDeclaration(rd);
            return;
        }
        ClassOrInterface type = 
                (ClassOrInterface) dec.getContainer();
        if (dec.isFormal() && type instanceof Class) {
            Class c = (Class) type;
            if (!c.isAbstract() && !c.isFormal()) {
                that.addError("formal member belongs to non-abstract, non-formal class: " + 
                        message(dec), 1100);
            }
        }
        if (type.isDynamic()) {
            if (dec instanceof Class) {
                that.addError("member class belongs to dynamic interface");
            }
            else if (!dec.isFormal()) {
                that.addError("non-formal member belongs to dynamic interface");
            }
        }
        List<ProducedType> signature = getSignature(dec);
        Declaration root = 
                type.getRefinedMember(name, 
                        signature, false);
        boolean legallyOverloaded = 
                !isOverloadedVersion(dec);
        if (root == null || root.equals(dec)) {
            dec.setRefinedDeclaration(dec);
            if (dec.isActual()) {
                that.addError("actual member does not refine any inherited member: " + 
                        message(dec), 1300);
            }
            else if (!legallyOverloaded) {
                if (dec.isActual()) {
                    that.addError("overloaded member does not refine an inherited overloaded member: " + 
                            message(dec));
                }
                else {
                    that.addError("duplicate or overloaded member name: " + 
                            message(dec));
                }
            }
            else {
                if (!getInheritedDeclarations(name, type).isEmpty()) {
                    that.addError("duplicate or overloaded member name in type hierarchy: " + 
                            message(dec));
                }
            }
        }
        else {
            dec.setRefinedDeclaration(root);
            if (root.isPackageVisibility() && 
                    !declaredInPackage(root, that.getUnit())) {
                that.addError("refined declaration is not visible: " + 
                        message(dec) + " refines " + message(root));
            }
            boolean found = false;
            TypeDeclaration rootType = 
                    (TypeDeclaration) root.getContainer();
            for (Declaration refined: 
                    getInterveningRefinements(name, signature, 
                            root, type, rootType)) {
                if (isOverloadedVersion(refined)) {
                    //if this member is overloaded, the
                    //inherited member it refines must
                    //also be overloaded
                    legallyOverloaded = true;
                }
                found = true;
                if (dec instanceof Method) {
                    if (!(refined instanceof Method)) {
                        that.addError("refined declaration is not a method: " + 
                                message(dec) + " refines " + message(refined));
                    }
                }
                else if (dec instanceof Class) {
                    if (!(refined instanceof Class)) {
                        that.addError("refined declaration is not a class: " + 
                                message(dec) + " refines " + message(refined));
                    }
                }
                else if (dec instanceof TypedDeclaration) {
                    if (refined instanceof Class || 
                        refined instanceof Method) {
                        that.addError("refined declaration is not an attribute: " + 
                                message(dec) + " refines " + message(refined));
                    }
                    else if (refined instanceof TypedDeclaration) {
                        if (((TypedDeclaration) refined).isVariable() && 
                                !((TypedDeclaration) dec).isVariable()) {
                            if (dec instanceof Value) {
                                that.addError("non-variable attribute refines a variable attribute: " + 
                                        message(dec) + " refines " + message(refined), 804);
                            }
                            else {
                                //TODO: this message seems like it's not quite right
                                that.addError("non-variable attribute refines a variable attribute: " + 
                                        message(dec) + " refines " + message(refined));
                            }
                        }
                    }
                }
                if (!dec.isActual()) {
                    that.addError("non-actual member refines an inherited member: " + 
                            message(dec) + " refines " + message(refined), 600);
                }
                if (!refined.isDefault() && !refined.isFormal()) {
                    that.addError("member refines a non-default, non-formal member: " + 
                            message(dec) + " refines " + message(refined), 500);
                }
                if (!type.isInconsistentType()) {
                    checkRefinedTypeAndParameterTypes(that, dec, type, refined);
                }
            }
            if (!found) {
                if (dec instanceof Method && 
                        root instanceof Method) { //see the condition in DeclarationVisitor.checkForDuplicateDeclaration()
                    that.addError("overloaded member does not refine any inherited member: " + 
                            message(dec));
                }
            }
            else if (!legallyOverloaded) {
                that.addError("overloaded member does not exactly refine an inherited overloaded member: " +
                        message(dec));
            }
        }
    }

    /*private boolean refinesOverloaded(Declaration dec, 
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
				if (!matches(p1.getType(), p2st, dec.getUnit())) {
                    return false;
            	}
            }
        }
        return true;
    }*/
    
    private void checkRefinedTypeAndParameterTypes(Tree.Declaration that,
            Declaration dec, ClassOrInterface ci, Declaration refined) {
        
    	List<ProducedType> typeArgs;
        if (refined instanceof Generic && 
                dec instanceof Generic) {
            List<TypeParameter> refinedTypeParams = 
                    ((Generic) refined).getTypeParameters();
            List<TypeParameter> refiningTypeParams = 
                    ((Generic) dec).getTypeParameters();
            checkRefiningMemberTypeParameters(that, dec, refined, 
            		refinedTypeParams, refiningTypeParams, false);
            typeArgs = checkRefiningMemberUpperBounds(that, 
                    ci, refined, 
                    refinedTypeParams, refiningTypeParams);
        }
        else {
        	typeArgs = emptyList();
        }
        
        ProducedType cit = ci.getType();
        ProducedReference refinedMember = 
                cit.getTypedReference(refined, typeArgs);
        ProducedReference refiningMember = 
                cit.getTypedReference(dec, typeArgs);
        Declaration refinedMemberDec = 
                refinedMember.getDeclaration();
		Declaration refiningMemberDec = 
		        refiningMember.getDeclaration();
		Node typeNode = getTypeErrorNode(that);
		if (refinedMemberIsDynamicallyTyped(refinedMemberDec, 
		        refiningMemberDec)) {
        	checkRefiningMemberDynamicallyTyped(refined, 
        	        refiningMemberDec, typeNode);
        }
		else if (refiningMemberIsDynamicallyTyped(refinedMemberDec, 
		        refiningMemberDec)) {
        	checkRefinedMemberDynamicallyTyped(refined, 
        	        refinedMemberDec, typeNode);
        }
		else if (refinedMemberIsVariable(refinedMemberDec)) {
            checkRefinedMemberTypeExactly(refiningMember, 
                    refinedMember, typeNode, refined);
        }
        else {
            //note: this version checks return type and parameter types in one shot, but the
            //resulting error messages aren't as friendly, so do it the hard way instead!
            //checkAssignable(refiningMember.getFullType(), refinedMember.getFullType(), that,
            checkRefinedMemberTypeAssignable(refiningMember, 
                    refinedMember, typeNode, refined);
        }
        if (dec instanceof Functional && 
                refined instanceof Functional) {
           checkRefiningMemberParameters(that, dec, refined, 
                   refinedMember, refiningMember, false);
        }
    }

	private void checkRefiningMemberParameters(Tree.Declaration that,
            Declaration dec, Declaration refined,
            ProducedReference refinedMember, 
            ProducedReference refiningMember,
            boolean forNative) {
		List<ParameterList> refiningParamLists = 
		        ((Functional) dec).getParameterLists();
		List<ParameterList> refinedParamLists = 
		        ((Functional) refined).getParameterLists();
		if (refinedParamLists.size()!=refiningParamLists.size()) {
		    String subject = forNative ? 
		            "native abstraction" : "refined member";
		    String current = forNative ? 
		            "native implementation" : "refining member";
			String message = 
			        current + " "
			        + "must have the same number of parameter lists as " + 
			        subject + ": " + message(dec);
			if (!forNative) {
			    message += " refines " + message(refined);
			}
            that.addError(message);
		}
		for (int i=0; 
		        i<refinedParamLists.size() && 
		        i<refiningParamLists.size(); 
		        i++) {
			checkParameterTypes(that, 
			        getParameterList(that, i), 
					refiningMember, refinedMember, 
					refiningParamLists.get(i), 
					refinedParamLists.get(i),
					forNative);
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
	    TypedDeclaration td = 
	            (TypedDeclaration) refiningMemberDec;
        if (!td.isDynamicallyTyped()) {
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
            Declaration refinedMemberDec, 
            Declaration refiningMemberDec) {
	    return refinedMemberDec instanceof TypedDeclaration && 
				refiningMemberDec instanceof TypedDeclaration && 
        		((TypedDeclaration) refinedMemberDec).isDynamicallyTyped();
    }

	private void checkRefiningMemberTypeParameters(
	        Tree.Declaration that,
	        Declaration dec, Declaration refined, 
            List<TypeParameter> refinedTypeParams,
            List<TypeParameter> refiningTypeParams,
            boolean forNative) {
	    int refiningSize = refiningTypeParams.size();
	    int refinedSize = refinedTypeParams.size();
	    if (refiningSize!=refinedSize) {
	        String subject = forNative ? 
	                "native header" : "refined member";
	        String current = forNative ? 
	                "native implementation" : "refining member";
	        String message = current + 
	                " does not have the same number of type parameters as " + 
	                subject + ": " + message(dec);
            if (!forNative) {
                message += " refines " + message(refined);
            }
            that.addError(message);
	    }
    }

	private List<ProducedType> checkRefiningMemberUpperBounds(
	        Tree.Declaration that,
            ClassOrInterface ci, Declaration refined,
            List<TypeParameter> refinedTypeParams, 
            List<TypeParameter> refiningTypeParams) {
        int refiningSize = refiningTypeParams.size();
        int refinedSize = refinedTypeParams.size();
	    int max = refiningSize <= refinedSize ? 
	            refiningSize : refinedSize;
	    if (max==0) {
	    	return emptyList();
	    }
	    //we substitute the type parameters of the refined
	    //declaration into the bounds of the refining 
	    //declaration
        Map<TypeParameter, ProducedType> substitution =
                new HashMap<TypeParameter, ProducedType>();
        for (int i=0; i<max; i++) {
            TypeParameter refinedTypeParam = 
                    refinedTypeParams.get(i);
            TypeParameter refiningTypeParam = 
                    refiningTypeParams.get(i);
            substitution.put(refiningTypeParam, 
                    refinedTypeParam.getType());
        }
        Map<TypeParameter, SiteVariance> noVariances = 
                emptyMap();
        TypeDeclaration rc = 
                (TypeDeclaration) 
                    refined.getContainer();
        //we substitute the type arguments of the subtype's
        //instantiation of the supertype into the bounds of 
        //the refined declaration
        ProducedType supertype = 
                ci.getType().getSupertype(rc);
        Map<TypeParameter, ProducedType> args = 
                supertype.getTypeArguments();
        Map<TypeParameter, SiteVariance> variances = 
                supertype.getVarianceOverrides();
		List<ProducedType> typeArgs = 
		        new ArrayList<ProducedType>(max); 
		for (int i=0; i<max; i++) {
	        TypeParameter refinedTypeParam = 
	                refinedTypeParams.get(i);
	        TypeParameter refiningTypeParam = 
	                refiningTypeParams.get(i);
	        ProducedType refinedProducedType = 
	                refinedTypeParam.getType();
	        List<ProducedType> refinedBounds = 
	                refinedTypeParam.getSatisfiedTypes();
            List<ProducedType> refiningBounds = 
                    refiningTypeParam.getSatisfiedTypes();
            for (ProducedType t: refiningBounds) {
                ProducedType refiningBound = 
	                    t.substitute(substitution, 
	                            noVariances);
	            //for every type constraint of the refining member, there must
	            //be at least one type constraint of the refined member which
	            //is assignable to it, guaranteeing that the intersection of
	            //the refined member bounds is assignable to the intersection
	            //of the refining member bounds
	            //TODO: would it be better to just form the intersections and
	            //      test assignability directly (the error messages might
	            //      not be as helpful, but it might be less restrictive)
	            boolean ok = false;
	            for (ProducedType refinedBound: refinedBounds) {
	                if (refinedBound.substitute(args, variances)
	                        .isSubtypeOf(refiningBound)) {
	                    ok = true;
	                }
	            }
	            if (!ok) {
	                that.addError("member type parameter '" + 
	                        refiningTypeParam.getName() +
	                        "' has upper bound which refined member type parameter '" + 
	                        refinedTypeParam.getName() + 
	                        "' of " + message(refined) + 
	                        " does not satisfy: '" + 
	                        t.getProducedTypeName(that.getUnit()) + 
	                        "'");
	            }
	        }
            for (ProducedType refinedBound: refinedBounds) {
                boolean ok = false;
                for (ProducedType refiningBound: refiningBounds) {
                    ProducedType bound = 
                            refiningBound.substitute(substitution, 
                                    noVariances);
                    if (refinedBound.substitute(args, variances)
                            .isSubtypeOf(bound)) {
                        ok = true;
                    }
                }
                if (!ok) {
                    that.addUnsupportedError("refined member type parameter '" + 
                            refinedTypeParam.getName() + 
                            "' of " + message(refined) +
                            " with upper bound which member type parameter '" + 
                            refiningTypeParam.getName() + 
                            "' does not satisfy not yet supported: '" + 
                            refinedBound.getProducedTypeName(that.getUnit()) + 
                            "'");
                }
            }
	        typeArgs.add(refinedProducedType);
	    }
	    return typeArgs;
    }

    private void checkRefinedMemberTypeAssignable(ProducedReference refiningMember, 
    		ProducedReference refinedMember, Node that, Declaration refined) {
        if (hasUncheckedNullType(refinedMember)) {
            Unit unit = refiningMember.getDeclaration().getUnit();
            ProducedType optionalRefinedType = 
                    unit.getOptionalType(refinedMember.getType());
            checkAssignableToOneOf(refiningMember.getType(), 
                    refinedMember.getType(), 
                    optionalRefinedType, that, 
            		"type of member must be assignable to type of refined member: " + 
            				message(refined), 9000);
        }
        else {
            checkAssignable(refiningMember.getType(), 
                    refinedMember.getType(), that,
            		"type of member must be assignable to type of refined member: " + 
            		        message(refined), 9000);
        }
    }

    private void checkRefinedMemberTypeExactly(ProducedReference refiningMember, 
    		ProducedReference refinedMember, Node that, Declaration refined) {
        if (hasUncheckedNullType(refinedMember)) {
            Unit unit = refiningMember.getDeclaration().getUnit();
            ProducedType optionalRefinedType = 
                    unit.getOptionalType(refinedMember.getType());
            checkIsExactlyOneOf(refiningMember.getType(), 
                    refinedMember.getType(), 
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
        Declaration dec = member.getDeclaration();
        return dec instanceof TypedDeclaration 
                && ((TypedDeclaration) dec).hasUncheckedNullType();
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
        boolean mayBeShared = !(dec instanceof TypeParameter);
        if (!dec.isClassOrInterfaceMember() && mayBeShared) {
            if (dec.isActual()) {
                that.addError("actual declaration is not a member of a class or interface: '" + 
                        dec.getName() + "'", 1301);
            }
            if (dec.isFormal()) {
                that.addError("formal declaration is not a member of a class or interface: '" + 
                        dec.getName() + "'", 1302);
            }
            if (dec.isDefault()) {
                that.addError("default declaration is not a member of a class or interface: '" + 
                        dec.getName() + "'", 1303);
            }
        }
        else if (!dec.isShared() && mayBeShared) {
            if (dec.isActual()) {
                that.addError("actual declaration must be shared: '" + 
                        dec.getName() + "'", 701);
            }
            if (dec.isFormal()) {
                that.addError("formal declaration must be shared: '" + 
                        dec.getName() + "'", 702);
            }
            if (dec.isDefault()) {
                that.addError("default declaration must be shared: '" + 
                        dec.getName() + "'", 703);
            }
        }
        else {
            if (dec.isActual()) {
                that.addError("declaration may not be actual: '" + 
                        dec.getName() + "'", 1301);
            }
            if (dec.isFormal()) {
                that.addError("declaration may not be formal: '" + 
                        dec.getName() + "'", 1302);
            }
            if (dec.isDefault()) {
                that.addError("declaration may not be default: '" + 
                        dec.getName() + "'", 1303);
            }
        }
    }
    
    private static String containerName(ProducedReference member) {
        Scope container = member.getDeclaration().getContainer();
        if (container instanceof Declaration) {
            Declaration dec = (Declaration) container;
            return dec.getName();
        }
        else if (container instanceof Package) {
            Package pack = (Package) container;
            return pack.getQualifiedNameString();
        }
        else {
            return "Unknown";
        }
    }

    private void checkParameterTypes(Tree.Declaration that, Tree.ParameterList pl,
            ProducedReference member, ProducedReference refinedMember,
            ParameterList params, ParameterList refinedParams, boolean forNative) {
        List<Parameter> paramsList = params.getParameters();
		List<Parameter> refinedParamsList = 
		        refinedParams.getParameters();
		if (paramsList.size()!=refinedParamsList.size()) {
           handleWrongParameterListLength(that, 
                   member, refinedMember, forNative);
        }
        else {
            for (int i=0; i<paramsList.size(); i++) {
                Parameter rparam = refinedParamsList.get(i);
                Parameter param = paramsList.get(i);
                ProducedType refinedParameterType = 
                		refinedMember.getTypedParameter(rparam)
                		        .getFullType();
                ProducedType parameterType = 
                		member.getTypedParameter(param)
                		        .getFullType();
                Tree.Parameter parameter = 
                        pl.getParameters().get(i);
                Node typeNode = parameter;
                if (parameter instanceof Tree.ParameterDeclaration) {
                	Tree.ParameterDeclaration pd = 
                	        (Tree.ParameterDeclaration) parameter;
                    Tree.Type type = 
                            pd.getTypedDeclaration().getType();
                	if (type!=null) {
                		typeNode = type;
                	}
                }
                if (parameter!=null) {
            		if (rparam.getModel().isDynamicallyTyped()) {
                    	checkRefiningParameterDynamicallyTyped(member,
                                refinedMember, param, typeNode);
                    }
            		else if (param.getModel() != null && 
            		        param.getModel().isDynamicallyTyped()) {
                    	checkRefinedParameterDynamicallyTyped(member,
                                refinedMember, rparam, param, typeNode);
                    }
            		else if (refinedParameterType==null || 
            		        parameterType==null) {
            			handleUnknownParameterType(member, 
            			        refinedMember, param,
            			        typeNode, forNative);
                    }
                    else {
                        checkRefiningParameterType(member, 
                                refinedMember, refinedParams, 
                                rparam, refinedParameterType,
                                param, parameterType,
                                typeNode, forNative);
                    }
                }
            }
        }
    }

	private void handleWrongParameterListLength(Tree.Declaration that,
            ProducedReference member, ProducedReference refinedMember,
            boolean forNative) {
	    String subject = forNative ? "native header" : "refined member";
	    String message = 
	            "member does not have the same number of parameters as " + 
	            subject + ": '" + member.getDeclaration().getName() + "'";
	    if (!forNative) {
	        message += " declared by '" + containerName(member) + 
	                "' refining '" + refinedMember.getDeclaration().getName() +
	                "' declared by '" + containerName(refinedMember) + "'";
	    }
	    that.addError(message, 9100);
    }

	private static void checkRefiningParameterType(ProducedReference member,
            ProducedReference refinedMember, ParameterList refinedParams,
            Parameter rparam, ProducedType refinedParameterType,
            Parameter param, ProducedType parameterType,
            Node typeNode, boolean forNative) {
	    //TODO: consider type parameter substitution!!!
	    String subject = forNative ? "native header" : "refined member";
	    String message = 
	            "type of parameter '" + param.getName() + "' of '" +
	            member.getDeclaration().getName() + "'";
	    if (!forNative) {
	        message += 
	                " declared by '" + containerName(member) + "'";
	    }
        message += 
                " is different to type of corresponding parameter '" +
                rparam.getName() + "' of " + subject + " '" +
                refinedMember.getDeclaration().getName() + "'";
        if (!forNative) {
            message += 
                    " of '" + containerName(refinedMember) + "'";
        }
        checkIsExactlyForInterop(typeNode.getUnit(), 
	            refinedParams.isNamedParametersSupported(), 
	            parameterType, refinedParameterType, typeNode,
	            message);
    }

	private void handleUnknownParameterType(ProducedReference member,
            ProducedReference refinedMember, Parameter param, 
            Node typeNode, boolean forNative) {
	    String subject = forNative ? "native header" : "refined member";
	    String message = 
	            "could not determine if parameter type is the same as the corresponding parameter of " + 
	            subject + ": '" +
	            param.getName() + "' of '" + 
	            member.getDeclaration().getName();
	    if (!forNative) {
	            message += 
	                    "' declared by '" + containerName(member) +
	                    "' refining '" + refinedMember.getDeclaration().getName() +
	                    "' declared by '" + containerName(refinedMember) + "'";
	    }
        typeNode.addError(message);
    }

	private void checkRefinedParameterDynamicallyTyped(
            ProducedReference member, ProducedReference refinedMember,
            Parameter rparam, Parameter param, Node typeNode) {
	    if (!rparam.getModel().isDynamicallyTyped()) {
	    	typeNode.addError(
	    	        "parameter which refines statically typed parameter must also be statically typed: '" + 
	    			param.getName() + "' of '" + 
	    	        member.getDeclaration().getName() + 
	                "' declared by '" + containerName(member) +
	                "' refining '" + refinedMember.getDeclaration().getName() +
	                "' declared by '" + containerName(refinedMember) + "'");
	    }
    }

	private void checkRefiningParameterDynamicallyTyped(
            ProducedReference member, ProducedReference refinedMember,
            Parameter param, Node typeNode) {
	    if (!param.getModel().isDynamicallyTyped()) {
	    	typeNode.addError(
	    	        "parameter which refines dynamically typed parameter must also be dynamically typed: '" + 
	    			param.getName() + "' of '" + 
	    	        member.getDeclaration().getName() + 
	                "' declared by '" + containerName(member) +
	                "' refining '" + refinedMember.getDeclaration().getName() +
	                "' declared by '" + containerName(refinedMember) + "'");
	    }
    }

    private static Tree.ParameterList getParameterList(Tree.Declaration that, int i) {
        if (that instanceof Tree.AnyMethod) {
            Tree.AnyMethod am = (Tree.AnyMethod) that;
            return am.getParameterLists().get(i);
        }
        else if (that instanceof Tree.AnyClass) {
            Tree.AnyClass ac = (Tree.AnyClass) that;
            return ac.getParameterList();
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
    
    @Override public void visit(Tree.SpecifierStatement that) {
        super.visit(that);
        List<ProducedType> sig = new ArrayList<ProducedType>();
        Tree.Term me = that.getBaseMemberExpression();
        while (me instanceof Tree.ParameterizedExpression) {
            sig.clear();
            Tree.ParameterizedExpression pe = 
                    (Tree.ParameterizedExpression) me;
            Tree.ParameterList pl = 
                    pe.getParameterLists().get(0);
            for (Tree.Parameter p: pl.getParameters()) {
                if (p!=null && p.getParameterModel()!=null) {
                    sig.add(p.getParameterModel().getType());
                }
                else {
                    sig.add(null);
                }
            }
            me = pe.getPrimary();
        }
        if (me instanceof Tree.BaseMemberExpression) {
            Tree.BaseMemberExpression bme = 
                    (Tree.BaseMemberExpression) me;
            Declaration d = 
                    getTypedDeclaration(bme.getScope(), 
                            name(bme.getIdentifier()), 
                            sig, false, that.getUnit());
            if (d instanceof TypedDeclaration) {
                that.setDeclaration((TypedDeclaration) d);
                Scope cs = getRealScope(that.getScope().getContainer());
                if (cs instanceof ClassOrInterface) {
                    ClassOrInterface ci = (ClassOrInterface) cs;
                    if (d.isClassOrInterfaceMember() &&
                            !d.getContainer().equals(cs) &&
                            ci.inherits((ClassOrInterface) d.getContainer())) {
                        // interpret this specification as a 
                        // refinement of an inherited member
                        if (d.getContainer()==that.getScope()) {
                            that.addError("parameter declaration hides refining member: '" +
                                    d.getName(that.getUnit()) + "' (rename parameter)");
                        }
                        else if (d instanceof Value) {
                            refineValue((Value) d, bme, that, ci);
                        }
                        else if (d instanceof Method) {
                            refineMethod((Method) d, bme, that, ci);
                        }
                        else {
                            //TODO!
                            bme.addError("not a reference to a formal attribute: '" + 
                                    d.getName(that.getUnit()) + "'");
                        }
                    }
                }
            }
        }
    }

    private void refineValue(final Value sv, 
            Tree.BaseMemberExpression bme,
            Tree.SpecifierStatement that, 
            ClassOrInterface c) {
        final ProducedReference rv = 
                getRefinedMember(sv, c);
        if (!sv.isFormal() && !sv.isDefault()
                && !sv.isShortcutRefinement()) { //this condition is here to squash a dupe message
            that.addError("inherited attribute may not be assigned in initializer and is neither formal nor default so may not be refined: " + 
                    message(sv), 510);
        }
        else if (sv.isVariable()) {
            that.addError("inherited attribute may not be assigned in initializer and is variable so may not be refined by non-variable: " + 
                    message(sv));
        }
        Value v = new Value();
        v.setName(sv.getName());
        v.setShared(true);
        v.setActual(true);
        v.getAnnotations().add(new Annotation("shared"));
        v.getAnnotations().add(new Annotation("actual"));
        v.setRefinedDeclaration(sv.getRefinedDeclaration());
        v.setUnit(that.getUnit());
        v.setContainer(c);
        v.setScope(c);
        v.setShortcutRefinement(true);
        setVisibleScope(v);
        c.addMember(v);
        that.setRefinement(true);
        that.setDeclaration(v);
        that.setRefined(sv);
        that.getUnit().addDeclaration(v);
        v.setType(new LazyProducedType(that.getUnit()) {
            @Override
            public ProducedType initQualifyingType() {
                return rv.getType().getQualifyingType();
            }
            @Override
            public Map<TypeParameter, ProducedType> initTypeArguments() {
                return rv.getType().getTypeArguments();
            }
            @Override
            public TypeDeclaration initDeclaration() {
                return rv.getType().getDeclaration();
            }
        });
    }

    private void refineMethod(final Method sm, 
            Tree.BaseMemberExpression bme,
            Tree.SpecifierStatement that, 
            ClassOrInterface c) {
        ClassOrInterface ci = 
                (ClassOrInterface) sm.getContainer();
        Declaration refined = 
                ci.getRefinedMember(sm.getName(), 
                        getSignature(sm), false);
        Method root = refined instanceof Method ? 
                (Method) refined : sm;
        if (!sm.isFormal() && !sm.isDefault()
                && !sm.isShortcutRefinement()) { //this condition is here to squash a dupe message
            that.addError("inherited method is neither formal nor default so may not be refined: " + 
                    message(sm));
        }
        final ProducedReference rm = getRefinedMember(sm,c);
        Method m = new Method();
        m.setName(sm.getName());
        List<Tree.ParameterList> tpls;
        Tree.Term me = that.getBaseMemberExpression();
        if (me instanceof Tree.ParameterizedExpression) {
            Tree.ParameterizedExpression pe = 
                    (Tree.ParameterizedExpression) me;
            tpls = pe.getParameterLists();
        }
        else {
            tpls = Collections.emptyList();
        }
        int i=0;
        for (ParameterList pl: sm.getParameterLists()) {
            ParameterList l = new ParameterList();
            Tree.ParameterList tpl = tpls.size()<=i ? 
                    null : tpls.get(i++);
            int j=0;
            for (final Parameter p: pl.getParameters()) {
                //TODO: meaningful errors when parameters don't line up
                //      currently this is handled elsewhere, but we can
                //      probably do it better right here
                if (tpl==null || tpl.getParameters().size()<=j) {
                    Parameter vp = new Parameter();
                    Value v = new Value();
                    vp.setModel(v);
                    v.setInitializerParameter(vp);
                    vp.setSequenced(p.isSequenced());
                    vp.setAtLeastOne(p.isAtLeastOne());
//                    vp.setDefaulted(p.isDefaulted());
                    vp.setName(p.getName());
                    v.setName(p.getName());
                    vp.setDeclaration(m);
                    v.setContainer(m);
                    v.setScope(m);
                    l.getParameters().add(vp);
                    v.setType(new LazyProducedType(that.getUnit()) {
                        @Override
                        public ProducedType initQualifyingType() {
                            return rm.getTypedParameter(p)
                                    .getFullType()
                                    .getQualifyingType();
                        }
                        @Override
                        public Map<TypeParameter,ProducedType> 
                        initTypeArguments() {
                            return rm.getTypedParameter(p)
                                    .getFullType()
                                    .getTypeArguments();
                        }
                        @Override
                        public TypeDeclaration initDeclaration() {
                            return rm.getTypedParameter(p)
                                    .getFullType()
                                    .getDeclaration();
                        }
                    });
                }
                else {
                    Tree.Parameter tp = tpl.getParameters().get(j);
                    Parameter rp = tp.getParameterModel();
                    rp.setDefaulted(p.isDefaulted());
                    rp.setDeclaration(m);
                    l.getParameters().add(rp);
                }
                j++;
            }
            m.getParameterLists().add(l);
        }
        if (!sm.getTypeParameters().isEmpty()) {
            bme.addError("method has type parameters: " +  
                    message(sm));
        }
        m.setShared(true);
        m.setActual(true);
        m.getAnnotations().add(new Annotation("shared"));
        m.getAnnotations().add(new Annotation("actual"));
        m.setRefinedDeclaration(root);
        m.setUnit(that.getUnit());
        m.setContainer(c);
        m.setShortcutRefinement(true);
        m.setDeclaredVoid(sm.isDeclaredVoid());
        setVisibleScope(m);
        c.addMember(m);
        that.setRefinement(true);
        that.setDeclaration(m);
        that.setRefined(sm);
        that.getUnit().addDeclaration(m);
        if (that.getScope() instanceof Specification){
            ((Specification) that.getScope()).setDeclaration(m);
        }
        m.setType(new LazyProducedType(that.getUnit()) {
            @Override
            public ProducedType initQualifyingType() {
                ProducedType type = rm.getType();
                return type==null ? null : type.getQualifyingType();
            }
            @Override
            public Map<TypeParameter,ProducedType> 
            initTypeArguments() {
                ProducedType type = rm.getType();
                return type==null ? null : type.getTypeArguments();
            }
            @Override
            public TypeDeclaration initDeclaration() {
                ProducedType type = rm.getType();
                return type==null ? null : type.getDeclaration();
            }
        });
        inheritDefaultedArguments(m);
    }
    
}
