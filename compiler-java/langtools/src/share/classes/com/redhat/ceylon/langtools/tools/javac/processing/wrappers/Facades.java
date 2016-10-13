package com.redhat.ceylon.langtools.tools.javac.processing.wrappers;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.redhat.ceylon.javax.annotation.processing.Filer;
import com.redhat.ceylon.javax.annotation.processing.Messager;
import com.redhat.ceylon.javax.annotation.processing.ProcessingEnvironment;
import com.redhat.ceylon.javax.annotation.processing.RoundEnvironment;
import com.redhat.ceylon.javax.lang.model.element.TypeElement;
import com.redhat.ceylon.javax.lang.model.element.TypeParameterElement;
import com.redhat.ceylon.javax.lang.model.element.VariableElement;
import com.redhat.ceylon.javax.lang.model.type.ArrayType;
import com.redhat.ceylon.javax.lang.model.type.DeclaredType;
import com.redhat.ceylon.javax.lang.model.type.ErrorType;
import com.redhat.ceylon.javax.lang.model.type.ExecutableType;
import com.redhat.ceylon.javax.lang.model.type.NoType;
import com.redhat.ceylon.javax.lang.model.type.NullType;
import com.redhat.ceylon.javax.lang.model.type.PrimitiveType;
import com.redhat.ceylon.javax.lang.model.type.ReferenceType;
import com.redhat.ceylon.javax.lang.model.type.TypeVariable;
import com.redhat.ceylon.javax.lang.model.type.UnionType;
import com.redhat.ceylon.javax.lang.model.type.WildcardType;
import com.redhat.ceylon.javax.lang.model.util.Elements;
import com.redhat.ceylon.javax.lang.model.util.Types;
import com.redhat.ceylon.javax.tools.FileObject;
import com.redhat.ceylon.javax.tools.JavaFileManager.Location;
import com.redhat.ceylon.javax.tools.JavaFileObject;
import com.redhat.ceylon.javax.tools.StandardLocation;
import com.redhat.ceylon.javax.lang.model.type.TypeMirror;
import com.redhat.ceylon.javax.lang.model.type.TypeKind;
import com.redhat.ceylon.javax.lang.model.SourceVersion;
import com.redhat.ceylon.javax.lang.model.element.AnnotationMirror;
import com.redhat.ceylon.javax.lang.model.element.AnnotationValue;
import com.redhat.ceylon.javax.lang.model.element.Element;
import com.redhat.ceylon.javax.lang.model.element.Name;
import com.redhat.ceylon.javax.lang.model.element.NestingKind;
import com.redhat.ceylon.javax.lang.model.element.PackageElement;
import com.redhat.ceylon.javax.lang.model.element.ElementKind;
import com.redhat.ceylon.javax.lang.model.element.ExecutableElement;
import com.redhat.ceylon.javax.lang.model.element.Modifier;

public class Facades {
    public static Set<? extends javax.lang.model.element.TypeElement> facadeTypeElements(Set<? extends TypeElement> annotations) {
        Set<javax.lang.model.element.TypeElement> ret = new HashSet<>(annotations.size());
        for(TypeElement el : annotations){
            ret.add(facade(el));
        }
        return ret;
    }

    public static javax.annotation.processing.RoundEnvironment facade(RoundEnvironment roundEnv) {
        if(roundEnv == null)
            return null;
        return new RoundEnvironmentFacade(roundEnv);
    }

    public static javax.annotation.processing.ProcessingEnvironment facade(ProcessingEnvironment processingEnv) {
        if(processingEnv == null)
            return null;
        return new ProcessingEnvironmentFacade(processingEnv);
    }

    public static javax.lang.model.element.TypeElement facade(TypeElement el) {
        if(el == null)
            return null;
        return new TypeElementFacade(el);
    }

    public static javax.lang.model.element.TypeParameterElement facade(TypeParameterElement el) {
        if(el == null)
            return null;
        return new TypeParameterElementFacade(el);
    }

    public static javax.lang.model.element.VariableElement facade(VariableElement el) {
        if(el == null)
            return null;
        return new VariableElementFacade(el);
    }

    public static javax.lang.model.element.PackageElement facade(PackageElement el) {
        if(el == null)
            return null;
        return new PackageElementFacade(el);
    }

    public static javax.lang.model.element.ExecutableElement facade(ExecutableElement el) {
        if(el == null)
            return null;
        return new ExecutableElementFacade(el);
    }

    public static javax.lang.model.element.Element facade(Element el) {
        if(el == null)
            return null;
        if(el instanceof TypeElement)
            return facade((TypeElement)el);
        if(el instanceof TypeParameterElement)
            return facade((TypeParameterElement)el);
        if(el instanceof VariableElement)
            return facade((VariableElement)el);
        if(el instanceof ExecutableElement)
            return facade((ExecutableElement)el);
        if(el instanceof PackageElement)
            return facade((PackageElement)el);
        return new ElementFacade(el);
    }

    public static javax.lang.model.element.Name facade(Name name) {
        return name == null ? null : new NameFacade(name);
    }

