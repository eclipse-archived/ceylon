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
package com.redhat.ceylon.compiler.java.loader.mirror;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.redhat.ceylon.compiler.java.util.Util;
import com.redhat.ceylon.compiler.loader.AbstractModelLoader;
import com.redhat.ceylon.compiler.loader.mirror.AnnotationMirror;
import com.redhat.ceylon.compiler.loader.mirror.ClassMirror;
import com.redhat.ceylon.compiler.loader.mirror.FieldMirror;
import com.redhat.ceylon.compiler.loader.mirror.MethodMirror;
import com.redhat.ceylon.compiler.loader.mirror.PackageMirror;
import com.redhat.ceylon.compiler.loader.mirror.TypeMirror;
import com.redhat.ceylon.compiler.loader.mirror.TypeParameterMirror;
import com.redhat.ceylon.compiler.typechecker.model.Module;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Symbol.ClassSymbol;
import com.sun.tools.javac.code.Symbol.MethodSymbol;
import com.sun.tools.javac.code.Symbol.VarSymbol;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.code.TypeTags;

public class JavacClass implements ClassMirror {

    public ClassSymbol classSymbol;

    private PackageMirror pkg;
    private TypeMirror superclass;
    private List<MethodMirror> methods;
    private List<TypeMirror> interfaces;
    private Map<String, AnnotationMirror> annotations;
    private List<TypeParameterMirror> typeParams;

    private ClassMirror enclosingClass;
    private boolean enclosingClassSet;

    private MethodMirror enclosingMethod;
    private boolean enclosingMethodSet;

    private List<FieldMirror> fields;

    private LinkedList<ClassMirror> innerClasses;

    private String cacheKey;

    public JavacClass(ClassSymbol classSymbol){
        this.classSymbol = classSymbol;
    }
    
    public String toString() {
        return getClass().getSimpleName() + " of " + classSymbol;
    }
    
    @Override
    public boolean isPublic() {
        return (classSymbol.flags() & Flags.PUBLIC) != 0;
    }

    @Override
    public boolean isProtected() {
        return (classSymbol.flags() & Flags.PROTECTED) != 0;
    }

    @Override
    public boolean isDefaultAccess() {
        return (classSymbol.flags() & (Flags.PROTECTED | Flags.PUBLIC | Flags.PRIVATE)) == 0;
    }

    @Override
    public String getQualifiedName() {
        // as taken from ClassSymbol.className():
        if(classSymbol.name.length() == 0)
            return classSymbol.flatname.toString();
        else
            return classSymbol.getQualifiedName().toString();
    }

    @Override
    public String getFlatName() {
        return classSymbol.flatname.toString();
    }
    
    @Override
    public PackageMirror getPackage() {
        if (pkg == null) {
            pkg = new JavacPackage(classSymbol.packge());
        }
        return pkg;
    }

    @Override
    public boolean isInterface() {
        return classSymbol.isInterface();
    }
    
    @Override
    public boolean isAnnotationType() {
        return (classSymbol.flags()& Flags.ANNOTATION) != 0;
    }

    @Override
    public boolean isCeylonToplevelAttribute() {
        return !isInnerClass() && getAnnotation(AbstractModelLoader.CEYLON_ATTRIBUTE_ANNOTATION) != null;
    }

    @Override
    public boolean isCeylonToplevelObject() {
        return !isInnerClass() && getAnnotation(AbstractModelLoader.CEYLON_OBJECT_ANNOTATION) != null;
    }

    @Override
    public boolean isCeylonToplevelMethod() {
        return !isInnerClass() && getAnnotation(AbstractModelLoader.CEYLON_METHOD_ANNOTATION) != null;
    }

    @Override
    public AnnotationMirror getAnnotation(String type) {
        if (annotations == null) {
            annotations = JavacUtil.getAnnotations(classSymbol);
        }
        return annotations.get(type);
    }

    @Override
    public boolean isLoadedFromSource() {
        return Util.isLoadedFromSource(classSymbol);
    }

    @Override
    public String getName() {
        return classSymbol.getSimpleName().toString();
    }

    @Override
    public boolean isAbstract() {
        return (classSymbol.flags() & Flags.ABSTRACT) != 0;
    }

    @Override
    public boolean isStatic() {
        return (classSymbol.flags() & Flags.STATIC) != 0;
    }

