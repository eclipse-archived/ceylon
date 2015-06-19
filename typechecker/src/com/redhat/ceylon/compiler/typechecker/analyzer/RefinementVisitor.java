package com.redhat.ceylon.compiler.typechecker.analyzer;


import static com.redhat.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.NO_TYPE_ARGS;
import static com.redhat.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.checkAssignable;
import static com.redhat.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.checkAssignableToOneOf;
import static com.redhat.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.checkIsExactly;
import static com.redhat.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.checkIsExactlyForInterop;
import static com.redhat.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.checkIsExactlyOneOf;
import static com.redhat.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.declaredInPackage;
import static com.redhat.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.getTypeErrorNode;
import static com.redhat.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.getTypedDeclaration;
import static com.redhat.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.isConstructor;
import static com.redhat.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.message;
import static com.redhat.ceylon.compiler.typechecker.analyzer.DeclarationVisitor.setVisibleScope;
import static com.redhat.ceylon.compiler.typechecker.analyzer.ExpressionVisitor.getRefinedMember;
import static com.redhat.ceylon.compiler.typechecker.tree.TreeUtil.name;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.getInheritedDeclarations;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.getInterveningRefinements;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.getNativeHeader;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.getRealScope;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.getSignature;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.isImplemented;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.isNamed;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.isObject;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.isOverloadedVersion;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.isResolvable;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.redhat.ceylon.compiler.typechecker.context.TypecheckerUnit;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;
import com.redhat.ceylon.model.typechecker.model.Annotation;
import com.redhat.ceylon.model.typechecker.model.Class;
import com.redhat.ceylon.model.typechecker.model.ClassOrInterface;
import com.redhat.ceylon.model.typechecker.model.Declaration;
import com.redhat.ceylon.model.typechecker.model.Function;
import com.redhat.ceylon.model.typechecker.model.Functional;
import com.redhat.ceylon.model.typechecker.model.Generic;
import com.redhat.ceylon.model.typechecker.model.LazyType;
import com.redhat.ceylon.model.typechecker.model.Package;
import com.redhat.ceylon.model.typechecker.model.Parameter;
import com.redhat.ceylon.model.typechecker.model.ParameterList;
import com.redhat.ceylon.model.typechecker.model.Reference;
import com.redhat.ceylon.model.typechecker.model.Scope;
import com.redhat.ceylon.model.typechecker.model.Setter;
import com.redhat.ceylon.model.typechecker.model.SiteVariance;
import com.redhat.ceylon.model.typechecker.model.Specification;
import com.redhat.ceylon.model.typechecker.model.Type;
import com.redhat.ceylon.model.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.model.typechecker.model.TypeParameter;
import com.redhat.ceylon.model.typechecker.model.TypedDeclaration;
import com.redhat.ceylon.model.typechecker.model.Unit;
import com.redhat.ceylon.model.typechecker.model.Value;