    public static Set<? extends javax.lang.model.element.Element> facadeElementSet(Set<? extends Element> elements) {
        Set<javax.lang.model.element.Element> ret = new HashSet<>(elements.size());
        for(Element el : elements)
            ret.add(facade(el));
        return ret;
    }

    public static List<? extends javax.lang.model.element.Element> facadeElementList(List<? extends Element> elements) {
        List<javax.lang.model.element.Element> ret = new ArrayList<>(elements.size());
        for(Element el : elements)
            ret.add(facade(el));
        return ret;
    }

    public static javax.lang.model.element.ElementKind facade(ElementKind kind) {
        return javax.lang.model.element.ElementKind.valueOf(kind.name());
    }

    public static Set<javax.lang.model.element.Modifier> facadeModifiers(Set<Modifier> modifiers) {
        EnumSet<javax.lang.model.element.Modifier> ret = EnumSet.noneOf(javax.lang.model.element.Modifier.class);
        for(Modifier mod : modifiers)
            ret.add(facade(mod));
        return ret;
    }

    public static javax.lang.model.element.Modifier facade(Modifier mod) {
        return javax.lang.model.element.Modifier.valueOf(mod.name());
    }

    public static javax.lang.model.type.ExecutableType facade(ExecutableType type) {
        return type == null ? null : new ExecutableTypeFacade(type);
    }

    public static javax.lang.model.type.NoType facade(NoType type) {
        return type == null ? null : new NoTypeFacade(type);
    }

    public static javax.lang.model.type.PrimitiveType facade(PrimitiveType type) {
        return type == null ? null : new PrimitiveTypeFacade(type);
    }

    public static javax.lang.model.type.UnionType facade(UnionType type) {
        return type == null ? null : new UnionTypeFacade(type);
    }

    public static javax.lang.model.type.WildcardType facade(WildcardType type) {
        return type == null ? null : new WildcardTypeFacade(type);
    }

    public static javax.lang.model.type.ArrayType facade(ArrayType type) {
        return type == null ? null : new ArrayTypeFacade(type);
    }

    public static javax.lang.model.type.NullType facade(NullType type) {
        return type == null ? null : new NullTypeFacade(type);
    }

    public static javax.lang.model.type.TypeVariable facade(TypeVariable type) {
        return type == null ? null : new TypeVariableFacade(type);
    }

    public static javax.lang.model.type.DeclaredType facade(DeclaredType type) {
        return type == null ? null : new DeclaredTypeFacade(type);
    }

    public static javax.lang.model.type.ErrorType facade(ErrorType type) {
        return type == null ? null : new ErrorTypeFacade(type);
    }

    public static javax.lang.model.type.TypeMirror facade(TypeMirror type) {
        if(type == null)
            return null;
        if(type instanceof ExecutableType)
            return facade((ExecutableType)type);
        if(type instanceof NoType)
            return facade((NoType)type);
        if(type instanceof PrimitiveType)
            return facade((PrimitiveType)type);
        if(type instanceof ArrayType)
            return facade((ArrayType)type);
        if(type instanceof ErrorType)
            return facade((ErrorType)type);
        if(type instanceof DeclaredType)
            return facade((DeclaredType)type);
        if(type instanceof NullType)
            return facade((NullType)type);
        if(type instanceof TypeVariable)
            return facade((TypeVariable)type);
        if(type instanceof ReferenceType)
            return facade((ReferenceType)type);
        if(type instanceof UnionType)
            return facade((UnionType)type);
        // FIXME: J8 has IntersectionType too!!
        if(type instanceof WildcardType)
            return facade((WildcardType)type);
        return new TypeMirrorFacade(type);
    }

    public static javax.lang.model.type.TypeKind facade(TypeKind kind) {
        return javax.lang.model.type.TypeKind.valueOf(kind.name());
    }

    public static List<? extends javax.lang.model.type.TypeMirror> facadeTypeMirrorList(List<? extends TypeMirror> types) {
        List<javax.lang.model.type.TypeMirror> ret = new ArrayList<>(types.size());
        for(TypeMirror el : types)
            ret.add(facade(el));
        return ret;
    }

    public static List<? extends javax.lang.model.type.TypeVariable> facadeTypeVariableList(List<? extends TypeVariable> types) {
        List<javax.lang.model.type.TypeVariable> ret = new ArrayList<>(types.size());
        for(TypeVariable el : types)
            ret.add(facade(el));
        return ret;
    }

    public static List<? extends javax.lang.model.element.AnnotationMirror> facadeAnnotationMirrors(List<? extends AnnotationMirror> mirrors) {
        List<javax.lang.model.element.AnnotationMirror> ret = new ArrayList<>(mirrors.size());
        for(AnnotationMirror el : mirrors)
            ret.add(facade(el));
        return ret;
    }

    public static javax.lang.model.element.AnnotationMirror facade(AnnotationMirror mirror) {
        return mirror == null ? null : new AnnotationMirrorFacade(mirror);
    }

