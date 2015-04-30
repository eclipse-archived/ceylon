/*
 * Copyright Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the authors tag. All rights reserved.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License version 2.
 * 
 * This particular file is subject to the "Classpath" exception as provided in the 
 * LICENSE file that accompanied this code.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */

package com.redhat.ceylon.compiler.loader.model;

import static com.redhat.ceylon.model.typechecker.model.Util.lookupMember;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.redhat.ceylon.compiler.java.codegen.AnnotationArgument;
import com.redhat.ceylon.compiler.java.codegen.AnnotationConstructorParameter;
import com.redhat.ceylon.compiler.java.codegen.AnnotationInvocation;
import com.redhat.ceylon.compiler.java.codegen.CodegenUtil;
import com.redhat.ceylon.compiler.java.codegen.Decl;
import com.redhat.ceylon.compiler.java.codegen.Naming;
import com.redhat.ceylon.compiler.java.codegen.ParameterAnnotationTerm;
import com.redhat.ceylon.compiler.java.util.Util;
import com.redhat.ceylon.compiler.loader.AbstractModelLoader;
import com.redhat.ceylon.compiler.loader.ModelLoader.DeclarationType;
import com.redhat.ceylon.compiler.loader.mirror.ClassMirror;
import com.redhat.ceylon.model.typechecker.model.Annotation;
import com.redhat.ceylon.model.typechecker.model.Class;
import com.redhat.ceylon.model.typechecker.model.Declaration;
import com.redhat.ceylon.model.typechecker.model.Module;
import com.redhat.ceylon.model.typechecker.model.Package;
import com.redhat.ceylon.model.typechecker.model.Parameter;
import com.redhat.ceylon.model.typechecker.model.ParameterList;
import com.redhat.ceylon.model.typechecker.model.ProducedType;
import com.redhat.ceylon.model.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.model.typechecker.model.Unit;
import com.redhat.ceylon.model.typechecker.model.Value;