    @Override
    public boolean isFinal() {
        return (classSymbol.flags() & Flags.FINAL) != 0;
    }

    @Override
    public List<MethodMirror> getDirectMethods() {
        if (methods == null) {
            List<MethodMirror> ret = new LinkedList<MethodMirror>();
            for(Symbol sym : classSymbol.getEnclosedElements()){
                if(sym instanceof MethodSymbol && (sym.flags() & Flags.PRIVATE) == 0){
                    ret.add(new JavacMethod(this, (MethodSymbol)sym));
                }
            }
            methods = Collections.unmodifiableList(ret);
        }
        return methods;
    }

    @Override
    public List<FieldMirror> getDirectFields() {
        if(fields == null){
            List<FieldMirror> ret = new LinkedList<FieldMirror>();
            for(Symbol sym : classSymbol.getEnclosedElements()){
                if(sym instanceof VarSymbol && (sym.flags() & Flags.PRIVATE) == 0){
                    ret.add(new JavacField((VarSymbol)sym));
                }
            }
            fields = Collections.unmodifiableList(ret);
        }
        return fields;
    }

    @Override
    public TypeMirror getSuperclass() {
        if (superclass == null) {
            Type supercls = classSymbol.getSuperclass();
            if (supercls != null && supercls.tag != TypeTags.NONE) {
                superclass = new JavacType(supercls);
            }
        }
        return superclass;
    }

    @Override
    public List<TypeMirror> getInterfaces() {
        if (interfaces == null) {
            List<TypeMirror> ret = new ArrayList<TypeMirror>(classSymbol.getInterfaces().size());
            for(Type interfce : classSymbol.getInterfaces())
                ret.add(new JavacType(interfce));
            interfaces = Collections.unmodifiableList(ret);
        }
        return interfaces;
    }

    @Override
    public List<TypeParameterMirror> getTypeParameters() {
        if (typeParams == null) {
            typeParams = Collections.unmodifiableList(JavacUtil.getTypeParameters(classSymbol));
        }
        return typeParams;
    }

    @Override
    public boolean isInnerClass() {
        return getAnnotation(AbstractModelLoader.CEYLON_CONTAINER_ANNOTATION) != null || classSymbol.owner instanceof ClassSymbol;
    }

    @Override
    public boolean isLocalClass() {
        return getAnnotation(AbstractModelLoader.CEYLON_LOCAL_CONTAINER_ANNOTATION) != null 
                || classSymbol.owner instanceof MethodSymbol;
    }

    @Override
    public List<ClassMirror> getDirectInnerClasses() {
        if(innerClasses == null){
            innerClasses = new LinkedList<ClassMirror>();
            for(Symbol elem : classSymbol.getEnclosedElements()){
                if(elem instanceof ClassSymbol)
                    innerClasses.add(new JavacClass((ClassSymbol) elem));
            }
        }
        return innerClasses;
    }

    @Override
    public boolean isAnonymous() {
        return classSymbol.name.isEmpty();
    }

    @Override
    public boolean isJavaSource() {
        return Util.isJavaSource(classSymbol);
    }

    @Override
    public ClassMirror getEnclosingClass() {
        if (!enclosingClassSet) {
            Symbol encl = classSymbol.getEnclosingElement();
            if (encl != null && encl instanceof ClassSymbol) {
                enclosingClass = new JavacClass((ClassSymbol)encl);
            }
            enclosingClassSet = true;
        }
        return enclosingClass;
    }

    @Override
    public MethodMirror getEnclosingMethod() {
        if (!enclosingMethodSet) {
            Symbol encl = classSymbol.getEnclosingElement();
            if (encl != null && encl instanceof MethodSymbol) {
                // it's a method, it must be in a Class
                ClassSymbol enclosingClass = (ClassSymbol) encl.getEnclosingElement();
                JavacClass enclosingClassMirror = new JavacClass(enclosingClass);
                enclosingMethod = new JavacMethod(enclosingClassMirror, (MethodSymbol)encl);
            }
            enclosingMethodSet = true;
        }
        return enclosingMethod;
    }

    @Override
    public boolean isEnum() {
        return (classSymbol.flags() & Flags.ENUM) != 0;
    }

    @Override
    public String getCacheKey(Module module) {
        if(cacheKey == null){
            String className = getQualifiedName();
            cacheKey = AbstractModelLoader.getCacheKeyByModule(module, className);
        }
        return cacheKey;
    }
}