    public static Map<? extends javax.lang.model.element.ExecutableElement, ? extends javax.lang.model.element.AnnotationValue> 
        facadeElementValues(Map<? extends ExecutableElement, ? extends AnnotationValue> elementValues) {
        Map<javax.lang.model.element.ExecutableElement, javax.lang.model.element.AnnotationValue> ret
            = new HashMap<>(elementValues.size());
        for(Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : elementValues.entrySet()){
            ret.put(facade(entry.getKey()), facade(entry.getValue()));
        }
        return ret;
    }

    public static javax.lang.model.element.AnnotationValue facade(AnnotationValue value) {
        return value == null ? null : new AnnotationValueFacade(value);
    }

    public static List<? extends javax.lang.model.element.VariableElement> facadeVariableElementList(List<? extends VariableElement> list) {
        List<javax.lang.model.element.VariableElement> ret = new ArrayList<>(list.size());
        for(VariableElement el : list)
            ret.add(facade(el));
        return ret;
    }

    public static List<? extends javax.lang.model.element.TypeParameterElement> facadeTypeParameterElementList(List<? extends TypeParameterElement> list) {
        List<javax.lang.model.element.TypeParameterElement> ret = new ArrayList<>(list.size());
        for(TypeParameterElement el : list)
            ret.add(facade(el));
        return ret;
    }

    public static List<? extends javax.lang.model.element.AnnotationValue> facadeAnnotationValueList(List<? extends AnnotationValue> list) {
        List<javax.lang.model.element.AnnotationValue> ret = new ArrayList<>(list.size());
        for(AnnotationValue el : list)
            ret.add(facade(el));
        return ret;
    }

    public static javax.annotation.processing.Messager facade(Messager messager) {
        return messager == null ? null : new MessagerFacade(messager);
    }

    public static Element unfacade(javax.lang.model.element.Element facade) {
        return ((ElementFacade)facade).f;
    }

    public static TypeElement unfacade(javax.lang.model.element.TypeElement facade) {
        return (TypeElement)((ElementFacade)facade).f;
    }

    public static ExecutableElement unfacade(javax.lang.model.element.ExecutableElement facade) {
        return (ExecutableElement)((ElementFacade)facade).f;
    }

    public static AnnotationMirror unfacade(javax.lang.model.element.AnnotationMirror arg3) {
        return ((AnnotationMirrorFacade)arg3).f;
    }

    public static AnnotationValue unfacade(javax.lang.model.element.AnnotationValue arg4) {
        return ((AnnotationValueFacade)arg4).f;
    }

    public static javax.lang.model.element.NestingKind facade(NestingKind nestingKind) {
        return javax.lang.model.element.NestingKind.valueOf(nestingKind.name());
    }

    public static javax.lang.model.util.Elements facade(Elements elements) {
        return elements == null ? null : new ElementsFacade(elements);
    }

    public static Element[] unfacade(javax.lang.model.element.Element[] arg) {
        Element[] ret = new Element[arg.length];
        int i=0;
        for(javax.lang.model.element.Element el : arg){
            ret[i++] = unfacade(el);
        }
        return ret;
    }

    public static TypeMirror[] unfacade(javax.lang.model.type.TypeMirror[] arg) {
        TypeMirror[] ret = new TypeMirror[arg.length];
        int i=0;
        for(javax.lang.model.type.TypeMirror el : arg){
            ret[i++] = unfacade(el);
        }
        return ret;
    }

    public static javax.annotation.processing.Filer facade(Filer filer) {
        return filer == null ? null : new FilerFacade(filer);
    }

    public static Location unfacade(javax.tools.JavaFileManager.Location location) {
        return location == null ? null : StandardLocation.valueOf(((javax.tools.StandardLocation)location).name());
    }

    public static javax.tools.FileObject facade(FileObject object) {
        if(object == null)
            return null;
        return new FileObjectFacade(object);
    }

    public static javax.tools.JavaFileObject facade(JavaFileObject object) {
        if(object == null)
            return null;
        return new JavaFileObjectFacade(object);
    }

    public static javax.tools.JavaFileObject.Kind facade(JavaFileObject.Kind kind) {
        return javax.tools.JavaFileObject.Kind.valueOf(kind.name());
    }

    public static javax.lang.model.SourceVersion facade(SourceVersion sourceVersion) {
        return javax.lang.model.SourceVersion.valueOf(sourceVersion.name());
    }

    public static javax.lang.model.util.Types facade(Types typeUtils) {
        return new TypesFacade(typeUtils);
    }

    public static TypeMirror unfacade(javax.lang.model.type.TypeMirror arg0) {
        return ((TypeMirrorFacade)arg0).f;
    }

    public static DeclaredType unfacade(javax.lang.model.type.DeclaredType arg0) {
        return (DeclaredType) ((DeclaredTypeFacade)arg0).f;
    }

    public static PrimitiveType unfacade(javax.lang.model.type.PrimitiveType arg0) {
        return (PrimitiveType) ((PrimitiveTypeFacade)arg0).f;
    }

    public static ExecutableType unfacade(javax.lang.model.type.ExecutableType arg0) {
        return (ExecutableType) ((ExecutableTypeFacade)arg0).f;
    }
}
