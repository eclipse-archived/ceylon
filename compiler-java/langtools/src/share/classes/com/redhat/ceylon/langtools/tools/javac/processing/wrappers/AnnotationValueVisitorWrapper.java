package com.redhat.ceylon.langtools.tools.javac.processing.wrappers;

import java.util.List;

import com.redhat.ceylon.javax.lang.model.element.AnnotationMirror;
import com.redhat.ceylon.javax.lang.model.element.AnnotationValue;
import com.redhat.ceylon.javax.lang.model.element.AnnotationValueVisitor;
import com.redhat.ceylon.javax.lang.model.element.VariableElement;
import com.redhat.ceylon.javax.lang.model.type.TypeMirror;

public class AnnotationValueVisitorWrapper<R, P> implements AnnotationValueVisitor<R, P> {

    private javax.lang.model.element.AnnotationValueVisitor<R, P> d;

    public AnnotationValueVisitorWrapper(javax.lang.model.element.AnnotationValueVisitor<R, P> d) {
        this.d = d;
    }

    @Override
    public R visit(AnnotationValue av, P p) {
        return d.visit(Facades.facade(av), p);
    }

    @Override
    public R visit(AnnotationValue av) {
        return d.visit(Facades.facade(av));
    }

    @Override
    public R visitBoolean(boolean b, P p) {
        return d.visitBoolean(b, p);
    }

    @Override
    public R visitByte(byte b, P p) {
        return d.visitByte(b, p);
    }

    @Override
    public R visitChar(char c, P p) {
        return d.visitChar(c, p);
    }

    @Override
    public R visitDouble(double d, P p) {
        return this.d.visitDouble(d, p);
    }

    @Override
    public R visitFloat(float f, P p) {
        return d.visitFloat(f, p);
    }

    @Override
    public R visitInt(int i, P p) {
        return d.visitInt(i, p);
    }

    @Override
    public R visitLong(long i, P p) {
        return d.visitLong(i, p);
    }

    @Override
    public R visitShort(short s, P p) {
        return d.visitShort(s, p);
    }

    @Override
    public R visitString(String s, P p) {
        return d.visitString(s, p);
    }

    @Override
    public R visitType(TypeMirror t, P p) {
        return d.visitType(Facades.facade(t), p);
    }

    @Override
    public R visitEnumConstant(VariableElement c, P p) {
        return d.visitEnumConstant(Facades.facade(c), p);
    }

    @Override
    public R visitAnnotation(AnnotationMirror a, P p) {
        return d.visitAnnotation(Facades.facade(a), p);
    }

    @Override
    public R visitArray(List<? extends AnnotationValue> vals, P p) {
        return d.visitArray(Facades.facadeAnnotationValueList(vals), p);
    }

    @Override
    public R visitUnknown(AnnotationValue av, P p) {
        try{
            return d.visitUnknown(Facades.facade(av), p);
        }catch(javax.lang.model.element.UnknownAnnotationValueException x){
            throw Wrappers.wrap(x);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof AnnotationValueVisitorWrapper == false)
            return false;
        return d.equals(((AnnotationValueVisitorWrapper)obj).d);
    }
    
    @Override
    public int hashCode() {
        return d.hashCode();
    }
    
    @Override
    public String toString() {
        return d.toString();
    }
}
