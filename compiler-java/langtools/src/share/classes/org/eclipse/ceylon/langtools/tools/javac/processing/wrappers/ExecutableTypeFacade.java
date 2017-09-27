package org.eclipse.ceylon.langtools.tools.javac.processing.wrappers;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.eclipse.ceylon.javax.lang.model.type.ExecutableType;
import org.eclipse.ceylon.javax.lang.model.type.TypeMirror;
import org.eclipse.ceylon.javax.lang.model.util.Elements;

public class ExecutableTypeFacade extends TypeMirrorFacade implements javax.lang.model.type.ExecutableType {

    public ExecutableTypeFacade(ExecutableType type) {
        super(type);
    }

    @Override
    public List<? extends javax.lang.model.type.TypeMirror> getParameterTypes() {
        return Facades.facadeTypeMirrorList(((ExecutableType)f).getParameterTypes());
    }

    @Override
    public javax.lang.model.type.TypeMirror getReturnType() {
        return Facades.facade(((ExecutableType)f).getReturnType());
    }

    @Override
    public List<? extends javax.lang.model.type.TypeMirror> getThrownTypes() {
        return Facades.facadeTypeMirrorList(((ExecutableType)f).getThrownTypes());
    }

    @Override
    public List<? extends javax.lang.model.type.TypeVariable> getTypeVariables() {
        return Facades.facadeTypeVariableList(((ExecutableType)f).getTypeVariables());
    }

    // Java 8 method
//    @Override
    public javax.lang.model.type.TypeMirror getReceiverType() {
        // must use reflection for it to work on Java 7
        try {
            Method method = ExecutableType.class.getMethod("getReceiverType");
            return Facades.facade((TypeMirror) method.invoke(f));
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof ExecutableTypeFacade == false)
            return false;
        return f.equals(((ExecutableTypeFacade)obj).f);
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
