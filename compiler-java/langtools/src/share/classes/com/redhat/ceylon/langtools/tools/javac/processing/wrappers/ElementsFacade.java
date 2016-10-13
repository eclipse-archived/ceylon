package com.redhat.ceylon.langtools.tools.javac.processing.wrappers;

import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Name;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;

import com.redhat.ceylon.javax.lang.model.util.Elements;

public class ElementsFacade implements javax.lang.model.util.Elements {

    private Elements f;

    public ElementsFacade(Elements f) {
        this.f = f;
    }

    @Override
    public List<? extends AnnotationMirror> getAllAnnotationMirrors(Element arg0) {
        return Facades.facadeAnnotationMirrors(f.getAllAnnotationMirrors(Facades.unfacade(arg0)));
    }

    @Override
    public List<? extends Element> getAllMembers(TypeElement arg0) {
        return Facades.facadeElementList(f.getAllMembers(Facades.unfacade(arg0)));
    }

    @Override
    public Name getBinaryName(TypeElement arg0) {
        return Facades.facade(f.getBinaryName(Facades.unfacade(arg0)));
    }

    @Override
    public String getConstantExpression(Object arg0) {
        return f.getConstantExpression(arg0);
    }

    @Override
    public String getDocComment(Element arg0) {
        return f.getDocComment(Facades.unfacade(arg0));
    }

    @Override
    public Map<? extends ExecutableElement, ? extends AnnotationValue> getElementValuesWithDefaults(AnnotationMirror arg0) {
        return Facades.facadeElementValues(f.getElementValuesWithDefaults(Facades.unfacade(arg0)));
    }

    @Override
    public Name getName(CharSequence arg0) {
        return Facades.facade(f.getName(arg0));
    }

    @Override
    public PackageElement getPackageElement(CharSequence arg0) {
        return Facades.facade(f.getPackageElement(arg0));
    }

    @Override
    public PackageElement getPackageOf(Element arg0) {
        return Facades.facade(f.getPackageOf(Facades.unfacade(arg0)));
    }

    @Override
    public TypeElement getTypeElement(CharSequence arg0) {
        return Facades.facade(f.getTypeElement(arg0));
    }

    @Override
    public boolean hides(Element arg0, Element arg1) {
        return f.hides(Facades.unfacade(arg0), Facades.unfacade(arg1));
    }

    @Override
    public boolean isDeprecated(Element arg0) {
        return f.isDeprecated(Facades.unfacade(arg0));
    }

    @Override
    public boolean overrides(ExecutableElement arg0, ExecutableElement arg1, TypeElement arg2) {
        return f.overrides(Facades.unfacade(arg0), Facades.unfacade(arg1), Facades.unfacade(arg2));
    }

    @Override
    public void printElements(Writer arg0, Element... arg1) {
        f.printElements(arg0, Facades.unfacade(arg1));
    }

    // Java 8 method
//    @Override
    public boolean isFunctionalInterface(TypeElement arg0) {
        // must use reflection for it to work on Java 7
        try {
            Method method = Elements.class.getMethod("isFunctionalInterface", com.redhat.ceylon.javax.lang.model.element.TypeElement.class);
            return (Boolean) method.invoke(f, Facades.unfacade(arg0));
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof ElementsFacade == false)
            return false;
        return f.equals(((ElementsFacade)obj).f);
    }
    
    @Override
    public int hashCode() {
        return f.hashCode();
    }
    
    @Override
    public String toString() {
        return f.toString();
    }
}