/**
 * Validates some simple rules relating to refinement and
 * native overloading. Also responsible for creating models
 * for methods and attributes written using "shortcut"-style
 * refinement.
 * This work happens during an intermediate phase in 
 * between the second and third phases of type analysis.
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
        inheritDefaultedArguments(
                that.getDeclarationModel());
    }

    @Override
    public void visit(Tree.AnyClass that) {
        super.visit(that);
        inheritDefaultedArguments(
                that.getDeclarationModel());
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
                for (int i=0; 
                        i<tdps.size() && i<rdps.size(); 
                        i++) {
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
                        message(dec), 
                        1200);
            }
            
            boolean mayBeRefined =
                    dec instanceof Value || 
                    dec instanceof Function ||
                    dec instanceof Class;
            if (!mayBeRefined) {
                checkNonrefinableDeclaration(that, dec);
            }
            
            boolean member = 
                    dec.isClassOrInterfaceMember() &&
                    dec.isShared() &&
                    !isConstructor(dec) &&
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
            
            if ((dec.isNative() && !dec.isNativeHeader())
                    || (dec.isMember()
                            && ((Declaration)dec.getContainer()).isNative()
                            && !((Declaration)dec.getContainer()).isNativeHeader())) {
                checkNative(that, dec);
            }
        }
        
    }

    private void checkNative(Tree.Declaration that, Declaration dec) {
        if (dec instanceof Setter) {
            // We ignore setter assuming the check done for their
            // getters will have been enough
            return;
        }
        // Find the header
        Declaration header =
                getNativeHeader(dec.getContainer(), dec.getName());
        if (header == null) {
            // We don't have a header but we still need to maintain
            // the same interface as the rest of the implementations,
            // so we choose another declaration to be the "header"
            List<Declaration> decls = getNativeMembers(dec.getContainer(), dec.getName());
            if (decls.size() > 1) {
                if (!dec.isShared()) {
                    that.addError("native implementation must have a header: " +
                            message(dec));
                }
                if (decls.get(0) != dec) {
                    header = decls.get(0);
                } else {
                    header = decls.get(1);
                }
            }
        }
        if (dec!=header) {
            checkNativeDeclaration(that, dec, header);
        }
    }
    
    // This methods retrieves a list of all the native members
    // of a given name in the given scope. And if the scope itself
    // is a native implementation we'll go look for all the
    // members in the other implementations as well. But only if
    // there wasn't a native header for the scope, because that
    // case is already handled elsewhere
    private List<Declaration> getNativeMembers(
            Scope container, String name) {
        ArrayList<Declaration> lst = new ArrayList<Declaration>();
        ArrayList<Scope> containers = new ArrayList<Scope>();
        if (container instanceof Declaration) {
            Declaration cd = (Declaration)container;
            if (cd.isNative() && !cd.isNativeHeader()) {
                // The container is a native implementation so
                // we first need to find _its_ implementations
                // but only if there's no header
                Declaration header =
                        getNativeHeader(cd.getContainer(), cd.getName());
                if (header == null) {
                    List<Declaration> cs = getNativeMembers(cd.getContainer(), cd.getName());
                    for (Declaration c : cs) {
                        // Is this the Value part of an object?
                        if (c instanceof Value && isObject((Value)c)) {
                            // Then use the Class part as the container
                            c = ((Value)c).getType().getDeclaration();
                        }
                        containers.add((Scope)c);
                    }
                }
            } else {
                containers.add(container);
            }
        } else {
            containers.add(container);
        }
        for (Scope s : containers) {
            for (Declaration dec: s.getMembers()) {
                if (isResolvable(dec)
                        && isNamed(name, dec)
                        && dec.isNative()) {
                    lst.add(dec);
                }
            }
        }
        return lst;
    }
    
    private void checkNativeDeclaration(Tree.Declaration that, 
            Declaration dec, Declaration header) {
        if (header == null && dec.isMember()) {
            if (dec.isNative() && dec.isShared() && !dec.isFormal() && !dec.isActual() && !dec.isDefault()) {
                that.addError("native member does not implement any header member: " +
                        message(dec));
            }
            if (!dec.isNative() && dec.isShared()) {
                that.addError("non-native shared members not allowed in native implementations: " +
                        message(dec));
            }
            return;
        }
        if (dec instanceof Function && 
                (header == null ||
                header instanceof Function)) {
            checkNativeMethod(that, 
                    (Function) dec, 
                    (Function) header);
        }
        else if (dec instanceof Value &&
                (header == null ||
                header instanceof Value)) {
            checkNativeValue(that, 
                    (Value) dec, 
                    (Value) header);
        }
        else if (dec instanceof Class &&
                (header == null ||
                header instanceof Class)) {
            checkNativeClass(that, 
                    (Class) dec, 
                    (Class) header);
        }
        else if (header != null) {
            that.addError("native declarations not of same type: " + 
                    message(dec));
        }
    }
    
    private void checkNativeClass(Tree.Declaration that, 
            Class dec, Class header) {
        if (header == null) {
            return;
        }
        if (dec.isShared() && !header.isShared()) {
            that.addError("native header is not shared: " +
                    message(dec));
        }
        if (!dec.isShared() && header.isShared()) {
            that.addError("native header is shared: " +
                    message(dec));
        }
        if (dec.isAbstract() && !header.isAbstract()) {
            that.addError("native header is not abstract: " +
                    message(dec));
        }
        if (!dec.isAbstract() && header.isAbstract()) {
            that.addError("native header is abstract: " +
                    message(dec));
        }
        if (dec.isFinal() && !header.isFinal()) {
            that.addError("native header is not final: " +
                    message(dec));
        }
        if (!dec.isFinal() && header.isFinal()) {
            that.addError("native header is final: " +
                    message(dec));
        }
        if (dec.isSealed() && !header.isSealed()) {
            that.addError("native header is not sealed: " +
                    message(dec));
        }
        if (!dec.isSealed() && header.isSealed()) {
            that.addError("native header is sealed: " +
                    message(dec));
        }
        if (dec.isAnnotation() && !header.isAnnotation()) {
            that.addError("native header is not an annotation type: " +
                    message(dec));
        }
        if (!dec.isAnnotation() && header.isAnnotation()) {
            that.addError("native header is an annotation type: " +
                    message(dec));
        }
        Type dext = dec.getExtendedType();
        Type aext = header.getExtendedType();
        if ((dext != null && aext == null)
                || (dext == null && aext != null)
                || !dext.isExactly(aext)) {
            that.addError("native classes do not extend the same type: " +
                    message(dec));
        }
        List<Type> dst = 
                dec.getSatisfiedTypes();
        List<Type> ast = 
                header.getSatisfiedTypes();
        if (dst.size() != ast.size() || 
                !dst.containsAll(ast)) {
            that.addError("native classes do not satisfy the same interfaces: " +
                    message(dec));
        }
        // FIXME probably not the right tests
        checkClassParameters(that,
                dec, header,
                dec.getReference(),
                header.getReference(),
                true);
        checkRefiningMemberTypeParameters(that,
                dec, header,
                dec.getTypeParameters(),
                header.getTypeParameters(),
                true);
        
        checkMissingMemberImpl(that, dec, header);
    }

    private void checkMissingMemberImpl(Tree.Declaration that, Class dec, Class header) {
        List<Declaration> hdrMembers = getNativeClassMembers(header);
        List<Declaration> implMembers = getNativeClassMembers(dec);
        Iterator<Declaration> hdrIter = hdrMembers.iterator();
        Iterator<Declaration> implIter = implMembers.iterator();
        boolean hdrNext = true;
        boolean implNext = true;
        Declaration hdr = null;
        Declaration impl = null;
        while (hdrIter.hasNext() && implIter.hasNext()) {
            if (hdrNext) {
                hdr = hdrIter.next();
            }
            if (implNext) {
                impl = implIter.next();
            }
            int cmp = declarationCmp.compare(hdr, impl);
            if (cmp < 0) {
                if (!isImplemented(hdr)) {
                    that.addError("native header '" + hdr.getName() +
                            "' of '" + containerName(hdr) +
                            "' has no native implementation");
                    return;
                }
                hdrNext = true;
                implNext = false;
            } else if (cmp > 0) {
                hdrNext = false;
                implNext = true;
            } else {
                hdrNext = true;
                implNext = true;
            }
        }
        if (hdrIter.hasNext()) {
            hdr = hdrIter.next();
            if (!isImplemented(hdr)) {
                that.addError("native header '" + hdr.getName() +
                        "' of '" + containerName(hdr) +
                        "' has no native implementation");
            }
        }
    }
    
    private static final Comparator<Declaration> declarationCmp =
            new Comparator<Declaration>() {
        @Override
        public int compare(Declaration o1, Declaration o2) {
            return o1.getName().compareTo(o2.getName());
        }
    };
    
    private List<Declaration> getNativeClassMembers(Class dec) {
        List<Declaration> members = dec.getMembers();
        ArrayList<Declaration> nats = new ArrayList<Declaration>(members.size());
        for (Declaration m : members) {
            if (m.isNative() && !m.isFormal() && !m.isActual() && !m.isDefault()) {
                nats.add(m);
            }
        }
        Collections.sort(nats, declarationCmp);
        return nats;
    }

    private void checkNativeMethod(Tree.Declaration that, 
            Function dec, Function header) {
        if (header == null) {
            return;
        }
        Type at = header.getType();
        if (!dec.getType().isExactly(at)) {
            that.addError("native implementation must have the same return type as native header: " +
                    message(dec) + " must have the type '" +
                    at.asString(that.getUnit()) + "'");
        }
        if (dec.isShared() && !header.isShared()) {
            that.addError("native header is not shared: " +
                    message(dec));
        }
        if (!dec.isShared() && header.isShared()) {
            that.addError("native header is shared: " +
                    message(dec));
        }
        if (dec.isAnnotation() && !header.isAnnotation()) {
            that.addError("native header is not an annotation constructor: " +
                    message(dec));
        }
        if (!dec.isAnnotation() && header.isAnnotation()) {
            that.addError("native header is an annotation constructor: " +
                    message(dec));
        }
        // FIXME probably not the right tests
        checkRefiningMemberParameters(that,
                dec, header,
                dec.getReference(),
                header.getReference(),
                true);
        checkRefiningMemberTypeParameters(that,
                dec, header,
                dec.getTypeParameters(),
                header.getTypeParameters(),
                true);
    }
    
    private void checkNativeValue(Tree.Declaration that, 
            Value dec, Value header) {
        if (header == null) {
            return;
        }
        Type at = header.getType();
        if (!dec.getType().isExactly(at)
                && !sameObjects(dec, header)) {
            that.addError("native implementation must have the same type as native header: " +
                    message(dec) + " must have the type '" +
                    at.asString(that.getUnit()) + "'");
        }
        if (dec.isShared() && !header.isShared()) {
            that.addError("native header is not shared: " +
                    message(dec));
        }
        if (!dec.isShared() && header.isShared()) {
            that.addError("native header is shared: " +
                    message(dec));
        }
        if (dec.isVariable() && !header.isVariable()) {
            that.addError("native header is not variable: " +
                    message(dec));
        }
        if (!dec.isVariable() && header.isVariable()) {
            that.addError("native header is variable: " +
                    message(dec));
        }
    }
    
    private boolean sameObjects(Value dec, Value header) {
        return isObject(dec) && isObject(header) && dec.getQualifiedNameString().equals(header.getQualifiedNameString());
    }

    private void checkClassParameters(Tree.Declaration that,
            Declaration dec, Declaration refined,
            Reference refinedMember, 
            Reference refiningMember,
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

    private void checkMember(Tree.Declaration that, 
            Declaration member) {
        String name = member.getName();
        if (name==null) {
            return;
        }
        if (member instanceof Setter) {
            Setter setter = (Setter) member;
            Value getter = setter.getGetter();
            Declaration rd = getter.getRefinedDeclaration();
            member.setRefinedDeclaration(rd);
            return;
        }
        ClassOrInterface type = 
                (ClassOrInterface) 
                    member.getContainer();
        if (member.isFormal() && 
                type instanceof Class) {
            Class c = (Class) type;
            if (!c.isAbstract() && !c.isFormal()) {
                that.addError("formal member belongs to non-abstract, non-formal class: " + 
                        message(member), 1100);
            }
        }
        if (type.isDynamic()) {
            if (member instanceof Class) {
                that.addError("member class belongs to dynamic interface");
            }
            else if (!member.isFormal()) {
                that.addError("non-formal member belongs to dynamic interface");
            }
        }
        List<Type> signature = getSignature(member);
        Declaration root = 
                type.getRefinedMember(name, 
                        signature, false);
        boolean legallyOverloaded = 
                !isOverloadedVersion(member) ||
                member.isNative();
        if (root == null || root.equals(member) || (root.isNative() && member.isNative())) {
            member.setRefinedDeclaration(member);
            if (member.isActual()) {
                that.addError("actual member does not refine any inherited member: " + 
                        message(member), 1300);
            }
            else if (!legallyOverloaded) {
                if (member.isActual()) {
                    that.addError("overloaded member does not refine an inherited overloaded member: " + 
                            message(member));
                }
                else {
                    that.addError("duplicate or overloaded member name: " + 
                            message(member));
                }
            }
            else {
                if (!getInheritedDeclarations(name, type).isEmpty()) {
                    that.addError("duplicate or overloaded member name in type hierarchy: " + 
                            message(member));
                }
            }
        }
        else {
            member.setRefinedDeclaration(root);
            if (root.isPackageVisibility() && 
                    !declaredInPackage(root, that.getUnit())) {
                that.addError("refined declaration is not visible: " + 
                        message(member) + " refines " + message(root));
            }
            boolean found = false;
            TypeDeclaration rootType = 
                    (TypeDeclaration) 
                        root.getContainer();
            List<Declaration> interveningRefinements = 
                    getInterveningRefinements(name, 
                            signature, root, type, rootType);
            for (Declaration refined: interveningRefinements) {
                if (isOverloadedVersion(refined)) {
                    //if this member is overloaded, the
                    //inherited member it refines must
                    //also be overloaded
                    legallyOverloaded = true;
                }
                found = true;
                if (member instanceof Function) {
                    if (!(refined instanceof Function)) {
                        that.addError("refined declaration is not a method: " + 
                                message(member) + " refines " + message(refined));
                    }
                }
                else if (member instanceof Class) {
                    if (!(refined instanceof Class)) {
                        that.addError("refined declaration is not a class: " + 
                                message(member) + " refines " + message(refined));
                    }
                }
                else if (member instanceof TypedDeclaration) {
                    if (refined instanceof Class || 
                        refined instanceof Function) {
                        that.addError("refined declaration is not an attribute: " + 
                                message(member) + " refines " + message(refined));
                    }
                    else if (refined instanceof TypedDeclaration) {
                        if (((TypedDeclaration) refined).isVariable() && 
                                !((TypedDeclaration) member).isVariable()) {
                            if (member instanceof Value) {
                                that.addError("non-variable attribute refines a variable attribute: " + 
                                        message(member) + " refines " + message(refined), 
                                        804);
                            }
                            else {
                                //TODO: this message seems like it's not quite right
                                that.addError("non-variable attribute refines a variable attribute: " + 
                                        message(member) + " refines " + message(refined));
                            }
                        }
                    }
                }
                if (!member.isActual()) {
                    that.addError("non-actual member refines an inherited member: " + 
                            message(member) + " refines " + message(refined), 
                            600);
                }
                if (!refined.isDefault() && !refined.isFormal()) {
                    that.addError("member refines a non-default, non-formal member: " + 
                            message(member) + " refines " + message(refined), 
                            500);
                }
                if (!type.isInconsistentType()) {
                    checkRefinedTypeAndParameterTypes(that, 
                            member, type, refined);
                }
            }
            if (!found) {
                if (member instanceof Function && 
                        root instanceof Function) { //see the condition in DeclarationVisitor.checkForDuplicateDeclaration()
                    that.addError("overloaded member does not refine any inherited member: " + 
                            message(member));
                }
            }
            else if (!legallyOverloaded) {
                that.addError("overloaded member does not exactly refine an inherited overloaded member: " +
                        message(member));
            }
        }
    }

    /*private boolean refinesOverloaded(Declaration dec, 
    		Declaration refined, Type st) {
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
            	Type p2st = p2.getType()
            			.substitute(st.getTypeArguments());
				if (!matches(p1.getType(), p2st, dec.getUnit())) {
                    return false;
            	}
            }
        }
        return true;
    }*/
    
    private void checkRefinedTypeAndParameterTypes(
            Tree.Declaration that, Declaration refining, 
            ClassOrInterface ci, Declaration refined) {
        
    	List<Type> typeArgs;
        if (refined instanceof Generic && 
            refining instanceof Generic) {
            List<TypeParameter> refinedTypeParams = 
                    ((Generic) refined).getTypeParameters();
            List<TypeParameter> refiningTypeParams = 
                    ((Generic) refining).getTypeParameters();
            checkRefiningMemberTypeParameters(that, 
                    refining, refined, refinedTypeParams, 
                    refiningTypeParams, false);
            typeArgs = checkRefiningMemberUpperBounds(that, 
                    ci, refined, 
                    refinedTypeParams, 
                    refiningTypeParams);
        }
        else {
        	typeArgs = NO_TYPE_ARGS;
        }
        
        Type cit = ci.getType();
        Reference refinedMember = 
                cit.getTypedReference(refined, 
                        typeArgs);
        Reference refiningMember = 
                cit.getTypedReference(refining, 
                        typeArgs);
        Declaration refinedMemberDec = 
                refinedMember.getDeclaration();
		Declaration refiningMemberDec = 
		        refiningMember.getDeclaration();
		Node typeNode = getTypeErrorNode(that);
		if (refinedMemberIsDynamicallyTyped(
		        refinedMemberDec, refiningMemberDec)) {
        	checkRefiningMemberDynamicallyTyped(refined, 
        	        refiningMemberDec, typeNode);
        }
		else if (refiningMemberIsDynamicallyTyped(
		        refinedMemberDec, refiningMemberDec)) {
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
        if (refining instanceof Functional && 
                refined instanceof Functional) {
           checkRefiningMemberParameters(that, refining, refined, 
                   refinedMember, refiningMember, false);
        }
    }

	private void checkRefiningMemberParameters(
	        Tree.Declaration that,
            Declaration refining, Declaration refined,
            Reference refinedMember, 
            Reference refiningMember,
            boolean forNative) {
		List<ParameterList> refiningParamLists = 
		        ((Functional) refining).getParameterLists();
		List<ParameterList> refinedParamLists = 
		        ((Functional) refined).getParameterLists();
		if (refinedParamLists.size()!=refiningParamLists.size()) {
		    String subject = 
		            forNative ? 
    		            "native header" :
    		            "refined member";
		    String current = 
		            forNative ? 
    		            "native implementation" : 
    		            "refining member";
		    StringBuilder message = new StringBuilder();
			message.append(current)
			        .append(" must have the same number of parameter lists as ")
			        .append(subject)
			        .append(": ")
			        .append(message(refining));
			if (!forNative) {
			    message.append(" refines ")
			            .append(message(refined));
			}
            that.addError(message.toString());
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

	private boolean refinedMemberIsVariable(
	        Declaration refinedMemberDec) {
	    return refinedMemberDec instanceof TypedDeclaration &&
                ((TypedDeclaration) refinedMemberDec).isVariable();
    }

	private void checkRefinedMemberDynamicallyTyped(
	        Declaration refined,
            Declaration refinedMemberDec, 
            Node typeNode) {
	    TypedDeclaration td = 
	            (TypedDeclaration) 
	                refinedMemberDec;
        if (!td.isDynamicallyTyped()) {
	    	typeNode.addError(
	    	        "member which refines statically typed refined member must also be statically typed: " + 
	    			message(refined));
	    }
    }

	private void checkRefiningMemberDynamicallyTyped(
	        Declaration refined,
            Declaration refiningMemberDec, 
            Node typeNode) {
	    TypedDeclaration td = 
	            (TypedDeclaration) 
	                refiningMemberDec;
        if (!td.isDynamicallyTyped()) {
	    	typeNode.addError(
	    	        "member which refines dynamically typed refined member must also be dynamically typed: " + 
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
	        String subject = 
	                forNative ? 
                        "native header" : 
                        "refined member";
	        String current = 
	                forNative ? 
	                    "native implementation" : 
	                    "refining member";
	        StringBuilder message = new StringBuilder();
	        message.append(current) 
	                .append(" does not have the same number of type parameters as ") 
	                .append(subject)
	                .append(": ") 
	                .append(message(dec));
            if (!forNative) {
                message.append(" refines ")
                        .append(message(refined));
            }
            that.addError(message.toString());
	    }
    }

	private List<Type> checkRefiningMemberUpperBounds(
	        Tree.Declaration that,
            ClassOrInterface ci, Declaration refined,
            List<TypeParameter> refinedTypeParams, 
            List<TypeParameter> refiningTypeParams) {
        int refiningSize = refiningTypeParams.size();
        int refinedSize = refinedTypeParams.size();
	    int max = refiningSize <= refinedSize ? 
	            refiningSize : refinedSize;
	    if (max==0) {
	    	return NO_TYPE_ARGS;
	    }
	    //we substitute the type parameters of the refined
	    //declaration into the bounds of the refining 
	    //declaration
        Map<TypeParameter, Type> substitution =
                new HashMap<TypeParameter, Type>();
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
        Type supertype = ci.getType().getSupertype(rc);
        Map<TypeParameter, Type> args = 
                supertype.getTypeArguments();
        Map<TypeParameter, SiteVariance> variances = 
                supertype.getVarianceOverrides();
		List<Type> typeArgs = 
		        new ArrayList<Type>(max); 
		for (int i=0; i<max; i++) {
	        TypeParameter refinedTypeParam = 
	                refinedTypeParams.get(i);
	        TypeParameter refiningTypeParam = 
	                refiningTypeParams.get(i);
	        Type refinedProducedType = 
	                refinedTypeParam.getType();
	        List<Type> refinedBounds = 
	                refinedTypeParam.getSatisfiedTypes();
            List<Type> refiningBounds = 
                    refiningTypeParam.getSatisfiedTypes();
            Unit unit = that.getUnit();
            for (Type bound: refiningBounds) {
                Type refiningBound = 
                        bound.substitute(substitution, 
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
	            for (Type refinedBound: refinedBounds) {
	                refinedBound = 
	                        refinedBound.substitute(
	                                args, variances);
	                if (refinedBound.isSubtypeOf(refiningBound)) {
	                    ok = true;
	                }
	            }
	            if (!ok) {
	                that.addError(
	                        "refining member type parameter '" + 
	                        refiningTypeParam.getName() +
	                        "' has upper bound which refined member type parameter '" + 
	                        refinedTypeParam.getName() + 
	                        "' of " + message(refined) + 
	                        " does not satisfy: '" + 
	                        bound.asString(unit) + 
	                        "'");
	            }
	        }
            for (Type bound: refinedBounds) {
                Type refinedBound =
                        bound.substitute(args, variances);
                boolean ok = false;
                for (Type refiningBound: refiningBounds) {
                    refiningBound = 
                            refiningBound.substitute(
                                    substitution, 
                                    noVariances);
                    if (refinedBound.isSubtypeOf(refiningBound)) {
                        ok = true;
                    }
                }
                if (!ok) {
                    that.addUnsupportedError(
                            "refined member type parameter '" + 
                            refinedTypeParam.getName() + 
                            "' of " + message(refined) +
                            " with upper bound which refining member type parameter '" + 
                            refiningTypeParam.getName() + 
                            "' does not satisfy not yet supported: '" + 
                            bound.asString(unit) + 
                            "' ('" +
                            refiningTypeParam.getName() +
                            "' should be upper bounded by '" +
                            refinedBound.asString(unit) + 
                            "')");
                }
            }
	        typeArgs.add(refinedProducedType);
	    }
	    return typeArgs;
    }

    private void checkRefinedMemberTypeAssignable(
            Reference refiningMember, 
    		Reference refinedMember,
    		Node that, Declaration refined) {
        if (hasUncheckedNullType(refinedMember)) {
            Unit unit = 
                    refiningMember.getDeclaration()
                        .getUnit();
            Type optionalRefinedType = 
                    unit.getOptionalType(
                            refinedMember.getType());
            checkAssignableToOneOf(refiningMember.getType(), 
                    refinedMember.getType(), 
                    optionalRefinedType, that, 
            		"type of member must be assignable to type of refined member: " + 
    				message(refined), 
    				9000);
        }
        else {
            checkAssignable(refiningMember.getType(), 
                    refinedMember.getType(), that,
            		"type of member must be assignable to type of refined member: " + 
    		        message(refined), 
    		        9000);
        }
    }

    private void checkRefinedMemberTypeExactly(
            Reference refiningMember, 
    		Reference refinedMember, 
    		Node that, Declaration refined) {
        if (hasUncheckedNullType(refinedMember)) {
            Unit unit = 
                    refiningMember.getDeclaration()
                        .getUnit();
            Type optionalRefinedType = 
                    unit.getOptionalType(
                            refinedMember.getType());
            checkIsExactlyOneOf(refiningMember.getType(), 
                    refinedMember.getType(), 
            		optionalRefinedType, that, 
            		"type of member must be exactly the same as type of variable refined member: " + 
            	            message(refined));
        }
        else {
            checkIsExactly(refiningMember.getType(), 
                    refinedMember.getType(), that,
            		"type of member must be exactly the same as type of variable refined member: " + 
            	            message(refined), 9000);
        }
    }

    private boolean hasUncheckedNullType(
            Reference member) {
        Declaration dec = member.getDeclaration();
        return dec instanceof TypedDeclaration && 
                ((TypedDeclaration) dec)
                    .hasUncheckedNullType();
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
            that.addError("actual declaration is not a method, getter, reference attribute, or class", 
                    1301);
        }
        if (dec.isFormal()) {
            that.addError("formal declaration is not a method, getter, reference attribute, or class", 
                    1302);
        }
        if (dec.isDefault()) {
            that.addError("default declaration is not a method, getter, reference attribute, or class", 
                    1303);
        }
    }

    private void checkNonMember(Tree.Declaration that, Declaration dec) {
        boolean mayBeShared = !(dec instanceof TypeParameter);
        if (!dec.isClassOrInterfaceMember() && mayBeShared) {
            if (dec.isActual()) {
                that.addError("actual declaration is not a member of a class or interface: '" + 
                        dec.getName() + "'", 
                        1301);
            }
            if (dec.isFormal()) {
                that.addError("formal declaration is not a member of a class or interface: '" + 
                        dec.getName() + "'", 
                        1302);
            }
            if (dec.isDefault()) {
                that.addError("default declaration is not a member of a class or interface: '" + 
                        dec.getName() + "'", 
                        1303);
            }
        }
        else if (!dec.isShared() && mayBeShared) {
            if (dec.isActual()) {
                that.addError("actual declaration must be shared: '" + 
                        dec.getName() + "'", 
                        701);
            }
            if (dec.isFormal()) {
                that.addError("formal declaration must be shared: '" + 
                        dec.getName() + "'", 
                        702);
            }
            if (dec.isDefault()) {
                that.addError("default declaration must be shared: '" + 
                        dec.getName() + "'", 
                        703);
            }
        }
        else {
            if (dec.isActual()) {
                that.addError("declaration may not be actual: '" + 
                        dec.getName() + "'", 
                        1301);
            }
            if (dec.isFormal()) {
                that.addError("declaration may not be formal: '" + 
                        dec.getName() + "'", 
                        1302);
            }
            if (dec.isDefault()) {
                that.addError("declaration may not be default: '" + 
                        dec.getName() + "'", 
                        1303);
            }
        }
    }
    
    private static String containerName(
            Reference member) {
        return containerName(member.getDeclaration());
    }

    private static String containerName(
            Declaration member) {
        Scope container = 
                member.getContainer();
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

    private void checkParameterTypes(
            Tree.Declaration that, 
            Tree.ParameterList pl,
            Reference member, 
            Reference refinedMember,
            ParameterList params, 
            ParameterList refinedParams, 
            boolean forNative) {
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
                Type refinedParameterType = 
                		refinedMember.getTypedParameter(rparam)
                		        .getFullType();
                Type parameterType = 
                		member.getTypedParameter(param)
                		        .getFullType();
                Tree.Parameter parameter = 
                        pl.getParameters().get(i);
                Node typeNode = parameter;
                if (parameter instanceof Tree.ParameterDeclaration) {
                	Tree.ParameterDeclaration pd = 
                	        (Tree.ParameterDeclaration) 
                	            parameter;
                    Tree.Type type = 
                            pd.getTypedDeclaration()
                                .getType();
                	if (type!=null) {
                		typeNode = type;
                	}
                }
                if (parameter!=null) {
            		if (rparam.getModel().isDynamicallyTyped()) {
                    	checkRefiningParameterDynamicallyTyped(
                    	        member, refinedMember, 
                    	        param, typeNode);
                    }
            		else if (param.getModel() != null && 
            		         param.getModel().isDynamicallyTyped()) {
                    	checkRefinedParameterDynamicallyTyped(
                    	        member, refinedMember, 
                    	        rparam, param, typeNode);
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

	private void handleWrongParameterListLength(
	        Tree.Declaration that,
            Reference member, 
            Reference refinedMember,
            boolean forNative) {
        StringBuilder message = new StringBuilder();
	    String subject = 
	            forNative ? 
    	            "native header" : 
    	            "refined member";
	    message.append("member does not have the same number of parameters as ") 
	            .append(subject)
	            .append(": '") 
	            .append(member.getDeclaration().getName())
	            .append("'");
	    if (!forNative) {
	        message.append(" declared by '") 
	                .append(containerName(member)) 
	                .append("' refining '") 
	                .append(refinedMember.getDeclaration().getName())
	                .append("' declared by '") 
	                .append(containerName(refinedMember))
	                .append("'");
	    }
	    that.addError(message.toString(), 9100);
    }

	private static void checkRefiningParameterType(
	        Reference member,
            Reference refinedMember, 
            ParameterList refinedParams,
            Parameter rparam, 
            Type refinedParameterType,
            Parameter param, 
            Type parameterType,
            Node typeNode, 
            boolean forNative) {
	    //TODO: consider type parameter substitution!!!
	    StringBuilder message = new StringBuilder();
	    String subject = 
	            forNative ? 
    	            "native header" : 
    	            "refined member";
	    message.append("type of parameter '")
	            .append(param.getName())
	            .append("' of '")
	            .append(member.getDeclaration().getName())
	            .append("'");
	    if (!forNative) {
	        message.append(" declared by '")
	                .append(containerName(member)) 
	                .append("'");
	    }
        message.append(" is different to type of corresponding parameter '")
                .append(rparam.getName())
                .append("' of ") 
                .append(subject)
                .append(" '")
                .append(refinedMember.getDeclaration().getName())
                .append("'");
        if (!forNative) {
            message.append(" of '")
                    .append(containerName(refinedMember)) 
                    .append("'");
        }
        checkIsExactlyForInterop(typeNode.getUnit(), 
	            refinedParams.isNamedParametersSupported(), 
	            parameterType, refinedParameterType, 
	            typeNode, message.toString());
    }

	private void handleUnknownParameterType(
	        Reference member,
            Reference refinedMember, 
            Parameter param, 
            Node typeNode, 
            boolean forNative) {
	    StringBuilder message = new StringBuilder();
	    String subject = 
	            forNative ? 
    	            "native header" : 
    	            "refined member";
	    message.append("could not determine if parameter type is the same as the corresponding parameter of ") 
	            .append(subject).append(": '")
	            .append(param.getName())
	            .append("' of '") 
	            .append(member.getDeclaration().getName());
	    if (!forNative) {
	            message.append("' declared by '") 
	                    .append(containerName(member))
	                    .append("' refining '") 
	                    .append(refinedMember.getDeclaration().getName())
	                    .append("' declared by '") 
	                    .append(containerName(refinedMember))
	                    .append("'");
	    }
        typeNode.addError(message.toString());
    }

	private void checkRefinedParameterDynamicallyTyped(
            Reference member, 
            Reference refinedMember,
            Parameter rparam, Parameter param, 
            Node typeNode) {
	    if (!rparam.getModel().isDynamicallyTyped()) {
	    	typeNode.addError(
	    	        "parameter which refines statically typed parameter must also be statically typed: '" + 
	    			param.getName() + "' of '" + 
	    	        member.getDeclaration().getName() + 
	                "' declared by '" + 
	    	        containerName(member) +
	                "' refining '" + 
	    	        refinedMember.getDeclaration().getName() +
	                "' declared by '" + 
	    	        containerName(refinedMember) + 
	    	        "'");
	    }
    }

	private void checkRefiningParameterDynamicallyTyped(
            Reference member, Reference refinedMember,
            Parameter param, Node typeNode) {
	    if (!param.getModel().isDynamicallyTyped()) {
	    	typeNode.addError(
	    	        "parameter which refines dynamically typed parameter must also be dynamically typed: '" + 
	    			param.getName() + "' of '" + 
	    	        member.getDeclaration().getName() + 
	                "' declared by '" + 
	    	        containerName(member) +
	                "' refining '" + 
	    	        refinedMember.getDeclaration().getName() +
	                "' declared by '" + 
	    	        containerName(refinedMember) + 
	    	        "'");
	    }
    }

    private static Tree.ParameterList getParameterList(
            Tree.Declaration that, int i) {
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
    
    @Override 
    public void visit(Tree.SpecifierStatement that) {
        super.visit(that);
        
        List<Type> sig = new ArrayList<Type>();
        Tree.Term term = that.getBaseMemberExpression();
        while (term instanceof Tree.ParameterizedExpression) {
            sig.clear();
            Tree.ParameterizedExpression pe = 
                    (Tree.ParameterizedExpression) 
                        term;
            Tree.TypeParameterList typeParameterList = 
                    pe.getTypeParameterList();
            if (typeParameterList!=null) {
                //TODO: remove this for #1329
                typeParameterList.addError("specification statements may not have type parameters");
            }
            Tree.ParameterList pl = 
                    pe.getParameterLists()
                        .get(0);
            for (Tree.Parameter p: pl.getParameters()) {
                if (p == null) {
                    sig.add(null);
                }
                else {
                    Parameter model = p.getParameterModel();
                    if (model!=null) {
                        sig.add(model.getType());
                    }
                    else {
                        sig.add(null);
                    }
                }
            }
            term = pe.getPrimary();
        }
        if (term instanceof Tree.BaseMemberExpression) {
            Tree.BaseMemberExpression bme = 
                    (Tree.BaseMemberExpression) 
                        term;
            Unit unit = that.getUnit();
            TypedDeclaration td = 
                    getTypedDeclaration(bme.getScope(), 
                            name(bme.getIdentifier()), 
                            sig, false, unit);
            if (td!=null) {
                that.setDeclaration(td);
                Scope scope = that.getScope();
                Scope container = scope.getContainer();
                Scope realScope = getRealScope(container);
                if (realScope instanceof ClassOrInterface) {
                    ClassOrInterface ci = 
                            (ClassOrInterface) 
                                realScope;
                    Scope tdcontainer = td.getContainer();
                    if (td.isClassOrInterfaceMember()) {
                        ClassOrInterface tdci = 
                                (ClassOrInterface) 
                                    tdcontainer;
                        if (!tdcontainer.equals(realScope) && 
                                ci.inherits(tdci)) {
                            // interpret this specification as a 
                            // refinement of an inherited member
                            if (tdcontainer==scope) {
                                that.addError("parameter declaration hides refining member: '" +
                                        td.getName(unit) + 
                                        "' (rename parameter)");
                            }
                            else if (td instanceof Value) {
                                refineAttribute((Value) td, 
                                        bme, that, ci);
                            }
                            else if (td instanceof Function) {
                                refineMethod((Function) td, 
                                        bme, that, ci);
                            }
                            else {
                                //TODO!
                                bme.addError("not a reference to a formal attribute: '" + 
                                        td.getName(unit) + "'");
                            }
                        }
                    }
                }
            }
        }
    }

    private void refineAttribute(final Value sv, 
            Tree.BaseMemberExpression bme,
            Tree.SpecifierStatement that, 
            ClassOrInterface c) {
        final Reference rv = getRefinedMember(sv, c);
        if (!sv.isFormal() && !sv.isDefault()
                && !sv.isShortcutRefinement()) { //this condition is here to squash a dupe message
            that.addError("inherited attribute may not be assigned in initializer and is neither formal nor default so may not be refined: " + 
                    message(sv), 510);
        }
        else if (sv.isVariable()) {
            that.addError("inherited attribute may not be assigned in initializer and is variable so may not be refined by non-variable: " + 
                    message(sv));
        }
        boolean lazy = 
                that.getSpecifierExpression() 
                    instanceof Tree.LazySpecifierExpression;
        Value v = new Value();
        v.setName(sv.getName());
        v.setShared(true);
        v.setActual(true);
        v.getAnnotations().add(new Annotation("shared"));
        v.getAnnotations().add(new Annotation("actual"));
        v.setRefinedDeclaration(sv.getRefinedDeclaration());
        Unit unit = that.getUnit();
        v.setUnit(unit);
        v.setContainer(c);
        v.setScope(c);
        v.setShortcutRefinement(true);
        v.setTransient(lazy);
        setVisibleScope(v);
        c.addMember(v);
        that.setRefinement(true);
        that.setDeclaration(v);
        that.setRefined(sv);
        unit.addDeclaration(v);
        v.setType(new LazyType(unit) {
            @Override
            public Type initQualifyingType() {
                return rv.getType().getQualifyingType();
            }
            @Override
            public Map<TypeParameter, Type> 
            initTypeArguments() {
                return rv.getType().getTypeArguments();
            }
            @Override
            public TypeDeclaration initDeclaration() {
                return rv.getType().getDeclaration();
            }
        });
    }

    private void refineMethod(final Function sm, 
            Tree.BaseMemberExpression bme,
            Tree.SpecifierStatement that, 
            ClassOrInterface c) {
        ClassOrInterface ci = 
                (ClassOrInterface) 
                    sm.getContainer();
        Declaration refined = 
                ci.getRefinedMember(sm.getName(), 
                        getSignature(sm), false);
        Function root = 
                refined instanceof Function ? 
                        (Function) refined : sm;
        if (!sm.isFormal() && !sm.isDefault()
                && !sm.isShortcutRefinement()) { //this condition is here to squash a dupe message
            that.addError("inherited method is neither formal nor default so may not be refined: " + 
                    message(sm));
        }
        final Reference rm = getRefinedMember(sm,c);
        Function m = new Function();
        m.setName(sm.getName());
        List<Tree.ParameterList> paramLists;
        List<TypeParameter> typeParams;
        Tree.Term me = that.getBaseMemberExpression();
        if (me instanceof Tree.ParameterizedExpression) {
            Tree.ParameterizedExpression pe = 
                    (Tree.ParameterizedExpression) me;
            paramLists = pe.getParameterLists();
            Tree.TypeParameterList typeParameterList = 
                    pe.getTypeParameterList();
            if (typeParameterList!=null) {
                typeParams = new ArrayList<TypeParameter>();
                for (Tree.TypeParameterDeclaration tpd: 
                    typeParameterList.getTypeParameterDeclarations()) {
                    typeParams.add(tpd.getDeclarationModel());
                }
            }
            else {
                typeParams = null;
            }
        }
        else {
            paramLists = emptyList();
            typeParams = null;
        }
        int i=0;
        TypecheckerUnit unit = that.getUnit();
        for (ParameterList pl: sm.getParameterLists()) {
            ParameterList l = new ParameterList();
            Tree.ParameterList tpl = 
                    paramLists.size()<=i ? null : 
                        paramLists.get(i++);
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
                    v.setType(new LazyType(unit) {
                        @Override
                        public Type initQualifyingType() {
                            return rm.getTypedParameter(p)
                                    .getFullType()
                                    .getQualifyingType();
                        }
                        @Override
                        public Map<TypeParameter,Type> 
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
                    Tree.Parameter tp =
                            tpl.getParameters()
                                .get(j);
                    Parameter rp = tp.getParameterModel();
                    rp.setDefaulted(p.isDefaulted());
                    rp.setDeclaration(m);
                    l.getParameters().add(rp);
                }
                j++;
            }
            m.getParameterLists().add(l);
        }
        if (typeParams!=null) {
            m.setTypeParameters(typeParams);
            //TODO: check 'em!!
        }
        else if (!sm.getTypeParameters().isEmpty()) {
            if (me instanceof Tree.ParameterizedExpression) {
                bme.addError("refined method is generic: '" +
                        sm.getName(unit) + 
                        "' declares type parameters");
            }
            else {
                //we're refining it by assigning a function
                //reference using the = specifier, not =>
                //copy the type parameters of the refined
                //declaration
                List<TypeParameter> typeParameters = 
                        sm.getTypeParameters();
                List<TypeParameter> tps = 
                        new ArrayList<TypeParameter>
                            (typeParameters.size());
                Map<TypeParameter,Type> subs = 
                        new HashMap<TypeParameter,Type>();
                for (int j=0; j<typeParameters.size(); j++) {
                    TypeParameter param = typeParameters.get(j);
                    TypeParameter tp = new TypeParameter();
                    tp.setName(param.getName());
                    tp.setUnit(unit);
                    tp.setScope(m);
                    tp.setContainer(m);
                    tp.setDeclaration(m);
                    tp.setCovariant(param.isCovariant());
                    tp.setContravariant(param.isContravariant());
                    tps.add(tp);
                    subs.put(param, tp.getType());
                }
                //we need to substitute these type parameters 
                //into the upper bounds of the type parameters
                //of the refined declaration
                for (int j=0; j<typeParameters.size(); j++) {
                    TypeParameter param = typeParameters.get(j);
                    TypeParameter tp = tps.get(j);
                    List<Type> sts = param.getSatisfiedTypes();
                    ArrayList<Type> ssts = 
                            new ArrayList<Type>(sts.size());
                    for (Type st: sts) {
                        ssts.add(st.substitute(subs, null));
                    }
                    tp.setSatisfiedTypes(ssts);
                    List<Type> cts = param.getCaseTypes();
                    if (cts!=null) {
                        ArrayList<Type> scts = 
                                new ArrayList<Type>(cts.size());
                        for (Type ct: cts) {
                            scts.add(ct.substitute(subs, null));
                        }
                        tp.setCaseTypes(scts);
                    }
                }
                m.setTypeParameters(tps);
            }
        }
        m.setShared(true);
        m.setActual(true);
        m.getAnnotations().add(new Annotation("shared"));
        m.getAnnotations().add(new Annotation("actual"));
        m.setRefinedDeclaration(root);
        m.setUnit(unit);
        m.setContainer(c);
        m.setShortcutRefinement(true);
        m.setDeclaredVoid(sm.isDeclaredVoid());
        setVisibleScope(m);
        c.addMember(m);
        that.setRefinement(true);
        that.setDeclaration(m);
        that.setRefined(sm);
        unit.addDeclaration(m);
        Scope scope = that.getScope();
        if (scope instanceof Specification) {
            Specification spec = (Specification) scope;
            spec.setDeclaration(m);
        }
        m.setType(new LazyType(unit) {
            @Override
            public Type initQualifyingType() {
                Type type = rm.getType();
                return type==null ? null : 
                    type.getQualifyingType();
            }
            @Override
            public Map<TypeParameter,Type> 
            initTypeArguments() {
                Type type = rm.getType();
                return type==null ? null : 
                    type.getTypeArguments();
            }
            @Override
            public TypeDeclaration initDeclaration() {
                Type type = rm.getType();
                return type==null ? null : 
                    type.getDeclaration();
            }
        });
        inheritDefaultedArguments(m);
    }
    
}
