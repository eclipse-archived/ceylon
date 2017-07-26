package com.redhat.ceylon.model.loader;

import com.redhat.ceylon.langtools.classfile.Annotation;
import com.redhat.ceylon.langtools.classfile.ClassFile;
import com.redhat.ceylon.langtools.classfile.ConstantPool;
import com.redhat.ceylon.langtools.classfile.ConstantPoolException;
import com.redhat.ceylon.langtools.classfile.RuntimeAnnotations_attribute;
import com.redhat.ceylon.langtools.classfile.Annotation.Annotation_element_value;
import com.redhat.ceylon.langtools.classfile.Annotation.Array_element_value;
import com.redhat.ceylon.langtools.classfile.Annotation.Class_element_value;
import com.redhat.ceylon.langtools.classfile.Annotation.Enum_element_value;
import com.redhat.ceylon.langtools.classfile.Annotation.Primitive_element_value;
import com.redhat.ceylon.langtools.classfile.Annotation.element_value;
import com.redhat.ceylon.langtools.classfile.Annotation.element_value_pair;
import com.redhat.ceylon.langtools.classfile.Attribute;
import com.redhat.ceylon.langtools.classfile.ConstantPool.CONSTANT_Class_info;
import com.redhat.ceylon.langtools.classfile.ConstantPool.CONSTANT_Double_info;
import com.redhat.ceylon.langtools.classfile.ConstantPool.CONSTANT_Fieldref_info;
import com.redhat.ceylon.langtools.classfile.ConstantPool.CONSTANT_Float_info;
import com.redhat.ceylon.langtools.classfile.ConstantPool.CONSTANT_Integer_info;
import com.redhat.ceylon.langtools.classfile.ConstantPool.CONSTANT_InterfaceMethodref_info;
import com.redhat.ceylon.langtools.classfile.ConstantPool.CONSTANT_InvokeDynamic_info;
import com.redhat.ceylon.langtools.classfile.ConstantPool.CONSTANT_Long_info;
import com.redhat.ceylon.langtools.classfile.ConstantPool.CONSTANT_MethodHandle_info;
import com.redhat.ceylon.langtools.classfile.ConstantPool.CONSTANT_MethodType_info;
import com.redhat.ceylon.langtools.classfile.ConstantPool.CONSTANT_Methodref_info;
import com.redhat.ceylon.langtools.classfile.ConstantPool.CONSTANT_Module_info;
import com.redhat.ceylon.langtools.classfile.ConstantPool.CONSTANT_NameAndType_info;
import com.redhat.ceylon.langtools.classfile.ConstantPool.CONSTANT_Package_info;
import com.redhat.ceylon.langtools.classfile.ConstantPool.CONSTANT_String_info;
import com.redhat.ceylon.langtools.classfile.ConstantPool.CONSTANT_Utf8_info;
import com.redhat.ceylon.langtools.classfile.ConstantPool.CPInfo;
import com.redhat.ceylon.langtools.classfile.ConstantPool.InvalidIndex;
import com.redhat.ceylon.langtools.classfile.ConstantPool.UnexpectedEntry;

