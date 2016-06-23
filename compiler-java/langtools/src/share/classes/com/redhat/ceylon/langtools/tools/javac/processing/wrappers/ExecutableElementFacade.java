package com.redhat.ceylon.langtools.tools.javac.processing.wrappers;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import com.redhat.ceylon.javax.lang.model.element.ExecutableElement;
import com.redhat.ceylon.javax.lang.model.type.TypeMirror;
import com.redhat.ceylon.javax.lang.model.util.Elements;

public class ExecutableElementFacade extends ElementFacade implements javax.lang.model.element.ExecutableElement {

    public ExecutableElementFacade(ExecutableElement f) {
        super(f);
    }

    @Override
    public javax.lang.model.element.AnnotationValue getDefaultValue() {
        return Facades.facade(((ExecutableElement)f).getDefaultValue());
    }

    @Override
    public List<? extends javax.lang.model.element.VariableElement> getParameters() {
        return Facades.facadeVariableElementList(((ExecutableElement)f).getParameters());
    }

    @Override
    public javax.lang.model.type.TypeMirror getReturnType() {
        return Facades.facade(((ExecutableElement)f).getReturnType());
    }

    @Override
    public List<? extends javax.lang.model.type.TypeMirror> getThrownTypes() {
        return Facades.facadeTypeMirrorList(((ExecutableElement)f).getThrownTypes());
    }

    @Override
    public List<? extends javax.lang.model.element.TypeParameterElement> getTypeParameters() {
        return Facades.facadeTypeParameterElementList(((ExecutableElement)f).getTypeParameters());
    }

    @Override
    public boolean isVarArgs() {
        return ((ExecutableElement)f).isVarArgs();
    }

    // Java 8 method
//    @Override
    public javax.lang.model.type.TypeMirror getReceiverType() {
        // must use reflection for it to work on Java 7
        try {
            Method method = ExecutableElement.class.getMethod("getReceiverType");
            return Facades.facade((TypeMirror) method.invoke(f));
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    // Java 8 method
//    @Override
    public boolean isDefault() {
        // must use reflection for it to work on Java 7
        try {
            Method method = ExecutableElement.class.getMethod("isDefault");
            return (Boolean) method.invoke(f);
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

}