/**
 * Represents a lazy Package declaration.
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
public class LazyPackage extends Package {
    
    private AbstractModelLoader modelLoader;
    private List<Declaration> compiledDeclarations = new ArrayList<Declaration>(3);
    private Set<Unit> lazyUnits = new HashSet<Unit>();
    private Map<String,Declaration> cache = new HashMap<String,Declaration>();
    
    public LazyPackage(AbstractModelLoader modelLoader){
        this.modelLoader = modelLoader;
    }
    
    @Override
    public Declaration getMember(String name, List<ProducedType> signature, boolean ellipsis) {
        // FIXME: what use is this method in the type checker?
        return getDirectMember(name, signature, ellipsis);
    }
    
    // FIXME: redo this method better: https://github.com/ceylon/ceylon-spec/issues/90
    @Override
    public Declaration getDirectMember(String name, List<ProducedType> signature, boolean ellipsis) {
//        System.err.println("getMember "+name+" "+signature+" "+ellipsis);
        boolean canCache = (signature == null && !ellipsis);
        if(canCache){
            if(cache.containsKey(name)) {
                Declaration cachedDeclaration = cache.get(name);
                if (cachedDeclaration != null || ! modelLoader.searchAgain(this, name)) {
                    return cachedDeclaration;
                }

            }
        }
        Declaration ret = getDirectMemberMemoised(name, signature, ellipsis);
        if(canCache){
            cache.put(name, ret);
        }
        return ret;
    }
    
    private Declaration getDirectMemberMemoised(String name, List<ProducedType> signature, boolean ellipsis) {
        synchronized(modelLoader.getLock()){

            String pkgName = getQualifiedNameString();
            Module module = getModule();
            
            // we need its package ready first
            modelLoader.loadPackage(module, pkgName, false);

            // make sure we iterate over a copy of compiledDeclarations, to avoid lazy loading to modify it and
            // cause a ConcurrentModificationException: https://github.com/ceylon/ceylon-compiler/issues/399
            Declaration d = lookupMember(compiledDeclarations, name, signature, ellipsis);
            if (d != null) {
                return d;
            }

            String className = getQualifiedName(pkgName, name);
            ClassMirror classSymbol = modelLoader.lookupClassMirror(module, className);

            // only get it from the classpath if we're not compiling it, unless
            // it happens to be a java source
            if(classSymbol != null && (!classSymbol.isLoadedFromSource() || classSymbol.isJavaSource())) {
                d = modelLoader.convertToDeclaration(module, className, DeclarationType.VALUE);
                if (d instanceof Class) {
                    Class c = (Class) d;
                    if (c.isAbstraction() && signature != null) {
                        ArrayList<Declaration> list = new ArrayList<Declaration>(c.getOverloads());
                        list.add(c);
                        return lookupMember(list, name, signature, ellipsis);
                    }
                }
                return d;
            }
            d = getDirectMemberFromSource(name);
            
            if (d == null
                    && Character.isLowerCase(name.codePointAt(0))
                    && Character.isUpperCase(Character.toUpperCase(name.codePointAt(0)))) {
                // Might be trying to get an annotation constructor for a Java annotation type
                // So try to find the annotation type with two strategies:
                // - urlDecoder -> UrlDecover and url -> Url
                // - urlDecoder -> URLDecoder and url -> URL
                for(String annotationName : Arrays.asList(Naming.capitalize(name), CodegenUtil.getReverseJavaBeanName(name), Naming.capitalize(name).replaceFirst("__(CONSTRUCTOR|TYPE|PACKAGE|FIELD|METHOD|ANNOTATION_TYPE|LOCAL_VARIABLE|PARAMETER|SETTER|GETTER)$", ""))){
                    Declaration possibleAnnotationType = getDirectMember(annotationName, signature, ellipsis);
                    if (possibleAnnotationType != null
                            && possibleAnnotationType instanceof LazyInterface
                            && ((LazyInterface)possibleAnnotationType).isAnnotationType()) {
                        // addMember() will have added a Method if we found an annotation type
                        // so now we can look for the constructor again
                        d = lookupMember(compiledDeclarations, name, signature, ellipsis);
                    }
                }
            }
            return d;
        }
    }

    private Declaration getDirectMemberFromSource(String name) {
        for (Declaration d: super.getMembers()) {
            if (com.redhat.ceylon.model.typechecker.model.Util.isResolvable(d) /* && d.isShared() */ 
            && com.redhat.ceylon.model.typechecker.model.Util.isNamed(name, d)) {
                return d;
            }
        }
        return null;
    }

    public String getQualifiedName(final String pkgName, String name) {
        // no need to quote the name itself as java keywords are lower-cased and we append a _ to every
        // lower-case toplevel so they can never be java keywords
        String className = pkgName.isEmpty() ? name : Util.quoteJavaKeywords(pkgName) + "." + name;
        return className;
    }
    
    // FIXME: This is only here for wildcard imports, and we should be able to make it lazy like the rest
    // with a bit of work in the typechecker
    // FIXME: redo this method better: https://github.com/ceylon/ceylon-spec/issues/90
    @Override
    public List<Declaration> getMembers() {
        synchronized(modelLoader.getLock()){
            // make sure the package is loaded
            modelLoader.loadPackage(getModule(), getQualifiedNameString(), true);
            List<Declaration> sourceDeclarations = super.getMembers();
            LinkedList<Declaration> ret = new LinkedList<Declaration>();
            ret.addAll(sourceDeclarations);
            ret.addAll(compiledDeclarations);
            return ret;
        }
    }

    @Override
    public void addMember(Declaration declaration) {
        super.addMember(declaration);
        flushCache(declaration);
    }
    
    private void flushCache(Declaration declaration) {
        cache.remove(declaration.getName());
    }

    public void addCompiledMember(Declaration d) {
        synchronized(modelLoader.getLock()){
            flushCache(d);
            compiledDeclarations.add(d);
            if (d instanceof LazyInterface
                    && !((LazyInterface)d).isCeylon()
                    && ((LazyInterface)d).isAnnotationType()) {
                makeInteropAnnotation((LazyInterface)d);
                
            }
            if ((d instanceof LazyClass ||
                    d instanceof LazyInterface)  && d.getUnit().getFilename() != null) {
                lazyUnits.add(d.getUnit());
            }
        }
    }

    /**
     * Adds extra members to the package for annotation interop.
     * For a Java declaration {@code @interface Annotation} we generate 
     * a model corresponding to:
     * <pre>
     *   annotation class Annotation$Proxy(...) satisfies Annotation {
     *       // a `shared` class parameter for each method of Annotation
     *   }
     *   annotation JavaAnnotation javaAnnotation(...) => JavaAnnotation$Proxy(...);
     * </pre>
     * 
     * We also make a {@code *__method}, {@code *__field} etc version for each
     * {@code @Target} program element
     * @param iface The model of the annotation @interface
     */
    private void makeInteropAnnotation(LazyInterface iface) {
        AnnotationProxyClass klass = makeInteropAnnotationClass(iface);
        
        compiledDeclarations.add(makeInteropAnnotationConstructor(iface, klass, null));
        
        EnumSet<AnnotationTarget> annotationTargets = AnnotationTarget.annotationTargets(klass);
        if (annotationTargets != null) {
            for (OutputElement target : OutputElement.possibleCeylonTargets(annotationTargets)) {
                compiledDeclarations.add(makeInteropAnnotationConstructor(iface, klass,  
                        target));
            }
        }
        
        compiledDeclarations.add(klass);
    }

    /**
     * <pre>
     *   annotation class Annotation$Proxy(...) satisfies Annotation {
     *       // a `shared` class parameter for each method of Annotation
     *   }
     * </pre>
     * @param iface The model of the annotation @interface
     * @return The annotation class for the given interface
     */
    protected AnnotationProxyClass makeInteropAnnotationClass(
            LazyInterface iface) {
        AnnotationProxyClass klass = new AnnotationProxyClass(iface);
        klass.setContainer(this);
        klass.setScope(this);
        klass.setName(iface.getName()+"$Proxy");
        klass.setShared(iface.isShared());
        klass.setAnnotation(true);
        Annotation annotationAnnotation = new Annotation();
        annotationAnnotation.setName("annotation");
        klass.getAnnotations().add(annotationAnnotation);
        klass.getSatisfiedTypes().add(iface.getType());
        klass.setUnit(iface.getUnit());
        ParameterList classpl = new ParameterList();
        klass.addParameterList(classpl);
        klass.setScope(this);
        
        for (Declaration member : iface.getMembers()) {
            boolean isValue = member.getName().equals("value");
            if (member instanceof JavaMethod) {
                JavaMethod m = (JavaMethod)member;
                Parameter klassParam = new Parameter();
                Value value = new Value();
                klassParam.setModel(value);
                value.setInitializerParameter(klassParam);
                klassParam.setDeclaration(klass);
                value.setContainer(klass);
                value.setScope(klass);
                value.setName(member.getName());
                klassParam.setName(member.getName());
                value.setType(annotationParameterType(iface.getUnit(), m));
                value.setUnboxed(true);
                value.setUnit(iface.getUnit());
                if(isValue)
                    classpl.getParameters().add(0, klassParam);
                else
                    classpl.getParameters().add(klassParam);
                klass.addMember(value);
            }
        }
        return klass;
    }

    /**
     * <pre>
     *   annotation JavaAnnotation javaAnnotation(...) => JavaAnnotation$Proxy(...);
     * </pre>
     * @param iface The model of the annotation @interface
     * @param klass The annotation class
     * @param ctorName The name of the constructor to generate
     * @param target The Java program element the constructor should target, 
     * if this is a disambiguating constructor, otherwise null
     * @return 
     */
    protected AnnotationProxyMethod makeInteropAnnotationConstructor(LazyInterface iface,
            AnnotationProxyClass klass, OutputElement oe) {
        
        String ctorName = oe == null ? CodegenUtil.getJavaBeanName(iface.getName()) : Naming.getDisambigAnnoCtorName(iface, oe);
        AnnotationProxyMethod ctor = new AnnotationProxyMethod();
        ctor.setAnnotationTarget(oe);
        ctor.setProxyClass(klass);
        ctor.setContainer(this);
        ctor.setAnnotation(true);
        ctor.setName(ctorName);
        ctor.setShared(iface.isShared());
        Annotation annotationAnnotation2 = new Annotation();
        annotationAnnotation2.setName("annotation");
        ctor.getAnnotations().add(annotationAnnotation2);
        ctor.setType(((TypeDeclaration)iface).getType());
        ctor.setUnit(iface.getUnit());
        
        ParameterList ctorpl = new ParameterList();
        ctorpl.setPositionalParametersSupported(false);
        ctor.addParameterList(ctorpl);
        
        AnnotationInvocation ai = new AnnotationInvocation();
        ai.setConstructorDeclaration(ctor);
        ai.setPrimary(klass);
        ai.setInterop(true);
        ctor.setAnnotationConstructor(ai);
        List<AnnotationArgument> annotationArgs = new ArrayList<AnnotationArgument>();
        for (Declaration member : iface.getMembers()) {
            boolean isValue = member.getName().equals("value");
            if (member instanceof JavaMethod) {
                JavaMethod m = (JavaMethod)member;
                
                ParameterAnnotationTerm term = new ParameterAnnotationTerm();
                AnnotationArgument argument = new AnnotationArgument();
                argument.setTerm(term);
                argument.setParameter(klass.getParameter(member.getName()));
                
                Parameter ctorParam = new Parameter();
                Value value = new Value();
                ctorParam.setModel(value);
                value.setInitializerParameter(ctorParam);
                ctorParam.setDeclaration(ctor);
                value.setContainer(klass);
                value.setScope(klass);
                ctorParam.setDefaulted(m.isDefaultedAnnotation());
                value.setName(member.getName());
                ctorParam.setName(member.getName());
                value.setType(annotationParameterType(iface.getUnit(), m));
                value.setUnboxed(true);
                value.setUnit(iface.getUnit());
                if(isValue)
                    ctorpl.getParameters().add(0, ctorParam);
                else
                    ctorpl.getParameters().add(ctorParam);
                term.setSourceParameter(ctorParam);
                ctor.addMember(value);
                
                AnnotationConstructorParameter acp = new AnnotationConstructorParameter();
                acp.setParameter(ctorParam);
                if(isValue)
                    ai.getConstructorParameters().add(0, acp);
                else
                    ai.getConstructorParameters().add(acp);
            
                annotationArgs.add(argument);
            }
        }
        ai.getAnnotationArguments().addAll(annotationArgs);
        return ctor;
    }

    private ProducedType annotationParameterType(Unit unit, JavaMethod m) {
        ProducedType type = m.getType();
        if (Decl.isJavaArray(type.getDeclaration())) {
            String name = type.getDeclaration().getQualifiedNameString();
            final ProducedType elementType;
            String underlyingType = null;
            if(name.equals("java.lang::ObjectArray")){
                ProducedType eType = type.getTypeArgumentList().get(0);
                String elementTypeName = eType.getDeclaration().getQualifiedNameString();
                if ("java.lang::String".equals(elementTypeName)) {
                    elementType = unit.getStringDeclaration().getType();
                } else if ("java.lang::Class".equals(elementTypeName)
                        || "java.lang.Class".equals(eType.getUnderlyingType())) {
                    // Two cases because the types 
                    // Class[] and Class<?>[] are treated differently by 
                    // AbstractModelLoader.obtainType()
                    
                    // TODO Replace with metamodel ClassOrInterface type
                    // once we have support for metamodel references
                    elementType = unit.getAnythingDeclaration().getType();
                    underlyingType = "java.lang.Class";
                } else {
                    elementType = eType;   
                }
                // TODO Enum elements
            } else if(name.equals("java.lang::LongArray")) {
                elementType = unit.getIntegerDeclaration().getType();
            } else if (name.equals("java.lang::ByteArray")) {
                elementType = unit.getByteDeclaration().getType();
            } else if (name.equals("java.lang::ShortArray")) {
                elementType = unit.getIntegerDeclaration().getType();
                underlyingType = "short";
            } else if (name.equals("java.lang::IntArray")){
                elementType = unit.getIntegerDeclaration().getType();
                underlyingType = "int";
            } else if(name.equals("java.lang::BooleanArray")){
                elementType = unit.getBooleanDeclaration().getType();
            } else if(name.equals("java.lang::CharArray")){
                elementType = unit.getCharacterDeclaration().getType();
                underlyingType = "char";
            } else if(name.equals("java.lang::DoubleArray")) {
                elementType = unit.getFloatDeclaration().getType();
            } else if (name.equals("java.lang::FloatArray")){
                elementType = unit.getFloatDeclaration().getType();
                underlyingType = "float";
            } else {
                throw new RuntimeException();
            }
            elementType.setUnderlyingType(underlyingType);
            ProducedType iterableType = unit.getIterableType(elementType);
            return iterableType;
        } else if ("java.lang::Class".equals(type.getDeclaration().getQualifiedNameString())) {
            // TODO Replace with metamodel ClassOrInterface type
            // once we have support for metamodel references
            return unit.getAnythingDeclaration().getType();
        } else {
            return type;
        }
    }
    
    @Override
    public Iterable<Unit> getUnits() {
        synchronized(modelLoader.getLock()){
            Iterable<Unit> sourceUnits = super.getUnits();
            LinkedList<Unit> ret = new LinkedList<Unit>();
            for (Unit unit : sourceUnits) {
                ret.add(unit);
            }
            ret.addAll(lazyUnits);
            return ret;
        }
    }

    @Override
    public void removeUnit(Unit unit) {
        synchronized(modelLoader.getLock()){
            for (Declaration d : unit.getDeclarations()) {
                flushCache(d);
                if (d instanceof TypeDeclaration) {
                    ((TypeDeclaration)d).clearProducedTypeCache();
                }
            }
            if (unit.getFilename().endsWith(".class") || unit.getFilename().endsWith(".java")) {
                lazyUnits.remove(unit);
                for (Declaration d : unit.getDeclarations()) {
                    compiledDeclarations.remove(d);
                    // TODO : remove the declaration from the declaration map in AbstractModelLoader
                }
                modelLoader.removeDeclarations(unit.getDeclarations());
            } else {            
                super.removeUnit(unit);
            }
        }
    }
}