public class ClassFileUtil {
    private static final ConstantPool.Visitor<Object, ConstantPool> ConstantPoolConverter = new ConstantPool.Visitor<Object, ConstantPool>(){

        @Override
        public Object visitClass(CONSTANT_Class_info info, ConstantPool p) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Object visitDouble(CONSTANT_Double_info info, ConstantPool p) {
            return info.value;
        }

        @Override
        public Object visitFieldref(CONSTANT_Fieldref_info info, ConstantPool p) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Object visitFloat(CONSTANT_Float_info info, ConstantPool p) {
            return info.value;
        }

        @Override
        public Object visitInteger(CONSTANT_Integer_info info, ConstantPool p) {
            return info.value;
        }

        @Override
        public Object visitInterfaceMethodref(CONSTANT_InterfaceMethodref_info info, ConstantPool p) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Object visitInvokeDynamic(CONSTANT_InvokeDynamic_info info, ConstantPool p) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Object visitLong(CONSTANT_Long_info info, ConstantPool p) {
            return info.value;
        }

        @Override
        public Object visitNameAndType(CONSTANT_NameAndType_info info, ConstantPool p) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Object visitMethodref(CONSTANT_Methodref_info info, ConstantPool p) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Object visitMethodHandle(CONSTANT_MethodHandle_info info, ConstantPool p) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Object visitMethodType(CONSTANT_MethodType_info info, ConstantPool p) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Object visitString(CONSTANT_String_info info, ConstantPool p) {
            try {
                return info.getString();
            } catch (ConstantPoolException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public Object visitUtf8(CONSTANT_Utf8_info info, ConstantPool p) {
            return info.value;
        }

        @Override
        public Object visitModule(CONSTANT_Module_info info, ConstantPool p) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Object visitPackage(CONSTANT_Package_info info, ConstantPool p) {
            // TODO Auto-generated method stub
            return null;
        }
    };
    
    private static final Annotation.element_value.Visitor<Object, ConstantPool> AnnotationConverter = new Annotation.element_value.Visitor<Object, ConstantPool>(){

        @Override
        public Object visitPrimitive(Primitive_element_value ev, ConstantPool p) {
            CPInfo cpInfo;
            try {
                cpInfo = p.get(ev.const_value_index);
                Object ret = cpInfo.accept(ConstantPoolConverter, p);
                // some primitives have no custom types
                switch (ev.tag) {
                case 'B':
                    return ((Integer)ret).byteValue();
                case 'C':
                    return new Character((char)((Integer)ret).intValue());
                case 'S':
                    return ((Integer)ret).shortValue();
                case 'Z':
                    int v = (Integer)ret;
                    return v == 0 ? Boolean.FALSE : Boolean.TRUE;
                }
                return ret;
            } catch (InvalidIndex e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public Object visitEnum(Enum_element_value ev, ConstantPool p) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Object visitClass(Class_element_value ev, ConstantPool p) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Object visitAnnotation(Annotation_element_value ev, ConstantPool p) {
            return ev.annotation_value;
        }

        @Override
        public Object visitArray(Array_element_value ev, ConstantPool p) {
            Object[] ret = new Object[ev.num_values];
            int i = 0;
            for(element_value val : ev.values){
                ret[i++] = val.accept(this, p);
            }
            return ret;
        }
    };

    public static Object getAnnotationValue(ClassFile classFile, Annotation moduleAnnotation, String name) {
        element_value val = null;
        for(element_value_pair pair : moduleAnnotation.element_value_pairs){
            try{
                if(classFile.constant_pool.getUTF8Value(pair.element_name_index).equals(name)){
                    val = pair.value;
                    break;
                }
            } catch (ConstantPoolException e) {
                throw new RuntimeException(e);
            }
        }
        if(val == null)
            return null;
        return val.accept(AnnotationConverter, classFile.constant_pool);
    }

    public static Annotation findAnnotation(ClassFile classFile, RuntimeAnnotations_attribute annotationsAttribute,
            Class<?> annotationClass) {
        return findAnnotation(classFile, annotationsAttribute, annotationClass.getName());
    }
    
    public static Annotation findAnnotation(ClassFile classFile, RuntimeAnnotations_attribute annotationsAttribute,
            String annotationClassName) {
        if(annotationsAttribute == null)
            return null;
        String classNameAsFieldDescriptor = toFieldDescriptor(annotationClassName);
        for(Annotation annot : annotationsAttribute.annotations){
            String typeFieldDescriptor;
            try {
                typeFieldDescriptor = classFile.constant_pool.getUTF8Value(annot.type_index);
            } catch (ConstantPoolException e) {
                throw new RuntimeException(e);
            }
            if(typeFieldDescriptor.equals(classNameAsFieldDescriptor))
                return annot;
        }
        return null;
    }

    private static String toFieldDescriptor(String annotationClassName) {
        return "L"+annotationClassName.replace('.', '/')+";";
    }

    public static Annotation findAnnotation(ClassFile classFile, String moduleAnnotation) {
        RuntimeAnnotations_attribute attribute = (RuntimeAnnotations_attribute) classFile.getAttribute(Attribute.RuntimeVisibleAnnotations);
        return findAnnotation(classFile, attribute, moduleAnnotation);
    }
}
